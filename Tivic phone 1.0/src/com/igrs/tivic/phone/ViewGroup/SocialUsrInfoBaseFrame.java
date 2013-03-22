package com.igrs.tivic.phone.ViewGroup;


import com.igrs.tivic.phone.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

public class SocialUsrInfoBaseFrame extends FrameLayout{

	public SocialUsrInfoBaseFrame(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();
	}

	public SocialUsrInfoBaseFrame(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView();
	}

	private void initView() {
		final LayoutInflater mLayoutInflater = LayoutInflater.from(getContext());
		View v = mLayoutInflater.inflate(R.layout.social_base_usrinfo, null);
		addView(v);
	}

}
