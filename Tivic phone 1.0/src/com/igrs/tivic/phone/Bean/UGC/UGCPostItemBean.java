package com.igrs.tivic.phone.Bean.UGC;

import com.igrs.tivic.phone.Bean.Base.UserBean;

public class UGCPostItemBean {
	
	

	/*成功返回
	{
	    "ret": 0,
	    "errcode": 0,
	    "msg": "ok",
	    "resp":
		{
			"more" : 0,				//1,还有更多;0,没有更多
			"count" : 10,			//本次获取贴子总数
			"items" :
			[
				{
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
				},

				{
					//普通用户贴
					"post_id" : 21,				// 贴子ID
					"time" : 1353998492,		// 发贴时间
					"uid" : 52,					// 发起人
					"nick" : "阿宗",	        // 最长16字
					"gender" : 1,				// 性别
					"loc" : 					// 最近上报的LBS位置
					{
						"lat" : "39.9821477",	// 经度
						"lon" : "116.3019744"	// 纬度
					}
					"title" : "和还会见测试",	// 标题
					"image" : "http://testimagev2.b0.upaiyun.com/userdata/img/20121127/1353998491-52.jpg",	// 大图
					"text" : "她ui哈桑萨斯我为鱼肉",
					"up_count" : 0,				// 支持个数
					"is_up": 1,                 // 0 支持过 不可点击 1 没有支持过 可点击
					"reply_count" : 1,			// 回复个数
					"is_top": 0,				// 1:置顶, 0:普通
					"official": 0,				// 1:官V,0:普通
				}
			]
		}
	}*/
	
	private int post_id;
	private String time;			// 发贴时间
	private UserBean uBean;
	private String title;			//标题
	private String image;			//大图
	private int up_count;			// 支持个数
	private int is_up;				// 0 支持过 不可点击 1 没有支持过 可点击
	private int reply_count;		// 回复个数
	private int is_top;				// 1:置顶, 0:普通
	private int official;			// 1:官V,0:普通
	private String official_url;	//官V短链接,保留待用
	public int getPost_id() {
		return post_id;
	}
	public void setPost_id(int post_id) {
		this.post_id = post_id;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public UserBean getuBean() {
		return uBean;
	}
	public void setuBean(UserBean uBean) {
		this.uBean = uBean;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public int getUp_count() {
		return up_count;
	}
	public void setUp_count(int up_count) {
		this.up_count = up_count;
	}
	public int getIs_up() {
		return is_up;
	}
	public void setIs_up(int is_up) {
		this.is_up = is_up;
	}
	public int getReply_count() {
		return reply_count;
	}
	public void setReply_count(int reply_count) {
		this.reply_count = reply_count;
	}
	public int getIs_top() {
		return is_top;
	}
	public void setIs_top(int is_top) {
		this.is_top = is_top;
	}
	public int getOfficial() {
		return official;
	}
	public void setOfficial(int official) {
		this.official = official;
	}
	public String getOfficial_url() {
		return official_url;
	}
	public void setOfficial_url(String official_url) {
		this.official_url = official_url;
	}
	
	
	
}
