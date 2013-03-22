package com.igrs.tivic.phone.Bean.PGC;

import java.util.ArrayList;

/*
 * |-- errcode : 0
|-- msg : "ok"
|-- resp
|     |--publishTime 数据更新时间
|     |--contents
|     |      |
|     |      |--type  （内容类型）
|     |      |--title （新闻标题）
|     |      |--source （新闻来源）
|     |      |--date    （发布日期）
|     |      |--author  （发布作者）
|     |      |--paragraphs（文章的段落数组）
|     |      |    |--item (无该标签，这里仅用来表示一个数组元素)
|     |      |    |    |-- type   （段落内容类型）
|     |      |    |    |-- content（段落内容：图片路径或者文字内容）



news内容实例：
-----------------------------------------------
{
    "ret":0,
    "errcode":0,
    "msg":"ok",
    "resp":{
           "publishTime":"2012-11-27 13:47:49",
           "contents": {
                            "type"   : "news",
                            "title"  : "新闻标题",
                            "source" : "新闻来源",
                            "date"   : "日期",
                            "author" : "作者",
                            "paragraphs" : [
                                            {
                                                type : "txt",
                                                content : "文章的一个段落",
                                            },
                                            {
                                                type : "img",
                                                content : "http://img.baidu.com/img/baike/pictures/r0s1g1.gif"
                                            },
                                            {
                                                type : "txt",
                                                content : "文章的一个段落",
                                            }
                                        ]
                        }
            }
}
 */
public class NewsBean {
	String id;
	String publishTime;
	String type = "news";
	String title;
	String source;
	String date;
	ArrayList<ParagraphBean> paragraphList;
	
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ArrayList<ParagraphBean> getParagraphList() {
		return paragraphList;
	}

	public void setParagraphList(ArrayList<ParagraphBean> paragraphList) {
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
