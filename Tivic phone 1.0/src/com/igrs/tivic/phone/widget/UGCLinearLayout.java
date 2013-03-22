package com.igrs.tivic.phone.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.igrs.tivic.phone.Activity.UGCActivity;

public class UGCLinearLayout extends WaterfallLinearLayout{

	
	public UGCLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	protected void flagScreenFigure(int velocityX){
		//zhanglr delete 暂时去掉这个左右滑动
		if(velocityX < -SNAP_VELOCITY || velocityX > SNAP_VELOCITY){
			UGCActivity ugca = (UGCActivity)context;
			ugca.jumpToWaterfall();
		}
	}
}
