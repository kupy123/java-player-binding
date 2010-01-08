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

import es.uji.robot.player.generated.abstractproxy.AbstractBlobfinder;
import es.uji.robot.player.generated.xdr.PlayerBlobfinderBlob;
import es.uji.robot.player.generated.xdr.PlayerBlobfinderData;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.xdr.XDRObject;

/**
*The blobfinder proxy provides an interface to color blob detectors
*such as the ACTS vision system.  See the Player User Manual for a
*complete description of the drivers that support this interface.
*/
public class Blobfinder extends AbstractBlobfinder{
	
	/** 
	*Image dimensions (pixels). 
	*/
	private int height;
	private int width;
	/** 
	*A list of detected blobs. 
	*/
	private ArrayList<PlayerBlobfinderBlob> blobs;
	
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public ArrayList<PlayerBlobfinderBlob> getBlobs() {
		return blobs;
	}
	public void setBlobs(ArrayList<PlayerBlobfinderBlob> blobs) {
		this.blobs = blobs;
	}
	
	public Blobfinder(Client client, int index){
		super.init(client, PLAYER_BLOBFINDER_CODE, index);
	}

	public void putMsg(PlayerMsghdr header, XDRObject data) {
		if ( header.getType() == PLAYER_MSGTYPE_DATA && header.getSubtype() == PLAYER_BLOBFINDER_DATA_BLOBS ){
			PlayerBlobfinderData blob = (PlayerBlobfinderData)data;
			width = blob.getWidth();
			height = blob.getHeight();
			blobs = blob.getBlobs();
		}
		else{
			System.err.println("Skipping blobfinder message with unknown type/subtype.");
		}
		
	}
	
	
	

}
