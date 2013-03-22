package com.igrs.tivic.phone.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Adapter.PushTvListdapter;

public class CustomTextView extends FrameLayout implements OnClickListener{

	private ImageView ivfocus;
	private TextView textview_title,textview_date,textview_desc,textview_percent,textview_percent2;
	private FrameLayout root;
	private LinearLayout focus_button_container;
	
	private FocusImageOnClickListener imageOnClickListener;
	
	public static final int STATE_UNFOCUS = 0;
	public static final int STATE_FOCUSED = 1;
	

	public CustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		root = (FrameLayout) li.inflate(R.layout.custom_textview_layout, this);
		ivfocus = (ImageView)root.findViewById(R.id.custom_textview_focus);
		ivfocus.setOnClickListener(this);
		
		textview_title = (TextView)root.findViewById(R.id.custom_textview_title);
		textview_date = (TextView)root.findViewById(R.id.custom_textview_date);
		textview_desc = (TextView)root.findViewById(R.id.custom_textview_desc);
		textview_percent = (TextView)root.findViewById(R.id.custom_textview_percent);
		textview_percent2 = (TextView)root.findViewById(R.id.custom_textview_percent_2);
		focus_button_container = (LinearLayout)root.findViewById(R.id.focus_button_container);
	}


	@Override
	public void onClick(View v) {
		int preLevel = ivfocus.getDrawable().getLevel();//单击按钮之前的状态
		
		if(imageOnClickListener!=null) imageOnClickListener.onClick(preLevel);
		
//		int currentLevel = (preLevel==0?1:0);
//		ivfocus.getDrawable().setLevel(currentLevel);
	}
	
	//设置蒙版左侧的icon
	public void setMaskLeftIcon(int resid){
		Drawable drawable = getResources().getDrawable(resid);
		// 这一步必须要做,否则不会显示.
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		textview_title.setCompoundDrawables(drawable, null, null, null);
	}
	
	//设置按钮是“关注”或是“未关注”
	public void setFocusImageViewState(int state){
		if(state == CustomTextView.STATE_UNFOCUS){//未收藏
			ivfocus.getDrawable().setLevel(0);
		}else{//已收藏
			ivfocus.getDrawable().setLevel(1);
		}
	}

	/**
	 * 按顺序设置  title、date、desc、percent的值
	 * @param datas
	 */
	public void setContent(String... datas){
		textview_title.setText(datas[0]);
		textview_date.setText(datas[1]);
		textview_desc.setText("\u3000\u3000"+datas[2]);
		textview_percent.setText(datas[3]);
		textview_percent2.setText(datas[3]);
	}
	/**
	 * 初始化   按钮当前状态   是否是已关注
	 * @param state
	 */
	public void initFocusState(int state){
		ivfocus.getDrawable().setLevel(state);
	}
	
	public void setFocusButtonOrientation(int layoutType){
		if(layoutType == PushTvListdapter.L_IMAGE_R_TEXT){
			focus_button_container.setGravity(Gravity.RIGHT);
			textview_percent.setVisibility(View.VISIBLE);
			textview_percent2.setVisibility(View.GONE);
		}else if(layoutType == PushTvListdapter.L_TEXT_R_IMAGE){
			focus_button_container.setGravity(Gravity.LEFT);
			textview_percent.setVisibility(View.GONE);
			textview_percent2.setVisibility(View.VISIBLE);
		}
	}
	public void setFocusImageOnClickListener(FocusImageOnClickListener imageOnClickListener) {
		this.imageOnClickListener = imageOnClickListener;
	}
	public interface FocusImageOnClickListener{
		public void onClick(int state);//0表示未被关注  1表示关注
	}
	public void setBackGroundImageView(int imageResource){
		root.setBackgroundResource(imageResource);
	}
}
