package com.igrs.tivic.phone.Adapter;

import java.util.ArrayList;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Bean.Login.UserPOIItemBean;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PoiItemNameAdapter extends BaseAdapter {
	private ArrayList<UserPOIItemBean> userPOIItemBeans;
	private Context context;
	public PoiItemNameAdapter(Context context,ArrayList<UserPOIItemBean> userPOIItemBeans) {
		this.context = context;
		this.userPOIItemBeans = userPOIItemBeans;
	}
 	@Override
	public int getCount() {
		return userPOIItemBeans.size();
	}

	@Override
	public Object getItem(int position) {
		return userPOIItemBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = View.inflate(context, R.layout.poiitem_gv_item, null);
		TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
		tv_name.setText(userPOIItemBeans.get(position).getPoi_text());
		return view;
	}

}
