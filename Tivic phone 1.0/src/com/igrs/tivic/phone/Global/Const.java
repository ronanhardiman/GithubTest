package com.igrs.tivic.phone.Global;

import java.io.File;

import android.os.Environment;

public class Const {
	
	public static final boolean DEBUG = true;
	
	public static final String SHAREDPREFERENCES_NAME = "my_pref";
    public static final String KEY_GUIDE_ACTIVITY = "guide_activity";
	
	/*
	 * database
	 */
	public static final String TIVIC_DATABASE_NAME = "tivic";
	public static final String EPG_TABLE = "epg_date";
	public final static String SDCARD_MNT = "/mnt/sdcard";
    public final static String SDCARD = "/sdcard";
	/*
	 * file path
	 */
	public static final File EXTERNAL_STORE_PATH = Environment
			.getExternalStorageDirectory();
	public static final String TIVIC_STORE_PATH = EXTERNAL_STORE_PATH
			+ "/tivic/cache";
	public static final String EPG_STORE_PATH = TIVIC_STORE_PATH + "/EPGData";
	public static final int UGC_SUBNAIL_MAX_WIDTH = 256;
	public static final int UGC_SUBNAIL_MIN_WIDTH = 128;
	public static final int Login_Process_MAX = 100;
	public static final int Login_Animation_Duration = 11000;
	public static final int LOGIN_SUCCESS = 0;
	public static final int LOGIN_USERNAME_PWD_ERROE = 2;
	public static final int LOGIN_EMAIL_NOT_ACTIVED = 100;
	public static final int LOGIN_MSG_NOT_ACTIVED = 101;	
	public static final int ERR_INTERNET_DISCONNECTED = 106;// 无网络连接
	public static final int SOCKET_TIMEOUT_EXCEPTION = 205;//socket 请求超时  or 无网络连接
	public static final int SOCKET_CONNECTION_TIMEOUT = 9000;//请求超时
	public final static int HTTP_EXCEPTION = 400;// 其他异常
	public final static int HTTP_SUCCESS = 200; // login success
	public static final int USER_EXIST_NO = 0; // 用户不存在
	public static final int USER_EXIST_YES = 2; // 用户已存在
	public static final int USER_MAIL_FALSE = 100; // 邮箱格式错误
	public static final int USER_PHONE_FALSE = 101; // 手机格式错误
	public static final int USERVALIDERROR = 201;// 用户名为空
	public final static int USERLEGALERROR = 202;// 用户名不合法
	public final static int PWDVALIDERROR = 203;// 密码为空
	public final static int PWDVLEGALERROR = 204;// 密码不合法
	public static final int CHK_CODE_ERROR = 11;//短信验证码错误
	public static final int USER_NOT_ACTIVED = 12;//手机号未激活
	public static final int SUCCESS = 0;
	public static final int PWD_ERROR = 101008;
	public static final int USERNAME_ERROR = 101043;
	public static final int USER_FORBIDDEN = 101044;
	public static final int USER_NICKNAME_EXISTS = 101045;
//	public static final int Base_Usr_Image_Alpha = 150;
//	public static final int Social_Arrow_Top = 10;
//	public static final int Social_Arrow_Dur = 90;
	public static int Male = 0;
	public static int Female = 1;
	// 0 表示关注了你 1 表示取消关注了你 2 表示拉黑了你 3 表示取消拉黑了你 4 admin
	public static final int Social_Notify_Type_Focus = 0;
	public static final int Social_Notify_Type_Cancle = 1;
	public static final int Social_Notify_Type_Black = 2;
	public static final int Social_Notify_Type_CancleBlack = 3;
	public static final int Social_Notify_Type_Admin = 4;
	// 通知总分类
	public static final int Fling_Distance_MAX = 200;
	public static final String PHOTO_128 = "!w128";
	public static final String imgHearder = URLConfig.avarter_path;	
	public static final int COUNTINPAGE = 30;
	public static final String DEFAULT_ENCODING = "utf-8";	//默认编码
	public static final String CONSUMER_KEY = "3947363888";//新浪微博的key
	
	
	public static final String TMPSAVEDIR =  Environment.getExternalStorageDirectory().getAbsolutePath() + "/tivic";	
	public static final String PHOTO_DIR = TMPSAVEDIR +"/tmpimg/";
	public static final String PHOTO_FILE = "tivic_temp_img.jpg";

}
