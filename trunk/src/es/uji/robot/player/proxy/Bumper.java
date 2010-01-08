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
import java.util.ArrayList;
import java.util.List;

import es.uji.robot.player.generated.abstractproxy.AbstractBumper;
import es.uji.robot.player.generated.xdr.PlayerBumperData;
import es.uji.robot.player.generated.xdr.PlayerBumperDefine;
import es.uji.robot.player.generated.xdr.PlayerBumperGeom;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.util.collection.ListHelper;
import es.uji.robot.xdr.XDRObject;

/**
*The bumper proxy provides an interface to the bumper sensors built
*into robots such as the RWI B21R.
*/
public class Bumper extends AbstractBumper{
	
	/** 
	*Pose of each bumper relative to robot (mm, mm, deg, mm, mm).
	*This structure is filled by calling retrieveGeometry().
	*values are x,y (of center) ,normal,length,curvature 
	*/
	private List<PlayerBumperDefine> poses;
	
	/** 
	*Bump data: unsigned char, either boolean or code indicating corner. 
	*/
	private ByteArrayOutputStream bumpers;
	
	public List<PlayerBumperDefine> getPoses() {
		return poses;
	}

	public void setPoses(ArrayList<PlayerBumperDefine> poses) {
		this.poses = poses;
	}

	public ByteArrayOutputStream getBumpers() {
		return bumpers;
	}

	public void setBumpers(ByteArrayOutputStream bumpers) {
		this.bumpers = bumpers;
	}
	
	public Bumper(Client client, int index){
		super.init(client, PLAYER_BUMPER_CODE, index);
	}

	/** 
	*Get the bumper geometry.
	 * @throws ClientException 
	*/
	public PlayerBumperGeom retrieveGeometry() throws ClientException{
		PlayerBumperGeom configure = new PlayerBumperGeom();
		
		configure = (PlayerBumperGeom)super.getClient().request(this, PLAYER_BUMPER_REQ_GET_GEOM, configure);
		
		if(configure != null){
			poses = new ListHelper<PlayerBumperDefine>().copyList(configure.getBumperDef());
		}
		
		return configure;
		
	}
	
	

	public void putMsg(PlayerMsghdr header, XDRObject data) {
		if ( header.getType() == PLAYER_MSGTYPE_DATA && header.getSubtype() == PLAYER_BUMPER_DATA_STATE ){
			PlayerBumperData bumper = (PlayerBumperData)data;
			bumpers = bumper.getBumpers();
		}
		else if ( header.getType() == PLAYER_MSGTYPE_DATA && header.getSubtype() == PLAYER_BUMPER_REQ_GET_GEOM ){
			PlayerBumperGeom geometry = (PlayerBumperGeom)data;
			poses = new ListHelper<PlayerBumperDefine>().copyList(geometry.getBumperDef());
		}
		else{
			System.err.println("Skipping bumper message with unknown type/subtype.");
		}
		
	}
	
	

}