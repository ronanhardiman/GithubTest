package com.igrs.tivic.phone.sqlite;

public interface FileStore {
	
	/**
	 * 把数据存入文件 
	 * @param filePath 存入的文件路径 
	 * @param fileName 存入的文件名,文件名由日期和频道号组成
	 * @param Data 存入的数据 
	 */
	public void putToFile(String filePath,String fileName,String data);
	
	/**
	 * 从文件中读取数据 
	 * @param filePath 文件路径
	 * @return 返回数据的String格式
	 */
	public String getFromFile(String filePath);

	
}
