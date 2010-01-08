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


public class XDRLong implements XDRObject{
	
	private Long value;
	
	public XDRLong(){
		this.value = 0l;
	}

	public XDRLong(Long value){
		this.value = value;
	}

	public XDRLong(byte[] buffer, int idx, int len) throws XDRException{
		this.fromXDR(buffer,idx,len);
	}
	
	/**
	 * Encodes a XDR Long
	 * 
	 * @return XDR Long encoding information 
	 */

	public byte[] toXDR(){
		byte[] ret = new byte[8];
		byte[] r1 = new byte[4];
		byte[] r2 = new byte[4];
		
		r1 = new XDRInt((int)(this.getValue() >>> 32)).toXDR();
		r2 = new XDRInt((int)(this.getValue() & 0x00000000FFFFFFFFl)).toXDR();
		
		System.arraycopy(r1, 0, ret, 0, 4);
		System.arraycopy(r2, 0, ret, 4, 4);
		
		return ret;
	}
	
	/**
	 * Decodes an XDR Long
	 * 
	 * @param XDR information
	 * @param Initial reading position
	 * @param Number of elements to read
	 */

	public void fromXDR(byte[] buffer, int idx, int len) throws XDRException{
		//XXX: El uso de len es erroneo, ya que se pasa siempre buffer.length y no el tama–o a leer, as’ que no tiene sentido comprobar nada ...
//		if ( len > buffer.length ) {
//			throw new XDRException("Invalid parameter 'len'. Array out of bounds.");
//		} else if ( len < idx+getBufferLength() ) {
//			throw new XDRException("Unable to decode "+getClass().getName()+". No enough bytes in buffer.");
//		}
		XDRInt xdrint = new XDRInt(buffer,idx,len);
		this.value = (long)xdrint.getValue();
		this.value = this.value << 32;
		idx += xdrint.getBufferLength();
		this.value += ((long)(new XDRInt(buffer, idx,len)).getValue() & 0x00000000FFFFFFFFl); 
	}
	
	/**
	 * Returns object size
	 * 
	 * @return Object size
	 */

	public int getBufferLength(){
		return 8;
	}
	
	/**
	 * Returns object value
	 * 
	 * @return Object value
	 */

	public long getValue(){
		return this.value;
	}

	@Override
	public Object getEnclosedValue() {
		return this.value;
	}
}