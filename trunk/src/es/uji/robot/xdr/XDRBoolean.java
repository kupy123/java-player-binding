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
public class XDRBoolean implements XDRObject{
	
	private Boolean value;
	
	public XDRBoolean(){
		this.value = true;
	}

	public XDRBoolean(Boolean value){
		this.value = value;
	} 

	public XDRBoolean(byte[] buffer, int idx, int len) throws XDRException{
		this.fromXDR(buffer,idx,len);
	}
	
	/**
	 * Encodes a XDR Boolean
	 * 
	 * @return XDR Boolean encoding information
	 */

	public byte[] toXDR(){
		int obj;
		if(this.getValue()){
			obj = 1;
		}
		else{
			obj = 0;
		}
		return new XDRInt(obj).toXDR();
	}
	
	/**
	 * Decodes a XDR Boolean
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
		int val = (new XDRInt(buffer,idx,len)).getValue();
		if(val==1){
			this.value=true;
		}
		else{
			this.value=false;
		}
	}
	
	/**
	 * Returns object size
	 * 
	 * @return Object size
	 */

	public int getBufferLength(){
		return 4;
	}

	/**
	 * Returns object value
	 * 
	 * @return Object value
	 */
	
	public boolean getValue(){
		return this.value;
	}
	
	@Override
	public Object getEnclosedValue() {
		return this.value;
	}
}