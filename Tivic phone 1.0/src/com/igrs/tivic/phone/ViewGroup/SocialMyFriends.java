package com.igrs.tivic.phone.ViewGroup;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Adapter.SocialMkFriendListAdapter;
import com.igrs.tivic.phone.Bean.Social.SocialFriendBean;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

public class SocialMyFriends extends LinearLayout {

	public SocialMyFriends(Context context) {
		super(context);
		initView();
	}

	public SocialMyFriends(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	private void initView() {
		View friendmain = LayoutInflater.from(getContext()).inflate(R.layout.social_mkfriend_list, null);
		ListView lv = (ListView) friendmain.findViewById(R.id.id_list);
		SocialMkFriendListAdapter adapter = new SocialMkFriendListAdapter(getContext());
		lv.setAdapter(adapter);
	}

}
