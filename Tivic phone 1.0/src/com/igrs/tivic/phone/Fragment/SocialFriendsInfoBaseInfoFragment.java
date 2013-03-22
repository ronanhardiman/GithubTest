package com.igrs.tivic.phone.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.widget.AsyncImageView;

public class SocialFriendsInfoBaseInfoFragment extends Fragment implements OnClickListener{

	ViewGroup usrbaseinfomain;
//	public static SocialFriendsInfoBaseInfoFragment instance;
	private TextView tv_social_signature;
	private EditText edit_social_signature;
	private View view;
	private Dialog social_signature_edit;
	private Button bt_photo;
	private Boolean flag = true;
	private Boolean flag1 = true;
	private Context mContext;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

//	public static SocialFriendsInfoBaseInfoFragment getInstance() {
//		if (instance == null)
//			instance = new SocialFriendsInfoBaseInfoFragment();
//		return instance;
//	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mContext = getActivity().getApplicationContext();
		if(flag){
			usrbaseinfomain = (ViewGroup) inflater.inflate(
					R.layout.social_base_friendsinfo, null);
			AsyncImageView aiv = (AsyncImageView) usrbaseinfomain.findViewById(R.id.social_userpic);
			aiv.setImageResource(R.drawable.base_default_avater);
			flag = false;
			flag1 = false;
		}
		
		return usrbaseinfomain;
	}

	private void init() {
		tv_social_signature=  (TextView) usrbaseinfomain.findViewById(R.id.tv_social_signature);
		tv_social_signature.setOnClickListener(this);
//		bt_photo = (Button) usrbaseinfomain.findViewById(R.id.my_photo);
//		bt_photo.setOnClickListener(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_social_signature:
			view = getActivity().getLayoutInflater().inflate(
					R.layout.social_edit_signature , null);
			social_signature_edit = new Dialog(getActivity(),
					R.style.login_sendcode_DialogTheme);
			social_signature_edit.setContentView(view);
			social_signature_edit.show();
			edit_social_signature = (EditText) social_signature_edit
									.findViewById(R.id.edit_social_signature);
			Button button = (Button) social_signature_edit
									.findViewById(R.id.bt_social_signature_sure);
			button.setOnClickListener(this);
			break;
		case R.id.bt_social_signature_sure:
			String signature = edit_social_signature.getText().toString();
			if(!signature.equals("")){
				tv_social_signature.setText(signature);
			}
			social_signature_edit.dismiss();
			break;
		case R.id.my_photo:
			Toast.makeText(getActivity(), "我的相册", 1).show();
			break;
		default:
			break;
		}
	}

}