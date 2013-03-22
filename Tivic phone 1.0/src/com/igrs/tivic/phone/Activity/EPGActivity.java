package com.igrs.tivic.phone.Activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Adapter.ChannelAdapter;
import com.igrs.tivic.phone.Adapter.EPGProgramAdapter;
import com.igrs.tivic.phone.Adapter.EPGViewPagerAdapter;
import com.igrs.tivic.phone.AsyncTask.EPGChannelAsyncTask;
import com.igrs.tivic.phone.AsyncTask.EPGGetCollectionAsyncTask;
import com.igrs.tivic.phone.AsyncTask.EPGMarqueeAsyncTask;
import com.igrs.tivic.phone.Bean.MarqueeListBean;
import com.igrs.tivic.phone.Bean.EPG.EPGChannelListBean;
import com.igrs.tivic.phone.Bean.EPG.EPGChannelsBean;
import com.igrs.tivic.phone.Bean.EPG.EPGDailyChannelInfo;
import com.igrs.tivic.phone.Bean.Login.UserRegisterBean;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Listener.EPGCollectionListener;
import com.igrs.tivic.phone.Listener.EPGMarqueeListener;
import com.igrs.tivic.phone.Listener.EPGUpdateUIListener;
import com.igrs.tivic.phone.Utils.GDUtils;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;
import com.igrs.tivic.phone.ViewGroup.EPGView;
import com.igrs.tivic.phone.ViewGroup.MulSlidingDrawer;
import com.igrs.tivic.phone.ViewGroup.MulSlidingDrawer.OnDrawerCloseListener;
import com.igrs.tivic.phone.ViewGroup.MulSlidingDrawer.OnDrawerOpenListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.NotificationType;
import com.umeng.fb.UMFeedbackService;
import com.umeng.update.UmengUpdateAgent;

