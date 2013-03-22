package com.igrs.tivic.phone.Interface;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

import com.igrs.tivic.phone.Bean.Base.PhotoListBean;
import com.igrs.tivic.phone.Bean.Social.SocialFriendBean;
import com.igrs.tivic.phone.Bean.Social.SocialFriendBeanList;

public interface ISocial {
	
	/**
	 * get frined list
	 * @return
	 */
	//获取我的好友、粉丝、陌生人、黑名单总列表
	public SocialFriendBeanList getSocialMyFrinedList(int uid);
	//获得好友的好友
	public SocialFriendBeanList getSocialFriendFriendsList(int uid);
	
	public List<SocialFriendBean> getSocialMyFriend(String uid);
	
	/**
	 * get mkFriend list
	 * @return
	 */
	public SocialFriendBeanList getSocialMKFriendList();
	
	public List<SocialFriendBean> getSocialMKFriend(String uid);
	/**
	 * get photo
	 * @param url 获取相册地址
	 * @param uid 用户id
	 * @return List<Bitmap>
	 */
	public PhotoListBean getPhotoList(String url, int uid);
	
	/**
	 * delete  photo 
	 * @param url
	 * @param urlList
	 */
	public void deletePhoto(String url,ArrayList<String> urlList);
}
