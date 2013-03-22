package com.igrs.tivic.phone.Bean.UGC;

import com.igrs.tivic.phone.Bean.VisitBean;
import com.igrs.tivic.phone.Bean.Base.UserBean;

/*
				//官V
				"post_id" : 1055,		// 贴子ID
				"time" : 1353998492,	// 发贴时间
				"uid" : 4000000000,		// 发帖人
				"nick" : "刘德华",		// 最长16字
				"gender" : 1,			// 性别:0-女 1-男 2-未知
				"loc" : 				// 最近上报的LBS位置
				{
					"lat" : "",	// 经度，官V无效
					"lon" : ""	// 纬度，官V无效
				}
				"title" : "爱你们～～",	// 标题
				"image" : "http://testimagev2.b0.upaiyun.com/userdata/img/20121127/1353998491-52.jpg",	// 大图
				"text" : "今天下午赴云南彝良地震慰问演出",
				"up_count" : 0,				// 支持个数
				"is_up": 0,					// 0 支持过 不可点击 1 没有支持过 可点击
				"reply_count" : 0,			// 回复个数
				"is_top": 1,				// 1:置顶, 0:普通
				"official": 1,				// 1:官V,0:普通
				"official_url": "http://XXXXXX" //官V短链接,保留待用
			}
 */
public class UGCDataBean {
	
	private UserBean usrinfo;	
	private int tid; 
	private String title;
	private String content;
	private String imageUrl;
	private String time;	
	private int isTop; //帖子是否置顶?
	private int upCount; //顶总数
	private int isUp; //是否已经顶过
	private int replyContent; //评论总数
	private int official; //是否为官方微博抓取
	private String officail_url; //官V链接，暂时不用
	private VisitBean vBean;
	private String sign;	//用户签名
	
	
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public VisitBean getvBean() {
		return vBean;
	}
	public void setvBean(VisitBean vBean) {
		this.vBean = vBean;
	}
	public UserBean getUsrinfo() {
		return usrinfo;
	}
	public void setUsrinfo(UserBean usrinfo) {
		this.usrinfo = usrinfo;
	}
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
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
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int isTop() {
		return isTop;
	}
	public void setTop(int isTop) {
		this.isTop = isTop;
	}
	public int getUpCount() {
		return upCount;
	}
	public void setUpCount(int upCount) {
		this.upCount = upCount;
	}
	public int getIsUp() {
		return isUp;
	}
	public void setIsUp(int isUp) {
		this.isUp = isUp;
	}
	public int getReplyContent() {
		return replyContent;
	}
	public void setReplyContent(int replyContent) {
		this.replyContent = replyContent;
	}
	public int getIsTop() {
		return isTop;
	}
	public void setIsTop(int isTop) {
		this.isTop = isTop;
	}
	public int getOfficial() {
		return official;
	}
	public void setOfficial(int official) {
		this.official = official;
	}
	public String getOfficail_url() {
		return officail_url;
	}
	public void setOfficail_url(String officail_url) {
		this.officail_url = officail_url;
	}	

	
	
}
