package com.igrs.tivic.phone.sqlite;

import java.io.File;

import android.content.Context;

import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.LocalSetting;
import com.igrs.tivic.phone.Global.URLConfig;
import com.igrs.tivic.phone.Utils.HttpClientUtils;
import com.igrs.tivic.phone.Utils.ModelUtil;

public class TIVICImpl implements ITIVIC {
	private Context context;
	LocalSetting localSetting = null;
	private FileStoreImpl fileStoreImpl;
	
	public TIVICImpl(Context context) {
		super();
		this.context = context;
		localSetting = LocalSetting.getInstance(context);
		localSetting.init(context);
		fileStoreImpl = new FileStoreImpl(context);
	}

	@Override
	public String getEPGChannelList(String currentDate,
			String channelId) {
		
		File dirEPG = new File(Const.EPG_STORE_PATH);
		//文件名的格式为 ：20121126_22  日期_频道号 
		String filename = currentDate+"_"+channelId+".html";
		File file = new File(dirEPG, filename);
		String data = null;
		if(!dirEPG.exists())
		{
			dirEPG.mkdir();
		}
		try{
			//根据文件名查询该文件是否存在
			if(!file.exists()){//文件不存在，本地没有相应数据,新建文件，并下载数据
				file.createNewFile();
				//TODO:从服务器获取数据，并存入文件
				String serverPath = URLConfig.epgUrl+"today";  //epg地址
				data = getEPGDataFromServer(serverPath, filename);
				
			}else{//文件数据存在，检查是否需要更新
				//获取对应日期和频道数据的存储时间
				String storeTime = localSetting.read(filename, "");
				if(isUpdate(storeTime)){
					//TODO:服务器有更新，从服务器重新下载数据,并 存入文件 
					String serverPath = URLConfig.epgUrl+"today";  //epg地址
					data = getEPGDataFromServer(serverPath, filename);
				}else{
					//TODO:服务器无更新，直接使用本地数据
					data =fileStoreImpl.getFromFile(Const.EPG_STORE_PATH+"/"+filename);
				}
			}
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return data;
	}

	private String getEPGDataFromServer(String serverPath,String filename) {
		// TODO Auto-generated method stub
		HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
		String json = new String("");
//		String json = httpClientUtils.requestEPG(serverPath, "utf-8");
//		json = httpClientUtils.requestEPG(URLManager.epgUrl+"today", "utf-8");
		
		if(ModelUtil.hasLength(json)){//如果下载数据不为空
			fileStoreImpl.putToFile(Const.EPG_STORE_PATH, filename, json);
		}
		return json;
	}

	@Override
	public boolean isExist(String filePath) {
		// TODO Auto-generated method stub
		File file = new File(filePath);
		if (file.exists()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isUpdate(String storeTime) {
		// TODO Auto-generated method stub
		return false;
	}

}
