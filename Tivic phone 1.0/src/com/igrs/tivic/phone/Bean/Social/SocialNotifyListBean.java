/**
 * 
 */
package com.igrs.tivic.phone.Bean.Social;

import java.util.ArrayList;

/**
 * @author kevin
 *
 */
public class SocialNotifyListBean {
	
	private  ArrayList<SocialNotifyBean> notifyList; 
	int ifMore;
	int count;
	
	public ArrayList<SocialNotifyBean> getNotifyList() {
		return notifyList;
	}
	public void setNotifyList(ArrayList<SocialNotifyBean> notifyList) {
		this.notifyList = notifyList;
	}
	public int getIfMore() {
		return ifMore;
	}
	public void setIfMore(int ifMore) {
		this.ifMore = ifMore;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}


	
}
