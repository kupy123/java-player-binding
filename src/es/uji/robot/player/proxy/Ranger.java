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

import java.util.ArrayList;

import es.uji.robot.player.generated.abstractproxy.AbstractRanger;
import es.uji.robot.player.generated.xdr.PlayerBbox3d;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.player.generated.xdr.PlayerPoint3d;
import es.uji.robot.player.generated.xdr.PlayerPose3d;
import es.uji.robot.player.generated.xdr.PlayerRangerConfig;
import es.uji.robot.player.generated.xdr.PlayerRangerDataIntns;
import es.uji.robot.player.generated.xdr.PlayerRangerDataIntnspose;
import es.uji.robot.player.generated.xdr.PlayerRangerDataRange;
import es.uji.robot.player.generated.xdr.PlayerRangerDataRangepose;
import es.uji.robot.player.generated.xdr.PlayerRangerGeom;
import es.uji.robot.player.generated.xdr.PlayerRangerIntnsConfig;
import es.uji.robot.player.generated.xdr.PlayerRangerPowerConfig;
import es.uji.robot.xdr.XDRObject;

/**
*The ranger proxy provides an interface to ranger sensor devices.
*/
public class Ranger extends AbstractRanger{

	/** 
	*Number of individual range sensors in the device. 
	*/
	private int sensorCount;
	/** 
	*Start angle of scans [rad]. May be unfilled. 
	*/
	private double minAngle;
	/** 
	*End angle of scans [rad]. May be unfilled.
	*/
	private double maxAngle;
	/** 
	*Scan resolution [rad]. May be unfilled. 
	*/
	private double resolution;
	/** 
	*Maximum range [m]. May be unfilled. 
	*/
	private double maxRange;
	/** 
	*Range resolution [m]. May be unfilled. 
	*/
	private double rangeResolution;
	/** 
	*Scanning frequency [Hz]. May be unfilled. 
	*/
	private double frequency;
	/** 
	*Device geometry in the robot CS: pose gives the position and orientation,
	*size gives the extent. These values are filled in by calling
	*retrieveGeometry(), or from pose-stamped data. 
	*/
	private PlayerPose3d devicePose;
	private PlayerBbox3d deviceSize;
	/** 
	*Geometry of each individual range sensor in the device (e.g. a single
	*sonar sensor in a sonar array). These values are filled in by calling
	*retrieveGeometry(), or from pose-stamped data.
	*/
	private ArrayList<PlayerPose3d> sensorPoses;
	private ArrayList<PlayerBbox3d> sensorSizes;
	/** 
	*Range data [m]. 
	*/
	private ArrayList<Double> ranges;
	/** 
	*Intensity data [m]. Note that this data may require setting of the
	*suitable property on the driver to before it will be filled. Possible
	*properties include intns_on for laser devices and volt_on for IR devices. 
	*/
	private ArrayList<Double> intensities;
	/** 
	*Scan bearings [radians]. 
	*/
	private ArrayList<Double> bearings;
	/** 
	*Scan points (x, y, z). 
	*/
	private ArrayList<PlayerPoint3d> points;
	
	public int getSensorCount() {
		return sensorCount;
	}

	public void setSensorCount(int sensorCount) {
		this.sensorCount = sensorCount;
	}

	public double getMinAngle() {
		return minAngle;
	}

	public void setMinAngle(double minAngle) {
		this.minAngle = minAngle;
	}

	public double getMaxAngle() {
		return maxAngle;
	}

	public void setMaxAngle(double maxAngle) {
		this.maxAngle = maxAngle;
	}

	public double getResolution() {
		return resolution;
	}

	public void setResolution(double resolution) {
		this.resolution = resolution;
	}

	public double getMaxRange() {
		return maxRange;
	}

	public void setMaxRange(double maxRange) {
		this.maxRange = maxRange;
	}

	public double getRangeResolution() {
		return rangeResolution;
	}

	public void setRangeResolution(double rangeResolution) {
		this.rangeResolution = rangeResolution;
	}

