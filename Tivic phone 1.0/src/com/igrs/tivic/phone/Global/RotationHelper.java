//package com.igrs.tivic.phone.Global;
//
//import android.app.Activity;
//import android.util.Log;
//import android.view.ViewGroup;
//import android.view.animation.AccelerateInterpolator;
//
//import com.igrs.tivic.phone.Animation.Rotate3dAnimation;
//import com.igrs.tivic.phone.Listener.DisplayNextViewListener;
//
//public class RotationHelper {
//	
//	DisplayNextViewListener displayNext;
//	
//	public RotationHelper(Activity con,int order){
//		displayNext = new DisplayNextViewListener(con, order);
//	}
//	
//	// 逆时针旋转90
//	public void applyFirstRotation(ViewGroup layout,float start, float end) {
//		// Find the center of the container
//		final float centerX = layout.getWidth() / 2.0f;
//		final float centerY = layout.getHeight() / 2.0f;
//		Log.i("centerX =" + centerX, "centerX");
//		Log.i("centerY =" + centerY, "centerY");
//
//		// Create a new 3D rotation with the supplied parameter
//		// The animation listener is used to trigger the next animation
//		final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end,
//				centerX, centerY, 310.0f, true);
//		rotation.setDuration(500);
//		rotation.setFillAfter(true);
//		rotation.setInterpolator(new AccelerateInterpolator());
//		rotation.setAnimationListener(displayNext);
//		layout.startAnimation(rotation);
//	}
//	
//	public void applyLastRotation(ViewGroup layout,float start, float end) {
//		// Find the center of the container
//		final float centerX = layout.getWidth() / 2.0f;
//		final float centerY = layout.getHeight() / 2.0f;
//		Log.i("centerX =" + centerX, "centerX");
//		Log.i("centerY =" + centerY, "centerY");
//
//		// Create a new 3D rotation with the supplied parameter
//		// The animation listener is used to trigger the next animation
//		final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end,
//				 160, 192, 310.0f, false);
//		rotation.setDuration(700);
//		rotation.setFillAfter(true);
//		rotation.setInterpolator(new AccelerateInterpolator());
//		layout.startAnimation(rotation);
//	}	
//}
