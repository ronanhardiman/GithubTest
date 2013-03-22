package com.igrs.tivic.phone.Parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.igrs.tivic.phone.Bean.LocationBean;
import com.igrs.tivic.phone.Bean.VisitBean;
import com.igrs.tivic.phone.Bean.Base.UserBean;
import com.igrs.tivic.phone.Bean.PGC.ContentPublishBean;
import com.igrs.tivic.phone.Bean.PGC.ContentPublishListBean;
import com.igrs.tivic.phone.Bean.Social.SocialMsgBean;
import com.igrs.tivic.phone.Bean.Social.SocialNewsBean;
import com.igrs.tivic.phone.Bean.UGC.UGCDataBean;
import com.igrs.tivic.phone.Bean.UGC.UGCListBean;
import com.igrs.tivic.phone.Bean.UGC.UGCPostItemBean;
import com.igrs.tivic.phone.Bean.UGC.UGCPostListBean;
import com.igrs.tivic.phone.Bean.UGC.UGCPublishRetBean;
import com.igrs.tivic.phone.Bean.UGC.UGCPostItemBean;
import com.igrs.tivic.phone.Bean.UGC.UGCPostListBean;
import com.igrs.tivic.phone.Bean.UGC.UGCReplyBean;
import com.igrs.tivic.phone.Bean.UGC.UGCReplyListBean;
import com.igrs.tivic.phone.Utils.ModelUtil;
import com.igrs.tivic.phone.Utils.Utils;

