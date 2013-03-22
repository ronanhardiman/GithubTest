package com.igrs.tivic.phone.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Bean.PGC.CollectBean;
import com.igrs.tivic.phone.widget.ImageTextView;
import com.igrs.tivic.phone.widget.SocialSaveTextItem;

public class SocialSaveListViewAdapter extends BaseAdapter {

	private ArrayList<CollectBean> datalist;// 放入adapter中的数据
	private Context context;
	private int listView_width,listView_height;
	
	private static final int TYPE_IMAGE = 0;
	private static final int TYPE_TEXT = 1;
	private static final int TYPE_COUNT = TYPE_TEXT+1;

	public SocialSaveListViewAdapter(Context context,ArrayList<CollectBean> datalist) {
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
		if (convertView == null) {
			listView_width = parent.getWidth();
			listView_height = listView_width*3/4;

			convertView = createItemView(position);
		} 
		setDataAndLayoutParam(convertView,position);
		return convertView;
	}
	private View createItemView(int position){
		View view = getItemViewType(position) == TYPE_IMAGE ? new ImageTextView(context,null):
			     new SocialSaveTextItem(context,null);
		view.setBackgroundResource(R.drawable.waterfall_textview_bg);
		return view;
	}

	private void setDataAndLayoutParam(View view,int position){
		
		AbsListView.LayoutParams llp = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT,listView_height);
//		llp.setMargins(5,5,5,0);
		
		view.setLayoutParams(llp);
		view.setPadding(5,5,5,5);
		String mt = datalist.get(position).getArticle_mediaType();
		if("text".equals(mt)){
			SocialSaveTextItem text = (SocialSaveTextItem)view;
			text.setMaskVisiable(View.VISIBLE);
			text.setMaskColor(0xaa000000);
			text.setLeftText(datalist.get(position).getArticle_title());
			text.setData(datalist.get(position).getArticle_title(),"\u3000\u3000"+datalist.get(position).getArticle_summary());
		}else{
			ImageTextView iv = (ImageTextView)view;
			iv.setMaskColor(0xaa000000);
			iv.setLeftText(datalist.get(position).getArticle_title());
			if("video".equals(mt)){
				iv.setImageUrl(datalist.get(position).getVideoImage()+"!w256");
				iv.getVideo_icon().setVisibility(View.VISIBLE);
			}else if("image".equals(mt)){
				iv.setImageUrl(datalist.get(position).getArticle_mediaUrl()+"!w256");
				iv.getVideo_icon().setVisibility(View.GONE);
			}
		}
	}
	@Override
	public int getItemViewType(int position) {
		String mt = datalist.get(position).getArticle_mediaType();
		if("video".equals(mt) || "image".equals(mt)){
			return TYPE_IMAGE;
		}else{
			return TYPE_TEXT;
		}
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_COUNT;
	}
}