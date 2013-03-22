package com.igrs.tivic.phone.Activity;

import java.io.File;
import java.io.IOException;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Adapter.SystemIconAdapter;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginRegistrationSystemAvaterActivity extends Activity implements OnClickListener, OnItemClickListener {
	private Button bt_back,bt_complete;
	private GridView gv_system_icon;
	private SystemIconAdapter adapter;
	private Context mContext;
	private int selectedCount = 0;
	private int selectedPosition = -1;
	private Bitmap bitmap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_registed_baseinfo_system_icon);
		mContext = this;
		adapter = new SystemIconAdapter(mContext);
		init();
		setListeners();
	}
	
	private void init() {
		bt_back = (Button) findViewById(R.id.bt_back);
		bt_complete = (Button) findViewById(R.id.bt_complete);
		gv_system_icon = (GridView) findViewById(R.id.gv_system_icon);
		gv_system_icon.setAdapter(adapter);
	}
	private void setListeners() {
		bt_back.setOnClickListener(this);
		bt_complete.setOnClickListener(this);
		gv_system_icon.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_back:
			finish();
			break;
		case R.id.bt_complete:
			handleComplete();
			break;
		default:
			break;
		}
	}

	private void handleComplete() {
		if(selectedCount==0) {
			finish();
		} else {
			File dir = new File(Const.TIVIC_STORE_PATH);
			if(!dir.exists()) {
				dir.mkdir();
			}
			String fileName = Const.TIVIC_STORE_PATH+"/my_avater_"+selectedPosition+".jpg";
			File image = new File(fileName);
			if(!image.exists()) {
				try {
					Utils.saveImageToSD(fileName, bitmap, 100);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			TivicGlobal.getInstance().userRegisterBean.setUserAvartarPath(image.getPath());
			TivicGlobal.getInstance().userRegisterBean.setModifyUserAvatar(true);
			TivicGlobal.getInstance().userRegisterBean.setSuccessModifyUserAvater(false);
			String from = getIntent().getStringExtra("from");
				if(from==null || TextUtils.isEmpty(from)) {
//					Intent intent = new Intent(mContext,SocialBaseActivity.class);
//					intent.putExtra("myImagePath", image.getPath());
//					startActivity(intent);
//					finish();
//					System.out.println("LoginRegistrationBaseInfoActivity");
					Intent intent = new Intent();
					intent.putExtra("imagePath", image.getPath());
					setResult(RESULT_OK, intent);
					finish();
			}else {
				if(from.equals("register")) {
//					Intent intent = new Intent(mContext,LoginRegistrationBaseInfoActivity.class);
//					intent.putExtra("imagePath", image.getPath());
//					startActivity(intent);
					Intent intent = new Intent();
					intent.putExtra("imagePath", image.getPath());
					setResult(RESULT_OK, intent);
					finish();
				} 
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
//		ImageView iv_selected = (ImageView) view.findViewById(R.id.iv_selected);
//		if(iv_selected.getVisibility() == View.VISIBLE) {
//			iv_selected.setVisibility(View.INVISIBLE);
//			selectedCount--;
//		} else {
//			if(selectedCount==1) {
//				Toast.makeText(mContext, "您只能选择一个头像", Toast.LENGTH_SHORT).show();
//				return;
//			}
//			selectedPosition = position;
//			iv_selected.setVisibility(View.VISIBLE);
//			
//			ImageView iv_avater = (ImageView) view.findViewById(R.id.iv_poiitem);
//			BitmapDrawable bd = (BitmapDrawable) iv_avater.getDrawable();
//			bitmap = bd.getBitmap();
//			selectedCount++;
//		}
		for(int i=0; i<adapter.getIcons().length; i++) {
			View subView = parent.getChildAt(i);
			ImageView iv_selected = (ImageView) subView.findViewById(R.id.iv_selected);
			iv_selected.setVisibility(View.INVISIBLE);
		}
		ImageView iv_selected = (ImageView) view.findViewById(R.id.iv_selected);
		iv_selected.setVisibility(View.VISIBLE);
		selectedPosition = position;
		iv_selected.setVisibility(View.VISIBLE);
		
		ImageView iv_avater = (ImageView) view.findViewById(R.id.iv_poiitem);
		BitmapDrawable bd = (BitmapDrawable) iv_avater.getDrawable();
		bitmap = bd.getBitmap();
		selectedCount = 1;
	}
}
