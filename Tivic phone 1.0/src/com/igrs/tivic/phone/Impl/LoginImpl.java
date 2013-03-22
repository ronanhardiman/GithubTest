package com.igrs.tivic.phone.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.JsonReader;

import com.igrs.tivic.phone.Bean.Base.UserBean;
import com.igrs.tivic.phone.Bean.Login.UserPOIItemBean;
import com.igrs.tivic.phone.Bean.Login.UserRegisterBean;
import com.igrs.tivic.phone.Bean.Social.SNSAccountsBean;
import com.igrs.tivic.phone.Bean.Social.SocialPoiBean;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Global.URLConfig;
import com.igrs.tivic.phone.Interface.ILogin;
import com.igrs.tivic.phone.Parser.RegisterParser;
import com.igrs.tivic.phone.Utils.HttpClientUtils;
import com.igrs.tivic.phone.Utils.JsonForRequest;
import com.igrs.tivic.phone.Utils.ModelUtil;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;

public class LoginImpl implements ILogin {
	
	/*
	 * 返回值 ret = 0 // 成功返回 ret = 1 // 参数错误 ret = 2 // 鉴权失败 ret = 3 // 服务器内部错误
	 * ret = 100 // 邮箱未验证通过// 邮箱未激活 ret = 101 // 手机号未验证通过 ret = 102 // 昵称已存在 ret
	 * = 107 // 邮箱格式错误
	 */
	HttpClientUtils httpClientUtils = null;
	private final static String charset = "utf-8";

	public LoginImpl() {
		httpClientUtils = HttpClientUtils.getInstance();
	}
	/*
	 * 检测用户是否被占用
	 */
	public int checkUsrnameExist(String usrname) {
		String jsonRequest = JsonForRequest.userExistJson(usrname);
		System.out.println("jsonRequest="+jsonRequest);
		String json = httpClientUtils.requestPostHttpClient4(URLConfig.exists_user, charset, jsonRequest);
		System.out.println("json="+json);
		return RegisterParser.isNOMailExisted(json);
	}

	/*
	 * 发送验证短信到手机
	 */
	public int sendVerifySMS(String phonenum) {
		String jsonRequest = JsonForRequest.sendNumberJson(phonenum);
//		System.out.println("jsonRequest="+jsonRequest);
		String json = httpClientUtils.requestPostHttpClient4(URLConfig.resend_sms, charset, jsonRequest);
//		System.out.println("json="+json);
		return RegisterParser.sendCodeToPhone(json);
	}
	

	/*
	 * 验证手机
	 */
	public int verifyPhoneNum(String phonenum, String verifycode) {
		String jsonRequest = JsonForRequest.verifyNumberJson(phonenum,verifycode);
		String json = httpClientUtils.requestPostHttpClient4(URLConfig.reg_checkPhoneNumber, charset, jsonRequest);
		return RegisterParser.checkPhone(json);
	}

	/*
	 * 注册 需要在这个接口内，初始化一些全局的UserRegisterBean、UsrBean等数据结构
	 * 返回值：0：手机注册成功，100：邮箱注册成功，但邮箱未激活 //100邮箱格式错误??，101手机格式错误
	 */
	public int register(UserRegisterBean userRegisterBean) {
		if ((!Utils.isEmailValid(userRegisterBean.getUserName()) && !Utils
				.isPhoneNumValid(userRegisterBean.getUserName()))
				|| Utils.isPasswordValid(userRegisterBean.getUserPassword())) {
			return 1;
		}
		String jsonRequest = JsonForRequest.registerJson(userRegisterBean.getUserName(), userRegisterBean.getUserPassword());
		String json = httpClientUtils.requestPostHttpClient4(URLConfig.reg_register, charset, jsonRequest);
		int code = Const.HTTP_EXCEPTION;
		if(!ModelUtil.hasLength(json)){
			return Const.HTTP_EXCEPTION;
		}
		try {
			code = RegisterParser.checkResultForRegister(json);
		} catch (Exception e) {
			e.printStackTrace();
			code = Const.HTTP_EXCEPTION;
		}
		return code;
	}

