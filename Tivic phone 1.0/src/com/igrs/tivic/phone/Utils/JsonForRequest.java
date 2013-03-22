package com.igrs.tivic.phone.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.text.TextUtils;
import android.util.Log;
import com.igrs.tivic.phone.Bean.LocationBean;
import com.igrs.tivic.phone.Bean.ScreenShotsBean;
import com.igrs.tivic.phone.Bean.VisitBean;
import com.igrs.tivic.phone.Bean.Base.BaseParamBean;
import com.igrs.tivic.phone.Bean.Base.MessageBean;
import com.igrs.tivic.phone.Bean.Base.UserBean;
import com.igrs.tivic.phone.Bean.Login.UserCheckinBean;
import com.igrs.tivic.phone.Bean.Login.UserPOIItemBean;
import com.igrs.tivic.phone.Bean.Login.UserRegisterBean;
import com.igrs.tivic.phone.Bean.PGC.ContentPublishReplyBean;
import com.igrs.tivic.phone.Bean.Social.SNSAccountsBean;
import com.igrs.tivic.phone.Bean.Social.SocialMessageBean;
import com.igrs.tivic.phone.Bean.Social.SocialPoiBean;
import com.igrs.tivic.phone.Bean.UGC.SearchProgramBean;
import com.igrs.tivic.phone.Bean.UGC.UGCPublishBean;
import com.igrs.tivic.phone.Global.TivicGlobal;

/**
 * SNS模块使用 封装 post 请求 json 格式.
 * 
 * @author admin
 * 
 */
public class JsonForRequest {

