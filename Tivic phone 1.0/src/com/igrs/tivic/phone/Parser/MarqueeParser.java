package com.igrs.tivic.phone.Parser;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.igrs.tivic.phone.Bean.MarqueeBean;
import com.igrs.tivic.phone.Bean.MarqueeListBean;
import com.igrs.tivic.phone.Utils.Utils;
/**
 * 解析 跑马灯
 * @author admin
 *
 */
public class MarqueeParser {
	
	public static MarqueeListBean getMarqueeData(String json){
		if(!Utils.isJsonValid(json)) return null;
		MarqueeListBean mLBeans = new MarqueeListBean();
		try {
			JSONObject jobj = new JSONObject(json);
			if(!jobj.isNull("ret")){
				if(0 == jobj.getInt("ret")){
					if(!jobj.isNull("resp")){
						JSONObject jnb = jobj.getJSONObject("resp");
						mLBeans.setPublishTime(jnb.getString("publishTime"));
						if(!jnb.isNull("contents")){
							JSONObject job = jnb.getJSONObject("contents");
							HashMap<Integer, MarqueeBean> marqueeList = new HashMap<Integer, MarqueeBean>();
							if(!job.isNull("marqueeList")){
								JSONArray ja = job.getJSONArray("marqueeList");
								int count = ja.length();
								
								for (int i = 0; i < count; i++) {
									JSONObject jna = ja.getJSONObject(i);
									MarqueeBean mBean = new MarqueeBean();
									mBean.setMarquee_id(jna.getInt("id"));
									mBean.setMarquee_type(jna.getString("type"));
									mBean.setMarquee_content(jna.getString("content"));
									marqueeList.put(jna.getInt("id"), mBean);
								}
								mLBeans.setMarqueeList(marqueeList);
							}
							if(!job.isNull("global")){
								JSONArray jar = job.getJSONArray("global");
								int len = jar.length();
								HashMap<Integer, MarqueeBean> globalMarqueeList = new HashMap<Integer, MarqueeBean>();
								for (int i = 0; i < len; i++) {
									Integer key = (Integer)jar.get(i);
									globalMarqueeList.put(key, marqueeList.get(key));
								}
								mLBeans.setGlobalMarqueeList(globalMarqueeList);
							}
							if(!job.isNull("channelMarqueeList")){
								JSONArray jsa = job.getJSONArray("channelMarqueeList");
								int length = jsa.length();
								HashMap<Integer, ArrayList<MarqueeBean>> channelMarqueeList = new HashMap<Integer, ArrayList<MarqueeBean>>();
								for (int i = 0; i < length; i++) {
									JSONObject jno = jsa.getJSONObject(i);
									int key = jno.getInt("id");
									if(!jno.isNull("marqueeIdList")){
										JSONArray jsy = jno.getJSONArray("marqueeIdList");
										int counts = jsy.length();
										ArrayList<MarqueeBean> MBeanLists = new ArrayList<MarqueeBean>();
										for (int j = 0; j < counts; j++) {
											int marqueeKey  = jsy.getInt(j);
											MBeanLists.add(marqueeList.get(marqueeKey));
										}
										channelMarqueeList.put(key, MBeanLists);
									}
								}
								mLBeans.setChannelMarqueeList(channelMarqueeList);
							}
						}
					}
				}else{
					
				}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mLBeans;
	}
}
