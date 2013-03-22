package com.igrs.tivic.phone.ViewGroup;



import com.igrs.tivic.phone.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

public class EPGLightShow extends LinearLayout {

	public EPGLightShow(Context context) {
		super(context);
		initView();
	}

	public EPGLightShow(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		View lightmain = LayoutInflater.from(getContext()).inflate(R.layout.program_light_item, null);
		addView(lightmain);
	}

}
