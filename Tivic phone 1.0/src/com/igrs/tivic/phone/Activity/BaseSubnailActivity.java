package com.igrs.tivic.phone.Activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Global.ShowProgressDialog;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Utils.GDUtils;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.widget.AsyncImageView;
import com.igrs.tivic.phone.widget.AsyncImageView.OnImageViewLoadListener;

public class BaseSubnailActivity extends Activity implements OnImageViewLoadListener{
	String imgUrl;
	int resId;
	AsyncImageView img;
	String TAG = "BaseSubnailActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.base_subnail_zoom);

		imgUrl = getIntent().getStringExtra("imgUrl"); // 图片名
		Log.i(TAG, "kevin add: enter BaseSubnailActivity imgUrl = " + imgUrl);

		img = (AsyncImageView) this.findViewById(R.id.large_image);
		img.setOnImageViewLoadListener(this);
		img.getImageNow(imgUrl + "!w512");// .setUrl(imgUrl);
//		img.setDefaultImageResource(R.drawable.base_queshengtu4_3);

		Toast toast = Toast.makeText(this,
				getString(R.string.base_subnail_close), Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM, 0, 0);
		toast.show();
		img.setOnClickListener(new View.OnClickListener() { // 点击返回
			public void onClick(View paramView) {
				finish();
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		GDUtils.getGDApplication(BaseSubnailActivity.this).onLowMemory();
		System.gc();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		GDUtils.getGDApplication(BaseSubnailActivity.this).destory();
		System.gc();
	}

	private void showImage(int resId) {
		img.setImageResource(resId);
	}

	private void resizeImageSubnail(int imageId) {

		Bitmap bmp = BitmapFactory.decodeResource(getResources(), imageId);
		int bmpWidth = bmp.getWidth();
		int bmpHeight = bmp.getHeight();
		Log.i(TAG, "bmpWidth = " + bmpWidth + ", bmpHeight = " + bmpHeight);
		if (bmpWidth < TivicGlobal.getInstance().displayWidth
				&& bmpHeight < TivicGlobal.getInstance().displayHeight) {
			showImage(resId);
			return;
		}
		float scale = 0;

		if (bmpWidth > TivicGlobal.getInstance().displayWidth) {
			scale = (float) (TivicGlobal.getInstance().displayWidth)
					/ (float) bmpWidth;

		} else {
			scale = (float) (TivicGlobal.getInstance().displayHeight)
					/ (float) bmpHeight;
		}

		Matrix matrix = new Matrix();

		matrix.postScale(scale, scale);

		Bitmap resizeBmp = Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeight,
				matrix, true);
		img.setImageBitmap(resizeBmp);

	}
	
	@Override
	public void onLoadingStarted(AsyncImageView imageView)
	{
		ShowProgressDialog.show(this, getString(R.string.base_image_loading), null);
	}
 
	public void onLoadingEnded(AsyncImageView imageView, Bitmap image)
 	{
		ShowProgressDialog.dismiss();
 	}

	public void onLoadingFailed(AsyncImageView imageView, Throwable throwable)	
    {
		ShowProgressDialog.dismiss();
		UIUtils.ToastMessage(this, getString(R.string.base_image_failed));
 	}

}
