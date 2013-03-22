package com.igrs.tivic.phone.Fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Activity.SocialBaseActivity;
import com.igrs.tivic.phone.Adapter.SocialSaveListViewAdapter;
import com.igrs.tivic.phone.Bean.PGC.CollectBean;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Impl.ContentImpl;
import com.igrs.tivic.phone.Utils.UIUtils;

public class SocialSaveFragment extends SocialBaseFragment implements OnItemClickListener,OnScrollListener{

	private ListView listview;
	SocialSaveListViewAdapter myAdapter;
	Handler updateUItHandler;
	ArrayList<CollectBean> ugcList = new ArrayList<CollectBean>();
	private boolean isloading = false;
	private RelativeLayout loadingDialog;
	
	private static final int INITE_LISTVIEW = 0;//初始化列表
	private static final int ADDMORE_LISTVIEW = 1;//分页向列表添加数据
	
	private int startIndex;
	
	Handler loadingHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ArrayList<CollectBean> beans = (ArrayList<CollectBean>)msg.obj;
			if(listview.getFooterViewsCount()>0)
				listview.removeFooterView(loadingDialog);
			isloading = false;
			if(beans==null) return;
			if(msg.arg1 == INITE_LISTVIEW){
				ugcList.clear();
			}
			ugcList.addAll(beans);
			myAdapter.notifyDataSetChanged();
		};
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup maingroup = (ViewGroup) inflater.inflate(R.layout.social_save, null);
		listview = (ListView) maingroup.findViewById(R.id.id_list);
		
		myAdapter = new SocialSaveListViewAdapter(this.getActivity(), ugcList);
		
		loadingDialog = (RelativeLayout) inflater.inflate(R.layout.page_load, null);
		listview.addFooterView(loadingDialog);
		listview.setAdapter(myAdapter);

		listview.setItemsCanFocus(true);
			
		listview.setOnItemClickListener(this);
		listview.setOnScrollListener(this);
		
		return maingroup;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(UIUtils.getCurrentFuncID() != FuncID.ID_SOCIAL_FRIENDS_DETAIL){
			UIUtils.setCurrentFuncID(FuncID.ID_SOCIAL_SAVE);
			SocialBaseActivity ac = (SocialBaseActivity)this.getActivity();
			if(ac!=null)
				ac.setMenuBarFocus(FuncID.ID_SOCIAL_SAVE);
		}
		startIndex = 0;
		getData(INITE_LISTVIEW);
	}
	
	private void getData(final int state){
		new Thread(new Runnable() {
			@Override
			public void run() {
				int parentId = 0;
				if(SocialSaveFragment.this.getArguments() == null){
					parentId = TivicGlobal.getInstance().userRegisterBean.getUserUID();
				}else{
					parentId = SocialSaveFragment.this.getArguments().getInt("uid");
				}
				ArrayList<CollectBean> beans = ContentImpl.getInstance().getCollectionList(parentId,startIndex);
				Message msg = loadingHandler.obtainMessage();
				msg.arg1 = state;
				msg.obj = beans;
				loadingHandler.sendMessage(msg);
			}
		}).start();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		Intent intent  =  new Intent();
		intent.setClassName("com.igrs.tivic.phone","com.igrs.tivic.phone.Activity.ContentActivity");
		intent.putExtra("currentId",position);
		intent.putParcelableArrayListExtra("testt",ugcList);
		intent.putExtra("fromFragment", 1);	//标识 来自Fragment
		this.getActivity().startActivity(intent);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
		int last_item_position = firstVisibleItem+visibleItemCount-1;
		if(last_item_position == totalItemCount-2){
//			Log.e("aaa","======onScroll=====");
			if(!isloading){
				isloading = true;
				addMore();
				listview.addFooterView(loadingDialog);
			}
		}
	}
	
	private ArrayList<CollectBean> addMore(){
		startIndex+=10;
		getData(ADDMORE_LISTVIEW);
		return new ArrayList<CollectBean>();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
	}
}