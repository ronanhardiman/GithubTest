package com.igrs.tivic.phone.Activity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.provider.MediaStore;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Utils.GDUtils;
import com.igrs.tivic.phone.Utils.ModelUtil;
import com.igrs.tivic.phone.Utils.Utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
//import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
//import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Bean.Login.UserRegisterBean;
import com.igrs.tivic.phone.Bean.Social.SocialNewsBean;
import com.igrs.tivic.phone.Fragment.SocialFriendsDetailFragment;
import com.igrs.tivic.phone.Fragment.SocialFriendsFragment;
import com.igrs.tivic.phone.Fragment.SocialMakeFriendFragment;
import com.igrs.tivic.phone.Fragment.SocialMenuBarFragment;
import com.igrs.tivic.phone.Fragment.SocialMessageDetailFragment;
import com.igrs.tivic.phone.Fragment.SocialMessageFragment;
import com.igrs.tivic.phone.Fragment.SocialNotifyFragment;
import com.igrs.tivic.phone.Fragment.SocialSaveFragment;
import com.igrs.tivic.phone.Fragment.SocialUGCFragment;
//import com.igrs.tivic.phone.Fragment.SocialUsrInfoBaseInfoFragment;
import com.igrs.tivic.phone.Fragment.SocialUsrInfoFragment;
import com.igrs.tivic.phone.Fragment.SocialUsrInfoFragment.OnFragmentAvaterLoadListener;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.ShowProgressDialog;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Global.URLConfig;
//import com.igrs.tivic.phone.Global.URLConfig;
import com.igrs.tivic.phone.Impl.LoginImpl;
import com.igrs.tivic.phone.Listener.RefreshDataListener;
import com.igrs.tivic.phone.Listener.SocialMenuBarClickListener;
import com.igrs.tivic.phone.Service.RefreshDataService;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.ViewGroup.BaseTopBar;
import com.igrs.tivic.phone.ViewGroup.SocialUsrInfoBaseFrame;
import com.igrs.tivic.phone.widget.AsyncImageView;
import com.umeng.analytics.MobclickAgent;

