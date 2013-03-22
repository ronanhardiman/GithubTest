package com.igrs.tivic.phone.Activity;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView.OnEditorActionListener;

import com.baidu.mapapi.BMapManager;
import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Bean.LocationBean;
import com.igrs.tivic.phone.Bean.Login.UserRegisterBean;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.ShowProgressDialog;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Global.URLConfig;
import com.igrs.tivic.phone.Impl.LoginImpl;
import com.igrs.tivic.phone.Interface.ILogin;
import com.igrs.tivic.phone.Utils.LocationProvider;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;
import com.igrs.tivic.phone.Utils.LocationProvider.GetLocationListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.NotificationType;
import com.umeng.fb.UMFeedbackService;
import com.umeng.update.UmengUpdateAgent;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;

public class LoginActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener{
	private SharedPreferences sp;
	private Context mContext;
	private EditText edit_username, edit_pwd;
	private CheckBox cb_remeber_pwd, cb_auto_login;
	private TextView forget_pwd, weibo_login;
	private Button bt_login, bt_registration, bt_visitor_login;
	private Weibo weibo;
	private Editor editor;
	private Oauth2AccessToken oauth2AccessToken;
	private LoginImpl loginImpl = new LoginImpl();
	private UserRegisterBean urb = TivicGlobal.getInstance().userRegisterBean;
	private LocationProvider provider;
	private BMapManager mBMapManager;
//	private String configName = "data/data/com.igrs.tivic.phone/shared_prefs/config.xml";
//	private final static int SWITCH_MAINACTIVITY = 1000;
//	private final static int SWITCH_GUIDACTIVITY = 1001;
//	public Handler mHandler = new Handler(){
//        public void handleMessage(Message msg) {
//            switch(msg.what){
//            case SWITCH_GUIDACTIVITY:
//            	Intent mIntent = new Intent();
//                mIntent.setClass(LoginActivity.this, GuideActivity.class);
//                LoginActivity.this.startActivity(mIntent);
//                break;
//            }
//            super.handleMessage(msg);
//        }
//    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.login_main);
		handleGetLocation();// 获得位置信息
		PackageManager pm = getPackageManager();
        try {
			PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);//获取当前版本号
			TivicGlobal.versionName = pi.versionName;
        } catch (Exception e) {
		}
		mContext = this;
//		initGuideActivity();//初始化 引导页
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		editor = sp.edit();
//		File configFile = new File(configName);
//		if(!configFile.exists()) {//初始化一些配置信息
//			editor.putBoolean("position_on", true);
//			editor.putBoolean("guide_on", true);
//			editor.commit();
//		}
		if (TextUtils.isEmpty(sp.getString("udid", ""))) {
			TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			TivicGlobal.UDID = tm.getDeviceId();// 获得设备的唯一标识
			sp.edit().putString("udid", tm.getDeviceId()).commit();
		} else {
			TivicGlobal.UDID = sp.getString("udid", "");
		}
		handleAutoLogin();// 自动登陆
		init();
		setListener();
	}
	//初始化引导页
//	private void initGuideActivity() {
//		Boolean isFirst = isFirstEnter();
//		if(isFirst)
//			mHandler.sendEmptyMessageDelayed(SWITCH_GUIDACTIVITY,100);
//	}

