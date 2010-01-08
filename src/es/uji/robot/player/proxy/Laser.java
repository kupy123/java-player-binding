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
import java.util.ArrayList;

import es.uji.robot.player.generated.abstractproxy.AbstractLaser;
import es.uji.robot.player.generated.xdr.PlayerLaserConfig;
import es.uji.robot.player.generated.xdr.PlayerLaserData;
import es.uji.robot.player.generated.xdr.PlayerLaserDataScanpose;
import es.uji.robot.player.generated.xdr.PlayerLaserGeom;
import es.uji.robot.player.generated.xdr.PlayerLaserGetIdConfig;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.player.generated.xdr.PlayerPoint2d;
import es.uji.robot.xdr.XDRObject;


/**
*The laser proxy provides an interface to scanning laser range finders
*such as the @ref driver_sicklms200.
*/
public class Laser extends AbstractLaser{

	/** 
	*Laser geometry in the robot cs: pose gives the position and
	*orientation, size gives the extent.  These values are filled in by
	*retrieveGeometry(). 
	*/
	private ArrayList<Double> pose = new ArrayList<Double>();
	private ArrayList<Double> size = new ArrayList<Double>();
	/** 
	*Robot pose (m,m,rad), filled in if the scan came with a pose attached
	*/
	private ArrayList<Double> robotPose;
	/** 
	*Is intesity data returned. 
	*/
	private int intensityOn;
	/** 
	*Number of points in the scan. 
	*/
	private int scanCount;
	/** 
	*Start bearing of the scan (radians). 
	*/
	private double scanStart;
	/** 
	*Angular resolution in radians. 
	*/
	private double scanResolution;
	/** 
	*Range resolution, in m. 
	*/
	private double rangeResolution;
	/** 
	*Maximum range of sensor, in m.
	*/
	private double maxRange;
	/** 
	*Scanning frequency in Hz. 
	*/
	private double scanningFrequency;
	/** 
	*Raw range data; range (m). 
	*/
	private ArrayList<Double> ranges;
	/** 
	*Scan data; range (m) and bearing (radians). 
	*/
	private ArrayList<ArrayList<Double>> scan;
	/** 
	*Scan data; x, y position (m). 
	*/
	private ArrayList<PlayerPoint2d> point;
	/** 
	*Scan reflection intensity values (0-3).  Note that the intensity
	*values will only be filled if intensity information is enabled
	*(using the sendConfiguration function). 
	*/
	private ArrayList<Integer> intensity;
	/** 
	*ID for this scan 
	*/
	private int scanId;
	/** 
	*Laser IDentification information
	*/
	private int laserId;
	/** 
	*Minimum range, in meters, in the right half of the scan (those ranges
	* from the first beam, counterclockwise, up to the middle of the scan,
	* including the middle beam, if one exists).
	*/
	private double minRight;
	/** 
	*Minimum range, in meters, in the left half of the scan (those ranges
	* from the first beam after the middle of the scan, counterclockwise, to
	* the last beam). 
	*/
	private double minLeft;
	
	public ArrayList<Double> getPose() {
		return pose;
	}

	public void setPose(ArrayList<Double> pose) {
		this.pose = pose;
	}

	public ArrayList<Double> getSize() {
		return size;
	}

	public void setSize(ArrayList<Double> size) {
		this.size = size;
	}

	public ArrayList<Double> getRobotPose() {
		return robotPose;
	}

	public void setRobotPose(ArrayList<Double> robotPose) {
		this.robotPose = robotPose;
	}

	public int getIntensityOn() {
		return intensityOn;
	}

	public void setIntensityOn(int intensityOn) {
		this.intensityOn = intensityOn;
	}

	public int getScanCount() {
		return scanCount;
	}

	public void setScanCount(int scanCount) {
		this.scanCount = scanCount;
	}

	public double getScanStart() {
		return scanStart;
	}

	public void setScanStart(double scanStart) {
		this.scanStart = scanStart;
	}

	public double getScanResolution() {
		return scanResolution;
	}

	public void setScanResolution(double scanResolution) {
		this.scanResolution = scanResolution;
	}

	public double getRangeResolution() {
		return rangeResolution;
	}

	public void setRangeResolution(double rangeResolution) {
		this.rangeResolution = rangeResolution;
	}

	public double getMaxRange() {
		return maxRange;
	}

	public void setMaxRange(double maxRange) {
		this.maxRange = maxRange;
	}

	public double getScanningFrequency() {
		return scanningFrequency;
	}

	public void setScanningFrequency(double scanningFrequency) {
		this.scanningFrequency = scanningFrequency;
	}

