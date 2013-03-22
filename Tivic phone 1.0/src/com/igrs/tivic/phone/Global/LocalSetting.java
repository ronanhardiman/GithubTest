/**
 *COPYRIGHT NOTICE
 *Copyright (c) 2009～2012, IGRSlab   
 *All rights reserved.
 * @author wjf
 * @file LocalSetting.java
 * @brief TODO
 * @version 
 * @date 2012-5-24
 * 
 */
package com.igrs.tivic.phone.Global;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * 
 * 应用程序配置类：用于保存用户相关信息及设置
 *COPYRIGHT NOTICE
 *Copyright (c) 2009～2012, IGRSlab   
 *All rights reserved.
 * @author wjf
 * @file LocalSetting.java
 * @brief TODO
 * @version 
 * @date 2012-5-24
 * 
 */
public class LocalSetting {
	public final static String APP_CONFIG = "config";
	public static final String PUBLISH_TEXT_KEY = "publish_text_key";//评论 Text key
	public static final String PUBLISH_IMAGE_KEY = "publish_image_key";//评论image key
	private Context mContext;
	private static LocalSetting instance;
	private SharedPreferences igrsSharedPreference;
	public static LocalSetting getInstance(Context context){
			if(instance == null){
				instance = new LocalSetting();
				instance.mContext = context;
			}
//			instance.mContext = context;
		return instance;
	}
	
	/**
	 * 获取Preference设置
	 */
	public static SharedPreferences getSharedPreferences(Context context)
	{
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public String get(String key){
		Properties props = get();
		return (props != null) ? props.getProperty(key) : null;
	}
	
	public Properties get(){
		FileInputStream fis = null;
		Properties props = new Properties();
		try {
			File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
			fis = new FileInputStream(dirConf.getPath() + File.separator + APP_CONFIG);
			props.load(fis);
		} catch (Exception e) {
		}finally{
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return props;
		
	}
	
	public void set(String key,String value){
		Properties props = get();
		props.setProperty(key, value);
		setProps(props);
	}
	
	private void setProps(Properties props) {
		FileOutputStream fos = null;
		try {
			//把config建在(自定义)app_config的目录下
			File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
			File conf = new File(dirConf,APP_CONFIG);
			fos = new FileOutputStream(conf);
			
			props.store(fos, null);
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				fos.close();
			} catch (Exception e) {
			}
		}
	}

	public void init(Context context){
		if(igrsSharedPreference == null)igrsSharedPreference = context.getSharedPreferences("TivicPhonePreferences", Context.MODE_PRIVATE);	
	}
	
	/**
	 * 写到缓存文件
	 * @param key
	 * @param value string类型
	 */
	public void write(String key ,String value){
		 Editor editor = igrsSharedPreference.edit();
		 editor.putString(key, value);
		 editor.commit();
	}
	/**
	 * 写到缓存文件
	 * @param key
	 * @param value int类型
	 */
	public void write(String key ,int value){
		Editor editor = igrsSharedPreference.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	/**
	 * 写到缓存文件
	 * @param key
	 * @param value boolean类型
	 */
	public void write(String key, boolean value) {
		Editor editor = igrsSharedPreference.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	/**
	 * 从文件中读数据
	 * @param key
	 * @param defaultValue
	 * @return String类型
	 */
	public String read(String key,String defaultValue){
		return igrsSharedPreference.getString(key, defaultValue);
	}
	/**
	 * 从文件中读数据
	 * @param key
	 * @param defaultValue
	 * @return int类型
	 */
	public int read(String key,int defaultValue){
		return igrsSharedPreference.getInt(key,defaultValue);
	}
	/**
	 * 从文件中读数据
	 * @param key
	 * @param defaultValue
	 * @return boolean类型
	 */
	public boolean read(String key,Boolean defaultValue){
		return igrsSharedPreference.getBoolean(key,defaultValue);
	}
}
