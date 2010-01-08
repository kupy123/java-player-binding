/**
* Copyright (C) 2009  Leo Nomdedeu, David Olmos
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*
*********************************************************************
*
* Authors: Leo Nomdedeu, David Olmos
* Release: 0.1pre_alfa
* Changelog:
* 		0.1pre_alfa: Initial release
*********************************************************************
*/

package es.uji.robot.player.proxy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import es.uji.robot.player.generated.abstractproxy.AbstractMap;
import es.uji.robot.player.generated.xdr.PlayerMapData;
import es.uji.robot.player.generated.xdr.PlayerMapDataVector;
import es.uji.robot.player.generated.xdr.PlayerMapInfo;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.player.generated.xdr.PlayerSegment;
import es.uji.robot.xdr.XDRObject;

/**
*The map proxy provides an interface to a map.
*/
public class Map extends AbstractMap{
	/** 
	*Map resolution, m/cell 
	*/
	private double resolution;
	/** 
	*Map size, in cells 
	*/
	private int height;
	private int width;
	/** 
	*Map origin, in meters (i.e., the real-world coordinates of cell 0,0)
	*/
	private ArrayList<Double> origin;
	/** 
	*Occupancy for each cell (empty = -1, unknown = 0, occupied = +1) 
	*/
	private ByteArrayOutputStream cells;
	/** 
	*Vector-based version of the map (call retrieveMap() to
	* fill this in). 
	*/
	private double vectorMinX;
	private double vectorMinY;
	private double vectorMaxX;
	private double vectorMaxY;
	
	private ArrayList<PlayerSegment> segments;

	public double getResolution() {
		return resolution;
	}

	public void setResolution(double resolution) {
		this.resolution = resolution;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public ArrayList<Double> getOrigin() {
		return origin;
	}

	public void setOrigin(ArrayList<Double> origin) {
		this.origin = origin;
	}

	public ByteArrayOutputStream getCells() {
		return cells;
	}

	public void setCells(ByteArrayOutputStream cells) {
		this.cells = cells;
	}

	public double getVectorMinX() {
		return vectorMinX;
	}

	public void setVectorMinX(double vectorMinX) {
		this.vectorMinX = vectorMinX;
	}

	public double getVectorMinY() {
		return vectorMinY;
	}

	public void setVectorMinY(double vectorMinY) {
		this.vectorMinY = vectorMinY;
	}

	public double getVectorMaxX() {
		return vectorMaxX;
	}

	public void setVectorMaxX(double vectorMaxX) {
		this.vectorMaxX = vectorMaxX;
	}

	public double getVectorMaxY() {
		return vectorMaxY;
	}

	public void setVectorMaxY(double vectorMaxY) {
		this.vectorMaxY = vectorMaxY;
	}

	public ArrayList<PlayerSegment> getSegments() {
		return segments;
	}

	public void setSegments(ArrayList<PlayerSegment> segments) {
		this.segments = segments;
	}
	
	public Map(Client client, int index){
		super.init(client, PLAYER_MAP_CODE, index);
	}

	/** 
	*@throws ClientException 
	 * @brief Get the map, which is stored in the proxy. 
	*/
	public void retrieveMap() throws ClientException{
		PlayerMapInfo infoRequest = new PlayerMapInfo();
		PlayerMapData dataRequest = new PlayerMapData();
		PlayerMapData dataResponse;
		
		int i,j;
		int oi, oj;
		int sx, sy;
		int si, sj;
		ByteArrayOutputStream cell;
		
		//Get the map info
		infoRequest = (PlayerMapInfo)super.getClient().request(this, PLAYER_MAP_REQ_GET_INFO, null);
		
		if ( infoRequest != null ){
			this.resolution = infoRequest.getScale();
			this.width = infoRequest.getWidth();
			this.height = infoRequest.getHeight();
			this.origin = new ArrayList<Double>();
			this.origin.add(infoRequest.getOrigin().getPx());
			this.origin.add(infoRequest.getOrigin().getPy());
			//this.origin.set(0, infoRequest.getOrigin().getPx());
			//this.origin.set(1, infoRequest.getOrigin().getPy());
		}
		
		//Retrieve the map in tiles
		
		//Tile size
		sy = sx = 640;
		oi = oj = 0;
		
		cells = new ByteArrayOutputStream();
		while( oi < width && oj < height ){
			si = Math.min(sx, width - oi);
			sj = Math.min(sy, height - oj);
			
			dataRequest.setCol(oi);
			dataRequest.setRow(oj);
			dataRequest.setWidth(si);
			dataRequest.setHeight(sj);
			
			dataResponse = (PlayerMapData)super.getClient().request(this, PLAYER_MAP_REQ_GET_DATA, dataRequest);
			
			if( dataResponse != null ){
				try {
					cells.write(dataResponse.getData().toByteArray());
				} catch (IOException e) {
					throw new ClientException(e);
				}
			}
			
		    oi += si;
		    if(oi >= width)
		    {
		      oi = 0;
		      oj += sj;
		    }
			
			
		}
		
	}
	
	/** 
	*@throws ClientException 
	 * @brief Get the vector map, which is stored in the proxy.
	*/
	public void retrieveVector() throws ClientException{
		PlayerMapDataVector vector = new PlayerMapDataVector();
		
		vector = (PlayerMapDataVector)super.getClient().request(this, PLAYER_MAP_REQ_GET_VECTOR, null);
		
		if( vector != null){
			vectorMinX = vector.getMinx();
			vectorMinY = vector.getMiny();
			vectorMaxX = vector.getMaxx();
			vectorMaxY = vector.getMaxy();
			segments = vector.getSegments();
		}
		
	}

	@Override
	public void putMsg(PlayerMsghdr header, XDRObject data) {
		// TODO Auto-generated method stub
		
	}

}