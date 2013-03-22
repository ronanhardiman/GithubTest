package com.igrs.tivic.phone.Adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Activity.MyTVActivity;
import com.igrs.tivic.phone.Adapter.MyTvListdapter.OnDeleteItemListener;
import com.igrs.tivic.phone.Bean.EPG.EPGProgramsBean;
import com.igrs.tivic.phone.Impl.MyTvImpl;
import com.igrs.tivic.phone.widget.ImageTextView;

public class MyTvViewPagerAdapter extends PagerAdapter{

	private Context mContext;
	
	private CustomOnItemClickListener mCustomOnItemClickListener;
	private OnListViewScrollStopLitener onListViewScrollStopLitener;
	
	private SparseArray<ArrayList<EPGProgramsBean>> listsDatas;//所有屏的数据集合 key为position； value为ListView用到的数据
	
	private SparseArray<ListView> myTvListViews;//标识每一屏对应的list对象
	private SparseArray<Integer> fisrtVisiblePositions;//每一屏中ListView对象对应的fistVisiblePostion值
	

	public MyTvViewPagerAdapter(Context mContext) {
		this.mContext = mContext;
		initListsDatas();
		
		myTvListViews = new SparseArray<ListView>();
		fisrtVisiblePositions = new SparseArray<Integer>();
	}
	
	private void initListsDatas(){
		listsDatas = new SparseArray<ArrayList<EPGProgramsBean>>();
		for(int i=0;i<7;i++){
			ArrayList<EPGProgramsBean> datas = new ArrayList<EPGProgramsBean>();
			listsDatas.append(i,datas);
		}
	}
	
