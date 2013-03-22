package com.igrs.tivic.phone.Bean.PGC;

import java.util.ArrayList;

public class ContentPublishListBean {
	
	ArrayList<ContentPublishBean> publishList;
	int ifMore;  //0,1
	private int total; //本次获取回复总数(3.获取回复列表（新接口）/tieba/list_reply  中用到)
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public ArrayList<ContentPublishBean> getPublishList() {
		return publishList;
	}
	public void setPublishList(ArrayList<ContentPublishBean> publishList) {
		this.publishList = publishList;
	}
	public int getIfMore() {
		return ifMore;
	}
	public void setIfMore(int ifMore) {
		this.ifMore = ifMore;
	}
	
	

}
