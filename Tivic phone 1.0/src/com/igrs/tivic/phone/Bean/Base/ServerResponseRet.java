package com.igrs.tivic.phone.Bean.Base;

/*
 
  //成功
{
  "ret": 0,
    "errcode": 0,
    "msg": "ok",
    "resp": {
	"reply_count": "5"               //回贴的数量
}
    
    //失败
{
    "ret": 2,
    "errcode": 0,
    "msg": "回复帖子失败！",
    "resp": ""
}
 */

public class ServerResponseRet {
	int ret;
	int errorcode;
	String msg;
	String  resp;
	public int getRet() {
		return ret;
	}
	public void setRet(int ret) {
		this.ret = ret;
	}
	public int getErrorcode() {
		return errorcode;
	}
	public void setErrorcode(int errorcode) {
		this.errorcode = errorcode;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getResp() {
		return resp;
	}
	public void setResp(String resp) {
		this.resp = resp;
	}
	
}
