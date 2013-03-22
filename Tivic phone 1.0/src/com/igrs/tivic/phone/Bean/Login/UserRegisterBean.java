package com.igrs.tivic.phone.Bean.Login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.igrs.tivic.phone.Bean.LocationBean;
import com.igrs.tivic.phone.Bean.VisitBean;
import com.igrs.tivic.phone.Bean.Social.SNSAccountsBean;
import com.igrs.tivic.phone.Bean.Social.SocialPoiBean;

public class UserRegisterBean {
	private int userUID;  //uid用户唯一标识
	private String userSID;  //sid由客户端保存，只有再次调用Login接口时由服务端更新
	private String userMail; ////用户邮箱跳转
	private int userId;
	private String userName;
	private String userPassword;
	private String userNickname;
	private int userGender;  // 性别，1 为男 0为女
	private String userBirthday;
	private int userStatus;  //0拖黑 1标准注册用户 6微博账户 7运维 9管理员账户
	private int userStars;   //星座
	private String userFoot;  //用户足迹 最后一次访问的栏目
	private String userSign;  //签名
	private String userWeiboId;  //绑定的weibo用户id,0为未绑定
	private String userAddtime;  
	private ArrayList<UserPOIItemBean> userPoiItems; // 明星，节目，内容0~3个item
	private String userVerify;              //验证码，验证后为空
	private int relation;      //0-陌生人，1-拉黑，2-粉丝，3-我关注对方，4-双向关注
	private VisitBean visit;
	private LocationBean loactionBean;
	private String userDistance;             //geohash计算距离
	private String userLastTime;
	private String userOnlineTime;
	private String userMsgTime;
	private String userActTime;
	private int userType;        //1 邮箱 2 手机 3新浪 4腾讯
	private String userAvatar;      //"/userdata/avatar/23/87/1005.jpg",//头像
	private boolean boundWeibo;//判断是否已经绑定了微博
	private boolean modifyUserAvatar;//判断用户是否修改头像
	private boolean successModifyUserAvater;//判断头像是否修改成功
	private String userAvartarPath;//记录用户头像的绝对位置
	private SNSAccountsBean snsAccounts;    //绑定的新浪微博
	private String fill_profile;
	
	private List<SocialPoiBean> filterSocialPoiBeans = new ArrayList<SocialPoiBean>();//用于记录过滤感兴趣条件
	private Map<String, Integer> filterItems = new HashMap<String,Integer>();//用于记录过滤的年龄，性别以及星座等条件
	