public class EPGActivity extends BaseActivity implements OnClickListener {
//	TivicGlobal tivicGlobal = TivicGlobal.getInstance();
	private EPGViewPagerAdapter epgViewPagerAdapter;
	private EPGUpdateUIListener epgUpdateUIListener;
	private EPGMarqueeListener marqueeListener;
	private EPGCollectionListener collectionListener;
	private List<EPGChannelsBean> channelsBeans;
	private MarqueeListBean maBean;
	private ChannelAdapter channelAdapter;
	private LinearLayout epg_channel_info;
	private ImageView channel_icon;
	private TextView channel_name;
	private MulSlidingDrawer sDrawer;
	private Button handle;
	private ViewPager epg_pager;
	private GridView allchannels;
	private boolean isFirstEnter = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		UIUtils.setCurrentFuncID(FuncID.ID_EPG);
		super.onCreate(savedInstanceState);
		initEPGLayout();
		setListener();
		loadMarquee();
		//zhanglirong add in 2013.02.05
		//用于友盟统计错误报告
		//错误报告包含应用程序版本，操作系统版本和设备型号以及程序出现异常时的Stacktrace，这些数据将帮助您修正应用程序的Bug
		MobclickAgent.onError(this);
		//用于友盟版本更新下载服务
//		UmengUpdateAgent.update(this);
		//开发者回复用户反馈的提示
//		UMFeedbackService.enableNewReplyNotification(this, NotificationType.AlertDialog);
	}

	
	
	@Override
	protected void onStart() {
		super.onStart();
	/*	initEPGLayout();
		setListener();
		loadMarquee();*/
		/*epgViewPagerAdapter.notifyDataSetChanged();
		epg_pager.setAdapter(epgViewPagerAdapter);*/
		if(isFirstEnter){
			isFirstEnter = false;
		}else{
			loadCollectionData();
		}
		
	}



	private void loadCollectionData() {
		EPGGetCollectionAsyncTask  collectionAsyncTask = new EPGGetCollectionAsyncTask();
		collectionAsyncTask.setCollectionListener(collectionListener);
		collectionAsyncTask.execute(0);
	}



	private void initEPGLayout() {
		if(channelsBeans == null){
			channelsBeans = new ArrayList<EPGChannelsBean>();
		}
		channelsBeans.clear();
		epg_pager = (ViewPager) findViewById(R.id.epg_pager);
		EPGChannelsBean epgChannelsBean = new EPGChannelsBean();
//		epgChannelsBean.setChannel_id(String.valueOf(1));
		epgChannelsBean.setChannel_name(getResources().getString(R.string.beijing));
		epgChannelsBean.setChannel_icon(0);
		channelsBeans.add(epgChannelsBean);
		epgViewPagerAdapter = new EPGViewPagerAdapter(mContext, channelsBeans);
		epg_pager.setAdapter(epgViewPagerAdapter);
		epg_channel_info = (LinearLayout) findViewById(R.id.epg_channel_info);
		channel_icon = (ImageView) findViewById(R.id.channel_icon);
		channel_name = (TextView) findViewById(R.id.channel_name);
		sDrawer = (MulSlidingDrawer) findViewById(R.id.slidingdrawer);
		if (sDrawer != null)
			sDrawer.close();
		handle = (Button) findViewById(R.id.handle);
		allchannels = (GridView) findViewById(R.id.allchannels);
		channelAdapter = new ChannelAdapter(mContext, channelsBeans);
		if(allchannels!=null)
			allchannels.setAdapter(channelAdapter);
		marqueeListener = new EPGMarqueeListener() {

			@Override
			public void notifyEPGMarquee(MarqueeListBean result) {
				if(result == null)
					return;
				maBean = result;
				loadEPGChannelData();
			}
		};
		epgUpdateUIListener = new EPGUpdateUIListener() {
			
			@Override
			public void onProgramListGet(EPGDailyChannelInfo result, int position) {
			}
			
			@Override
			public void onChannelListGet(EPGChannelListBean result) {
				if(result == null){
					Toast.makeText(EPGActivity.this, getString(R.string.get_channellist_failed), 1).show();
					channelAdapter.setChannelBeans(channelsBeans);
					return;
				}
				channelsBeans.clear();
				channelsBeans = result.getEpgChannelsBeanList();
				//set channel icon
//				for(EPGChannelsBean channelsBean : channelsBeans){
				if(channelsBeans != null){
					for(int i = 0; i < channelsBeans.size() ; i++){
						String channel_name = channelsBeans.get(i).getChannel_name();
						if(channelsBeans.get(i).getLiveUrlsList().size() != 0){
							String liveUrl = channelsBeans.get(i).getLiveUrlsList().get(0).getDownLoadUrl();
							System.out.println(liveUrl);
						}
						int channel_icon = Utils.setChannelIcon(mContext, channel_name);
						if(channel_icon != 0)
							channelsBeans.get(i).setChannel_icon(channel_icon);
					}
					channelAdapter.setChannelBeans(channelsBeans);
					channelAdapter.notifyDataSetChanged();
				}
				
				//set channelname and channelicon
				if(channelsBeans != null && channelsBeans.size() > 0)
				{
					EPGChannelsBean channelsBean = channelsBeans.get(0);
					channel_name.setText(channelsBean.getChannel_name());
					if(channelsBean.getChannel_icon() != 0)
						channel_icon.setImageResource(channelsBean.getChannel_icon());
				}
				epgViewPagerAdapter.setChannelsBeans(channelsBeans);
				if(maBean != null)
					epgViewPagerAdapter.setMaBean(maBean);
				epgViewPagerAdapter.notifyDataSetChanged();
				epg_pager.setAdapter(epgViewPagerAdapter);
			}
		};
		//collection listener
		collectionListener = new EPGCollectionListener() {
			
			@Override
			public void notifyEPGCollection(SparseArray<SparseIntArray> result) {
				if(result == null || channelsBeans == null){
					return;
				}
				SparseArray<SparseIntArray> collectionArray = result;
				epgViewPagerAdapter.setCollectionArray(collectionArray);
//				epgViewPagerAdapter.updateCollectionUI();
				EPGView epgView = (EPGView) epg_pager.findViewWithTag(TivicGlobal.getInstance().viewPager_page);
				if(epgView!=null){
					epgView.setViewsData(channelsBeans.get(TivicGlobal.getInstance().viewPager_page));
					epgView.updateCollectonUI(collectionArray);
				}
			}
		};
		
	}
	
	private void setListener() {
		sDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			
			@Override
			public void onDrawerOpened() {
				android.view.ViewGroup.LayoutParams params = handle.getLayoutParams();
				params.height = 30;
				handle.setBackgroundResource(R.drawable.hander_up);
				handle.setLayoutParams(params);
			}
		});
		sDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			
			@Override
			public void onDrawerClosed() {
				android.view.ViewGroup.LayoutParams params = handle.getLayoutParams();
				params.height = 15;
				handle.setBackgroundResource(R.drawable.handle_down);
				handle.setLayoutParams(params);
			}
		});
		allchannels.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(channelsBeans.get(arg2).getChannel_icon() != 0)
					channel_icon.setImageResource(channelsBeans.get(arg2).getChannel_icon());
				else
					channel_icon.setImageResource(R.drawable.beijing_big);
				channel_name.setText(channelsBeans.get(arg2).getChannel_name());
				handle.performClick();
				epg_pager.setCurrentItem(arg2,false);
