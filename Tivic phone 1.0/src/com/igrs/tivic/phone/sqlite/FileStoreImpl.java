package com.igrs.tivic.phone.sqlite;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Global.LocalSetting;

public class FileStoreImpl implements FileStore {
	private Context context;
	LocalSetting localSetting = LocalSetting.getInstance(context);
	public FileStoreImpl(Context context) {
		this.context = context;
	}

	@Override
	public void putToFile(String filePath, String fileName, String data) {
		localSetting.init(context);
		try{
			File dir = new File(filePath);
			if(!dir.exists()){
				dir.mkdir();
			}
			File file = new File(dir, fileName);
			if(!file.exists()){
				file.createNewFile();
			}
			byte[] b = data.getBytes();
			FileOutputStream fStream;
			fStream = new FileOutputStream(file);
			OutputStream stream = new BufferedOutputStream(fStream);
			stream.write(b);
			stream.flush();
			stream.close();
			//文件存储成功后，把 存储文件的时间保存起来
			Calendar calendar = Calendar.getInstance();
			String currentDate = calendar.get(Calendar.YEAR)+""+(calendar.get(Calendar.MONTH)+1)+calendar.get(Calendar.DAY_OF_MONTH);
			String currentTime = calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE);
			localSetting.write(fileName, currentDate+"_"+currentTime);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public String getFromFile(String filePath) {
		File file = new File(filePath);
		if(!file.exists()){
			Toast.makeText(context, context.getResources().getString(R.string.file_noexist), Toast.LENGTH_LONG).show();
		}
		StringBuffer sb = new StringBuffer();
		try{
			BufferedReader reader = null;
			reader = new BufferedReader(new FileReader(file));
			String text = null;
			while ((text = reader.readLine())!= null) {
				sb.append(text);
			}
			Log.i("zyl", "data="+sb.toString());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

}
