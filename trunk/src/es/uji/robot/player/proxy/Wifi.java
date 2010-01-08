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
import java.util.ArrayList;

import es.uji.robot.player.generated.abstractproxy.AbstractWifi;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.player.generated.xdr.PlayerWifiData;
import es.uji.robot.player.generated.xdr.PlayerWifiLink;
import es.uji.robot.xdr.XDRObject;

/**
*The wifi proxy is used to query the state of a wireless network.  It
*returns information such as the link quality and signal strength of
*access points or of other wireless NIC's on an ad-hoc network.
*/
public class Wifi extends AbstractWifi{
	
	/** 
	*A list containing info for each link. 
	*/
	private ArrayList<PlayerWifiLink> links;
	private ByteArrayOutputStream ip;


	public ArrayList<PlayerWifiLink> getLinks() {
		return links;
	}

	public void setLinks(ArrayList<PlayerWifiLink> links) {
		this.links = links;
	}

	public ByteArrayOutputStream getIp() {
		return ip;
	}

	public void setIp(ByteArrayOutputStream ip) {
		this.ip = ip;
	}


	public Wifi(Client client, int index){
		super.init(client, PLAYER_WIFI_CODE, index);
	}

	/** 
	*@brief Get link state. 
	*/
	public PlayerWifiLink retrieveLink(int link){
		return links.get(link);
	}

	@Override
	public void putMsg(PlayerMsghdr header, XDRObject data) {
		if(header.getType() == PLAYER_MSGTYPE_DATA){
			PlayerWifiData wifi = (PlayerWifiData)data;
			
			for(int i = 0; i < wifi.getLinks().size(); i++){
				links.get(i).setMode(wifi.getLinks().get(i).getMode());
				links.get(i).setEncrypt(wifi.getLinks().get(i).getEncrypt());
				links.get(i).setFreq(wifi.getLinks().get(i).getFreq());
				links.get(i).setQual(wifi.getLinks().get(i).getQual());
				links.get(i).setLevel(wifi.getLinks().get(i).getLevel());
				links.get(i).setNoise(wifi.getLinks().get(i).getNoise());
			}
		}
		else{
			System.err.println("Skipping wifi message with unknown type/subtype.");
		}
	}

}