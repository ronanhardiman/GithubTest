package com.igrs.tivic.phone.Bean.PGC;

import java.util.ArrayList;

public class ChartBean {
	int id;
	String title;
	ArrayList<ProgramInfoBean> programList;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public ArrayList<ProgramInfoBean> getProgramList() {
		return programList;
	}
	public void setProgramList(ArrayList<ProgramInfoBean> programList) {
		this.programList = programList;
	}
	
	
}