	@Override
	public Object instantiateItem(View paramView, int position) {
		ViewPager vp = (ViewPager) paramView;
		View view = createItmView(position);
		vp.addView(view, 0);
		return view;
	}
	@Override
	public void destroyItem(View paramView, int paramInt, Object paramObject) {
		ViewPager vp = (ViewPager)paramView;
		vp.removeView((View)paramObject);
//		myTvListStates.remove(paramInt);
	}
	@Override
	public void finishUpdate(View paramView) {
	}
	@Override
	public int getCount() {
		return listsDatas.size();
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

	private View createItmView(final int viewPager_position){
		final ListView listView = new ListView(mContext);
		final MyTvListdapter myAdapter = new MyTvListdapter(mContext,listsDatas.get(viewPager_position));
		listView.setAdapter(myAdapter);
		listView.setScrollingCacheEnabled(false);//滚动的时候没有阴影
		
		if(listsDatas.get(viewPager_position).size()==0){
			final Handler preBeanHandler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					ArrayList<EPGProgramsBean> beans = (ArrayList<EPGProgramsBean>)msg.obj;
					if(beans==null || beans.size()==0) return;
					listsDatas.get(viewPager_position).addAll(beans);
					myAdapter.notifyDataSetChanged();
					int intiFirstVisiblePosition = beans.get(0).getInitSelectionIndex();
					fisrtVisiblePositions.put(viewPager_position,intiFirstVisiblePosition);//初始化 firstVisiblePosition
					listView.setSelection(intiFirstVisiblePosition);//初始时 listView的item与当前时间对应
				}
			};
			new Thread(new Runnable() {
				@Override
				public void run() {
					ArrayList<EPGProgramsBean> beans = MyTvImpl.getInstance().getFocusProgramsList(viewPager_position);
					Message msg = Message.obtain();
					msg.obj = beans;
					preBeanHandler.sendMessage(msg);
				}
			}).start();
		}
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
				if(listsDatas.get(viewPager_position).get(position).isShowDelFlag()){//显示出删除按钮时
					listsDatas.get(viewPager_position).get(position).setShowDelFlag(false);
					((ImageTextView)view.findViewById(R.id.mytv_itv)).setDeleteIconVisible(View.GONE);//隐藏  删除按钮
				}else{
					if(mCustomOnItemClickListener!=null){
						mCustomOnItemClickListener.doEvent((EPGProgramsBean)parent.getItemAtPosition(position));
						
//						//将MODEL_VIEWPAGER模式下所有弹出删除标记设为隐藏
//						hideModelVIEWPAGERDelFlag(listsDatas,myAdapter);
//						
//						//将MODEL_LISTVIEW模式下所有弹出删除标记设为隐藏
//						MyTVActivity mtv = (MyTVActivity)mContext;
//						hideModelLISTDelFlag(mtv.getModel_listview_lv());
					}
				}
			}
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				((ImageTextView)view.findViewById(R.id.mytv_itv)).setDeleteIconVisible(View.VISIBLE);//显示  删除按钮
				listsDatas.get(viewPager_position).get(position).setShowDelFlag(true);
				return true;
			}
		});
		
		listView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if(scrollState == OnScrollListener.SCROLL_STATE_IDLE){
					int firstVisiblePosition = listView.getFirstVisiblePosition();
					//作用是 滚动结束后 要更新“时间段icon”
					if(onListViewScrollStopLitener!=null) 
						onListViewScrollStopLitener.onScrollStop(((EPGProgramsBean)myAdapter.getItem(firstVisiblePosition)).getHour());

					//作用是  保存当前listView的firstVisiblePosition的值
					fisrtVisiblePositions.put(viewPager_position,firstVisiblePosition);
				}
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {}
		});
		
		//功能是：保证MODEL_VIEWPAGER和MODEL_LISTVIEW模式下数据一致
		//也就是一个模式下删除一个数据，那么同时删除另一个模式下对应的数据
		myAdapter.setOnDeleteItemListener(new OnDeleteItemListener() {
			@Override
			public void onDeleteItem(EPGProgramsBean targetDelBean) {
				MyTVActivity mtv = (MyTVActivity)mContext;
				MyTvListdapter mAdapter = (MyTvListdapter)mtv.getModel_listview_lv().getAdapter();
				deleMODELLISTVProgram(mAdapter,targetDelBean);//删除MODELIST模式下的数据
				
				asynDelAnotherDayProgram(targetDelBean);//删除MODEL_VIEWPAGER模式下 其他天的节目
				myAdapter.notifyDataSetChanged();
			}
		});
		
		myTvListViews.put(viewPager_position,listView);
		
		return listView;
	}
	//删除MODELIST模式下的数据
	public void deleMODELLISTVProgram(MyTvListdapter mAdapter,EPGProgramsBean targetDelBean){
		if(mAdapter!=null){
			removeSameProgram(mAdapter.getDatas(),targetDelBean);//删除MODEL_LISTVIEW模式下指定的数据
			mAdapter.notifyDataSetChanged();
			Log.e("aaa","==========deleMODELLISTVProgram==============");
		}
	}
	//删除一个节目时，由于其他天的时间也可能有此节目，则将其他时间的节目也同时删除
	public void asynDelAnotherDayProgram(EPGProgramsBean targetDelBean){
		for(int i=0;i<listsDatas.size();i++){
			removeSameProgram(listsDatas.get(i),targetDelBean);
		}
	}
	//隐藏MODEL_LISTVIEW模式下删除按钮
	public void hideModelLISTDelFlag(ListView model_list_view){
		MyTvListdapter model_list_mAdapter = (MyTvListdapter)model_list_view.getAdapter();
		if(model_list_mAdapter==null) return;
		for(EPGProgramsBean b : model_list_mAdapter.getDatas()){
			b.setShowDelFlag(false);
		}
		model_list_mAdapter.notifyDataSetChanged();
	}
	//隐藏MODEL_VIEWPAGER模式下删除按钮
	public void hideModelVIEWPAGERDelFlag(SparseArray<ArrayList<EPGProgramsBean>> listsDatas,
			final MyTvListdapter currentAdapter){
		//将MODEL_VIEWPAGER模式下所有弹出删除标记设为隐藏
		for(int i=0;i<listsDatas.size();i++){
			ArrayList<EPGProgramsBean> bs = listsDatas.valueAt(i);
			for(EPGProgramsBean b : bs){
				b.setShowDelFlag(false);
			}
		}
		currentAdapter.notifyDataSetChanged();
	}
	//从数组中删除指定名称的元素
	private void removeSameProgram(List<EPGProgramsBean> programs,EPGProgramsBean deletedProgram){
		if(deletedProgram==null || programs==null) return;
		List<EPGProgramsBean> temps = new ArrayList<EPGProgramsBean>();
		for(EPGProgramsBean epgb : programs){
			if(epgb.getProgram_name().equals(deletedProgram.getProgram_name())){
				temps.add(epgb);
			}
		}
		programs.removeAll(temps);
	}

	public interface CustomOnItemClickListener{
		public void doEvent(EPGProgramsBean ePGProgramsBean);
	}
	public void setmCustomOnItemClickListener(
			CustomOnItemClickListener mCustomOnItemClickListener) {
		this.mCustomOnItemClickListener = mCustomOnItemClickListener;
	}
	public interface OnListViewScrollStopLitener{
		public void onScrollStop(int item_Hour);//item_hour每个item对应的hour属性
	}
	public void setOnListViewScrollStopLitener(
			OnListViewScrollStopLitener onListViewScrollStopLitener) {
		this.onListViewScrollStopLitener = onListViewScrollStopLitener;
	}
	public SparseArray<Integer> getFisrtVisiblePositions() {
		return fisrtVisiblePositions;
	}
	public SparseArray<ListView> getMyTvListViews() {
		return myTvListViews;
	}
	public SparseArray<ArrayList<EPGProgramsBean>> getListsDatas() {
		return listsDatas;
	}
}
