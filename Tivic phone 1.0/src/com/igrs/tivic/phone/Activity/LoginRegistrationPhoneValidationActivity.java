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
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class LoginRegistrationPhoneValidationActivity extends Activity
		implements OnClickListener {

	private EditText et_check_phone_code;
	private TextView tv_count_time,tv_resend_phone_code,tv_registied_account_login,tv_send_code,tv_dismiss;
	private ImageView iv_resend_phone_code,iv_registed_completed;
	private LinearLayout ll_phone_code,ll_code_error_msg;
	private TimeCount timeCount;
	private Context mContext;
	private LoginImpl loginImpl = new LoginImpl();
	private static final int SEND_SMS_RESULT = 0;
	private static final int VERIFY_RESULT = 1;
	private UserRegisterBean userRegisterBean = TivicGlobal.getInstance().userRegisterBean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_registration_phone_validation);
		mContext = this;
		timeCount = new TimeCount(60*1000, 1000);
		timeCount.start();
		init();
		setListeners();
	}
	private void init() {
		et_check_phone_code = (EditText) findViewById(R.id.et_check_phone_code);
		et_check_phone_code.setOnEditorActionListener(new OnEditorActionListener() {

			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (event != null
						&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
					InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					in.hideSoftInputFromWindow(v.getApplicationWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
					register();
				}
				return false;
			}
		});
		tv_count_time = (TextView) findViewById(R.id.tv_count_time);
		tv_resend_phone_code = (TextView) findViewById(R.id.tv_resend_phone_code);
		iv_resend_phone_code = (ImageView) findViewById(R.id.iv_resend_phone_code);
		iv_registed_completed = (ImageView) findViewById(R.id.iv_registed_completed);
		ll_code_error_msg = (LinearLayout) findViewById(R.id.ll_code_error_msg);
		tv_dismiss = (TextView) findViewById(R.id.tv_dismiss);
		tv_send_code = (TextView) findViewById(R.id.tv_send_code);
		tv_registied_account_login = (TextView) findViewById(R.id.registied_account_login);
		String text = tv_registied_account_login.getText().toString();
		tv_registied_account_login.setText("<< "+text);
	}

	private void setListeners() {
		iv_resend_phone_code.setOnClickListener(this);
		iv_registed_completed.setOnClickListener(this);
		tv_registied_account_login.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_resend_phone_code:
			if(ll_code_error_msg.getVisibility()==View.VISIBLE) {
				ll_code_error_msg.setVisibility(View.INVISIBLE);
			}
			new AsyncTask<Void, Void, Integer>(){
				protected void onPreExecute() {
					ShowProgressDialog.show2(mContext,R.string.login_registed_handle, null);
				};

				@Override
				protected Integer doInBackground(Void... params) {
					int sendSmsResult = loginImpl.sendVerifySMS(userRegisterBean.getUserName());
					return sendSmsResult;
				}
				
				protected void onPostExecute(Integer result) {
					ShowProgressDialog.dismiss();
					handleResult(result,SEND_SMS_RESULT);
				};
			}.execute();
			
			break;
		case R.id.registied_account_login:
			Intent intent = new Intent(mContext,LoginActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.iv_registed_completed:
//			if(iv_resend_phone_code.getVisibility()==View.VISIBLE) {
//				Toast.makeText(mContext, getResources().getString(R.string.login_regsited_sendphone_again), Toast.LENGTH_SHORT).show();
//				return;
//			}
			register();
			break;
		default:
			break;
		}
	}
	private void register() {
		final String phoneNumber = userRegisterBean.getUserName();
		String password = userRegisterBean.getUserPassword();
		final String inputNumber = et_check_phone_code.getText().toString().trim();
		if(TextUtils.isEmpty(inputNumber)) {
			Toast.makeText(mContext, getResources().getString(R.string.login_regsited_code_empty), Toast.LENGTH_SHORT).show();
			return;
		}
		new AsyncTask<Void, Void, Integer>(){
			protected void onPreExecute() {
//				pd = ProgressDialog.show(mContext, "", getResources().getString(R.string.login_registed_handle));
			};

			@Override
			protected Integer doInBackground(Void... params) {
				int verifyResult = loginImpl.verifyPhoneNum(phoneNumber, inputNumber);
				return verifyResult;
			}
			
			protected void onPostExecute(Integer result) {
//				if(pd!=null && pd.isShowing()) {
//					pd.dismiss();
//				}
				handleResult(result,VERIFY_RESULT);
			};
		}.execute();
	}
	private void handleResult(int resultCode, int resultType) {
		switch (resultType) {
		case SEND_SMS_RESULT://处理重新发送短信结果
			if(resultCode==0) {
				iv_resend_phone_code.setVisibility(View.INVISIBLE);
				tv_resend_phone_code.setVisibility(View.GONE);
//				ll_phone_code.setVisibility(View.VISIBLE);
				tv_send_code.setVisibility(View.VISIBLE);
				tv_count_time.setVisibility(View.VISIBLE);
				tv_dismiss.setVisibility(View.VISIBLE);
				timeCount.start();
			} else if(resultCode==101) {
				Toast.makeText(mContext, getResources().getString(R.string.user_phone_false), Toast.LENGTH_SHORT).show();
			}else if (resultCode == -1) {
				UIUtils.ToastMessage(mContext, R.string.net_error);
			}else {
				Toast.makeText(mContext, getResources().getString(R.string.login_regsited_sendphone_error), Toast.LENGTH_SHORT).show();
			}
			break;
		case VERIFY_RESULT://处理短信验证结果
			if(resultCode==0) {//短信验证成功
				new AsyncTask<Void, Void, Integer>() {
					protected void onPreExecute() {
						ShowProgressDialog.show2(mContext,R.string.login_registed_handle, null);
					};
					@Override
					protected Integer doInBackground(Void... params) {
						int registerCode = loginImpl.registerByPhone(userRegisterBean.getUserName(),userRegisterBean.getUserPassword());
						return registerCode;
					}
					protected void onPostExecute(Integer result) {
						ShowProgressDialog.dismiss();
						if(result==0) {//通过手机注册成功
//							int loginCode = loginImpl.login(userRegisterBean.getUserName(), userRegisterBean.getUserPassword());
//							System.out.println("loginCode="+loginCode);
//							if(loginCode==0) {//登录成功
								Toast.makeText(mContext, getResources().getString(R.string.login_regsitedbyphone_success), Toast.LENGTH_SHORT).show();
								Intent intent = new Intent(mContext,LoginRegistrationBaseInfoActivity.class);
								startActivity(intent);
								finish();
//							} else if(loginCode==Const.SOCKET_TIMEOUT_EXCEPTION){
//								Toast.makeText(mContext, getResources().getString(R.string.login_request_time_out), Toast.LENGTH_SHORT).show();
//							}
						} else if (result == -1) {
							UIUtils.ToastMessage(mContext, R.string.net_error);
						}else {
							Toast.makeText(mContext, getResources().getString(R.string.login_regsitedbyphone_error), Toast.LENGTH_SHORT).show();
						}
					};
				}.execute();
			

				
			} else if(resultCode==1) {
				Toast.makeText(mContext, getResources().getString(R.string.user_phone_false), Toast.LENGTH_SHORT).show();
			}else if(resultCode==2) {
				ll_code_error_msg.setVisibility(View.VISIBLE);
				Toast.makeText(mContext, getResources().getString(R.string.login_regsited_phonecode_error), Toast.LENGTH_SHORT).show();
			}else if (resultCode == -1) {
				UIUtils.ToastMessage(mContext, R.string.net_error);
			}
			break;
		default:
			break;
		}
	}
	class TimeCount extends CountDownTimer {
		
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			tv_send_code.setVisibility(View.GONE);
			tv_count_time.setVisibility(View.GONE);
			tv_dismiss.setVisibility(View.GONE);
			tv_resend_phone_code.setVisibility(View.VISIBLE);
			iv_resend_phone_code.setVisibility(View.VISIBLE);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			tv_count_time.setText(millisUntilFinished / 1000 + "");
		}
		
	}
	@Override
	protected void onStop() {
		ShowProgressDialog.dismiss();
		super.onStop();
	}
}
