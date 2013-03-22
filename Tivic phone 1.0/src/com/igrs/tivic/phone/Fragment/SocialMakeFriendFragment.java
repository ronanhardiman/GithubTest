package com.igrs.tivic.phone.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Activity.SocialBaseActivity;
import com.igrs.tivic.phone.Adapter.SocialMkFriendListAdapter;
import com.igrs.tivic.phone.AsyncTask.SocialMkFriendsAsyncTask;
import com.igrs.tivic.phone.AsyncTask.SocialRecommedListAsyncTask;
import com.igrs.tivic.phone.Bean.Base.UserBean;
import com.igrs.tivic.phone.Bean.Login.UserPOIItemBean;
import com.igrs.tivic.phone.Bean.Login.UserRegisterBean;
import com.igrs.tivic.phone.Bean.PGC.NewsBean;
import com.igrs.tivic.phone.Bean.Social.SocialFriendBean;
import com.igrs.tivic.phone.Bean.Social.SocialFriendBeanList;
import com.igrs.tivic.phone.Bean.Social.SocialPoiBean;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Listener.SocialRecommendListener;
import com.igrs.tivic.phone.Listener.SocialUpadteUIListener;
import com.igrs.tivic.phone.Utils.UIUtils;

	public class SocialMakeFriendFragment extends SocialBaseFragment implements OnClickListener {

//		public static SocialMakeFriendFragment instance;
		private List<SocialFriendBean> mkfBeans;
		private List<SocialFriendBean> mkfMaleBeans;
		private List<SocialFriendBean> mkfFemaleBeans;
		private ViewGroup mkfriendmain;
		private LinearLayout loadView;
		private SocialUpadteUIListener socialUpadteUIListener;
		private SocialMkFriendListAdapter mkFriendListAdapter;
		private Button bt_all,bt_female,bt_male;
		private LinearLayout select_condition;
		private TextView tv_seconditon;
		private StringBuffer secondition = new StringBuffer();
		private SocialRecommendListener recommendListener;
		private int mark = 0; // 0为显示所有，1为显示男 ，2为显示女
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			//setHasOptionsMenu(true);
		}
