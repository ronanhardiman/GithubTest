package com.igrs.tivic.phone.Impl;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.igrs.tivic.phone.Bean.LocationBean;
import com.igrs.tivic.phone.Bean.Base.BaseParamBean;
import com.igrs.tivic.phone.Bean.Base.UserBean;
import com.igrs.tivic.phone.Bean.Social.SocialMessageBean;
import com.igrs.tivic.phone.Bean.Social.SocialMessageHistoryListBean;
import com.igrs.tivic.phone.Bean.Social.SocialMessageListBean;
import com.igrs.tivic.phone.Bean.Social.SocialMsgBean;
import com.igrs.tivic.phone.Bean.Social.SocialNewsBean;
import com.igrs.tivic.phone.Bean.Social.SocialNotifyBean;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Global.URLConfig;
import com.igrs.tivic.phone.Interface.IMessage;
import com.igrs.tivic.phone.Parser.NotifyMessageParser;
import com.igrs.tivic.phone.Utils.HttpClientUtils;
import com.igrs.tivic.phone.Utils.JsonForRequest;
import com.igrs.tivic.phone.Utils.Utils;

public class MessageImpl implements IMessage {

	HttpClientUtils httpClientUtils;
	private static  MessageImpl instance;

	public static final int NUM_MAX = 100;
	public static final int NUM_MIN = 5;
	public static final int NUM_MIDDLE = 10;

	public static final int MSG_GET_MESSAGE_LIST = 100;
	public static final int MSG_GET_MESSAGE_HISTORY_LIST = 101;
	public static final int MSG_SEND_MESSAGE = 102;

	private static String TAG = "MessageImpl";

	Handler updateUIHandler;


	public MessageImpl()
	{
		httpClientUtils = HttpClientUtils.getInstance();
	}

	public static MessageImpl getInstance()
	{
		if(instance == null)
		{
			instance = new  MessageImpl();
		}
		return instance;
	}

	public void setUpdateUIHandler(Handler handler)
	{
		this.updateUIHandler = handler;
	}

	
	@Override
	public boolean getMessageList(BaseParamBean param) {
		// TODO Auto-generated method stub
		if(param == null)
			return false;
		
		final BaseParamBean paramtest = param;

		new Thread(){
			   public void run(){				   
//step1				
//				   SocialMessageListBean listBean = new SocialMessageListBean();		  
//				   ArrayList<SocialMessageBean> datalist = new ArrayList<SocialMessageBean>();
//				   listBean.setCount(NUM_MAX);
//				   listBean.setIfMore(1);
//					for(int i=0; i<NUM_MAX; i++)
//					{
//						
//						SocialMessageBean data = new SocialMessageBean();
//						UserBean usrinfo = new UserBean();
//						usrinfo.setUid(100 + i);
//						usrinfo.setUserNickName("usrName" + i);
//						usrinfo.setUserGender(i%2);
//						usrinfo.setUsrLocation(new LocationBean(101, 102));
//						data.setUsrinfo(usrinfo);				
//						if(i%2 == 0)
//						{
//							data.setText("This is long message content This is long message content This is long message content!");
//							
//						}else{
//							data.setText("This is short message!");
//
//						}
//						data.setUid(100+i);
//						data.setFrom(10+i);
//						data.setTo(20+i);
//						
//						data.setTime(Utils.getCurrentTimeHMS());
//						datalist.add(data);
//					}
//					listBean.setMessageList(datalist);
//step2
					final String jsonrequest = JsonForRequest.getMessageParamJson(paramtest);
//					Log.i(TAG, "kevin add: jsonrequest 8 = "+jsonrequest);

					String jsonret = httpClientUtils.requestPostHttpClient4(URLConfig.Message_getMessageHomeListUrl, "utf-8", jsonrequest);
					SocialMessageListBean listBean = NotifyMessageParser.getMessageList(jsonret);
//					Log.i(TAG, "kevin add: jsonrequest 9 = "+jsonret);

//step3		
					//TODO
					//拉取最新私信时，获得好友发的最新未读私信个数
					if(listBean != null && paramtest.getOffset() == 0){
						ArrayList<SocialMsgBean> newlist = getNewMessageList();
						if(newlist != null && newlist.size() > 0 && listBean.getCount() > 0 && listBean.getMessageList() != null)
						{
							int size = listBean.getMessageList().size();
							for(int i=0; i<size; i++)
							{
								listBean.getMessageList().get(i).setCountnew(findCountByUid(listBean.getMessageList().get(i).getPartner_id(), newlist));
							}
						}
					}
					 Message msg = updateUIHandler.obtainMessage(); 
					 msg.arg1 = MSG_GET_MESSAGE_LIST;
					 msg.obj = listBean;
					 updateUIHandler.sendMessage(msg);
			   }
			}.start();
			

			 
			 return true;
	}

