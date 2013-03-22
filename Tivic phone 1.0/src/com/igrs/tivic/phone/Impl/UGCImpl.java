package com.igrs.tivic.phone.Impl;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.igrs.tivic.phone.Bean.BaseResponseBean;
import com.igrs.tivic.phone.Bean.LocationBean;
import com.igrs.tivic.phone.Bean.Base.BaseParamBean;
import com.igrs.tivic.phone.Bean.Base.UserBean;
import com.igrs.tivic.phone.Bean.UGC.UGCDataBean;
import com.igrs.tivic.phone.Bean.UGC.UGCListBean;
import com.igrs.tivic.phone.Bean.UGC.UGCPublishBean;
import com.igrs.tivic.phone.Bean.UGC.UGCPublishRetBean;
import com.igrs.tivic.phone.Bean.UGC.UGCReplyBean;
import com.igrs.tivic.phone.Bean.UGC.UGCReplyListBean;
import com.igrs.tivic.phone.Interface.IUGC;
import com.igrs.tivic.phone.Parser.NotifyMessageParser;
import com.igrs.tivic.phone.Parser.UGCDataParser;
import com.igrs.tivic.phone.Utils.HttpClientUtils;
import com.igrs.tivic.phone.Utils.JsonForRequest;
import com.igrs.tivic.phone.Utils.Utils;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.URLConfig;

public class UGCImpl implements IUGC {

	HttpClientUtils httpClientUtils;
	private static UGCImpl instance;

	public static final int MSG_GET_UGC_LIST = 100;
	public static final int MSG_GET_UGC_DETAIL = 101;
	public static final int MSG_GET_UGC_DETAIL_PUBLISG_LIST = 102;
	public static final int MSG_GET_MY_UGC_LIST = 103;
	public static final int MSG_REPLY_PUBLISH = 104;
	public static final int MSG_UGC_NEW = 105;
	public static final int MSG_UGC_SUPPORT = 106;

	private static String TAG = "UGCImpl";
	Handler ugcUpdateUIHandler;
	Handler publishUpdateUIHandler;
	Handler myUGCUpdateUIHandler;

	public UGCImpl() {
		httpClientUtils = HttpClientUtils.getInstance();
	}

	public static UGCImpl getInstance() {
		if (instance == null) {
			instance = new UGCImpl();
		}
		return instance;
	}

	public void setUgcUpdateUIHandler(Handler handler) {
		this.ugcUpdateUIHandler = handler;
	}

	public void setPublishUpdateUIHandler(Handler handler) {
		this.publishUpdateUIHandler = handler;
	}

	public void setMyUGCUpdateUIHandler(Handler handler) {
		this.myUGCUpdateUIHandler = handler;
	}

