package com.igrs.tivic.phone.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.R.string;
import com.igrs.tivic.phone.Activity.VideoPlayActivity;
import com.igrs.tivic.phone.Adapter.EPGProgramAdapter;
import com.igrs.tivic.phone.AsyncTask.EPGAddCollectionAsyncTask;
import com.igrs.tivic.phone.AsyncTask.EPGCancelCollectionAsyncTask;
import com.igrs.tivic.phone.AsyncTask.EPGGetCollectionAsyncTask;
import com.igrs.tivic.phone.AsyncTask.EPGProgramAsyncTask;
import com.igrs.tivic.phone.Bean.ChannelMarquee;
import com.igrs.tivic.phone.Bean.MarqueeBean;
import com.igrs.tivic.phone.Bean.MarqueeListBean;
import com.igrs.tivic.phone.Bean.EPG.EPGChannelListBean;
import com.igrs.tivic.phone.Bean.EPG.EPGChannelsBean;
import com.igrs.tivic.phone.Bean.EPG.EPGDailyChannelInfo;
import com.igrs.tivic.phone.Bean.EPG.EPGProgramBean;
import com.igrs.tivic.phone.Bean.EPG.EPGProgramsBean;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Listener.EPGAddCollectionListener;
import com.igrs.tivic.phone.Listener.EPGCancelCollectionListener;
import com.igrs.tivic.phone.Listener.EPGCollectionListener;
import com.igrs.tivic.phone.Listener.EPGUpdateUIListener;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;
import com.igrs.tivic.phone.ViewGroup.PullToRefreshListView1.OnLoadMoreListener;
import com.igrs.tivic.phone.ViewGroup.PullToRefreshListView1.OnLoadNextDayEPGDataListener;
import com.igrs.tivic.phone.ViewGroup.PullToRefreshListView1.OnLoadPreviousDayEPGDataListener;
import com.igrs.tivic.phone.ViewGroup.PullToRefreshListView1.OnRefreshListener;
import com.igrs.tivic.phone.widget.AsyncImageView;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EPGView extends FrameLayout implements OnLoadPreviousDayEPGDataListener, OnLoadNextDayEPGDataListener, View.OnClickListener {
	private TivicGlobal tivicGlobal = TivicGlobal.getInstance();
	private SimpleDateFormat df_yyyy_MM_dd_HH_mm_ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private boolean isFirstEnter = true;
	private static Context mContext;
	private EPGUpdateUIListener epgUpdateUIListener;
	private EPGCollectionListener collectionListener;
	private EPGAddCollectionListener addCollectionListener;
	private EPGCancelCollectionListener cancelCollectionListener;
	private EPGProgramAdapter epgProgramAdapter;
	private LinearLayout epg_data;
	private LayoutInflater inflater;
	private EPGChannelsBean channelsBean;
	private List<EPGProgramsBean> programsBeans = new ArrayList<EPGProgramsBean>();
	private String channel_ad,currentProgram;
	private MarqueeListBean maBean;
	private SparseArray<SparseIntArray> collectionArray = new SparseArray<SparseIntArray>();
	//epg tv
	private AsyncImageView channelimg;
	private ImageView iv_play,livenow;
	private TextView tv_cpro;
	private ProgressBar pro_progressBar;
	private String curProgram_starttime,curProgram_endtime;
	private Date curProgram_startdate,curProgram_enddate;
	private Timer timer = new Timer();
	private MyTask myTask = new MyTask();
	private int current_postion = 0;
	private Handler handler;
	//epg list
	private LinearLayout epg_loadView;
	private PullToRefreshListView1 listView;
	private EPGLightShow epgLightShow;
	private TextView program_name,program_time;
	private ImageView iv_collection;
	private ScrollForeverTextView program_ad,program_hot;
	private int loadlater = 0 ,loadbefore = 0;
	private static final int LOADLATER = 10 ,LOADBEFORE = 11 , LOADCURRENT =12;
	private int mark = LOADCURRENT;
	private String channel_id;
	private String program_id;
	private int curPostion;
	private float poster_scale = 3.0f/8.0f;
	
	public EPGView(Context context) {
		super(context);
		this.mContext = context;
		inflater = LayoutInflater.from(mContext);
		initView();
	}

	public EPGView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		inflater = LayoutInflater.from(mContext);
		initView();
	}
	

	public EPGView(Context context, EPGChannelsBean channelsBean,
			MarqueeListBean maBean) {
		super(context);
		this.mContext = context;
		inflater = LayoutInflater.from(mContext);
		this.channelsBean = channelsBean;
		this.maBean = maBean;
		initView();
	}

	public void setMaBean(MarqueeListBean maBean) {
		this.maBean = maBean;
	}

	public void setCollectionArray(SparseArray<SparseIntArray> collectionArray) {
		this.collectionArray = collectionArray;
	}

	private void initView() {
		epg_data = (LinearLayout) inflater.inflate(R.layout.epg_data, null);
		epg_loadView = (LinearLayout) epg_data.findViewById(R.id.epg_loading);
		channelimg = (AsyncImageView) epg_data.findViewById(R.id.channel_img);
		channelimg.setDefaultImageResource(R.drawable.public_default_pic_8_3);
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) channelimg.getLayoutParams();
		float width = tivicGlobal.screenWidth;
		float height = width * poster_scale;
		
		if(width != 0 || height != 0){
			params.width = (int) width;
			params.height = (int) height;
			channelimg.setLayoutParams(params);
		}
		iv_play = (ImageView) epg_data.findViewById(R.id.play);
		tv_cpro = (TextView) epg_data.findViewById(R.id.tv_curprogram);
		livenow = (ImageView) epg_data.findViewById(R.id.epg_live_now);
		pro_progressBar = (ProgressBar) epg_data.findViewById(R.id.progressBar);
		program_ad = (ScrollForeverTextView) epg_data.findViewById(R.id.program_ad);
		listView = (PullToRefreshListView1) epg_data.findViewById(R.id.lv_program);
		//zhnanglr add new
		listView.initToEPGListviewMode();
		epgProgramAdapter = new EPGProgramAdapter(mContext, programsBeans);
		listView.setAdapter(epgProgramAdapter);
		listView.setOnPreviousDataLoadListener(this);
		listView.setOnNextDataLoadListener(this);
		//light show
		epgLightShow = (EPGLightShow) epg_data.findViewById(R.id.epg_lightshow);
		program_name = (TextView) epgLightShow.findViewById(R.id.program_light_name);
		program_time = (TextView) epgLightShow.findViewById(R.id.program_light_time);
		program_hot = (ScrollForeverTextView) epgLightShow.findViewById(R.id.program_hot);
		iv_collection = (ImageView) epgLightShow.findViewById(R.id.cp_collection);
		if(channelsBean != null)
			channel_id = channelsBean.getChannel_id();
		myTask.setTimer(timer);
		myTask.setProgressBar(pro_progressBar);
		timer.schedule(myTask, 1000*60, 1000*60);
		handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				EPGProgramsBean programBean = (EPGProgramsBean) msg.obj;
				tv_cpro.setText(getResources().getString(R.string.current_play)+programBean.getProgram_name());
				channelimg.setUrl(programBean.getUrl_p83());
			}
			
		};
		epgUpdateUIListener = new EPGUpdateUIListener() {
			
			@Override
			public void onProgramListGet(EPGDailyChannelInfo result, int position) {
				epg_loadView.setVisibility(View.GONE);
				listView.onPreviousLoadComplete();
				listView.onNextLoadComplete();
				if(result == null || result.getEpgProgramBeanList() == null){
					if(programsBeans.size() <= 0 ){
						iv_collection.setVisibility(View.GONE);
						epgLightShow.setClickable(false);
					}
					epg_loadView.setVisibility(View.GONE);
					listView.hideLoadMoreFooterView();
					return;
				}
				if(mark == LOADCURRENT){
					programsBeans = result.getEpgProgramBeanList();
				}else if(mark == LOADLATER){
					listView.onNextLoadComplete();
					programsBeans.addAll(result.getEpgProgramBeanList());
					if(loadlater >= 7){
						listView.hideLoadMoreFooterView();
					}
					if(result.getEpgProgramBeanList().size() <= 0)
						Toast.makeText(mContext, mContext.getString(R.string.later_nodata), Toast.LENGTH_LONG).show();
				}else if(mark == LOADBEFORE){
					listView.onPreviousLoadComplete();
					programsBeans.addAll(0, result.getEpgProgramBeanList());
					if(result.getEpgProgramBeanList().size() <= 0)
						Toast.makeText(mContext, mContext.getString(R.string.before_nodata), Toast.LENGTH_LONG).show();
				}
//				if(programsBeans.size() > 0){//program data is not null
				epgProgramAdapter.setProgramBeans(programsBeans);
				epgProgramAdapter.notifyDataSetChanged();
				if(programsBeans.size() >0){//program data is not null
//					listView.setSelection(1);
					//set lightshow
					EPGProgramsBean programsBean = programsBeans.get(curPostion);
					program_name.setText(programsBean.getProgram_name());
					program_time.setText(getProgramLightTime(programsBean));
					if(programsBean.getHotspot() != "" || programsBean.getHotspot() != null){
						program_hot.setText(programsBean.getHotspot());
					}
					//set poster
					try{
						EPGProgramsBean live_program;
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date currentDate = new Date();
//						Date currentDate = df.parse("2012-12-27 05:30:00");
						for(int i = 0 ; i < programsBeans.size() ; i++){
							//current time contrst program
							live_program = programsBeans.get(i);
							curProgram_starttime = live_program.getStart_display();
							curProgram_endtime = live_program.getEnd_display();
							curProgram_startdate = df.parse(curProgram_starttime);
							curProgram_enddate = df.parse(curProgram_endtime);
							if(((currentDate.getTime()-curProgram_startdate.getTime())>=0) &&
									((currentDate.getTime() -curProgram_enddate.getTime())<= 0 )){
								current_postion = i;
								String posterUrl = live_program.getUrl_p83();
								channelimg.setUrl(posterUrl);
								currentProgram = live_program.getProgram_name();
								tv_cpro.setText(getResources().getString(R.string.current_play)+currentProgram);
								int pro_schedule = getProgramSchedule(curProgram_startdate,curProgram_enddate);
								pro_progressBar.setProgress(pro_schedule);
								/*pro_progressBar.setProgress(pro_schedule-2);
								pro_progressBar.setSecondaryProgress(pro_schedule);*/
								myTask.setStart_date(curProgram_startdate);
								myTask.setEnd_date(curProgram_enddate);
							}
							
						}
//						listView.setAdapter(epgProgramAdapter);
					}catch (Exception e) {
					}
				}
				if(isFirstEnter){
					//if the first enter channel
					livenow.performClick();
					isFirstEnter = false;
				}
				
				if(maBean != null && channelsBean != null){
					StringBuffer sb = new StringBuffer();
					if(maBean.getGlobalMarqueeList().size() > 0){
						//contain global marquee 
						HashMap<Integer, MarqueeBean> globalMap = maBean.getGlobalMarqueeList();
						Set<Integer> key = globalMap.keySet();
						for(Iterator its = key.iterator(); its.hasNext();){
							Integer k = (Integer) its.next();
							MarqueeBean globalMarquee = globalMap.get(k);
							sb.append(globalMarquee.getMarquee_content());
							sb.append("     ");
						}
					}
					if(maBean.getChannelMarqueeList().containsKey(Integer.valueOf(channel_id))){
						//contain channel marquee
						ArrayList<MarqueeBean> channelMarquees = maBean.getChannelMarqueeList().get(Integer.valueOf(channel_id));
						for(MarqueeBean marqueeBean : channelMarquees){
							sb.append(marqueeBean.getMarquee_content());
							sb.append("     ");
						}
						channel_ad = sb.toString();
					}
					if((maBean.getGlobalMarqueeList().size()<= 0) && 
							!(maBean.getChannelMarqueeList().containsKey(Integer.valueOf(channel_id)))){
						//channel marquee and global marquee is null
						channel_ad = currentProgram;
					}
					channel_ad = sb.toString();
					program_ad.setText(channel_ad);
				}
				if(programsBeans.size() > curPostion)
					listView.setSelection(curPostion+1);
				else
					listView.setSelection(curPostion);
//				listView.setSelection(curPostion+2);
				loadCollectionData();
			}
			
			@Override
			public void onChannelListGet(EPGChannelListBean result) {
				
			}
		};
		
		collectionListener = new EPGCollectionListener() {
			
			@Override
			public void notifyEPGCollection(SparseArray<SparseIntArray> result) {
				if(result == null){
//					Toast.makeText(mContext, "关注不成功！", Toast.LENGTH_LONG).show();
					return;
				}
				if(channelsBean != null && channelsBean.getChannel_id() != null){
					String channel_id = channelsBean.getChannel_id();
					int len = result.indexOfKey(Integer.parseInt(channel_id));
					if(len >= 0){
						SparseIntArray programSArray=result.get(Integer.parseInt(channel_id));
//						for(EPGProgramsBean programsBean : programsBeans){
						for(int i = 0 ; i <programsBeans.size(); i++){
							int program_id = programsBeans.get(i).getProgram_id();
							int key = programSArray.indexOfKey(program_id);
							if(key >= 0){
								programsBeans.get(i).setCollection(true);
							}else{
								programsBeans.get(i).setCollection(false);
							}
						}
					}else{
						for(EPGProgramsBean programsBean : programsBeans){
							programsBean.setCollection(false);
						}
					}
				}
				epgProgramAdapter.setProgramBeans(programsBeans);
				epgProgramAdapter.notifyDataSetChanged();
//				EPGProgramsBean programsBean = programsBeans.get(tivicGlobal.currentPostion);
				//set light show item attention state
				if(programsBeans != null && programsBeans.size() > 0){
					EPGProgramsBean programsBean = programsBeans.get(curPostion);
					if(programsBean.isCollection()){
						iv_collection.setImageResource(R.drawable.cellectioned);
					}else{
						iv_collection.setImageResource(R.drawable.add_collect);
					}
				}
			}
		};
		addCollectionListener = new EPGAddCollectionListener() {
			
			@Override
			public void notifyEPGAddCollection() {
//				iv_collection.setImageResource(R.drawable.cellectioned);
				loadCollectionData();
			}
		};
		cancelCollectionListener = new EPGCancelCollectionListener() {
			
			@Override
			public void notifyEPGCancelCollection() {
//				iv_collection.setImageResource(R.drawable.add_collect);
				loadCollectionData();
			}
		};
		setListener();
		addView(epg_data);
	}
	
	/**
	 * get current program schedule
	 * @param start_date
	 * @param end_date
	 * @return 
	 */
	private int getProgramSchedule(Date start_date,
			Date end_date) {
		Date currentDate = new Date();
		long program_time = end_date.getTime() - start_date.getTime();
		long program_currenttime = currentDate.getTime() - start_date.getTime();
		int pro_schedule = (int) (Float.valueOf(String.valueOf(program_currenttime))/Float.valueOf(String.valueOf(program_time))*100);
		return pro_schedule;
	}

	private void setListener() {
		iv_collection.setOnClickListener(this);
		livenow.setOnClickListener(this);
		iv_play.setOnClickListener(this);
		epgLightShow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			/*	Toast.makeText(mContext,
						"program" + (tivicGlobal.currentPostion + 1) + "进入二级页面",
						Toast.LENGTH_LONG).show();*/
				Intent intent = new Intent();

				//Visit 赋值 
				//zhanglr modify
				EPGProgramsBean programsBean = null ;
				if(programsBeans.size() > 0)
					programsBean = programsBeans.get(curPostion);
				if(programsBean!=null){
					//传固定值，测试用
					/*String program_id = String.valueOf(102);
					String channel_id = String.valueOf(1);
					String channel_name = "安徽卫视";
					intent.putExtra("program_id", program_id);
					intent.putExtra("channel_id", channel_id);
					intent.putExtra("program_title", programsBean.getProgram_name());
					intent.putExtra("channel_name", channel_name);*/
//真实数据					
					
					/*intent.putExtra("channel_id", programsBean.getChannel_id());
					intent.putExtra("channel_name", programsBean.getChannel_name());*/
					Log.e("currentPid","==================currentPid===="+programsBean.getProgram_id()+","+channelsBean.getChannel_id()+","+programsBean.getProgram_name()+","+channelsBean.getChannel_name());

					intent.putExtra("channel_id", channelsBean.getChannel_id());
					intent.putExtra("channel_name", channelsBean.getChannel_name());
					intent.putExtra("program_id", programsBean.getProgram_id());
					intent.putExtra("program_title", programsBean.getProgram_name());
					
				}

				intent.setClassName("com.igrs.tivic.phone",
						"com.igrs.tivic.phone.Activity.WaterfallActivity");
				// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				UIUtils.setCurrentFuncID(FuncID.ID_WATERFALL);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
				mContext.startActivity(intent);
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(programsBeans != null){
					tivicGlobal.currentPostion = arg2-1;
					curPostion = tivicGlobal.currentPostion;
					EPGProgramsBean programsBean = programsBeans.get(tivicGlobal.currentPostion);
//					listView.setSelection(arg2+1);
					listView.setSelection(arg2);
					program_name.setText(programsBean.getProgram_name());
					program_time.setText(getProgramLightTime(programsBean));
					program_hot.setText(programsBean.getHotspot());
					if(programsBean.isCollection()){
						iv_collection.setImageResource(R.drawable.cellectioned);
					}else{
						iv_collection.setImageResource(R.drawable.add_collect);
					}
				}
			}
		});
		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == SCROLL_STATE_IDLE) {
//					ll_times.get(postion).setVisibility(View.GONE);+
					int cur = listView.getFirstVisiblePosition();
					if(cur == 0) cur = 1;
					
//					listView.setSelection(cur+1);
					listView.setSelection(cur);
					tivicGlobal.currentPostion = cur-1;
					curPostion = tivicGlobal.currentPostion;
					if(programsBeans != null && programsBeans.size() > 0){
						EPGProgramsBean programsBean = programsBeans.get(tivicGlobal.currentPostion);
						program_name.setText(programsBean.getProgram_name());
						program_time.setText(getProgramLightTime(programsBean));
						program_hot.setText(programsBean.getHotspot());
						if(programsBean.isCollection()){
							iv_collection.setImageResource(R.drawable.cellectioned);
						}else{
							iv_collection.setImageResource(R.drawable.add_collect);
						}
						/*if(programBean.isIs_collection()){
							iv_collectionList.get(postion).setImageResource(R.drawable.cellectioned);
						}else{
							iv_collectionList.get(postion).setImageResource(R.drawable.add_collect);
						}*/
					}
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
			}
		});
	}

	public void setViewsData(EPGChannelsBean channelsBean){
		this.channelsBean = channelsBean;
	}

	public void initData(){
		if(channelsBean != null && channelsBean.getChannel_id() != null){
			channel_id = channelsBean.getChannel_id();
			String currentDate = Utils.getCurrentDateYMD();
			loadProgramData(channel_id , currentDate);
		}else{
			epg_loadView.setVisibility(View.GONE);
			iv_collection.setVisibility(View.GONE);
//			Toast.makeText(mContext, "无法获取节目信息", Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * get collection program
	 */
	protected void loadCollectionData() {
		EPGGetCollectionAsyncTask collectionAsyncTask = new EPGGetCollectionAsyncTask();
		collectionAsyncTask.setCollectionListener(collectionListener);
		collectionAsyncTask.execute(0);
	}
	
	/**
	 * 返回直播频道列表时更新关注状态
	 * @param collectionArray
	 */
	public void updateCollectonUI(SparseArray<SparseIntArray> collectionArray){
		//collection list is null
		if(collectionArray == null){
			return;
		}
		if(channelsBean != null && channelsBean.getChannel_id() != null){
			String channel_id = channelsBean.getChannel_id();
			int len = collectionArray.indexOfKey(Integer.parseInt(channel_id));
			if(len >= 0){
				SparseIntArray programSArray = collectionArray.get(Integer.parseInt(channel_id));
//				for(EPGProgramsBean programsBean : programsBeans){
				for(int i = 0 ; i <programsBeans.size(); i++){
					int program_id = programsBeans.get(i).getProgram_id();
					int key = programSArray.indexOfKey(program_id);
					if(key >= 0){
						programsBeans.get(i).setCollection(true);
					}else{
						programsBeans.get(i).setCollection(false);
					}
				}
			}else{
				for(EPGProgramsBean programsBean : programsBeans){
					programsBean.setCollection(false);
				}
			}
		}
		epgProgramAdapter.setProgramBeans(programsBeans);
		epgProgramAdapter.notifyDataSetChanged();
//		EPGProgramsBean programsBean = programsBeans.get(tivicGlobal.currentPostion);
		//set light show item attention state
		if(programsBeans != null && programsBeans.size() > 0){
			EPGProgramsBean programsBean = programsBeans.get(curPostion);
			if(programsBean.isCollection()){
				iv_collection.setImageResource(R.drawable.cellectioned);
			}else{
				iv_collection.setImageResource(R.drawable.add_collect);
			}
		}
	
	}
	
	private void loadProgramData(String channel_id ,String loadProgramDate) {
		String[] params = new String[2];
		params[0] = channel_id;
		params[1] = loadProgramDate;
		EPGProgramAsyncTask epgProgramAsyncTask = new EPGProgramAsyncTask();
		epgProgramAsyncTask.setEpgUpdateUIListener(epgUpdateUIListener);
		epgProgramAsyncTask.execute(params);
	}

	@Override
	public void onPreviousLoad() {
//		loadProgramData(channelsBean.getChannel_id(), Utils.getCurrentDateYMD());
		loadbefore --;
		String loadPreviousDate = Utils.getFromDateToWeek(loadbefore);
		loadProgramData(channelsBean.getChannel_id(), loadPreviousDate);
		mark = LOADBEFORE;
	}

	@Override
	public void onNextLoad() {
//		loadProgramData(tivicGlobal.currentVisit.getVisit_channelid());
		loadlater ++;
//		curPostion = programsBeans.size();
		String loadNextDate = Utils.getFromDateToWeek(loadlater);
		loadProgramData(channelsBean.getChannel_id(), loadNextDate);
		listView.onNextLoadComplete();
		mark = LOADLATER;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.play:
			//play video
			if(channelsBean.getLiveUrlsList().size() > 0){
				try{
//				Intent it = new Intent(Intent.ACTION_VIEW); 				
				String path = channelsBean.getLiveUrlsList().get(0).getLiveUrl();
				Log.d("EPGView", "video path = " + path);
				
				//zhanglir add for test
				String url1 = "http://player.pptv.com/v/cSOKCHDWRoTnZc0.swf";
				String url2 = "http://v.pptv.com/show/cSOKCHDWRoTnZc0.html";
				String url3 = "http://web-play.pptv.com/web-m3u8-300145.m3u8";
				String url4 = "synacast://29+p3trV5KqbmKiWqJ6imqSbnqGhnbCdoKCcpOvJ4OSmmKWXoqCamqyanqGgl6icqqicmaag5dTc3bCUn6Kcm6SWoaKam6aToaCgo66VoKCn3trV5KqbmKeXop6em6qTp6KanaafqKCcmbHa1ODgo6WUoaGll6ebp56emq6Tp6GmoaaVoKvhzebZqp+bmqianqSil6ybnqijo66VoKCn3trV5KqbmKyWnqGhnqSdnqKjo66VoKCn3dnV5KqbmKyWnqGhnqSdnqKko66VqKCn3dnV5KqbmKeXpZ6gn6Sbpp6kobCdoKicpOrI4OSmmKWXoKKamqeXnqKcl6eVpaqkma6Vq9jg2eqfn5+emaiToaGel6iVnqGcnrCdoKvU3ebZqp+bn6eToaWhl66Toqimoaag2OTc3bCUn6GenqSZpp6in6SdqKqkmZymxK2dmaeXltPUyuTT1dyp5KzIpdKcoK7InaWhnqqSpNWhzaOdqaSflquYoafQyteZpNLPnvOL09nQpqiZpqWStrOboZ6dnKiToqWgl6eZqaqkmamYqqScnKagpqGamqmXnqKhnaSWpKimoaaXpaqgmaiWq6adl6eYop6enqqToaSlo66Vo6SmnaaYoauimqSWpaOamq6VnqGkn7CdoqCko6qXoKSS19fS1a0iFC0iPjA2PA==";
				String url5 = "http://124.160.184.207:80/live/5/45/887d4d23ee894ceb8f5f457fa24f3e97.m3u8";
				String url6 = "http://218.59.213.203:80/live/5/45/238baa4ac12f427896cecf12234159ef.m3u8?chid=300163&pre=ikan&";
				String url7 = "https://openapi.youku.com/v2/videos/player/v.m3u8?client_id=1697a5c9c24550d2&video_id=XNTA1MjQ2NzA0";
				//根据直播地址的格式调用不同的activity
				if(path.endsWith(".html") || (path.indexOf("cntv")!= -1)){  
					mContext.startActivity(getHtmlFileIntent(path));
				}else if(path.endsWith(".m3u8")){
					mContext.startActivity(getM3U8FileIntent(path));
				}else{
					mContext.startActivity(getVideoFileIntent(url6));
				}
//				Uri uri = Uri.parse(path); 
//		        it.setDataAndType(uri , "video/*"); 
//		        mContext.startActivity(getVideoFileIntent(url5));  //可以播放
//		        mContext.startActivity(getVideoFileIntent(url6));  //可以播放
				}catch(ActivityNotFoundException e){
					UIUtils.ToastMessage(mContext, mContext.getString(R.string.base_player_not_found));
				}
		        
			}else{
				Toast.makeText(mContext, mContext.getString(R.string.video_not_play), Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.cp_collection:
			if(tivicGlobal.mIsLogin){
	//			EPGProgramsBean programsBean = programsBeans.get(tivicGlobal.currentPostion);
				EPGProgramsBean programsBean = programsBeans.get(curPostion);
				boolean isCollection = programsBean.isCollection();
				if(!isCollection){
					//add attention
	//				Toast.makeText(mContext, "add attention", Toast.LENGTH_SHORT).show();
					if(channelsBean != null)
						channel_id = channelsBean.getChannel_id();
					program_id = String.valueOf(programsBean.getProgram_id());
					if(channel_id != null && program_id != ""){
						addAttention(program_id,channel_id);
					}else{
						Toast.makeText(mContext, mContext.getResources().getString(R.string.attention_fail), Toast.LENGTH_LONG).show();
					}
					
				}else{//cancel attention
//					Toast.makeText(mContext, "cancel attention", Toast.LENGTH_SHORT).show();
					if(channelsBean != null)
						channel_id = channelsBean.getChannel_id();
					program_id = String.valueOf(programsBean.getProgram_id());
					if(channel_id != null && program_id != ""){
						cancelAttention(program_id,channel_id);
					}else{
						Toast.makeText(mContext, mContext.getResources().getString(R.string.attention_fail), Toast.LENGTH_LONG).show();
					}
				}
			}else{
				Intent intent = new Intent();
				intent.setClassName("com.igrs.tivic.phone",
						"com.igrs.tivic.phone.Activity.LoginActivity");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
				mContext.startActivity(intent);
			}
			break;
		case R.id.epg_live_now:
			try{
				EPGProgramsBean live_program;
				String start_time,end_time;
				Date start_date,end_date;
				
				Date currentDate = new Date();
//				Date currentDate = df.parse("2012-12-27 01:00:00");
				for(int i = 0 ; i < programsBeans.size() ; i++){
					//current time contrst program
					live_program = programsBeans.get(i);
					start_time = live_program.getStart_display();
					end_time = live_program.getEnd_display();
					start_date = df_yyyy_MM_dd_HH_mm_ss.parse(start_time);
					end_date = df_yyyy_MM_dd_HH_mm_ss.parse(end_time);
					if(((currentDate.getTime()-start_date.getTime())>=0) &&
							((currentDate.getTime() - end_date.getTime())<= 0 )){
						tivicGlobal.currentPostion = i;
						curPostion = i;
//						listView.setSelection(i+2);
						listView.setSelection(i+1);
						program_name.setText(live_program.getProgram_name());
						program_time.setText(getProgramLightTime(live_program));
						program_hot.setText(live_program.getHotspot());
						if(live_program.isCollection()){
							iv_collection.setImageResource(R.drawable.cellectioned);
						}else{
							iv_collection.setImageResource(R.drawable.add_collect);
						}
						return;
					}
					if(i == programsBeans.size()-1){
						if(!isFirstEnter)
							Toast.makeText(mContext, mContext.getString(R.string.the_channel_not_liveprogram), 1).show();
					}
				}
			}catch (Exception e) {
			}
			/*lists.get(postion).setSelection(hour+1);
			EPGProgramBean programBean = channelBeans.get(postion).getProgramBeans().get(hour);
			program_nameList.get(postion).setText(programBean.getPrograme_title());
			program_timeList.get(postion).setText(programBean.getStart_time());*/
			break;
		default:
			break;
		}
	}
	
	public static Intent getHtmlFileIntent(String param) {
		Uri uri = Uri.parse(param);//.buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build();
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.setDataAndType(uri, "text/html");
		return intent;
	}
	
	public static Intent getVideoFileIntent(String param) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.parse(param);
		intent.setDataAndType(uri, "video/*");
		return intent;
	}
	
	public static Intent getM3U8FileIntent(String param){
		Intent intent = new Intent();
		intent.setClass(mContext, VideoPlayActivity.class);
		intent.putExtra("video_url", param);
		return intent;
	}
	
	/**
	 * cancel attention program
	 * @param program_id2
	 * @param channel_id2
	 */
	private void cancelAttention(String program_id, String channel_id) {
		String[] params = new String[]{program_id,channel_id};
		EPGCancelCollectionAsyncTask cancelCollectionAsyncTask = new EPGCancelCollectionAsyncTask();
		cancelCollectionAsyncTask.setCancelCollectionListener(cancelCollectionListener);
		cancelCollectionAsyncTask.execute(params);
	}
	
	/**
	 * add attention program
	 * @param program_id
	 * @param channel_id
	 */
	private void addAttention(String program_id, String channel_id) {
		String[] params = new String[]{program_id,channel_id};
		EPGAddCollectionAsyncTask addCollectionAsyncTask = new EPGAddCollectionAsyncTask();
		addCollectionAsyncTask.setAddCollectionListener(addCollectionListener);
		addCollectionAsyncTask.execute(params);
	}
	
	private String getProgramLightTime(EPGProgramsBean programsBean){
		String start_time = programsBean.getStart_display();
		String end_time = programsBean.getEnd_display();
		String time = Utils.getProgramDateMDHM(start_time)+"--"+Utils.getProgramDateHM(end_time);
		return time;
	}
	
	class MyTask extends TimerTask{
		private Timer timer;
		private ProgressBar progressBar;
		private Date start_date, end_date;
		
		public void setTimer(Timer timer) {
			this.timer = timer;
		}

		public void setProgressBar(ProgressBar progressBar) {
			this.progressBar = progressBar;
		}

		public void setStart_date(Date start_date) {
			this.start_date = start_date;
		}

		public void setEnd_date(Date end_date) {
			this.end_date = end_date;
		}

		@Override
		public void run() {
			if(start_date != null && end_date !=null){
				if(end_date.getTime() < new Date().getTime()){
					current_postion++;
					EPGProgramsBean programBean = programsBeans.get(current_postion);
					Message msg = new Message();
					Bundle bundle = new Bundle();
					msg.obj = programBean;
					handler.sendMessage(msg);
				/*	tv_cpro.setText(getResources().getString(R.string.current_play)+programBean.getProgram_name());
					channelimg.setUrl(programBean.getUrl_p83());*/
					try {
						start_date = df_yyyy_MM_dd_HH_mm_ss.parse(programBean.getStart_display());
						end_date = df_yyyy_MM_dd_HH_mm_ss.parse(programBean.getEnd_display());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				int schedule = getProgramSchedule(start_date, end_date);
				pro_progressBar.setProgress(schedule);
				/*pro_progressBar.setProgress(schedule-2);
				pro_progressBar.setSecondaryProgress(schedule);*/
			}
			
		}
		
	}
}
