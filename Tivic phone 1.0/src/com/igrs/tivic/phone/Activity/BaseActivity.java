package com.igrs.tivic.phone.Activity;

//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
//import android.os.AsyncTask;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
//import android.text.TextUtils;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
//import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Bean.Login.UserRegisterBean;
import com.igrs.tivic.phone.Bean.Social.SocialNewsBean;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Global.URLConfig;
//import com.igrs.tivic.phone.Global.URLConfig;
//import com.igrs.tivic.phone.Impl.LoginImpl;
//import com.igrs.tivic.phone.Impl.UGCImpl;
import com.igrs.tivic.phone.Listener.RefreshDataListener;
import com.igrs.tivic.phone.Service.RefreshDataService;
import com.igrs.tivic.phone.Utils.GDUtils;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;
//import com.igrs.tivic.phone.Utils.Utils;
import com.igrs.tivic.phone.ViewGroup.BaseEmpty;
import com.igrs.tivic.phone.ViewGroup.BaseTopBar;
import com.igrs.tivic.phone.ViewGroup.SlideMenuViewGroup;
import com.igrs.tivic.phone.ViewGroup.UsrMenuBar;
import com.igrs.tivic.phone.widget.AsyncImageView;
//用于友盟统计
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends Activity implements RefreshDataListener {

	public SlideMenuViewGroup mainGroup;
	public BaseEmpty baseEmpty;
	public BaseTopBar mBaseTopBar;
	public UsrMenuBar mUsrMenuBar;
	public Context mContext;
	public AsyncImageView userBtn;
	public TextView newShow;
	private String TAG = "BaseActivity";
	public FrameLayout base_main;
	// private PopupWindow setting_popup;
	private ServiceConnection mConnection;
	private boolean mIsBound;
	private RefreshDataService mBoundService;
	private final int MSG_GET_COUNT_NEW = 50;
	private final int MSG_NET_DISCONNECT = 205;

	private Handler updateCountHandler;
	private UserRegisterBean userbean = TivicGlobal.getInstance().userRegisterBean;
	protected PopupWindow pw;
	private LinearLayout ll_myinfo, ll_search, ll_exit, ll_setting;
	private Display display;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.base_main);
		base_main = (FrameLayout) findViewById(R.id.base_main);
		display = getWindowManager().getDefaultDisplay();
		sp = getSharedPreferences("config", MODE_PRIVATE);
		getScreeanSize();
		initLayout();
		init();
		//程序退到后台运行时，后台服务停止，保护服务器
//		bindService();
		initPopupWindow();

	}

	private void initPopupWindow() {
		View view = View.inflate(mContext, R.layout.menu_popup, null);
		ll_myinfo = (LinearLayout) view.findViewById(R.id.ll_myinfo);
		ll_exit = (LinearLayout) view.findViewById(R.id.ll_exit);
		ll_search = (LinearLayout) view.findViewById(R.id.ll_search);
		ll_setting = (LinearLayout) view.findViewById(R.id.ll_setting);
		ll_myinfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TivicGlobal.getInstance().mIsLogin) {
					pw.dismiss();
					Intent intent = new Intent(BaseActivity.this,
							LoginRegistrationBaseInfoActivity.class);
					intent.putExtra("fromClass", getClass().getName());
					startActivity(intent);
				} else {
					Toast.makeText(mContext, R.string.menu_login_first,
							Toast.LENGTH_SHORT).show();

				}
			}
		});
		ll_exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pw.dismiss();
				Intent exitIntent = new Intent(BaseActivity.this, SettingActivity.class);
				exitIntent.putExtra("exit", true);
				exitIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				exitIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(exitIntent);

			}
		});
		ll_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pw.dismiss();
