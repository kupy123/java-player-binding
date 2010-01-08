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

import es.uji.robot.player.generated.abstractproxy.AbstractCamera;
import es.uji.robot.player.generated.xdr.PlayerCameraData;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.xdr.XDRObject;

/**
*The camera proxy can be used to get images from a camera.
*/
public class Camera extends AbstractCamera{
	
	/** 
	*Image dimensions (pixels). 
	*/
	private int height;
	private int width;
	/** 
	*Image bits-per-pixel (8, 16, 24). 
	*/
	private int bitsPerPixel;
	/** 
	*Image format (e.g., RGB888). 
	*/
	private int format;
	/** 
	*Some images (such as disparity maps) use scaled pixel values;
	*for these images, fdiv specifies the scale divisor (i.e., divide
	*the integer pixel value by fdiv to recover the real pixel value). 
	*/
	private int fdiv;
	/** 
	*Image compression method.
	*/
	private int compression;
	/** 
	*Image data (byte aligned, row major order).  Multi-byte image
	*formats (such as MONO16) are automatically converted to the
	*correct host byte ordering.
	*/
	private ByteArrayOutputStream image;
	
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

	public int getBitsPerPixel() {
		return bitsPerPixel;
	}

	public void setBitsPerPixel(int bitsPerPixel) {
		this.bitsPerPixel = bitsPerPixel;
	}

	public int getFormat() {
		return format;
	}

	public void setFormat(int format) {
		this.format = format;
	}

	public int getFdiv() {
		return fdiv;
	}

	public void setFdiv(int fdiv) {
		this.fdiv = fdiv;
	}

	public int getCompression() {
		return compression;
	}

	public void setCompression(int compression) {
		this.compression = compression;
	}

	public ByteArrayOutputStream getImage() {
		return image;
	}

	public void setImage(ByteArrayOutputStream image) {
		this.image = image;
	}
	
	public Camera(Client client, int index){
		super.init(client, PLAYER_CAMERA_CODE, index);
	}

	public void putMsg(PlayerMsghdr header, XDRObject data) {
		if ( header.getType() == PLAYER_MSGTYPE_DATA && header.getSubtype() == PLAYER_CAMERA_DATA_STATE ){
			PlayerCameraData camera = (PlayerCameraData)data;
			
			width = camera.getWidth();
			height = camera.getHeight();
			bitsPerPixel = camera.getBpp();
			format = camera.getFormat();
			fdiv = camera.getFdiv();
			compression = camera.getCompression();
			image = camera.getImage();
		}
		else{
			System.err.println("Skipping camera message with unknown type/subtype.");
		}
	}
	
	
}
