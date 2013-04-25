package com.lq.viewpagerloadingtest;

import android.support.v4.view.PagerAdapter;
import android.view.View;

public class MyViewPagerAdapter2 extends PagerAdapter {

	public MyViewPagerAdapter2(MainActivity mainActivity) {
		
	}

	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return false;
	}

}
