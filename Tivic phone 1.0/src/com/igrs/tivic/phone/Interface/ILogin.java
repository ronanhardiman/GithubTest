package com.igrs.tivic.phone.Interface;

import com.igrs.tivic.phone.Bean.Login.UserRegisterBean;
import com.igrs.tivic.phone.Bean.Social.SNSAccountsBean;

/*
 用户注册/登录流程
1，邮箱注册流程：先注册，后验证
	Step1)输入邮箱，检测用户是否被占用	exists_user
		ipad1.1实装:用户邮箱必须为从未【register】过，与Step3是否验证通过无关。	
	Step2)填写密码，点击注册			register	
	Step3)用户转入邮箱点击验证链接。
		ipad1.1实装:点击打开浏览器，跳转至响应邮箱首页。	
	Step4)登录							login
2，手机注册流程：先验证，后注册
	Step1)输入手机号，检测用户是否被占用	exists_user
		ipad1.1实装:无论是否成功【valid_code】,只要用户从未成功登录系统，则该用户可用。	
	Step2)点击发送验证码，向手机发送短信	resend_sms
	Step3)用户输入短息验证码后验证			valid_code	
	Step4)用户输入密码后，点击注册		register			
		ipad1.1实装:手机用户的【register】操作成功后，无需调用【login】
3，第三方登录流程：（sinaWeibo）
	采用oauth2.0的客户端认证方式，具体参考相关文档。
 */
public interface ILogin {
	
/*
 * 返回值
 	ret = 0		// 成功返回
	ret = 1		// 参数错误
	ret = 2		// 鉴权失败
	ret = 3		// 服务器内部错误

	ret = 100	// 邮箱未验证通过// 邮箱未激活
	ret = 101	// 手机号未验证通过
	ret = 102   // 昵称已存在
	ret = 107   // 邮箱格式错误
 */
	
/*
 *  检测用户是否被占用
 */
	public int checkUsrnameExist(String usrname);
	
/*
 *发送验证短信到手机
 */
	public int sendVerifySMS(String phonenum);
	
/*
 * 验证手机	
 */
	public int verifyPhoneNum(String phonenum, String verifycode);
	
/*
 * 注册 
 * 需要在这个接口内，初始化一些全局的UserRegisterBean、UsrBean等数据结构
 * 返回值：0：手机注册成功，100：邮箱注册成功，但邮箱未激活   //100邮箱格式错误??，101手机格式错误
 */	
	public int register(UserRegisterBean userRegisterBean);

/*
 * 登录
 * 成功后需要在这个接口内，初始化一些全局的UserRegisterBean、UsrBean、SNSAccountsBean等数据结构
 * 返回值：0成功，100邮箱未激活，101短信未激活
 
 *重要说明：
1)登录成功后，表示用户通过了口令验证，客户端须保存返回的sid，绝大多数api调用时必须填写此sid。
2)服务端利用sid对用户做身份认证，移动设备重启后用户可以使用存储在本地的sid调用api，不强迫用户重新登录。
3)没有显式的logout接口，只有再次调用Login接口时由服务端更新并返回新的sid。客户端注销操作仅需要清空本地的sid。
4)凡是必须填写sid的接口，如果不填sid或填写失效的sid，服务端会返回以下错误，此时客户端需要用户重新登录。
{"ret":2,"errcode":403,"msg":"Forbidden","resp":""}
 */
	public int login(String usrname, String password);
	
/*
 * 注销 
 */	
	public void logout();
	
/*
 *微博登录 
 *成功后需要在这个接口内，初始化一些全局的UserRegisterBean、UsrBean等数据结构
 */	
	public int loginWeibo(SNSAccountsBean bean);
	

}
