package com.igrs.tivic.phone.Activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Adapter.ScreenChannelApdater;
import com.igrs.tivic.phone.Adapter.ScreenShotAdapter;
import com.igrs.tivic.phone.Bean.VisitBean;
import com.igrs.tivic.phone.Bean.EPG.EPGChannelListBean;
import com.igrs.tivic.phone.Bean.EPG.EPGChannelsBean;
import com.igrs.tivic.phone.Bean.Login.UserRegisterBean;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.ShowProgressDialog;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Impl.ContentImpl;
import com.igrs.tivic.phone.Impl.EPGImpl;
import com.igrs.tivic.phone.Utils.GDUtils;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;
import com.igrs.tivic.phone.ViewGroup.ScrollForeverTextView;
import com.igrs.tivic.phone.widget.AsyncImageView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ScreenShotActivity extends Activity implements OnClickListener {
	private ImageView iv_back, iv_channel, iv_complete, iv_save_screen_shot,
			iv_anew_screen_shot,iv_down;
	private ScrollForeverTextView tv_channel_name;
	private ImageView iv_left,iv_right;
	private LinearLayout ll_channel_info;
	private Gallery gv_screen_shot;
	private EPGImpl epgImpl = EPGImpl.getInstance();
	private ContentImpl contentImpl = ContentImpl.getInstance();
	private VisitBean currentVisitBean = TivicGlobal.getInstance().currentVisit;
	private EPGChannelListBean channelListBean;
	private String[] images;
	private List<EPGChannelsBean> channels;
	private ScreenShotAdapter adapter;
	private ScreenChannelApdater channelApdater;
	private Context mContext;
	private PopupWindow popupWindow;
	private ListView lv;
//	private float density = getResources().getDisplayMetrics().density;
	private String currentChannelId;
	private UserRegisterBean urb = TivicGlobal.getInstance().userRegisterBean;
	private static final int REQUESTCODE_SNAPSHOT = 203;// 请求截屏
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.epg_screen_shot);
		mContext = this;
		init();
		setListeners();
		initData();
		
	}

	

	private void initData() {
		new AsyncTask<Void, Void, String[]>() {
			protected void onPreExecute() {
				ShowProgressDialog.show2(mContext,R.string.login_regsited_interest_load_data, null);
			};
			@Override
			protected String[] doInBackground(Void... params) {
				if(currentVisitBean!=null && !TextUtils.isEmpty(currentVisitBean.getVisit_channelid())) {//初始化当前的channelId
					currentChannelId = currentVisitBean.getVisit_channelid();
				} 
				channelListBean = epgImpl.getChannelList();
				if(channelListBean == null) {
					return null;
				}
				channels = channelListBean.getEpgChannelsBeanList();
				if(currentVisitBean!=null && !TextUtils.isEmpty(currentVisitBean.getVisit_channelid())) {//初始化当前的channelId
					currentChannelId = currentVisitBean.getVisit_channelid();
				} else {
					currentChannelId = channels.get(0).getChannel_id();
				}
				images = contentImpl.getScreenShotImagesList(Integer.parseInt(currentChannelId));
				return images;
			}
			protected void onPostExecute(String[] result) {
				ShowProgressDialog.dismiss();
				if(result == null) {
					UIUtils.ToastMessage(mContext, R.string.net_error);
					return;
				}
				adapter = new ScreenShotAdapter(mContext, images);
				gv_screen_shot.setAdapter(adapter);
				gv_screen_shot.setSelection(0);
				initPopupWindow(channels);
				initChannelNameAndIcon();
				gv_screen_shot.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
//						UIUtils.Logd("count",gv_screen_shot.getChildCount()+"");
//						for(int i=0;i<gv_screen_shot.getChildCount();i++) {
//							View item = gv_screen_shot.getChildAt(i);
//							UIUtils.Logd("chen", "item==null?"+(item==null));
//							ImageView iv_selected = (ImageView) item.findViewById(R.id.iv_selected);
//							if(iv_selected.getVisibility()==View.VISIBLE) {
//								iv_selected.setVisibility(View.INVISIBLE);
//							}
//						}
//						view.findViewById(R.id.iv_selected).setVisibility(View.VISIBLE);
						String str1 = getResources().getString(R.string.you_choose);
						String str2 = getResources().getString(R.string.pic);
						Toast.makeText(mContext, str1+(position+1)+str2, Toast.LENGTH_SHORT).show();
						gv_screen_shot.setSelection(position);
					}
				});
			};
			
		}.execute();
	}
	public void initPopupWindow(List<EPGChannelsBean> channelsBeans) {
//		View view  = View.inflate(mContext, R.layout.screen_shot_listview, null);
		lv = new ListView(mContext);
		lv.setPadding(10,30,10,30);
//		lv.setDividerHeight(30);
//		lv = (ListView) view.findViewById(R.id.lv_datas);
		if(channelsBeans != null) {
			channelApdater = new ScreenChannelApdater(mContext, channelsBeans);
			lv.setAdapter(channelApdater);
		}
		lv.setCacheColorHint(0x000000);
		lv.setBackgroundDrawable(new ColorDrawable(0xEEEEEE));
//		lv.setBackgroundColor(0xEEEEEE);
		lv.setDivider(getResources().getDrawable(R.drawable.public_jieping_pindaotanchuang_fengexian));
		lv.setBackgroundResource(R.drawable.public_jieping_pindaotanchuang_bg);
        lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				if(popupWindow!=null && popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
				currentChannelId = channels.get(position).getChannel_id();
				initChannelNameAndIcon();
				anewScreenShot();
			}
		});
        
        
		int width = getWindowManager().getDefaultDisplay().getWidth()/2;
		int height = getWindowManager().getDefaultDisplay().getHeight()/2;
		popupWindow = new PopupWindow(lv, width, height);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.public_jieping_pindaotanchuang_bg));
