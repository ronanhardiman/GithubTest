package com.igrs.tivic.phone.Adapter;

import java.util.ArrayList;
import java.util.List;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Bean.EPG.EPGProgramBean;
import com.igrs.tivic.phone.widget.AsyncImageView;

import android.R.bool;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class SocialPhotoAdapter extends BaseAdapter {
	private Context context;
//	private List<Bitmap> myPhotos;
	private List<String> myPhotoUrls;
	private boolean isDelete;
	/*final int ViewType = 2;
	final int Type_Add = 0;
	final int Type_Show = 1;*/
	
	public SocialPhotoAdapter(Context context){
		this.context = context;
//		myPhotos = new ArrayList<Bitmap>();
		isDelete = false;
		myPhotoUrls = new ArrayList<String>();
	}
	
	/*public void setMyPhotos(List<Bitmap> myPhotos) {
		this.myPhotos = myPhotos;
	}*/

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}

	public void setMyPhotoUrls(List<String> myPhotoUrls) {
		this.myPhotoUrls = myPhotoUrls;
	}

	@Override
	public int getCount() {
		return myPhotoUrls.size()+1;
	}

	@Override
	public Object getItem(int item) {
		// TODO Auto-generated method stub
		if(item == 0)
			return item;
		else
			return myPhotoUrls.get(item-1);
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
			showHolder.delete = (ImageView) convertView.findViewById(R.id.social_iv_delete);
			convertView.setTag(showHolder);
		}else{
			showHolder = (ShowHolder) convertView.getTag();
		}
		//if position == 0,not operate
		if(position > 0){
			String photoUrl = myPhotoUrls.get(position-1);
			showHolder.photo.setUrl(photoUrl);
			if(isDelete)
				showHolder.delete.setVisibility(View.VISIBLE);
			else
				showHolder.delete.setVisibility(View.GONE);
			/*Bitmap photo = myPhotos.get(position-1);
			showHolder.photo.setImageBitmap(photo);*/
			/*View v = convertView;
			iv = (ImageView) v.findViewById(R.id.social_iv_photo);
			 Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(
			 * ),R.drawable.ic_launcher); 
			 * iv.setImageBitmap(bitmap);
			 
			iv.setImageResource(R.drawable.tianjiazhaopian);*/
		}else{
			showHolder.delete.setVisibility(View.GONE);
			if(isDelete){
				Drawable drawable = context.getResources().getDrawable(R.drawable.social_xiangce_tianjiazhaopian);
				showHolder.photo.setImageDrawable(drawable);
//				showHolder.photo.setDefaultImageResource(R.drawable.social_xiangce_tianjiazhaopian);
//				showHolder.photo.setBackgroundResource(R.drawable.social_xiangce_tianjiazhaopian);
			}else{
				Drawable drawable = context.getResources().getDrawable(R.drawable.tianjiazhaopian);
				showHolder.photo.setImageDrawable(drawable);
//				showHolder.photo.setDefaultImageResource(R.drawable.tianjiazhaopian);
//				showHolder.photo.setBackgroundResource(R.drawable.tianjiazhaopian);
			}
			
		}
		return convertView;
	}
	
	/*//add program
	public void addBitmap(Bitmap photo) {
		myPhotos.add(photo);
	}*/
	
	public void addPhotoUrl(String photoUrl) {
		myPhotoUrls.add(photoUrl);
	}
	
	class ShowHolder{
		AsyncImageView photo;
		ImageView delete;
	}


}
