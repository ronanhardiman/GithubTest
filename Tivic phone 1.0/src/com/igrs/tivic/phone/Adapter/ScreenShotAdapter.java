package com.igrs.tivic.phone.Adapter;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.widget.AsyncImageView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ScreenShotAdapter extends BaseAdapter {
	private String[] images;
	private Context context;
	public ScreenShotAdapter(Context context,String[] images) {
		this.context = context;
		this.images = images;
	}
	@Override
	public int getCount() {
		return images.length;
	}

	@Override
	public Object getItem(int position) {
		return images[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
//		AsyncImageView aiv = new AsyncImageView(context);
//		aiv.getImageNow(images[position]);
//		return aiv;
		ImageViewHolder ivh = null;
		if(convertView==null) {
//			convertView = View.inflate(context, R.layout.image_item, null);
			convertView = View.inflate(context, R.layout.screen_shot_image_item, null);
			ivh = new ImageViewHolder();
			ivh.iv_poiitem = (AsyncImageView) convertView.findViewById(R.id.iv_poiitem);
			ivh.iv_selected = (ImageView) convertView.findViewById(R.id.iv_selected);
			convertView.setTag(ivh);
		} else {
			ivh = (ImageViewHolder) convertView.getTag();
		}
		ivh.iv_poiitem.setDefaultImageResource(R.drawable.base_queshengtu4_3);
		ivh.iv_poiitem.setUrl(images[position], 3);
//		ivh.iv_poiitem.onImageRequestEnded(request, image)
		return convertView;
	}
	public String[] getImages() {
		return images;
	}
	public void setImages(String[] images) {
		this.images = images;
	}
	private static class ImageViewHolder{
		AsyncImageView iv_poiitem;
		ImageView iv_selected;
	}
}