	public LinkedList<Integer> friendUids = new LinkedList<Integer>();//通过点击查看他的好友存放点击的当前好友的uid
	public boolean isNeedsUid = false;//是否需要从friendUids里面取
	public int getRelation() {
		return relation;
	}
	public void setRelation(int relation) {
		this.relation = relation;
	}
	public VisitBean getVisit() {
		return visit;
	}
	public void setVisit(VisitBean visit) {
		this.visit = visit;
	}
	public String getUserMail() {
		return userMail;
	}
	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}
	public ArrayList<UserPOIItemBean> getUserPoiItems() {
		return userPoiItems;
	}
	public void setUserPoiItems(ArrayList<UserPOIItemBean> userPoiItems) {
		this.userPoiItems = userPoiItems;
	}
	public int getUserUID() {
		return userUID;
	}
	public void setUserUID(int userUID) {
		this.userUID = userUID;
	}
	public String getUserSID() {
		return userSID;
	}
	public void setUserSID(String userSID) {
		this.userSID = userSID;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getUserNickname() {
		return userNickname;
	}
	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}
	public int getUserGender() {
		return userGender;
	}
	public void setUserGender(int userGender) {
		this.userGender = userGender;
	}
	public String getUserBirthday() {
		return userBirthday;
	}
	public void setUserBirthday(String userBirthday) {
		this.userBirthday = userBirthday;
	}
	public int getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}
	public int getUserStars() {
		return userStars;
	}
	public void setUserStars(int userStars) {
		this.userStars = userStars;
	}
	public String getUserFoot() {
		return userFoot;
	}
	public void setUserFoot(String userFoot) {
		this.userFoot = userFoot;
	}
	public String getUserSign() {
		return userSign;
	}
	public void setUserSign(String userSign) {
		this.userSign = userSign;
	}
	public String getUserWeiboId() {
		return userWeiboId;
	}
	public void setUserWeiboId(String userWeiboId) {
		this.userWeiboId = userWeiboId;
	}
	public String getUserAddtime() {
		return userAddtime;
	}
	public void setUserAddtime(String userAddtime) {
		this.userAddtime = userAddtime;
	}
	public String getUserVerify() {
		return userVerify;
	}
	public void setUserVerify(String userVerify) {
		this.userVerify = userVerify;
	}
	public LocationBean getLoactionBean() {
		return loactionBean;
	}
	public void setLoactionBean(LocationBean loactionBean) {
		this.loactionBean = loactionBean;
	}
	public String getUserDistance() {
		return userDistance;
	}
	public void setUserDistance(String userDistance) {
		this.userDistance = userDistance;
	}
	public String getUserLastTime() {
		return userLastTime;
	}
	public void setUserLastTime(String userLastTime) {
		this.userLastTime = userLastTime;
	}
	public String getUserOnlineTime() {
		return userOnlineTime;
	}
	public void setUserOnlineTime(String userOnlineTime) {
		this.userOnlineTime = userOnlineTime;
	}
	public String getUserMsgTime() {
		return userMsgTime;
	}
	public void setUserMsgTime(String userMsgTime) {
		this.userMsgTime = userMsgTime;
	}
	public String getUserActTime() {
		return userActTime;
	}
	public void setUserActTime(String userActTime) {
		this.userActTime = userActTime;
	}
	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}
	public String getUserAvatar() {
		return userAvatar;
	}
	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}
	public SNSAccountsBean getSnsAccounts() {
		return snsAccounts;
	}
	public void setSnsAccounts(SNSAccountsBean snsAccounts) {
		this.snsAccounts = snsAccounts;
	}
	public String getFill_profile() {
		return fill_profile;
	}
	public void setFill_profile(String fill_profile) {
		this.fill_profile = fill_profile;
	}
	
	public boolean isBoundWeibo() {
		return boundWeibo;
	}
	public void setBoundWeibo(boolean boundWeibo) {
		this.boundWeibo = boundWeibo;
	}
	
	public boolean isSuccessModifyUserAvater() {
		return successModifyUserAvater;
	}
	public void setSuccessModifyUserAvater(boolean successModifyUserAvater) {
		this.successModifyUserAvater = successModifyUserAvater;
	}
	public boolean isModifyUserAvatar() {
		return modifyUserAvatar;
	}
	public void setModifyUserAvatar(boolean modifyUserAvatar) {
		this.modifyUserAvatar = modifyUserAvatar;
	}
	public String getUserAvartarPath() {
		return userAvartarPath;
	}
	public void setUserAvartarPath(String userAvartarPath) {
		this.userAvartarPath = userAvartarPath;
	}
	
	public List<SocialPoiBean> getFilterSocialPoiBeans() {
		return filterSocialPoiBeans;
	}
	public Map<String, Integer> getFilterItems() {
		return filterItems;
	}
	@Override
	public String toString() {
		return "UserRegisterBean [userUID=" + userUID + ", userSID=" + userSID
				+ ", userMail=" + userMail + ", userId=" + userId
				+ ", userName=" + userName + ", userPassword=" + userPassword
				+ ", userNickname=" + userNickname + ", userGender="
				+ userGender + ", userBirthday=" + userBirthday
				+ ", userStatus=" + userStatus + ", userStars=" + userStars
				+ ", userFoot=" + userFoot + ", userSign=" + userSign
				+ ", userWeiboId=" + userWeiboId + ", userAddtime="
				+ userAddtime + ", userPoiItems=" + userPoiItems
				+ ", userVerify=" + userVerify + ", relation=" + relation
				+ ", visit=" + visit + ", loactionBean=" + loactionBean
				+ ", userDistance=" + userDistance + ", userLastTime="
				+ userLastTime + ", userOnlineTime=" + userOnlineTime
				+ ", userMsgTime=" + userMsgTime + ", userActTime="
				+ userActTime + ", userType=" + userType + ", userAvatar="
				+ userAvatar + ", snsAccounts=" + snsAccounts
				+ ", fill_profile=" + fill_profile + "]";
	}
	
}
