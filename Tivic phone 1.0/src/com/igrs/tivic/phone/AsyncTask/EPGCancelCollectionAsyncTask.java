package com.igrs.tivic.phone.AsyncTask;

import com.igrs.tivic.phone.Impl.EPGImpl;
import com.igrs.tivic.phone.Interface.IEPG;
import com.igrs.tivic.phone.Listener.EPGCancelCollectionListener;

import android.os.AsyncTask;

public class EPGCancelCollectionAsyncTask extends
		AsyncTask<String[], Object, Object> {
	private EPGCancelCollectionListener cancelCollectionListener;
	
	
	
	public void setCancelCollectionListener(
			EPGCancelCollectionListener cancelCollectionListener) {
		this.cancelCollectionListener = cancelCollectionListener;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected Object doInBackground(String[]... params) {
		// TODO Auto-generated method stub
		String[] str = params[0];
		IEPG epgImpl = EPGImpl.getInstance();
		epgImpl.removeFocus(str[0], str[1]);
		return null;
	}

	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(cancelCollectionListener != null)
			cancelCollectionListener.notifyEPGCancelCollection();
	}

}
