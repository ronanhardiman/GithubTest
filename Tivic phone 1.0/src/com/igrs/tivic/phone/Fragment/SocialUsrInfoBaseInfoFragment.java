package com.igrs.tivic.phone.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.TivicGlobal;

public class SocialUsrInfoBaseInfoFragment extends SocialBaseFragment implements OnClickListener, OnKeyListener{

	ViewGroup usrbaseinfomain;
//	public static SocialUsrInfoBaseInfoFragment instance;
	private TextView tv_social_signature;
	private EditText edit_social_signature;
	private View view;
	private Dialog social_signature_edit;
	private Button bt_photo,edit_head;
	private ImageView iv_user;
	private final int FROMLOCALPHOTO = 1,CUR = 2;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

//	public static SocialUsrInfoBaseInfoFragment getInstance() {
//		if (instance == null)
//			instance = new SocialUsrInfoBaseInfoFragment();
//		return instance;
//	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mContext = getActivity().getApplicationContext();
		usrbaseinfomain = (ViewGroup) inflater.inflate(
				R.layout.social_base_usrinfo, null);
		init();
		return usrbaseinfomain;
	}

	private void init() {
		tv_social_signature=  (TextView) usrbaseinfomain.findViewById(R.id.tv_social_signature);
		tv_social_signature.setOnClickListener(this);
		bt_photo = (Button) usrbaseinfomain.findViewById(R.id.my_photo);
		bt_photo.setOnClickListener(this);
		edit_head = (Button) usrbaseinfomain.findViewById(R.id.edit_head);
		edit_head.setOnClickListener(this);
		iv_user = (ImageView) usrbaseinfomain.findViewById(R.id.social_userpic);
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
		case R.id.edit_head:
			ShowPickDialog();
			break;
		default:
			break;
		}
	}

	private void ShowPickDialog() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(getActivity()).setTitle("修改头像")
		.setNegativeButton("相册", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
//				Toast.makeText(mContext, "修改头像", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
				startActivityForResult(intent, FROMLOCALPHOTO);
			}
		}).show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case FROMLOCALPHOTO:
			startPhotoZoom(data.getData());
			break;
		case CUR:
			if(data != null){
				setPicToView(data);
			}
			break;
		default:
			break;
		}
	}
	
	private void startPhotoZoom(Uri data) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(data, "image/*");
		//下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, CUR);
	}
	/**
	 * 保存裁剪之后的图片数据
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(photo);
//			iv_user.setBackgroundDrawable(drawable);
			iv_user.setImageDrawable(drawable);
		}
	}
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
}