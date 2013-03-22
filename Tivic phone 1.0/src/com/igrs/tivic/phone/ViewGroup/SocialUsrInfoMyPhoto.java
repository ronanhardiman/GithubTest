package com.igrs.tivic.phone.ViewGroup;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Adapter.SocialPhotoAdapter;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SocialUsrInfoMyPhoto extends SocialUsrInfoDetailBase {

	public SocialUsrInfoMyPhoto(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();
	}

	public SocialUsrInfoMyPhoto(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		View photomain = LayoutInflater.from(getContext()).inflate(R.layout.social_base_photo, null);
		/*GridView gridview = (GridView) photomain.findViewById(R.id.gv_social_photo);
		SocialPhotoAdapter adapter = new SocialPhotoAdapter(getContext());
		gridview.setAdapter(adapter);*/
		addView(photomain);
	}
}