public class SocialBaseActivity extends Activity implements
		SocialMenuBarClickListener, RefreshDataListener {
	public BaseTopBar mBaseTopBar;
	public SocialMenuBarFragment mUsrMenuBar;
	public Context mContext,mmContext;
	public TivicGlobal tivicGlobal = TivicGlobal.getInstance();
	private final String TAG = "SocialBaseActivity";
	private ServiceConnection mConnection;
	private boolean mIsBound;
	private RefreshDataService mBoundService;
	private LoginImpl loginImpl = new LoginImpl();
	private final int MSG_GET_COUNT_NEW = 50;
	private final int MSG_NET_DISCONNECT = 205;
	private final int MSG_MODIFY_INFO = 200;
	private Handler updateCountHandler;
	private Handler updateSendMessageSubnailHandler;
	private LinearLayout ll_myinfo, ll_search, ll_exit, ll_setting;
	private PopupWindow pw;
	private Bitmap mBitmap;
	private AsyncImageView mAiv;
	public static File tempFile;
	private UserRegisterBean urb = TivicGlobal.getInstance().userRegisterBean;
	private Display display;
	private SharedPreferences sp;
	private LinearLayout ll_main;
	int currentUid = 0;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 111:
				int result = (Integer) msg.obj;
				ShowProgressDialog.dismiss();
				if (result == 0) {
					urb.setSuccessModifyUserAvater(true);
					mAiv.setImageBitmap(mBitmap);
					if(listener!=null) {
						listener.onAvaterChanged(urb.getUserAvartarPath());
					}
					if(listener2!=null) {
						listener2.onAvaterChanged(urb.getUserAvartarPath());
					}
					Toast.makeText(mContext,
							R.string.social_modify_avater_success,
							Toast.LENGTH_LONG).show();
				} else if (result == 2) {
					Toast.makeText(mContext,
							R.string.social_modify_avater_error,
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(mContext,
							R.string.social_modify_avater_error,
							Toast.LENGTH_LONG).show();
				}
				
				break;

			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		currentUid = getIntent().getIntExtra("uid", 0);
		mContext = this.getApplicationContext();
		mmContext = this;
		sp = getSharedPreferences("config", MODE_PRIVATE);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			tempFile = new File(Environment.getExternalStorageDirectory(),
					getPhotoFileName());
		} else {
			tempFile = new File(getCacheDir(), getPhotoFileName());
		}
		setContentView(R.layout.social_main);
		display = getWindowManager().getDefaultDisplay();
		ll_main = (LinearLayout) findViewById(R.id.ll_main);
		initLayout();
		initPopupWindow();
//		if(getIntent()!=null) {
//		String imagePath = getIntent().getStringExtra("myImagePath");
//		if(!TextUtils.isEmpty(imagePath)) {
//			modifyUserInfo2(BitmapFactory.decodeFile(imagePath));
//		}
//	}
//		bindService();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		bindService();
		init();
		initMKFrientEnable();
		initUserAvater();
		//用于友盟统计
		MobclickAgent.onResume(this);
	}

	private void initUserAvater() {
		final AsyncImageView aiv = (AsyncImageView) mUsrMenuBar.getView().findViewById(R.id.img_usr);
		Utils.initAvater(aiv);
//		String userAvaterPath = urb.getUserAvartarPath();
//		if(urb.isModifyUserAvatar() && urb.isSuccessModifyUserAvater() && !TextUtils.isEmpty(userAvaterPath)) {
//			UIUtils.Logd("chen", "SocialBase userAvaterPath"+userAvaterPath);
//			UIUtils.Logd("chen", "SocialBase from SDCard");
//			Bitmap bm = BitmapFactory.decodeFile(userAvaterPath);
//			aiv.setImageBitmap(bm);
//		} else {
//			if (urb.getUserAvatar() != null
//			&& !TextUtils.isEmpty(urb.getUserAvatar())) {
//		if (urb.getUserAvatar().contains(".jpg")) {// 初始化用户头像
//			UIUtils.Logd("chen", "SocialBase from net");
//			final String imagePath = URLConfig.avarter_path
//					+ urb.getUserAvatar() + "!w128";
//			new AsyncTask<Void, Void, Bitmap>() {
//				@Override
//				protected Bitmap doInBackground(Void... params) {
//					Bitmap bitmap = getImageFromNet(imagePath);
//					return bitmap;
//				}
//				
//				protected void onPostExecute(Bitmap result) {
//					aiv.setImageBitmap(result);
//				};
//			}.execute();
//		}
//	}
//		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		unbindService();
		//用于友盟统计
		MobclickAgent.onPause(this);
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		GDUtils.getGDApplication(mContext).onLowMemory();
		System.gc();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		GDUtils.getGDApplication(mContext).destory();
		System.gc();
	}

	@Override
	public void onItemSelected(int func_id) {
		jumpToFragment(func_id);
		setMenuBarFocus(func_id);
	}

	public void setUpdateSendMessageSubnailHandler(Handler handler) {
		this.updateSendMessageSubnailHandler = handler;
	}

	public void initLayout() {
		mBaseTopBar = (BaseTopBar) findViewById(R.id.base_top_bar);
		mUsrMenuBar = (SocialMenuBarFragment) getFragmentManager()
				.findFragmentById(R.id.social_menu_bar);
		mUsrMenuBar.getView().findViewById(R.id.icon_setup).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(pw!=null && pw.isShowing()) {
//					pw.dismiss();
					dismissPopup();
				} else {
					popup();
				} 
			}
		});
		mBaseTopBar.setParent(this);
		mConnection = new ServiceConnection() {
			public void onServiceConnected(ComponentName className,
					IBinder service) {
				mBoundService = ((RefreshDataService.RefreshDataServiceBinder) service)
						.getService();
				mBoundService.setRefreshDataListener(SocialBaseActivity.this);
				Log.d(TAG, "kevin add: onServiceConnected!");
			}

			public void onServiceDisconnected(ComponentName className) {
				// This is called when the connection with the service has been
				// unexpectedly disconnected -- that is, its process crashed.
				// Because it is running in our same process, we should never
				// see this happen.
				mBoundService = null;
				Log.d(TAG, "kevin add: onServiceDisconnected!");
			}
		};
		updateCountHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.arg1) {
				case MSG_GET_COUNT_NEW: {

					SocialNewsBean bean = (SocialNewsBean) msg.obj;
					int notifynew = bean.getAct_sum();
					int messagenew = bean.getMsg_sum();
					// Log.d(TAG, "kevin add: onDataChanged() notifynew = "
					// + notifynew + ",messagenew = " + messagenew);
					mUsrMenuBar.setNotifyCount(notifynew);
					mUsrMenuBar.setMessageCount(messagenew);

				}

				break;
				case MSG_NET_DISCONNECT:
					UIUtils.ToastMessage(mContext, getString(R.string.net_error));
					break;
				}

			}
		};
	}

	// 当后台Service获取新通知，需要刷新界面时，会回调该方法

	@Override
	public void onDataChanged(SocialNewsBean countBean) {

		Message msg = updateCountHandler.obtainMessage();
		msg.arg1 = MSG_GET_COUNT_NEW;
		msg.obj = countBean;
		updateCountHandler.sendMessage(msg);
	}

	@Override 
	public void onNetworkDisconnect()
	{
		Message msg = updateCountHandler.obtainMessage();
		msg.arg1 = MSG_NET_DISCONNECT;
		updateCountHandler.sendMessage(msg);
	}
	
	public void bindService() {
		if(!TivicGlobal.getInstance().mIsLogin)
			return;
		bindService(new Intent(SocialBaseActivity.this,
				RefreshDataService.class), mConnection,
				Context.BIND_AUTO_CREATE);
		mIsBound = true;
	}

	public void unbindService() {
		if(!TivicGlobal.getInstance().mIsLogin)
			return;
		if (mIsBound && mConnection != null) {
			// Detach our existing connection.
			unbindService(mConnection);
			mIsBound = false;
		}
	}

	public void init() {
		mBaseTopBar.init();
		mUsrMenuBar.init();
		jumpToFragment(TivicGlobal.getInstance().mCurrentFuncID);
		mBaseTopBar.setSocialTitle();
	}

	public void jumpToFragment(int func_id) {
		mUsrMenuBar.initFocusIcon(func_id);
		switch (func_id) {
		case FuncID.ID_SOCIAL_INFO:
			if(tivicGlobal.socialInfoState != FuncID.ID_SOCIAL_INFO_PHOTO)
				clickUsrInfo();
			break;
		case FuncID.ID_SOCIAL_PUBLISH:
			clickUGC();
			break;
		case FuncID.ID_SOCIAL_SAVE:
			clickSave();
			break;
		case FuncID.ID_SOCIAL_FRIENDS:
			clickFriends();
			break;
		case FuncID.ID_SOCIAL_FRIENDS_DETAIL:
			clickFriendsDetail();
			break;
		case FuncID.ID_SOCIAL_NOTIFY:
			clickNotify();
			break;
		case FuncID.ID_SOCIAL_MESSAGE:
			clickMessage();
			break;
		// case FuncID.ID_SOCIAL_MESSAGE_DETAIL:
		// clickMessageDetail();
		// break;
		case FuncID.ID_SOCIAL_MKFRIEND:
			clickMkFriend();
			break;
		}

	}

	public void clickUsrInfo() {
		clearFriendUids();
		UIUtils.setCurrentFuncID(FuncID.ID_SOCIAL_INFO);
		SocialUsrInfoFragment fragment = new SocialUsrInfoFragment();
		openOrReplaceMainFragment(fragment);

	}

	public void clickUGC() {
		clearFriendUids();
		UIUtils.setCurrentFuncID(FuncID.ID_SOCIAL_PUBLISH);
		SocialUGCFragment fragment = new SocialUGCFragment();
		openOrReplaceMainFragment(fragment);

	}

	public void clickSave() {
		clearFriendUids();
		UIUtils.setCurrentFuncID(FuncID.ID_SOCIAL_SAVE);
		SocialSaveFragment fragment = new SocialSaveFragment();
		openOrReplaceMainFragment(fragment);

	}

	public void clickFriends() {
		clearFriendUids();
		UIUtils.setCurrentFuncID(FuncID.ID_SOCIAL_FRIENDS);
		SocialFriendsFragment fragment = new SocialFriendsFragment();
		openOrReplaceMainFragment(fragment);

	}

	public void clickFriendsDetail() {

		UIUtils.setCurrentFuncID(FuncID.ID_SOCIAL_FRIENDS_DETAIL);
		SocialFriendsDetailFragment fragment = new SocialFriendsDetailFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("uid", currentUid);
		fragment.setArguments(bundle);
		openOrReplaceMainFragment(fragment);

	}

	public void clickNotify() {
		clearFriendUids();
		UIUtils.setCurrentFuncID(FuncID.ID_SOCIAL_NOTIFY);
		SocialNotifyFragment fragment = new SocialNotifyFragment();
		openOrReplaceMainFragment(fragment);

	}

	public void clickMessage() {
		clearFriendUids();
		UIUtils.setCurrentFuncID(FuncID.ID_SOCIAL_MESSAGE);
		SocialMessageFragment fragment = new SocialMessageFragment();
		openOrReplaceMainFragment(fragment);

	}

	public void clickMessageDetail(int uid) {

		UIUtils.setCurrentFuncID(FuncID.ID_SOCIAL_MESSAGE_DETAIL);
		SocialMessageDetailFragment fragment = new SocialMessageDetailFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("uid", uid);
		fragment.setArguments(bundle);
		openOrReplaceMainFragmentWithBackStack(fragment);

	}

	public void clickMkFriend() {
		clearFriendUids();
		UIUtils.setCurrentFuncID(FuncID.ID_SOCIAL_MKFRIEND);
		SocialMakeFriendFragment fragment = new SocialMakeFriendFragment();
		openOrReplaceMainFragment(fragment);

	}
	//add by chen
	public void clearFriendUids() {//清除保存的uid
		urb.friendUids.clear();
	}
	public void openOrReplaceMainFragment(Fragment fragment) {

		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.setCustomAnimations(R.anim.fragment_slide_left_enter,
				R.anim.fragment_slide_left_exit,
				R.anim.fragment_slide_right_enter,
				R.anim.fragment_slide_right_exit);

		ft.replace(R.id.social_empty, fragment);
		ft.commit();
	}

	public void openOrReplaceMainFragmentWithBackStack(Fragment fragment) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.setCustomAnimations(R.anim.fragment_slide_left_enter,
				R.anim.fragment_slide_left_exit,
				R.anim.fragment_slide_right_enter,
				R.anim.fragment_slide_right_exit);
		ft.replace(R.id.social_empty, fragment);
		ft.addToBackStack(null);

		ft.commit();
	}

	public void setMenuBarFocus(int funid) {
		mUsrMenuBar.initFocusIcon(funid);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu");
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
			popup();
		return false;
	}
	private void initPopupWindow() {
		View view = View.inflate(mContext, R.layout.menu_popup, null);
		ll_myinfo = (LinearLayout) view.findViewById(R.id.ll_myinfo);
		ll_exit = (LinearLayout) view.findViewById(R.id.ll_exit);
		ll_search = (LinearLayout) view.findViewById(R.id.ll_search);
		ll_setting = (LinearLayout) view.findViewById(R.id.ll_setting);
		ll_myinfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TivicGlobal.getInstance().mIsLogin) {
					pw.dismiss();
					Intent intent = new Intent(SocialBaseActivity.this,
							LoginRegistrationBaseInfoActivity.class);
					intent.putExtra("fromClass", getClass().getName());
					startActivity(intent);
				} else {
					Toast.makeText(mContext, R.string.menu_login_first,
							Toast.LENGTH_SHORT).show();

				}
			}
		});
		ll_exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pw.dismiss();
				Intent exitIntent = new Intent(SocialBaseActivity.this, SettingActivity.class);
				exitIntent.putExtra("exit", true);
				exitIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				exitIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(exitIntent);

			}
		});
		ll_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pw.dismiss();
