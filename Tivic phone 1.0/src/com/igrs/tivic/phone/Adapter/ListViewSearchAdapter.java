package com.igrs.tivic.phone.Adapter;

import java.util.ArrayList;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.R.color;
import com.igrs.tivic.phone.Activity.SearchActivity;
import com.igrs.tivic.phone.Bean.EPG.EPGProgramsBean;
import com.igrs.tivic.phone.Bean.PGC.ProgramInfoBean;
import com.igrs.tivic.phone.Utils.ModelUtil;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;
import com.igrs.tivic.phone.widget.AsyncImageView;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListViewSearchAdapter extends BaseAdapter{
	private Context mContext;
	private ArrayList<EPGProgramsBean> lvSearchData;
	private int searchListitemSource;
	private LayoutInflater mInflater;
	static class LvItemView{
		public TextView title;
		public TextView detail; 
		public AsyncImageView image;
	    public TextView program;
	    public TextView date;  
	    public LinearLayout layout;
	    public ImageView icon;
	}
	public ListViewSearchAdapter(Context mContext,
			/*ArrayList<EPGProgramsBean> lvSearchData,*/ int searchListitem) {
		this.mContext = mContext;
//		this.lvSearchData = lvSearchData;
		this.searchListitemSource = searchListitem;
		mInflater = LayoutInflater.from(mContext);
//		listCount = lvSearchData == null ? 0 : lvSearchData.size();
	}
	@Override
	public int getCount() {
		return lvSearchData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LvItemView lvItemView = null;
		if(convertView == null){
			convertView = mInflater.inflate(searchListitemSource, null);
			lvItemView = new LvItemView();
			lvItemView.image = (AsyncImageView) convertView.findViewById(R.id.search_item_image);
			lvItemView.title = (TextView) convertView.findViewById(R.id.search_item_title);
			lvItemView.program = (TextView) convertView.findViewById(R.id.search_item_program);
			lvItemView.detail = (TextView) convertView.findViewById(R.id.search_item_detail);
			lvItemView.layout = (LinearLayout) convertView.findViewById(R.id.search_listitem_ll);
			lvItemView.date = (TextView) convertView.findViewById(R.id.search_item_date);
			lvItemView.icon = (ImageView) convertView.findViewById(R.id.channel_icon);
			convertView.setTag(lvItemView);
		}else{
			lvItemView = (LvItemView) convertView.getTag();
		}
		EPGProgramsBean pBean = lvSearchData.get(position);
		UIUtils.Logd("lq", "name: "+pBean.getProgram_name()+" time : "+pBean.getStart_display()+" detail : "+pBean.getPlay_period()
				+" channelid : "+pBean.getChannel_id()+" img: "+pBean.getUrl_p43());
		if(TextUtils.isEmpty(pBean.getProgram_name())){
			pBean.setProgram_name("");
		}
		if(pBean.isFromEditer()){
			lvItemView.layout.setVisibility(LinearLayout.GONE);
			lvItemView.image.setVisibility(View.GONE);
			lvItemView.title.setText(pBean.getProgram_name());
		}else{
			lvItemView.title.setVisibility(View.GONE);
			lvItemView.image.setVisibility(View.VISIBLE);
			lvItemView.layout.setVisibility(View.VISIBLE);
//			lvItemView.icon.setBackgroundResource(resid);
			lvItemView.program.setText(pBean.getProgram_name());
			lvItemView.image.setDefaultImageResource(R.drawable.base_subnail);
			lvItemView.image.setUrl(pBean.getUrl_p43());
			lvItemView.detail.setText(pBean.getPlay_period());
			lvItemView.date.setText(pBean.getStart_display());
			int icon_id = Utils.setChannelIconSmall(mContext, Utils.getChannelNameById(mContext, pBean.getChannel_id()));
			lvItemView.icon.setBackgroundResource(icon_id);
		}
		
//		if(SearchActivity.fromEditer){//默认显示的搜索结果
//			lvItemView.layout.setVisibility(LinearLayout.GONE);
//			lvItemView.image.setVisibility(View.GONE);
//			
//			lvItemView.title.setText(pBean.getProgram_name());
//		}else{//用户搜索的结果
//			lvItemView.title.setVisibility(View.GONE);
//			lvItemView.image.setVisibility(View.VISIBLE);
//			lvItemView.layout.setVisibility(View.VISIBLE);
//			
//			lvItemView.program.setText(pBean.getProgram_name());
//			lvItemView.image.setUrl(pBean.getUrl_p43());
//			lvItemView.detail.setText(pBean.getPlay_period());
//			lvItemView.date.setText(pBean.getStart_display());
//		}
		convertView.setContentDescription(pBean.getProgram_name());
		return convertView;
	}
	public void setListData(ArrayList<EPGProgramsBean> lvSearchData) {
		this.lvSearchData = lvSearchData;
	}

}
