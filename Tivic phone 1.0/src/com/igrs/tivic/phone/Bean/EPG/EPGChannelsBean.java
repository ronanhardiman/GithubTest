package com.igrs.tivic.phone.Bean.EPG;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author admin
 *
 */
public class EPGChannelsBean {
	private String channel_id;
	private String channel_name;
	private int channel_icon;
	private String channel_alias;	//别名,逗号分隔
	private int channel_sequence;	//优先级排序,值越小，优先级越高
	private ArrayList<ChannelsLiveUrlsBean> liveUrlsList;	//直播列表
	private List<EPGProgramsBean> programsBeans;   //program list
	public String getChannel_id() {
		return channel_id;
	}
	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}
	
	public List<EPGProgramsBean> getProgramsBeans() {
		return programsBeans;
	}
	public void setProgramsBeans(List<EPGProgramsBean> programsBeans) {
		this.programsBeans = programsBeans;
	}
	public int getChannel_icon() {
		return channel_icon;
	}
	public void setChannel_icon(int channel_icon) {
		this.channel_icon = channel_icon;
	}
	public String getChannel_name() {
		return channel_name;
	}
	public void setChannel_name(String channel_name) {
		this.channel_name = channel_name;
	}
	public String getChannel_alias() {
		return channel_alias;
	}
	public void setChannel_alias(String channel_alias) {
		this.channel_alias = channel_alias;
	}
	public int getChannel_sequence() {
		return channel_sequence;
	}
	public void setChannel_sequence(int channel_sequence) {
		this.channel_sequence = channel_sequence;
	}
	public ArrayList<ChannelsLiveUrlsBean> getLiveUrlsList() {
		return liveUrlsList;
	}
	public void setLiveUrlsList(ArrayList<ChannelsLiveUrlsBean> liveUrlsList) {
		this.liveUrlsList = liveUrlsList;
	}
	
}
