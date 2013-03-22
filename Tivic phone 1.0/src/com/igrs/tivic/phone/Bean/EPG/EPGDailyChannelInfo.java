package com.igrs.tivic.phone.Bean.EPG;

import java.util.ArrayList;

/**
 * 获取某一天，某一频道的EPG信息，按start_display时间升序排列
 * @author admin
 *
 */
public class EPGDailyChannelInfo {
	private int channel_id;
	private String channel_name;
	private String day;
	private ArrayList<EPGProgramsBean> epgProgramBeanList;
	public int getChannel_id() {
		return channel_id;
	}
	public void setChannel_id(int channel_id) {
		this.channel_id = channel_id;
	}
	public String getChannel_name() {
		return channel_name;
	}
	public void setChannel_name(String channel_name) {
		this.channel_name = channel_name;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public ArrayList<EPGProgramsBean> getEpgProgramBeanList() {
		return epgProgramBeanList;
	}
	public void setEpgProgramBeanList(ArrayList<EPGProgramsBean> epgProgramBeanList) {
		this.epgProgramBeanList = epgProgramBeanList;
	}
	
}
