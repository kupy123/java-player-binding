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

import es.uji.robot.player.generated.abstractproxy.AbstractWsn;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.player.generated.xdr.PlayerWsnCmd;
import es.uji.robot.player.generated.xdr.PlayerWsnData;
import es.uji.robot.player.generated.xdr.PlayerWsnDatafreqConfig;
import es.uji.robot.player.generated.xdr.PlayerWsnDatatypeConfig;
import es.uji.robot.player.generated.xdr.PlayerWsnNodeData;
import es.uji.robot.player.generated.xdr.PlayerWsnPowerConfig;
import es.uji.robot.xdr.XDRObject;

/**
 * The wsn proxy provides an interface to a Wireless Sensor Network.
 *
 */
public class Wsn extends AbstractWsn{	
	
	/**
	 *  The type of WSN node.                                               
	 */
	private int nodeType;
	/**
	 *  The ID of the WSN node.                                             
	 */
	private int nodeId;
	/**
	 *  The ID of the WSN node's parent (if existing).                      
	 */
	private int nodeParentId;
	/**
	 *  The WSN node's data packet.                                         
	 */
	private PlayerWsnNodeData dataPacket;
	
	
	public int getNodeType() {
		return nodeType;
	}
	public void setNodeType(int nodeType) {
		this.nodeType = nodeType;
	}
	public int getNodeId() {
		return nodeId;
	}
	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}
	public int getNodeParentId() {
		return nodeParentId;
	}
	public void setNodeParentId(int nodeParentId) {
		this.nodeParentId = nodeParentId;
	}
	public PlayerWsnNodeData getDataPacket() {
		return dataPacket;
	}
	public void setDataPacket(PlayerWsnNodeData dataPacket) {
		this.dataPacket = dataPacket;
	}
	
	public Wsn(Client client, int index){
		super.init(client, PLAYER_WSN_CODE, index);
	}
	
	/**
	 *  Set the device state. 
	 * @throws ClientException 
	 */
	public void sendDeviceState(int nodeId, int groupId, int deviceNr, int state) throws ClientException{
		PlayerWsnCmd command = new PlayerWsnCmd();
		command.setNodeId(nodeId);
		command.setGroupId(groupId);
		command.setDevice(deviceNr);
		command.setEnable((char)state);
		
		super.getClient().write(this, PLAYER_WSN_CMD_DEVSTATE, command);	
		
	}
	/** 
	 * Put the node in sleep mode (0) or wake it up (1). 
	 * @throws ClientException 
	 */
	public PlayerWsnPowerConfig power(int nodeId, int groupId, int value) throws ClientException{
		PlayerWsnPowerConfig configure = new PlayerWsnPowerConfig();
		configure.setNodeId(nodeId);
		configure.setGroupId(groupId);
		configure.setValue((char)value);
		
		return (PlayerWsnPowerConfig)super.getClient().request(this, PLAYER_WSN_REQ_POWER, configure);
	}
	
	/**
	 *  Change the data type to RAW or converted engineering units. 
	 * @throws ClientException 
	 */
	public PlayerWsnDatatypeConfig dataType(int value) throws ClientException{
		PlayerWsnDatatypeConfig configure = new PlayerWsnDatatypeConfig();
		configure.setValue((char)value);
		
		return (PlayerWsnDatatypeConfig)super.getClient().request(this, PLAYER_WSN_REQ_DATATYPE, configure);
	}
	
	/** 
	 * Change data delivery frequency. 
	 * @throws ClientException 
	 */
	public PlayerWsnDatafreqConfig dataFrequency(int nodeId, int groupId, double frequency) throws ClientException{
		PlayerWsnDatafreqConfig configure = new PlayerWsnDatafreqConfig();
		configure.setNodeId(nodeId);
		configure.setGroupId(groupId);
		configure.setFrequency(frequency);
		
		return (PlayerWsnDatafreqConfig)super.getClient().request(this, PLAYER_WSN_REQ_DATAFREQ, configure);
	}
	
	@Override
	public void putMsg(PlayerMsghdr header, XDRObject data) {
		if( header.getType() == PLAYER_MSGTYPE_DATA && header.getSubtype() ==  PLAYER_WSN_DATA_STATE){
			PlayerWsnData wsn = (PlayerWsnData)data;
			nodeType = wsn.getNodeType();
			nodeId = wsn.getNodeId();
			nodeParentId = wsn.getNodeParentId();
			dataPacket = wsn.getDataPacket();
		}
		else{
			System.err.println("Skipping wsn message with unknown type/subtype.");
		}
		
	}
}