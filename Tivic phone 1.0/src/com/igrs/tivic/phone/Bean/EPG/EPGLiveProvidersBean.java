package com.igrs.tivic.phone.Bean.EPG;

public class EPGLiveProvidersBean {
	private String provider_name;			//供应商名称
	private String provider_downloadUrl;	//下载地址,WEB直播页为空
	public String getProvider_name() {
		return provider_name;
	}
	public void setProvider_name(String provider_name) {
		this.provider_name = provider_name;
	}
	public String getProvider_downloadUrl() {
		return provider_downloadUrl;
	}
	public void setProvider_downloadUrl(String provider_downloadUrl) {
		this.provider_downloadUrl = provider_downloadUrl;
	}
	
}
