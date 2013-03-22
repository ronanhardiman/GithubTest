package com.igrs.tivic.phone.Fragment;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Activity.SocialBaseActivity;
import com.igrs.tivic.phone.Adapter.SociaSystemUsrIconAdapter;

public class SocialSystemUserIconFragment extends SocialBaseFragment implements OnClickListener{
//	private static SocialSystemUserIconFragment instance;
	private Context mContext;
	private Button bt_finish_select_icon,bt_back;

//	public static SocialSystemUserIconFragment getInstance(
//			SocialSystemUserIconFragment instance) {
//		if (instance == null) {
//			instance = new SocialSystemUserIconFragment();
//		}
//		return instance;
//	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mContext = this.getActivity().getApplication();
		container = (ViewGroup) inflater.inflate(
				R.layout.social_system_usericon, null);
		SociaSystemUsrIconAdapter adapter = new SociaSystemUsrIconAdapter(mContext);
		GridView system_grid = (GridView) container
				.findViewById(R.id.system_grid);
		system_grid.setAdapter(adapter);
		bt_finish_select_icon = (Button) container.findViewById(R.id.bt_finish_select_icon);
		bt_finish_select_icon.setOnClickListener(this);
		bt_back = (Button) container.findViewById(R.id.bt_back);
		bt_back.setOnClickListener(this);
		system_grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
			}
		});
		return container;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_finish_select_icon:
//			SocialUsrInfoFragment usrinfo = new SocialUsrInfoFragment();
//			FragmentTransaction ft = getFragmentManager().beginTransaction();
//			ft.replace(R.id.social_empty, usrinfo);
//			ft.addToBackStack(null);
//			ft.commit();

			break;
		case R.id.bt_back:
			SocialUsrInfoFragment usrinfo_fragment = new SocialUsrInfoFragment();
			FragmentTransaction ft_socialurinfo = getFragmentManager().beginTransaction();
			ft_socialurinfo.replace(R.id.social_empty, usrinfo_fragment);
			//zhanglr delete
			//ft_socialurinfo.addToBackStack(null);
			ft_socialurinfo.commit();
		break;
		default:
			break;
		}
	}

}
