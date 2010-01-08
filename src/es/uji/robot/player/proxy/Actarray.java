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

import es.uji.robot.player.generated.abstractproxy.AbstractActarray;
import es.uji.robot.player.generated.value.PlayerActarrayAccelConfigValue;
import es.uji.robot.player.generated.value.PlayerActarrayActuatorValue;
import es.uji.robot.player.generated.value.PlayerActarrayActuatorgeomValue;
import es.uji.robot.player.generated.value.PlayerOrientation3dValue;
import es.uji.robot.player.generated.value.PlayerPoint3dValue;
import es.uji.robot.player.generated.xdr.PlayerActarrayAccelConfig;
import es.uji.robot.player.generated.xdr.PlayerActarrayActuator;
import es.uji.robot.player.generated.xdr.PlayerActarrayActuatorgeom;
import es.uji.robot.player.generated.xdr.PlayerActarrayBrakesConfig;
import es.uji.robot.player.generated.xdr.PlayerActarrayCurrentCmd;
import es.uji.robot.player.generated.xdr.PlayerActarrayData;
import es.uji.robot.player.generated.xdr.PlayerActarrayGeom;
import es.uji.robot.player.generated.xdr.PlayerActarrayHomeCmd;
import es.uji.robot.player.generated.xdr.PlayerActarrayMultiCurrentCmd;
import es.uji.robot.player.generated.xdr.PlayerActarrayMultiPositionCmd;
import es.uji.robot.player.generated.xdr.PlayerActarrayMultiSpeedCmd;
import es.uji.robot.player.generated.xdr.PlayerActarrayPositionCmd;
import es.uji.robot.player.generated.xdr.PlayerActarrayPowerConfig;
import es.uji.robot.player.generated.xdr.PlayerActarraySpeedCmd;
import es.uji.robot.player.generated.xdr.PlayerActarraySpeedConfig;
import es.uji.robot.player.generated.xdr.PlayerBumperDefine;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.player.generated.xdr.PlayerOrientation3d;
import es.uji.robot.player.generated.xdr.PlayerPoint3d;
import es.uji.robot.util.collection.ListHelper;
import es.uji.robot.xdr.XDRObject;

/**
*The actarray proxy provides an interface to actuator arrays
*such as the ActivMedia Pioneer Arm. See the Player User Manual for a
*complete description of the drivers that support this interface.
*/

public class Actarray extends AbstractActarray{
		
	/** 
	*The actuator data, geometry and motor state.
	*/
	private ArrayList<PlayerActarrayActuator> actuatorsData;
	/**
	*Actuator we have geometry for
	*/
	private ArrayList<PlayerActarrayActuatorgeom> actuatorsGeometry;
	/** 
	*Reports if the actuators are off (0) or on (1) 
	*/
	private int motorState;
	/** 
	*The position of the base of the actarray. 
	*/
	private PlayerPoint3d basePos;
	/** 
	*The orientation of the base of the actarray. 
	*/
	private PlayerOrientation3d baseOrientation;
	

	public ArrayList<PlayerActarrayActuator> getActuatorsData() {
		return actuatorsData;
	}

	public void setActuatorsData(ArrayList<PlayerActarrayActuator> actuatorsData) {
		this.actuatorsData = actuatorsData;
	}

	public ArrayList<PlayerActarrayActuatorgeom> getActuatorsGeometry() {
		return actuatorsGeometry;
	}

	public void setActuatorsGeometry(
			ArrayList<PlayerActarrayActuatorgeom> actuatorsGeometry) {
		this.actuatorsGeometry = actuatorsGeometry;
	}

	public int getMotorState() {
		return motorState;
	}

	public void setMotorState(int motorState) {
		this.motorState = motorState;
	}

	public PlayerPoint3d getBasePos() {
		return (PlayerPoint3d) basePos;
	}

	public void setBasePos(PlayerPoint3d basePos) {
		this.basePos = basePos;
	}

	public PlayerOrientation3d getBaseOrientation() {
		return (PlayerOrientation3d) baseOrientation;
	}

	public void setBaseOrientation(PlayerOrientation3d baseOrientation) {
		this.baseOrientation = baseOrientation;
	}
	
	public Actarray(Client client, int index){
		super.init(client, PLAYER_ACTARRAY_CODE, index);
	}

	/**
	*Accessor method for the actuator data 
	*/
	public PlayerActarrayActuator getActuatorData(int index) throws ProxyException{
		//assert(index < actuatorsData.size());
		if(index < actuatorsData.size()){
			return (PlayerActarrayActuator) actuatorsData.get(index);
		}
		else{
			throw new ProxyException("Array out of bounds.");
		}
	}
	
	/** 
	*Accessor method for the actuator geom 
	*/
	public PlayerActarrayActuatorgeom getActuatorGeometry(int index) throws ProxyException{
		if(index < actuatorsGeometry.size()){
			return (PlayerActarrayActuatorgeom) actuatorsGeometry.get(index);
		}
		else{
			throw new ProxyException("Array out of bounds.");
		}
		
	}
	
	/** 
	* Get the actarray geometry. 
	 * @throws ClientException 
	*/
	public PlayerActarrayGeom retrieveGeom() throws ClientException{
		PlayerActarrayGeom geometry = new PlayerActarrayGeom();
		geometry = (PlayerActarrayGeom)super.getClient().request(this,PLAYER_ACTARRAY_REQ_GET_GEOM,null);
		
		if(geometry != null){
			actuatorsGeometry = new ListHelper<PlayerActarrayActuatorgeom>().copyList(geometry.getActuators());
			//actuatorsGeometry = geometry.getActuators();
			basePos = geometry.getBasePos();
			baseOrientation = geometry.getBaseOrientation();
		}
		
		
		return geometry;
	}
	
