package com.igrs.tivic.phone.widget;

import com.igrs.tivic.phone.Activity.WaterfallActivity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class WaterfallLinearLayout extends FrameLayout{

	private int mTouchSlop;
	private float mLastMotionX;
	private VelocityTracker mVelocityTracker;
	public static final int SNAP_VELOCITY = 700;
	protected Context context;
	
	public WaterfallLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		mTouchSlop = ViewConfiguration.get(this.getContext()).getScaledTouchSlop();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		final float x = ev.getX();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mLastMotionX = x;
			break;
		case MotionEvent.ACTION_MOVE:
			final int xDiff = (int)Math.abs(x-mLastMotionX);
			if(xDiff>mTouchSlop){
				return true;
			}
			break;
		}
		return false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		int action = event.getAction();
		if(mVelocityTracker==null){
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
		switch (action) {
		case MotionEvent.ACTION_UP:
			mVelocityTracker.computeCurrentVelocity(1000);
			int velocityX = (int)mVelocityTracker.getXVelocity();
			
			flagScreenFigure(velocityX);
			
			if(mVelocityTracker != null){
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			break;
		}
		return true;
	}
	
	protected void flagScreenFigure(int velocityX){
		if(velocityX < -SNAP_VELOCITY || velocityX > SNAP_VELOCITY){
			WaterfallActivity wfa = (WaterfallActivity)context;
			wfa.jumpToUGC();
		}
	}
}
