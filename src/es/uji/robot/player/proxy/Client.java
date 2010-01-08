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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import sun.misc.Cleaner;

import es.uji.robot.player.connection.ConnectionManager;
import es.uji.robot.player.generated.abstractproxy.AbstractClient;
import es.uji.robot.player.generated.structure.PlayerClientItem;
import es.uji.robot.player.generated.structure.PlayerDeviceInfo;
import es.uji.robot.player.generated.value.PlayerDevaddrValue;
import es.uji.robot.player.generated.xdr.PlayerAddReplaceRuleReq;
import es.uji.robot.player.generated.xdr.PlayerDevaddr;
import es.uji.robot.player.generated.xdr.PlayerDeviceDatamodeReq;
import es.uji.robot.player.generated.xdr.PlayerDeviceDevlist;
import es.uji.robot.player.generated.xdr.PlayerDeviceDriverinfo;
import es.uji.robot.player.generated.xdr.PlayerDeviceReq;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.xdr.XDRException;
import es.uji.robot.xdr.XDRObject;

public class Client extends AbstractClient{
		
	protected static final int PLAYERXDR_MSGHDR_SIZE = 40;
	protected static final int PLAYER_MAX_MESSAGE_SIZE = 8388608;
	protected static final int PLAYERXDR_MAX_MESSAGE_SIZE = (4*PLAYER_MAX_MESSAGE_SIZE);
	
	protected static final int PLAYERC_QUEUE_RING_SIZE = 512;
	
	private ConnectionManager clientConnection;
	
//	private int id;
	
	/** How many messages were lost on the server due to overflows, incremented by player, cleared by user. */
	private int overflowCount;
	/** @internal Data delivery mode */
	private char mode;

	/** @internal Data request flag; if mode == PLAYER_DATAMODE_PULL, have we
	 * requested and not yet received a round of data? */
	private boolean dataRequested;

	/** @internal Data request flag; if mode == PLAYER_DATAMODE_PULL, have we
	 * received any data in this round? */
	private boolean dataReceived;


	/** List of available (but not necessarily subscribed) devices.
    This list is filled in by retrieveDeviceList(). */
	//TODO: This can be removed using data from "devices" instead.
	private ArrayList<PlayerDeviceInfo> deviceInfos;

	/** List of subscribed devices */
	private ArrayList<Device> devices;
	
	private Hashtable<short[], Device> devicesHashtable; 
	
//	public int getId() {
//		return id;
//	}
//
//	public void setId(int id) {
//		this.id = id;
//	}

	/** @internal A circular queue used to buffer incoming data packets. */
	private List<PlayerClientItem> qitems;
	private int qfirst, qlen;

//	  /** @internal Temp buffers for incoming / outgoing packets. */
//	private ByteArrayOutputStream data;

	/** infinite loop control variable to avoid deadlocks */
	private boolean closing = false;
	
	public int getQfirst() {
		return qfirst;
	}

	public void setQfirst(int qfirst) {
		this.qfirst = qfirst;
	}

	public int getQlen() {
		return qlen;
	}

	public void setQlen(int qlen) {
		this.qlen = qlen;
	}

//	public ByteArrayOutputStream getData() {
//		return data;
//	}
//
//	public void setData(ByteArrayOutputStream data) {
//		this.data = data;
//	}

	public ConnectionManager getClientConnection(){
		return clientConnection;
	}
	
	public void setClientConnection(ConnectionManager clientConnection){
		this.clientConnection = clientConnection;
	}

	public int getOverflowCount() {
		return overflowCount;
	}

	public void setOverflowCount(int overflowCount) {
		this.overflowCount = overflowCount;
	}

	public char getMode() {
		return mode;
	}

	public void setMode(char mode) {
		this.mode = mode;
	}

	public boolean isDataRequested() {
		return dataRequested;
	}

	public void setDataRequested(boolean dataRequested) {
		this.dataRequested = dataRequested;
	}

	public boolean isDataReceived() {
		return dataReceived;
	}

	public void setDataReceived(boolean dataReceived) {
		this.dataReceived = dataReceived;
	}

	public ArrayList<PlayerDeviceInfo> getDeviceInfos() {
		return deviceInfos;
	}

	public void setDeviceInfos(ArrayList<PlayerDeviceInfo> deviceInfos) {
		this.deviceInfos = deviceInfos;
	}
	
