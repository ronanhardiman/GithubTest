package com.igrs.tivic.phone.Adapter;

import java.util.ArrayList;
import java.util.List;

import com.igrs.tivic.phone.Bean.MarqueeListBean;
import com.igrs.tivic.phone.Bean.EPG.EPGChannelsBean;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.ViewGroup.EPGView;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class EPGViewPagerAdapter extends PagerAdapter {
	private Context mContext;
	private LayoutInflater inflater;
	private List<EPGChannelsBean> channelsBeans = new ArrayList<EPGChannelsBean>();
	private SparseArray<SparseIntArray> collectionArray = new SparseArray<SparseIntArray>();
	private MarqueeListBean maBean;

	public EPGViewPagerAdapter(Context mContext,
			List<EPGChannelsBean> channelsBeans) {
		this.mContext = mContext;
		this.channelsBeans = channelsBeans;
	}

	public void setMaBean(MarqueeListBean maBean) {
		this.maBean = maBean;
	}

	public void setChannelsBeans(List<EPGChannelsBean> channelsBeans) {
		this.channelsBeans = channelsBeans;
	}

	public void setCollectionArray(SparseArray<SparseIntArray> collectionArray) {
		this.collectionArray = collectionArray;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (channelsBeans != null)
			return channelsBeans.size();
		else
			return 0;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		View channelItem = createItemView(position);
		container.addView(channelItem, 0); // why transfer 0
		return channelItem;
	}

	private View createItemView(int position) {
		EPGView epgView = new EPGView(mContext);
		// EPGView epgView = new EPGView(mContext, channelsBeans.get(position),
		// maBean);
		epgView.setViewsData(channelsBeans.get(position));
		//set Tag
		epgView.setTag(position);
		if (maBean != null)
			epgView.setMaBean(maBean);
		epgView.initData();
		return epgView;
	}
	
	/*public void updateCollectionUI(){
		if(collectionArray == null){
			return;
		}
		if(epgView != null && channelsBeans.size() > 0){
			epgView = new EPGView(mContext);
			epgView.setViewsData(channelsBeans.get(0));
			epgView.updateCollectonUI(collectionArray);
		}
	}*/
	
	

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == (arg1);
	}

	@Override
	public void setPrimaryItem(View container, int position, Object object) {
		// TODO Auto-generated method stub
		// zhanglr delete
		// if(channelsBeans != null && channelsBeans.size() > position)
		// TivicGlobal.getInstance().currentVisit.setVisit_channelid(channelsBeans.get(position).getChannel_id());
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		if (channelsBeans != null && channelsBeans.size() > 0) {
			((ViewPager) container).removeView((View) object);
		}
	}

	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startUpdate(ViewGroup container) {
		// TODO Auto-generated method stub
		super.startUpdate(container);
	}

}
