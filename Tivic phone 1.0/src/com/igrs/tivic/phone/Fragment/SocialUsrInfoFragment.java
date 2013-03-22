package com.igrs.tivic.phone.Fragment;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Activity.LoginRegistrationSystemAvaterActivity;
import com.igrs.tivic.phone.Activity.SocialBaseActivity;
import com.igrs.tivic.phone.Activity.SocialBaseActivity.ChangAvaterListener;
import com.igrs.tivic.phone.Activity.SocialPhotoBrowseActivity;
import com.igrs.tivic.phone.Adapter.SocialPhotoAdapter;
import com.igrs.tivic.phone.AsyncTask.SocialPhotoAsyncTask;
import com.igrs.tivic.phone.AsyncTask.SocialPhotoDeleteAsyncTask;
import com.igrs.tivic.phone.Bean.Base.PhotoListBean;
import com.igrs.tivic.phone.Bean.Login.UserRegisterBean;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.ShowProgressDialog;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Global.URLConfig;
import com.igrs.tivic.phone.Impl.ContentImpl;
import com.igrs.tivic.phone.Impl.LoginImpl;
import com.igrs.tivic.phone.Impl.SocialFriendsDetailsImpl;
import com.igrs.tivic.phone.Listener.SocialPhotoListener;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;
import com.igrs.tivic.phone.ViewGroup.SocialUsrInfoBaseFrame;
import com.igrs.tivic.phone.ViewGroup.SocialUsrInfoContainer;
import com.igrs.tivic.phone.ViewGroup.SocialUsrInfoDetail;
import com.igrs.tivic.phone.ViewGroup.SocialUsrInfoDetailBase;
import com.igrs.tivic.phone.ViewGroup.SocialUsrInfoManager;
import com.igrs.tivic.phone.ViewGroup.SocialUsrInfoMyPhoto;
import com.igrs.tivic.phone.widget.AsyncImageView;

public class SocialUsrInfoFragment extends SocialBaseFragment implements
		OnClickListener, ChangAvaterListener {
	Context mContext;
	// public static SocialUsrInfoFragment instance;
	ViewGroup usrinfomain;
	private SocialPhotoListener socialPhotoListener;
	private Boolean flag = false;
	private SocialUsrInfoBaseFrame baseinfo; //
	private Button edit_qianming, edit_head, edit_quxiao;
	private View view;
	private TextView tv_social_signature, tv_usernickname;//, tv_user_age;
	private Dialog social_signature_edit;
	private EditText edit_social_signature;
	private AsyncImageView iv_user;
	private SocialUsrInfoContainer container;
	private SocialUsrInfoManager socialUsrInfoManager;
	private SocialUsrInfoDetail socialUsrInfoDetail;
	private SocialUsrInfoMyPhoto socialUsrInfoMyPhoto;
	private GridView gv_photo;
	private SocialPhotoAdapter myPhotoAdapter;
	// private List<Bitmap> myPhotoList; //my photo list
	private List<String> myPhotoUrlList; // my photo url list
	private List<String> myPhotoUrlList_128; // my photo url list 128px
	private List<String> myPhotoUrlList_512; // my photo url list 512px
	private LinearLayout loadView;
	public static final int FROMLOCALPHOTO = 1, CUR = 22, LOADPHOTO_LOCAL = 5,
			APHOTO = 6, LOADPHOTO_CAMERA = 7, SYSTEMPHOTO = 34, CAPTURE = 3;
	private static final int LOAD_IMAGE = 8, LOAD_CAMERA = 9;
	private byte[] b;
	private Bitmap myBitmap;
	private boolean isDelete = false;
	// private SocialUsrInfoBaseInfoFragment baseinfo;
	// private SocialUsrInfoInterestFragment interest;
	private CheckBox chk_photo;
	private PopupWindow mpopupWindow;
	private ContentImpl contentImpl;
	private Date date;
	private UserRegisterBean userbean;
	private String photoDir_fromlocal;
	private LoginImpl loginImpl = new LoginImpl();
	private OnFragmentAvaterLoadListener listener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		((SocialBaseActivity) getActivity()).setListener(this);
		super.onCreate(savedInstanceState);
	}

	// public static SocialUsrInfoFragment getInstance() {
	// if (instance == null)
	// instance = new SocialUsrInfoFragment();
	// return instance;
	//
	// }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		UIUtils.Logi("chen", "socialUsrInfoFragment onCreateView");
		mContext = getActivity().getApplicationContext();
		if (flag == false) {
			usrinfomain = (ViewGroup) inflater.inflate(R.layout.social_usrinfo,
					null);
			flag = true;
		}
		init();
		new AsyncTask<Void, Void, Integer>() {//重新获取用户的资料
		protected void onPreExecute() {
		};
		@Override
		protected Integer doInBackground(Void... params) {
			int result = new LoginImpl().getMyInfo();
			return result;
		}
		@Override
		protected void onPostExecute(Integer result) {
			if(result == -1) {
				UIUtils.ToastMessage(getActivity(), R.string.net_error);
			}
			if (!TextUtils.isEmpty(userbean.getUserSign())) {
				tv_social_signature.setText(userbean.getUserSign());
			} else {
				tv_social_signature.setText(getResources().getString(R.string.login_registed_hobby_default_signature));
			}
			if (!TextUtils.isEmpty(userbean.getUserNickname())) {
				tv_usernickname.setText(userbean.getUserNickname());
			}
			super.onPostExecute(result);
		}
	}.execute();