	public ArrayList<Device> getDevices() throws ClientException{
		if ( devices.size() == 0 && clientConnection.isConnected() ) {
			PlayerDeviceDevlist devlist = (PlayerDeviceDevlist)request(null, PLAYER_PLAYER_REQ_DEVLIST, null);
			List<? extends PlayerDevaddrValue> devs = devlist.getDevices();
			for ( int i = 0; i < devs.size(); i++) {
				PlayerDeviceInfo devinfo = new PlayerDeviceInfo();
				devinfo.setAddr((PlayerDevaddr)devs.get(i));
				//deviceInfos.add(devinfo);
				
				PlayerDeviceDriverinfo req = new PlayerDeviceDriverinfo();
				req.setAddr(devs.get(i));
				try {
					PlayerDeviceDriverinfo driver = (PlayerDeviceDriverinfo)request(null, PLAYER_PLAYER_REQ_DRIVERINFO, req);
					devinfo.setDrivername(driver.getDriverName());
					deviceInfos.add(devinfo);
					System.out.println("driverName "+driver.getDriverName());
				} catch (ClientException e) {
					System.out.println("eeee "+e.toString());
					//XXX: Super chapuza temporal!
					if ( e.getMessage().contains("NACK") )
						continue;
					else
						throw e;
				}
			}
			
		}
		return devices;
	}
	
