package com.igrs.tivic.phone.Adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Bean.LocationBean;
import com.igrs.tivic.phone.Bean.VisitBean;
import com.igrs.tivic.phone.Bean.Social.SocialFriendBean;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Utils.LocationUtils;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.widget.AsyncImageView;

import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SocialFriendAdapter extends BaseExpandableListAdapter {
	private Context context;
	private LayoutInflater inflater;
//	private String[] frinedTypes = {"我的好友","陌生人","黑名单"};
	private SocialFriendBean socialFriendBean;
	private Map<Integer, List<SocialFriendBean>> friendsTypeMap = new HashMap<Integer, List<SocialFriendBean>>();
	private Map<Integer, String> friendsOnline = new HashMap<Integer, String>();
	public SocialFriendAdapter(Context context) {
		super();
		this.context = context;
		inflater = LayoutInflater.from(context);
	}
	//friend type map
	public void setFriendsTypeMap(
			Map<Integer, List<SocialFriendBean>> friendsTypeMap) {
		this.friendsTypeMap = friendsTypeMap;
	}
	//friend type online map
	public void setFriendsOnline(Map<Integer, String> friendsOnline) {
		this.friendsOnline = friendsOnline;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return friendsTypeMap.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		FriendHodler friendHodler = null; 
		if(socialFriendBean == null)
			socialFriendBean = new SocialFriendBean();
		if (convertView == null){
			convertView = inflater.inflate(R.layout.social_friends_childs, null);
			friendHodler = new FriendHodler();
			friendHodler.friend_name = (TextView) convertView.findViewById(R.id.social_friend_name);
			friendHodler.friend_head = (AsyncImageView) convertView.findViewById(R.id.social_friend_icon);
			friendHodler.head_cover = (ImageView) convertView.findViewById(R.id.head_cover);
			friendHodler.iv_gender = (ImageView) convertView.findViewById(R.id.social_friend_gender);
			friendHodler.friend_distance = (TextView) convertView.findViewById(R.id.social_friend_distance);
			friendHodler.friend_program = (TextView) convertView.findViewById(R.id.social_friend_program);
			friendHodler.friend_trends = (TextView) convertView.findViewById(R.id.social_friend_trends);
			convertView.setTag(friendHodler);
		}else{
			friendHodler = (FriendHodler) convertView.getTag();
		}
		//set res
		socialFriendBean = friendsTypeMap.get(groupPosition).get(childPosition);
		friendHodler.friend_head.setDefaultImageResource(R.drawable.base_default_avater);
		//set head
		String head_url = UIUtils.getUsrAvatar(socialFriendBean.getUserBean()
				.getUid());
		if (head_url == ""){
//			friendHodler.friend_head.setDefaultImageResource(R.drawable.base_default_avater);
			/*if(!socialFriendBean.isOnline())
				friendHodler.head_cover.setVisibility(View.VISIBLE);
			else
				friendHodler.head_cover.setVisibility(View.GONE);*/
		}else{
			friendHodler.friend_head.setUrl(head_url);
			/*if(!socialFriendBean.isOnline())
				friendHodler.head_cover.setVisibility(View.VISIBLE);
			else
				friendHodler.head_cover.setVisibility(View.GONE);*/
		}
		// set location
		LocationBean location = TivicGlobal.getInstance().userRegisterBean.getLoactionBean();
		LocationBean friend_location = socialFriendBean.getUserBean()
				.getUsrLocation();
		String distance = LocationUtils.getDistance(context, location, friend_location);
		friendHodler.friend_distance.setText(distance);
		friendHodler.friend_name.setText(socialFriendBean.getUserBean().getUserNickName());
		// set program
		if(socialFriendBean.getUserBean().getVisit() != null){
			VisitBean friendVisit = socialFriendBean.getUserBean().getVisit();
			friendHodler.friend_program.setText(context.getString(R.string.social_programe_title) + friendVisit.getVisit_ProgramTitle());
		}
		//这个字段目前服务器没有，空缺
		friendHodler.friend_trends.setText("");
		// set gender
		if(socialFriendBean.getUserBean().getUserGender() == 1){
			friendHodler.iv_gender.setImageResource(R.drawable.ugc_gender_maile);
		}else{
			friendHodler.iv_gender.setImageResource(R.drawable.ugc_gender_femaile);
		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return friendsTypeMap.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
//		return frinedTypes[groupPosition];
		return friendsOnline.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
//		return frinedTypes.length;
		return friendsOnline.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.social_friends_groups, null);
		TextView tv = (TextView) convertView.findViewById(R.id.tv_group);
		tv.setText(friendsOnline.get(groupPosition));
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
	class FriendHodler{
		AsyncImageView friend_head;
		ImageView head_cover;
		ImageView iv_gender;
		TextView friend_name;
		TextView friend_distance;
		TextView friend_program;
		TextView friend_trends;
	}
}