	public double getFrequency() {
		return frequency;
	}

	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}

	public PlayerPose3d getDevicePose() {
		return devicePose;
	}

	public void setDevicePose(PlayerPose3d devicePose) {
		this.devicePose = devicePose;
	}

	public PlayerBbox3d getDeviceSize() {
		return deviceSize;
	}

	public void setDeviceSize(PlayerBbox3d deviceSize) {
		this.deviceSize = deviceSize;
	}

	public ArrayList<PlayerPose3d> getSensorPoses() {
		return sensorPoses;
	}

	public void setSensorPoses(ArrayList<PlayerPose3d> sensorPoses) {
		this.sensorPoses = sensorPoses;
	}

	public ArrayList<PlayerBbox3d> getSensorSizes() {
		return sensorSizes;
	}

	public void setSensorSizes(ArrayList<PlayerBbox3d> sensorSizes) {
		this.sensorSizes = sensorSizes;
	}

	public ArrayList<Double> getRanges() {
		return ranges;
	}

	public void setRanges(ArrayList<Double> ranges) {
		this.ranges = ranges;
	}

	public ArrayList<Double> getIntensities() {
		return intensities;
	}

	public void setIntensities(ArrayList<Double> intensities) {
		this.intensities = intensities;
	}

	public ArrayList<Double> getBearings() {
		return bearings;
	}

	public void setBearings(ArrayList<Double> bearings) {
		this.bearings = bearings;
	}

	public ArrayList<PlayerPoint3d> getPoints() {
		return points;
	}

	public void setPoints(ArrayList<PlayerPoint3d> points) {
		this.points = points;
	}
	
	public Ranger(Client client, int index){
		super.init(client, PLAYER_RANGER_CODE, index);
	}

	/** 
	*@throws ClientException 
	 * @brief Get the ranger geometry.
	*/
	public PlayerRangerGeom retrieveGeometry() throws ClientException{
		PlayerRangerGeom geometry = new PlayerRangerGeom();
		
		geometry = (PlayerRangerGeom)super.getClient().request(this, PLAYER_RANGER_REQ_GET_GEOM, null);
		
		if(geometry != null){
			devicePose = geometry.getPose();
			deviceSize = geometry.getSize();
			sensorPoses = geometry.getSensorPoses();
			sensorSizes = geometry.getSensorSizes();
		}
		
		return geometry;
		
	}
	
	/** 
	*@brief Turn device power on or off.
	*@param value Set to TRUE to turn power on, FALSE to turn power off. 
	 * @throws ClientException 
	*/
	public PlayerRangerPowerConfig powerConfiguration(char value) throws ClientException{
		PlayerRangerPowerConfig configure = new PlayerRangerPowerConfig();
		configure.setState(value);
		
		return (PlayerRangerPowerConfig)super.getClient().request(this, PLAYER_RANGER_REQ_POWER, configure);
		
	}
	
	/** 
	*@brief Turn intensity data on or off.
	*@param value Set to TRUE to turn the data on, FALSE to turn the data off. 
	 * @throws ClientException 
	*/
	public PlayerRangerIntnsConfig intensityConfiguration(char value) throws ClientException{
		PlayerRangerIntnsConfig configure = new PlayerRangerIntnsConfig();
		configure.setState(value);
		
		return (PlayerRangerIntnsConfig)super.getClient().request(this, PLAYER_RANGER_REQ_INTNS, configure); 
	}
	
	/** 
	*@brief Set the ranger device's configuration. Not all values may be used.
	*@param minAngle Start angle of scans [rad].
	*@param maxAngle End angle of scans [rad].
	*@param resolution Scan resolution [rad].
	*@param maxRange Maximum range [m].
	*@param rangeResolution Range resolution [m].
	*@param frequency Scanning frequency [Hz]. 
	 * @throws ClientException 
	*/
	public PlayerRangerConfig sendConfiguration(double minAngle, double maxAngle, double resolution, double maxRange, double rangeResolution, double frequency) throws ClientException{
		PlayerRangerConfig configure = new PlayerRangerConfig();
		PlayerRangerConfig response = new PlayerRangerConfig();
		
		configure.setMinAngle(minAngle);
		configure.setMaxAngle(maxAngle);
		configure.setResolution(resolution);
		configure.setMaxRange(maxRange);
		configure.setRangeRes(rangeResolution);
		configure.setFrequency(frequency);
		
		response = (PlayerRangerConfig)super.getClient().request(this, PLAYER_RANGER_REQ_SET_CONFIG, configure);
		
		if(response != null){
			minAngle = response.getMinAngle();
			maxAngle = response.getMaxAngle();
			resolution = response.getResolution();
			maxRange = response.getMaxRange();
			rangeResolution = response.getRangeRes();
			frequency = response.getFrequency();
		}
		
		return response;
	}
	
	/** 
	*@brief Get the ranger device's configuration. Not all values may be filled.
	*@param minAngle Start angle of scans [rad].
	*@param maxAngle End angle of scans [rad].
	*@param resolution Scan resolution [rad].
	*@param maxRange Maximum range [m].
	*@param range_Resolutioin Range resolution [m].
	*@param frequency Scanning frequency [Hz]. 
	 * @throws ClientException 
	*/
	public PlayerRangerConfig retrieveConfiguration() throws ClientException{
		PlayerRangerConfig configure = new PlayerRangerConfig();
		PlayerRangerConfig response = new PlayerRangerConfig();
		
		configure = (PlayerRangerConfig)super.getClient().request(this, PLAYER_RANGER_REQ_GET_CONFIG, configure);
		
		if(configure != null){
			minAngle = configure.getMinAngle();
			maxAngle = configure.getMaxAngle();
			resolution = configure.getResolution();
			maxRange = configure.getMaxRange();
			rangeResolution = configure.getRangeRes();
			frequency = configure.getFrequency();
		}
		
		if(new Double(minAngle) != null){
			response.setMinAngle(minAngle);
		}
		if(new Double(maxAngle) != null){
			response.setMaxAngle(maxAngle);
		}
		if(new Double(resolution) != null){
			response.setResolution(resolution);
		}
		if(new Double(maxRange) != null){
			response.setMaxRange(maxRange);
		}
		if(new Double(rangeResolution) != null){
			response.setRangeRes(rangeResolution);
		}
		if(new Double(frequency) != null){
			response.setFrequency(frequency);
		}
		
		return response;
	}

	@Override
	public void putMsg(PlayerMsghdr header, XDRObject data) {
		if(header.getSize() > 0){
			if (header.getType() ==  PLAYER_MSGTYPE_DATA && header.getSubtype() ==  PLAYER_RANGER_DATA_RANGE){
				PlayerRangerDataRange range = (PlayerRangerDataRange)data;
				this.ranges = range.getRanges();
				calculateBearings();
				calculatePoints();				
			}
			else if (header.getType() ==  PLAYER_MSGTYPE_DATA && header.getSubtype() ==  PLAYER_RANGER_DATA_RANGEPOSE){
				PlayerRangerDataRangepose rangePose = (PlayerRangerDataRangepose)data;
				ranges = rangePose.getData().getRanges();
				devicePose = rangePose.getGeom().getPose();
				deviceSize = rangePose.getGeom().getSize();
				sensorPoses = rangePose.getGeom().getSensorPoses();
				sensorSizes = rangePose.getGeom().getSensorSizes();
				sensorCount = rangePose.getGeom().getSensorPoses().size();
				calculateBearings();
				calculatePoints();	
			}
			else if (header.getType() ==  PLAYER_MSGTYPE_DATA && header.getSubtype() ==  PLAYER_RANGER_DATA_INTNS){
				PlayerRangerDataIntns intns = (PlayerRangerDataIntns)data;
				intensities = intns.getIntensities();
			}
			else if (header.getType() ==  PLAYER_MSGTYPE_DATA && header.getSubtype() ==  PLAYER_RANGER_DATA_INTNSPOSE){
				PlayerRangerDataIntnspose intnsPose = (PlayerRangerDataIntnspose)data;
				intensities = intnsPose.getData().getIntensities();
				devicePose = intnsPose.getGeom().getPose();
				deviceSize = intnsPose.getGeom().getSize();
				sensorPoses = intnsPose.getGeom().getSensorPoses();
				sensorSizes = intnsPose.getGeom().getSensorSizes();
				sensorCount = intnsPose.getGeom().getSensorPoses().size();
			}
			else if (header.getType() ==  PLAYER_MSGTYPE_DATA && header.getSubtype() ==  PLAYER_RANGER_DATA_GEOM){
				PlayerRangerGeom geometry = (PlayerRangerGeom)data;
				devicePose = geometry.getPose();
				deviceSize = geometry.getSize();
				sensorPoses = geometry.getSensorPoses();
				sensorSizes = geometry.getSensorSizes();
				sensorCount = geometry.getSensorPoses().size();
			}
			else{
				System.err.println("Skipping ranger message with unknown type/subtype.");
			}
		}
		
	}
	
	/**
	 * Calculate bearings
	 */
	public void calculateBearings(){
		if (bearings.size() == 0 && bearings != null){
			bearings = null;
		}
		else{
			double b = minAngle;
			for (int i = 0; i < ranges.size(); i++ ){
				bearings.set(i, b);
				b += resolution;
			}
		}
	}
	
	/**
	 * Calculate scan points
	 */
	
	public void calculatePoints(){
		if(points.size() == 0 && points != null){
			points = null;
		}
		else{
			double b = minAngle;
			for (int i = 0; i < ranges.size(); i++ ){
				double r = ranges.get(i);
				points.get(i).setPx(r * Math.cos(b));
				points.get(i).setPy(r * Math.sin(b));
				points.get(i).setPz(0.0);
				b += resolution;
			}
		}
	}

}
