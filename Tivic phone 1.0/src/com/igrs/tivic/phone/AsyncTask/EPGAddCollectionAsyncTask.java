package com.igrs.tivic.phone.AsyncTask;

import com.igrs.tivic.phone.Impl.EPGImpl;
import com.igrs.tivic.phone.Interface.IEPG;
import com.igrs.tivic.phone.Listener.EPGAddCollectionListener;

import android.os.AsyncTask;

public class EPGAddCollectionAsyncTask extends
		AsyncTask<String[], Object, Object> {
	private EPGAddCollectionListener addCollectionListener;
	
	public void setAddCollectionListener(
			EPGAddCollectionListener addCollectionListener) {
		this.addCollectionListener = addCollectionListener;
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
		epgImpl.focusProgram(str[0], str[1]);
		return null;
	}

	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(addCollectionListener != null)
			addCollectionListener.notifyEPGAddCollection();
	}

}
