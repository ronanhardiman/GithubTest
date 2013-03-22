package com.igrs.tivic.phone.Parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.igrs.tivic.phone.Bean.EPG.EPGProgramsBean;
import com.igrs.tivic.phone.Utils.ModelUtil;
import com.igrs.tivic.phone.Utils.Utils;

public class MyTvParser {
	/**
	 * 3, 获取指定用户的关注节目列表:全部节目,按加入关注的时间降序排列
	mytv/pg_list_all
	 */
/*	{
		"req": {
			'uid': 12345
		}
	}
	{
		"resp": {
			"items" : [
				{
				//play item1
				"program_id": 15,
				"hint": "每周一、三、五播出",
				"name": "节目名称",
				"url_p43": "http://XXXXXX",	//海报URL 规格4x3
				"url_p83": "http://XXXXXX",	//海报URL 规格8x3
				"url_epg": "http://xxxxx",   //EPG海报URL
				"hotspot": "热点推荐词"
				},
				...
			]
		}
	}*/
	
	public static ArrayList<EPGProgramsBean> getFocusList(String json){
		if(!Utils.isJsonValid(json)) return null;
		ArrayList<EPGProgramsBean> eBeanList = new ArrayList<EPGProgramsBean>();
		try {
			JSONObject job = new JSONObject(json);
			if(!job.isNull("ret")){
				if(0 == job.getInt("ret")){
					if(!job.isNull("resp")){
						JSONObject jbj = job.getJSONObject("resp");
						if(!jbj.isNull("items")){
							JSONArray jar = jbj.getJSONArray("items");
							int length = jar.length();
							for (int i = 0; i < length; i++) {
								EPGProgramsBean eBean = new EPGProgramsBean();
								JSONObject jno = jar.getJSONObject(i);
								eBean.setProgram_id(jno.getInt("program_id"));
//								eBean.setChannel_id(jno.getInt("channelId"));
								eBean.setHint(jno.getString("hint"));
								eBean.setProgram_name(jno.getString("program_name"));
								eBean.setUrl_p43(jno.getString("url_p43"));
								eBean.setUrl_p83(jno.getString("url_p83"));
								eBean.setUrl_epg(jno.getString("url_epg"));
								eBean.setHotspot(jno.getString("hotspot"));
								eBeanList.add(eBean);
							}
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return eBeanList;
	}
	
	/**
	 * 获取我的关注节目列表:某一天会播出的节目
	mytv/pg_list_day
	 * @param json
	 * @return
	 */
	
/*	"resp": {
		"day":1234567890,
		"items": [
		{
			//play item1
			"channel_id": 10,
			"channel_name":"频道名称",
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
	}*/
	
	public static ArrayList<EPGProgramsBean> getDayFocusList(String json){
		if(!Utils.isJsonValid(json)) return null;
		ArrayList<EPGProgramsBean> epgBeanList = new ArrayList<EPGProgramsBean>();
		int initPosition=0;
		try {
			JSONObject job = new JSONObject(json);
			if(!job.isNull("ret")){
				if(0 == job.getInt("ret")){
					if(!job.isNull("resp")){
						JSONObject jso = job.getJSONObject("resp");
						if(!jso.isNull("items")){
							JSONArray jsa = jso.getJSONArray("items");
							int count = jsa.length();
							for (int i = 0; i < count; i++) {
								JSONObject jno = jsa.getJSONObject(i);
								EPGProgramsBean epgBean = new EPGProgramsBean();
								epgBean.setChannel_id(jno.getInt("channel_id"));
								epgBean.setChannel_name(jno.getString("channel_name"));
								epgBean.setProgram_id(jno.getInt("program_id"));
								String date = jno.getString("start_display");
								
								epgBean.setStart_display(date);
								
//Log.e("afff","====================="+date+","+date.substring(11,13)+"=="+Integer.parseInt(date.substring(11,13).trim()));
								int hour = Integer.parseInt(date.substring(11,13).trim());
								epgBean.setHour(hour);//将时间分解出“时”
								if(hour <= Utils.getHour()){
									initPosition = i;//初始时 listView的item与当前时间对应   放入集合的第0个元素的initSelectionIndex中了
								}
								
								epgBean.setEnd_display(jno.getString("end_display"));
								epgBean.setProgram_name(jno.getString("name"));
								epgBean.setUrl_p43(jno.getString("url_p43"));
								epgBean.setUrl_p83(jno.getString("url_p83"));
								epgBean.setUrl_epg(jno.getString("url_epg"));
								epgBean.setHotspot(jno.getString("hotspot"));
								epgBeanList.add(epgBean);
							}
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		if(epgBeanList.size()>0)
			epgBeanList.get(0).setInitSelectionIndex(initPosition);
		return epgBeanList;
	}
	/*
	 * 5,获取的指定用户的关注节目—频道列表
mytv/pg_list_with_channel
	 */
	public static SparseArray<SparseIntArray> getChannelProgramFocusList(String json){
		if(!Utils.isJsonValid(json)) return null;
		SparseArray<SparseIntArray> intArrays = new SparseArray<SparseIntArray>();
		try {
			JSONObject job = new JSONObject(json);
			if(!job.isNull("ret")){
				if(0 == job.getInt("ret")){
					if(!job.isNull("resp")){
						JSONObject jbj = job.getJSONObject("resp");
						if(!jbj.isNull("items")){
							JSONArray jar = jbj.getJSONArray("items");
							int length = jar.length();
							for (int i = 0; i < length; i++) {
								SparseIntArray sArray = new SparseIntArray();
								JSONObject jbe = jar.getJSONObject(i);
								int p_id = jbe.getInt("program_id");
								int c_id = jbe.getInt("channel_id");
								sArray.put(p_id, c_id);
								int key = intArrays.indexOfKey(c_id);
								if(key < 0){//没有该channel_id 的节目
									intArrays.put(c_id, sArray);
								}else{//已经存有 该channel_id 的 节目
									SparseIntArray sparray = intArrays.valueAt(key);
									sparray.put(p_id, c_id);
									intArrays.put(c_id, sparray);
								}
							}
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return intArrays;
	}
}
