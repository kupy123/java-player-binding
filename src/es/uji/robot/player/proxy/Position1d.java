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

import es.uji.robot.player.generated.abstractproxy.AbstractPosition1d;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.player.generated.xdr.PlayerPosition1dCmdPos;
import es.uji.robot.player.generated.xdr.PlayerPosition1dCmdVel;
import es.uji.robot.player.generated.xdr.PlayerPosition1dData;
import es.uji.robot.player.generated.xdr.PlayerPosition1dGeom;
import es.uji.robot.player.generated.xdr.PlayerPosition1dPowerConfig;
import es.uji.robot.player.generated.xdr.PlayerPosition1dSetOdomReq;
import es.uji.robot.xdr.XDRObject;

/**
*The position1d proxy provides an interface to 1 DOF actuator such as a
*linear or rotational actuator.
*/
public class Position1d extends AbstractPosition1d{

	/** 
	*Robot geometry in robot cs: pose gives the position1d and
	*orientation, size gives the extent.  These values are filled in
	*by retrieveGeometry(). 
	*/
	private ArrayList<Double> pose;
	private ArrayList<Double> size;
	/** 
	*Odometric pose [m] or [rad]. 
	*/
	private double pos;
	/** 
	*Odometric velocity [m/s] or [rad/s]. 
	*/
	private double velocity;
	/** 
	*Stall flag [0, 1]. 
	*/
	private int stall;
	/** 
	*Status bitfield of extra data in the following order:
      - status (unsigned byte)
        - bit 0: limit min
        - bit 1: limit center
        - bit 2: limit max
        - bit 3: over current
        - bit 4: trajectory complete
        - bit 5: is enabled
        - bit 6:
        - bit 7:
	*/
	private int status;
	
	public ArrayList<Double> getPose() {
		return pose;
	}

	public void setPose(ArrayList<Double> pose) {
		this.pose = pose;
	}

	public ArrayList<Double> getSize() {
		return size;
	}

	public void setSize(ArrayList<Double> size) {
		this.size = size;
	}

	public double getPos() {
		return pos;
	}

	public void setPos(double pos) {
		this.pos = pos;
	}

	public double getVelocity() {
		return velocity;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	public int getStall() {
		return stall;
	}

	public void setStall(int stall) {
		this.stall = stall;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public Position1d(Client client, int index){
		super.init(client, PLAYER_POSITION1D_CODE, index);
	}

	/** 
	*Enable/disable the motors
	 * @throws ClientException 
	*/
	public PlayerPosition1dPowerConfig enable(int enable) throws ClientException{
		PlayerPosition1dPowerConfig configure = new PlayerPosition1dPowerConfig();
		configure.setState((char)enable);
		
		return (PlayerPosition1dPowerConfig)super.getClient().request(this, PLAYER_POSITION1D_REQ_MOTOR_POWER, configure);
	}
	
	/** 
	*Get the position1d geometry.
	 * @throws ClientException 
	*/
	public PlayerPosition1dGeom retrieveGeometry() throws ClientException{
		PlayerPosition1dGeom geometry = new PlayerPosition1dGeom();
		
		geometry = (PlayerPosition1dGeom)super.getClient().request(this, PLAYER_POSITION1D_REQ_GET_GEOM, null);
		
		if(geometry != null){
			pose.add(geometry.getPose().getPx());
			pose.add(geometry.getPose().getPy());
			pose.add(geometry.getPose().getPyaw());
			
			size.add(geometry.getSize().getSl());
			size.add(geometry.getSize().getSw());
		}
		
		return geometry;
	}
	
	/** 
	*Set the target speed. 
	 * @throws ClientException 
	*/
	public void sendCommandVelocity(double velocity, int state) throws ClientException{
		PlayerPosition1dCmdVel command = new PlayerPosition1dCmdVel();
		command.setVel((float)velocity);
		command.setState((char)state);
		
		super.getClient().write(this, PLAYER_POSITION1D_CMD_VEL, command);
		
		
	}
	
	/** 
	*@throws ClientException 
	 * @brief Set the target position.
    * @param position, target position
    * @param state, target state 
	*/
	public void sendCommandPosition(double position, int state) throws ClientException{
		sendCommandPositionWithVelocity(position,0,state);
	}
	
	/** 
	*@throws ClientException 
	* @brief Set the target position with movement velocity
	* @param position The position to move to
	* @param velocity The speed at which to move to the position
	*/
	public void sendCommandPositionWithVelocity(double position, double velocity, int state) throws ClientException{
		PlayerPosition1dCmdPos command = new PlayerPosition1dCmdPos();
		command.setPos((float)position);
		command.setVel((float)velocity);
		command.setState((char)state);
		
		super.getClient().write(this, PLAYER_POSITION1D_CMD_POS, command);
		
	}
	
	/** 
	*Set the odometry offset 
	 * @throws ClientException 
	*/
	public PlayerPosition1dSetOdomReq sendOdometry(double odometry) throws ClientException{
		PlayerPosition1dSetOdomReq request = new PlayerPosition1dSetOdomReq();
		request.setPos((float)odometry);
		
		return (PlayerPosition1dSetOdomReq)super.getClient().request(this, PLAYER_POSITION1D_REQ_GET_GEOM, request);
		
	}
	
	public void print(String prefix){
		if(prefix.length() > 0){
			System.out.println(prefix);
		}
		
		System.out.println("#time\t\tpos\tvel\tstall\tstatus\n" + super.getDatatime() + "\t" + pos + " \t" + velocity + "\t" + stall + "\t" + status	+ "\n");	
	}

	@Override
	public void putMsg(PlayerMsghdr header, XDRObject data) {
		if( header.getType() == PLAYER_MSGTYPE_DATA && header.getSubtype() ==  PLAYER_POSITION1D_DATA_STATE){
			PlayerPosition1dData p1 = (PlayerPosition1dData)data;
			pos = p1.getPos();
			velocity = p1.getVel();
			stall = p1.getStall();
			status = p1.getStatus();
		}
		else{
			System.err.println("Skipping position1d message with unknown type/subtype.");
		}
		
	}

}