package com.igrs.tivic.phone.Parser;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.util.SparseIntArray;

import com.igrs.tivic.phone.Bean.LocationBean;
import com.igrs.tivic.phone.Bean.VisitBean;
import com.igrs.tivic.phone.Bean.Base.PhotoListBean;
import com.igrs.tivic.phone.Bean.Base.UserBean;
import com.igrs.tivic.phone.Bean.Login.UserPOIItemBean;
import com.igrs.tivic.phone.Bean.PGC.CollectBean;
import com.igrs.tivic.phone.Bean.PGC.CollectListBean;
import com.igrs.tivic.phone.Utils.ModelUtil;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;
/**
 * 查询用户相关信息
 */
public class UsersDetailsParser {
	/**
	 * 
	 * @param json
	 * @return
	 */
	
	/*user/get_info_base
	{
	    "uid": "1005",
	    "sid": "6e3926e2848760a3ea2fbb3ccee6d1ea",
	    "ver": 1,
	    "req": {
	        "uid": "1005"		//指定被查询的用户
	    }
	}
	成功
	{
	    "ret": 0,
	    "errcode": 0,
	    "msg": "ok",
	    "resp": {
	        "info": {
	            "relation": 0,		//0-陌生人，1-拉黑，2-粉丝，3-我关注对方，4-双向关注
	            "nick": "ssslll",				// 最长16字
	            "gender": "1",					// 0-女，1-男，2未知
	            "birthday": "564334976",
	            "sign": "newsign",				// 最长32字
	            "loc": {						// 最近上报的LBS位置
	                "lat": "39.9820579244",		// 纬度
	                "lon": "116.302093086"		// 经度
	            },
	            "visit": {			//最近浏览栏目位置，新用户有可能为空
	                "id": "",		//栏目ID
	                "title": ""		//栏目名称
					"channelid": ""	//频道id（可选）
	            },
	            "last_time": ""		//最后上线时间戳
	        }
	    }
	}
	uid参数不存在
	{
	    'ret': 1,
	    'errcode': 0,
	    'msg': 'uid不存在！',
	    'resp': ''
	}
	获取失败
	{
	    'ret': 2,
	    'errcode': 0,
	    'msg': '获取基本信息失败！',
	    'resp': ''
	}*/
	
