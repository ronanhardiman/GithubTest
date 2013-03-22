package com.igrs.tivic.phone.Adapter;


import java.util.ArrayList;
import java.util.List;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Bean.EPG.EPGChannelsBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChannelAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<EPGChannelsBean> channelsBeans = new ArrayList<EPGChannelsBean>();
	
	public ChannelAdapter(Context context,List<EPGChannelsBean> channelsBeans) {
		super();
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.channelsBeans = channelsBeans;
	}

	public void setChannelBeans(List<EPGChannelsBean> channelsBeans) {
		this.channelsBeans = channelsBeans;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(channelsBeans != null)
			return channelsBeans.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(channelsBeans != null)
			return channelsBeans.get(position);
		else
			return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ChannelHolder channelHolder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.channel_item, null);
			channelHolder = new ChannelHolder();
			channelHolder.name = (TextView) convertView.findViewById(R.id.channel_name);
			channelHolder.icon = (ImageView) convertView.findViewById(R.id.channel_icon);
			convertView.setTag(channelHolder);
		}else{
			channelHolder = (ChannelHolder) convertView.getTag();
		}
		//设置资源
		EPGChannelsBean epgChannelsBean = channelsBeans.get(position);
		channelHolder.name.setText(epgChannelsBean.getChannel_name());//show channel name 
		if(epgChannelsBean.getChannel_icon() != 0)
			channelHolder.icon.setImageResource(epgChannelsBean.getChannel_icon());
		else 
			channelHolder.icon.setImageResource(R.drawable.beijing_big);
		/*channelHolder.icon.setImageResource(epgChannelBean.getIcon_path());
		channelHolder.name.setText(epgChannelBean.getChannel_titile());*/
		return convertView;
	}
	class ChannelHolder{
		private TextView name;
		private ImageView icon;
	}
}
