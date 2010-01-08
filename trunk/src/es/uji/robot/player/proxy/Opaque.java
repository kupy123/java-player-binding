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

import es.uji.robot.player.generated.abstractproxy.AbstractOpaque;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.player.generated.xdr.PlayerOpaqueData;
import es.uji.robot.xdr.XDRObject;

/**
*The opaque proxy provides an interface for generic messages to drivers.
*See examples/plugins/opaquedriver for an example of using this interface in
*combination with a custom plugin.
*/
public class Opaque extends AbstractOpaque{
	
	/** for backwards compatibility */
	protected static final int PLAYER_OPAQUE_REQ = PLAYER_OPAQUE_REQ_DATA;
	protected static final int PLAYER_OPAQUE_CMD = PLAYER_OPAQUE_CMD_DATA;
	
	/** 
	*Data
	*/
	private ByteArrayOutputStream data;

	public ByteArrayOutputStream getData() {
		return data;
	}

	public void setData(ByteArrayOutputStream data) {
		this.data = data;
	}
	
	public Opaque(Client client, int index){
		super.init(client, PLAYER_OPAQUE_CODE, index);
	}

	/** 
	*@throws ClientException 
	 * @brief Send a generic command 
	*/
	public void Command(PlayerOpaqueData data) throws ClientException{
		super.getClient().write(this, PLAYER_OPAQUE_CMD, data);
	}
	
	/** 
	*@throws ClientException 
	 * @brief Send a generic request
	*/
	public PlayerOpaqueData request(PlayerOpaqueData request) throws ClientException{
		return (PlayerOpaqueData)super.getClient().request(this, PLAYER_OPAQUE_REQ, request);
	}

	@Override
	public void putMsg(PlayerMsghdr header, XDRObject data) {
		if( header.getType() == PLAYER_MSGTYPE_DATA && header.getSubtype() == PLAYER_OPAQUE_DATA_STATE ){
			PlayerOpaqueData opaque = (PlayerOpaqueData)data;
			this.data = opaque.getData();
		}
		else{
			System.err.println("Skipping opaque message with unknown type/subtype.");
		}
		
	}
	
}