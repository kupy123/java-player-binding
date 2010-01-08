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

import java.util.ArrayList;

import es.uji.robot.player.generated.abstractproxy.AbstractGraphics2d;
import es.uji.robot.player.generated.xdr.PlayerColor;
import es.uji.robot.player.generated.xdr.PlayerGraphics2dCmdPoints;
import es.uji.robot.player.generated.xdr.PlayerGraphics2dCmdPolygon;
import es.uji.robot.player.generated.xdr.PlayerGraphics2dCmdPolyline;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.player.generated.xdr.PlayerPoint2d;
import es.uji.robot.xdr.XDRObject;

/**
*The graphics2d proxy provides an interface to the graphics2d
*/
public class Graphics2d extends AbstractGraphics2d{
	
	/** 
	*current drawing color 
	*/
	private PlayerColor color;

	public PlayerColor getColor() {
		return color;
	}

	public void setColor(PlayerColor color) {
		this.color = color;
	}
	
	public Graphics2d(Client client, int index){
		//color.setRed((char) 0xff);
		super.init(client, PLAYER_GRAPHICS2D_CODE, index);
	}

	/** 
	*Set the current drawing color 
	*/
	public void sendColor(PlayerColor color){
		this.color = color;
	}
	
	/** 
	*Draw some points 
	 * @throws ClientException 
	*/
	public void drawPoints(ArrayList<PlayerPoint2d> points) throws ClientException{
		PlayerGraphics2dCmdPoints command = new PlayerGraphics2dCmdPoints();
			
		command.setPoints(points);
		command.setColor(color);
		
		super.getClient().write(this, PLAYER_GRAPHICS2D_CMD_POINTS, command);
		
	}
	
	/** 
	*Draw a polyline that connects an array of points 
	 * @throws ClientException 
	*/
	public void drawPolyline(ArrayList<PlayerPoint2d> points) throws ClientException{
		PlayerGraphics2dCmdPolyline command = new PlayerGraphics2dCmdPolyline();
		command.setPoints(points);
		command.setColor(color);
		
		super.getClient().write(this, PLAYER_GRAPHICS2D_CMD_POLYLINE, command);
	}
	
	/** 
	*Draw a polygon 
	 * @throws ClientException 
	*/
	public void drawPolygon(ArrayList<PlayerPoint2d> points, int filled, PlayerColor fillColor) throws ClientException{
		PlayerGraphics2dCmdPolygon command = new PlayerGraphics2dCmdPolygon();
		
		command.setPoints(points);
		command.setFilled((char)filled);
		command.setColor(color);
		
		if(filled == 1){
			command.setFillColor(fillColor);
		}
		
		super.getClient().write(this, PLAYER_GRAPHICS2D_CMD_POLYGON, command);
	}
	
	/** 
	*Clear the canvas 
	 * @throws ClientException 
	*/
	public void clear() throws ClientException{
		super.getClient().write(this, PLAYER_GRAPHICS2D_CMD_CLEAR, null);
	}

	@Override
	public void putMsg(PlayerMsghdr header, XDRObject data) {
		// TODO Auto-generated method stub
		
	}

}