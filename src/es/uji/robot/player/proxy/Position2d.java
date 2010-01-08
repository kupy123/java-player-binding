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

import es.uji.robot.player.generated.abstractproxy.AbstractPosition2d;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.player.generated.xdr.PlayerPose2d;
import es.uji.robot.player.generated.xdr.PlayerPosition2dCmdCar;
import es.uji.robot.player.generated.xdr.PlayerPosition2dCmdPos;
import es.uji.robot.player.generated.xdr.PlayerPosition2dCmdVel;
import es.uji.robot.player.generated.xdr.PlayerPosition2dData;
import es.uji.robot.player.generated.xdr.PlayerPosition2dGeom;
import es.uji.robot.player.generated.xdr.PlayerPosition2dPositionModeReq;
import es.uji.robot.player.generated.xdr.PlayerPosition2dPowerConfig;
import es.uji.robot.player.generated.xdr.PlayerPosition2dSetOdomReq;
import es.uji.robot.xdr.XDRObject;

/**
*The position2d proxy provides an interface to a mobile robot base,
*such as the ActiveMedia Pioneer series.  The proxy supports both
*differential drive robots (which are capable of forward motion and
*rotation) and omni-drive robots (which are capable of forward,
*sideways and rotational motion).
*/
public class Position2d extends AbstractPosition2d{

	/** 
	*Robot geometry in robot cs: pose gives the position2d and
	*orientation, size gives the extent.  These values are filled in
	*by retrieveGeometry(). 
	*/
	private ArrayList<Double> pose = new ArrayList<Double>();
	private ArrayList<Double> size = new ArrayList<Double>();
	/** 
	*Odometric pose (m, m, rad). 
	*/
	private double poseX;
	private double poseY;
	private double poseA;
	/** 
	*Odometric velocity (m/s, m/s, rad/s). 
	*/
	private double velocityX;
	private double velocityY;
	private double velocityA;
	/** 
	*Stall flag [0, 1]. 
	*/
	private int stall;

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

	public double getPoseX() {
		return poseX;
	}

	public void setPoseX(double poseX) {
		this.poseX = poseX;
	}

	public double getPoseY() {
		return poseY;
	}

	public void setPoseY(double poseY) {
		this.poseY = poseY;
	}

	public double getPoseA() {
		return poseA;
	}

	public void setPoseA(double poseA) {
		this.poseA = poseA;
	}

	public double getVelocityX() {
		return velocityX;
	}

	public void setVelocityX(double velocityX) {
		this.velocityX = velocityX;
	}

	public double getVelocityY() {
		return velocityY;
	}

	public void setVelocityY(double velocityY) {
		this.velocityY = velocityY;
	}

	public double getVelocityA() {
		return velocityA;
	}

	public void setVelocityA(double velocityA) {
		this.velocityA = velocityA;
	}

	public int getStall() {
		return stall;
	}

	public void setStall(int stall) {
		this.stall = stall;
	}
	
	public Position2d(Client client, int index){
		super.init(client, PLAYER_POSITION2D_CODE, index);
	}

	/** 
	*Enable/disable the motors 
	 * @throws ClientException 
	*/
	public PlayerPosition2dPowerConfig enable(int enable) throws ClientException{
		PlayerPosition2dPowerConfig configure = new PlayerPosition2dPowerConfig();
		configure.setState((char)enable);
		
		return (PlayerPosition2dPowerConfig)super.getClient().request(this, PLAYER_POSITION2D_REQ_MOTOR_POWER, configure);
		
	}
	
	public PlayerPosition2dPositionModeReq positionControl(int type) throws ClientException{
		PlayerPosition2dPositionModeReq configure = new PlayerPosition2dPositionModeReq();
		
		configure.setState(type);
		
		return (PlayerPosition2dPositionModeReq)super.getClient().request(this, PLAYER_POSITION2D_REQ_POSITION_MODE, configure);
	}
	
