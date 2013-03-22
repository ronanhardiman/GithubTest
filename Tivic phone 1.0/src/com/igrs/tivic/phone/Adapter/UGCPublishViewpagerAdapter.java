package com.igrs.tivic.phone.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Activity.ContentPublishActivity;
import com.igrs.tivic.phone.Bean.UGC.UGCDataBean;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.ViewGroup.PublishView;
import com.igrs.tivic.phone.ViewGroup.PublishView.onButtonClickListener;

public class UGCPublishViewpagerAdapter extends PagerAdapter {
	private Context context;
	private ArrayList<UGCDataBean> ugcList;
	private final String TAG = "UGCPublishViewpagerAdapter";
	public UGCPublishViewpagerAdapter(Context context, ArrayList<UGCDataBean> list){
		this.context = context;
		this.ugcList = list;
	}
	@Override
	public void destroyItem(View container, int position, Object object) {
//        Log.d(TAG, "kevin add: enter destroyItem!");
		((ViewPager) container).removeView((View)object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
//        Log.d(TAG, "kevin add: enter instantiateItem!");

		View itemView = createItemView(position);
		container.addView(itemView, 0);
		return itemView;
	}
	
	public UGCDataBean getItemDataBean(int index)
	{
		if(index >= ugcList.size())
			return null;
		else
			return ugcList.get(index);
	}

	private View createItemView(int pos) {

		PublishView pView = new PublishView(context);
		pView.setViewsData(ugcList.get(pos));
		pView.initData();
		pView.setOnButtonClickListener(new onButtonClickListener() {
			
			@Override
			public void onClick(int index) {

					Intent comment_intent = new Intent(context, ContentPublishActivity.class);
					comment_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
					comment_intent.putExtra("tid", TivicGlobal.getInstance().currentTid);
					comment_intent.putExtra("pid", TivicGlobal.getInstance().currentVisit.getVisit_pid());
					context.startActivity(comment_intent);
				
			}
		});
		return pView;
	}
	@Override
	public int getCount() {
		return ugcList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	/**
     * 跳转到每个页面都要执行的方法
     */
    @Override
    public void setPrimaryItem(View container, int position, Object object) {
    	if(ugcList != null && ugcList.size() > position)
    		TivicGlobal.getInstance().currentTid = ugcList.get(position).getTid();
    }
    
    @Override
    public void startUpdate(ViewGroup container) {
        // TODO Auto-generated method stub
        super.startUpdate(container);

    }
    
    @Override
    public int getItemPosition(Object object) {
            // TODO Auto-generated method stub
            return POSITION_NONE;
    }
}
