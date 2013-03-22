package com.igrs.tivic.phone.Bean.Waterfall;

import java.util.ArrayList;
/**
 * 
 * @author admin
 *
 */
public class ProgramDetailForPager {
	
	private String publishTime;
	private ArrayList<ArrayList<ProgramDetailBean>> contentBlockList; //各个模块内容 	blockList
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public ArrayList<ArrayList<ProgramDetailBean>> getContentBlockList() {
		return contentBlockList;
	}
	public void setContentBlockList(
			ArrayList<ArrayList<ProgramDetailBean>> contentBlockList) {
		this.contentBlockList = contentBlockList;
	}
	
}
