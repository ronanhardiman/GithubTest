/**
 *COPYRIGHT NOTICE
 *Copyright (c) 2009ï½?012, IGRSlab   
 *All rights reserved.
 * @author wjf
 * @file TivicScaleAnimation.java
 * @brief TODO
 * @version 
 * @date 2012-5-23
 * 
 */
package com.igrs.tivic.phone.Animation;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.igrs.tivic.phone.Bean.Point;

/**
 * COPYRIGHT NOTICE Copyright (c) 2009ï½?012, IGRSlab All rights reserved.
 * 
 * @author wjf
 * @file TivicScaleAnimation.java
 * @brief TODO
 * @version
 * @date 2012-5-23
 * 
 */
public class TAnimation {

	public static TranslateAnimation getTranslateAnimation(float fromXDelta,
			float toXDelta, float fromYDelta, float toYDelta) {
		return new TranslateAnimation(fromXDelta, toXDelta, fromYDelta,
				toYDelta);
	}

	public static Animation getScaleAnimation(float fromX, float toX,
			float fromY, float toY) {
		return new ScaleAnimation(fromX, toX, fromY, toY);
	}

	public static AnimationSet getAnimationSet() {
		return new AnimationSet(true);
	}

	/**
	 * 
	 * @param fromXValue Change in X coordinate to apply at the start of the animation. This value can either be an absolute number if fromXType is ABSOLUTE, or a percentage (where 1.0 is 100%) otherwise.
	 * @param toXValue Change in X coordinate to apply at the end of the animation. This value can either be an absolute number if toXType is ABSOLUTE, or a percentage (where 1.0 is 100%) otherwise.
	 * @param fromYValue Change in Y coordinate to apply at the start of the animation. This value can either be an absolute number if fromYType is ABSOLUTE, or a percentage (where 1.0 is 100%) otherwise.
	 * @param toYValue Change in Y coordinate to apply at the end of the animation. This value can either be an absolute number if toYType is ABSOLUTE, or a percentage (where 1.0 is 100%) otherwise.
	 */
	public static Animation getTranslateAnimationSelf(float fromXValue,
			float toXValue, float fromYValue, float toYValue) {
		TranslateAnimation tAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, fromXValue,
				Animation.RELATIVE_TO_SELF, toXValue,
				Animation.RELATIVE_TO_SELF, fromYValue,
				Animation.RELATIVE_TO_SELF, toYValue);
		tAnimation.setDuration(500);
		return tAnimation;
	}
	/**
	 * 
	 * @param fromAlpha
	 * @param toAlpha
	 * @return
	 */
	public static Animation getAlphaAnimationSelf(float fromAlpha, float toAlpha) {
		AlphaAnimation animation = new AlphaAnimation(fromAlpha, toAlpha);
		animation.setDuration(300);
		return animation;
	}
}
