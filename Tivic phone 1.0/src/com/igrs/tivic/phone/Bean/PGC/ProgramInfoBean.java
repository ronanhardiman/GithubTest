package com.igrs.tivic.phone.Bean.PGC;

public class ProgramInfoBean {
	String name;
	String time;
	int channelid;
	int programid;
	String intrduce;
	String keys;		//待定
	String imgUrl;    //节目图片的URL
	int isFocus = 0;//0 未收藏  1已收藏
	
	public int getIsFocus() {
		return isFocus;
	}
	public void setIsFocus(int isFocus) {
		this.isFocus = isFocus;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getChannelid() {
		return channelid;
	}
	public void setChannelid(int channelid) {
		this.channelid = channelid;
	}
	public int getProgramid() {
		return programid;
	}
	public void setProgramid(int programid) {
		this.programid = programid;
	}
	public String getIntrduce() {
		return intrduce;
	}
	public void setIntrduce(String intrduce) {
		this.intrduce = intrduce;
	}
	public String getKeys() {
		return keys;
	}
	public void setKeys(String keys) {
		this.keys = keys;
	}
}
