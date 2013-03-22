package com.igrs.tivic.phone.Bean.UGC;

import java.util.ArrayList;

public class UGCListBean {

	private ArrayList<UGCDataBean> dataList;
	int count;		//返回帖子总数
	int ifMore;    //去掉该参数，由偏移量offset决定是否取下一页offset<count
	
	public ArrayList<UGCDataBean> getDataList() {
		return dataList;
	}
	public void setDataList(ArrayList<UGCDataBean> dataList) {
		this.dataList = dataList;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getIfMore() {
		return ifMore;
	}
	public void setIfMore(int ifMore) {
		this.ifMore = ifMore;
	}
	
	
}
