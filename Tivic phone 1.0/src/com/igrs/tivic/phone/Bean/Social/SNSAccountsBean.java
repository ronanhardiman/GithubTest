package com.igrs.tivic.phone.Bean.Social;

public class SNSAccountsBean {
	//绑定的新浪微博
	
	private String AccountsType;  //???数据库里为0     //新浪微博固定0
	private String AccessKey;     //数据库里无此字段
	private String AccessToken;   //"2.006wG7JCJlPvQB22dfcdcb343jVjQE",
	private String Uniqid;        //sns_id   新浪微博用户唯一标识
	public String getAccountsType() {
		return AccountsType;
	}
	public void setAccountsType(String accountsType) {
		AccountsType = accountsType;
	}
	public String getAccessKey() {
		return AccessKey;
	}
	public void setAccessKey(String accessKey) {
		AccessKey = accessKey;
	}
	public String getAccessToken() {
		return AccessToken;
	}
	public void setAccessToken(String accessToken) {
		AccessToken = accessToken;
	}
	public String getUniqid() {
		return Uniqid;
	}
	public void setUniqid(String uniqid) {
		Uniqid = uniqid;
	}
	
	
}
