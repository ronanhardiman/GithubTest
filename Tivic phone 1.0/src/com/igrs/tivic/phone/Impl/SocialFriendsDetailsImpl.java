package com.igrs.tivic.phone.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.igrs.tivic.phone.Bean.Base.BaseParamBean;
import com.igrs.tivic.phone.Bean.Base.UserBean;
import com.igrs.tivic.phone.Bean.Login.UserPOIItemBean;
import com.igrs.tivic.phone.Bean.Login.UserRegisterBean;
import com.igrs.tivic.phone.Bean.Social.SocialFriendBeanList;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Global.URLConfig;
import com.igrs.tivic.phone.Interface.ISocialFriendDetails;
import com.igrs.tivic.phone.Parser.FriendsParser;
import com.igrs.tivic.phone.Parser.UsersDetailsParser;
import com.igrs.tivic.phone.Utils.HttpClientUtils;
import com.igrs.tivic.phone.Utils.JsonForRequest;
import com.igrs.tivic.phone.Utils.UIUtils;

public class SocialFriendsDetailsImpl implements ISocialFriendDetails{
	private static SocialFriendsDetailsImpl instance = null;
	private HttpClientUtils httpClientUtils;
	private final static String charset = "utf-8";
	private UserBean pBean;
	private Handler handler;
	private Handler listHandler;
	private Handler focus_handler;
	private Handler unfocus_handler;
	private Handler focus_sx_handler;
	private Handler unfocus_sx_handler;
	

	public void setFocus_sx_handler(Handler focusSxHandler) {
		focus_sx_handler = focusSxHandler;
	}

	public void setUnfocus_sx_handler(Handler unfocusSxHandler) {
		unfocus_sx_handler = unfocusSxHandler;
	}

	public void setUnfocus_handler(Handler unfocusHandler) {
		unfocus_handler = unfocusHandler;
	}

	public void setFocus_handler(Handler focusHandler) {
		focus_handler = focusHandler;
	}

	public void setListHandler(Handler listHandler) {
		this.listHandler = listHandler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	private SocialFriendsDetailsImpl() {
		httpClientUtils = HttpClientUtils.getInstance();
	}

	public static SocialFriendsDetailsImpl getInstance() {
		if (instance == null) {
			instance = new SocialFriendsDetailsImpl();
		}
		return instance;
	}
	public void getUserBaseInfo(int uid) {
		final int tempuid = uid;
		new Thread(new Runnable() {
			@Override
			public void run() {
				BaseParamBean bBean = new BaseParamBean();
				bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserId());
				bBean.setSid(TivicGlobal.getInstance().userRegisterBean
						.getUserSID());
				bBean.setVer(1);
				bBean.setPartner_id(tempuid); // 高峰测试帐号

				HashMap<String, String> map = new HashMap<String, String>();
				String jsonToSting = JsonForRequest.getBaseInfo_ExtInfo(bBean);
				map.put("json", jsonToSting);
				String json = httpClientUtils.requestPostHttpClient(
						URLConfig.get_base_info, "utf-8", map);
				UIUtils.Logi("chen", "friend detail="+json);
				pBean = UsersDetailsParser.getUserBaseInfo(json);
				Message msg = handler.obtainMessage();
				msg.obj = pBean;
				handler.sendMessage(msg);
			}
		}).start();
	}
	

