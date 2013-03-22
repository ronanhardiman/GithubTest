package com.igrs.tivic.phone.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Activity.WaterfallActivity;
import com.igrs.tivic.phone.Bean.Waterfall.ItemBean;
import com.igrs.tivic.phone.Bean.Waterfall.ProgramDetailBean;
import com.igrs.tivic.phone.Bean.Waterfall.SubpageContent;
import com.igrs.tivic.phone.Bean.Waterfall.WaterfallAdapterData;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.widget.ImageTextView;
import com.igrs.tivic.phone.widget.WaterfallTextView;

public class WaterfallListdapter extends BaseAdapter implements OnClickListener{

	private ArrayList<ItemBean> data;
	private ArrayList<ProgramDetailBean> sendToContentActivityData;
	private Context context;
	
	public static final int TYPE_PIC_PIC = 0;
	public static final int TYPE_PIC_TEXT = TYPE_PIC_PIC+1;
	public static final int TYPE_TEXT_PIC = TYPE_PIC_TEXT+1;
	public static final int TYPE_TEXT_TEXT = TYPE_TEXT_PIC+1;
	public static final int TYPE_SINGLE_PIC = TYPE_TEXT_TEXT+1;
	
	public static final int TYPE_COUNT = TYPE_SINGLE_PIC +1;
	private static final String SPACE = "\u3000\u3000";
	private static final int MDV = 5;//默认margin值
	
	private int listView_width,listView_height;
	
