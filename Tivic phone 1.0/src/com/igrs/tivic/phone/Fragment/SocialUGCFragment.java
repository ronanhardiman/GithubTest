package com.igrs.tivic.phone.Fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Activity.SocialBaseActivity;
import com.igrs.tivic.phone.Adapter.SocialUGCListViewAdapter;
import com.igrs.tivic.phone.Bean.Base.BaseParamBean;
import com.igrs.tivic.phone.Bean.UGC.UGCDataBean;
import com.igrs.tivic.phone.Bean.UGC.UGCListBean;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.ShowProgressDialog;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Impl.UGCImpl;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;
import com.igrs.tivic.phone.ViewGroup.PullToRefreshListView1;
import com.igrs.tivic.phone.ViewGroup.PullToRefreshListView1.OnLoadMoreListener;
import com.igrs.tivic.phone.ViewGroup.PullToRefreshListView1.OnRefreshListener;

public class SocialUGCFragment extends SocialBaseFragment implements
		OnRefreshListener, OnLoadMoreListener {

	private PullToRefreshListView1 ugcListview;
	private ViewGroup ugcmain;
	private String TAG = "SocialUGCFragment";
	SocialUGCListViewAdapter myAdapter;
	Handler myUgcUpdateUItHandler;
	ArrayList<UGCDataBean> ugcList = new ArrayList<UGCDataBean>();
	private UGCImpl UGCInterface;
	private int currentUid = 0;
	private int offset = 0;
	private int pagecount = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = this.getActivity().getApplicationContext();
		// TODO Auto-generated method stub
		if(getArguments() != null)
			currentUid = getArguments().getInt("uid");	
		ugcmain = (ViewGroup) inflater.inflate(R.layout.social_ugc, null);
		initSocialUGCLayout();
//		initUGCData();

		return ugcmain;
	}

	private void initUGCData() {
		
		if (myUgcUpdateUItHandler == null) {
			myUgcUpdateUItHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					switch (msg.arg1) {
					case UGCImpl.MSG_GET_MY_UGC_LIST:

						UGCListBean listbean = (UGCListBean) msg.obj;
						if (listbean != null) {
							// 返回的帖子总数
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
							ugcListview.showLoadMoreFooterVeiw(); // 设置列表底部视图
						} else {
							ugcListview.hideLoadMoreFooterView(); // 设置列表底部视图
						}

						myAdapter.notifyDataSetChanged();
						if (offset == 0) {
							ShowProgressDialog.dismiss();
							ugcListview.onRefreshComplete();
						} else {
							ugcListview.onLoadMoreComplete();

						}
						offset = ugcList.size();

						break;

					}

				}
			};
		}
		UGCInterface = UGCImpl.getInstance();
		UGCInterface.setMyUGCUpdateUIHandler(myUgcUpdateUItHandler);
		
		if (ugcList == null) {
			ugcList = new ArrayList<UGCDataBean>();
		}
		if (offset == 0) {
			ugcList.clear();

		}

		BaseParamBean param = new BaseParamBean();
		param.setVer(1); // 注意这个接口的version是1
		param.setUid(TivicGlobal.getInstance().userRegisterBean.getUserId());
		//获取好友的发帖列表
		if(currentUid == 0)
		{
			currentUid = TivicGlobal.getInstance().userRegisterBean.getUserId();
		}
		param.setPartner_id(currentUid);
		param.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
		param.setPid(102);
		param.setStartTime(0); // 基准时间，paging=2时该字段无效
		param.setSortType(2); // 分页方式，2-所有记录按时间降序排列
		param.setCountInPage(Const.COUNTINPAGE);
		param.setOffset(offset);
		if (offset == 0)
			ShowProgressDialog.show(this.getActivity(), null, null);
		UGCInterface.getMyUGCList(param);

	}

	private void clearUGCData() {
		ugcList.clear();
		if(myAdapter!=null)
			myAdapter.notifyDataSetChanged();
		if(ugcListview!=null)
			ugcListview.hideLoadMoreFooterView();
		offset = 0;
	}

	private void initSocialUGCLayout() {
		TextView title = (TextView)ugcmain
				.findViewById(R.id.id_title);
		if(TivicGlobal.mCurrentFuncID == FuncID.ID_SOCIAL_PUBLISH)
			title.setVisibility(View.VISIBLE);
		else
			title.setVisibility(View.GONE);
		ugcListview = (PullToRefreshListView1) ugcmain
				.findViewById(R.id.id_list);
		ugcListview.hideLoadMoreFooterView();
		ugcListview.setOnRefreshListener(this);
		ugcListview.setOnLoadMoreListener(this);

		if (myAdapter == null)
			myAdapter = new SocialUGCListViewAdapter(mContext, ugcList);
		ugcListview.setAdapter(myAdapter);
		ugcListview.setItemsCanFocus(true);
		OnItemClickListener item_listener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int pos = position - 1;
				Intent intent = new Intent();

				if (ugcList != null && pos >= 0 && pos < ugcList.size()) {
					intent.putExtra("tid", ugcList.get(pos).getTid());
					intent.putExtra("listcount", ugcList.size());
				}
				// 用于区别打开帖子详情时是由节目进入还是由社交自己的帖子进入
				// FuncID.ID_UGC表示是由节目进入 FunID.ID_SOCIAL_PUBLISH表示由社交进入
				intent.putExtra("funcid", FuncID.ID_SOCIAL_PUBLISH);
				intent.putExtra("partner_id", currentUid);
				intent.setClassName("com.igrs.tivic.phone",
						"com.igrs.tivic.phone.Activity.UGCPublishActivity");
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				UIUtils.setCurrentFuncID(FuncID.ID_PUBLISH);
				startActivity(intent);
			}
		};
		ugcListview.setOnItemClickListener(item_listener);

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

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		if (UIUtils.getCurrentFuncID() != FuncID.ID_SOCIAL_FRIENDS_DETAIL) {
			UIUtils.setCurrentFuncID(FuncID.ID_SOCIAL_PUBLISH);
			SocialBaseActivity ac = (SocialBaseActivity) this.getActivity();
			ac.setMenuBarFocus(FuncID.ID_SOCIAL_PUBLISH);
		}
		super.onResume();
		clearUGCData();
		myAdapter.notifyDataSetChanged();
		ugcListview.hideLoadMoreFooterView();
		initUGCData();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}
}