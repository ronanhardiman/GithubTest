package com.igrs.tivic.phone.Bean.EPG;

import java.util.List;

public class EPGChannelBean {

	private String channel_id;
	private int icon_path;
	private EPGProgramBean currentProgram;
	private String channel_titile;
	private List<EPGProgramBean> programBeans;
	
	public List<EPGProgramBean> getProgramBeans() {
		return programBeans;
	}
	public void setProgramBeans(List<EPGProgramBean> programBeans) {
		this.programBeans = programBeans;
	}
	
	public EPGProgramBean getCurrentProgram() {
		return currentProgram;
	}
	public void setCurrentProgram(EPGProgramBean currentProgram) {
		this.currentProgram = currentProgram;
	}
	public String getChannel_id() {
		return channel_id;
	}
	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}
	public int getIcon_path() {
		return icon_path;
	}
	public void setIcon_path(int icon_path) {
		this.icon_path = icon_path;
	}
	public String getChannel_titile() {
		return channel_titile;
	}
	public void setChannel_titile(String channel_titile) {
		this.channel_titile = channel_titile;
	}
	
}
