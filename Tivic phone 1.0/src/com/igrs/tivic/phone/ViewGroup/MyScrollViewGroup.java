package com.igrs.tivic.phone.ViewGroup;
import com.igrs.tivic.phone.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Scroller;

public class MyScrollViewGroup extends ViewGroup {

	Scroller scroller;
//	boolean isLeftShow = false;
	private View rightView;
	private View leftView;
	
	public MyScrollViewGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyScrollViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		scroller = new Scroller(getContext());
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		leftView = findViewById(R.id.left);
		int width = leftView.getMeasuredWidth();
		int height = leftView.getMeasuredHeight();
		
		if(leftView.getVisibility() != View.GONE){
			leftView.layout(-(width - findViewById(R.id.icon_usr).getMeasuredWidth() - findViewById(R.id.icon_cut_line).getMeasuredWidth()), 0, 0, height);
		}
		
		rightView = findViewById(R.id.right);
		int width_r = rightView.getMeasuredWidth();
		int height_r = rightView.getMeasuredHeight();
		if(rightView.getVisibility() != View.GONE){
			rightView.layout(0, 0, width_r, height_r);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	public void scrollMenuBar() {
		enableChildrenCache();
		ImageView icon_user = (ImageView) findViewById(R.id.icon_usr);
		ImageView icon_cut_line = (ImageView) findViewById(R.id.icon_cut_line);
//		int cur_x = scroller.getCurrX();// The new X offset as an absolute distance from the origin.
//		int start_x = scroller.getStartX();//The start X offset as an absolute distance from the origin.
//		System.out.println(cur_x + "  ====== "+start_x);
		int distance = (rightView.getMeasuredWidth() - icon_user.getMeasuredWidth() - icon_cut_line.getMeasuredWidth());
		int dis = leftView.getMeasuredWidth();
		if (getScrollX() == 0) {
			scroller.startScroll(0, 0, -distance, 0);
			findViewById(R.id.icon_cut_line).setVisibility(View.VISIBLE);
//			isLeftShow = true;
		} else {
			scroller.startScroll(-distance, 0, distance, 0 , 500);
			findViewById(R.id.icon_cut_line).setVisibility(View.INVISIBLE);
//			isLeftShow = false;
		}
//		int cur_x_1 = scroller.getCurrX();
//		int start_x_1 = scroller.getStartX();
//		System.out.println(cur_x_1 + "  ******* "+start_x_1);
		invalidate();
	}

	@Override
	public void computeScroll() {
		super.computeScroll();
		if(scroller.computeScrollOffset()){
			scrollTo(scroller.getCurrX(), scroller.getCurrY());
			postInvalidate();
		}else {
			clearChildrenCache();
		}
	}
	
	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
//		postInvalidate();
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
	
	public void initState() {
		enableChildrenCache();
		if (getScrollX() != 0) {
//			滑回去无动画
			scroller.startScroll(0, 0, 0, 0, 500);
	}
		invalidate();
	}
	
}
