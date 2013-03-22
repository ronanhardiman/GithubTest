package com.igrs.tivic.phone.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import com.igrs.tivic.phone.Bean.Login.UserPOIItemBean;
import com.igrs.tivic.phone.Bean.Login.UserRegisterBean;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Impl.LoginImpl;
import com.igrs.tivic.phone.Listener.SocialRecommendListener;

import android.os.AsyncTask;

public class SocialRecommedListAsyncTask extends
		AsyncTask<Object, Object, List<UserPOIItemBean>> {
	private List<UserPOIItemBean> remmendList;
	private UserRegisterBean rBean;
	private LoginImpl loginImpl;
	private SocialRecommendListener recommendListener;

	public void setRecommendListener(SocialRecommendListener recommendListener) {
		this.recommendListener = recommendListener;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		remmendList = new ArrayList<UserPOIItemBean>();
		rBean = TivicGlobal.getInstance().userRegisterBean;
		loginImpl = new LoginImpl();
	}

	@Override
	protected List<UserPOIItemBean> doInBackground(Object... params) {
		// TODO Auto-generated method stub
		remmendList.clear();
		List<UserPOIItemBean> poiStarsInfo = loginImpl.getPoiStarsInfo(rBean);
		List<UserPOIItemBean> poiProgramsInfo = loginImpl
				.getPoiProgramsInfo(rBean);
		List<UserPOIItemBean> poiCategoriesInfo = loginImpl
				.getPoiCategoriesInfo(rBean);
		if (poiStarsInfo != null)
			remmendList.addAll(poiStarsInfo);
		if (poiProgramsInfo != null)
			remmendList.addAll(poiProgramsInfo);
		if (poiCategoriesInfo != null)
			remmendList.addAll(poiCategoriesInfo);
		return remmendList;
	}

	@Override
	protected void onPostExecute(List<UserPOIItemBean> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(recommendListener != null)
			recommendListener.notifySocialRecommendUI(result);
	}

}
