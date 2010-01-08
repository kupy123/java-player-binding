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

import es.uji.robot.player.generated.abstractproxy.AbstractSpeech;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.player.generated.xdr.PlayerSpeechRecognitionData;
import es.uji.robot.xdr.XDRObject;

/**
*The speech recognition proxy provides an interface to a speech recognition system.
*/
public class SpeechRecognition extends AbstractSpeech{
	
	/**SpeechRecognition code*/
	protected static final int PLAYER_SPEECH_RECOGNITION_CODE = 50;
	
	/** Data subtype: recognized string */
	protected static final int PLAYER_SPEECH_RECOGNITION_DATA_STRING = 1;
	
	private String rawText;
	
	public String getRawText() {
		return rawText;
	}
	public String[] getWords() {
		return rawText.split(" ");
	}
	
	public SpeechRecognition(Client client, int index){
		super.init(client, PLAYER_SPEECH_RECOGNITION_CODE, index);
	}
	
	@Override
	public void putMsg(PlayerMsghdr header, XDRObject data) {
		if(header.getType() == PLAYER_MSGTYPE_DATA && header.getSubtype() == PLAYER_SPEECH_RECOGNITION_DATA_STRING ){
			PlayerSpeechRecognitionData speech = (PlayerSpeechRecognitionData)data;
			
			rawText = speech.getText().toString();
		}
		else{
			System.err.println("Skipping speechRecognition message with unknown type/subtype.");
		}
		
	}
	

	
	
}