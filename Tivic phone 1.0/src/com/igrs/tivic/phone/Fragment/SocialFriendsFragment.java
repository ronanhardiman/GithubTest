package com.igrs.tivic.phone.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts.Data;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Activity.SocialBaseActivity;
import com.igrs.tivic.phone.Adapter.SocialFriendAdapter;
import com.igrs.tivic.phone.AsyncTask.SocialFriendsAsyncTask;
import com.igrs.tivic.phone.Bean.LocationBean;
import com.igrs.tivic.phone.Bean.Base.UserBean;
import com.igrs.tivic.phone.Bean.Social.SocialFriendBean;
import com.igrs.tivic.phone.Bean.Social.SocialFriendBeanList;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Listener.SocialUpadteUIListener;
import com.igrs.tivic.phone.Utils.LocationUtils;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.sqlite.ITIVIC;
	
	public class SocialFriendsFragment extends SocialBaseFragment {
		
//		public static SocialFriendsFragment instance;
		private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		private ViewGroup friendsmain;
		private ExpandableListView expand_frineds;
		private SocialFriendAdapter expandFriendAdapter;
		private SocialUpadteUIListener socialUpadteUIListener;
//		private String[] friendTypes = {getResources().getString(R.string.my_good_friend),getResources().getString(R.string.stranger),getResources().getString(R.string.blanklist)};
		private String[] friendTypes = new String[3];
//		private String[] friendTypes = {"我的好友","陌生人","黑名单"};
//		private List<SocialFriendBean> friendsList;   //all frineds list
		private SocialFriendBeanList myFriendList;
		private List<SocialFriendBean> friendsAll;
		private List<SocialFriendBean> friendsFrd;
		private List<SocialFriendBean> friendsStranger;
		private List<SocialFriendBean> friendsBlankList;
		private Map<Integer, List<SocialFriendBean>> friendsTypeMap;
		private Map<Integer,String> friendsOnline;
		private LinearLayout loadView;
		private final  int STRANGER = 0, BLANKLIST =1,FANS = 2, ONEATTENTION = 3 , TWOATTENTION = 4; 
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		
		}
