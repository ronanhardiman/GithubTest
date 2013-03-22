package com.igrs.tivic.phone.Adapter;

import java.util.ArrayList;
import java.util.List;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Bean.EPG.EPGProgramBean;
import com.igrs.tivic.phone.widget.AsyncImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class SocialFriendPhotoAdapter extends BaseAdapter {
	private Context context;
	private ImageView iv;
//	private List<Bitmap> myPhotos;
	private List<String> myPhotosUrls;
	/*final int ViewType = 2;
	final int Type_Add = 0;
	final int Type_Show = 1;*/
	
	public SocialFriendPhotoAdapter(Context context){
		this.context = context;
//		myPhotos = new ArrayList<Bitmap>();
		myPhotosUrls = new ArrayList<String>();
	}
	
	/*public void setMyPhotos(List<Bitmap> myPhotos) {
		this.myPhotos = myPhotos;
	}*/
	
	public void setMyPhotosUrls(List<String> myPhotosUrls) {
		this.myPhotosUrls = myPhotosUrls;
	}
	
	@Override
	public int getCount() {
		return myPhotosUrls.size();
	}

	

	@Override
	public Object getItem(int item) {
		// TODO Auto-generated method stub
		return myPhotosUrls.get(item);
	}

	@Override
	public long getItemId(int itemId) {
		// TODO Auto-generated method stub
		return itemId;
	}

	/*@Override
	public int getViewTypeCount() {
		return ViewType;
	}

	@Override
	public int getItemViewType(int position) {
		if(position == 0){
			return Type_Add;
		}else{
			return Type_Show;
		}
	}*/

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ShowHolder showHolder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.social_photo_item, null);
			showHolder = new ShowHolder();
			showHolder.photo = (AsyncImageView) convertView.findViewById(R.id.social_iv_photo);
			convertView.setTag(showHolder);
		}else{
			showHolder = (ShowHolder) convertView.getTag();
		}
		//set resource
		String photoUrl = myPhotosUrls.get(position);
		showHolder.photo.setUrl(photoUrl);
		return convertView;
	}
	
	public void addPhotoUrl(String photoUrl){
		myPhotosUrls.add(photoUrl);
	}
	
	/*//add program
	public void addBitmap(Bitmap photo) {
		myPhotos.add(photo);
	}*/
	
	class ShowHolder{
		AsyncImageView photo;
	}

}
