package com.igrs.tivic.phone.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Animation.TAnimation;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.ShowProgressDialog;
import com.igrs.tivic.phone.Impl.LoginImpl;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;

public class LoginFindpwdActivity extends Activity implements OnClickListener {
	
	private ImageView iv_findpwd_mail, iv_findpwd_phone, iv_send_mail,
			iv_send_code, iv_next_step;
	private RelativeLayout rl_findpwd_mail, rl_findpwd_phone;
	private EditText et_mail, et_phone, et_code;
	private TextView tv_send_already, tv_time_count, tv_resend,tv_registied_account_login;
	private TimeCount timeCount;
	private Context mContext;
	private LoginImpl loginImpl = new LoginImpl();
	private String name;//用户登陆失败时用户已经输入的用户名
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_findpwd_main);
		name = getIntent().getStringExtra("name");
		timeCount = new TimeCount(60 * 1000, 1000);
		mContext = this;
		init();
		setListeners();
	}

	private void init() {
		iv_findpwd_mail = (ImageView) findViewById(R.id.iv_findpwd_mail);
		iv_findpwd_phone = (ImageView) findViewById(R.id.iv_findpwd_phone);
		iv_next_step = (ImageView) findViewById(R.id.iv_next_step);
		iv_send_mail = (ImageView) findViewById(R.id.iv_send_mail);
		iv_send_code = (ImageView) findViewById(R.id.iv_send_code);

		rl_findpwd_mail = (RelativeLayout) findViewById(R.id.rl_findpwd_mail);
		rl_findpwd_phone = (RelativeLayout) findViewById(R.id.rl_findpwd_phone);

		et_mail = (EditText) findViewById(R.id.et_mail);
		et_code = (EditText) findViewById(R.id.et_code);
		et_phone = (EditText) findViewById(R.id.et_phone);
		if(!TextUtils.isEmpty(name)) {
			if(Utils.isEmailValid(name)) {
				et_mail.setText(name);
				rl_findpwd_phone.setVisibility(View.INVISIBLE);
				rl_findpwd_mail.setVisibility(View.VISIBLE);
				iv_findpwd_mail.setImageResource(R.drawable.login_findpassword_mail_selected);
				iv_findpwd_phone.setImageResource(R.drawable.login_findpassword_phone_normal);
			} else {
				et_phone.setText(name);
				rl_findpwd_phone.setVisibility(View.VISIBLE);
				rl_findpwd_mail.setVisibility(View.INVISIBLE);
				iv_findpwd_mail.setImageResource(R.drawable.login_findpassword_mail_normal);
				iv_findpwd_phone.setImageResource(R.drawable.login_findpassword_phone_selected);
			}
		}
		tv_send_already = (TextView) findViewById(R.id.tv_send_already);
		tv_resend = (TextView) findViewById(R.id.tv_resend);
		tv_time_count = (TextView) findViewById(R.id.tv_time_count);
		tv_registied_account_login = (TextView) findViewById(R.id.registied_account_login);
		tv_registied_account_login.setText("<< "+getResources().getString(R.string.registied_account_login));
	}

	private void setListeners() {
		iv_findpwd_mail.setOnClickListener(this);
		iv_findpwd_phone.setOnClickListener(this);
		iv_next_step.setOnClickListener(this);
		iv_send_code.setOnClickListener(this);
		iv_send_mail.setOnClickListener(this);
		tv_registied_account_login.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_findpwd_mail:
			iv_findpwd_mail.setImageResource(R.drawable.login_findpassword_mail_selected);
			iv_findpwd_phone.setImageResource(R.drawable.login_findpassword_phone_normal);
			rl_findpwd_mail.setVisibility(View.VISIBLE);
			rl_findpwd_phone.setVisibility(View.GONE);
			break;
		case R.id.iv_findpwd_phone:
			iv_findpwd_mail.setImageResource(R.drawable.login_findpassword_mail_normal);
			iv_findpwd_phone.setImageResource(R.drawable.login_findpassword_phone_selected);
			rl_findpwd_mail.setVisibility(View.GONE);
			rl_findpwd_phone.setVisibility(View.VISIBLE);
			break;
		case R.id.iv_next_step:
//			if(tv_time_count.getVisibility()!=View.VISIBLE) {
//				Toast.makeText(mContext, getResources().getString(R.string.login_findpwd_get_code_first), Toast.LENGTH_SHORT).show();
//				return;
//			}
			final String vefifyNumber = et_code.getText().toString().trim();
			if(TextUtils.isEmpty(vefifyNumber)) {
				Toast.makeText(mContext, getResources().getString(R.string.login_regsited_code_empty),Toast.LENGTH_SHORT).show();
				return;
			}
			String phoneNumber = et_phone.getText().toString().trim();
			new AsyncTask<Void, Void, Integer>(){
				protected void onPreExecute() {
					ShowProgressDialog.show2(mContext, R.string.login_registed_handle, null);
				};

				@Override
				protected Integer doInBackground(Void... params) {
					int checkCodeResult = loginImpl.verifyCodeForPwd(vefifyNumber);
					return checkCodeResult;
				}
				
				protected void onPostExecute(Integer checkCodeResult) {
					ShowProgressDialog.dismiss();
//					System.out.println("checkCodeResult="+checkCodeResult);
					if(checkCodeResult==0) {//验证成功
//						System.out.println("验证成功");
						Intent intent = new Intent(mContext,LoginFindpwdPhoneActivity.class);
						intent.putExtra("verifyNumber", vefifyNumber);
						startActivity(intent);
						finish();
					} else if(checkCodeResult==Const.CHK_CODE_ERROR) {
						Toast.makeText(mContext, getResources().getString(R.string.login_regsited_phonecode_error), Toast.LENGTH_LONG).show();
					} else if(checkCodeResult==Const.USER_NOT_ACTIVED) {
						Toast.makeText(mContext, getResources().getString(R.string.login_regsited_phone_not_actived), Toast.LENGTH_LONG).show();
					} else if(checkCodeResult==Const.HTTP_EXCEPTION) {
						Toast.makeText(mContext, getResources().getString(R.string.login_request_exception), Toast.LENGTH_LONG).show();
					} else if(checkCodeResult == -1) {
						UIUtils.ToastMessage(mContext, R.string.net_error);
					}
				};
			}.execute();
			

			break;
		case R.id.iv_send_code:
			sendCode();
			break;
		case R.id.iv_send_mail:
			sendMail();
			break;
		case R.id.registied_account_login:
			Intent intent = new Intent(mContext,LoginActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}

	private void sendMail() {//发送验证邮件
		final String mail = et_mail.getText().toString().trim();
		if(TextUtils.isEmpty(mail)) {
			Toast.makeText(mContext, getResources().getString(R.string.login_findpwd_input_mail_error), Toast.LENGTH_SHORT).show();
			return;
		}
		if(!Utils.isEmailValid(mail)) {
			Toast.makeText(mContext, getResources().getString(R.string.user_mail_false), Toast.LENGTH_LONG).show();
			return;
		}
		new AsyncTask<Void, Void, Integer>(){
			protected void onPreExecute() {
			};

			@Override
			protected Integer doInBackground(Void... params) {
				int resultCode = loginImpl.checkUsrnameExist(mail);
				return resultCode;
			}
			
			protected void onPostExecute(Integer result) {
				if(result!=Const.USER_EXIST_YES) {
					Toast.makeText(mContext, getResources().getString(R.string.login_findpwd_input_correct_mail), Toast.LENGTH_SHORT).show();
					return;
				} else if(result == -1) {
					UIUtils.ToastMessage(mContext, R.string.net_error);
				}
			};
		}.execute();
		
		
		
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
					Intent intent = new Intent(mContext,LoginFindpwdMailActivity.class);
					intent.putExtra("username", mail);
					startActivity(intent);
					finish();
				} else if(result==2) {
					Toast.makeText(mContext, getResources().getString(R.string.login_register_email_send_forpwd_error), Toast.LENGTH_LONG).show();
				} else if(result==Const.HTTP_EXCEPTION) {
					Toast.makeText(mContext, getResources().getString(R.string.login_register_email_send_forpwd_exception), Toast.LENGTH_LONG).show();
				} else if (result == -1) {
					UIUtils.ToastMessage(mContext, R.string.net_error);
				}
			};
		}.execute();
		
