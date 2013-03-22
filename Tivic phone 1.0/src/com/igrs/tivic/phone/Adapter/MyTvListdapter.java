package com.igrs.tivic.phone.Adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Bean.BaseResponseBean;
import com.igrs.tivic.phone.Bean.EPG.EPGProgramsBean;
import com.igrs.tivic.phone.Impl.MyTvImpl;
import com.igrs.tivic.phone.Utils.Utils;
import com.igrs.tivic.phone.widget.ImageTextView;

public class MyTvListdapter extends BaseAdapter {

	private List<EPGProgramsBean> datas;//放入adapter中的数据

	private LayoutInflater inflater;
	private Context context;
	private OnDeleteItemListener onDeleteItemListener;
	private int listView_width;
	
	private Handler delHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			Log.e("aaa","========================"+msg.arg1);
			if(msg.arg1==1){
				if(onDeleteItemListener!=null){
					onDeleteItemListener.onDeleteItem(datas.get(msg.what));//作用是 更新所有收藏列表
				}
//				delDiffTimeProgramByName(datas.get(msg.what),datas);//what的值是 要删除的position
//				notifyDataSetChanged();
				Toast.makeText(context,R.string.remove_success, Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(context,R.string.remove_fail, Toast.LENGTH_SHORT).show();
			}
		}
	};
	//跟据节目名删除列表中所有对应此节目名的节目（一个节目可以在不同时间播放）
//	public void delDiffTimeProgramByName(EPGProgramsBean deletedProgram,List<EPGProgramsBean> programs){
//		if(deletedProgram==null || programs==null) return;
//		ArrayList<EPGProgramsBean> deletedPrograms = new ArrayList<EPGProgramsBean>();
//		for(EPGProgramsBean program : programs){
//			if(program.getProgram_name().equals(deletedProgram.getProgram_name())){
//				deletedPrograms.add(program);
//			}
//		}
//		programs.removeAll(deletedPrograms);
//	}
	
	public MyTvListdapter(Context context,List<EPGProgramsBean> data){
		this.datas = data;
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		return datas.size();
	}
	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if(convertView==null){
			convertView = inflater.inflate(R.layout.mytv_list_item, null);
			listView_width = parent.getWidth();
			vh = new ViewHolder();
			vh.itv = (ImageTextView)convertView.findViewById(R.id.mytv_itv);
			vh.itv.setBackGroundImageView(R.drawable.waterfall_textview_bg);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder)convertView.getTag();
		}
//		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(parent.getWidth(),parent.getHeight()/3);
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,listView_width*3/8);
		llp.setMargins(5,5,5,0);
		vh.itv.setLayoutParams(llp);
		vh.itv.setMaskColor(0xaa000000);
		
		String sd = "";
		if(datas.get(position).getStart_display()!=null){
			sd = datas.get(position).getStart_display();
		}
		vh.itv.setLeftText(datas.get(position).getProgram_name()+" "+sd);
		
		if(datas.get(position).getChannel_name()!=null && Utils.setChannelIcon(context,datas.get(position).getChannel_name())!=0){
			vh.itv.setMaskLeftIcon(Utils.setChannelIcon(context,datas.get(position).getChannel_name()));
		}
		
		vh.itv.setImageUrl(datas.get(position).getUrl_p83()+"!w256");
		
		//设置“删除icon”是否显示
		if(datas.get(position).isShowDelFlag()){
			vh.itv.setDeleteIconVisible(View.VISIBLE);
		}else{
			vh.itv.setDeleteIconVisible(View.GONE);
		}
		
		final int currentPosition = position;
		
		vh.itv.getRight_icon_delete().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.e("aaa","=============delete====onClick==========");
				new Thread(new Runnable(){
					@Override
					public void run() {
						BaseResponseBean brb = MyTvImpl.getInstance().removeFocus(datas.get(currentPosition).getProgram_id()+"", 
								datas.get(currentPosition).getChannel_id()+"");
						Message msg = delHandler.obtainMessage();
						if(brb!=null && brb.getRet()==0){
							msg.arg1=1;
							msg.what = currentPosition;
						}else{
							msg.arg1=0;
						}
						delHandler.sendMessage(msg);
					}
				}).start();
			}
		});
		
		return convertView;
	}
	public class ViewHolder{
		ImageTextView itv;
	}
	public void setOnDeleteItemListener(OnDeleteItemListener onDeleteItemListener) {
		this.onDeleteItemListener = onDeleteItemListener;
	}
	
	public interface OnDeleteItemListener{
		public void onDeleteItem(EPGProgramsBean bean);
	}
	public List<EPGProgramsBean> getDatas() {
		return datas;
	}
}