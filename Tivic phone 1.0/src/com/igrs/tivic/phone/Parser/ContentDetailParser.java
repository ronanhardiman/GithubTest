package com.igrs.tivic.phone.Parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.igrs.tivic.phone.Bean.LocationBean;
import com.igrs.tivic.phone.Bean.Base.UserBean;
import com.igrs.tivic.phone.Bean.PGC.ChartBean;
import com.igrs.tivic.phone.Bean.PGC.ContentPublishBean;
import com.igrs.tivic.phone.Bean.PGC.ContentPublishListBean;
import com.igrs.tivic.phone.Bean.PGC.ContentPublishReplyBean;
import com.igrs.tivic.phone.Bean.PGC.NewsBean;
import com.igrs.tivic.phone.Bean.PGC.ParagraphBean;
import com.igrs.tivic.phone.Bean.PGC.ProgramInfoBean;
import com.igrs.tivic.phone.Bean.PGC.PushTVBean;
import com.igrs.tivic.phone.Bean.PGC.TVFindBean;
import com.igrs.tivic.phone.Bean.PGC.TVFindParagraphBean;
import com.igrs.tivic.phone.Bean.PGC.VideoBean;
import com.igrs.tivic.phone.Utils.ModelUtil;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;
public class ContentDetailParser {
	