//	private Boolean isFirstEnter() {
//		String mResult = mContext.getSharedPreferences(Const.SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE)
//				.getString(Const.KEY_GUIDE_ACTIVITY, "");
//		if(mResult.equalsIgnoreCase("false")){
//			return false;
//		}else{
//			return true;
//		}
//	}

	private void handleAutoLogin() {
		boolean autoLogin = sp.getBoolean("autoLogin", true);
		if (autoLogin) {
			final String username = sp.getString("username", "");
			final String password = sp.getString("password", "");
			if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
				new AsyncTask<Void, Void, Integer>() {
					protected void onPreExecute() {
						ShowProgressDialog.show2(mContext, R.string.login_isloging, null);
					};

					@Override
					protected Integer doInBackground(Void... params) {
//						LoginImpl loginImpl = new LoginImpl();
						SystemClock.sleep(2000);
						int code = loginImpl.login(username, password);
						return code;
					}

					protected void onPostExecute(Integer result) {
						ShowProgressDialog.dismiss();
						handleResult(result);
					};
				}.execute();
			}
		}
	}

	private void init() {
		edit_pwd = (EditText) findViewById(R.id.edit_pwd);
		edit_pwd.setOnEditorActionListener(new OnEditorActionListener() {

			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (event != null
						&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
					InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					in.hideSoftInputFromWindow(v.getApplicationWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
					login();
				}
				return false;
			}
		});
		edit_username = (EditText) findViewById(R.id.edit_username);
		cb_remeber_pwd = (CheckBox) findViewById(R.id.remeber_pwd);
		cb_auto_login = (CheckBox) findViewById(R.id.auto_login);
		forget_pwd = (TextView) findViewById(R.id.forget_pwd);
		weibo_login = (TextView) findViewById(R.id.weibo_login);
		bt_login = (Button) findViewById(R.id.bt_login);
		bt_registration = (Button) findViewById(R.id.bt_registration);
		bt_visitor_login = (Button) findViewById(R.id.bt_visitor_login);
		String spUsername = sp.getString("username", "");
		if (!TextUtils.isEmpty(spUsername)) {
			edit_username.setText(spUsername);
		}
		boolean autoLogin = sp.getBoolean("autoLogin", true);
		if (autoLogin) {
			cb_auto_login.setChecked(true);
		}
		boolean remeberPwd = sp.getBoolean("remeberPwd", true);
		if (remeberPwd) {
			cb_remeber_pwd.setChecked(true);
			String username = sp.getString("username", "");
			String password = sp.getString("password", "");
			if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
				edit_username.setText(username);
				edit_pwd.setText(password);
			}
		}
	}

	private void setListener() {
		bt_login.setOnClickListener(this);
		bt_registration.setOnClickListener(this);
		bt_visitor_login.setOnClickListener(this);
		forget_pwd.setOnClickListener(this);
		weibo_login.setOnClickListener(this);

		cb_auto_login.setOnCheckedChangeListener(this);
		cb_remeber_pwd.setOnCheckedChangeListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.forget_pwd:
			String name = edit_username.getText().toString().trim();
			intent = new Intent(mContext, LoginFindpwdActivity.class);
			if (!TextUtils.isEmpty(name)) {
				intent.putExtra("name", name);
			}
			startActivity(intent);
			finish();
			break;
		case R.id.weibo_login:
			loginByWeibo();

			break;
		case R.id.bt_login:
			login();
			break;
		case R.id.bt_registration:
			if (!Utils.isConnected(mContext)) {
				Toast.makeText(mContext, R.string.login_net_error,
						Toast.LENGTH_SHORT).show();
				return;
			}
			intent = new Intent(mContext, LoginRegistrationActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.bt_visitor_login:
			if (!Utils.isConnected(mContext)) {
				Toast.makeText(mContext, R.string.login_net_error,
						Toast.LENGTH_SHORT).show();
				return;
			}
			UIUtils.setCurrentFuncID(FuncID.ID_EPG);
			intent = new Intent(mContext, EPGActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}

	private void loginByWeibo() {// 通过微博登陆
		
		if (!Utils.isConnected(mContext)) {
			Toast.makeText(mContext, R.string.login_net_error,
					Toast.LENGTH_SHORT).show();
			return;
		}
		CookieSyncManager.createInstance(getApplicationContext()); 
		CookieSyncManager.getInstance().startSync(); 
		CookieManager.getInstance().removeAllCookie();//清除浏览器缓存
		
		weibo = Weibo.getInstance(Const.CONSUMER_KEY, URLConfig.REDIRECT_URL);
		weibo.authorize(mContext, new MyWeiboAuthListener());
	}

	private class MyWeiboAuthListener implements WeiboAuthListener {

		@Override
		public void onCancel() {
			Toast.makeText(mContext, R.string.login_weibo_cancel_auth,
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onComplete(Bundle bundle) {
			Toast.makeText(mContext, R.string.login_weibo_success,
					Toast.LENGTH_LONG).show();
			final String uid = bundle.getString("uid");
			// System.out.println("uid="+uid);
			final String access_token = bundle.getString("access_token");
			if (!TextUtils.isEmpty(access_token)) {
				editor.putString("access_token", access_token);// 存放取得的access_token
				editor.commit();
			}
			if (!TextUtils.isEmpty(uid)) {
				editor.putString("uid", uid);
				editor.commit();
			}
			// System.out.println("access_token="+access_token);
			String expires_in = bundle.getString("expires_in");
			// System.out.println("expires_in="+expires_in);
			oauth2AccessToken = new Oauth2AccessToken(access_token, expires_in);
			if (!TextUtils.isEmpty(uid) && !TextUtils.isEmpty(access_token)) {
				new AsyncTask<Void, Void, Integer>() {
					protected void onPreExecute() {
						ShowProgressDialog.show2(mContext,R.string.login_isloging_by_weibo, null);
					};

					@Override
					protected Integer doInBackground(Void... params) {
						int result = loginImpl.loginByWeibo(access_token, uid);
						if(result == 0) {
							loginImpl.getMyExtInfo();
							loginImpl.getMyInfo();
						}
						return result;
					}

					protected void onPostExecute(Integer result) {
						ShowProgressDialog.dismiss();
						if (result == 0) {// 微博登陆成功
							Toast.makeText(mContext, R.string.login_success,
									Toast.LENGTH_LONG).show();
//							Intent intent = new Intent(mContext,
//									LoginRegistrationBaseInfoActivity.class);
//							startActivity(intent);
//							finish();
//							if (Integer.parseInt(urb.getFill_profile()) == 0) {
//								UIUtils.setCurrentFuncID(FuncID.ID_EPG);
//								Intent intent = new Intent(mContext, EPGActivity.class);
//								startActivity(intent);
//								finish();
//							} else {
//								Intent intent = new Intent(mContext,
//										LoginRegistrationBaseInfoActivity.class);
//								startActivity(intent);
//								finish();
//							}
							boolean position_on = sp.getBoolean("position_on", true);
							if (position_on) {// 处理用户位置信息
//								UIUtils.Logi("chen","处理了上传位置");
								handleUploadLocation();
							}
							TivicGlobal.getInstance().mIsLogin = true;
							UIUtils.setCurrentFuncID(FuncID.ID_EPG);
							Intent intent = new Intent(mContext, EPGActivity.class);
							startActivity(intent);
							finish();
						} else if (result == 1) {// 获取sina用户信息失败
							Toast.makeText(mContext,
									R.string.login_by_weibo_error,
									Toast.LENGTH_LONG).show();

						} else if(result == -1) {
							UIUtils.ToastMessage(mContext, R.string.net_error);
						} else if(result == Const.USER_FORBIDDEN) {
							UIUtils.ToastMessage(mContext, R.string.login_uname_forbidden);
						}
					};
				}.execute();
			}
		}

		@Override
		public void onError(WeiboDialogError arg0) {
//			Toast.makeText(mContext, R.string.login_weibo_error,
//					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException arg0) {
//			Toast.makeText(mContext, R.string.login_weibo_error,
//					Toast.LENGTH_LONG).show();
		}

	}

	private void login() {
		if (!Utils.isConnected(mContext)) {
			Toast.makeText(mContext, R.string.login_net_error,
					Toast.LENGTH_SHORT).show();
			return;
		}
		final String username = edit_username.getText().toString().trim();
		String password = edit_pwd.getText().toString().trim();
		if (TextUtils.isEmpty(username)) {
			Toast.makeText(mContext,
					getResources().getString(R.string.user_valid_error),
					Toast.LENGTH_LONG).show();
			return;
		}
		if (TextUtils.isEmpty(password)) {
			Toast.makeText(mContext,
					getResources().getString(R.string.pwd_valid_error),
					Toast.LENGTH_LONG).show();
			return;
		}
		if (!Utils.isPhoneNumValid(username) && !Utils.isEmailValid(username)) {
			Toast.makeText(
					mContext,
					getResources().getString(
							R.string.login_register_username_error),
					Toast.LENGTH_LONG).show();
			return;
		}
		new AsyncTask<Void, Void, Integer>() {
			protected void onPreExecute() {
				ShowProgressDialog.show2(mContext, R.string.login_isloging, null);
				
			};

			@Override
			protected Integer doInBackground(Void... params) {
//				LoginImpl loginImpl = new LoginImpl();
				SystemClock.sleep(2000);
				String username = edit_username.getText().toString().trim();
				String password = edit_pwd.getText().toString().trim();
				int loginCode = loginImpl.login(username, password);
				return loginCode;
			}

			protected void onPostExecute(Integer result) {
				ShowProgressDialog.dismiss();
				handleResult(result);
			};
		}.execute();
	}

	@Override
	protected void onStart() {
		Intent intent = getIntent();
		String name = intent.getStringExtra("username");
		if (!TextUtils.isEmpty(name)) {
			edit_username.setText(name);
		}
		super.onStart();
	}

	protected void handleResult(Integer result) {
		switch (result) {
		case -1:
			UIUtils.ToastMessage(mContext, R.string.net_error);
		break;
		case Const.USERLEGALERROR:
			Toast.makeText(mContext,
					getResources().getString(R.string.user_legal_error),
					Toast.LENGTH_SHORT).show();
			break;
		case Const.PWDVLEGALERROR:
			Toast.makeText(mContext,
					getResources().getString(R.string.pwd_legal_error),
					Toast.LENGTH_SHORT).show();
			break;
		case Const.HTTP_EXCEPTION:
			Toast.makeText(mContext,
					getResources().getString(R.string.login_error),
					Toast.LENGTH_SHORT).show();
			break;
		case Const.SOCKET_TIMEOUT_EXCEPTION:
			Toast.makeText(mContext,
					getResources().getString(R.string.login_time_out),
					Toast.LENGTH_SHORT).show();
			break;
		case Const.USER_FORBIDDEN:
			UIUtils.ToastMessage(mContext, R.string.login_uname_forbidden);
			break;
		case Const.USERNAME_ERROR:
			UIUtils.ToastMessage(mContext, R.string.login_uname_error);
			break;
		case Const.PWD_ERROR:
			UIUtils.ToastMessage(mContext, R.string.login_pwd_error);
			break;
		case Const.SUCCESS:// 登录成功
			urb.setUserPassword(edit_pwd.getText().toString().trim());
			Toast.makeText(mContext,
					getResources().getString(R.string.login_success),
					Toast.LENGTH_SHORT).show();
			boolean position_on = sp.getBoolean("position_on", true);
			if (position_on) {// 处理用户位置信息
//				System.out.println("处理了上传位置");
//				getLocation();
				handleUploadLocation();
			}
			boolean remeberPwd = sp.getBoolean("remeberPwd", true);
			if (remeberPwd) {
				String password = edit_pwd.getText().toString().trim();
				editor.putString("password", password);
			}
			String username = edit_username.getText().toString().trim();
			editor.putString("username", username);
			editor.commit();
			if (Integer.parseInt(urb.getFill_profile()) == 0) {
				UIUtils.setCurrentFuncID(FuncID.ID_EPG);
				Intent intent = new Intent(mContext, EPGActivity.class);
				startActivity(intent);
				finish();
			} else {
				Intent intent = new Intent(mContext,
						LoginRegistrationBaseInfoActivity.class);
				startActivity(intent);
				finish();
			}
			break;
		case Const.LOGIN_EMAIL_NOT_ACTIVED:
			Toast.makeText(mContext,
					getResources().getString(R.string.login_email_not_actived),
					Toast.LENGTH_SHORT).show();
			break;
		case Const.LOGIN_USERNAME_PWD_ERROE:
			Toast.makeText(mContext,
					getResources().getString(R.string.login_pwd_error),
					Toast.LENGTH_SHORT).show();
			break;
		case Const.LOGIN_MSG_NOT_ACTIVED:
			Toast.makeText(mContext,
					getResources().getString(R.string.login_msg_not_activited),
					Toast.LENGTH_SHORT).show();
			break;
		case 111:
			UIUtils.ToastMessage(mContext, R.string.login_uname_error);
			break;
		default:
			break;
		}
	}


	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.auto_login:
			if (isChecked) {
				editor.putBoolean("autoLogin", true);
				editor.commit();
				if(!cb_remeber_pwd.isChecked()) {
					cb_remeber_pwd.setChecked(true);
					editor.putBoolean("remeberPwd", true);
					editor.commit();
				}
			} else {
				editor.putBoolean("autoLogin", false);
				editor.commit();
			}
			break;
		case R.id.remeber_pwd:
			if (isChecked) {
				editor.putBoolean("remeberPwd", true);
				editor.commit();
			} else {
				editor.putBoolean("remeberPwd", false);
				editor.commit();
			}
			break;
		default:
			break;
		}
	}

	private void handleGetLocation() {// 获得位置信息
		provider = LocationProvider.getInstance(this);
		provider.init();
		mBMapManager = provider.getmBMapManager();
		mBMapManager.start();
		provider.getLocation();
	}

	private void handleUploadLocation() {// 上传位置信息
		double lat = TivicGlobal.lat;
		double lon = TivicGlobal.lon;
//		 System.out.println("LoginActivity经度" + lon);
//		 System.out.println("LoginActivity纬度" + lat);
		if (lat != 0 && lon != 0) {
			urb.setLoactionBean(new LocationBean(lat, lon));
		}
		new AsyncTask<Void, Void, Integer>() {
			@Override
			protected Integer doInBackground(Void... params) {
				int result = loginImpl.updateUserLocation(urb.getLoactionBean()
						.getLon(), urb.getLoactionBean().getLat());
				return result;
			}

			protected void onPostExecute(Integer result) {
				if (result == 0) {
//					System.out.println("更新位置成功");
				}
			};
		}.execute();

	}

	@Override
	protected void onResume() {
		if (mBMapManager != null) {
			mBMapManager.start();
		}
		super.onResume();

	}

	@Override
	protected void onPause() {
		if (mBMapManager != null) {
			mBMapManager.stop();
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (mBMapManager != null) {
			mBMapManager.destroy();
			mBMapManager = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		ShowProgressDialog.dismiss();
		super.onStop();
	}
}
