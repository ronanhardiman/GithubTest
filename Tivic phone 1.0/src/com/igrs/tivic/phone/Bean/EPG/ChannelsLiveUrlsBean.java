package com.igrs.tivic.phone.Bean.EPG;
/**
 * 直播列表
 * @author admin
 *
 */
public class ChannelsLiveUrlsBean {
	private String name;	//供应商名称
	private String liveUrl;	//直播地址
	private String downLoadUrl;	//应用下载地址，比如PPTV的下载地址
	
	public String getDownLoadUrl() {
		return downLoadUrl;
	}
	public void setDownLoadUrl(String downLoadUrl) {
		this.downLoadUrl = downLoadUrl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLiveUrl() {
		return liveUrl;
	}
	public void setLiveUrl(String liveUrl) {
		this.liveUrl = liveUrl;
	}
	
}
