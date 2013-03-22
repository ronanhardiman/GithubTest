package com.igrs.tivic.phone.Adapter;

import java.util.ArrayList;
import java.util.LinkedList;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Bean.Social.SocialMessageBean;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.URLConfig;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;
import com.igrs.tivic.phone.widget.AsyncImageView;

public class SocialMessageDetailListViewAdapter extends BaseAdapter {

	private LinkedList<SocialMessageBean> datalist;// 放入adapter中的数据
	private Context context;
	private String TAG = "SocialMessageDetailListViewAdapter";
	private int COME_MSG = 0;
	private int TO_MSG = 1;

	public SocialMessageDetailListViewAdapter(Context context,
			LinkedList<SocialMessageBean> datalist) {
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
	public int getItemViewType(int position) {
		// 区别两种view的类型，标注两个不同的变量来分别表示各自的类型
		SocialMessageBean entity = datalist.get(position);
		if (entity.isComeMsg()) {
			return COME_MSG;
		} else {
			return TO_MSG;
		}
	}

	@Override
	public int getViewTypeCount() {
		// 这个方法默认返回1，如果希望listview的item都是一样的就返回1，我们这里有两种风格，返回2
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		SocialMessageBean bean = datalist.get(position);
		ChatHolder chatHolder = null;
		if (convertView == null) {
			chatHolder = new ChatHolder();
			if (bean.isComeMsg()) {
				convertView = inflater.inflate(
						R.layout.social_messag_chat_to_item, null);
			} else {
				convertView = inflater.inflate(
						R.layout.social_messag_chat_from_item, null);
			}
			chatHolder.message = (TextView) convertView
					.findViewById(R.id.text_message);
			chatHolder.updatetime = (TextView) convertView
					.findViewById(R.id.text_time);
			chatHolder.subnail = (AsyncImageView) convertView
					.findViewById(R.id.img_subnail);
			convertView.setTag(chatHolder);
		} else {
			chatHolder = (ChatHolder) convertView.getTag();
		}

		String sendtime;
		if (bean.getText() == null || bean.getText().isEmpty()) {
			chatHolder.message.setVisibility(View.GONE);

		} else {
			chatHolder.message.setVisibility(View.VISIBLE);
			chatHolder.message.setText(bean.getText());
		}
		if (bean.getTime() != null) {
			if (bean.getTime().contains("-") || bean.getTime().contains(":"))
				sendtime = bean.getTime();
			else
				sendtime = Utils.formatToDateTime(bean.getTime());
			chatHolder.updatetime.setText(sendtime);
		}
		if (bean.getImgUrl() != null
				&& bean.getImgUrl().length() > URLConfig.avarter_path.length()) {
			chatHolder.subnail.setVisibility(View.VISIBLE);
			chatHolder.subnail.setDefaultImageResource(R.drawable.base_subnail);
			chatHolder.subnail.setUrl(UIUtils.getImgSubnailUrl(
					datalist.get(position).getImgUrl(), 64));
			chatHolder.subnail.setScaleType(ImageView.ScaleType.FIT_XY);
		} else {

			chatHolder.subnail.setVisibility(View.GONE);

		}
		final int pos = position;
		chatHolder.subnail.setOnClickListener(new OnClickListener() { // 点击放大
					public void onClick(View paramView) {
						Intent intent = new Intent();
						// TODO put Extra resId or imgUrl to
						intent.putExtra("imgUrl", datalist.get(pos).getImgUrl());
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.setClassName("com.igrs.tivic.phone",
								"com.igrs.tivic.phone.Activity.BaseSubnailActivity");
						context.startActivity(intent);
					}
				});

		return convertView;
	}

	@Override
	public boolean isEnabled(int position) {
		return super.isEnabled(position);
	}

	private class ChatHolder {
		private TextView message;
		private TextView updatetime;
		private AsyncImageView subnail;
	}
}