	/**
	 * 解析news
	 */
	public static NewsBean getContentNews(String json){
		if(!Utils.isJsonValid(json)) return null;
		NewsBean nBeans = new NewsBean();
		try {
			JSONObject jo = new JSONObject(json);
			if(!jo.isNull("ret")){
				if(0 == jo.getInt("ret")){
					if(!jo.isNull("resp")){
						JSONObject job = jo.getJSONObject("resp");
						nBeans.setPublishTime(job.getString("publishTime"));
						if(!job.isNull("contents")){
							JSONObject jsb = job.getJSONObject("contents");
							nBeans.setId(String.valueOf(jsb.getInt("id")));
							nBeans.setType(jsb.getString("type"));//是否不用读取..
							nBeans.setTitle(jsb.getString("title"));
							nBeans.setSource(jsb.getString("source"));
							nBeans.setDate(jsb.getString("date"));
							if(!jsb.isNull("paragraphs")){
								JSONArray ja = jsb.getJSONArray("paragraphs");
								int count = ja.length();
								ArrayList<ParagraphBean> pBeans = new ArrayList<ParagraphBean>();
								for (int i = 0; i < count; i++) {
									JSONObject jso = ja.getJSONObject(i);
									ParagraphBean pBean = new ParagraphBean();
									pBean.setType(jso.getString("type"));
									pBean.setContent(jso.getString("content"));
									pBeans.add(pBean);
								}
								nBeans.setParagraphList(pBeans);
							}
						}
					}
				}else{
					//返回失败
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nBeans;
	}
	
	/**
	 * 解析 PushTV  榜单
	 */
	public static PushTVBean getContentPushTV(String json){
		if(!Utils.isJsonValid(json)) return null;
		PushTVBean pBean = new PushTVBean();
		try {
			JSONObject jo = new JSONObject(json);
			if(!jo.isNull("ret")){
				if(0 == jo.getInt("ret")){
					if(!jo.isNull("resp")){
						JSONObject jobj = jo.getJSONObject("resp");
						
						pBean.setPublishTime(jobj.getString("publishTime"));
						if(!jobj.isNull("contents")){
							JSONArray ja = jobj.getJSONArray("contents");
							int count = ja.length();
							ArrayList<ChartBean> cBeans = new ArrayList<ChartBean>();
							
							for (int i = 0; i < count; i++) {
								JSONObject jnb = ja.getJSONObject(i);
								ChartBean cBean = new ChartBean();
								cBean.setId(jnb.getInt("id"));
								cBean.setTitle(jnb.getString("title"));
								if(!jnb.isNull("programs")){
									JSONArray jarr = jnb.getJSONArray("programs");
									int len = jarr.length();
									ArrayList<ProgramInfoBean> pInfoBeans = new ArrayList<ProgramInfoBean>();
									
									for (int j = 0; j < len; j++) {
										JSONObject jno = jarr.getJSONObject(j);
										ProgramInfoBean oBean = new ProgramInfoBean();
										oBean.setName(jno.getString("name"));
										oBean.setTime(jno.getString("time"));
										oBean.setProgramid(jno.getInt("programId"));
										oBean.setChannelid(jno.getInt("channelId"));
										oBean.setIntrduce(jno.getString("introduce"));
										oBean.setKeys(jno.getString("keys"));
										oBean.setImgUrl(jno.getString("imgUrl"));
										pInfoBeans.add(oBean);
									}
									cBean.setProgramList(pInfoBeans);
								}
								cBeans.add(cBean);
							}
							pBean.setChart(cBeans);
						}
					}
				}else{
					
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pBean;
	}
	/**
	 * 解析 video
	 */
	public static VideoBean getContentVide(String json){
		if(!Utils.isJsonValid(json)) return null;
		VideoBean vBean = new VideoBean();
		try {
			JSONObject jo = new JSONObject(json);
			if(!jo.isNull("ret")){
				if(0 == jo.getInt("ret")){
					if(!jo.isNull("resp")){
						JSONObject jsob = jo.getJSONObject("resp");
						vBean.setPublishTime(jsob.getString("publishTime"));
						if(!jsob.isNull("contents")){
							JSONObject job = jsob.getJSONObject("contents");
							vBean.setType(job.getString("type"));
							vBean.setTitle(job.getString("title"));
							vBean.setSource(job.getInt("source"));
							vBean.setDate(job.getString("date"));
							vBean.setId(String.valueOf(job.getInt("id")));
//							vBean.setAuthor(job.getString("author"));
							vBean.setPosterUrl(job.getString("poster"));
							vBean.setVideoUrl(job.getString("videoUrl"));
						}
					}
				}else{
					
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return vBean;
	}
	
	/**
	 * 解析 tvfind
	 */
	public static TVFindBean getContentTVfind(String json){
		if(!Utils.isJsonValid(json)) return null;
		TVFindBean tBean = new TVFindBean();
		try {
			JSONObject jo = new JSONObject(json);
			if(!jo.isNull("ret")){
				if(0 == jo.getInt("ret")){
					if(!jo.isNull("resp")){
						JSONObject job = jo.getJSONObject("resp");
						tBean.setPublishTime(job.getString("publishTime"));
						if(!job.isNull("contents")){
							JSONObject joj = job.getJSONObject("contents");
							tBean.setId(String.valueOf(joj.getInt("id")));
							tBean.setType(joj.getString("type"));
							tBean.setDate(joj.getString("date"));
//							tBean.setAuthor(joj.getString("author"));	//被去掉的字段
							if(!joj.isNull("paragraphs")){
								JSONArray ja = joj.getJSONArray("paragraphs");
								int count = ja.length();
								ArrayList<TVFindParagraphBean> tvBeans = new ArrayList<TVFindParagraphBean>();
								for (int i = 0; i < count; i++) {
									JSONObject jsb = ja.getJSONObject(i);
									TVFindParagraphBean tvBean = new TVFindParagraphBean();
									tvBean.setTitle(jsb.getString("title"));
									tvBean.setLable(jsb.getString("label"));
									tvBean.setIntroduce(jsb.getString("introduce"));
									tvBean.setPrice(jsb.getString("price"));
									tvBean.setLinkUrl(jsb.getString("link"));
									if(!jsb.isNull("imgs")){
										JSONArray jna = jsb.getJSONArray("imgs");
										int len = jna.length();
										ArrayList<String> list = new ArrayList<String>();
										for (int j = 0; j < len; j++) {
											list.add((String)jna.get(j));
										}
										tvBean.setImgUrl(list);
									}
									tvBeans.add(tvBean);
								}
								tBean.setParagraphList(tvBeans);
							}
						}
					}
				}else{
					
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return tBean;
	}
	/**
	 * 1.回复栏目文章
		/lanmu/reply
	 * @param json
	 * @return
	 */
/*	{
	    "ret": 0,
	    "errcode": 0,
	    "msg": "ok",
	    "resp": {
		"reply_count": "5"               //回贴的数量,包含本次发帖
	    }
	}*/
	
	
	public static int getContentReply(String json){
		if(!Utils.isJsonValid(json)) return -1;
		int reply_count = 0;
		try {
			JSONObject job = new JSONObject(json);
			if(!job.isNull("ret")){
				if(0 == job.getInt("ret")){
					if(!job.isNull("resp")){
						JSONObject jbj = job.getJSONObject("resp");
						reply_count = jbj.getInt("reply_count");
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return reply_count;
	}
	/**
	 * 2.获取评论列表（接口升级）version2
	/lanmu/list_reply
	 * @param json
	 */
	
/*	{
	    "ret": 0,
	    "errcode": 0,
	    "msg": "ok",
	    "resp": {
	        "total": 50,                         //指定贴子的总回复数
	        "items": [
	            {
					"reply_id": 50,				//评论id
	                "user_id": 53,                      // 发评论用户id
	                "time": 1354088060,             // 发表时间
	                "nick": "神奇",                   // 昵称
	                "gender": 1,                    // 性别，1 为男 0为女
	                "loc": {
	                    "lat": "39.982161767984",
	                    "lon": "116.30200866053"
	                },
	                "image": "",                      // 大图url
	                "text": "地方主义国会要闻",        // 评论内容
					"program_id" : 713,			// 回帖人最后一次访问的节目id， 0-没有访问过任何节目
					"channel_id": 10,		// 节目关联频道id, 0-没有关联频道
					"program_name": "\u6cd5\u5236\u8fdb\u884c\u65f6" // 节目名称	
					
	            },
	            {
					"reply_id": 98，
	                "uid": 7,
	                "time": 1352970218,
	                "nick": "xiaowei",
	                "gender": 0,
	                "loc": {
	                    "lat": "39.981854552158",       // 经度
	                    "lon": "116.30239439506"        // 纬度
	                },
	                "image": "http://testimagev2.b0.upaiyun.com/userdata/img/20121115/1352970218-7.jpg",
	                "text": "XXX",
					"program_id" : 713,
					"channel_id": 10,
					"program_name": "\u6cd5\u5236\u8fdb\u884c\u65f6"
	            }
				...
	        ]
	    }
	}*/
	
	public static ContentPublishListBean getContentReplyList(String json){
		if(!Utils.isJsonValid(json)) return null;
		ContentPublishListBean clBeanList = new ContentPublishListBean();
		try {
			JSONObject job = new JSONObject(json);
			if(!job.isNull("ret")){
				if(0 == job.getInt("ret")){
					if(!job.isNull("resp")){
						JSONObject jbj = job.getJSONObject("resp");
						clBeanList.setTotal(jbj.getInt("total"));
						if(!jbj.isNull("items")){
							JSONArray jar = jbj.getJSONArray("items");
							int length = jar.length();
							ArrayList<ContentPublishBean> cBeanList = new ArrayList<ContentPublishBean>();
							for (int i = 0; i < length; i++) {
								ContentPublishBean cBean = new ContentPublishBean();
								JSONObject jsb = jar.getJSONObject(i);
								cBean.setReply_id(jsb.getInt("reply_id"));
								cBean.setStartTime(Utils.string2unixTimestamp(jsb.getLong("time")));
								
								UserBean uBean = new UserBean();
								uBean.setUid(jsb.getInt("user_id"));
								uBean.setUserNickName(jsb.getString("nick"));
								uBean.setUserGender(jsb.getInt("gender"));
								if(!jsb.isNull("loc")){
									JSONObject jbe = jsb.getJSONObject("loc");
									LocationBean lBean = new LocationBean();
									if(!jbe.isNull("lat") && !ModelUtil.isEmpty(jbe.getString("lat"))){
										lBean.setLat(Float.parseFloat(jbe.getString("lat")));
									}
									if(!jbe.isNull("lon") && !ModelUtil.isEmpty(jbe.getString("lon"))){
										lBean.setLon(Float.parseFloat(jbe.getString("lon")));
									}
									uBean.setUsrLocation(lBean);
								}
								cBean.setUsrinfo(uBean);
								cBean.setPublishImage(jsb.getString("image"));
								cBean.setPublishText(jsb.getString("text"));
								if(!jsb.isNull("program_name")){
									cBean.setProgramName(jsb.getString("program_name"));
								}
								if(!jsb.isNull("program_id")){
									cBean.setFoot(jsb.getString("program_id"));
								}
								if(!jsb.isNull("channel_id")){
									cBean.setChannelid(jsb.getString("channel_id"));
//									UIUtils.Logd("lq", "channel_id : "+jsb.getString("channel_id"));
								}
								if(!jsb.isNull("sign")){
									cBean.setSign(jsb.getString("sign"));
								}
								cBeanList.add(cBean);
							}
							clBeanList.setPublishList(cBeanList);
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return clBeanList;
	}
	
	
}
