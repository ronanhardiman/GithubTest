package com.igrs.tivic.phone.Bean.EPG;

import java.util.ArrayList;

public class EPGChannelListBean {
	private ArrayList<EPGChannelsBean> epgChannelsBeanList;
	public ArrayList<EPGChannelsBean> getEpgChannelsBeanList() {
		return epgChannelsBeanList;
	}
	public void setEpgChannelsBeanList(
			ArrayList<EPGChannelsBean> epgChannelsBeanList) {
		this.epgChannelsBeanList = epgChannelsBeanList;
	}
	
/*	1,获取频道列表信息接口，公共接口，无需登录
	pgc/channel_list

描述：获取所有频道信息，一次拿全所有数据，无分页

{
	"req":
	{
		
	}
}

成功
{
	"resp":{
		"channels": [
			{
				"id": 10,
				"name":"频道名称",
				"alias":",",         //别名,逗号分隔
				"sequence": 1        //优先级排序,值越小，优先级越高
				"live_urls":[        //直播列表 
					{
					"name": "CNTV",        //供应商名称
					"live_url":"",          //直播地址
					"download_url":""       //应用下载地址，比如PPTV的下载地址
					}
					......
				]
			},
			....
		]
	}
}*/
	
	
}
