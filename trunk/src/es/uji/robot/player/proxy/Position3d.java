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

import es.uji.robot.player.generated.abstractproxy.AbstractPosition3d;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.player.generated.xdr.PlayerPose3d;
import es.uji.robot.player.generated.xdr.PlayerPosition3dCmdPos;
import es.uji.robot.player.generated.xdr.PlayerPosition3dCmdVel;
import es.uji.robot.player.generated.xdr.PlayerPosition3dData;
import es.uji.robot.player.generated.xdr.PlayerPosition3dGeom;
import es.uji.robot.player.generated.xdr.PlayerPosition3dPowerConfig;
import es.uji.robot.player.generated.xdr.PlayerPosition3dResetOdomConfig;
import es.uji.robot.player.generated.xdr.PlayerPosition3dSetOdomReq;
import es.uji.robot.player.generated.xdr.PlayerPosition3dVelocityModeConfig;
import es.uji.robot.xdr.XDRObject;

/**
*The position3d proxy provides an interface to a mobile robot base,
*such as the Segway RMP series.  The proxy supports both differential
*drive robots (which are capable of forward motion and rotation) and
*omni-drive robots (which are capable of forward, sideways and
*rotational motion).
*/
public class Position3d extends AbstractPosition3d{
	
	/** 
	*Robot geometry in robot cs: pose gives the position3d and
	*orientation, size gives the extent.  These values are filled in
	*by retrieveGeometry(). 
	*/
	private ArrayList<Double> pose;
	private ArrayList<Double> size;
	/** 
	*Device position (m).
	*/
	private double poseX;
	private double poseY;
	private double poseZ;
	/** 
	*Device orientation (radians). 
	*/
	private double poseRoll;
	private double posePitch;
	private double poseYaw;
	/** 
	*Linear velocity (m/s). 
	*/
	private double velocityX;
	private double velocityY;
	private double velocityZ;
	/** 
	*Angular velocity (radians/sec). 
	*/
	private double velocityRoll;
	private double velocityPitch;
	private double velocityYaw;
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

	public double getPoseZ() {
		return poseZ;
	}

	public void setPoseZ(double poseZ) {
		this.poseZ = poseZ;
	}

	public double getPoseRoll() {
		return poseRoll;
	}

	public void setPoseRoll(double poseRoll) {
		this.poseRoll = poseRoll;
	}

	public double getPosePitch() {
		return posePitch;
	}

	public void setPosePitch(double posePitch) {
		this.posePitch = posePitch;
	}

	public double getPoseYaw() {
		return poseYaw;
	}

