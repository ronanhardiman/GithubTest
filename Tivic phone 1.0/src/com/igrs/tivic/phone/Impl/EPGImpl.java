package com.igrs.tivic.phone.Impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.jar.Attributes.Name;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Bean.BaseResponseBean;
import com.igrs.tivic.phone.Bean.MarqueeListBean;
import com.igrs.tivic.phone.Bean.Base.BaseParamBean;
import com.igrs.tivic.phone.Bean.EPG.EPGChannelBean;
import com.igrs.tivic.phone.Bean.EPG.EPGChannelListBean;
import com.igrs.tivic.phone.Bean.EPG.EPGDailyChannelInfo;
import com.igrs.tivic.phone.Bean.EPG.EPGProgramBean;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Global.URLConfig;
import com.igrs.tivic.phone.Interface.IEPG;
import com.igrs.tivic.phone.Listener.EPGUpdateUIListener;
import com.igrs.tivic.phone.Parser.EPGDataParser;
import com.igrs.tivic.phone.Parser.MarqueeParser;
import com.igrs.tivic.phone.Parser.MyTvParser;
import com.igrs.tivic.phone.Parser.NotifyMessageParser;
import com.igrs.tivic.phone.Utils.HttpClientUtils;
import com.igrs.tivic.phone.Utils.JsonForRequest;
import com.igrs.tivic.phone.Utils.Utils;


public class EPGImpl implements IEPG {
	
	public static final int NUM_MAX = 20;
	public static final int NUM_MIN = 5;
	public static final int NUM_MIDDLE = 10;
	
	public static final int MSG_GET_CHANNEL_LIST = 100;
	public static final int MSG_GET_PROGRAM_LIST = 101;
	
	static EPGImpl instance;
	private EPGUpdateUIListener epgUpdateUIListener;
	private final static String charset = "utf-8";
	private HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
	Handler epgUpdateUItHandler;
	
	public static EPGImpl getInstance()
	{
		if(instance == null)
			instance = new EPGImpl();
		return instance;
	}
	
	public void setEPGUpdateUIListener(EPGUpdateUIListener listner)
	{
		this.epgUpdateUIListener = listner;
	}
	
	public void setEPGUpdateUIHandler(Handler handler)
	{
		this.epgUpdateUItHandler = handler;
	}
	/**
	 * current test data channellist
	 */
	@Override
	public EPGChannelListBean getChannelList(){
		
		HashMap<String, String> map = new HashMap<String, String>();
		String jsonToSting = JsonForRequest.getChannelList();
		map.put("json", jsonToSting);
		String json = httpClientUtils.requestPostHttpClient(
				URLConfig.get_epgChannelList, charset, map);
		EPGChannelListBean epgListBean = EPGDataParser.getChannelList(json);
		return epgListBean;
	}
	
