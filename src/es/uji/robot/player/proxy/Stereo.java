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

import es.uji.robot.player.generated.abstractproxy.AbstractStereo;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.player.generated.xdr.PlayerPointcloud3dStereoElement;
import es.uji.robot.player.generated.xdr.PlayerStereoData;
import es.uji.robot.xdr.XDRObject;

/**
 * The stereo proxy provides an interface to a stereo device.
 *
 */
public class Stereo extends AbstractStereo{

	/** Left channel image */
	private Camera leftChannel;
	/**
	 * Right channel image
	 */
	private Camera rightChannel;
	/**
	 * Disparity image
	 */
	private Camera disparity;
	/** 3-D stereo point cloud */
	private ArrayList<PlayerPointcloud3dStereoElement> points;
	
	public Camera getLeftChannel() {
		return leftChannel;
	}
	public void setLeftChannel(Camera leftChannel) {
		this.leftChannel = leftChannel;
	}
	public Camera getRightChannel() {
		return rightChannel;
	}
	public void setRightChannel(Camera rightChannel) {
		this.rightChannel = rightChannel;
	}
	public Camera getDisparity() {
		return disparity;
	}
	public void setDisparity(Camera disparity) {
		this.disparity = disparity;
	}
	public ArrayList<PlayerPointcloud3dStereoElement> getPoints() {
		return points;
	}
	public void setPoints(ArrayList<PlayerPointcloud3dStereoElement> points) {
		this.points = points;
	}
	
	
	public Stereo(Client client, int index){
		super.init(client, PLAYER_STEREO_CODE, index);
	}
	
	@Override
	public void putMsg(PlayerMsghdr header, XDRObject data) {
		if(header.getType() == PLAYER_MSGTYPE_DATA && header.getSubtype() ==  PLAYER_STEREO_DATA_STATE){
			PlayerStereoData stereo = (PlayerStereoData)data;
			
			leftChannel.setWidth(stereo.getLeftChannel().getWidth());
			leftChannel.setHeight(stereo.getLeftChannel().getHeight());
			leftChannel.setBitsPerPixel(stereo.getLeftChannel().getBpp());
			leftChannel.setFormat(stereo.getLeftChannel().getFormat());
			leftChannel.setFdiv(stereo.getLeftChannel().getFdiv());
			leftChannel.setCompression(stereo.getLeftChannel().getCompression());
			leftChannel.setImage(stereo.getLeftChannel().getImage());
			
			rightChannel.setWidth(stereo.getRightChannel().getWidth());
			rightChannel.setHeight(stereo.getRightChannel().getHeight());
			rightChannel.setBitsPerPixel(stereo.getRightChannel().getBpp());
			rightChannel.setFormat(stereo.getRightChannel().getFormat());
			rightChannel.setFdiv(stereo.getRightChannel().getFdiv());
			rightChannel.setCompression(stereo.getRightChannel().getCompression());
			rightChannel.setImage(stereo.getRightChannel().getImage());
			
			disparity.setWidth(stereo.getDisparity().getWidth());
			disparity.setHeight(stereo.getDisparity().getHeight());
			disparity.setBitsPerPixel(stereo.getDisparity().getBpp());
			disparity.setFormat(stereo.getDisparity().getFormat());
			disparity.setFdiv(stereo.getDisparity().getFdiv());
			disparity.setCompression(stereo.getDisparity().getCompression());
			disparity.setImage(stereo.getDisparity().getImage());
			
			points = stereo.getPoints();
		}
		else{
			// TODO:Skipping stereo message with unknown type/subtype
		}
		
	}
	
	
	
}
