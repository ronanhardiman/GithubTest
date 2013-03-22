package com.igrs.tivic.phone.Adapter;

import com.igrs.tivic.phone.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class LoginRegistedGalleryAdapter extends BaseAdapter {
	public Context context;
	public LoginRegistedGalleryAdapter (Context context){
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		  ImageView i = new ImageView(context);
	        i.setImageResource(R.drawable.base_default_avater);
	        i.setScaleType(ImageView.ScaleType.CENTER_CROP);
//	        i.setLayoutParams(new Gallery.LayoutParams(88, 88));
	        return i;
	}

}
