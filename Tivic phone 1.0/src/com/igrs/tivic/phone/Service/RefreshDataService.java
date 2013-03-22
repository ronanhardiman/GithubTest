package com.igrs.tivic.phone.Service;

import java.util.Calendar;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Bean.Base.BaseParamBean;
import com.igrs.tivic.phone.Bean.Social.SocialNewsBean;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Impl.MessageImpl;
import com.igrs.tivic.phone.Impl.NotifyImpl;
import com.igrs.tivic.phone.Listener.RefreshDataListener;
import com.igrs.tivic.phone.Utils.UIUtils;

public class RefreshDataService extends Service {

	private int newNotify = 0; // 新通知个数
	private int newMessage = 0; // 新消息个数
	private RefreshDataListener updateListener;
	Context context;
	NotifyImpl notifyinterface;
	int requestNotifyIndext = 0;
	int requestMessageIndex = 0;
	String TAG = "RefreshDataService";
	TivicGlobal tivicGlobal;
	BaseParamBean param;
	boolean isStop = false;
	private final IBinder mBinder = new RefreshDataServiceBinder();

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		context = this.getApplicationContext();
		notifyinterface = NotifyImpl.getInstance();
		tivicGlobal = TivicGlobal.getInstance();

		doInBackgroud();
	}

	@Override
	public void onDestroy() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
//		Log.i(TAG, "onStartCommand Started.");
		if (isStop)
			doInBackgroud();
		return START_NOT_STICKY;
	}

	public void setRefreshDataListener(RefreshDataListener listener) {
		this.updateListener = listener;
	}

	private void doInBackgroud() {
		if(!tivicGlobal.mIsLogin)
			return;
		if (param == null)
			param = new BaseParamBean();
		param.setVer(1);
		param.setUid(TivicGlobal.getInstance().userRegisterBean.getUserId()); // 其实在这里这个参数没用
		param.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());

		new Thread() {
			public void run() {
				
				while (!isStop) {
					SocialNewsBean ret = null;
					if (updateListener != null) {
						ret = notifyinterface.getCountNew(param);
						if (ret != null) {
							newNotify = ret.getAct_sum();
							newMessage = ret.getMsg_sum();
							TivicGlobal.getInstance().notifyCount = newNotify;
							TivicGlobal.getInstance().messageCount = newMessage;

							updateListener.onDataChanged(ret);
							//每分钟向服务器请求一次count new
							try {
								Thread.sleep(60000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

						}else{
							updateListener.onNetworkDisconnect();
							isStop = true;
						}
					}else{
						//等待主进程绑定本服务，并传刷新界面的Handler进来
						UIUtils.Logi(TAG, "updateListener is null!");
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

				}
			}
		}.start();
	}

	public void stopBackground() {
		isStop = true;
	}

	public class RefreshDataServiceBinder extends Binder {
		public RefreshDataService getService() {
			return RefreshDataService.this;
		}
	}
}
