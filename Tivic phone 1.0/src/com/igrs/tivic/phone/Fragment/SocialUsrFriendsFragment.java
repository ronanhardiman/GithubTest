package com.igrs.tivic.phone.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Activity.SocialBaseActivity;
import com.igrs.tivic.phone.Adapter.SocialMkFriendListAdapter;
import com.igrs.tivic.phone.AsyncTask.SocialFriendFriendsAsyncTask;
import com.igrs.tivic.phone.AsyncTask.SocialFriendsAsyncTask;
import com.igrs.tivic.phone.Bean.Base.UserBean;
import com.igrs.tivic.phone.Bean.Social.SocialFriendBean;
import com.igrs.tivic.phone.Bean.Social.SocialFriendBeanList;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Listener.SocialUpadteUIListener;
import com.igrs.tivic.phone.Utils.UIUtils;

import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SocialUsrFriendsFragment extends SocialBaseFragment {
//	private static SocialUsrFriendsFragment instance = null;
	private ListView friendsList;
	private List<SocialFriendBean> taFriendBeans;
	private TextView tv_name;
	private Boolean flag = true;
	private ViewGroup fdsuf;
	private Handler friends_detaills;
	private Context mContext;
	private SocialUpadteUIListener socialUpadteUIListener;
	int currentUid = 0;
	SocialMkFriendListAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Bundle bundle = this.getArguments();
		currentUid = bundle.getInt("uid");
		
		mContext = this.getActivity();
		if (flag) {
			fdsuf = (ViewGroup) inflater.inflate(R.layout.social_mkfriend_list,
					null);
			init();
			flag = false;
		}
		return fdsuf;
	}

	private void init() {
		ListView lv = (ListView) fdsuf.findViewById(R.id.id_list);
		if(adapter == null)
			adapter = new SocialMkFriendListAdapter(
				getActivity());
		initData();
		adapter.setMkfBeans(taFriendBeans);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int id,
					long arg3) {
				// TODO Auto-generated method stub
				jumptoDetails(id);
			}
		});
		loadFriendData();
	}

	//read friend data from server by uid
	private void loadFriendData() {
		SocialFriendFriendsAsyncTask friendFriendsAsyncTask = new SocialFriendFriendsAsyncTask();
		//获取自己的好友列表传自己的uid，获取好友的好友列表，传好友uid，都用partner_id字段
//		friendFriendsAsyncTask.setPartnerUsrID(currentUid);
		friendFriendsAsyncTask.setSocialUpdateUIListener(socialUpadteUIListener);
		friendFriendsAsyncTask.execute(currentUid); //传递用户uid
	}
	
	private void initData() {
		// TODO Auto-generated method stub
		if(taFriendBeans == null)
			taFriendBeans = new ArrayList<SocialFriendBean>();
		
		socialUpadteUIListener = new SocialUpadteUIListener() {
			
			@Override
			public void notifySocialFriendUI(SocialFriendBeanList result) {
				if(result == null) {
					UIUtils.ToastMessage(mContext, R.string.net_error);
					return;
				}
				taFriendBeans.clear();
				taFriendBeans.addAll(result.getFriendBeansList());
				adapter.notifyDataSetChanged();
			}


			@Override
			public void notifySocilaMkFriendUI(SocialFriendBeanList result) {
				// TODO Auto-generated method stub
				
			}

		};
		

		
	}

	public void jumptoDetails(int index) {
		
		int uid = taFriendBeans.get(index).getUserBean().getUid();
		SocialFriendsDetailFragment fragment_detail = new SocialFriendsDetailFragment();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.remove(fragment_detail);
		 Bundle b = new Bundle();
		 b.putInt("uid", uid);
		 fragment_detail.setArguments(b);
		
//		//zhanglr add here
//		UIUtils.setCurrentFuncID(FuncID.ID_SOCIAL_FRIENDS_DETAIL);
//		SocialBaseActivity ac = (SocialBaseActivity)this.getActivity();
//		ac.setMenuBarFocus(FuncID.ID_SOCIAL_FRIENDS_DETAIL);
//		//end add
		 ft.replace(R.id.social_empty, fragment_detail);
		 //add by chen
		 ft.addToBackStack(null);
		 TivicGlobal.getInstance().userRegisterBean.friendUids.addFirst(currentUid);
		 TivicGlobal.getInstance().userRegisterBean.isNeedsUid = false;
		 ft.commit();
	}

//	public static SocialUsrFriendsFragment getInstance() {
//		if (instance == null)
//			instance = new SocialUsrFriendsFragment();
//		return instance;
//	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}


}