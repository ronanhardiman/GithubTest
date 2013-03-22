package com.igrs.tivic.phone.sqlite;

import java.util.List;


public interface ITIVIC {
	/**
	 * 获取EPG频道列表
	 * @return json数据的String形式
	 */
	public String getEPGChannelList(String currentDate,String channelId);
	
	/**
	 * 判断epg数据是否存在
	 * @param fileName 文件路径
	 * @return
	 */
	public boolean isExist(String filePath);
	
	/**
	 * 判断服务器的数据是否有更新
	 * @param storeTime 存储的时间
	 * @return
	 */
	public boolean isUpdate(String storeTime);
}
