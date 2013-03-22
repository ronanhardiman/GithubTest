package com.igrs.tivic.phone.Bean.UGC;

import com.igrs.tivic.phone.Bean.Base.UserBean;

/*
			{
				"uid" : "52",			// 发起人
				"time" : "1353999210",		// 发起时间
				"nick" : "神奇",	        // 最长16字
				"gender" : "1",
				"loc" : 			// 最近上报的LBS位置
				{
					"lat" : "39.982486",	// 经度
					"lon" : "116.301675"					// 纬度
				}
				"image" : "",	                // 大图url
				"text" : "自己回复",
				"foot" : "713"                  // channelID and programID?
			}
 */
public class UGCReplyBean {
	
	private UserBean usrinfo;
	private String time;
	String imgUrl;
	String replyText;
	int channelId;
	int reply_id;
	int programId;
	int ppid; //暂时用不到 ，评论id
	int program_id;
	String program_name;
	String sign;	//用户签名
	boolean isVisitOpen = false;
	
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public int getProgram_id() {
		return program_id;
	}
	public void setProgram_id(int program_id) {
		this.program_id = program_id;
	}
	public String getProgram_name() {
		return program_name;
	}
	public void setProgram_name(String program_name) {
		this.program_name = program_name;
	}
	public int getReply_id() {
		return reply_id;
	}
	public void setReply_id(int reply_id) {
		this.reply_id = reply_id;
	}
	public UserBean getUsrinfo() {
		return usrinfo;
	}
	public void setUsrinfo(UserBean usrinfo) {
		this.usrinfo = usrinfo;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getReplyText() {
		return replyText;
	}
	public void setReplyText(String replyText) {
		this.replyText = replyText;
	}
	public int getChannelId() {
		return channelId;
	}
	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}
	public int getProgramId() {
		return programId;
	}
	public void setProgramId(int programId) {
		this.programId = programId;
	}
	public int getPpid() {
		return ppid;
	}
	public void setPpid(int ppid) {
		this.ppid = ppid;
	}
	
	public boolean isVisitOpen() {
		return isVisitOpen;
	}
	public void setVisitOpen(boolean isVisitOpen) {
		this.isVisitOpen = isVisitOpen;
	}
}
