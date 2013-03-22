package com.igrs.tivic.phone.Activity;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Bean.Login.UserRegisterBean;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.ShowProgressDialog;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Impl.LoginImpl;
import com.igrs.tivic.phone.Utils.UIUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginRegistrationMailValidationActivity extends Activity implements OnClickListener{
	private Context mContext;
	private UserRegisterBean userRegisterBean = TivicGlobal.getInstance().userRegisterBean;
	private ImageView iv_complete;
	private TextView tv_send_msg_again,tv_registied_account_login,tv_login_register_email_send_success,tv_login_register_email;
	private LoginImpl loginImpl = new LoginImpl();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_registration_validation);
		mContext = this;
		init();
		setListeners();
	}
	
	private void init() {
		tv_registied_account_login = (TextView) findViewById(R.id.registied_account_login);
		String text = tv_registied_account_login.getText().toString();
		tv_registied_account_login.setText("<< "+text);
		tv_send_msg_again = (TextView) findViewById(R.id.tv_send_msg_again);
		iv_complete = (ImageView) findViewById(R.id.iv_complete);
		tv_login_register_email_send_success = (TextView) findViewById(R.id.login_register_email_send_success);
		tv_login_register_email = (TextView) findViewById(R.id.login_register_email);
	
		
		tv_login_register_email.setVisibility(View.VISIBLE);
		tv_login_register_email.setText(userRegisterBean.getUserName());
	}
	private void setListeners() {
		tv_registied_account_login.setOnClickListener(this);
		tv_send_msg_again.setOnClickListener(this);
		iv_complete.setOnClickListener(this);
	}
//	public boolean isMailActivated() {
//		int code = loginImpl.login(userRegisterBean.getUserName(), userRegisterBean.getUserPassword());
//		System.out.println("code="+code);
//		return code==0;//0表示登录成功
//	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.registied_account_login:
			Intent intent = new Intent(mContext,LoginActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.iv_complete:
			new AsyncTask<Void, Void, Integer>(){
				protected void onPreExecute() {
					ShowProgressDialog.show2(mContext,R.string.login_registed_handle, null);
				};

				@Override
				protected Integer doInBackground(Void... params) {
					int code = loginImpl.login(userRegisterBean.getUserName(), userRegisterBean.getUserPassword());
//					System.out.println("code="+code);
					return code;
				}
				
				protected void onPostExecute(Integer result) {
					ShowProgressDialog.dismiss();
					handleResult(result);
				}

			
			}.execute();
			
			break;
		case R.id.tv_send_msg_again:
			sendMsgAgain();
			break;
		default:
			break;
		}
		
	}

	protected void handleResult(Integer result) {
		if(result!=0) {
			tv_login_register_email_send_success.setText(getResources().getString(R.string.login_register_email_send_code_text4));
//			tv_login_register_email.setVisibility(View.VISIBLE);
//			tv_login_register_email.setText(userRegisterBean.getUserName());
			tv_login_register_email.setVisibility(View.GONE);
		}else {
			Intent basicInfoIntent = new Intent(mContext,LoginRegistrationBaseInfoActivity.class);
			startActivity(basicInfoIntent);
			finish();
		}
		
	}

	private void sendMsgAgain() {
		new AsyncTask<Void, Void, Integer>(){
			protected void onPreExecute() {
				ShowProgressDialog.show2(mContext,R.string.login_registed_handle, null);
			};

			@Override
			protected Integer doInBackground(Void... params) {
				int result = loginImpl.resendMail(userRegisterBean.getUserName());
				return result;
			}
			
			protected void onPostExecute(Integer result) {
				ShowProgressDialog.dismiss();
				if(result==0) {
					tv_login_register_email_send_success.setText(getResources().getString(R.string.login_register_email_send_code_text3));
					tv_login_register_email.setVisibility(View.GONE);
					Toast.makeText(mContext, getResources().getString(R.string.login_regsited_sendmail_again_success), Toast.LENGTH_LONG).show();
				} else if(result==2) {
					Toast.makeText(mContext, getResources().getString(R.string.login_regsited_sendmail_again_fail), Toast.LENGTH_LONG).show();
				}else if (result == -1) {
					UIUtils.ToastMessage(mContext, R.string.net_error);
				}
			};
		}.execute();
	}
	@Override
	protected void onStop() {
		ShowProgressDialog.dismiss();
		super.onStop();
	}
}