//				UIUtils.Logi("zyl", "当前显示第" + epg_pager.getCurrentItem() + "页 ");
			}
		});
		epg_pager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				if(channelsBeans.get(arg0).getChannel_icon() != 0)
					channel_icon.setImageResource(channelsBeans.get(arg0).getChannel_icon());
				else
					channel_icon.setImageResource(R.drawable.beijing_big);
				channel_name.setText(channelsBeans.get(arg0).getChannel_name());
				TivicGlobal.getInstance().currentPostion = 0;
				TivicGlobal.getInstance().viewPager_page = arg0;
				UIUtils.Logi("zyl", "onPageSelected="+arg0);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				UIUtils.Logi("zyl", "onPageScrollStateChanged="+arg0);
			}
		});
		epg_channel_info.setOnClickListener(this);
	}

	private void loadEPGChannelData() {
		EPGChannelAsyncTask epgChannelAsyncTask = new EPGChannelAsyncTask();
		epgChannelAsyncTask.setEpgUpdateUIListener(epgUpdateUIListener);
		epgChannelAsyncTask.execute();
	}
	
	private void loadMarquee(){
		EPGMarqueeAsyncTask marqueeAsyncTask = new EPGMarqueeAsyncTask();
		marqueeAsyncTask.setMarqueeListener(marqueeListener);
		marqueeAsyncTask.execute(0);
	}
	
	@Override
	protected void onResume() {
		if(sDrawer != null)
			sDrawer.close();
		//回到一级页面后清空足迹
		TivicGlobal.getInstance().currentVisit.setVisit_pid(null);
		super.onResume();
		UIUtils.setCurrentFuncID(FuncID.ID_EPG);
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		TivicGlobal.getInstance().viewPager_page = 0;
	}



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
			/*if(TivicGlobal.getInstance().mIsLogin)
				dialog();
			else
				android.os.Process.killProcess(android.os.Process.myPid());*/
			dialog();
		}

		return super.onKeyDown(keyCode, event);
	}
	
	private void dialog() {
		AlertDialog.Builder builder = new Builder(EPGActivity.this);
		builder.setMessage(getString(R.string.commit_quit));
		builder.setTitle(getString(R.string.prompt));
		builder.setPositiveButton(getString(R.string.commit), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				TivicGlobal.userRegisterBean = new UserRegisterBean();
				GDUtils.getGDApplication(mContext).destory();
				System.gc();
				android.os.Process.killProcess(android.os.Process.myPid());  
			}
		});
		builder.setNegativeButton(getString(R.string.cancle), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.epg_channel_info:
			handle.performClick();
			break;

		default:
			break;
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus && TivicGlobal.getInstance().firstEnterSys) {
			TivicGlobal.getInstance().firstEnterSys = false;
			TivicGlobal.getInstance().screenWidth = epg_channel_info.getWidth();
			Log.i("zyl", "screen=" + TivicGlobal.getInstance().screenWidth);
		}
	}

	


}
