package com.igrs.tivic.phone.Bean.Waterfall;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class SubpageContent implements Parcelable{
	private int content_id;					//对应的文章id	
	private String title;					//文章标题
	private String content_time;			//发布时间
	private String summary;					//简介
	private String source;					//文章来源
	private String media_type;				//文章的媒体类型:"image"(表示mediaUrl中的路径指向图片)，"text"(表示mediaUrl无用);
	private String media_url;				//文章的媒体链接 与media_type配合使用
	private String link;					//文章的外部链接（例如链接到淘宝等外部网站）
	private String content_tpye;			//文章类型: "news","tvfind","video"
	private ArrayList<String> articleList;	//三级页面链接
	
	private String videoImage;				//视频的海报
	private String top;						//是否为置顶文章：0表示非置顶，1表示置顶
	
	private Object linkobj;
	
	
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(content_id);
		dest.writeList(articleList);
		dest.writeString(content_tpye);
	}
	
	public static final Parcelable.Creator<SubpageContent> CREATOR = new Parcelable.Creator<SubpageContent>() {
		// 重写Creator
		@Override//实现从source中创建出类的实例
		public SubpageContent createFromParcel(Parcel source) {
			SubpageContent sc = new SubpageContent();
			sc.content_id = source.readInt();
			sc.articleList = source.readArrayList(ArrayList.class.getClassLoader());
			sc.content_tpye = source.readString();
			return sc;
		}
		@Override//创建一个类型为T，长度为size的数组。
		public SubpageContent[] newArray(int size) {
			return new SubpageContent[size];
		}
	};
	public ArrayList<String> getArticleList() {
		return articleList;
	}
	public void setArticleList(ArrayList<String> articleList) {
		this.articleList = articleList;
	}
	public Object getLinkObj() {
		return linkobj;
	}
	public void setLinkObj(Object link) {
		this.linkobj = link;
	}
	public int getContent_id() {
		return content_id;
	}
	public void setContent_id(int content_id) {
		this.content_id = content_id;
	}
	public String getContent_time() {
		return content_time;
	}
	public void setContent_time(String content_time) {
		this.content_time = content_time;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getContent_tpye() {
		return content_tpye;
	}
	public void setContent_tpye(String content_tpye) {
		this.content_tpye = content_tpye;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMedia_type() {
		return media_type;
	}
	public void setMedia_type(String media_type) {
		this.media_type = media_type;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getMedia_url() {
		return media_url;
	}
	public void setMedia_url(String media_url) {
		this.media_url = media_url;
	}
	public String getVideoImage() {
		return videoImage;
	}
	public void setVideoImage(String videoImage) {
		this.videoImage = videoImage;
	}
	public String getTop() {
		return top;
	}
	public void setTop(String top) {
		this.top = top;
	}
	@Override
	public String toString() {
		return "SubpageContent [title=" + title +", media_type=" + media_type +",summary=" + summary +
		",media_url=" + media_url + ",link=" + link +"]";
	}
}

