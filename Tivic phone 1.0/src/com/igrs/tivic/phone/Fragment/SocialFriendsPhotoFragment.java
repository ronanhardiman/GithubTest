package com.igrs.tivic.phone.Fragment;

import java.util.ArrayList;
import java.util.List;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Activity.SocialPhotoBrowseActivity;
import com.igrs.tivic.phone.Adapter.SocialFriendPhotoAdapter;
import com.igrs.tivic.phone.Adapter.SocialPhotoAdapter;
import com.igrs.tivic.phone.AsyncTask.SocialPhotoAsyncTask;
import com.igrs.tivic.phone.Bean.Base.PhotoListBean;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Listener.SocialPhotoListener;
import com.igrs.tivic.phone.Utils.UIUtils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SocialFriendsPhotoFragment extends SocialBaseFragment {
//	private static SocialFriendsPhotoFragment instance;
	ViewGroup friendPhotoMain;
	//photo
	private SocialPhotoListener socialPhotoListener;
	private GridView gv_friendphoto;
	private SocialFriendPhotoAdapter friendPhotoAdapter;
//	private List<Bitmap> friendPhotoList;
	private List<String> friendPhotoUrlList;
	private List<String> friendPhotoUrlList_128;
	private List<String> friendPhotoUrlList_512;
	private LinearLayout loadView;
	private int photo_uid;
	private int friendUid;
	
//	public static SocialFriendsPhotoFragment instance(){
//		if(instance == null){
//			instance = new SocialFriendsPhotoFragment();
//		}
//		return instance;
//	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	public void setPhoto_uid(int photo_uid) {
		this.photo_uid = photo_uid;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = this.getActivity().getApplicationContext();
		friendPhotoMain = (ViewGroup) inflater.inflate(R.layout.social_base_photo, null);
		init();
		
		return friendPhotoMain;
	}
	
	private void init() {
//		if(friendPhotoList == null)
//			friendPhotoList = new ArrayList<Bitmap>();
		if(friendPhotoUrlList == null)
			friendPhotoUrlList = new ArrayList<String>();
		if(friendPhotoUrlList_128 == null)
			friendPhotoUrlList_128 = new ArrayList<String>();
		if(friendPhotoUrlList_512 == null)
			friendPhotoUrlList_512 = new ArrayList<String>();
		loadView = (LinearLayout) friendPhotoMain.findViewById(R.id.loadingView);
		gv_friendphoto = (GridView) friendPhotoMain.findViewById(R.id.gv_social_photo);
		friendPhotoAdapter = new SocialFriendPhotoAdapter(mContext);
		friendPhotoAdapter.setMyPhotosUrls(friendPhotoUrlList);
		gv_friendphoto.setAdapter(friendPhotoAdapter);
		socialPhotoListener = new SocialPhotoListener() {
			
			@Override
			public void notifySocialPhotoUI(PhotoListBean myPhotos) {
				loadView.setVisibility(View.GONE);
				//if photo list is null,return
				if(myPhotos == null) {
					Toast.makeText(mContext, mContext.getString(R.string.get_friendphoto_failed), Toast.LENGTH_LONG).show();
					return;
				}
				friendPhotoUrlList.clear();
				friendPhotoUrlList_128.clear();
				friendPhotoUrlList_512.clear();
				friendPhotoUrlList = myPhotos.getUrllist();
				for(String friendPhotoUrl : friendPhotoUrlList){
//					String friendPhotoUrl_128 = friendPhotoUrl + Const.PHOTO_128;
					String friendPhotoUrl_128 = UIUtils.getImgSubnailUrl(friendPhotoUrl,128);
					friendPhotoUrlList_128.add(friendPhotoUrl_128);
					String friendPhotoUrl_512 = UIUtils.getImgSubnailUrl(friendPhotoUrl, 512);
					friendPhotoUrlList_512.add(friendPhotoUrl_512);
				}
				friendPhotoAdapter.setMyPhotosUrls(friendPhotoUrlList_128);
				friendPhotoAdapter.notifyDataSetChanged();
			}

			@Override
			public void notifyDeletePhotoUI() {
				
			}
		};
		gv_friendphoto.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TivicGlobal.getInstance().mCurrentUserPhotoID = photo_uid;
				Intent photoBrowseIntent = new Intent(getActivity(),
						SocialPhotoBrowseActivity.class);
			/*	photoBrowseIntent.putStringArrayListExtra(
						"photoUrlList",
						(ArrayList<String>) friendPhotoUrlList);*/
				photoBrowseIntent.putStringArrayListExtra(
						"photoUrlList_128",
						(ArrayList<String>) friendPhotoUrlList_128);
				photoBrowseIntent.putStringArrayListExtra("photoUrlList_512", (ArrayList<String>) friendPhotoUrlList_512);
				photoBrowseIntent.putExtra("location", arg2);
				startActivity(photoBrowseIntent);
			}
		});
		/*socialPhotoListener = new SocialPhotoListener() {
			
			@Override
			public void notifySocialPhotoUI(List<Bitmap> myPhotos) {
				loadView.setVisibility(View.GONE);
				friendPhotoList = myPhotos;
				friendPhotoAdapter.setMyPhotos(friendPhotoList);
				friendPhotoAdapter.notifyDataSetChanged();
			}
		};*/
	}
	//load photo data
	private void loadFriendPhotoData() {
		SocialPhotoAsyncTask socialPhotoAsyncTask = new SocialPhotoAsyncTask(
				mContext);
		socialPhotoAsyncTask.setSocialPhotoListener(socialPhotoListener);
		socialPhotoAsyncTask.execute(photo_uid);
	}
	
	private void clearFriendPhotoData() {
		friendPhotoUrlList.clear();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		loadFriendPhotoData();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
		clearFriendPhotoData();
	}
	

	

}
