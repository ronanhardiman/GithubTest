package com.igrs.tivic.phone.ViewGroup;

//import static android.util.Log.e;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Activity.BaseActivity;
import com.igrs.tivic.phone.Activity.LoginActivity;
import com.igrs.tivic.phone.Bean.Login.UserRegisterBean;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Utils.UIUtils;


public class UsrMenuBar extends LinearLayout{

	private ImageView icon_info;
	private ImageView icon_publish;
	private ImageView icon_save;
	private ImageView icon_friends;
	private ImageView icon_notify;
	private ImageView icon_message;
	private ImageView icon_mkfriend;
	private ImageView icon_setup;
	private SharedPreferences sp;
	private Editor editor;
	private TextView text_notify;
	private TextView text_message;
	Context mContext;
	BaseActivity parentActivity;
	private String TAG = "UsrMenuBar";
	TivicGlobal tivicGlobal = TivicGlobal.getInstance();
	private UserRegisterBean urb = TivicGlobal.getInstance().userRegisterBean;
	public UsrMenuBar(Context context) {
		this(context, null);
	}

	public UsrMenuBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context.getApplicationContext();
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		editor = sp.edit();
		LayoutInflater.from(context).inflate(R.layout.usr_menu_bar, this, true);
		initLayout();

	}

	public void initLayout() {
		icon_info = (ImageView) findViewById(R.id.icon_info);
		icon_publish = (ImageView) findViewById(R.id.icon_publish);
		icon_save = (ImageView) findViewById(R.id.icon_save);
		icon_friends = (ImageView) findViewById(R.id.icon_friends);
		icon_notify = (ImageView) findViewById(R.id.icon_notify);
		icon_message = (ImageView) findViewById(R.id.icon_message);
		icon_mkfriend = (ImageView) findViewById(R.id.icon_mkfriend);
		icon_setup = (ImageView) findViewById(R.id.icon_setup);	

		text_notify = (TextView) findViewById(R.id.text_remind);
		text_message = (TextView) findViewById(R.id.text_remindmess);
		
		icon_info.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO
				clickIcon(FuncID.ID_SOCIAL_INFO);
				int width = UIUtils.px2dip(mContext, 156);
				int height = UIUtils.px2dip(mContext, 100);
				int y = UIUtils.px2dip(mContext, 1024);
				int x = UIUtils.px2dip(mContext, 768);
//				Log.i(TAG, "kevin add: menu bar width = " + width + " top bar height = " + height + "(" + x + "," + y + ")");
			}

		});
		icon_publish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO
				clickIcon(FuncID.ID_SOCIAL_PUBLISH);
			}

		});
		icon_save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO
				clickIcon(FuncID.ID_SOCIAL_SAVE);
			}

		});
		icon_friends.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO
				clickIcon(FuncID.ID_SOCIAL_FRIENDS);
			}

		});
		icon_notify.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO
				setNotifyCount(0);
				clickIcon(FuncID.ID_SOCIAL_NOTIFY);
			}

		});
		icon_message.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO
       			//取消统一关闭提示，改为进入聊天详情用op置为已读，提示数据后台自动处理s
				//setMessageCount(0);
				clickIcon(FuncID.ID_SOCIAL_MESSAGE);
			}

		});
		icon_mkfriend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO
				if(!((String)icon_mkfriend.getTag()).equals("disable"))
					clickIcon(FuncID.ID_SOCIAL_MKFRIEND);
			}

		});
		icon_setup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO
				parentActivity.openMenu();
//				clickIcon(FuncID.ID_LOGIN);
//				logout();
				
			}

		});
	}
	public void logout() {
		if(!TivicGlobal.getInstance().mIsLogin) {
			Toast.makeText(mContext, R.string.setting_login_first, Toast.LENGTH_SHORT).show();
		} else {
			urb.setUserSID(null);
			TivicGlobal.getInstance().mIsLogin = false;
			editor.putBoolean("autoLogin", false);
			editor.commit();
			TivicGlobal.getInstance().userRegisterBean = new UserRegisterBean();
			Toast.makeText(mContext, R.string.setting_logout_success, Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(mContext, LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(intent);
		}
	}
	
	public void showUsrIcon(boolean isShow)
	{
		ImageView icon_photo = (ImageView) findViewById(R.id.img_usr);
		icon_photo.setVisibility(View.VISIBLE);
	}
	
	public void setParent(BaseActivity activity)
	{
		this.parentActivity = activity;
	}
	
	public void clickIcon(int func_id)
	{
//		Log.i(TAG, "kevin add: onClick");
		switch(func_id){
			case FuncID.ID_SOCIAL_INFO:
			case FuncID.ID_SOCIAL_PUBLISH:
			case FuncID.ID_SOCIAL_SAVE:
			case FuncID.ID_SOCIAL_FRIENDS:
			case FuncID.ID_SOCIAL_NOTIFY:
			case FuncID.ID_SOCIAL_MESSAGE:				
			case FuncID.ID_SOCIAL_MKFRIEND:	
				try{
					Intent intent = new Intent();
					intent.setClassName("com.igrs.tivic.phone",
							"com.igrs.tivic.phone.Activity.SocialBaseActivity");
					parentActivity.startActivity(intent);
					UIUtils.setCurrentFuncID(func_id);
//					Log.i(TAG, "kevin add: jiump to LoginActivity");
					}catch (Exception e) {    
			              Log.e(TAG, "startActivity error! " + e.toString());
			        }
			break;
			case FuncID.ID_LOGIN:
				try{
					Intent intent = new Intent();
					intent.setClassName("com.igrs.tivic.phone",
							"com.igrs.tivic.phone.Activity.LoginActivity");
					parentActivity.startActivity(intent);
					UIUtils.setCurrentFuncID(FuncID.ID_LOGIN);
//					Log.i(TAG, "kevin add: jiump to LoginActivity");
					}catch (Exception e) {    
			              Log.e(TAG, "startActivity error! " + e.toString());
			        }
				break;
		}
	}
	
	public void setNotifyCount(int count)
	{
		if(count > 0)
		{
			text_notify.setText(String.valueOf(count));
			text_notify.setVisibility(View.VISIBLE);
		}else{
			TivicGlobal.getInstance().notifyCount = 0;
			text_notify.setVisibility(View.GONE);
		}
		
	}
	
	public void setMessageCount(int count)
	{
		if(count > 0)
		{
			text_message.setText(String.valueOf(count));
			text_message.setVisibility(View.VISIBLE);
		}else{
			TivicGlobal.getInstance().messageCount = 0;
			text_message.setVisibility(View.GONE);
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

}
