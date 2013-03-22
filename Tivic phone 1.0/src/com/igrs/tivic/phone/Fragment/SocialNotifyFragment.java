package com.igrs.tivic.phone.Fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.app.FragmentTransaction;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Activity.SocialBaseActivity;
import com.igrs.tivic.phone.Adapter.SocialNotifyListViewAdapter;
import com.igrs.tivic.phone.Bean.Base.BaseParamBean;
import com.igrs.tivic.phone.Bean.Social.SocialNotifyBean;
import com.igrs.tivic.phone.Bean.Social.SocialNotifyListBean;
import com.igrs.tivic.phone.Bean.UGC.UGCListBean;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.ShowProgressDialog;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Impl.NotifyImpl;
import com.igrs.tivic.phone.Impl.UGCImpl;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.ViewGroup.PullToRefreshListView1;
import com.igrs.tivic.phone.ViewGroup.PullToRefreshListView1.OnLoadMoreListener;
import com.igrs.tivic.phone.ViewGroup.PullToRefreshListView1.OnRefreshListener;

	public class SocialNotifyFragment extends SocialBaseFragment implements
	OnRefreshListener, OnLoadMoreListener  {

		private PullToRefreshListView1 notifyListview;
		private ViewGroup notifymain;
		private ArrayList<SocialNotifyBean> notifyList = new ArrayList<SocialNotifyBean>();
		private String TAG = "SocialNotifyFragment";
		SocialNotifyListViewAdapter myAdapter;
		Handler updateUItHandler;
		private NotifyImpl notifyInterface;
		
		int page_flag = 0;
		int totalCount = 0;
		private int notice_id_offset = 0;
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			if (container == null)
				return null;

			notifymain = (ViewGroup) inflater.inflate(R.layout.social_notify, null);
			initSocialNotifyLayout();
			clearNoticeData();
			initNotifyData();
			return notifymain;
		}
				
		private void initSocialNotifyLayout()
		{

				notifyListview = (PullToRefreshListView1)notifymain.findViewById(R.id.id_list);
				notifyListview.hideLoadMoreFooterView();
				notifyListview.setOnRefreshListener(this);
				notifyListview.setOnLoadMoreListener(this);

				if(myAdapter == null)
					myAdapter = new SocialNotifyListViewAdapter(mContext, notifyList);
				notifyListview.setAdapter(myAdapter);
				notifyListview.setItemsCanFocus(true);
				OnItemClickListener item_listener = new OnItemClickListener()     
				{   
				        @Override   
				        public void onItemClick(AdapterView<?> parent, View view, int position, long id)    
				        {   
				        	//TODO 可能是bug，需要解决
				        	//由下拉Listview造成的列表item的位置从1开始计数而不是0
				        	int pos = position -1;
				        	if(pos<0)
				        		pos = 0;
				        	if(notifyList.get(pos).getStatusType() != Const.Social_Notify_Type_Admin)
				        	jumpToFriendsDetail(notifyList.get(pos).getUid());
				        }     
				};
				notifyListview.setOnItemClickListener(item_listener);
				

		}
		
		public void jumpToFriendsDetail(int uid)
		{
			UIUtils.setCurrentFuncID(FuncID.ID_SOCIAL_FRIENDS_DETAIL);
			SocialFriendsDetailFragment fragment = new SocialFriendsDetailFragment();		
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			SocialBaseActivity ac = (SocialBaseActivity)this.getActivity();
			ac.setMenuBarFocus(FuncID.ID_SOCIAL_FRIENDS_DETAIL);
			Bundle bundle = new Bundle();  
            bundle.putInt("uid", uid);  
            fragment.setArguments(bundle);          		
			//ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.setCustomAnimations(R.anim.fragment_slide_left_enter,  
	                R.anim.fragment_slide_left_exit,  
	                R.anim.fragment_slide_right_enter,  
	                R.anim.fragment_slide_right_exit);
			ft.replace(R.id.social_empty, fragment);
			ft.addToBackStack(null);
			ft.commit();
		}
		
		private void initNotifyData() {
			if(updateUItHandler == null){
			updateUItHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					switch (msg.arg1) {
					case NotifyImpl.MSG_GET_NOTIFY_LIST:
						
						SocialNotifyListBean listbean = (SocialNotifyListBean) msg.obj;
						if (listbean != null && listbean.getNotifyList() != null) {
//							Log.i(TAG, "kevin add: getMessage: datalist size = " + listbean.getNotifyList().size());
							totalCount = listbean.getCount();
							if (listbean.getCount() > 0)
							{
								notifyList.addAll(listbean.getNotifyList());
//								Log.i(TAG, "kevin add: add all");
							}
						}else{
							Log.i(TAG, "kevin add: msg.obj == null");
						}
						// update ui
						if (listbean!=null && listbean.getCount() > notifyList.size()) {
							notifyListview.showLoadMoreFooterVeiw(); // 设置列表底部视图
							page_flag = 1;
						} else {
							notifyListview.hideLoadMoreFooterView(); // 设置列表底部视图
							page_flag = 0;
						}

						myAdapter.notifyDataSetChanged();

						if (notice_id_offset == 0) {
							ShowProgressDialog.dismiss();
							notifyListview.onRefreshComplete();
							//拉取最新消息列表后，将消息提示置空
							SocialBaseActivity ac= (SocialBaseActivity)SocialNotifyFragment.this.getActivity();
							if(ac!=null)
								ac.hideNotifyIcon();
						} else {
							notifyListview.onLoadMoreComplete();

						}
						if(notifyList.size() > 0)
							notice_id_offset = notifyList.get(notifyList.size()-1).getNotice_id();
						else
							notice_id_offset = 0;
						break;

					}

				}
			};
		}
			if(notifyList == null)
			{
				notifyList = new ArrayList<SocialNotifyBean>();
			}
			if (notice_id_offset == 0)
				notifyList.clear();
			notifyInterface = NotifyImpl.getInstance();		
			
			notifyInterface.setNotifyUpdateUIHandler(updateUItHandler);
			BaseParamBean param = new BaseParamBean();
			param.setVer(1); //注意这个接口的version是1
			param.setUid(TivicGlobal.getInstance().userRegisterBean.getUserId());
			param.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
			param.setCountInPage(Const.COUNTINPAGE);
			param.setReqnum(Const.COUNTINPAGE);
			param.setPage_flag(page_flag);//分页标识，0为取首页 1后翻 2前翻
			param.setOffset(notice_id_offset);
			param.setNid(notice_id_offset); //翻页时，做偏移量算
			param.setOpreate(1); //0：只查询；1：查询并设置所有未读消息为已读，且仅在pageIndex为0时生效。
			if (notice_id_offset == 0)
				ShowProgressDialog.show(this.getActivity(), null, null);
			notifyInterface.getNotifyList(param);

		}

		private void clearNoticeData()
		{
			notice_id_offset = 0;
			page_flag = 0;
			notifyList.clear();
			if(myAdapter!=null)
				myAdapter.notifyDataSetChanged();
			if(notifyListview!=null)
				notifyListview.hideLoadMoreFooterView();
		}
		
		@Override
		public void onRefresh() {
			clearNoticeData();
			initNotifyData();
		}

		@Override
		public void onClickLoadMore() {
			loadMore();
		}

		private void loadMore() {
			if (notifyList != null && notifyList.size() < 0 || notifyList.size() >= totalCount)
				return;
			initNotifyData();
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
			UIUtils.setCurrentFuncID(FuncID.ID_SOCIAL_NOTIFY);
			SocialBaseActivity ac = (SocialBaseActivity)this.getActivity();
			ac.setMenuBarFocus(FuncID.ID_SOCIAL_NOTIFY);
			super.onResume();
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