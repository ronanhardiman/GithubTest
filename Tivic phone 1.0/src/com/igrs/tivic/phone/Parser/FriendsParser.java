package com.igrs.tivic.phone.Parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.igrs.tivic.phone.Bean.LocationBean;
import com.igrs.tivic.phone.Bean.VisitBean;
import com.igrs.tivic.phone.Bean.Base.UserBean;
import com.igrs.tivic.phone.Bean.Social.SocialFriendBean;
import com.igrs.tivic.phone.Bean.Social.SocialFriendBeanList;
import com.igrs.tivic.phone.Utils.ModelUtil;
import com.igrs.tivic.phone.Utils.Utils;

public class FriendsParser {
	/**
	 * 1,获取指定用户的粉丝列表
	 * @param json
	 * @return
	 */
	
/*	1,获取指定用户的粉丝列表(接口升级VERSION 2)  and 2,获取指定用户的好友列表。 and 3，获取登录用户的陌生人列表。
 * 
	/friend/list_fans

	描述：返回指定用户的粉丝列表。粉丝指关注了【指定用户】的用户。粉丝可能众多，需要分页

	参数：
	ver			固定2，第二版
	user_id		指定用户id，必填。
	offset		记录偏移量，必填。取值范围：>=0
	cpp			每页条数，count per page，必填。
				取值范围：[1, 50]。
	{
		"uid": 12345,
		"sid": "12345klsfksfj",
		"ver": 2,	
		"req": {
		  "user_id": 32,
		  "offset": 0,
		  "cpp": 2
		}
	}

	//成功返回的数据
	{
	  "ret": 0,
	  "errcode": 0,
	  "msg": "ok",
	  "resp": {
	    "total": 5,					//粉丝总数
	    "items": [
	      {
	        "user_id": 130,
	        "nick": "bestslFoxmail",
	        "gender": 0,
	        "sign": "Ytutitofjxjklb",
	        "birthday": 662400000,
	        "last_time": 1356764788,//最后一次在线时间
	        "relation": 2,			//0-陌生人，1-拉黑，2-粉丝，3-我关注对方，4-双向关注
	        "loc": {
	          "lat": "",
	          "lon": ""
	        },
	        "visit": {
	          "program_id": 123,	//最后一次访问的节目
	          "title": "动物世界"	//节目名称
	        }
	      },
	      {
	        "user_id": 131,
	        "nick": "bestslqq",
	        "gender": 1,
	        "sign": "Quqiqoqoqoww",
	        "birthday": 536169600,
	        "last_time": 1356762718,
	        "relation": 2,
	        "loc": {
	          "lat": "",
	          "lon": ""
	        },
	        "visit": {
	          "program_id": 0,
	          "title": ""
	        }
	      }
	    ]
	  }
	}*/
	
