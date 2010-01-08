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

import es.uji.robot.player.generated.abstractproxy.AbstractVectormap;
import es.uji.robot.player.generated.xdr.PlayerExtent2d;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.player.generated.xdr.PlayerVectormapInfo;
import es.uji.robot.player.generated.xdr.PlayerVectormapLayerData;
import es.uji.robot.player.generated.xdr.PlayerVectormapLayerInfo;
import es.uji.robot.xdr.XDRObject;

/**
*The vectormap proxy provides an interface to a map of geometric features.
*/
public class Vectormap extends AbstractVectormap{

	/** 
	*Spatial reference identifier. Use '0' if you are not using spatial references. 
	*/
	private int spatialReferenceId;
	/** 
	*Boundary area. 
	*/
	private PlayerExtent2d extent;
	/** 
	*Layer data. 
	*/
	private ArrayList<PlayerVectormapLayerData> layersData;
	/** 
	*Layer info. 
	*/
	private ArrayList<PlayerVectormapLayerInfo> layersInfo;
	/** 
	*geos geometry returned by get_feature_data 
	*/
	//private GEOSGeom geometry;


	public int getSpatialReferenceId() {
		return spatialReferenceId;
	}

	public void setSpatialReferenceId(int spatialReferenceId) {
		this.spatialReferenceId = spatialReferenceId;
	}

	public PlayerExtent2d getExtent() {
		return extent;
	}

	public void setExtent(PlayerExtent2d extent) {
		this.extent = extent;
	}

	public ArrayList<PlayerVectormapLayerData> getLayersData() {
		return layersData;
	}

	public void setLayersData(
			ArrayList<PlayerVectormapLayerData> layersData) {
		this.layersData = layersData;
	}

	public ArrayList<PlayerVectormapLayerInfo> getLayersInfo() {
		return layersInfo;
	}

	public void setLayersInfo(
			ArrayList<PlayerVectormapLayerInfo> layersInfo) {
		this.layersInfo = layersInfo;
	}

	/*public GEOSGeom getGeometry() {
		return geometry;
	}

	public void setGeometry(GEOSGeom geometry) {
		this.geometry = geometry;
	}*/
	
	public Vectormap(Client client, int index){
		//TODO: throw new ProxyException("GEOSGeom not used");
		//super.init(client, PLAYER_VECTORMAP_CODE, index);
	}

	/** 
	*@throws ClientException 
	 * @brief Get the vectormap metadata, which is stored in the proxy. 
	*/
	public void retrieveMapInfo() throws ClientException{
		PlayerVectormapInfo response = new PlayerVectormapInfo();
		
		response = (PlayerVectormapInfo)super.getClient().request(this, PLAYER_VECTORMAP_REQ_GET_MAP_INFO, null);
		
		if(response != null){
			spatialReferenceId = response.getSrid();
			extent = response.getExtent();
			layersInfo = response.getLayers();
		}
	}
	
	/** 
	*@throws ClientException 
	 * @brief Get the layer data by index. Must only be used after a successfull call to retrieveMapInfo(). 
	*/
	public void retrieveLayerData(int layerIndex) throws ClientException{
		PlayerVectormapLayerData request = new PlayerVectormapLayerData();
		PlayerVectormapLayerData response = new PlayerVectormapLayerData();
		
		request.setName(layersInfo.get(layerIndex).getName());
		
		response = (PlayerVectormapLayerData)super.getClient().request(this, PLAYER_VECTORMAP_REQ_GET_LAYER_DATA, request);
		
		if( response != null ){
			layersData.set(layerIndex, response);		}
		
	}
	
	/**
	*@throws ClientException 
	 * @brief Write layer data. 
	*/
	public void writeLayer(PlayerVectormapLayerData data) throws ClientException{
		super.getClient().request(this, PLAYER_VECTORMAP_REQ_WRITE_LAYER, data);
	}
	
	/** 
	*@brief Clean up the dynamically allocated memory for the vectormap. 
	*/
	public void cleanup(){
		if(layersData != null){
			layersData = null;
		}
		spatialReferenceId = -1;
		
	}
	
	/** 
	*@brief Get an individual feature as a geos geometry. Must only be used after a successful call to playerc_vectormap_get_layer_data.
	*  The geos geometry is owned by the proxy, duplicate it if it is needed after the next call to get_feature_data. Non-reentrant. 
	*/
	/*public GEOSGem retrieveFeatureData(int layerIndex, int featureIndex){
	
	}
	*/
	
	@Override
	public void putMsg(PlayerMsghdr header, XDRObject data) {
		// TODO Auto-generated method stub
		
	}

}