package com.igrs.tivic.phone.Activity;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.ShowProgressDialog;
import com.igrs.tivic.phone.Impl.LoginImpl;
import com.igrs.tivic.phone.Utils.UIUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginFindpwdMailActivity extends Activity implements  OnClickListener{
	private TextView tv_registied_account_login,tv_resend_mail;
	private Context mContext;
	private ImageView iv_complete;
	private LoginImpl loginImpl = new LoginImpl();
//	private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_findpwd_mail_msg);
		mContext = this;
		init();
		setListeners();
	}
	private void init() {
		tv_registied_account_login = (TextView) findViewById(R.id.registied_account_login);
		String text = tv_registied_account_login.getText().toString();
		tv_registied_account_login.setText("<< "+text);
		tv_resend_mail = (TextView) findViewById(R.id.tv_resend_mail);
		iv_complete = (ImageView) findViewById(R.id.iv_complete);
	}
	private void setListeners() {
		tv_registied_account_login.setOnClickListener(this);
		tv_resend_mail.setOnClickListener(this);
		iv_complete.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.registied_account_login:
			Intent intent = new Intent(mContext,LoginActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.tv_resend_mail:
			resendMail();
			break;
		case R.id.iv_complete:
			Toast.makeText(mContext, getResources().getString(R.string.login_findpwd_reset_pwd), Toast.LENGTH_LONG).show();
			Intent loginIntent = new Intent(mContext,LoginActivity.class);
			loginIntent.putExtra("username", getIntent().getStringExtra("username"));
			startActivity(loginIntent);
			finish();
			break;
		default:
			break;
		}
		
	}
	private void resendMail() {
		final String mail = getIntent().getStringExtra("username");
		new AsyncTask<Void, Void, Integer>(){
			protected void onPreExecute() {
				ShowProgressDialog.show2(mContext,R.string.login_registed_handle, null);
			};

			@Override
			protected Integer doInBackground(Void... params) {
				int sendMailResult = loginImpl.sendMailForPwd(mail);
				return sendMailResult;
			}
			
			protected void onPostExecute(Integer result) {
				ShowProgressDialog.dismiss();
				if(result==0) {
					Toast.makeText(mContext, getResources().getString(R.string.login_register_email_send_code_text1), Toast.LENGTH_LONG).show();
				} else if(result==2) {
					Toast.makeText(mContext, getResources().getString(R.string.login_register_email_send_forpwd_error), Toast.LENGTH_LONG).show();
				} else if(result==Const.HTTP_EXCEPTION) {
					Toast.makeText(mContext, getResources().getString(R.string.login_register_email_send_forpwd_error), Toast.LENGTH_LONG).show();
				} else if (result == -1) {
					UIUtils.ToastMessage(mContext, R.string.net_error);
				}
			};
		}.execute();
		
		
		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			Intent loginIntent = new Intent(mContext,LoginActivity.class);
			loginIntent.putExtra("username", getIntent().getStringExtra("username"));
			startActivity(loginIntent);
			finish();
		}
			
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onStop() {
		ShowProgressDialog.dismiss();
		super.onStop();
	}
}
