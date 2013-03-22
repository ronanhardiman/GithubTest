package com.igrs.tivic.phone.Bean.Login;
/**
 * 
 * @author admin
 *
 */
public class UserPOIItemBean {
	
/*	成功
	{
	    "ret": 0,
	    "errcode": 0,
	    "msg": "ok",
	    "resp": {
	        "items": [
	            {
	                "icon": "",
	                "text": "星座",
	                "key": "astroid",
	                "value": "7"			// 0(白羊)~11(双鱼)
	            },
	            {
	                "icon": "",
	                "text": "年代",
	                "key": "age",
	                "value": "564334976"	//birthday 1970-1-1偏移量
	            },
	            {
	                "icon": "",
	                "text": "性别",
	                "key": "gender",
	                "value": "1"			//0：woman；1：man
	            },
	            {
	                "icon": "http://image.tivic.com/userdata/title/1352989398.png",
	                "text": "明星",
	                "key": "star_id",
	                "value": "1"			//titleId
	            },
	            {
	                "icon": "http://image.tivic.com/userdata/title/1352989419.png",
	                "text": "节目",
	                "key": "program_id",
	                "value": "2"			//titleId
	            },
	            {
	                "icon": "http://image.tivic.com/userdata/title/1352989514.png",
	                "text": "内容",
	                "key": "category_id",
	                "value": "6"			//titleId
	            },
	            {
	                "icon": "http://image.tivic.com/userdata/title/1353122653.jpg",
	                "text": "内容",
	                "key": "category_id",
	                "value": "18"			//titleId
	            },
	            {
	                "icon": "http://image.tivic.com/userdata/title/1353122720.jpg",
	                "text": "内容",
	                "key": "category_id",
	                "value": "20"			//titleId
	            }
	        ]
	    }
	}
	*注：明星，节目，内容0~3个item
	*
	*/
	public static final int STAR_TYPE = 0;//明星
	public static final int PROGRAM_TYPE = 1;//节目
	public static final int CATE_TYPE = 2;//内容
	private String poi_icon;
	private String poi_text;
	private String poi_key;
	private int poi_value;
	private boolean isChecked;
	private int poi_type;
	public int getPoi_type() {
		return poi_type;
	}
	public void setPoi_type(int poi_type) {
		this.poi_type = poi_type;
	}
	public String getPoi_icon() {
		return poi_icon;
	}
	public void setPoi_icon(String poi_icon) {
		this.poi_icon = poi_icon;
	}
	public String getPoi_text() {
		return poi_text;
	}
	public void setPoi_text(String poi_text) {
		this.poi_text = poi_text;
	}
	public String getPoi_key() {
		return poi_key;
	}
	public void setPoi_key(String poi_key) {
		this.poi_key = poi_key;
	}
	public int getPoi_value() {
		return poi_value;
	}
	public void setPoi_value(int poi_value) {
		this.poi_value = poi_value;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	
}
