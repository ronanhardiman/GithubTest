package com.igrs.tivic.phone.Bean.EPG;
/**
 * 用于替换旧  EPGProgramBean
 */
public class EPGProgramsBean {
	private int channel_id;
	private String channel_name;
	private String hint;		//"每周一、三、五播出"
	//以上mytv/pg_list_all 接口 专用
	private int program_id;
	private String start_display;
	private String end_display;
	private String program_name;
	private String url_p43;			//海报URL 规格4x3
	private String url_p83;			//海报URL 规格8x3
	private String url_epg;			//EPG海报URL
	private String hotspot;			//"热点推荐词"
	
	private int count;		//用户访问节目的访问量   (在search/pghot_user_visit 接口中用到)
	private String play_period;	//编辑编写的一句话
	private boolean isCollection = false;
	
	private int hour;//播放的时间  只是“时”
	private int initSelectionIndex;
	private boolean isShowDelFlag;//是否显示删除按钮
	public boolean isFromEditer;	//是否来自编辑
	
	
	public boolean isFromEditer() {
		return isFromEditer;
	}
	public void setFromEditer(boolean isFromEditer) {
		this.isFromEditer = isFromEditer;
	}
	public boolean isShowDelFlag() {
		return isShowDelFlag;
	}
	public void setShowDelFlag(boolean isShowDelFlag) {
		this.isShowDelFlag = isShowDelFlag;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getInitSelectionIndex() {
		return initSelectionIndex;
	}
	public void setInitSelectionIndex(int initSelectionIndex) {
		this.initSelectionIndex = initSelectionIndex;
	}
	public boolean isCollection() {
		return isCollection;
	}
	public void setCollection(boolean isCollection) {
		this.isCollection = isCollection;
	}
	public String getPlay_period() {
		return play_period;
	}
	public void setPlay_period(String play_period) {
		this.play_period = play_period;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getChannel_id() {
		return channel_id;
	}
	public void setChannel_id(int channel_id) {
		this.channel_id = channel_id;
	}
	public String getChannel_name() {
		return channel_name;
	}
	public void setChannel_name(String channel_name) {
		this.channel_name = channel_name;
	}
	public String getHint() {
		return hint;
	}
	public void setHint(String hint) {
		this.hint = hint;
	}
	public int getProgram_id() {
		return program_id;
	}
	public void setProgram_id(int program_id) {
		this.program_id = program_id;
	}
	public String getStart_display() {
		return start_display;
	}
	public void setStart_display(String start_display) {
		this.start_display = start_display;
	}
	public String getEnd_display() {
		return end_display;
	}
	public void setEnd_display(String end_display) {
		this.end_display = end_display;
	}
	public String getProgram_name() {
		return program_name;
	}
	public void setProgram_name(String program_name) {
		this.program_name = program_name;
	}
	public String getUrl_p43() {
		return url_p43;
	}
	public void setUrl_p43(String url_p43) {
		this.url_p43 = url_p43;
	}
	public String getUrl_p83() {
		return url_p83;
	}
	public void setUrl_p83(String url_p83) {
		this.url_p83 = url_p83;
	}
	public String getUrl_epg() {
		return url_epg;
	}
	public void setUrl_epg(String url_epg) {
		this.url_epg = url_epg;
	}
	public String getHotspot() {
		return hotspot;
	}
	public void setHotspot(String hotspot) {
		this.hotspot = hotspot;
	}
}