//		public static SocialMakeFriendFragment getInstance() { 
//			if(instance == null)
//				instance = new SocialMakeFriendFragment();
//			return instance;
//		}

		public void setMkfBeans(List<SocialFriendBean> mkfBeans) {
			this.mkfBeans = mkfBeans;
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			mContext = getActivity().getApplicationContext();
			mkfriendmain = (ViewGroup) inflater.inflate(R.layout.social_mkfriend, null);
			init();
			return mkfriendmain;
		}
	
		private void init() {
			if(mkfBeans == null)
				mkfBeans = new ArrayList<SocialFriendBean>();
			if(mkfFemaleBeans == null)
				mkfFemaleBeans = new ArrayList<SocialFriendBean>();
			if(mkfMaleBeans == null)
				mkfMaleBeans = new ArrayList<SocialFriendBean>();
			mkfFemaleBeans.clear();
			mkfMaleBeans.clear();
			mkfBeans.clear();
			ListView lv = (ListView) mkfriendmain.findViewById(R.id.mkf_list);
			loadView = (LinearLayout) mkfriendmain.findViewById(R.id.social_mkfriend_loading);
			bt_all = (Button) mkfriendmain.findViewById(R.id.social_mkd_all);
			bt_female = (Button) mkfriendmain.findViewById(R.id.social_mkd_woman);
			bt_male = (Button) mkfriendmain.findViewById(R.id.social_mkd_man);
			select_condition = (LinearLayout) mkfriendmain.findViewById(R.id.social_mkfriend_select_condition);
			tv_seconditon = (TextView) mkfriendmain.findViewById(R.id.social_mkd_seconditon);
			bt_all.setOnClickListener(this);
			bt_all.setBackgroundResource(R.drawable.social_page_all_press);
			bt_all.requestFocus();
			bt_female.setOnClickListener(this);
			bt_male.setOnClickListener(this);
			mkFriendListAdapter = new SocialMkFriendListAdapter(getActivity());
			mkFriendListAdapter.setMkfBeans(mkfBeans);
			lv.setAdapter(mkFriendListAdapter);
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long id) {
					jumpToFriendsDetail(position);
					
				}
			});
			recommendListener = new SocialRecommendListener() {
				
				@Override
				public void notifySocialRecommendUI(List<UserPOIItemBean> recommendList) {
					initSelectCondition(recommendList);
				}
			};
			socialUpadteUIListener = new SocialUpadteUIListener() {
				
				@Override
				public void notifySocilaMkFriendUI(SocialFriendBeanList result) {
					loadView.setVisibility(View.GONE);
					if(result == null){
						return;
					}
					
					mkfBeans = result.getFriendBeansList();
					mkFriendListAdapter.setMkfBeans(mkfBeans);
					mkFriendListAdapter.notifyDataSetChanged();
					
					
					for(SocialFriendBean socialFriendBean : mkfBeans){
						if(socialFriendBean.getUserBean().getUserGender() == 0){
							mkfFemaleBeans.add(socialFriendBean);
						}else{
							mkfMaleBeans.add(socialFriendBean);
						}
					}
					
				}
				public void notifySocilaMkFriendUI(List<SocialFriendBean> mkfBeans) {
					//updata mkfriend
					loadView.setVisibility(View.GONE);
					setMkfBeans(mkfBeans);
					mkFriendListAdapter.setMkfBeans(mkfBeans);
					mkFriendListAdapter.notifyDataSetChanged();
					if(mkfFemaleBeans == null)
						mkfFemaleBeans = new ArrayList<SocialFriendBean>();
					if(mkfMaleBeans == null)
						mkfMaleBeans = new ArrayList<SocialFriendBean>();
					mkfFemaleBeans.clear();
					mkfMaleBeans.clear();
					
						for(SocialFriendBean socialFriendBean : mkfBeans){
							if(socialFriendBean.getUserBean().getUserGender() == 0){
								mkfFemaleBeans.add(socialFriendBean);
							}else{
								mkfMaleBeans.add(socialFriendBean);
							}
						}
					
				}

				@Override
				public void notifySocialFriendUI(SocialFriendBeanList result) {
					
				}

				
			};
		}
		private void loadMkFriendData() {
			loadView.setVisibility(View.VISIBLE);
			SocialMkFriendsAsyncTask socialMkFriendsAsyncTask = new SocialMkFriendsAsyncTask();
			socialMkFriendsAsyncTask.setSocialUpadteUIListener(socialUpadteUIListener);
			socialMkFriendsAsyncTask.execute(0);
		}
		private void clearMkFriendData(){
			if(mkfBeans != null)
				mkfBeans.clear();
			if(mkfBeans != null)
				mkfFemaleBeans.clear();
			if(mkfBeans != null)
				mkfMaleBeans.clear();
		}
		public void jumpToFriendsDetail(int position)
		{
			//zhanglr add here
//			UIUtils.setCurrentFuncID(FuncID.ID_SOCIAL_FRIENDS_DETAIL);
//			SocialBaseActivity ac = (SocialBaseActivity)this.getActivity();
//			ac.setMenuBarFocus(FuncID.ID_SOCIAL_FRIENDS_DETAIL);

			//end add
			SocialFriendsDetailFragment fragment = new SocialFriendsDetailFragment();
			FragmentTransaction ft = getFragmentManager().beginTransaction();	
			//ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.setCustomAnimations(R.anim.fragment_slide_left_enter,  
	                R.anim.fragment_slide_left_exit,  
	                R.anim.fragment_slide_right_enter,  
	                R.anim.fragment_slide_right_exit);
			Bundle bundle = new Bundle();  
			if(mkfBeans!= null && mkfBeans.get(position)!= null)
			{
				bundle.putInt("uid", mkfBeans.get(position).getUserBean().getUid()); 
	            fragment.setArguments(bundle);
			}
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
//			 menu.add("Menu 1a").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
//	            menu.add("Menu 1b").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			//Toast.makeText(getActivity(), "index is"+getShownIndex()+" && menu text is "+item.getTitle(), 1000).show();
			return super.onOptionsItemSelected(item);
		}
		
		@Override
		public void onPause() {
			super.onPause();
			System.out.println("Fragment-->onPause");
		}
		
		
		@Override
		public void onStop() {
			super.onStop();
//			clearMkFriendData();
			System.out.println("Fragment-->onStop");
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
			UIUtils.setCurrentFuncID(FuncID.ID_SOCIAL_MKFRIEND);
			SocialBaseActivity ac = (SocialBaseActivity)this.getActivity();
			ac.setMenuBarFocus(FuncID.ID_SOCIAL_MKFRIEND);
			super.onResume();
			loadRecommendList();
			loadMkFriendData();
			
		}
		
		
		private void loadRecommendList() {
			SocialRecommedListAsyncTask recommedListAsyncTask = new SocialRecommedListAsyncTask();
			recommedListAsyncTask.setRecommendListener(recommendListener);
			recommedListAsyncTask.execute(0);
		}
		private void initSelectCondition(List<UserPOIItemBean> recommendList) {
			UserRegisterBean rBean = TivicGlobal.getInstance().userRegisterBean;
			Map<String, Integer> filterItems = rBean.getFilterItems();
			List<SocialPoiBean> filterSocialPoiBeans = rBean.getFilterSocialPoiBeans();
			if(filterItems.size()==0 && filterSocialPoiBeans.size()==0){
				select_condition.setVisibility(View.GONE);
			}
			if(filterItems.containsKey(TivicGlobal.getInstance().AGE)){
				Integer age = filterItems.get(TivicGlobal.getInstance().AGE);
				String period = String.valueOf(age);
				if(isAdded()){
					secondition.append(period.substring(2)+ getResources().getString(R.string.hou));
					secondition.append("  ");
				}
			}
			if(filterItems.containsKey(TivicGlobal.getInstance().GENDER)){
				//gender ：0--femail,1--mail
				int g = filterItems.get(TivicGlobal.getInstance().GENDER);
				String gender = "";
				if(g == 0){
					if(isAdded())
						gender = getResources().getString(R.string.female);
				}else{
					if(isAdded())
						gender = getResources().getString(R.string.male);
				}
				secondition.append(gender);
				secondition.append("  ");
			}
			if(filterItems.containsKey(TivicGlobal.getInstance().STAR)){
				//constellation: 0--Aries,1--Taurus,2--Gemini,3--Cancer,4--Leo,5--Virgo
				//6--libra,7--Scorpio,8--Sagittarius,9--Capricornus,10--aquarius,11--Pisces
				int c = filterItems.get(TivicGlobal.getInstance().STAR);
				switch (c) {
				case 0:
					if(isAdded())
						secondition.append(getResources().getString(R.string.Aries));
					break;
				case 1:
					if(isAdded())
						secondition.append(getResources().getString(R.string.Taurus));
					break;
				case 2:
					if(isAdded())
						secondition.append(getResources().getString(R.string.Gemini));
					break;
				case 3:
					if(isAdded())
						secondition.append(getResources().getString(R.string.Cancer));
					break;
				case 4:
					if(isAdded())
						secondition.append(getResources().getString(R.string.Leo));
					break;
				case 5:
					if(isAdded())
						secondition.append(getResources().getString(R.string.Virgo));
					break;
				case 6:
					if(isAdded())
						secondition.append(getResources().getString(R.string.libra));
					break;
				case 7:
					if(isAdded())
						secondition.append(getResources().getString(R.string.Scorpio));
					break;
				case 8:
					if(isAdded())
						secondition.append(getResources().getString(R.string.Sagittarius));
					break;
				case 9:
					if(isAdded())
						secondition.append(getResources().getString(R.string.Capricornus));
					break;
				case 10:
					if(isAdded())
						secondition.append(getResources().getString(R.string.aquarius));
					break;
				case 11:
					if(isAdded())
						secondition.append(getResources().getString(R.string.Pisces));
					break;
				default:
					break;
				}
				secondition.append("  ");
			}
			for(UserPOIItemBean poiItemBean : recommendList){
				for(int i = 0 ;i<filterSocialPoiBeans.size();i++){
					if(Integer.valueOf(poiItemBean.getPoi_key()) == filterSocialPoiBeans.get(i).getValue()){
						secondition.append(poiItemBean.getPoi_text());
						secondition.append("  ");
					}
				}
			}
			tv_seconditon.setVisibility(View.VISIBLE);
			tv_seconditon.setText(secondition);
		}
		@Override
		public void onDestroy() {
			super.onDestroy();
		}
		
		@Override
		public void onDestroyView() {
			super.onDestroyView();
		}
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.social_mkd_all:
				//update data
				if(mark != 0){
					mkFriendListAdapter.setMkfBeans(mkfBeans);
					mkFriendListAdapter.notifyDataSetChanged();
					mark = 0;
				}
				//update ui
				bt_all.setBackgroundResource(R.drawable.social_page_all_press);
				bt_male.setBackgroundResource(R.drawable.social_page_man_normal);
				bt_female.setBackgroundResource(R.drawable.social_page_woman_normal);
				
				break;
			case R.id.social_mkd_man:
				if(mark != 1){
					mkFriendListAdapter.setMkfBeans(mkfMaleBeans);
					mkFriendListAdapter.notifyDataSetChanged();
					mark = 1;
				}
				bt_all.setBackgroundResource(R.drawable.social_page_all_normal);
				bt_male.setBackgroundResource(R.drawable.social_page_man_press);
				bt_female.setBackgroundResource(R.drawable.social_page_woman_normal);
				break;
			case R.id.social_mkd_woman:
				if(mark != 2){
					mkFriendListAdapter.setMkfBeans(mkfFemaleBeans);
					mkFriendListAdapter.notifyDataSetChanged();
					mark = 2;
				}
				bt_all.setBackgroundResource(R.drawable.social_page_all_normal);
				bt_male.setBackgroundResource(R.drawable.social_page_man_normal);
				bt_female.setBackgroundResource(R.drawable.social_page_woman_press);
				break;
			default:
				break;
			}
		}
	}