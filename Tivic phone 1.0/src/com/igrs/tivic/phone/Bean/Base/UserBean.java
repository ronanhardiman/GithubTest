package com.igrs.tivic.phone.Bean.Base;
import com.igrs.tivic.phone.Bean.LocationBean;
import com.igrs.tivic.phone.Bean.VisitBean;

public class UserBean {
	private int userID;
	private String sId;
	private int relation;      //0-陌生人，1-拉黑，2-粉丝，3-我关注对方，4-双向关注
	private String userNickName;
	private int userGender;    // 性别，1 为男 0为女
	private String userImage;
	private String userBirthday;
	private String userSign;
	private LocationBean usrLocation;
	private VisitBean visit;
	private String last_time;
	
	public String getsId() {
		return sId;
	}
	public void setsId(String sId) {
		this.sId = sId;
	}
	public int getRelation() {
		return relation;
	}
	public void setRelation(int relation) {
		this.relation = relation;
	}
	public String getUserBirthday() {
		return userBirthday;
	}
	public void setUserBirthday(String userBirthday) {
		this.userBirthday = userBirthday;
	}
	public String getUserSign() {
		return userSign;
	}
	public void setUserSign(String userSign) {
		this.userSign = userSign;
	}
	public String getLast_time() {
		return last_time;
	}
	public void setLast_time(String last_time) {
		this.last_time = last_time;
	}
	public VisitBean getVisit() {
		return visit;
	}
	public void setVisit(VisitBean visit) {
		this.visit = visit;
	}
	public int getUid() {
		return userID;
	}
	public void setUid(int userID) {
		this.userID = userID;
	}

	public String getUserNickName() {
		return userNickName;
	}
	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}
	public String getUserImage() {
		return userImage;
	}
	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

	public int getUserGender() {
		return userGender;
	}
	public void setUserGender(int userSex) {
		this.userGender = userSex;
	}

	public LocationBean getUsrLocation() {
		return usrLocation;
	}
	public void setUsrLocation(LocationBean usrLocation) {
		this.usrLocation = usrLocation;
	}
}
