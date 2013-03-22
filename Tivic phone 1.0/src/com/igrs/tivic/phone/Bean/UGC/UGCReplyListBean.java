package com.igrs.tivic.phone.Bean.UGC;

import java.util.ArrayList;

public class UGCReplyListBean {
	
	private ArrayList<UGCReplyBean> dataList;
	int ifMore;
	int count;
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public ArrayList<UGCReplyBean> getDataList() {
		return dataList;
	}
	public void setDataList(ArrayList<UGCReplyBean> dataList) {
		this.dataList = dataList;
	}
	public int getIfMore() {
		return ifMore;
	}
	public void setIfMore(int ifMore) {
		this.ifMore = ifMore;
	}
	
		
}