//				Intent searchIntent = new Intent(BaseActivity.this, SearchActivity.class);
//				startActivity(searchIntent);
				logout();
			}
		});
		ll_setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pw.dismiss();
				Intent settingIntent = new Intent(BaseActivity.this, SettingActivity.class);
				startActivity(settingIntent);

			}
		});
		view.setFocusableInTouchMode(true);  
		view.setOnKeyListener(new OnKeyListener() {  
		    @Override  
		    public boolean onKey(View v, int keyCode, KeyEvent event) {  
		        // TODO Auto-generated method stub  
		        if ((keyCode == KeyEvent.KEYCODE_MENU)&&(pw.isShowing())) {  
		            pw.dismiss();// 这里写明模拟menu的PopupWindow退出就行  
		            return true;  
		        }  
		        return false;  
		    }  
		});  
		pw = new PopupWindow(view, display.getWidth(), display.getHeight() / 3);
		pw.setFocusable(true);
		pw.setBackgroundDrawable(new ColorDrawable());

	}
	public void logout() {
		if(!TivicGlobal.getInstance().mIsLogin) {
			Toast.makeText(mContext, R.string.setting_login_first, Toast.LENGTH_SHORT).show();
		} else {
//			userbean.setUserSID(null);
			TivicGlobal.getInstance().mIsLogin = false;
//			TivicGlobal.getInstance().userRegisterBean.setSuccessModifyUserAvater(false);
			TivicGlobal.getInstance().userRegisterBean = new UserRegisterBean();
			sp.edit().putBoolean("autoLogin", false).commit();
			Toast.makeText(mContext, R.string.setting_logout_success, Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(mContext,LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
		}
	}
	public void popup() {
		if (pw != null) {
			pw.showAtLocation(base_main, Gravity.TOP | Gravity.LEFT, 0,
					display.getHeight() - pw.getHeight());
		}
	}

	public void dismissPopup() {
		if (pw != null) {
			pw.dismiss();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//add new
		bindService();
//		userBtn.setAvaterUrl(UIUtils.getUsrAvatar(TivicGlobal.getInstance().userRegisterBean.getUserId()));
//		initUserBtn();
		Utils.initAvater(userBtn);
		closeMenuBar();
		initMKFrientEnable();
		initMenuBarCountNew();
		//用于友盟统计
		MobclickAgent.onResume(this);

	}
	
//	public void initUserBtn() {
//		String userAvaterPath = userbean.getUserAvartarPath();
//		System.out.println("userbean.isModifyUserAvatar()="+(userbean.isModifyUserAvatar()));
//		System.out.println("userbean.isSuccessModifyUserAvater()="+(userbean.isSuccessModifyUserAvater()));
//		System.out.println("!TextUtils.isEmpty(userAvaterPath="+(!TextUtils.isEmpty(userAvaterPath)));
//		if(userbean.isModifyUserAvatar() && userbean.isSuccessModifyUserAvater() && !TextUtils.isEmpty(userAvaterPath)) {
//			System.out.println("----------------from SD");
//			Bitmap bm = BitmapFactory.decodeFile(userAvaterPath);
//			userBtn.setImageBitmap(bm);
//		} else {
//			if (userbean.getUserAvatar() != null
//			&& !TextUtils.isEmpty(userbean.getUserAvatar())) {
//		if (userbean.getUserAvatar().contains(".jpg")) {// 初始化用户头像
//			final String imagePath = URLConfig.avarter_path
//					+ userbean.getUserAvatar() + "!w128";
//			System.out.println("----------------from net");
//			new AsyncTask<Void, Void, Bitmap>() {
//				@Override
//				protected Bitmap doInBackground(Void... params) {
//					Bitmap bitmap = getImageFromNet(imagePath);
//					return bitmap;
//				}
//				
//				protected void onPostExecute(Bitmap result) {
//					userBtn.setImageBitmap(result);
//				};
//			}.execute();
//		}
//	}
//		}
//	}
	
	private void initMenuBarCountNew() {
		if (mUsrMenuBar != null) {
			mUsrMenuBar.setNotifyCount(TivicGlobal.getInstance().notifyCount);
			mUsrMenuBar.setMessageCount(TivicGlobal.getInstance().messageCount);
		}
		if (TivicGlobal.getInstance().notifyCount
				+ TivicGlobal.getInstance().messageCount <= 0){
			setNewShow(0);
		}else{
				setNewShow(1);
		}
	}
	
	public void initMKFrientEnable()
	{
		if(TivicGlobal.getInstance().currentVisit.getVisit_pid() == null || TivicGlobal.getInstance().currentVisit.getVisit_pid().isEmpty())
		{
			mUsrMenuBar.setMKFriendEnable(false);
		}else{
			mUsrMenuBar.setMKFriendEnable(true);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unbindService();
		//用于友盟统计
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();

	}
	
	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		GDUtils.getGDApplication(mContext).onLowMemory();
		System.gc();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		GDUtils.getGDApplication(mContext).destory();
		System.gc();
	}

	public void initLayout() {
		mainGroup = (SlideMenuViewGroup) findViewById(R.id.mainGroup);
		mBaseTopBar = (BaseTopBar) findViewById(R.id.base_top_bar);
		mUsrMenuBar = (UsrMenuBar) findViewById(R.id.usr_menu_bar);
		baseEmpty = (BaseEmpty) findViewById(R.id.base_empty);
		userBtn = (AsyncImageView) findViewById(R.id.usr_btn);
		userBtn.setImageResource(R.drawable.base_default_avater);
//		userBtn.getImageNow(UIUtils.getUsrAvatar(TivicGlobal.getInstance().userRegisterBean
//				.getUserId()));
		newShow = (TextView) findViewById(R.id.usr_remind);
		if (TivicGlobal.getInstance().mIsLogin == false) {
			setNewShow(0);
		}
		userBtn.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					clickUsrIcon();
				}
				return true;
			}
		});

		if(mConnection == null){
		mConnection = new ServiceConnection() {
			public void onServiceConnected(ComponentName className,
					IBinder service) {
				mBoundService = ((RefreshDataService.RefreshDataServiceBinder) service)
						.getService();
				mBoundService.setRefreshDataListener(BaseActivity.this);
				// Log.d(TAG, "kevin add: onServiceConnected!");
			}

			public void onServiceDisconnected(ComponentName className) {
				// This is called when the connection with the service has been
				// unexpectedly disconnected -- that is, its process crashed.
				// Because it is running in our same process, we should never
				// see this happen.
				mBoundService = null;
				// Log.d(TAG, "kevin add: onServiceDisconnected!");
			}
		};
		}
		if(updateCountHandler == null){
		updateCountHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.arg1) {
				case MSG_GET_COUNT_NEW: {

					SocialNewsBean bean = (SocialNewsBean) msg.obj;
					int notifynew = bean.getAct_sum();
					int messagenew = bean.getMsg_sum();
					mUsrMenuBar.setNotifyCount(notifynew);
					mUsrMenuBar.setMessageCount(messagenew);
					setNewShow(notifynew + messagenew);
				}
				break;
				case MSG_NET_DISCONNECT:
					UIUtils.ToastMessage(mContext, getString(R.string.net_error));
					break;

				}

			}
		};
		}
	}

	// 当后台Service获取新通知，需要刷新界面时，会回调该方法

	@Override
	public void onDataChanged(SocialNewsBean countBean) {

		Message msg = updateCountHandler.obtainMessage();
		msg.arg1 = MSG_GET_COUNT_NEW;
		msg.obj = countBean;
		updateCountHandler.sendMessage(msg);
	}

	@Override 
	public void onNetworkDisconnect()
	{
		Message msg = updateCountHandler.obtainMessage();
		msg.arg1 = MSG_NET_DISCONNECT;
		updateCountHandler.sendMessage(msg);
		
	}
	
	public void setNewShow(int count) {
		if (count > 0) {
			if(userBtn.isShown())
				newShow.setVisibility(View.VISIBLE);
		} else {
			newShow.setVisibility(View.GONE);
		}
	}

	public void bindService() {
		if(!TivicGlobal.getInstance().mIsLogin)
			return;
		Intent intent = new Intent(BaseActivity.this, RefreshDataService.class);
		if (mConnection != null) {
			bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
			mIsBound = true;
		}
	}

	public void unbindService() {
		if(!TivicGlobal.getInstance().mIsLogin)
			return;
		if (mIsBound && mConnection != null) {
			// Detach our existing connection.
			unbindService(mConnection);
			mIsBound = false;
		}
	}

	public void closeMenuBar() {

		if (mainGroup != null && mainGroup.ifMenuBarShown()) {
			// if the menubar is open, do nothing but close it!
			mainGroup.scrollMenuBar();
		}

	}

	public void clickUsrIcon() {
		if (!TivicGlobal.getInstance().mIsLogin) {
			try {
				Intent intent = new Intent();
				intent.setClassName("com.igrs.tivic.phone",
						"com.igrs.tivic.phone.Activity.LoginActivity");
				startActivity(intent);
				// Log.i(TAG, "kevin add: jiump to LoginActivity");
			} catch (Exception e) {
				Log.e(TAG, "startActivity error! " + e.toString());
			}
		} else {
			mBaseTopBar.hideSelectorBar();
			mainGroup.scrollMenuBar();
			setNewShow(0);
		}
	}

	public void init() {
		mUsrMenuBar.setParent(this);
		mBaseTopBar.setParent(this);
		baseEmpty.init();
		mBaseTopBar.init();

	}

	private void getScreeanSize() {
		/* 取得屏幕分辨率大小 */

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		TivicGlobal.getInstance().displayWidth = dm.widthPixels;
		TivicGlobal.getInstance().displayHeight = dm.heightPixels;
	}

	public void hideMenuBar() {
		findViewById(R.id.usr_menu_bar).setVisibility(View.GONE);
		findViewById(R.id.usr_btn).setVisibility(View.GONE);
		findViewById(R.id.usr_remind).setVisibility(View.GONE);
	}
	
	public void showMenuBar(){
		findViewById(R.id.usr_menu_bar).setVisibility(View.VISIBLE);
		findViewById(R.id.usr_btn).setVisibility(View.VISIBLE);
		findViewById(R.id.usr_remind).setVisibility(View.VISIBLE);
	}

	public void jumpToActivity(int func_id) {

		Intent intent = new Intent();
		switch (func_id) {
		case FuncID.ID_LOGIN:
			try {

				intent.setClassName("com.igrs.tivic.phone",
						"com.igrs.tivic.phone.Activity.LoginActivity");
				startActivity(intent);
				UIUtils.setCurrentFuncID(FuncID.ID_LOGIN);
				// Log.i(TAG, "kevin add: jiump to LoginActivity");
			} catch (Exception e) {
				Log.e(TAG, "startActivity error! " + e.toString());
			}
			break;
		case FuncID.ID_EPG:
			try {
				intent.setClassName("com.igrs.tivic.phone",
						"com.igrs.tivic.phone.Activity.EPGActivity");
				startActivity(intent);
				UIUtils.setCurrentFuncID(FuncID.ID_EPG);
				// Log.i(TAG, "kevin add: jiump to EPGActivity");
			} catch (Exception e) {
				Log.e(TAG, "startActivity error! " + e.toString());
			}
			break;
		case FuncID.ID_WATERFALL:
			try {
				intent.setClassName("com.igrs.tivic.phone",
						"com.igrs.tivic.phone.Activity.WaterfallActivity");
				startActivity(intent);
				UIUtils.setCurrentFuncID(FuncID.ID_WATERFALL);
				// Log.i(TAG, "kevin add: jiump to WaterfallActivity");
			} catch (Exception e) {
				Log.e(TAG, "startActivity error! " + e.toString());
			}
			break;
		case FuncID.ID_PUSHTV:
			try {
				intent.setClassName("com.igrs.tivic.phone",
						"com.igrs.tivic.phone.Activity.PushTVActivity");
				startActivity(intent);
				UIUtils.setCurrentFuncID(FuncID.ID_PUSHTV);
				// Log.i(TAG, "kevin add: jiump to PushTVActivity");
			} catch (Exception e) {
				Log.e(TAG, "startActivity error! " + e.toString());
			}
			break;
		case FuncID.ID_MYTV:
			try {
				intent.setClassName("com.igrs.tivic.phone",
						"com.igrs.tivic.phone.Activity.MyTVActivity");
				startActivity(intent);
				UIUtils.setCurrentFuncID(FuncID.ID_MYTV);
				// Log.i(TAG, "kevin add: jiump to MyTVActivity");
			} catch (Exception e) {
				Log.e(TAG, "startActivity error! " + e.toString());
			}
			break;
		case FuncID.ID_CONTENT:
			try {
				intent.setClassName("com.igrs.tivic.phone",
						"com.igrs.tivic.phone.Activity.ContentActivity");
				startActivity(intent);
				UIUtils.setCurrentFuncID(FuncID.ID_CONTENT);
				// Log.i(TAG, "kevin add: jiump to ContentActivity");
			} catch (Exception e) {
				Log.e(TAG, "startActivity error! " + e.toString());
			}
			break;
		case FuncID.ID_UGC_JUMP:
			try {
				intent.setClassName("com.igrs.tivic.phone",
						"com.igrs.tivic.phone.Activity.UGCJumptActivity");
				startActivity(intent);
				UIUtils.setCurrentFuncID(FuncID.ID_UGC_JUMP);
//				Log.i(TAG, "kevin add: jiump to UGCJumptActivity");
			} catch (Exception e) {
				Log.e(TAG, "startActivity error! " + e.toString());
			}
			break;
		case FuncID.ID_UGC:
			try {
				intent.setClassName("com.igrs.tivic.phone",
						"com.igrs.tivic.phone.Activity.UGCActivity");
				startActivity(intent);
				UIUtils.setCurrentFuncID(FuncID.ID_UGC);
//				Log.i(TAG, "kevin add: jiump to UGCActivity");
			} catch (Exception e) {
				Log.e(TAG, "startActivity error! " + e.toString());
			}
			break;
		case FuncID.ID_SOCIAL:
			try {
				intent.setClassName("com.igrs.tivic.phone",
						"com.igrs.tivic.phone.Activity.SocialBaseActivity");
				startActivity(intent);
				UIUtils.setCurrentFuncID(FuncID.ID_SOCIAL);
//				Log.i(TAG, "kevin add: jiump to SocialActivity");
			} catch (Exception e) {
				Log.e(TAG, "startActivity error! " + e.toString());
			}
			break;
		case FuncID.ID_PUBLISH:
			try {
				intent.setClassName("com.igrs.tivic.phone",
						"com.igrs.tivic.phone.Activity.UGCPublishActivity");
				startActivity(intent);
				UIUtils.setCurrentFuncID(FuncID.ID_PUBLISH);
//				Log.i(TAG, "kevin add: jiump to UGCPublishActivity");
			} catch (Exception e) {
				Log.e(TAG, "startActivity error! " + e.toString());
			}
			break;
		}
		;

	}
	public Bitmap getImageFromNet(String path) {
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(6 * 1000);
			conn.setRequestMethod("GET");
			conn.addRequestProperty("Cache-Control", "no-cache");
			if (conn.getResponseCode() == 200) {
				InputStream is = conn.getInputStream();
				Bitmap bitmap = BitmapFactory.decodeStream(is);
				return bitmap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu");
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
			popup();
		return false;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

			if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (pw.isShowing()) {
				dismissPopup();
				return true;
			}
			if(mBaseTopBar.getParentActivity() instanceof SearchActivity ){
				if(((SearchActivity) mBaseTopBar.getParentActivity()).isFirstSearch){
					mBaseTopBar.getParentActivity().finish();
				}else{
					
					((SearchActivity) mBaseTopBar.getParentActivity()).setEditFrameLayout(true);
					((SearchActivity) mBaseTopBar.getParentActivity()).initEditerData();
					((SearchActivity) mBaseTopBar.getParentActivity()).setListViewColor();
					((SearchActivity) mBaseTopBar.getParentActivity()).setFirstSearch(true);
					return true;
				}
				
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	public void openMenu()
	{
		if (pw.isShowing()) {
			dismissPopup();
		} else {
			popup();
		}
	}
	

}
