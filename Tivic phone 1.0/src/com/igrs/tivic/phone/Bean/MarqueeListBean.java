package com.igrs.tivic.phone.Bean;

import java.util.ArrayList;
import java.util.HashMap;

public class MarqueeListBean {
	private String publishTime;
	private HashMap<Integer, MarqueeBean> marqueeList ;
	private HashMap<Integer, MarqueeBean> globalMarqueeList ;
	private HashMap<Integer, ArrayList<MarqueeBean>> channelMarqueeList ;
	
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public HashMap<Integer, MarqueeBean> getMarqueeList() {
		return marqueeList;
	}
	public void setMarqueeList(HashMap<Integer, MarqueeBean> marqueeList) {
		this.marqueeList = marqueeList;
	}
	public HashMap<Integer, MarqueeBean> getGlobalMarqueeList() {
		return globalMarqueeList;
	}
	public void setGlobalMarqueeList(HashMap<Integer, MarqueeBean> globalMarqueeList) {
		this.globalMarqueeList = globalMarqueeList;
	}
	public HashMap<Integer, ArrayList<MarqueeBean>> getChannelMarqueeList() {
		return channelMarqueeList;
	}
	public void setChannelMarqueeList(
			HashMap<Integer, ArrayList<MarqueeBean>> channelMarqueeList) {
		this.channelMarqueeList = channelMarqueeList;
	}
	
	
	

}
