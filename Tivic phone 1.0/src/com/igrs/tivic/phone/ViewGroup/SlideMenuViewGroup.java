package com.igrs.tivic.phone.ViewGroup;

import com.igrs.tivic.phone.R;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.Scroller;

public class SlideMenuViewGroup extends ViewGroup {

	Scroller scroller;
	int count = 0;
	boolean isMenuBarShow = false;

	public SlideMenuViewGroup(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlideMenuViewGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		scroller = new Scroller(getContext());
	}

	public boolean ifMenuBarShown() {
		return isMenuBarShow;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		measureViews(widthMeasureSpec, heightMeasureSpec);
	}

	public void measureViews(int widthMeasureSpec, int heightMeasureSpec) {
		View v0 = findViewById(R.id.base_wave);
		v0.measure(widthMeasureSpec, v0.getLayoutParams().height + v0.getTop()
				+ v0.getBottom());

		View v1 = findViewById(R.id.base_empty);
		v1.measure(widthMeasureSpec, heightMeasureSpec);

		View v2 = findViewById(R.id.usr_menu_bar);
		v2.measure(v2.getLayoutParams().width + v2.getLeft() + v2.getRight(),
				heightMeasureSpec);

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		int childCount = getChildCount();

		View wave = findViewById(R.id.base_wave);
		int height = wave.getMeasuredHeight();
		int width = wave.getMeasuredWidth();
		if (wave.getVisibility() != View.GONE)
			wave.layout(0, 0, wave.getMeasuredWidth(), wave.getMeasuredHeight());

		View empty = findViewById(R.id.base_empty);
		if (empty.getVisibility() != View.GONE)
			empty.layout(0, height, empty.getMeasuredWidth(),
					empty.getMeasuredHeight());

		View menu = findViewById(R.id.usr_menu_bar);
		if (menu.getVisibility() != View.GONE)
			menu.layout(width, 0, width + menu.getMeasuredWidth(),
					menu.getMeasuredHeight());

	}

	public void scrollMenuBar() {
		enableChildrenCache();
		if (getScrollX() == 0) {
			scroller.startScroll(0, 0,
					(findViewById(R.id.usr_menu_bar).getMeasuredWidth()), 0, 500);
			isMenuBarShow = true;
		} else {
//			滑回去无动画
//			scroller.startScroll(0, 0, 0, 0, 500);
//			滑回去有动画
			scroller.startScroll(findViewById(R.id.usr_menu_bar).getMeasuredWidth(), 0, findViewById(R.id.usr_menu_bar).getMeasuredWidth()*(-1), 0, 500);
			isMenuBarShow = false;
		}
		invalidate();
	}
	
	public void closeScrollMenuBar()
	{
		if(getScrollX() != 0){
			scroller.startScroll(0, 0, 0, 0);
//			scroller.startScroll(findViewById(R.id.usr_menu_bar).getMeasuredWidth(), 0, findViewById(R.id.usr_menu_bar).getMeasuredWidth()*(-1), 0, 500);
			isMenuBarShow = false;
		}
	}

	void enableChildrenCache() {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View layout = (View) getChildAt(i);
			layout.setDrawingCacheEnabled(true);
		}
	}

	void clearChildrenCache() {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View layout = (View) getChildAt(i);
			layout.setDrawingCacheEnabled(false);
		}
	}

	@Override
	public void computeScroll() {
		super.computeScroll();
		if (scroller.computeScrollOffset()) {
			scrollTo(scroller.getCurrX(), scroller.getCurrY());
		} else {
			clearChildrenCache();
		}
	}

	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
		postInvalidate();
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		View child;
		for (int i = 0; i < getChildCount(); i++) {
			child = getChildAt(i);
			child.setFocusable(true);
			child.setClickable(true);
		}
	}
}
