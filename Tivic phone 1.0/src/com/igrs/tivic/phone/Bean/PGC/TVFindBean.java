package com.igrs.tivic.phone.Bean.PGC;

import java.util.ArrayList;

/*
 * |-- ret : 0
|-- errcode : 0
|-- msg : "ok"
|-- resp
|     |--publishTime 数据更新时间
|     |--contents
|     |      |
|     |      |--type      （内容类型）
|     |      |--date      （发布日期）
|     |      |--author    （发布作者）
|     |      |--paragraphs (find的内容)
|     |      |   |--item      ( tvfind内容数组中的一个元素)
|     |      |    |-- title     (标题)
|     |      |    |-- imgs      (图片路径的数组)
|     |      |    |-- label     (简介的标签)
|     |      |    |-- introduce（引言或者商品的介绍）
|     |      |    |-- price     (商品价格)
|     |      |    |-- link      ( 商品的链接)
|     |      |--recommend (推荐的tvfind)
|     |      |  |--item     (推荐的tvfind中的一个元素)
|     |      |    |-- focusImage (tvfind的焦点图)
|     |      |    |-- link       (tvfind的链接)
 */
public class TVFindBean {
	String publishTime;
	String type = "tvfind";
	String date;
//	String author;
	String id;	//tvfind id
	ArrayList<TVFindParagraphBean> paragraphList;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ArrayList<TVFindParagraphBean> getParagraphList() {
		return paragraphList;
	}

	public void setParagraphList(ArrayList<TVFindParagraphBean> paragraphList) {
		this.paragraphList = paragraphList;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

//	public String getAuthor() {
//		return author;
//	}
//
//	public void setAuthor(String author) {
//		this.author = author;
//	}
}
