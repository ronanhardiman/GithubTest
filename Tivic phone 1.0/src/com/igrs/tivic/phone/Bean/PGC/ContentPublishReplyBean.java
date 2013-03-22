package com.igrs.tivic.phone.Bean.PGC;

/*
/lanmu/reply
{
    "ver": 1,
    "uid": 53,
    "req": {
        "news_id": "651",             // 文章ID
	"text": "hello world",
	"photo" :                     // 可选
		{
			"type" : 0,			// 0-POST附件（文件名为'file'），1-图片URL
			"file_name" : "file",		// 0的情况 or
			"file_url" : "http://xx",	// 1的情况
		},
	"sync_sina" : 1,				// 转发新浪微博（可选）
    }
} 

 */
public class ContentPublishReplyBean {

	int uid;
	int contentId;	//文章ID
	String replyText;
	String photoUrl;
	int syncSinaFlag;  //0,1
	int type; //0-POST附件（文件名为'file'），1-图片URL
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getContentId() {
		return contentId;
	}
	public void setContentId(int contentId) {
		this.contentId = contentId;
	}
	public String getReplyText() {
		return replyText;
	}
	public void setReplyText(String replyText) {
		this.replyText = replyText;
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
