package com.igrs.tivic.phone.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Activity.SocialBaseActivity;
import com.igrs.tivic.phone.Activity.SocialBaseActivity.ChangAvaterListener2;
import com.igrs.tivic.phone.Animation.TAnimation;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Listener.SocialMenuBarClickListener;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;
import com.igrs.tivic.phone.widget.AsyncImageView;


public class SocialMenuBarFragment extends Fragment implements ChangAvaterListener2{

	private ImageView icon_info;
	private ImageView icon_publish;
	private ImageView icon_save;
	private ImageView icon_friends;
	private ImageView icon_notify;
	private ImageView icon_message;
	private ImageView icon_mkfriend;
	private ImageView icon_setup;
	private AsyncImageView icon_usrImg;
	private TextView text_notify;
	private TextView text_message;	
	private ImageView icon_info_focus;
	private ImageView icon_publish_focus;
	private ImageView icon_save_focus;
	private ImageView icon_friends_focus;
	private ImageView icon_notify_focus;
	private ImageView icon_message_focus;
	private ImageView icon_mkfriend_focus;

	
	Context mContext;
	private String TAG = "SocailMenuBarFragment";
	TivicGlobal tivicGlobal = TivicGlobal.getInstance();
	ViewGroup menubarVG;
	SocialMenuBarClickListener mListener;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		((SocialBaseActivity) getActivity()).setListener2(this);
		super.onCreate(savedInstanceState); 
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		menubarVG = (ViewGroup) inflater.inflate(R.layout.usr_menu_bar, container, false);
		initLayout();
		init();
		return menubarVG;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mContext = getActivity().getApplicationContext();
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
		try {
            mListener = (SocialMenuBarClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement SocialMenuBarClickListener");
        }
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub		
		super.onResume();
//		onAvaterChanged(null);
		Utils.initAvater(icon_usrImg);
		UIUtils.Logi("chen", "menubar init avater");
		initMenuBarCountNew();
//		initArrowPosition(UIUtils.getCurrentFuncID(), true);
	}
	
//	public void onAvaterChanged(Bitmap bm)
//	{
//		if(bm == null)
//			icon_usrImg.setAvaterUrl(UIUtils.getUsrAvatar(TivicGlobal.getInstance().userRegisterBean.getUserId()));
//			Utils.initAvater(icon_usrImg);
//		else
//			icon_usrImg.setImageBitmap(bm);
//	}
	
