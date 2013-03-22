
package com.igrs.tivic.phone.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Adapter.WaterfallListdapter;
import com.igrs.tivic.phone.Bean.Waterfall.WaterfallAdapterData;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Impl.WaterFallImpl;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;
import com.igrs.tivic.phone.ViewGroup.PullToRefreshListView1;
import com.igrs.tivic.phone.ViewGroup.PullToRefreshListView1.OnRefreshListener;

public  class WaterfallActivity extends BaseActivity implements OnRefreshListener{
	
	TivicGlobal tivicGlobal = TivicGlobal.getInstance();
	private PullToRefreshListView1 mListView;
	private WaterfallListdapter mAdapter;
	float distance = 0.0f;
	private String currentPid;//节目号
	private String currentCid;//频道号
	private String program_title;
	private String channel_name;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			WaterfallAdapterData wad = (WaterfallAdapterData)msg.obj;
			if(wad.getItemBeans() == null || wad.getItemBeans().size() == 0) {
				UIUtils.setCurrentFuncID(FuncID.ID_UGC);
				Intent intent = new Intent(WaterfallActivity.this,UGCActivity.class);
				intent.putExtra("nopgcdata", "yes");
				startActivity(intent);
				WaterfallActivity.this.finish();
				return;
			}
			mAdapter = new WaterfallListdapter(WaterfallActivity.this,wad);
			if(mListView != null) {
				mListView.setAdapter(mAdapter);
				mListView.onRefreshComplete();
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		UIUtils.setCurrentFuncID(FuncID.ID_WATERFALL);
		super.onCreate(savedInstanceState);
		UpdateLastVisit();
		mListView = (PullToRefreshListView1)findViewById(R.id.waterfall_listview);
		if(mListView!=null){
			mListView.setScrollingCacheEnabled(false);
			mListView.setDivider(null);
			mListView.setOnRefreshListener(this);
			mListView.hideLoadMoreFooterView();
		}
		Button jump = (Button)findViewById(R.id.id_jump_to_pgc);
		jump.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view)
			{
				jumpToUGC();
			}
		});
	}
	@Override
	protected void onResume() {
		UIUtils.setCurrentFuncID(FuncID.ID_WATERFALL);
		super.onResume();
		getData();
	}
	private void getData(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				WaterfallAdapterData wad = WaterFallImpl.getContentByUrl(WaterfallActivity.this,currentPid);
				Message msg = new Message();
				msg.obj = wad;
				mHandler.sendMessage(msg);
			}
		}).start();
	}
	
	private void UpdateLastVisit(){
		try{

			program_title = getIntent().getStringExtra("program_title");
			channel_name = getIntent().getStringExtra("channel_name");
			currentPid = getIntent().getStringExtra("program_id");
			if(currentPid == null || currentPid.isEmpty())
				currentPid = String.valueOf(getIntent().getIntExtra("program_id", 100));
			currentCid = getIntent().getStringExtra("channel_id");
			if(currentCid == null || currentCid.isEmpty())
				currentCid = String.valueOf(getIntent().getIntExtra("channel_id", 100));
		}catch(Exception e){
			
		}
		if(channel_name == null || channel_name.isEmpty())
		{
			channel_name= Utils.getChannelNameById(WaterfallActivity.this, Integer.parseInt(currentCid));
		}
//Log.e("currentPid","==================pid,cid,pname,cname===="+currentPid+","+currentCid+","+program_title+","+channel_name);
		WaterFallImpl.UpdateLastVisit(currentCid,currentPid,program_title,channel_name);
		TivicGlobal.getInstance().currentVisit.setVisit_channelid(currentCid);
		TivicGlobal.getInstance().currentVisit.setVisit_pid(currentPid);
		TivicGlobal.getInstance().currentVisit.setVisit_ProgramTitle(program_title);
		TivicGlobal.getInstance().currentVisit.setVisit_ChannelName(channel_name);
		mBaseTopBar.setChannelInfo();
		initMKFrientEnable();
	}

	//左右滑动或翻转动画加载UGC贴吧
	public void jumpToUGC() {
		if(tivicGlobal.mIsLogin){
			Intent intent = new Intent();
			intent.setClassName("com.igrs.tivic.phone",
					"com.igrs.tivic.phone.Activity.UGCActivity");
			intent.putExtra("pid", currentPid);
			startActivity(intent);			
			UIUtils.setCurrentFuncID(FuncID.ID_UGC);
//			this.finish();
		}else{
			Toast.makeText(WaterfallActivity.this, getString(R.string.base_login_remind_ugc), Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public void onRefresh() {
		getData();
	}
}