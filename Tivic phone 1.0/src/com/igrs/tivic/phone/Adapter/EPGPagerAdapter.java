package com.igrs.tivic.phone.Adapter;

import java.util.ArrayList;
import java.util.List;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Activity.LoginActivity;
import com.igrs.tivic.phone.Activity.WaterfallActivity;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Utils.UIUtils;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


public class EPGPagerAdapter extends PagerAdapter {
	TivicGlobal tivicGlobal = TivicGlobal.getInstance();
	ArrayList<View> list;
	private List<LinearLayout> lch = new ArrayList<LinearLayout>();
	private ListView program ;
	private Context mContext;
	private LayoutInflater inflater;
	private int mCount;
	private EPGProgramAdapter myProgramAdapter;
	
	public EPGPagerAdapter(Context mContext,List<LinearLayout> lch) {
		this.mContext = mContext;
		inflater = LayoutInflater.from(mContext);
		this.lch = lch;
	}
	
	public void setLch(List<LinearLayout> lch) {
		this.lch = lch;
	}

	@Override
	public int getCount() {
		return lch.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == (arg1);
	}

	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return super.getItemPosition(object);
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		// TODO Auto-generated method stub
		if(lch.size()>0){
			((ViewPager)arg0).removeView((View) arg2);
//			arg0.destroyDrawingCache();
		}
//			((ViewPager)arg0).removeView((View) arg2);
//			((ViewPager)arg0).removeView(lch.get(arg1));

	}

	/*@Override
	public Object instantiateItem(View arg0, int arg1) {
		// TODO Auto-generated method stub
		((ViewPager) arg0).addView(list.get(arg1));
		return list.get(arg1);
	}*/

	@Override
	public Object instantiateItem(View container, int position) {
		// TODO Auto-generated method stub
		LinearLayout ll = lch.get(position);
		((ViewPager)container).addView(ll);
		return ll;
		/*View v = inflater.inflate(R.layout.epg_data, null);
		((ViewPager)container).addView(v);*/
		/*program = (ListView) v.findViewById(R.id.lv_program);
		myProgramAdapter = new ProgramAdapter(mContext);
		program.setAdapter(myProgramAdapter);*/
		
		
	}
	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void finishUpdate(View arg0) {
		// TODO Auto-generated method stub

	}
}

	

	