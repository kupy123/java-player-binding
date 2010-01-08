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

import es.uji.robot.player.generated.abstractproxy.AbstractPtz;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.player.generated.xdr.PlayerPtzCmd;
import es.uji.robot.player.generated.xdr.PlayerPtzData;
import es.uji.robot.player.generated.xdr.PlayerPtzReqControlMode;
import es.uji.robot.player.generated.xdr.PlayerPtzReqStatus;
import es.uji.robot.xdr.XDRObject;

/**
*The ptz proxy provides an interface to pan-tilt units such as the Sony
*PTZ camera.
*/
public class Ptz extends AbstractPtz{

	/** 
	*The current ptz pan and tilt angles.  pan : pan angle (+ve to
	*the left, -ve to the right).  tilt : tilt angle (+ve upwrds, -ve
	*downwards). 
	*/
	private double pan;
	private double tilt;
	/** 
	*The current zoom value (field of view angle). 
	*/
	private double zoom;
	/** 
	*The current pan and tilt status 
	*/
	private int status;
	

	public double getPan() {
		return pan;
	}

	public void setPan(double pan) {
		this.pan = pan;
	}

	public double getTilt() {
		return tilt;
	}

	public void setTilt(double tilt) {
		this.tilt = tilt;
	}

	public double getZoom() {
		return zoom;
	}

	public void setZoom(double zoom) {
		this.zoom = zoom;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public Ptz(Client client, int index){
		super.init(client, PLAYER_PTZ_CODE, index);
	}

	/**
	*@brief Set the pan, tilt and zoom values.
	*@param pan Pan value, in radians; 0 = centered.
	*@param tilt Tilt value, in radians; 0 = level.
	*@param zoom Zoom value, in radians (corresponds to camera field of view).
	 * @throws ClientException 
	*/
	public void send(double pan, double tilt, double zoom) throws ClientException{
		PlayerPtzCmd command = new PlayerPtzCmd();
		command.setPan((int)pan);
		command.setTilt((float)tilt);
		command.setZoom((float)zoom);
		command.setPanspeed(0);
		command.setTiltspeed(0);
		
		super.getClient().write(this, PLAYER_PTZ_CMD_STATE, command);
	}
	
	/** 
	*@brief Query the pan and tilt status.
	 * @throws ClientException 
	*/
	public PlayerPtzReqStatus queryStatus() throws ClientException{
		PlayerPtzReqStatus command = new PlayerPtzReqStatus();
		
		command = (PlayerPtzReqStatus)super.getClient().request(this, PLAYER_PTZ_REQ_STATUS, null);
		
		if(command != null){
			status = command.getStatus();
		}
		
		return command;
	}
	
	/** 
	*@brief Set the pan, tilt and zoom values (and speed)
	*@param pan Pan value, in radians; 0 = centered.
	*@param tilt Tilt value, in radians; 0 = level.
	*@param zoom Zoom value, in radians (corresponds to camera field of view).
	*@param panspeed Pan speed, in radians/sec.
	*@param tiltspeed Tilt speed, in radians/sec.
	 * @throws ClientException 
	*/
	public void sendWs(double pan, double tilt, double zoom, double panspeed, double tiltspeed) throws ClientException{
		PlayerPtzCmd command = new PlayerPtzCmd();
		command.setPan((int)pan);
		command.setTilt((float)tilt);
		command.setZoom((float)zoom);
		command.setPanspeed((float)panspeed);
		command.setTiltspeed((float)tiltspeed);
		
		super.getClient().write(this, PLAYER_PTZ_CMD_STATE, command);
	}
	
	/** 
	*@brief Change control mode (select velocity or position control)
	*@param mode Desired mode (@ref PLAYER_PTZ_VELOCITY_CONTROL or @ref PLAYER_PTZ_POSITION_CONTROL)
	 * @throws ClientException 
	*/
	public PlayerPtzReqControlMode sendControlMode(int mode) throws ClientException{
		PlayerPtzReqControlMode configure = new PlayerPtzReqControlMode();
		configure.setMode(mode);
		
		return (PlayerPtzReqControlMode)super.getClient().request(this, PLAYER_PTZ_REQ_CONTROL_MODE, configure);
		
	}

	@Override
	public void putMsg(PlayerMsghdr header, XDRObject data) {
		PlayerPtzData ptz = (PlayerPtzData)data;
		pan = ptz.getPan();
		tilt = ptz.getTilt();
		zoom = ptz.getZoom();
		
	}

}