	public static SocialFriendBeanList getSocialFanList(String json){
		if(!Utils.isJsonValid(json)) return null;
		SocialFriendBeanList sFBeanList = new SocialFriendBeanList();
		try {
			JSONObject job = new JSONObject(json);
			if(!job.isNull("ret")){
				if(0 == job.getInt("ret")){
					if(!job.isNull("resp")){
						JSONObject jbj = job.getJSONObject("resp");
						sFBeanList.setTotal(jbj.getInt("total"));
						if(!jbj.isNull("items")){
							JSONArray jar = jbj.getJSONArray("items");
							int length = jar.length();
							ArrayList<SocialFriendBean> sBeanList = new ArrayList<SocialFriendBean>();
							for (int i = 0; i < length; i++) {
								JSONObject joj = jar.getJSONObject(i);
								SocialFriendBean sBean = new SocialFriendBean();
								UserBean uBean = new UserBean();
								uBean.setUid(joj.getInt("user_id"));
								uBean.setUserNickName(joj.getString("nick"));
								uBean.setUserGender(joj.getInt("gender"));
								uBean.setUserSign(joj.getString("sign"));
								uBean.setUserBirthday(joj.getString("birthday"));
								uBean.setLast_time(Utils.string2unixTimestamp(joj.getLong("last_time")));
								if(!joj.isNull("relation")){
									uBean.setRelation(joj.getInt("relation"));
								}
								if(!joj.isNull("loc")){
									JSONObject jsj = joj.getJSONObject("loc");
									LocationBean lBean = new LocationBean();
									if(!jsj.isNull("lat") && !ModelUtil.isEmpty(jsj.getString("lat"))){
										lBean.setLat(Float.parseFloat(jsj.getString("lat")));
									}
									if(!jsj.isNull("lon") && !ModelUtil.isEmpty(jsj.getString("lon"))){
										lBean.setLon(Float.parseFloat(jsj.getString("lon")));
									}
									uBean.setUsrLocation(lBean);
								}
								if(!joj.isNull("visit")){
									JSONObject jno = joj.getJSONObject("visit");
									VisitBean vBean = new VisitBean();
									vBean.setVisit_pid(String.valueOf(jno.getInt("program_id")));
									vBean.setVisit_ProgramTitle(jno.getString("title"));
									uBean.setVisit(vBean);
								}
								sBean.setUserBean(uBean);
								sBeanList.add(sBean);
							}
							sFBeanList.setFriendBeansList(sBeanList);
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return sFBeanList;
	}
	
	/**
	 * 2,获取指定用户的好友列表。/friend/list_follow
	 * 返回数据 同  1,获取指定用户的粉丝列表
	 * @param json
	 * @return
	 */
	public static SocialFriendBeanList getFriendList(String json){
		return getSocialFanList(json);
	}
	
	/**
	 * 关注(他人)
	 * @param json
	 * @return
	 */
	
/*	5,关注（他人）
	/friend/follow

	//请求
	"uid" : 1019,
	"req" : 
	      {
			"uid" : 1012
	      }

	//成功
	{
	    "ret": 0,
	    "errcode": 0,
	    "msg": "ok",
	    "resp": {
		"relation": 3			//0-陌生人，1-拉黑，2-粉丝，3-我关注对方，4-双向关注
	    }
	}*/
	
	public static int getFollow(String json){
		return getUnFollow(json);
	}
	/**
	 * 取消关注(他人)
	 * @param json
	 * @return
	 */
	
/*	6，取消关注（他人）
	/friend/unfollow

	//请求
	"uid" : 1019,
	"req" : 
	      {
			"uid" : 1012
	      }
	//成功
	{
	    "ret": 0,
	    "errcode": 0,
	    "msg": "ok",
	    "resp": {
		"relation": 0	//0-陌生人，1-拉黑，2-粉丝，3-我关注对方，4-双向关注
	    }
	}*/
	
	public static int getUnFollow(String json){
		if(!Utils.isJsonValid(json)) return -1;
		int follow_ret = 5;
		try {
			JSONObject job = new JSONObject(json);
			if(!job.isNull("ret")){
				if(0 == job.getInt("ret")){
					if(!job.isNull("resp")){
						JSONObject joj = job.getJSONObject("resp");
						follow_ret = joj.getInt("relation");
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return follow_ret;
	}
	
	/**
	 * 7,拉黑（他人）
	 * @param json
	 * @return
	 */
	
/*	/friend/block

	//请求
	"uid" : 1019,
	"req" : 
	      {
			"uid" : 1012
	      }
	//成功返回
	{
	    "ret": 0,
	    "errcode": 0,
	    "msg": "ok",
	    "resp": {
		"relation": 1	//0-陌生人，1-拉黑，2-粉丝，3-我关注对方，4-双向关注
	    }
	}*/
	
	public static int getBlock(String json){
		return getUnFollow(json);
	}
	
	/**
	 * 8,取消拉黑（他人）
	 * @param json
	 * @return
	 */
	
/*	8,取消拉黑（他人）
	/friend/unblock

	//请求
	"uid" : 1019,
	"req" : 
	      {
			"uid" : 1012
	      }
	//成功
	{
	    "ret": 0,
	    "errcode": 0,
	    "msg": "ok",
	    "resp": {
		"relation": 0	//0-陌生人，1-拉黑，2-粉丝，3-我关注对方，4-双向关注
	    }
	}*/
	
	public static int getUnBlock(String json){
		return getUnFollow(json);
	}
	/**
	 * 9, 查找用户
	friend/find_people
	 * @param json
	 */
	
/*	描述：查找与登录用户处于同一节目下的用户
	program_id	节目id，必填
	offset		记录偏移量，必填。取值范围：>=0
	cpp			每页条数，count per page，必填。
				取值范围：[1, 50]。
	以下为筛选条件，可选，条件之间是AND关系，求交集。
	period		出生年代，可选。十位取整 1970，1980，1990...
	gender		性别，0女，1男
	astroid		星座code，0(白羊)~11(双鱼)
	tags		用户喜好标签id， 0～9个，标签间是AND关系：既喜欢1又喜欢2...

	{
		"uid": 32,	//登录用户
		"sid": "sessionid123456789"
		"req": 
		{
			"program_id": 50,
			"offset": 0,
			"cpp": 30,
			"period": 1980,		//80后，90后...
			"gender": 1,
			"astroid": 6,
			"tags": [1,2,3,4,5,6,7,8,9]
		}
	}

	成功返回
	{
	  "ret": 0,
	  "errcode": 0,
	  "msg": "ok",
	  "resp": {
	    "total": 1,
	    "items": [
	      {
	        "user_id": 28,
	        "nick": "Gary",
	        "gender": 1,
	        "sign": "mail_newsign Rock u!",
	        "birthday": 496512000,
	        "last_time": 1357263860,
	        "loc": {
	          "lat": "38.981834817226",
	          "lon": "37.30248258679",
	          "geo": "syespn9wwwfb164qfmc2"
	        }
	      }
	    ]
	  }
	}*/
	
	public static SocialFriendBeanList findPeople(String json){
		
		return getSocialFanList(json);
	}
}
