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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

import es.uji.robot.player.generated.abstractproxy.AbstractBlackboard;
import es.uji.robot.player.generated.xdr.PlayerBlackboardEntry;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.xdr.XDRObject;

/**
*The blackboard proxy provides an interface to a simple data-store in a similar fashion to a hash-map.
*Data is set and retrieved by using a label. Any player message structure can be stored in the blackboard.
*At this time it is up to the user to pack and unpack the entry data. The xdr functions can be used to do
*this.
*/
public class Blackboard extends AbstractBlackboard{

	public static interface BlackboardEventHandler {
		public void onBlackboardKeyUpdate(PlayerBlackboardEntry event);
	}
	
	private BlackboardEventHandler blackboardEventHandler;

	public BlackboardEventHandler getBlackboardEventHandler() {
		return blackboardEventHandler;
	}

	public void setBlackboardEventHandler(
			BlackboardEventHandler blackboardEventHandler) {
		this.blackboardEventHandler = blackboardEventHandler;
	}
	
	public Blackboard(Client client, int index){
		super.init(client, PLAYER_BLACKBOARD_CODE, index);
	}
	
	private PlayerBlackboardEntry packEntryString(ByteArrayOutputStream key, ByteArrayOutputStream group, ByteArrayOutputStream str){
		PlayerBlackboardEntry entry = new PlayerBlackboardEntry();
		
		entry.setType((short)PLAYERC_BLACKBOARD_DATA_TYPE_COMPLEX);
		entry.setSubtype((short)PLAYERC_BLACKBOARD_DATA_SUBTYPE_STRING);
		
		entry.setKey(key);
		
		entry.setGroup(group);
		
		entry.setData(str);
		
		//TODO: Test if timeStampSec and timeStampUsec are correct
		entry.setTimestampSec((int)(System.currentTimeMillis() * 0.001));
		entry.setTimestampUsec((int)(System.currentTimeMillis() * 1000));
		
		return entry;
	}
	
	private PlayerBlackboardEntry packEntryInt(ByteArrayOutputStream key, ByteArrayOutputStream group, int i){
		PlayerBlackboardEntry entry = new PlayerBlackboardEntry();
		
		entry.setType((short)PLAYERC_BLACKBOARD_DATA_TYPE_SIMPLE);
		entry.setSubtype((short)PLAYERC_BLACKBOARD_DATA_SUBTYPE_INT);
		
		entry.setKey(key);
		
		entry.setGroup(group);
		
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(i);
		entry.setData(baos);
		
		//TODO: Test if timeStampSec and timeStampUsec are correct
		entry.setTimestampSec((int)(System.currentTimeMillis() * 0.001));
		entry.setTimestampUsec((int)(System.currentTimeMillis() * 1000));
		
		return entry;
	}
	
	private PlayerBlackboardEntry packEntryDouble(ByteArrayOutputStream key, ByteArrayOutputStream group, double i){
		PlayerBlackboardEntry entry = new PlayerBlackboardEntry();
		
		entry.setType((short)PLAYERC_BLACKBOARD_DATA_TYPE_SIMPLE);
		entry.setSubtype((short)PLAYERC_BLACKBOARD_DATA_SUBTYPE_DOUBLE);
		
		entry.setKey(key);
		
		entry.setGroup(group);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(i);
		entry.setData(baos);
		
		//TODO: Test if timeStampSec and timeStampUsec are correct
		entry.setTimestampSec((int)(System.currentTimeMillis() * 0.001));
		entry.setTimestampUsec((int)(System.currentTimeMillis() * 1000));
		
		return entry;
	}
	
	protected ByteArrayOutputStream unpackEntryString(PlayerBlackboardEntry entry){
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		
		if(entry != null){
			if( entry.getType() == PLAYERC_BLACKBOARD_DATA_TYPE_COMPLEX && entry.getSubtype() == PLAYERC_BLACKBOARD_DATA_SUBTYPE_STRING ){
				result = entry.getData();
			}
		}
		return result;
	}
	
	protected int unpackEntryInt(PlayerBlackboardEntry entry) throws IOException{
		int result = 0;
		
		if(entry != null){
			if( entry.getType() == PLAYERC_BLACKBOARD_DATA_TYPE_SIMPLE && entry.getSubtype() == PLAYERC_BLACKBOARD_DATA_SUBTYPE_INT ){
				//TODO: Test if player uses BigEndian or LittleEndian
				//result = new Integer(entry.getData().toString());
				DataInputStream dis = new DataInputStream(new ByteArrayInputStream(entry.getData().toByteArray()));
				result = dis.readInt();
			}
		}
		return result;
	}
	
	protected double unpackEntryDouble(PlayerBlackboardEntry entry) throws IOException{
		Double result = 0.0;
		
		if(entry != null){
			if( entry.getType() == PLAYERC_BLACKBOARD_DATA_TYPE_SIMPLE && entry.getSubtype() == PLAYERC_BLACKBOARD_DATA_SUBTYPE_DOUBLE ){
				//TODO: Test if player uses BigEndian or LittleEndian
				//result = new Double(entry.getData().toString());
				DataInputStream dis = new DataInputStream(new ByteArrayInputStream(entry.getData().toByteArray()));
				result = dis.readDouble();
			}
		}
		return result;
	}
	
