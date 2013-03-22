package com.igrs.tivic.phone.Adapter;

import java.util.List;
import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Bean.EPG.EPGChannelsBean;
import com.igrs.tivic.phone.Utils.Utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ScreenChannelApdater extends BaseAdapter {
	private List<EPGChannelsBean>	channels;
	private Context context;
	public ScreenChannelApdater(Context context,List<EPGChannelsBean> channels) {
		this.context = context;
		this.channels = channels;
	}
	@Override
	public int getCount() {
		return channels.size();
	}

	@Override
	public Object getItem(int position) {
		return channels.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if(convertView==null) {
			convertView = View.inflate(context, R.layout.screen_shot_channel_item,null);
			vh = new ViewHolder();
			vh.tv_channel_name = (TextView) convertView.findViewById(R.id.tv_channel_name);
			vh.iv_channel = (ImageView) convertView.findViewById(R.id.iv_channel);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		String channelName = channels.get(position).getChannel_name();
		vh.tv_channel_name.setText(channelName);
		vh.iv_channel.setImageResource(R.drawable.big_channel_icon_29_hunan);
		int rid = Utils.setChannelIcon(context, channelName);
		if(rid!=0) {
			vh.iv_channel.setImageResource(rid);
		}
		return convertView;
	}
	private static class ViewHolder{
		TextView tv_channel_name;
		ImageView iv_channel;
	}
}
