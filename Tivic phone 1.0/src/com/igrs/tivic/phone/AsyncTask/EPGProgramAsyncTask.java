package com.igrs.tivic.phone.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import com.igrs.tivic.phone.Bean.EPG.EPGDailyChannelInfo;
import com.igrs.tivic.phone.Bean.EPG.EPGProgramBean;
import com.igrs.tivic.phone.Impl.EPGImpl;
import com.igrs.tivic.phone.Listener.EPGUpdateUIListener;

import android.os.AsyncTask;

public class EPGProgramAsyncTask extends
		AsyncTask<String[], Object, EPGDailyChannelInfo> {
	private EPGUpdateUIListener epgUpdateUIListener;
	private List<EPGProgramBean> programBeans;
	private EPGDailyChannelInfo epgDailyChannelInfo;
	private int position;

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		programBeans = new ArrayList<EPGProgramBean>();
	}

	/*
	 * @Override protected List<EPGProgramBean> doInBackground(String[]...
	 * params) { // TODO Auto-generated method stub EPGImpl epgImpl = new
	 * EPGImpl(); programBeans = epgImpl.getEPGProgramList(params[0]); return
	 * programBeans; }
	 */
	@Override
	protected EPGDailyChannelInfo doInBackground(String[]... params) {
		// TODO Auto-generated method stub
		EPGImpl epgImpl = new EPGImpl();
		String[] param = params[0];
		epgDailyChannelInfo = epgImpl.getProgramList(param[0], param[1]);
		return epgDailyChannelInfo;
	}

	@Override
	protected void onPostExecute(EPGDailyChannelInfo result) {
		// TODO Auto-generated method stub
		if (epgUpdateUIListener != null)
			epgUpdateUIListener.onProgramListGet(result, position);
	}

	public void setEpgUpdateUIListener(EPGUpdateUIListener epgUpdateUIListener) {
		this.epgUpdateUIListener = epgUpdateUIListener;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}