	protected boolean checkEntryIsString(PlayerBlackboardEntry entry){
		if(entry != null){
			if( entry.getType() == PLAYERC_BLACKBOARD_DATA_TYPE_COMPLEX && entry.getSubtype() == PLAYERC_BLACKBOARD_DATA_SUBTYPE_STRING ){
				return true;
			}
		}
		return false;
	}
	
	protected boolean checkEntryIsInt(PlayerBlackboardEntry entry){
		if(entry != null){
			if( entry.getType() == PLAYERC_BLACKBOARD_DATA_TYPE_SIMPLE && entry.getSubtype() == PLAYERC_BLACKBOARD_DATA_SUBTYPE_INT ){
				return true;
			}
		}
		return false;
	}
	
	protected boolean checkEntryIsDouble(PlayerBlackboardEntry entry){
		if(entry != null){
			if( entry.getType() == PLAYERC_BLACKBOARD_DATA_TYPE_SIMPLE && entry.getSubtype() == PLAYERC_BLACKBOARD_DATA_SUBTYPE_DOUBLE ){
				return true;
			}
		}
		return false;
	}
	
	/** @throws ClientException 
	 * @brief Subscribe to a key.
	 */
	
	public PlayerBlackboardEntry subscribeToKey(ByteArrayOutputStream key, ByteArrayOutputStream group) throws ClientException{
		PlayerBlackboardEntry request = new PlayerBlackboardEntry();
		
		request.setKey(key);
		request.setGroup(group);
		
		return (PlayerBlackboardEntry)super.getClient().request(this, PLAYER_BLACKBOARD_REQ_SUBSCRIBE_TO_KEY, request);
	}
	
	/** @throws ClientException 
	 * @brief Get the current value of a key, without subscribing. 
	 */
	
	public PlayerBlackboardEntry retrieveEntry(ByteArrayOutputStream key, ByteArrayOutputStream group) throws ClientException{
		PlayerBlackboardEntry request = new PlayerBlackboardEntry();
		
		request.setKey(key);
		request.setGroup(group);
		
		return (PlayerBlackboardEntry)super.getClient().request(this, PLAYER_BLACKBOARD_REQ_GET_ENTRY, request);
	}
	
	
	/** @throws ClientException 
	 * @brief Unsubscribe from a key. 
	 */
	public void unsubscribeFromKey(ByteArrayOutputStream key, ByteArrayOutputStream group) throws ClientException{
		PlayerBlackboardEntry request = new PlayerBlackboardEntry();
		
		request.setKey(key);
		request.setGroup(group);
		
		super.getClient().request(this, PLAYER_BLACKBOARD_REQ_UNSUBSCRIBE_FROM_KEY, request);
	}
	
	
	/** @throws ClientException 
	 * @brief Subscribe to a group. The current entries are sent as data messages. 
	 */
	public void subscribeToGroup(ByteArrayOutputStream group) throws ClientException{
		PlayerBlackboardEntry request = new PlayerBlackboardEntry();
		
		request.setGroup(group);
		
		super.getClient().request(this, PLAYER_BLACKBOARD_REQ_SUBSCRIBE_TO_GROUP, request);
	}
	
	
	/** @throws ClientException 
	 * @brief Unsubscribe from a group. 
	 */
	public void unsubscribeFromGroup(ByteArrayOutputStream group) throws ClientException{
		PlayerBlackboardEntry request = new PlayerBlackboardEntry();
		
		request.setGroup(group);
		
		super.getClient().request(this, PLAYER_BLACKBOARD_REQ_UNSUBSCRIBE_FROM_GROUP, request);
	}
	

	/**
	 * Send an entry value.
	 * @throws ClientException 
	 */
	public void sendEntry(PlayerBlackboardEntry entry) throws ClientException{
		super.getClient().request(this, PLAYER_BLACKBOARD_REQ_SET_ENTRY, entry);
	}
	
	
	/**
	 * Send a string value.
	 * @throws ClientException 
	 */
	public void sendString(ByteArrayOutputStream key, ByteArrayOutputStream group, ByteArrayOutputStream value) throws ClientException{
		PlayerBlackboardEntry entry = packEntryString(key, group, value);
		sendEntry(entry);
	}
	
	/**
	 * Send an int value.
	 * @throws ClientException 
	 */
	public void sendInt(ByteArrayOutputStream key, ByteArrayOutputStream group, int value) throws ClientException{
		PlayerBlackboardEntry entry = packEntryInt(key, group, value);
		sendEntry(entry);
	}
	
	/**
	 * Send a double value.
	 * @throws ClientException 
	 */
	public void sendDouble(ByteArrayOutputStream key, ByteArrayOutputStream group, double value) throws ClientException{
		PlayerBlackboardEntry entry = packEntryDouble(key, group, value);
		sendEntry(entry);
	}
	

	public void putMsg(PlayerMsghdr header, XDRObject data) {
		if ( blackboardEventHandler != null ) {
			PlayerBlackboardEntry event = (PlayerBlackboardEntry)data;
			blackboardEventHandler.onBlackboardKeyUpdate(event);
		}
		
	}
	
	
	
	
}