package com.igrs.tivic.phone.Adapter;

import java.util.ArrayList;
import java.util.List;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.R.drawable;
import com.igrs.tivic.phone.Bean.Login.UserRegisterBean;
import com.igrs.tivic.phone.Bean.Social.SocialPoiBean;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.widget.AsyncImageView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SocialUserLabelAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<SocialPoiBean> beans;
	private int type;
	public static final int FILTER_TYPE = 1;
	public static final int OTHER_TYPE = 2;
	public static final String STAR = "star";
	public static final String GENDER = "gender";
	public static final String AGE = "age";
	private UserRegisterBean urb = TivicGlobal.getInstance().userRegisterBean;
	public SocialUserLabelAdapter(Context context,ArrayList<SocialPoiBean> beans,int type) {
		this.context = context;
		this.beans = beans;
		this.type = type;
	}
	@Override
	public int getCount() {
		return beans.size();
	}

	@Override
	public Object getItem(int position) {
		return beans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = View.inflate(context, R.layout.social_grid_view_item, null);
		AsyncImageView aiv = (AsyncImageView) view.findViewById(R.id.iv_user_label);
		aiv.setDefaultImageResource(R.drawable.base_subnail);
		ImageView iv = (ImageView) view.findViewById(R.id.iv_background);
		SocialPoiBean bean = beans.get(position);
		if(bean.getType()==SocialPoiBean.AGE_TYPE) {
			if(urb.getFilterItems().containsKey(AGE) && type==FILTER_TYPE) {
				iv.setImageResource(R.drawable.social_nianling_selected);
			} else {
				iv.setImageResource(R.drawable.social_nianling);
			}
			int rid = Integer.parseInt(bean.getUrl());
			aiv.setImageResource(rid);
		} else if(bean.getType()==SocialPoiBean.GENDER_TYPE) {
			if(urb.getFilterItems().containsKey(GENDER) && type==FILTER_TYPE) {
				iv.setImageResource(R.drawable.social_xingbie_selected);
			} else {
				iv.setImageResource(R.drawable.xingbie);
			}
			aiv.setImageResource(Integer.parseInt(bean.getUrl()));
		} else if(bean.getType()==SocialPoiBean.CONSTELLATION_TYPE) {
			if(urb.getFilterItems().containsKey(STAR) && type==FILTER_TYPE) {
				iv.setImageResource(R.drawable.social_xingzuo_selected);
			} else {
				iv.setImageResource(R.drawable.social_xingzuo);
			}
			aiv.setImageResource(Integer.parseInt(bean.getUrl()));
		} else if(bean.getType()==SocialPoiBean.STAR_TYPE){
			iv.setImageResource(R.drawable.social_star);
			for(SocialPoiBean socialPoiBean : urb.getFilterSocialPoiBeans()) {
				if(socialPoiBean.getValue() == bean.getValue() && type==FILTER_TYPE) {
					iv.setImageResource(R.drawable.social_star_selected);
				} 
			}
			aiv.setUrl(bean.getUrl());
		} else if(bean.getType()==SocialPoiBean.PROGRAM_TYPE) {
			iv.setImageResource(R.drawable.social_jiemu);
			for(SocialPoiBean socialPoiBean : urb.getFilterSocialPoiBeans()) {
				if(socialPoiBean.getValue() == bean.getValue() && type==FILTER_TYPE) {
					iv.setImageResource(R.drawable.social_jiemu_selected);
				} 
			}
			aiv.setUrl(bean.getUrl());
		}  else if(bean.getType()==SocialPoiBean.CATEGORY_TYPE) {//等待切图
			iv.setImageResource(R.drawable.social_neirong);
			for(SocialPoiBean socialPoiBean : urb.getFilterSocialPoiBeans()) {
				if(socialPoiBean.getValue() == bean.getValue() && type==FILTER_TYPE) {
					iv.setImageResource(R.drawable.social_neirong_selected);
				} 
			}
			aiv.setUrl(bean.getUrl());
		} else if(bean.getType()==SocialPoiBean.SINAWEIBO_TYPE) {
//			for(SocialPoiBean socialPoiBean : urb.getFilterSocialPoiBeans()) {
//				if(socialPoiBean.getValue() == bean.getValue() && type==FILTER_TYPE) {
//					
//				} else {
					iv.setImageResource(R.drawable.social_bangding);
//				}
//			}
			aiv.setImageResource(R.drawable.social_bangding_weibo);
		}
		return view;
	}

}
