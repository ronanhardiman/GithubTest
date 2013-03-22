package com.igrs.tivic.phone.Bean.Login;

public class UserCheckinBean {
/*
 * 	"req" :
	{
		"photo" : 
		{
			"type" : 0,					// 0-POST附件,json数据后追加file/base64；1-图片URL
			"file_url" : "http://xx",	// 1-图片URL的情况下使用
		},
		"text" : "哈哈！TV客截屏，惊喜连连…",		// 用户选取的文字（可选），pad默认，用户不可修改
		"sync_sina" : 1,				// 转发新浪微博（可选）
	}
 */
	int type; //0-POST附件,json数据后追加file/base64；1-图片URL
	String photoUri; //file path or url
	String checkinText;
	int syncSina;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getPhotoUri() {
		return photoUri;
	}
	public void setPhotoUri(String photoUri) {
		this.photoUri = photoUri;
	}
	public String getCheckinText() {
		return checkinText;
	}
	public void setCheckinText(String checkinText) {
		this.checkinText = checkinText;
	}
	public int getSyncSina() {
		return syncSina;
	}
	public void setSyncSina(int syncSina) {
		this.syncSina = syncSina;
	}
	
	
}
