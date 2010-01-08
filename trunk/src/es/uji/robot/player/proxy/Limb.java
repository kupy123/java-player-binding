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

import es.uji.robot.player.generated.abstractproxy.AbstractLimb;
import es.uji.robot.player.generated.xdr.PlayerLimbBrakesReq;
import es.uji.robot.player.generated.xdr.PlayerLimbData;
import es.uji.robot.player.generated.xdr.PlayerLimbGeomReq;
import es.uji.robot.player.generated.xdr.PlayerLimbPowerReq;
import es.uji.robot.player.generated.xdr.PlayerLimbSetposeCmd;
import es.uji.robot.player.generated.xdr.PlayerLimbSetpositionCmd;
import es.uji.robot.player.generated.xdr.PlayerLimbSpeedReq;
import es.uji.robot.player.generated.xdr.PlayerLimbVecmoveCmd;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.player.generated.xdr.PlayerNull;
import es.uji.robot.player.generated.xdr.PlayerPoint3d;
import es.uji.robot.xdr.XDRObject;


/**
*The limb proxy provides an interface to limbs using forward/inverse
*kinematics, such as the ActivMedia Pioneer Arm. See the Player User Manual for a
*complete description of the drivers that support this interface.
*/
public class Limb extends AbstractLimb{

	private PlayerLimbData data;
	private PlayerLimbGeomReq geometry;

	public PlayerLimbData getData() {
		return data;
	}

	public void setData(PlayerLimbData data) {
		this.data = data;
	}

	public PlayerLimbGeomReq getGeometry() {
		return geometry;
	}

	public void setGeometry(PlayerLimbGeomReq geometry) {
		this.geometry = geometry;
	}
	
	public Limb(Client client, int index){
		super.init(client, PLAYER_LIMB_CODE, index);
	}

	/** 
	*Get the limb geometry.
	* @returns PlayerLimbGeomReq request
	* @throws ClientException 
	*/
	public PlayerLimbGeomReq retrieveGeometry() throws ClientException{ 
		PlayerLimbGeomReq geometry = new PlayerLimbGeomReq();
		
		geometry = (PlayerLimbGeomReq)super.getClient().request(this, PLAYER_LIMB_REQ_GEOM, null);
		
		if(geometry != null){
			this.geometry = geometry;
		}
		
		return geometry;
			
	}
	
	/** 
	*Command the end effector to move home. 
	 * @throws ClientException 
	*/
	public void homeCommand() throws ClientException{
		PlayerNull command = new PlayerNull();
		
		super.getClient().write(this, PLAYER_LIMB_CMD_HOME, command);
	}
	
	/**
	*Command the end effector to stop immediatly. 
	 * @throws ClientException 
	*/
	public void stopCommand() throws ClientException{
		PlayerNull command = new PlayerNull();
		
		super.getClient().write(this, PLAYER_LIMB_CMD_STOP, command);
	}
	
	/** 
	*Command the end effector to move to a specified pose. 
	 * @throws ClientException 
	*/
	public void sendPoseCommand(float pX, float pY, float pZ, float aX, float aY, float aZ, float oX, float oY, float oZ) throws ClientException{
		PlayerLimbSetposeCmd command = new PlayerLimbSetposeCmd();
		PlayerPoint3d position = new PlayerPoint3d();
		PlayerPoint3d approach = new PlayerPoint3d();
		PlayerPoint3d orientation = new PlayerPoint3d();
		
		position.setPx(pX);
		position.setPy(pY);
		position.setPz(pZ);
		command.setPosition(position);
		approach.setPx(aX);
		approach.setPy(aY);
		approach.setPz(aZ);
		command.setApproach(approach);
		orientation.setPx(oX);
		orientation.setPy(oY);
		orientation.setPz(oZ);
		command.setOrientation(orientation);
		
		super.getClient().write(this, PLAYER_LIMB_CMD_SETPOSE, command);
	}
	
	/** 
	*Command the end effector to move to a specified position
	*(ignoring approach and orientation vectors). 
	 * @throws ClientException 
	*/
	public void sendPositionCommand(float pX, float pY, float pZ) throws ClientException{
		PlayerLimbSetpositionCmd command = new PlayerLimbSetpositionCmd();
		PlayerPoint3d position = new PlayerPoint3d();
		
		position.setPx(pX);
		position.setPy(pY);
		position.setPz(pZ);
		command.setPosition(position);
		
		super.getClient().write(this, PLAYER_LIMB_CMD_SETPOSITION, command);
	}
	
	/** 
	*Command the end effector to move along the provided vector from
	*its current position for the provided distance. 
	 * @throws ClientException 
	*/
	public void vectorMoveCommand(float X, float Y, float Z, float length) throws ClientException{
		PlayerLimbVecmoveCmd command = new PlayerLimbVecmoveCmd();
		PlayerPoint3d direction = new PlayerPoint3d();
		
		direction.setPx(X);
		direction.setPy(Y);
		direction.setPz(Z);
		command.setDirection(direction);
		command.setLength(length);
		
		super.getClient().write(this, PLAYER_LIMB_CMD_VECMOVE, command);
		
	}
	
	/** 
	*Turn the power to the limb on or off. Be careful
	*when turning power on that the limb is not obstructed from its home
	*position in case it moves to it (common behaviour). 
	 * @throws ClientException 
	*/
	public PlayerLimbPowerReq power(int enable) throws ClientException{
		PlayerLimbPowerReq configure = new PlayerLimbPowerReq();
		configure.setValue((char)enable);
		
		return (PlayerLimbPowerReq)super.getClient().request(this, PLAYER_LIMB_REQ_POWER, configure);
	}
	
	/**
	*Turn the brakes of all actuators in the limb that have them on or off. 
	 * @throws ClientException 
	*/
	public PlayerLimbBrakesReq brakes(int enable) throws ClientException{
		PlayerLimbBrakesReq configure = new PlayerLimbBrakesReq();
		configure.setValue((char)enable);
		
		return (PlayerLimbBrakesReq)super.getClient().request(this, PLAYER_LIMB_REQ_BRAKES, configure);
		
	}
	
	/** 
	*Set the speed of the end effector (m/s) for all subsequent movement commands. 
	 * @throws ClientException 
	*/
	public PlayerLimbSpeedReq speedConfiguration(float speed) throws ClientException{
		PlayerLimbSpeedReq configure = new PlayerLimbSpeedReq();
		configure.setSpeed(speed);
		
		return (PlayerLimbSpeedReq)super.getClient().request(this, PLAYER_LIMB_REQ_SPEED, configure);
		
	}

	@Override
	public void putMsg(PlayerMsghdr header, XDRObject data) {
		if (header.getType() == PLAYER_MSGTYPE_DATA && header.getSubtype() ==  PLAYER_LIMB_DATA_STATE){
			PlayerLimbData limb = (PlayerLimbData)data;
			this.data.setPosition(limb.getPosition());
			this.data.setOrientation(limb.getOrientation());
			this.data.setApproach(limb.getApproach());
			this.data.setState(limb.getState());
		}
		else{
			System.err.println("Skipping limb message with unknown type/subtype.");
		}
		
	}

}
