package com.igrs.tivic.phone.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.MailTo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Bean.Base.UserBean;
import com.igrs.tivic.phone.Bean.Login.UserRegisterBean;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.ShowProgressDialog;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Impl.LoginImpl;
import com.igrs.tivic.phone.Interface.ILogin;
import com.igrs.tivic.phone.Utils.JsonForRequest;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;

public class LoginRegistrationActivity extends Activity implements
		OnClickListener {
	public Context mContext;
	private TextView tv_registied_account_login;
	private final String TAG = "RegistrationActivity";
	private Button bt_login_register_next;
	private EditText ed_re_user_account, ed_re_user_pwd, ed_re_user_pwd_again;
	private String user_phoneoremail;
	private String user_pwd;
	private LoginImpl register = new LoginImpl();
	private UserRegisterBean userRegisterBean = TivicGlobal.getInstance().userRegisterBean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.login_registration_main);
		init();
	}

	private void init() {
		tv_registied_account_login = (TextView) findViewById(R.id.registied_account_login);
		String text = tv_registied_account_login.getText().toString();
		tv_registied_account_login.setText("<< "+text);
		tv_registied_account_login.setOnClickListener(this);
		bt_login_register_next = (Button) findViewById(R.id.bt_login_register_next);
		bt_login_register_next.setOnClickListener(this);
		ed_re_user_account = (EditText) findViewById(R.id.ed_re_user_account);
		ed_re_user_pwd = (EditText) findViewById(R.id.ed_re_user_pwd);
		ed_re_user_pwd_again = (EditText) findViewById(R.id.ed_re_user_pwd_again);
		ed_re_user_pwd_again.setOnEditorActionListener(new OnEditorActionListener() {

			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (event != null
						&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
					InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					in.hideSoftInputFromWindow(v.getApplicationWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
					handleNext();
				}
				return false;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.registied_account_login:
			TivicGlobal.getInstance().mIsLogin = false;
			try {
				Intent intent = new Intent();
				intent.setClass(this, LoginActivity.class);
				startActivity(intent);
				UIUtils.setCurrentFuncID(FuncID.ID_EPG);
			} catch (Exception e) {
				Log.e(TAG, "startActivity error! " + e.toString());
			}
			finish();
			break;
		case R.id.bt_login_register_next:
			handleNext();
			break;
		default:
			break;
		}
	}
	public void handleNext() {
		TivicGlobal.getInstance().mIsLogin = false;
		user_phoneoremail = ed_re_user_account.getText().toString().trim();
		user_pwd = ed_re_user_pwd.getText().toString().trim();
		String user_pwd_again = ed_re_user_pwd_again.getText().toString().trim();
		if (Utils.isPhoneNumValid(user_phoneoremail)==false
				&& Utils.isEmailValid(user_phoneoremail)==false) {
			Toast.makeText(
					mContext,
					getResources().getString(
							R.string.user_emial_phone_false),
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!Utils.isPasswordValid(user_pwd)) {
			Toast.makeText(mContext,
					getResources().getString(R.string.user_pwd_faild),
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!(user_pwd.equals(user_pwd_again))) {
			Toast.makeText(
					mContext,
					getResources().getString(R.string.user_pwd_again_faild),
					Toast.LENGTH_SHORT).show();
			return;
		}
		new AsyncTask<Void, Void, Integer>(){
			protected void onPreExecute() {
				bt_login_register_next.setClickable(false);
			};

			@Override
			protected Integer doInBackground(Void... params) {
				int ret = register.checkUsrnameExist(ed_re_user_account.getText()
						.toString().trim());
				return ret;
			}
			
			protected void onPostExecute(Integer result) {
				ShowProgressDialog.dismiss();
				bt_login_register_next.setClickable(true);
				handleResult(result);
			};
		}.execute();
	}
	protected void handleResult(Integer result) {
		switch (result) {
		case -1:
				UIUtils.ToastMessage(mContext, R.string.net_error);
		break;
		case Const.USER_MAIL_FALSE:
			Toast.makeText(mContext,
					getResources().getText(R.string.user_mail_false),
					Toast.LENGTH_SHORT).show();
			break;
		case Const.USER_PHONE_FALSE:
			Toast.makeText(mContext,
					getResources().getText(R.string.user_phone_false),
					Toast.LENGTH_SHORT).show();
			break;
		case Const.USER_EXIST_NO:
			try {
				if(Utils.isEmailValid(user_phoneoremail)) {//发送邮件
					new AsyncTask<Void, Void, Integer>() {
						@Override
						protected void onPostExecute(Integer result) {
							ShowProgressDialog.dismiss();
							handleMailResult(result);
							super.onPostExecute(result);
						}
						
						@Override
						protected void onPreExecute() {
							ShowProgressDialog.show2(mContext,R.string.login_registed_handle, null);
							super.onPreExecute();
						}

						@Override
						protected Integer doInBackground(Void... params) {
							int sendMailResult = register.registerByMail(user_phoneoremail, user_pwd);
							return sendMailResult;
						}
						
					}.execute();
				
					
					
				} else {//发送短信
					new AsyncTask<Void, Void, Integer>() {
						@Override
						protected void onPostExecute(Integer result) {
							ShowProgressDialog.dismiss();
							handleSmsResult(result);
							super.onPostExecute(result);
						}
						
						@Override
						protected void onPreExecute() {
							ShowProgressDialog.show2(mContext,R.string.login_registed_handle, null);
							super.onPreExecute();
						}

						@Override
						protected Integer doInBackground(Void... params) {
							int sendSmsResult = register.sendVerifySMS(user_phoneoremail);
							return sendSmsResult;
						}
						
					}.execute();
				}
			} catch (Exception e) {
				Log.e(TAG, "startActivity error! " + e.toString());
			}
			break;
		case Const.USER_EXIST_YES://!!!!这里假设可以重新发送短信
			Toast.makeText(mContext,
					getResources().getText(R.string.user_exist_yes),
					Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
		
	}

	private void handleMailResult(int sendMailResult) {
		if(sendMailResult==Const.HTTP_SUCCESS) {//注册成功但邮箱未激活
			userRegisterBean.setUserName(user_phoneoremail);
			userRegisterBean.setUserPassword(user_pwd);
			Intent intent = new Intent(mContext,LoginRegistrationMailValidationActivity.class);
			startActivity(intent);
			finish();
			
		} else if(sendMailResult==1) {//邮件发送失败
			Toast.makeText(mContext, getResources().getString(R.string.login_register_email_send_error), Toast.LENGTH_LONG).show();
			return;
		} else if(sendMailResult==100) {
			Toast.makeText(mContext, getResources().getString(R.string.login_register_email_error), Toast.LENGTH_LONG).show();
			return;
		}else if (sendMailResult == -1) {
			UIUtils.ToastMessage(mContext, R.string.net_error);
			return;
		}
	}

	private void handleSmsResult(int sendSmsResult) {
		if(sendSmsResult==0) {//短信发送成功
			Intent intent = new Intent(mContext,LoginRegistrationPhoneValidationActivity.class);
			userRegisterBean.setUserName(user_phoneoremail);
			userRegisterBean.setUserPassword(user_pwd);
			intent.putExtra("username",user_phoneoremail);
			intent.putExtra("password", user_pwd);
			startActivity(intent);
			finish();
		} else if (sendSmsResult == -1) {
			UIUtils.ToastMessage(mContext, R.string.net_error);
		}else if(sendSmsResult==101) {
			Toast.makeText(mContext, getResources().getString(R.string.user_phone_false), Toast.LENGTH_SHORT).show();
		}else {
			Toast.makeText(mContext, getResources().getString(R.string.login_regsited_sendphone_error), Toast.LENGTH_SHORT).show();
		}
		
	}
	@Override
	protected void onStop() {
		ShowProgressDialog.dismiss();
		super.onStop();
	}
}
