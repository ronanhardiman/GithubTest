package com.igrs.tivic.phone.Bean;
/*
		uid : 12位数字,	// USER ID
		sid : 字符串,	// SESSION
		ver : 1,		// 协议版本号
		udid: "设备ID" //设备唯一标识
		type: "iPad"    //设备类型
 */
public class SNSRequestBaseBean {
	int uid;
	String sid;
	String ver;
	String udid;
	String type;
	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getVer() {
		return ver;
	}
	public void setVer(String ver) {
		this.ver = ver;
	}
	public String getUdid() {
		return udid;
	}
	public void setUdid(String udid) {
		this.udid = udid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
