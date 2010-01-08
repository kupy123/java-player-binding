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

import es.uji.robot.player.generated.abstractproxy.AbstractLog;
import es.uji.robot.player.generated.xdr.PlayerLogGetState;
import es.uji.robot.player.generated.xdr.PlayerLogSetFilename;
import es.uji.robot.player.generated.xdr.PlayerLogSetReadRewind;
import es.uji.robot.player.generated.xdr.PlayerLogSetReadState;
import es.uji.robot.player.generated.xdr.PlayerLogSetWriteState;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.xdr.XDRObject;

/**
*The log proxy provides start/stop control of data logging
*/
public class Log extends AbstractLog{
	
	
	/** 
	*What kind of log device is this? Either PLAYER_LOG_TYPE_READ or
	*PLAYER_LOG_TYPE_WRITE. Call retrieveState() to fill it. 
	*/
	private int type;
	/** 
	*Is logging/playback enabled? Call retrieveState() to
	*fill it. 
	*/
	private int state;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
	public Log(Client client, int index){
		super.init(client, PLAYER_LOG_CODE, index);
	}

	/** 
	*@throws ClientException 
	 * @brief Start/stop logging 
	*/
	public PlayerLogSetWriteState sendWriteState(int state) throws ClientException{
		PlayerLogSetWriteState command = new PlayerLogSetWriteState();
		
		command.setState((char)state);
		
		return (PlayerLogSetWriteState)super.getClient().request(this, PLAYER_LOG_REQ_SET_WRITE_STATE, command);	
		
	}
	
	/** 
	*@throws ClientException 
	 * @brief Start/stop playback 
	*/
	public PlayerLogSetReadState sendReadState(int state) throws ClientException{
		PlayerLogSetReadState command = new PlayerLogSetReadState();
		command.setState((char)state);
		return (PlayerLogSetReadState)super.getClient().request(this, PLAYER_LOG_REQ_SET_READ_STATE, command);
		
	}
	
	/** 
	*@throws ClientException 
	 * @brief Rewind playback 
	*/
	public PlayerLogSetReadRewind sendReadRewind() throws ClientException{
		return (PlayerLogSetReadRewind)super.getClient().request(this, PLAYER_LOG_REQ_SET_READ_REWIND, null);
	}
	
	/** 
	*@throws ClientException 
	 * @brief Get logging/playback state.
	*The result is written into the proxy.
	*/
	public PlayerLogGetState retrieveState() throws ClientException{
		PlayerLogGetState request = new PlayerLogGetState();
		
		request = (PlayerLogGetState)super.getClient().request(this, PLAYER_LOG_REQ_GET_STATE, request);
		
		if(request != null){
			type = request.getType();
			state = request.getState();
		}
		
		return request;
	}
	
	/** 
	*@throws ProxyException 
	 * @throws ClientException 
	 * @brief Change name of log file to write to. 
	*/
	public PlayerLogSetFilename sendFilename(ByteArrayOutputStream filename) throws ProxyException, ClientException{
		PlayerLogSetFilename command = new PlayerLogSetFilename();
		
		if(filename.size() > command.getFilename().size()){
			throw new ProxyException("Filename too long.");
		}
		command.setFilename(filename);
		
		return (PlayerLogSetFilename)super.getClient().request(this, PLAYER_LOG_REQ_SET_FILENAME, command);	
	}

	@Override
	public void putMsg(PlayerMsghdr header, XDRObject data) {
		
	}

}