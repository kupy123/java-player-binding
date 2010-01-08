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
import java.util.Iterator;

public class XDRArray<T extends XDRObject> extends ArrayList<T> implements XDRObject{
	
	private static final long serialVersionUID = 6473373383645443313L;

	Class<T> classType;
	
	public XDRArray(){
	}

	public XDRArray(ArrayList<T> value){
		clear();
		addAll(value);
	}
	
	public XDRArray(byte[] buffer, int idx, int len, Integer count, Class<T> clazz) throws XDRException{
		this.classType = clazz;
		this.fromXDR(buffer, idx, len, count);
	}
	
	/**
	 * Encodes a XDR Array
	 * 
	 * @return XDR Array encoding information
	 * @throws XDRException 
	 */

	public byte[] toXDR() throws XDRException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Iterator<? extends XDRObject> iter = this.iterator();
		while (iter.hasNext()){
			try {
				baos.write(XDREncodingHelper.encode(iter.next()));
			} catch (IOException e) {
				throw new XDRException(e);
			}
		}
		return baos.toByteArray();
	}
	
	/**
	 * Encodes a XDR Array
	 * 
	 * @param Indicates if we are working with a static or dynamic array
	 * @return XDR Array encoding information
	 * @throws XDRException 
	 */

	public byte[] toXDR(boolean dynamic) throws XDRException{
		if (dynamic){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				baos.write(new XDRInt(this.size()).toXDR());
			} catch (IOException e1) {
				throw new XDRException(e1);
			}
			try {
				baos.write(this.toXDR());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return baos.toByteArray();
		}
		else{
			return this.toXDR();
		}
	}
	
	/**
	 * Decodes an XDR Array
	 * 
	 * @param XDR information
	 * @param Initial reading position
	 * @param Number of elements to read
	 */
	
	public void fromXDR(byte[] buffer, int idx, int len) throws XDRException{
		fromXDR(buffer, idx, len, null);
	}
	
	/**
	 * Decodes an XDR array
	 * 
	 * @param XDR information
	 * @param Initial reading position
	 * @param Number of elements to read
	 * @throws XDRException 
	 */
	
	public void fromXDR(byte[] buffer, int idx, int len, Integer count) throws XDRException{
		//XXX: El uso de len es erroneo, ya que se pasa siempre buffer.length y no el tama–o a leer, as’ que no tiene sentido comprobar nada ...
//		if ( len > buffer.length ) {
//			throw new XDRException("Invalid parameter 'len'. Array out of bounds.");
//		} else if ( len < idx+buffer.length ) {
//			throw new XDRException("Unable to decode "+getClass().getName()+". No enough bytes in buffer.");
//		}
		if ( count == null ) {
			XDRInt xdrcount = new XDRInt(buffer, idx, len);
			idx += xdrcount.getBufferLength();
			count = new Integer(xdrcount.getValue());
		}
		for ( int i = 0; i < count; i++ ) {
			T dec;
			try {
				//TODO: Ojo que esto puede petar si classType no funciona como esperamos pq ser’a null, pero entonces no tendr’a sentido la parametrizaci—n ...
				dec = classType.newInstance();
				dec.fromXDR(buffer, idx, len);
				idx += dec.getBufferLength();
				this.add(dec);
			} catch (InstantiationException e) {
				throw new XDRException(e);
			} catch (IllegalAccessException e) {
				throw new XDRException(e);
			}
		}
	}
	
	/**
	 * Returns object size
	 * 
	 * @return Object size
	 */
	
	public int getBufferLength(){
		if ( this.size() == 0 )
			return 0;
		
		XDRObject obj = this.get(0);
		return obj.getBufferLength()*this.size();

//		int len = 0;
		
		
//		if(obj instanceof Integer){
//			len = new XDRInt((Integer)obj).getBufferLength();
//		}
//		else if(obj instanceof Byte){
//			len = new XDRByte((Byte)obj).getBufferLength();
//		}
//		else if(obj instanceof Short){
//			len = new XDRShort((Short)obj).getBufferLength();
//		}
//		else if(obj instanceof Long){
//			len = new XDRLong((Long)obj).getBufferLength();
//		}
//		else if(obj instanceof Float){
//			len = new XDRFloat((Float)obj).getBufferLength();
//		}
//		else if(obj instanceof Double){
//			len = new XDRDouble((Double)obj).getBufferLength();
//		}
//		else if(obj instanceof String){
//			len = new XDRString((String)obj).getBufferLength();
//		}
//		else if(obj instanceof Boolean){
//			len = new XDRBoolean((Boolean)obj).getBufferLength();
//		}
		//TODO: obj instanceof ArrayList
		/*
		else if(obj instanceof ArrayList){
			ArrayList objeto = new ArrayList();
			objeto.add(obj);
			len = new XDRArray(objeto).getBufferLength();
		}
		*/
//		System.out.println(len*this.size());
//		return len*this.size();
	}
	
	/**
	 * Returns object value
	 * 
	 * @return Object value
	 */
	
	
	public ArrayList<Object> getValue(){
		ArrayList<Object> ret = new ArrayList<Object>();
		
		for(int i = 0; i < this.size(); i++){
			XDRObject obj = this.get(i);
			Object o = obj.getEnclosedValue();
			ret.add(o);
		}
		return ret;
	}

	
	public Object getEnclosedValue() {
		return this.getValue();
	}
}