	public WaterfallListdapter(Context context,WaterfallAdapterData data){
		this.data = data.getItemBeans();
		this.sendToContentActivityData = data.getSendToContentActivityData();
		this.context = context;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView==null){
			convertView = createLayout(position);
			listView_width = parent.getWidth();
			listView_height = parent.getHeight();
		}
		fillContent(convertView,position);
		return convertView;
	}
	@Override
	public int getItemViewType(int position) {
		return data.get(position).getItemtype();
	}
	@Override
	public int getViewTypeCount() {
		return TYPE_COUNT;
	}
	
	@Override
	public void onClick(View v) {
		Intent intent  =  new Intent();
		intent.setClassName("com.igrs.tivic.phone","com.igrs.tivic.phone.Activity.ContentsActivity");
		intent.putParcelableArrayListExtra("testt",sendToContentActivityData);
		intent.putExtra("currentId", (Integer)v.getTag());
		((WaterfallActivity)context).startActivity(intent);
		UIUtils.setCurrentFuncID(FuncID.ID_CONTENT);
	}
	
	private LinearLayout createLayout(int position){
		LinearLayout container = new LinearLayout(context);
		container.setOrientation(LinearLayout.HORIZONTAL);
		ImageTextView aiv1 = new ImageTextView(context,null);aiv1.setOnClickListener(this);
		aiv1.setId(R.id.waterfall_item_asyiv_1);aiv1.setMaskResource(R.drawable.waterfall_mask_bg);
		aiv1.setBackGroundImageView(R.drawable.waterfall_textview_bg);
		
		ImageTextView aiv2 = new ImageTextView(context,null);aiv2.setOnClickListener(this);
		aiv2.setId(R.id.waterfall_item_asyiv_2);aiv2.setMaskResource(R.drawable.waterfall_mask_bg);
		aiv2.setBackGroundImageView(R.drawable.waterfall_textview_bg);
		
		WaterfallTextView tv1 = new WaterfallTextView(context,null);tv1.setId(R.id.waterfall_item_tv_1);
		tv1.setOnClickListener(this);
		WaterfallTextView tv2 = new WaterfallTextView(context,null);tv2.setId(R.id.waterfall_item_tv_2);
		tv2.setOnClickListener(this);
		switch (getItemViewType(position)) {
		case TYPE_PIC_PIC:
			container.addView(aiv1);
			container.addView(aiv2);
			break;
		case TYPE_PIC_TEXT:
			container.addView(aiv1);
			container.addView(tv1);
			break;
		case TYPE_TEXT_PIC:
			container.addView(tv1);
			container.addView(aiv1);
			break;
		case TYPE_TEXT_TEXT:
			container.addView(tv1);
			container.addView(tv2);
			break;
		case TYPE_SINGLE_PIC:
			container.addView(aiv1);
			break;
		}
		return container;
	}
	
	
	
	private void fillContent(View currentView,int position){
		ItemBean ib = (ItemBean)getItem(position);
		int itemtype = getItemViewType(position);
		switch (itemtype) {
		case TYPE_PIC_PIC:
			setViewLayoutAndData(ib,(ImageTextView)currentView.findViewById(R.id.waterfall_item_asyiv_1),(ImageTextView)currentView.findViewById(R.id.waterfall_item_asyiv_2));
			break;
		case TYPE_PIC_TEXT:
			setViewLayoutAndData(ib,(ImageTextView)currentView.findViewById(R.id.waterfall_item_asyiv_1),(WaterfallTextView)currentView.findViewById(R.id.waterfall_item_tv_1));
			break;
		case TYPE_TEXT_PIC:
			setViewLayoutAndData(ib,(WaterfallTextView)currentView.findViewById(R.id.waterfall_item_tv_1),(ImageTextView)currentView.findViewById(R.id.waterfall_item_asyiv_1));
			break;
		case TYPE_TEXT_TEXT:
			setViewLayoutAndData(ib,(WaterfallTextView)currentView.findViewById(R.id.waterfall_item_tv_1),(WaterfallTextView)currentView.findViewById(R.id.waterfall_item_tv_2));
			break;
		case TYPE_SINGLE_PIC:
			setViewLayoutParam(LayoutParams.MATCH_PARENT,listView_width*3/4,
					(ImageTextView)currentView.findViewById(R.id.waterfall_item_asyiv_1),ib.getProgramDetailBean().get(0),
					new Rect(MDV,MDV,MDV,0));
			ImageTextView itv_single = (ImageTextView)currentView.findViewById(R.id.waterfall_item_asyiv_1);
			itv_single.setMaskVisiable(View.GONE);
			itv_single.getIv().setPadding(4, 4, 8, 8);
			setImageTextViewData(itv_single,ib.getProgramDetailBean().get(0));
			break;
		}
	}
	//设置每个item的布局及要显示的内容
	private void setViewLayoutAndData(ItemBean ib,View viewLeft,View viewRight){
		
		if(ib.getProgramDetailBean().size()<2){//当item中1×1的只有一个时
			lastItemOnlyOneView(ib.getProgramDetailBean().get(0),viewLeft,viewRight);
			return;
		}
		
		//左侧view控件
		if(viewLeft instanceof ImageTextView){
			ImageTextView itv = (ImageTextView)viewLeft;
			itv.setMaskVisiable(View.GONE);
			setViewLayoutParam(listView_width/2,listView_width*3/8,itv,ib.getProgramDetailBean().get(0),
					 new Rect(MDV,MDV,MDV,0));
			setImageTextViewData(itv,ib.getProgramDetailBean().get(0));
		}else if(viewLeft instanceof WaterfallTextView){
			WaterfallTextView wtv = (WaterfallTextView)viewLeft;
			setViewLayoutParam(listView_width/2,listView_width*3/8,
					wtv,ib.getProgramDetailBean().get(0),
					new Rect(MDV,MDV,MDV,0));
			setWaterfallTextView(wtv,ib.getProgramDetailBean().get(0));
		}
		
		//右侧view控件
		if(viewRight instanceof ImageTextView){
			ImageTextView itv = (ImageTextView)viewRight;
			itv.setMaskVisiable(View.GONE);
			setViewLayoutParam(LayoutParams.MATCH_PARENT,listView_width*3/8,itv,
					ib.getProgramDetailBean().get(1),new Rect(0,MDV,MDV,0));
			setImageTextViewData(itv,ib.getProgramDetailBean().get(1));
		}else if(viewRight instanceof WaterfallTextView){
			WaterfallTextView wtv = (WaterfallTextView)viewRight;
			setViewLayoutParam(LayoutParams.MATCH_PARENT,listView_width*3/8,
					wtv,ib.getProgramDetailBean().get(1),
					new Rect(0,MDV,MDV,0));
			setWaterfallTextView(wtv,ib.getProgramDetailBean().get(1));
		}
	}
	
	private void lastItemOnlyOneView(ProgramDetailBean pdb,View viewLeft,View viewRight){
		
		if(viewLeft instanceof ImageTextView){
			ImageTextView l_itv = (ImageTextView)viewLeft;
			ImageTextView r_itv = (ImageTextView)viewRight;
			
			if(pdb.getPlace().getPoint_x()==0){//右面控件隐藏
				r_itv.setVisibility(View.GONE);
				
				l_itv.setMaskVisiable(View.GONE);
				setViewLayoutParam(listView_width/2,listView_width*3/8,l_itv,pdb,new Rect(MDV,MDV,MDV,0));
				setImageTextViewData(l_itv,pdb);
			}else{//左面控件隐藏
				LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(listView_width/2,listView_width*3/8);
				l_itv.setLayoutParams(llp);
				l_itv.setVisibility(View.INVISIBLE);
				
				r_itv.setMaskVisiable(View.GONE);
				setViewLayoutParam(LayoutParams.MATCH_PARENT,listView_width*3/8,r_itv,pdb,new Rect(2*MDV,MDV,MDV,0));
				setImageTextViewData(r_itv,pdb);
			}
		}else if(viewLeft instanceof WaterfallTextView){
			WaterfallTextView l_wtv = (WaterfallTextView)viewLeft;
			WaterfallTextView r_wtv = (WaterfallTextView)viewRight;
			
			if(pdb.getPlace().getPoint_x()==0){//右面控件隐藏
				r_wtv.setVisibility(View.GONE);
				
				setViewLayoutParam(listView_width/2,listView_width*3/8,l_wtv,pdb,new Rect(MDV,MDV,MDV,0));
				setWaterfallTextView(l_wtv,pdb);
			}else{//左面控件隐藏
				LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(listView_width/2,listView_width*3/8);
				l_wtv.setLayoutParams(llp);
				l_wtv.setVisibility(View.INVISIBLE);
				
				setViewLayoutParam(LayoutParams.MATCH_PARENT,listView_width*3/8,r_wtv,pdb,new Rect(2*MDV,MDV,MDV,0));
				setWaterfallTextView(r_wtv,pdb);
			}
		}
	}
	
	//设置ImageTextView控件需要的值
	private void setImageTextViewData(ImageTextView itv,ProgramDetailBean bean){
//		Log.e("aaa","========image======title="+bean.getContent().getTitle()+"====url="+bean.getContent().getMedia_url());
		String type = bean.getContent().getMedia_type();
		String url = bean.getContent().getMedia_url();
		if("video".equals(type)){
			url = bean.getContent().getVideoImage();
			itv.getVideo_icon().setVisibility(View.VISIBLE);
		}else{
			itv.getVideo_icon().setVisibility(View.GONE);
		}
		itv.setImageUrl(url+"!w256");
		itv.setTag(bean.getContent().getContent_id());
		itv.setLeftText(bean.getContent().getTitle());
	}
	//设置WaterfallTextView控件需要的值
	private void setWaterfallTextView(WaterfallTextView wftv,ProgramDetailBean bean){
		SubpageContent content1 = bean.getContent();
//		Log.e("aaa","========text======title="+content1.getTitle());
		wftv.setTag(bean.getContent().getContent_id());
		wftv.setData(content1.getTitle(),content1.getSource()+" "+content1.getContent_time(),SPACE+content1.getSummary());
	}
	//设置view的布局
	private void setViewLayoutParam(int width,int height,View view,ProgramDetailBean bean,Rect r){
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(width,height);
		switch (bean.getType()){
		case ProgramDetailBean.LEFT_NEXT:
			r.left=0;
			updateBackgroundResource(view,R.drawable.waterfall_textview_bg_right);
			break;
		case ProgramDetailBean.TOP_NEXT:
			r.top=0;
			updateBackgroundResource(view,R.drawable.waterfall_textview_bg_bottom);
			break;
		case ProgramDetailBean.RIGHT_NEXT:
			updateBackgroundResource(view,R.drawable.waterfall_textview_bg_left);
			r.right=0;
			break;
		case ProgramDetailBean.BOTTOM_NEXT:
			updateBackgroundResource(view,R.drawable.waterfall_textview_bg_top);
			r.bottom=0;
			break;
		}
		llp.setMargins(r.left,r.top,r.right,r.bottom);
		view.setLayoutParams(llp);
	}
	
	private void updateBackgroundResource(View view,int resid){
		if(view instanceof ImageTextView){
			ImageTextView itv = (ImageTextView)view;
			itv.setMyBackRes(resid);
		}else if(view instanceof WaterfallTextView){
			WaterfallTextView wtv = (WaterfallTextView)view;
			wtv.setMyBackRes(resid);
		}
	}
}