package com.igrs.tivic.phone.Bean.Social;

public class SocialNotifyBean {

	//通知类型{Const.Social_Notify_Type_System,Const.Social_Notify_Type_Status,Const.Social_Notify_Type_Share}
	//int notifyType; 	//目前只支持系统通知 和 好友状态变更
	int notice_id;		//通知id，可选，仅在翻页时有效。	
	int statusType;		// 0 表示关注了你  1 表示取消关注了你  2 表示拉黑了你  3 表示取消拉黑了你  4 系统
	int uid; 			//该好友id
	String nickName; 	//好友昵称
	int gender;		 	//好友性别
	String updateTime; 	//通知时间	
	String content; 	//通知内容
	
	public int getNotice_id() {
		return notice_id;
	}
	public void setNotice_id(int notice_id) {
		this.notice_id = notice_id;
	}
//	public int getNotifyType() {
//		return notifyType;
//	}
//	public void setNotifyType(int notifyType) {
//		this.notifyType = notifyType;
//	}
	public int getStatusType() {
		return statusType;
	}
	public void setStatusType(int statusType) {
		this.statusType = statusType;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
	
}
