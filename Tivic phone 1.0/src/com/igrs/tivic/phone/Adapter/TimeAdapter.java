package com.igrs.tivic.phone.Adapter;


import com.igrs.tivic.phone.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TimeAdapter extends BaseAdapter {
	private Context context;
	private String[] times = { "1:00", "2:00", "3:00", "4:00", "5:00", "6:00",
			"7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00",
			"14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00",
			"21:00", "22:00", "23:00" };
	public TimeAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return times.length;
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
		convertView = LayoutInflater.from(context).inflate(R.layout.epg_time_item,
				null);
		TextView tv = (TextView) convertView.findViewById(R.id.tv);
		tv.setText(times[position]);

		return convertView;
	}

}
