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

import es.uji.robot.player.generated.abstractproxy.AbstractGps;
import es.uji.robot.player.generated.xdr.PlayerGpsData;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.xdr.XDRObject;

/*
*The gps proxy provides an interface to a GPS-receiver.
*/
public class Gps extends AbstractGps{

	/** 
	*UTC time (seconds since the epoch) 
	*/
	private double utcTime;
	/** 
	*Latitude and logitude (degrees).  Latitudes are positive for
	*north, negative for south.  Logitudes are positive for east,
	*negative for west. 
	*/
	private double latitude;
	private double longitud;
	/** 
	*Altitude (meters).  Positive is above sea-level, negative is
	*below. 
	*/
	private double altitude;
	/** 
	*UTM easting and northing (meters).
	*/
	private double utmEasting;
	private double utmNorthing;
	/** 
	Horizontal dilution of precision. 
	*/
	private double horizontalDilutionOfPrecision;
	/** 
	*Vertical dilution of precision. 
	*/
	private double verticalDilutionOfPrecision;
	/** 
	*Horizontal and vertical error (meters). 
	*/
	private double horizontalError;
	private double verticalError;
	/** 
	*Quality of fix 0 = invalid, 1 = GPS fix, 2 = DGPS fix 
	*/
	private int qualify;
	/** 
	Number of satellites in view. 
	*/
	private int satellitesCount;
	
	public double getUtcTime() {
		return utcTime;
	}
	public void setUtcTime(double utcTime) {
		this.utcTime = utcTime;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitud() {
		return longitud;
	}
	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}
	public double getAltitude() {
		return altitude;
	}
	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}
	public double getUtmEasting() {
		return utmEasting;
	}
	public void setUtmEasting(double utmEasting) {
		this.utmEasting = utmEasting;
	}
	public double getUtmNorthing() {
		return utmNorthing;
	}
	public void setUtmNorthing(double utmNorthing) {
		this.utmNorthing = utmNorthing;
	}
	public double getHorizontalDilutionOfPrecision() {
		return horizontalDilutionOfPrecision;
	}
	public void setHorizontalDilutionOfPrecision(
			double horizontalDilutionOfPrecision) {
		this.horizontalDilutionOfPrecision = horizontalDilutionOfPrecision;
	}
	public double getVerticalDilutionOfPrecision() {
		return verticalDilutionOfPrecision;
	}
	public void setVerticalDilutionOfPrecision(double verticalDilutionOfPrecision) {
		this.verticalDilutionOfPrecision = verticalDilutionOfPrecision;
	}
	public double getHorizontalError() {
		return horizontalError;
	}
	public void setHorizontalError(double horizontalError) {
		this.horizontalError = horizontalError;
	}
	public double getVerticalError() {
		return verticalError;
	}
	public void setVerticalError(double verticalError) {
		this.verticalError = verticalError;
	}
	public int getQualify() {
		return qualify;
	}
	public void setQualify(int qualify) {
		this.qualify = qualify;
	}
	public int getSatellitesCount() {
		return satellitesCount;
	}
	public void setSatellitesCount(int satellitesCount) {
		this.satellitesCount = satellitesCount;
	}

	public Gps(Client client, int index){
		super.init(client, PLAYER_GPS_CODE, index);
	}
	@Override
	public void putMsg(PlayerMsghdr header, XDRObject data) {
		if ( header.getType() == PLAYER_MSGTYPE_DATA && header.getSubtype() ==  PLAYER_GPS_DATA_STATE){
			PlayerGpsData gps = (PlayerGpsData)data;
			
			utcTime = gps.getTimeSec() + gps.getTimeUsec()/1e6;
			
			latitude = gps.getLatitude() / 1e7;
			longitud = gps.getLongitude() / 1e7;
			altitude = gps.getAltitude() / 1e3;
			
			utmEasting = gps.getUtmE() / 100.0;
			utmNorthing = gps.getUtmN() / 100.0;
			
			horizontalDilutionOfPrecision = gps.getHdop() / 10.0;
			verticalDilutionOfPrecision = gps.getVdop() / 10.0;
			
			horizontalError = gps.getErrHorz();
			verticalError = gps.getErrVert();
			
			qualify = gps.getQuality();
			satellitesCount = gps.getNumSats();
		}
		else{
			System.err.println("Skipping gps message with unknown type/subtype.");
		}
		
	}
	
	
	
}