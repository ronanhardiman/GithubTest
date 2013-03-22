package com.igrs.tivic.phone.AsyncTask;

import com.igrs.tivic.phone.Bean.MarqueeListBean;
import com.igrs.tivic.phone.Impl.EPGImpl;
import com.igrs.tivic.phone.Interface.IEPG;
import com.igrs.tivic.phone.Listener.EPGMarqueeListener;

import android.os.AsyncTask;

public class EPGMarqueeAsyncTask extends AsyncTask<Object, Object, MarqueeListBean> {
	private EPGMarqueeListener marqueeListener;
	
	
	public void setMarqueeListener(EPGMarqueeListener marqueeListener) {
		this.marqueeListener = marqueeListener;
	}


	@Override
	protected MarqueeListBean doInBackground(Object... params) {
		// TODO Auto-generated method stub
		IEPG epgImpl = EPGImpl.getInstance();
		MarqueeListBean maBean = epgImpl.getMarquee();
		return maBean;
	}


	@Override
	protected void onPostExecute(MarqueeListBean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(marqueeListener != null){
			marqueeListener.notifyEPGMarquee(result);
		}
	}

}
