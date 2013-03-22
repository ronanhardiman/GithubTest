package com.igrs.tivic.phone.Bean.Social;

/**
 * 私信
 * @author admin
 *
 */
public class SocialMsgBean {
	private String uid;	//发信人id
	private int count;	//相关的私信数量
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}
