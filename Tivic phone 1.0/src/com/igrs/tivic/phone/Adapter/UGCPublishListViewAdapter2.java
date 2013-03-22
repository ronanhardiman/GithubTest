package com.igrs.tivic.phone.Adapter;

import java.util.ArrayList;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Adapter.ContentPublishListViewAdapter2.ViewHolder;
import com.igrs.tivic.phone.Bean.UGC.UGCReplyBean;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Global.URLConfig;
import com.igrs.tivic.phone.Utils.LocationUtils;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.ViewGroup.MyScrollViewGroup;
import com.igrs.tivic.phone.widget.AsyncImageView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UGCPublishListViewAdapter2 extends BaseAdapter{
	private Context mContext;
	private LayoutInflater inflater ;
	ArrayList<UGCReplyBean> datalist;
	final String TAG = "UGCPublishListViewAdapter2";
//	private SparseArray<View> vArray = new SparseArray<View>();
//	private ArrayList<SparseArray<View>> viewsList = new ArrayList<SparseArray<View>>();
	
	public UGCPublishListViewAdapter2(Context context,ArrayList<UGCReplyBean> data){
		this.mContext = context;
		this.datalist = data;
		inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		if(datalist != null)		
			return datalist.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
//		final View view;
		final ViewHolder viewHolder;
		final UGCReplyBean bean = datalist.get(position);
//		if(vArray.get(position) == null){
		if(convertView == null){
			convertView = inflater.inflate(R.layout.base_publish_item_zhuiren, null);
			viewHolder = new ViewHolder();
			viewHolder.viewGroup = (MyScrollViewGroup) convertView.findViewById(R.id.viewgroup);
			viewHolder.image_user = (AsyncImageView) convertView.findViewById(R.id.icon_usr);
			viewHolder.text_publish = (TextView) convertView.findViewById(R.id.text_content);
			viewHolder.text_name = (TextView) convertView.findViewById(R.id.text_name);
			viewHolder.text_distance = (TextView) convertView.findViewById(R.id.text_distence);
			viewHolder.text_time = (TextView) convertView.findViewById(R.id.text_time);
			viewHolder.image_gender = (ImageView)convertView.findViewById(R.id.icon_gender);
			viewHolder.image_subnail = (AsyncImageView)convertView.findViewById(R.id.icon_subnail);
			viewHolder.image_user_1 = (ImageView) convertView.findViewById(R.id.icon_usr_1);
			viewHolder.current_watch = (TextView) convertView.findViewById(R.id.text_current_watch);
			viewHolder.user_sign = (TextView)convertView.findViewById(R.id.text_user_sign);
			convertView.setTag(viewHolder);			
//			vArray.put(position, view);
//			viewsList.add(position, vArray);
		}else{
//			view = vArray.get(position);
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.text_publish.setText("       " + bean.getReplyText());	
		viewHolder.text_publish.setTypeface(Typeface.DEFAULT,Typeface.NORMAL);
		if(bean.getUsrinfo().getUserGender() == 0)
		{
			viewHolder.image_gender.setImageResource(R.drawable.ugc_gender_femaile);
			viewHolder.image_user.setDefaultImageResource(R.drawable.base_default_avater);
			viewHolder.image_user_1.setBackgroundResource(R.drawable.ugc_zhuiren_famale_online);
		}else{
			viewHolder.image_gender.setImageResource(R.drawable.ugc_gender_maile);
			viewHolder.image_user.setDefaultImageResource(R.drawable.base_default_avater);
			viewHolder.image_user_1.setBackgroundResource(R.drawable.ugc_zhuiren_male_online);
		}
		viewHolder.text_name.setText(bean.getUsrinfo().getUserNickName());
		viewHolder.image_user.setAvaterUrl(UIUtils.getUsrAvatar(bean.getUsrinfo().getUid()));
		if(bean.getProgram_name() == null)
			viewHolder.current_watch.setText(mContext.getString(R.string.social_programe_title_empty));
		else
		{
			viewHolder.current_watch.setText(mContext.getString(R.string.social_programe_title) + bean.getProgram_name());
		}
		if(bean.getImgUrl() == null || bean.getImgUrl().length() < Const.imgHearder.length())
		{
			viewHolder.image_subnail.setVisibility(View.GONE);
		}
		else
		{
			viewHolder.image_subnail.setVisibility(View.VISIBLE);		
			viewHolder.image_subnail.setUrl(UIUtils.getImgSubnailUrl(bean.getImgUrl(), 64));
			viewHolder.image_subnail.setDefaultImageResource(R.drawable.base_subnail);
			viewHolder.image_subnail.setScaleType(ImageView.ScaleType.FIT_XY);

		}

		viewHolder.image_subnail.setOnClickListener(new OnClickListener() { // 点击放大
			public void onClick(View paramView) {
				Intent intent = new Intent();
				// TODO put Extra resId or imgUrl to BaseSubnailActivity
				intent.putExtra("imgUrl", bean.getImgUrl());
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setClassName("com.igrs.tivic.phone",
						"com.igrs.tivic.phone.Activity.BaseSubnailActivity");
				mContext.startActivity(intent);
			}
		});
		viewHolder.text_time.setText(String.valueOf(bean.getTime()));
		viewHolder.text_distance.setText(LocationUtils.getDistance(mContext, TivicGlobal.getInstance().userRegisterBean.getLoactionBean(), bean.getUsrinfo().getUsrLocation()));
		
		viewHolder.user_sign.setText(bean.getSign());
		//click for zhui ren
		viewHolder.image_user.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				viewHolder.viewGroup.scrollMenuBar();
				if(bean.isVisitOpen())
					bean.setVisitOpen(false);
				else
					bean.setVisitOpen(true);
			}
		});
		if(!bean.isVisitOpen())
		{
			viewHolder.viewGroup.initState();
		}
		//click for detail
		viewHolder.image_user_1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("uid", bean.getUsrinfo().getUid());
				intent.setClassName("com.igrs.tivic.phone", "com.igrs.tivic.phone.Activity.SocialBaseActivity");
				UIUtils.setCurrentFuncID(FuncID.ID_SOCIAL_FRIENDS_DETAIL);
				mContext.startActivity(intent);
			}
		});
		viewHolder.current_watch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "kevin add : 贴吧评论追人 cid,pid,pname" + bean.getChannelId() +","+bean.getProgram_id()+"," + bean.getProgram_name());
