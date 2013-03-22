package com.igrs.tivic.phone.AsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.igrs.tivic.phone.Bean.EPG.EPGChannelBean;
import com.igrs.tivic.phone.Bean.EPG.EPGChannelListBean;
import com.igrs.tivic.phone.Global.URLConfig;
import com.igrs.tivic.phone.Impl.EPGImpl;
import com.igrs.tivic.phone.Interface.IEPG;
import com.igrs.tivic.phone.Listener.EPGUpdateUIListener;
import com.igrs.tivic.phone.Parser.EPGDataParser;
import com.igrs.tivic.phone.Utils.HttpClientUtils;
import com.igrs.tivic.phone.Utils.JsonForRequest;

import android.os.AsyncTask;

public class EPGChannelAsyncTask extends AsyncTask<String, Object, EPGChannelListBean> {
	private EPGUpdateUIListener epgUpdateUIListener;
	private List<EPGChannelBean> epgChannelBeans;
	private EPGChannelListBean epgListBean;
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		epgChannelBeans = new ArrayList<EPGChannelBean>();
	}


	/*@Override
	protected List<EPGChannelBean> doInBackground(String... params) {
		IEPG epgImpl = new EPGImpl();
		epgChannelBeans = epgImpl.getEPGChannelList(params[0]);
		return epgChannelBeans;
		
	}*/

	@Override
	protected EPGChannelListBean doInBackground(String... params) {
		// TODO Auto-generated method stub
		IEPG epgImpl = new EPGImpl();
		epgListBean = epgImpl.getChannelList();
		return epgListBean;
	}
	
	@Override
	protected void onPostExecute(EPGChannelListBean result) {
		// TODO Auto-generated method stub
		if(epgUpdateUIListener != null)
			epgUpdateUIListener.onChannelListGet(result);
	}
	public void setEpgUpdateUIListener(EPGUpdateUIListener epgUpdateUIListener) {
		this.epgUpdateUIListener = epgUpdateUIListener;
	}


	

}