//		init();
		// baseinfo =
		// (SocialUsrInfoBaseInfoFragment)getFragmentManager().findFragmentById(R.id.base_info);
		// interest =
		// (SocialUsrInfoInterestFragment)getFragmentManager().findFragmentById(R.id.base_interest);
		return usrinfomain;
	}

	private void init() {
		/*
		 * if(myPhotoList == null) myPhotoList = new ArrayList<Bitmap>();
		 */

		contentImpl = ContentImpl.getInstance();
		if (myPhotoUrlList == null)
			myPhotoUrlList = new ArrayList<String>();
		if (myPhotoUrlList_128 == null)
			myPhotoUrlList_128 = new ArrayList<String>();
		if (myPhotoUrlList_512 == null)
			myPhotoUrlList_512 = new ArrayList<String>();
		userbean = TivicGlobal.getInstance().userRegisterBean;
		tv_usernickname = (TextView) usrinfomain
				.findViewById(R.id.tv_user_nickname);
//		if (!TextUtils.isEmpty(userbean.getUserNickname())) {
//			tv_usernickname.setText(userbean.getUserNickname());
//		}
		tv_social_signature = (TextView) usrinfomain
				.findViewById(R.id.tv_social_signature);
//		if (!TextUtils.isEmpty(userbean.getUserSign())) {
//			tv_social_signature.setText(userbean.getUserSign());
//		}
		baseinfo = (SocialUsrInfoBaseFrame) usrinfomain
				.findViewById(R.id.base_info);
		chk_photo = (CheckBox) baseinfo.findViewById(R.id.my_photo);
		edit_head = (Button) baseinfo.findViewById(R.id.edit_head);
		iv_user = (AsyncImageView) baseinfo.findViewById(R.id.social_userpic);
		iv_user.setDefaultImageResource(R.drawable.base_default_avater);
//		initAvater();// 初始化头像
		edit_qianming = (Button) baseinfo.findViewById(R.id.edit_qianming);
		chk_photo.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
//					TivicGlobal.getInstance().socialInfoState = FuncID.ID_SOCIAL_INFO_PHOTO;
//					edit_head.setBackgroundResource(R.drawable.touxiang);
//					edit_qianming.setBackgroundResource(R.drawable.qianming);
					socialUsrInfoDetail.setVisibility(View.GONE);
					socialUsrInfoMyPhoto.setVisibility(View.VISIBLE);
					loadMyPhotoData();
				} else {
//					TivicGlobal.getInstance().socialInfoState = FuncID.ID_SOCIAL_INFO;
					socialUsrInfoMyPhoto.setVisibility(View.GONE);
					socialUsrInfoDetail.setVisibility(View.VISIBLE);
				}
			}
		});
		edit_head.setOnClickListener(this);
		iv_user.setOnClickListener(this);
		edit_qianming.setOnClickListener(this);
		socialUsrInfoManager = new SocialUsrInfoManager();
		container = (SocialUsrInfoContainer) usrinfomain
				.findViewById(R.id.container);
		socialUsrInfoDetail = (SocialUsrInfoDetail) usrinfomain
				.findViewById(R.id.info);
		socialUsrInfoMyPhoto = (SocialUsrInfoMyPhoto) usrinfomain
				.findViewById(R.id.photo);
		loadView = (LinearLayout) socialUsrInfoMyPhoto
				.findViewById(R.id.loadingView);
		gv_photo = (GridView) socialUsrInfoMyPhoto
				.findViewById(R.id.gv_social_photo);

		gv_photo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, final View arg1,
					int arg2, long arg3) {
				if (arg2 == 0) {
					if (isDelete == false) {
						AlertDialog.Builder dialog = new AlertDialog.Builder(
								getActivity());
						dialog.setTitle(mContext.getString(R.string.select))
								.setItems(
										new String[] { mContext.getString(R.string.local_photo), 
												mContext.getString(R.string.camera), 
												mContext.getString(R.string.delete_photo),
												mContext.getString(R.string.cancel) },
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// stub
												switch (which) {
												// from local photo
												case 0:
													if (!checkSDCard())
														return;
													TivicGlobal.getInstance().socialInfoState = FuncID.ID_SOCIAL_INFO_PHOTO;
													Intent getImage = new Intent(
															Intent.ACTION_GET_CONTENT);
													getImage.addCategory(Intent.CATEGORY_OPENABLE);
													getImage.setType("image/jpeg");
													startActivityForResult(
															getImage,
															LOAD_IMAGE);
													/*
													 * Intent intent = new
													 * Intent
													 * (Intent.ACTION_PICK,
													 * null);
													 * intent.setDataAndType
													 * (MediaStore.Images.Media.
													 * EXTERNAL_CONTENT_URI
													 * ,"image/*"); //
													 * startActivityForResult
													 * (intent,
													 * LOADPHOTO_LOCAL);
													 * startActivityForResult
													 * (intent, APHOTO);
													 */
													break;
												case 1:
													// Toast.makeText(getActivity(),
													// "相机",
													// Toast.LENGTH_LONG).show();
													if (!checkSDCard())
														return;
													TivicGlobal.getInstance().socialInfoState = FuncID.ID_SOCIAL_INFO_PHOTO;
													/*
													 * String sdStatus =
													 * Environment
													 * .getExternalStorageState
													 * (); if
													 * (!sdStatus.equals(Environment
													 * .MEDIA_MOUNTED)) { //
													 * 检测sd是否可用 //
													 * Log.v("TestFile", //
													 * "SD card is not avaiable/writeable right now."
													 * );
													 * Toast.makeText(mContext,
													 * "SD卡不可用,请检查sd卡！"
													 * ,Toast.LENGTH_SHORT
													 * ).show(); return; }
													 */
													/*
													 * Intent photoIntent = new
													 * Intent(MediaStore.
													 * ACTION_IMAGE_CAPTURE);
													 * 
													 * //下面这句指定调用相机拍照后的照片存储的路径
													 * photoIntent
													 * .putExtra(MediaStore
													 * .EXTRA_OUTPUT,
													 * Uri.fromFile(new
													 * File(Environment
													 * .getExternalStorageDirectory
													 * (),"test.jpg"))); //
													 * startActivityForResult
													 * (photoIntent,
													 * LOADPHOTO_CAMERA);
													 */Intent cameraIntent = new Intent(
															"android.media.action.IMAGE_CAPTURE");
													startActivityForResult(
															cameraIntent,
															LOAD_CAMERA);
													break;
												case 2:
													Toast.makeText(getActivity(),mContext.getString(R.string.delete_photo),
															Toast.LENGTH_LONG).show();
													
													setOtherClickAble(false);
													/*
													 * for(int i = 1;i<
													 * gv_photo.getCount(); i++
													 * ){ ImageView iv =
													 * (ImageView)
													 * gv_photo.getChildAt
													 * (i).findViewById
													 * (R.id.social_iv_delete);
													 * iv
													 * .setVisibility(View.VISIBLE
													 * );
													 * myPhotoDeleteList.add(iv
													 * ); }
													 */
													isDelete = true;
													myPhotoAdapter
															.setIsDelete(isDelete);
													myPhotoAdapter
															.notifyDataSetChanged();

													/*
													 * for(int i = 0; i<
													 * myPhotoDeleteList
													 * .size();i++){ final int
													 * postion = i;
													 * myPhotoDeleteList
													 * .get(postion
													 * ).setOnClickListener(new
													 * OnClickListener() {
													 * 
													 * @Override public void
													 * onClick(View v) {
													 * stub
													 * Toast.makeText(getActivity
													 * (), postion+"",
													 * 1).show(); } }); }
													 */
												case 3:
													dialog.dismiss();
													break;
												default:
													break;
												}

											}

											
										}).show();
					} else {
						if (isDelete ==true) {
							isDelete = false;
							setOtherClickAble(true);
							myPhotoAdapter.setIsDelete(isDelete);
							myPhotoAdapter.notifyDataSetChanged();
						}
					}
					// 从本地上传相片
					/*
					 * Intent intent = new Intent(Intent.ACTION_PICK, null);
					 * intent
					 * .setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI
					 * ,"image/*"); startActivityForResult(intent, ADD_PHOTO);
					 */
				} else { // 不是第一条
					if (isDelete) {
						// 删除图片
						Toast.makeText(getActivity(), mContext.getString(R.string.delete_photo) + arg2 + "", 1)
								.show();
						// 删除图片的方法
						String deleteUrl = myPhotoUrlList.get(arg2 - 1);
						System.out.println(mContext.getString(R.string.delete_photo_url) + deleteUrl);
						/*
						 * ArrayList<String> urlList = new ArrayList<String>();
						 * urlList.add(deleteUrl);
						 */
						deletePhoto(deleteUrl);

						// 删除图片之后重新加载相册
						loadMyPhotoData();
					} else {
						// 放大显示
//						TivicGlobal.getInstance().socialInfoState = FuncID.ID_SOCIAL_INFO_PHOTO;
						Intent photoBrowseIntent = new Intent(getActivity(),
								SocialPhotoBrowseActivity.class);
						/*photoBrowseIntent.putStringArrayListExtra(
								"photoUrlList",
								(ArrayList<String>) myPhotoUrlList);*/
						photoBrowseIntent.putStringArrayListExtra(
								"photoUrlList_128",
								(ArrayList<String>) myPhotoUrlList_128);
						photoBrowseIntent.putStringArrayListExtra("photoUrlList_512", (ArrayList<String>) myPhotoUrlList_512);
						photoBrowseIntent.putExtra("location", arg2 - 1);
						startActivity(photoBrowseIntent);
					}
				}

			}
		});
		myPhotoAdapter = new SocialPhotoAdapter(mContext);
		myPhotoAdapter.setMyPhotoUrls(myPhotoUrlList);
		gv_photo.setAdapter(myPhotoAdapter);
		socialUsrInfoManager.setmContainer(container);
		socialPhotoListener = new SocialPhotoListener() {

			@Override
			public void notifySocialPhotoUI(PhotoListBean myPhotos) {
				TivicGlobal.getInstance().socialInfoState = FuncID.ID_SOCIAL_INFO_OTHER;
				loadView.setVisibility(View.GONE);
				if(myPhotos == null){
					gv_photo.setVisibility(View.GONE);
					Toast.makeText(mContext, mContext.getString(R.string.get_photo_failed), Toast.LENGTH_LONG).show();
					return;
				}
				/*// if photo list is null,return
				if (myPhotos.getUrllist() == null)
					return;*/
				myPhotoUrlList.clear();
				myPhotoUrlList_128.clear();
				myPhotoUrlList_512.clear();
				myPhotoUrlList = myPhotos.getUrllist();
				for (String myPhotoUrl : myPhotoUrlList) {
					// String myPhotUrl_128 = myPhotoUrl+ Const.PHOTO_128;
					String myPhotUrl_128 = UIUtils.getImgSubnailUrl(myPhotoUrl,
							128);
					myPhotoUrlList_128.add(myPhotUrl_128);
					String myPhotoUrl_512 = UIUtils.getImgSubnailUrl(myPhotoUrl, 512);
					myPhotoUrlList_512.add(myPhotoUrl_512);
					// System.out.println(myPhotUrl_128);
				}
				myPhotoAdapter.setMyPhotoUrls(myPhotoUrlList_128);
				myPhotoAdapter.notifyDataSetChanged();
			}

			@Override
			public void notifyDeletePhotoUI() {
				loadMyPhotoData();
			}
		};
	}

	private void initAvater() {
		Utils.initAvater(iv_user);
		UIUtils.Logi("chen", "userInfo init avater");
	}
	public interface OnFragmentAvaterLoadListener{
		public void onFragmentAvaterLoad(Bitmap bm);
	}
	public OnFragmentAvaterLoadListener getListener() {
		return listener;
	}

	public void setListener(OnFragmentAvaterLoadListener listener) {
		this.listener = listener;
	}
	// delete photo
	protected void deletePhoto(String deleteUrl) {
		SocialPhotoDeleteAsyncTask photoDeleteAsyncTask = new SocialPhotoDeleteAsyncTask(
				mContext);
		photoDeleteAsyncTask.setSocialPhotoListener(socialPhotoListener);
		photoDeleteAsyncTask.execute(deleteUrl);
	}

	/*
	 * //upload photo data private void upLoadMyPhotoData(){
	 * contentImpl.upLoadPhoto(URLConfig.get_photo_upload, filePath); }
	 */

	// load photo data