	public Client(String host, int port){
		clientConnection = new ConnectionManager(host, port);
		
		deviceInfos = new ArrayList<PlayerDeviceInfo>();
		devices = new ArrayList<Device>();
		devicesHashtable = new Hashtable<short[], Device>();
		
		//TODO: How to initialize the id ?
		
		this.qitems = new Vector<PlayerClientItem>();
		qfirst = 0;
		qlen = 0;
		
		Timer timer = new Timer("ConnectionPullingThread", true);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
			}
		};
		
		long delay = 0;
		long period = 10; //10 ms
		//timer.scheduleAtFixedRate(task, delay, period);
	}
	
	
	/** @brief Set the transport type.

	@param transport Either PLAYERC_TRANSPORT_UDP or PLAYERC_TRANSPORT_TCP
	*/
	public void setTransport(int transport){
		clientConnection.setTransport(transport);
	}
	
	/** @throws IOException 
	 * @throws UnknownHostException 
	 * @brief Connect to the server.
	*/
	public void connect() throws UnknownHostException, IOException{
		clientConnection.connect();
		clientConnection.setRequestTimeout(10000);
		byte[] banner = clientConnection.receive(PLAYER_IDENT_STRLEN, true);
		
		System.out.println(new String(banner)+" connected on host "+clientConnection.getHost()+" and port "+clientConnection.getPort());
	}
	
	/**
	 * Disconnect from the server, with potential retry
	 */
	public void disconnectRetry(){
		
	}
	
	/** @throws IOException 
	 * @brief Disconnect from the server.
	*/
	
	public void disconnect() throws IOException{
		clientConnection.disconnect();
	}
	
	/** @brief Set a replace rule for the client queue on the server

	If a rule with the same pattern already exists, it will be replaced with
	the new rule (i.e., its setting to replace will be updated).

	@param interf Interface to set replace rule for (-1 for wildcard)

	@param index Index to set replace rule for (-1 for wildcard)

	@param type Type to set replace rule for (-1 for wildcard),
	i.e. PLAYER_MSGTYPE_DATA

	@param subtype Message subtype to set replace rule for (-1 for wildcard)

	@param replace Should we replace these messages
	 * @throws ClientException 

	@returns Returns response from request petition to client.

	*/
	
	public PlayerAddReplaceRuleReq sendReplaceRule(int interf, int index, int type, int subtype, int replace) throws ClientException{
		PlayerAddReplaceRuleReq request = new PlayerAddReplaceRuleReq();
		
		request.setInterf(interf);
		request.setIndex(index);
		request.setType(type);
		request.setSubtype(subtype);
		request.setReplace(replace);
		
		return (PlayerAddReplaceRuleReq)this.request(null, PLAYER_PLAYER_REQ_ADD_REPLACE_RULE, request);
		
	}
	
	/** @brief Change the server's data delivery mode.

	Be sure to @ref libplayerc_datamodes "read about data modes" before using this function.

	@param mode Data delivery mode; must be one of
	PLAYERC_DATAMODE_PUSH, PLAYERC_DATAMODE_PULL; the defalt mode is
	PLAYERC_DATAMODE_PUSH.
	 * @throws ClientException 

	@returns Returns response from request petition to client.

	*/
	
	public PlayerDeviceDatamodeReq datamode(char mode) throws ClientException{
		PlayerDeviceDatamodeReq request = new PlayerDeviceDatamodeReq();
		
		request.setMode(mode);
		this.mode = mode;
		
		return (PlayerDeviceDatamodeReq)this.request(null, PLAYER_PLAYER_REQ_DATAMODE, request);
	}
	
	
	/** @brief Request a round of data.

	Request a round of data; only valid when in a request/reply (aka PULL)
	data delivery mode.  But you don't need to call this function, because @ref
	read() will do it for you if the client is in a PULL mode.
	
	@throws ClientException

	*/
	
	public void requestData() throws ClientException {
		if ( mode != PLAYER_DATAMODE_PULL) {
			throw new ClientException("Not in PULL mode");
		} else if (dataRequested) {
			//TODO: include a proper logger
			System.err.println("WARNING :: Data already requested");
		} else {
			this.request(null, PLAYER_PLAYER_REQ_DATA, null);
			dataRequested = true;
			dataReceived = false;
		}
	}
	
	/** @brief Test to see if there is pending data. Send a data request if one has not been sent already.
	 * A data request is necessary to provoke a response from the server.

	@param timeout Timeout value (ms).  Set timeout to 0 to check for
	currently queued data.
	 * @throws ClientException 

	@returns Returns -1 on error, 0 or 1 otherwise.

	*/
	/*public int peek(int timeout) throws ClientException{
		// First check the message queue
		if( qlen > 0 ){
			return 1;
		}
		
		// In case we're in PULL mode, first request a round of data.
		requestData();
		
		return internalPeek(timeout);
		
	}*/
	
	/** @brief Test to see if there is pending data. Don't send a request for data.
	 * This function is reliant on a call being made elsewhere to request data from
	 * the server.

	@param timeout Timeout value (ms).  Set timeout to 0 to check for
	currently queued data.

	@returns Returns -1 on error, 0 or 1 otherwise.

	*/
	/*public int internalPeek(int timeout){
		//TODO: Method used to obtain the number of data to be read.
	}*/
	
	
	
	/** @brief Read data from the server (blocking).

	@returns PUSH mode: For data packets, will return the ID of the proxy that got
	the data; for synch packets, will return the ID of the client itself;
	on error, will return NULL.
	PULL mode: Will return NULL on error, the ID of the client on success. Will
	never return the ID of a proxy other than the client.

	*/
	public Device read() throws ClientException {
		while (!closing) {
			//TODO: test if read is called even in PULL or PUSH mode as requestData will throw an exception if in PUSH mode
			this.requestData();
			Device dev = this.readNonBlockWithProxy();
			if ( dev != null )
				return dev;
			else{
				try {
					Thread.sleep(10); // 10ms
				} catch (InterruptedException e) {
					throw new ClientException(e);
				}
			}
		}
		return null;
	}
	
	
	
	/**
	 * Read and process a packet (nonblocking), fills in pointer to proxy that got data
	 * returns "null" if no data recieved, "id" if data recieved
	 */
	
	public Device readNonBlockWithProxy() throws ClientException {
		while(!closing){
			//See if there is any queued data
			PlayerClientItem packet = this.pop();
			if ( packet == null ){
				// If there is no queued data, peek at the socket
				packet = this.readPacket();
			}
			
			// One way or another, we got a new packet into (header,client->data),
		    // so process it
			
			PlayerMsghdr header = packet.getHeader();
			
			if(header.getType() == PLAYER_MSGTYPE_RESP_ACK){
				System.err.println("WARNING :: Discarding unclaimed ACK");
			}
			
			else if( header.getType() == PLAYER_MSGTYPE_SYNCH){
				dataRequested = false;
				//XXX: Esto es un parche!
				int PLAYER_PLAYER_SYNCH_OVERFLOW = 110;
				if( header.getSubtype() == PLAYER_PLAYER_SYNCH_OVERFLOW){
					//TODO: Test if it is ok.
					DataInputStream dis = new DataInputStream(new ByteArrayInputStream(packet.getData().toByteArray()));
					try {
						overflowCount += dis.readInt();
					} catch (IOException e) {
						throw new ClientException(e);
					}
				}
				if( !dataReceived ){
					System.err.println("WARNING :: No data recieved with SYNC");
				}
				else{
					return this;
				}
			}
			
			else if ( header.getType() == PLAYER_MSGTYPE_DATA ){
				//XXX: Deshabilitado temporalmente
//				clientConnection.setLastTime(clientConnection.getDataTime());
//				clientConnection.setDataTime(header.getTimestamp());
				if( mode == PLAYER_DATAMODE_PUSH){
					return this.dispatch(packet);
				}
				else{ // PULL mode
					Device result = this.dispatch(packet);
					
					if ( result == null){
						System.err.println("WARNING :: Failed to dispatch data message: subtype" + header.getSubtype());
						System.err.println("address: " +
								header.getAddr().getHost() + ":" +
								header.getAddr().getRobot() + ":" + 
								header.getAddr().getInterf() + ":" +
								header.getAddr().getIndex() + "\nsize:" +
								header.getSize());
						throw new ClientException("Failed to dispatch data message.");
					}
					else{
						return result;
					}
				}
			}
			
			else{
				System.err.println("Unexpected message type [" + header.getType() + "]");
				System.err.println("address: " +
						header.getAddr().getHost() + ":" +
						header.getAddr().getRobot() + ":" + 
						header.getAddr().getInterf() + ":" +
						header.getAddr().getIndex() + "\nsize:" +
						header.getSize());
				throw new ClientException("Unexpected message type.");
			}
			
		}
		return null;
	}
	
	
	
	/**
	 * Write data to the server
	 * @param Information of the device
	 * @param Header subtype
	 * @param Data to be written
	 * @throws ClientException 
	 */
	
	public void write(Device info, int subtype, XDRObject command) throws ClientException{
		this.write(info, subtype, command, null);
	}
	
	/**
	 * Write data to the server
	 * @param Information of the device
	 * @param Header subtype
	 * @param Data to be written
	 * @param timestamp
	 * @throws ClientException 
	 */
	
	
	public void write(Device info, int subtype, XDRObject command, Long timestamp) throws ClientException{
		PlayerMsghdr header = new PlayerMsghdr();
		
		header.setAddr(info.getAddress());
		header.setType((char)PLAYER_MSGTYPE_CMD);
		header.setSubtype((char)subtype);
		if ( timestamp != null )
			header.setTimestamp( timestamp );
		
		writePacket(header, command);
	}
	
	/** @brief Issue a request to the server and await a reply (blocking). @internal

	The response pointer is filled with a pointer to the response data received. 

	If an error is returned then no data will have been stored in response.

	@returns Returns XDRObject requested.

	*/
	
	public XDRObject request(Device info, int subtype, XDRObject command) throws ClientException {
		PlayerClientItem response;
		PlayerMsghdr requestHeader = new PlayerMsghdr();
		
		if(info == null){
			PlayerDevaddr addr = new PlayerDevaddr();
			addr.setInterf((short)PLAYER_PLAYER_CODE);
			
			addr.setHost(0);
			addr.setRobot(0);
			addr.setIndex((short)0);
			
			requestHeader.setAddr(addr);
		}
		else{
			requestHeader.setAddr(info.getAddress());
			
		}
		requestHeader.setType((char)PLAYER_MSGTYPE_REQ);
		requestHeader.setSubtype((char)subtype);
		
		writePacket(requestHeader, command);
		
		double t = clientConnection.getRequestTimeout();
		
		// Read packets until we get a reply.  Data packets get queued up
		// for later processing.
		while( t > 0 ){
			long last = System.currentTimeMillis();
			
			// Peek at the socket
			//internalPeek(10);
			
			// There's data on the socket, so read a packet (blocking).
			response = readPacket();
			
			long current = System.currentTimeMillis();
			
			t -= current - last;
			
			if( response.getHeader().getType() == PLAYER_MSGTYPE_DATA || response.getHeader().getType() == PLAYER_MSGTYPE_SYNCH ){
				// Queue up any incoming data and sync packets for later processing
				push(response);
			} else if( response.getHeader().getType() == PLAYER_MSGTYPE_RESP_ACK){
				// Using TCP, we only need to check the interface and index
				if ( response.getHeader().getAddr().getInterf() != requestHeader.getAddr().getInterf()  || 
						response.getHeader().getAddr().getIndex() != requestHeader.getAddr().getIndex() ||
						response.getHeader().getSubtype() != requestHeader.getSubtype()){
					throw new ClientException("got the wrong kind of reply (not good).");
				}
				if( response.getHeader().getSize() > 0){
					XDRObject obj = decodeXDRObjectResponse(response);
					if ( obj != null )
						return obj;
					else {
						System.out.println("response="+response);
						return null;
					}
				}
			} else if( response.getHeader().getType() == PLAYER_MSGTYPE_RESP_NACK){
				// Using TCP, we only need to check the interface and index
				if ( response.getHeader().getAddr().getInterf() != requestHeader.getAddr().getInterf()  || 
						response.getHeader().getAddr().getIndex() != requestHeader.getAddr().getIndex() ||
						response.getHeader().getSubtype() != requestHeader.getSubtype()){
					throw new ClientException("got the wrong kind of reply (not good)");
				} else {
					throw new ClientException("got NACK from request");
				}
			} else {
				throw new ClientException("timed out waiting for server reply to request "+
						requestHeader.getAddr().getInterf()+":"+
						requestHeader.getAddr().getIndex()+":"+
						requestHeader.getType()+":"+
						requestHeader.getSubtype()+":");
			}
		}
		return null;
	}

	/**
	 * Add a device proxy
	 * @param device
	 */
	
	public void addDevice(Device device){
		device.setFresh(0);
		devices.add(device);
		devicesHashtable.put(new short[]{device.getAddress().getInterf(), device.getAddress().getIndex()}, device);
	}
	
	/**
	 * Delete a device proxy
	 * @param device
	 * @throws ClientException
	 */
	
	public void deleteDevice(Device device) throws ClientException{
		if(devices.contains(device)){
			devices.remove(device);
			devicesHashtable.remove(new short[]{device.getAddress().getInterf(), device.getAddress().getIndex()});
		}
		else{
			System.err.println("ERROR :: Unknown device.");
			throw new ClientException("Unkown device.");
		}
	}
	
	public Device getDevice(short interf, short index) {
		if ( devicesHashtable.containsKey(new short[]{interf, index}) ) {
			return devicesHashtable.get(new short[]{interf, index});
		} else {
			System.err.println("WARNING :: No device for interf:"+interf+" index:"+index+".");
			return null;
		}
	}
	
	/** @brief Get the list of available device ids.

	This function queries the server for the list of available devices,
	and write result to the devinfos list in the client object.

	 * @throws ClientException 

	*/
	public void retrieveDeviceList() throws ClientException{
		PlayerDeviceDevlist response;
		
		response = (PlayerDeviceDevlist)this.request(null, PLAYER_PLAYER_REQ_DEVLIST, null);
		
		Iterator<? extends PlayerDevaddrValue> it = response.getDevices().iterator();
		while(it.hasNext()){
			PlayerDeviceInfo devinfo = new PlayerDeviceInfo();
			devinfo.setAddr((PlayerDevaddr)it.next());
			deviceInfos.add(devinfo);
		}
		
		this.retrieveDriverInfo();
	}
	
	/**
	 * Get the driver info for all devices.  The data is written into the
	 * proxy structure rather than returned to the caller.
	 * @throws ClientException 
	 */
	private void retrieveDriverInfo() throws ClientException{
		PlayerDeviceDriverinfo request = new PlayerDeviceDriverinfo();
		PlayerDeviceDriverinfo response;
		
		
		for( int i = 0; i<deviceInfos.size(); i++){
			request.setAddr(deviceInfos.get(i).getAddr());
			
			response = (PlayerDeviceDriverinfo)this.request(null, PLAYER_PLAYER_REQ_DRIVERINFO, request);
			
			deviceInfos.get(i).setDrivername(response.getDriverName());
		}
	}
	
	/**  
	 * @brief Subscribe a device. @internal
	 * @throws ClientException
	 */
	public String subscribe(int code, int index, int access) throws ClientException{
		PlayerDeviceReq request = new PlayerDeviceReq();
		PlayerDeviceReq response = new PlayerDeviceReq();
		
		PlayerDevaddr addr = new PlayerDevaddr();
		addr.setHost(0);
		addr.setRobot(0);
		addr.setInterf((short)code);
		addr.setIndex((short)index);		
		
		request.setAddr(addr);
		request.setAccess((char)access);
		
		response = (PlayerDeviceReq)this.request(null, PLAYER_PLAYER_REQ_DEV, request);
		
		if ( request.getAccess() != access){
			throw new ClientException("Requested " + access + " access, but got " + response.getAccess() + " access");
		}
			
		return response.getDriverName();
		
	}
	
	/** 
	 * @brief Unsubscribe a device. @internal
	 *  @throws ClientException
	 */
	
	public void unsubscribe(int code, int index) throws ClientException{
		PlayerDeviceReq request = new PlayerDeviceReq();
		PlayerDeviceReq response = new PlayerDeviceReq();
		
		PlayerDevaddr addr = new PlayerDevaddr();
		
		addr.setHost(0);
		addr.setRobot(0);
		addr.setInterf((short)code);
		addr.setInterf((short)index);
		request.setAddr(addr);
		request.setAccess((char)PLAYER_CLOSE_MODE);
		
		response = (PlayerDeviceReq)this.request(null, PLAYER_PLAYER_REQ_DEV, request);
		
		if ( response.getAccess() != PLAYER_CLOSE_MODE){
			throw new ClientException("Requested " + PLAYER_CLOSE_MODE + " access, but got " + response.getAccess() + " access");
		}
		
		
	}
	
	protected PlayerClientItem readPacket() throws ClientException{
		if ( clientConnection == null) {
			throw new ClientException("Unavailable clientConnection.");
		}
		
		//XXX: Method implemented in client.c at line 1060
		//XXX: First we test if there is a socket to read
		//XXX: Then we have to retrieve the header and unpack it with timeout
		//XXX: Then we have to obtain the body and unpack it 
		//XXX: Finally we have to return the data read
		try {
			byte[] header = clientConnection.receive(PLAYERXDR_MSGHDR_SIZE, true);
			
			PlayerMsghdr pheader = new PlayerMsghdr(header,0,header.length);
			
			byte[] message = clientConnection.receive(pheader.getSize(), true);
			
			PlayerClientItem item = new PlayerClientItem();
			item.setHeader(pheader);
			if ( message != null )
				item.getData().write(message);
			
			return item;
		} catch (IOException e) {
			throw new ClientException(e);
		} catch (XDRException e) {
			throw new ClientException(e);
		}
	}

	protected void writePacket(PlayerMsghdr header, XDRObject data) throws ClientException {
		if ( clientConnection == null) {
			throw new ClientException("Unavailable clientConnection.");
		}
		if ( header == null ) {
			throw new ClientException("Illegal arguments passed. Must not be null.");
		}
		
		if(clientConnection != null){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] dataBytes;
			try {
				if ( data != null )
					dataBytes = data.toXDR();
				else
					dataBytes = new byte[]{};
			} catch (XDRException e) {
				throw new ClientException(e);
			} catch (IOException e) {
				throw new ClientException(e);
			}
			
			// Write in the encoded size and current time
			header.setSize(dataBytes.length);
			if ( header.getTimestamp() == 0.0 )
				header.setTimestamp( (System.nanoTime() / 1000) );  //us
			//header.setTimestamp( System.nanoTime() / 1000 ); //already set in write method
			
			try{
				baos.write(header.toXDR());
				baos.write(dataBytes);
			} catch (XDRException e){
				throw new ClientException(e);
			} catch (IOException e){
				throw new ClientException(e);
			}
			
			try {
				clientConnection.send(baos.toByteArray());
			} catch (IOException e) {
				throw new ClientException(e);
			}
		}
	}
	
	
	public PlayerClientItem pop(){
		synchronized (qitems) {
			PlayerClientItem response;
			if (qlen > 0) {
				response = qitems.get(qfirst);
				qfirst = (qfirst + 1) % PLAYERC_QUEUE_RING_SIZE;
				qlen--;
			} else {
				response = null;
			}
			
			return response; 
		}
	}
	
	
	public void push(PlayerClientItem item) throws QueueOverflowException {
		synchronized (qitems) {
			if(qlen == PLAYERC_QUEUE_RING_SIZE){
				throw new QueueOverflowException("queue overflow; discarding packets");
				//TODO examinar si es correcto no hacer esto, o si hacerlo, o lanzar o no lanzar la excepci—n de arriba
				//qfirst = (qfirst + 1) % PLAYERC_QUEUE_RING_SIZE;
				//qlen = -1;
			}
			//qitems.set( ((qfirst + qlen) % PLAYERC_QUEUE_RING_SIZE), item);
			qitems.add( ((qfirst + qlen) % PLAYERC_QUEUE_RING_SIZE), item);
			qlen++;
		}
	}
	
	
	
	public Device dispatch(PlayerClientItem packet) throws ClientException{
		Device device;
		
		// Look for a device proxy to handle this data
//		for(int i = 0; i < devices.size(); i++){
//			device = devices.get(i);
//			
//			if(device.getAddress().getInterf() == header.getAddr().getInterf() && device.getAddress().getIndex() == header.getAddr().getIndex()){
//				// Fill out timing info
//				device.setLasttime(device.getDatatime());
//				device.setDatatime(header.getTimestamp());
//				
//				//TODO: Call the registerd handler for this device
//				//XXX: How to call the registered handler for the device ??
//				//XXX: Implemented in method dispatch in the file client.c at line 1331
//				ESTE ES EL Q LLAMA AL PUTMSG DE LOS PROXIES ...
//			}
//		}
		
		PlayerMsghdr header = packet.getHeader();
		device = getDevice(header.getAddr().getInterf(), header.getAddr().getIndex());
		
		if ( device != null ) {
			device.setLasttime(device.getDatatime());
			device.setDatatime(header.getTimestamp());
			
			//call the registered handler for this device
			XDRObject data = decodeXDRObjectResponse(packet);
			device.putMsg(header, data);
			
			// mark as fresh
			device.setFresh(1);
			
			// call any additional registered callback
			device.callCallbacks();
			
			return device;
		}
		return null;
	}
	
	//XXX: Method timed_recv, file client.c at line 102
	//XXX: this method performs a select before the read so we can have a timeout
	//XXX: this stops the client hanging forever if the target disappears from the network
	
	/** @brief Set the timeout for client requests.
	@param milliseconds Milliseconds to wait for a reply.
	*/
	
	public void sendRequestTimeout(int milliseconds){
		clientConnection.setRequestTimeout(milliseconds);
	}
	
	//XXX: Deshabilitado temporalmente
