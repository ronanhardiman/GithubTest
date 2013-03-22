//package com.igrs.tivic.phone.Listener;
//
//import android.util.Log;
//import android.view.GestureDetector.SimpleOnGestureListener;
//import android.view.MotionEvent;
//import android.widget.Toast;
//
//import com.igrs.tivic.phone.Activity.UGCActivity;
//import com.igrs.tivic.phone.Global.Const;
//import com.igrs.tivic.phone.Global.RotationHelper;
//
//public class GestureListener extends SimpleOnGestureListener {
//	
//	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//			float velocityY) {
//		
//		if (e1.getX()-e2.getX() > 0
//				&& Math.abs(velocityX) > 50) {
//
//// 			切换Activity
////			Toast.makeText(this, "向左手势", Toast.LENGTH_SHORT).show();
////			rotateHelper = new RotationHelper(UGCActivity.this,	Const.KEY_SECOND_CLOCKWISE);
////			rotateHelper.applyFirstRotation(base_main, 0, 90);
//		} else if (e2.getX()-e1.getX() > 0
//				&& Math.abs(velocityX) > 50) {
//			
////			切换Activity
//
////			Toast.makeText(this, "向右手势", Toast.LENGTH_SHORT).show();
////			rotateHelper = new RotationHelper(UGCActivity.this,	Const.KEY_SECOND_CLOCKWISE);
////			rotateHelper.applyFirstRotation(base_main, 0, 90);
//		}
//		
//		return false;  
//	}
//	public boolean onDoubleTap(MotionEvent e) {  
//        Log.i("MyGesture", "onDoubleTap");  
//        //return super.onDoubleTap(e);  
//        return false;
//    }  
//
//	@Override
//	public boolean onDown(MotionEvent e)
//	{
//		// TODO Auto-generated method stub
//		Log.i("TEST", "onDown");
//		return true;
//		//return super.onDown(e);
//	}
//
//	@Override
//	public void onLongPress(MotionEvent e)
//	{
//		// TODO Auto-generated method stub
//		Log.i("TEST", "onLongPress");
//		//super.onLongPress(e);
//	}
//
//	@Override
//	public boolean onScroll(MotionEvent e1, MotionEvent e2,
//			float distanceX, float distanceY)
//	{
//		// TODO Auto-generated method stub
//		Log.i("TEST", "onScroll:distanceX = " + distanceX + " distanceY = " + distanceY);
//		//return super.onScroll(e1, e2, distanceX, distanceY);
//		return false;
//	}
//
//	@Override
//	public boolean onSingleTapUp(MotionEvent e)
//	{
//		// TODO Auto-generated method stub
//		Log.i("TEST", "onSingleTapUp");
//		//return super.onSingleTapUp(e);
//		return false;
//	}
//	
//	public void onShowPress(MotionEvent e) {  
//        Log.i("TEST", "onShowPress");  
//        //super.onShowPress(e);  
//    }  
//}
