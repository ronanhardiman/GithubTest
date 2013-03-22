package com.igrs.tivic.phone.Impl;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.igrs.tivic.phone.Bean.BaseResponseBean;
import com.igrs.tivic.phone.Bean.Base.BaseParamBean;
import com.igrs.tivic.phone.Bean.EPG.EPGProgramsBean;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Global.URLConfig;
import com.igrs.tivic.phone.Parser.MyTvParser;
import com.igrs.tivic.phone.Parser.NotifyMessageParser;
import com.igrs.tivic.phone.Utils.HttpClientUtils;
import com.igrs.tivic.phone.Utils.JsonForRequest;
import com.igrs.tivic.phone.Utils.Utils;

public class MyTvImpl {
	private static MyTvImpl instance = null;
	private HttpClientUtils httpClientUtils;
	private final static String charset = "utf-8";

	private MyTvImpl() {
		httpClientUtils = HttpClientUtils.getInstance();
	}

	public static MyTvImpl getInstance() {
		if (instance == null) {
			instance = new MyTvImpl();
		}
		return instance;
	}

	/**
	 * 取消收藏mytv/pg_unfav
	 * @param json
	 */
	public BaseResponseBean removeFocus(final String program_id, final String channel_id) {
		BaseParamBean bBean = new BaseParamBean();
		bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserUID());
		bBean.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
		bBean.setVer(1);
		bBean.setPartner_id(TivicGlobal.getInstance().userRegisterBean
				.getUserUID());

		HashMap<String, String> map = new HashMap<String, String>();
		String jsonToSting = JsonForRequest.add_removeFocusPrograms(bBean,program_id, channel_id);
		map.put("json", jsonToSting);
		String json = httpClientUtils.requestPostHttpClient(URLConfig.get_remove_focus, charset, map);
Log.e("aaa","============removeFocus======"+json);
		return NotifyMessageParser.getBaseResponse(json);
	}

	/**
	 * 获取指定用户的关注节目列表:全部节目,按加入关注的时间降序排列 mytv/pg_list_all
	 * @param json
	 */
	public ArrayList<EPGProgramsBean> getFocusProgramsList() {
		BaseParamBean bBean = new BaseParamBean();
		bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserUID());
		bBean.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
		bBean.setVer(1);
		bBean.setPartner_id(TivicGlobal.getInstance().userRegisterBean
				.getUserUID());

		HashMap<String, String> map = new HashMap<String, String>();
		String jsonToSting = JsonForRequest.getPeopleFocusProgramsList(bBean);
		map.put("json", jsonToSting);
		String json = httpClientUtils.requestPostHttpClient(
				URLConfig.get_MyTv_list, charset, map);
//Log.e("aaa","==========mytv=focused=all======"+json);
		BaseResponseBean brBean = NotifyMessageParser.getBaseResponse(json);
		ArrayList<EPGProgramsBean> epgProgramsBeanList = MyTvParser
				.getFocusList(json);
		return epgProgramsBeanList;
	}

	/**
	 * 4, 获取我的关注节目列表:某一天会播出的节目 mytv/pg_list_day
	 * @param day
	 */
	public ArrayList<EPGProgramsBean> getFocusProgramsList(final int day) {
		BaseParamBean bBean = new BaseParamBean();
		bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserUID());
		bBean.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
		bBean.setVer(1);
		bBean.setPartner_id(TivicGlobal.getInstance().userRegisterBean
				.getUserUID());

		HashMap<String, String> map = new HashMap<String, String>();

		int weekDay = Utils.dayForWeek() - 1;// 当前日期是星期几 weekDay值为0-6

		String d = String.valueOf(Utils.unixTimestamp2String(Utils
				.getFromDateToWeek(day - weekDay)));
		String jsonToSting = JsonForRequest.getDayFocusProgramsList(bBean, d);
		map.put("json", jsonToSting);
		String json = httpClientUtils.requestPostHttpClient(
				URLConfig.get_MyTv_daily_list, charset, map);
//		Log.e("afff", "==============focusP============" + json);
		BaseResponseBean brBean = NotifyMessageParser.getBaseResponse(json);
		ArrayList<EPGProgramsBean> dayProgramsBeanList = MyTvParser
				.getDayFocusList(json);
		return dayProgramsBeanList;
	}

	/**
	 * 5,获取的指定用户的关注节目—频道列表 mytv/pg_list_with_channel
	 */
	public void getChannelProgramFocusList() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				BaseParamBean bBean = new BaseParamBean();
				bBean.setUid(TivicGlobal.getInstance().userRegisterBean
						.getUserUID());
				bBean.setSid(TivicGlobal.getInstance().userRegisterBean
						.getUserSID());
				bBean.setVer(1);
				bBean.setPartner_id(TivicGlobal.getInstance().userRegisterBean
						.getUserUID());

				String jsonToSting = JsonForRequest
						.getChannelProgramFocusList(bBean);
				String json = httpClientUtils.requestPostHttpClient4(
						URLConfig.get_MyTv_channel_program_list, charset,
						jsonToSting);
				BaseResponseBean brBean = NotifyMessageParser
						.getBaseResponse(json);
				SparseArray<SparseIntArray> sArrays = MyTvParser
						.getChannelProgramFocusList(json);
			}
		}).start();
	}
}
