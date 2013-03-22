package com.igrs.tivic.phone.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Bean.Social.SocialNotifyBean;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;
import com.igrs.tivic.phone.widget.AsyncImageView;

public class SocialNotifyListViewAdapter extends BaseAdapter {

	private ArrayList<SocialNotifyBean> datalist;//放入adapter中的数据
	private Context context;
	private String TAG = "SocialNotifyListViewAdapter";
	
	public SocialNotifyListViewAdapter(Context context, ArrayList<SocialNotifyBean> datalist){
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
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewHolder viewholder = null;
		if(convertView == null)
		{	
			viewholder = new ViewHolder();
			convertView = inflater.inflate(R.layout.social_notify_listview_item, null);
			viewholder.icon = (AsyncImageView)convertView.findViewById(R.id.icon_notify);
			viewholder.title = (TextView)convertView.findViewById(R.id.text_title);
			viewholder.updatetime = (TextView)convertView.findViewById(R.id.text_time);
			viewholder.notifycontent = (TextView)convertView.findViewById(R.id.text_content);
			convertView.setTag(viewholder);
		}else{
			viewholder  = (ViewHolder)convertView.getTag();
		}
		SocialNotifyBean currentBean = datalist.get(position);
		
		if(currentBean.getStatusType() == Const.Social_Notify_Type_Admin)			
		{
			viewholder.title.setText(R.string.social_notify_system);
			viewholder.icon.setDefaultImageResource(R.drawable.social_notify_icon);
			viewholder.icon.setUrl(null);
		}
		else 		
		{
			viewholder.title.setText(R.string.social_notify_status);
			viewholder.icon.setDefaultImageResource(R.drawable.base_default_avater);
			viewholder.icon.setUrl(UIUtils.getUsrAvatar(currentBean.getUid()));
		}
		viewholder.notifycontent.setText(currentBean.getContent());
		viewholder.updatetime.setText(Utils.formatToDateTime(currentBean.getUpdateTime()).substring(5));
				
		return convertView;
	}
	@Override
	public boolean isEnabled(int position) {
		return super.isEnabled(position);
	}
	
	class ViewHolder{
		AsyncImageView icon;
		TextView title;
		TextView updatetime;
		TextView notifycontent;
	}
}