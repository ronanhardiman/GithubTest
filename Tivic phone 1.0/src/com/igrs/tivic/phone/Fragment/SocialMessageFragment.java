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
//import android.widget.AbsListView;
import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.ProgressBar;
//import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ListView;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Activity.SocialBaseActivity;
import com.igrs.tivic.phone.Adapter.SocialMessageListViewAdapter;
import com.igrs.tivic.phone.Bean.Base.BaseParamBean;
import com.igrs.tivic.phone.Bean.Social.SocialMessageBean;
import com.igrs.tivic.phone.Bean.Social.SocialMessageListBean;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.ShowProgressDialog;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Impl.MessageImpl;
//import com.igrs.tivic.phone.Impl.NotifyImpl;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.ViewGroup.PullToRefreshListView1;
import com.igrs.tivic.phone.ViewGroup.PullToRefreshListView1.OnLoadMoreListener;
import com.igrs.tivic.phone.ViewGroup.PullToRefreshListView1.OnRefreshListener;

public class SocialMessageFragment extends SocialBaseFragment implements
		OnRefreshListener, OnLoadMoreListener {

	private PullToRefreshListView1 messageListview;
	private ViewGroup messagemain;
	private ArrayList<SocialMessageBean> messageList = new ArrayList<SocialMessageBean>();
	private String TAG = "SocialMessageFragment";
	private SocialMessageListViewAdapter myAdapter;
	Handler updateUItHandler;
	private MessageImpl messageInterface;
	private int offset = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		messagemain = (ViewGroup) inflater.inflate(R.layout.social_message,
				null);
		initSocialMessageLayout();
		clearMessageData();
		initMessageData();
		return messagemain;
	}

	private void initSocialMessageLayout() {

		messageListview = (PullToRefreshListView1) messagemain
				.findViewById(R.id.id_list);
		messageListview.hideLoadMoreFooterView();
		messageListview.setOnRefreshListener(this);
		messageListview.setOnLoadMoreListener(this);
		messageList.clear();
		if (myAdapter == null)
			myAdapter = new SocialMessageListViewAdapter(mContext, messageList);
		messageListview.setAdapter(myAdapter);
		messageListview.setItemsCanFocus(true);
		final SocialBaseActivity ac = (SocialBaseActivity)this.getActivity();
		OnItemClickListener item_listener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int pos = position-1;
				if(pos < 0)
					pos = 0;
				ac.clickMessageDetail(messageList.get(pos).getPartner_id());
				ac.setMessageCount(messageList.get(pos).getCountnew());
			}
		};
		messageListview.setOnItemClickListener(item_listener);

	}

	

	private void initMessageData() {
		if (updateUItHandler == null) {
			updateUItHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					switch (msg.arg1) {
					case MessageImpl.MSG_GET_MESSAGE_LIST:

						SocialMessageListBean listbean = (SocialMessageListBean) msg.obj;
						if (listbean != null
								&& listbean.getMessageList() != null) {

							if (listbean.getCount() > 0) {
								messageList.addAll(listbean.getMessageList());
							}
						} else {
							Log.i(TAG, "kevin add: msg.obj == null");
						}
						if (listbean!=null && (listbean.getCount() > messageList.size()
								|| listbean.getIfMore() > 0)) {
							messageListview.showLoadMoreFooterVeiw(); // 设置列表底部视图
						} else {
							messageListview.hideLoadMoreFooterView(); // 设置列表底部视图
						}

						myAdapter.notifyDataSetChanged();
						if (offset == 0) {
							ShowProgressDialog.dismiss();
							messageListview.onRefreshComplete();
						} else {
							messageListview.onLoadMoreComplete();

						}
						offset = messageList.size();
		
						break;

					}

				}
			};
		}
		if (messageList == null) {
			messageList = new ArrayList<SocialMessageBean>();
		}
		messageInterface = MessageImpl.getInstance();
		messageInterface.setUpdateUIHandler(updateUItHandler);
		if (offset == 0) {
			messageList.clear();

		}
		BaseParamBean param = new BaseParamBean();
		param.setVer(1);
		param.setUid(TivicGlobal.getInstance().userRegisterBean.getUserId());
		param.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
		param.setCountInPage(Const.COUNTINPAGE);
		param.setReqnum(Const.COUNTINPAGE);
		param.setOffset(offset); // 同上
		if (offset == 0)
			ShowProgressDialog.show(this.getActivity(), null, null);
		messageInterface.getMessageList(param);

	}

	private void clearMessageData() {
		if (messageList != null)
			messageList.clear();
		if(myAdapter!=null)
			myAdapter.notifyDataSetChanged();
		if(messageListview!=null)
			messageListview.hideLoadMoreFooterView();
		offset = 0;
	}

	@Override
	public void onRefresh() {
		clearMessageData();
		initMessageData();
	}

	@Override
	public void onClickLoadMore() {
		loadMore();
	}

	private void loadMore() {
		// 会话最大上线200
		if (offset < 0 || offset > 200)
			return;
		initMessageData();
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
		UIUtils.setCurrentFuncID(FuncID.ID_SOCIAL_MESSAGE);
		SocialBaseActivity ac = (SocialBaseActivity) this.getActivity();
		ac.setMenuBarFocus(FuncID.ID_SOCIAL_MESSAGE);
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