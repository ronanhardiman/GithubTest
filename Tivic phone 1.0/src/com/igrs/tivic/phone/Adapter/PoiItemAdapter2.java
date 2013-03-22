package com.igrs.tivic.phone.Adapter;

import java.util.List;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Bean.Login.UserPOIItemBean;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.widget.AsyncImageView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PoiItemAdapter2 extends BaseAdapter{
	public static final int GALLERY_TYPE = 0;
	public static final int GRIDVIEW_TYPE = 1;
	private List<UserPOIItemBean> poiItemsBeans;
	private int type;
	private Context context;
//	private List<UserPOIItemBean> userPOIItemBeans = TivicGlobal.getInstance().userRegisterBean.getUserPoiItems();
	public PoiItemAdapter2(Context context,List<UserPOIItemBean> poiItemsBeans,int type) {
		this.context = context;
		this.poiItemsBeans = poiItemsBeans;
		this.type = type;
	}
	@Override
	public int getCount() {
		return poiItemsBeans.size();
	}

	@Override
	public Object getItem(int position) {
		return poiItemsBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		UserPOIItemBean poiItem = poiItemsBeans.get(position);
		ImageViewHolder ivh = null;
		TextViewHolder tvh = null;
		if(type==GALLERY_TYPE) {
			if(convertView==null) {
//				convertView = View.inflate(context, R.layout.image_item, null);
				convertView = View.inflate(context, R.layout.poi_image_item, null);
				ivh = new ImageViewHolder();
				ivh.iv_poiitem = (AsyncImageView) convertView.findViewById(R.id.iv_poiitem);
				ivh.iv_selected = (ImageView) convertView.findViewById(R.id.iv_selected);
				convertView.setTag(ivh);
			} else {
				ivh = (ImageViewHolder) convertView.getTag();
			}
			String imagePath = poiItem.getPoi_icon();
//			ivh.iv_poiitem.setImageResource(R.drawable.base_queshengtu4_3);
			ivh.iv_poiitem.setDefaultImageResource(R.drawable.base_queshengtu4_3);
			ivh.iv_poiitem.setUrl(imagePath);
			if(poiItem.isChecked()) {
					ivh.iv_selected.setVisibility(View.VISIBLE);
			} 
			return convertView;
		} else {
//			if(convertView == null) {
//				convertView = View.inflate(context, R.layout.text_item, null);
//				tvh = new TextViewHolder();
//				tvh.iv_selected = (ImageView) convertView.findViewById(R.id.iv_selected);
//				tvh.tv_poiitem = (TextView) convertView.findViewById(R.id.tv_poiitem);
//				convertView.setTag(tvh);
//			} else {
//				tvh = (TextViewHolder) convertView.getTag();
//			}
//			tvh.tv_poiitem.setText(poiItem.getPoi_text());
//			System.out.println("adapter里是否被选中"+poiItem.isChecked());
//			if(poiItem.isChecked()) {
//				tvh.iv_selected.setVisibility(View.VISIBLE);
//			}
//			return convertView;
			View view = View.inflate(context, R.layout.text_item, null);
			TextView tv = (TextView) view.findViewById(R.id.tv_poiitem);
			tv.setText(poiItem.getPoi_text());
			ImageView iv_selected = (ImageView) view.findViewById(R.id.iv_selected);
			if(poiItem.isChecked()) {
				iv_selected.setVisibility(View.VISIBLE);
			}
			return view;
		}
	}
	public List<UserPOIItemBean> getPoiItemsBeans() {
		return poiItemsBeans;
	}
	public void setPoiItemsBeans(List<UserPOIItemBean> poiItemsBeans) {
		this.poiItemsBeans = poiItemsBeans;
	}
	private static class ImageViewHolder{
		AsyncImageView iv_poiitem;
		ImageView iv_selected;
	}
	private static class TextViewHolder{
		TextView tv_poiitem;
		ImageView iv_selected;
	}
}