	public void setPoseYaw(double poseYaw) {
		this.poseYaw = poseYaw;
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

	public double getVelocityZ() {
		return velocityZ;
	}

	public void setVelocityZ(double velocityZ) {
		this.velocityZ = velocityZ;
	}

	public double getVelocityRoll() {
		return velocityRoll;
	}

	public void setVelocityRoll(double velocityRoll) {
		this.velocityRoll = velocityRoll;
	}

	public double getVelocityPitch() {
		return velocityPitch;
	}

	public void setVelocityPitch(double velocityPitch) {
		this.velocityPitch = velocityPitch;
	}

	public double getVelocityYaw() {
		return velocityYaw;
	}

	public void setVelocityYaw(double velocityYaw) {
		this.velocityYaw = velocityYaw;
	}

	public int getStall() {
		return stall;
	}

	public void setStall(int stall) {
		this.stall = stall;
	}
	
	public Position3d(Client client, int index){
		super.init(client, PLAYER_POSITION3D_CODE, index);
	}
	/** 
	*Enable/disable the motors 
	 * @throws ClientException 
	*/
	public PlayerPosition3dPowerConfig enable(int enable) throws ClientException{
		PlayerPosition3dPowerConfig configure = new PlayerPosition3dPowerConfig();
		configure.setState((char)enable);
		
		return (PlayerPosition3dPowerConfig)super.getClient().request(this, PLAYER_POSITION3D_REQ_MOTOR_POWER, configure);
		
	}
	
	/** 
	*Get the position3d geometry.
	 * @throws ClientException 
	*/
	public PlayerPosition3dGeom retrieveGeometry() throws ClientException{
		PlayerPosition3dGeom geometry = new PlayerPosition3dGeom();
		
		geometry = (PlayerPosition3dGeom)super.getClient().request(this, PLAYER_POSITION3D_REQ_GET_GEOM, null);
		
		if(geometry != null){
			pose.add(geometry.getPose().getPx());
			pose.add(geometry.getPose().getPy());
			pose.add(geometry.getPose().getPz());
			
			size.add(geometry.getSize().getSh());
			size.add(geometry.getSize().getSl());
			size.add(geometry.getSize().getSw());
		}
		
		return geometry;
	}
	
	/** 
	*Set the target speed.  velocityX : forward speed (m/s).  velocityY : sideways
	*speed (m/s); velocityZ : vertical speed (m/s). velocityRoll : roll speed (rad/s) .
	*velocityPitch : pitch speed (rad/s) . velocityTheta : theta speed (rad/s).
	*All speeds are defined in the robot coordinate system. 
	 * @throws ClientException 
	*/
	public void sendVelocity(double velocityX, double velocityY, double velocityZ, double velocityRoll, double velocityPitch, double velocityTheta, int state) throws ClientException{
		PlayerPosition3dCmdVel command = new PlayerPosition3dCmdVel();
		PlayerPose3d vel = new PlayerPose3d();
		vel.setPx(velocityX);
		vel.setPy(velocityY);
		vel.setPz(velocityZ);
		vel.setPpitch(velocityPitch);
		vel.setProll(velocityRoll);
		vel.setPyaw(velocityTheta);
		command.setVel(vel);
		command.setState((char)state);
		
		super.getClient().write(this, PLAYER_POSITION3D_CMD_SET_VEL, command);
		
	}
	
	/** 
	*For compatibility with old position3d interface 
	 * @throws ClientException 
	*/
	public void sendSpeed(double velocityX, double velocityY, double velocityZ, int state) throws ClientException{
		sendVelocity(velocityX, velocityY, velocityZ, 0, 0, 0, state);
	}
	
	/** 
	*Set the target pose (goalX, goalY, goalA, goalRoll, goalPithc, goalTheta) is the target pose in the
	*odometric coordinate system. 
	 * @throws ClientException 
	*/
	public void sendPose(double goalX, double goalY, double goalZ, double goalRoll, double goalPitch, double goalTheta) throws ClientException{
		PlayerPosition3dCmdPos command = new PlayerPosition3dCmdPos();
		PlayerPose3d pos = new PlayerPose3d();
		pos.setPx(goalX);
		pos.setPy(goalY);
		pos.setPz(goalZ);
		pos.setProll(goalRoll);
		pos.setPpitch(goalPitch);
		pos.setPyaw(goalTheta);
		
		super.getClient().write(this, PLAYER_POSITION3D_CMD_SET_POS, command);
	}
	
	/** 
	*Set the target pose (position,velocity) define desired position and motion speed 
	 * @throws ClientException 
	*/
	public void sendPositionWithVelocity(PlayerPose3d position, PlayerPose3d velocity) throws ClientException{
		PlayerPosition3dCmdPos command = new PlayerPosition3dCmdPos();
		command.setPos(position);
		command.setVel(velocity);
		
		super.getClient().write(this, PLAYER_POSITION3D_CMD_SET_POS, command);
	}
	
	/** 
	*For compatibility with old position3d interface 
	 * @throws ClientException 
	*/
	public void sendCommandPosition(double goalX, double goalY, double goalZ) throws ClientException{
		sendPose(goalX, goalY, goalZ, 0, 0, 0);
	}
	
	/** 
	*Set the velocity mode. This is driver dependent. 
	 * @throws ClientException 
	*/
	public PlayerPosition3dVelocityModeConfig sendVelocityMode(int mode) throws ClientException{
		PlayerPosition3dVelocityModeConfig configure = new PlayerPosition3dVelocityModeConfig();
		configure.setValue(mode);
		
		return (PlayerPosition3dVelocityModeConfig)super.getClient().request(this, PLAYER_POSITION3D_REQ_VELOCITY_MODE, configure);
		
		
	}
	
	/** 
	*Set the odometry offset 
	 * @throws ClientException 
	*/
	protected PlayerPosition3dSetOdomReq sendOdometry(double odometryX, double odometryY, double odometryZ, double odometryRoll, double odometryPitch, double odometryYaw) throws ClientException{
		PlayerPosition3dSetOdomReq request = new PlayerPosition3dSetOdomReq();
		PlayerPose3d pos = new PlayerPose3d();
		pos.setPx(odometryX);
		pos.setPy(odometryY);
		pos.setPz(odometryZ);
		pos.setProll(odometryRoll);
		pos.setPpitch(odometryPitch);
		pos.setPyaw(odometryYaw);
		
		return (PlayerPosition3dSetOdomReq)super.getClient().request(this, PLAYER_POSITION3D_REQ_SET_ODOM, request);
		
		
	}
	
	/** 
	*Reset the odometry offset 
	 * @throws ClientException 
	*/
	public PlayerPosition3dResetOdomConfig resetOdometry() throws ClientException{
		PlayerPosition3dResetOdomConfig configure = new PlayerPosition3dResetOdomConfig();
		
		return (PlayerPosition3dResetOdomConfig)super.getClient().request(this, PLAYER_POSITION3D_REQ_RESET_ODOM, configure);
		
	}

	@Override
	public void putMsg(PlayerMsghdr header, XDRObject data) {
		if(header.getType() == PLAYER_MSGTYPE_DATA  && header.getSubtype() == PLAYER_POSITION3D_DATA_STATE ){
			PlayerPosition3dData p3 = (PlayerPosition3dData)data;
			poseX = p3.getPos().getPx();
			poseY = p3.getPos().getPy();
			poseZ = p3.getPos().getPz();
			
			poseRoll = p3.getPos().getProll();
			posePitch = p3.getPos().getPpitch();
			poseYaw = p3.getPos().getPyaw();
			
			velocityX = p3.getVel().getPx();
			velocityY = p3.getVel().getPy();
			velocityZ = p3.getVel().getPz();
			
			velocityRoll = p3.getVel().getProll();
			velocityPitch = p3.getVel().getPpitch();
			velocityYaw = p3.getVel().getPyaw();
			
			stall = p3.getStall();
		}
		else{
			System.err.println("Skipping position3d message with unknown type/subtype.");
		}
		
	}
	
	
	
}