package com.igrs.tivic.phone.Adapter;

import java.util.List;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Bean.LocationBean;
import com.igrs.tivic.phone.Bean.VisitBean;
import com.igrs.tivic.phone.Bean.Social.SocialFriendBean;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Utils.LocationUtils;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.widget.AsyncImageView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SocialMkFriendListAdapter extends BaseAdapter {
	private Context context;
	private List<SocialFriendBean> mkfBeans;
	private SocialFriendBean socialFriendBean;
	private LayoutInflater inflater;
	private TivicGlobal tivicGlobal;
	public SocialMkFriendListAdapter(Context context) {
		super();
		this.context = context;
		inflater = LayoutInflater.from(context);
		tivicGlobal = TivicGlobal.getInstance();
	}

	public void setMkfBeans(List<SocialFriendBean> mkfBeans) {
		this.mkfBeans = mkfBeans;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mkfBeans.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mkfBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		MKFriendHolder mFriendHolder = null;
		if(socialFriendBean == null){
			socialFriendBean = new SocialFriendBean();
		}
		if(convertView == null){
			mFriendHolder = new MKFriendHolder();
			convertView = inflater.inflate(R.layout.social_mkfriend_item, null);
			mFriendHolder.friend_icon = (AsyncImageView) convertView.findViewById(R.id.social_mkfriend_icon);
			mFriendHolder.friend_name = (TextView) convertView.findViewById(R.id.social_mkfriend_name);
			mFriendHolder.friend_distance = (TextView) convertView.findViewById(R.id.social_mkfriend_distance);
			mFriendHolder.iv_gender = (ImageView) convertView.findViewById(R.id.social_mkfriend_gender);
			mFriendHolder.friend_program = (TextView) convertView.findViewById(R.id.social_mkfriend_program);
			mFriendHolder.friend_trends = (TextView) convertView.findViewById(R.id.social_mkfriend_trends);
			convertView.setTag(mFriendHolder);
		}else{
			mFriendHolder = (MKFriendHolder) convertView.getTag();
		}
		//set res
		socialFriendBean = mkfBeans.get(position);
		mFriendHolder.friend_icon.setDefaultImageResource(R.drawable.base_default_avater);
		//set head
		String head_url = UIUtils.getUsrAvatar(socialFriendBean.getUserBean().getUid());
		if(head_url == "")
			mFriendHolder.friend_icon.setDefaultImageResource(R.drawable.base_default_avater);
		else
			mFriendHolder.friend_icon.setUrl(head_url);
		//set location 
		LocationBean location = tivicGlobal.userRegisterBean.getLoactionBean();
		LocationBean friend_location = socialFriendBean.getUserBean().getUsrLocation();
		String distance =LocationUtils.getDistance(context, location, friend_location);
		mFriendHolder.friend_distance.setText(distance);
		//set gender
		if(socialFriendBean.getUserBean().getUserGender() == 1){
			mFriendHolder.iv_gender.setImageResource(R.drawable.ugc_gender_maile);
		}else{
			mFriendHolder.iv_gender.setImageResource(R.drawable.ugc_gender_femaile);
		}
		
		//set program
		VisitBean friendVisit = socialFriendBean.getUserBean().getVisit();
		if(friendVisit!=null&&friendVisit.getVisit_ProgramTitle() != null)
			mFriendHolder.friend_program.setText(friendVisit.getVisit_ProgramTitle());
		
		mFriendHolder.friend_program.setText(socialFriendBean.getUserBean().getUserSign());
//		mFriendHolder.friend_distance.setText("1.3km");
		mFriendHolder.friend_name.setText(socialFriendBean.getUserBean().getUserNickName());
		mFriendHolder.friend_trends.setText("");
		return convertView;
	}
	class MKFriendHolder{
		AsyncImageView friend_icon;
		TextView friend_name;
		TextView friend_distance;
		TextView friend_program;
		TextView friend_trends;
		ImageView iv_gender;
	}
}
