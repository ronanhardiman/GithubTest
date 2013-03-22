package com.igrs.tivic.phone.Activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Adapter.MyTvListdapter;
import com.igrs.tivic.phone.Adapter.MyTvListdapter.OnDeleteItemListener;
import com.igrs.tivic.phone.Adapter.MyTvViewPagerAdapter;
import com.igrs.tivic.phone.Adapter.MyTvViewPagerAdapter.CustomOnItemClickListener;
import com.igrs.tivic.phone.Adapter.MyTvViewPagerAdapter.OnListViewScrollStopLitener;
import com.igrs.tivic.phone.Bean.EPG.EPGProgramsBean;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Impl.MyTvImpl;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;
import com.igrs.tivic.phone.widget.ImageTextView;

public  class MyTVActivity extends BaseActivity implements OnClickListener,OnPageChangeListener{
	
	TivicGlobal tivicGlobal = TivicGlobal.getInstance();
	
	private ImageView btn_pre,btn_next,time_rang;
	private TextView my_tv_date_title,my_tv_time_rang_title;
	private FrameLayout btn_time_ring_container;

	List<String> data;
	
	private ListView model_listview_lv;
	
	private ViewPager mPager;
	private MyTvViewPagerAdapter mAdapter;
	private int currentDayPosition;//当天对应的viewPager的position
	private int currentItemPossition;
	
	/**
	 * 1.显示所有一个星期数据的listview模式（MODEL_LISTVIEW）  
	 * 2.分屏显示viewpager模式  一瓶显示一天的数据（MODEL_VIEWPAGER）
	 */
	private static final int MODEL_VIEWPAGER = 0;
	private static final int MODEL_LISTVIEW = 1;
//	private List<MyTVListItem> model_list_datas;//MODEL_LISTVIEW模式对应的数据集
	
	private int viewPager_image_level;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		UIUtils.setCurrentFuncID(FuncID.ID_MYTV);
		super.onCreate(savedInstanceState);
		mPager = (ViewPager)findViewById(R.id.pager);
		model_listview_lv= (ListView)findViewById(R.id.my_tv_lv);
		
	
		mPager.setOnPageChangeListener(this);
		
		my_tv_date_title = (TextView)findViewById(R.id.my_tv_date_title);
		my_tv_time_rang_title = (TextView)findViewById(R.id.my_tv_time_rang_title);
		
		btn_time_ring_container = (FrameLayout)findViewById(R.id.btn_time_ring_container);
        btn_pre = (ImageView)findViewById(R.id.btn_pre);
        btn_next = (ImageView)findViewById(R.id.btn_next);
        
       
        
