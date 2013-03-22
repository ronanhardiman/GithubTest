package com.igrs.tivic.phone.Activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
//import android.view.GestureDetector;
//import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
//import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Adapter.UGCListViewAdapter;
import com.igrs.tivic.phone.Bean.Base.BaseParamBean;
//import com.igrs.tivic.phone.Bean.Base.UserBean;
//import com.igrs.tivic.phone.Bean.Social.SocialMessageBean;
import com.igrs.tivic.phone.Bean.UGC.UGCDataBean;
import com.igrs.tivic.phone.Bean.UGC.UGCListBean;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.FuncID;
//import com.igrs.tivic.phone.Global.RotationHelper;
import com.igrs.tivic.phone.Global.ShowProgressDialog;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Impl.UGCImpl;
import com.igrs.tivic.phone.Listener.UGCItemClickListener;
import com.igrs.tivic.phone.Utils.UIUtils;
//import com.igrs.tivic.phone.Utils.Utils;
import com.igrs.tivic.phone.ViewGroup.BaseTopBar.UGCNewClickListener;
import com.igrs.tivic.phone.ViewGroup.PullToRefreshListView1;
import com.igrs.tivic.phone.ViewGroup.PullToRefreshListView1.OnLoadMoreListener;
import com.igrs.tivic.phone.ViewGroup.PullToRefreshListView1.OnRefreshListener;

public class UGCActivity extends BaseActivity implements UGCItemClickListener,
		UGCNewClickListener, OnRefreshListener, OnLoadMoreListener {

	private PullToRefreshListView1 UGCListView;
	private String TAG = "UGCActivity";
	private Handler ugcUpdateUItHandler;
	ArrayList<UGCDataBean> ugcList = new ArrayList<UGCDataBean>();
	private UGCImpl UGCInterface;
	private UGCListViewAdapter myAdapter;
	public static final int REQUESTCODE_PUBLISH = 100;
	public static final int REQUESTCODE_UGC_NEW = 101;
	private int currentPid = 0;
	private int pagecount = 0;
	private int offset = 0;
	boolean ispgcnodata = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		UIUtils.setCurrentFuncID(FuncID.ID_UGC);
		super.onCreate(savedInstanceState);
		if (TivicGlobal.getInstance().currentVisit.getVisit_pid() != null)
			currentPid = Integer
					.parseInt(TivicGlobal.getInstance().currentVisit
							.getVisit_pid());
		else
			currentPid = 102;
		String ishaspgc = getIntent().getStringExtra("nopgcdata");
		if(ishaspgc!=null && ishaspgc.endsWith("yes"))
			ispgcnodata = true;
		else
			ispgcnodata = false;
		initUGCLayout();
	}

	private void initUGCData() {
		if (ugcUpdateUItHandler == null) {

			ugcUpdateUItHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					switch (msg.arg1) {
					case UGCImpl.MSG_GET_UGC_LIST:
						UGCListBean listbean = (UGCListBean) msg.obj;
						if (listbean != null) {
							pagecount = listbean.getCount();

							if (listbean.getCount() > 0
									&& listbean.getDataList() != null) {
								ugcList.addAll(listbean.getDataList());
							}

						} else {
							Log.i(TAG, "kevin add: msg.obj == null");
						}
						// update ui
						if (listbean != null && listbean.getCount() > ugcList.size()) {
							UGCListView.showLoadMoreFooterVeiw(); // 设置列表底部视图
						} else {
							UGCListView.hideLoadMoreFooterView(); // 设置列表底部视图
						}
						myAdapter.notifyDataSetChanged();
						if (offset == 0) {
							ShowProgressDialog.dismiss();
							UGCListView.onRefreshComplete();
						} else {
							UGCListView.onLoadMoreComplete();

						}
						offset = ugcList.size();
						break;
					case UGCImpl.MSG_UGC_SUPPORT:
						// int upCount = msg.arg2;
						// if(ugcList.get(currentFocus).getIsUp() == 0)
						// //为0表示已经支持过，不可再支持，为1表示可支持
						// {
						// 		UIUtils.ToastMessage(UGCActivity.this,
						// 		getString(R.string.ugc_supportAlready));
						// }else
						// {
						// 		myAdapter.notifyDataSetChanged();
						// }

						break;
					}

				}
			};
		}
		UGCInterface = UGCImpl.getInstance();
		UGCInterface.setUgcUpdateUIHandler(ugcUpdateUItHandler);

		if (ugcList == null) {
			ugcList = new ArrayList<UGCDataBean>();
		}
		if (offset == 0) {
			ugcList.clear();

		}
		BaseParamBean param = new BaseParamBean();
		param.setVer(2); // 注意这个接口的version是2
		param.setUid(TivicGlobal.getInstance().userRegisterBean.getUserId());
		param.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
