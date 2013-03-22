package com.igrs.tivic.phone.Bean.PGC;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class CollectBean implements Parcelable{
	
	//成功
	/*{
	    "ret": 0,
	    "errcode": 0,
	    "msg": "ok",
	    "resp": {
	        "article": {                             // 被收藏的文章
	            "fav_time": 1354080636,              // 收藏时间   (这里是数值类型)
	            "id": "569",
	            "title": "远方的家3--余少群《我们约会吧》难逃刘亦菲 爱情主张不要白富美",
	            "summary": "余少群《我们约会吧》难逃刘亦菲 爱情主张不要白富美",
	            "source": "厦门热线",                // 文章来源
	            "mediaType": "text",
	            "mediaUrl": "",
	            "type": "news",
	            "time": 2012,
	            "link": "",
	            "articleList": 0,
	            "playTime": "",
	            "videoImage": "",
	            "mask": "",
	            "top": ""
	        }
	    }
	}*/
	
	private String format;  // 布局 本字段字符串类型 (这里0没变成字符串)
	private long fav_time; //收藏时间   (这里是数值类型) (用于收藏)
	private String time;   // 文本时间(用于收藏列表的获取)
	private String fav_id;
	private String article_title;
	private String article_summary;
	private String article_source;  // 文章来源
	private String article_mediaType;
	private String article_mediaUrl;
	private String article_type;
	private String article_time;
	private String article_link;
	private ArrayList<String> articleList;
	private String article_playTime;
	private String videoImage;
	private int article_mask;	//标示三级页面最后一页评论 是全屏幕还是半屏
	private int article_top;	//标示置顶 1 为置顶
	
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(fav_id);
		dest.writeList(articleList);
		dest.writeString(article_type);
	}
	
	public static final Parcelable.Creator<CollectBean> CREATOR = new Parcelable.Creator<CollectBean>() {
		// 重写Creator
		@Override//实现从source中创建出类的实例
		public CollectBean createFromParcel(Parcel source) {
			CollectBean cb = new CollectBean();
			cb.fav_id = source.readString();
			cb.articleList = source.readArrayList(ArrayList.class.getClassLoader());
			cb.article_type = source.readString();
			return cb;
		}
		@Override//创建一个类型为T，长度为size的数组。
		public CollectBean[] newArray(int size) {
			return new CollectBean[size];
		}
	};
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public ArrayList<String> getArticleList() {
		return articleList;
	}
	public void setArticleList(ArrayList<String> articleList) {
		this.articleList = articleList;
	}
	public Long getFav_time() {
		return fav_time;
	}
	public void setFav_time(Long fav_time) {
		this.fav_time = fav_time;
	}
	public String getFav_id() {
		return fav_id;
	}
	public void setFav_id(String fav_id) {
		this.fav_id = fav_id;
	}
	public String getArticle_title() {
		return article_title;
	}
	public void setArticle_title(String article_title) {
		this.article_title = article_title;
	}
	public String getArticle_summary() {
		return article_summary;
	}
	public void setArticle_summary(String article_summary) {
		this.article_summary = article_summary;
	}
	public String getArticle_source() {
		return article_source;
	}
	public void setArticle_source(String article_source) {
		this.article_source = article_source;
	}
	public String getArticle_mediaType() {
		return article_mediaType;
	}
	public void setArticle_mediaType(String article_mediaType) {
		this.article_mediaType = article_mediaType;
	}
	public String getArticle_mediaUrl() {
		return article_mediaUrl;
	}
	public void setArticle_mediaUrl(String article_mediaUrl) {
		this.article_mediaUrl = article_mediaUrl;
	}
	public String getArticle_type() {
		return article_type;
	}
	public void setArticle_type(String article_type) {
		this.article_type = article_type;
	}
	public String getArticle_time() {
		return article_time;
	}
	public void setArticle_time(String article_time) {
		this.article_time = article_time;
	}
	public String getArticle_link() {
		return article_link;
	}
	public void setArticle_link(String article_link) {
		this.article_link = article_link;
	}
	public String getArticle_playTime() {
		return article_playTime;
	}
	public void setArticle_playTime(String article_playTime) {
		this.article_playTime = article_playTime;
	}
	public String getVideoImage() {
		return videoImage;
	}
	public void setVideoImage(String videoImage) {
		this.videoImage = videoImage;
	}
	public int getArticle_mask() {
		return article_mask;
	}
	public void setArticle_mask(int article_mask) {
		this.article_mask = article_mask;
	}
	public int getArticle_top() {
		return article_top;
	}
	public void setArticle_top(int article_top) {
		this.article_top = article_top;
	}
	
	
}
