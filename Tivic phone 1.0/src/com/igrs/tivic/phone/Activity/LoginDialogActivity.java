package com.igrs.tivic.phone.Activity;


import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Impl.LoginImpl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class LoginDialogActivity extends Activity {
	private ViewSwitcher mViewSwitcher;
	private ImageButton btn_close;
	private Button btn_login;
	private AutoCompleteTextView mAccount;
	private EditText et_pwd;
	private AnimationDrawable loadingAnimation;
	private View loginLoading;
//	private CheckBox cb_rememberMe;
	// private int curLoginType;
	private InputMethodManager imm;
	private Context mContext;
	private LoginImpl loginImpl = new LoginImpl();
	private int handleType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_dialog);
		handleType = getIntent().getIntExtra("handleType", 0);
		mContext = this;
		init();
		setListeners();
	}

	private void init() {
		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		// curLoginType = getIntent().getIntExtra("LOGINTYPE", LOGIN_OTHER);
		mViewSwitcher = (ViewSwitcher) findViewById(R.id.logindialog_view_switcher);
		loginLoading = (View) findViewById(R.id.login_loading);
		mAccount = (AutoCompleteTextView) findViewById(R.id.login_account);
		et_pwd = (EditText) findViewById(R.id.login_password);
//		cb_rememberMe = (CheckBox) findViewById(R.id.login_checkbox_rememberMe);
		btn_close = (ImageButton) findViewById(R.id.login_close_button);
		btn_login = (Button) findViewById(R.id.login_btn_login);
	}

	private void setListeners() {
		btn_close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btn_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
				
				final String username = mAccount.getText().toString();
				final String pwd = et_pwd.getText().toString().toString();
//				boolean isRememberMe = chb_rememberMe.isChecked();
				//判断输入
				if(TextUtils.isEmpty(username)) {
					Toast.makeText(mContext, R.string.login_dialog_username_error,Toast.LENGTH_SHORT).show();
					return;
				}
				if(TextUtils.isEmpty(pwd)) {
					Toast.makeText(mContext, R.string.login_dialog_pwd_error,Toast.LENGTH_SHORT).show();
					return;
				}
		        btn_close.setVisibility(View.GONE);
		        loadingAnimation = (AnimationDrawable)loginLoading.getBackground();
		        loadingAnimation.start();
		        mViewSwitcher.showNext();
		        
		        new AsyncTask<Void, Void, Integer>() {//登录
		        	protected void onPreExecute() {
		        		
		        	};
					@Override
					protected Integer doInBackground(Void... params) {
						int result = loginImpl.login(username, pwd);
						return result;
					}
					protected void onPostExecute(Integer result) {
						handle(result);
					}
					
		        	
		        }.execute();
				
			}
		});
	}
	private void handle(Integer result) {
		switch (result) {
		case Const.USERLEGALERROR:
			Toast.makeText(mContext, getResources().getString(R.string.user_legal_error), Toast.LENGTH_SHORT).show();
			mViewSwitcher.showPrevious();
			btn_close.setVisibility(View.VISIBLE);
			break;	
		case Const.PWDVLEGALERROR:
			Toast.makeText(mContext, getResources().getString(R.string.pwd_legal_error), Toast.LENGTH_SHORT).show();
			mViewSwitcher.showPrevious();
			btn_close.setVisibility(View.VISIBLE);
			break;
		case Const.HTTP_EXCEPTION:
			Toast.makeText(mContext, getResources().getString(R.string.login_error), Toast.LENGTH_SHORT).show();
			mViewSwitcher.showPrevious();
			btn_close.setVisibility(View.VISIBLE);
			break;
		case Const.SOCKET_TIMEOUT_EXCEPTION:
			Toast.makeText(mContext, getResources().getString(R.string.login_time_out), Toast.LENGTH_SHORT).show();
			mViewSwitcher.showPrevious();
			btn_close.setVisibility(View.VISIBLE);
			break;
		case Const.SUCCESS://登录成功
			Toast.makeText(mContext,getResources().getString(R.string.login_success), Toast.LENGTH_SHORT).show();
			if(handleType==1) {//跳转到修改用户资料界面
				Intent intent = new Intent(mContext,LoginRegistrationBaseInfoActivity.class);
				intent.putExtra("fromClass", getClass().getName());
				startActivity(intent);
				finish();
			} else if(handleType==2) {//跳转到修改密码界面
				Intent intent = new Intent(mContext,SettingModifyPwdActivity.class);
//				intent.putExtra("password", et_pwd.getText().toString());
				startActivity(intent);
				finish();
			}
			break;
		case Const.LOGIN_EMAIL_NOT_ACTIVED:					
			Toast.makeText(mContext,getResources().getString(R.string.login_email_not_actived), Toast.LENGTH_SHORT).show();
			btn_close.setVisibility(View.VISIBLE);
			mViewSwitcher.showPrevious();
			break;
		case Const.LOGIN_USERNAME_PWD_ERROE:
			Toast.makeText(mContext,getResources().getString(R.string.login_uname_pwd_error), Toast.LENGTH_SHORT).show();
			btn_close.setVisibility(View.VISIBLE);
			mViewSwitcher.showPrevious();
			break;
		case Const.LOGIN_MSG_NOT_ACTIVED:
			mViewSwitcher.showPrevious();
			btn_close.setVisibility(View.VISIBLE);
			Toast.makeText(mContext, getResources().getString(R.string.login_msg_not_activited), Toast.LENGTH_SHORT).show();
		default:
			break;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.onDestroy();
		}
		return super.onKeyDown(keyCode, event);
	}
}