//	/** @brief Set the connection retry limit.
//	@param limit The number of times to attempt to reconnect to the server.  Give -1 for
//	       infinite retry.
//	*/
//	
//	public void setRetryLimit(int limit){
//		clientConnection.setRetryLimit(limit);
//	}
//	
//	/** @brief Set the connection retry sleep time.
//	@param time The amount of time, in seconds, to sleep between reconnection attempts.
//	*/
//	
//	public void setRetryTime(double time){
//		clientConnection.setRetryTime((int)time);
//	}
	
	private XDRObject getXDRObject(PlayerClientItem response) throws ClientException {
		short iface = response.getHeader().getAddr().getInterf();
		char type = response.getHeader().getType();
		char subtype = response.getHeader().getSubtype();
		return getXDRObject(iface, type, subtype);
	}
	
	private XDRObject getXDRObject(short iface, char type, char subtype) throws ClientException {
		MyKey key = new MyKey(iface,type,subtype);
		XDRObject objTemplate = xdrObjectTable.get(key);
		if ( objTemplate == null && 
			(type == PLAYER_MSGTYPE_RESP_ACK || type == PLAYER_MSGTYPE_RESP_NACK)) {
			key.setType((char)PLAYER_MSGTYPE_REQ);
			objTemplate = xdrObjectTable.get(key);
		}
		if ( objTemplate != null )
			try {
				return objTemplate.getClass().newInstance();
			} catch (InstantiationException e) {
				throw new ClientException(e);
			} catch (IllegalAccessException e) {
				throw new ClientException(e);
			}
		return null;
	}
	
	private XDRObject decodeXDRObjectResponse(PlayerClientItem response) throws ClientException {
		XDRObject responseXDRObject = getXDRObject(response);
		if ( responseXDRObject != null ) {
			byte[] buffer = response.getData().toByteArray();
			try {
				responseXDRObject.fromXDR(buffer, 0, buffer.length);
			} catch (XDRException e) {
				throw new ClientException(e);
			}
		}
		return responseXDRObject;
	}

	@Override
	public void putMsg(PlayerMsghdr header, XDRObject data) {
		// TODO Auto-generated method stub
		
	}
	  
}