	/*
	 * 1、检测用户是否被占用 user/exists_user { "ver": 1, "req": { "un": "user@domain.com"
	 * 邮箱或手机 } }
	 */
	public static String userExistJson(String username) {
		JSONObject userExist = new JSONObject();
		try {
			userExist.put("ver", 1);
			userExist.put("cv", TivicGlobal.versionName);
			JSONObject req_object = new JSONObject();
			req_object.put("un", username);
			userExist.put("req", req_object);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return userExist.toString();
	}

	public static String sendNumberJson(String phonenum) {
		JSONObject send_sms = new JSONObject();
		try {
			send_sms.put("ver", 1);
			send_sms.put("type", "Android");
			send_sms.put("cv", TivicGlobal.versionName);
			JSONObject req_object = new JSONObject();
			req_object.put("un", phonenum);
			send_sms.put("req", req_object);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return send_sms.toString();
	}

	public static String verifyNumberJson(String phonenum, String verifycode) {
		JSONObject valid_code = new JSONObject();
		try {
			valid_code.put("ver", 1);
			valid_code.put("cv", TivicGlobal.versionName);
			JSONObject req_object = new JSONObject();
			req_object.put("un", phonenum);
			req_object.put("code", verifycode);
			valid_code.put("req", req_object);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return valid_code.toString();
	}

	/*
	 * 手机验证成功后，通过手机注册
	 */
	public static String registerJson(String username, String pw) {
		JSONObject register = new JSONObject();
		try {
			register.put("ver", 1);
			register.put("cv", TivicGlobal.versionName);
			register.put("type", "Android");
			if (TextUtils.isEmpty(TivicGlobal.UDID)) {
				register.put("udid", "t t" + System.currentTimeMillis());
			} else {
				register.put("udid", TivicGlobal.UDID);
			}
			JSONObject req_object = new JSONObject();
			req_object.put("un", username);
			req_object.put("pw", pw);
			register.put("req", req_object);

		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		return register.toString();
	}

	/**
	 * old login
	 * 
	 * @param usrname
	 * @param password
	 * @return
	 */
	public static String logJsonOld(String usrname, String password) {
		JSONObject login = new JSONObject();
		try {
			JSONObject req_object = new JSONObject();
			req_object.put("un", usrname);
			req_object.put("pw", password);
			// System.out.println(TivicGlobal.UDID);
			if (TextUtils.isEmpty(TivicGlobal.UDID)) {
				login.put("udid", "t t" + System.currentTimeMillis());
			} else {
				login.put("udid", TivicGlobal.UDID);
			}
			login.put("req", req_object);
			login.put("ver", 1);
			login.put("cv", TivicGlobal.versionName);
			login.put("type", "Android");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		return login.toString();
	}

	/**
	 * phone版新接口 user/login
	 * 
	 * @param bean
	 * @return
	 */

	/*
	 * user/login { udid: "设备ID" //设备唯一标识 type: "iPad" //设备类型 "ver": 1, "req": {
	 * "un": "bestsl227@sina.com", "pw": "111111" } }
	 */
	public static String loginJsonNew(BaseParamBean bBean, String udid,
			String type, String username, String pw) {
		JSONObject jobject = new JSONObject();
		try {
			jobject.put("udid", udid);
			jobject.put("type", type);
			jobject.put("ver", bBean.getVer());

			JSONObject req_object = new JSONObject();
			req_object.put("un", username);
			req_object.put("pw", pw);

			jobject.put("req", req_object);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jobject.toString();
	}

	public static String logWeiboJson(SNSAccountsBean bean) {
		JSONObject loginWeibo = new JSONObject();
		try {
			loginWeibo.put("ver", 1);
			JSONObject req_object = new JSONObject();
			req_object.put("type", 0);
			req_object.put("access_toke", bean.getAccessToken());
			req_object.put("access_key", bean.getAccessKey());
			req_object.put("uniqid", bean.getUniqid());
			loginWeibo.put("req", req_object);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return loginWeibo.toString();
	}

	/*
	 * #####获取基本信息 user/get_info_base { "uid": "1005", "sid":
	 * "6e3926e2848760a3ea2fbb3ccee6d1ea", "ver": 1, "req": { "uid": "1005"
	 * //指定被查询的用户 } }
	 */

	// or

	/*
	 * #####获取扩展信息 user/get_info_ext { "uid": "1005", "sid":
	 * "6e3926e2848760a3ea2fbb3ccee6d1ea", "ver": 1, "req": { "uid": "1005"
	 * //指定被查询的用户 } }
	 */
	/**
	 * 获取基本信息 or 获取扩展信息
	 */
	public static String getBaseInfo_ExtInfo(BaseParamBean bBean) {

		JSONObject jobject = new JSONObject();
		try {
			jobject.put("uid", bBean.getPartner_id());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject object = basejson(bBean, jobject);

		return object.toString();

	}

	/**
	 * 获取指定用户的关注节目列表:全部节目,按加入关注的时间降序排列
	 * 
	 */

	public static String getUserAttentionList(BaseParamBean bBean, String uid) {
		JSONObject jobject = new JSONObject();
		try {
			jobject.put("uid", uid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject object = basejson(bBean, jobject);
		return object.toString();
	}

	/**
	 * 获取我的关注节目列表:某一天会播出的节目
	 */

	/*
	 * mytv/pg_list_day { "req": { 'day': [数据格式unix1970]，某一天的零点时刻 } }
	 */
	public static String getPersonalAttentionList(BaseParamBean bBean,
			String day) {
		JSONObject jobject = new JSONObject();
		try {
			jobject.put("day", day);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject object = basejson(bBean, jobject);
		return object.toString();
	}

	/**
	 * 基本json 对象 { "uid": "1005", "sid": "6e3926e2848760a3ea2fbb3ccee6d1ea",
	 * "ver": 1, "req": { "uid": "1005" //指定被查询的用户 } }
	 * 
	 * @param bBean
	 * @param jobject
	 * @return
	 */
	public static JSONObject basejson(BaseParamBean bBean, JSONObject jobject) {
		JSONObject object = new JSONObject();
		try {
			object.put("uid", bBean.getUid());
			object.put("sid", bBean.getSid());
			object.put("ver", bBean.getVer());
			object.put("udid", bBean.getUdid());
			object.put("type", bBean.getType());
			object.put("cv", TivicGlobal.versionName);

			// jobject.put("bar_id", bBean.getPid()); //贴吧id，与pid一样的概念
			// jobject.put("pid", bBean.getPid());
			// jobject.put("tid", bBean.getTid());
			// jobject.put("post_id", bBean.getTid());
			// jobject.put("notice_id", bBean.getNid());
			// jobject.put("msg_id", bBean.getMid());
			object.put("req", jobject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}

	/**
	 * 修改用户信息
	 */
	public static String modifyInfo(BaseParamBean bBean, UserBean uBean) {
		JSONObject object = new JSONObject();
		JSONObject jobject = new JSONObject();

		try {
			object.put("uid", bBean.getUid());
			object.put("sid", bBean.getSid());
			object.put("ver", bBean.getVer());

			jobject.put("nick", uBean.getUserNickName());
			jobject.put("sign", uBean.getUserSign());

			object.put("req", jobject);
			object.put("cv", TivicGlobal.versionName);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return object.toString();
	}

	/**
	 * 修改用户头像 imagePath 图片绝对路径
	 */
	/*public static String modifyUserAvatar(BaseParamBean bBean) {
		JSONObject object = new JSONObject();
		try {
			object.put("uid", bBean.getUid());
			object.put("sid", bBean.getSid());
			object.put("ver", bBean.getVer());

			JSONObject req = new JSONObject();
			req.put("avatar", 1);
			JSONArray ja_category = new JSONArray();
			ja_category.put(0, 1);
			ja_category.put(1, 2);
			ja_category.put(2, 3);
			JSONArray ja_program = new JSONArray();
			ja_program.put(0, 1);
			JSONArray ja_star = new JSONArray();
			ja_star.put(0, 1);
			req.put("category", ja_category);
			req.put("program", ja_program);
			req.put("star", ja_star);

			object.put("req", req);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}*/

	/**
	 * 修改用户密码
	 */
	public static String modifyPassword(BaseParamBean bBean, String oldPw,
			String newPw) {
		JSONObject jobject = new JSONObject();
		try {
			jobject.put("old", oldPw);
			jobject.put("new", newPw);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject object = basejson(bBean, jobject);

		return object.toString();
	}

	/**
	 * 上传用户图片
	 */
	public static String UploadPhoto(BaseParamBean bBean) {
		JSONObject jobject = new JSONObject();
		try {
			jobject.put("uid", bBean.getUid());
			jobject.put("sid", bBean.getSid());
			jobject.put("ver", bBean.getVer());
			jobject.put("cv", TivicGlobal.versionName);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jobject.toString();
	}

	/**
	 * 删除用户图片 user/del_photo
	 */
	public static String deletePhotos(BaseParamBean bBean,
			ArrayList<String> urlList) {
		JSONObject object = new JSONObject();
		try {
			object.put("uid", bBean.getUid());
			object.put("sid", bBean.getSid());
			object.put("ver", bBean.getVer());
			object.put("cv", TivicGlobal.versionName);
			JSONArray urls = new JSONArray();
			for (int i = 0; i < urlList.size(); i++) {
				urls.put(i, urlList.get(i));
			}
			JSONObject jbj = new JSONObject();
			jbj.put("urls", urls);
			object.put("req", jbj);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return object.toString();
	}

	/**
	 * 获取用户照片列表（我或他人）
	 */

	public static String getPhotoList(BaseParamBean bBean, int page_no,
			int per_page) {
		JSONObject jobject = new JSONObject();
		try {
			jobject.put("uid", bBean.getPartner_id());
			jobject.put("page_no", page_no);
			jobject.put("per_page", per_page);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject object = basejson(bBean, jobject);

		return object.toString();
	}

	/**
	 * 更新用户坐标
	 */
	public static String updateLoaction(BaseParamBean bBean, LocationBean lBean) {
		JSONObject jobject = new JSONObject();
		try {
			jobject.put("lat", lBean.getLat());
			jobject.put("lon", lBean.getLon());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject object = basejson(bBean, jobject);
		return object.toString();
	}

	/**
	 * 保活ONLINE
	 */
	public static String keepOnline(BaseParamBean bBean) {
		JSONObject jobject = new JSONObject();
		JSONObject object = basejson(bBean, jobject);
		return object.toString();
	}

	/**
	 * 用户签到（默认文字:哈哈！TV客截屏，惊喜连连…）
	 */

	public static String checkIn(BaseParamBean bBean, UserCheckinBean ucBean) {
		JSONObject jobject = new JSONObject();
		try {
			JSONObject photo = new JSONObject();
			photo.put("type", ucBean.getType());
			photo.put("file_url", ucBean.getPhotoUri());

			jobject.put("photo", photo);

			jobject.put("text", ucBean.getCheckinText());
			jobject.put("sync_sina", ucBean.getSyncSina());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject object = basejson(bBean, jobject);
		return object.toString();
	}

	/**
	 * 更新最近浏览节目位置 user/visit
	 */

	public static String updataLastVisit(BaseParamBean bBean, VisitBean vBean) {
		JSONObject jobject = new JSONObject();
		try {
			jobject.put("visit", vBean.getVisit_pid());
			jobject.put("channelid", vBean.getVisit_channelid());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject object = basejson(bBean, jobject);
		return object.toString();
	}

	/**
	 * 获取推荐节目列表 or 获取推荐明星列表 or 获取推荐内容列表（喜欢剧，爱情剧，战争剧）
	 * 
	 * req json对象都为空
	 */

	/*
	 * /program/list
	 * 
	 * "req" : //不需要参数 { <内空> }
	 */

	public static String getRecommendList(BaseParamBean bBean) {
		JSONObject jobject = new JSONObject();

		try {
			jobject.put("uid", bBean.getUid());
			jobject.put("sid", bBean.getSid());
			jobject.put("ver", bBean.getVer());
			jobject.put("udid", bBean.getUdid());

			JSONObject jbj = new JSONObject();
			jobject.put("req", jbj);
			jobject.put("cv", TivicGlobal.versionName);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// JSONObject object = basejson(bBean, jobject);
		return jobject.toString();
	}

	/**
	 * 回复栏目文章 (以下 req 对象里面是否 需要 添加 sid : )
	 */
	public static String replyArticle(BaseParamBean bBean,
			ContentPublishReplyBean bean) {
		JSONObject jobject = new JSONObject();
		try {
			jobject.put("news_id", bBean.getContentId());
			jobject.put("text", bean.getReplyText());
			JSONObject photo = new JSONObject();
			photo.put("type", bean.getType());
			photo.put("file_url", bean.getPhotoUrl());
			if(bean.getType() != 2){
				jobject.put("photo", photo);
			}
			jobject.put("sync_sina", bean.getSyncSinaFlag());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject object = basejson(bBean, jobject);
		return object.toString();
	}

	/**
	 * 获取回复列表
	 */

	/*
	 * 参数： news_id 指定文章id，必填。 offset 记录偏移量，必填。取值范围：>=0 cpp 每页条数，count per
	 * page，必填。 取值范围：[1, 50]。
	 * 
	 * { "ver": 2, "req": { "news_id": 651, // 文章ID "offset": 0, "cpp": 30, } }
	 */

	public static String getReplyList(BaseParamBean bBean) {
		JSONObject jobject = new JSONObject();
		try {
			jobject.put("news_id", bBean.getContentId());
			jobject.put("offset", bBean.getOffset());
			jobject.put("cpp", bBean.getCountInPage());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject object = basejson(bBean, jobject);
		return object.toString();
	}

	/**
	 * 加入收藏 article_id ? 是否能从 bBean.getContentId() 中取到.
	 * 
	 * or 取消收藏
	 * 
	 */

	/*
	 * /article/favorite { "ver": 1, "uid": 53, "req": { "article_id": "569" //
	 * 文章ID } }
	 */

	public static String add_removeCollection(BaseParamBean bBean) {
		JSONObject object = new JSONObject();
		JSONObject jobject = new JSONObject();
		try {
			object.put("uid", bBean.getUid());
			// object.put("user_id", bBean.getUid());
			object.put("sid", bBean.getSid());
			object.put("ver", bBean.getVer());
			object.put("udid", bBean.getUdid());
			// object.put("type", bBean.getType());

			jobject.put("article_id", bBean.getContentId());

			object.put("req", jobject);
			object.put("cv", TivicGlobal.versionName);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// JSONObject object = basejson(bBean, jobject);
		return object.toString();
	}

	/**
	 * 4.是否收藏 /article/isfavorite
	 * 
	 * @param bBean
	 * @return
	 */

	/*
	 * v2 版本 { "ver": 2, "uid": 53, "req": { "article_id": [569,485,486] //
	 * 数组形式的文章ID 列表 } }
	 */

	public static String isCollection(BaseParamBean bBean, int[] article_id) {
		JSONObject jobject = new JSONObject();
		try {
			jobject.put("article_id", article_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject object = basejson(bBean, jobject);
		return object.toString();
	}

	/**
	 * 3.获取指定用户的收藏列表(接口升级VERSION 2)，/article/blockList
	 */

	public static String getUserCollectionList(BaseParamBean bBean) {
		JSONObject jobject = new JSONObject();
		try {
			jobject.put("uid", bBean.getPartner_id());// 这里的uid 是指定用户的 uid.
			jobject.put("offset", bBean.getOffset());
			jobject.put("cpp", bBean.getCountInPage());
			jobject.put("devtype", bBean.getType());//区分是  pad / phone

		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject object = basejson(bBean, jobject);
		return object.toString();
	}

	/*********** 贴吧相关 ***********/

	/*
	 * 0.获取指定贴吧的贴子列表（新接口） /tieba/list_post
	 * 
	 * 描述：返回指定贴吧的帖子列表。按post_id降序排列，新帖子的post_id永远比旧帖子的post_id大。
	 * 
	 * 参数： page_flag 分页标识，必填 0：取首页； 1：向后翻页，获取小于上次返回的最后一条post_id的帖子；
	 * 2：向前翻页，获取大于上次返回的第一条post_id的帖子。 bar_id 指定贴吧id，必填。 post_id 帖子id，可选，仅在翻页时有效。
	 * reqnum 每次获取数量，必填。 取值范围：[1, 30]。
	 * 
	 * 说明；贴吧的同义词是栏目，节目...
	 * 
	 * 获取贴吧id为1的帖子，取首页 { ”ver" : 2, "req" : { "bar_id" : 1, "page_flag" : 0,
	 * "reqnum" : 10, } }
	 * 
	 * 向后翻：假设上次返回的post_id: 109,108,...100 共10条，post_id应当设为100 { "req": {
	 * "bar_id" : 1, "page_flag" : 1, "post_id" : 100, "reqnum" : 10, } }
	 * 
	 * 向前翻：假设上次返回的post_id: 95,94,...,86 共10条，post_id应当设为95 { "req": { "bar_id" :
	 * 1, "page_flag" : 2, "post_id" : 95, "reqnum" : 10, } }
	 */

	/**
	 * 获取贴子、评论、我的帖子、TA的帖子列表
	 */
	public static String getUGCListParamJson(BaseParamBean bBean) {
		JSONObject jobject = new JSONObject();
		try {
			jobject.put("bar_id", bBean.getPid()); // 贴吧id，与节目id一样的概念
			jobject.put("post_id", bBean.getTid()); // 帖子id
			jobject.put("pn", bBean.getPageIndex());
			jobject.put("cpp", bBean.getCountInPage());
			jobject.put("offset", bBean.getOffset());
			jobject.put("user_id", bBean.getPartner_id());

		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject object = basejson(bBean, jobject);
		return object.toString();
	}

	/**
	 * 3.获取回复列表（新接口） /tieba/list_reply
	 */

	/*
	 * 3.获取回复列表（新接口） /tieba/list_reply
	 * 
	 * 描述：返回指定帖子的回复列表。按reply_id升序排列，新评论的reply_id永远比旧回复的reply_id大。
	 * 
	 * 参数： page_flag 分页标识，必填 0：取首页； 1：向后翻页，获取大于上次返回的最后一条reply_id的帖子；
	 * 2：向前翻页，获取小于上次返回的第一条reply_id的帖子。 post_id 帖子id，必填。 reply_id
	 * 回复id，可选，仅在翻页时有效。 reqnum 每次获取数量，必填。 取值范围：[1, 30]。
	 * 
	 * 获取首页 { "ver" : 2, "req" : { "post_id" : 21, "page_flag" : 0, "reqnum" :
	 * 10, } }
	 * 
	 * 向后翻：假设上次返回的reply_id: 100,101...109 共10条，reply_id应当设为109 { "req": {
	 * "post_id" : 21, "page_flag" : 1, "reply_id" : 109, "reqnum" : 10, } }
	 * 
	 * 向前翻：假设上次返回的msg_id: 86,87,...,95 共10条，msg_id应当设为86 { "req": { "post_id" :
	 * 21, "page_flag" : 2, "reply_id" : 86, "reqnum" : 10, } }
	 */

	// public static String getTieBaReplyList(BaseParamBean bBean){
	// JSONObject jobject = new JSONObject();
	// try {
	// jobject.put("post_id", bBean.getPost_id());
	// jobject.put("page_flay", bBean.getPage_flag());
	// jobject.put("reqnum", bBean.getReqnum());
	// jobject.put("reply_id", bBean.getReply_id());
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// JSONObject object = basejson(bBean, jobject);
	// return object.toString();
	// }

	/**
	 * 发布帖子与回帖或评论的参数json
	 */
	/*
	 * /tieba/post { "ver": 1, "uid" : 52, "req" : { "pid" : 1, // 所属栏目ID
	 * "photo" : // 可选 { "type" : 0, // 0-POST附件（文件名为'file'），1-图片URL "file_url"
	 * : "http://xx", // 1的情况下使用 }, "title" : "和还会见测试", // 标题 "text" :
	 * "她ui哈桑萨斯我为鱼肉", // 用户选取的文字（必填） "sync_sina" : 0 // 转发新浪微博（可选）"sync_qq" : 0,
	 * // 是否传QQ（一期不用做） } }
	 */

	public static String makePublishParamJson(BaseParamBean bBean,
			UGCPublishBean uBean) {
		JSONObject jobject = new JSONObject();
		try {
			jobject.put("pid", bBean.getPid());
			jobject.put("tid", bBean.getTid());

			JSONObject photo = new JSONObject();
			photo.put("type", uBean.getType());
			photo.put("file_url", uBean.getPhotoUrl());
			if(uBean.getType() != 2){
				jobject.put("photo", photo);
			}
			jobject.put("title", uBean.getTitle());
			jobject.put("text", uBean.getContent());
			jobject.put("sync_sina", uBean.getSyncSinaFlag());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject object = basejson(bBean, jobject);
		return object.toString();
	}

	/**
	 * 获取回复列表(tieba)
	 */

	/*
	 * /tieba/list_reply { "ver": 1, "uid" : 52, "req" : { "tid" : 21, // 贴子ID
	 * "time" : 0, // 起始时间 "paging" : 2, // 分页方式，0-拉新，1-拉旧 "count" : 20, //
	 * 一次取30条 } }
	 */

	// public static String getPostReplyList(BaseParamBean bBean){
	// JSONObject jobject = new JSONObject();
	// try {
	// jobject.put("tid", bBean.getTid());
	// jobject.put("time", bBean.getStartTime());
	// jobject.put("paging", bBean.getSortType());
	// jobject.put("count", bBean.getCountInPage());
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// JSONObject object = basejson(bBean, jobject);
	//		
	// return object.toString();
	// }

	/**
	 * 回复贴子(tieba)
	 */

	/*
	 * /tieba/reply { "ver": 1, "uid" : 52, "req" : { "tid" : 21, // 所属贴子ID
	 * "photo" : { "type" : 0, // 0-POST附件（文件名为'file'），1-图片URL "file_name" :
	 * "file", // 0的情况 or "file_url" : "http://xxtest.jpg", // 1的情况 }, "text" :
	 * "自己回复", // 用户选取的文字（可选） "sync_sina" : 0 // 转发新浪微博（可选）"sync_qq" : 0, //
	 * 是否传QQ（一期不用做） } }
	 */

	// public static String replyPost(BaseParamBean bBean,UGCPublishBean uBean){
	// JSONObject jobject = new JSONObject();
	// try {
	// jobject.put("tid", uBean.getUid());
	// JSONObject photo = new JSONObject();
	// photo.put("type", uBean.getType());
	// photo.put("file_url", uBean.getPhotoUrl());
	// jobject.put("photo", photo);
	//			
	// jobject.put("text", uBean.getContent());
	// jobject.put("sync_sina", uBean.getSyncSinaFlag());
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// JSONObject object = basejson(bBean, jobject);
	// return object.toString();
	// }

	/**
	 * 支持贴子 or delete 帖子
	 */

	/*
	 * /tieba/up { "ver": 1, "uid" : 53 "req" : { "tid" : 21, // 所属贴子ID } }
	 * 
	 * 删除帖子,用户只能删除自己的帖子。 tieba/del { "req": { "tid": 123 } }
	 */
	public static String supportOrdeleteParamJson(BaseParamBean bBean) {
		JSONObject jobject = new JSONObject();
		try {
			jobject.put("tid", bBean.getTid());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject object = basejson(bBean, jobject);
		return object.toString();
	}

	/**
	 * 新版通知：获取通知列表。按时间降序排列
	 */

	/*
	 * activity/history 参数： page_flag: 分页标识 0 获取最新通知，并使服务器更新新通知统计基准时间；1
	 * 获取小于act_id的通知,向前翻页时使用 act_id：仅在page_flag=1时有效，可选 reqnum：每次请求条数 1～30条 {
	 * "req": { "page_flag": 0, "act_time": 1353575492, "reqnum": 10, } }
	 */

	public static String getNoticeParamJson(BaseParamBean bBean) {
		JSONObject jobject = new JSONObject();
		try {
			jobject.put("page_flag", bBean.getPage_flag());
			// jobject.put("act_time", bBean.getStartTime());
			jobject.put("reqnum", bBean.getCountInPage());
			jobject.put("notice_id", bBean.getNid());
			jobject.put("op", bBean.getOpreate());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject object = basejson(bBean, jobject);
		return object.toString();
	}

	/**
	 * 新版私信:获取私信首页会话列表
	 */

	/*
	 * message/home 偏移量+每此请求条数：必填。 时间：查找指定时间之前发生的会话。可选。一般填0，表示当前时间。 { "req" : {
	 * "offset": 0, //偏移量，取首页为0，翻页时取前一页最后一条记录+1：取值范围[0~200] "reqnum" : 30,
	 * //每页数量：取值范围[1~30] "time": 0, } }
	 */

	public static String getMessageParamJson(BaseParamBean bBean) {
		JSONObject jobject = new JSONObject();
		try {
			jobject.put("offset", bBean.getOffset());
			jobject.put("reqnum", bBean.getCountInPage());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject object = basejson(bBean, jobject);
		return object.toString();
	}

	/**
	 * 新版获取消息历史记录，按时间降序排列 message/history
	 */

	/*
	 * page_flag: 分页标识 0 最新，1 获取小于msg_id的私信,向前翻页时使用 msg_id：仅在page_flag=1时有效，可选
	 * reqnum：每次请求条数 1～30条 op: 0只查询；1查询并设置所有未读消息为已读，且仅在page_flag=0时有效。 { "req":
	 * { "uid": 123, //对话伙伴 "page_flag": 0, "reqnum": 10, "op": 0, } }
	 */

	public static String getHistoryMessageParamJson(BaseParamBean bBean) {
		JSONObject jobject = new JSONObject();
		try {
			jobject.put("partner_id", bBean.getPartner_id());
			jobject.put("page_flag", bBean.getPage_flag());
			jobject.put("reqnum", bBean.getCountInPage());
			jobject.put("op", bBean.getOpreate());
			jobject.put("msg_id", bBean.getMid());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject object = basejson(bBean, jobject);
		return object.toString();
	}

	/**
	 * 发表回复私信 message/send
	 * 
	 * @param param
	 * @param bean
	 * @return
	 */

	/*
	 * /message/send { "uid": "53", "ver": 1, "req": { "uid": 9, "text":
	 * "hello world!haha,i love you!", "photo": { "type": 1, //0-上传本地图片
	 * 1-给定图片的url "file_url":
	 * "http://photocdn.sohu.com/20120619/Img346004266.jpg" //图片URL,仅在type=1时有效
	 * } } }
	 */

	public static String getSendMessageParamJson(BaseParamBean param,
			SocialMessageBean bean) {
		JSONObject jobject = new JSONObject();
		try {
			jobject.put("text", bean.getText());
			jobject.put("uid", param.getPartner_id());
			if (bean.getImgUrl() != null) {
				JSONObject jt = new JSONObject();
				jt.put("type", bean.getType());
				jt.put("file_url", bean.getImgUrl());
				jobject.put("photo", jt);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject object = basejson(param, jobject);
		return object.toString();
	}

	/**
	 * 足迹相关追加channelid
	 */

	/*
	 * pgc/epg_list { "req": { 'day': [数据格式unix1970]，当天的零点时刻 'channel_id': 10
	 * //频道ID } }
	 */

	public static String getEPGList(BaseParamBean bBean, String day,
			String channel_id) {
		JSONObject jobject = new JSONObject();
		try {
			jobject.put("day", day);
			jobject.put("channel_id", channel_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject object = basejson(bBean, jobject);
		return object.toString();
	}

	/**
	 * 关注节目 or 取消关注
	 * 
	 */
	/*
	 * mytv/pg_fav { "req": { 'program_id': 15 //节目ID 'channel_id': 10
	 * //频道ID,只收藏节目时为0 } }
	 */

	public static String add_removeFocusPrograms(BaseParamBean bBean,
			String program_id, String channel_id) {
		JSONObject jobject = new JSONObject();
		try {
			jobject.put("program_id", program_id);
			jobject.put("channel_id", channel_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject object = basejson(bBean, jobject);
		return object.toString();
	}

	/**
	 * 修改用户注册时的基本信息
	 */
	/*
	 * user/modify_info { "uid": 1005 "sid": "cfa629d6da75f8f5c01eae7a1576c3da",
	 * "ver": 1, "nick": "xxx", "req": { "birthday": 564335000, "category": [
	 * //内容 6, 18, 20 ], "gender": 1, "program": [ //节目 2 ], "star": [ //明星 1 ],
	 * "sign": "newsign" } }
	 */
	public static String getBaseInfo(UserRegisterBean urb) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", urb.getUserUID());
			jsonObject.put("sid", urb.getUserSID());
			jsonObject.put("ver", 1);
			jsonObject.put("nick", urb.getUserNickname());
			jsonObject.put("cv", TivicGlobal.versionName);

			JSONObject req = new JSONObject();
			req.put("birthday", urb.getUserBirthday());
			req.put("gender", urb.getUserGender());
			JSONArray category = new JSONArray();
			req.put("category", category);
			JSONArray program = new JSONArray();
			req.put("program", program);
			JSONArray star = new JSONArray();
			req.put("star", star);
			req.put("sign", urb.getUserSign());

			jsonObject.put("req", req);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonObject.toString();
	}

	/*
	 * 重新发送邮件
	 */
	public static String reSendMail(String username) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("cv", TivicGlobal.versionName);
			JSONObject req = new JSONObject();
			req.put("un", username);
			jsonObject.put("req", req);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}

	/*
	 * 发送找回密码邮箱
	 */
	public static String sendMailForPwd(String username) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("cv", TivicGlobal.versionName);
			JSONObject req = new JSONObject();
			req.put("un", username);
			jsonObject.put("req", req);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}

	/*
	 * 发送找回密码短信
	 */
	public static String sendMsgForPwd(String username) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("cv", TivicGlobal.versionName);
			JSONObject req = new JSONObject();
			req.put("un", username);
			jsonObject.put("req", req);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}

	/*
	 * 验证找回密码短信
	 */
	public static String verifyCodeForPwd(String vefifyNumber) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("cv", TivicGlobal.versionName);
			JSONObject req = new JSONObject();
			req.put("id", TivicGlobal.getInstance().userUID);
			req.put("code", vefifyNumber);
			jsonObject.put("req", req);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}

	public static String modifyPwdByPhone(String uid, String code, String pwd) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("cv", TivicGlobal.versionName);
			JSONObject req = new JSONObject();
			req.put("id", uid);
			req.put("code", code);
			req.put("pass", pwd);
			jsonObject.put("req", req);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}

	public static String getPoiStarsInfo(UserRegisterBean urb) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", urb.getUserUID());
			jsonObject.put("sid", urb.getUserSID());
			jsonObject.put("ver", "1");
			jsonObject.put("cv", TivicGlobal.versionName);
			JSONObject req = new JSONObject();
			jsonObject.put("req", req);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}

	public static String getPoiProgramsInfo(UserRegisterBean urb) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", urb.getUserUID());
			jsonObject.put("sid", urb.getUserSID());
			jsonObject.put("ver", "1");
			jsonObject.put("cv", TivicGlobal.versionName);
			JSONObject req = new JSONObject();
			jsonObject.put("req", req);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}

	public static String getPoiCategoriesInfo(UserRegisterBean urb) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", urb.getUserUID());
			jsonObject.put("sid", urb.getUserSID());
			jsonObject.put("cv", TivicGlobal.versionName);
			jsonObject.put("ver", "1");
			JSONObject req = new JSONObject();
			jsonObject.put("req", req);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}

	public static String modifyUserBasicInfo(UserRegisterBean urb,
			List<String> stars, List<String> programs, List<String> categories) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", urb.getUserUID());
			jsonObject.put("sid", urb.getUserSID());
			jsonObject.put("ver", 1);
			jsonObject.put("cv", TivicGlobal.versionName);
			JSONObject req = new JSONObject();
			if (urb.isModifyUserAvatar()) {
				req.put("avatar", 1);
			}
			req.put("nick", urb.getUserNickname());
			req.put("birthday", urb.getUserBirthday());
			req.put("gender", urb.getUserGender());
			JSONArray category = null;
			if (categories.size() == 0) {
				category = new JSONArray();
			} else {
				category = new JSONArray(categories);
			}
			req.put("category", category);

			JSONArray program = null;
			if (programs.size() == 0) {
				program = new JSONArray();
			} else {
				program = new JSONArray(programs);
			}
			req.put("program", program);

			JSONArray star = null;
			if (stars.size() == 0) {
				star = new JSONArray();
			} else {
				star = new JSONArray(stars);
			}
			req.put("star", star);
			req.put("sign", urb.getUserSign());

			jsonObject.put("req", req);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}
	
	
	public static String modifyUserBasicInfo2(UserRegisterBean urb) {
		List<String> stars = new ArrayList<String>();
		List<String> programs = new ArrayList<String>();
		List<String> categories = new ArrayList<String>();
		List<UserPOIItemBean> userPOIItemBeans = urb.getUserPoiItems();
		for(UserPOIItemBean item: userPOIItemBeans) {
			if(item.getPoi_type()==UserPOIItemBean.STAR_TYPE) {
				stars.add(item.getPoi_key());
			} else if(item.getPoi_type()==UserPOIItemBean.CATE_TYPE) {
				categories.add(item.getPoi_key());
			} else if(item.getPoi_type()==UserPOIItemBean.PROGRAM_TYPE) {
				programs.add(item.getPoi_key());
			}
		}
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", urb.getUserUID());
			jsonObject.put("sid", urb.getUserSID());
			jsonObject.put("ver", 1);
			jsonObject.put("cv", TivicGlobal.versionName);

			JSONObject req = new JSONObject();
			if (urb.isModifyUserAvatar()) {
				req.put("avatar", 1);
			}
			req.put("nick", urb.getUserNickname());
			req.put("birthday", urb.getUserBirthday());
			req.put("gender", urb.getUserGender());
			JSONArray category = null;
			if (categories.size() == 0) {
				category = new JSONArray();
			} else {
				category = new JSONArray(categories);
			}
			req.put("category", category);

			JSONArray program = null;
			if (programs.size() == 0) {
				program = new JSONArray();
			} else {
				program = new JSONArray(programs);
			}
			req.put("program", program);

			JSONArray star = null;
			if (stars.size() == 0) {
				star = new JSONArray();
			} else {
				star = new JSONArray(stars);
			}
			req.put("star", star);
			req.put("sign", urb.getUserSign());

			jsonObject.put("req", req);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}

	/**
	 * 聚合查询： status/count_new
	 */
	/*
	 * { "req": {} }
	 */

	public static String getCountNews(BaseParamBean bBean) {
		JSONObject jobject = new JSONObject();
		JSONObject object = basejson(bBean, jobject);
		return object.toString();
	}

	/*
	 * 微博登录 snsaccount/login { "udid": "", "ver": 1, "req": { "type": 0,
	 * //新浪微博固定0 "access_token": "2.00XXXXXXXXXXXXXXX", "access_key": "",
	 * //新浪微博未使用此字段 "uniqid": "1971479501", //新浪微博用户唯一标识 } }
	 */
	public static String loginByWeibo(String access_token, String uniqid) {
		JSONObject login = new JSONObject();
		try {
			if (TextUtils.isEmpty(TivicGlobal.UDID)) {
				login.put("udid", "t t" + System.currentTimeMillis());
			} else {
				login.put("udid", TivicGlobal.UDID);
			}
			login.put("ver", 1);
			login.put("type", "Android");
			login.put("cv", TivicGlobal.versionName);
			JSONObject req = new JSONObject();
			req.put("type", 0);
			req.put("access_token", access_token);
			// req.put("access_key", "");
			req.put("uniqid", uniqid);
			login.put("req", req);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return login.toString();
	}

	/*
	 * 绑定微博
	 */
	public static String fastenWeibo(String access_token, String uniqid) {
		UserRegisterBean urb = TivicGlobal.getInstance().userRegisterBean;
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", urb.getUserUID());
			jsonObject.put("sid", urb.getUserSID());
			jsonObject.put("ver", 1);
			jsonObject.put("cv", TivicGlobal.versionName);
			JSONObject req = new JSONObject();
			req.put("type", 0);
			req.put("access_token", access_token);
			req.put("uniqid", uniqid);
			jsonObject.put("req", req);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonObject.toString();
	}

	/*
	 * 解除绑定微博
	 */
	public static String unFastenWeibo() {
		UserRegisterBean urb = TivicGlobal.getInstance().userRegisterBean;
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", urb.getUserUID());
			jsonObject.put("sid", urb.getUserSID());
			jsonObject.put("ver", 1);
			jsonObject.put("cv", TivicGlobal.versionName);
			JSONObject req = new JSONObject();
			req.put("type", 0);
			jsonObject.put("req", req);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}

	/*
	 * 获取好友、粉丝、陌生人、黑名单 等好友列表相关，通用此接口
	 */
	public static String getFriendsList(BaseParamBean bBean) {
		JSONObject jobject = new JSONObject();
		try {
			jobject.put("user_id", bBean.getPartner_id());
			jobject.put("offset", bBean.getOffset());
			jobject.put("cpp", bBean.getCountInPage());

		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject object = basejson(bBean, jobject);
		return object.toString();
	}

	/*
	 * 
	 * 修改密码
	 */
	public static String modifyPwd(String oldPwd, String newPwd) {
		UserRegisterBean urb = TivicGlobal.getInstance().userRegisterBean;
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", urb.getUserUID());
			jsonObject.put("sid", urb.getUserSID());
			jsonObject.put("ver", 1);
			jsonObject.put("cv", TivicGlobal.versionName);
			JSONObject req = new JSONObject();
			req.put("old", oldPwd);
			req.put("new", newPwd);
			jsonObject.put("req", req);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}

	public static String getFanFollowStrangerList(BaseParamBean bBean) {
		JSONObject jobject = new JSONObject();
		try {
			jobject.put("user_id", bBean.getPartner_id());
			jobject.put("offset", bBean.getOffset());
			jobject.put("cpp", bBean.getCountInPage());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject object = basejson(bBean, jobject);
		return object.toString();
	}

	/**
	 * 
	 * @param bBean
	 * @return
	 */

	/*
	 * 4，获取登录用户的黑名单列表。(接口升级VERSION 2) /friend/list_blocked
	 * 
	 * 描述：返回登录用户的黑名单列表。登录用户将不会收到由黑名单中的用户发出的私信。 一次获取全部黑名单，无分页。 参数： ver 固定2，第二版
	 * 
	 * { "uid": 12345, "sid": "12345klsfksfj", "ver": 2, "req": {} }
	 */

	public static String getBlockedList(BaseParamBean bBean) {
		JSONObject jobject = new JSONObject();
		JSONObject object = basejson(bBean, jobject);
		return object.toString();
	}

	/**
	 * 5,关注（他人） /friend/follow
	 * 
	 * @param bBean
	 * @return
	 */
	/*
	 * //请求 "uid" : 1019, "req" : { "uid" : 1012 }
	 */

	public static String getFollow(BaseParamBean bBean) {
		JSONObject jobject = new JSONObject();
		try {
			jobject.put("uid", bBean.getPartner_id());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject object = basejson(bBean, jobject);
		return object.toString();
	}

	/**
	 * 取消关注(他人)
	 * 
	 * @param bBean
	 * @return
	 */
	public static String getUnFollow(BaseParamBean bBean) {

		return getFollow(bBean);
	}

	/**
	 * 7,拉黑（他人） /friend/block
	 * 
	 * @return
	 */
	public static String getBlock(BaseParamBean bBean) {

		return getFollow(bBean);
	}

	/**
	 * 8,取消拉黑（他人） /friend/unblock
	 * 
	 * @param bBean
	 * @return
	 */
	public static String getUnBlock(BaseParamBean bBean) {
		return getFollow(bBean);
	}

	/**
	 * 9, 查找用户 friend/find_people
	 * @param registerBean 
	 * 
	 * @return
	 */

	/*
	 * "uid": 32, //登录用户 "sid": "sessionid123456789" "req": { "program_id": 50,
	 * "offset": 0, "cpp": 30, "period": 1980, //80后，90后... "gender": 1,
	 * "astroid": 6, "tags": [1,2,3,4,5,6,7,8,9] }
	 */

	public static String getFindPeople(BaseParamBean bBean, UserRegisterBean registerBean) {
		JSONObject jobject = new JSONObject();
		UserRegisterBean rBean = registerBean;
		int period = 0,gender = 100,astroid = 100;
 		Map<String, Integer> filterItems = rBean.getFilterItems();
		List<SocialPoiBean> filterSocialPoiBeans = rBean.getFilterSocialPoiBeans();
		List tags = new ArrayList();
		JSONArray json = new JSONArray();
		
		//"period","gender","astroid"
		if(filterItems.containsKey(TivicGlobal.getInstance().AGE)){
			period = filterItems.get(TivicGlobal.getInstance().AGE);
		}
		if(filterItems.containsKey(TivicGlobal.getInstance().STAR)){
			astroid = filterItems.get(TivicGlobal.getInstance().STAR);
		}
		if(filterItems.containsKey(TivicGlobal.getInstance().GENDER)){
			gender = filterItems.get(TivicGlobal.getInstance().GENDER);
		}
		if(filterSocialPoiBeans.size() != 0){
			for(SocialPoiBean socialPoiBean: filterSocialPoiBeans){
				json.put(socialPoiBean.getValue());
			}
		}
		try {
			// jobject.put("program_id", bBean.getPid());
			jobject.put("program_id", bBean.getPid());
			jobject.put("offset", bBean.getOffset());
			jobject.put("cpp", bBean.getCountInPage());
			if(period != 0)
				jobject.put("period", period);
			if(gender != 100)
				jobject.put("gender", gender);
			if(astroid != 100)
				jobject.put("astroid", astroid);
			jobject.put("tags", json);
			// jobject.put("cpp", bBean.getCountInPage());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject object = basejson(bBean, jobject);
		return object.toString();
	}

	/**
	 * 1,获取频道列表信息接口，公共接口，无需登录 pgc/channel_list
	 * 
	 * @param bBean
	 * @return
	 */

	public static String getChannelList() {
		JSONObject jobject = new JSONObject();
		JSONObject object = new JSONObject();
		try {
			object.put("req", jobject);
			object.put("cv", TivicGlobal.versionName);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	/**
	 * 3, 获取指定用户的关注节目列表:全部节目,按加入关注的时间降序排列 mytv/pg_list_all
	 * 
	 * @return
	 */

	/*
	 * "req": { 'uid': 12345 }
	 */

	public static String getPeopleFocusProgramsList(BaseParamBean bBean) {
		JSONObject jobject = new JSONObject();
		try {
			jobject.put("uid", bBean.getPartner_id());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject object = basejson(bBean, jobject);
		return object.toString();
	}

	/**
	 * 4, 获取我的关注节目列表:某一天会播出的节目 mytv/pg_list_day
	 * 
	 * @param bBean
	 * @param day
	 * @return
	 */

	// "req": {
	// 'day': [数据格式unix1970]，某一天的零点时刻
	// }

	public static String getDayFocusProgramsList(BaseParamBean bBean, String day) {
		JSONObject jobject = new JSONObject();
		try {
			jobject.put("day", day);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject object = basejson(bBean, jobject);
		return object.toString();
	}

	/**
	 * 节目搜索接口 search/program
	 * 
	 * @return
	 */

	/*
	 * req: { "name":"" //搜索的节目名 比如 非诚 }
	 */

	public static String getSearchProgramsList(BaseParamBean bBean,
			SearchProgramBean sBean) {
		JSONObject jobject = new JSONObject();
		try {
			jobject.put("name", sBean.getSearchProgramsName());
			jobject.put("start_day", sBean.getStart_day());
			jobject.put("end_day", sBean.getEnd_day());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject object = basejson(bBean, jobject);
		return object.toString();
	}

	/**
	 * 节目热度表_用户访问最多的节目 search/pghot_user_visit
	 * 
	 * @param bBean
	 * @return
	 */
	public static String getSearchHotProgramsList(BaseParamBean bBean) {
		return getChannelList();
	}

	/*
	 * 获得最新的版本信息
	 */
	public static String getLastestVersion() {
		JSONObject jsonObject = new JSONObject();
		try {
			JSONObject req = new JSONObject();
			req.put("type", 22);
			jsonObject.put("req", req);
			jsonObject.put("cv", TivicGlobal.versionName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}

	/*
	 * 获得用户感兴趣的明星、节目、内容
	 */
	public static String getPoiItemInfo() {
		UserRegisterBean urb = TivicGlobal.getInstance().userRegisterBean;
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", urb.getUserUID());
			jsonObject.put("sid", urb.getUserSID());
			jsonObject.put("ver", 1);
			jsonObject.put("cv", TivicGlobal.versionName);
			JSONObject req = new JSONObject();
			req.put("uid", urb.getUserUID());
			jsonObject.put("req", req);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}

	/**
	 * 5.获取指定用户的所有收藏文章id /article/list_all_ids
	 * 
	 * @return
	 */

	/*
	 * "ver" : 2, "req" : { "uid" : 21, }
	 */

	public static String getCollectArticleIds(BaseParamBean bBean) {
		JSONObject jobject = new JSONObject();
		try {
			jobject.put("uid", bBean.getUid());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject object = basejson(bBean, jobject);
		return object.toString();
	}

	/**
	 * #####获取频道截图列表 snapshot.tivic.com/v3/get_snapshots.php
	 * 
	 * @param bBean
	 * @param sBean
	 * @return
	 */
	public static String getSrceenShots(BaseParamBean bBean,
			ScreenShotsBean sBean) {
		JSONObject jobject = new JSONObject();
		try {
			jobject.put("channel_id", sBean.getChannel_id());
			jobject.put("timestamp", sBean.getTimestamp());
			jobject.put("count", sBean.getCount());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject object = basejson(bBean, jobject);
		return object.toString();
	}

	/*
	 * 更新用户位置信息
	 */
	public static String updateUserLocation(double lon, double lat) {
		UserRegisterBean urb = TivicGlobal.getInstance().userRegisterBean;
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", urb.getUserUID());
			jsonObject.put("sid", urb.getUserSID());
			jsonObject.put("ver", 1);
			jsonObject.put("cv", TivicGlobal.versionName);
			JSONObject req = new JSONObject();
			req.put("lat", lat);
			req.put("lon", lon);
			jsonObject.put("req", req);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}

	/**
	 * 5,获取的指定用户的关注节目—频道列表 mytv/pg_list_with_channel
	 * 描述：获取指定用户关注的所有节目id，以及每个节目所属的频道id
	 * 
	 * @param bBean
	 * @return
	 */

	/*
	 * uid 指定用户id。必填 { "req": { 'uid': 12345 } }
	 */

	public static String getChannelProgramFocusList(BaseParamBean bBean) {
		JSONObject jobject = new JSONObject();
		try {
			jobject.put("uid", bBean.getPartner_id());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject object = basejson(bBean, jobject);
		return object.toString();
	}

	/***
	 * 获取指定用户好友列表
	 */

	public static String getSocialFriendsList(BaseParamBean bBean) {
		JSONObject jobject = new JSONObject();
		try {
			jobject.put("uid", bBean.getPartner_id());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject object = basejson(bBean, jobject);
		return object.toString();
	}
	/*
	 * 获得用户的扩展信息
	 */
	public static String getExtInfo() {
		UserRegisterBean urb = TivicGlobal.getInstance().userRegisterBean;
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", urb.getUserUID());
			jsonObject.put("sid", urb.getUserSID());
			jsonObject.put("ver", 1);
			jsonObject.put("cv", TivicGlobal.versionName);
			JSONObject req = new JSONObject();
			req.put("uid",urb.getUserUID());
			jsonObject.put("req", req);
		} catch (Exception e) {
			
		}
		return jsonObject.toString();
	}

	public static String getFriendInfo(int uid) {
		UserRegisterBean urb = TivicGlobal.getInstance().userRegisterBean;
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", urb.getUserUID());
			jsonObject.put("sid", urb.getUserSID());
			jsonObject.put("ver", 1);
			jsonObject.put("cv", TivicGlobal.versionName);
			JSONObject req = new JSONObject();
			req.put("uid",uid);
			jsonObject.put("req", req);
		} catch (Exception e) {
			
		}
		return jsonObject.toString();
	}
	/*
	 * 获得用户的基本信息
	 * user/get_info_base
		{
		    "uid": "1005",
		    "sid": "6e3926e2848760a3ea2fbb3ccee6d1ea",
		    "ver": 1,
		    "req": {
		        "uid": "1005"		//指定被查询的用户
		    }
		}
	 */
	public static String getMyInfo() {
		UserRegisterBean urb = TivicGlobal.getInstance().userRegisterBean;
		JSONObject jsonObject = new JSONObject();
		UIUtils.Logi("chen", "uid==="+urb.getUserUID());
		try {
			jsonObject.put("uid", urb.getUserUID());
			jsonObject.put("sid", urb.getUserSID());
			jsonObject.put("ver", 1);
			jsonObject.put("cv", TivicGlobal.versionName);
			JSONObject req = new JSONObject();
			req.put("uid",urb.getUserUID());
			jsonObject.put("req", req);
		} catch (Exception e) {
			
		}
		return jsonObject.toString();
	}
	/*
	 * 获得用户本身的扩展信息
	 */
	public static String getMyExtInfo() {
		UserRegisterBean urb = TivicGlobal.getInstance().userRegisterBean;
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", urb.getUserUID());
			jsonObject.put("sid", urb.getUserSID());
			jsonObject.put("ver", 1);
			jsonObject.put("cv", TivicGlobal.versionName);
			JSONObject req = new JSONObject();
			req.put("uid",urb.getUserUID());
			jsonObject.put("req", req);
		} catch (Exception e) {
			
		}
		return jsonObject.toString();
	}
	/**
	 * 拉黑好友
	 */
	public static String getPullIntoBlackList(int uid) {
		UserRegisterBean urb = TivicGlobal.getInstance().userRegisterBean;
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", urb.getUserUID());
			jsonObject.put("sid", urb.getUserSID());
			jsonObject.put("ver", 1);
			jsonObject.put("cv", TivicGlobal.versionName);
			JSONObject req = new JSONObject();
			req.put("uid",uid);
			jsonObject.put("req", req);
		} catch (Exception e) {
			
		}
		return jsonObject.toString();
	}
	/**
	 * 取消拉黑好友
	 */
	public static String getPullOutOfBlackList(int uid) {
		UserRegisterBean urb = TivicGlobal.getInstance().userRegisterBean;
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", urb.getUserUID());
			jsonObject.put("sid", urb.getUserSID());
			jsonObject.put("ver", 1);
			jsonObject.put("cv", TivicGlobal.versionName);
			JSONObject req = new JSONObject();
			req.put("uid",uid);
			jsonObject.put("req", req);
		} catch (Exception e) {
			
		}
		return jsonObject.toString();
	}

}
