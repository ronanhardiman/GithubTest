package com.igrs.tivic.phone.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import com.igrs.tivic.phone.Bean.Base.UserBean;
import com.igrs.tivic.phone.Bean.Social.SocialFriendBean;
import com.igrs.tivic.phone.Bean.Social.SocialFriendBeanList;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Impl.SocialImpl;
import com.igrs.tivic.phone.Interface.ISocial;
import com.igrs.tivic.phone.Listener.SocialUpadteUIListener;

import android.os.AsyncTask;

public class SocialFriendsAsyncTask extends AsyncTask<Object, Object, SocialFriendBeanList> {
	private List<SocialFriendBean> friendsList;
	private SocialFriendBeanList myFriendList;
	private SocialUpadteUIListener socialUpadteUIListener;
	private int partnerId = 0;
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		friendsList = new ArrayList<SocialFriendBean>();
	}
	
	public  void setPartnerUsrID(int uid)
	{
		partnerId = uid;
	}
	@Override
	protected SocialFriendBeanList doInBackground(Object... params) {
		// TODO Auto-generated method stub
		ISocial socialImpl = new SocialImpl();
		if(partnerId == TivicGlobal.getInstance().userRegisterBean.getUserId())
			myFriendList = socialImpl.getSocialMyFrinedList(partnerId);
		else
			myFriendList = socialImpl.getSocialFriendFriendsList(partnerId);
		return myFriendList;
	}


	
	/*@Override
	protected List<SocialFriendBean> doInBackground(Object... params) {
		// TODO Auto-generated method stub
		ISocial socialImpl = new SocialImpl();
		friendsList = socialImpl.getSocialMyFriend("");
		return friendsList;
	}*/

	@Override
	protected void onPostExecute(SocialFriendBeanList result) {
		if(socialUpadteUIListener != null)
			socialUpadteUIListener.notifySocialFriendUI(result);
	}

	public void setSocialUpdateUIListener(SocialUpadteUIListener socialUpadteUIListener){
		this.socialUpadteUIListener = socialUpadteUIListener;
	}

}
