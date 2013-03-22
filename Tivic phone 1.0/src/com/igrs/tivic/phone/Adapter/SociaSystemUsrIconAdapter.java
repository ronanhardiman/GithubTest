package com.igrs.tivic.phone.Adapter;

import com.igrs.tivic.phone.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class SociaSystemUsrIconAdapter extends BaseAdapter {
	private int[] drawables = { R.drawable.public_tx_1, R.drawable.public_tx_2,
			R.drawable.public_tx_3, R.drawable.public_tx_4, R.drawable.public_tx_5, R.drawable.public_tx_6, R.drawable.public_tx_7 };
	private Context mContext;

	public SociaSystemUsrIconAdapter(Context context) {
		this.mContext = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return drawables.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.social_system_icon_item, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView.findViewById(R.id.system_icon);
			holder.imageView.setImageResource(drawables[position]);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

}
	class ViewHolder{
		ImageView imageView;
}