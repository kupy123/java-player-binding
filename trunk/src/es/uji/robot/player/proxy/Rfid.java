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

import es.uji.robot.player.generated.abstractproxy.AbstractRfid;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.player.generated.xdr.PlayerRfidData;
import es.uji.robot.player.generated.xdr.PlayerRfidTag;
import es.uji.robot.xdr.XDRObject;

/**
 * The rfid proxy provides an interface to a RFID reader.
 */
public class Rfid extends AbstractRfid{
	
	/**
	 * The list of RFID tags. 
	 */
	private ArrayList<PlayerRfidTag> tags;

	public ArrayList<PlayerRfidTag> getTags() {
		return tags;
	}

	public void setTags(ArrayList<PlayerRfidTag> tags) {
		this.tags = tags;
	}
	
	public Rfid(Client client, int index){
		super.init(client, PLAYER_RFID_CODE, index);
	}

	@Override
	public void putMsg(PlayerMsghdr header, XDRObject data) {
		if( header.getType() == PLAYER_MSGTYPE_DATA && header.getSubtype() ==  PLAYER_RFID_DATA_TAGS){
			PlayerRfidData rfid = (PlayerRfidData)data;
			
			//Cleanup our existing tags
			if(tags != null){
				tags.clear();
			}
			tags = rfid.getTags();
		}
		else{
			System.err.println("Skipping rfid message with unknown type/subtype.");
		}
		
	}
	
	
	
	
}