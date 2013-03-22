package com.igrs.tivic.phone.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import com.igrs.tivic.phone.Bean.Base.UserBean;
import com.igrs.tivic.phone.Bean.Social.SocialFriendBean;
import com.igrs.tivic.phone.Bean.Social.SocialFriendBeanList;
import com.igrs.tivic.phone.Impl.SocialImpl;
import com.igrs.tivic.phone.Interface.ISocial;
import com.igrs.tivic.phone.Listener.SocialUpadteUIListener;

import android.os.AsyncTask;

public class SocialMkFriendsAsyncTask extends
		AsyncTask<Object, Object, SocialFriendBeanList> {
	private List<SocialFriendBean> mkfBeans;
	private SocialFriendBeanList mkfList;
	private SocialUpadteUIListener socialUpadteUIListener;
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		mkfBeans = new ArrayList<SocialFriendBean>();
	}

	@Override
	protected SocialFriendBeanList doInBackground(Object... params) {
		// TODO Auto-generated method stub
		ISocial socialImpl = new SocialImpl();
		mkfList = socialImpl.getSocialMKFriendList();
		return mkfList;
	}
	
	/*@Override
	protected List<SocialFriendBean> doInBackground(Object... params) {
		ISocial socialImpl = new SocialImpl();
		mkfBeans = socialImpl.getSocialMKFriend("");
		return mkfBeans;
	}*/

	@Override
	protected void onPostExecute(SocialFriendBeanList result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(socialUpadteUIListener != null)
			socialUpadteUIListener.notifySocilaMkFriendUI(result);
	}

	

	public void setSocialUpadteUIListener(SocialUpadteUIListener socialUpadteUIListener){
		this.socialUpadteUIListener = socialUpadteUIListener;
	}
}