	public void getFollowFriend(int uid){
		final int tenpuid = uid;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				BaseParamBean bBean = new BaseParamBean();
				bBean.setUid(TivicGlobal.getInstance().userRegisterBean
						.getUserUID());
				bBean.setSid(TivicGlobal.getInstance().userRegisterBean
						.getUserSID());
				bBean.setVer(1);
				bBean.setPartner_id(tenpuid);
				String jsonForRequest = JsonForRequest.getFollow(bBean);
				String json = httpClientUtils.requestPostHttpClient4(URLConfig.get_follow_people, "utf-8", jsonForRequest);
				Log.d("json", json);
			}
		}).start();
	}
	//add by chen
	public int getFollowFriend2(int uid){
				BaseParamBean bBean = new BaseParamBean();
				bBean.setUid(TivicGlobal.getInstance().userRegisterBean
						.getUserUID());
				bBean.setSid(TivicGlobal.getInstance().userRegisterBean
						.getUserSID());
				bBean.setVer(1);
				bBean.setPartner_id(uid);
				String jsonForRequest = JsonForRequest.getFollow(bBean);
				String json = httpClientUtils.requestPostHttpClient4(URLConfig.get_follow_people, "utf-8", jsonForRequest);
				return FriendsParser.getFollow(json);
	}
	public void getUnFollowFriend(int uid){
		final int tempuid = uid;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				BaseParamBean bBean = new BaseParamBean();
				bBean.setUid(TivicGlobal.getInstance().userRegisterBean
						.getUserUID());
				bBean.setSid(TivicGlobal.getInstance().userRegisterBean
						.getUserSID());
				bBean.setVer(1);
				bBean.setPartner_id(tempuid); 
				String jsonForRequest = JsonForRequest.getUnFollow(bBean);
				String json = httpClientUtils.requestPostHttpClient4(URLConfig.get_Unfollow_people, "utf-8", jsonForRequest);
				Log.d("json_follow", json);
			}
		}).start();
	}
	//add by chen
	public int getUnFollowFriend2(int uid){
				BaseParamBean bBean = new BaseParamBean();
				bBean.setUid(TivicGlobal.getInstance().userRegisterBean
						.getUserUID());
				bBean.setSid(TivicGlobal.getInstance().userRegisterBean
						.getUserSID());
				bBean.setVer(1);
				bBean.setPartner_id(uid); 
				String jsonForRequest = JsonForRequest.getUnFollow(bBean);
//				UIUtils.Logi("chen", "取消关注jsonForRequest"+jsonForRequest);
				String json = httpClientUtils.requestPostHttpClient4(URLConfig.get_Unfollow_people, "utf-8", jsonForRequest);
//				UIUtils.Logi("chen", "取消关注json"+json);
				return FriendsParser.getUnFollow(json);
	}
	public void getBlockFriend(int uid){
		final int tempuid = uid;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				BaseParamBean bBean = new BaseParamBean();
				bBean.setUid(TivicGlobal.getInstance().userRegisterBean
						.getUserUID());
				bBean.setSid(TivicGlobal.getInstance().userRegisterBean
						.getUserSID());
				bBean.setVer(1);
				bBean.setPartner_id(tempuid); // 高峰测试帐号
				String jsonForRequest = JsonForRequest.getBlock(bBean);
				String json = httpClientUtils.requestPostHttpClient4(URLConfig.get_block_people, "utf-8", jsonForRequest);
			}
		}).start();
	}
	public void getUnBlockFriend(int uid) {
		// TODO Auto-generated method stub
		final int tempuid = uid;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				BaseParamBean bBean = new BaseParamBean();
				bBean.setUid(TivicGlobal.getInstance().userRegisterBean
						.getUserUID());
				bBean.setSid(TivicGlobal.getInstance().userRegisterBean
						.getUserSID());
				bBean.setVer(1);
				bBean.setPartner_id(tempuid); // 高峰测试帐号
				String jsonForRequest = JsonForRequest.getUnBlock(bBean);
				String json = httpClientUtils.requestPostHttpClient4(URLConfig.get_Unblock_people, "utf-8", jsonForRequest);
			}
		}).start();
	}

	public void getUserExpendInfo(int uid) {
		// TODO Auto-generated method stub
		final int tempuid = uid;
		new Thread(new Runnable() {
			@Override
			public void run() {
				BaseParamBean bBean = new BaseParamBean();
				bBean.setUid(TivicGlobal.getInstance().userRegisterBean
						.getUserUID());
				bBean.setSid(TivicGlobal.getInstance().userRegisterBean
						.getUserSID());
				bBean.setVer(1);
				bBean.setPartner_id(tempuid); // 高峰测试帐号

				HashMap<String, String> map = new HashMap<String, String>();
				String jsonToSting = JsonForRequest.getBaseInfo_ExtInfo(bBean);
				map.put("json", jsonToSting);
				String json = httpClientUtils.requestPostHttpClient(
						URLConfig.get_ext_info, "utf-8", map);
				//Log.d("json", json);
				ArrayList<UserPOIItemBean> user_expendlist = UsersDetailsParser.getUserExtInfo(json);
				Message msg_list = listHandler.obtainMessage();
				msg_list.obj = user_expendlist;
				listHandler.sendMessage(msg_list);
				
			}
		}).start();
	}
	
	/*
	 * 获得好友的信息
	 * user/get_info_ext
		{
		    "uid": "1005",
		    "sid": "6e3926e2848760a3ea2fbb3ccee6d1ea",
		    "ver": 1,
		    "req": {
		        "uid": "1005"		//指定被查询的用户
		    }
		}
	 */
	/*
	 * 获得好友的扩展信息
	 */
	public ArrayList<UserPOIItemBean> getFriendInfo(int uid) {
		String jsonRequest = JsonForRequest.getFriendInfo(uid);
		String json = httpClientUtils.requestPostHttpClient4(URLConfig.get_ext_info, "utf-8",jsonRequest);
		return UsersDetailsParser.getUserExtInfo(json);
	}
	
	
	public void modifyUserAvatar(String avaterPath){
//		BaseParamBean bBean = new BaseParamBean();
//		bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserUID());
//		bBean.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
//		bBean.setVer(1);
		UserRegisterBean urBean = TivicGlobal.getInstance().userRegisterBean;
		urBean.setUserAvartarPath(avaterPath);
		HashMap<String, String> map = new HashMap<String, String>();
		String jsonToSting = JsonForRequest.modifyUserBasicInfo2(urBean);
		map.put("json", jsonToSting);
		map.put("file", avaterPath);
		
		String json_modify_avatar = httpClientUtils.requestPostHttpClient(URLConfig.get_modify_info, charset, map);
	}

	public SocialFriendBeanList getFriendFriendsList(){
		BaseParamBean bBean = new BaseParamBean();
		bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserUID());
		bBean.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
		bBean.setVer(1);
		String jsonToString = JsonForRequest.getSocialFriendsList(bBean);
		String json_friends_list = httpClientUtils.requestPostHttpClient4(URLConfig.get_follow_List, charset, jsonToString);
		SocialFriendBeanList friends_list = FriendsParser.getFriendList(json_friends_list);
		return friends_list;
	}
	/**
	 * 拉黑好友
	 */
	public int pullIntoBlackList(int uid) {
		String jsonForRequest = JsonForRequest.getPullIntoBlackList(uid);
		String json = httpClientUtils.requestPostHttpClient4(URLConfig.get_block_people, "utf-8",jsonForRequest);
		return UsersDetailsParser.parsePullIntoBlackList(json);
	}
	/**
	 * 取消拉黑好友
	 */
	public int pullOutOfBlackList(int uid) {
		String jsonForRequest = JsonForRequest.getPullOutOfBlackList(uid);
//		UIUtils.Logi("chen", "取消拉黑JsonRequest="+jsonForRequest);
		String json = httpClientUtils.requestPostHttpClient4(URLConfig.get_Unblock_people, "utf-8",jsonForRequest);
//		UIUtils.Logi("chen", "取消拉黑Json="+json);
		return UsersDetailsParser.parsePullOutOfBlackList(json);
	}
}
