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

import es.uji.robot.player.generated.abstractproxy.AbstractBlinkenlight;
import es.uji.robot.player.generated.xdr.PlayerBlinkenlightCmdColor;
import es.uji.robot.player.generated.xdr.PlayerBlinkenlightCmdFlash;
import es.uji.robot.player.generated.xdr.PlayerBlinkenlightCmdPower;
import es.uji.robot.player.generated.xdr.PlayerBlinkenlightData;
import es.uji.robot.player.generated.xdr.PlayerColor;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.xdr.XDRObject;

/**
*The blinkenlight proxy provides an interface to a (possibly colored
*and/or blinking) indicator light.
*/

public class Blinkenlight extends AbstractBlinkenlight{

	private int enabled;
	private double dutyCycle;
	private double period;
	private char red;
	private char green;
	private char blue;
	
	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

	public double getDutyCycle() {
		return dutyCycle;
	}

	public void setDutyCycle(double dutyCycle) {
		this.dutyCycle = dutyCycle;
	}

	public double getPeriod() {
		return period;
	}

	public void setPeriod(double period) {
		this.period = period;
	}

	public char getRed() {
		return red;
	}

	public void setRed(char red) {
		this.red = red;
	}

	public char getGreen() {
		return green;
	}

	public void setGreen(char green) {
		this.green = green;
	}

	public char getBlue() {
		return blue;
	}

	public void setBlue(char blue) {
		this.blue = blue;
	}
	
	public Blinkenlight(Client client, int index){
		super.init(client, PLAYER_BLINKENLIGHT_CODE, index);
	}

	/** 
	*Enable/disable power to the blinkenlight device. 
	 * @throws ClientException 
	*/
	public void enable(int enable) throws ClientException{
		PlayerBlinkenlightCmdPower command = new PlayerBlinkenlightCmdPower();
		
		command.setEnable((char)enable);
		
		super.getClient().write(this, PLAYER_BLINKENLIGHT_CMD_POWER, command);
		
	}
	
	/** 
	*Set the output color for the blinkenlight device. 
	 * @throws ClientException 
	*/
	public void color(int id, char red, char green, char blue) throws ClientException{
		PlayerBlinkenlightCmdColor command = new PlayerBlinkenlightCmdColor();
		PlayerColor color = new PlayerColor();
		
		color.setRed(red);
		color.setGreen(green);
		color.setBlue(blue);
		
		command.setColor(color);
		command.setId((short)id);
		
		super.getClient().write(this, PLAYER_BLINKENLIGHT_CMD_COLOR, command);
		
	}
	
	/** 
	*Make the light blink, setting the period in seconds and the
	*mark/space ratiom (0.0 to 1.0). 
	 * @throws ClientException 
	*/
	public void blink(int id, float period, float dutyCycle) throws ClientException{
		PlayerBlinkenlightCmdFlash command = new PlayerBlinkenlightCmdFlash();
		
		command.setId((short)id);
		command.setPeriod(period);
		command.setDutycycle(dutyCycle);
		
		super.getClient().write(this, PLAYER_BLINKENLIGHT_CMD_FLASH, command);
		
	}

	public void putMsg(PlayerMsghdr header, XDRObject data) {
		if( header.getType() == PLAYER_MSGTYPE_DATA && header.getSubtype() == PLAYER_BLINKENLIGHT_DATA_STATE ){
			PlayerBlinkenlightData blink = (PlayerBlinkenlightData)data;
			
			enabled = blink.getEnable();
			dutyCycle = blink.getDutycycle();
			period = blink.getPeriod();
			red = blink.getColor().getRed();
			green = blink.getColor().getGreen();
			blue = blink.getColor().getBlue();
		}
		else{
			System.err.println("Skipping blinkenlight message with unknown type/subtype.");
		}
		
	}
}