//		int sendMailResult = loginImpl.sendMailForPwd(mail);
//		if(sendMailResult==0) {
//			Intent intent = new Intent(mContext,LoginFindpwdMailActivity.class);
//			intent.putExtra("username", mail);
//			startActivity(intent);
//			finish();
//		} else if(sendMailResult==2) {
//			Toast.makeText(mContext, getResources().getString(R.string.login_register_email_send_forpwd_error), Toast.LENGTH_LONG).show();
//		} else if(sendMailResult==Const.HTTP_EXCEPTION) {
//			Toast.makeText(mContext, getResources().getString(R.string.login_register_email_send_forpwd_exception), Toast.LENGTH_LONG).show();
//		}
		
	}

	private void sendCode() {
		final String phoneNumber = et_phone.getText().toString().trim();
		if(TextUtils.isEmpty(phoneNumber)) {
			Toast.makeText(mContext, getResources().getString(R.string.login_findpwd_input_number_error), Toast.LENGTH_SHORT).show();
			return;
		}
		if(!Utils.isPhoneNumValid(phoneNumber)) {
			Toast.makeText(mContext, getResources().getString(R.string.user_phone_false), Toast.LENGTH_LONG).show();
			return;
		}
		new AsyncTask<Void, Void, Integer>(){
			protected void onPreExecute() {
			};

			@Override
			protected Integer doInBackground(Void... params) {
				int resultCode = loginImpl.checkUsrnameExist(phoneNumber);
				return resultCode;
			}
			
			protected void onPostExecute(Integer result) {
				if(result!=Const.USER_EXIST_YES) {
					Toast.makeText(mContext, getResources().getString(R.string.login_findpwd_input_correct_number), Toast.LENGTH_SHORT).show();
					return;
				}else if (result == -1) {
					UIUtils.ToastMessage(mContext, R.string.net_error);
				}
			};
		}.execute();
		
		
		new AsyncTask<Void, Void, Integer>(){
			protected void onPreExecute() {
				ShowProgressDialog.show2(mContext,R.string.login_registed_handle, null);
			};

			@Override
			protected Integer doInBackground(Void... params) {
				int sendVerifyCode = loginImpl.sendMsgForPwd(phoneNumber);
				return sendVerifyCode;
			}
			
			protected void onPostExecute(Integer result) {
				ShowProgressDialog.dismiss();
				if(result==0) {//发送验证码成功
					tv_send_already.setText(getResources().getString(R.string.login_findpwd_send_code_already));
					tv_time_count.setVisibility(View.VISIBLE);
					tv_resend.setVisibility(View.VISIBLE);
					timeCount.start();
				} else if(result==2){
					Toast.makeText(mContext, getResources().getString(R.string.login_regsited_sendphone_error), Toast.LENGTH_SHORT).show();
				} else if(result==Const.HTTP_EXCEPTION) {
					Toast.makeText(mContext, getResources().getString(R.string.login_regsited_sendphone_error), Toast.LENGTH_SHORT).show();
				}else if (result == -1) {
					UIUtils.ToastMessage(mContext, R.string.net_error);
				}
			};
		}.execute();
		
		
	}

	

	private class TimeCount extends CountDownTimer {

		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			tv_time_count.setVisibility(View.INVISIBLE);
			tv_send_already.setText(getResources().getString(R.string.login_findpwd_click_resend));
			tv_resend.setText(getResources().getString(R.string.login_findpwd_get_new_code));
			iv_send_code.setVisibility(View.VISIBLE);
			iv_send_code.setImageResource(R.drawable.chongxinfasong);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			tv_time_count.setText(millisUntilFinished/1000+"");
			iv_send_code.setVisibility(View.INVISIBLE);
		}

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder builder = new Builder(mContext);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setTitle(R.string.login_registed_warning);
			builder.setMessage(R.string.login_registed_confirm_leave_modify_pwd);
			builder.setPositiveButton(getResources().getString(R.string.login_registed_confirm), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(mContext,LoginActivity.class);
					startActivity(intent);
					finish();
				}
			});
			builder.setNegativeButton(getResources().getString(R.string.login_registed_cancel), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
			builder.create().show();
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onStop() {
		ShowProgressDialog.dismiss();
		super.onStop();
	}
}
