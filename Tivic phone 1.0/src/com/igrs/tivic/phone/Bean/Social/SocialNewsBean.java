package com.igrs.tivic.phone.Bean.Social;

import java.util.ArrayList;

public class SocialNewsBean {
	private ArrayList<SocialMsgBean> sBeanList ;
	private int act_sum;	//最新通知数量
	private int msg_sum;	//总计，最新私信数量
	
	public ArrayList<SocialMsgBean> getMessageBeanList() {
		return sBeanList;
	}
	public void setMessageBeanList(ArrayList<SocialMsgBean> sBeanList) {
		this.sBeanList = sBeanList;
	}
	public int getAct_sum() {
		return act_sum;
	}
	public void setAct_sum(int act_sum) {
		this.act_sum = act_sum;
	}
	public int getMsg_sum() {
		return msg_sum;
	}
	public void setMsg_sum(int msg_sum) {
		this.msg_sum = msg_sum;
	}
	
}
