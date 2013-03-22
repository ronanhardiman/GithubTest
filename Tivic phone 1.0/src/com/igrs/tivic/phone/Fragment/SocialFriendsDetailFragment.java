package com.igrs.tivic.phone.Fragment;

//import java.util.Calendar;
//import java.util.List;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
//import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.test.UiThreadTest;
//import android.util.Log;
//import android.view.KeyEvent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
//import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
//import android.view.View.OnKeyListener;
//import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
//import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Activity.SocialBaseActivity;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.TivicGlobal;
//import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Impl.SocialFriendsDetailsImpl;
//import com.igrs.tivic.phone.Interface.ISocialFriendDetails;
import com.igrs.tivic.phone.Utils.LocationUtils;
import com.igrs.tivic.phone.Utils.UIUtils;
//import com.igrs.tivic.phone.Utils.Utils;
//import com.igrs.tivic.phone.Adapter.SocialPhotoAdapter;
import com.igrs.tivic.phone.Bean.Base.UserBean;
import com.igrs.tivic.phone.Bean.Login.UserRegisterBean;
//import com.igrs.tivic.phone.Bean.PGC.TVFindBean;
//import com.igrs.tivic.phone.ViewGroup.SocialUsrInfoDetailBase;
import com.igrs.tivic.phone.widget.AsyncImageView;
import com.umeng.common.net.p;

