package com.igrs.tivic.phone.Bean;

import java.util.ArrayList;

public class ChannelMarquee {
	private int channelMarqueeId;
	private ArrayList<Integer> marqueeIds;//marquee id 在MarqueeList 按照marquee_id 查找对应的 marquee
	public int getChannelMarqueeId() {
		return channelMarqueeId;
	}
	public void setChannelMarqueeId(int channelMarqueeId) {
		this.channelMarqueeId = channelMarqueeId;
	}
	public ArrayList<Integer> getMarqueeIds() {
		return marqueeIds;
	}
	public void setMarqueeIds(ArrayList<Integer> marqueeIds) {
		this.marqueeIds = marqueeIds;
	}
	
}
