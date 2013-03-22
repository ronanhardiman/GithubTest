package com.igrs.tivic.phone.Bean.EPG;

public class EPGProgramBean {
	private int program_id;
	private String programe_title;
	private String programe_icon;  // 海报url(推荐节目用到)
	private String start_time;
	private String end_time;
	private String programe_detail;
	private String channel_id;
	private boolean is_collection = false;
	
	public String getPrograme_icon() {
		return programe_icon;
	}
	public void setPrograme_icon(String programe_icon) {
		this.programe_icon = programe_icon;
	}
	public int getProgram_id() {
		return program_id;
	}
	public void setProgram_id(int program_id) {
		this.program_id = program_id;
	}
	public String getPrograme_title() {
		return programe_title;
	}
	public void setPrograme_title(String programe_title) {
		this.programe_title = programe_title;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getPrograme_detail() {
		return programe_detail;
	}
	public void setPrograme_detail(String programe_detail) {
		this.programe_detail = programe_detail;
	}
	public String getChannel_id() {
		return channel_id;
	}
	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}
	public boolean isIs_collection() {
		return is_collection;
	}
	public void setIs_collection(boolean is_collection) {
		this.is_collection = is_collection;
	}
	
	

}
