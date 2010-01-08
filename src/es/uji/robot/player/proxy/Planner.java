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

import es.uji.robot.player.generated.abstractproxy.AbstractPlanner;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.player.generated.xdr.PlayerPlannerCmd;
import es.uji.robot.player.generated.xdr.PlayerPlannerData;
import es.uji.robot.player.generated.xdr.PlayerPlannerEnableReq;
import es.uji.robot.player.generated.xdr.PlayerPlannerWaypointsReq;
import es.uji.robot.player.generated.xdr.PlayerPose2d;
import es.uji.robot.xdr.XDRObject;

/**
*The planner proxy provides an interface to a 2D motion planner.
*/
public class Planner extends AbstractPlanner{	

	/** 
	*Did the planner find a valid path? 
	*/
	private int pathValid;
	/** 
	*Have we arrived at the goal? 
	*/
	private int pathDone;
	/** 
	*Current pose (m, m, radians). 
	*/
	private double poseX;
	private double poseY;
	private double poseA;
	/** 
	*Goal location (m, m, radians) 
	*/
	private double goalX;
	private double goalY;
	private double goalA;
	/** 
	*Current waypoint location (m, m, radians) 
	*/
	private double waypointX;
	private double waypointY;
	private double waypointA;
	/** 
	*Current waypoint index (handy if you already have the list
	*of waypoints). May be negative if there's no plan, or if
	*the plan is done 
	*/
	private int currentWaypoint;
	/** 
	*List of waypoints in the current plan (m,m,radians).  Call
	*retrieveWayPoints() to fill this in. 
	*/
	private ArrayList<PlayerPose2d> waypoints;
	
	
	
	public int getPathValid() {
		return pathValid;
	}


	public void setPathValid(int pathValid) {
		this.pathValid = pathValid;
	}


	public int getPathDone() {
		return pathDone;
	}


	public void setPathDone(int pathDone) {
		this.pathDone = pathDone;
	}


	public double getPoseX() {
		return poseX;
	}


	public void setPoseX(double poseX) {
		this.poseX = poseX;
	}


	public double getPoseY() {
		return poseY;
	}


	public void setPoseY(double poseY) {
		this.poseY = poseY;
	}


	public double getPoseA() {
		return poseA;
	}


	public void setPoseA(double poseA) {
		this.poseA = poseA;
	}


	public double getGoalX() {
		return goalX;
	}


	public void setGoalX(double goalX) {
		this.goalX = goalX;
	}


	public double getGoalY() {
		return goalY;
	}


	public void setGoalY(double goalY) {
		this.goalY = goalY;
	}


	public double getGoalA() {
		return goalA;
	}


	public void setGoalA(double goalA) {
		this.goalA = goalA;
	}


	public double getWaypointX() {
		return waypointX;
	}


	public void setWaypointX(double waypointX) {
		this.waypointX = waypointX;
	}


	public double getWaypointY() {
		return waypointY;
	}


	public void setWaypointY(double waypointY) {
		this.waypointY = waypointY;
	}


	public double getWaypointA() {
		return waypointA;
	}


	public void setWaypointA(double waypointA) {
		this.waypointA = waypointA;
	}


	public int getCurrentWaypoint() {
		return currentWaypoint;
	}


	public void setCurrentWaypoint(int currentWaypoint) {
		this.currentWaypoint = currentWaypoint;
	}


	public ArrayList<PlayerPose2d> getWaypoints() {
		return waypoints;
	}


	public void setWaypoints(ArrayList<PlayerPose2d> waypoints) {
		this.waypoints = waypoints;
	}


	public Planner(Client client, int index){
		super.init(client, PLAYER_PLANNER_CODE, index);
	}
	
	
	/** 
	*@throws ClientException 
	 * @brief Set the goal pose (gx, gy, ga) 
	*/
	public void sendCommandPose(double goalX, double goalY, double goalA) throws ClientException{
		PlayerPlannerCmd command = new PlayerPlannerCmd();
		PlayerPose2d goal = new PlayerPose2d();
		
		goal.setPx(goalX);
		goal.setPy(goalY);
		goal.setPa(goalA);
		
		command.setGoal(goal);
		
		super.getClient().write(this, PLAYER_PLANNER_CMD_GOAL, command);
	}
	
	/** 
	*@throws ClientException 
	 * @brief Get the list of waypoints.
	*Writes the result into the proxy rather than returning it to the
	*caller.
	*/
	public void retrieveWaypoints() throws ClientException{
		PlayerPlannerWaypointsReq configure = new PlayerPlannerWaypointsReq();
		waypoints = new ArrayList<PlayerPose2d>();
		
		configure = (PlayerPlannerWaypointsReq)super.getClient().request(this, PLAYER_PLANNER_REQ_GET_WAYPOINTS, null);
		
		if(configure != null){
			PlayerPose2d pose = new PlayerPose2d();
			for(int i = 0; i < configure.getWaypoints().size(); i++){
				pose.setPx(configure.getWaypoints().get(i).getPx());
				pose.setPy(configure.getWaypoints().get(i).getPy());
				pose.setPa(configure.getWaypoints().get(i).getPa());
				waypoints.add(i, pose);
			}
		}
	}
	
	/** 
	*@throws ClientException 
	 * @brief Enable / disable the robot's motion
	*Set state to 1 to enable, 0 to disable.
	*/
	public PlayerPlannerEnableReq enable(int state) throws ClientException{
		PlayerPlannerEnableReq request = new PlayerPlannerEnableReq();
		
		request.setState((char)state);
		
		return (PlayerPlannerEnableReq)super.getClient().request(this, PLAYER_PLANNER_REQ_ENABLE, request);
		
	}

	@Override
	public void putMsg(PlayerMsghdr header, XDRObject data) {
		if(header.getType() == PLAYER_MSGTYPE_DATA && header.getSubtype() == PLAYER_PLANNER_DATA_STATE ){
			PlayerPlannerData planner = (PlayerPlannerData)data;
			pathValid = planner.getValid();
			pathDone = planner.getDone();
			
			poseX = planner.getPos().getPx();
			poseY = planner.getPos().getPy();
			poseA = planner.getPos().getPa();
			
			goalX = planner.getGoal().getPx();
			goalY = planner.getGoal().getPy();
			goalA = planner.getGoal().getPa();
			
			waypointX = planner.getWaypoint().getPx();
			waypointY = planner.getWaypoint().getPy();
			waypointA = planner.getWaypoint().getPa();
			
			currentWaypoint = planner.getWaypointIdx();
		}
		else{
			System.err.println("Skipping planner message with unknown type/subtype.");
		}
		
	}
	
}