//				if(bean.getChannelId() != 0 && bean.getProgram_id() != 0 && bean.getProgram_name()!=null)
				if(bean.getProgram_id() != 0 && bean.getProgram_name()!=null)
				{	Intent intent = new Intent();
					intent.putExtra("program_id", String.valueOf(bean.getProgram_id()));
					intent.putExtra("program_title", bean.getProgram_name());
					intent.putExtra("channel_id", String.valueOf(bean.getChannelId()));
//					intent.putExtra("channel_name", bean.getUsrinfo().getVisit().getVisit_ChannelName());
					intent.setClassName("com.igrs.tivic.phone", "com.igrs.tivic.phone.Activity.WaterfallActivity");
					UIUtils.setCurrentFuncID(FuncID.ID_WATERFALL);
					mContext.startActivity(intent);
				}else{
					UIUtils.ToastMessage(mContext, mContext.getString(R.string.visit_frined_failed));
				}
			}
		});
		
		return convertView;
	}
	static class ViewHolder{
		AsyncImageView image_user;
		ImageView image_user_1;
		ImageView image_gender;
		AsyncImageView image_subnail;
		TextView text_publish;
		TextView text_publish_1;
		TextView text_name;
		TextView text_distance;
		TextView text_time;
		TextView current_watch;
		TextView user_sign; 
		MyScrollViewGroup viewGroup;
	}

}
