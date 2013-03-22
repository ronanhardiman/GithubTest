package com.igrs.tivic.phone.AsyncTask;

import java.util.ArrayList;

import com.igrs.tivic.phone.Bean.MarqueeListBean;
import com.igrs.tivic.phone.Bean.PGC.NewsBean;
import com.igrs.tivic.phone.Bean.PGC.PushTVBean;
import com.igrs.tivic.phone.Bean.PGC.TVFindBean;
import com.igrs.tivic.phone.Bean.PGC.VideoBean;
import com.igrs.tivic.phone.Bean.Waterfall.ProgramDetailForPagers;
import com.igrs.tivic.phone.Global.URLConfig;
import com.igrs.tivic.phone.Parser.ContentDetailParser;
import com.igrs.tivic.phone.Parser.MarqueeParser;
import com.igrs.tivic.phone.Parser.WaterFallParser;
import com.igrs.tivic.phone.Utils.HttpClientUtils;
import com.igrs.tivic.phone.sqlite.ITIVIC;
import com.igrs.tivic.phone.sqlite.TIVICImpl;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class EPGAsyncTask extends AsyncTask<String, Object, String> {
	private Context context;
	
	
	public EPGAsyncTask(Context context) {
		this.context = context;
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		ITIVIC tivicImpl = new TIVICImpl(context);
		System.out.println("params="+params[0]+","+params[1]);
		String data = tivicImpl.getEPGChannelList(params[0].toString(), params[1].toString());
		
		//just for test;
//		String json = HttpClientUtils.getInstance().requesPost(URLConfig.test_news, "utf-8");
//		String json = HttpClientUtils.getInstance().requestGetHttpClient(URLConfig.test_news, "utf-8");
//		String json = HttpClientUtils.getInstance().requestGet(URLConfig.test_marqueeIdList, "utf-8");
		
//		Log.i("lq", "json : ============"+json);
//		String json = HttpClientUtils.getInstance().requestGet(URLConfig.test_news, "utf-8");
//		String json = HttpClientUtils.getInstance().requestGet("http://10.1.33.49/test/epg.json", "utf-8");
//		NewsBean nBean = ContentDetailParser.getContentNews(json);
//		PushTVBean ptBean = ContentDetailParser.getContentPushTV(json);
//		TVFindBean tvBean = ContentDetailParser.getContentTVfind(json);
//		VideoBean vBean = ContentDetailParser.getContentVide(json);
//		ArrayList<ProgramDetailForPagers> lists = WaterFallParser.parserProgramDetail2(json);
//		MarqueeListBean mListBean = MarqueeParser.getMarqueeData(json);
		return data;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

}
