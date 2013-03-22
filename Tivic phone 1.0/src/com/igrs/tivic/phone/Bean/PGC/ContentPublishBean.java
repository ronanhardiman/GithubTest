package com.igrs.tivic.phone.Bean.PGC;

import com.igrs.tivic.phone.Bean.Base.UserBean;

/*
 {
    "ret": 0,
    "errcode": 0,
    "msg": "ok",
    "resp": {
        "more": 0,                              // 是否还有更多
        "results": [
            {
                "uid": "53",                      // 发起人id
                "time": "1354088060",             // 发起时间
                "nick": "神奇",                   // 昵称
                "gender": "1",                    // 性别，1 为男 0为女
                "loc": {
                    "lat": "39.982161767984",  // 经度
                    "lon": "116.30200866053"  // 纬度
                },
                "image": "http://testimagev2.b0.upaiyun.com/userdata/img/20121115/1352970218-7.jpg",// 大图url
                "text": "地方主义国会要闻"        // 评论内容
            },
    		...
        ]
    }
}
 */
public class ContentPublishBean {

	UserBean usrinfo;
	private int reply_id;	//评论id
	String startTime;
	String publishText;
	String publishImage;
	private String foot;	//回帖人最后一次访问的节目id
	private String channelid;	//节目关联频道id, 0-没有关联频道
	private String programName;
	private String sign;		//用户签名
	private boolean isVisitOpen = false;
	
	
	public boolean isVisitOpen() {
		return isVisitOpen;
	}
	public void setVisitOpen(boolean isVisitOpen) {
		this.isVisitOpen = isVisitOpen;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public int getReply_id() {
		return reply_id;
	}
	public void setReply_id(int reply_id) {
		this.reply_id = reply_id;
	}
	public String getFoot() {
		return foot;
	}
	public void setFoot(String foot) {
		this.foot = foot;
	}
	public String getChannelid() {
		return channelid;
	}
	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}
	public UserBean getUsrinfo() {
		return usrinfo;
	}
	public void setUsrinfo(UserBean usrinfo) {
		this.usrinfo = usrinfo;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getPublishText() {
		return publishText;
	}
	public void setPublishText(String publishText) {
		this.publishText = publishText;
	}
	public String getPublishImage() {
		return publishImage;
	}
	public void setPublishImage(String publishImage) {
		this.publishImage = publishImage;
	}
	
	
}
