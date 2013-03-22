package com.igrs.tivic.phone.Bean;
/**
 * 响应基本 参数
 * @author admin
 *
 */
public class BaseResponseBean {
	private int ret;
	private int errcode;
	private String msg;
	private String resp;
	
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
	public String getResp() {
		return resp;
	}
	public void setResp(String resp) {
		this.resp = resp;
	}
	
}
