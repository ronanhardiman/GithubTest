package com.igrs.tivic.phone.Interface;

import com.igrs.tivic.phone.Bean.PGC.NewsBean;
import com.igrs.tivic.phone.Bean.PGC.PushTVBean;
import com.igrs.tivic.phone.Bean.PGC.TVFindBean;
import com.igrs.tivic.phone.Bean.PGC.VideoBean;

public interface IContent {
	public NewsBean getNewsBean(String url);
	public PushTVBean getPushTVBean(String url);
	public TVFindBean getTVFindBean(String url);
	public VideoBean getVideoBean(String url);
	
	public void getNews(String url,int index);	//三级页面 news
	public void getTvFind(String url,int index);	//三级页面 Tvfind
	public void getVideo(String url,int index);	//三级页面 video
	public void getPushTv(String url); // 榜单
	public void getWaterFall(String url);	//瀑布流
}
