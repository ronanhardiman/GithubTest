package zp.picture;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

public class MyPagerAdapter2 extends PagerAdapter {

	private static final String TAG = "PicViewActivity";
	public List<View> mListViews;

	public MyPagerAdapter2(List<View> mListViews) {
		this.mListViews = mListViews;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		Log.d(TAG, "destroyItem");
		((ViewPager) arg0).removeView(mListViews.get(arg1));
	}

	@Override
	public void finishUpdate(View arg0) {
		Log.d(TAG, "finishUpdate");
	}

	@Override
	public int getCount() {
		Log.d(TAG, "getCount");
		return mListViews.size();
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
		Log.d(TAG, "instantiateItem");
		((ViewPager) arg0).addView(mListViews.get(arg1), 0);
		return mListViews.get(arg1);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		Log.d(TAG, "isViewFromObject");
		return arg0 == (arg1);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		Log.d(TAG, "restoreState");
	}

	@Override
	public Parcelable saveState() {
		Log.d(TAG, "saveState");
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
		Log.d(TAG, "startUpdate");
	}

}
