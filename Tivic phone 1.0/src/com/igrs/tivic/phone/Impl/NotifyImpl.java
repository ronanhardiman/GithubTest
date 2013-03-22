package com.igrs.tivic.phone.Impl;

import java.util.ArrayList;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.igrs.tivic.phone.Bean.LocationBean;
import com.igrs.tivic.phone.Bean.Base.BaseParamBean;
import com.igrs.tivic.phone.Bean.Base.UserBean;
import com.igrs.tivic.phone.Bean.Social.SocialNewsBean;
import com.igrs.tivic.phone.Bean.Social.SocialNotifyBean;
import com.igrs.tivic.phone.Bean.Social.SocialNotifyListBean;
import com.igrs.tivic.phone.Bean.UGC.UGCDataBean;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.URLConfig;
import com.igrs.tivic.phone.Interface.INotify;
import com.igrs.tivic.phone.Parser.NotifyMessageParser;
import com.igrs.tivic.phone.Utils.HttpClientUtils;
import com.igrs.tivic.phone.Utils.JsonForRequest;
import com.igrs.tivic.phone.Utils.Utils;

public class NotifyImpl implements INotify {

	HttpClientUtils httpClientUtils;
	private static  NotifyImpl instance;
	
	public static  int NUM_MAX = 100;
	public static  int NUM_MIN = 5;
	public static  int NUM_MIDDLE = 10;
	
	public static final int MSG_GET_NOTIFY_LIST = 100;

	
	private static String TAG = "NotifyImpl";
	
	Handler notifyUpdateUIHandler;

	
	public NotifyImpl()
	{
		httpClientUtils = HttpClientUtils.getInstance();
	}
	
	public static NotifyImpl getInstance()
	{
		if(instance == null)
		{
			instance = new  NotifyImpl();
		}
		return instance;
	}
	
	public void setNotifyUpdateUIHandler(Handler handler)
	{
		this.notifyUpdateUIHandler = handler;
	}
	
	@Override
	public boolean getNotifyList(BaseParamBean param) {
		// TODO Auto-generated method stub
		if(param == null)
			return false;
		
		final BaseParamBean paramtest = param;
		 if(paramtest.getOpreate() == 1)
		 {
			 //TODO
			 countNew = null;
		 }
		new Thread(){
			   public void run(){				   

					final String jsonrequest = JsonForRequest.getNoticeParamJson(paramtest);
					//Log.i(TAG, "kevin add: jsonrequest 10 = "+jsonrequest);
					String jsonret = httpClientUtils.requestPostHttpClient4(URLConfig.Notify_getNotifyListUrl, "utf-8", jsonrequest);
					//Log.i(TAG, "kevin add: jsonrequest 11 = "+jsonret);
					SocialNotifyListBean listBean = null;
					listBean = NotifyMessageParser.getNoticeHistoryList(jsonret);
					//step3		
					//TODO
					
					 Message msg = notifyUpdateUIHandler.obtainMessage(); 
					 msg.arg1 = MSG_GET_NOTIFY_LIST;
					 msg.obj = listBean;
					 notifyUpdateUIHandler.sendMessage(msg);
						//0：只查询；
						//1：查询并设置所有未读消息为已读，且仅在pageIndex为0时生效。

			   }
			}.start();
					 
			 return true;

	}

	private SocialNewsBean countNew = null;
	
	public SocialNewsBean getCountNew(BaseParamBean param)
	{
		if(param == null)
			return null;
		if(countNew == null)
			countNew = new SocialNewsBean();
		final String jsonrequest = JsonForRequest.getBaseInfo_ExtInfo(param);
		//Log.i(TAG, "kevin add: jsonrequest 8 = "+jsonrequest);
		String jsonret = httpClientUtils.requestPostHttpClient4(URLConfig.getCountNewUrl, "utf-8", jsonrequest);
		//Log.i(TAG, "kevin add: jsonrequest 9 = "+jsonret);
		if(jsonret == null || jsonret.length() == 0 || jsonret.equals(String.valueOf(Const.SOCKET_TIMEOUT_EXCEPTION)))
			countNew = null;
		else
			countNew = NotifyMessageParser.getCountNews(jsonret);

		return countNew;
			
	}
}
