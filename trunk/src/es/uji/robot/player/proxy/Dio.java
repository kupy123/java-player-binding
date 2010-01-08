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

import es.uji.robot.player.generated.abstractproxy.AbstractDio;
import es.uji.robot.player.generated.xdr.PlayerDioData;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.xdr.XDRObject;

/**
*The dio proxy provides an interface to the digital input/output sensors.
*/
public class Dio extends AbstractDio{
	
	/**
	*The number of valid digital inputs.
	*/
	private int count;
	/**
	*A bitfield of the current digital inputs.
	*/
	private int digitalInputs;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getDigitalInputs() {
		return digitalInputs;
	}

	public void setDigitalInputs(int digitalInputs) {
		this.digitalInputs = digitalInputs;
	}

	public Dio(Client client, int index){
		super.init(client, PLAYER_DIO_CODE, index);
	}

	/** 
	*Set the output for the dio device. 
	 * @throws ClientException 
	*/
	public void sendOutput(char outputCount, int bits) throws ClientException{
		PlayerDioData command = new PlayerDioData();
		
		command.setCount(outputCount);
		command.setBits(bits);
		
		super.getClient().write(this, PLAYER_DIO_CMD_VALUES, command);
	}
	
	public void putMsg(PlayerMsghdr header, XDRObject data) {
		if ( header.getType() == PLAYER_MSGTYPE_DATA && header.getSubtype() == PLAYER_DIO_DATA_VALUES ){
			PlayerDioData dio = (PlayerDioData)data;
			
			count = dio.getCount();
			digitalInputs = dio.getBits();
		}
		else{
			System.err.println("Skipping dio message with unknown type/subtype.");
		}
		
	}

}

