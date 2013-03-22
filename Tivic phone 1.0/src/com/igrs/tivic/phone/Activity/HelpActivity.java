package com.igrs.tivic.phone.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Bean.LocationBean;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Utils.LocationUtils;
import com.igrs.tivic.phone.Utils.UIUtils;

public class HelpActivity extends Activity {

	public Context mContext;
	private LinearLayout help_layout;
	private String TAG = "HelpActivity";
	private int tivicID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this.getApplicationContext();
		setContentView(R.layout.help_main);
		initLayout();
		init();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
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

	public void initLayout() {

		help_layout = (LinearLayout) findViewById(R.id.help_view);
		tivicID = TivicGlobal.getInstance().mCurrentFuncID;
//		UIUtils.ToastMessage(this, tivicID+"fdfsfsd");
		if(tivicID == FuncID.ID_UGC){
			help_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.help_ugc));
		}else if(tivicID == FuncID.ID_PUSHTV){
			help_layout.setBackgroundResource(R.drawable.help_rebo);
		}else if(tivicID == FuncID.ID_MYTV){
			help_layout.setBackgroundResource(R.drawable.help_wodedianshi);
		}else if(tivicID == FuncID.ID_SOCIAL){
			help_layout.setBackgroundResource(R.drawable.help_social_cebianlan);
		}else if(tivicID == FuncID.ID_SOCIAL_INFO){
			help_layout.setBackgroundResource(R.drawable.help_social_ziliao);
		}else if(tivicID == FuncID.ID_SOCIAL_FRIENDS){
			help_layout.setBackgroundResource(R.drawable.help_social_haoyou);
		}else if(tivicID == FuncID.ID_SOCIAL_FRIENDS_DETAIL){
			help_layout.setBackgroundResource(R.drawable.help_social_haoyouxinxi);
		}else if(tivicID == FuncID.ID_SOCIAL_MESSAGE){
			help_layout.setBackgroundResource(R.drawable.help_social_sixin);
		}else if(tivicID == FuncID.ID_SOCIAL_MESSAGE_DETAIL){
			help_layout.setBackgroundResource(R.drawable.help_social_sixinduihua);
		}else if(tivicID == FuncID.ID_SOCIAL_PUBLISH){
			help_layout.setBackgroundResource(R.drawable.help_social_tieba);
		}else if(tivicID == FuncID.ID_SOCIAL_NOTIFY){
			help_layout.setBackgroundResource(R.drawable.help_social_tongzhi);
		}else if(tivicID == FuncID.ID_SOCIAL_PUBLISH){
			help_layout.setBackgroundResource(R.drawable.help_social_tieba);
		}else if(tivicID == FuncID.ID_UGC_PUBLISH){
			help_layout.setBackgroundResource(R.drawable.help_ugc_tiezizhankai);
		}else if(tivicID == FuncID.ID_HELP){
			help_layout.setBackgroundResource(R.drawable.help_ugc_tiezizhankai);
		}else if(tivicID == FuncID.ID_EPG){
			help_layout.setBackgroundResource(R.drawable.help_zhibopindao);
		}else if(tivicID == FuncID.ID_WATERFALL){
			help_layout.setBackgroundResource(R.drawable.help_pgc);
		}else if(tivicID == FuncID.ID_SOCIAL_MKFRIEND){
			help_layout.setBackgroundResource(R.drawable.help_social_jiaoyou);
		}else if(tivicID == FuncID.ID_SOCIAL_SAVE){
			help_layout.setBackgroundResource(R.drawable.help_social_shoucang);
		}else if(tivicID == FuncID.ID_PUBLISH){
			help_layout.setBackgroundResource(R.drawable.help_social_tieba);
		}else if(tivicID == FuncID.ID_CONTENT) {
			help_layout.setBackgroundResource(R.drawable.help_pgc_sanjijiemian_shipintuwenwenzi);
		}
			
		help_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	public void init() {
////add by zhanglr
////测试获取位置的单元模块，请勿删除
//		LocationUtils loc = LocationUtils.getInstance(this);
//		LocationBean beanfrom = LocationUtils.getLocationBean();
//		LocationBean beanto = new LocationBean(45.98328,106.30831);
//		UIUtils.Logd("HelpActivity", "kevin add: bean lat: " + beanfrom.getLat() + " lon: " + beanfrom.getLon());
//		UIUtils.Logd("HelpActivity", "kevin add: distance: " + LocationUtils.getDistance(beanfrom, null));
		
	}

}