//		loadView.setVisibility(View.VISIBLE);
	private void loadMyPhotoData() {
		SocialPhotoAsyncTask socialPhotoAsyncTask = new SocialPhotoAsyncTask(
				mContext);
		socialPhotoAsyncTask.setSocialPhotoListener(socialPhotoListener);
		socialPhotoAsyncTask.execute(TivicGlobal.getInstance().userRegisterBean
				.getUserId());
	}

	private void showDetailWindow(int num,
			SocialUsrInfoDetailBase mSocialUsrInfoDetailBase) {
		socialUsrInfoManager.showDetailWindow(num, mSocialUsrInfoDetailBase);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onStart() {
		// init();
		super.onStart();
	}

	@Override
	public void onResume() {
		UIUtils.setCurrentFuncID(FuncID.ID_SOCIAL_INFO);
		SocialBaseActivity ac = (SocialBaseActivity) this.getActivity();
		ac.setMenuBarFocus(FuncID.ID_SOCIAL_INFO);
		initAvater();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.social_userpic:
			// Toast.makeText(mContext, "我的资料", Toast.LENGTH_SHORT).show();
			socialUsrInfoDetail.setVisibility(View.VISIBLE);
			socialUsrInfoMyPhoto.setVisibility(View.GONE);
			chk_photo.setChecked(false);
			break;
		case R.id.bt_social_signature_sure:
			final String signature = edit_social_signature.getText().toString();
			if (!TextUtils.isEmpty(signature.trim())) {
				userbean.setUserSign(signature);
				new AsyncTask<Void, Void, Integer>() {

					@Override
					protected Integer doInBackground(Void... params) {
						int result = loginImpl.modifyUserBasicInfo3(userbean);
						return result;
					}

					protected void onPostExecute(Integer result) {
						if (result == 0) {
							Toast.makeText(getActivity(),
									R.string.social_modify_sign_success,
									Toast.LENGTH_LONG).show();
							tv_social_signature.setText(signature);
						} else if (result == 2) {
							Toast.makeText(getActivity(),
									R.string.social_modify_sign_error,
									Toast.LENGTH_LONG).show();
						}
						social_signature_edit.dismiss();
					};

				}.execute();
			}
			break;
		case R.id.edit_head:
			chk_photo.setChecked(false);
//			edit_qianming.setBackgroundResource(R.drawable.qianming);
//			edit_head.setBackgroundResource(R.drawable.touxiang_selected);
			ShowPickDialog();
			break;
		case R.id.edit_qianming:
			chk_photo.setChecked(false);
//			edit_qianming.setBackgroundResource(R.drawable.qianming_selected);
//			edit_head.setBackgroundResource(R.drawable.touxiang);
			view = getActivity().getLayoutInflater().inflate(
					R.layout.social_edit_signature, null);
			social_signature_edit = new Dialog(getActivity(),
					R.style.login_sendcode_DialogTheme);
			social_signature_edit.setContentView(view);
			social_signature_edit.show();
			edit_social_signature = (EditText) social_signature_edit
					.findViewById(R.id.edit_social_signature);
			if(!TextUtils.isEmpty(userbean.getUserSign())){
				edit_social_signature.setText(userbean.getUserSign());
			}
			edit_social_signature.addTextChangedListener(new TextWatcher() {
				private CharSequence temp;
				private int selectionStart;
				private int selectionEnd;

				@Override
				public void beforeTextChanged(CharSequence s, int arg1,
						int arg2, int arg3) {
					temp = s;
				}

				@Override
				public void onTextChanged(CharSequence s, int arg1, int arg2,
						int arg3) {
				}

				@Override
				public void afterTextChanged(Editable s) {
					selectionStart = edit_social_signature.getSelectionStart();
					selectionEnd = edit_social_signature.getSelectionEnd();
					if (temp.length() > 32) {
						Toast.makeText(
								getActivity(),
								getResources()
										.getString(
												R.string.login_registed_hobby_signtext_max),
								Toast.LENGTH_SHORT).show();
						s.delete(selectionStart - 1, selectionEnd);
						int tempSelection = selectionStart;
						edit_social_signature.setText(s);
						edit_social_signature.setSelection(tempSelection);
					}
				}

			});
			Button button = (Button) social_signature_edit
					.findViewById(R.id.bt_social_signature_sure);
			button.setOnClickListener(this);
			social_signature_edit.findViewById(R.id.bt_quxiao_signature)
					.setOnClickListener(this);

			break;
		case R.id.xiangcexuanqu:
			if (mpopupWindow.isShowing()) {
				mpopupWindow.dismiss();
				mpopupWindow.setBackgroundDrawable(new BitmapDrawable());
			}
			if (!checkSDCard())
				return;
			Intent intent1 = new Intent(Intent.ACTION_PICK, null);
			intent1.setDataAndType(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			getActivity().startActivityForResult(intent1, FROMLOCALPHOTO);
			break;
		case R.id.paishe:
			if (mpopupWindow.isShowing()) {
				mpopupWindow.dismiss();
				mpopupWindow.setBackgroundDrawable(new BitmapDrawable());
			}
			if (!checkSDCard())
				return;
			// 调用系统的拍照功能
			Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// 指定调用相机拍照后照片的储存路径
			intent2.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(SocialBaseActivity.tempFile));
			getActivity().startActivityForResult(intent2, CAPTURE);
			break;
		case R.id.xitong:
			if (mpopupWindow.isShowing()) {
				mpopupWindow.dismiss();
			}
			Intent intent3 = new Intent(getActivity(),
					LoginRegistrationSystemAvaterActivity.class);
			getActivity().startActivityForResult(intent3, SYSTEMPHOTO);
			break;
		case R.id.bt_quxiao_signature:
			social_signature_edit.dismiss();
			break;
		default:
			break;
		}
	}

	private void ShowPickDialog() {
		View popupwindow = LayoutInflater.from(mContext).inflate(
				R.layout.social_pop_touxiang, null);
		popupwindow.findViewById(R.id.xiangcexuanqu).setOnClickListener(this);
		popupwindow.findViewById(R.id.paishe).setOnClickListener(this);
		popupwindow.findViewById(R.id.xitong).setOnClickListener(this);
		mpopupWindow = new PopupWindow(popupwindow, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		mpopupWindow.setFocusable(true);
		mpopupWindow.setOutsideTouchable(true);
		mpopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mpopupWindow.showAsDropDown(edit_head, -100, 0);

	}

	private void startPhotoZoom(Uri data) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(data, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
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

	// private void startPhotoZoom2(Uri uri, int size) {
	// Intent intent = new Intent("com.android.camera.action.CROP");
	// intent.setDataAndType(uri, "image/*");
	// // crop为true是设置在开启的intent中设置显示的view可以剪裁
	// intent.putExtra("crop", "true");
	//
	// // aspectX aspectY 是宽高的比例
	// intent.putExtra("aspectX", 1);
	// intent.putExtra("aspectY", 1);
	//
	// // outputX,outputY 是剪裁图片的宽高
	// intent.putExtra("outputX", size);
	// intent.putExtra("outputY", size);
	// intent.putExtra("return-data", true);
	//
	// startActivityForResult(intent, CUR);
	// }

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata, int mark) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			final Bitmap bitmap = photo;
			/*
			 * Drawable drawable = new BitmapDrawable(photo); //
			 * iv_user.setBackgroundDrawable(drawable);
			 * iv_user.setImageDrawable(drawable);
			 */
			if (mark == FROMLOCALPHOTO) {
				iv_user.setImageBitmap(bitmap);
				photoDir_fromlocal = savePhoto(photo);
				System.out.println("photodir=" + photoDir_fromlocal);
			} else if (mark == LOADPHOTO_LOCAL) { // 上传照片
				/*
				 * myPhotoList.add(photo);
				 * myPhotoAdapter.setMyPhotos(myPhotoList);
				 * myPhotoAdapter.notifyDataSetChanged();
				 */
				// TODO:把添加的照片上传服务器
				/*
				 * new Thread(){
				 * 
				 * @Override public void run() { 
				 * stub super.run(); contentImpl.upLoadPhoto("", photoPath); }
				 * 
				 * }.start();
				 */
				final String photoDir = savePhoto(photo);
				System.out.println("photodir=" + photoDir);
				new Thread() {

					@Override
					public void run() {
						super.run();
						// 上传照片
						contentImpl.upLoadPhoto("", photoDir);
						loadMyPhotoData();
					}
				}.start();

				/*
				 * myPhotoUrlList.add(photoDir);
				 * myPhotoAdapter.setMyPhotoUrls(myPhotoUrlList);
				 * myPhotoAdapter.notifyDataSetChanged();
				 */

			}
		}
	}

	private String savePhoto(Bitmap photo) {
		String photoDir = "";
		File file = imageSaveToFile(photo);
		// check photo size，if size large，scale photo
		Bitmap photoScale = fitSizeImage(file);
		;
		/*
		 * while(file.length()>2*1024*1024){ photoScale = fitSizeImage(file); }
		 */
		file = imageSaveToFile(photoScale);
		photoDir = file.getPath();
		return photoDir;
	}

	private File imageSaveToFile(Bitmap photo) {

		File file = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		ByteArrayOutputStream baos = null;
		try {

			baos = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] byteArray = baos.toByteArray();

			// String saveDir =
			// Environment.getExternalStorageDirectory()+"/uploadphoto";
			String saveDir = Const.TMPSAVEDIR;
			String savePhotoDir = Const.PHOTO_DIR;
			File dir = new File(saveDir);
			File photoDir = new File(savePhotoDir);
			if (!dir.exists()) {
				dir.mkdir();
			}

			if (!photoDir.exists()) {
				photoDir.mkdir();
			}
			/*
			 * String timeStamp = new
			 * SimpleDateFormat("yyyyMMddHHmmss").format(new Date()); String
			 * fileName = "photo_" + timeStamp + ".jpg";//照片命名
			 */
			// String fileName = "photo.jpg";
			String fileName = Const.PHOTO_FILE;
			file = new File(savePhotoDir, fileName);
			file.delete();
			if (!file.exists()) {
				file.createNewFile();
			}
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(byteArray);
			// photoDir = file.getPath();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return file;
	}

	/**
	 * check photo size,if size large,scale photo
	 * 
	 * @param file
	 */
	private Bitmap fitSizeImage(File file) {
		Bitmap scaleBmp = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		/*
		 * if(file.length() < 20480){ //0-20k opts.inSampleSize = 1; }else
		 * if(file.length() < 51200){ //20-50k opts.inSampleSize = 2; }else
		 * if(file.length() <307200){ //50-300k opts.inSampleSize = 4; }else
		 * if(file.length() <819200){ //300-800k opts.inSampleSize = 6; }else
		 * if(file.length() < 1048576){ //800-1024k opts.inSampleSize = 8;
		 * }else{ opts.inSampleSize = 10; }
		 */
		if (file.length() < 1 * 1024 * 1024) { // 0-1M
			opts.inSampleSize = 1;
		} else if (file.length() < 2 * 1024 * 1024) { // 1M-2M
			opts.inSampleSize = 1;
		} else if (file.length() < 4 * 1024 * 1024) { // 2M-4M
			opts.inSampleSize = 4;
		} else if (file.length() < 6 * 1024 * 1024) { // 4M-6M
			opts.inSampleSize = 6;
		} else if (file.length() < 8 * 1024 * 1024) { // 6M-8M
			opts.inSampleSize = 8;
		} else if (file.length() < 10 * 1024 * 1024) { // 8M-10M
			opts.inSampleSize = 10;
		} else if (file.length() < 12 * 1024 * 1024) { // 10M-12M
			opts.inSampleSize = 12;
		} else if (file.length() < 20 * 1024 * 1024) { // 12M-20M
			opts.inSampleSize = 20;
		} else {
			opts.inSampleSize = 25;
		}
		scaleBmp = BitmapFactory.decodeFile(file.getPath(), opts);
		return scaleBmp;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// if(resultCode != getActivity().RESULT_OK) return;

		/*
		 * new Thread(){
		 * 
		 * @Override public void run() {
		 */
		switch (requestCode) {
		case LOADPHOTO_LOCAL: // add photo
			if (data != null) {
				// Uri photoUri = data.getData();
				/*
				 * Uri photoUri = data.getData(); String photoPath =
				 * Utils.getAbsolutePathFromNoStandardUri(photoUri);
				 */
				// startPhotoZoom(data.getData(), LOADPHOTO_LOCAL);
			}
			break;
		case APHOTO: // cut add photo

			if (data != null) {
				/*
				 * Uri photoUri = data.getData(); String photoPath =
				 * Utils.getAbsolutePathFromNoStandardUri(photoUri);
				 */
				setPicToView(data, LOADPHOTO_LOCAL);
			}
			break;
		case LOADPHOTO_CAMERA:
			File photo = new File(Environment.getExternalStorageDirectory()
					+ "/test.jpg");
			// startPhotoZoom(Uri.fromFile(photo),LOADPHOTO_LOCAL);
			new Thread(new Runnable() {

				@Override
				public void run() {
					SocialFriendsDetailsImpl sfdi = SocialFriendsDetailsImpl
							.getInstance();
					sfdi.modifyUserAvatar(photoDir_fromlocal);
					// SocialFriendsDetailsImpl sfdi =
					// SocialFriendsDetailsImpl.getInstance();
					// sfdi.modifyUserAvatar(userbean, path);
				}
			}).start();
			break;
		case LOAD_IMAGE: // from local upload photo
			if (data != null) {
//				TivicGlobal.getInstance().socialInfoState = FuncID.ID_SOCIAL_INFO_OTHER;
				loadPhotoByLoacal(data);
			}else{
				TivicGlobal.getInstance().socialInfoState = FuncID.ID_SOCIAL_INFO_OTHER;
			}
			break;
		case LOAD_CAMERA: // from camera upload photo
			if (data != null) {
//				TivicGlobal.getInstance().socialInfoState = FuncID.ID_SOCIAL_INFO_OTHER;
				loadPhotoByCamera(data);
			}else{
				TivicGlobal.getInstance().socialInfoState = FuncID.ID_SOCIAL_INFO_OTHER;
			}
			break;
		default:
			break;
		}
		/*
		 * }
		 * 
		 * }.start();
		 */

	}

	private void loadPhotoByCamera(Intent data) {
		try {
			Bundle extras = data.getExtras();
			myBitmap = (Bitmap) extras.get("data");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			b = baos.toByteArray();
			final String photoDir = savePhoto(myBitmap);
			System.out.println("photodir=" + photoDir);
			new Thread() {

				@Override
				public void run() {
					super.run();
					// 上传照片
					contentImpl.upLoadPhoto("", photoDir);
					loadMyPhotoData();
				}
			}.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadPhotoByLoacal(Intent data) {
		ContentResolver resolver = getActivity().getContentResolver();
		try {
			// 获得图片的Uri
			Uri uri = data.getData();
			// 将图片内容解析成字节数组
			b = readStream(resolver.openInputStream(Uri.parse(uri.toString())));
			// 将字节数组转换为ImageView可调用的Bitmap对象
			myBitmap = getPicFromBytes(b, null);
			final String photoDir = savePhoto(myBitmap);
			System.out.println("photodir=" + photoDir);
			new Thread() {

				@Override
				public void run() {
					super.run();
					// 上传照片
					contentImpl.upLoadPhoto("", photoDir);
					loadMyPhotoData();
//					TivicGlobal.getInstance().socialInfoState = FuncID.ID_SOCIAL_INFO_OTHER;
				}
			}.start();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static byte[] readStream(InputStream inStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;
	}

	public static Bitmap getPicFromBytes(byte[] bytes,
			BitmapFactory.Options opts) {
		if (bytes != null)
			if (opts != null)
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
						opts);
			else
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return null;
	}

	public static String string2unixTimestamp(long utime) {
		Date date = new Date(utime * 1000);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		return sdf.format(date);
	}

	public Bitmap getImageFromNet(String path) {
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(6 * 1000);
			conn.setRequestMethod("GET");
			conn.addRequestProperty("Cache-Control", "no-cache");
			if (conn.getResponseCode() == 200) {
				InputStream is = conn.getInputStream();
				Bitmap bitmap = BitmapFactory.decodeStream(is);
				return bitmap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void setOtherClickAble(boolean b) {
		chk_photo.setClickable(b);
		edit_qianming.setClickable(b);
		edit_head.setClickable(b);
		iv_user.setClickable(b);
	}
	@Override
	public void onAvaterChanged(String userAvaterPath) {
		UIUtils.Logi("chen", "userInfo change avater");
		Bitmap bm = BitmapFactory.decodeFile(userAvaterPath);
		iv_user.setImageBitmap(bm);
	}

}