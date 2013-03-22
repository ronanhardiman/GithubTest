//package com.igrs.tivic.phone.Listener;
//
//import android.app.Activity;
//import android.view.animation.Animation;
//
//import com.igrs.tivic.phone.Activity.UGCActivity;
//import com.igrs.tivic.phone.Activity.WaterfallActivity;
//import com.igrs.tivic.phone.Global.Const;
//public class DisplayNextViewListener implements Animation.AnimationListener {
//
//	Object obj;
//
//	// 动画监听器的构造函数
//	Activity ac;
//	int order;
//
//	public DisplayNextViewListener(Activity ac, int order) {
//		this.ac = ac;
//		this.order = order;
//	}
//
//	public void onAnimationStart(Animation animation) {
//	}
//
//	public void onAnimationEnd(Animation animation) {
//		doSomethingOnEnd(order);
//	}
//
//	public void onAnimationRepeat(Animation animation) {
//	}
//
//	private final class SwapViews implements Runnable {
//		public void run() {
//			switch (order) {
//			case Const.KEY_FIRST_INVERSE:
//				((WaterfallActivity) ac).jumpToUGC();
//				break;
//			case Const.KEY_SECOND_CLOCKWISE:
//				((UGCActivity) ac).jumpToWaterfall();
//				break;
//			}
//		}
//	}
//
//	public void doSomethingOnEnd(int _order) {
//		switch (_order) {
//		case Const.KEY_FIRST_INVERSE:
//			((WaterfallActivity) ac).base_main.post(new SwapViews());
//			break;
//
//		case Const.KEY_SECOND_CLOCKWISE:
//			((UGCActivity) ac).base_main.post(new SwapViews());
//			break;
//		}
//	}
//}
