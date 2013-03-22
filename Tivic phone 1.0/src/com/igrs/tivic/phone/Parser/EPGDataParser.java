package com.igrs.tivic.phone.Parser;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.igrs.tivic.phone.Bean.EPG.ChannelsLiveUrlsBean;
import com.igrs.tivic.phone.Bean.EPG.EPGChannelListBean;
import com.igrs.tivic.phone.Bean.EPG.EPGChannelsBean;
import com.igrs.tivic.phone.Bean.EPG.EPGDailyChannelInfo;
import com.igrs.tivic.phone.Bean.EPG.EPGLiveProvidersBean;
import com.igrs.tivic.phone.Bean.EPG.EPGProgramsBean;
import com.igrs.tivic.phone.Utils.HttpClientUtils;
import com.igrs.tivic.phone.Utils.Utils;

public class EPGDataParser {
	/**
	 * 获取所有频道信息，一次拿全所有数据，无分页
	 * @param json
	 * @return
	 */
	
/*	1,获取频道列表信息接口，公共接口，无需登录
	pgc/channel_list

	描述：获取所有频道信息，一次拿全所有数据，无分页

	{
		"req":
		{
			
		}
	}

	成功
	{
		"resp":{
			"channels": [
				{
					"id": 10,
					"name":"频道名称",
					"alias":",",         //别名,逗号分隔
					"sequence": 1        //优先级排序,值越小，优先级越高
					"live_urls":[        //直播列表 
						{
						"name": "CNTV",        //供应商名称
						"liveUrl":"",          //直播地址
						}
						......
					]
				},
				....
			],
			 "live_providers": [
				{
					"name": "CNTV",        //供应商名称
					"downloadUrl":""       //下载地址,WEB直播页为空
				}
			 ]
			
		}
	}*/
	
	public static EPGChannelListBean getChannelList(String json){
		if(!Utils.isJsonValid(json)) return null;
		EPGChannelListBean listBean = new EPGChannelListBean();
		try {
			JSONObject job = new JSONObject(json);
			if(!job.isNull("ret")){
				if(0 == job.getInt("ret")){
					if(!job.isNull("resp")){
						JSONObject jsb = job.getJSONObject("resp");
						JSONArray jna = jsb.getJSONArray("channels");
						int count = jna.length();
						ArrayList<EPGChannelsBean> epgBeans = new ArrayList<EPGChannelsBean>();
						for (int i = 0; i < count; i++) {
							JSONObject jobj = jna.getJSONObject(i);
							EPGChannelsBean cBean = new EPGChannelsBean();
							cBean.setChannel_id(jobj.getString("id"));
							cBean.setChannel_name(jobj.getString("name"));
							cBean.setChannel_alias(jobj.getString("alias"));
							cBean.setChannel_sequence(jobj.getInt("sequence"));
							if(!jobj.isNull("live_urls")){
								JSONArray jsa = jobj.getJSONArray("live_urls");
								int len = jsa.length();
								ArrayList<ChannelsLiveUrlsBean> cUrlBeanList = new ArrayList<ChannelsLiveUrlsBean>();
								for (int j = 0; j < len; j++) {
									JSONObject joa = jsa.getJSONObject(j);
									ChannelsLiveUrlsBean pBean = new ChannelsLiveUrlsBean();
									if(!joa.isNull("name")){
										pBean.setName(joa.getString("name"));
									}
									if(!joa.isNull("live_url")){
										pBean.setLiveUrl(joa.getString("live_url"));
									}
									if(!joa.isNull("download_url")){
										pBean.setDownLoadUrl(joa.getString("download_url"));
									}
									cUrlBeanList.add(pBean);
								}
								cBean.setLiveUrlsList(cUrlBeanList);
							}
							epgBeans.add(cBean);
						}
						listBean.setEpgChannelsBeanList(epgBeans);
					}
				}
			}
		} catch (Exception e) {
		}
		return listBean;
	}
	
	/**
	 * 获取某一天，某一频道的EPG信息，按start_display时间升序排列
	 */
	
/*	

	参数：
	day			某一天的零点时刻，必填
	channel_id	频道id，必填

	{
		"req": {
			'day':	1234567800, //[数据格式unix1970]
			'channel_id': 10	//频道ID
		}
	}
	{
		"resp": {
			"channel_id": 10,
			"channel_name":"频道名称",
			"day":1234567890,//[数据格式unix1970]，当天的零点时刻
			"items": [
			{
				//play item1
				"program_id": 15,           
				"start_display": 111111111,	//unix时间,1970偏移量
				"end_display": 111111111,		//unix时间,1970偏移量
				"name": "节目名称",
				"url_p43": "http://XXXXXX",	//海报URL 规格4x3
				"url_p83": "http://XXXXXX",	//海报URL 规格8x3
				"url_epg": "http://xxxxx",   //EPG海报URL
				"hotspot": "热点推荐词"
			},
			{
				//play item2
				...
			},
			...
			]
		}
	}*/
	
	public static EPGDailyChannelInfo getEPGDailyInfo(String json){
		if(!Utils.isJsonValid(json)) return null;
		EPGDailyChannelInfo info = new EPGDailyChannelInfo();
		try {
			JSONObject job = new JSONObject(json);
			if(!job.isNull("ret")){
				if(0 == job.getInt("ret")){
					if(!job.isNull("resp")){
						JSONObject jso = job.getJSONObject("resp");
						info.setChannel_id(jso.getInt("channel_id"));
						info.setChannel_name(jso.getString("channel_name"));
						info.setDay(jso.getString("day"));
						if(!jso.isNull("items")){
							JSONArray jsa = jso.getJSONArray("items");
							int count = jsa.length();
							ArrayList<EPGProgramsBean> epgBeanList = new ArrayList<EPGProgramsBean>();
							for (int i = 0; i < count; i++) {
								JSONObject jno = jsa.getJSONObject(i);
								EPGProgramsBean epgBean = new EPGProgramsBean();
								epgBean.setProgram_id(jno.getInt("program_id"));
								epgBean.setStart_display(jno.getString("start_display"));
								epgBean.setEnd_display(jno.getString("end_display"));
								epgBean.setProgram_name(jno.getString("name"));
								epgBean.setUrl_p43(jno.getString("url_p43"));
								epgBean.setUrl_p83(jno.getString("url_p83"));
								epgBean.setUrl_epg(jno.getString("url_epg"));
								epgBean.setHotspot(jno.getString("hotspot"));
								epgBeanList.add(epgBean);
							}
							info.setEpgProgramBeanList(epgBeanList);
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return info;
	}
}
