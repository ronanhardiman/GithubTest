package com.igrs.tivic.phone.Bean.PGC;
/*
 * 
|-- ret : 0
|-- errcode : 0
|-- msg : "ok"
|-- resp
|     |--publishTime 数据更新时间
|     |--contents
|     |      |
|     |      |--type  （内容类型）
|     |      |--title （视频标题）
|     |      |--source （视频来源）
|     |      |--date    （发布日期）
|     |      |--author  （发布作者）
|     |      |--poster  （视频海报）
|     |      |--video_url (视频地址)
 {
    "ret":0,
    "errcode":0,
    "msg":"ok",
    "resp":{
           "publishTime":"2012-11-27 13:47:49",
           "contents": {
                            "type"   : "video",
                            "title"  : "新闻标题",
                            "source" : "1",
                            "date"   : "日期",
                            "author" : "作者",
                            "poster" : "http://img.baidu.com/img/baike/pictures/r0s1g1.gif",
                            "video_url" : "http://10.1.33.49/test/test.mp4"
                        }
            }

 */
public class VideoBean {
	String type = "video";
	String title;
	int source;
	String date;
	String author;
	String posterUrl;
	String videoUrl;
	String publishTime;
	String id;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPosterUrl() {
		return posterUrl;
	}
	public void setPosterUrl(String posterUrl) {
		this.posterUrl = posterUrl;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	
}