	public ArrayList<Double> getRanges() {
		return ranges;
	}

	public void setRanges(ArrayList<Double> ranges) {
		this.ranges = ranges;
	}

	public ArrayList<ArrayList<Double>> getScan() {
		return scan;
	}

	public void setScan(ArrayList<ArrayList<Double>> scan) {
		this.scan = scan;
	}

	public ArrayList<PlayerPoint2d> getPoint() {
		return point;
	}

	public void setPoint(ArrayList<PlayerPoint2d> point) {
		this.point = point;
	}

	public ArrayList<Integer> getIntensity() {
		return intensity;
	}

	public void setIntensity(ArrayList<Integer> intensity) {
		this.intensity = intensity;
	}

	public int getScanId() {
		return scanId;
	}

	public void setScanId(int scanId) {
		this.scanId = scanId;
	}

	public int getLaserId() {
		return laserId;
	}

	public void setLaserId(int laserId) {
		this.laserId = laserId;
	}

	public double getMinRight() {
		return minRight;
	}

	public void setMinRight(double minRight) {
		this.minRight = minRight;
	}

	public double getMinLeft() {
		return minLeft;
	}

	public void setMinLeft(double minLeft) {
		this.minLeft = minLeft;
	}

	public Laser(Client client, int index){
		super.init(client, PLAYER_LASER_CODE, index);
	}
	
	/** 
	*Configure the laser.
	*@param minAngle, maxAngle Start and end angles for the scan
	*(radians).
	*@param resolution Angular resolution in radians. Valid values depend on the
	*underlyling driver.
	*@param rangeResolution Range resolution in m.  Valid values depend on the
	*underlyling driver.
	*@param intensity Intensity flag; set to 1 to enable reflection intensity data.
	*@param scanningFrequency Scanning frequency in Hz. Valid values depend on the
	*underlyling driver.
	 * @throws ClientException 
	*/
	public void sendConfiguration(double minAngle, double maxAngle, double resolution, double rangeResolution, char intensity, double scanningFrequency) throws ClientException{
		PlayerLaserConfig configure = new PlayerLaserConfig();
		
		configure.setMinAngle((float)minAngle);
		configure.setMaxAngle((float)maxAngle);
		configure.setResolution((float)resolution);
		configure.setIntensity((char)intensity);
		configure.setRangeRes((float)rangeResolution);
		configure.setScanningFrequency((float)scanningFrequency);
		
		
		super.getClient().request(this, PLAYER_LASER_REQ_SET_CONFIG, configure);
		
		this.scanStart = configure.getMinAngle();
		this.scanResolution = configure.getResolution();
		this.rangeResolution = configure.getRangeRes();
		this.intensityOn = configure.getIntensity();
		this.scanningFrequency = configure.getScanningFrequency();
	}
	
	/** 
	*Get the laser configuration.
	*@param minAngle, maxAngle Start and end angles for the scan
	*(radians).
	*@param resolution Angular resolution in radians. Valid values depend on the
	*underlyling driver.
	*@param rangeResolution Range resolution in m.  Valid values depend on the	
	underlyling driver.
	*@param intensity Intensity flag; set to 1 to enable reflection intensity data.
	*@param scanningFrequency Scanning frequency in Hz. Valid values depend on the
	*underlyling driver.
	 * @throws ClientException 
	 * @returns PlayerLaserConfig
	*/
	public PlayerLaserConfig retrieveConfiguration() throws ClientException{
		PlayerLaserConfig configure = new PlayerLaserConfig();
		PlayerLaserConfig response = new PlayerLaserConfig();
		
		configure = (PlayerLaserConfig)super.getClient().request(this, PLAYER_LASER_REQ_GET_CONFIG, null);
		
		if(configure != null){
			scanStart = configure.getMinAngle();
			scanResolution = configure.getResolution();
			intensityOn = configure.getIntensity();
			rangeResolution = configure.getRangeRes();
			maxRange = configure.getMaxRange();
			
			response.setMinAngle(configure.getMinAngle());
			response.setMaxAngle(configure.getMaxAngle());
			response.setResolution(configure.getResolution());
			response.setIntensity(configure.getIntensity());
			response.setRangeRes(configure.getRangeRes());
			response.setScanningFrequency(configure.getScanningFrequency());
		}
		return response;
	}
	
