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
public class XDRString implements XDRObject{
	
	private String value;
	
	public XDRString(){
		this.value = "";
	}

	public XDRString(String value){
		this.value = value;
	} 

	public XDRString(byte[] buffer, int idx, int len) throws XDRException{
		this.fromXDR(buffer,idx,len);
	}
	
	/**
	 * Encodes a XDR String
	 * 
	 * @return XDR String encoding information 
	 */

	public byte[] toXDR(){
		byte[] strbytes = value.getBytes();
		
		byte[] ret = new byte[strbytes.length+4];
		byte[] r1 = new byte[4];
		
		r1 = new XDRInt((int)strbytes.length).toXDR();
		System.arraycopy(r1, 0, ret, 0, 4);
		System.arraycopy(strbytes, 0, ret, 4, strbytes.length);
		
		return ret;
	}
	
	/**
	 * Decodes an XDR String
	 * 
	 * @param XDR information
	 * @param Initial reading position
	 * @param Number of elements to read
	 * @throws XDRException 
	 */

	public void fromXDR(byte[] buffer, int idx, int len) throws XDRException{
		//XXX: El uso de len es erroneo, ya que se pasa siempre buffer.length y no el tama–o a leer, as’ que no tiene sentido comprobar nada ...
//		if ( len > buffer.length ) {
//			throw new XDRException("Invalid parameter 'len'. Array out of bounds.");
//		} else if ( len < idx+getBufferLength() ) {
//			throw new XDRException("Unable to decode "+getClass().getName()+". No enough bytes in buffer.");
//		}
		XDRInt xdrlen = new XDRInt(buffer,idx,len);
		int length = new Integer(xdrlen.getValue());
		idx += xdrlen.getBufferLength();
		
		if (length > 0){
			byte [] bytes = new byte[length];
			System.arraycopy(buffer, idx, bytes, 0, length);
			this.value = new String(bytes);
		}
	}
	
	/**
	 * Returns object size
	 * 
	 * @return Object size
	 */

	public int getBufferLength(){
		return value.length()+4;
	}
	
	/**
	 * Returns object value
	 * 
	 * @return Object value
	 */

	public String getValue(){
		return this.value;
	}
	
	@Override
	public Object getEnclosedValue() {
		return this.value;
	}
}