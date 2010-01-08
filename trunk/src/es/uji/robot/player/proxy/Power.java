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

import es.uji.robot.player.generated.abstractproxy.AbstractPower;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.player.generated.xdr.PlayerPowerData;
import es.uji.robot.xdr.XDRObject;

/**
*The power proxy provides an interface through which battery levels can
*be monitored.
*/
public class Power extends AbstractPower{

	/** 
	*status bits. Bitwise-and with PLAYER_POWER_MASK_ values to see
	*which fields are being set by the driver. 
	*/
	private int valid;
	/** 
	*Battery charge (Volts). 
	*/
	private double charge;
	/** 
	*Battery charge (percent full). 
	*/
	private double percent;
	/** 
	*energy stored (Joules) 
	*/
	private double joules;
	/** 
	*power currently being used (Watts). Negative numbers indicate
	*charging. 
	*/
	private double watts;
	/** 
	*charging flag. TRUE if charging, else FALSE 
	*/
	private int charging;

	public int getValid() {
		return valid;
	}
	public void setValid(int valid) {
		this.valid = valid;
	}
	public double getCharge() {
		return charge;
	}
	public void setCharge(double charge) {
		this.charge = charge;
	}
	public double getPercent() {
		return percent;
	}
	public void setPercent(double percent) {
		this.percent = percent;
	}
	public double getJoules() {
		return joules;
	}
	public void setJoules(double joules) {
		this.joules = joules;
	}
	public double getWatts() {
		return watts;
	}
	public void setWatts(double watts) {
		this.watts = watts;
	}
	public int getCharging() {
		return charging;
	}
	public void setCharging(int charging) {
		this.charging = charging;
	}
	
	public Power(Client client, int index){
		super.init(client, PLAYER_POWER_CODE, index);
	}
	@Override
	public void putMsg(PlayerMsghdr header, XDRObject data) {
		PlayerPowerData power = (PlayerPowerData)data;
		valid = power.getValid();
		
		if((valid & PLAYER_POWER_MASK_VOLTS) == 1){
			charge = power.getVolts();
		}
		if((valid & PLAYER_POWER_MASK_PERCENT) == 1){
			percent = power.getPercent();
		}
		if((valid & PLAYER_POWER_MASK_JOULES) == 1){
			joules = power.getJoules();
		}
		if((valid & PLAYER_POWER_MASK_WATTS) == 1){
			watts = power.getWatts();
		}
		if((valid & PLAYER_POWER_MASK_CHARGING) == 1){
			charging = power.getCharging();
		}
		
	}
	
	

}