package com.igrs.tivic.phone.Activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.TextView;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Adapter.PushTvViewPagerAdapter;
import com.igrs.tivic.phone.Adapter.PushTvViewPagerAdapter.CustomOnItemClickListener;
import com.igrs.tivic.phone.Adapter.PushTvViewPagerAdapter.MyOnRefereshListener;
import com.igrs.tivic.phone.Bean.PGC.ChartBean;
import com.igrs.tivic.phone.Bean.PGC.ProgramInfoBean;
import com.igrs.tivic.phone.Bean.PGC.PushTVBean;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Global.URLConfig;
import com.igrs.tivic.phone.Impl.PushTvImpl;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;
import com.igrs.tivic.phone.widget.CoverFlow;

public  class PushTVActivity extends BaseActivity  implements OnItemSelectedListener{
	
	TivicGlobal tivicGlobal = TivicGlobal.getInstance();
	private TextView alert_info;
	private CoverFlow cf;
	private ViewPager mPager;
	
	List<String> data;
	
	private PushTvViewPagerAdapter mAdapter;
	private ArrayList<ChartBean> chartBeans;//榜单信息

	private Handler mhandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			PushTVBean bean = (PushTVBean)msg.obj;
			alert_info.setVisibility(View.GONE);
			if(bean==null || bean.getChart()==null || bean.getChart().size()==0 )return;
			chartBeans = bean.getChart();
			setChartTitle();//初始化榜单标题
			setChartContent();//生成榜单内容
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		UIUtils.setCurrentFuncID(FuncID.ID_PUSHTV);
		super.onCreate(savedInstanceState);
		
		mPager = (ViewPager)findViewById(R.id.pager);
        alert_info = (TextView)findViewById(R.id.alert_info_id);
        cf = (CoverFlow)findViewById(R.id.cf);
        
        getNetData();//获取网络信息
	}
	@Override
	protected void onResume() {
		//回到一级页面后清空足迹
		TivicGlobal.getInstance().currentVisit.setVisit_pid(null);
		super.onResume();
		UIUtils.setCurrentFuncID(FuncID.ID_PUSHTV);
	}
	
	//获得服务器数据
	private void getNetData(){
		alert_info.setVisibility(View.VISIBLE);
		PushTvImpl.getInstance().setContentUpdateHandler(mhandler);
		PushTvImpl.getInstance().getPushTv(URLConfig.get_pushtvList);
	}
	
	//设置榜单标题
	private void setChartTitle(){
		CoverFlowAdapter cfAdapter = new CoverFlowAdapter(this);
		cf.setAdapter(cfAdapter);//自定义图片的填充方式
		cf.setAnimationDuration(1500);
		cf.setOnItemSelectedListener(this);
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		mPager.setCurrentItem(position);
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}
	
	//设置榜单内容
	private void setChartContent(){
		mAdapter = new PushTvViewPagerAdapter(this,chartBeans);
		mPager.setAdapter(mAdapter);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				cf.setSelection(position);//设置榜单标题
			}
			@Override
			public void onPageScrolled(int position, float arg1, int arg2) {
			}
			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
		mAdapter.setmCustomOnItemClickListener(new CustomOnItemClickListener() {
			@Override
			public void doEvent(ProgramInfoBean programInfoBean) {
				jumpToActivity(programInfoBean);
			}
		});
		mAdapter.setmMyOnRefereshListener(new MyOnRefereshListener() {
			@Override
			public void onRefresh() {
				getNetData();
			}
		});
	}
	public void jumpToActivity(ProgramInfoBean programInfoBean) {

		Intent intent = new Intent();
		if(programInfoBean.getProgramid()==0)
			programInfoBean.setProgramid(102);
		Log.e("afff","================jumpToActivity====="+programInfoBean.getProgramid()+","+programInfoBean.getChannelid());
		intent.putExtra("program_id",programInfoBean.getProgramid()+"");
		intent.putExtra("channel_id",programInfoBean.getChannelid()+"");
		intent.putExtra("program_title",programInfoBean.getName());
		intent.putExtra("channel_name",Utils.getChannelNameById(this,programInfoBean.getChannelid()));
		;
		intent.setClassName("com.igrs.tivic.phone",
				"com.igrs.tivic.phone.Activity.WaterfallActivity");
		startActivity(intent);
		UIUtils.setCurrentFuncID(FuncID.ID_WATERFALL);
	}
	
	public class CoverFlowAdapter extends BaseAdapter {
		int mGalleryItemBackground;
		private Context mContext;

		public CoverFlowAdapter(Context context) {
			mContext = context;
			TypedArray typedArray = obtainStyledAttributes(R.styleable.Gallery);
			mGalleryItemBackground = typedArray.getResourceId(
					R.styleable.Gallery_android_galleryItemBackground, 0);
		}
		// 第1点改进，返回一个很大的值，例如，Integer.MAX_VALUE
		public int getCount() {
			return chartBeans.size();
		}
		public Object getItem(int position) {
			return position;
		}
		public long getItemId(int position) {
			return position;
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView iv = new TextView(mContext);
			iv.setText(chartBeans.get(position).getTitle());
			iv.setLayoutParams(new Gallery.LayoutParams(60, 30));
//			iv.setBackgroundColor(Color.YELLOW);
			iv.setGravity(Gravity.CENTER);
			iv.setSingleLine(true);
			iv.setEllipsize(TruncateAt.END);
			iv.setTextSize(5);
			return iv;
		}
	}
}