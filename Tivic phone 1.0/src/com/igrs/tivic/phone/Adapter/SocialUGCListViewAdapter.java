package com.igrs.tivic.phone.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Bean.UGC.UGCDataBean;

public class SocialUGCListViewAdapter extends BaseAdapter {

	private ArrayList<UGCDataBean> datalist;//放入adapter中的数据
	private Context context;

	private String TAG = "SocialUGCListViewAdapter";
	
	public SocialUGCListViewAdapter(Context context, ArrayList<UGCDataBean> datalist){
		this.context = context;
		this.datalist = datalist;
	}
	@Override
	public int getCount() {
		return datalist.size();
	}
	@Override
	public Object getItem(int position) {
		return datalist.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewholder = null;
		if(convertView == null)
		{
			viewholder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.social_ugc_listview_item, null);
			viewholder.title = (TextView)convertView.findViewById(R.id.text_ugctitle);
			viewholder.updatetime = (TextView)convertView.findViewById(R.id.text_time);
			viewholder.ugccontent = (TextView)convertView.findViewById(R.id.text_content);
			viewholder.commentcount = (TextView)convertView.findViewById(R.id.text_count);
			convertView.setTag(viewholder);
		}else{
			viewholder = (ViewHolder)convertView.getTag();
		}
		UGCDataBean bean = datalist.get(position);
		viewholder.updatetime.setText(bean.getTime());
		viewholder.ugccontent.setText(bean.getContent());
		String count = String.valueOf(bean.getReplyContent()) + context.getResources().getString(R.string.base_publish_count);
		viewholder.commentcount.setText(count);
		viewholder.title.setText(bean.getTitle());
		return convertView;
	}
	@Override
	public boolean isEnabled(int position) {
		return super.isEnabled(position);
	}
	
	class ViewHolder{
		TextView title;
		TextView updatetime;
		TextView ugccontent;
		TextView commentcount;
	}
}