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

import es.uji.robot.player.generated.abstractproxy.AbstractAio;
import es.uji.robot.player.generated.xdr.PlayerAioCmd;
import es.uji.robot.player.generated.xdr.PlayerAioData;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.xdr.XDRObject;

/**
*The aio proxy provides an interface to the analog input/output sensors.
*/

public class Aio extends AbstractAio{
	
	/**
	*A bitfield of the current digital inputs.
	*/
	private ArrayList<Float> voltages;

	public ArrayList<Float> getVoltages() {
		return voltages;
	}

	public void setVoltages(ArrayList<Float> voltages) {
		this.voltages = voltages;
	}
	
	public Aio(Client client, int index){
		super.init(client, PLAYER_AIO_CODE, index);
	}

	/**
	*Send the output for the aio device
	 * @throws ClientException 
	*/
	public void sendOutput(int id, float volt) throws ClientException{
		PlayerAioCmd command  = new PlayerAioCmd();
		
		command.setId(id);
		command.setVoltage(volt);
		
		super.getClient().write(this, PLAYER_AIO_CMD_STATE, command);
	}
	
	/**
	*Retrieve the aio data 
	 * @throws ProxyException 
	*/
	public float getData(int index) throws ProxyException{
		if(index < voltages.size()){
			return (float)voltages.get(index);
		}
		else{
			throw new ProxyException("Array out of bounds.");
		}
	}

	public void putMsg(PlayerMsghdr header, XDRObject data) {
		if( header.getType() == PLAYER_MSGTYPE_DATA && header.getSubtype() == PLAYER_AIO_DATA_STATE){
			voltages = ((PlayerAioData)data).getVoltages();
		}
		else{
			System.err.println("Skipping aio message with unknown type/subtype.");
		}
		
	}

}