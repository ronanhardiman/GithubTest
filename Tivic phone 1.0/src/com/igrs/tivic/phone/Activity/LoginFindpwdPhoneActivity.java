package com.igrs.tivic.phone.Activity;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.ShowProgressDialog;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Impl.LoginImpl;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginFindpwdPhoneActivity extends Activity implements OnClickListener {
	private TextView tv_registied_account_login;
	private EditText et_newpwd,et_repeat_newpwd;
	private ImageView iv_complete;
	private Context mContext;
	private String verifyNumber;
	private LoginImpl loginImpl = new LoginImpl();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_findpwd_phone_enterpwd);
		mContext = this;
		if(getIntent()!=null) {
			verifyNumber = getIntent().getStringExtra("verifyNumber");
		}
		init();
		setListeners();
	}
	private void init() {
		tv_registied_account_login = (TextView) findViewById(R.id.registied_account_login);
		String text = tv_registied_account_login.getText().toString();
		tv_registied_account_login.setText("<< "+text);
		et_newpwd =(EditText) findViewById(R.id.et_newpwd);
		et_repeat_newpwd = (EditText) findViewById(R.id.et_repeat_newpwd);
		iv_complete = (ImageView) findViewById(R.id.iv_complete);
	}
	private void setListeners() {
		tv_registied_account_login.setOnClickListener(this);
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
		case R.id.iv_complete:
			modifyPwd();
			break;
		default:
			break;
		}
		
	}
	private void modifyPwd() {
		final String newPwd = et_newpwd.getText().toString().trim();
		String repeatPwd = et_repeat_newpwd.getText().toString().trim();
		if(TextUtils.isEmpty(newPwd)) {
			Toast.makeText(mContext, getResources().getString(R.string.newpwd_valid_error), Toast.LENGTH_LONG).show();
			return;
		}
		if(!Utils.isPasswordValid(newPwd)) {
			Toast.makeText(mContext, getResources().getString(R.string.user_pwd_faild), Toast.LENGTH_LONG).show();
			return;
		}
		if(TextUtils.isEmpty(repeatPwd)) {
			Toast.makeText(mContext, getResources().getString(R.string.newpwd_repeat_valid_error), Toast.LENGTH_LONG).show();
			return;
		}
		if(!newPwd.equals(repeatPwd)) {
			Toast.makeText(mContext, getResources().getString(R.string.newpwd_not_equal), Toast.LENGTH_LONG).show();
			return;
		}
//		if(!TextUtils.isEmpty(verifyNumber)) {
//		
//		}
		new AsyncTask<Void, Void, Integer>(){
			protected void onPreExecute() {
				ShowProgressDialog.show2(mContext,R.string.login_registed_handle, null);
			};

			@Override
			protected Integer doInBackground(Void... params) {
				int result = loginImpl.modifyPwdByPhone(TivicGlobal.getInstance().userRegisterBean.getUserUID()+"", verifyNumber, newPwd);
				return result;
			}
			
			protected void onPostExecute(Integer result) {
				ShowProgressDialog.dismiss();
				if(result==0) {//修改成功
					Toast.makeText(mContext, getResources().getString(R.string.login_findpwd_modify_success), Toast.LENGTH_LONG).show();
					Intent intent = new Intent(mContext,LoginActivity.class);
					startActivity(intent);
					finish();
				} else if(result==2) {
					Toast.makeText(mContext, getResources().getString(R.string.login_findpwd_modify_code_error), Toast.LENGTH_LONG).show();
				} else if(result==Const.HTTP_EXCEPTION) {
					Toast.makeText(mContext, getResources().getString(R.string.login_findpwd_modify_fail), Toast.LENGTH_LONG).show();
				}else if (result == -1) {
					UIUtils.ToastMessage(mContext, R.string.net_error);
				}
			};
		}.execute();
		
	
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