	/**
	 * current test data program
	 */
	@Override
	public EPGDailyChannelInfo getProgramList(String channel_id,String loadProgramDate) {
		// TODO Auto-generated method stub
		HashMap<String, String> map = new HashMap<String, String>();
		BaseParamBean bBean = new BaseParamBean();
		bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserUID());
		bBean.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
		bBean.setVer(1);
		
		
		GregorianCalendar _date = new  GregorianCalendar(2012, 12, 27);
		long date = Utils.unixTimestamp2String(loadProgramDate);
//		long date = _date.getTimeInMillis()/1000;
		//Log.i("EPGImpl", "kevin add: jsonrequest 8 = "+ date);
		String day = Long.toString(date);
//		String day = Long.toString(1356537600);
		String jsonToSting = JsonForRequest.getEPGList(bBean, day, channel_id);
		map.put("json", jsonToSting);
		String json = httpClientUtils.requestPostHttpClient(
				URLConfig.get_epg_list, charset, map);
		EPGDailyChannelInfo epgProgramList = EPGDataParser.getEPGDailyInfo(json);
		return epgProgramList;
		
	}

	/**
	 * 5,获取的指定用户的关注节目—频道列表
	 *	mytv/pg_list_with_channel
	 */
	@Override
	public SparseArray<SparseIntArray> getChannelProgramFocusList(){
		BaseParamBean bBean = new BaseParamBean();
		bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserUID());
		bBean.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
		bBean.setVer(1);
		bBean.setPartner_id(TivicGlobal.getInstance().userRegisterBean
				.getUserUID());

		String jsonToSting = JsonForRequest.getChannelProgramFocusList(bBean);
		String json = httpClientUtils.requestPostHttpClient4(
				URLConfig.get_MyTv_channel_program_list, charset, jsonToSting);
		BaseResponseBean brBean = NotifyMessageParser.getBaseResponse(json);
		SparseArray<SparseIntArray> sArrays = MyTvParser.getChannelProgramFocusList(json);
		return sArrays;
	}
	
	/**
	 * add attention
	 * mytv/pg_fav
	 */
	@Override
	public void focusProgram(String program_id, String channel_id) {
		BaseParamBean bBean = new BaseParamBean();
		bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserUID());
		bBean.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
		bBean.setVer(1);
		bBean.setPartner_id(TivicGlobal.getInstance().userRegisterBean.getUserUID());
		
		HashMap<String, String> map = new HashMap<String, String>();
		String jsonToSting = JsonForRequest.add_removeFocusPrograms(bBean, program_id, channel_id);
		map.put("json", jsonToSting);
		String json = httpClientUtils.requestPostHttpClient(URLConfig.get_add_focus, charset, map);
		BaseResponseBean brBean = NotifyMessageParser.getBaseResponse(json);
	}
	
	/**
	 * cancel attention
	 */
	@Override
	public void removeFocus(String program_id, String channel_id) {
		// TODO Auto-generated method stub
		BaseParamBean bBean = new BaseParamBean();
		bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserUID());
		bBean.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
		bBean.setVer(1);
		bBean.setPartner_id(TivicGlobal.getInstance().userRegisterBean.getUserUID());
		
		HashMap<String, String> map = new HashMap<String, String>();
		String jsonToSting = JsonForRequest.add_removeFocusPrograms(bBean, program_id, channel_id);
		map.put("json", jsonToSting);
		String json = httpClientUtils.requestPostHttpClient(URLConfig.get_remove_focus, charset, map);
		BaseResponseBean brBean = NotifyMessageParser.getBaseResponse(json);
	}
	
	@Override
	public MarqueeListBean getMarquee() {
		String json = httpClientUtils.requestGet(URLConfig.get_marqueeIdList, charset);
		MarqueeListBean maBean = MarqueeParser.getMarqueeData(json);
		return maBean;
	}
	
	@Override
	public List<EPGChannelBean> getEPGChannelList(String currentDate) {
		//test data 
		int[] icons = { R.drawable.beijing_big, R.drawable.big_channel_icon_25_anhui,
				R.drawable.big_channel_icon_35_dongfang, R.drawable.big_channel_icon_17_heilongjiang,
				R.drawable.big_channel_icon_51_guangdong, R.drawable.big_channel_icon_21_henan,
				R.drawable.big_channel_icon_29_hunan, R.drawable.jiangsu_big,
				R.drawable.big_channel_icon_19_liaoning, R.drawable.big_channel_icon_16_lvyou, };
		String[] names = { "北京卫视", "安徽卫视", "东方卫视", "黑龙江卫视", "广东卫视", "河南卫视",
				"湖南卫视", "江苏卫视", "辽宁卫视", "旅游卫视" };
		//test data end
		//fill data 
		List<EPGChannelBean> epgChannelBeans = new ArrayList<EPGChannelBean>();
		for(int i = 0; i<icons.length ; i++){
			EPGChannelBean epgChannelBean = new EPGChannelBean();
			epgChannelBean.setChannel_id(i+"");
			epgChannelBean.setChannel_titile(names[i]);
			epgChannelBean.setIcon_path(icons[i]);
			epgChannelBeans.add(epgChannelBean);
		}
		return epgChannelBeans;
	}
	
	public List<EPGProgramBean> getEPGProgramList(String[] params ){
		List<EPGProgramBean> programBeans = new ArrayList<EPGProgramBean>();
		String date = params[0];
		// fill programBeans data
		String[] names = { "北京卫视", "安徽卫视", "东方卫视", "凤凰卫视", "广西卫视", "河南卫视",
				"湖南卫视", "江苏卫视", "辽宁卫视", "旅游卫视" };
		int channelId = Integer.parseInt(params[1]);
		String channelName;
		channelName = names[channelId];
		int[] program_ids = {1002,1004,1002,1004,1002,1004,1002,1004,1002,1004,1002,1004,1002,1004,1002,1004,
				1002,1004,1002,1004,1002,1004,1002,1004
		};
		String[] program_names = { "program1", "program2", "program3",
				"program4", "program5", "program6", "program7", "program8",
				"program9", "program10", "program11", "program12", "program13",
				"program14", "program15", "program16", "program17",
				"program18", "program19", "program20", "program21",
				"program22", "program23","program24" };
		String[] times = { "0:00","1:00", "2:00", "3:00", "4:00", "5:00", "6:00",
				"7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00",
				"14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00",
				"21:00", "22:00", "23:00","24:00"};
		for (int i = 0; i < program_names.length; i++) {
			EPGProgramBean programBean = new EPGProgramBean();
			programBean.setProgram_id(program_ids[i]);
			programBean.setPrograme_title(channelName+program_names[i]);
			programBean.setStart_time(date+"  "+times[i]);
			programBean.setEnd_time(times[i+1]);
			programBeans.add(programBean);
		}
		// fill programBeans data end
		return programBeans;
	}
	
	public ArrayList<EPGChannelBean> getEPGChannelList1(String datetime)
	{
		ArrayList<EPGChannelBean> channelList = new ArrayList<EPGChannelBean>();
		for(int i=0; i<NUM_MAX; i++)
		{
			EPGChannelBean bean = new EPGChannelBean();
			bean.setChannel_id("001");
			bean.setChannel_titile("频道"+i);
			bean.setIcon_path(R.drawable.beijing_big);
			channelList.add(bean);
		}
		return channelList;
	}
	
	public void getEPGChannelList2(String currentDate)
	{
		new Thread(){
			   public void run(){
				   ArrayList<EPGChannelBean> channelList = new ArrayList<EPGChannelBean>();
					for(int i=0; i<NUM_MAX; i++)
					{
						EPGChannelBean bean = new EPGChannelBean();
						bean.setChannel_id("001");
						bean.setChannel_titile("频道"+i);
						bean.setIcon_path(R.drawable.beijing_big);
						channelList.add(bean);
					}
//					epgUpdateUIListener.onChannelListGet(channelList);
			   }
			}.start();
	}
	
	public void getEPGChannelList3(String currentDate)
	{
		ArrayList<EPGChannelBean> channelList = new ArrayList<EPGChannelBean>();
		for(int i=0; i<NUM_MAX; i++)
		{
			EPGChannelBean bean = new EPGChannelBean();
			bean.setChannel_id("001");
			bean.setChannel_titile("频道"+i);
			bean.setIcon_path(R.drawable.beijing_big);
			channelList.add(bean);
		}
		 Message msg = epgUpdateUItHandler.obtainMessage(); 
		 msg.arg1 = MSG_GET_CHANNEL_LIST;
		 msg.obj = channelList;
		 epgUpdateUItHandler.sendMessage(msg);
		
	}
	
	public ArrayList<EPGProgramBean> getEPGProgramList1(String channel_id)
	{
		ArrayList<EPGProgramBean> programList = new ArrayList<EPGProgramBean>();
		for(int i=0; i<NUM_MAX; i++)
		{
			EPGProgramBean bean = new EPGProgramBean();
			bean.setChannel_id("001");
			bean.setStart_time("09:30");
			bean.setChannel_id("10:50");
			if(i%2 == 0)
				bean.setIs_collection(true);
			else
				bean.setIs_collection(false);
			bean.setPrograme_detail("This is a very very interest program i had ever seen!! " + i);
			bean.setPrograme_title("The World Of The Animal");
			programList.add(bean);
		}
		return programList;
	}

	public void getEPGProgramList2(String channel_id)
	{
		new Thread(){
			   public void run(){
					ArrayList<EPGProgramBean> programList = new ArrayList<EPGProgramBean>();
					for(int i=0; i<NUM_MAX; i++)
					{
						EPGProgramBean bean = new EPGProgramBean();
						bean.setChannel_id("001");
						bean.setStart_time("09:30");
						bean.setChannel_id("10:50");
						if(i%2 == 0)
							bean.setIs_collection(true);
						else
							bean.setIs_collection(false);
						bean.setPrograme_detail("This is a very very interest program i had ever seen!! " + i);
						bean.setPrograme_title("The World Of The Animal");
						programList.add(bean);
					}
//					epgUpdateUIListener.onProgramListGet(programList,0);
			   }
			}.start();
	}
	
	public void getEPGProgramList3(String channel_id)
	{
		ArrayList<EPGProgramBean> programList = new ArrayList<EPGProgramBean>();
		for(int i=0; i<NUM_MAX; i++)
		{
			EPGProgramBean bean = new EPGProgramBean();
			bean.setChannel_id("001");
			bean.setStart_time("09:30");
			bean.setChannel_id("10:50");
			if(i%2 == 0)
				bean.setIs_collection(true);
			else
				bean.setIs_collection(false);
			bean.setPrograme_detail("This is a very very interest program i had ever seen!! " + i);
			bean.setPrograme_title("The World Of The Animal");
			programList.add(bean);
		}
		 Message msg = epgUpdateUItHandler.obtainMessage(); 
		 msg.arg1 = MSG_GET_PROGRAM_LIST;
		 msg.obj = programList;
		 epgUpdateUItHandler.sendMessage(msg);
	}

	

	
}