public class SocialFriendsDetailFragment extends SocialBaseFragment implements
		OnClickListener {
	private Boolean flag = true;
	private ViewGroup fdsdetailmain;
//	private ImageView iv_user;
	private AsyncImageView friendIcon;
//	private SocialUsrInfoDetailBase detail;
	// private SocialUsrInfoMyPhoto photo;
	private CheckBox chk_xiangce;
	private Button bt_siliao, bt_social_gengduo,chk_focus;
//	private SocialFriendsInfoBaseInfoFragment friend_base;
	private PopupWindow mPopupWindow;
	private View popupwindow;
	private Context mContext;
	private Context context;
	private SocialFriendsDetailsImpl friends_detail;
	private TextView username,tv_social_signature,tv_friend_distance;
	private ImageView tv_friends_connection,iv_lahei;
	private Handler handler;//,focus_handler,unfocus_handler,focus_sx_handler,unfocus_sx_handler;
	//add by chen qiang
//	private Handler mHandler;
	private UserBean userBean;
//	private boolean isFriendInBlack = false;
//	private boolean isFriendBetweenGZ = false;
//	private boolean isFriendGuanzhu = false;
	private int userRelation;
	private UserRegisterBean userRegisterBean;
	private SocialFriendsInfoBaseInfoFragment info;
	private TextView tv_ta;
//add by zhanglr
	private final String TAG = "SocialFriendsDetailFragment";
	private int currentUid = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle bundle = this.getArguments();
		userRegisterBean = TivicGlobal.instance.userRegisterBean;
		//add by chen
//		UIUtils.Logi("chen", "userRegisterBean.friendUids的长度="+userRegisterBean.friendUids.size());
		if(userRegisterBean.isNeedsUid && userRegisterBean.friendUids.size()>0) {
				currentUid = userRegisterBean.friendUids.removeFirst();
//				UIUtils.Logi("chen", "from list");
		}
		 else {
			if(bundle.getInt("uid")!= 0){
				currentUid = bundle.getInt("uid");
			}else{
				currentUid = TivicGlobal.getInstance().mCurrentUserPhotoID;
			}
//			UIUtils.Logi("chen", "new uid");
		}
//		UIUtils.Logi("chen", "uid==="+currentUid);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mContext = this.getActivity().getApplicationContext();
		context = getActivity();
		//add by zhanglr
//		Bundle bundle = this.getArguments();
//		userRegisterBean = TivicGlobal.instance.userRegisterBean;
//		//add by chen
//		if(userRegisterBean.isNeedsUid && userRegisterBean.friendUids.size()>0) {
//				currentUid = userRegisterBean.friendUids.removeFirst();
//				UIUtils.Logi("chen", "from list");
//		}
//		 else {
//			if(bundle.getInt("uid")!= 0){
//				currentUid = bundle.getInt("uid");
//			}else{
//				currentUid = TivicGlobal.getInstance().mCurrentUserPhotoID;
//			}
//			UIUtils.Logi("chen", "new uid");
//		}
//		UIUtils.Logi("chen", "uid==="+currentUid);
//		UIUtils.Logd(TAG, "Bundle uid = " + currentUid);
		
		fdsdetailmain = (ViewGroup) inflater.inflate(R.layout.social_frddetail, null);
		tv_ta = (TextView) fdsdetailmain.findViewById(R.id.tv_ta);
	    info = new SocialFriendsInfoBaseInfoFragment();
//		info.getView()
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.friend_baseinfo, info);
//		friendIcon = (AsyncImageView) info.getView().findViewById(R.id.social_userpic);
////		friendIcon.setOnClickListener(this);
//		chk_xiangce = (CheckBox) info.getView().findViewById(R.id.chk_social_xiangce);
//		bt_siliao = (Button)info.getView().findViewById(R.id.chk_social_siliao);
//		bt_social_gengduo = (Button) info.getView().findViewById(R.id.bt_social_gengduo);
//		username = (TextView) info.getView().findViewById(R.id.tv_username);
//		tv_social_signature = (TextView) info.getView().findViewById(R.id.tv_social_signature);
//		tv_friend_distance = (TextView) info.getView().findViewById(R.id.friends_name);
//		tv_friends_connection = (ImageView) info.getView().findViewById(R.id.friends_connection);
		ft.commit();
		
		SocialUsrInfoInterestFragment interest_fragment = new SocialUsrInfoInterestFragment();
		Bundle bundle_interest = new Bundle();  
		bundle_interest.putInt("uid", currentUid); 
		interest_fragment.setArguments(bundle_interest);
		
		FragmentTransaction ft_interest = getFragmentManager()
				.beginTransaction();
		ft_interest.replace(R.id.socialcontainer, interest_fragment);
		ft_interest.commit();
		init();
		return fdsdetailmain;
	}

	private void init() {
		

		friends_detail = SocialFriendsDetailsImpl.getInstance();
		friends_detail.getUserBaseInfo(currentUid);
		handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				userBean = (UserBean) msg.obj;
				if(userBean == null) {
					UIUtils.ToastMessage(getActivity(), R.string.net_error);
					return;
				}
				friendIcon = (AsyncImageView) info.getView().findViewById(R.id.social_userpic);
//				friendIcon.setOnClickListener(this);
				chk_xiangce = (CheckBox) info.getView().findViewById(R.id.chk_social_xiangce);
				bt_siliao = (Button)info.getView().findViewById(R.id.chk_social_siliao);
				bt_social_gengduo = (Button) info.getView().findViewById(R.id.bt_social_gengduo);
				username = (TextView) info.getView().findViewById(R.id.tv_username);
				tv_social_signature = (TextView) info.getView().findViewById(R.id.tv_social_signature);
				tv_friend_distance = (TextView) info.getView().findViewById(R.id.friends_name);
				tv_friends_connection = (ImageView) info.getView().findViewById(R.id.friends_connection);
				UIUtils.Logi("chen", "currentUid="+currentUid+",userRegisterBean.getUserUID()="+userRegisterBean.getUserUID());
				if(currentUid == userRegisterBean.getUserUID()) {
					tv_friend_distance.setVisibility(View.INVISIBLE);
					tv_friends_connection.setVisibility(View.INVISIBLE);
				}
//				friendIcon = (AsyncImageView) fdsdetailmain.findViewById(R.id.social_userpic);
////				friendIcon.setOnClickListener(this);
//				chk_xiangce = (CheckBox) fdsdetailmain.findViewById(R.id.chk_social_xiangce);
//				bt_siliao = (Button) fdsdetailmain.findViewById(R.id.chk_social_siliao);
//				bt_social_gengduo = (Button) fdsdetailmain.findViewById(R.id.bt_social_gengduo);
//				username = (TextView) fdsdetailmain.findViewById(R.id.tv_username);
//				tv_social_signature = (TextView) fdsdetailmain.findViewById(R.id.tv_social_signature);
//				tv_friend_distance = (TextView) fdsdetailmain.findViewById(R.id.friends_name);
//				tv_friends_connection = (ImageView) fdsdetailmain.findViewById(R.id.friends_connection);
				username.setText(userBean.getUserNickName());
				if(TextUtils.isEmpty(userBean.getUserSign())) {
					tv_social_signature.setText(R.string.friend_default_sign);
				} else {
					tv_social_signature.setText(userBean.getUserSign());
				}
				String distance = LocationUtils.getDistance(getActivity(), userRegisterBean.getLoactionBean(), userBean.getUsrLocation());
				tv_friend_distance.setText(distance);
				String img_uri = UIUtils.getUsrAvatar(currentUid);
				friendIcon.setDefaultImageResource(R.drawable.base_default_avater);
				friendIcon.setUrl(img_uri);
				friendIcon.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						chk_xiangce.setChecked(false);
						SocialUsrInfoInterestFragment interest_fragment = new SocialUsrInfoInterestFragment();
						Bundle bundle_interest = new Bundle();  
						bundle_interest.putInt("uid", currentUid); 
						interest_fragment.setArguments(bundle_interest);
						FragmentTransaction ft_interest = getFragmentManager()
								.beginTransaction();
						ft_interest.replace(R.id.socialcontainer, interest_fragment);
						// zhanglr delete
						// ft_interest.addToBackStack("null");
						ft_interest.commit();
					}
				});
