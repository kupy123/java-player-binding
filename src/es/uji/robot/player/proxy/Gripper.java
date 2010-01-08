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

import es.uji.robot.player.generated.abstractproxy.AbstractGripper;
import es.uji.robot.player.generated.xdr.PlayerBbox3d;
import es.uji.robot.player.generated.xdr.PlayerGripperData;
import es.uji.robot.player.generated.xdr.PlayerGripperGeom;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.player.generated.xdr.PlayerNull;
import es.uji.robot.player.generated.xdr.PlayerPose3d;
import es.uji.robot.xdr.XDRObject;

/**
*The gripper proxy provides an interface to the gripper
*/
public class Gripper extends AbstractGripper{
	
	/** 
	*Gripper geometry in the robot cs:
	*pose gives the position and orientation,
	*outer_size gives the extent when open
	*inner_size gives the size of the space between the open fingers
	*These values are initially zero, but can be filled in by calling
	*retrieveGeometry(). 
	*/
	private PlayerPose3d pose;
	private PlayerBbox3d outerSize;
	private PlayerBbox3d innerSize;
	/** 
	*The number of breakbeams the gripper has 
	*/
	private char numBeams;
	/** 
	*The capacity of the gripper's store - if 0, the gripper cannot store
	*/
	private char capacity;
	/** 
	*The gripper's state: may be one of PLAYER_GRIPPER_STATE_OPEN,
	*PLAYER_GRIPPER_STATE_CLOSED, PLAYER_GRIPPER_STATE_MOVING
	*or PLAYER_GRIPPER_STATE_ERROR. 
	*/
	private char state;
	/** 
	*The position of the object in the gripper 
	*/
	private int beams;
	/** 
	*The number of currently-stored objects 
	*/
	private char stored;

	public PlayerPose3d getPose() {
		return pose;
	}

	public void setPose(PlayerPose3d pose) {
		this.pose = pose;
	}

	public PlayerBbox3d getOuterSize() {
		return outerSize;
	}

	public void setOuterSize(PlayerBbox3d outerSize) {
		this.outerSize = outerSize;
	}

	public PlayerBbox3d getInnerSize() {
		return innerSize;
	}

	public void setInnerSize(PlayerBbox3d innerSize) {
		this.innerSize = innerSize;
	}

	public char getNumBeams() {
		return numBeams;
	}

	public void setNumBeams(char numBeams) {
		this.numBeams = numBeams;
	}

	public char getCapacity() {
		return capacity;
	}

	public void setCapacity(char capacity) {
		this.capacity = capacity;
	}

	public char getState() {
		return state;
	}

	public void setState(char state) {
		this.state = state;
	}

	public int getBeams() {
		return beams;
	}

	public void setBeams(int beams) {
		this.beams = beams;
	}

	public char getStored() {
		return stored;
	}

	public void setStored(char stored) {
		this.stored = stored;
	}
	
	public Gripper(Client client, int index){
		super.init(client, PLAYER_GRIPPER_CODE, index);
	}
	/** 
	*Command the gripper to open 
	 * @throws ClientException 
	*/
	public void openCommand() throws ClientException{
		PlayerNull command = new PlayerNull();
		
		super.getClient().write(this, PLAYER_GRIPPER_CMD_OPEN, command);
	}
	
	/** 
	* Command the gripper to close 
	 * @throws ClientException 
	*/
	public void closeCommand() throws ClientException{
		PlayerNull command = new PlayerNull();	
		
		super.getClient().write(this, PLAYER_GRIPPER_CMD_CLOSE, command);

	}
	
	/** 
	*Command the gripper to stop 
	 * @throws ClientException 
	*/
	public void stopCommand() throws ClientException{
		PlayerNull command = new PlayerNull();
		
		super.getClient().write(this, PLAYER_GRIPPER_CMD_STOP, command);

	}
	
	/** 
	*Command the gripper to store 
	 * @throws ClientException 
	*/
	public void storeCommand() throws ClientException{
		PlayerNull command = new PlayerNull();	
		
		super.getClient().write(this, PLAYER_GRIPPER_CMD_STORE, command);

	}
	
	/** 
	*Command the gripper to retrieve
	 * @throws ClientException 
	*/
	public void retrieveCommand() throws ClientException{
		PlayerNull command = new PlayerNull();
		
		super.getClient().write(this, PLAYER_GRIPPER_CMD_RETRIEVE, command);

		
	}
	
	/**
	*Print a human-readable version of the gripper state.
	*/
	public void printout(String prefix){
		if(prefix.length() > 0){
			System.out.println(prefix);
		}
		
		String state = null;
		
		if(this.state == PLAYER_GRIPPER_STATE_OPEN){
			state = "open";
		}
		else if(this.state == PLAYER_GRIPPER_STATE_CLOSED){
			state = "closed";
		}
		else if(this.state == PLAYER_GRIPPER_STATE_MOVING){
			state = "moving";
		}
		else{
			state = "error";
		}
		
		System.out.println("[" + super.getDatatime() + "] pose[(" + pose.getPx() + "," + pose.getPy() + "," + pose.getPz() + "),(" + pose.getProll() + "," + pose.getPpitch() + "," + pose.getPyaw() + ")] outerSize[" + outerSize.getSw() + ","  + outerSize.getSl() + "," + outerSize.getSh() + "] innerSize[" + innerSize.getSw() + "," + innerSize.getSl() + "," + innerSize.getSh() + "] state[" + state + "] beams[" + beams);
		
	}
	
	/**
	*Get the gripper geometry.
	 * @throws ClientException 
	*/
	public PlayerGripperGeom retrieveGeometry() throws ClientException{
		PlayerGripperGeom configure = new PlayerGripperGeom();
		
		configure = (PlayerGripperGeom)super.getClient().request(this, PLAYER_GRIPPER_REQ_GET_GEOM, null);
		
		if(configure != null){
			pose.setPx(configure.getPose().getPx());
			pose.setPy(configure.getPose().getPy());
			pose.setPz(configure.getPose().getPz());
			pose.setProll(configure.getPose().getProll());
			pose.setPpitch(configure.getPose().getPpitch());
			pose.setPyaw(configure.getPose().getPyaw());
			
			innerSize.setSh(configure.getInnerSize().getSh());
			innerSize.setSl(configure.getInnerSize().getSl());
			innerSize.setSw(configure.getInnerSize().getSw());
			
			outerSize.setSh(configure.getInnerSize().getSh());
			outerSize.setSl(configure.getInnerSize().getSl());
			outerSize.setSw(configure.getInnerSize().getSw());
			
			//pose = configure.getPose();
			//innerSize = configure.getInnerSize();
			//outerSize = configure.getOuterSize();
			numBeams = configure.getNumBeams();
			capacity = configure.getCapacity();
		}
		
		return configure;
	}

	@Override
	public void putMsg(PlayerMsghdr header, XDRObject data) {
		if( header.getType() == PLAYER_MSGTYPE_DATA && header.getSubtype() ==  PLAYER_GRIPPER_DATA_STATE){
			PlayerGripperData gripper = (PlayerGripperData)data;
			
			state = gripper.getState();
			beams = gripper.getBeams();
			stored = gripper.getStored();
		}
		else{
			System.err.println("Skipping gripper message with unknown type/subtype.");
		}
		
	}
	
	
}
