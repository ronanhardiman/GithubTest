package com.igrs.tivic.phone.Adapter;

import java.util.ArrayList;
import java.util.List;

import com.igrs.tivic.phone.widget.AsyncImageView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Gallery.LayoutParams;

public class SocialPhotoBrowseAdapter extends BaseAdapter {
	private List<String> photoUrlList_128;
	private Context mContext;
	
	public SocialPhotoBrowseAdapter(Context context) {
		super();
		this.mContext = context;
		photoUrlList_128 = new ArrayList<String>();
	}

	public void setPhotoUrlList_128(List<String> photoUrlList_128) {
		this.photoUrlList_128 = photoUrlList_128;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return photoUrlList_128.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return photoUrlList_128.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		AsyncImageView i = new AsyncImageView(mContext);
		i.setUrl(photoUrlList_128.get(position));
		i.setAdjustViewBounds(true);
		/*i.setLayoutParams(new Gallery.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));*/
		i.setLayoutParams(new Gallery.LayoutParams(
				128, 128));
		i.setBackgroundColor(0xFFffffff);
		return i;
	}

}
