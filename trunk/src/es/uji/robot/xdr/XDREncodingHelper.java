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

package es.uji.robot.xdr;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class XDREncodingHelper{
	
	/*
	public static byte[] encode(Object obj, int countvar) throws XDRException{
		if ( obj == null ) {
			throw new NullPointerException("obj must not be null");
		}
		if(obj instanceof ArrayList){
			return new XDRArray((ArrayList)obj).toXDR(countvar);
		}
		else {
			throw new XDRException("Not a supported XDR type '"+obj.getClass()+"'");
		}
	}
	*/

	public static byte[] encode(Object obj) throws XDRException, IOException{
		if ( obj == null ) {
			throw new NullPointerException("obj must not be null");
		}
		if(obj instanceof XDREncodable){
			return ((XDREncodable)obj).toXDR();
		}
		else if(obj instanceof Integer){
			return new XDRInt((Integer)obj).toXDR();
		}
		else if(obj instanceof Byte){
			return new XDRByte((Byte)obj).toXDR();
		}
		else if(obj instanceof Short){
			return new XDRShort((Short)obj).toXDR();
		}
		else if(obj instanceof Long){
			return new XDRLong((Long)obj).toXDR();
		}
		else if(obj instanceof Float){
			return new XDRFloat((Float)obj).toXDR();
		}
		else if(obj instanceof Double){
			return new XDRDouble((Double)obj).toXDR();
		}
		else if(obj instanceof String){
			return new XDRString((String)obj).toXDR();
		}
		else if(obj instanceof Boolean){
			return new XDRBoolean((Boolean)obj).toXDR();
		}
		else if(obj instanceof Character){
			return new XDRChar((Character)obj).toXDR();
		}
		else if(obj instanceof ByteArrayOutputStream){
			byte[] baosBytes = ((ByteArrayOutputStream)obj).toByteArray();
			byte[] sizeBytes = new XDRInt(baosBytes.length).toXDR();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(sizeBytes);
			baos.write(baosBytes);
			byte[] bytes = baos.toByteArray();
			return bytes;
		}
		else if(obj instanceof ArrayList){
			//TODO: Esto hay que arreglarlo con el parametrizado del XDRArray ...
			return new XDRArray((ArrayList)obj).toXDR();			
		}
		else {
			throw new XDRException("Not a supported XDR type '"+obj.getClass()+"'");
		}
	}
}