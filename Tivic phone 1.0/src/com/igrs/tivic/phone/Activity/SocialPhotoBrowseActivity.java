package com.igrs.tivic.phone.Activity;

import java.util.ArrayList;
import java.util.List;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Adapter.SocialPhotoBrowseAdapter;
import com.igrs.tivic.phone.AsyncTask.SocialPhotoBrowseAsyncTask;
import com.igrs.tivic.phone.Listener.SocialPhotoBrowseListener;
import com.igrs.tivic.phone.Utils.GDUtils;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Gallery.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ViewSwitcher.ViewFactory;

public class SocialPhotoBrowseActivity extends Activity 
implements ViewFactory, OnItemClickListener {
	private ImageSwitcher mSwitcher;
	private Gallery mGallery;
	private List<String> myPhotoUrlList_128; //my photo url list 128px
	private List<String> myPhotoUrlList_512; //my photo url list 512px
	private int location;
	private LinearLayout ll_photo_loading;
	private SocialPhotoBrowseListener socialPhotoBrowseListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.social_photo_browse);
		init();
	}
	private void init() {
		if(myPhotoUrlList_128 == null)
			myPhotoUrlList_128 = new ArrayList<String>();
		if(myPhotoUrlList_512 == null)
			myPhotoUrlList_512 = new ArrayList<String>();
		myPhotoUrlList_128.clear();
		myPhotoUrlList_128 = getIntent().getStringArrayListExtra("photoUrlList_128");
		myPhotoUrlList_512 = getIntent().getStringArrayListExtra("photoUrlList_512");
		location = getIntent().getIntExtra("location", 0);
		mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
		mGallery = (Gallery) findViewById(R.id.gallery);
		ll_photo_loading = (LinearLayout) findViewById(R.id.photo_browse_loading);
		mSwitcher.setFactory(this);
		SocialPhotoBrowseAdapter photoBrowseAdapter = new SocialPhotoBrowseAdapter(this);
		photoBrowseAdapter.setPhotoUrlList_128(myPhotoUrlList_128);
		mGallery.setAdapter(photoBrowseAdapter);
		mGallery.setOnItemClickListener(this);
		mGallery.setCallbackDuringFling(false);
		
		socialPhotoBrowseListener = new SocialPhotoBrowseListener() {
			
			@Override
			public void notifyPhotoBrowse(Drawable drawable) {
				ll_photo_loading.setVisibility(View.GONE);
				mSwitcher.setImageDrawable(drawable);
			}
		};
	}
	@Override
	public View makeView() {
		ImageView i = new ImageView(this);
		i.setBackgroundColor(0xFFffffff);
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		i.setLayoutParams(new ImageSwitcher.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		return i;
		
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		/*Uri photoUri = Uri.parse(myPhotoUrlList_128.get(arg2));
		mSwitcher.setImageURI(photoUri);*/
//		mSwitcher.setImageResource(R.drawable.kldby);
//		String photoUrl = myPhotoUrlList.get(arg2);
		loadPhotoBrowse(arg2);
		
	}
	private void loadPhotoBrowse(int position) {
		ll_photo_loading.setVisibility(View.VISIBLE);
		mSwitcher.setImageURI(null);
		String photourl_512 = myPhotoUrlList_512.get(position);
		SocialPhotoBrowseAsyncTask socialPhotoSingleAsyncTask = new SocialPhotoBrowseAsyncTask();
		socialPhotoSingleAsyncTask.setSocialPhotoBrowseListener(socialPhotoBrowseListener);
		socialPhotoSingleAsyncTask.execute(photourl_512);
	}
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		
	}
	@Override
	protected void onResume() {
		super.onResume();
		mGallery.setSelection(location);
		loadPhotoBrowse(location);
		
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		GDUtils.getGDApplication(SocialPhotoBrowseActivity.this).onLowMemory();
		System.gc();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		GDUtils.getGDApplication(SocialPhotoBrowseActivity.this).destory();
		System.gc();
	}
	
}