//				Intent searchIntent = new Intent(SocialBaseActivity.this, SearchActivity.class);
//				startActivity(searchIntent);
				logout();
			}
		});
		ll_setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pw.dismiss();
				Intent settingIntent = new Intent(SocialBaseActivity.this, SettingActivity.class);
				startActivity(settingIntent);

			}
		});
		view.setFocusableInTouchMode(true);  
		view.setOnKeyListener(new OnKeyListener() {  
		    @Override  
		    public boolean onKey(View v, int keyCode, KeyEvent event) {  
		        // TODO Auto-generated method stub  
		        if ((keyCode == KeyEvent.KEYCODE_MENU)&&(pw.isShowing())) {  
		            pw.dismiss();// 这里写明模拟menu的PopupWindow退出就行  
		            return true;  
		        }  
		        return false;  
		    }  
		});  
		pw = new PopupWindow(view, display.getWidth(), display.getHeight() / 3);
		pw.setFocusable(true);
		pw.setBackgroundDrawable(new BitmapDrawable());

	}
	public void logout() {
		if(!TivicGlobal.getInstance().mIsLogin) {
			Toast.makeText(mContext, R.string.setting_login_first, Toast.LENGTH_SHORT).show();
		} else {
//			urb.setUserSID(null);
			TivicGlobal.getInstance().mIsLogin = false;
//			TivicGlobal.getInstance().userRegisterBean.setSuccessModifyUserAvater(false);
			TivicGlobal.getInstance().userRegisterBean = new UserRegisterBean();
			sp.edit().putBoolean("autoLogin", false).commit();
			Toast.makeText(mContext, R.string.setting_logout_success, Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(mContext,LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		// 社交7个功能主页，返回直接退出社交
		TivicGlobal.getInstance().userRegisterBean.isNeedsUid = true;
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (pw!=null && pw.isShowing()) {
				dismissPopup();
				return true;
			}
			if (UIUtils.getCurrentFuncID() >= FuncID.ID_SOCIAL_INFO
					&& UIUtils.getCurrentFuncID() <= FuncID.ID_SOCIAL_MKFRIEND){
				this.finish();
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	public void dismissPopup() {
		if (pw != null) {
			pw.dismiss();
		}
	}
	public void popup() {
		if (pw != null) {
			pw.showAtLocation(ll_main, Gravity.TOP | Gravity.LEFT, 0,
					display.getHeight() - pw.getHeight());
		}
	}

	

	private String theThumbnail;
	private String imgPath;
	private static final int REQUESTCODE_PHOTOS = 200;// 请求相册
	private static final int REQUESTCODE_CAMERA = 201;// 请求照相机
	private static final int REQUESTCODE_SNAPSHOT = 203;// 请求截屏
	private static final int MSG_REQUESTIMG = 204;// 请求截屏
	private static final int MSG_REQUESTIMGPATH = 205;// 请求图片路径

	public void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		final int requestCode = _requestCode;
		final int resultCode = _resultCode;
		final Intent data = _data;
		
		if (resultCode != Activity.RESULT_OK)
			return;

		new Thread() {
			public void run() {
				Bitmap bitmap = null;
				if (requestCode == REQUESTCODE_PHOTOS) {
					if (data == null)
						return;
					
					Uri thisUri = data.getData();
					String thePath = Utils
							.getAbsolutePathFromNoStandardUri(thisUri);
					if (ModelUtil.isEmpty(thePath)) {
						imgPath = Utils.getAbsoluteImagePath(
								SocialBaseActivity.this, thisUri);
					} else {
						imgPath = thePath;
					}
					// 获取图片缩略图 只有Android2.1以上版本支持
					String imageName = Utils.getFileName(imgPath);
					bitmap = Utils.loadImgThumbnail(SocialBaseActivity.this,
							imageName, MediaStore.Images.Thumbnails.MICRO_KIND);

				} else if (requestCode == REQUESTCODE_CAMERA) {// 拍摄图片
					imgPath = Const.PHOTO_DIR + Const.PHOTO_FILE;

				} else if (requestCode == REQUESTCODE_SNAPSHOT) { // 截屏
					bitmap = null;
					imgPath = data.getStringExtra("image_path");				
				}
				
				
				if (bitmap == null && !ModelUtil.isEmpty(imgPath)) {
					bitmap = Utils.loadImgThumbnail(imgPath, 100, 100);
				}
				if (bitmap != null) {
					String savePath = Const.PHOTO_DIR;
					File savedir = new File(savePath);
					if (!savedir.exists()) {
						savedir.mkdirs();
					}

					String largeFileName = Utils.getFileName(imgPath);
					String largeFilePath = savePath + largeFileName;
					// 判断是否已存在缩略图
					if (largeFileName.startsWith("thumb_")
							&& new File(largeFilePath).exists()) {
						theThumbnail = largeFilePath;
						// imgFile = new File(theThumbnail);
					} else {
						// 生成上传的800宽度图片
						String thumbFileName = "thumb_" + largeFileName;
						theThumbnail = savePath + thumbFileName;
						if (new File(theThumbnail).exists()) {
							// imgFile = new File(theThumbnail);
						} else {
							// 压缩上传的图片
							try {
								Utils.createImageThumbnail(mContext, imgPath,
										Const.PHOTO_DIR + "thumb_" + Const.PHOTO_FILE,
										800, 80);
								// imgFile = new File(theThumbnail);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
//回传Bitmap显示缩略图
					Message msg = new Message();
					msg.what = MSG_REQUESTIMG;
					msg.obj = bitmap;
					updateSendMessageSubnailHandler.sendMessage(msg);
//回传图片路径，发私信用
					Message msg2 = new Message();
					msg2.what = MSG_REQUESTIMGPATH;
					msg2.obj = imgPath;
					updateSendMessageSubnailHandler.sendMessage(msg2);

			};
		}
			}.start();
		switch (requestCode) {
		
		case SocialUsrInfoFragment.FROMLOCALPHOTO:
			if (data != null) {
				startPhotoZoom(data.getData(), 150);
			}
			break;
		case SocialUsrInfoFragment.CUR:
			if (data != null) {
				setPicToView(data);
			}
			break;
		case SocialUsrInfoFragment.CAPTURE:
			startPhotoZoom(Uri.fromFile(tempFile), 150);
			break;
		case SocialUsrInfoFragment.SYSTEMPHOTO:
			UIUtils.Logi("chen","baseActivityOnActivityResult -----------------------");
			String imagePath = data.getStringExtra("imagePath");
			mBitmap = BitmapFactory.decodeFile(imagePath);
			SocialUsrInfoFragment sif = (SocialUsrInfoFragment) getFragmentManager().findFragmentById(R.id.social_empty);
			SocialUsrInfoBaseFrame sbf = (SocialUsrInfoBaseFrame) sif.getView().findViewById(R.id.base_info);
			mAiv = (AsyncImageView) sbf.findViewById(R.id.social_userpic);
//			modifyUserInfo(bitmap, aiv);
			modifyUserInfo2();
			break;
		default:
			break;
		}
	}

	public void initMKFrientEnable()
	{
		if(TivicGlobal.getInstance().currentVisit.getVisit_pid() == null || TivicGlobal.getInstance().currentVisit.getVisit_pid().isEmpty())
		{
			mUsrMenuBar.setMKFriendEnable(false);
		}else{
			mUsrMenuBar.setMKFriendEnable(true);
		}
	}
	private ChangAvaterListener listener;
	
	public ChangAvaterListener getListener() {
		return listener;
	}

	public void setListener(ChangAvaterListener listener) {
		this.listener = listener;
	}
	public interface ChangAvaterListener{
		void onAvaterChanged(String userAvaterPath);
	}
	private ChangAvaterListener2 listener2;
	public ChangAvaterListener2 getListener2() {
		return listener2;
	}

	public void setListener2(ChangAvaterListener2 listener2) {
		this.listener2 = listener2;
	}
	public interface ChangAvaterListener2{
		void onAvaterChanged(String userAvaterPath);
	}
//	private void modifyUserInfo(final Bitmap bitmap,final AsyncImageView aiv) {
//	new AsyncTask<Void, Void, Integer>() {
//		protected void onPreExecute() {
//			pd = ProgressDialog.show(mmContext, "", getResources().getString(R.string.login_registed_handle));
//			pd.setCancelable(true);
//		};
//		@Override
//		protected Integer doInBackground(Void... params) {
//			int result = loginImpl.modifyUserBasicInfo3(urb);
//			
//			return result;
//		}
//		protected void onPostExecute(Integer result) {
//			if(pd!=null && pd.isShowing()) {
//				pd.dismiss();
//			}
//			if (result == 0) {
//				urb.setSuccessModifyUserAvater(true);
//				aiv.setImageBitmap(bitmap);
//				if(listener!=null) {
//					listener.onAvaterChanged(urb.getUserAvartarPath());
//				}
//				Toast.makeText(mContext,
//						R.string.social_modify_avater_success,
//						Toast.LENGTH_LONG).show();
//			} else if (result == 2) {
//				Toast.makeText(mContext,
//						R.string.social_modify_avater_error,
//						Toast.LENGTH_LONG).show();
//			} else {
//				Toast.makeText(mContext,
//						R.string.social_modify_avater_error,
//						Toast.LENGTH_LONG).show();
//			}
//		};
//
//	}.execute();
//}
	
	
	private void modifyUserInfo2() {
		ShowProgressDialog.show2(mmContext,R.string.login_registed_handle, null);
//				urb.setSuccessModifyUserAvater(false);
				new Thread() {
					public void run() {
						int result = loginImpl.modifyUserBasicInfo3(urb);
						Message msg = mHandler.obtainMessage();
						msg.what = 111;
						msg.obj = result;
						mHandler.sendMessageDelayed(msg, 1500);
					};
				}.start();	
				
				
				

		}
	private void setPicToView(Intent picdata) {// 将进行剪裁后的图片显示到UI界面上并保持在本地
		Bundle bundle = picdata.getExtras();
		if (bundle != null) {
			mBitmap = bundle.getParcelable("data");
			String path = savePhoto(mBitmap);
//			 System.out.println("保存路径"+path);
			SocialUsrInfoFragment sif = (SocialUsrInfoFragment) getFragmentManager().findFragmentById(R.id.social_empty);
			SocialUsrInfoBaseFrame sbf = (SocialUsrInfoBaseFrame) sif.getView().findViewById(R.id.base_info);
			mAiv = (AsyncImageView) sbf.findViewById(R.id.social_userpic);
//			modifyUserInfo(bitmap, aiv);
			modifyUserInfo2();
		}
	}
	private void startPhotoZoom(Uri uri, int size) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");

		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", size);
		intent.putExtra("outputY", size);
		intent.putExtra("return-data", true);
		urb.setSuccessModifyUserAvater(false);
		startActivityForResult(intent, SocialUsrInfoFragment.CUR);
	}
	private String savePhoto(Bitmap photo) {
		String photoDir = "";
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] byteArray = baos.toByteArray();
			String saveDir = null;
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				saveDir = Environment.getExternalStorageDirectory()
						+ "/uploadphoto";
			} else {
				saveDir = getCacheDir() + "/uploadphoto";
			}
			File dir = new File(saveDir);
			if (!dir.exists()) {
				dir.mkdir();
			}
			String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
					.format(new Date());
//			String fileName = "photo_" + timeStamp + ".jpg";// 照片命名
			String fileName = "avater_" + urb.getUserUID() + "_photo.jpg";
			File file = new File(saveDir, fileName);
			file.delete();
			if (!file.exists()) {
				file.createNewFile();
			}
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(byteArray);
			photoDir = file.getPath();
			// System.out.println("phtotoDir="+photoDir);
			if (!TextUtils.isEmpty(photoDir)) {
//				Utils.deleteFile(getCacheDir());
				urb.setUserAvartarPath(photoDir);
				urb.setModifyUserAvatar(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return photoDir;
	}
	private String getPhotoFileName() { // 使用系统当前日期加以调整作为照片的名称
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}
	public Bitmap getImageFromNet(String path) {
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(6 * 1000);
			conn.setRequestMethod("GET");
			conn.addRequestProperty("Cache-Control", "no-cache");
			if (conn.getResponseCode() == 200) {
				InputStream is = conn.getInputStream();
				Bitmap bitmap = BitmapFactory.decodeStream(is);
				return bitmap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

//	@Override
//	public void onFragmentAvaterLoad(Bitmap bm) {
//		mUsrMenuBar.onAvaterChanged(bm);
//	}
	
	public void hideNotifyIcon()
	{
		mUsrMenuBar.setNotifyCount(0);
	}
	
	public void setMessageCount(int count)
	{
		//将已读消息从提示的总数中去掉
		int newcount = TivicGlobal.getInstance().messageCount - count;
		if(newcount < 0)
			newcount = 0;
		mUsrMenuBar.setMessageCount(newcount);
	}
}
