package com.igrs.tivic.phone.Bean.UGC;
/*
 返回的参数好多，真的用的着吗？
 	{
		"time" : "1353998492",			// 时间
		"title" : "和还会见测试",               //标题
		"text" : "她ui哈桑萨斯我为鱼肉",			// 文本
		"img" : "/userdata/img/20121127/1353998491-52.jpg",	// 图片URL
	} 
 */
public class UGCPublishRetBean {
	String time;
	String title;
	String content;
	String imgUrl;
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
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
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
	
}