	/**
	*Command a joint in the array to move to a specified position. 
	 * @throws ClientException 
	*/
	public void positionCommand(int joint, float position) throws ClientException{
		PlayerActarrayPositionCmd command = new PlayerActarrayPositionCmd();
		
		command.setJoint(joint);
		command.setPosition(position);
		
		super.getClient().write(this,PLAYER_ACTARRAY_CMD_POS,command);
		
	}
	
	/**
	*Command all joints in the array to move to specified positions. 
	 * @throws ClientException 
	*/
	public void multiPositionCommand(ArrayList<Float> positions) throws ClientException{
		PlayerActarrayMultiPositionCmd command = new PlayerActarrayMultiPositionCmd();
		
		command.setPositions(positions);
		
		super.getClient().write(this, PLAYER_ACTARRAY_CMD_MULTI_POS, command);
	}
	
	/** 
	*Command a joint in the array to move at a specified speed. 
	 * @throws ClientException 
	*/
	public void speedCommand(int joint, float speed) throws ClientException{
		PlayerActarraySpeedCmd command = new PlayerActarraySpeedCmd();
		
		command.setJoint(joint);
		command.setSpeed(speed);
		
		super.getClient().write(this, PLAYER_ACTARRAY_CMD_SPEED, command);
	}
	
	/**
	*Command a joint in the array to move at a specified speed.
	 * @throws ClientException 
	*/
	public void multiSpeedCommand(ArrayList<Float> speeds) throws ClientException{
		PlayerActarrayMultiSpeedCmd command = new PlayerActarrayMultiSpeedCmd();
		
		command.setSpeeds(speeds);
		
		super.getClient().write(this, PLAYER_ACTARRAY_CMD_MULTI_SPEED, command);
	}
	
	/**
	*Command a joint to go to its home position. 
	 * @throws ClientException 
	*/
	public void homeCommand(int joint) throws ClientException{
		PlayerActarrayHomeCmd command = new PlayerActarrayHomeCmd();
		
		command.setJoint(joint);
		
		super.getClient().write(this, PLAYER_ACTARRAY_CMD_HOME, command);
	}
	
	/**
	*Command a joint in the array to move with a specified current. 
	 * @throws ClientException 
	*/
	public void currentCommand(int joint, float current) throws ClientException{
		PlayerActarrayCurrentCmd command = new PlayerActarrayCurrentCmd();
		
		command.setJoint(joint);
		command.setCurrent(current);
		
		super.getClient().write(this, PLAYER_ACTARRAY_CMD_CURRENT, command);
	
	}
	
	/**
	*Command all joints in the array to move with specified currents. 
	 * @throws ClientException 
	*/
	public void multiCurrentCommand(ArrayList<Float> currents) throws ClientException{
		PlayerActarrayMultiCurrentCmd command = new PlayerActarrayMultiCurrentCmd();
		
		command.setCurrents(currents);
		
		super.getClient().write(this, PLAYER_ACTARRAY_CMD_MULTI_CURRENT, command);
		
	}
	
	/** 
	*Turn the power to the array on or off. Be careful
	*when turning power on that the array is not obstructed from its home
	*position in case it moves to it (common behavior). 
	 * @throws ClientException 
	*/
	public PlayerActarrayPowerConfig power(int enable) throws ClientException{
		PlayerActarrayPowerConfig configure = new PlayerActarrayPowerConfig();
		
		configure.setValue((char)enable);
		
		return (PlayerActarrayPowerConfig)super.getClient().request(this, PLAYER_ACTARRAY_REQ_POWER,configure);
	
	}
	
	/**
	Turn the brakes of all actuators in the array that have them on or off. 
	 * @throws ClientException 
	*/
	public PlayerActarrayBrakesConfig brakes(int enable) throws ClientException{
		PlayerActarrayBrakesConfig configure = new PlayerActarrayBrakesConfig();
		
		configure.setValue((char)enable);
		
		return (PlayerActarrayBrakesConfig)super.getClient().request(this, PLAYER_ACTARRAY_REQ_BRAKES,configure);
		
	}
	
	/**
	Set the speed of a joint for all subsequent movement commands. 
	 * @throws ClientException 
	*/
	public PlayerActarraySpeedConfig speedConfiguration(int joint, float speed) throws ClientException{
		PlayerActarraySpeedConfig configure = new PlayerActarraySpeedConfig();
		
		configure.setJoint(joint);
		configure.setSpeed(speed);
		
		return (PlayerActarraySpeedConfig)super.getClient().request(this, PLAYER_ACTARRAY_REQ_SPEED,configure);
	}
	
	/**
	*Set the acceleration of a joint for all subsequent movement commands
	 * @throws ClientException 
	*/
	public PlayerActarrayAccelConfigValue accelerationConfiguration(int joint, float acceleration) throws ClientException{
		PlayerActarrayAccelConfig configure = new PlayerActarrayAccelConfig();
		
		configure.setJoint(joint);
		configure.setAccel(acceleration);
		
		return (PlayerActarrayAccelConfig)super.getClient().request(this, PLAYER_ACTARRAY_REQ_ACCEL, configure);
		
	
	}

	public void putMsg(PlayerMsghdr header, XDRObject data) {
		if( header.getType() == PLAYER_MSGTYPE_DATA && header.getSubtype() == PLAYER_ACTARRAY_DATA_STATE ){
			actuatorsData = ((PlayerActarrayData)data).getActuators();
			motorState = ((PlayerActarrayData)data).getMotorState();
		}
		else{
			System.err.println("Skipping actarray message with unknown type/subtype.");
		}
		
	}


}