package com.igrs.tivic.phone.Adapter;

import java.util.ArrayList;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Adapter.UGCPublishListViewAdapter2.ViewHolder;
import com.igrs.tivic.phone.Bean.PGC.ContentPublishBean;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Global.URLConfig;
import com.igrs.tivic.phone.Utils.LocationUtils;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;
import com.igrs.tivic.phone.ViewGroup.ContentsView;
import com.igrs.tivic.phone.ViewGroup.MyScrollViewGroup;
import com.igrs.tivic.phone.widget.AsyncImageView;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * listviewitem  点击 可追人
 * @author lq
 *
 */
public class ContentPublishListViewAdapter2 extends BaseAdapter{
	private Context mContext;
	private ArrayList<ContentPublishBean> datalist;
	private LayoutInflater inflater ;
	private SparseArray<View> vArray = new SparseArray<View>();
//	private ArrayList<SparseArray<View>> viewsList = new ArrayList<SparseArray<View>>();
	public ContentPublishListViewAdapter2(Context context, ArrayList<ContentPublishBean> _datalist){
		this.mContext = context;
		this.datalist = _datalist;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
//		final View view;
		final ViewHolder viewHolder;
		final ContentPublishBean bean = datalist.get(position);
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
			viewHolder.user_sign = (TextView) convertView.findViewById(R.id.text_user_sign);
			convertView.setTag(viewHolder);
			
//			vArray.put(position, view);
//			viewsList.add(position, vArray);
			
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		
		viewHolder.text_publish.setText(bean.getPublishText());	
		viewHolder.image_user.setDefaultImageResource(R.drawable.base_default_avater);

		if(bean.getUsrinfo().getUserGender() == 0)
		{
			viewHolder.image_gender.setImageResource(R.drawable.ugc_gender_femaile);
			viewHolder.image_user_1.setBackgroundResource(R.drawable.ugc_zhuiren_famale_online);
		}else{
			viewHolder.image_gender.setImageResource(R.drawable.ugc_gender_maile);
			viewHolder.image_user_1.setBackgroundResource(R.drawable.ugc_zhuiren_male_online);
		}
		viewHolder.text_name.setText(bean.getUsrinfo().getUserNickName());
		viewHolder.image_user.setAvaterUrl(UIUtils.getUsrAvatar(bean.getUsrinfo().getUid()));
		if(bean.getPublishImage() == null || bean.getPublishImage().length() < Const.imgHearder.length())
		{
			viewHolder.image_subnail.setVisibility(View.GONE);
		}
		else
		{
			viewHolder.image_subnail.setVisibility(View.VISIBLE);		
			viewHolder.image_subnail.setAvaterUrl(UIUtils.getImgSubnailUrl(bean.getPublishImage(), 64));
			viewHolder.image_subnail.setDefaultImageResource(R.drawable.base_subnail);
			viewHolder.image_subnail.setScaleType(ImageView.ScaleType.FIT_XY);

		}
//		final int pos = position;
		viewHolder.image_subnail.setOnClickListener(new OnClickListener() { // 点击放大
			public void onClick(View paramView) {
				Intent intent = new Intent();
				// TODO put Extra resId or imgUrl to BaseSubnailActivity
				intent.putExtra("imgUrl", bean.getPublishImage());
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setClassName("com.igrs.tivic.phone",
						"com.igrs.tivic.phone.Activity.BaseSubnailActivity");
				mContext.startActivity(intent);
			}
		});
		viewHolder.text_time.setText(String.valueOf(bean.getStartTime()));
		viewHolder.text_distance.setText(LocationUtils.getDistance(mContext, TivicGlobal.getInstance().userRegisterBean.getLoactionBean(), bean.getUsrinfo().getUsrLocation()));
		
		//显示用户签名,收看的节目
		if(bean.getProgramName() == null)
			viewHolder.current_watch.setText(mContext.getString(R.string.social_programe_title_empty));
		else
		{
			viewHolder.current_watch.setText(mContext.getString(R.string.social_programe_title) + bean.getProgramName());
		}
		viewHolder.user_sign.setText(bean.getSign());
		
		//click for zhui ren
		viewHolder.image_user.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				viewHolder.viewGroup.scrollMenuBar();
				if(bean.isVisitOpen()){
					bean.setVisitOpen(false);
				}else{
					bean.setVisitOpen(true);
				}
				
			}
		});
		
		if(!bean.isVisitOpen()){
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
				Log.d("UGCPublishListViewAdapter2", "kevin add : 三级页面追人 cid,pid,pname" + bean.getChannelid() +","+bean.getFoot()+"," + bean.getProgramName());
				if(bean.getFoot() != null && bean.getProgramName()!=null)
				{
					Intent intent = new Intent();
					intent.putExtra("program_id", bean.getFoot());
					intent.putExtra("program_title", bean.getProgramName());
					intent.putExtra("channel_id", bean.getChannelid());
					intent.putExtra("channel_name", Utils.getChannelNameById(mContext, Integer.parseInt(bean.getChannelid() != null ? bean.getChannelid(): "0")));
					intent.setClassName("com.igrs.tivic.phone", "com.igrs.tivic.phone.Activity.WaterfallActivity");
					UIUtils.setCurrentFuncID(FuncID.ID_WATERFALL);
					mContext.startActivity(intent);
//					UIUtils.Logd("lq", "bean.getChannelName() : "+ Utils.getChannelNameById(mContext, Integer.parseInt(bean.getChannelid() != null ? bean.getChannelid(): "0"))+"  channel_id : "+bean.getChannelid());
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
