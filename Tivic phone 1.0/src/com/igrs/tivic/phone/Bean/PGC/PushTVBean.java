package com.igrs.tivic.phone.Bean.PGC;

import java.util.ArrayList;

/*
 * |-- ret : 0
|-- errcode : 0
|-- msg : "ok"
|-- resp
|     |--publishTime 数据更新时间
|     |--contents
|     |      |--item(一条榜单记录)
|     |      |    |--id      （榜单的id）
|     |      |    |--title   （榜单的标题)
|     |      |    |--programs（榜单中的节目）
|     |      |    |     |--item(某个榜单中的一个节目记录)
|     |      |    |     |   |--programId   (节目id)
|     |      |    |     |   |--channelId   (频道id)
|     |      |    |     |   |--name        (节目名称)
|     |      |    |     |   |--title       (节目时间)
|     |      |    |     |   |--introduce   (节目介绍)
|     |      |    |     |   |--keys       （预留关键字：例如收视率10%）
 */

public class PushTVBean {
	String publishTime;
	ArrayList<ChartBean> chart;
	public ArrayList<ChartBean> getChart() {
		return chart;
	}
	public void setChart(ArrayList<ChartBean> chart) {
		this.chart = chart;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
}

