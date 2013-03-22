package com.igrs.tivic.phone.Bean.Social;

import com.igrs.tivic.phone.Bean.Base.UserBean;

public class SocialMessageBean {

	int uid; 		//好友id，用于私信首页
	int partner_id; //好友id
	int mid; 		//消息id，用于聊天记录
	int from;		//发送方id
	int to;  		//接收方id
	String text; 	//消息内容
	String imgUrl; 	//消息附图
	String time;	//消息时间
	int type;		//图片类型，0本地 1网络
	UserBean usrinfo;
	int countnew;	//该好友发的未读新消息个数
	
	public int getPartner_id() {
		return partner_id;
	}
	public void setPartner_id(int partner_id) {
		this.partner_id = partner_id;
	}
	public UserBean getUsrinfo() {
		return usrinfo;
	}
	public void setUsrinfo(UserBean usrinfo) {
		this.usrinfo = usrinfo;
	}
	
	private boolean isComeMsg;//true 为好友发的，false为自己发的
	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getMid() {
		return mid;
	}
	public void setMid(int mid) {
		this.mid = mid;
	}
	public int getFrom() {
		return from;
	}
	public void setFrom(int from) {
		this.from = from;
	}
	public int getTo() {
		return to;
	}
	public void setTo(int to) {
		this.to = to;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public boolean isComeMsg() {
		return isComeMsg;
	}
	public void setComeMsg(boolean isComeMsg) {
		this.isComeMsg = isComeMsg;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getCountnew() {
		return countnew;
	}
	public void setCountnew(int countnew) {
		this.countnew = countnew;
	}
	
	
}


	

