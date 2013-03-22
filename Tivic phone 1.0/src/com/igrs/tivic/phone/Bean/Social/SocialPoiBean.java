package com.igrs.tivic.phone.Bean.Social;

public class SocialPoiBean {
	private int type;
	private String url;
	private int value;
	private boolean isChecked;
	public static final int AGE_TYPE = 0;
	public static final int CONSTELLATION_TYPE = 1;
	public static final int GENDER_TYPE = 2;
	public static final int STAR_TYPE = 3;
	public static final int PROGRAM_TYPE = 4;
	public static final int CATEGORY_TYPE = 5;
	public static final int SINAWEIBO_TYPE = 6;
	public SocialPoiBean() {
		super();
	}
	public SocialPoiBean(int type, String url) {
		super();
		this.type = type;
		this.url = url;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	
}
