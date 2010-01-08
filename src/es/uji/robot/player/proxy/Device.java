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
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import es.uji.robot.player.generated.abstractproxy.CommonDefines;
import es.uji.robot.player.generated.xdr.PlayerDevaddr;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.xdr.XDRObject;

public abstract class Device implements CommonDefines{
	
	/**
	 *  Pointer to the client proxy. 
	 */
	private Client client;
	/** 
	 * Device address 
	 */
	private PlayerDevaddr address;
	
	/** The driver name. */
	private String drivername;
	
	/** The subscribe flag is non-zero if the device has been
    successfully subscribed (read-only). */
	private boolean subscribed;

	/** Data timestamp, i.e., the time at which the data was generated (s). */
	private double datatime;

	/** Data timestamp from the previous data. */
	private double lasttime;

	/** Freshness flag.  Set to 1 whenever data is dispatched to this
    proxy.  Useful with the multi-client, but the user must manually
    set it to 0 after using the data. */
	private int fresh;
	
	/** Freshness flag.  Set to 1 whenever data is dispatched to this
    proxy.  Useful with the multi-client, but the user must manually
    set it to 0 after using the data. */
	private int freshgeom;
	
	/** Freshness flag.  Set to 1 whenever data is dispatched to this
    proxy.  Useful with the multi-client, but the user must manually
    set it to 0 after using the data. */
	private int freshconfig;
	

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public PlayerDevaddr getAddress() {
		return address;
	}

	public void setAddress(PlayerDevaddr address) {
		this.address = address;
	}

	public String getDrivername() {
		return drivername;
	}

	public void setDrivername(String drivername) {
		this.drivername = drivername;
	}

	public boolean getSubscribed() {
		return subscribed;
	}

	public void setSubscribed(boolean subscribed) {
		this.subscribed = subscribed;
	}

	public double getDatatime() {
		return datatime;
	}

	public void setDatatime(double datatime) {
		this.datatime = datatime;
	}

	public double getLasttime() {
		return lasttime;
	}

	public void setLasttime(double lasttime) {
		this.lasttime = lasttime;
	}

	public int getFresh() {
		return fresh;
	}

	public void setFresh(int fresh) {
		this.fresh = fresh;
	}

	public int getFreshgeom() {
		return freshgeom;
	}

	public void setFreshgeom(int freshgeom) {
		this.freshgeom = freshgeom;
	}

	public int getFreshconfig() {
		return freshconfig;
	}

	public void setFreshconfig(int freshconfig) {
		this.freshconfig = freshconfig;
	}
	
	/** @brief Initialise the device. @internal */
	public void init(Client client, int code, int index){
		this.client = client;
		//XXX: Inicializado address
		this.address = new PlayerDevaddr();
		this.address.setHost(0);
		this.address.setRobot(client.getClientConnection().getPort());
		this.address.setInterf((short)code);
		this.address.setIndex((short)index);
		this.subscribed = false;
		
		if(this.client != null){
			this.client.addDevice(this);
		}
	}
	
	/** @throws ClientException 
	 * @brief Finalize the device. @internal */
	public void term() throws ClientException{
		if(client != null){
			client.deleteDevice(this);
		}
	}
	
	/** @throws ProxyException 
	 * @throws ClientException 
	 * @brief Subscribe the device. @internal */
	public void subscribe(int access) throws ProxyException, ClientException{
		this.drivername = client.subscribe(this.address.getInterf(), this.address.getIndex(), access);
		subscribed = true;
	}
	
