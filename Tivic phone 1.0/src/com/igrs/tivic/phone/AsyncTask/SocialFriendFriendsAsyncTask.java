package com.igrs.tivic.phone.AsyncTask;

import com.igrs.tivic.phone.Bean.Social.SocialFriendBeanList;
import com.igrs.tivic.phone.Impl.SocialImpl;
import com.igrs.tivic.phone.Interface.ISocial;
import com.igrs.tivic.phone.Listener.SocialUpadteUIListener;

import android.os.AsyncTask;

public class SocialFriendFriendsAsyncTask extends
		AsyncTask<Integer, Object, SocialFriendBeanList> {
	
	private SocialUpadteUIListener socialUpadteUIListener; 
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected SocialFriendBeanList doInBackground(Integer... params) {
		// TODO Auto-generated method stub
		int uid = params[0];
		ISocial socialImpl = new SocialImpl();
		SocialFriendBeanList friendFriendList = socialImpl.getSocialFriendFriendsList(uid);
		return friendFriendList;
	}

	@Override
	protected void onPostExecute(SocialFriendBeanList result) {
		if(socialUpadteUIListener != null)
			socialUpadteUIListener.notifySocialFriendUI(result);
	}

	public void setSocialUpdateUIListener(
			SocialUpadteUIListener socialUpadteUIListener) {
		// TODO Auto-generated method stub
		this.socialUpadteUIListener = socialUpadteUIListener;
	}

}
