package com.igrs.tivic.phone.Bean.Waterfall;

import java.util.ArrayList;

public class WaterfallAdapterData {

	private ArrayList<ItemBean> itemBeans;
	private ArrayList<ProgramDetailBean> sendToContentActivityData;
	
	public ArrayList<ItemBean> getItemBeans() {
		return itemBeans;
	}
	public void setItemBeans(ArrayList<ItemBean> itemBeans) {
		this.itemBeans = itemBeans;
	}
	public ArrayList<ProgramDetailBean> getSendToContentActivityData() {
		return sendToContentActivityData;
	}
	public void setSendToContentActivityData(
			ArrayList<ProgramDetailBean> sendToContentActivityData) {
		this.sendToContentActivityData = sendToContentActivityData;
	}
}
