package com.igrs.tivic.phone.Bean;

public class MarqueeBean {
	private int marquee_id;
	private int marquee_channel_id; 
	private int marquee_program_id;
	private String marquee_type;
	private String marquee_content;
	
	public int getMarquee_channel_id() {
		return marquee_channel_id;
	}
	public void setMarquee_channel_id(int marquee_channel_id) {
		this.marquee_channel_id = marquee_channel_id;
	}
	public int getMarquee_program_id() {
		return marquee_program_id;
	}
	public void setMarquee_program_id(int marquee_program_id) {
		this.marquee_program_id = marquee_program_id;
	}
	public int getMarquee_id() {
		return marquee_id;
	}
	public void setMarquee_id(int marquee_id) {
		this.marquee_id = marquee_id;
	}
	public String getMarquee_type() {
		return marquee_type;
	}
	public void setMarquee_type(String marquee_type) {
		this.marquee_type = marquee_type;
	}
	public String getMarquee_content() {
		return marquee_content;
	}
	public void setMarquee_content(String marquee_content) {
		this.marquee_content = marquee_content;
	}
}