	@Override
	public boolean getUGCList(BaseParamBean param) {
		// TODO Auto-generated method stub
		if (param == null)
			return false;

		final BaseParamBean paramtest = param;
		new Thread() {
			public void run() {
				// step1
				// UGCListBean ugcList = new UGCListBean();
				// ArrayList<UGCDataBean> datalist = new
				// ArrayList<UGCDataBean>();
				// ugcList.setCount(paramtest.getCountInPage());
				// ugcList.setIfMore(1);
				// for(int i=0; i<paramtest.getCountInPage(); i++)
				// {
				//
				// UGCDataBean data = new UGCDataBean();
				// UserBean usrinfo = new UserBean();
				// usrinfo.setUid(100 + i);
				// usrinfo.setUserNickName("usrName" + i);
				// usrinfo.setUserGender(i%2);
				// usrinfo.setUsrLocation(new LocationBean(101, 102));
				// data.setUsrinfo(usrinfo);
				// data.setTid(200 + i);
				// data.setTitle("This is the UGC title " + i);
				// if(i%2 == 0)
				// data.setContent("帖子内容帖子内容帖子内容帖子内容帖子内容帖子内容帖子内容帖子内容帖子内容帖子内容帖子内容帖子内容帖子内容帖子内容帖子内容帖子内容帖子内容帖子内容帖子内容帖子内容帖子内容！");
				// else
				// data.setContent("帖子内容帖子内容!");
				// data.setImageUrl("");
				// data.setTime(Utils.getCurrentTimeHMS());
				// data.setTop(i%2);
				// data.setUpCount(20+i);
				// data.setReplyContent(50+i);
				// datalist.add(data);
				// }
				// ugcList.setDataList(datalist);
				// step2
				final String jsonrequest = JsonForRequest
						.getUGCListParamJson(paramtest);
				// Log.i("kevin add:", "jsonrequest 1 = " + jsonrequest);
				Message msg = ugcUpdateUIHandler.obtainMessage();
				msg.arg1 = MSG_GET_UGC_LIST;
				String jsonret = httpClientUtils.requestPostHttpClient4(
						URLConfig.UGC_getUGCListUrl, "utf-8", jsonrequest);
				UGCListBean ugcList = null;
//				Log.i("kevin add:", "jsonrequest 2 = " + jsonret);
				if (jsonret.equals(String
						.valueOf(Const.SOCKET_TIMEOUT_EXCEPTION))) {
					// 服务器请求失败
					msg.obj = null;
					Log.e(TAG, "kevin add: get UGC lsit error! " + jsonret);
				} else {
					ugcList = UGCDataParser.getUGCList(jsonret);
					// Log.i(TAG, "kevin add:  count = " + ugcList.getCount() +
					// "ifMore = " + ugcList.getIfMore());

					if (ugcList == null || ugcList.getDataList() == null)
						Log.e(TAG, "kevin add: parse json failed!");
					msg.obj = ugcList;
				}
//				if(ugcList != null && ugcList.getDataList() != null && ugcList.getDataList().size() > 1)
//					Log.i("kevin add:", "jsonrequest 2 = tid = " + ugcList.getDataList().get(0).getTid());

				// step3
				// TODO
				ugcUpdateUIHandler.sendMessage(msg);
			}
		}.start();

		return true;

	}

	public boolean replyPublish(BaseParamBean param, UGCPublishBean bean) {
		if (param == null)
			return false;

		final BaseParamBean paramtest = param;
		final UGCPublishBean beantest = bean;
		new Thread() {
			public void run() {
				// step1
				// step2
				final String jsonrequest = JsonForRequest.makePublishParamJson(
						paramtest, beantest);
				// Log.i("kevin add:", "jsonrequest 51 = " + jsonrequest);
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("json", jsonrequest);
				if (beantest.getType() == 0)
					map.put("file", beantest.getPhotoUrl());
				String jsonret = httpClientUtils.requestPostHttpClient(
						URLConfig.UGC_replyUrl, "utf-8", map);
				// Log.i("kevin add:", "jsonrequest 52 = " + jsonret);
				BaseResponseBean retbean = NotifyMessageParser
						.getBaseResponse(jsonret);
				// if(retbean.getRet() != 0)
				// {
				// Log.i("kevin add:", "jsonrequest 53 = " + retbean.getMsg());
				// }
				int ret = UGCDataParser.parserReplyPost(jsonret);
				// step3
				// TODO

				Message msg = ugcUpdateUIHandler.obtainMessage();
				msg.arg1 = MSG_REPLY_PUBLISH;
				msg.arg2 = ret;
				if (jsonret.equals(String
						.valueOf(Const.SOCKET_TIMEOUT_EXCEPTION))) {
					// 服务器请求失败
					msg.arg2 = -1;
				}
				ugcUpdateUIHandler.sendMessage(msg);
			}
		}.start();
		return true;
	}

