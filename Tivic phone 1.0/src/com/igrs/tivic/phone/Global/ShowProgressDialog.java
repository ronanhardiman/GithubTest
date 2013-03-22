package com.igrs.tivic.phone.Global;

import com.igrs.tivic.phone.Utils.UIUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.util.Log;

public class ShowProgressDialog {

	public static ProgressDialog wait;

	/*
	 * msg 为要显示在等待Dialog里的提示文字，缺省可以为null
	 * thread 为希望通过等待Dialog的返回操作要取消执行的线程，缺省可以为null
	 */
	public static void show(Context context,String msg, Thread thread) {
		final Thread th = thread;
		try{
			dismiss();
			wait = new ProgressDialog(context);
			//设置风格为圆形
			wait.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			wait.setTitle(null);
			wait.setIcon(null);
			//设置提示信息
			if(msg != null)
				wait.setMessage(msg);
			//设置是否可以通过返回键取消
			wait.setCancelable(true);
			wait.setIndeterminate(false);
			//设置取消监听
			wait.setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					if(th != null)
						th.interrupt();
				}
			});
			wait.show();
		}catch(Exception e){
			UIUtils.Loge("ShowProgressDialog", "show failed!");
		}
	}
	public static void show2(Context context,int resourceId, Thread thread) {
		final Thread th = thread;
		dismiss();
		wait = new ProgressDialog(context);
		//设置风格为圆形
		wait.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		wait.setTitle(null);
		wait.setIcon(null);
		//设置提示信息
		if(resourceId!= 0)
			wait.setMessage(context.getResources().getString(resourceId));
		//设置是否可以通过返回键取消
		wait.setCancelable(true);
		wait.setIndeterminate(false);
		//设置取消监听
		wait.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				if(th != null)
					th.interrupt();
			}
		});
		wait.show();
	}
	public static void dismiss()
	{
		try{
			if(wait != null)
				if(wait.isShowing())
					wait.dismiss();
		}catch(Exception e){
			UIUtils.Loge("ShowProgressDialog", "dismiss failed!");
		}
	}
}
