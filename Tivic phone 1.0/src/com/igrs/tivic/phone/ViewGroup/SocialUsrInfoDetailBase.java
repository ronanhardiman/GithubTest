package com.igrs.tivic.phone.ViewGroup;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

public class SocialUsrInfoDetailBase extends FrameLayout {
	public LinearLayout linearLayout;
	private LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
			LayoutParams.MATCH_PARENT);
	public SocialUsrInfoDetailBase(Context context) {
		super(context);
		initView();
	}

	public SocialUsrInfoDetailBase(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		linearLayout = new LinearLayout(getContext());
		linearLayout.setLayoutParams(params);
	}

}