//		Log.e("TivicGlobal",
//				"kevin add : TivicGlobal.getInstance().currentVisit.getVisit_pid()="
//						+ TivicGlobal.getInstance().currentVisit.getVisit_pid());

		if (TivicGlobal.getInstance().currentVisit.getVisit_pid() != null)
			param.setPid(Integer.parseInt(TivicGlobal.getInstance().currentVisit
					.getVisit_pid()));
		else
			param.setPid(102);
		param.setStartTime(0); // 基准时间，paging=2时该字段无效
		param.setSortType(2); // 分页方式，2-所有记录按时间降序排列
		param.setCountInPage(Const.COUNTINPAGE);
		// UIUtils.Logd(TAG, "kevin add: offset in init = " + offset);
		param.setOffset(offset);

		if (offset == 0) {
			ShowProgressDialog.show(this, null, null);
		}
		UGCInterface.getUGCList(param);

	}

	private void clearUGCData() {
		ugcList.clear();
		offset = 0;
	}

	private void initUGCLayout() {
		mBaseTopBar.setOnUGCNewListner(this);
		if (ugcList == null) {
			ugcList = new ArrayList<UGCDataBean>();
		}
		UGCListView = (PullToRefreshListView1) findViewById(R.id.id_list);
		UGCListView.hideLoadMoreFooterView();
		UGCListView.setOnRefreshListener(this);
		UGCListView.setOnLoadMoreListener(this);

		if (myAdapter == null)
			myAdapter = new UGCListViewAdapter(mContext, ugcList);
		myAdapter.setOnItemClickListener(this);
		UGCListView.setAdapter(myAdapter);
		UGCListView.setItemsCanFocus(false);
		Button jump = (Button)findViewById(R.id.id_jump_to_pgc);
		jump.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view)
			{
				jumpToWaterfall();
			}
		});
	}

	@Override
	public void onUGCNewClick() {
		Intent comment_intent = new Intent(this, ContentPublishActivity.class);
		comment_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		comment_intent.putExtra("pid", currentPid);
		startActivity(comment_intent);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		UIUtils.setCurrentFuncID(FuncID.ID_UGC);
		super.onResume();
		clearUGCData();
		initUGCData();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onReplyClicked(UGCDataBean bean, int pos) {
		Intent getContent = new Intent();
		getContent.setClass(this, ContentPublishActivity.class);
		getContent.putExtra("tid", bean.getTid());
		getContent.putExtra("pid", currentPid);
		// for open the ContentPublishActivity to reply
		UIUtils.setCurrentFuncID(FuncID.ID_UGC_PUBLISH);
		startActivity(getContent);

	}

	@Override
	public void onUpClicked(UGCDataBean bean, int pos) {
		if (bean.getIsUp() == 0) {
			return;
		}
		BaseParamBean param = new BaseParamBean();
		param.setVer(1); // 注意这个接口的version是1
		param.setUid(TivicGlobal.getInstance().userRegisterBean.getUserId());
		param.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
		param.setTid(bean.getTid());
		UGCInterface.supportPublish(param);
	}

	// 左右滑动翻转动画加载瀑布流
	public void jumpToWaterfall() {
		if(ispgcnodata)
		{
			UIUtils.setCurrentFuncID(FuncID.ID_UGC_JUMP);
			Intent intent = new Intent(UGCActivity.this,UGCJumptActivity.class);
			startActivity(intent);
		}
		try {
			this.finish();
		} catch (Exception e) {
			Log.e(TAG, "startActivity error! " + e.toString());
		}
	}

	@Override
	public void onRefresh() {

		clearUGCData();
		initUGCData();
	}

	@Override
	public void onClickLoadMore() {

		loadMore();
	}

	private void loadMore() {

		if (offset < 0 || offset > pagecount)
			return;
		initUGCData();
	}

}