	public static UserBean getUserBaseInfo(String json){
		if(!Utils.isJsonValid(json))
			return null;
		UserBean uBean = new UserBean();
		try {
			JSONObject jo = new JSONObject(json);
			if(!jo.isNull("ret")){
				if(0 == jo.getInt("ret")){// success
					if(!jo.isNull("resp")){
						JSONObject jnb = jo.getJSONObject("resp");
						if(!jnb.isNull("info")){
							JSONObject job = jnb.getJSONObject("info");
							if(!job.isNull("relation")) {
								uBean.setRelation(job.getInt("relation"));
							}
							uBean.setUserNickName(job.getString("nick"));
							if(job.getString("gender").equals("")){
								uBean.setUserGender(0);
							}else{
								uBean.setUserGender(Integer.parseInt(job.getString("gender")));
							}
							uBean.setUserBirthday(job.getString("birthday"));
							uBean.setUserSign(job.getString("sign"));
							
							if(!job.isNull("loc")){
								JSONObject jnj = job.getJSONObject("loc");
								LocationBean lBean = new LocationBean();
								if(ModelUtil.isEmpty(jnj.getString("lat"))){
									lBean.setLat(0);
								}else{
									lBean.setLat(Float.parseFloat(jnj.getString("lat")));
								}
								if(ModelUtil.isEmpty(jnj.getString("lon"))){
									lBean.setLon(0);
								}else{
									lBean.setLon(Float.parseFloat(jnj.getString("lon")));
								}
								uBean.setUsrLocation(lBean);
							}
							
							if(!job.isNull("visit")){
								JSONObject jsb = job.getJSONObject("visit");
								VisitBean vBean = new VisitBean();
								vBean.setVisit_pid(jsb.getString("id"));
								vBean.setVisit_ProgramTitle(jsb.getString("title"));
								if(!jsb.isNull("channelid")){
									vBean.setVisit_channelid(jsb.getString("channelid"));
								}
								uBean.setVisit(vBean);
							}
							uBean.setLast_time(job.getString("last_time"));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return uBean;
	}
	
	
	/*#####获取扩展信息
	user/get_info_ext
	{
	    "uid": "1005",
	    "sid": "6e3926e2848760a3ea2fbb3ccee6d1ea",
	    "ver": 1,
	    "req": {
	        "uid": "1005"		//指定被查询的用户
	    }
	}
	成功
	{
	    "ret": 0,
	    "errcode": 0,
	    "msg": "ok",
	    "resp": {
	        "items": [
	            {
	                "icon": "",
	                "text": "星座",
	                "key": "astroid",
	                "value": "7"			// 0(白羊)~11(双鱼)
	            },
	            {
	                "icon": "",
	                "text": "年代",
	                "key": "age",
	                "value": "564334976"	//birthday 1970-1-1偏移量
	            },
	            {
	                "icon": "",
	                "text": "性别",
	                "key": "gender",
	                "value": "1"			//0：woman；1：man
	            },
	            {
	                "icon": "http://image.tivic.com/userdata/title/1352989398.png",
	                "text": "明星",
	                "key": "star_id",
	                "value": "1"			//titleId
	            },
	            {
	                "icon": "http://image.tivic.com/userdata/title/1352989419.png",
	                "text": "节目",
	                "key": "program_id",
	                "value": "2"			//titleId
	            },
	            {
	                "icon": "http://image.tivic.com/userdata/title/1352989514.png",
	                "text": "内容",
	                "key": "category_id",
	                "value": "6"			//titleId
	            },
	            {
	                "icon": "http://image.tivic.com/userdata/title/1353122653.jpg",
	                "text": "内容",
	                "key": "category_id",
	                "value": "18"			//titleId
	            },
	            {
	                "icon": "http://image.tivic.com/userdata/title/1353122720.jpg",
	                "text": "内容",
	                "key": "category_id",
	                "value": "20"			//titleId
	            }
	        ]
	    }
	}
	*注：明星，节目，内容0~3个item
	uid参数不存在
	{
	    'ret': 1,
	    'errcode': 0,
	    'msg': 'uid不存在！',
	    'resp': ''
	}
	获取扩展信息失败
	{
	    'ret': 2,
	    'errcode': 0,
	    'msg': '获取扩展信息失败！',
	    'resp': ''
	}*/
	/**
	 * 返回扩展信息列表,根据key 判断类型(明星 ,节目 ,内容 ,星座 ,年代 ,性别 )
	 */
	public static ArrayList<UserPOIItemBean> getUserExtInfo(String json){
		if(!Utils.isJsonValid(json))
			return null;
		ArrayList<UserPOIItemBean> uBeanLists = new ArrayList<UserPOIItemBean>();
		try {
			JSONObject jo = new JSONObject(json);
			if(!jo.isNull("ret")){
				if(0 == jo.getInt("ret")){//获取扩展信息成功
					if(!jo.isNull("resp")){
						JSONObject jnb = jo.getJSONObject("resp");
						if(!jnb.isNull("items")){
							JSONArray ja = jnb.getJSONArray("items");
							int count = ja.length();
							for (int i = 0; i < count; i++) {
								JSONObject job = ja.getJSONObject(i);
								UserPOIItemBean uBean = new UserPOIItemBean();
								uBean.setPoi_icon(job.getString("icon"));
								uBean.setPoi_text(job.getString("text"));
								uBean.setPoi_key(job.getString("key"));
								uBean.setPoi_value(job.getInt("value"));
								uBeanLists.add(uBean);
							}
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return uBeanLists ;
	}
	
/*	#####获取用户照片列表（我或他人）
	user/list_photo
	{
	    "uid": "80",
	    "sid": "6e3926e2848760a3ea2fbb3ccee6d1ea",
	    "ver": 1,	
		"req" :
		{
			"uid" : 80,							// 指定用户uid
			"page_no" : 0,						// 页号
			"per_page" : 20						// 每页条数
		}
	}
	成功
	{
		"ret" :  0,
		"errcode" :  0,
		"msg" :  "ok",
		"resp" :
		{
			"more" : 0,							// 是否还有更多
			"results" : 
			[
				"url" : "http://xxx.com/imgs/20121128/1354072898-80.png",	// 上传后生成的URL
				"url" : "http://xxx.com/imgs/20121128/1354072898-80.png",	// 上传后生成的URL
				"url" : "http://xxx.com/imgs/20121128/1354072898-80.png",	// 上传后生成的URL
				"url" : "http://xxx.com/imgs/20121128/1354072898-80.png",	// 上传后生成的URL
				....
			]
		}
	}
	参数不完整
	{
		'ret' :  1,
		'errcode' :  0,
		'msg' :  'not enough params!',
		'resp' :  ''
	}
	获取失败
	{
		'ret' :  2,
		'errcode' :  0,
		'msg' :  'failed to get photo list!',
		'resp' :  ''
	}*/
	
	public static PhotoListBean getUserPhotoList(String json){
		if(!Utils.isJsonValid(json))
			return null;
		PhotoListBean pBean = new PhotoListBean();
		try {
			JSONObject jo = new JSONObject(json);
			if(!jo.isNull("ret")){
				if(0 == jo.getInt("ret")){//用户照片列表获取成功
					if(!jo.isNull("resp")){
						JSONObject jso = jo.getJSONObject("resp");
						pBean.setIfmore(jso.getInt("more"));
						if(!jso.isNull("results")){
							JSONArray ja = jso.getJSONArray("results");
							int count = ja.length();
							ArrayList<String> urlList = new ArrayList<String>();
							for (int i = 0; i < count; i++) {
								urlList.add(i, ja.getString(i));
							}
							pBean.setUrllist(urlList);
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return pBean;
	}
	/**
	 * 获取推荐明星列表   and 获取推荐节目列表 and 获取推荐内容列表 （喜欢剧，爱情剧，战争剧）
	 *  公用 Bean  UserPOIItemBean
	 */
	public static ArrayList<UserPOIItemBean> getRecommendAll(String json){
		if(!Utils.isJsonValid(json))
			return null;
		ArrayList<UserPOIItemBean> starsList = new ArrayList<UserPOIItemBean>();
		try {
			JSONObject jb = new JSONObject(json);
			if(!jb.isNull("ret")){
				if(0 == jb.getInt("ret")){
					if(!jb.isNull("resp")){
						JSONObject job = jb.getJSONObject("resp");
						if(!job.isNull("results")){
							JSONArray ja = job.getJSONArray("results");
							int len = ja.length();
							for (int i = 0; i < len; i++) {
								JSONObject jnb = ja.getJSONObject(i);
								UserPOIItemBean uBean = new UserPOIItemBean();
								uBean.setPoi_icon(jnb.getString("icon"));
								uBean.setPoi_text(jnb.getString("text"));
								if(!jnb.isNull("star_id")){
									uBean.setPoi_value(jnb.getInt("star_id"));//star_id
									uBean.setPoi_key("star_id");
								}
								if(!jnb.isNull("program_id")){
									uBean.setPoi_value(jnb.getInt("program_id"));
									uBean.setPoi_key("program_id");
								}
								if(!jnb.isNull("category_id")){
									uBean.setPoi_value(jnb.getInt("category_id"));
									uBean.setPoi_key("category_id");
								}
								starsList.add(uBean);
							}
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return starsList;
	}
	/**
	 * 把文章 加入收藏
	 * 
	 */
	public static CollectBean AddCollection(String json){
		if(!Utils.isJsonValid(json))
			return null;
		CollectBean cBean = new CollectBean();
		try {
			JSONObject jb = new JSONObject(json);
			if(!jb.isNull("ret")){
				if(0 == jb.getInt("ret")){
					if(!jb.isNull("resp")){
						JSONObject job = jb.getJSONObject("resp");
						if(!job.isNull("article")){
							JSONObject jbj = job.getJSONObject("article");
							cBean.setFav_time(jbj.getLong("fav_time"));
							cBean.setFav_id(jbj.getString("id"));
							cBean.setArticle_title(jbj.getString("title"));
							cBean.setArticle_summary(jbj.getString("summary"));
							cBean.setArticle_source(jbj.getString("source"));
							cBean.setArticle_mediaType(jbj.getString("mediaType"));
							cBean.setArticle_mediaUrl(jbj.getString("mediaUrl"));
							cBean.setArticle_type(jbj.getString("type"));
							cBean.setArticle_time(jbj.getString("time"));
							cBean.setArticle_link(jbj.getString("link"));
//							cBean.setArticleList(0);
							cBean.setArticle_playTime(jbj.getString("playTime"));
							cBean.setVideoImage(jbj.getString("videoImage"));
							cBean.setArticle_mask(jbj.getString("mask") == "" ? 0 : jbj.getInt("mask"));
							cBean.setArticle_top(jbj.getInt("top"));
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return cBean;
	}
	
	/**
	 * 修改用户信息
	user/modify_info
	 */
	public static int ModifyInfo(String json){
		if(!Utils.isJsonValid(json))
			return -1;
		
		int ret_code = 2;
		try {
			JSONObject job = new JSONObject(json);
			if(!job.isNull("ret")){
				if(0 == job.getInt("ret")){
					return job.getInt("ret");
				}else{
					ret_code = job.getInt("ret");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return ret_code;
	}
	/**
	 * 修改用户密码
		user/modify_pwd
	 */
	public static int ModifyPassword(String json){
		if(!Utils.isJsonValid(json))
			return -1;
		int ret_code = 2;
		try {
			JSONObject job = new JSONObject(json);
			if(!job.isNull("msg")){
				String msg = job.getString("msg");
				Log.i("lq", "msg : "+msg);
			}
			if(!job.isNull("ret")){
				if(0 == job.getInt("ret")){
					return job.getInt("ret");
				}else{
					ret_code = job.getInt("ret");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ret_code;
	}
	/**
	 * 更改用户Location
	 */
	public static int ModifyLocation(String json){
		if(!Utils.isJsonValid(json))
			return -1;
		int ret_code = 2;
		try {
			JSONObject job = new JSONObject(json);
			if(!job.isNull("ret")){
				if(0 == job.getInt("ret")){
					return job.getInt("ret");
				}else{
					ret_code = job.getInt("ret");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ret_code;
	}
	/**
	 * 获取指定用户的收藏列表(接口升级VERSION 2)
	 * @param json
	 * @return
	 */
	
/*	/article/blockList

	uid		指定用户id，必填。
	offset		记录偏移量，必填。取值范围：>=0
	cpp			每页条数，count per page，必填。
				取值范围：[1, 50]。

	{
	    "ver" : 2,
	    "req" :
		{
			"uid" : 21,
			"offset" : 0,
			"cpp" : 10,
		}
	}

	//成功返回
	{
	    "ret": 0,
	    "errcode": 0,
	    "msg": "ok",
	    "resp":
		{
	        "total": 100,              // 总收藏文章数
	        "items": 
		       [
	                    {
			    "id": 569,                // 三级页面id 本字段数字类型
			    "title": "远方的家3--余少群《我们约会吧》难逃刘亦菲 爱情主张不要白富美",    // 文本标题
			    "time": "2012-11-20",      // 文本时间
			    "summary": "余少群《我们约会吧》难逃刘亦菲 爱情主张不要白富美",      // 摘要
			    "source": "厦门热线",      // 内容来源
			    "mediaType": "text",       // 媒体类型 image／video／text 3种 非图片是视频的值为text 本字段字符串类型
			    "mediaUrl": "",            // 媒体文件url
			    "link": "",                // 广告url
			    "type": "news",            // 内容类型 news ／tvfind ／video ／ad
			    "articleList": [           // 三级页面webviewurl列表
				"http://static.tivic.com/article/2012/10/15/569_ipad_1.html"
			    ],
			    "palyTime": 0,             // 视频总时长 本字段数字类型
			    "videoImage": "",          // 视频的图片
			    "mask": 0,                 // 标示三级页面最后一页评论 是全屏幕还是半屏
			    "top": 0,                   // 标示置顶 1 为置顶
			    "format": 0                // 布局 本字段字符串类型 
	                    },
	                    ...
	               ]
	        }
	}*/
	
	public static CollectListBean getCollectionList(String json){
		if(!Utils.isJsonValid(json))
			return null;
		CollectListBean cListBean = new CollectListBean();
		try {
			JSONObject job = new JSONObject(json);
			if(!job.isNull("ret")){
				if(0 == job.getInt("ret")){
					if(!job.isNull("resp")){
						JSONObject jbj = job.getJSONObject("resp");
						cListBean.setTotal(jbj.getInt("total"));
						if(!jbj.isNull("items")){
							JSONArray jsr = jbj.getJSONArray("items");
							int length = jsr.length();
							ArrayList<CollectBean> cBeanList = new ArrayList<CollectBean>();
							for (int i = 0; i < length; i++) {
								CollectBean cBean = new CollectBean();
								JSONObject jbe = jsr.getJSONObject(i);
								cBean.setFav_id(String.valueOf(jbe.getInt("id")));
								cBean.setArticle_title(jbe.getString("title"));
								cBean.setTime(jbe.getString("time"));
								cBean.setArticle_summary(jbe.getString("summary"));
								cBean.setArticle_source(jbe.getString("source"));
								cBean.setArticle_mediaType(jbe.getString("mediaType"));
								cBean.setArticle_mediaUrl(jbe.getString("mediaUrl"));
								cBean.setArticle_link(jbe.getString("link"));
								cBean.setArticle_type(jbe.getString("type"));
								if(!jbe.isNull("articleList")){
									JSONArray jar = jbe.getJSONArray("articleList");
									ArrayList<String> urlList = new ArrayList<String>();
									for (int j = 0; j < jar.length(); j++) {
										urlList.add(jar.getString(j));
									}
									cBean.setArticleList(urlList);
								}
								cBean.setArticle_playTime(jbe.getString("playTime"));
								cBean.setVideoImage(jbe.getString("videoImage"));
								cBean.setArticle_mask(jbe.getInt("mask"));
								cBean.setArticle_top(jbe.getInt("top"));
								cBean.setFormat(jbe.getString("format"));	
								cBeanList.add(cBean);
							}
							cListBean.setCollectList(cBeanList);
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return cListBean;
	}
	/**
	 * 5.获取指定用户的所有收藏文章id
	/article/list_all_ids
	 * 仅返回文章id，不排序
	 * @return
	 */
	
/*	"resp": {
		"items":[0,1,2,3,5,6,7,9...]
	}*/
	
	public static SparseIntArray getCollectedArticlesIdList(String json){
		if(!Utils.isJsonValid(json))
			return null;
		SparseIntArray article_ids = new SparseIntArray();
		try {
			JSONObject job = new JSONObject(json);
			if(!job.isNull("ret")){
				if(0 == job.getInt("ret")){
					if(!job.isNull("resp")){
						JSONObject jso = job.getJSONObject("resp");
						if(!jso.isNull("items")){
							JSONArray jbj = jso.getJSONArray("items");
							int length = jbj.length();
							for (int i = 0; i < length; i++) {
								int id = jbj.getInt(i);
								article_ids.put(id, id);
							}
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return article_ids;
	}
	
	/**
	 * #####获取频道截图列表
snapshot.tivic.com/v3/get_snapshots.php
	 * @param json
	 * @return
	 */
	public static String[] getScreenShotsImagesList(String json){
		if(!Utils.isJsonValid(json))
			return null;
		String[] screenShotsImages = new String[6];//测试设置 大小为  6
		try {
			JSONObject job = new JSONObject(json);
			if(!job.isNull("ret")){
				if(0 == job.getInt("ret")){
					if(!job.isNull("resp")){
						JSONObject joj = job.getJSONObject("resp");
						if(!joj.isNull("images")){
							JSONArray jar = joj.getJSONArray("images");
							int length = jar.length();
							for (int i = 0; i < length; i++) {
								screenShotsImages[i] = jar.getString(i);
								UIUtils.Logd("chen","截图地址:"+jar.getString(i));
							}
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return screenShotsImages;
	}

	/**
	 * 拉黑好友
	 */
	public static int parsePullIntoBlackList(String json) {
		if(!Utils.isJsonValid(json)) return -1;
		int relation = 5;
		try {
			JSONObject jsonObject = new JSONObject(json);
			if(!jsonObject.isNull("ret")) {
				int code = jsonObject.getInt("ret");
				if(code == 0) {
					JSONObject resp = jsonObject.getJSONObject("resp");
					if(!resp.isNull("relation")) {
						relation = resp.getInt("relation");
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return relation;
	}

	/**
	 * 取消拉黑好友
	 */
	public static int parsePullOutOfBlackList(String json) {
		if(!Utils.isJsonValid(json)) return -1;
		int relation = 5;
		try {
			JSONObject jsonObject = new JSONObject(json);
			if(!jsonObject.isNull("ret")) {
				int code = jsonObject.getInt("ret");
				if(code == 0) {
					JSONObject resp = jsonObject.getJSONObject("resp");
					if(!resp.isNull("relation")) {
						relation = resp.getInt("relation");
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return relation;
	}
	
}
