package com.igrs.tivic.phone.Global;

import com.igrs.tivic.phone.Bean.VisitBean;
import com.igrs.tivic.phone.Bean.Base.UserBean;
import com.igrs.tivic.phone.Bean.Login.UserRegisterBean;


public class TivicGlobal {
	public static final String STAR = "star";
	public static final String GENDER = "gender";
	public static final String AGE = "age";
	public static TivicGlobal instance = null;
	public String userUID = null;//登录成功的uid;
	public float screenWidth = 0; 
	public int viewPager_page = 0;
	public boolean firstEnterSys = true;
	public static TivicGlobal getInstance(){
		synchronized (TivicGlobal.class) {
			if(instance == null)instance = new TivicGlobal();
		}
		return instance;
	}
	public void exit(){
		instance = null;
	}
	public static double lon = 0;//经纬度
	public static double lat = 0;
	public static String UDID = "";//设备的唯一标识
	public static String ACCESS_TOKEN = "";//设备的唯一标识
	public static int displayWidth = 0; //屏幕宽度px值
	public static int displayHeight = 0; ////屏幕高度px值
	public static int mCurrentFuncID = FuncID.ID_EPG;
	public static int mCurrentUserPhotoID = 0;
	public static int socialInfoState = FuncID.ID_SOCIAL_INFO_OTHER;
	public static boolean mIsLogin = false; //用户是否为登录状态，涉及登录模式都需判断
	public static boolean mIsOnline = true; //用户是否保持在线，涉及登录模式都需判断
	public static int currentPostion = 0; //直播频道当前位置
	public static VisitBean currentVisit = new VisitBean(); //用户足迹，进入的节目基本信息 当切换频道和节目前，都需要对此进行赋值
	public static UserRegisterBean userRegisterBean = new UserRegisterBean();//用户注册的基本信息
	public static int notifyCount = 0; 
	public static int messageCount = 0;
	public static int currentTid = 0; //贴吧详情左右滑动使用
	public static int currentCid = 0; //三级页面左右滑动使用
	public static String userAvaterPath = "";//用户的头像
	public static String versionName = "";
}
