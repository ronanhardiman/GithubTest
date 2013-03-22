package com.igrs.tivic.phone.Bean.Social;

import android.R.bool;

import com.igrs.tivic.phone.Bean.Base.UserBean;

public class SocialFriendBean {
	private UserBean userBean;
	private String distance;
	private boolean isOnline;
	private int friendType;   //0为我的好友，1为陌生人，2为黑名单
	
	public int getFriendType() {
		return friendType;
	}
	public void setFriendType(int friendType) {
		this.friendType = friendType;
	}
	public boolean isOnline() {
		return isOnline;
	}
	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}
	public UserBean getUserBean() {
		return userBean;
	}
	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	
}
