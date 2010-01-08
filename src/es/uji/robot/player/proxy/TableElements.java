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

import es.uji.robot.player.generated.value.*;

public class TableElements{
    private ArrayList<TableObject> elements = new ArrayList<TableObject>();

    public ArrayList<TableObject> getElements(){
        return this.elements;
    }
    public void setElements(ArrayList<TableObject> elements){
        this.elements = elements;
    }

    public TableElements(){
        elements.add(new TableObject((short)Position3d.PLAYER_POSITION3D_CODE ,(char)Position3d.PLAYER_MSGTYPE_DATA ,(char)Position3d.PLAYER_POSITION3D_DATA_STATE ,new PlayerPosition3dData()));
        elements.add(new TableObject((short)Position3d.PLAYER_POSITION3D_CODE ,(char)Position3d.PLAYER_MSGTYPE_DATA ,(char)Position3d.PLAYER_POSITION3D_DATA_GEOMETRY ,new PlayerPosition3dGeom()));
        elements.add(new TableObject((short)Position3d.PLAYER_POSITION3D_CODE ,(char)Position3d.PLAYER_MSGTYPE_CMD ,(char)Position3d.PLAYER_POSITION3D_CMD_SET_VEL ,new PlayerPosition3dCmdVel()));
        elements.add(new TableObject((short)Position3d.PLAYER_POSITION3D_CODE ,(char)Position3d.PLAYER_MSGTYPE_CMD ,(char)Position3d.PLAYER_POSITION3D_CMD_SET_POS ,new PlayerPosition3dCmdPos()));
        elements.add(new TableObject((short)Position3d.PLAYER_POSITION3D_CODE ,(char)Position3d.PLAYER_MSGTYPE_REQ ,(char)Position3d.PLAYER_POSITION3D_REQ_GET_GEOM ,new PlayerPosition3dGeom()));
        elements.add(new TableObject((short)Position3d.PLAYER_POSITION3D_CODE ,(char)Position3d.PLAYER_MSGTYPE_REQ ,(char)Position3d.PLAYER_POSITION3D_REQ_MOTOR_POWER ,new PlayerPosition3dPowerConfig()));
        elements.add(new TableObject((short)Position3d.PLAYER_POSITION3D_CODE ,(char)Position3d.PLAYER_MSGTYPE_REQ ,(char)Position3d.PLAYER_POSITION3D_REQ_VELOCITY_MODE ,new PlayerPosition3dVelocityModeConfig()));
        elements.add(new TableObject((short)Position3d.PLAYER_POSITION3D_CODE ,(char)Position3d.PLAYER_MSGTYPE_REQ ,(char)Position3d.PLAYER_POSITION3D_REQ_POSITION_MODE ,new PlayerPosition3dPositionModeReq()));
        elements.add(new TableObject((short)Position3d.PLAYER_POSITION3D_CODE ,(char)Position3d.PLAYER_MSGTYPE_REQ ,(char)Position3d.PLAYER_POSITION3D_REQ_RESET_ODOM ,new PlayerPosition3dSetOdomReq()));
        elements.add(new TableObject((short)Position3d.PLAYER_POSITION3D_CODE ,(char)Position3d.PLAYER_MSGTYPE_REQ ,(char)Position3d.PLAYER_POSITION3D_REQ_SET_ODOM ,new PlayerPosition3dSetOdomReq()));
        elements.add(new TableObject((short)Position3d.PLAYER_POSITION3D_CODE ,(char)Position3d.PLAYER_MSGTYPE_REQ ,(char)Position3d.PLAYER_POSITION3D_REQ_SPEED_PID ,new PlayerPosition3dSpeedPidReq()));
        elements.add(new TableObject((short)Position3d.PLAYER_POSITION3D_CODE ,(char)Position3d.PLAYER_MSGTYPE_REQ ,(char)Position3d.PLAYER_POSITION3D_REQ_POSITION_PID ,new PlayerPosition3dPositionPidReq()));
        elements.add(new TableObject((short)Position3d.PLAYER_POSITION3D_CODE ,(char)Position3d.PLAYER_MSGTYPE_REQ ,(char)Position3d.PLAYER_POSITION3D_REQ_SPEED_PROF ,new PlayerPosition3dSpeedProfReq()));
        elements.add(new TableObject((short)Mcom.PLAYER_MCOM_CODE ,(char)Mcom.PLAYER_MSGTYPE_REQ ,(char)Mcom.PLAYER_MCOM_REQ_PUSH ,new PlayerMcomConfig()));
        elements.add(new TableObject((short)Mcom.PLAYER_MCOM_CODE ,(char)Mcom.PLAYER_MSGTYPE_REQ ,(char)Mcom.PLAYER_MCOM_REQ_POP ,new PlayerMcomReturn()));
        elements.add(new TableObject((short)Mcom.PLAYER_MCOM_CODE ,(char)Mcom.PLAYER_MSGTYPE_REQ ,(char)Mcom.PLAYER_MCOM_REQ_READ ,new PlayerMcomReturn()));
        elements.add(new TableObject((short)Mcom.PLAYER_MCOM_CODE ,(char)Mcom.PLAYER_MSGTYPE_REQ ,(char)Mcom.PLAYER_MCOM_REQ_CLEAR ,new PlayerMcomConfig()));
        elements.add(new TableObject((short)Mcom.PLAYER_MCOM_CODE ,(char)Mcom.PLAYER_MSGTYPE_REQ ,(char)Mcom.PLAYER_MCOM_REQ_SET_CAPACITY ,new PlayerMcomConfig()));
        elements.add(new TableObject((short)Ir.PLAYER_IR_CODE ,(char)Ir.PLAYER_MSGTYPE_REQ ,(char)Ir.PLAYER_IR_REQ_POSE ,new PlayerIrPose()));
        elements.add(new TableObject((short)Ir.PLAYER_IR_CODE ,(char)Ir.PLAYER_MSGTYPE_REQ ,(char)Ir.PLAYER_IR_REQ_POWER ,new PlayerIrPowerReq()));
        elements.add(new TableObject((short)Ir.PLAYER_IR_CODE ,(char)Ir.PLAYER_MSGTYPE_DATA ,(char)Ir.PLAYER_IR_DATA_RANGES ,new PlayerIrData()));
        elements.add(new TableObject((short)Simulation.PLAYER_SIMULATION_CODE ,(char)Simulation.PLAYER_MSGTYPE_REQ ,(char)Simulation.PLAYER_SIMULATION_REQ_GET_POSE2D ,new PlayerSimulationPose2dReq()));
        elements.add(new TableObject((short)Simulation.PLAYER_SIMULATION_CODE ,(char)Simulation.PLAYER_MSGTYPE_REQ ,(char)Simulation.PLAYER_SIMULATION_REQ_SET_POSE2D ,new PlayerSimulationPose2dReq()));
        elements.add(new TableObject((short)Simulation.PLAYER_SIMULATION_CODE ,(char)Simulation.PLAYER_MSGTYPE_REQ ,(char)Simulation.PLAYER_SIMULATION_REQ_GET_POSE3D ,new PlayerSimulationPose3dReq()));
        elements.add(new TableObject((short)Simulation.PLAYER_SIMULATION_CODE ,(char)Simulation.PLAYER_MSGTYPE_REQ ,(char)Simulation.PLAYER_SIMULATION_REQ_SET_POSE3D ,new PlayerSimulationPose3dReq()));
        elements.add(new TableObject((short)Simulation.PLAYER_SIMULATION_CODE ,(char)Simulation.PLAYER_MSGTYPE_REQ ,(char)Simulation.PLAYER_SIMULATION_REQ_GET_PROPERTY ,new PlayerSimulationPropertyReq()));
        elements.add(new TableObject((short)Simulation.PLAYER_SIMULATION_CODE ,(char)Simulation.PLAYER_MSGTYPE_REQ ,(char)Simulation.PLAYER_SIMULATION_REQ_SET_PROPERTY ,new PlayerSimulationPropertyReq()));
        elements.add(new TableObject((short)Graphics3d.PLAYER_GRAPHICS3D_CODE ,(char)Graphics3d.PLAYER_MSGTYPE_CMD ,(char)Graphics3d.PLAYER_GRAPHICS3D_CMD_CLEAR ,new PlayerNull()));
        elements.add(new TableObject((short)Graphics3d.PLAYER_GRAPHICS3D_CODE ,(char)Graphics3d.PLAYER_MSGTYPE_CMD ,(char)Graphics3d.PLAYER_GRAPHICS3D_CMD_DRAW ,new PlayerGraphics3dCmdDraw()));
        elements.add(new TableObject((short)Graphics3d.PLAYER_GRAPHICS3D_CODE ,(char)Graphics3d.PLAYER_MSGTYPE_CMD ,(char)Graphics3d.PLAYER_GRAPHICS3D_CMD_TRANSLATE ,new PlayerGraphics3dCmdTranslate()));
        elements.add(new TableObject((short)Graphics3d.PLAYER_GRAPHICS3D_CODE ,(char)Graphics3d.PLAYER_MSGTYPE_CMD ,(char)Graphics3d.PLAYER_GRAPHICS3D_CMD_ROTATE ,new PlayerGraphics3dCmdRotate()));
        elements.add(new TableObject((short)Graphics3d.PLAYER_GRAPHICS3D_CODE ,(char)Graphics3d.PLAYER_MSGTYPE_CMD ,(char)Graphics3d.PLAYER_GRAPHICS3D_CMD_PUSH ,new PlayerNull()));
        elements.add(new TableObject((short)Graphics3d.PLAYER_GRAPHICS3D_CODE ,(char)Graphics3d.PLAYER_MSGTYPE_CMD ,(char)Graphics3d.PLAYER_GRAPHICS3D_CMD_POP ,new PlayerNull()));
        elements.add(new TableObject((short)Sonar.PLAYER_SONAR_CODE ,(char)Sonar.PLAYER_MSGTYPE_REQ ,(char)Sonar.PLAYER_SONAR_REQ_GET_GEOM ,new PlayerSonarGeom()));
        elements.add(new TableObject((short)Sonar.PLAYER_SONAR_CODE ,(char)Sonar.PLAYER_MSGTYPE_REQ ,(char)Sonar.PLAYER_SONAR_REQ_POWER ,new PlayerSonarPowerConfig()));
        elements.add(new TableObject((short)Sonar.PLAYER_SONAR_CODE ,(char)Sonar.PLAYER_MSGTYPE_DATA ,(char)Sonar.PLAYER_SONAR_DATA_RANGES ,new PlayerSonarData()));
        elements.add(new TableObject((short)Sonar.PLAYER_SONAR_CODE ,(char)Sonar.PLAYER_MSGTYPE_DATA ,(char)Sonar.PLAYER_SONAR_DATA_GEOM ,new PlayerSonarGeom()));
        elements.add(new TableObject((short)Position2d.PLAYER_POSITION2D_CODE ,(char)Position2d.PLAYER_MSGTYPE_REQ ,(char)Position2d.PLAYER_POSITION2D_REQ_GET_GEOM ,new PlayerPosition2dGeom()));
        elements.add(new TableObject((short)Position2d.PLAYER_POSITION2D_CODE ,(char)Position2d.PLAYER_MSGTYPE_REQ ,(char)Position2d.PLAYER_POSITION2D_REQ_MOTOR_POWER ,new PlayerPosition2dPowerConfig()));
        elements.add(new TableObject((short)Position2d.PLAYER_POSITION2D_CODE ,(char)Position2d.PLAYER_MSGTYPE_REQ ,(char)Position2d.PLAYER_POSITION2D_REQ_VELOCITY_MODE ,new PlayerPosition2dVelocityModeConfig()));
        elements.add(new TableObject((short)Position2d.PLAYER_POSITION2D_CODE ,(char)Position2d.PLAYER_MSGTYPE_REQ ,(char)Position2d.PLAYER_POSITION2D_REQ_POSITION_MODE ,new PlayerPosition2dPositionModeReq()));
        elements.add(new TableObject((short)Position2d.PLAYER_POSITION2D_CODE ,(char)Position2d.PLAYER_MSGTYPE_REQ ,(char)Position2d.PLAYER_POSITION2D_REQ_SET_ODOM ,new PlayerPosition2dSetOdomReq()));
        elements.add(new TableObject((short)Position2d.PLAYER_POSITION2D_CODE ,(char)Position2d.PLAYER_MSGTYPE_REQ ,(char)Position2d.PLAYER_POSITION2D_REQ_RESET_ODOM ,new PlayerNull()));
        elements.add(new TableObject((short)Position2d.PLAYER_POSITION2D_CODE ,(char)Position2d.PLAYER_MSGTYPE_REQ ,(char)Position2d.PLAYER_POSITION2D_REQ_SPEED_PID ,new PlayerPosition2dSpeedPidReq()));
        elements.add(new TableObject((short)Position2d.PLAYER_POSITION2D_CODE ,(char)Position2d.PLAYER_MSGTYPE_REQ ,(char)Position2d.PLAYER_POSITION2D_REQ_POSITION_PID ,new PlayerPosition2dPositionPidReq()));
        elements.add(new TableObject((short)Position2d.PLAYER_POSITION2D_CODE ,(char)Position2d.PLAYER_MSGTYPE_REQ ,(char)Position2d.PLAYER_POSITION2D_REQ_SPEED_PROF ,new PlayerPosition2dSpeedProfReq()));
        elements.add(new TableObject((short)Position2d.PLAYER_POSITION2D_CODE ,(char)Position2d.PLAYER_MSGTYPE_DATA ,(char)Position2d.PLAYER_POSITION2D_DATA_STATE ,new PlayerPosition2dData()));
        elements.add(new TableObject((short)Position2d.PLAYER_POSITION2D_CODE ,(char)Position2d.PLAYER_MSGTYPE_DATA ,(char)Position2d.PLAYER_POSITION2D_DATA_GEOM ,new PlayerPosition2dGeom()));
        elements.add(new TableObject((short)Position2d.PLAYER_POSITION2D_CODE ,(char)Position2d.PLAYER_MSGTYPE_CMD ,(char)Position2d.PLAYER_POSITION2D_CMD_VEL ,new PlayerPosition2dCmdVel()));
        elements.add(new TableObject((short)Position2d.PLAYER_POSITION2D_CODE ,(char)Position2d.PLAYER_MSGTYPE_CMD ,(char)Position2d.PLAYER_POSITION2D_CMD_POS ,new PlayerPosition2dCmdPos()));
        elements.add(new TableObject((short)Position2d.PLAYER_POSITION2D_CODE ,(char)Position2d.PLAYER_MSGTYPE_CMD ,(char)Position2d.PLAYER_POSITION2D_CMD_CAR ,new PlayerPosition2dCmdCar()));
        elements.add(new TableObject((short)Position2d.PLAYER_POSITION2D_CODE ,(char)Position2d.PLAYER_MSGTYPE_CMD ,(char)Position2d.PLAYER_POSITION2D_CMD_VEL_HEAD ,new PlayerPosition2dCmdVelHead()));
        elements.add(new TableObject((short)Ranger.PLAYER_RANGER_CODE ,(char)Ranger.PLAYER_MSGTYPE_DATA ,(char)Ranger.PLAYER_RANGER_DATA_RANGE ,new PlayerRangerDataRange()));
        elements.add(new TableObject((short)Ranger.PLAYER_RANGER_CODE ,(char)Ranger.PLAYER_MSGTYPE_DATA ,(char)Ranger.PLAYER_RANGER_DATA_RANGEPOSE ,new PlayerRangerDataRangepose()));
        elements.add(new TableObject((short)Ranger.PLAYER_RANGER_CODE ,(char)Ranger.PLAYER_MSGTYPE_DATA ,(char)Ranger.PLAYER_RANGER_DATA_INTNS ,new PlayerRangerDataIntns()));
        elements.add(new TableObject((short)Ranger.PLAYER_RANGER_CODE ,(char)Ranger.PLAYER_MSGTYPE_DATA ,(char)Ranger.PLAYER_RANGER_DATA_INTNSPOSE ,new PlayerRangerDataIntnspose()));
        elements.add(new TableObject((short)Ranger.PLAYER_RANGER_CODE ,(char)Ranger.PLAYER_MSGTYPE_DATA ,(char)Ranger.PLAYER_RANGER_DATA_GEOM ,new PlayerRangerGeom()));
        elements.add(new TableObject((short)Ranger.PLAYER_RANGER_CODE ,(char)Ranger.PLAYER_MSGTYPE_REQ ,(char)Ranger.PLAYER_RANGER_REQ_GET_GEOM ,new PlayerRangerGeom()));
        elements.add(new TableObject((short)Ranger.PLAYER_RANGER_CODE ,(char)Ranger.PLAYER_MSGTYPE_REQ ,(char)Ranger.PLAYER_RANGER_REQ_POWER ,new PlayerRangerPowerConfig()));
        elements.add(new TableObject((short)Ranger.PLAYER_RANGER_CODE ,(char)Ranger.PLAYER_MSGTYPE_REQ ,(char)Ranger.PLAYER_RANGER_REQ_INTNS ,new PlayerRangerIntnsConfig()));
        elements.add(new TableObject((short)Ranger.PLAYER_RANGER_CODE ,(char)Ranger.PLAYER_MSGTYPE_REQ ,(char)Ranger.PLAYER_RANGER_REQ_SET_CONFIG ,new PlayerRangerConfig()));
        elements.add(new TableObject((short)Ranger.PLAYER_RANGER_CODE ,(char)Ranger.PLAYER_MSGTYPE_REQ ,(char)Ranger.PLAYER_RANGER_REQ_GET_CONFIG ,new PlayerRangerConfig()));
        elements.add(new TableObject((short)Health.PLAYER_HEALTH_CODE ,(char)Health.PLAYER_MSGTYPE_DATA ,(char)Health.PLAYER_HEALTH_DATA_STATE ,new PlayerHealthData()));
        elements.add(new TableObject((short)Graphics2d.PLAYER_GRAPHICS2D_CODE ,(char)Graphics2d.PLAYER_MSGTYPE_CMD ,(char)Graphics2d.PLAYER_GRAPHICS2D_CMD_CLEAR ,new PlayerNull()));
        elements.add(new TableObject((short)Graphics2d.PLAYER_GRAPHICS2D_CODE ,(char)Graphics2d.PLAYER_MSGTYPE_CMD ,(char)Graphics2d.PLAYER_GRAPHICS2D_CMD_POINTS ,new PlayerGraphics2dCmdPoints()));
        elements.add(new TableObject((short)Graphics2d.PLAYER_GRAPHICS2D_CODE ,(char)Graphics2d.PLAYER_MSGTYPE_CMD ,(char)Graphics2d.PLAYER_GRAPHICS2D_CMD_POLYLINE ,new PlayerGraphics2dCmdPolyline()));
        elements.add(new TableObject((short)Graphics2d.PLAYER_GRAPHICS2D_CODE ,(char)Graphics2d.PLAYER_MSGTYPE_CMD ,(char)Graphics2d.PLAYER_GRAPHICS2D_CMD_POLYGON ,new PlayerGraphics2dCmdPolygon()));
        elements.add(new TableObject((short)Fiducial.PLAYER_FIDUCIAL_CODE ,(char)Fiducial.PLAYER_MSGTYPE_DATA ,(char)Fiducial.PLAYER_FIDUCIAL_DATA_SCAN ,new PlayerFiducialData()));
        elements.add(new TableObject((short)Fiducial.PLAYER_FIDUCIAL_CODE ,(char)Fiducial.PLAYER_MSGTYPE_REQ ,(char)Fiducial.PLAYER_FIDUCIAL_REQ_GET_GEOM ,new PlayerFiducialGeom()));
        elements.add(new TableObject((short)Fiducial.PLAYER_FIDUCIAL_CODE ,(char)Fiducial.PLAYER_MSGTYPE_REQ ,(char)Fiducial.PLAYER_FIDUCIAL_REQ_GET_FOV ,new PlayerFiducialFov()));
        elements.add(new TableObject((short)Fiducial.PLAYER_FIDUCIAL_CODE ,(char)Fiducial.PLAYER_MSGTYPE_REQ ,(char)Fiducial.PLAYER_FIDUCIAL_REQ_SET_FOV ,new PlayerFiducialFov()));
        elements.add(new TableObject((short)Fiducial.PLAYER_FIDUCIAL_CODE ,(char)Fiducial.PLAYER_MSGTYPE_REQ ,(char)Fiducial.PLAYER_FIDUCIAL_REQ_GET_ID ,new PlayerFiducialId()));
        elements.add(new TableObject((short)Fiducial.PLAYER_FIDUCIAL_CODE ,(char)Fiducial.PLAYER_MSGTYPE_REQ ,(char)Fiducial.PLAYER_FIDUCIAL_REQ_SET_ID ,new PlayerFiducialId()));
        elements.add(new TableObject((short)Audio.PLAYER_AUDIO_CODE ,(char)Audio.PLAYER_MSGTYPE_DATA ,(char)Audio.PLAYER_AUDIO_DATA_WAV_REC ,new PlayerAudioWav()));
        elements.add(new TableObject((short)Audio.PLAYER_AUDIO_CODE ,(char)Audio.PLAYER_MSGTYPE_DATA ,(char)Audio.PLAYER_AUDIO_DATA_SEQ ,new PlayerAudioSeq()));
        elements.add(new TableObject((short)Audio.PLAYER_AUDIO_CODE ,(char)Audio.PLAYER_MSGTYPE_DATA ,(char)Audio.PLAYER_AUDIO_DATA_MIXER_CHANNEL ,new PlayerAudioMixerChannelList()));
        elements.add(new TableObject((short)Audio.PLAYER_AUDIO_CODE ,(char)Audio.PLAYER_MSGTYPE_DATA ,(char)Audio.PLAYER_AUDIO_DATA_STATE ,new PlayerAudioState()));
        elements.add(new TableObject((short)Audio.PLAYER_AUDIO_CODE ,(char)Audio.PLAYER_MSGTYPE_CMD ,(char)Audio.PLAYER_AUDIO_CMD_WAV_PLAY ,new PlayerAudioWav()));
        elements.add(new TableObject((short)Audio.PLAYER_AUDIO_CODE ,(char)Audio.PLAYER_MSGTYPE_CMD ,(char)Audio.PLAYER_AUDIO_CMD_WAV_STREAM_REC ,new PlayerBool()));
        elements.add(new TableObject((short)Audio.PLAYER_AUDIO_CODE ,(char)Audio.PLAYER_MSGTYPE_CMD ,(char)Audio.PLAYER_AUDIO_CMD_SAMPLE_PLAY ,new PlayerAudioSampleItem()));
        elements.add(new TableObject((short)Audio.PLAYER_AUDIO_CODE ,(char)Audio.PLAYER_MSGTYPE_CMD ,(char)Audio.PLAYER_AUDIO_CMD_SEQ_PLAY ,new PlayerAudioSeq()));
        elements.add(new TableObject((short)Audio.PLAYER_AUDIO_CODE ,(char)Audio.PLAYER_MSGTYPE_CMD ,(char)Audio.PLAYER_AUDIO_CMD_MIXER_CHANNEL ,new PlayerAudioMixerChannelList()));
        elements.add(new TableObject((short)Audio.PLAYER_AUDIO_CODE ,(char)Audio.PLAYER_MSGTYPE_REQ ,(char)Audio.PLAYER_AUDIO_REQ_WAV_REC ,new PlayerAudioWav()));
        elements.add(new TableObject((short)Audio.PLAYER_AUDIO_CODE ,(char)Audio.PLAYER_MSGTYPE_REQ ,(char)Audio.PLAYER_AUDIO_REQ_SAMPLE_LOAD ,new PlayerAudioSample()));
        elements.add(new TableObject((short)Audio.PLAYER_AUDIO_CODE ,(char)Audio.PLAYER_MSGTYPE_REQ ,(char)Audio.PLAYER_AUDIO_REQ_SAMPLE_RETRIEVE ,new PlayerAudioSample()));
        elements.add(new TableObject((short)Audio.PLAYER_AUDIO_CODE ,(char)Audio.PLAYER_MSGTYPE_REQ ,(char)Audio.PLAYER_AUDIO_REQ_SAMPLE_REC ,new PlayerAudioSampleItem()));
        elements.add(new TableObject((short)Audio.PLAYER_AUDIO_CODE ,(char)Audio.PLAYER_MSGTYPE_REQ ,(char)Audio.PLAYER_AUDIO_REQ_MIXER_CHANNEL_LIST ,new PlayerAudioMixerChannelListDetail()));
        elements.add(new TableObject((short)Audio.PLAYER_AUDIO_CODE ,(char)Audio.PLAYER_MSGTYPE_REQ ,(char)Audio.PLAYER_AUDIO_REQ_MIXER_CHANNEL_LEVEL ,new PlayerAudioMixerChannelList()));
        elements.add(new TableObject((short)Opaque.PLAYER_OPAQUE_CODE ,(char)Opaque.PLAYER_MSGTYPE_DATA ,(char)Opaque.PLAYER_OPAQUE_DATA_STATE ,new PlayerOpaqueData()));
        elements.add(new TableObject((short)Opaque.PLAYER_OPAQUE_CODE ,(char)Opaque.PLAYER_MSGTYPE_CMD ,(char)Opaque.PLAYER_OPAQUE_CMD_DATA ,new PlayerOpaqueData()));
        elements.add(new TableObject((short)Opaque.PLAYER_OPAQUE_CODE ,(char)Opaque.PLAYER_MSGTYPE_REQ ,(char)Opaque.PLAYER_OPAQUE_REQ_DATA ,new PlayerOpaqueData()));
        elements.add(new TableObject((short)Blackboard.PLAYER_BLACKBOARD_CODE ,(char)Blackboard.PLAYER_MSGTYPE_REQ ,(char)Blackboard.PLAYER_BLACKBOARD_REQ_SUBSCRIBE_TO_KEY ,new PlayerBlackboardEntry()));
        elements.add(new TableObject((short)Blackboard.PLAYER_BLACKBOARD_CODE ,(char)Blackboard.PLAYER_MSGTYPE_REQ ,(char)Blackboard.PLAYER_BLACKBOARD_REQ_UNSUBSCRIBE_FROM_KEY ,new PlayerBlackboardEntry()));
        elements.add(new TableObject((short)Blackboard.PLAYER_BLACKBOARD_CODE ,(char)Blackboard.PLAYER_MSGTYPE_REQ ,(char)Blackboard.PLAYER_BLACKBOARD_REQ_SET_ENTRY ,new PlayerBlackboardEntry()));
        elements.add(new TableObject((short)Blackboard.PLAYER_BLACKBOARD_CODE ,(char)Blackboard.PLAYER_MSGTYPE_REQ ,(char)Blackboard.PLAYER_BLACKBOARD_REQ_SUBSCRIBE_TO_GROUP ,new PlayerBlackboardEntry()));
        elements.add(new TableObject((short)Blackboard.PLAYER_BLACKBOARD_CODE ,(char)Blackboard.PLAYER_MSGTYPE_REQ ,(char)Blackboard.PLAYER_BLACKBOARD_REQ_UNSUBSCRIBE_FROM_GROUP ,new PlayerBlackboardEntry()));
        elements.add(new TableObject((short)Blackboard.PLAYER_BLACKBOARD_CODE ,(char)Blackboard.PLAYER_MSGTYPE_REQ ,(char)Blackboard.PLAYER_BLACKBOARD_REQ_GET_ENTRY ,new PlayerBlackboardEntry()));
        elements.add(new TableObject((short)Blackboard.PLAYER_BLACKBOARD_CODE ,(char)Blackboard.PLAYER_MSGTYPE_DATA ,(char)Blackboard.PLAYER_BLACKBOARD_DATA_UPDATE ,new PlayerBlackboardEntry()));
        elements.add(new TableObject((short)Wsn.PLAYER_WSN_CODE ,(char)Wsn.PLAYER_MSGTYPE_DATA ,(char)Wsn.PLAYER_WSN_DATA_STATE ,new PlayerWsnData()));
        elements.add(new TableObject((short)Wsn.PLAYER_WSN_CODE ,(char)Wsn.PLAYER_MSGTYPE_CMD ,(char)Wsn.PLAYER_WSN_CMD_DEVSTATE ,new PlayerWsnCmd()));
        elements.add(new TableObject((short)Wsn.PLAYER_WSN_CODE ,(char)Wsn.PLAYER_MSGTYPE_REQ ,(char)Wsn.PLAYER_WSN_REQ_POWER ,new PlayerWsnPowerConfig()));
        elements.add(new TableObject((short)Wsn.PLAYER_WSN_CODE ,(char)Wsn.PLAYER_MSGTYPE_REQ ,(char)Wsn.PLAYER_WSN_REQ_DATATYPE ,new PlayerWsnDatatypeConfig()));
        elements.add(new TableObject((short)Wsn.PLAYER_WSN_CODE ,(char)Wsn.PLAYER_MSGTYPE_REQ ,(char)Wsn.PLAYER_WSN_REQ_DATAFREQ ,new PlayerWsnDatafreqConfig()));
        elements.add(new TableObject((short)Map.PLAYER_MAP_CODE ,(char)Map.PLAYER_MSGTYPE_DATA ,(char)Map.PLAYER_MAP_DATA_INFO ,new PlayerMapInfo()));
        elements.add(new TableObject((short)Map.PLAYER_MAP_CODE ,(char)Map.PLAYER_MSGTYPE_REQ ,(char)Map.PLAYER_MAP_REQ_GET_INFO ,new PlayerMapInfo()));
        elements.add(new TableObject((short)Map.PLAYER_MAP_CODE ,(char)Map.PLAYER_MSGTYPE_REQ ,(char)Map.PLAYER_MAP_REQ_GET_DATA ,new PlayerMapData()));
        elements.add(new TableObject((short)Map.PLAYER_MAP_CODE ,(char)Map.PLAYER_MSGTYPE_REQ ,(char)Map.PLAYER_MAP_REQ_GET_VECTOR ,new PlayerMapDataVector()));
        elements.add(new TableObject((short)Actarray.PLAYER_ACTARRAY_CODE ,(char)Actarray.PLAYER_MSGTYPE_REQ ,(char)Actarray.PLAYER_ACTARRAY_REQ_POWER ,new PlayerActarrayPowerConfig()));
        elements.add(new TableObject((short)Actarray.PLAYER_ACTARRAY_CODE ,(char)Actarray.PLAYER_MSGTYPE_REQ ,(char)Actarray.PLAYER_ACTARRAY_REQ_BRAKES ,new PlayerActarrayBrakesConfig()));
        elements.add(new TableObject((short)Actarray.PLAYER_ACTARRAY_CODE ,(char)Actarray.PLAYER_MSGTYPE_REQ ,(char)Actarray.PLAYER_ACTARRAY_REQ_GET_GEOM ,new PlayerActarrayGeom()));
        elements.add(new TableObject((short)Actarray.PLAYER_ACTARRAY_CODE ,(char)Actarray.PLAYER_MSGTYPE_REQ ,(char)Actarray.PLAYER_ACTARRAY_REQ_SPEED ,new PlayerActarraySpeedConfig()));
        elements.add(new TableObject((short)Actarray.PLAYER_ACTARRAY_CODE ,(char)Actarray.PLAYER_MSGTYPE_REQ ,(char)Actarray.PLAYER_ACTARRAY_REQ_ACCEL ,new PlayerActarrayAccelConfigValue()));
        elements.add(new TableObject((short)Actarray.PLAYER_ACTARRAY_CODE ,(char)Actarray.PLAYER_MSGTYPE_CMD ,(char)Actarray.PLAYER_ACTARRAY_CMD_POS ,new PlayerActarrayPositionCmd()));
        elements.add(new TableObject((short)Actarray.PLAYER_ACTARRAY_CODE ,(char)Actarray.PLAYER_MSGTYPE_CMD ,(char)Actarray.PLAYER_ACTARRAY_CMD_MULTI_POS ,new PlayerActarrayMultiPositionCmd()));
        elements.add(new TableObject((short)Actarray.PLAYER_ACTARRAY_CODE ,(char)Actarray.PLAYER_MSGTYPE_CMD ,(char)Actarray.PLAYER_ACTARRAY_CMD_SPEED ,new PlayerActarraySpeedCmd()));
        elements.add(new TableObject((short)Actarray.PLAYER_ACTARRAY_CODE ,(char)Actarray.PLAYER_MSGTYPE_CMD ,(char)Actarray.PLAYER_ACTARRAY_CMD_MULTI_SPEED ,new PlayerActarrayMultiSpeedCmd()));
        elements.add(new TableObject((short)Actarray.PLAYER_ACTARRAY_CODE ,(char)Actarray.PLAYER_MSGTYPE_CMD ,(char)Actarray.PLAYER_ACTARRAY_CMD_HOME ,new PlayerActarrayHomeCmd()));
        elements.add(new TableObject((short)Actarray.PLAYER_ACTARRAY_CODE ,(char)Actarray.PLAYER_MSGTYPE_CMD ,(char)Actarray.PLAYER_ACTARRAY_CMD_CURRENT ,new PlayerActarrayCurrentCmd()));
        elements.add(new TableObject((short)Actarray.PLAYER_ACTARRAY_CODE ,(char)Actarray.PLAYER_MSGTYPE_CMD ,(char)Actarray.PLAYER_ACTARRAY_CMD_MULTI_CURRENT ,new PlayerActarrayMultiCurrentCmd()));
        elements.add(new TableObject((short)Actarray.PLAYER_ACTARRAY_CODE ,(char)Actarray.PLAYER_MSGTYPE_DATA ,(char)Actarray.PLAYER_ACTARRAY_DATA_STATE ,new PlayerActarrayData()));
        elements.add(new TableObject((short)Log.PLAYER_LOG_CODE ,(char)Log.PLAYER_MSGTYPE_REQ ,(char)Log.PLAYER_LOG_REQ_SET_WRITE_STATE ,new PlayerLogSetWriteState()));
        elements.add(new TableObject((short)Log.PLAYER_LOG_CODE ,(char)Log.PLAYER_MSGTYPE_REQ ,(char)Log.PLAYER_LOG_REQ_SET_READ_STATE ,new PlayerLogSetReadState()));
        elements.add(new TableObject((short)Log.PLAYER_LOG_CODE ,(char)Log.PLAYER_MSGTYPE_REQ ,(char)Log.PLAYER_LOG_REQ_GET_STATE ,new PlayerLogGetState()));
        elements.add(new TableObject((short)Log.PLAYER_LOG_CODE ,(char)Log.PLAYER_MSGTYPE_REQ ,(char)Log.PLAYER_LOG_REQ_SET_READ_REWIND ,new PlayerLogSetReadRewind()));
        elements.add(new TableObject((short)Log.PLAYER_LOG_CODE ,(char)Log.PLAYER_MSGTYPE_REQ ,(char)Log.PLAYER_LOG_REQ_SET_FILENAME ,new PlayerLogSetFilename()));
        elements.add(new TableObject((short)Pointcloud3d.PLAYER_POINTCLOUD3D_CODE ,(char)Pointcloud3d.PLAYER_MSGTYPE_DATA ,(char)Pointcloud3d.PLAYER_POINTCLOUD3D_DATA_STATE ,new PlayerPointcloud3dData()));
        elements.add(new TableObject((short)Blinkenlight.PLAYER_BLINKENLIGHT_CODE ,(char)Blinkenlight.PLAYER_MSGTYPE_DATA ,(char)Blinkenlight.PLAYER_BLINKENLIGHT_DATA_STATE ,new PlayerBlinkenlightData()));
        elements.add(new TableObject((short)Blinkenlight.PLAYER_BLINKENLIGHT_CODE ,(char)Blinkenlight.PLAYER_MSGTYPE_CMD ,(char)Blinkenlight.PLAYER_BLINKENLIGHT_CMD_STATE ,new PlayerBlinkenlightCmd()));
        elements.add(new TableObject((short)Blinkenlight.PLAYER_BLINKENLIGHT_CODE ,(char)Blinkenlight.PLAYER_MSGTYPE_CMD ,(char)Blinkenlight.PLAYER_BLINKENLIGHT_CMD_POWER ,new PlayerBlinkenlightCmdPower()));
        elements.add(new TableObject((short)Blinkenlight.PLAYER_BLINKENLIGHT_CODE ,(char)Blinkenlight.PLAYER_MSGTYPE_CMD ,(char)Blinkenlight.PLAYER_BLINKENLIGHT_CMD_COLOR ,new PlayerBlinkenlightCmdColor()));
        elements.add(new TableObject((short)Blinkenlight.PLAYER_BLINKENLIGHT_CODE ,(char)Blinkenlight.PLAYER_MSGTYPE_CMD ,(char)Blinkenlight.PLAYER_BLINKENLIGHT_CMD_FLASH ,new PlayerBlinkenlightCmdFlash()));
        elements.add(new TableObject((short)Localize.PLAYER_LOCALIZE_CODE ,(char)Localize.PLAYER_MSGTYPE_DATA ,(char)Localize.PLAYER_LOCALIZE_DATA_HYPOTHS ,new PlayerLocalizeData()));
        elements.add(new TableObject((short)Localize.PLAYER_LOCALIZE_CODE ,(char)Localize.PLAYER_MSGTYPE_REQ ,(char)Localize.PLAYER_LOCALIZE_REQ_SET_POSE ,new PlayerLocalizeSetPose()));
        elements.add(new TableObject((short)Localize.PLAYER_LOCALIZE_CODE ,(char)Localize.PLAYER_MSGTYPE_REQ ,(char)Localize.PLAYER_LOCALIZE_REQ_GET_PARTICLES ,new PlayerLocalizeGetParticles()));
        elements.add(new TableObject((short)Rfid.PLAYER_RFID_CODE ,(char)Rfid.PLAYER_MSGTYPE_DATA ,(char)Rfid.PLAYER_RFID_DATA_TAGS ,new PlayerRfidData()));
        elements.add(new TableObject((short)Rfid.PLAYER_RFID_CODE ,(char)Rfid.PLAYER_MSGTYPE_REQ ,(char)Rfid.PLAYER_RFID_REQ_POWER ,new PlayerNull()));
        elements.add(new TableObject((short)Rfid.PLAYER_RFID_CODE ,(char)Rfid.PLAYER_MSGTYPE_REQ ,(char)Rfid.PLAYER_RFID_REQ_READTAG ,new PlayerNull()));
        elements.add(new TableObject((short)Rfid.PLAYER_RFID_CODE ,(char)Rfid.PLAYER_MSGTYPE_REQ ,(char)Rfid.PLAYER_RFID_REQ_WRITETAG ,new PlayerNull()));
        elements.add(new TableObject((short)Rfid.PLAYER_RFID_CODE ,(char)Rfid.PLAYER_MSGTYPE_REQ ,(char)Rfid.PLAYER_RFID_REQ_LOCKTAG ,new PlayerNull()));
        elements.add(new TableObject((short)Planner.PLAYER_PLANNER_CODE ,(char)Planner.PLAYER_MSGTYPE_DATA ,(char)Planner.PLAYER_PLANNER_DATA_STATE ,new PlayerPlannerData()));
        elements.add(new TableObject((short)Planner.PLAYER_PLANNER_CODE ,(char)Planner.PLAYER_MSGTYPE_CMD ,(char)Planner.PLAYER_PLANNER_CMD_GOAL ,new PlayerPlannerCmd()));
        elements.add(new TableObject((short)Planner.PLAYER_PLANNER_CODE ,(char)Planner.PLAYER_MSGTYPE_REQ ,(char)Planner.PLAYER_PLANNER_REQ_GET_WAYPOINTS ,new PlayerPlannerWaypointsReq()));
        elements.add(new TableObject((short)Planner.PLAYER_PLANNER_CODE ,(char)Planner.PLAYER_MSGTYPE_REQ ,(char)Planner.PLAYER_PLANNER_REQ_ENABLE ,new PlayerPlannerEnableReq()));
        elements.add(new TableObject((short)Power.PLAYER_POWER_CODE ,(char)Power.PLAYER_MSGTYPE_DATA ,(char)Power.PLAYER_POWER_DATA_STATE ,new PlayerPowerData()));
        elements.add(new TableObject((short)Power.PLAYER_POWER_CODE ,(char)Power.PLAYER_MSGTYPE_REQ ,(char)Power.PLAYER_POWER_REQ_SET_CHARGING_POLICY_REQ ,new PlayerPowerChargepolicyConfig()));
        elements.add(new TableObject((short)Gripper.PLAYER_GRIPPER_CODE ,(char)Gripper.PLAYER_MSGTYPE_DATA ,(char)Gripper.PLAYER_GRIPPER_DATA_STATE ,new PlayerGripperData()));
        elements.add(new TableObject((short)Gripper.PLAYER_GRIPPER_CODE ,(char)Gripper.PLAYER_MSGTYPE_REQ ,(char)Gripper.PLAYER_GRIPPER_REQ_GET_GEOM ,new PlayerGripperGeom()));
        elements.add(new TableObject((short)Gripper.PLAYER_GRIPPER_CODE ,(char)Gripper.PLAYER_MSGTYPE_CMD ,(char)Gripper.PLAYER_GRIPPER_CMD_OPEN ,new PlayerNull()));
        elements.add(new TableObject((short)Gripper.PLAYER_GRIPPER_CODE ,(char)Gripper.PLAYER_MSGTYPE_CMD ,(char)Gripper.PLAYER_GRIPPER_CMD_CLOSE ,new PlayerNull()));
        elements.add(new TableObject((short)Gripper.PLAYER_GRIPPER_CODE ,(char)Gripper.PLAYER_MSGTYPE_CMD ,(char)Gripper.PLAYER_GRIPPER_CMD_STOP ,new PlayerNull()));
        elements.add(new TableObject((short)Gripper.PLAYER_GRIPPER_CODE ,(char)Gripper.PLAYER_MSGTYPE_CMD ,(char)Gripper.PLAYER_GRIPPER_CMD_STORE ,new PlayerNull()));
        elements.add(new TableObject((short)Gripper.PLAYER_GRIPPER_CODE ,(char)Gripper.PLAYER_MSGTYPE_CMD ,(char)Gripper.PLAYER_GRIPPER_CMD_RETRIEVE ,new PlayerNull()));
        elements.add(new TableObject((short)Position1d.PLAYER_POSITION1D_CODE ,(char)Position1d.PLAYER_MSGTYPE_REQ ,(char)Position1d.PLAYER_POSITION1D_REQ_GET_GEOM ,new PlayerPosition1dGeom()));
        elements.add(new TableObject((short)Position1d.PLAYER_POSITION1D_CODE ,(char)Position1d.PLAYER_MSGTYPE_REQ ,(char)Position1d.PLAYER_POSITION1D_REQ_MOTOR_POWER ,new PlayerPosition1dPowerConfig()));
        elements.add(new TableObject((short)Position1d.PLAYER_POSITION1D_CODE ,(char)Position1d.PLAYER_MSGTYPE_REQ ,(char)Position1d.PLAYER_POSITION1D_REQ_VELOCITY_MODE ,new PlayerPosition1dVelocityModeConfig()));
        elements.add(new TableObject((short)Position1d.PLAYER_POSITION1D_CODE ,(char)Position1d.PLAYER_MSGTYPE_REQ ,(char)Position1d.PLAYER_POSITION1D_REQ_POSITION_MODE ,new PlayerPosition1dPositionModeReq()));
        elements.add(new TableObject((short)Position1d.PLAYER_POSITION1D_CODE ,(char)Position1d.PLAYER_MSGTYPE_REQ ,(char)Position1d.PLAYER_POSITION1D_REQ_SET_ODOM ,new PlayerPosition1dSetOdomReq()));
        elements.add(new TableObject((short)Position1d.PLAYER_POSITION1D_CODE ,(char)Position1d.PLAYER_MSGTYPE_REQ ,(char)Position1d.PLAYER_POSITION1D_REQ_RESET_ODOM ,new PlayerPosition1dResetOdomConfig()));
        elements.add(new TableObject((short)Position1d.PLAYER_POSITION1D_CODE ,(char)Position1d.PLAYER_MSGTYPE_REQ ,(char)Position1d.PLAYER_POSITION1D_REQ_SPEED_PID ,new PlayerPosition1dSpeedPidReq()));
        elements.add(new TableObject((short)Position1d.PLAYER_POSITION1D_CODE ,(char)Position1d.PLAYER_MSGTYPE_REQ ,(char)Position1d.PLAYER_POSITION1D_REQ_POSITION_PID ,new PlayerPosition1dPositionPidReq()));
        elements.add(new TableObject((short)Position1d.PLAYER_POSITION1D_CODE ,(char)Position1d.PLAYER_MSGTYPE_REQ ,(char)Position1d.PLAYER_POSITION1D_REQ_SPEED_PROF ,new PlayerPosition1dSpeedProfReq()));
        elements.add(new TableObject((short)Position1d.PLAYER_POSITION1D_CODE ,(char)Position1d.PLAYER_MSGTYPE_DATA ,(char)Position1d.PLAYER_POSITION1D_DATA_STATE ,new PlayerPosition1dData()));
        elements.add(new TableObject((short)Position1d.PLAYER_POSITION1D_CODE ,(char)Position1d.PLAYER_MSGTYPE_DATA ,(char)Position1d.PLAYER_POSITION1D_DATA_GEOM ,new PlayerPosition1dGeom()));
        elements.add(new TableObject((short)Position1d.PLAYER_POSITION1D_CODE ,(char)Position1d.PLAYER_MSGTYPE_CMD ,(char)Position1d.PLAYER_POSITION1D_CMD_VEL ,new PlayerPosition1dCmdVel()));
        elements.add(new TableObject((short)Position1d.PLAYER_POSITION1D_CODE ,(char)Position1d.PLAYER_MSGTYPE_CMD ,(char)Position1d.PLAYER_POSITION1D_CMD_POS ,new PlayerPosition1dCmdPos()));
        elements.add(new TableObject((short)Blobfinder.PLAYER_BLOBFINDER_CODE ,(char)Blobfinder.PLAYER_MSGTYPE_DATA ,(char)Blobfinder.PLAYER_BLOBFINDER_DATA_BLOBS ,new PlayerBlobfinderData()));
        elements.add(new TableObject((short)Blobfinder.PLAYER_BLOBFINDER_CODE ,(char)Blobfinder.PLAYER_MSGTYPE_REQ ,(char)Blobfinder.PLAYER_BLOBFINDER_REQ_SET_COLOR ,new PlayerBlobfinderColorConfig()));
        elements.add(new TableObject((short)Blobfinder.PLAYER_BLOBFINDER_CODE ,(char)Blobfinder.PLAYER_MSGTYPE_REQ ,(char)Blobfinder.PLAYER_BLOBFINDER_REQ_SET_IMAGER_PARAMS ,new PlayerBlobfinderImagerConfig()));
        elements.add(new TableObject((short)SpeechRecognition.PLAYER_SPEECH_RECOGNITION_CODE ,(char)SpeechRecognition.PLAYER_MSGTYPE_DATA ,(char)SpeechRecognition.PLAYER_SPEECH_RECOGNITION_DATA_STRING ,new PlayerSpeechRecognitionData()));
        elements.add(new TableObject((short)Gps.PLAYER_GPS_CODE ,(char)Gps.PLAYER_MSGTYPE_DATA ,(char)Gps.PLAYER_GPS_DATA_STATE ,new PlayerGpsData()));
        elements.add(new TableObject((short)Wifi.PLAYER_WIFI_CODE ,(char)Wifi.PLAYER_MSGTYPE_REQ ,(char)Wifi.PLAYER_WIFI_REQ_MAC ,new PlayerWifiMacReq()));
        elements.add(new TableObject((short)Wifi.PLAYER_WIFI_CODE ,(char)Wifi.PLAYER_MSGTYPE_REQ ,(char)Wifi.PLAYER_WIFI_REQ_IWSPY_ADD ,new PlayerWifiIwspyAddrReq()));
        elements.add(new TableObject((short)Wifi.PLAYER_WIFI_CODE ,(char)Wifi.PLAYER_MSGTYPE_REQ ,(char)Wifi.PLAYER_WIFI_REQ_IWSPY_DEL ,new PlayerWifiIwspyAddrReq()));
        elements.add(new TableObject((short)Wifi.PLAYER_WIFI_CODE ,(char)Wifi.PLAYER_MSGTYPE_REQ ,(char)Wifi.PLAYER_WIFI_REQ_IWSPY_PING ,new PlayerWifiIwspyAddrReq()));
        elements.add(new TableObject((short)Wifi.PLAYER_WIFI_CODE ,(char)Wifi.PLAYER_MSGTYPE_DATA ,(char)Wifi.PLAYER_WIFI_DATA_STATE ,new PlayerWifiData()));
        elements.add(new TableObject((short)Dio.PLAYER_DIO_CODE ,(char)Dio.PLAYER_MSGTYPE_DATA ,(char)Dio.PLAYER_DIO_DATA_VALUES ,new PlayerDioData()));
        elements.add(new TableObject((short)Dio.PLAYER_DIO_CODE ,(char)Dio.PLAYER_MSGTYPE_CMD ,(char)Dio.PLAYER_DIO_CMD_VALUES ,new PlayerDioCmd()));
        elements.add(new TableObject((short)Bumper.PLAYER_BUMPER_CODE ,(char)Bumper.PLAYER_MSGTYPE_DATA ,(char)Bumper.PLAYER_BUMPER_DATA_STATE ,new PlayerBumperData()));
        elements.add(new TableObject((short)Bumper.PLAYER_BUMPER_CODE ,(char)Bumper.PLAYER_MSGTYPE_DATA ,(char)Bumper.PLAYER_BUMPER_DATA_GEOM ,new PlayerBumperGeom()));
        elements.add(new TableObject((short)Bumper.PLAYER_BUMPER_CODE ,(char)Bumper.PLAYER_MSGTYPE_REQ ,(char)Bumper.PLAYER_BUMPER_REQ_GET_GEOM ,new PlayerBumperGeom()));
        elements.add(new TableObject((short)Camera.PLAYER_CAMERA_CODE ,(char)Camera.PLAYER_MSGTYPE_DATA ,(char)Camera.PLAYER_CAMERA_DATA_STATE ,new PlayerCameraData()));
        elements.add(new TableObject((short)Speech.PLAYER_SPEECH_CODE ,(char)Speech.PLAYER_MSGTYPE_CMD ,(char)Speech.PLAYER_SPEECH_CMD_SAY ,new PlayerSpeechCmd()));
        elements.add(new TableObject((short)Aio.PLAYER_AIO_CODE ,(char)Aio.PLAYER_MSGTYPE_CMD ,(char)Aio.PLAYER_AIO_CMD_STATE ,new PlayerAioCmd()));
        elements.add(new TableObject((short)Aio.PLAYER_AIO_CODE ,(char)Aio.PLAYER_MSGTYPE_DATA ,(char)Aio.PLAYER_AIO_DATA_STATE ,new PlayerAioData()));
        elements.add(new TableObject((short)Laser.PLAYER_LASER_CODE ,(char)Laser.PLAYER_MSGTYPE_DATA ,(char)Laser.PLAYER_LASER_DATA_SCAN ,new PlayerLaserData()));
        elements.add(new TableObject((short)Laser.PLAYER_LASER_CODE ,(char)Laser.PLAYER_MSGTYPE_DATA ,(char)Laser.PLAYER_LASER_DATA_SCANPOSE ,new PlayerLaserDataScanpose()));
        elements.add(new TableObject((short)Laser.PLAYER_LASER_CODE ,(char)Laser.PLAYER_MSGTYPE_REQ ,(char)Laser.PLAYER_LASER_REQ_GET_GEOM ,new PlayerLaserGeom()));
        elements.add(new TableObject((short)Laser.PLAYER_LASER_CODE ,(char)Laser.PLAYER_MSGTYPE_REQ ,(char)Laser.PLAYER_LASER_REQ_SET_CONFIG ,new PlayerLaserConfig()));
        elements.add(new TableObject((short)Laser.PLAYER_LASER_CODE ,(char)Laser.PLAYER_MSGTYPE_REQ ,(char)Laser.PLAYER_LASER_REQ_GET_CONFIG ,new PlayerLaserConfig()));
        elements.add(new TableObject((short)Laser.PLAYER_LASER_CODE ,(char)Laser.PLAYER_MSGTYPE_REQ ,(char)Laser.PLAYER_LASER_REQ_POWER ,new PlayerLaserPowerConfig()));
        elements.add(new TableObject((short)Laser.PLAYER_LASER_CODE ,(char)Laser.PLAYER_MSGTYPE_REQ ,(char)Laser.PLAYER_LASER_REQ_GET_ID ,new PlayerLaserGetIdConfig()));
        elements.add(new TableObject((short)Laser.PLAYER_LASER_CODE ,(char)Laser.PLAYER_MSGTYPE_REQ ,(char)Laser.PLAYER_LASER_REQ_SET_FILTER ,new PlayerLaserSetFilterConfig()));
        elements.add(new TableObject((short)Joystick.PLAYER_JOYSTICK_CODE ,(char)Joystick.PLAYER_MSGTYPE_DATA ,(char)Joystick.PLAYER_JOYSTICK_DATA_STATE ,new PlayerJoystickData()));
        elements.add(new TableObject((short)Client.PLAYER_PLAYER_CODE ,(char)Client.PLAYER_MSGTYPE_REQ ,(char)Client.PLAYER_PLAYER_REQ_DEVLIST ,new PlayerDeviceDevlist()));
        elements.add(new TableObject((short)Client.PLAYER_PLAYER_CODE ,(char)Client.PLAYER_MSGTYPE_REQ ,(char)Client.PLAYER_PLAYER_REQ_DRIVERINFO ,new PlayerDeviceDriverinfo()));
        elements.add(new TableObject((short)Client.PLAYER_PLAYER_CODE ,(char)Client.PLAYER_MSGTYPE_REQ ,(char)Client.PLAYER_PLAYER_REQ_DEV ,new PlayerDeviceReq()));
        elements.add(new TableObject((short)Client.PLAYER_PLAYER_CODE ,(char)Client.PLAYER_MSGTYPE_REQ ,(char)Client.PLAYER_PLAYER_REQ_DATA ,new PlayerNull()));
        elements.add(new TableObject((short)Client.PLAYER_PLAYER_CODE ,(char)Client.PLAYER_MSGTYPE_REQ ,(char)Client.PLAYER_PLAYER_REQ_DATAMODE ,new PlayerDeviceDatamodeReq()));
        elements.add(new TableObject((short)Client.PLAYER_PLAYER_CODE ,(char)Client.PLAYER_MSGTYPE_REQ ,(char)Client.PLAYER_PLAYER_REQ_AUTH ,new PlayerDeviceAuthReq()));
        elements.add(new TableObject((short)Client.PLAYER_PLAYER_CODE ,(char)Client.PLAYER_MSGTYPE_REQ ,(char)Client.PLAYER_PLAYER_REQ_NAMESERVICE ,new PlayerDeviceNameserviceReq()));
        elements.add(new TableObject((short)Client.PLAYER_PLAYER_CODE ,(char)Client.PLAYER_MSGTYPE_REQ ,(char)Client.PLAYER_PLAYER_REQ_ADD_REPLACE_RULE ,new PlayerAddReplaceRuleReq()));
        elements.add(new TableObject((short)Client.PLAYER_PLAYER_CODE ,(char)Client.PLAYER_MSGTYPE_SYNCH ,(char)Client.PLAYER_PLAYER_SYNCH_OK ,new PlayerNull()));
        elements.add(new TableObject((short)Client.PLAYER_PLAYER_CODE ,(char)Client.PLAYER_MSGTYPE_SYNCH ,(char)Client.PLAYER_PLAYER_SYNCH_OVERFLOW ,new PlayerUint32()));
        elements.add(new TableObject((short)Imu.PLAYER_IMU_CODE ,(char)Imu.PLAYER_MSGTYPE_DATA ,(char)Imu.PLAYER_IMU_DATA_STATE ,new PlayerImuDataState()));
        elements.add(new TableObject((short)Imu.PLAYER_IMU_CODE ,(char)Imu.PLAYER_MSGTYPE_DATA ,(char)Imu.PLAYER_IMU_DATA_CALIB ,new PlayerImuDataCalib()));
        elements.add(new TableObject((short)Imu.PLAYER_IMU_CODE ,(char)Imu.PLAYER_MSGTYPE_DATA ,(char)Imu.PLAYER_IMU_DATA_QUAT ,new PlayerImuDataQuat()));
        elements.add(new TableObject((short)Imu.PLAYER_IMU_CODE ,(char)Imu.PLAYER_MSGTYPE_DATA ,(char)Imu.PLAYER_IMU_DATA_EULER ,new PlayerImuDataEuler()));
        elements.add(new TableObject((short)Imu.PLAYER_IMU_CODE ,(char)Imu.PLAYER_MSGTYPE_REQ ,(char)Imu.PLAYER_IMU_REQ_SET_DATATYPE ,new PlayerImuDatatypeConfig()));
        elements.add(new TableObject((short)Imu.PLAYER_IMU_CODE ,(char)Imu.PLAYER_MSGTYPE_REQ ,(char)Imu.PLAYER_IMU_REQ_RESET_ORIENTATION ,new PlayerImuResetOrientationConfig()));
        elements.add(new TableObject((short)Ptz.PLAYER_PTZ_CODE ,(char)Ptz.PLAYER_MSGTYPE_REQ ,(char)Ptz.PLAYER_PTZ_REQ_GENERIC ,new PlayerPtzReqGeneric()));
        elements.add(new TableObject((short)Ptz.PLAYER_PTZ_CODE ,(char)Ptz.PLAYER_MSGTYPE_REQ ,(char)Ptz.PLAYER_PTZ_REQ_CONTROL_MODE ,new PlayerPtzReqControlMode()));
        elements.add(new TableObject((short)Ptz.PLAYER_PTZ_CODE ,(char)Ptz.PLAYER_MSGTYPE_REQ ,(char)Ptz.PLAYER_PTZ_REQ_GEOM ,new PlayerPtzGeom()));
        elements.add(new TableObject((short)Ptz.PLAYER_PTZ_CODE ,(char)Ptz.PLAYER_MSGTYPE_REQ ,(char)Ptz.PLAYER_PTZ_REQ_STATUS ,new PlayerPtzReqStatus()));
        elements.add(new TableObject((short)Ptz.PLAYER_PTZ_CODE ,(char)Ptz.PLAYER_MSGTYPE_DATA ,(char)Ptz.PLAYER_PTZ_DATA_STATE ,new PlayerPowerData()));
        elements.add(new TableObject((short)Ptz.PLAYER_PTZ_CODE ,(char)Ptz.PLAYER_MSGTYPE_DATA ,(char)Ptz.PLAYER_PTZ_DATA_GEOM ,new PlayerPtzGeom()));
        elements.add(new TableObject((short)Ptz.PLAYER_PTZ_CODE ,(char)Ptz.PLAYER_MSGTYPE_CMD ,(char)Ptz.PLAYER_PTZ_CMD_STATE ,new PlayerPtzCmd()));
        elements.add(new TableObject((short)Vectormap.PLAYER_VECTORMAP_CODE ,(char)Vectormap.PLAYER_MSGTYPE_REQ ,(char)Vectormap.PLAYER_VECTORMAP_REQ_GET_MAP_INFO ,new PlayerVectormapInfo()));
        elements.add(new TableObject((short)Vectormap.PLAYER_VECTORMAP_CODE ,(char)Vectormap.PLAYER_MSGTYPE_REQ ,(char)Vectormap.PLAYER_VECTORMAP_REQ_GET_LAYER_DATA ,new PlayerVectormapLayerData()));
        elements.add(new TableObject((short)Vectormap.PLAYER_VECTORMAP_CODE ,(char)Vectormap.PLAYER_MSGTYPE_REQ ,(char)Vectormap.PLAYER_VECTORMAP_REQ_WRITE_LAYER ,new PlayerVectormapLayerData()));
        elements.add(new TableObject((short)Limb.PLAYER_LIMB_CODE ,(char)Limb.PLAYER_MSGTYPE_DATA ,(char)Limb.PLAYER_LIMB_DATA_STATE ,new PlayerLimbData()));
        elements.add(new TableObject((short)Limb.PLAYER_LIMB_CODE ,(char)Limb.PLAYER_MSGTYPE_CMD ,(char)Limb.PLAYER_LIMB_CMD_HOME ,new PlayerNull()));
        elements.add(new TableObject((short)Limb.PLAYER_LIMB_CODE ,(char)Limb.PLAYER_MSGTYPE_CMD ,(char)Limb.PLAYER_LIMB_CMD_STOP ,new PlayerNull()));
        elements.add(new TableObject((short)Limb.PLAYER_LIMB_CODE ,(char)Limb.PLAYER_MSGTYPE_CMD ,(char)Limb.PLAYER_LIMB_CMD_SETPOSE ,new PlayerLimbSetposeCmd()));
        elements.add(new TableObject((short)Limb.PLAYER_LIMB_CODE ,(char)Limb.PLAYER_MSGTYPE_CMD ,(char)Limb.PLAYER_LIMB_CMD_SETPOSITION ,new PlayerLimbSetpositionCmd()));
        elements.add(new TableObject((short)Limb.PLAYER_LIMB_CODE ,(char)Limb.PLAYER_MSGTYPE_CMD ,(char)Limb.PLAYER_LIMB_CMD_VECMOVE ,new PlayerLimbVecmoveCmd()));
        elements.add(new TableObject((short)Limb.PLAYER_LIMB_CODE ,(char)Limb.PLAYER_MSGTYPE_REQ ,(char)Limb.PLAYER_LIMB_REQ_POWER ,new PlayerLimbPowerReq()));
        elements.add(new TableObject((short)Limb.PLAYER_LIMB_CODE ,(char)Limb.PLAYER_MSGTYPE_REQ ,(char)Limb.PLAYER_LIMB_REQ_BRAKES ,new PlayerLimbBrakesReq()));
        elements.add(new TableObject((short)Limb.PLAYER_LIMB_CODE ,(char)Limb.PLAYER_MSGTYPE_REQ ,(char)Limb.PLAYER_LIMB_REQ_GEOM ,new PlayerLimbGeomReq()));
        elements.add(new TableObject((short)Limb.PLAYER_LIMB_CODE ,(char)Limb.PLAYER_MSGTYPE_REQ ,(char)Limb.PLAYER_LIMB_REQ_SPEED ,new PlayerLimbSpeedReq()));
  }
}
