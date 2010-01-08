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

import es.uji.robot.player.generated.abstractproxy.AbstractFiducial;
import es.uji.robot.player.generated.xdr.PlayerFiducialData;
import es.uji.robot.player.generated.xdr.PlayerFiducialGeom;
import es.uji.robot.player.generated.xdr.PlayerFiducialItem;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.xdr.XDRObject;

/**
*The fiducial proxy provides an interface to a fiducial detector.  This
*device looks for fiducials (markers or beacons) in the laser scan, and
*determines their identity, range, bearing and orientation.  See the
*Player User Manual for a complete description of the various drivers
*that support the fiducial interface.
*/
public class Fiducial extends AbstractFiducial{
	
	/** 
	*Geometry in robot cs.  These values are filled in by
	*retrieveGeometry().  [pose] is the detector pose in the
	*robot cs, [size] is the detector size.
	*/
	private PlayerFiducialGeom fiducialGeometry;
	/** 
	List of detected beacons. 
	*/
	private ArrayList<PlayerFiducialItem> fiducials;
	
	public PlayerFiducialGeom getFiducialGeometry() {
		return fiducialGeometry;
	}

	public void setFiducialGeometry(PlayerFiducialGeom fiducialGeometry) {
		this.fiducialGeometry = fiducialGeometry;
	}

	public ArrayList<PlayerFiducialItem> getFiducials() {
		return fiducials;
	}

	public void setFiducials(ArrayList<PlayerFiducialItem> fiducials) {
		this.fiducials = fiducials;
	}

	public Fiducial(Client client, int index){
		super.init(client, PLAYER_FIDUCIAL_CODE, index);
	}

	/** 
	*Get the fiducial geometry.
	 * @throws ClientException 
	*/
	public PlayerFiducialGeom retrieveGeometry() throws ClientException{
		PlayerFiducialGeom configure = new PlayerFiducialGeom();
		
		configure = (PlayerFiducialGeom)super.getClient().request(this, PLAYER_FIDUCIAL_REQ_GET_GEOM, null);
		
		if(configure != null){
			fiducialGeometry = configure;
		}
		
		return configure;
		
	}


	@Override
	public void putMsg(PlayerMsghdr header, XDRObject data) {
		if ( header.getType() == PLAYER_MSGTYPE_DATA && header.getSubtype() ==  PLAYER_FIDUCIAL_DATA_SCAN){
			PlayerFiducialData fiducial = (PlayerFiducialData)data;
			
			fiducials = fiducial.getFiducials();
		}
		else{
			System.err.println("Skipping fiducial message with unknown type/subtype.");
		}
		
	}

}