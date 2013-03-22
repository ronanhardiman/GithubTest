package com.igrs.tivic.phone.Bean;
/**
 * 用户访问的节目 
 */
public class VisitBean {
	private String visit_pid;
	private String visit_programTitle;
	private String visit_channelid;
	private String visit_channelname;
	
	public String getVisit_pid() {
		return visit_pid;
	}
	public void setVisit_pid(String visit_id) {
		this.visit_pid = visit_id;
	}
	public String getVisit_ProgramTitle() {
		return visit_programTitle;
	}
	public void setVisit_ProgramTitle(String visit_title) {
		this.visit_programTitle = visit_title;
	}
	public String getVisit_channelid() {
		return visit_channelid;
	}
	public void setVisit_channelid(String visit_channelid) {
		this.visit_channelid = visit_channelid;
	}

	public String getVisit_ChannelName() {
		return visit_channelname;
	}
	public void setVisit_ChannelName(String visit_channelname) {
		this.visit_channelname = visit_channelname;
	}
	
}