public class UGCDataParser {
	/**
	 * 获取指定贴吧的贴子列表（新接口）   and 获取指定用户发表的帖子列表 (/tieba/list_post_user)
		/tieba/list_post
	 * @param json
	 * @return
	 */
	public static UGCListBean getUGCList(String json)
	{	
		if(!Utils.isJsonValid(json))
			return null;
		
		UGCListBean uBeanList = new UGCListBean();
		try {
			JSONObject job = new JSONObject(json);
			if(!job.isNull("ret")){
				if(0 == job.getInt("ret")){
					if(!job.isNull("resp")){
						JSONObject jno = job.getJSONObject("resp");
						uBeanList.setCount(jno.getInt("total"));
						if(!jno.isNull("items")){
							JSONArray jsa = jno.getJSONArray("items");
							int len = jsa.length();
							ArrayList<UGCDataBean> dBeanList = new ArrayList<UGCDataBean>();
							for (int i = 0; i < len; i++) {
								JSONObject jsn = jsa.getJSONObject(i);
								UGCDataBean uBean = new UGCDataBean();
								uBean.setTid(jsn.getInt("post_id"));
								uBean.setTime(Utils.string2unixTimestamp(jsn.getLong("time")));
								uBean.setTitle(jsn.getString("title"));
								uBean.setImageUrl(jsn.getString("image"));
								uBean.setContent(jsn.getString("text"));
								uBean.setUpCount(jsn.getInt("up_count"));
								uBean.setIsUp(jsn.getInt("is_up"));
								uBean.setReplyContent(jsn.getInt("reply_count"));
								if(!jsn.isNull("sign")){
									uBean.setSign(jsn.getString("sign"));
								}
								if(!jsn.isNull("is_top")){
									uBean.setIsTop(jsn.getInt("is_top"));
								}else{
									uBean.setIsTop(0);
								}
								if(!jsn.isNull("official"))
									uBean.setOfficial(jsn.getInt("official"));
								if(!jsn.isNull("official_url"))
									uBean.setOfficail_url(jsn.getString("official_url"));
								VisitBean cBean = new VisitBean();
								if(!jsn.isNull("program_name")){
									cBean.setVisit_ProgramTitle(jsn.getString("program_name"));
								}
								if(!jsn.isNull("program_id")){
									cBean.setVisit_pid(jsn.getString("program_id"));
								}
								if(!jsn.isNull("channel_id")){
									cBean.setVisit_channelid(jsn.getString("channel_id"));
								}
								uBean.setvBean(cBean);
//								if(jsn.isNull("channel_id") && jsn.isNull("program_id") && jsn.isNull("program_name")){
//									if(null != cBean){
//										cBean = null;
//									}
//								}
								UserBean userBean = new UserBean();
								userBean.setUid(jsn.getInt("uid"));
								userBean.setUserNickName(jsn.getString("nick"));
								userBean.setUserGender(jsn.getInt("gender"));
								if(!jsn.isNull("loc")){
									JSONObject jnb = jsn.getJSONObject("loc");
									
									LocationBean lBean = new LocationBean();
									if(!jnb.isNull("lat") && !ModelUtil.isEmpty(jnb.getString("lat"))){
										lBean.setLat(Float.parseFloat(jnb.getString("lat")));
									}
									if(!jnb.isNull("lon") && !ModelUtil.isEmpty(jnb.getString("lon"))){
										lBean.setLon(Float.parseFloat(jnb.getString("lon")));
									}
									userBean.setUsrLocation(lBean);
								}
								uBean.setUsrinfo(userBean);
								dBeanList.add(uBean);
							}
							uBeanList.setDataList(dBeanList);
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return uBeanList;
	}
	
	/**
	 * 2.发布贴子
		/tieba/post
	 */
	
	//成功
	/*{
	    "ret": 0,
	    "errcode": 0,
	    "msg": "ok",
	    "resp": 
		{
			"time" : "1353998492",			// 时间
			"title" : "和还会见测试",               //标题
			"text" : "她ui哈桑萨斯我为鱼肉",			// 文本
			"img" : "/userdata/img/20121127/1353998491-52.jpg",	// 图片URL
		}
	}
	//失败
	{
	    "ret": 2,
	    "errcode": 0,
	    "msg": "发表帖子失败！",
	    "resp": ""
	}*/
	
	public static UGCPublishRetBean parserPublishPost(String json){
		if(!Utils.isJsonValid(json))
			return null;
		UGCPublishRetBean uBean = new UGCPublishRetBean();
		try {
			JSONObject job = new JSONObject(json);
			if(!job.isNull("ret")){
				if(0 == job.getInt("ret")){
					if(!job.isNull("resp")){
						JSONObject jso = job.getJSONObject("resp");
						uBean.setTime(Utils.string2unixTimestamp(jso.getLong("time")));
						uBean.setTitle(jso.getString("title"));
						uBean.setContent(jso.getString("text"));
						uBean.setImgUrl(jso.getString("img"));
					}
				}else{
					return null;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return uBean;
	}
	
	/**
	 * 获取回复列表
	 * 
	 */
	
	//成功
/*	{
	    "ret": 0,
	    "errcode": 0,
	    "msg": "ok",
	    "resp": {
	        "more": 0,                              // 是否还有更多
	        "results": [
	            {
	                "uid": "53",                      // 发起人id
	                "time": "1354088060",             // 发起时间
	                "nick": "神奇",                   // 昵称
	                "gender": "1",                    // 性别，1 为男 0为女
	                "loc": {
	                    "lat": "39.982161767984",
	                    "lon": "116.30200866053"
	                },
	                "image": "",                      // 大图url
	                "text": "地方主义国会要闻"        // 评论内容
	            },
	            {
	                "uid": "7",
	                "time": "1352970218",
	                "nick": "xiaowei",
	                "gender": "",                       //这里应该是0 ，
	                "loc": {
	                    "lat": "39.981854552158",       // 经度
	                    "lon": "116.30239439506"        // 纬度
	                },
	                "image": "http://testimagev2.b0.upaiyun.com/userdata/img/20121115/1352970218-7.jpg",
	                "text": ""
	            }
	        ]
	    }
	}*/
	
/*	3.获取回复列表（新接口）
	/tieba/list_reply*/
	
	public static UGCReplyListBean getReplyList(String json){
		if(!Utils.isJsonValid(json))
			return null;
		UGCReplyListBean replyList = new UGCReplyListBean();
		try {
			JSONObject jb = new JSONObject(json);
			if(!jb.isNull("ret")){
				if(0 == jb.getInt("ret")){
					if(!jb.isNull("resp")){
						JSONObject job = jb.getJSONObject("resp");
						
						replyList.setCount(job.getInt("total"));
//						replyList.setIfMore(job.getInt("more"));
						if(!job.isNull("items")){
							JSONArray ja = job.getJSONArray("items");
							int len = ja.length();
							ArrayList<UGCReplyBean> publishList = new ArrayList<UGCReplyBean>();
							for (int i = 0; i < len; i++) {
								JSONObject jnb = ja.getJSONObject(i);
								UGCReplyBean cBean = new UGCReplyBean();
								cBean.setReply_id(jnb.getInt("reply_id"));
								cBean.setTime(Utils.string2unixTimestamp(jnb.getLong("time")));
								cBean.setImgUrl(jnb.getString("image"));
								cBean.setReplyText(jnb.getString("text"));
//								cBean.setFoot(jnb.getString("foot"));
								cBean.setChannelId(jnb.getInt("channel_id"));
								cBean.setProgramId(jnb.getInt("program_id"));
								if(!jnb.isNull("sign")){
									cBean.setSign(jnb.getString("sign"));
								}
								UserBean uBean = new UserBean();
								
								uBean.setUid(jnb.getInt("user_id"));
								uBean.setUserNickName(jnb.getString("nick"));
								uBean.setUserGender(jnb.getInt("gender"));
								
								if(!jnb.isNull("loc")){
									LocationBean lBean = new LocationBean();
									JSONObject jsb = jnb.getJSONObject("loc");
									if(!jsb.isNull("lat") && !ModelUtil.isEmpty(jsb.getString("lat"))){
										lBean.setLat(Float.parseFloat(jsb.getString("lat")));
									}
									if(!jsb.isNull("lon") && !ModelUtil.isEmpty(jsb.getString("lon"))){
										lBean.setLon(Float.parseFloat(jsb.getString("lon")));
									}
									uBean.setUsrLocation(lBean);
								}
								if(!jnb.isNull("program_id")){
									cBean.setProgram_id(jnb.getInt("program_id"));
								}
								if(!jnb.isNull("program_name")){
									cBean.setProgram_name(jnb.getString("program_name"));
								}
								if(!jnb.isNull("channel_id")){
									cBean.setChannelId(jnb.getInt("channel_id"));
								}
								cBean.setUsrinfo(uBean);
								publishList.add(cBean);
							}
							replyList.setDataList(publishList);
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return replyList;
		
	}
	
	/**
	 * 回复贴子
	 * /tieba/reply
	 */
	
/*	
 * 4.回复贴子                                 or 支持贴子  成功返回     "up_count" : "1"  // 支持后返回数量更新
	/tieba/reply
 * //成功返回
	{
	    "ret": 0,
	    "errcode": 0,
	    "msg": "ok",
	    "resp": 
		{
			"reply_count" : "1",			// 回复后，总的回帖数
		}
	}*/
	
	public static int parserReplyPost(String json){
		if(!Utils.isJsonValid(json))
			return -1;
		
		int reply_up_count = 0;  //回复后，总的回帖数
		try {
			JSONObject job = new JSONObject(json);
			if(!job.isNull("ret")){
				if(0 == job.getInt("ret")){
					if(!job.isNull("resp")){
						JSONObject jnb = job.getJSONObject("resp");
						if(!jnb.isNull("reply_count")){
							reply_up_count = jnb.getInt("reply_count");
						}else if(!jnb.isNull("up_count")){
							reply_up_count = jnb.getInt("up_count");
						}
					}
				}
			}
		} catch (Exception e) {
		}
		return reply_up_count;
	}
	
	
}
