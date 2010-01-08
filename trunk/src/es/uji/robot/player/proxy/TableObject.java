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

import es.uji.robot.xdr.XDRObject;

public class TableObject{
    private short iface;
    private char type;
    private char subtype;
    private XDRObject object;

    public TableObject(short iface, char type, char subtype, XDRObject object){
        this.iface = iface;
        this.type = type;
        this.subtype = subtype;
        this.object = object;
    }
    public short getIface(){
        return this.iface;
    }

    public void setIface(short iface){
        this.iface = iface;
    }

    public char getType(){
        return this.type;
    }

    public void setType(char type){
        this.type = type;
    }

    public char getSubtype(){
        return this.subtype;
    }

    public void setSubtype(char subtype){
        this.subtype = subtype;
    }

    public XDRObject getObject(){
        return this.object;
    }

    public void setIface(XDRObject object){
        this.object = object;
    }

}