//		public static SocialFriendsFragment getInstance() { 
//			if(instance == null)
//				instance = new SocialFriendsFragment();
//			return instance;
//		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			friendsmain = (ViewGroup) inflater.inflate(R.layout.social_friends, null);
			init();
			return friendsmain;
		}
	
		private void init() {
			if(friendsOnline == null)
				friendsOnline = new HashMap<Integer, String>();
			if(friendsTypeMap == null)
				friendsTypeMap = new HashMap<Integer, List<SocialFriendBean>>();
			friendsOnline.clear();
			friendsTypeMap.clear();
			if(isAdded()){
				friendTypes[0] = getResources().getString(R.string.my_good_friend);
				friendTypes[1] = getResources().getString(R.string.stranger);
				friendTypes[2] = getResources().getString(R.string.blanklist);
			}
			expand_frineds = (ExpandableListView) friendsmain.findViewById(R.id.expandlist);
			loadView = (LinearLayout) friendsmain.findViewById(R.id.social_friends_loading);
			if(expandFriendAdapter == null)
				expandFriendAdapter = new SocialFriendAdapter(getActivity());
			expandFriendAdapter.setFriendsOnline(friendsOnline);
			expandFriendAdapter.setFriendsTypeMap(friendsTypeMap);
			expand_frineds.setGroupIndicator(null);   //去掉默认的箭头
			expand_frineds.setAdapter(expandFriendAdapter);
			//设置expandlistview条目点击监听器
			expand_frineds.setOnChildClickListener(new OnChildClickListener() {
				
				@Override
				public boolean onChildClick(ExpandableListView parent, View v,
						int groupPosition, int childPosition, long id) {
					jumpToFriendsDetail(groupPosition, childPosition);
					return false;
				}
			});
			socialUpadteUIListener = new SocialUpadteUIListener() {
				
				@Override
				public void notifySocialFriendUI(SocialFriendBeanList result) {
					loadView.setVisibility(View.GONE);
					if(result == null){
						Toast.makeText(mContext, mContext.getString(R.string.get_friend_failed), Toast.LENGTH_LONG).show();
						return;
					}
					int frdOnline=0,strangerOnline=0,blankOnlien=0;
					if(friendsOnline == null)
						friendsOnline = new HashMap<Integer, String>();
					if(friendsTypeMap == null)
						friendsTypeMap = new HashMap<Integer, List<SocialFriendBean>>();
					if(friendsAll == null)
						friendsAll = new ArrayList<SocialFriendBean>();
					if(friendsFrd == null)
						friendsFrd = new ArrayList<SocialFriendBean>();
					if(friendsStranger == null)
						friendsStranger = new ArrayList<SocialFriendBean>();
					if(friendsBlankList == null)
						friendsBlankList = new ArrayList<SocialFriendBean>();
					friendsOnline.clear();
					friendsTypeMap.clear();
					friendsFrd.clear();
					friendsStranger.clear();
					friendsBlankList.clear();
					loadView.setVisibility(View.GONE);
					friendsAll = result.getFriendBeansList();
//					ArrayList<SocialFriendBean> friendsBeans = (ArrayList<SocialFriendBean>) deleteRepeatData(friendsAll);
					for(SocialFriendBean socialFriendBean : friendsAll){
						UserBean userBean = socialFriendBean.getUserBean();
						switch (userBean.getRelation()) {
						//my friend
						case ONEATTENTION:
						case TWOATTENTION:
							if(isOnline(socialFriendBean)){
								socialFriendBean.setOnline(true);
								frdOnline++;
							}else{
								socialFriendBean.setOnline(false);
							}
							friendsFrd.add(socialFriendBean);  //my frined list
							break;
						case FANS:
						case STRANGER:
							if(isOnline(socialFriendBean)){
								socialFriendBean.setOnline(true);
								strangerOnline++;
							}else{
								socialFriendBean.setOnline(false);
							}
							friendsStranger.add(socialFriendBean);
							break;
						case BLANKLIST:
							if(isOnline(socialFriendBean)){
								socialFriendBean.setOnline(true);
								blankOnlien++;
							}else{
								socialFriendBean.setOnline(false);
							}
							friendsBlankList.add(socialFriendBean);
							break;
						default:
							break;
						}
					}
					
					friendsFrd = sortFriendsList(friendsFrd);
					friendsStranger = sortFriendsList(friendsStranger);
					friendsBlankList = sortFriendsList(friendsBlankList);
					
					friendsOnline.put(0, friendTypes[0]+"("+frdOnline+"/"+friendsFrd.size()+")");
					friendsOnline.put(1, friendTypes[1]+"("+strangerOnline+"/"+friendsStranger.size()+")");
					friendsOnline.put(2, friendTypes[2]+"("+blankOnlien+"/"+friendsBlankList.size()+")");
					friendsTypeMap.put(0, friendsFrd);
					friendsTypeMap.put(1, friendsStranger);
					friendsTypeMap.put(2, friendsBlankList);
					expandFriendAdapter.setFriendsOnline(friendsOnline);
					expandFriendAdapter.setFriendsTypeMap(friendsTypeMap); 
					expandFriendAdapter.notifyDataSetChanged();
					expand_frineds.expandGroup(0);
				}
				
				public void notifySocialFriendUI(List<SocialFriendBean> friendsList) {
					
					int frdOnline=0,strangerOnline=0,blankOnlien=0;
					if(friendsOnline == null)
						friendsOnline = new HashMap<Integer, String>();
					if(friendsTypeMap == null)
						friendsTypeMap = new HashMap<Integer, List<SocialFriendBean>>();
					if(friendsFrd == null)
						friendsFrd = new ArrayList<SocialFriendBean>();
					if(friendsStranger == null)
						friendsStranger = new ArrayList<SocialFriendBean>();
					if(friendsBlankList == null)
						friendsBlankList = new ArrayList<SocialFriendBean>();
					friendsOnline.clear();
					friendsTypeMap.clear();
					friendsFrd.clear();
					friendsStranger.clear();
					friendsBlankList.clear();
					loadView.setVisibility(View.GONE);
					for(SocialFriendBean socialFriendBean: friendsList){
						if(socialFriendBean.getFriendType() == 0){
							friendsFrd.add(socialFriendBean);
							if(socialFriendBean.isOnline())
								frdOnline++;
						}else if(socialFriendBean.getFriendType() == 1){
							friendsStranger.add(socialFriendBean);
							if(socialFriendBean.isOnline())
								strangerOnline++;
						}else if(socialFriendBean.getFriendType() == 2){
							friendsBlankList.add(socialFriendBean);
							if(socialFriendBean.isOnline())
								blankOnlien++;
						}
					}
					friendsOnline.put(0, friendTypes[0]+"("+frdOnline+"/"+friendsFrd.size()+")");
					friendsOnline.put(1, friendTypes[1]+"("+strangerOnline+"/"+friendsStranger.size()+")");
					friendsOnline.put(2, friendTypes[2]+"("+blankOnlien+"/"+friendsBlankList.size()+")");
					friendsTypeMap.put(0, friendsFrd);
					friendsTypeMap.put(1, friendsStranger);
					friendsTypeMap.put(2, friendsBlankList);
					expandFriendAdapter.setFriendsOnline(friendsOnline);
					expandFriendAdapter.setFriendsTypeMap(friendsTypeMap); 
					expandFriendAdapter.notifyDataSetChanged();
				}


				@Override
				public void notifySocilaMkFriendUI(SocialFriendBeanList result) {
					
				}

			};
		}
		/**
		 * sort friend list
		 * @param friendsFrd2
		 * @return
		 */
		protected List<SocialFriendBean> sortFriendsList(
				List<SocialFriendBean> friendsList) {
			SocialFriendBean [] friendBeans = new SocialFriendBean[friendsList.size()];
			for(int i = 0 ; i < friendsList.size() ; i++){
				friendBeans[i] = friendsList.get(i);
			}
			for(int i = 0 ; i < friendBeans.length ; i++){
				for(int j = i ; j < friendBeans.length ; j++){
					LocationBean location = TivicGlobal.getInstance().userRegisterBean.getLoactionBean();
					LocationBean friend_location1 = friendBeans[i].getUserBean().getUsrLocation();
					LocationBean friend_location2 = friendBeans[j].getUserBean().getUsrLocation();
					if(LocationUtils.getDistance(location.getLat(), location.getLon(), friend_location1.getLat(), friend_location1.getLon())
							> LocationUtils.getDistance(location.getLat(), location.getLon(), friend_location2.getLat(), friend_location2.getLon())){
						SocialFriendBean socialFriendBean = friendBeans[i];
						friendBeans[i] = friendBeans[j];
						friendBeans[j] = socialFriendBean;
					}
				}
			}
			friendsList.clear();
			for(int i = 0 ; i <friendBeans.length; i++){
				friendsList.add(friendBeans[i]);
			}
			return friendsList;
		}
		protected List<SocialFriendBean> deleteRepeatData(List<SocialFriendBean> friendsAll) {
			/*HashSet<SocialFriendBean> frendsSet = new HashSet<SocialFriendBean>(friendsAll);
			friendsAll.clear();
			friendsAll.addAll(frendsSet);*/
			HashMap<Integer, SocialFriendBean> friendsMap = new HashMap<Integer, SocialFriendBean>();
			for(SocialFriendBean friendBean : friendsAll){
				friendsMap.put(friendBean.getUserBean().getUid(), friendBean);
			}
			friendsAll.clear();
			Collection<SocialFriendBean> c = friendsMap.values();
			friendsAll.addAll(c);
			return friendsAll;
		}
		
		protected boolean isOnline(SocialFriendBean socialFriendBean) {
			try {
				Date date = new Date();
				String lastTime = socialFriendBean.getUserBean().getLast_time();
				Date lastData = df.parse(lastTime);
				long l = date.getTime() - lastData.getTime();
				if(l < 30*60*1000)
					return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
		//read friend data from server by uid
		private void loadFriendData() {
			loadView.setVisibility(View.VISIBLE);
			String uid = TivicGlobal.getInstance().userUID;
			SocialFriendsAsyncTask socialFriendsAsyncTask = new SocialFriendsAsyncTask();
			//获取自己的好友列表传自己的uid，获取好友的好友列表，传好友uid，都用partner_id字段
			socialFriendsAsyncTask.setPartnerUsrID(TivicGlobal.getInstance().userRegisterBean.getUserId());
			socialFriendsAsyncTask.setSocialUpdateUIListener(socialUpadteUIListener);
			socialFriendsAsyncTask.execute(uid); //传递用户uid
		}
		
		private void clearFriendData()
		{
			if(friendsFrd != null)
				friendsFrd.clear();
			if(friendsStranger != null)
				friendsStranger.clear();
			if(friendsBlankList != null)
				friendsBlankList.clear();
			if(friendsOnline != null)
				friendsOnline.clear();
			if(friendsTypeMap != null)
				friendsTypeMap.clear();
		}
		public void jumpToFriendsDetail(int groupPosition, int childPosition)
		{
			//zhanglr add here
			UIUtils.setCurrentFuncID(FuncID.ID_SOCIAL_FRIENDS_DETAIL);
			SocialBaseActivity ac = (SocialBaseActivity)this.getActivity();
			ac.setMenuBarFocus(FuncID.ID_SOCIAL_FRIENDS_DETAIL);
			int currentUid = 0;
//			UIUtils.Logd("SocialFriendsFragment", "groupPosition = " + groupPosition + "childPosition = " + childPosition);
			currentUid = friendsTypeMap.get(groupPosition).get(childPosition).getUserBean().getUid();
			//end add
			SocialFriendsDetailFragment fragment = new SocialFriendsDetailFragment();		
			FragmentTransaction ft = getFragmentManager().beginTransaction();	
			//ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			Bundle bundle = new Bundle();
			bundle.putInt("uid", currentUid);
			fragment.setArguments(bundle);
			ft.setCustomAnimations(R.anim.fragment_slide_left_enter,  
	                R.anim.fragment_slide_left_exit,  
	                R.anim.fragment_slide_right_enter,  
	                R.anim.fragment_slide_right_exit);
			ft.replace(R.id.social_empty, fragment);
			ft.addToBackStack(null);
			ft.commit();
		}
		
		
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

		}
		
		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			super.onCreateOptionsMenu(menu, inflater);
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			return super.onOptionsItemSelected(item);
		}
		
		@Override
		public void onPause() {
			super.onPause();
		}
		
		
		@Override
		public void onStop() {
			super.onStop();	
			clearFriendData();
		}
		
		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
		}
		
		@Override
		public void onStart() {
			super.onStart();
		}
		
		@Override
		public void onResume() {
			UIUtils.setCurrentFuncID(FuncID.ID_SOCIAL_FRIENDS);
			SocialBaseActivity ac = (SocialBaseActivity)this.getActivity();
			ac.setMenuBarFocus(FuncID.ID_SOCIAL_FRIENDS);
			super.onResume();
			loadFriendData();
		}
		
		@Override
		public void onDestroy() {
			super.onDestroy();
		}
		
		@Override
		public void onDestroyView() {
			super.onDestroyView();
		}
	}