	private int findCountByUid(int uid, ArrayList<SocialMsgBean> newlist)
	{
		int count = 0;
		for(int i=0; i< newlist.size(); i++)
		{
			int tempuid = Integer.parseInt(newlist.get(i).getUid());
			if(tempuid == uid)
			{
				count = newlist.get(i).getCount();
				break;
			}
		}
		return count;
	}
	
	@Override
	public boolean getMsgHistoryList(BaseParamBean param) {
		// TODO Auto-generated method stub
		if(param == null)
			return false;
		final BaseParamBean parambean = param;
		
		new Thread(){
			   public void run(){				   
//step1				
//				   SocialMessageListBean listBean = new SocialMessageListBean();		  
//				   ArrayList<SocialMessageBean> datalist = new ArrayList<SocialMessageBean>();
//				   listBean.setCount(NUM_MAX);
//				   listBean.setIfMore(1);
//					for(int i=0; i<NUM_MAX; i++)
//					{
//						
//						SocialMessageBean data = new SocialMessageBean();
//						UserBean usrinfo = new UserBean();
//						
//						usrinfo.setUserNickName("usrName" + i);
//						usrinfo.setUserGender(i%2);
//						usrinfo.setUsrLocation(new LocationBean(101, 102));
//						data.setUsrinfo(usrinfo);
//						usrinfo.setUid(100 + i);
//						if(i%2 == 0)
//						{					
//							data.setText("This is long message content This is long message content This is long message content!");
//							data.setFrom(100); //模拟用户自己的id
//							data.setTo(parambean.getUid()); //该好友的id
//							data.setComeMsg(false);
//						}else{
//							data.setText("This is short message!");
//							data.setFrom(parambean.getUid()); //模拟用户自己的id
//							data.setTo(100); //该好友的id
//							data.setComeMsg(true);
//						}
//						data.setImgUrl("");
//						data.setTime(Utils.getCurrentTimeHMS());
//						datalist.add(data);
//					}
//					listBean.setMessageList(datalist);
//step2
					final String jsonrequest = JsonForRequest.getHistoryMessageParamJson(parambean);
					//Log.i(TAG, "kevin add: jsonrequest 11 = "+jsonrequest);

					String jsonret = httpClientUtils.requestPostHttpClient4(URLConfig.Message_getMessageHistoryListUrl, "utf-8", jsonrequest);
//					Log.i(TAG, "kevin add: jsonrequest 12 getMsgHistoryList = "+jsonret);

					SocialMessageHistoryListBean listBean = NotifyMessageParser.getMessageHistoryList(jsonret);
//step3		
					//TODO
					
					 Message msg = updateUIHandler.obtainMessage(); 
					 msg.arg1 = MSG_GET_MESSAGE_HISTORY_LIST;
					 msg.obj = listBean;
					 updateUIHandler.sendMessage(msg);
			   }
			}.start();
			

			 
			 return true;
	}

	@Override
	public boolean sendMessage(BaseParamBean param, SocialMessageBean bean) {
		// TODO Auto-generated method stub
		if(bean == null)
			return false;
		final SocialMessageBean beantest = bean;
		final BaseParamBean paramtest = param;
		new Thread(){
			public void run(){
				//Log.d(TAG, "kevin add: jsonrequest 12 partner_id = " + paramtest.getPartner_id());

				final String jsonrequest = JsonForRequest.getSendMessageParamJson(paramtest, beantest);
				//Log.i(TAG, "kevin add: jsonrequest 13 = "+jsonrequest);

				HashMap<String, String> map = new HashMap<String, String>();
				map.put("json", jsonrequest);
				if(beantest.getType() == 0)//上传本地图片
					map.put("file", beantest.getImgUrl());
				String jsonret = httpClientUtils.requestPostHttpClient(URLConfig.Message_sendMessageUrl, "utf-8", map);
//				Log.i(TAG, "kevin add: jsonrequest 14 = "+jsonret);
				SocialMessageBean ret;
				if(jsonret != null && !jsonret.isEmpty() && !jsonret.equals(String.valueOf(Const.SOCKET_TIMEOUT_EXCEPTION)))
					ret = NotifyMessageParser.sendMessage(jsonret);
				else
					ret = null;
//				Log.d(TAG, "kevin add: jsonrequest sendMessage = "+jsonret);
				Message msg = updateUIHandler.obtainMessage(); 
				msg.arg1 = MSG_SEND_MESSAGE;
				msg.obj = ret;
				updateUIHandler.sendMessage(msg);
		   }
		}.start();
		
		return false;
	}

	public ArrayList<SocialMsgBean> getNewMessageList()
	{
		BaseParamBean param = new BaseParamBean();
		param.setVer(1);
		param.setUid(TivicGlobal.getInstance().userRegisterBean.getUserId()); // 其实在这里这个参数没用
		param.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());;
		SocialNewsBean ret = NotifyImpl.getInstance().getCountNew(param);
		if(ret!= null && ret.getMsg_sum() >0)
		{
			return ret.getMessageBeanList();
		}
		return null;
	}

}
