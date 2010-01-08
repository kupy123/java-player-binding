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

package es.uji.robot.player.connection;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class ConnectionManager implements Runnable{
	
	protected static final int PLAYERC_TRANSPORT_TCP = 1;
	protected static final int PLAYERC_TRANSPORT_UDP = 2;
	
	private String host;
	private int port;
	private int transport;
	private boolean connected;
//	private int retryLimit;
//	private int retryTime;
//	private double dataTime;
//	private double lastTime;	
	private int requestTimeout;
	private ClientTCP clientTCP;
	private ClientUDP clientUDP;
	
	protected ByteArrayOutputStream buffer; //XXX: Mirar a ver c—mo quitar los leidos ... recordar procesadores! Controlar los bytes consumidos!
	
	class ClientTCP {
		private Socket socket;
		private InputStream is;
		private OutputStream os;
		private DataInputStream dis;
		private DataOutputStream dos;
		
		public ClientTCP(String host, int port) throws UnknownHostException, IOException {
			socket = new Socket(host,port);
		}

		public Socket getSocket() {
			return socket;
		}

		public InputStream getInputStream() throws IOException {
			if ( is == null && socket != null )
				is = socket.getInputStream();
			return is;
		}

		public OutputStream getOutputStream() throws IOException {
			if ( os == null && socket != null )
				os = socket.getOutputStream();
			return os;
		}

		public DataInputStream getDataInputStream() throws IOException {
			if ( dis == null && getInputStream() != null )
				dis = new DataInputStream(getInputStream());
			return dis;
		}

		public DataOutputStream getDataOutputStream() throws IOException {
			if ( dos == null && getOutputStream() != null )
				dos = new DataOutputStream(getOutputStream());
			return dos;
		}
	}
	
	class ClientUDP {
		private DatagramSocket socket;
		
		public ClientUDP(String host, int port) throws SocketException, UnknownHostException {
			socket = new DatagramSocket(port, InetAddress.getByName(host));
		}

		public DatagramSocket getSocket() {
			return socket;
		}
	}
	
	public ConnectionManager(String host, int port) {
		super();
		setHost(host);
		setPort(port);
		setTransport(PLAYERC_TRANSPORT_TCP);
	}

	public ByteArrayOutputStream getBuffer() {
		return buffer;
	}

	public void setBuffer(ByteArrayOutputStream buffer) {
		this.buffer = buffer;
	}

	public ClientTCP getClientTCP() {
		return clientTCP;
	}

	public ClientUDP getClientUDP() {
		return clientUDP;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getTransport() {
		return transport;
	}

	public void setTransport(int transport) {
		this.transport = transport;
	}

	public boolean isConnected() {
		return connected;
	}

//	public int getRetryLimit() {
//		return retryLimit;
//	}
//
//	public void setRetryLimit(int retryLimit) {
//		this.retryLimit = retryLimit;
//	}
//
//	public int getRetryTime() {
//		return retryTime;
//	}
//
//	public void setRetryTime(int retryTime) {
//		this.retryTime = retryTime;
//	}
//
//	public double getDataTime() {
//		return dataTime;
//	}
//
//	public void setDataTime(double dataTime) {
//		this.dataTime = dataTime;
//	}
//
//	public double getLastTime() {
//		return lastTime;
//	}
//
//	public void setLastTime(double lastTime) {
//		this.lastTime = lastTime;
//	}

	public int getRequestTimeout() {
		return requestTimeout;
	}

	public void setRequestTimeout(int requestTimeout) {
		this.requestTimeout = requestTimeout;
	}
	
	public void connect() throws UnknownHostException, IOException{
		// TCP Transport
		if ( transport == PLAYERC_TRANSPORT_TCP ) {
			clientTCP = new ClientTCP(host, port);
			if ( clientTCP != null && clientTCP.getSocket() != null ){
				connected = true;
			}
		}
		
		// UDP Transport
		else if ( transport == PLAYERC_TRANSPORT_UDP ) {
			clientUDP = new ClientUDP(host, port);
			if ( clientUDP != null && clientUDP.getSocket() != null ){
				connected = true;
			}
			// Send and empty message to get things going
			byte[] s = null;
			send(s);
		}
		
		//XXX: Deshabilitado temporalmente
//		if ( connected ) {
//			new Thread(this).start();
//		}
	}
	
	public void disconnect() throws IOException{
		if ( transport == PLAYERC_TRANSPORT_TCP ){
			clientTCP.getSocket().close();
			connected = false;
		}
		else if ( transport == PLAYERC_TRANSPORT_UDP ){
			clientUDP.getSocket().close();
			connected = false;
		}
	}
	
	public void send(byte[] bytes) throws IOException{
		// TCP Transport
		if ( transport == PLAYERC_TRANSPORT_TCP ){
			clientTCP.getOutputStream().write(bytes);
			clientTCP.getOutputStream().flush();
		}
		// UDP Transport
		else if ( transport == PLAYERC_TRANSPORT_UDP ){
			DatagramPacket sendUDP = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(host), port);
			clientUDP.getSocket().send(sendUDP);
		}
	}
	
	public byte[] receive(int numBytes, boolean fully) throws IOException{
		byte[] res = new byte[numBytes];
		int n = 0;
		// TCP Transport
		if ( transport == PLAYERC_TRANSPORT_TCP ){
			try {
				clientTCP.getSocket().setSoTimeout(getRequestTimeout());
				if ( fully ) {
					clientTCP.getDataInputStream().readFully(res);
					n = numBytes;
				} else
					n = clientTCP.getDataInputStream().read(res);
			} catch (SocketTimeoutException e) {
				throw new IOException("SocketTimeoutException occurred and captured",e);
			}
		}
		// UDP Transport
		else if ( transport == PLAYERC_TRANSPORT_UDP ){
			clientUDP.getSocket().setSoTimeout(getRequestTimeout());
			
			do {
				DatagramPacket receiveUDP = new DatagramPacket(res, n, res.length);
				clientUDP.getSocket().receive(receiveUDP);
				n += receiveUDP.getLength();
			} while (fully && n < numBytes);
		}
		// Return what needs to be returned
		if ( n <= 0 ) {
			return null;
		} else if ( n < res.length) {
			byte[] r = new byte[n];
			System.arraycopy(res, 0, r, 0, n);
			return r;
		} else if ( n == res.length ) {
			return res;
		} else {
			throw new IOException("Unexpected received data length n="+n);
		}
	}

	public void run() {
		//XXX: Deshabilitado temporalmente
//		while ( connected ) {
//			byte[] b;
//			try {
//				b = receive();
//				buffer.write(b);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (ClassNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
	}

}
