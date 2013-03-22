package com.igrs.tivic.phone.Adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Activity.PushTVActivity;
import com.igrs.tivic.phone.Bean.PGC.ProgramInfoBean;
import com.igrs.tivic.phone.Bean.PGC.PushTVBean;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Impl.PushTvImpl;
import com.igrs.tivic.phone.Utils.Utils;
import com.igrs.tivic.phone.widget.CustomTextView;
import com.igrs.tivic.phone.widget.CustomTextView.FocusImageOnClickListener;
import com.igrs.tivic.phone.widget.ImageTextView;

public class PushTvListdapter extends BaseAdapter {

	private List<ProgramInfoBean> data;//放入adapter中的数据
	private LayoutInflater inflater; 
	public static final int L_IMAGE_R_TEXT = 0;//item的布局类型   左图右文
	public static final int L_TEXT_R_IMAGE = L_IMAGE_R_TEXT+1;//左文右图
	private static final int TYPE_COUNT = L_TEXT_R_IMAGE+1;
	private Context context;
	
	private Handler isFoucsHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.arg1 == PushTvImpl.DO_SUCCESS){
				notifyDataSetChanged();//更新列表
			}else{
				Toast.makeText(context,R.string.focus_fail,Toast.LENGTH_LONG).show();
			}
		}
	};
	
	public PushTvListdapter(Context context,List<ProgramInfoBean> data){
		this.data = data;
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder vh=null;
		if(convertView == null){
			if(getItemViewType(position) == L_IMAGE_R_TEXT){
				convertView = inflater.inflate(R.layout.pushtv_list_item_1, null);
			}else{
				convertView = inflater.inflate(R.layout.pushtv_list_item_2, null);
			}
			vh = new ViewHolder();
			vh.itv = (ImageTextView)convertView.findViewById(R.id.itv_left);
			vh.ctv = (CustomTextView)convertView.findViewById(R.id.ctv_left);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder)convertView.getTag();
		}
		AbsListView.LayoutParams alp = new AbsListView.LayoutParams(parent.getWidth(),3*parent.getWidth()/8);
		convertView.setPadding(5,5,5,9);
		convertView.setLayoutParams(alp);
//		vh.ctv.setLayoutParams(new LinearLayout.LayoutParams(parent.getWidth()/2,parent.getHeight()/3));
//		vh.itv.setLayoutParams(new LinearLayout.LayoutParams(parent.getWidth()/2,parent.getHeight()/3));
		
//		if(data.get(position).getChannel_name()!=null && Utils.setChannelIcon(context,data.get(position).getChannel_name())!=0){
//			vh.ctv.setMaskLeftIcon(Utils.setChannelIcon(context,data.get(position).getChannel_name()));
//		}
		vh.ctv.setFocusButtonOrientation(getItemViewType(position));
		vh.itv.setImageUrl(data.get(position).getImgUrl()+"!w128");
		Log.e("aaa","==========ImageUrl=========="+data.get(position).getChannelid()+","+data.get(position).getProgramid());
		vh.ctv.setFocusImageViewState(data.get(position).getIsFocus());//初始化 “收藏”状态
		vh.ctv.setContent(data.get(position).getName(),
							data.get(position).getTime(),
							data.get(position).getIntrduce(),
							data.get(position).getKeys());
		vh.ctv.setMaskLeftIcon(Utils.setChannelIconSmall(context, Utils.getChannelNameById(context, data.get(position).getChannelid())));
		
		
		vh.ctv.setFocusImageOnClickListener(new FocusImageOnClickListener() {
			@Override
			public void onClick(int state) {
				isLogon();
				PushTvImpl.getInstance().setIsFoucsHandler(isFoucsHandler);
				if(state == CustomTextView.STATE_UNFOCUS){//未收藏
					PushTvImpl.getInstance().focusProgram(data.get(position));
				}else{//已收藏
					PushTvImpl.getInstance().removeFocus(data.get(position));
				}
			}
		});
		return convertView;
	}
	private void isLogon(){
		if (!TivicGlobal.getInstance().mIsLogin) {
			Intent intent = new Intent();
			intent.setClassName("com.igrs.tivic.phone",
					"com.igrs.tivic.phone.Activity.LoginActivity");
			context.startActivity(intent);
			((PushTVActivity)context).finish();
			return;
		}
	}
	class ViewHolder{
		ImageTextView itv;
		CustomTextView ctv;
	}
	@Override//控制哪项Item不可用（也就是单击，长按事件失效）
	public boolean isEnabled(int position) {
		return super.isEnabled(position);
	}
	@Override
	public int getItemViewType(int position) {
		int result = (position+1)%2;
		if(result == 1){
			return L_IMAGE_R_TEXT;
		}else{
			return L_TEXT_R_IMAGE;
		}
	}
	
	@Override
	public int getViewTypeCount() {
		return TYPE_COUNT;
	}
}