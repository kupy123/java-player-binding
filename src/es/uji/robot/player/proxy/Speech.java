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

import es.uji.robot.player.generated.abstractproxy.AbstractSpeech;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.player.generated.xdr.PlayerSpeechCmd;
import es.uji.robot.xdr.XDRObject;

/**
*The speech recognition proxy provides an interface to a speech recognition system.
*/
public class Speech extends AbstractSpeech{
	
	public Speech(Client client, int index){
		super.init(client, PLAYER_SPEECH_CODE, index);
	}

	/** 
	 * Set the output for the speech device. 
	 * @throws ClientException 
	 */
	public void say(String str) throws ClientException{
		PlayerSpeechCmd command = new PlayerSpeechCmd();
		command.setString(str);
		
		super.getClient().write(this, PLAYER_SPEECH_CMD_SAY, command);
		
		
	}

	@Override
	public void putMsg(PlayerMsghdr header, XDRObject data) {
		
	}
	
	


}