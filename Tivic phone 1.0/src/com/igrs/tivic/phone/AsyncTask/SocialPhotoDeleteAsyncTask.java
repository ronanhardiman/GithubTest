package com.igrs.tivic.phone.AsyncTask;

import java.util.ArrayList;

import com.igrs.tivic.phone.Global.URLConfig;
import com.igrs.tivic.phone.Impl.SocialImpl;
import com.igrs.tivic.phone.Listener.SocialPhotoListener;

import android.content.Context;
import android.os.AsyncTask;

public class SocialPhotoDeleteAsyncTask extends
		AsyncTask<String, Object, Object> {
	private Context mContext;
	private SocialPhotoListener socialPhotoListener;
	private ArrayList<String> urlList;
	
	
	public SocialPhotoDeleteAsyncTask(Context mContext) {
		super();
		this.mContext = mContext;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		urlList = new ArrayList<String>();
	}

	public void setSocialPhotoListener(SocialPhotoListener socialPhotoListener) {
		this.socialPhotoListener = socialPhotoListener;
	}

	@Override
	protected Object doInBackground(String... params) {
		// TODO Auto-generated method stub
		String url = params[0];
		url = url.replace(URLConfig.avarter_path, "");
		urlList.add(url);
		SocialImpl socialImpl = new SocialImpl(mContext);
		socialImpl.deletePhoto("", urlList);
		return null;
	}

	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(socialPhotoListener != null)
			socialPhotoListener.notifyDeletePhotoUI();
	}

}
