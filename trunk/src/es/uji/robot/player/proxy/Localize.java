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

import es.uji.robot.player.generated.abstractproxy.AbstractLocalize;
import es.uji.robot.player.generated.xdr.PlayerLocalizeData;
import es.uji.robot.player.generated.xdr.PlayerLocalizeGetParticles;
import es.uji.robot.player.generated.xdr.PlayerLocalizeHypoth;
import es.uji.robot.player.generated.xdr.PlayerLocalizeParticle;
import es.uji.robot.player.generated.xdr.PlayerLocalizeSetPose;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.player.generated.xdr.PlayerPose2d;
import es.uji.robot.xdr.XDRObject;

/**
*The localize proxy provides an interface to localization drivers.
*Generally speaking, these are abstract drivers that attempt to
*localize the robot by matching sensor observations (odometry, laser
*and/or sonar) against a prior map of the environment (such as an
*occupancy grid).  Since the pose may be ambiguous, multiple hypotheses
*may returned; each hypothesis specifies one possible pose estimate for
*the robot.  See the Player manual for details of the localize
*interface, and and drivers that support it (such as the amcl driver).
*/
public class Localize extends AbstractLocalize{

	/** 
	*Map dimensions (cells). 
	*/
	private int mapSizeX;
	private int mapSizeY;
	/** 
	*Map scale (m/cell). 
	*/
	private double mapScale;
	/** 
	*Next map tile to read.
	*/
	private int mapTileX;
	private int mapTileY;
	/** 
	*Map data (empty = -1, unknown = 0, occupied = +1).
	*/
	private ByteArrayOutputStream mapCells;
	/** 
	*The number of pending (unprocessed) sensor readings. 
	*/
	private int pendingCount;
	/** 
	*The timestamp on the last reading processed. 
	*/
	private double pendingTime;
	/** 
	*List of possible poses. 
	*/
	private ArrayList<PlayerLocalizeHypoth> hypoths;
	
	private ArrayList<Double> mean;
	private double variance;
	private ArrayList<PlayerLocalizeParticle> particles;
	
	public int getMapSizeX() {
		return mapSizeX;
	}

	public void setMapSizeX(int mapSizeX) {
		this.mapSizeX = mapSizeX;
	}

	public int getMapSizeY() {
		return mapSizeY;
	}

	public void setMapSizeY(int mapSizeY) {
		this.mapSizeY = mapSizeY;
	}

	public double getMapScale() {
		return mapScale;
	}

	public void setMapScale(double mapScale) {
		this.mapScale = mapScale;
	}

	public int getMapTileX() {
		return mapTileX;
	}

	public void setMapTileX(int mapTileX) {
		this.mapTileX = mapTileX;
	}

	public int getMapTileY() {
		return mapTileY;
	}

	public void setMapTileY(int mapTileY) {
		this.mapTileY = mapTileY;
	}

	public ByteArrayOutputStream getMapCells() {
		return mapCells;
	}

	public void setMapCells(ByteArrayOutputStream mapCells) {
		this.mapCells = mapCells;
	}

	public int getPendingCount() {
		return pendingCount;
	}

	public void setPendingCount(int pendingCount) {
		this.pendingCount = pendingCount;
	}

	public double getPendingTime() {
		return pendingTime;
	}

	public void setPendingTime(double pendingTime) {
		this.pendingTime = pendingTime;
	}

	public ArrayList<PlayerLocalizeHypoth> getHypoths() {
		return hypoths;
	}

	public void setHypoths(ArrayList<PlayerLocalizeHypoth> hypoths) {
		this.hypoths = hypoths;
	}

	public ArrayList<Double> getMean() {
		return mean;
	}

	public void setMean(ArrayList<Double> mean) {
		this.mean = mean;
	}

	public double getVariance() {
		return variance;
	}

	public void setVariance(double variance) {
		this.variance = variance;
	}

	public ArrayList<PlayerLocalizeParticle> getParticles() {
		return particles;
	}

	public void setParticles(ArrayList<PlayerLocalizeParticle> particles) {
		this.particles = particles;
	}
	
	public Localize(Client client, int index){
		super.init(client, PLAYER_LOCALIZE_CODE, index);
	}

	/** 
	*Set the the robot pose (mean and covariance). 
	 * @throws ClientException 
	*/
	public PlayerLocalizeSetPose sendPose(ArrayList<Double> pose,ArrayList<Double> covariance) throws ClientException{
		PlayerLocalizeSetPose command = new PlayerLocalizeSetPose();
		
		PlayerPose2d mean = new PlayerPose2d();
		mean.setPx(pose.get(0));
		mean.setPy(pose.get(1));
		mean.setPa(pose.get(2));
		command.setMean(mean);
		command.setCov(covariance);
		
		return (PlayerLocalizeSetPose)super.getClient().request(this, PLAYER_LOCALIZE_REQ_SET_POSE, command);
		
	}
	
	/** 
	*@throws ClientException 
	 * @brief Request the particle set.
	*the result. 
	*/
	public PlayerLocalizeGetParticles retrieveParticles() throws ClientException{
		PlayerLocalizeGetParticles request = new PlayerLocalizeGetParticles();
		mean = new ArrayList<Double>();
		
		request = (PlayerLocalizeGetParticles)super.getClient().request(this, PLAYER_LOCALIZE_REQ_GET_PARTICLES, null);
		
		if (request != null){
			mean.add(request.getMean().getPa());
			mean.add(request.getMean().getPx());
			mean.add(request.getMean().getPy());
			
			variance = request.getVariance();
			
			particles = (ArrayList<PlayerLocalizeParticle>)(Object)request.getParticles();
		}
		
		return request;
	
	}

	@Override
	public void putMsg(PlayerMsghdr header, XDRObject data) {
		PlayerLocalizeData localize = (PlayerLocalizeData)data;
		pendingCount = localize.getPendingCount();
		pendingTime = localize.getPendingTime();
		hypoths = (ArrayList<PlayerLocalizeHypoth>)(Object)localize.getHypoths();
		
	}

}