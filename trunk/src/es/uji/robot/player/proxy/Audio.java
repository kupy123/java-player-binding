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

import es.uji.robot.player.generated.abstractproxy.AbstractAudio;
import es.uji.robot.player.generated.xdr.PlayerAudioMixerChannel;
import es.uji.robot.player.generated.xdr.PlayerAudioMixerChannelList;
import es.uji.robot.player.generated.xdr.PlayerAudioMixerChannelListDetail;
import es.uji.robot.player.generated.xdr.PlayerAudioSample;
import es.uji.robot.player.generated.xdr.PlayerAudioSampleItem;
import es.uji.robot.player.generated.xdr.PlayerAudioSampleRecReq;
import es.uji.robot.player.generated.xdr.PlayerAudioSeq;
import es.uji.robot.player.generated.xdr.PlayerAudioState;
import es.uji.robot.player.generated.xdr.PlayerAudioWav;
import es.uji.robot.player.generated.xdr.PlayerBool;
import es.uji.robot.player.generated.xdr.PlayerMsghdr;
import es.uji.robot.xdr.XDRObject;

/**
*Audio device data
*/
public class Audio extends AbstractAudio{

	/**
	*Details of the channels from the mixer. 
	*/
	private PlayerAudioMixerChannelListDetail channelDetailsList;
	/** 
	*last block of recorded data 
	*/
	private PlayerAudioWav wavData;
	/** 
	*last block of seq data 
	*/
	private PlayerAudioSeq seqData;
	/** 
	*current channel data 
	*/
	private PlayerAudioMixerChannelList mixerData;
	/** 
	*current driver state 
	*/
	private int state;
	
	private int lastIndex;

	public PlayerAudioMixerChannelListDetail getChannelDetailsList() {
		return channelDetailsList;
	}

	public void setChannelDetailsList(
			PlayerAudioMixerChannelListDetail channelDetailsList) {
		this.channelDetailsList = channelDetailsList;
	}

	public PlayerAudioWav getWavData() {
		return wavData;
	}

	public void setWavData(PlayerAudioWav wavData) {
		this.wavData = wavData;
	}

	public PlayerAudioSeq getSeqData() {
		return seqData;
	}

	public void setSeqData(PlayerAudioSeq seqData) {
		this.seqData = seqData;
	}

	public PlayerAudioMixerChannelList getMixerData() {
		return mixerData;
	}

