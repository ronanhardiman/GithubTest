package com.igrs.tivic.phone.Parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.igrs.tivic.phone.Bean.EPG.EPGProgramsBean;
import com.igrs.tivic.phone.Utils.Utils;

public class SearchParser {
	
	/**
	 * 节目搜索接口
	search/program
	 * @param json
	 * @return
	 */
	public static ArrayList<EPGProgramsBean> getSearchPrograms(String json){
		if(!Utils.isJsonValid(json))
			return null;
		ArrayList<EPGProgramsBean> beanList = new ArrayList<EPGProgramsBean>();
			try {
				JSONObject jsb = new JSONObject(json);
				if(!jsb.isNull("ret")){
					if(0 == jsb.getInt("ret")){
						if(!jsb.isNull("resp")){
							JSONObject job = jsb.getJSONObject("resp");
							if(!job.isNull("items")){
								JSONArray jar = job.getJSONArray("items");
								int length = jar.length();
								EPGProgramsBean eBean = new EPGProgramsBean();
								for (int i = 0; i < length; i++) {
									JSONObject joj = jar.getJSONObject(i);
									if(!joj.isNull("channel_id")){
										eBean.setChannel_id(joj.getInt("channel_id"));
									}
									if(!joj.isNull("program_id")){
										eBean.setProgram_id(joj.getInt("program_id"));
									}
									if(!joj.isNull("start_display")){
										eBean.setStart_display(Utils.formatToWeek(joj.getString("start_display")));
									}
									if(!joj.isNull("end_display")){
										eBean.setEnd_display(joj.getString("end_display"));
									}
									if(!joj.isNull("name")){
										eBean.setProgram_name(joj.getString("name"));
									}
									if(!joj.isNull("url_p43")){
										eBean.setUrl_p43(joj.getString("url_p43"));
									}
									if(!joj.isNull("url_p83")){
										eBean.setUrl_p83(joj.getString("url_p83"));
									}
									if(!joj.isNull("url_epg")){
										eBean.setUrl_epg(joj.getString("url_epg"));
									}
									if(!joj.isNull("hotspot")){
										eBean.setHotspot(joj.getString("hotspot"));
									}
									if(!joj.isNull("play_period")){
										eBean.setPlay_period(joj.getString("play_period"));
									}
									if(!joj.isNull("count")){
										eBean.setCount(joj.getInt("count"));
									}
									eBean.setFromEditer(false);
									beanList.add(eBean);
								}
							}
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		return beanList;
	}
	/**
	 * 节目热度表_用户访问最多的节目
	search/pghot_user_visit
	 * @param json
	 */
	
/*	resp:
	{
		items:[
			{
				"program_id": 123,
				"name": "节目名称",
				"count": 10  //用户访问节目的访问量
			}
			.....		
	}*/
	
	public static ArrayList<EPGProgramsBean> getHotProgramsList(String json){
		if(!Utils.isJsonValid(json))
			return null;
		
		ArrayList<EPGProgramsBean> beanList = new ArrayList<EPGProgramsBean>();
			try {
				JSONObject jsb = new JSONObject(json);
				if(!jsb.isNull("ret")){
					if(0 == jsb.getInt("ret")){
						if(!jsb.isNull("resp")){
							JSONObject job = jsb.getJSONObject("resp");
							if(!job.isNull("items")){
								JSONArray jar = job.getJSONArray("items");
								int length = jar.length();
								for (int i = 0; i < length; i++) {
									EPGProgramsBean eBean = new EPGProgramsBean();
									JSONObject joj = jar.getJSONObject(i);
									eBean.setProgram_id(joj.getInt("program_id"));
									eBean.setProgram_name(joj.getString("name"));
									eBean.setCount(joj.getInt("count"));
									eBean.setFromEditer(true);
									beanList.add(eBean);
								}
							}
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return beanList;
	}
}
