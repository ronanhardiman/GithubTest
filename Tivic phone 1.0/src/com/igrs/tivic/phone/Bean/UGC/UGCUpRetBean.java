package com.igrs.tivic.phone.Bean.UGC;
/*
 //成功返回	
{
    "ret": 0,
    "errcode": 0,
    "msg": "ok",
    "resp":
	{
		"up_count" : "1",				// 支持后返回数量更新
	}
}
//失败
{
    "ret" : 2,
    "errcode" : 0,
    "msg" : "不能重复支持",
    "resp": ""
}
//这种是参数空
{
    "ret" : 1,
    "errcode" : 0,
    "msg" : "tid不存在！",
    "resp": ""
}
 */
public class UGCUpRetBean {
	
	int ret;
	int errcode;
	String msg;
	int upCount;
	
	public int getRet() {
		return ret;
	}
	public void setRet(int ret) {
		this.ret = ret;
	}
	public int getErrcode() {
		return errcode;
	}
	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getUpCount() {
		return upCount;
	}
	public void setUpCount(int upCount) {
		this.upCount = upCount;
	}
	
	
}