	/** 
	*Get the laser geometry.
	*This writes the result into the proxy rather than returning it to the
	*caller.
	 * @throws ClientException 
	*/
	public void retrieveGeometry() throws ClientException{
		PlayerLaserGeom geometry = new PlayerLaserGeom();
		
		geometry = (PlayerLaserGeom)super.getClient().request(this, PLAYER_LASER_REQ_GET_GEOM, null);
		
		if(geometry != null){
			pose.add(0, geometry.getPose().getPx());
			pose.add(1, geometry.getPose().getPy());
			pose.add(2, geometry.getPose().getPyaw());
			size.add(0, geometry.getSize().getSl());
			size.add(1, geometry.getSize().getSw());
		}
	}
	
	/** 
	*Get the laser IDentification information.
	*This writes the result into the proxy rather than returning it to the
	*caller.
	 * @throws ClientException 
	*/
	public void retrieveId() throws ClientException{
		PlayerLaserGetIdConfig configure = new PlayerLaserGetIdConfig();
		
		configure = (PlayerLaserGetIdConfig)super.getClient().request(this, PLAYER_LASER_REQ_GET_ID, null);
		
		laserId = configure.getSerialNumber();
	}
	
	/** 
	*Print a human-readable summary of the laser state.
	*/
	public void printout(String prefix){
		if(prefix != null){
			System.out.println(prefix + ": ");
		}
		
		System.out.println(super.getDatatime() + " scanId: " + scanId  + " scanCount: " + scanCount);
		
		System.out.println("# ranges");
		for(int i = 0; i < ranges.size(); i++){
			System.out.println(ranges.get(i));
		}
		
	}

	@Override
	public void putMsg(PlayerMsghdr header, XDRObject data) {
		int i;
		double b;
		double db;
		double r;
		if(header.getType() == PLAYER_MSGTYPE_DATA && header.getSubtype() ==  PLAYER_LASER_DATA_SCAN){
			PlayerLaserData laser = (PlayerLaserData)data;
			b = laser.getMinAngle();
			db = laser.getResolution();
			
			scanStart = b;
			scanResolution = db;
			maxRange = laser.getMaxRange();
			minLeft = laser.getMaxRange();
			minRight = laser.getMaxRange();
			
			ArrayList<Double> scanData = new ArrayList<Double>();
			PlayerPoint2d pointData = new PlayerPoint2d();
			
			for(i = 0; i < laser.getRanges().size(); i++){
				r = laser.getRanges().get(i);
				
				if(r >= 0){
					this.ranges.set(i, r);
					scanData.clear();
					scanData.set(0, r);
					scanData.set(1, b);
					this.scan.set(i, scanData);
					pointData.setPx(r * Math.cos(b));
					pointData.setPy(r * Math.sin(b));
					this.point.set(i, pointData);
					
					b += db;
					
					if(i <= laser.getRanges().size()/2 && r < minRight){
						minRight = r;
					}
					else if(i > laser.getRanges().size()/2 && r < minLeft ){
						minLeft = r;
					}
				}
			}
			
			byte[] intensities = laser.getIntensity().toByteArray();
			for(i = 0; i < intensities.length; i++){
				this.intensity.set(i, (int)intensities[i]);
			}
			
			scanId = laser.getId();
		}
		else if(header.getType() == PLAYER_MSGTYPE_DATA && header.getSubtype() ==  PLAYER_LASER_DATA_SCANPOSE){
			PlayerLaserDataScanpose pose = new PlayerLaserDataScanpose();
			
			b = pose.getScan().getMinAngle();
			db = pose.getScan().getResolution();
			
			scanStart = b;
			scanResolution = db;
			
			ArrayList<Double> scanData = new ArrayList<Double>();
			PlayerPoint2d pointData = new PlayerPoint2d();
			
			for(i = 0; i < pose.getScan().getRanges().size(); i++){
				r = pose.getScan().getRanges().get(i);
				if(r >= 0){
					this.ranges.set(i, r);
					scanData.clear();
					scanData.set(0, r);
					scanData.set(1, b);
					this.scan.set(i, scanData);
					pointData.setPx(r * Math.cos(b));
					pointData.setPy(r * Math.sin(b));
					this.point.set(i, pointData);
				}
			}
			
			byte[] intensities = pose.getScan().getIntensity().toByteArray();
			for(i = 0; i < intensities.length; i++){
				this.intensity.set(i, (int)intensities[i]);
			}
			
			scanId = pose.getScan().getId();
			robotPose.set(0, pose.getPose().getPx());
			robotPose.set(1, pose.getPose().getPy());
			robotPose.set(2, pose.getPose().getPa());
		}
		else{
			System.err.println("Skipping laser message with unknown type/subtype.");
		}
			
		
	}

}