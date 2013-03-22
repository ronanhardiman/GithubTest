package com.igrs.tivic.phone.Bean.Social;

import java.util.LinkedList;

public class SocialMessageHistoryListBean {
	LinkedList<SocialMessageBean> messageList;
	int ifMore;
	int count; //  在接口 message/home中 表示  : 本次获取条目总数
			   //  在接口message/history 中表示:  两个用户之间的私信总数  total
	public LinkedList<SocialMessageBean> getMessageList() {
		return messageList;
	}
	public void setMessageList(LinkedList<SocialMessageBean> messageList) {
		this.messageList = messageList;
	}
	public int getIfMore() {
		return ifMore;
	}
	public void setIfMore(int ifMoew) {
		this.ifMore = ifMoew;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
