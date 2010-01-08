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

import es.uji.robot.player.generated.abstractproxy.AbstractIr;
import es.uji.robot.player.generated.xdr.PlayerIrData;
import es.uji.robot.player.generated.xdr.PlayerIrPose;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.xdr.XDRObject;

/**
*The ir proxy provides an interface to the ir sensors built into robots
*such as the RWI B21R.
*/
public class Ir extends AbstractIr{
	
	/**
	*data
	*/
	private PlayerIrData data;
	/**
	*config
	*/
	private PlayerIrPose poses;
	
	public PlayerIrData getData() {
		return data;
	}
	
	public void setData(PlayerIrData data) {
		this.data = data;
	}

	public PlayerIrPose getPoses() {
		return poses;
	}

	public void setPoses(PlayerIrPose poses) {
		this.poses = poses;
	}
	
	public Ir(Client client, int index){
		super.init(client, PLAYER_IR_CODE, index);
	}

	/** 
	*Get the ir geometry.
	 * @throws ClientException 
	*/
	public PlayerIrPose retrieveGeometry() throws ClientException{
		PlayerIrPose response = new PlayerIrPose();
		
		response = (PlayerIrPose)super.getClient().request(this, PLAYER_IR_REQ_POSE, null);
		
		if(response != null){
			poses = response;
		}
		
		return response;
	
	}


	@Override
	public void putMsg(PlayerMsghdr header, XDRObject data) {
		if ( header.getType() == PLAYER_MSGTYPE_DATA && header.getSubtype() == PLAYER_IR_DATA_RANGES ){
			PlayerIrData ir = (PlayerIrData)data;
			this.data = ir;
		}
		else{
			System.err.println("Skipping ir message with unknown type/subtype.");
		}
		
	}
	
}