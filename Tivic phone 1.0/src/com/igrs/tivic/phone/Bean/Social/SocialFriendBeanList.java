package com.igrs.tivic.phone.Bean.Social;

import java.util.ArrayList;

public class SocialFriendBeanList {
	private ArrayList<SocialFriendBean> friendBeansList;
	private int total;
	public ArrayList<SocialFriendBean> getFriendBeansList() {
		return friendBeansList;
	}
	public void setFriendBeansList(ArrayList<SocialFriendBean> friendBeansList) {
		this.friendBeansList = friendBeansList;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
}
