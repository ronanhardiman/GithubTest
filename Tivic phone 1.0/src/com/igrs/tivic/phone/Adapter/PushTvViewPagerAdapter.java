package com.igrs.tivic.phone.Adapter;


import java.util.ArrayList;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.igrs.tivic.phone.Bean.PGC.ChartBean;
import com.igrs.tivic.phone.Bean.PGC.ProgramInfoBean;
import com.igrs.tivic.phone.ViewGroup.PullToRefreshListView1;
import com.igrs.tivic.phone.ViewGroup.PullToRefreshListView1.OnRefreshListener;


public class PushTvViewPagerAdapter extends PagerAdapter{

	private Context mContext;
	private CustomOnItemClickListener mCustomOnItemClickListener;
	private MyOnRefereshListener mMyOnRefereshListener;
	private ArrayList<ChartBean> chartBeans;

	public PushTvViewPagerAdapter(Context mContext,ArrayList<ChartBean> chartBeans) {
		this.mContext = mContext;
		this.chartBeans = chartBeans;
	}
	
	@Override
	public Object instantiateItem(View paramView, int paramInt) {
		View view = createItmView(paramInt);
		( (ViewPager) paramView).addView(view, 0);
		return view;
	}
	@Override
	public void destroyItem(View paramView, int paramInt, Object paramObject) {
		ViewPager vp = (ViewPager)paramView;
		vp.removeView((View)paramObject);
	}
	@Override
	public void finishUpdate(View paramView) {
	}
	@Override
	public int getCount() {
		return chartBeans.size();
	}
	@Override
	public boolean isViewFromObject(View paramView, Object paramObject) {
		return paramView == paramObject;
	}
	@Override
	public void restoreState(Parcelable paramParcelable, ClassLoader paramClassLoader) {
	}
	@Override
	public Parcelable saveState() {
		return null;
	}
	@Override
	public void startUpdate(View paramView) {
	}

	private View createItmView(int position){
		PullToRefreshListView1 listView = new PullToRefreshListView1(mContext);
		listView.setScrollingCacheEnabled(false);//滚动的时候没有阴影
		
		listView.setDivider(null);
		listView.hideLoadMoreFooterView();
		
		
		final ArrayList<ProgramInfoBean> pibs = chartBeans.get(position).getProgramList();
		PushTvListdapter myAdapter = new PushTvListdapter(mContext,pibs);
		listView.setAdapter(myAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
				if(mCustomOnItemClickListener!=null){
					if(position>0)
						mCustomOnItemClickListener.doEvent(pibs.get(position-1));
				}
			}
		});
		listView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
//				listView.onRefreshComplete();
				if(mMyOnRefereshListener!=null){
					mMyOnRefereshListener.onRefresh();
				}
			}
		});
		return listView;
	}
	
	public interface CustomOnItemClickListener{
		public void doEvent(ProgramInfoBean programInfoBean);
	}
	public void setmCustomOnItemClickListener(
			CustomOnItemClickListener mCustomOnItemClickListener) {
		this.mCustomOnItemClickListener = mCustomOnItemClickListener;
	}
	public interface MyOnRefereshListener{
		public void onRefresh();
	}
	public void setmMyOnRefereshListener(MyOnRefereshListener mMyOnRefereshListener) {
		this.mMyOnRefereshListener = mMyOnRefereshListener;
	}
}