	/*
	 * 登录 成功后需要在这个接口内，初始化一些全局的UserRegisterBean、UsrBean、SNSAccountsBean等数据结构
	 * 返回值：0成功，100邮箱未激活，101短信未激活
	 * 
	 * 重要说明： 1)登录成功后，表示用户通过了口令验证，客户端须保存返回的sid，绝大多数api调用时必须填写此sid。
	 * 2)服务端利用sid对用户做身份认证，移动设备重启后用户可以使用存储在本地的sid调用api，不强迫用户重新登录。
	 * 3)没有显式的logout接口，只有再次调用Login接口时由服务端更新并返回新的sid。客户端注销操作仅需要清空本地的sid。
	 * 4)凡是必须填写sid的接口，如果不填sid或填写失效的sid，服务端会返回以下错误，此时客户端需要用户重新登录。
	 * {"ret":2,"errcode":403,"msg":"Forbidden","resp":""}
	 */
	public int login(String usrname, String password) {
		if(!ModelUtil.hasLength(usrname)) return Const.USERVALIDERROR; //用户名空
		if(!Utils.isUsrNameValid(usrname) && !Utils.isPasswordValid(usrname) && !Utils.isEmailValid(usrname))
			return Const.USERLEGALERROR;  //用户名 不合法
		if(!ModelUtil.hasLength(password)) return Const.PWDVALIDERROR;  //密码为空
		if(!Utils.isPasswordValid(password)) return Const.PWDVLEGALERROR;   //密码不合法
		String jsonRequest = JsonForRequest.logJsonOld(usrname, password);
		System.out.println("loginJsonRequest==="+jsonRequest);
		String json = httpClientUtils.requestPostHttpClient4(URLConfig.login_login, charset, jsonRequest); 
		System.out.println("loginJson==="+json);
		int code = Const.HTTP_EXCEPTION;
		if(!ModelUtil.hasLength(json)) return code;
		if(json.equals(Const.SOCKET_TIMEOUT_EXCEPTION+"")) return Const.SOCKET_TIMEOUT_EXCEPTION;
		try {
			code = RegisterParser.resultForLogin(json);
		} catch (Exception e) {
			e.printStackTrace();
			code = Const.HTTP_EXCEPTION;
		}
		return code;
	}
	public int loginCheckMailActived(String usrname, String password) {
		if(!ModelUtil.hasLength(usrname)) return Const.USERVALIDERROR; //用户名空
		if(!Utils.isUsrNameValid(usrname) && !Utils.isPasswordValid(usrname) && !Utils.isEmailValid(usrname))
			return Const.USERLEGALERROR;  //用户名 不合法
		if(!ModelUtil.hasLength(password)) return Const.PWDVALIDERROR;  //密码为空
		if(!Utils.isPasswordValid(password)) return Const.PWDVLEGALERROR;   //密码不合法
		String jsonRequest = JsonForRequest.logJsonOld(usrname, password);
		String json = httpClientUtils.requestPostHttpClient4(URLConfig.login_login, charset, jsonRequest); 
		int code = Const.HTTP_EXCEPTION;
		if(!ModelUtil.hasLength(json)) return code;
		if(json.equals(Const.SOCKET_TIMEOUT_EXCEPTION+"")) return Const.SOCKET_TIMEOUT_EXCEPTION;
		try {
			code = RegisterParser.resultForLogin(json);
			
		} catch (Exception e) {
			e.printStackTrace();
			code = Const.HTTP_EXCEPTION;
		}
		return code;
	}
	/*
	 * 注销
	 */
	public void logout() {
//		String url = URLConfig.login_logout;
//		httpClientUtils.requestGet(url, charset);
	}

