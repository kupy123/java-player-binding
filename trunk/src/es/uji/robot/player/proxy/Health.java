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

import es.uji.robot.player.generated.abstractproxy.AbstractHealth;
import es.uji.robot.player.generated.xdr.PlayerHealthCpu;
import es.uji.robot.player.generated.xdr.PlayerHealthData;
import es.uji.robot.player.generated.xdr.PlayerHealthMemory;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.xdr.XDRObject;

/**
*The health proxy provides an interface to the HEALTH Module.
*/
public class Health extends AbstractHealth{

	/** 
	*The current cpu usage						
	*/
	private PlayerHealthCpu cpuUsage;
	/** 
	*The memory stats						
	*/
	private PlayerHealthMemory memory;
	/** 
	*The swap stats						
	*/
	private PlayerHealthMemory swap;

	public PlayerHealthCpu getCpuUsage() {
		return cpuUsage;
	}
	public void setCpuUsage(PlayerHealthCpu cpuUsage) {
		this.cpuUsage = cpuUsage;
	}
	public PlayerHealthMemory getMemory() {
		return memory;
	}
	public void setMemory(PlayerHealthMemory memory) {
		this.memory = memory;
	}
	public PlayerHealthMemory getSwap() {
		return swap;
	}
	public void setSwap(PlayerHealthMemory swap) {
		this.swap = swap;
	}
	
	public Health(Client client, int index){
		super.init(client, PLAYER_HEALTH_CODE, index);
	}
	@Override
	public void putMsg(PlayerMsghdr header, XDRObject data) {
		if ( header.getType() == PLAYER_MSGTYPE_DATA && header.getSubtype() == PLAYER_HEALTH_DATA_STATE ){
			PlayerHealthData health = (PlayerHealthData)data;
			
			cpuUsage = health.getCpuUsage();
			memory = health.getMem();
			swap = health.getSwap();
		}
		else{
			System.err.println("Skipping health message with unknown type/subtype.");
		}
		
	}
	
	
	
	
	
}