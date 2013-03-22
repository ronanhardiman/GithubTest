package com.igrs.tivic.phone.Impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Bean.Base.BaseParamBean;
import com.igrs.tivic.phone.Bean.Base.PhotoListBean;
import com.igrs.tivic.phone.Bean.Base.UserBean;
import com.igrs.tivic.phone.Bean.EPG.EPGChannelListBean;
import com.igrs.tivic.phone.Bean.Login.UserRegisterBean;
import com.igrs.tivic.phone.Bean.Social.SocialFriendBean;
import com.igrs.tivic.phone.Bean.Social.SocialFriendBeanList;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Global.URLConfig;
import com.igrs.tivic.phone.Interface.ISocial;
import com.igrs.tivic.phone.Parser.EPGDataParser;
import com.igrs.tivic.phone.Parser.FriendsParser;
import com.igrs.tivic.phone.Parser.UsersDetailsParser;
import com.igrs.tivic.phone.Utils.HttpClientUtils;
import com.igrs.tivic.phone.Utils.JsonForRequest;
import com.igrs.tivic.phone.Utils.UIUtils;

public class SocialImpl implements ISocial {
	private Context context;
	private HttpClientUtils httpClientUtils;
	private final static String charset = "utf-8";
	
	public SocialImpl() {
		super();
		init();
	}

	public SocialImpl(Context context) {
		super();
		this.context = context;
		init();
	}
	
	private void init() {
		// TODO Auto-generated method stub
		httpClientUtils = HttpClientUtils.getInstance();
	}
	
	/**
	 * friend test data romate
	 * url  /friend/list_follow
	 * @return
	 */
	@Override
	public SocialFriendBeanList getSocialMyFrinedList(int uid){
		HashMap<String, String> map = new HashMap<String, String>();
		BaseParamBean bBean = new BaseParamBean();
		bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserUID());
		bBean.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
		bBean.setPartner_id(uid);
		bBean.setOffset(0);
		bBean.setCountInPage(Const.COUNTINPAGE);
		bBean.setVer(2);
		String jsonToSting = JsonForRequest.getFriendsList(bBean);
		
		map.put("json", jsonToSting);
		//获取好友列表
		String json = httpClientUtils.requestPostHttpClient(
				URLConfig.get_follow_List, charset, map);
		//获取粉丝列表
		String json1 = httpClientUtils.requestPostHttpClient(
				URLConfig.get_fansList, charset, map);
		//获取黑名单列表
		String json2 = httpClientUtils.requestPostHttpClient(
				URLConfig.get_blocked_List, charset, map);
		//陌生人
		String json3 = httpClientUtils.requestPostHttpClient(
				URLConfig.get_stranger_List, charset, map);

		SocialFriendBeanList myfriendList = FriendsParser.getFriendList(json);
		SocialFriendBeanList myfriendList1 = FriendsParser.getFriendList(json1);
		SocialFriendBeanList myfriendList2 = FriendsParser.getFriendList(json2);
		SocialFriendBeanList myfriendList3 = FriendsParser.getFriendList(json3);
		if(myfriendList != null && myfriendList1 != null && myfriendList1.getFriendBeansList() != null)
		{
			myfriendList.getFriendBeansList().addAll(myfriendList1.getFriendBeansList());
			myfriendList.setTotal(myfriendList.getTotal() + myfriendList1.getTotal());
		}
		if(myfriendList != null && myfriendList2 != null && myfriendList2.getFriendBeansList() != null)
		{
			myfriendList.getFriendBeansList().addAll(myfriendList2.getFriendBeansList());
			myfriendList.setTotal(myfriendList.getTotal() + myfriendList2.getTotal());
		}
		if(myfriendList != null && myfriendList3 != null && myfriendList3.getFriendBeansList() != null)
		{
			myfriendList.getFriendBeansList().addAll(myfriendList3.getFriendBeansList());
			myfriendList.setTotal(myfriendList.getTotal() + myfriendList3.getTotal());
		}
//		UIUtils.Logd("SocialImpl", "00001 jsonrequest = " + json1);
//		UIUtils.Logd("SocialImpl", "00002 jsonrequest = " + json2);
//		UIUtils.Logd("SocialImpl", "00003 jsonrequest = " + json3);