	private void initMenuBarCountNew()
	{
		setNotifyCount(TivicGlobal.getInstance().notifyCount);
		setMessageCount(TivicGlobal.getInstance().messageCount);
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
		
	public void initLayout() {
		icon_info = (ImageView)menubarVG.findViewById(R.id.icon_info);
		icon_publish = (ImageView)menubarVG.findViewById(R.id.icon_publish);
		icon_save = (ImageView)menubarVG.findViewById(R.id.icon_save);
		icon_friends = (ImageView)menubarVG.findViewById(R.id.icon_friends);
		icon_notify = (ImageView)menubarVG.findViewById(R.id.icon_notify);
		icon_message = (ImageView)menubarVG.findViewById(R.id.icon_message);
		icon_mkfriend = (ImageView)menubarVG.findViewById(R.id.icon_mkfriend);
		text_notify = (TextView)menubarVG.findViewById(R.id.text_remind);
		text_message = (TextView)menubarVG.findViewById(R.id.text_remindmess);
		icon_usrImg = (AsyncImageView)menubarVG.findViewById(R.id.img_usr);
//		icon_usrImg.setAvaterUrl(UIUtils.getUsrAvatar(TivicGlobal.getInstance().userRegisterBean.getUserId()));
//		icon_usrImg.setDefaultImageResource(R.drawable.menu);
		icon_setup = (ImageView)menubarVG.findViewById(R.id.icon_setup);
		
		icon_info_focus = (ImageView)menubarVG.findViewById(R.id.icon_info_focus);
		icon_publish_focus = (ImageView)menubarVG.findViewById(R.id.icon_publish_focus);
		icon_save_focus = (ImageView)menubarVG.findViewById(R.id.icon_save_focus);
		icon_friends_focus = (ImageView)menubarVG.findViewById(R.id.icon_friends_focus);
		icon_notify_focus = (ImageView)menubarVG.findViewById(R.id.icon_notify_focus);
		icon_message_focus = (ImageView)menubarVG.findViewById(R.id.icon_message_focus);
		icon_mkfriend_focus = (ImageView)menubarVG.findViewById(R.id.icon_mkfriend_focus);
		icon_info.setOnClickListener(new OnClickListener() {
	       	@Override
				public void onClick(View v) {		      		
	       			mListener.onItemSelected(FuncID.ID_SOCIAL_INFO);
				}

			});
		icon_publish.setOnClickListener(new OnClickListener() {
	       	@Override
				public void onClick(View v) {		      		
	       			mListener.onItemSelected(FuncID.ID_SOCIAL_PUBLISH);
	       			
				}

			});
		
		icon_save.setOnClickListener(new OnClickListener() {
	       	@Override
				public void onClick(View v) {		      		

	       			mListener.onItemSelected(FuncID.ID_SOCIAL_SAVE);
	       			
				}

			});
		icon_friends.setOnClickListener(new OnClickListener() {
	       	@Override
				public void onClick(View v) {		      		

	       			mListener.onItemSelected(FuncID.ID_SOCIAL_FRIENDS);
	       			
				}

			});
		icon_notify.setOnClickListener(new OnClickListener() {
	       	@Override
				public void onClick(View v) {		      		

	       			mListener.onItemSelected(FuncID.ID_SOCIAL_NOTIFY);
	       			
				}

			});
		icon_message.setOnClickListener(new OnClickListener() {
	       	@Override
				public void onClick(View v) {		      		
	
	       			mListener.onItemSelected(FuncID.ID_SOCIAL_MESSAGE);
	       			
				}

			});
		icon_mkfriend.setOnClickListener(new OnClickListener() {
	       	@Override
				public void onClick(View v) {		      		

	       			if(icon_mkfriend.getTag().equals("enable"))
	       				mListener.onItemSelected(FuncID.ID_SOCIAL_MKFRIEND);
	       			
				}

			});
		icon_setup.setOnClickListener(new OnClickListener() {
	       	@Override
				public void onClick(View v) {		      		

				}

			});
		icon_usrImg.setOnClickListener(new OnClickListener() {
	       	@Override
	       		public void onClick(View v) {		
//	       			UIUtils.Logi("chen", "click avater");
	       			TivicGlobal.getInstance().userRegisterBean.friendUids.clear();
	       			getActivity().finish();
				}

			});
	
	}
	
	public void init() {

		icon_usrImg.setVisibility(View.VISIBLE);
		initFocusIcon(tivicGlobal.mCurrentFuncID);
		if(TivicGlobal.getInstance().currentVisit.getVisit_pid()!=null)
		{
			setMKFriendEnable(true);
		}else{
			setMKFriendEnable(false);
		}
	}
	
	
	public void setNotifyCount(int count)
	{
		if(count > 0)
		{
			text_notify.setText(String.valueOf(count));
			text_notify.setVisibility(View.VISIBLE);
		}else{
			text_notify.setVisibility(View.GONE);
			TivicGlobal.getInstance().notifyCount = 0;
		}
		
	}
	
	public void setMessageCount(int count)
	{
		if(count > 0)
		{
			text_message.setText(String.valueOf(count));
			text_message.setVisibility(View.VISIBLE);
		}else{
			text_message.setVisibility(View.GONE);
			TivicGlobal.getInstance().messageCount = 0;
		}
	}
	
	public void initFocusIcon(int funcid)
	{
		switch(funcid){
		case FuncID.ID_SOCIAL_INFO:
			icon_info_focus.setVisibility(View.VISIBLE);
			icon_publish_focus.setVisibility(View.GONE);
			icon_save_focus.setVisibility(View.GONE);
			icon_friends_focus.setVisibility(View.GONE);
			icon_notify_focus.setVisibility(View.GONE);
			icon_message_focus.setVisibility(View.GONE);
			icon_mkfriend_focus.setVisibility(View.GONE);
			break;
		case FuncID.ID_SOCIAL_SAVE:
			icon_info_focus.setVisibility(View.GONE);
			icon_publish_focus.setVisibility(View.GONE);
			icon_save_focus.setVisibility(View.VISIBLE);
			icon_friends_focus.setVisibility(View.GONE);
			icon_notify_focus.setVisibility(View.GONE);
			icon_message_focus.setVisibility(View.GONE);
			icon_mkfriend_focus.setVisibility(View.GONE);
			break;
		case FuncID.ID_SOCIAL_PUBLISH:
			icon_info_focus.setVisibility(View.GONE);
			icon_publish_focus.setVisibility(View.VISIBLE);
			icon_save_focus.setVisibility(View.GONE);
			icon_friends_focus.setVisibility(View.GONE);
			icon_notify_focus.setVisibility(View.GONE);
			icon_message_focus.setVisibility(View.GONE);
			icon_mkfriend_focus.setVisibility(View.GONE);
			break;
		case FuncID.ID_SOCIAL_FRIENDS:
		case FuncID.ID_SOCIAL_FRIENDS_DETAIL:
			icon_info_focus.setVisibility(View.GONE);
			icon_publish_focus.setVisibility(View.GONE);
			icon_save_focus.setVisibility(View.GONE);
			icon_friends_focus.setVisibility(View.VISIBLE);
			icon_notify_focus.setVisibility(View.GONE);
			icon_message_focus.setVisibility(View.GONE);
			icon_mkfriend_focus.setVisibility(View.GONE);
			break;
		case FuncID.ID_SOCIAL_NOTIFY:
			icon_info_focus.setVisibility(View.GONE);
			icon_publish_focus.setVisibility(View.GONE);
			icon_save_focus.setVisibility(View.GONE);
			icon_friends_focus.setVisibility(View.GONE);
			icon_notify_focus.setVisibility(View.VISIBLE);
			icon_message_focus.setVisibility(View.GONE);
			icon_mkfriend_focus.setVisibility(View.GONE);
			break;
		case FuncID.ID_SOCIAL_MESSAGE:
		case FuncID.ID_SOCIAL_MESSAGE_DETAIL:
			icon_info_focus.setVisibility(View.GONE);
			icon_publish_focus.setVisibility(View.GONE);
			icon_save_focus.setVisibility(View.GONE);
			icon_friends_focus.setVisibility(View.GONE);
			icon_notify_focus.setVisibility(View.GONE);
			icon_message_focus.setVisibility(View.VISIBLE);
			icon_mkfriend_focus.setVisibility(View.GONE);
			break;
		case FuncID.ID_SOCIAL_MKFRIEND:
			icon_info_focus.setVisibility(View.GONE);
			icon_publish_focus.setVisibility(View.GONE);
			icon_save_focus.setVisibility(View.GONE);
			icon_friends_focus.setVisibility(View.GONE);
			icon_notify_focus.setVisibility(View.GONE);
			icon_message_focus.setVisibility(View.GONE);
			icon_mkfriend_focus.setVisibility(View.VISIBLE);
			break;			
			
		}

	}
	
	public void setMKFriendEnable(boolean show)
	{
		if(show)
		{
			icon_mkfriend.setImageResource(R.drawable.base_menu_mkfriend);
			icon_mkfriend.setTag("enable");
		}else{
			icon_mkfriend.setImageResource(R.drawable.base_menu_mkfriend_disable);
			icon_mkfriend.setTag("disable");
		}
	}

	@Override
	public void onAvaterChanged(String userAvaterPath) {
		UIUtils.Logi("chen", "menubar change avater");
		Bitmap bm = BitmapFactory.decodeFile(userAvaterPath);
		icon_usrImg.setImageBitmap(bm);
	}
}