//		popupWindow.setBackgroundDrawable(new ColorDrawable());
	}
	private void initChannelNameAndIcon() {
		
			for(EPGChannelsBean channel : channels) {
				if(channel.getChannel_id().equals(currentChannelId)) {
					String channelName = channel.getChannel_name();
					tv_channel_name.setText(channelName);
					iv_channel.setImageResource(Utils.setChannelIcon(mContext, channelName));
				}
			}
	}

	private void init() {
		iv_left = (ImageView) findViewById(R.id.iv_left);
		iv_right = (ImageView) findViewById(R.id.iv_right);
		int width = TivicGlobal.getInstance().displayWidth;
		iv_left.getLayoutParams().width = (int) ((width-370)/2);
		iv_right.getLayoutParams().width = (int) ((width-370)/2);
		iv_anew_screen_shot = (ImageView) findViewById(R.id.iv_anew_screen_shot);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_channel = (ImageView) findViewById(R.id.iv_channel);
		iv_complete = (ImageView) findViewById(R.id.iv_complete);
		iv_save_screen_shot = (ImageView) findViewById(R.id.iv_save_screen_shot);
		iv_down = (ImageView) findViewById(R.id.iv_down);
		ll_channel_info = (LinearLayout) findViewById(R.id.ll_channel_info);
		
		tv_channel_name = (ScrollForeverTextView) findViewById(R.id.tv_channel_name);
		gv_screen_shot = (Gallery) findViewById(R.id.gv_screen_shot);
		gv_screen_shot.setUnselectedAlpha(1.1f);
		gv_screen_shot.setSelection(0);
//		gv_screen_shot.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view,
//					int position, long id) {
//				Toast.makeText(mContext, "您选择了第"+(position+1)+"张图片", Toast.LENGTH_SHORT).show();
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//				
//			}
//		});
	}

	private void setListeners() {
		iv_anew_screen_shot.setOnClickListener(this);
		iv_complete.setOnClickListener(this);
		iv_save_screen_shot.setOnClickListener(this);
		iv_back.setOnClickListener(this);
		iv_down.setOnClickListener(this);
		ll_channel_info.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_anew_screen_shot:
			anewScreenShot();
			break;
		case R.id.iv_save_screen_shot:
			saveImage();
			break;
		case R.id.iv_down:
			if(popupWindow!=null) {
				popupWindow.showAsDropDown(iv_down, -150, 0);
			}
			break;
		case R.id.ll_channel_info:
			if(popupWindow!=null) {
				popupWindow.showAsDropDown(iv_down, -150, 0);
			}
			break;
		case R.id.iv_back:
			finish();
			break;
		case R.id.iv_complete:
			saveScreenShot();
			//liq add 返回发评论
//			System.out.println("gv_screen_shot.getSelectedItemPosition()="+gv_screen_shot.getSelectedItemPosition());
//			Intent image_intent = new Intent(this , ContentPublishActivity.class);
//			image_intent.putExtra("image_url", images[gv_screen_shot.getSelectedItemPosition()]);
//			setResult(Activity.RESULT_OK, image_intent);
			//zhanglr add 返回发私信
//			Intent message_intent = new Intent(this , SocialBaseActivity.class);
//			message_intent.putExtra("image_url", images[gv_screen_shot.getSelectedItemPosition()]);
//			setResult(Activity.RESULT_OK, message_intent);
//			finish();
			break;
		
		default:
			break;
		}
	}


	private void saveScreenShot() {
		if(images == null) {
			UIUtils.ToastMessage(mContext, R.string.net_error);
			return;
		}
		final String imageUrl = images[gv_screen_shot.getSelectedItemPosition()];
		new AsyncTask<Void, Void, InputStream>() {

			@Override
			protected InputStream doInBackground(Void... params) {
				try {
					URL url = new URL(imageUrl);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setReadTimeout(6 * 1000);
					conn.setRequestMethod("GET");
					InputStream is = conn.getInputStream();
					return is;
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
			protected void onPostExecute(InputStream result) {
				if(result==null) {
					Toast.makeText(mContext, R.string.rescreen_shot, Toast.LENGTH_LONG).show();
					return;
				} else {
					File dir = new File(Const.TIVIC_STORE_PATH);
					if(!dir.exists()) {
						dir.mkdir();
					}
					Bitmap bm = BitmapFactory.decodeStream(result);
					String filePath = null;
					if(urb.getUserUID()>0) {
						filePath = Const.TIVIC_STORE_PATH+"/user_"+urb.getUserUID()+"_screenshot.jpg";
					} else {
						filePath = Const.TIVIC_STORE_PATH+"/"+System.currentTimeMillis()+".jpg";
					}
					try {
						if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
							Utils.saveImageToSD(filePath, bm, 100);
						} else {
							Toast.makeText(mContext, R.string.sdcard_not_exists, Toast.LENGTH_LONG).show();
							return;
						}
					} catch (IOException e) {
						e.printStackTrace();
						Toast.makeText(mContext, R.string.save_pic_error, Toast.LENGTH_LONG).show();
					}
//					String msg = getResources().getString(R.string.save_pic_success)+filePath;
//					Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
					
					System.out.println("gv_screen_shot.getSelectedItemPosition()="+gv_screen_shot.getSelectedItemPosition());
					Intent image_intent = new Intent(mContext , ContentPublishActivity.class);
//					image_intent.putExtra("image_url", images[gv_screen_shot.getSelectedItemPosition()]);
					image_intent.putExtra("image_url", filePath);
					setResult(Activity.RESULT_OK, image_intent);
					//zhanglr add 返回发私信
					Intent message_intent = new Intent(mContext , SocialBaseActivity.class);
//					message_intent.putExtra("image_url", images[gv_screen_shot.getSelectedItemPosition()]);
					message_intent.putExtra("image_path", filePath);
					setResult(Activity.RESULT_OK, message_intent);
					finish();
				}
			};
			
		}.execute();
		
	}
	private void saveImage() {
		if(images == null) {
			UIUtils.ToastMessage(mContext, R.string.net_error);
			return;
		}
		File dir = new File(Const.TIVIC_STORE_PATH);
		if(!dir.exists()) {
			dir.mkdir();
		}
		String filePath = Const.TIVIC_STORE_PATH+"/"+System.currentTimeMillis()+".jpg";
		RelativeLayout  rl = (RelativeLayout) gv_screen_shot.getSelectedView();
		AsyncImageView aiv = (AsyncImageView) rl.getChildAt(0);
		Drawable drawable = aiv.getDrawable();
		Bitmap bitmap =  ((BitmapDrawable)drawable).getBitmap();
		try {
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				Utils.saveImageToSD(filePath, bitmap, 100);
			} else {
				Toast.makeText(mContext, R.string.sdcard_not_exists, Toast.LENGTH_LONG).show();
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(mContext, R.string.save_pic_error, Toast.LENGTH_LONG).show();
		}
		String msg = getResources().getString(R.string.save_pic_success)+filePath;
		Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
	}



	private void anewScreenShot() {
		new AsyncTask<Void, Void, String[]>() {
			protected void onPreExecute() {
				ShowProgressDialog.show2(mContext,R.string.login_regsited_interest_load_data, null);
			};
			@Override
			protected String[] doInBackground(Void... params) {
				if(currentChannelId!=null) {
					images = contentImpl.getScreenShotImagesList(Integer.parseInt(currentChannelId));
				}
				return images;
			}
			protected void onPostExecute(String[] result) {
				ShowProgressDialog.dismiss();
				if(result == null) {
					UIUtils.ToastMessage(mContext, R.string.net_error);
					return;
				}
				adapter = new ScreenShotAdapter(mContext, images);
				gv_screen_shot.setAdapter(adapter);
				gv_screen_shot.setSelection(0);
//				adapter.setImages(images);
//				adapter.notifyDataSetChanged();
//				gv_screen_shot.setSelection(0);
			};
		}.execute();
	}
	@Override
	protected void onPause() {
		ShowProgressDialog.dismiss();
		super.onPause();
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
}