	/*
	 * 微博登录成功后需要在这个接口内，初始化一些全局的UserRegisterBean、UsrBean等数据结构
	 */
	public int loginWeibo(SNSAccountsBean bean) {
		String jsonRequest = JsonForRequest.logWeiboJson(bean);
		String json = httpClientUtils.requestPostHttpClient2(URLConfig.login_weibo, charset, jsonRequest);
//		String json = httpClientUtils.requesPost(URLConfig.login_weibo, charset);
		RegisterParser.parserWeibo(json);
		return 0;
	}
	/*
	 * 手机验证成功后，通过手机注册
	 */
	public int registerByPhone(String username,String password) {
		String jsonRequest = JsonForRequest.registerJson(username, password);
		String json = httpClientUtils.requestPostHttpClient4(URLConfig.reg_register, charset, jsonRequest);
		return RegisterParser.parseRegisterByPhone(json);
	}
	/*
	 * 通过邮件注册，返回Const.HTTP_SUCCESS代表注册成功，但未激活   100邮箱代表格式错误 1代表邮件发送失败
	 */
	public int registerByMail(String username,String password) {
		String jsonRequest = JsonForRequest.registerJson(username, password);
		String json = httpClientUtils.requestPostHttpClient4(URLConfig.reg_register, charset, jsonRequest);
		return RegisterParser.parseRegisterByMail(json);
	}
	/*
	 *修改用户信息，不包含用户 感兴趣的东西
	 */
	public int modifyUserBasicInfo1(UserRegisterBean urb) {
		String jsonReqtest = JsonForRequest.getBaseInfo(urb);
//		System.out.println("modifyJsonForRequest="+jsonReqtest);
		String json = httpClientUtils.requestPostHttpClient4(URLConfig.modify_user_register_info, charset, jsonReqtest);
//		System.out.println("modifyJson="+json);
		return RegisterParser.parseModifyUserBasicInfo1(json);
		
	}
	/*
	 * 重新发送邮件
	 */
	public int resendMail(String username) {
		String jsonReqtest = JsonForRequest.reSendMail(username);
		String json = httpClientUtils.requestPostHttpClient4(URLConfig.resend_mail, charset, jsonReqtest);
		return RegisterParser.parseResendMail(json);
	}
	/*
	 * 发送找回密码邮件
	 */
	public int sendMailForPwd(String username) {
		String jsonRequest = JsonForRequest.sendMailForPwd(username);
//		System.out.println("maiijsonRequest="+jsonRequest);
		String json = httpClientUtils.requestPostHttpClient4(URLConfig.send_mail_for_pwd, charset, jsonRequest);
//		System.out.println("json="+json);
		return RegisterParser.parseMailForPwd(json);
	}
	/*
	 * 发送找回密码的短信
	 */
	public int sendMsgForPwd(String username) {
		String jsonRequest = JsonForRequest.sendMsgForPwd(username);
		String json = httpClientUtils.requestPostHttpClient4(URLConfig.send_msg_for_pwd, charset, jsonRequest);
		return RegisterParser.parseMsgForPwd(json);
	}
	/*
	 * 验证找回密码短信
	 */
	public int verifyCodeForPwd(String vefifyNumber) {
		String jsonRequest = JsonForRequest.verifyCodeForPwd(vefifyNumber);
		String json = httpClientUtils.requestPostHttpClient4(URLConfig.verify_msg_for_pwd, charset, jsonRequest);
		return RegisterParser.parseVerifyMsgForPwd(json);
	}
	/*
	 * 通过短信修改密码
	 */
	public int modifyPwdByPhone(String uid,String code,String pwd) {
		String jsonRequest = JsonForRequest.modifyPwdByPhone(uid,code,pwd);
		String json = httpClientUtils.requestPostHttpClient4(URLConfig.modify_pwd_by_phone, charset, jsonRequest);
		return RegisterParser.parseModifyPwdByPhone(json);
	}
	/*
	 * 获得推荐明星列表
	 */
	public List<UserPOIItemBean> getPoiStarsInfo(UserRegisterBean urb) {
		String jsonRequest = JsonForRequest.getPoiStarsInfo(urb);
		String json = httpClientUtils.requestPostHttpClient4(URLConfig.starts_info, charset, jsonRequest);
		return RegisterParser.parsePoiStarsInfo(json);
	}
	/*
	 * 获得推荐节目列表
	 */
	public List<UserPOIItemBean> getPoiProgramsInfo(UserRegisterBean urb) {
		String jsonRequest = JsonForRequest.getPoiProgramsInfo(urb);
		String json = httpClientUtils.requestPostHttpClient4(URLConfig.programs_info, charset, jsonRequest);
		return RegisterParser.parsePoiProgramsInfo(json);
	}
	/*
	 * 获得推荐内容列表
	 */
	public List<UserPOIItemBean> getPoiCategoriesInfo(UserRegisterBean urb) {
		String jsonRequest = JsonForRequest.getPoiCategoriesInfo(urb);
		String json = httpClientUtils.requestPostHttpClient4(URLConfig.categories_info, charset, jsonRequest);
		return RegisterParser.parsePoiCategoriesInfo(json);
	}
	/*
	 * 修改用户资料，包括喜欢的节目、内容以及明星
	 */
	public int modifyUserBasicInfo(UserRegisterBean urb,List<String> stars,List<String>programs,List<String>categories) {
		String jsonRequest = JsonForRequest.modifyUserBasicInfo(urb,stars,programs,categories);
		System.out.println("modify1JsonRequest="+jsonRequest);
		String json = httpClientUtils.requestPostHttpClient4(URLConfig.modify_user_register_info, charset, jsonRequest);
		System.out.println("modify1Json="+json);
		return RegisterParser.parseModifyUserBasicInfo(json);
		
	}
	/*
	 * 修改用户资料，包括用户的头像
	 */
	public int modifyUserBasicInfo2(UserRegisterBean urb,List<String> stars,List<String>programs,List<String>categories) {
		if(urb.isModifyUserAvatar()) {
			HashMap<String, String> map = new HashMap<String, String>();
			String jsonRequest = JsonForRequest.modifyUserBasicInfo(urb,stars,programs,categories);
			System.out.println("modifyJsonRequest="+jsonRequest);
			map.put("json", jsonRequest);
			map.put("file", urb.getUserAvartarPath());
			String json = httpClientUtils.requestPostHttpClient(URLConfig.get_modify_info, charset, map);
			System.out.println("modifyJson="+json);
			return RegisterParser.parseModifyUserBasicInfo(json);
		} else {
			return modifyUserBasicInfo(urb, stars, programs, categories);
		}
	}
	public int modifyUserBasicInfo3(UserRegisterBean urb) {
		HashMap<String, String> map = new HashMap<String, String>();
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
		if(urb.isModifyUserAvatar()) {
			String jsonRequest = JsonForRequest.modifyUserBasicInfo(urb,stars,programs,categories);
			System.out.println("modifyJsonRequest="+jsonRequest);
			map.put("json", jsonRequest);
			map.put("file", urb.getUserAvartarPath());
			String json = httpClientUtils.requestPostHttpClient(URLConfig.get_modify_info, charset, map);
			System.out.println("modifyJson="+json);
			return RegisterParser.parseModifyUserBasicInfo(json);
		} else {
			return modifyUserBasicInfo(urb, stars, programs, categories);
		}
	}
	