	/** 
	*Get the position2d geometry.
	 * @throws ClientException 
	*/
	public PlayerPosition2dGeom retrieveGeometry() throws ClientException{
		PlayerPosition2dGeom geometry = new PlayerPosition2dGeom();
		
		geometry = (PlayerPosition2dGeom)super.getClient().request(this, PLAYER_POSITION2D_REQ_GET_GEOM, null);
		
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
	*Set the target speed.  velocityX : forward speed (m/s).  velocityY : sideways
	*speed (m/s); this field is used by omni-drive robots only.  velocityA :
	*rotational speed (rad/s).  All speeds are defined in the robot
	*coordinate system. 
	 * @throws ClientException 
	*/
	public void sendCommandVelocity(double velocityX, double velocityY, double velocityA, int state) throws ClientException{
		PlayerPosition2dCmdVel command = new PlayerPosition2dCmdVel();
		PlayerPose2d vel = new PlayerPose2d();
		vel.setPx(velocityX);
		vel.setPy(velocityY);
		vel.setPa(velocityA);
		command.setVel(vel);
		command.setState((char)state);
		
		super.getClient().write(this, PLAYER_POSITION2D_CMD_VEL, command);
		
		
	}
	
	/** 
	*Set the target pose with given motion velocity
	 * @throws ClientException 
	*/
	public void sendCommandPositionWithVelocity(PlayerPose2d position, PlayerPose2d velocity, int state) throws ClientException{
		PlayerPosition2dCmdPos command = new PlayerPosition2dCmdPos();
		command.setPos(position);
		command.setVel(velocity);
		command.setState((char)state);
		
		super.getClient().write(this, PLAYER_POSITION2D_CMD_POS, command);
		
		
	}
	/** 
	*Set the target pose (goalX, goalY, goalA) is the target pose in the
	*odometric coordinate system. 
	 * @throws ClientException 
	*/
	public void sendCommandPosition(double goalX, double goalY, double goalA, int state) throws ClientException{
		PlayerPosition2dCmdPos command = new PlayerPosition2dCmdPos();
		PlayerPose2d pose = new PlayerPose2d();
		pose.setPx(goalX);
		pose.setPy(goalY);
		pose.setPa(goalA);
		command.setPos(pose);
		command.setState((char)state);
		
		super.getClient().write(this, PLAYER_POSITION2D_CMD_POS, command);
	}
	
	/** 
	*Set the target cmd for car like position 
	 * @throws ClientException 
	*/
	public void sendCommandCar(double velocityX, double a) throws ClientException{
		PlayerPosition2dCmdCar command = new PlayerPosition2dCmdCar();
		command.setVelocity(velocityX);
		command.setAngle(a);
		
		super.getClient().write(this, PLAYER_POSITION2D_CMD_CAR, command);
	}
	
	/** 
	*Set the odometry offset 
	 * @throws ClientException 
	*/
	public PlayerPosition2dSetOdomReq sendOdometry(double odometryX, double odometryY, double odometryA) throws ClientException{
		PlayerPosition2dSetOdomReq request = new PlayerPosition2dSetOdomReq();
		PlayerPose2d pose = new PlayerPose2d();
		pose.setPx(odometryX);
		pose.setPy(odometryY);
		pose.setPa(odometryA);
		request.setPose(pose);
		
		return (PlayerPosition2dSetOdomReq)super.getClient().request(this, PLAYER_POSITION2D_REQ_SET_ODOM, request);
	}
	
	public void print(String prefix){
		if(prefix.length() > 0){
			System.out.println(prefix);
		}
		
		System.out.println("#time\t\tpx\tpy\tpa\tvx\tvy\tva\tstall\n" + super.getDatatime() + "\t" + poseX + " \t" + poseY + "\t" + poseA + "\t" + velocityX	+ "\t" + velocityY + "\t" + velocityA + "\t" + stall + "\n");	
	}

	@Override
	public void putMsg(PlayerMsghdr header, XDRObject data) {
		if(header.getType() == PLAYER_MSGTYPE_DATA && header.getSubtype() ==  PLAYER_POSITION2D_DATA_STATE){
			PlayerPosition2dData p2 = (PlayerPosition2dData)data;
			poseX = p2.getPos().getPx();
			poseY = p2.getPos().getPy();
			poseA = p2.getPos().getPa();
			
			velocityX = p2.getVel().getPx();
			velocityY = p2.getVel().getPy();
			velocityA = p2.getVel().getPa();
			
			stall = p2.getStall();
		}
		else{
			System.err.println("Skipping position2d message with unknown type/subtype.");
		}
		
	}

}