	/** @throws ProxyException 
	 * @throws ClientException 
	 * @brief Unsubscribe the device. @internal */
	public void unsubscribe() throws ProxyException, ClientException{
		subscribed = false;
		client.unsubscribe(this.address.getInterf(), this.address.getIndex());
		
	}
//	
//	/** @throws ClientException 
//	 * @brief Request capabilities of device */
//	public PlayerCapabilitiesReq hasCapability(int type, int subtype) throws ClientException{
//		PlayerCapabilitiesReq request = new PlayerCapabilitiesReq();
//		
//		request.setType(type);
//		request.setSubtype(subtype);
//		
//		return (PlayerCapabilitiesReq)client.request(this, PLAYER_CAPABILTIES_REQ, request);
//	}
//	
//	/** @throws ClientException 
//	 * @brief Request an integer property */
//	public int retrieveIntegerProperty(ByteArrayOutputStream property) throws ClientException{
//		PlayerIntpropReq request = new PlayerIntpropReq();
//		PlayerIntpropReq response = new PlayerIntpropReq();
//		
//		request.setKey(property);
//		request.setValue(0);
//		
//		response = (PlayerIntpropReq)client.request(this, PLAYER_GET_INTPROP_REQ, request);
//		
//		return response.getValue();
//		
//	}
//	
//	/** @throws ClientException 
//	 * @brief Set an integer property */
//	public void setIntegerProperty(ByteArrayOutputStream property, int value) throws ClientException{
//		PlayerIntpropReq request = new PlayerIntpropReq();
//		
//		request.setKey(property);
//		request.setValue(value);
//		
//		client.write(this, PLAYER_SET_INTPROP_REQ, request);
//	}
//	
//	/** @throws ClientException 
//	 * @brief Request a double property */
//	public double retrieveDoubleProperty(ByteArrayOutputStream property) throws ClientException{
//		PlayerDblpropReq request = new PlayerDblpropReq();
//		PlayerDblpropReq response = new PlayerDblpropReq();
//		
//		request.setKey(property);
//		request.setValue(0);
//		
//		response = (PlayerDblpropReq)client.request(this, PLAYER_GET_DBLPROP_REQ, request);
//		
//		return response.getValue();
//	}
//	
//	
//	/** @throws ClientException 
//	 * @brief Set a double property */
//	public void setDoubleProperty(ByteArrayOutputStream property, double value) throws ClientException{
//		PlayerDblpropReq request = new PlayerDblpropReq();
//		
//		request.setKey(property);
//		request.setValue(value);
//		
//		client.write(this, PLAYER_SET_DBLPROP_REQ, request);
//	}
//	
//	/** @throws ClientException 
//	 * @brief Request a string property */
//	public ByteArrayOutputStream retrieveStringProperty(ByteArrayOutputStream property) throws ClientException{
//		PlayerStrpropReq request = new PlayerStrpropReq();
//		PlayerStrpropReq response = new PlayerStrpropReq();
//		
//		request.setKey(property);
//		request.setValue(null);
//		
//		response = (PlayerStrpropReq)client.request(this, PLAYER_GET_STRPROP_REQ, request);
//		
//		return response.getKey();
//	}
//	
//	/** @throws ClientException 
//	 * @brief Set a string property */
//	public void setStringProperty(ByteArrayOutputStream property, ByteArrayOutputStream value) throws ClientException{
//		PlayerStrpropReq request = new PlayerStrpropReq();
//		
//		request.setKey(property);
//		request.setValue(value);
//		
//		client.write(this, PLAYER_SET_STRPROP_REQ, request);
//	}
	
	public abstract void putMsg(PlayerMsghdr header, XDRObject data);
	
	public static interface Callback{
		public Object callback(Object o);
	}
	
	List<Callback> callbacks = new Vector<Callback>();
	Hashtable<Callback, Object> callbackParameters = new Hashtable<Callback, Object>();
	
	public void callCallbacks() {
		synchronized (callbacks) {
			for (int i = 0; i < callbacks.size(); i++) {
				Callback callback = callbacks.get(i);
				callback.callback(callbackParameters.get(callback));
			}
		}
	}
	
	public void addCallback(Callback callback, Object parameters) {
		synchronized (callbacks) {
			callbacks.add(callback);
			callbackParameters.put(callback, parameters);
		}
	}
	
	public void deleteCallback(Callback callback) {
		synchronized (callbacks) {
			if ( callbacks.contains(callback) ) { 
				callbacks.remove(callback);
				callbackParameters.remove(callback);
			}
		}
	}
	
	public void clearCallbacks() {
		synchronized (callbacks) {
			callbacks.clear();
			callbackParameters.clear();
		}
	}
	
}