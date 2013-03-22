package com.igrs.tivic.phone.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.igrs.tivic.phone.Listener.SocialPhotoBrowseListener;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

public class SocialPhotoBrowseAsyncTask extends
		AsyncTask<String, Object, Drawable> {
	private SocialPhotoBrowseListener socialPhotoBrowseListener;
	
	
	
	public void setSocialPhotoBrowseListener(
			SocialPhotoBrowseListener socialPhotoBrowseListener) {
		this.socialPhotoBrowseListener = socialPhotoBrowseListener;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected Drawable doInBackground(String... params) {
		// TODO Auto-generated method stub
		Drawable drawable = null;
		try {
			
			URL photoUrl = new URL(params[0]);
			HttpURLConnection conn = (HttpURLConnection) photoUrl.openConnection();
			conn.setConnectTimeout(5000);
			conn.setDoInput(true);
			conn.connect();
			InputStream inputStream=conn.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(inputStream); 
			drawable = new BitmapDrawable(bitmap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return drawable;
	}

	@Override
	protected void onPostExecute(Drawable result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(socialPhotoBrowseListener != null)
			socialPhotoBrowseListener.notifyPhotoBrowse(result);
	}

	
}
