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

import es.uji.robot.player.generated.abstractproxy.AbstractImu;
import es.uji.robot.player.generated.xdr.PlayerImuDataCalib;
import es.uji.robot.player.generated.xdr.PlayerImuDataEuler;
import es.uji.robot.player.generated.xdr.PlayerImuDataQuat;
import es.uji.robot.player.generated.xdr.PlayerImuDataState;
import es.uji.robot.player.generated.xdr.PlayerImuDatatypeConfig;
import es.uji.robot.player.generated.xdr.PlayerImuResetOrientationConfig;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.player.generated.xdr.PlayerPose3d;
import es.uji.robot.xdr.XDRObject;


/**
 * The imu proxy provides an interface to an Inertial Measurement Unit.
 *
 */
public class Imu extends AbstractImu{	
	
	/**
	 *  The complete pose of the IMU in 3D coordinates + angles             
	 */
	private PlayerPose3d pose;
	/** 
	 * Calibrated IMU data (accel, gyro, magnetometer)                     
	 */
	private PlayerImuDataCalib calibData;
	/**
	 *  Orientation data as quaternions                                     
	 */
	private float q0;
	private float q1;
	private float q2;
	private float q3;

	public PlayerPose3d getPose() {
		return pose;
	}
	public void setPose(PlayerPose3d pose) {
		this.pose = pose;
	}
	public PlayerImuDataCalib getCalibData() {
		return calibData;
	}
	public void setCalibData(PlayerImuDataCalib calibData) {
		this.calibData = calibData;
	}
	public float getQ0() {
		return q0;
	}
	public void setQ0(float q0) {
		this.q0 = q0;
	}
	public float getQ1() {
		return q1;
	}
	public void setQ1(float q1) {
		this.q1 = q1;
	}
	public float getQ2() {
		return q2;
	}
	public void setQ2(float q2) {
		this.q2 = q2;
	}
	public float getQ3() {
		return q3;
	}
	public void setQ3(float q3) {
		this.q3 = q3;
	}
	
	public Imu(Client client, int index){
		super.init(client, PLAYER_IMU_CODE, index);
	}
	
	/** 
	 * Change the data type to one of the predefined data structures. 
	 * @throws ClientException 
	 */
	public PlayerImuDatatypeConfig datatype(int value) throws ClientException{
		PlayerImuDatatypeConfig configure = new PlayerImuDatatypeConfig();
		
		configure.setValue((char)value);
		
		return (PlayerImuDatatypeConfig)super.getClient().request(this, PLAYER_IMU_REQ_SET_DATATYPE, configure);
		
		
	}
	/**
	 * Reset orientation
	 * @throws ClientException 
	 */
	public PlayerImuResetOrientationConfig orientation(int value) throws ClientException{
		PlayerImuResetOrientationConfig configure = new PlayerImuResetOrientationConfig();
		
		configure.setValue(value);
		
		return (PlayerImuResetOrientationConfig)super.getClient().request(this, PLAYER_IMU_REQ_RESET_ORIENTATION, configure);
		
		
	}
	@Override
	public void putMsg(PlayerMsghdr header, XDRObject data) {
		if (header.getType() == PLAYER_MSGTYPE_DATA && header.getSubtype() ==  PLAYER_IMU_DATA_STATE){
			PlayerImuDataState state = (PlayerImuDataState)data;
			pose = state.getPose();
		}
		else if (header.getType() == PLAYER_MSGTYPE_DATA && header.getSubtype() ==  PLAYER_IMU_DATA_CALIB){
			PlayerImuDataCalib calib = (PlayerImuDataCalib)data;
			calibData.setAccelX(calib.getAccelX());
			calibData.setAccelY(calib.getAccelY());
			calibData.setAccelZ(calib.getAccelZ());
			calibData.setGyroX(calib.getGyroX());
			calibData.setGyroY(calib.getGyroY());
			calibData.setGyroZ(calib.getGyroZ());
			calibData.setMagnX(calib.getMagnX());
			calibData.setMagnY(calib.getMagnY());
			calibData.setMagnZ(calib.getMagnZ());
		}
		else if (header.getType() == PLAYER_MSGTYPE_DATA && header.getSubtype() ==  PLAYER_IMU_DATA_QUAT){
			PlayerImuDataQuat quat = (PlayerImuDataQuat)data;
			calibData = quat.getCalibData();
			q0 = quat.getQ0();
			q1 = quat.getQ1();
			q2 = quat.getQ2();
			q3 = quat.getQ3();
		}
		else if (header.getType() == PLAYER_MSGTYPE_DATA && header.getSubtype() ==  PLAYER_IMU_DATA_EULER){
			PlayerImuDataEuler euler = (PlayerImuDataEuler)data;
			calibData = euler.getCalibData();
			pose.setProll(euler.getOrientation().getProll());
			pose.setPpitch(euler.getOrientation().getPpitch());
			pose.setPyaw(euler.getOrientation().getPyaw());
		}
		else{
			System.err.println("Skipping imu message with unknown type/subtype.");
		}
		
	}
}