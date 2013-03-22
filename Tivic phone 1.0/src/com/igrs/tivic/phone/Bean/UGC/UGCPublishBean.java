package com.igrs.tivic.phone.Bean.UGC;

/*
发布贴子
/tieba/post
{
    "ver": 1,
    "uid" : 52,
    "req" :
	{
		"pid" : 1,				// 所属栏目ID
		"photo" :                               // 可选
		{
			"type" : 0,			// 0-POST附件（文件名为'file'），1-图片URL
			"file_name" : "file",		// 0的情况 or
			"file_url" : "http://xx",	// 1的情况
		},
		"title" : "和还会见测试",		// 标题
		"text" : "她ui哈桑萨斯我为鱼肉",	// 用户选取的文字（必填）
		"sync_sina" : 0				// 转发新浪微博（可选）"sync_qq" : 0,	// 是否传QQ（一期不用做）	
	}
 

 */
public class UGCPublishBean {

	int pid;
	int uid;
	int type; //0-POST附件（文件名为'file'），1-图片URL
	String title;
	String content;
	String photoUrl;
	int syncSinaFlag;  //0,1
	
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	public int getSyncSinaFlag() {
		return syncSinaFlag;
	}
	public void setSyncSinaFlag(int syncSinaFlag) {
		this.syncSinaFlag = syncSinaFlag;
	}
	
	
	
	
}

