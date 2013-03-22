package com.igrs.tivic.phone.Utils;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.igrs.tivic.phone.Global.AppContext;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Global.URLConfig;

import android.content.Context;
import android.util.Log;
import com.igrs.tivic.phone.Bean.VisitBean;
import com.igrs.tivic.phone.Bean.Base.UserBean;
import com.igrs.tivic.phone.Global.TivicGlobal;

public class UIUtils {
	
	public static final String TAG = "UIUtils";
	
	public static void setCurrentFuncID(int func_id)
	{
		TivicGlobal.getInstance().mCurrentFuncID = func_id;
	}
	
	public static int getCurrentFuncID()
	{
		return TivicGlobal.getInstance().mCurrentFuncID;
	}
	
	
	
	public static void setVisitBean(VisitBean bean)
	{
		TivicGlobal.getInstance().currentVisit.setVisit_channelid(bean.getVisit_channelid());
		TivicGlobal.getInstance().currentVisit.setVisit_pid(bean.getVisit_pid());
		TivicGlobal.getInstance().currentVisit.setVisit_ProgramTitle(bean.getVisit_ProgramTitle());
	}
	
	public VisitBean getVisitBean()
	{
		return TivicGlobal.getInstance().currentVisit;
	}
	
	public static String getUsrAvatar(int uid)
	{
		String imgurl = URLConfig.avarter_path + "/userdata/avatar";
		String uids = String.valueOf(uid);
		String md5s = MD5Utils.getMD5String(uids);
		//Log.d(TAG, "md5uid = " + md5s);
		String folder1 = "/" + md5s.substring(0, 2);
		String folder2 = "/" + md5s.substring(2, 4);
		String imgname = "/" + uids + ".jpg!w64";
		return (imgurl + folder1 + folder2 + imgname);
		
	}
	/*
	 * 通过图片url和缩略图宽度px，获取缩略图url
	 */
	public static String getImgSubnailUrl(String url, int width)
	{
		String newUrl = url;
		newUrl = newUrl + "!w" + String.valueOf(width);
		return newUrl;
	}	
	/** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;       
        int px = (int) (dpValue * scale + 0.5f);
        //Log.i("UIUtils", "kevin add: getDisplayMetrics().density = " + scale + " px = " + px);
        return px;
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        int dip = (int) (pxValue / scale + 0.5f);  
        //Log.i("UIUtils", "kevin add: getDisplayMetrics().density = " + scale + " sip = " + dip);
        return dip;
    }
    
    /**
	 * 编辑器显示保存的草稿
	 * @param context
	 * @param editer
	 * @param temlKey
	 */
	public static void showTempEditContent(Activity context, EditText editer, String temlKey) {
		String tempContent = ((AppContext)context.getApplication()).getProperty(temlKey);
		if(!ModelUtil.isEmpty(tempContent)) {
			editer.setText(tempContent);
			editer.setSelection(tempContent.length());//设置光标位置
		}
	}
	/**
	 * 弹出Toast消息
	 * @param msg
	 */
	public static void ToastMessage(Context cont,String msg)
	{
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}
	public static void ToastMessage(Context cont,int msg)
	{
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}
	public static void ToastMessage(Context cont,String msg,int time)
	{
		Toast.makeText(cont, msg, time).show();
	}
	
	public static void Logi(String TAG, String message)
	{
		if(Const.DEBUG)
		{
			Log.i(TAG, message);
		}
	}
	public static void Logd(String TAG, String message)
	{
		if(Const.DEBUG)
		{
			Log.d(TAG, message);
		}
	}
	public static void Loge(String TAG, String message)
	{
		if(Const.DEBUG)
		{
			Log.e(TAG, message);
		}
	}
	
}
