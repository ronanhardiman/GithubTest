package com.igrs.tivic.phone.Adapter;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.widget.AsyncImageView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class SystemIconAdapter extends BaseAdapter {
	private int[] icons = { R.drawable.public_tx_1, R.drawable.public_tx_2,
			R.drawable.public_tx_3, R.drawable.public_tx_4,
			R.drawable.public_tx_5, R.drawable.public_tx_6,
			R.drawable.public_tx_7};

	public int[] getIcons() {
		return icons;
	}

	private Context context;

	public SystemIconAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return icons.length;
	}

	@Override
	public Object getItem(int position) {
		return icons[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.image_item2, null);
			vh = new ViewHolder();
			vh.iv_poiitem = (AsyncImageView) convertView
					.findViewById(R.id.iv_poiitem);
			vh.iv_selected = (ImageView) convertView
					.findViewById(R.id.iv_selected);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		vh.iv_poiitem.setImageResource(icons[position]);
		return convertView;
	}

	private static class ViewHolder {
		AsyncImageView iv_poiitem;
		ImageView iv_selected;
	}
}
