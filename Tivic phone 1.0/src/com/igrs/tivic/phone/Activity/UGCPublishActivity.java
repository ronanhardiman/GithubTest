
package com.igrs.tivic.phone.Activity;


import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Adapter.UGCPublishViewpagerAdapter;
import com.igrs.tivic.phone.Bean.Base.BaseParamBean;
import com.igrs.tivic.phone.Bean.UGC.UGCDataBean;
import com.igrs.tivic.phone.Bean.UGC.UGCListBean;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.ShowProgressDialog;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Impl.UGCImpl;
import com.igrs.tivic.phone.Utils.UIUtils;

public class UGCPublishActivity extends BaseActivity implements OnClickListener{
	TivicGlobal tivicGlobal = TivicGlobal.getInstance();
	private ViewPager viewpager_ugcpublish;
	Handler ugcUpdateUIHandler;
	UGCPublishViewpagerAdapter viewpagerAdapter;
	ArrayList<UGCDataBean> ugcList = new ArrayList<UGCDataBean>();
	private UGCImpl UGCInterface;
	private static String TAG = "UGCPublishActivity";
	private Button comment;
	private int currentTid = 0; 
	private int partner_id = 0;
	private int enterId = 0;
	private int offset = 0;
	private int listcount = 0;
//	private int currentPageIndex = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		UIUtils.setCurrentFuncID(FuncID.ID_PUBLISH);
		super.onCreate(savedInstanceState);
		currentTid = getIntent().getIntExtra("tid", 0);
		enterId = getIntent().getIntExtra("funcid", 0);
		partner_id = getIntent().getIntExtra("partner_id", TivicGlobal.getInstance().userRegisterBean.getUserId());
		//在帖子列表获得的总帖子数
		listcount = getIntent().getIntExtra("listcount", 0);
//		Log.i(TAG, "kevin add: listcount = " + listcount);
		initUGCPublishLayout();
	}
		
	private void initUGCData() {
		if(ugcList == null)
		{
			ugcList = new ArrayList<UGCDataBean>();
		}
		
		UGCInterface = UGCImpl.getInstance();		
		
		UGCInterface.setUgcUpdateUIHandler(ugcUpdateUIHandler);
		UGCInterface.setMyUGCUpdateUIHandler(ugcUpdateUIHandler);
		
		BaseParamBean param = new BaseParamBean();
		param.setUid(TivicGlobal.getInstance().userRegisterBean.getUserId());
		param.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
		if(TivicGlobal.getInstance().currentVisit.getVisit_pid() != null)
			param.setPid(Integer.parseInt(TivicGlobal.getInstance().currentVisit
				.getVisit_pid()));
		else
			param.setPid(102);
		param.setPartner_id(partner_id);//获取自己或好友发帖列表
		param.setStartTime(0); //基准时间，paging=2时该字段无效
		param.setSortType(2); //分页方式，2-所有记录按时间降序排列
		param.setCountInPage(Const.COUNTINPAGE);
		param.setOffset(offset);
		if(offset == 0)
		{
			ugcList.clear();
			ShowProgressDialog.show(this, null, null);
		}
		if(enterId == FuncID.ID_UGC)
		{
			mBaseTopBar.setChannelInfo();
			param.setVer(2); //注意这个接口的version是2
			UGCInterface.getUGCList(param);
		}
		else
		{
			mBaseTopBar.setUGCPublishTitle();
			param.setVer(1); //注意这个接口的version是1
			UGCInterface.getMyUGCList(param);
			
		}
		

	}

	private int findTidPosition(int tid)
	{
//		Log.d(TAG, "kevin add: current Tid = " + tid);
		if(ugcList == null || ugcList.size() == 0)
			return -1;
		int pos = 0;
		for(UGCDataBean item : ugcList)
		{
			if(item.getTid() == currentTid)
			{
//				Log.d(TAG, "kevin add: current pos = " + pos);
				return pos; 
			}else{
				pos++;
			}
		}
		return -1;
	}
	
	private void clearUGCData()
	{
		ugcList.clear();
		UGCInterface = null;
		offset = 0;
	}
		
	private void initUGCPublishLayout()
	{		
		viewpager_ugcpublish = (ViewPager) findViewById(R.id.viewpager_content);
		ImageView back_control = (ImageView) findViewById(R.id.icon_back_control);
		back_control.setOnClickListener(this);
		comment = (Button)findViewById(R.id.button_comment);
		if(comment != null)
			comment.setOnClickListener(this);
		viewpagerAdapter = new UGCPublishViewpagerAdapter(this, ugcList);
		viewpager_ugcpublish.setAdapter(viewpagerAdapter);
//		viewpager_ugcpublish.setPageMarginDrawable(R.drawable.all_bg_fengegxian);
		if(ugcUpdateUIHandler == null){
		ugcUpdateUIHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.arg1) {
				case UGCImpl.MSG_GET_UGC_LIST:
					
					UGCListBean listbean = (UGCListBean) msg.obj;
					if (listbean != null) {						
						if (listbean.getCount() > 0 && listbean.getDataList() != null)
						{
							ugcList.addAll(listbean.getDataList());
							if(ugcList.size() != 0 && listcount > ugcList.size())
							{
								offset = ugcList.size();
								initUGCData();
							}else{
								int pos = findTidPosition(currentTid);
								// update ui
								ShowProgressDialog.dismiss();
								viewpagerAdapter.notifyDataSetChanged();
								if(pos >= 0)
									viewpager_ugcpublish.setCurrentItem(pos, false);
							}
						}else{
							ShowProgressDialog.dismiss();
						}
					}else{
						ShowProgressDialog.dismiss();
						Log.i(TAG, "kevin add: msg.obj == null");
					}
					
					break;
				case UGCImpl.MSG_GET_MY_UGC_LIST:
					
					UGCListBean mylistbean = (UGCListBean) msg.obj;
					if (mylistbean != null && mylistbean.getDataList() != null) {
						
						if (mylistbean.getCount() > 0 && mylistbean.getDataList() != null)
						{
							ugcList.addAll(mylistbean.getDataList());
							if(ugcList.size() != 0 && listcount > ugcList.size())
							{
								offset = ugcList.size();
								initUGCData();
							}else{
								int pos = findTidPosition(currentTid);
								// update ui
								ShowProgressDialog.dismiss();
								viewpagerAdapter.notifyDataSetChanged();
								if(pos >= 0)
									viewpager_ugcpublish.setCurrentItem(pos, false);
							}
						}else{
							ShowProgressDialog.dismiss();
						}
					}else{
						ShowProgressDialog.dismiss();
						Log.i(TAG, "kevin add: msg.obj == null");
					}

					break;					

				}

			}
		};
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		UIUtils.setCurrentFuncID(FuncID.ID_PUBLISH);
		initUGCData();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		currentTid = TivicGlobal.getInstance().currentTid;

	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		clearUGCData();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();	
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.icon_back_control://topbar 返回按钮
			finish();
			break;
		case R.id.button_comment://点击评论
			Intent comment_intent = new Intent(this, ContentPublishActivity.class);
			comment_intent.putExtra("tid", TivicGlobal.getInstance().currentTid);
			comment_intent.putExtra("pid", TivicGlobal.getInstance().currentVisit.getVisit_pid());
			startActivity(comment_intent);
			break;
		default:
			break;
		}
	}
}
