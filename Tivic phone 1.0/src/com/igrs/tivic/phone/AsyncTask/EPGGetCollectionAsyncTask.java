package com.igrs.tivic.phone.AsyncTask;

import com.igrs.tivic.phone.Impl.EPGImpl;
import com.igrs.tivic.phone.Impl.MyTvImpl;
import com.igrs.tivic.phone.Interface.IEPG;
import com.igrs.tivic.phone.Listener.EPGCollectionListener;

import android.os.AsyncTask;
import android.util.SparseArray;
import android.util.SparseIntArray;

public class EPGGetCollectionAsyncTask extends AsyncTask<Object, Object, SparseArray<SparseIntArray>> {
	private EPGCollectionListener collectionListener;
	
	
	public void setCollectionListener(EPGCollectionListener collectionListener) {
		this.collectionListener = collectionListener;
	}


	@Override
	protected SparseArray<SparseIntArray> doInBackground(Object... params) {
		// TODO Auto-generated method stub
		IEPG epgImpl = new EPGImpl();
		SparseArray<SparseIntArray> sArrays = epgImpl.getChannelProgramFocusList();
		return sArrays;
	}


	@Override
	protected void onPostExecute(SparseArray<SparseIntArray> result) {
		super.onPostExecute(result);
		if(collectionListener != null)
			collectionListener.notifyEPGCollection(result);
	}

	
}
