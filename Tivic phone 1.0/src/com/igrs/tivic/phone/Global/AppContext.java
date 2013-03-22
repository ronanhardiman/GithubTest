package com.igrs.tivic.phone.Global;

import java.io.File;
import java.util.HashMap;

import org.apache.http.impl.client.DefaultHttpClient;

import com.igrs.tivic.phone.Utils.HttpClientUtils;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.HttpResponseCache;
import android.util.Log;
/**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 * @author admin
 *
 */
public class AppContext extends Application {
	public static AppContext instance;
//	public static AppContext getInstance() {
//		if (instance == null) {
//			instance = new AppContext();
//		}
//		return instance;
//	}
	private HashMap<String, String> collectionMap = new HashMap<String, String>();
	
	/**
	 * 检测网络是否可用
	 * @return
	 */
	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		enableHttpResponseCache();
		instance = this;
	}
	/**
	 * 开启httpurlconnnection 的get 缓存.
	 */
	private void enableHttpResponseCache() {
		try {
			long httpCacheSize = 10 * 1024 * 1024;//10MB
			File httpCacheDir = new File(getCacheDir(), "http");
//			Log.i("lq", "getCacheDir() : " + getCacheDir().toString());
			Class.forName("android.net.http.HttpResponseCache")
					.getMethod("install", File.class, long.class)
					.invoke(null, httpCacheDir, httpCacheSize);
		} catch (Exception e) {
			Log.e("===>", e.getMessage(), e);
		}
	}
	
	public void onStopHttpResponseCache(){
		HttpResponseCache cache = HttpResponseCache.getInstalled();
		if (cache != null) {
			cache.flush();
		}
	}
	
	public void putCollection(String key,String value){
		collectionMap.put(key, value);
	}
	public void removeCollection(String key){
		collectionMap.remove(key);
	}
	public void setCollection(HashMap<String, String> collectionMap){
		this.collectionMap.clear();
		this.collectionMap.putAll(collectionMap);
	}
	public HashMap<String, String> getCollectionMap(){
		return collectionMap;
	}
	public void setProperty(String key,String value){
		LocalSetting.getInstance(this).set(key, value);
	}

	public String getProperty(String temlKey) {
		return LocalSetting.getInstance(this).get(temlKey);
	}
}