	public boolean supportPublish(BaseParamBean param) {
		if (param == null)
			return false;

		final BaseParamBean paramtest = param;
		new Thread() {
			public void run() {
				// step1
				// step2
				final String jsonrequest = JsonForRequest
						.supportOrdeleteParamJson(paramtest);
				// Log.i(TAG, "kevin add: jsonrequest 10 = "+jsonrequest);

				String jsonret = httpClientUtils.requestPostHttpClient4(
						URLConfig.UGC_supportUrl, "utf-8", jsonrequest);
				// Log.i(TAG, "kevin add: jsonrequest 11 = "+jsonret);

				BaseResponseBean retbean = NotifyMessageParser
						.getBaseResponse(jsonret);
				int ret = 0;
				Message msg = ugcUpdateUIHandler.obtainMessage();
				msg.arg1 = MSG_UGC_SUPPORT;
				if (retbean.getRet() != 0)
					ret = -1;
				else
					ret = UGCDataParser.parserReplyPost(jsonret); // 支持、回帖等返回值类型相同，统一处理
					// step3
					// TODO
				msg.arg2 = ret;
				ugcUpdateUIHandler.sendMessage(msg);
			}
		}.start();
		return true;
	}

	public boolean newPublish(BaseParamBean param, UGCPublishBean bean) {
		if (param == null)
			return false;

		final BaseParamBean paramtest = param;
		final UGCPublishBean beantest = bean;
		new Thread() {
			public void run() {
				// step1
				// step2
				final String jsonrequest = JsonForRequest.makePublishParamJson(
						paramtest, beantest);
//				Log.i("kevin add:", "jsonrequest = " + jsonrequest);
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("json", jsonrequest);
				if (beantest.getType() == 0)
					map.put("file", beantest.getPhotoUrl());

				Message msg = ugcUpdateUIHandler.obtainMessage();
				msg.arg1 = MSG_UGC_NEW;
				String jsonret = httpClientUtils.requestPostHttpClient(
						URLConfig.UGC_postUGCUrl, "utf-8", map);
				if (jsonret.equals(String
						.valueOf(Const.SOCKET_TIMEOUT_EXCEPTION))) {
					// 服务器请求失败
					msg.obj = null;
				} else {
					UGCPublishRetBean ret = UGCDataParser
							.parserPublishPost(jsonret);
					msg.obj = ret;
				}

				// step3
				ugcUpdateUIHandler.sendMessage(msg);
			}
		}.start();
		return true;
	}

	public boolean getReplyList(BaseParamBean param) {
		if (param == null)
			return false;
		final BaseParamBean paramtest = param;
		// final String jsonrequest = httpClientUtils.makeJsonRequest(param);
		new Thread() {
			public void run() {
				// step1
				// UGCReplyListBean replyList = new UGCReplyListBean();
				// ArrayList<UGCReplyBean> datalist = new
				// ArrayList<UGCReplyBean>();
				// replyList.setIfMore(1);
				// for(int i=0; i<paramtest.getCountInPage(); i++)
				// {
				// UGCReplyBean data = new UGCReplyBean();
				// UserBean usrinfo = new UserBean();
				// usrinfo.setUid(100 + i);
				// usrinfo.setUserNickName("usrName" + i);
				// usrinfo.setUserGender(i%2);
				// usrinfo.setUsrLocation(new LocationBean(101, 102));
				// data.setUsrinfo(usrinfo);
				// data.setProgramId(paramtest.getPid());
				// data.setChannelId(paramtest.getCid());
				// if(i%2 == 0)
				// data.setReplyText("评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容！");
				// else
				// data.setReplyText("评论内容评论内容!");
				// data.setImgUrl("");
				// data.setTime(Utils.getCurrentTimeHMS());
				// datalist.add(data);
				// }
				// replyList.setDataList(datalist);
				// step2
				final String jsonrequest = JsonForRequest
						.getUGCListParamJson(paramtest);
				// Log.i("kevin add:", "jsonrequest 6 = " + jsonrequest);

				String jsonret = httpClientUtils.requestPostHttpClient4(
						URLConfig.UGC_getReplyListUrl, "utf-8", jsonrequest);

				// step3
				// TODO
				Message msg = ugcUpdateUIHandler.obtainMessage();
				msg.arg1 = MSG_GET_UGC_DETAIL_PUBLISG_LIST;
				if (jsonret.equals(String
						.valueOf(Const.SOCKET_TIMEOUT_EXCEPTION))) {
					// 服务器请求失败
					msg.obj = null;
				} else {
					UGCReplyListBean replyList = UGCDataParser
							.getReplyList(jsonret);
					msg.obj = replyList;
					// Log.i("kevin add:", "jsonrequest 7  count1 =" +
					// replyList.getCount());
					// Log.i("kevin add:", "jsonrequest 7  count2 =" +
					// replyList.getDataList().size());

				}

				publishUpdateUIHandler.sendMessage(msg);
			}
		}.start();

		return true;
	}

