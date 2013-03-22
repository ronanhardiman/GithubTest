package com.igrs.tivic.phone.Activity;

import java.util.Timer;
import java.util.TimerTask;

import com.igrs.tivic.phone.R;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

public class AppWidgetMain extends AppWidgetProvider {

	private Timer timer = new Timer();
	private int[] appWidgetIds;
	private AppWidgetManager appWidgetManager;
	private Context context;
	private int count = 1;

	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		this.appWidgetManager = appWidgetManager;
		this.appWidgetIds = appWidgetIds;
		this.context = context;

		timer = new Timer();
		timer.schedule(timerTask, 0, 5000);
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				int n = appWidgetIds.length;
				for (int i = 0; i < n; i++) {
					int appWidgetId = appWidgetIds[i];
					RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget_layout);
					if(count%2==0){
						views.setImageViewResource(R.id.imageview,R.drawable.base_queshengtu4_3);
					}else{
						views.setImageViewResource(R.id.imageview,R.drawable.base_queshengtu4_3);
					}
//					
					appWidgetManager.updateAppWidget(appWidgetId, views);
//					Log.e("aaa","================================");
				}
				count++;
				break;
			}
			super.handleMessage(msg);
		}
	};
	private TimerTask timerTask = new TimerTask() {
		public void run() {
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message); // �������͵���Ϣ����
		}
	};

}
