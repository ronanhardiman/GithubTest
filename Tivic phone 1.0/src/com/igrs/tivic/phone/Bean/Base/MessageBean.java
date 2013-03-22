package com.igrs.tivic.phone.Bean.Base;
/**
 * 通知(Notice) and 私信 (Messages)
 * @author admin
 *
 */
public class MessageBean {
	private String page_flag ;
	private String act_time;
	private String reqnum;
	
	private int offset ;  	//偏移量，取首页为0，翻页时取前一页最后一条记录+1：取值范围[0~200]
	private String time;
	
	private String uid;   	//对话伙伴
	private String op;   	//0只查询；1查询并设置所有未读消息为已读，且仅在page_flag=0时有效。
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public String getPage_flag() {
		return page_flag;
	}
	public void setPage_flag(String page_flag) {
		this.page_flag = page_flag;
	}
	public String getAct_time() {
		return act_time;
	}
	public void setAct_time(String act_time) {
		this.act_time = act_time;
	}
	public String getReqnum() {
		return reqnum;
	}
	public void setReqnum(String reqnum) {
		this.reqnum = reqnum;
	}
	
	
}
