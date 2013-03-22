package com.igrs.tivic.phone.Parser;

import java.util.ArrayList;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.igrs.tivic.phone.Bean.BaseResponseBean;
import com.igrs.tivic.phone.Bean.LocationBean;
import com.igrs.tivic.phone.Bean.Base.UserBean;
import com.igrs.tivic.phone.Bean.Social.SocialMessageBean;
import com.igrs.tivic.phone.Bean.Social.SocialMessageHistoryListBean;
import com.igrs.tivic.phone.Bean.Social.SocialMessageListBean;
import com.igrs.tivic.phone.Bean.Social.SocialMsgBean;
import com.igrs.tivic.phone.Bean.Social.SocialNewsBean;
import com.igrs.tivic.phone.Bean.Social.SocialNotifyBean;
import com.igrs.tivic.phone.Bean.Social.SocialNotifyListBean;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Utils.ModelUtil;
import com.igrs.tivic.phone.Utils.Utils;

public class NotifyMessageParser {
	
	
	public static SocialMessageListBean getMessageList(String json){
		if(!Utils.isJsonValid(json)) return null;
		SocialMessageListBean sListBean = new SocialMessageListBean();
		try {
			JSONObject job = new JSONObject(json);
			if(!job.isNull("ret")){
				if(0 == job.getInt("ret")){
					if(!job.isNull("resp")){
						JSONObject jbj = job.getJSONObject("resp");
						sListBean.setIfMore(jbj.getInt("more"));
						if(!jbj.isNull("count")){	
							sListBean.setCount(jbj.getInt("count"));
						}else if(!jbj.isNull("total")){	
							sListBean.setCount(jbj.getInt("total"));
						}
						if(!jbj.isNull("items")){
							JSONArray jsa = jbj.getJSONArray("items");
							int len = jsa.length();
							ArrayList<SocialMessageBean> messageList = new ArrayList<SocialMessageBean>();
							for (int i = 0; i < len; i++) {
								JSONObject jso = jsa.getJSONObject(i);
								SocialMessageBean sBean = new SocialMessageBean();
								if(!jso.isNull("partner_id")){
									sBean.setUid(jso.getInt("partner_id")); 
									sBean.setPartner_id(jso.getInt("partner_id"));
								}else if(!jso.isNull("msg_id")){
									sBean.setMid(jso.getInt("msg_id"));
								}
								if(!jso.isNull("from")){
									sBean.setFrom(jso.getInt("from"));
									if(jso.getInt("from") == TivicGlobal.getInstance().userRegisterBean.getUserId())
										sBean.setComeMsg(false);
									else
										sBean.setComeMsg(true);
								}
								
								if(!jso.isNull("to")){
									sBean.setTo(jso.getInt("to"));
								}
								if(!jso.isNull("nick")){
									UserBean uBean = new UserBean();
									uBean.setUserNickName(jso.getString("nick"));
									uBean.setUid(jso.getInt("partner_id"));
									if(!jso.isNull("gender")){
										uBean.setUserGender(jso.getInt("gender"));
									}
									if(!jso.isNull("lat")){
										LocationBean lBean = new LocationBean();
										if(!jso.isNull("lat") && !ModelUtil.isEmpty(jso.getString("lat"))){
											lBean.setLat(Float.parseFloat(jso.getString("lat")));
										}
										if(!jso.isNull("lon") && !ModelUtil.isEmpty(jso.getString("lon"))){
											lBean.setLon(Float.parseFloat(jso.getString("lon")));
										}
										uBean.setUsrLocation(lBean);
									}
									sBean.setUsrinfo(uBean);
								}
								if(!jso.isNull("addtime"))
									sBean.setTime(jso.getString("addtime"));
								else if(!jso.isNull("time"))
									sBean.setTime(jso.getString("time"));
								sBean.setText(jso.getString("text"));
								if(!jso.isNull("image"))
									sBean.setImgUrl(jso.getString("image"));
								messageList.add(sBean);
							}
							sListBean.setMessageList(messageList);
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return sListBean;
	}

	public static SocialMessageHistoryListBean getMessageHistoryList(String json){
		if(!Utils.isJsonValid(json)) return null;
		SocialMessageHistoryListBean sListBean = new SocialMessageHistoryListBean();
		try {
			JSONObject job = new JSONObject(json);
			if(!job.isNull("ret")){
				if(0 == job.getInt("ret")){
					if(!job.isNull("resp")){
						JSONObject jbj = job.getJSONObject("resp");
						sListBean.setIfMore(jbj.getInt("more"));
						if(!jbj.isNull("count")){	
							sListBean.setCount(jbj.getInt("count"));
						}else if(!jbj.isNull("total")){
							sListBean.setCount(jbj.getInt("total"));
						}
						if(!jbj.isNull("items")){
							JSONArray jsa = jbj.getJSONArray("items");
							int len = jsa.length();
							LinkedList<SocialMessageBean> messageList = new LinkedList<SocialMessageBean>();
							for (int i = 0; i < len; i++) {
								JSONObject jso = jsa.getJSONObject(i);
								SocialMessageBean sBean = new SocialMessageBean();
								if(!jso.isNull("partner_id")){
									sBean.setUid(jso.getInt("partner_id")); 
									sBean.setPartner_id(jso.getInt("partner_id"));
								}else if(!jso.isNull("msg_id")){
									sBean.setMid(jso.getInt("msg_id"));
								}
								if(!jso.isNull("from")){
									sBean.setFrom(jso.getInt("from"));
									if(jso.getInt("from") == TivicGlobal.getInstance().userRegisterBean.getUserId())
										sBean.setComeMsg(false);
									else
										sBean.setComeMsg(true);
								}
								
								if(!jso.isNull("to")){
									sBean.setTo(jso.getInt("to"));
								}
								if(!jso.isNull("nick")){								
									UserBean uBean = new UserBean();
									uBean.setUserNickName(jso.getString("nick"));
									uBean.setUid(jso.getInt("partner_id"));
									if(!jso.isNull("gender")){
										uBean.setUserGender(jso.getInt("gender"));
									}
									if(!jso.isNull("lat")){
										LocationBean lBean = new LocationBean();
										if(!jso.isNull("lat") && !ModelUtil.isEmpty(jso.getString("lat"))){
											lBean.setLat(Float.parseFloat(jso.getString("lat")));
										}
										if(!jso.isNull("lon") && !ModelUtil.isEmpty(jso.getString("lon"))){
											lBean.setLon(Float.parseFloat(jso.getString("lon")));
										}
										uBean.setUsrLocation(lBean);
									}
									sBean.setUsrinfo(uBean);
								}
								if(!jso.isNull("addtime"))
									sBean.setTime(jso.getString("addtime"));
								else if(!jso.isNull("time"))
									sBean.setTime(jso.getString("time"));
								sBean.setText(jso.getString("text"));
								if(!jso.isNull("image"))
									sBean.setImgUrl(jso.getString("image"));
								messageList.addFirst(sBean);
							}
							sListBean.setMessageList(messageList);
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return sListBean;
	}
	
	
	public static SocialMessageBean sendMessage(String json){
		if(!Utils.isJsonValid(json)) return null;
		SocialMessageBean sBean = new SocialMessageBean();
		try {
			JSONObject job = new JSONObject(json);
			if(!job.isNull("ret")){
				if(0 == job.getInt("ret")){
					if(!job.isNull("resp")){
						JSONObject jno = job.getJSONObject("resp");
						sBean.setTime(jno.getString("time"));
						sBean.setText(jno.getString("text"));
						sBean.setImgUrl(jno.getString("image"));
						
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return sBean;
	}
	
	
	public static SocialNotifyListBean getNoticeHistoryList(String json){
		if(!Utils.isJsonValid(json)) return null;
		SocialNotifyListBean sListBean = new SocialNotifyListBean();
		try {
			JSONObject job = new JSONObject(json);
			if(!job.isNull("ret")){
				if(0 == job.getInt("ret")){
					if(!job.isNull("resp")){
						JSONObject jon = job.getJSONObject("resp");
						sListBean.setIfMore(jon.getInt("more"));
						sListBean.setCount(jon.getInt("total"));
						if(!jon.isNull("items")){
							JSONArray joa = jon.getJSONArray("items");
							ArrayList<SocialNotifyBean> notifyList = new ArrayList<SocialNotifyBean>();
							int len = joa.length();
							for (int i = 0; i < len; i++) {
								JSONObject jnb = joa.getJSONObject(i);
								SocialNotifyBean sBean = new SocialNotifyBean();
								sBean.setStatusType(jnb.getInt("type"));
								sBean.setNotice_id(jnb.getInt("notice_id"));
								sBean.setUid(jnb.getInt("uid"));
								sBean.setNickName(jnb.getString("nick"));
								sBean.setGender(jnb.getInt("gender"));
								sBean.setUpdateTime(jnb.getString("time"));
								JSONObject data = jnb.getJSONObject("data");
								sBean.setContent(data.getString("text"));

								notifyList.add(sBean);
							}
							sListBean.setNotifyList(notifyList);
						}
						
					}
				}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return sListBean;
	}
	

	public static SocialNewsBean getCountNews(String json){
		if(!Utils.isJsonValid(json)) return null;
		SocialNewsBean sBean = new SocialNewsBean();
		try {
			JSONObject job = new JSONObject(json);
			if(!job.isNull("ret")){
				if(0 == job.getInt("ret")){
					if(!job.isNull("resp")){
						JSONObject jbj = job.getJSONObject("resp");
						if(!jbj.isNull("msg_person")){
							JSONArray jar = jbj.getJSONArray("msg_person");
							int length = jar.length();
							ArrayList<SocialMsgBean> sBeanList = new ArrayList<SocialMsgBean>();
							for (int i = 0; i < length; i++) {
								JSONObject jnb = jar.getJSONObject(i);
								SocialMsgBean sMBean = new SocialMsgBean();
								sMBean.setUid(jnb.getString("user_id"));
								sMBean.setCount(jnb.getInt("count"));
								sBeanList.add(sMBean);
							}
							sBean.setMessageBeanList(sBeanList);
						}
						if(!jbj.isNull("notice_sum"))
								sBean.setAct_sum(jbj.getInt("notice_sum"));
						if(!jbj.isNull("msg_sum"))
							sBean.setMsg_sum(jbj.getInt("msg_sum"));
						
					}
				}
			}
		} catch (JSONException e) {
			Log.e("NotifyMessageParser", "json = " + json);
			e.printStackTrace();
		}
		return sBean;
	}

	public static BaseResponseBean getBaseResponse(String json){
		if(!Utils.isJsonValid(json)) return null;
		BaseResponseBean bBean = new BaseResponseBean();
		try {
			JSONObject jobject = new JSONObject(json);
			if(!jobject.isNull("ret")){
				if(0 != jobject.getInt("ret")){
					bBean.setRet(jobject.getInt("ret"));
					bBean.setErrcode(jobject.getInt("errcode"));
					bBean.setMsg(jobject.getString("msg"));
				}else{
					bBean.setRet(0);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return bBean;
	}
}
