package com.igrs.tivic.phone.Bean;
/*
 * 获取频道截图列表
 */
public class ScreenShotsBean {
	private String channel_id;	//频道ID
	private String timestamp;	//截取时间.可选,默认当前时间,13位数字精确到毫秒
	private int count;			//获取图片数目.可选,默认6张,以时间升序排列
	private String img_url;		//所截图片的url地址
	private String img_path;	//所截图片保存到本地的路径
	
	public String getChannel_id() {
		return channel_id;
	}
	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	public String getImg_path() {
		return img_path;
	}
	public void setImg_path(String img_path) {
		this.img_path = img_path;
	}
	
	
}