	public boolean getMyUGCList(BaseParamBean param) {
		if (param == null)
			return false;
		final BaseParamBean paramtest = param;
//		final String jsonrequest = httpClientUtils.makeJsonRequest(param);

		new Thread() {
			public void run() {
				// step1
				//
				// UGCListBean ugcList = new UGCListBean();
				// ArrayList<UGCDataBean> datalist = new
				// ArrayList<UGCDataBean>();
				// ugcList.setCount(paramtest.getCountInPage());
				// ugcList.setIfMore(1);
				// for(int i=0; i<paramtest.getCountInPage(); i++)
				// {
				//
				// UGCDataBean data = new UGCDataBean();
				// UserBean usrinfo = new UserBean();
				// usrinfo.setUid(100 + i);
				// usrinfo.setUserNickName("usrName" + i);
				// usrinfo.setUserGender(i%2);
				// usrinfo.setUsrLocation(new LocationBean(101, 102));
				// data.setUsrinfo(usrinfo);
				// data.setTid(200 + i);
				// data.setTitle("This is the my UGC title!" + i);
				// if(i%2 == 0)
				// data.setContent("帖子内容帖子内容帖子内容帖子内容帖子内容帖子内容帖子内容帖子内容帖子内容帖子内容帖子内容帖子内容帖子内容帖子内容帖子内容帖子内容帖子内容帖子内容帖子内容帖子内容帖子内容！");
				// else
				// data.setContent("帖子内容帖子内容!");
				// data.setImageUrl("");
				// data.setTime(Utils.getCurrentDateYMDHMS());
				// data.setTop(i%2);
				// data.setUpCount(20+i);
				// data.setReplyContent(50+i);
				// datalist.add(data);
				// }
				// try {
				// Thread.sleep(1500);
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }
				// ugcList.setDataList(datalist);
				// step2
				final String jsonrequest = JsonForRequest
						.getUGCListParamJson(paramtest);
				//Log.i("kevin add:", "jsonrequest 3 = " + jsonrequest);
				String jsonret = httpClientUtils.requestPostHttpClient4(
						URLConfig.UGC_getUsrUGCListUrl, "utf-8", jsonrequest);
				UGCListBean ugcList = null;
				Message msg = myUGCUpdateUIHandler.obtainMessage();
				msg.arg1 = MSG_GET_MY_UGC_LIST;

				if (jsonret.equals(String
						.valueOf(Const.SOCKET_TIMEOUT_EXCEPTION))) {
					// 服务器请求失败
					msg.obj = null;
				} else {
					ugcList = UGCDataParser.getUGCList(jsonret);
					msg.obj = ugcList;
				}
				// step3
//				if(ugcList != null)
//					Log.i("kevin add:", "jsonrequest 4 = tid = " + ugcList.getDataList().get(0).getTid());

				myUGCUpdateUIHandler.sendMessage(msg);
			}
		}.start();

		return true;
	}

	//通过界面异步加载数据
	public UGCReplyListBean getReplyList2(BaseParamBean param) {
		if (param == null)
			return null;
		UGCReplyListBean replyList = null;
		final String jsonrequest = JsonForRequest.getUGCListParamJson(param);
		String jsonret = httpClientUtils.requestPostHttpClient4(
				URLConfig.UGC_getReplyListUrl, "utf-8", jsonrequest);
		if (jsonret.equals(String.valueOf(Const.SOCKET_TIMEOUT_EXCEPTION))) {
			// 服务器请求失败
			replyList = null;
		} else {
			replyList = UGCDataParser.getReplyList(jsonret);
		}
		//Log.i("kevin add:", "jsonrequest 51 = " + jsonret);

		return replyList;
	}

}
