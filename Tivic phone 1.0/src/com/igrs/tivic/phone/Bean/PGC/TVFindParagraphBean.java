package com.igrs.tivic.phone.Bean.PGC;

import java.util.ArrayList;

public class TVFindParagraphBean {
	String title;
	String lable;	//引言 或者商品描述
	String introduce;
	String price;
	String linkUrl;
	ArrayList<String> imgUrl;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLable() {
		return lable;
	}
	public void setLable(String lable) {
		this.lable = lable;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public ArrayList<String> getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(ArrayList<String> imgUrl) {
		this.imgUrl = imgUrl;
	}

}
