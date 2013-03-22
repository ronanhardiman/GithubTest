package com.igrs.tivic.phone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Utils.UIUtils;

public class UGCJumptActivity extends BaseActivity {

	private String TAG = "UGCJumptActivity";
	float distance = 0.0f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this.getApplicationContext();
		initUGCJumptLayout();
	}

	private void initUGCJumptLayout() {
		
		Button jump = (Button)findViewById(R.id.id_jump_to_pgc);
		jump.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view)
			{
				jumpToUGC();
			}
		});
		
		ImageView image = (ImageView) findViewById(R.id.image);
		if (image != null)
			image.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						jumpToUGC();
						break;
					case MotionEvent.ACTION_UP:

						break;
					}
					return false;
				}
			});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		UIUtils.setCurrentFuncID(FuncID.ID_UGC_JUMP);
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	private void jumpToUGC()
	{
		try {
			Intent intent = new Intent();
			intent.setClassName("com.igrs.tivic.phone",
					"com.igrs.tivic.phone.Activity.UGCActivity");
			intent.putExtra("nopgcdata", "yes");
			startActivity(intent);
			UIUtils.setCurrentFuncID(FuncID.ID_UGC);
			finish();

		} catch (Exception e) {
			Log.e(TAG, "startActivity error! " + e.toString());
		}
	}

}
