package com.igrs.tivic.phone.Adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Activity.UGCActivity;
import com.igrs.tivic.phone.Activity.UGCPublishActivity;
import com.igrs.tivic.phone.Bean.UGC.UGCDataBean;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Listener.UGCItemClickListener;
import com.igrs.tivic.phone.Utils.LocationUtils;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.widget.AsyncImageView;

public class UGCListViewAdapter extends BaseAdapter {

	private List<UGCDataBean> data;//放入adapter中的数据
	private Context context;
	private String TAG = "UGCListViewAdapter";
	private UGCItemClickListener listener;
	
	
	public UGCListViewAdapter(Context context, List<UGCDataBean> data){
		this.context = context;
		this.data = data;
		
	}
	@Override
	public int getCount() {
		return data.size();
	}
	@Override
	public Object getItem(int position) {
		return data.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void setOnItemClickListener(UGCItemClickListener listener)
	{
		this.listener = listener;
	}
	
	ViewHolder viewholder = null;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		viewholder = null;
		final int pos = position;
		final UGCDataBean currentbean = data.get(position);
		if(convertView == null)
		{
			convertView = inflater.inflate(R.layout.ugc_list_item_single, null);
			viewholder = new ViewHolder();
			viewholder.item = (LinearLayout)convertView.findViewById(R.id.item_main);
			viewholder.usrHead = (AsyncImageView)convertView.findViewById(R.id.icon_usr);
			viewholder.top = (ImageView)convertView.findViewById(R.id.id_icon_top);
			viewholder.gender = (ImageView)convertView.findViewById(R.id.icon_gender);
			viewholder.subnail = (AsyncImageView)convertView.findViewById(R.id.subimg);
			viewholder.distance = (TextView)convertView.findViewById(R.id.text_distence);
			viewholder.name = (TextView)convertView.findViewById(R.id.text_name);
			viewholder.title = (TextView)convertView.findViewById(R.id.id_text_title);
			viewholder.content = (TextView)convertView.findViewById(R.id.id_text_content);
			viewholder.republish = (TextView)convertView.findViewById(R.id.id_text_reply);
			viewholder.support = (TextView)convertView.findViewById(R.id.id_text_support);
			viewholder.icon_up = (ImageView)convertView.findViewById(R.id.icon_up);
			viewholder.icon_reply = (ImageView)convertView.findViewById(R.id.icon_reply);
			viewholder.support_anim = (TextView)convertView.findViewById(R.id.id_one);
			convertView.setTag(viewholder);
		}else{
			viewholder = (ViewHolder)convertView.getTag();
		}

		
		if(currentbean.getUsrinfo().getUserGender() == 0)
		{
			viewholder.gender.setImageResource(R.drawable.ugc_gender_femaile);
			viewholder.usrHead.setDefaultImageResource(R.drawable.base_default_avater);
		}else{
			viewholder.gender.setImageResource(R.drawable.ugc_gender_maile);
			viewholder.usrHead.setDefaultImageResource(R.drawable.base_default_avater);
		}
		viewholder.usrHead.setAvaterUrl(UIUtils.getUsrAvatar(currentbean.getUsrinfo().getUid()));
		if(currentbean.getImageUrl() != null && currentbean.getImageUrl().length() > 0)
		{
			viewholder.subnail.setVisibility(View.VISIBLE);
			viewholder.subnail.setDefaultImageResource(R.drawable.base_subnail);
				if(currentbean.getOfficial() == 1)
					viewholder.subnail.setUrl(currentbean.getImageUrl(), 4);
				else
					viewholder.subnail.setUrl(UIUtils.getImgSubnailUrl(currentbean.getImageUrl(), 128), 1);
				viewholder.subnail.setScaleType(ImageView.ScaleType.FIT_XY);
				
		}else{
			viewholder.subnail.setVisibility(View.GONE);
		}
		if(currentbean.getIsTop() == 1)
		{
			viewholder.top.setVisibility(View.VISIBLE);
		}else{
			viewholder.top.setVisibility(View.GONE);	
		}
		viewholder.distance.setText(LocationUtils.getDistance(context, TivicGlobal.getInstance().userRegisterBean.getLoactionBean(), currentbean.getUsrinfo().getUsrLocation()));
		viewholder.title.setText(currentbean.getTitle());
		viewholder.name.setText(currentbean.getUsrinfo().getUserNickName());
		viewholder.content.setText("       " +currentbean.getContent());
		viewholder.content.setTypeface(Typeface.DEFAULT,Typeface.NORMAL);
		String repltext = "("+String.valueOf(currentbean.getReplyContent())+")";
		viewholder.republish.setText(repltext);
		String uptext = "("+String.valueOf(currentbean.getUpCount())+")";
		viewholder.support.setText(uptext);
		
		viewholder.republish.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view)
			{
				listener.onReplyClicked(currentbean, pos);
			}
		});
		viewholder.icon_reply.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view)
			{
				listener.onReplyClicked(currentbean, pos);		
			}
		});	
		viewholder.icon_up.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view)
			{
				if(currentbean.getIsUp() == 0)
				{
					UIUtils.ToastMessage(context, context.getString(R.string.ugc_supportAlready));
				}else{
					listener.onUpClicked(currentbean, pos);
					int upcount = currentbean.getUpCount() + 1;
					currentbean.setUpCount(upcount);
					currentbean.setIsUp(0);
					notifyDataSetChanged();
					UIUtils.ToastMessage(context, context.getString(R.string.ugc_support));
				}
			}
		});	
		viewholder.support.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view)
			{
				if(currentbean.getIsUp() == 0)
				{
					UIUtils.ToastMessage(context, context.getString(R.string.ugc_supportAlready));
				}else{
					listener.onUpClicked(currentbean, pos);
					int upcount = currentbean.getUpCount() + 1;
					currentbean.setUpCount(upcount);
					currentbean.setIsUp(0);
					notifyDataSetChanged();
				}
			}
		});
		
		viewholder.item.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(context,
						UGCPublishActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				UIUtils.setCurrentFuncID(FuncID.ID_PUBLISH);
				//用于区别打开帖子详情时是由节目进入还是由社交自己的帖子进入
				//FuncID.ID_UGC表示是由节目进入 FunID.ID_SOCIAL_PUBLISH表示由社交进入
				intent.putExtra("funcid", FuncID.ID_UGC); 
				intent.putExtra("tid", currentbean.getTid());
				intent.putExtra("listcount", data.size());
				//Log.i(TAG, "kevin add: tid = " + currentbean.getTid());
				context.startActivity(intent);
			}
		});
		return convertView;
	}
	@Override
	public boolean isEnabled(int position) {
		return super.isEnabled(position);
	}
	
	public void startAnim()
	{
		viewholder.support_anim.setVisibility(View.VISIBLE);
		Animation animation = AnimationUtils.loadAnimation(context, R.anim.add_one);
		viewholder.support_anim.startAnimation(animation);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				viewholder.support_anim.setVisibility(View.GONE);
			}
		}, 1000);
	}
	
	class ViewHolder{
		LinearLayout item;
		AsyncImageView usrHead;
		ImageView gender;
		AsyncImageView subnail;
		ImageView top;
		ImageView icon_up;
		ImageView icon_reply;
		TextView name;
		TextView distance;
		TextView title;
		TextView content;	
		TextView republish;
		TextView support;
		TextView support_anim;
	}
}