	public void setMixerData(PlayerAudioMixerChannelList mixerData) {
		this.mixerData = mixerData;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getLastIndex() {
		return lastIndex;
	}

	public void setLastIndex(int lastIndex) {
		this.lastIndex = lastIndex;
	}
	
	public Audio(Client client, int index){
		super.init(client, PLAYER_AUDIO_CODE, index);
	}

	/** 
	*Command to play an audio block 
	 * @throws ClientException 
	*/
	public void wavPlayCommand(ByteArrayOutputStream data,int format) throws ClientException{
		PlayerAudioWav command = new PlayerAudioWav();
		
		command.setData(data);
		command.setFormat(format);
		
		super.getClient().write(this, PLAYER_AUDIO_CMD_WAV_PLAY, command);
	}
	
	/** 
	*Command to set recording state
	 * @throws ClientException 
	*/
	public void wavStreamRecordingCommand(char state) throws ClientException{
		PlayerBool command = new PlayerBool();
		
		command.setState(state);
		
		super.getClient().write(this, PLAYER_AUDIO_CMD_WAV_STREAM_REC, command);
	
	}
	
	/** 
	*Command to play prestored sample 
	 * @throws ClientException 
	*/
	public void samplePlayCommand(int index) throws ClientException{
		PlayerAudioSampleItem command = new PlayerAudioSampleItem();
		
		command.setIndex(index);
		
		super.getClient().write(this, PLAYER_AUDIO_CMD_SAMPLE_PLAY, command);
		
	}
	
	/** 
	*Command to play sequence of tones
	 * @throws ClientException 
	*/
	public void sequencePlayCommand(PlayerAudioSeq tones) throws ClientException{
		super.getClient().write(this, PLAYER_AUDIO_CMD_SEQ_PLAY, tones);
	}
	
	/** 
	*Command to set mixer levels for multiple channels 
	 * @throws ClientException 
	*/
	public void mixerMultipleChannelsCommand(PlayerAudioMixerChannelList levels) throws ClientException{
		super.getClient().write(this, PLAYER_AUDIO_CMD_MIXER_CHANNEL, levels);
	}
	
	/** 
	*Command to set mixer levels for a single channel 
	 * @throws ClientException 
	*/
	public void mixerChannelCommand(int index, float amplitude, char active) throws ClientException{
		PlayerAudioMixerChannelList command = new PlayerAudioMixerChannelList();
		ArrayList<PlayerAudioMixerChannel> channels = new ArrayList<PlayerAudioMixerChannel>();
		PlayerAudioMixerChannel channel = new PlayerAudioMixerChannel();
		
		channel.setIndex(index);
		channel.setAmplitude(amplitude);
		
		PlayerBool act = new PlayerBool();
		act.setState(active);
		channel.setActive(act);
		
		channels.add(channel);
		
		command.setChannels(channels);
		
		super.getClient().write(this, PLAYER_AUDIO_CMD_MIXER_CHANNEL, command);
		
	}
	
	/** 
	*Request to record a single audio block
	*Value is returned into wav_data, block length is determined by device 
	 * @throws ClientException 
	*/
	public PlayerAudioWav wavRecord() throws ClientException{
		PlayerAudioWav response = new PlayerAudioWav();
		response = (PlayerAudioWav)super.getClient().request(this, PLAYER_AUDIO_REQ_WAV_REC, null);
		wavData = response;
		
		return response;
	}
	
	/** 
	*Request to load an audio sample 
	 * @throws ClientException 
	*/
	public PlayerAudioSample sampleLoad(int index, ByteArrayOutputStream data, int format) throws ClientException{
		PlayerAudioSample sample = new PlayerAudioSample();
		PlayerAudioWav wav = new PlayerAudioWav();
		
		wav.setData(data);
		wav.setFormat(format);
		sample.setSample(wav);
		sample.setIndex(index);
		
		return (PlayerAudioSample)super.getClient().request(this, PLAYER_AUDIO_REQ_SAMPLE_LOAD, sample);
		
	}
	
	/** 
	*Request to retrieve an audio sample
	*Data is stored in wavData 
	 * @throws ClientException 
	*/
	public PlayerAudioSample sampleRetrieve(int index) throws ClientException{
		PlayerAudioSample request = new PlayerAudioSample();
		PlayerAudioSample response = new PlayerAudioSample();
		PlayerAudioWav sample = new PlayerAudioWav();
		
		
		request.setIndex(index);
		request.setSample(sample);
		
		response = (PlayerAudioSample)super.getClient().request(this, PLAYER_AUDIO_REQ_SAMPLE_RETRIEVE, request);
		
		if(response != null){
			wavData = response.getSample();
		}
		
		return response;
	}
	
	/**
	*Request to record new sample 
	 * @throws ClientException 
	*/
	public PlayerAudioSampleRecReq sampleRecord(int index, int length) throws ClientException{
		PlayerAudioSampleRecReq request = new PlayerAudioSampleRecReq();
		PlayerAudioSampleRecReq response = new PlayerAudioSampleRecReq();
		
		request.setIndex(index);
		request.setLength(length);
		
		response = (PlayerAudioSampleRecReq)super.getClient().request(this, PLAYER_AUDIO_REQ_SAMPLE_REC, request);
		
		if(response != null){
			lastIndex = response.getIndex();
		}
		
		return response;	
		
	}
	
	/** 
	*Request mixer channel data
	*result is stored in mixerData
	 * @throws ClientException 
	*/
	public PlayerAudioMixerChannelList retrieveMixerLevels() throws ClientException{
		PlayerAudioMixerChannelList response = new PlayerAudioMixerChannelList();
		response = (PlayerAudioMixerChannelList)super.getClient().request(this, PLAYER_AUDIO_REQ_MIXER_CHANNEL_LEVEL, null);
		
		if(response != null){
			mixerData = response;
		}
		
		return response;
	}
	
	/** 
	*Request mixer channel details list
	*result is stored in channelDetailsList
	 * @throws ClientException 
	*/
	public PlayerAudioMixerChannelListDetail retrieveMixerDetails() throws ClientException{
		PlayerAudioMixerChannelListDetail response = new PlayerAudioMixerChannelListDetail();
		
		response = (PlayerAudioMixerChannelListDetail)super.getClient().request(this, PLAYER_AUDIO_REQ_MIXER_CHANNEL_LIST, null);
		
		if(response != null){
			channelDetailsList = response;
		}
		
		return response;
	
	}

	public void putMsg(PlayerMsghdr header, XDRObject data) {
		if( header.getType() == PLAYER_MSGTYPE_DATA && header.getSubtype() == PLAYER_AUDIO_DATA_WAV_REC){
			PlayerAudioWav wav = (PlayerAudioWav)data;
			if ( header.getSize() > 0 ){
				if ( wavData.getData() != null){
					wavData = null;
				}
				wavData.setData(wav.getData());
				wavData.setFormat(wav.getFormat());
			}
		}
		else if( header.getType() == PLAYER_MSGTYPE_DATA && header.getSubtype() == PLAYER_AUDIO_DATA_SEQ){
			PlayerAudioSeq seq = (PlayerAudioSeq)data;
			if ( header.getSize() > 0 ){
				seqData.setTones(seq.getTones());
			}
		}
		else if( header.getType() == PLAYER_MSGTYPE_DATA && header.getSubtype() == PLAYER_AUDIO_DATA_MIXER_CHANNEL){
			PlayerAudioMixerChannelList clist = (PlayerAudioMixerChannelList)data;
			if ( header.getSize() > 0 ){
				mixerData.setChannels(clist.getChannels());
			}
		}
		else if( header.getType() == PLAYER_MSGTYPE_DATA && header.getSubtype() == PLAYER_AUDIO_DATA_STATE){
			PlayerAudioState audioState = (PlayerAudioState)data;
			if ( header.getSize() > 0 ){
				state = audioState.getState();
			}
		}
		else{
			System.err.println("Skipping audio message with unknown type/subtype");
		}
		
	}
}