        time_rang = (ImageView)findViewById(R.id.btn_time_ring); 
        time_rang.setTag(MODEL_VIEWPAGER);//默认  viewPager模式
        btn_pre.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_time_ring_container.setOnClickListener(this);
        
	}
	@Override
	protected void onStart() {
		super.onStart();
		
		mAdapter = new MyTvViewPagerAdapter(this);
		mPager.setAdapter(mAdapter);
		initListViewState();
		if(model_listview_lv.getVisibility() == View.VISIBLE){
			initCreateAllDaysListView();
		}
	}
	@Override
	protected void onResume() {
		//回到一级页面后清空足迹
		TivicGlobal.getInstance().currentVisit.setVisit_pid(null);
		super.onResume();
		UIUtils.setCurrentFuncID(FuncID.ID_MYTV);
	
	}
	private void initListViewState(){
		my_tv_date_title.setText(Utils.getCurrentDateYMD());
		currentDayPosition = Utils.dayForWeek()-1;
		if(currentDayPosition == 0){//周一时
			setImageButtonUnClickable(btn_pre);
		}
		if(currentDayPosition == 6){//周日时
			setImageButtonUnClickable(btn_next);
		}
		mPager.setCurrentItem(currentDayPosition);//显示当天的内容
		currentItemPossition = currentDayPosition;
		updateTimeRange(Utils.getHour()/6);//根据系统当前时间初始化当前时段
		
		mAdapter.setmCustomOnItemClickListener(new CustomOnItemClickListener() {
			@Override
			public void doEvent(EPGProgramsBean ePGProgramsBean) {
				jumpToActivity(ePGProgramsBean);
			}
		});
		mAdapter.setOnListViewScrollStopLitener(new OnListViewScrollStopLitener() {
			@Override
			public void onScrollStop(int item_Hour) {
				updateTimeRange(item_Hour/6);
			}
		});
	}
	
	
	/**
	 * 更新“时段icon”的状态
	 * @param nowLevel
	 */
	private void updateTimeRange(int nowLevel){
		time_rang.getDrawable().setLevel(nowLevel);
		switch (nowLevel) {
		case 0:
			my_tv_time_rang_title.setText(R.string.day_night);
			break;
		case 1:
			my_tv_time_rang_title.setText(R.string.day_morning);
			break;
		case 2:
			my_tv_time_rang_title.setText(R.string.day_afternoon);
			break;
		case 3:
			my_tv_time_rang_title.setText(R.string.day_goldtime);
			break;
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_next:
			mPager.setCurrentItem(mPager.getCurrentItem()+1);
			switchPreOrNextClickable();
			break;
		case R.id.btn_pre:
			mPager.setCurrentItem(mPager.getCurrentItem()-1);
			switchPreOrNextClickable();
			break;
		case R.id.btn_time_ring_container:
			switchTimeRangModel();
			break;
		}
	}
	
	private void switchPreOrNextClickable(){
		if(mPager.getCurrentItem() == 0){
			setImageButtonUnClickable(btn_pre);
		}else if(mPager.getCurrentItem() == 6){
			setImageButtonUnClickable(btn_next);
		}else{
			setImageButtonClickable(btn_next);
			setImageButtonClickable(btn_pre);
		}
	}
	
	private void switchTimeRangModel(){
		if((Integer)time_rang.getTag() == MODEL_VIEWPAGER){
			time_rang.setTag(MODEL_LISTVIEW);
			
			model_listview_lv.setVisibility(View.VISIBLE);
			mPager.setVisibility(View.GONE);
			
			setImageButtonUnClickable(btn_next);
			setImageButtonUnClickable(btn_pre);
			saveModelViewPagerlevel();
			
			my_tv_date_title.setVisibility(View.GONE);
			my_tv_time_rang_title.setVisibility(View.GONE);
			time_rang.setImageResource(R.drawable.my_tv_list_model);
			
//			if(model_listview_lv.getCount()==0)
				initCreateAllDaysListView();
		}else{
			time_rang.setTag(MODEL_VIEWPAGER);
			model_listview_lv.setVisibility(View.GONE);
			mPager.setVisibility(View.VISIBLE);
			
			my_tv_date_title.setVisibility(View.VISIBLE);
			my_tv_time_rang_title.setVisibility(View.VISIBLE);
			time_rang.setImageResource(R.drawable.my_tv_time_rang_selector);
			time_rang.getDrawable().setLevel(viewPager_image_level);
			
			setImageButtonClickable(btn_next);
			setImageButtonClickable(btn_pre);
		}
		hideAllDeleFlag();
	}
	private void setImageButtonClickable(ImageView sourceIv){
		sourceIv.setClickable(true);
		sourceIv.setAlpha(255);
	}
	private void setImageButtonUnClickable(ImageView sourceIv){
		sourceIv.setClickable(false);
		sourceIv.setAlpha(50);
	}
	//保存  viewPager模式下的“时间段标题”、日期和“时间段leve”的值
	private void saveModelViewPagerlevel(){
		viewPager_image_level = time_rang.getDrawable().getLevel();
	}

	private void initCreateAllDaysListView(){
		
		final Handler allBeansHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				ArrayList<EPGProgramsBean> beans = (ArrayList<EPGProgramsBean>)msg.obj;
				if(beans==null || beans.size()==0) return;
				final MyTvListdapter myAdapter = new MyTvListdapter(mContext,beans);
				model_listview_lv.setAdapter(myAdapter);
				myAdapter.setOnDeleteItemListener(new OnDeleteItemListener() {
					@Override
					public void onDeleteItem(EPGProgramsBean targetDelBean) {
						mAdapter.deleMODELLISTVProgram(myAdapter,targetDelBean);//删除MODELIST模式下的数据
						
						mAdapter.asynDelAnotherDayProgram(targetDelBean);//删除MODEL_VIEWPAGER模式下 其他天的节目
						ListView pagerCurrentlv = mAdapter.getMyTvListViews().get(currentItemPossition);
						((MyTvListdapter)pagerCurrentlv.getAdapter()).notifyDataSetChanged();
					}
				});
			}
		};
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				ArrayList<EPGProgramsBean> beans = MyTvImpl.getInstance().getFocusProgramsList();
				Message msg = Message.obtain();
				msg.obj = beans;
				allBeansHandler.sendMessage(msg);
			}
		}).start();
		
		model_listview_lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				EPGProgramsBean bean = (EPGProgramsBean)model_listview_lv.getAdapter().getItem(position);
				if(bean.isShowDelFlag()){//显示出删除按钮时
					bean.setShowDelFlag(false);
					((ImageTextView)view.findViewById(R.id.mytv_itv)).setDeleteIconVisible(View.GONE);//隐藏  删除按钮
				}else{
					jumpToActivity((EPGProgramsBean)model_listview_lv.getAdapter().getItem(position));
//					hideAllDeleFlag();
				}
			}
		});
		model_listview_lv.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				((ImageTextView)view.findViewById(R.id.mytv_itv)).setDeleteIconVisible(View.VISIBLE);//显示  删除按钮
				((EPGProgramsBean)model_listview_lv.getAdapter().getItem(position)).setShowDelFlag(true);
				return true;
			}
		});
	}
	//隐藏所有删除标记
	private void hideAllDeleFlag(){
		//将MODEL_VIEWPAGER模式下所有弹出删除标记设为隐藏
		ListView currentlv = mAdapter.getMyTvListViews().get(currentItemPossition);
		if(currentlv==null) return;
		mAdapter.hideModelVIEWPAGERDelFlag(mAdapter.getListsDatas(),(MyTvListdapter)currentlv.getAdapter());
		
		//将MODEL_LISTVIEW模式下所有弹出删除标记设为隐藏
		mAdapter.hideModelLISTDelFlag(model_listview_lv);
	}
	
	private void jumpToActivity(EPGProgramsBean bean) {
		Intent intent = new Intent();
		Log.e("afff","================jumpToActivity====="+bean.getProgram_id()+","+bean.getProgram_name());
		intent.putExtra("program_id",bean.getProgram_id()+"");
		intent.putExtra("channel_id",bean.getChannel_id()+"");
		intent.putExtra("program_title",bean.getProgram_name());
		intent.putExtra("channel_name",bean.getChannel_name());
		
		intent.setClassName("com.igrs.tivic.phone","com.igrs.tivic.phone.Activity.WaterfallActivity");
		startActivity(intent);
		UIUtils.setCurrentFuncID(FuncID.ID_WATERFALL);
	}
	@Override
	protected void onStop() {
		super.onStop();
		hideAllDeleFlag();
	}
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}
	@Override
	public void onPageSelected(int position) {
		String currentPageMappingDate = getViewPagerItemMappingDate(position);
		my_tv_date_title.setText(currentPageMappingDate);
		currentItemPossition = position;
		//恢复每屏listView的状态
		ListView currentlv = mAdapter.getMyTvListViews().get(position);
		switchPreOrNextClickable();
		if(currentlv!=null){
			Integer fisrtVisiblePosition = mAdapter.getFisrtVisiblePositions().get(position);
			if(fisrtVisiblePosition == null || currentlv.getAdapter().getCount()==0) 
				return;
			else if(fisrtVisiblePosition > currentlv.getAdapter().getCount()-1){
				fisrtVisiblePosition = currentlv.getAdapter().getCount()-1;
				mAdapter.getFisrtVisiblePositions().put(position, fisrtVisiblePosition);
			}
			currentlv.setSelection(fisrtVisiblePosition);
			EPGProgramsBean eb = (EPGProgramsBean)currentlv.getAdapter().getItem(fisrtVisiblePosition);
			updateTimeRange(eb.getHour()/6);//恢复当前listView状态下对应“时段icon”状态
			((MyTvListdapter)currentlv.getAdapter()).notifyDataSetChanged();//每次刷新下列表
		}
		hideAllDeleFlag();
	}
	private String getViewPagerItemMappingDate(int position){     
		return Utils.getFromDateToWeek(position-currentDayPosition);
	}
	public ListView getModel_listview_lv() {
		return model_listview_lv;
	}
}