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

import es.uji.robot.player.generated.abstractproxy.AbstractSonar;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.player.generated.xdr.PlayerPose3d;
import es.uji.robot.player.generated.xdr.PlayerSonarData;
import es.uji.robot.player.generated.xdr.PlayerSonarGeom;
import es.uji.robot.util.collection.ListHelper;
import es.uji.robot.xdr.XDRObject;

/**
*The sonar proxy provides an interface to the sonar range sensors built
*into robots such as the ActiveMedia Pioneer series.
*/
public class Sonar extends AbstractSonar{
	
	/** 
	*Pose of each sonar relative to robot (m, m, radians).  This
	*structure is filled by calling retrieveGeometry(). 
	*/
	private ArrayList<PlayerPose3d> poses;
	/** 
	*Scan data: range (m). 
	*/
	private ArrayList<Double> scan;

	public ArrayList<PlayerPose3d> getPoses() {
		return poses;
	}

	public void setPoses(ArrayList<PlayerPose3d> poses) {
		this.poses = poses;
	}

	public ArrayList<Double> getScan() {
		return scan;
	}

	public void setScan(ArrayList<Double> scan) {
		this.scan = scan;
	}
	
	public Sonar(Client client, int index){
		super.init(client, PLAYER_SONAR_CODE, index);
	}

	/** 
	*@throws ClientException 
	 * @brief Get the sonar geometry.
	*/
	public PlayerSonarGeom retrieveGeometry() throws ClientException{
		PlayerSonarGeom geometry = new PlayerSonarGeom();
		
		geometry = (PlayerSonarGeom)super.getClient().request(this, PLAYER_SONAR_REQ_GET_GEOM, null);
		
		if(geometry != null){
			//XXX: Chapuza grande! Solucionar como toca!
			ListHelper lh = new ListHelper<PlayerPose3d>();
			poses = (ArrayList<PlayerPose3d>)lh.copyList(geometry.getPoses());
		}
		
		return geometry;
	}



	@Override
	public void putMsg(PlayerMsghdr header, XDRObject data) {
		if(header.getType() == PLAYER_MSGTYPE_DATA && header.getSubtype() ==  PLAYER_SONAR_DATA_RANGES){
			PlayerSonarData sonar = (PlayerSonarData)data;
			for(int i = 0; i < sonar.getRanges().size(); i++){
				scan.set(i, (double)sonar.getRanges().get(i));
			}
		}
		else{
			System.err.println("Skipping sonar message with unknown type/subtype.");
		}
		
	}
}