package com.igrs.tivic.phone.Bean.UGC;
/**
 * 搜索节目
 * @author admin
 *
 */
public class SearchProgramBean {
	private String searchProgramsName;	//搜索的节目名或一部分    比如 非诚，必填，非空
	private String start_day;			//从什么时间开始，格式："2012-10-10", 
	private String end_day;				//到什么时间为止，格式："2012-11-11"
	public String getSearchProgramsName() {
		return searchProgramsName;
	}
	public void setSearchProgramsName(String searchProgramsName) {
		this.searchProgramsName = searchProgramsName;
	}
	public String getStart_day() {
		return start_day;
	}
	public void setStart_day(String start_day) {
		this.start_day = start_day;
	}
	public String getEnd_day() {
		return end_day;
	}
	public void setEnd_day(String end_day) {
		this.end_day = end_day;
	}
	

}
