package com.igrs.tivic.phone.widget;

import com.igrs.tivic.phone.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WaterfallTextView extends LinearLayout{

	private TextView title,source,desc;
	private LinearLayout root;
	
	public WaterfallTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		root = (LinearLayout) li.inflate(R.layout.waterfall_textview_layout, this);
		root.setBackgroundResource(R.drawable.waterfall_textview_bg);
		title = (TextView)root.findViewById(R.id.waterfall_textview_title);
		source = (TextView)root.findViewById(R.id.waterfall_textview_source);
		desc = (TextView)root.findViewById(R.id.waterfall_textview_desc);
	}
	
	public void setMyBackRes(int resid){
		root.setBackgroundResource(resid);
	}
	
	public void setData(String... datas){
		title.setText(datas[0]);
		source.setText(datas[1]);
		desc.setText(datas[2]);
	}
}
