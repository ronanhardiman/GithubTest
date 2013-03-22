package com.igrs.tivic.phone.Bean.Social;

import com.igrs.tivic.phone.Bean.LocationBean;
import com.igrs.tivic.phone.Bean.Base.UserBean;

public class WeiboBean {
	private UserBean userBean;
	private String userSID;  //sid由客户端保存，只有再次调用Login接口时由服务端更新
	private String sign;
	private String relative;
	private String visit_id;
	private String visit_title;
	private String visit_channelId; //频道id（可选）
	private String last_time;
	private int type; //新浪微博用户
	
	public String getVisit_channelId() {
		return visit_channelId;
	}
	public void setVisit_channelId(String visit_channelId) {
		this.visit_channelId = visit_channelId;
	}
	public UserBean getUserBean() {
		return userBean;
	}
	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}
	public String getUserSID() {
		return userSID;
	}
	public void setUserSID(String userSID) {
		this.userSID = userSID;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getRelative() {
		return relative;
	}
	public void setRelative(String relative) {
		this.relative = relative;
	}
	public String getVisit_id() {
		return visit_id;
	}
	public void setVisit_id(String visit_id) {
		this.visit_id = visit_id;
	}
	public String getVisit_title() {
		return visit_title;
	}
	public void setVisit_title(String visit_title) {
		this.visit_title = visit_title;
	}
	public String getLast_time() {
		return last_time;
	}
	public void setLast_time(String last_time) {
		this.last_time = last_time;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	
}
