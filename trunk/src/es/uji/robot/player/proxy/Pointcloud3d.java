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

import es.uji.robot.player.generated.abstractproxy.AbstractPointcloud3d;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.player.generated.xdr.PlayerPointcloud3dData;
import es.uji.robot.player.generated.xdr.PlayerPointcloud3dElement;
import es.uji.robot.xdr.XDRObject;

/**
 * The pointcloud3d proxy provides an interface to a pointcloud3d device.
 *
 */
public class Pointcloud3d extends AbstractPointcloud3d{
	
	/**
	 *  The list of 3D pointcloud elements. 
	 */
	private ArrayList<PlayerPointcloud3dElement> points;
	
	public ArrayList<PlayerPointcloud3dElement> getPoints() {
		return points;
	}
	public void setPoints(ArrayList<PlayerPointcloud3dElement> points) {
		this.points = points;
	}
	

	public Pointcloud3d(Client client, int index){
		super.init(client, PLAYER_POINTCLOUD3D_CODE, index);
	}
	
	@Override
	public void putMsg(PlayerMsghdr header, XDRObject data) {
		if(header.getType() == PLAYER_MSGTYPE_DATA && header.getSubtype() ==  PLAYER_POINTCLOUD3D_DATA_STATE){
			PlayerPointcloud3dData pointCloud = (PlayerPointcloud3dData)data;
			points = pointCloud.getPoints();
		}
		else{
			System.err.println("Skipping pointcloud3d message with unknown type/subtype.");
		}
		
	}
	
	
	
	
}