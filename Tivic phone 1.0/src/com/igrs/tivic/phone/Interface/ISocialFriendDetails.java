package com.igrs.tivic.phone.Interface;

import java.util.ArrayList;

import com.igrs.tivic.phone.Bean.Base.UserBean;
import com.igrs.tivic.phone.Bean.Login.UserPOIItemBean;

public interface ISocialFriendDetails {
	public void getUserBaseInfo(int uid);
	public void getUserExpendInfo(int uid);
	public void getFollowFriend(int uid);
	public void getUnFollowFriend(int uid);
	public void getBlockFriend(int uid);
	public void getUnBlockFriend(int uid);
}
