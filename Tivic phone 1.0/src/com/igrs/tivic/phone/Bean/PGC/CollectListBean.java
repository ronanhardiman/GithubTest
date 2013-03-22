package com.igrs.tivic.phone.Bean.PGC;

import java.util.ArrayList;

/**
 * 收藏列表
 * @author admin
 *
 */
public class CollectListBean {
	private ArrayList<CollectBean> collectList;
	private int total;
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public ArrayList<CollectBean> getCollectList() {
		return collectList;
	}
	public void setCollectList(ArrayList<CollectBean> collectList) {
		this.collectList = collectList;
	}
	
}
