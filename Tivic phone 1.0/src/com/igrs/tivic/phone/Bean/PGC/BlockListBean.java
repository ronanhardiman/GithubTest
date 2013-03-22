package com.igrs.tivic.phone.Bean.PGC;

import java.util.ArrayList;

public class BlockListBean {
	private int ifMore; //是否还有更多
	private int count;  //应该是列表总数，但和下面的记录对不上
	private ArrayList<CollectBean> collectBean;
	private int current_fav_time; //当前时间
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
	public ArrayList<CollectBean> getCollectBean() {
		return collectBean;
	}
	public void setCollectBean(ArrayList<CollectBean> collectBean) {
		this.collectBean = collectBean;
	}
	public int getCurrent_fav_time() {
		return current_fav_time;
	}
	public void setCurrent_fav_time(int current_fav_time) {
		this.current_fav_time = current_fav_time;
	}
	
	
}