	/*
	 * 通过微博登陆
	 */
	public int loginByWeibo(String access_token,String uniqid) {
		String jsonRequest = JsonForRequest.loginByWeibo(access_token,uniqid);
		UIUtils.Logi("chen","jsonForRequest="+jsonRequest);
		String json = httpClientUtils.requestPostHttpClient4(URLConfig.login_by_weibo, charset, jsonRequest);
		UIUtils.Logi("chen","json="+json);
		return RegisterParser.parseLoginByWeibo(json);
	}
	/*
	 * 绑定微博
	 */
	public int fastenWeibo(String access_token, String uniqid) {
		String jsonRequest = JsonForRequest.fastenWeibo(access_token,uniqid);
//		System.out.println("jsonRequest="+jsonRequest);
		String json = httpClientUtils.requestPostHttpClient4(URLConfig.fasten_weibo, charset, jsonRequest);
//		System.out.println("json="+json);
		return RegisterParser.parseFastenWeibo(json);
	}
	/*
	 * 解除绑定微博
	 */
	public int unFastenWeibo() {
		String jsonRequest = JsonForRequest.unFastenWeibo();
//		System.out.println("jsonRequest="+jsonRequest);
		String json = httpClientUtils.requestPostHttpClient4(URLConfig.unfasten_weibo, charset, jsonRequest);
//		System.out.println("json="+json);
		return RegisterParser.parserUnFastenWeibo(json);
		
	}
	/*
	 * 修改密码
	 */
	public int modifyPwd(String oldPwd,String newPwd) {
		String jsonRequest = JsonForRequest.modifyPwd(oldPwd,newPwd);
//		System.out.println("modifyPwd_jsonRequest="+jsonRequest);
		String json = httpClientUtils.requestPostHttpClient4(URLConfig.modify_pwd, charset, jsonRequest);
//		System.out.println("modifyPwd_json="+json);
		return RegisterParser.parseModifyPwd(json);
	}
	/*
	 * 获得最新版本
	 */
	public StringBuilder getLastestVersion() {
		String jsonRequest = JsonForRequest.getLastestVersion();
		String json = httpClientUtils.requestPostHttpClient4(URLConfig.upgrade_info, charset, jsonRequest);
//		System.out.println("jsonRequest="+jsonRequest);
//		System.out.println("json="+json);
		return RegisterParser.parseLastestVersion(json);
	}
	/*
	 * 更新用户位置信息
	 */
	public int updateUserLocation(double lon,double lat) {
		String jsonRequest = JsonForRequest.updateUserLocation(lon, lat);
		String json = httpClientUtils.requestPostHttpClient4(URLConfig.update_user_location, charset, jsonRequest);
//		System.out.println("jsonRequest="+jsonRequest);
//		System.out.println("json="+json);
		return RegisterParser.parseUpdateUserLocation(json);
	}
	/*
	 * 获得用户的扩展信息
	 */
	public void getExtInfo(ArrayList<SocialPoiBean> beans) {
		String jsonRequest = JsonForRequest.getExtInfo();
		String json = httpClientUtils.requestPostHttpClient4(URLConfig.poi_item_info, charset, jsonRequest);
//		System.out.println("jsonRequest="+jsonRequest);
//		System.out.println("json="+json);
		RegisterParser.parseGetExtInfo(json,beans);
	}
	/*
	 * 获得用户本身的基本信息
	 */
	public int getMyInfo() {
		String jsonRequest = JsonForRequest.getMyInfo();
		String json = httpClientUtils.requestPostHttpClient4(URLConfig.get_base_info, charset, jsonRequest);
//		System.out.println("用户基本信息jsonRequest="+jsonRequest);
//		System.out.println("用户基本信息json="+json);
		return RegisterParser.parseGetMyInfo(json);
	}
	/*
	 * 获得用户本身的扩展信息
	 */
	public int getMyExtInfo() {
		String jsonRequest = JsonForRequest.getMyExtInfo();
		String json = httpClientUtils.requestPostHttpClient4(URLConfig.poi_item_info, charset, jsonRequest);
//		System.out.println("jsonRequest="+jsonRequest);
//		System.out.println("json="+json);
		return RegisterParser.parseGetMyExtInfo(json);
	}
}
