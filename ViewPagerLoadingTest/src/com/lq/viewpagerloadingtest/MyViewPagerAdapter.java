package com.lq.viewpagerloadingtest;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyViewPagerAdapter extends PagerAdapter {
	
	private List<String> StringList ;
	private Context mContext;
	private MainActivity mActivity;
	private int viewPagerSize;
	private int loadingSize = 5;	
	private int leftViewId;
	private int rightViewId;
	private DataLoadingListener dataLoadingListener;
	interface DataLoadingListener{
		void Loading(int position);
	}
	public void setDataLoadingListener(DataLoadingListener dataLoadingListener){
		this.dataLoadingListener = dataLoadingListener;
	}
	public MyViewPagerAdapter(MainActivity mActivity) {
		this.mActivity = mActivity;
		mContext = mActivity;
	}
	
	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		dataLoadingListener.Loading(position);
		/*mActivity.setCurrentViewpagerID(position);
		if(position  == rightViewId){
			//add more view 
//			mActivity.initData(loadingSize,position);
			mActivity.initData(position);
//			loadMore(position);
		}*/
	}
	
	private void loadMore(int position) {
//		int len = position + loadingSize;//
//		if(len > StringList.size()){
//			len = StringList.size() - position;
//			
//		}else if(len == StringList.size()){
//			
//		}else if(len < StringList.size()){
//			
//		}
		
//		for (int i = 0; i < StringList.size(); i++) {
//			
//		}
		
		StringList.set(position + 1, "loading : "+position + 1);
//		notifyDataSetChanged();
		rightViewId++;
	}
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View itemView = creatItemView(position);
		container.addView(itemView, 0);
		return itemView;
	}
	
	private View creatItemView(int position) {
		TextView tView = new TextView(mContext);
		tView.setText(StringList.get(position));
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		lp.gravity = Gravity.CENTER;
		tView.setGravity(Gravity.CENTER);
//		setLayoutParams(lp);
		return tView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View)object);
	}
	
	@Override
	public int getCount() {
		return StringList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	public void setDate(int leftViewId, int rightViewId) {
		this.leftViewId = leftViewId;
		this.rightViewId = rightViewId;
	}
	public void setList(List<String> list){
		this.StringList = list;
	}
}
