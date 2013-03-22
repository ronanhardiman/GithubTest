package com.igrs.tivic.phone.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import com.igrs.tivic.phone.Bean.Base.PhotoListBean;
import com.igrs.tivic.phone.Global.URLConfig;
import com.igrs.tivic.phone.Impl.SocialImpl;
import com.igrs.tivic.phone.Listener.SocialPhotoListener;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

public class SocialPhotoAsyncTask extends AsyncTask<Integer, Object, PhotoListBean> {
//	private List<Bitmap> myPhotoList;
	private PhotoListBean pBean;
	private SocialPhotoListener socialPhotoListener;
	private Context context;
	
	
	public SocialPhotoAsyncTask(Context context) {
		super();
		this.context = context;

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pBean = new PhotoListBean();
//		myPhotoList = new ArrayList<Bitmap>();
	}

	@Override
	protected PhotoListBean doInBackground(Integer... arg0) {
		SocialImpl socialImpl = new SocialImpl(context);
		String url = URLConfig.get_photo_list;
		int photo_uid = arg0[0];
		pBean = socialImpl.getPhotoList(url,photo_uid);
		return pBean;
	}

	@Override
	protected void onPostExecute(PhotoListBean result) {
		super.onPostExecute(result);
		if(socialPhotoListener != null)
			socialPhotoListener.notifySocialPhotoUI(result);
	}

	public void setSocialPhotoListener(SocialPhotoListener socialPhotoListener) {
		this.socialPhotoListener = socialPhotoListener;
	}

}