		return myfriendList;
	}
	
	
	@Override
	public SocialFriendBeanList getSocialFriendFriendsList(int uid){
		HashMap<String, String> map = new HashMap<String, String>();
		BaseParamBean bBean = new BaseParamBean();
		bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserUID());
		bBean.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
		bBean.setPartner_id(uid);
		bBean.setOffset(0);
		bBean.setCountInPage(Const.COUNTINPAGE);
		bBean.setVer(2);
		String jsonToSting = JsonForRequest.getFriendsList(bBean);
		
		map.put("json", jsonToSting);
		//获取好友列表
		String json = httpClientUtils.requestPostHttpClient(
				URLConfig.get_follow_List, charset, map);
		

		SocialFriendBeanList myfriendList = FriendsParser.getFriendList(json);
		
		return myfriendList;
	}
	
	@Override
	public List<SocialFriendBean> getSocialMyFriend(String uid) {
		List<SocialFriendBean> friendsList = new ArrayList<SocialFriendBean>();
		for(int i =0 ; i <10 ;i++){
			SocialFriendBean  socialFriendBean = new SocialFriendBean();
			UserBean userBean = new UserBean();
			userBean.setUserGender(i%2); 
			socialFriendBean.setUserBean(userBean);
			if(i%2 == 0 ){
				socialFriendBean.setOnline(true);   //0
			}else{
				socialFriendBean.setOnline(false);  //1
			}
			if(i%3 == 0){
				socialFriendBean.setFriendType(0);
			}else if(i%3 == 1){
				socialFriendBean.setFriendType(1);
			}else{
				socialFriendBean.setFriendType(2);
			}
			friendsList.add(socialFriendBean);
		}
		return friendsList;
	}

	/**
	 * mkfriend list romate
	 */
	@Override
	public SocialFriendBeanList getSocialMKFriendList() {
		// TODO Auto-generated method stub
		HashMap<String, String> map = new HashMap<String, String>();
		BaseParamBean bBean = new BaseParamBean();
		UserRegisterBean registerBean = TivicGlobal.getInstance().userRegisterBean;
		bBean.setUid(registerBean.getUserUID());
		bBean.setSid(registerBean.getUserSID());
		bBean.setOffset(0);
		bBean.setCountInPage(Const.COUNTINPAGE);
		if(TivicGlobal.getInstance().currentVisit.getVisit_pid() != null)
			bBean.setPid(Integer.parseInt(TivicGlobal.getInstance().currentVisit.getVisit_pid()));
		else
			bBean.setPid(102);
		bBean.setVer(1);
		String jsonToSting = JsonForRequest.getFindPeople(bBean,registerBean);
		//UIUtils.Logd("SocialImpl", "jsonret 1 = " + jsonToSting);
		
//		map.put("json", jsonToSting);
//		String jsonret = httpClientUtils.requestPostHttpClient(
//				URLConfig.get_find_people, charset, map);
		String jsonret = httpClientUtils.requestPostHttpClient4(
				URLConfig.get_find_people, "utf-8", jsonToSting);
		SocialFriendBeanList mkfList = FriendsParser.findPeople(jsonret);
//		UIUtils.Logd("SocialImpl", "jsonret 2 = " + jsonret);
		return mkfList;
	}

	@Override
	public List<SocialFriendBean> getSocialMKFriend(String uid) {
		// TODO Auto-generated method stub
		List<SocialFriendBean> mkFriendsList = new ArrayList<SocialFriendBean>();
		for(int i = 0 ; i < 10 ; i++){
			SocialFriendBean socialFriendBean = new SocialFriendBean();
			UserBean userBean = new UserBean();
			userBean.setUserGender(i%2);
			socialFriendBean.setUserBean(userBean);
			mkFriendsList.add(socialFriendBean);
		}
		return mkFriendsList;
	}
	/**
	 * get photo list
	 */
	@Override
	public PhotoListBean getPhotoList(String url,int uid) {
		// TODO Auto-generated method stub
//		List<Bitmap> photoList = new ArrayList<Bitmap>();
		/*for(int i = 0 ; i<10 ;i++){
			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.photo_test); 
			photoList.add(bitmap);
		}*/
		PhotoListBean pBean = new PhotoListBean();
		BaseParamBean bBean = new BaseParamBean();
		bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserUID());
		bBean.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
		bBean.setPartner_id(uid);
		bBean.setVer(1);
		/*bBean.setOffset(0);
		bBean.setPageIndex(20);*/
		int page_no = 0 ;//第几页
		int per_page = Const.COUNTINPAGE ;//每次获取多少条
		String json = httpClientUtils.requestPostHttpClient4(url, "utf-8", JsonForRequest.getPhotoList(bBean, page_no, per_page));
		pBean = UsersDetailsParser.getUserPhotoList(json);
		return pBean;
	}
	
	@Override
	public void deletePhoto(String url,ArrayList<String> urlList){
		BaseParamBean bBean = new BaseParamBean();
		bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserUID());
		bBean.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
		bBean.setVer(1);
		
		HashMap<String, String> map = new HashMap<String, String>();
		String jsonToSting = JsonForRequest.deletePhotos(bBean, urlList);
		map.put("json", jsonToSting);
		String json_delete_photo = httpClientUtils.requestPostHttpClient(URLConfig.get_photos_delete, charset, map);
	}

}
