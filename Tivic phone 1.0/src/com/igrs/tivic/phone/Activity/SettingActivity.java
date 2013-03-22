package com.igrs.tivic.phone.Activity;


import java.io.File;
import java.text.DecimalFormat;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Bean.Login.UserRegisterBean;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Impl.LoginImpl;
import com.igrs.tivic.phone.Utils.GDUtils;
import com.umeng.fb.UMFeedbackService;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends Activity implements OnCheckedChangeListener,
		OnClickListener {
	private CheckBox cb_guide_alert, cb_position_alert;
	private RelativeLayout rl_share, rl_modify_info, rl_modify_pwd,
			rl_clear_cache, rl_user_feedback, rl_score, rl_about, rl_logout,
			rl_version_update;
	private SharedPreferences sp;
	private TextView tv_cache_size;
	private Editor editor;
	public static final String GUIDE_SETTING = "guide_on";
	public static final String POSITION_SETTING = "position_on";
	private UserRegisterBean urb = TivicGlobal.getInstance().userRegisterBean;
	private LoginImpl loginImpl = new LoginImpl();
	private Context mContext;
	@Override
	public void onCreate(Bundle savedInstanceState) {
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		editor = sp.edit();
		mContext = this;
		init();
		initCheckBoxState();
		setListeners();
	}
	@Override
	protected void onStart() {
		boolean exit = getIntent().getBooleanExtra("exit", false);
		if(exit) {
			TivicGlobal.userRegisterBean = new UserRegisterBean();
			GDUtils.getGDApplication(mContext).destory();
			System.gc();
			finish();
		}
		super.onStart();
	}
	private void init() {
		cb_guide_alert = (CheckBox) findViewById(R.id.cb_guide_alert);
		cb_position_alert = (CheckBox) findViewById(R.id.cb_position_alert);
		tv_cache_size = (TextView) findViewById(R.id.tv_cache_size);
		tv_cache_size.setText(getDataSize(getCacheSize(getCacheDir())));//设置缓存的大小
		rl_about = (RelativeLayout) findViewById(R.id.rl_about);
		rl_clear_cache = (RelativeLayout) findViewById(R.id.rl_clear_cache);
		rl_logout = (RelativeLayout) findViewById(R.id.rl_logout);
		rl_modify_info = (RelativeLayout) findViewById(R.id.rl_modify_info);
		rl_modify_pwd = (RelativeLayout) findViewById(R.id.rl_modify_pwd);
		rl_score = (RelativeLayout) findViewById(R.id.rl_score);
		rl_share = (RelativeLayout) findViewById(R.id.rl_share);
		rl_user_feedback = (RelativeLayout) findViewById(R.id.rl_user_feedback);
		rl_version_update = (RelativeLayout) findViewById(R.id.rl_version_update);
	}

	private void initCheckBoxState() {
		boolean guide = sp.getBoolean(GUIDE_SETTING, false);
		if (guide) {
			cb_guide_alert.setChecked(true);
		}
		boolean position = sp.getBoolean(POSITION_SETTING, true);
		if (position) {
			cb_position_alert.setChecked(true);
		}
	}

	private void setListeners() {
		cb_guide_alert.setOnCheckedChangeListener(this);
		cb_position_alert.setOnCheckedChangeListener(this);
		rl_about.setOnClickListener(this);
		rl_clear_cache.setOnClickListener(this);
		rl_logout.setOnClickListener(this);
		rl_modify_info.setOnClickListener(this);
		rl_modify_pwd.setOnClickListener(this);
		rl_score.setOnClickListener(this);
		rl_share.setOnClickListener(this);
		rl_user_feedback.setOnClickListener(this);
		rl_version_update.setOnClickListener(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.cb_guide_alert:
			if(isChecked) {
				editor.putBoolean(GUIDE_SETTING, true);
				editor.commit();
				Toast.makeText(mContext, R.string.setting_guide_opened, Toast.LENGTH_SHORT).show();
			} else {
				editor.putBoolean(GUIDE_SETTING, false);
				editor.commit();
				Toast.makeText(mContext, R.string.setting_guide_closed, Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.cb_position_alert:
			if(isChecked) {
				editor.putBoolean(POSITION_SETTING, true);
				editor.commit();
				Toast.makeText(mContext,R.string.setting_position_opened, Toast.LENGTH_SHORT).show();
			} else {
				editor.putBoolean(POSITION_SETTING, false);
				editor.commit();
				Toast.makeText(mContext, R.string.setting_position_closed, Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_about:
			showAboutUsDialog();
			break;
		case R.id.rl_clear_cache:
			clearCache();
			
			break;
		case R.id.rl_logout:
			logout();
			break;
		case R.id.rl_modify_info:
			handleModifyInfo();
			break;
		case R.id.rl_modify_pwd:
			handleModifyPwd();
			break;
		case R.id.rl_score:
			handleScore();
			break;

		case R.id.rl_share:
			shareToFriend();
			break;
		case R.id.rl_user_feedback:
			//modify by zhanglirong in 2013.02.05
			Intent feedBackIntent = new Intent(mContext,FeedBackActivity.class);
			startActivity(feedBackIntent);
			//友盟用户反馈
			//UMFeedbackService.openUmengFeedbackSDK(this);
			break;
		case R.id.rl_version_update:
			getLastestVersion();//还未写好
			break;
		default:
			break;
		}
	}

	private void clearCache() {
		deleteFile(getCacheDir());
		tv_cache_size.setText(getDataSize(getCacheSize(getCacheDir())));
	}
	public long getCacheSize(File cache) {
		long size = 0;
		if(cache.exists()) {
			for(File file :cache.listFiles()) {
				if(!file.isDirectory()) {
					size+=file.length();
				} else {
					size+=getCacheSize(file);
				}
			}
		}
		return size;
	}
	public  void deleteFile(File cache) {
		for(File file : cache.listFiles()) {
			if(file.isFile()) {
				file.delete();
			} else {
				deleteFile(file);
				file.delete();
			}
		}
	}
	public static String getDataSize(long size) {
		DecimalFormat formater = new DecimalFormat("####.00");
		if(size<0) {
			size =0;
		}
		if(size==0) {
			return "0KB";
		}
		if(size<=1024) {
			return size+"bytes";
		} else if(size<=1024*1024)  {
			float kbSize = size/1024f;
			return formater.format(kbSize)+"KB";
		} else if(size<=1024*1024*1024) {
			float mbSize = size/1024/1024f;
			return formater.format(mbSize)+"M";
		} else {
			return "size error";
		}
	}
	private void handleScore() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		final AlertDialog ad = builder.create();
		View view = View.inflate(mContext, R.layout.setting_score, null);
		ad.show();
		ad.setContentView(view);
		Button confirm = (Button) view.findViewById(R.id.confirm);
		Button cancel = (Button)view.findViewById(R.id.cancel);
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(mContext, R.string.setting_after_score, Toast.LENGTH_SHORT).show();
				ad.dismiss();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ad.dismiss();
			}
		});
	}

	private void showAboutUsDialog() {
		AlertDialog.Builder builder  = new Builder(mContext);
		builder.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
		builder.setTitle(getResources().getString(R.string.setting_about));
		PackageManager pm = getPackageManager();
		PackageInfo pi;
		try {
			pi = pm.getPackageInfo(getPackageName(), 0);
			String versionName = pi.versionName;
//			String about = getResources().getString(R.string.setting_about);
			String appName = getResources().getString(R.string.setting_app_name);
			String version = getResources().getString(R.string.setting_version);
			String developer = getResources().getString(R.string.setting_developer);
			String detail = getResources().getString(R.string.setting_about_detail);
			String msg = appName+"\n"+version+versionName+"\n"+developer+"\n"+detail;
			builder.setMessage(msg);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		builder.setPositiveButton(getResources().getString(R.string.setting_modifypwd_confirm), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
			}
		});
		builder.create().show();
	}

	public void logout() {
		if(!TivicGlobal.getInstance().mIsLogin) {
			Toast.makeText(mContext, R.string.setting_login_first, Toast.LENGTH_SHORT).show();
		} else {
//			urb.setUserSID(null);
			TivicGlobal.getInstance().mIsLogin = false;
			TivicGlobal.getInstance().userRegisterBean = new UserRegisterBean();
			editor.putBoolean("autoLogin", false);
			editor.commit();
			Toast.makeText(mContext, R.string.setting_logout_success, Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(mContext,LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
		}
	}

	private void shareToFriend() {
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.setting_shareto_friend));
		shareIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.setting_share_detail));
		shareIntent = Intent.createChooser(shareIntent, getResources().getString(R.string.setting_share));
		startActivity(shareIntent);
	}

	public void getLastestVersion() {
		//modify by zhanglirong in 2013.02.05
//		new AsyncTask<Void, Void, StringBuilder>() {
//			@Override
//			protected void onPreExecute() {
//				pd = ProgressDialog.show(mContext, "", getResources().getString(R.string.setting_get_lastest_version));
//				super.onPreExecute();
//			}
//			@Override
//			protected StringBuilder doInBackground(Void... params) {
//				StringBuilder sb = loginImpl.getLastestVersion();
//				return sb;
//			}
//			@Override
//			protected void onPostExecute(StringBuilder result) {
//				pd.dismiss();
//				super.onPostExecute(result);
//			}
//		}.execute();
		//友盟版本更新下载服务
		UmengUpdateAgent.update(this);
		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
		        @Override
		        public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
		            switch (updateStatus) {
		            case 0: // has update
		                UmengUpdateAgent.showUpdateDialog(mContext, updateInfo);
		                break;
		            case 1: // has no update
		                Toast.makeText(mContext, R.string.has_no_update, Toast.LENGTH_SHORT)
		                        .show();
		                break;
		            case 2: // none wifi
		                Toast.makeText(mContext, R.string.none_wifi, Toast.LENGTH_SHORT)
		                        .show();
		                break;
		            case 3: // time out
		                Toast.makeText(mContext, R.string.time_out, Toast.LENGTH_SHORT)
		                        .show();
		                break;
		            }
		        }
		});
	}

	private void handleModifyInfo() {
		if(TivicGlobal.getInstance().mIsLogin) {
			Intent intent = new Intent(mContext,LoginRegistrationBaseInfoActivity.class);
			intent.putExtra("fromClass", getClass().getName());
			startActivity(intent);
		} else {
			Intent intent = new Intent(mContext,LoginDialogActivity.class);
			intent.putExtra("handleType", 1);//1代表修改个人资料，2代表修改密码
			startActivity(intent);
		}
		
	}

	private void handleModifyPwd() {//修改密码操作
		if(TivicGlobal.getInstance().mIsLogin) {
			Intent intent = new Intent(mContext,SettingModifyPwdActivity.class);
			startActivity(intent);
			
		} else {
			Intent intent = new Intent(mContext,LoginDialogActivity.class);
			intent.putExtra("handleType", 2);//1代表修改个人资料，2代表修改密码
			startActivity(intent);
		}
		
	}

}