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

import es.uji.robot.player.generated.abstractproxy.AbstractGraphics3d;
import es.uji.robot.player.generated.xdr.PlayerColor;
import es.uji.robot.player.generated.xdr.PlayerGraphics3dCmdDraw;
import es.uji.robot.player.generated.xdr.PlayerGraphics3dCmdRotate;
import es.uji.robot.player.generated.xdr.PlayerGraphics3dCmdTranslate;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.player.generated.xdr.PlayerPoint3d;
import es.uji.robot.xdr.XDRObject;

/**
*The graphics3d proxy provides an interface to the graphics3d
*/
public class Graphics3d extends AbstractGraphics3d{

	/** 
	*current drawing color 
	*/
	private PlayerColor color;

	public PlayerColor getColor() {
		return color;
	}

	public void setColor(PlayerColor color) {
		this.color = color;
	}

	/** 
	*Set the current drawing color 
	*/
	public void sendColor(PlayerColor color){
		this.color = color;
	}
	
	/** 
	*Draw some points in the given mode 
	 * @throws ClientException 
	*/
	public void draw(int mode, ArrayList<PlayerPoint3d> points) throws ClientException{
		PlayerGraphics3dCmdDraw command = new PlayerGraphics3dCmdDraw();
		
		command.setDrawMode(mode);
		command.setPoints(points);
		command.setColor(color);
		
		super.getClient().write(this, PLAYER_GRAPHICS3D_CMD_DRAW, command);
	}
	
	/** 
	*Clear the canvas 
	 * @throws ClientException 
	*/
	public void clear() throws ClientException{
		super.getClient().write(this, PLAYER_GRAPHICS3D_CMD_CLEAR, null);
	}
	
	/** 
	*Translate the drawing coordinate system in 3d 
	 * @throws ClientException 
	*/
	public void translate(double x, double y, double z) throws ClientException{
		PlayerGraphics3dCmdTranslate command = new PlayerGraphics3dCmdTranslate();
		
		command.setX(x);
		command.setY(y);
		command.setZ(z);
		
		super.getClient().write(this, PLAYER_GRAPHICS3D_CMD_TRANSLATE, command);
		
	}
	
	/** 
	*Rotate the drawing coordinate system by [a] radians about the vector described by [x,y,z] 
	 * @throws ClientException 
	*/
	public void rotate(double a, double x, double y, double z) throws ClientException{
		PlayerGraphics3dCmdRotate command = new PlayerGraphics3dCmdRotate();
		
		command.setA(a);
		command.setX(x);
		command.setY(y);
		command.setZ(z);
		
		super.getClient().write(this, PLAYER_GRAPHICS3D_CMD_ROTATE, command);
		
	}

	@Override
	public void putMsg(PlayerMsghdr header, XDRObject data) {
		// TODO Auto-generated method stub
		
	}

}