//				UIUtils.Logi("chen", "userBean.getRelation()==="+userBean.getRelation());
				if(userBean.getRelation() == 0){
					tv_friends_connection.setImageResource(R.drawable.social_friends_guanzhu_04);
					userRelation = 0;
				}else if(userBean.getRelation()== 2){
					tv_friends_connection.setImageResource(R.drawable.social_friends_guanzhu_03);
					userRelation = 2;
				}else if(userBean.getRelation()== 3){
					tv_friends_connection.setImageResource(R.drawable.social_friends_guanzhu_01);
					userRelation = 3;
				}else if(userBean.getRelation() == 4){
					userRelation = 4;
					tv_friends_connection.setImageResource(R.drawable.social_friends_guanzhu_02);
				} else if(userBean.getRelation() == 1) {
					tv_friends_connection.setImageResource(R.drawable.social_friends_guanzhu_04);
					userRelation = 1;
				}
				bt_social_gengduo.setOnClickListener(new Button.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(currentUid != userRegisterBean.getUserUID()) {
							chk_xiangce.setChecked(false);
							showDialog();
							initPopup();
						} else {
							UIUtils.ToastMessage(context, R.string.check_youself);
						}
					}
				});
				
				chk_xiangce.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						tv_ta.setVisibility(View.GONE);
						if(isChecked){
							SocialFriendsPhotoFragment xiangce = new SocialFriendsPhotoFragment();
							xiangce.setPhoto_uid(currentUid);
							FragmentTransaction ft_xiangce = getFragmentManager()
									.beginTransaction();
							ft_xiangce.replace(R.id.socialcontainer, xiangce);
							// zhanglr delete
							// ft_xiangce.addToBackStack(null);
							ft_xiangce.commit();
						}else{
							SocialUsrInfoInterestFragment interest_fragment = new SocialUsrInfoInterestFragment();
							Bundle bundle_interest = new Bundle();  
							bundle_interest.putInt("uid", currentUid); 
							interest_fragment.setArguments(bundle_interest);
							FragmentTransaction ft_interest = getFragmentManager()
									.beginTransaction();
							ft_interest.replace(R.id.socialcontainer, interest_fragment);
							// zhanglr delete
							// ft_interest.addToBackStack("null");
							ft_interest.commit();
						}
					}
				});
				
				bt_siliao.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(currentUid == userRegisterBean.getUserUID()) {
							UIUtils.ToastMessage(mContext, R.string.siliao_youself);
							return;
						}
						UIUtils.setCurrentFuncID(FuncID.ID_SOCIAL_MESSAGE_DETAIL);
						SocialBaseActivity ac = (SocialBaseActivity) getActivity();
						ac.setMenuBarFocus(FuncID.ID_SOCIAL_MESSAGE_DETAIL);
						// end add
						SocialMessageDetailFragment fragment = new SocialMessageDetailFragment();
						Bundle bd = new Bundle();
						bd.putInt("uid", currentUid);
						fragment.setArguments(bd);
						FragmentTransaction ft = getFragmentManager().beginTransaction();
						ft.replace(R.id.social_empty, fragment);
						ft.addToBackStack(null);
						ft.commit();
					}
				});
			}
			
		};

		
		
		friends_detail.setHandler(handler);
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
		if(currentUid!=0)
			TivicGlobal.getInstance().mCurrentUserPhotoID = currentUid;
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
		switch (UIUtils.getCurrentFuncID()) {
		case FuncID.ID_SOCIAL_MESSAGE_DETAIL:
			UIUtils.setCurrentFuncID(FuncID.ID_SOCIAL_FRIENDS_DETAIL);
			SocialBaseActivity ac = (SocialBaseActivity) this.getActivity();
			ac.setMenuBarFocus(FuncID.ID_SOCIAL_FRIENDS_DETAIL);
			break;
		}

		UIUtils.setCurrentFuncID(FuncID.ID_SOCIAL_FRIENDS_DETAIL);
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
	//add by chen
	public void initRelationImages() {
		if(userRelation == 0){
			tv_friends_connection.setImageResource(R.drawable.social_friends_guanzhu_04);
			chk_focus.setBackgroundResource(R.drawable.guanzhu_dx);
		}else if(userRelation == 2){
			tv_friends_connection.setImageResource(R.drawable.social_friends_guanzhu_03);
			chk_focus.setBackgroundResource(R.drawable.guanzhu_sx);
		}else if(userRelation == 3){
			tv_friends_connection.setImageResource(R.drawable.social_friends_guanzhu_01);
			chk_focus.setBackgroundResource(R.drawable.quxiao_dx);
		}else if(userRelation  == 4){
			tv_friends_connection.setImageResource(R.drawable.social_friends_guanzhu_02);
			chk_focus.setBackgroundResource(R.drawable.quxiao_sx_selected);
		}else if(userRelation  == 1){
			tv_friends_connection.setImageResource(R.drawable.social_friends_guanzhu_04);
			chk_focus.setEnabled(false);
		}
	}
	public void initPopup() {
//		UIUtils.Logi("chen", "relation================"+userRelation);
		ImageView iv_shoucang = (ImageView) popupwindow.findViewById(R.id.ta_shoucang);
		ImageView iv_haoyou = (ImageView) popupwindow.findViewById(R.id.ta_haoyou);
		ImageView iv_line1 = (ImageView) popupwindow.findViewById(R.id.iv_line1);
		ImageView iv_line2 = (ImageView) popupwindow.findViewById(R.id.iv_line2);
//		ImageView iv_lahei2 = (ImageView) popupwindow.findViewById(R.id.iv_lahei);
		if(userRelation == 0){
			chk_focus.setBackgroundResource(R.drawable.guanzhu_dx);
			iv_haoyou.setVisibility(View.GONE);
			iv_shoucang.setVisibility(View.GONE);
			iv_line1.setVisibility(View.GONE);
			iv_line2.setVisibility(View.GONE);
			iv_lahei.setImageResource(R.drawable.lahei);
			chk_focus.setEnabled(true);
		}else if(userRelation == 3){
			chk_focus.setBackgroundResource(R.drawable.quxiao_dx);
			iv_haoyou.setVisibility(View.GONE);
			iv_shoucang.setVisibility(View.GONE);
			iv_line1.setVisibility(View.GONE);
			iv_line2.setVisibility(View.GONE);
			iv_lahei.setImageResource(R.drawable.lahei);
			chk_focus.setEnabled(true);
		}else if(userRelation == 4){
			chk_focus.setBackgroundResource(R.drawable.quxiao_sx_selected);
			iv_haoyou.setVisibility(View.VISIBLE);
			iv_shoucang.setVisibility(View.VISIBLE);
			iv_line1.setVisibility(View.VISIBLE);
			iv_line2.setVisibility(View.VISIBLE);
			iv_lahei.setImageResource(R.drawable.lahei);
			chk_focus.setEnabled(true);
		}else if(userRelation == 1){
			chk_focus.setEnabled(false);
			iv_haoyou.setVisibility(View.GONE);
			iv_shoucang.setVisibility(View.GONE);
			iv_line1.setVisibility(View.GONE);
			iv_line2.setVisibility(View.GONE);
			iv_lahei.setImageResource(R.drawable.quxiaolahei);
			chk_focus.setEnabled(false);
		}else if(userRelation == 2){
			chk_focus.setBackgroundResource(R.drawable.guanzhu_sx);
			iv_haoyou.setVisibility(View.GONE);
			iv_shoucang.setVisibility(View.GONE);
			iv_line1.setVisibility(View.GONE);
			iv_line2.setVisibility(View.GONE);
			iv_lahei.setImageResource(R.drawable.lahei);
			chk_focus.setEnabled(true);
		}
	}
	private void showDialog() {
		popupwindow = LayoutInflater.from(mContext).inflate(
				R.layout.social_pop_gengduo, null);
		popupwindow.findViewById(R.id.ta_shoucang).setOnClickListener(this);
		popupwindow.findViewById(R.id.ta_haoyou).setOnClickListener(this);
		popupwindow.findViewById(R.id.ta_fatie).setOnClickListener(this);
		chk_focus = (Button) popupwindow.findViewById(R.id.chk_focus);
		chk_focus.setOnClickListener(this);
		iv_lahei = (ImageView) popupwindow.findViewById(R.id.iv_lahei);
		if(userRelation == 1) {
			iv_lahei.setImageResource(R.drawable.quxiaolahei);
		} else {
			iv_lahei.setImageResource(R.drawable.lahei);
		}
		iv_lahei.setOnClickListener(this);
//		chk_lahei = (CheckBox) popupwindow.findViewById(R.id.chk_lahei);
//		if(isFriendInBlack) {
//			chk_lahei.setBackgroundResource(R.drawable.quxiaolahei);
//			chk_lahei.setChecked(true);
//		} else {
//			chk_lahei.setBackgroundResource(R.drawable.lahei);
//			chk_lahei.setChecked(false);
//		}
//		chk_lahei.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				// TODO Auto-generated method stub
//				if(isChecked){
//					friends_detail.getBlockFriend(currentUid);
//					chk_lahei.setBackgroundResource(R.drawable.quxiaolahei);
//					isFriendInBlack=true;
//					chk_focus.setEnabled(false);
//					userRelation = 1;
//					
//					UIUtils.ToastMessage(getActivity(), "拉黑了");
//				}else {
//					friends_detail.getUnBlockFriend(currentUid);
//					chk_lahei.setBackgroundResource(R.drawable.lahei);
//					isFriendInBlack=false;
//					chk_focus.setEnabled(true);
//					UIUtils.ToastMessage(getActivity(), "取消拉黑了");
//				}
//			}
//		});
		mPopupWindow = new PopupWindow(popupwindow, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.showAsDropDown(bt_social_gengduo, -100, 0);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.social_userpic:
			chk_xiangce.setChecked(false);
			SocialUsrInfoInterestFragment interest_fragment = new SocialUsrInfoInterestFragment();
			Bundle bundle = new Bundle();  
			bundle.putInt("uid", currentUid); 
			interest_fragment.setArguments(bundle);
			FragmentTransaction ft_interest = getFragmentManager()
					.beginTransaction();
			ft_interest.replace(R.id.socialcontainer, interest_fragment);
			// zhanglr delete
			// ft_interest.addToBackStack("null");
			ft_interest.commit();
			break;
		case R.id.chk_social_xiangce:
//			SocialFriendsPhotoFragment xiangce = new SocialFriendsPhotoFragment();
//			FragmentTransaction ft_xiangce = getFragmentManager()
//					.beginTransaction();
//			ft_xiangce.replace(R.id.socialcontainer, xiangce);
//			// zhanglr delete
//			// ft_xiangce.addToBackStack(null);
//			ft_xiangce.commit();

			break;
		case R.id.bt_social_gengduo:
//			chk_xiangce.setChecked(false);
//			showDialog();
			break;
		case R.id.chk_social_siliao:
			// zhanglr add here
			// if(mPopupWindow.isShowing()){
			// mPopupWindow.dismiss();
			// mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			// }
//			UIUtils.setCurrentFuncID(FuncID.ID_SOCIAL_MESSAGE_DETAIL);
//			SocialBaseActivity ac = (SocialBaseActivity) this.getActivity();
//			ac.setMenuBarFocus(FuncID.ID_SOCIAL_MESSAGE_DETAIL);
//			// end add
//			SocialMessageDetailFragment fragment = new SocialMessageDetailFragment();
//			Bundle bd = new Bundle();
//			bd.putInt("uid", currentUid);
//			fragment.setArguments(bd);
//			FragmentTransaction ft = getFragmentManager().beginTransaction();
//			ft.replace(R.id.social_empty, fragment);
//			ft.addToBackStack(null);
//			ft.commit();
			break;
		case R.id.ta_fatie:
			if (mPopupWindow.isShowing()) {
				mPopupWindow.dismiss();
				mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			}
			tv_ta.setVisibility(View.VISIBLE);
			tv_ta.setText(R.string.ta_fatie);
			SocialUGCFragment fragment_ugc = new SocialUGCFragment();
			Bundle bundle_ugc = new Bundle();
			bundle_ugc.putInt("uid", currentUid);
			fragment_ugc.setArguments(bundle_ugc);
			FragmentTransaction ft_ugc = getFragmentManager()
					.beginTransaction();
			ft_ugc.replace(R.id.socialcontainer, fragment_ugc);
			// zhanglr delete
			// ft_ugc.addToBackStack(null);
			ft_ugc.commit();

			break;
		case R.id.ta_haoyou:
			if (mPopupWindow.isShowing()) {
				mPopupWindow.dismiss();
				mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			}
			tv_ta.setVisibility(View.VISIBLE);
			tv_ta.setText(R.string.ta_haoyou);
			SocialUsrFriendsFragment fragment_friends = new SocialUsrFriendsFragment();
			Bundle bundle2 = new Bundle();
			bundle2.putInt("uid", currentUid);
			FragmentTransaction ft_friends = getFragmentManager()
					.beginTransaction();
			ft_friends.remove(fragment_friends);
			fragment_friends.setArguments(bundle2);
			ft_friends.replace(R.id.socialcontainer, fragment_friends);
			// zhanglr delete
			// ft_friends.addToBackStack(null);
			ft_friends.commit();
			break;
		case R.id.ta_shoucang:
			if (mPopupWindow.isShowing()) {
				mPopupWindow.dismiss();
				mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			}
			tv_ta.setVisibility(View.VISIBLE);
			tv_ta.setText(R.string.ta_shoucang);
			SocialSaveFragment fragment_save = new SocialSaveFragment();
			Bundle bundle3 = new Bundle();
			bundle3.putInt("uid", currentUid);
			fragment_save.setArguments(bundle3);
			FragmentTransaction ft_save = getFragmentManager()
					.beginTransaction();
			ft_save.replace(R.id.socialcontainer, fragment_save);
			// zhanglr delete
			// ft_save.addToBackStack(null);
			ft_save.commit();
			break;
		case R.id.iv_lahei:
			handleLahei();
			break;
		case R.id.chk_focus:
			handleFocus();
			break;
		default:
			break;
		}
	}

	private void handleLahei() {
		if(userRelation == 1) {
			new AsyncTask<Void, Void, Integer>() {

				@Override
				protected Integer doInBackground(Void... params) {
					int result = friends_detail.pullOutOfBlackList(currentUid);
					return result;
				}
				protected void onPostExecute(Integer result) {
//					UIUtils.Logi("chen", "取消拉黑结果"+result);
					if(result == -1) {
						UIUtils.ToastMessage(context,R.string.cancel_lahei_chk_net);
					} else if(result == 5) {
						UIUtils.ToastMessage(context, R.string.cancel_lahei_fail);
					} else {
						userRelation = result;
						UIUtils.ToastMessage(context,R.string.cancel_lahei_success);
						initPopup();
						initRelationImages();
					}
				};
			}.execute();
		} else {
			new AsyncTask<Void, Void, Integer>() {

				@Override
				protected Integer doInBackground(Void... params) {
					int result = friends_detail.pullIntoBlackList(currentUid);
					return result;
				}
				protected void onPostExecute(Integer result) {
//					UIUtils.Logi("chen", "拉黑结果"+result);
					if(result == -1) {
						UIUtils.ToastMessage(context, R.string.lahei_chk_net);
					} else if(result == 5) {
						UIUtils.ToastMessage(context, R.string.lahei_fail);
					} else {
						userRelation = result;
						UIUtils.ToastMessage(context, R.string.lahei_success);
						initPopup();
						initRelationImages();
					}
				};
			}.execute();
		}
	}
	
	private void handleFocus() {
		 if(userRelation==0 || userRelation==2) {
			new AsyncTask<Void, Void, Integer>() {
				
				@Override
				protected Integer doInBackground(Void... params) {
					int result = friends_detail.getFollowFriend2(currentUid);
					return result;
				}
				protected void onPostExecute(Integer result) {
//					UIUtils.Logi("chen", "关注好友结果"+result);
					if(result == -1) {
						UIUtils.ToastMessage(getActivity(), R.string.focus_chk_net);
					} else if(result==5) {
						UIUtils.ToastMessage(getActivity(), R.string.focus_faill);
					} else {
						userRelation = result;
						userBean.setRelation(result);
						initRelationImages();
						UIUtils.ToastMessage(getActivity(), R.string.focus_success);
						initPopup();
					}
				};
				
			}.execute();
		} else if(userRelation==3 || userRelation==4) {
			new AsyncTask<Void, Void, Integer>() {
				
				@Override
				protected Integer doInBackground(Void... params) {
					int result = friends_detail.getUnFollowFriend2(currentUid);
					return result;
				}
				protected void onPostExecute(Integer result) {
//					UIUtils.Logi("chen", "取消关注结果"+result);
					if(result == -1) {
						UIUtils.ToastMessage(getActivity(), R.string.cancel_focus_chk_net);
					}
					else if(result==5) {
						UIUtils.ToastMessage(getActivity(), R.string.cancel_focus_fail);
					} else {
						userRelation = result;
						userBean.setRelation(result);
						initRelationImages();
						UIUtils.ToastMessage(getActivity(), R.string.cancel_focus_success);
						initPopup();
					}
				};
				
			}.execute();
		}
	}
}