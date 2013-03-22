package com.igrs.tivic.phone.Parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.text.TextUtils;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Bean.LocationBean;
import com.igrs.tivic.phone.Bean.VisitBean;
import com.igrs.tivic.phone.Bean.Base.UserBean;
import com.igrs.tivic.phone.Bean.Login.UserPOIItemBean;
import com.igrs.tivic.phone.Bean.Login.UserRegisterBean;
import com.igrs.tivic.phone.Bean.Social.SNSAccountsBean;
import com.igrs.tivic.phone.Bean.Social.SocialPoiBean;
import com.igrs.tivic.phone.Bean.Social.WeiboBean;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Utils.ModelUtil;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;

public class RegisterParser {

	/**
	 * 注册
	 */
	public static int register(String json) {
		if(!Utils.isJsonValid(json))
			return -1;
		
		int code = 0;
		try {
			JSONObject jo = new JSONObject(json);
			if (!jo.isNull("ret")) {
				code = jo.getInt("ret");
			}
			if (!jo.isNull("resp")) {
				JSONObject jb = jo.getJSONObject("resp");
				UserRegisterBean ub = new UserRegisterBean();
				ub.setUserUID(jb.getInt("uid"));
				ub.setUserSID(jb.getString("sid"));
				ub.setUserMail(jb.getString("url"));
				if (!jb.isNull("profile")) {
					JSONObject job = jb.getJSONObject("profile");
					ub.setUserName(job.getString("username"));
					ub.setUserPassword(job.getString("password"));
					ub.setUserNickname(job.getString("nickname"));
					ub.setUserGender(job.getInt("gender"));
					ub.setUserBirthday(job.getString("birthday"));
					ub.setUserStatus(job.getInt("status"));
					ub.setUserStars(job.getInt("stars"));
					ub.setUserFoot(job.getString("foot"));
					ub.setUserWeiboId(job.getString("weibo"));
					ub.setUserVerify(job.getString("verfiy"));
					LocationBean lBean = new LocationBean();
					if(!job.isNull("lat") && !ModelUtil.isEmpty(job.getString("lat"))){
						lBean.setLat(Float.parseFloat(job.getString("lat")));
					}
					if(!job.isNull("lon") && !ModelUtil.isEmpty(job.getString("lon"))){
						lBean.setLon(Float.parseFloat(job.getString("lon")));
					}
					ub.setLoactionBean(lBean);
					ub.setUserDistance(job.getString("geo"));
					ub.setUserLastTime(job.getString("last_time"));
					ub.setUserOnlineTime(job.getString("online_time"));
					ub.setUserMsgTime(job.getString("msg_time"));
					ub.setUserActTime(job.getString("act_time"));
					ub.setUserType(job.getInt("type"));
					ub.setUserAvatar(job.getString("avater"));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return code;
	}

	/**
	 * 判断注册 邮箱 或者 手机 是否已经存在
	 */
	public static int isNOMailExisted(String json) {
		if(!Utils.isJsonValid(json))
			return -1;
		
		int ret_code = 0;// ret_code = 0 用户不存在 ( ret_code = 2 用户存在)
		try {
			JSONObject jObject = new JSONObject(json);
			if (!jObject.isNull("ret")) {
				ret_code = Integer.parseInt(jObject.getString("ret"));
			} else {

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return ret_code;
	}

	/**
	 * 发送验证短信到手机
	 */

	/*
	 * user/resend_sms { "ver": 1, "req": { "un": "18610010001" } } 成功 { "ret":
	 * 0, "errcode": 0, "msg": "ok", "resp": { "verify": 3000
	 * //取回的验证码，历史遗留，其实不应返回 } } 手机号非法 { "ret": 101, "errcode": 0, "msg":
	 * "\u624b\u673a\u53f7\u672a\u9a8c\u8bc1\u901a\u8fc7!", "resp": "" }
	 */

	public static int sendCodeToPhone(String json) {
		if(!Utils.isJsonValid(json))
			return -1;
		
		int code = 0;
		try {
			JSONObject jObject = new JSONObject(json);
			if (!jObject.isNull("ret")) {
				code = Integer.parseInt(jObject.getString("ret"));
			} else {

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return code;
	}

	/**
	 * 验证手机
	 * 
	 */

	/*
	 * 成功 { "ret": 0, "errcode": 0, "msg": "ok", "resp": { "valid": 1 } }
	 * 手机号格式错误 { "ret": 1, "errcode": 0, "msg":
	 * "1861000012 is not a valid phone number!", "resp": [] } 验证码错误 { "ret": 2,
	 * "errcode": 0, "msg": "verify code 6453 is not match the 18610000001",
	 * "resp": { "valid": 0 } }
	 */
	public static int checkPhone(String json) {
		
		if(!Utils.isJsonValid(json))
			return -1;
		
		int code = 0;
		try {
			JSONObject jObject = new JSONObject(json);
			if (!jObject.isNull("ret")) {
				code = Integer.parseInt(jObject.getString("ret"));
			} else {

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return code;
	}

	/**
	 * 判断是否注册
	 * 
	 * @param json
	 * @return
	 */
	public static Boolean isRegistered(String json) {
		if(!Utils.isJsonValid(json))
			return false;
		
		try {
			JSONObject jObject = new JSONObject(json);
			if (jObject.isNull("uid")) {
				return true;
			} else {
				return false;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 判断是否登录成功
	 */
	public static int resultForLogin(String json) throws Exception {
		if(!Utils.isJsonValid(json))
			return -1;
		
		UserRegisterBean urb = TivicGlobal.getInstance().userRegisterBean;
		int code = Const.HTTP_EXCEPTION;
		if (TextUtils.isEmpty(json)) {
			return code;
		}
		try {
			JSONObject jObject = new JSONObject(json);
			code = Integer.parseInt(jObject.getString("ret"));
			if(code == 2) {
				int errorCode = Integer.parseInt(jObject.getString("errcode"));
				if(errorCode == 101008) {
					return Const.PWD_ERROR;
				} else if(errorCode == 101043) {
					return Const.USERNAME_ERROR;
				} else if(errorCode == 101044) {
					return Const.USER_FORBIDDEN;
				}
			}
			if (code == 0) {
				if (!jObject.isNull("resp")) {
					JSONObject resp = jObject.getJSONObject("resp");
					int uid = resp.getInt("uid");
					String sid = resp.getString("sid");
					urb.setUserUID(uid);
					urb.setUserSID(sid);
					TivicGlobal.getInstance().userUID = uid + "";

					TivicGlobal.getInstance().mIsLogin = true;// 处理一些用户登录成功时的操作
					if (!resp.isNull("profile")) {
						JSONObject profile = resp.getJSONObject("profile");
						if (!profile.isNull("id")) {
							int id = profile.getInt("id");
							urb.setUserId(id);
						}
						if (!profile.isNull("username")) {
							String username = profile.getString("username");
							urb.setUserName(username);
						}
						if (!profile.isNull("nickname")) {
							String nickname = profile.getString("nickname");
							urb.setUserNickname(nickname);
						}
						if (!profile.isNull("gender")) {
							int gender = profile.getInt("gender");
							urb.setUserGender(gender);
						}
						if (!profile.isNull("birthday")) {
							String birthday = profile.getString("birthday");
							urb.setUserBirthday(birthday);
						}
						if (!profile.isNull("status")) {
							String status = profile.getString("status");
							if (!TextUtils.isEmpty(status)) {
								urb.setUserStatus(profile.getInt("status"));
							}
						}
						if (!profile.isNull("stars")) {
							String stars = profile.getString("stars");
							if (!TextUtils.isEmpty(stars)) {
								urb.setUserStars(Integer.parseInt(stars));
							}
						}
						if (!profile.isNull("foot")) {
							String foot = profile.getString("foot");
							urb.setUserFoot(foot);
						}
						if (!profile.isNull("sign")) {
							String sign = profile.getString("sign");
							urb.setUserSign(sign);
						}
						if (!profile.isNull("weibo")) {
							String weibo = profile.getString("weibo");
							urb.setUserWeiboId(weibo);
						}
						if (!profile.isNull("addtime")) {
							String addtime = profile.getString("addtime");
							urb.setUserAddtime(addtime);
						}
						ArrayList<UserPOIItemBean> poiItems = new ArrayList<UserPOIItemBean>();
						if (!profile.isNull("star")) {
							String stars = profile.getString("star");
							if (!TextUtils.isEmpty(stars)) {
								String[] ids = stars.split(",");
								for (String id : ids) {
									UserPOIItemBean poiItem = new UserPOIItemBean();
									poiItem.setPoi_key(id);
									poiItem.setPoi_type(UserPOIItemBean.STAR_TYPE);
									poiItem.setChecked(true);
									poiItems.add(poiItem);
								}
							}
						}
						if (!profile.isNull("content")) {
							String contents = profile.getString("content");
							if (!TextUtils.isEmpty(contents)) {
								String[] ids = contents.split(",");
								for (String id : ids) {
									UserPOIItemBean poiItem = new UserPOIItemBean();
									poiItem.setPoi_key(id);
									poiItem.setPoi_type(UserPOIItemBean.CATE_TYPE);
									poiItem.setChecked(true);
									poiItems.add(poiItem);
								}
							}
						}
						if (!profile.isNull("cate")) {
							String cates = profile.getString("cate");
							if (!TextUtils.isEmpty(cates)) {
								String[] ids = cates.split(",");
								for (String id : ids) {
									UserPOIItemBean poiItem = new UserPOIItemBean();
									poiItem.setPoi_key(id);
									poiItem.setPoi_type(UserPOIItemBean.PROGRAM_TYPE);
									poiItem.setChecked(true);
									poiItems.add(poiItem);
								}
							}
						}
						urb.setUserPoiItems(poiItems);
						if (!profile.isNull("verify")) {
							String verify = profile.getString("verify");
							urb.setUserVerify(verify);
						}
						if (!profile.isNull("lat") && !profile.isNull("lon")) {

							String latStr = profile.getString("lat");
							String lonStr = profile.getString("lon");
							if (!TextUtils.isEmpty(latStr)
									&& !TextUtils.isEmpty(lonStr)) {

								LocationBean locationBean = new LocationBean(
										Float.parseFloat(latStr),
										Float.parseFloat(lonStr));
								urb.setLoactionBean(locationBean);
							}
						}
						if (!profile.isNull("geo")) {
							String geo = profile.getString("geo");
							urb.setUserDistance(geo);
						}
						if (!profile.isNull("last_time")) {
							String last_time = profile.getString("last_time");
							urb.setUserLastTime(last_time);
						}
						if (!profile.isNull("online_time")) {
							String online_time = profile
									.getString("online_time");
							urb.setUserOnlineTime(online_time);
						}
						if (!profile.isNull("msg_time")) {
							String msg_time = profile.getString("msg_time");
							urb.setUserMsgTime(msg_time);
						}
						if (!profile.isNull("act_time")) {
							String act_time = profile.getString("act_time");
							urb.setUserActTime(act_time);
						}
						if (!profile.isNull("type")) {
							int type = profile.getInt("type");
							urb.setUserType(type);
						}
						if (!profile.isNull("avatar")) {
							String avatar = profile.getString("avatar");
							urb.setUserAvatar(avatar);
						}
					}
					if (!resp.isNull("bound_weibo")) {
						int bound_weibo = resp.getInt("bound_weibo");
						if (!resp.isNull("accounts")) {
							if (bound_weibo == 1) {// 1代表绑定了微博
								urb.setBoundWeibo(true);
								JSONObject accounts = resp
										.getJSONObject("accounts");
								SNSAccountsBean snsAccountsBean = new SNSAccountsBean();
								if (!accounts.isNull("type")) {
									String type = accounts.getString("type");
									snsAccountsBean.setAccountsType(type);
								}
								if (!accounts.isNull("access_key")) {
									String access_key = accounts
											.getString("access_key");
									snsAccountsBean.setAccessKey(access_key);
								}
								if (!accounts.isNull("access_token")) {
									String access_token = accounts
											.getString("access_token");
									snsAccountsBean
											.setAccessToken(access_token);
								}
								if (!accounts.isNull("uniqid")) {
									String uniqid = accounts
											.getString("uniqid");
									snsAccountsBean.setUniqid(uniqid);
								}
								urb.setSnsAccounts(snsAccountsBean);
							} else {
								urb.setBoundWeibo(false);
							}
						}
					}
					if (!resp.isNull("fill_profile")) {
						int fill_profile = resp.getInt("fill_profile");
						urb.setFill_profile(fill_profile + "");
					}
				}
			}
			return code;
		} catch (JSONException e) {
			e.printStackTrace();
			return Const.HTTP_EXCEPTION;
		}
	}

	/**
	 * 判断是否注册成功
	 * 
	 * @param json
	 * @return
	 */
	public static int checkResultForRegister(String json) throws Exception {
		if(!Utils.isJsonValid(json))
			return -1;
		
		int code = Const.HTTP_EXCEPTION;
		try {
			JSONObject jObject = new JSONObject(json);
			if (!jObject.isNull("uid")) {
				code = Const.HTTP_SUCCESS;
			} else {
				code = Integer.parseInt(jObject.getString("ret"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return code;
	}

	/**
	 * 通过用户名获取 uid
	 */
	public static String getUID(String json) {
		
		if(!Utils.isJsonValid(json))
			return null;
		
		String uid = "";
		if (ModelUtil.hasLength(json)) {
			try {
				JSONObject jObject = new JSONObject(json);
				if (!jObject.isNull("uid")) {
					uid = jObject.getString("uid");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return uid;
	}

	/**
	 * 微博
	 * 
	 * @param json
	 * @return
	 */

	/*
	 * 成功登录： { "ret": 0, "errcode": 0, "msg": "ok", "resp": { "uid": "11",
	 * "sid": "cdc72912bbce91fcf96223e635dfa", "profile": { "relation": 0,
	 * //此字段无效 "nick": "xiaoming", "gender": 1, "sign": "", "loc": { "lat":"",
	 * "lon":"" }, "visit": { "id": "", "title": "", }, "last_time":null,
	 * "type":3 //新浪微博用户 } } } 获取sina用户信息失败： { 'ret' => 1, 'errcode' => 0, 'msg'
	 * => 'can not get weibo user info!', 'resp' => '' }
	 */

	public static int parserWeibo(String json) {
		
		if(!Utils.isJsonValid(json))
			return -1;
		
		int code = 0;
		if (ModelUtil.hasLength(json)) {
			try {
				JSONObject jObject = new JSONObject(json);
				if (!jObject.isNull("ret")) {
					code = jObject.getInt("ret");
				} else {

				}
				if (!jObject.isNull("resp")) {
					JSONObject jo = jObject.getJSONObject("resp");
					UserBean uBean = new UserBean();
					WeiboBean wBean = new WeiboBean();
					uBean.setUid(jo.getInt("uid"));
					if (!jo.isNull("profile")) {
						JSONObject job = jo.getJSONObject("profile");
						uBean.setUserGender(job.getInt("gender"));
						uBean.setUserNickName(job.getString("nick"));
						if (!job.isNull("loc")) {
							JSONObject jno = job.getJSONObject("loc");
							LocationBean locationBean = new LocationBean();
							if(!jno.isNull("lat") && !ModelUtil.isEmpty(jno.getString("lat"))){
								locationBean.setLat(Float.parseFloat(jno.getString("lat")));
							}
							if(!jno.isNull("lon") && !ModelUtil.isEmpty(jno.getString("lon"))){
								locationBean.setLon(Float.parseFloat(jno.getString("lon")));
							}
							uBean.setUsrLocation(locationBean);
						}
						if (!job.isNull("visit")) {
							JSONObject jsb = job.getJSONObject("visit");
							wBean.setVisit_id(jsb.getString("id"));
							wBean.setVisit_title(jsb.getString("title"));
							wBean.setVisit_channelId(jsb.getString("channelid"));
						}
						wBean.setLast_time(job.getString("last_time"));
						wBean.setType(job.getInt("type"));
					}
					wBean.setUserSID(jo.getString("sid"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {

		}
		return code;
	}

	/*
	 * 手机验证成功后，通过手机注册返回的JSON数据
	 */
	public static int parseRegisterByPhone(String json) {
		
		if(!Utils.isJsonValid(json))
			return -1;
		
		UserRegisterBean urb = TivicGlobal.getInstance().userRegisterBean;
		urb.setFill_profile("1");
		int code = 0;
		try {
			JSONObject jObject = new JSONObject(json);
			code = Integer.parseInt(jObject.getString("ret"));
			if (code == 0) {
				if (!jObject.isNull("resp")) {
					JSONObject resp = jObject.getJSONObject("resp");
					int uid = resp.getInt("uid");
					String sid = resp.getString("sid");
					urb.setUserUID(uid);
					urb.setUserSID(sid);
					TivicGlobal.getInstance().userUID = uid + "";

					TivicGlobal.getInstance().mIsLogin = true;// 处理一些用户登录成功时的操作
					if (!resp.isNull("profile")) {
						JSONObject profile = resp.getJSONObject("profile");
						if (!profile.isNull("id")) {
							int id = profile.getInt("id");
							urb.setUserId(id);
						}
						if (!profile.isNull("username")) {
							String username = profile.getString("username");
							urb.setUserName(username);
						}
						if (!profile.isNull("nickname")) {
							String nickname = profile.getString("nickname");
							urb.setUserNickname(nickname);
						}
						if (!profile.isNull("gender")) {
							int gender = profile.getInt("gender");
							urb.setUserGender(gender);
						}
						if (!profile.isNull("birthday")) {
							String birthday = profile.getString("birthday");
							urb.setUserBirthday(birthday);
						}
						if (!profile.isNull("status")) {
							String status = profile.getString("status");
							if (!TextUtils.isEmpty(status)) {
								urb.setUserStatus(profile.getInt("status"));
							}
						}
						if (!profile.isNull("stars")) {
							String stars = profile.getString("stars");
							if (!TextUtils.isEmpty(stars)) {
								urb.setUserStars(Integer.parseInt(stars));
							}
						}
						if (!profile.isNull("foot")) {
							String foot = profile.getString("foot");
							urb.setUserFoot(foot);
						}
						if (!profile.isNull("sign")) {
							String sign = profile.getString("sign");
							urb.setUserSign(sign);
						}
						if (!profile.isNull("weibo")) {
							String weibo = profile.getString("weibo");
							urb.setUserWeiboId(weibo);
						}
						if (!profile.isNull("addtime")) {
							String addtime = profile.getString("addtime");
							urb.setUserAddtime(addtime);
						}
						ArrayList<UserPOIItemBean> poiItems = new ArrayList<UserPOIItemBean>();
						if (!profile.isNull("star")) {
							String stars = profile.getString("star");
							if (!TextUtils.isEmpty(stars)) {
								String[] ids = stars.split(",");
								for (String id : ids) {
									UserPOIItemBean poiItem = new UserPOIItemBean();
									poiItem.setPoi_key(id);
									poiItem.setPoi_type(UserPOIItemBean.STAR_TYPE);
									poiItem.setChecked(true);
									poiItems.add(poiItem);
								}
							}
						}
						if (!profile.isNull("content")) {
							String contents = profile.getString("content");
							if (!TextUtils.isEmpty(contents)) {
								String[] ids = contents.split(",");
								for (String id : ids) {
									UserPOIItemBean poiItem = new UserPOIItemBean();
									poiItem.setPoi_key(id);
									poiItem.setPoi_type(UserPOIItemBean.CATE_TYPE);
									poiItem.setChecked(true);
									poiItems.add(poiItem);
								}
							}
						}
						if (!profile.isNull("cate")) {
							String cates = profile.getString("cate");
							if (!TextUtils.isEmpty(cates)) {
								String[] ids = cates.split(",");
								for (String id : ids) {
									UserPOIItemBean poiItem = new UserPOIItemBean();
									poiItem.setPoi_key(id);
									poiItem.setPoi_type(UserPOIItemBean.PROGRAM_TYPE);
									poiItem.setChecked(true);
									poiItems.add(poiItem);
								}
							}
						}
						urb.setUserPoiItems(poiItems);
						if (!profile.isNull("verify")) {
							String verify = profile.getString("verify");
							urb.setUserVerify(verify);
						}
						if (!profile.isNull("lat") && !profile.isNull("lon")) {

							String latStr = profile.getString("lat");
							String lonStr = profile.getString("lon");
							if (!TextUtils.isEmpty(latStr)
									&& !TextUtils.isEmpty(lonStr)) {

								LocationBean locationBean = new LocationBean(
										Float.parseFloat(latStr),
										Float.parseFloat(lonStr));
								urb.setLoactionBean(locationBean);
							}
						}
						if (!profile.isNull("geo")) {
							String geo = profile.getString("geo");
							urb.setUserDistance(geo);
						}
						if (!profile.isNull("last_time")) {
							String last_time = profile.getString("last_time");
							urb.setUserLastTime(last_time);
						}
						if (!profile.isNull("online_time")) {
							String online_time = profile
									.getString("online_time");
							urb.setUserOnlineTime(online_time);
						}
						if (!profile.isNull("msg_time")) {
							String msg_time = profile.getString("msg_time");
							urb.setUserMsgTime(msg_time);
						}
						if (!profile.isNull("act_time")) {
							String act_time = profile.getString("act_time");
							urb.setUserActTime(act_time);
						}
						if (!profile.isNull("type")) {
							int type = profile.getInt("type");
							urb.setUserType(type);
						}
						if (!profile.isNull("avatar")) {
							String avatar = profile.getString("avatar");
							urb.setUserAvatar(avatar);
						}
					}
				}
			}
			return code;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return code;
	}

	/*
	 * 解析邮箱注册返回的JSON数据
	 */
	public static int parseRegisterByMail(String json) {
		
		if(!Utils.isJsonValid(json))
			return -1;
		
		UserRegisterBean urb = TivicGlobal.getInstance().userRegisterBean;
		int code = Const.HTTP_EXCEPTION;
		try {
			JSONObject jsonObject = new JSONObject(json);
			if (!jsonObject.isNull("ret")) {
				code = jsonObject.getInt("ret");
				if (code == 100) {// 因为返回两个值为100，所以要先判断

					if (!jsonObject.isNull("resp")) {
						code = Const.HTTP_SUCCESS;
						JSONObject resp = jsonObject.getJSONObject("resp");
						int uid = resp.getInt("uid");
						urb.setUserUID(uid);
						String sid = resp.getString("sid");
						urb.setUserSID(sid);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return code;
		}

		return code;
	}

	/*
	 * 解析修改用户信息，不包含用户 感兴趣的东西返回的JSON数据
	 */
	public static int parseModifyUserBasicInfo1(String json) {
		
		if(!Utils.isJsonValid(json))
			return -1;
		
		int code = Const.HTTP_EXCEPTION;
		try {
			JSONObject jsonObject = new JSONObject(json);
			if (!jsonObject.isNull("ret")) {
				code = jsonObject.getInt("ret");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return code;
		}

		return code;
	}

	public static int parseResendMail(String json) {
		if(!Utils.isJsonValid(json))
			return -1;
		
		int code = Const.HTTP_EXCEPTION;
		try {
			JSONObject jsonObject = new JSONObject(json);
			if (!jsonObject.isNull("ret")) {
				code = jsonObject.getInt("ret");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return code;
		}

		return code;
	}

	/*
	 * 解析通过邮箱找回密码返回的JSON数据
	 */

	public static int parseMailForPwd(String json) {
		
		if(!Utils.isJsonValid(json))
			return -1;
		
		int code = Const.HTTP_EXCEPTION;
		try {
			JSONObject jsonObject = new JSONObject(json);
			if (!jsonObject.isNull("ret")) {
				code = jsonObject.getInt("ret");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return code;
		}

		return code;
	}

	public static int parseMsgForPwd(String json) {
		
		if(!Utils.isJsonValid(json))
			return -1;
		
		UserRegisterBean urb = TivicGlobal.getInstance().userRegisterBean;
		int code = Const.HTTP_EXCEPTION;
		try {
			JSONObject jsonObject = new JSONObject(json);
			if (!jsonObject.isNull("ret")) {
				code = jsonObject.getInt("ret");
				if (code == 0) {// 短信发送成功
					JSONObject resp = jsonObject.getJSONObject("resp");
					int uid = resp.getInt("id");
					TivicGlobal.getInstance().userUID = uid + "";
					urb.setUserUID(uid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return code;
		}

		return code;
	}

	/*
	 * 
	 * 成功： { "ret": 0, "errcode": 0, "msg": "ok", "resp": "" }
	 * 
	 * 失败： 返回码(ret) 错误码(errcode) 说明(msg) 1 105101 params not enough 2 105102
	 * user not found 2 105103 vc not match 验证码无效 2 105104 user not active 用户未激活
	 */

	public static int parseVerifyMsgForPwd(String json) {
		
		if(!Utils.isJsonValid(json))
			return -1;
		
		int code = Const.HTTP_EXCEPTION;
		try {
			JSONObject jsonObject = new JSONObject(json);
			if (!jsonObject.isNull("ret")) {
				code = jsonObject.getInt("ret");
				if (code == 2) {
					int errorcode = jsonObject.getInt("errcode");
//					System.out.println("errorcode=" + errorcode);
					if (errorcode == 105203) {// 验证码无效
						code = Const.CHK_CODE_ERROR;
					} else if (errorcode == 105104) {// 用户未激活
						code = Const.USER_NOT_ACTIVED;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return code;
		}

		return code;
	}

	public static int parseModifyPwdByPhone(String json) {
		
		if(!Utils.isJsonValid(json))
			return -1;
		
		int code = Const.HTTP_EXCEPTION;
		try {
			JSONObject jsonObject = new JSONObject(json);
			if (!jsonObject.isNull("ret")) {
				code = jsonObject.getInt("ret");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return code;
		}

		return code;
	}

	public static List<UserPOIItemBean> parsePoiStarsInfo(String json) {
		
		if(!Utils.isJsonValid(json))
			return null;
		
		List<UserPOIItemBean> poiItems = new ArrayList<UserPOIItemBean>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			if (!jsonObject.isNull("ret")) {
				int code = jsonObject.getInt("ret");
				if (code == 0) {
					if (!jsonObject.isNull("resp")) {
						JSONObject resp = jsonObject.getJSONObject("resp");
						if (!resp.isNull("results")) {
							JSONArray results = resp.getJSONArray("results");
							for (int i = 0; i < results.length(); i++) {
								UserPOIItemBean item = new UserPOIItemBean();
								JSONObject star = results.getJSONObject(i);
								item.setPoi_type(UserPOIItemBean.STAR_TYPE);
								item.setPoi_icon(star.getString("icon"));
								item.setPoi_text(star.getString("text"));
								item.setPoi_key(star.getString("star_id"));
								poiItems.add(item);
							}
						}

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return poiItems;
		}
		return poiItems;
	}

	public static List<UserPOIItemBean> parsePoiProgramsInfo(String json) {
		
		if(!Utils.isJsonValid(json))
			return null;
		
		List<UserPOIItemBean> poiItems = new ArrayList<UserPOIItemBean>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			if (!jsonObject.isNull("ret")) {
				int code = jsonObject.getInt("ret");
				if (code == 0) {
					if (!jsonObject.isNull("resp")) {
						JSONObject resp = jsonObject.getJSONObject("resp");
						if (!resp.isNull("results")) {
							JSONArray results = resp.getJSONArray("results");
							for (int i = 0; i < results.length(); i++) {
								UserPOIItemBean item = new UserPOIItemBean();
								JSONObject program = results.getJSONObject(i);
								item.setPoi_type(UserPOIItemBean.PROGRAM_TYPE);
								item.setPoi_icon(program.getString("icon"));
								item.setPoi_text(program.getString("text"));
								item.setPoi_key(program.getString("program_id"));
								poiItems.add(item);
							}
						}

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return poiItems;
		}
		return poiItems;
	}

	public static List<UserPOIItemBean> parsePoiCategoriesInfo(String json) {
		
		if(!Utils.isJsonValid(json))
			return null;
		
		List<UserPOIItemBean> poiItems = new ArrayList<UserPOIItemBean>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			if (!jsonObject.isNull("ret")) {
				int code = jsonObject.getInt("ret");
				if (code == 0) {
					if (!jsonObject.isNull("resp")) {
						JSONObject resp = jsonObject.getJSONObject("resp");
						if (!resp.isNull("results")) {
							JSONArray results = resp.getJSONArray("results");
							for (int i = 0; i < results.length(); i++) {
								UserPOIItemBean item = new UserPOIItemBean();
								JSONObject category = results.getJSONObject(i);
								item.setPoi_type(UserPOIItemBean.CATE_TYPE);
								item.setPoi_icon(category.getString("icon"));
								item.setPoi_text(category.getString("text"));
								item.setPoi_key(category
										.getString("category_id"));
								poiItems.add(item);
							}
						}

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return poiItems;
		}
		return poiItems;
	}

	public static int parseModifyUserBasicInfo(String json) {
		if(!Utils.isJsonValid(json))
			return -1;
		
		int code = Const.HTTP_EXCEPTION;
		try {
			JSONObject jsonObject = new JSONObject(json);
			if (!jsonObject.isNull("ret")) {
				code = jsonObject.getInt("ret");
				if(code == 2) {
					int errorCode = jsonObject.getInt("errcode");
					if(errorCode == Const.USER_NICKNAME_EXISTS) {
						return Const.USER_NICKNAME_EXISTS;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return code;
		}

		return code;
	}

	public static int parseLoginByWeibo(String json) {
		
		if(!Utils.isJsonValid(json))
			return -1;
		
		UserRegisterBean urb = TivicGlobal.getInstance().userRegisterBean;
		urb.setUserPoiItems(new ArrayList<UserPOIItemBean>());
		int code = Const.HTTP_EXCEPTION;
		try {
			JSONObject jsonObject = new JSONObject(json);
			if (!jsonObject.isNull("ret")) {
				code = jsonObject.getInt("ret");
				if(code == 2) {
					int errorCode = Integer.parseInt(jsonObject.getString("errcode"));
					 if(errorCode == 101044) {
						return Const.USER_FORBIDDEN;
				}
				}
				if (code == 0) {
//					if(TextUtils.isEmpty(urb.getUserSign())) {
//						urb.setFill_profile("1");
//					} else {
//						urb.setFill_profile("0");
//					}
					urb.setBoundWeibo(true);//设置绑定微博微true
					urb.setFill_profile("0");
					TivicGlobal.getInstance().mIsLogin = true;
					if (!jsonObject.isNull("resp")) {
						JSONObject resp = jsonObject.getJSONObject("resp");
						if (!resp.isNull("uid")) {
							String uid = resp.getString("uid");
//							UIUtils.Logi("chen", "uid=========="+uid);
							urb.setUserUID(Integer.parseInt(uid));
							urb.setUserId(Integer.parseInt(uid));
						}
						if (!resp.isNull("sid")) {
							String sid = resp.getString("sid");
							urb.setUserSID(sid);
						}
						if (!resp.isNull("profile")) {
							JSONObject profile = resp.getJSONObject("profile");
							if (!profile.isNull("nick")) {
								String nick = profile.getString("nick");
								urb.setUserNickname(nick);
							}
							if (!profile.isNull("gender")) {
								if(!TextUtils.isEmpty(profile.getString("gender"))) {
									int gender = profile.getInt("gender");
									urb.setUserGender(gender);
								}
							}
							if (!profile.isNull("sign")) {
								String sign = profile.getString("sign");
								urb.setUserSign(sign);
							}
							if (!profile.isNull("loc")) {
								JSONObject loc = profile.getJSONObject("loc");
								if (!loc.isNull("lat") && !loc.isNull("lon")) {

									String latStr = loc.getString("lat");
									String lonStr = loc.getString("lon");
									if (!TextUtils.isEmpty(latStr)
											&& !TextUtils.isEmpty(lonStr)) {

										LocationBean locationBean = new LocationBean(
												Float.parseFloat(latStr),
												Float.parseFloat(lonStr));
										urb.setLoactionBean(locationBean);
									}
								}
							}
							if (!profile.isNull("visit")) {
								JSONObject visit = profile
										.getJSONObject("visit");
								VisitBean visitBean = new VisitBean();
								if (!visit.isNull("id")) {
									String id = visit.getString("id");
									visitBean.setVisit_pid(id);
								}
								if (!visit.isNull("title")) {
									String title = visit.getString("title");
									visitBean.setVisit_ProgramTitle(title);
								}
								if (!visit.isNull("channelid")) {
									String channelid = visit
											.getString("channelid");
									visitBean.setVisit_channelid(channelid);
								}
								urb.setVisit(visitBean);
							}
							if (!profile.isNull("last_time")) {
								String last_time = profile
										.getString("last_time");
								urb.setUserLastTime(last_time);
							}
							if (!profile.isNull("type")) {
								int type = profile.getInt("type");
								urb.setUserType(type);
							}
							if (!profile.isNull("avatar")) {
								String avatar = profile.getString("avatar");
								urb.setUserAvatar(avatar);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			code = Const.HTTP_EXCEPTION;
			return code;
		}

		return code;
	}

	/*
	 * 绑定微博
	 */
	public static int parseFastenWeibo(String json) {
		if(!Utils.isJsonValid(json))
			return -1;
		
		int code = Const.HTTP_EXCEPTION;
		try {
			JSONObject jsonObject = new JSONObject(json);
			if (!jsonObject.isNull("ret")) {
				code = jsonObject.getInt("ret");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return code;
		}

		return code;
	}

	public static int parserUnFastenWeibo(String json) {
		if(!Utils.isJsonValid(json))
			return -1;
		
		int code = Const.HTTP_EXCEPTION;
		try {
			JSONObject jsonObject = new JSONObject(json);
			if (!jsonObject.isNull("ret")) {
				code = jsonObject.getInt("ret");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return code;
		}

		return code;
	}

	/*
	 * 修改密码
	 */
	public static int parseModifyPwd(String json) {
		if(!Utils.isJsonValid(json))
			return -1;
		
		int code = Const.HTTP_EXCEPTION;
		try {
			JSONObject jsonObject = new JSONObject(json);
			if (!jsonObject.isNull("ret")) {
				code = jsonObject.getInt("ret");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return code;
		}

		return code;
	}

	/*
	 * 获得最新的版本信息
	 */
	public static StringBuilder parseLastestVersion(String json) {
		if(!Utils.isJsonValid(json))
			return null;
		
		StringBuilder sb = new StringBuilder();
		try {
			JSONObject jsonObject = new JSONObject(json);
			if (!jsonObject.isNull("version")) {
				String version = jsonObject.getString("version");
				sb.append(version);
			}
			if (!jsonObject.isNull("description")) {
				String description = jsonObject.getString("description");
				sb.append(description);
			}
			if (!jsonObject.isNull("download_url")) {
				String download_url = jsonObject.getString("download_url");
				sb.append(download_url);
			}
			if (!jsonObject.isNull("update_time")) {
				String update_time = jsonObject.getString("update_time");
				sb.append(update_time);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb;
	}
	/*
	 * 更新用户位置
	 */
	public static int parseUpdateUserLocation(String json) {
		if(!Utils.isJsonValid(json))
			return -1;
		int code = Const.HTTP_EXCEPTION;
		try {
			JSONObject jsonObject = new JSONObject(json);
			if (!jsonObject.isNull("ret")) {
				code = jsonObject.getInt("ret");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return code;
		}

		return code;
	}
	/*
	 * 获得用户的扩展信息
	 */
	public static void parseGetExtInfo(String json, ArrayList<SocialPoiBean> beans) {
		if(!Utils.isJsonValid(json))
			return;
		try {
			JSONObject jsonObject = new JSONObject(json);
			if(!jsonObject.isNull("resp")) {
				JSONObject resp = jsonObject.getJSONObject("resp");
				if(!resp.isNull("items")) {
					JSONArray jsonArray = resp.getJSONArray("items");
					for(int i=0;i<jsonArray.length();i++) {
						JSONObject item = jsonArray.getJSONObject(i);
						String key = item.getString("key");
						if("astroid".equals(key)) {
							int value = Integer.parseInt(item.getString("value"));
							int[] constellation = { R.drawable.social_date_xingzuo_baiyang,
									R.drawable.social_date_xingzuo_jinniu,
									R.drawable.social_date_xingzuo_shuangzi,
									R.drawable.social_date_xingzuo_juxie,
									R.drawable.social_date_xingzuo_shizi,
									R.drawable.social_date_xingzuo_chunv,
									R.drawable.social_date_xingzuo_tianping,
									R.drawable.social_date_xingzuo_tianxie,
									R.drawable.social_date_xingzuo_sheshou,
									R.drawable.social_date_xingzuo_mojie,
									R.drawable.social_date_xingzuo_shuiping,
									R.drawable.social_date_xingzuo_shuangyu };
							SocialPoiBean constellBean = new SocialPoiBean(SocialPoiBean.CONSTELLATION_TYPE, constellation[value]+"");
							beans.add(constellBean);
						} else if("star_id".equals(key)) {
							int value = Integer.parseInt(item.getString("value"));
							SocialPoiBean starBean = new SocialPoiBean(SocialPoiBean.STAR_TYPE, item.getString("icon"));
							starBean.setValue(value);
							beans.add(starBean);
						} else if("program_id".equals(key)) {
							int value = Integer.parseInt(item.getString("value"));
							SocialPoiBean proBean = new SocialPoiBean(SocialPoiBean.PROGRAM_TYPE, item.getString("icon"));
							proBean.setValue(value);
							beans.add(proBean);
						} else if("category_id".equals(key)) {
							int value = Integer.parseInt(item.getString("value"));
							SocialPoiBean cateBean = new SocialPoiBean(SocialPoiBean.CATEGORY_TYPE, item.getString("icon"));
							cateBean.setValue(value);
							beans.add(cateBean);
						}
					}
				}
			}
		} catch (Exception e) {
		}
	}
	/*
	 * 获得用户的基本信息
	 */
	public static int parseGetMyInfo(String json) {
		if(!Utils.isJsonValid(json))
			return -1;
		int code = -1;
		UserRegisterBean urb = TivicGlobal.getInstance().userRegisterBean;
		try {
			JSONObject jo = new JSONObject(json);
			if(!jo.isNull("ret")){
				code = jo.getInt("ret");
				if(0 == code){// success
					if(!jo.isNull("resp")){
						JSONObject jnb = jo.getJSONObject("resp");
						if(!jnb.isNull("info")){
							JSONObject job = jnb.getJSONObject("info");
							urb.setUserNickname(job.getString("nick"));
							if(job.getString("gender").equals("")){
								urb.setUserGender(0);
							}else{
								urb.setUserGender(Integer.parseInt(job.getString("gender")));
							}
							urb.setUserBirthday(job.getString("birthday"));
							urb.setUserSign(job.getString("sign"));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return code;
	}
	/*
	 * 获得用户本身的扩展信息
	 */
	public static int parseGetMyExtInfo(String json) {
		UserRegisterBean urb = TivicGlobal.getInstance().userRegisterBean;
		if(!Utils.isJsonValid(json))
			return -1;
		int code = -1;
		try {
			
			JSONObject jsonObject = new JSONObject(json);
			code = jsonObject.getInt("ret");
			if(code == 0) {
				UIUtils.Logi("chen", "获取扩展信息成功");
				if(!jsonObject.isNull("resp")) {
					JSONObject resp = jsonObject.getJSONObject("resp");
					if(!resp.isNull("items")) {
						JSONArray jsonArray = resp.getJSONArray("items");
						ArrayList<UserPOIItemBean> poiItems = new ArrayList<UserPOIItemBean>();
						for(int i=0;i<jsonArray.length();i++) {
							JSONObject item = jsonArray.getJSONObject(i);
							String key = item.getString("key");
							if("star_id".equals(key)) {
								int value = Integer.parseInt(item.getString("value"));
								UserPOIItemBean poiItem = new UserPOIItemBean();
								poiItem.setPoi_key(value+"");
								poiItem.setPoi_type(UserPOIItemBean.STAR_TYPE);
								poiItem.setChecked(true);
								poiItems.add(poiItem);
								
							} else if("program_id".equals(key)) {
								int value = Integer.parseInt(item.getString("value"));
								UserPOIItemBean poiItem = new UserPOIItemBean();
								poiItem.setPoi_key(value+"");
								poiItem.setPoi_type(UserPOIItemBean.PROGRAM_TYPE);
								poiItem.setChecked(true);
								poiItems.add(poiItem);
							} else if("category_id".equals(key)) {
								int value = Integer.parseInt(item.getString("value"));
								UserPOIItemBean poiItem = new UserPOIItemBean();
								poiItem.setPoi_key(value+"");
								poiItem.setPoi_type(UserPOIItemBean.CATE_TYPE);
								poiItem.setChecked(true);
								poiItems.add(poiItem);
							}
						}
						urb.setUserPoiItems(poiItems);
						UIUtils.Logi("chen", "size======"+poiItems.size());
					}
				}
			}
		} catch (Exception e) {
		}
		return code;
	}

}
