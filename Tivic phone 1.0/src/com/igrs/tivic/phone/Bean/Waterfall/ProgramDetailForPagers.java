package com.igrs.tivic.phone.Bean.Waterfall;


import java.util.ArrayList;

public class ProgramDetailForPagers {
	private String publishTime;
	private ArrayList<ProgramDetailBean> ProgramDetailBeans;
	
	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public ArrayList<ProgramDetailBean> getProgramDetailBeans() {
		return ProgramDetailBeans;
	}

	public void setProgramDetailBeans(
			ArrayList<ProgramDetailBean> programDetailBeans) {
		ProgramDetailBeans = programDetailBeans;
	} 
	
}

