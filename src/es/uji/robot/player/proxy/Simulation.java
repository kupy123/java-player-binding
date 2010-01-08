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

import es.uji.robot.player.generated.abstractproxy.AbstractSimulation;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.player.generated.xdr.PlayerPose2d;
import es.uji.robot.player.generated.xdr.PlayerPose3d;
import es.uji.robot.player.generated.xdr.PlayerSimulationPose2dReq;
import es.uji.robot.player.generated.xdr.PlayerSimulationPose3dReq;
import es.uji.robot.player.generated.xdr.PlayerSimulationPropertyReq;
import es.uji.robot.xdr.XDRObject;

/**
*The simulation proxy is used to interact with objects in a simulation.
*/
public class Simulation extends AbstractSimulation{


	public Simulation(Client client, int index){
		super.init(client, PLAYER_SIMULATION_CODE, index);
	}
	
	/** 
	*@throws ClientException 
	 * @brief Set the 2D pose of a named simulation object 
	*/
	public PlayerSimulationPose2dReq sendPose2d(ByteArrayOutputStream name, double goalX, double goalY, double goalA) throws ClientException{
		PlayerSimulationPose2dReq request = new PlayerSimulationPose2dReq();
		PlayerPose2d pose = new PlayerPose2d();
		pose.setPx(goalX);
		pose.setPy(goalY);
		pose.setPa(goalA);
		request.setPose(pose);
		request.setName(name);
		
		return (PlayerSimulationPose2dReq)super.getClient().request(this, PLAYER_SIMULATION_REQ_SET_POSE2D, request);
	}
	
	/** 
	*@throws ClientException 
	 * @brief Get the 2D pose of a named simulation object 
	*/
	public PlayerPose2d retrievePose2d(ByteArrayOutputStream name) throws ClientException{
		PlayerSimulationPose2dReq request = new PlayerSimulationPose2dReq();
		PlayerSimulationPose2dReq configure = new PlayerSimulationPose2dReq();
		
		request.setName(name);
		
		configure = (PlayerSimulationPose2dReq)super.getClient().request(this, PLAYER_SIMULATION_REQ_GET_POSE2D, request);
		
		PlayerPose2d response = new PlayerPose2d();
		if(configure != null){
			response.setPa(configure.getPose().getPa());
			response.setPx(configure.getPose().getPx());
			response.setPy(configure.getPose().getPy());
		}
		
		return response;
	}
	
	/** 
	*@throws ClientException 
	 * @brief Set the 3D pose of a named simulation object 
	*/
	public PlayerSimulationPose3dReq sendPose3d(ByteArrayOutputStream name, double goalX, double goalY, double goalZ, double goalRoll, double goalPitch, double goalYaw) throws ClientException{
		PlayerSimulationPose3dReq request = new PlayerSimulationPose3dReq();
		PlayerPose3d pose = new PlayerPose3d();
		pose.setPx(goalX);
		pose.setPy(goalY);
		pose.setPz(goalZ);
		pose.setProll(goalRoll);
		pose.setPpitch(goalPitch);
		pose.setPyaw(goalYaw);
		request.setPose(pose);
		request.setName(name);
		
		return 	(PlayerSimulationPose3dReq)super.getClient().request(this, PLAYER_SIMULATION_REQ_SET_POSE3D, request);	
		
	}
	
	/** 
	*@throws ClientException 
	 * @brief Get the 3D pose of a named simulation object 
	*/
	public PlayerPose3d retrievePose3d(ByteArrayOutputStream name) throws ClientException{
		PlayerSimulationPose3dReq request = new PlayerSimulationPose3dReq();
		PlayerSimulationPose3dReq configure = new PlayerSimulationPose3dReq();
		
		request.setName(name);
		
		configure = (PlayerSimulationPose3dReq)super.getClient().request(this, PLAYER_SIMULATION_REQ_GET_POSE3D, request);
		
		PlayerPose3d response = new PlayerPose3d();
		if(configure != null){
			response.setPx(configure.getPose().getPx());
			response.setPy(configure.getPose().getPy());
			response.setPz(configure.getPose().getPz());
			response.setProll(configure.getPose().getProll());
			response.setPpitch(configure.getPose().getPpitch());
			response.setPyaw(configure.getPose().getPyaw());
		}
		
		return response;
	}
	
	/** 
	*@throws ClientException 
	 * @brief Set a property value 
	*/
	public PlayerSimulationPropertyReq sendProperty(ByteArrayOutputStream name, ByteArrayOutputStream property, ByteArrayOutputStream value) throws ClientException{
		PlayerSimulationPropertyReq request = new PlayerSimulationPropertyReq();
		
		request.setName(name);
		request.setProp(property);
		request.setValue(value);
		
		return (PlayerSimulationPropertyReq)super.getClient().request(this, PLAYER_SIMULATION_REQ_SET_PROPERTY, request);
	}
	
	/**
	*@throws ClientException 
	 * @brief Get a property value 
	*/
	public PlayerSimulationPropertyReq retrieveProperty(ByteArrayOutputStream name, ByteArrayOutputStream property, ByteArrayOutputStream value) throws ClientException{
		PlayerSimulationPropertyReq request = new PlayerSimulationPropertyReq();
		
		request.setName(name);
		request.setProp(property);
		request.setValue(value);
		
		return (PlayerSimulationPropertyReq)super.getClient().request(this, PLAYER_SIMULATION_REQ_GET_PROPERTY, request);
		
	}

	@Override
	public void putMsg(PlayerMsghdr header, XDRObject data) {
		// TODO Auto-generated method stub
		
	}
	


}