package com.igrs.tivic.phone.Activity;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Bean.Login.UserRegisterBean;
import com.igrs.tivic.phone.Global.ShowProgressDialog;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Impl.LoginImpl;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class SettingModifyPwdActivity extends Activity implements OnClickListener {
	private EditText et_new_pwd,et_repeat_pwd,et_pwd;
	private Button bt_confirm;
	private ImageButton bt_close;
	private Context mContext;
	private LoginImpl loginImpl = new LoginImpl();
	private UserRegisterBean urb = TivicGlobal.getInstance().userRegisterBean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_modify_pwd);
		mContext = this;
		init();
		setListeners();
	}
	private void init() {
		et_pwd = (EditText) findViewById(R.id.et_pwd);
		et_new_pwd = (EditText) findViewById(R.id.et_new_pwd);
		et_repeat_pwd = (EditText) findViewById(R.id.et_repeat_pwd);
		bt_confirm = (Button) findViewById(R.id.bt_confirm);
		bt_close = (ImageButton) findViewById(R.id.bt_close);
	}
	private void setListeners() {
		bt_confirm.setOnClickListener(this);
		bt_close.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_confirm:
			modifyPwd();
			break;
		case R.id.bt_close:
			finish();
			break;
		default:
			break;
		}
	}
	private void modifyPwd() {
		final String pwd = et_pwd.getText().toString();
		final String newPwd = et_new_pwd.getText().toString();
		String repeatPwd = et_repeat_pwd.getText().toString();
		if(TextUtils.isEmpty(pwd)) {
			Toast.makeText(mContext, R.string.setting_modifypwd_input_pwd_error, Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(newPwd)) {
			Toast.makeText(mContext, R.string.setting_modifypwd_input_newpwd_error, Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(repeatPwd)) {
			Toast.makeText(mContext, R.string.setting_modifypwd_repeat_input_newpwd_error, Toast.LENGTH_SHORT).show();
			return;
		}
		if(!newPwd.equals(repeatPwd)) {
			Toast.makeText(mContext, R.string.setting_modifypwd_newpwd_not_equal, Toast.LENGTH_SHORT).show();
			return;
		}
		new AsyncTask<Void, Void, Integer>(){
			protected void onPreExecute() {
				ShowProgressDialog.show2(mContext,R.string.login_registed_handle, null);
			};
			@Override
			protected Integer doInBackground(Void... params) {
				int result = loginImpl.modifyPwd(pwd, newPwd);
				return result;
			}
			protected void onPostExecute(Integer result) {
				ShowProgressDialog.dismiss();
				if(result==0) {
					Toast.makeText(mContext, R.string.setting_modifypwd_success, Toast.LENGTH_LONG).show();
					finish();
				} else if(result==2) {
					Toast.makeText(mContext, R.string.setting_modifypwd_error, Toast.LENGTH_SHORT).show();
				}
			};
		}.execute();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.onDestroy();
		}
		return super.onKeyDown(keyCode, event);
	}
}
