package com.igrs.tivic.phone.Activity;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Adapter.SociaSystemUsrIconAdapter;
import com.igrs.tivic.phone.Fragment.SocialUsrInfoFragment;
import com.igrs.tivic.phone.Global.TivicGlobal;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class SocialSystemIconActivity extends Activity implements
		OnClickListener {
	private Button bt_finish_select_icon, bt_back;
	private GridView system_grid;
	private Bundle b;
;
	private int[] drawable = { R.drawable.public_tx_1, R.drawable.public_tx_2,
			R.drawable.public_tx_3, R.drawable.public_tx_4, R.drawable.public_tx_5, R.drawable.public_tx_6, R.drawable.public_tx_7 };

	private int drawable_img;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.social_system_usericon);
		SociaSystemUsrIconAdapter adapter = new SociaSystemUsrIconAdapter(this);
		system_grid = (GridView) findViewById(R.id.system_grid);
		system_grid.setAdapter(adapter);
		bt_finish_select_icon = (Button) findViewById(R.id.bt_finish_select_icon);
		bt_finish_select_icon.setOnClickListener(this);
		bt_back = (Button) findViewById(R.id.bt_back);
		bt_back.setOnClickListener(this);
		system_grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				drawable_img = drawable[position];
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_finish_select_icon:
	        Intent mIntent = new Intent();  
	        mIntent.putExtra("user_pic", drawable_img);  
	       	setResult(4,mIntent);  
	       	finish();  
			break;
		case R.id.bt_back:
			setResult(4);  
			this.finish();
			break;
		default:
			break;
		}
	}
}
