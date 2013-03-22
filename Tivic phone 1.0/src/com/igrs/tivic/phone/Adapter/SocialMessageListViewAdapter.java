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
import com.igrs.tivic.phone.Bean.Social.SocialMessageBean;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Utils.LocationUtils;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;
import com.igrs.tivic.phone.widget.AsyncImageView;

public class SocialMessageListViewAdapter extends BaseAdapter {

	private ArrayList<SocialMessageBean> datalist;//放入adapter中的数据
	private Context context;

	private String TAG = "SocialMessageListViewAdapter";
	
	public SocialMessageListViewAdapter(Context context, ArrayList<SocialMessageBean> datalist){
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
			convertView = inflater.inflate(R.layout.social_message_listview_item, null);
			viewholder.usricon = (AsyncImageView)convertView.findViewById(R.id.icon_usr);
			viewholder.gender = (ImageView)convertView.findViewById(R.id.icon_gender);
			viewholder.updatetime = (TextView)convertView.findViewById(R.id.text_time);
			viewholder.messagecontent = (TextView)convertView.findViewById(R.id.text_content);
			viewholder.distance = (TextView)convertView.findViewById(R.id.text_distence);
			viewholder.name = (TextView)convertView.findViewById(R.id.text_name);
			viewholder.countnew = (TextView)convertView.findViewById(R.id.count_new);
			convertView.setTag(viewholder);
		}else {
			viewholder = (ViewHolder)convertView.getTag();
		}
		SocialMessageBean bean = datalist.get(position);
		//TODO
//		暂时服务器没这个字段,之后会加上
		String sendtime;
		if (bean.getTime().contains("-") || bean.getTime().contains(":"))
			sendtime = bean.getTime();
		else
			sendtime = Utils.formatToDate(bean.getTime());
		viewholder.updatetime.setText(sendtime);
		viewholder.messagecontent.setText(bean.getText());
		viewholder.name.setText(bean.getUsrinfo().getUserNickName());
		viewholder.distance.setText(LocationUtils.getDistance(context, TivicGlobal.getInstance().userRegisterBean.getLoactionBean(), bean.getUsrinfo().getUsrLocation()));
		if(bean.getUsrinfo().getUserGender() == 0)
		{
			viewholder.gender.setImageResource(R.drawable.ugc_gender_femaile);
			viewholder.usricon.setDefaultImageResource(R.drawable.base_default_avater);
		}else{
			viewholder.gender.setImageResource(R.drawable.ugc_gender_maile);
			viewholder.usricon.setDefaultImageResource(R.drawable.base_default_avater);
		}
		viewholder.usricon.setUrl(UIUtils.getUsrAvatar(bean.getPartner_id()));
		if(bean.getCountnew() > 0)
		{
			viewholder.countnew.setVisibility(View.VISIBLE);
			viewholder.countnew.setText(String.valueOf(bean.getCountnew()));
		}else{
			viewholder.countnew.setVisibility(View.GONE);
		}
		return convertView;
	}
	@Override
	public boolean isEnabled(int position) {
		return super.isEnabled(position);
	}
	
	private class ViewHolder{
		private AsyncImageView usricon;
		private ImageView gender;	
		private TextView name;
		private TextView distance;
		private TextView updatetime;
		private TextView messagecontent;
		private TextView countnew;
	}
}