package com.igrs.tivic.phone.Impl;


import java.util.ArrayList;
import java.util.HashMap;

import com.igrs.tivic.phone.Bean.BaseResponseBean;
import com.igrs.tivic.phone.Bean.Base.BaseParamBean;
import com.igrs.tivic.phone.Bean.EPG.EPGProgramsBean;
import com.igrs.tivic.phone.Bean.PGC.ProgramInfoBean;
import com.igrs.tivic.phone.Bean.PGC.PushTVBean;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Global.URLConfig;
import com.igrs.tivic.phone.Parser.ContentDetailParser;
import com.igrs.tivic.phone.Parser.MyTvParser;
import com.igrs.tivic.phone.Parser.NotifyMessageParser;
import com.igrs.tivic.phone.Utils.HttpClientUtils;
import com.igrs.tivic.phone.Utils.JsonForRequest;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class PushTvImpl{
	private static PushTvImpl instance = null;
	private HttpClientUtils httpClientUtils;
	private final static String charset = "utf-8";
	public final static int MSG_PUSHTV = 102;		//pushTv
	private Handler contentUpdateHander;
	private Handler isFoucsHandler;	//收藏列表
	
	public static final int DO_SUCCESS = 0;//操作成功
	public static final int DO_FAIL = 1;//操作失败
	
	public void setIsFoucsHandler(Handler isFoucsHandler) {
		this.isFoucsHandler = isFoucsHandler;
	}
	private PushTvImpl(){
		httpClientUtils = HttpClientUtils.getInstance();
	}
	public static PushTvImpl getInstance(){
		if(instance == null){
			instance = new PushTvImpl();
		}
		return instance;
	}
	public void setContentUpdateHandler(Handler handler){
		this.contentUpdateHander = handler;
	}
	/***
	 * 获取指定用户的关注节目列表:全部节目,按加入关注的时间降序排列
	 */
	private ArrayList<EPGProgramsBean> getFocusProgramsList(){
		BaseParamBean bBean = new BaseParamBean();
		bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserUID());
		bBean.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
		bBean.setVer(1);
		bBean.setPartner_id(TivicGlobal.getInstance().userRegisterBean.getUserUID());
		
		HashMap<String, String> map = new HashMap<String, String>();
		String jsonToSting = JsonForRequest.getPeopleFocusProgramsList(bBean);
		map.put("json", jsonToSting);
		String json = httpClientUtils.requestPostHttpClient(URLConfig.get_MyTv_list, charset, map);
Log.e("pushTv","==========allFocus====json=="+json);
		BaseResponseBean brBean = NotifyMessageParser.getBaseResponse(json);
		return MyTvParser.getFocusList(json);
	}
	/**
	 * 更新 pushTv列表中节目是否已收藏的状态
	 */
	private void updatePushTVBeanIsFocusState(PushTVBean ptvBean,ArrayList<EPGProgramsBean> allFoucsProgramBean){
		if(ptvBean==null || allFoucsProgramBean==null || ptvBean.getChart()==null) return;
		for(int i=0;i<ptvBean.getChart().size();i++){
			ArrayList<ProgramInfoBean> programList = ptvBean.getChart().get(i).getProgramList();
			for(ProgramInfoBean pb : programList){
				for(EPGProgramsBean focusedProgram : allFoucsProgramBean){
					if(/*pb.getChannelid()==focusedProgram.getChannel_id() && */pb.getProgramid()==focusedProgram.getProgram_id()){
						pb.setIsFocus(1);
						break;
					}
				}
			}
		}
	}
	//获取PushTV
	public void getPushTv(final String url) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String json = httpClientUtils.requestGet(url, charset);//测试requestGet 方法 异常
Log.e("pushTv","==============json=="+json);
				PushTVBean ptvBean = ContentDetailParser.getContentPushTV(json);
				
				if (TivicGlobal.getInstance().mIsLogin) {//登录时  才判断节目是否已收藏
					ArrayList<EPGProgramsBean> allFoucsProgramBean =  getFocusProgramsList();
					updatePushTVBeanIsFocusState(ptvBean,allFoucsProgramBean);
				}
				
				Message msg = contentUpdateHander.obtainMessage();
				msg.arg1 = MSG_PUSHTV;
				msg.obj = ptvBean;
				contentUpdateHander.sendMessage(msg);
			}
		}).start();
		
	}
	/**
	 * 1，关注节目
	mytv/pg_fav
	 * @param json
	 */
	public void focusProgram(final ProgramInfoBean bean){
		new Thread(new Runnable() {
			@Override
			public void run() {
				BaseParamBean bBean = new BaseParamBean();
				bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserUID());
				bBean.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
				bBean.setVer(1);
				bBean.setPartner_id(TivicGlobal.getInstance().userRegisterBean.getUserUID());
				
				HashMap<String, String> map = new HashMap<String, String>();
				String jsonToSting = JsonForRequest.add_removeFocusPrograms(bBean,String.valueOf(bean.getProgramid()),String.valueOf(bean.getChannelid()));
				map.put("json", jsonToSting);
				String json = httpClientUtils.requestPostHttpClient(URLConfig.get_add_focus, charset, map);
				BaseResponseBean brBean = NotifyMessageParser.getBaseResponse(json);
				
				sendMsgToHandler(brBean,bean,1);
			}
		}).start();
	}
	/**
	 * 取消关注
	mytv/pg_unfav
	 * @param json
	 */
	public void removeFocus(final ProgramInfoBean bean){
		new Thread(new Runnable() {
			@Override
			public void run() {
				BaseParamBean bBean = new BaseParamBean();
				bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserUID());
				bBean.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
				bBean.setVer(1);
				bBean.setPartner_id(TivicGlobal.getInstance().userRegisterBean.getUserUID());
				
				HashMap<String, String> map = new HashMap<String, String>();
				String jsonToSting = JsonForRequest.add_removeFocusPrograms(bBean,String.valueOf(bean.getProgramid()),String.valueOf(bean.getChannelid()));
				map.put("json", jsonToSting);
				String json = httpClientUtils.requestPostHttpClient(URLConfig.get_remove_focus, charset, map);
				BaseResponseBean brBean = NotifyMessageParser.getBaseResponse(json);
				
				sendMsgToHandler(brBean,bean,0);
			}
		}).start();
	}
	
	private void sendMsgToHandler(BaseResponseBean brBean,ProgramInfoBean bean,int isFocusState){
		if(isFoucsHandler!=null){
			Message msg = new Message();
			if(brBean!=null && brBean.getRet()==0){//收藏或取消收藏成功
				bean.setIsFocus(isFocusState);
				msg.arg1 = DO_SUCCESS;
			}else{
				msg.arg1 = DO_FAIL;
			}
			isFoucsHandler.sendMessage(msg);
		}
	}
}