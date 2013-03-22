package com.igrs.tivic.phone.Activity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Bean.Login.UserRegisterBean;
import com.igrs.tivic.phone.Global.ShowProgressDialog;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Global.URLConfig;
import com.igrs.tivic.phone.Impl.LoginImpl;
import com.igrs.tivic.phone.Utils.MD5Utils;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;
import com.igrs.tivic.phone.widget.AsyncImageView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginRegistrationBaseInfoActivity extends Activity implements
		OnClickListener {
	private AsyncImageView iv_user_pic;
	private Button bt_change_user_pic, bt_login_basic_informal_next;
	private EditText et_nickname, et_birth, et_signature;
	private TextView tv_user_age, tv_constellation;
	private CheckBox cb_user_sex;
	private Context mContext;
	public static LoginRegistrationBaseInfoActivity loginRegistrationBaseInfoActivity;
//	private String systemAvaterPath;// 从选择头像的Activity传递过来的
	private UserRegisterBean userRegisterBean = TivicGlobal.getInstance().userRegisterBean;
	private DatePickerDialog dpd;
	private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 结果
	private static final int PHOTO_REQUEST_SYSTEM = 4;
	// 创建一个以当前时间为名称的文件
	private File tempFile;
	private OnDateSetListener ds;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_registed_baseinfo_page_one);
		loginRegistrationBaseInfoActivity = this;
		mContext = this;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			tempFile = new File(Environment.getExternalStorageDirectory(),
					getPhotoFileName());
		} else {
			tempFile = new File(getCacheDir(), getPhotoFileName());
		}
//		if (getIntent() != null) {
//			systemAvaterPath = getIntent().getStringExtra("imagePath");
//		}
		init();
		new AsyncTask<Void, Void, Integer>() {
			protected void onPreExecute() {
				ShowProgressDialog.show2(mContext,R.string.login_registed_handle, null);
			};
			@Override
			protected Integer doInBackground(Void... params) {
				int result = new LoginImpl().getMyInfo();
				return result;
			}
			@Override
			protected void onPostExecute(Integer result) {
				ShowProgressDialog.dismiss();
				if(result == -1) {
					UIUtils.ToastMessage(mContext, R.string.net_error);
				}
				initData();
				super.onPostExecute(result);
			}
		}.execute();
		setListeners();
//		if (!TextUtils.isEmpty(systemAvaterPath)) {
//			Bitmap bm = BitmapFactory.decodeFile(systemAvaterPath);
//			iv_user_pic.setImageBitmap(bm);
//		}
	}
//	private void initAvater() {
//		String userAvaterPath = userRegisterBean.getUserAvartarPath();
//		if(userRegisterBean.isModifyUserAvatar() && userRegisterBean.isSuccessModifyUserAvater() && !TextUtils.isEmpty(userAvaterPath)) {
//			Bitmap bm = BitmapFactory.decodeFile(userAvaterPath);
//			iv_user_pic.setImageBitmap(bm);
//		} else {
//			if (userRegisterBean.getUserAvatar() != null
//			&& !TextUtils.isEmpty(userRegisterBean.getUserAvatar())) {
//		if (userRegisterBean.getUserAvatar().contains(".jpg")) {// 初始化用户头像
//			final String imagePath = URLConfig.avarter_path
//					+ userRegisterBean.getUserAvatar() + "!w128";
//			new AsyncTask<Void, Void, Bitmap>() {
//				@Override
//				protected Bitmap doInBackground(Void... params) {
//					Bitmap bitmap = getImageFromNet(imagePath);
//					return bitmap;
//				}
//				
//				protected void onPostExecute(Bitmap result) {
//					iv_user_pic.setImageBitmap(result);
//				};
//			}.execute();
//		}
//	}}
//		}
	private void initData() {
//		if (TextUtils.isEmpty(systemAvaterPath)) {
//			initAvater();
//		}
//		initAvater();
		Utils.initAvater(iv_user_pic);
		if (!TextUtils.isEmpty(userRegisterBean.getUserNickname())) {
			et_nickname.setText(userRegisterBean.getUserNickname());
		}
		if (!TextUtils.isEmpty(userRegisterBean.getUserBirthday())
				&& Integer.parseInt(userRegisterBean.getUserBirthday())!=0) {// 处理生日
			long time = Long.parseLong(userRegisterBean.getUserBirthday());
			String dateStr = string2unixTimestamp(time);
			et_birth.setText(dateStr.split(" ")[0]);
			
			
			String birthStr = et_birth.getText().toString();
			String[] strs = birthStr.split("-");
			int years = Integer.parseInt(strs[0]);
			int month = Utils.getFormatDayOrMonth(strs[1]);
			int day = Utils.getFormatDayOrMonth(strs[2]);
			Calendar c = Calendar.getInstance();
			c.set(years, month, day);
			int age = Utils.getAge(c);
			if (age == -1) {
				Toast.makeText(
						mContext,
						getResources().getString(
								R.string.login_registed_hobby_birth_wrong),
						Toast.LENGTH_SHORT).show();
				return;
			}
			String star = Utils.getConstellation(month,day,getResources().getString(R.string.xingzuo),getResources().getString(R.string.zuo));
			tv_user_age.setText(age + "");
			tv_constellation.setText(star);
		}
		if (!TextUtils.isEmpty(userRegisterBean.getUserSign())) {
			et_signature.setText(userRegisterBean.getUserSign());
		}
		int gender = userRegisterBean.getUserGender();
		if (gender == 0) {
			cb_user_sex.setChecked(true);
		} else {
			cb_user_sex.setChecked(false);
		}

	}

	private void init() {
		bt_login_basic_informal_next = (Button) findViewById(R.id.bt_login_basic_informal_next);
		bt_change_user_pic = (Button) findViewById(R.id.bt_change_user_pic);
		iv_user_pic = (AsyncImageView) findViewById(R.id.iv_user_pic);
//		if (userRegisterBean.getFill_profile() != null
//				&& userRegisterBean.getFill_profile().equals("1")) {
//			iv_user_pic.setImageResource(R.drawable.base_queshengtu4_3);
//			iv_user_pic.setDefaultImageResource(R.drawable.base_queshengtu4_3);
//		}
		iv_user_pic.setDefaultImageResource(R.drawable.base_default_avater);
		et_nickname = (EditText) findViewById(R.id.ed_login_register_nickname);
		et_birth = (EditText) findViewById(R.id.ed_login_register_birth);
		et_nickname.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart ;
            private int selectionEnd ;
            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                    int arg3) {
                temp = s;
            }
            
            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2,
                    int arg3) {
            }
            
            @Override
            public void afterTextChanged(Editable s) {
                 selectionStart = et_nickname.getSelectionStart();
                selectionEnd = et_nickname.getSelectionEnd();
                if (temp.length() > 14) {
                    Toast.makeText(mContext,
                           getResources().getString(R.string.login_registed_hobby_nickname_max), Toast.LENGTH_SHORT)
                            .show();
                    s.delete(selectionStart-1, selectionEnd);
                    int tempSelection = selectionStart;
                    et_nickname.setText(s);
                    et_nickname.setSelection(tempSelection);
                }
            }


        });
		et_signature = (EditText) findViewById(R.id.ed_login_registed_signature);
		et_signature.addTextChangedListener(new TextWatcher() {
	            private CharSequence temp;
	            private int selectionStart ;
	            private int selectionEnd ;
	            @Override
	            public void beforeTextChanged(CharSequence s, int arg1, int arg2,
	                    int arg3) {
	                temp = s;
	            }
	            
	            @Override
	            public void onTextChanged(CharSequence s, int arg1, int arg2,
	                    int arg3) {
	            }
	            
	            @Override
	            public void afterTextChanged(Editable s) {
	                 selectionStart = et_signature .getSelectionStart();
	                selectionEnd = et_signature .getSelectionEnd();
	                if (temp.length() > 32) {
	                    Toast.makeText(mContext,
	                           getResources().getString(R.string.login_registed_hobby_signtext_max), Toast.LENGTH_SHORT)
	                            .show();
	                    s.delete(selectionStart-1, selectionEnd);
	                    int tempSelection = selectionStart;
	                    et_signature .setText(s);
	                    et_signature .setSelection(tempSelection);
	                }
	            }


	        });
		tv_user_age = (TextView) findViewById(R.id.tv_user_age);
		tv_constellation = (TextView) findViewById(R.id.tv_constellation);
		cb_user_sex = (CheckBox) findViewById(R.id.cb_user_sex);
		ds = new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				String monthStr = null;
				String dayStr = null;
				if ((monthOfYear + 1) < 10) {
					monthStr = "0" + (monthOfYear + 1);
				} else {
					monthStr = (monthOfYear + 1) + "";
				}
				if (dayOfMonth < 10) {
					dayStr = "0" + dayOfMonth;
				} else {
					dayStr = dayOfMonth + "";
				}
				et_birth.setText(year + "-" + monthStr + "-" + dayStr);
				String birthStr = et_birth.getText().toString();
//				if (!Utils.isDateValid(birthStr)) {
//					Toast.makeText(
//							mContext,
//							getResources().getString(
//									R.string.login_registed_hobby_birth_error),
//							Toast.LENGTH_SHORT).show();
//					return;
//				}
				String[] strs = birthStr.split("-");
				int years = Integer.parseInt(strs[0]);
				int month = Utils.getFormatDayOrMonth(strs[1]);
				int day = Utils.getFormatDayOrMonth(strs[2]);
				Calendar c = Calendar.getInstance();
				c.set(years, month, day);
				int age = Utils.getAge(c);
				if (age == -1) {
					Toast.makeText(
							mContext,
							getResources().getString(
									R.string.login_registed_hobby_birth_wrong),
							Toast.LENGTH_SHORT).show();
					return;
				}
				String star = Utils.getConstellation(month,day,getResources().getString(R.string.xingzuo),getResources().getString(R.string.zuo));
				tv_user_age.setText(age + "");
				tv_constellation.setText(star);
			}
		};
			if(!userRegisterBean.getUserBirthday().equals("0") && !TextUtils.isEmpty(userRegisterBean.getUserBirthday())) {
				String myCalendar = getCalendar();
				String[] strs = myCalendar.split("-");
				dpd = new DatePickerDialog(mContext, ds, Integer.parseInt(strs[0]), Integer.parseInt(strs[1])-1, Integer.parseInt(strs[2]));
			} else {
				dpd = new DatePickerDialog(this, ds, 1985, 0, 1);
			}
	}

	private void setListeners() {
		bt_login_basic_informal_next.setOnClickListener(this);
		bt_change_user_pic.setOnClickListener(this);
		et_birth.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					dpd.show();
				}
			}
		});
		et_birth.setOnClickListener(this);
		cb_user_sex.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					userRegisterBean.setUserGender(0);
				} else {
					userRegisterBean.setUserGender(1);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_login_basic_informal_next:
			if (!checkData()) {
				Toast.makeText(mContext,
						R.string.login_regsited_birth_nick_empty,
						Toast.LENGTH_LONG).show();
				return;
			}
			handleBasicInfo();
			break;
		case R.id.bt_change_user_pic:
			changeUserAvatar();
			break;
		case R.id.ed_login_register_birth:
			dpd.show();
			break;
		default:
			break;
		}
	}

	private void changeUserAvatar() {
		showDialog();
	}

	private void showDialog() {
		new AlertDialog.Builder(this).setTitle("头像设置").setItems(
				new String[] { "本地相册", "相机拍照", "系统头像", "取消" },
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							dialog.dismiss();
							Intent intent1 = new Intent(Intent.ACTION_PICK,
									null);
							intent1.setDataAndType(
									MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
									"image/*");
							startActivityForResult(intent1,
									PHOTO_REQUEST_GALLERY);
							break;
						case 1:
							dialog.dismiss();
							// 调用系统的拍照功能
							Intent intent2 = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							// 指定调用相机拍照后照片的储存路径
							intent2.putExtra(MediaStore.EXTRA_OUTPUT,
									Uri.fromFile(tempFile));
							startActivityForResult(intent2,
									PHOTO_REQUEST_TAKEPHOTO);
							break;
						case 2:
							dialog.dismiss();
							Intent intent3 = new Intent(mContext,
									LoginRegistrationSystemAvaterActivity.class);
							intent3.putExtra("from", "register");
//							startActivity(intent3);
							startActivityForResult(intent3, PHOTO_REQUEST_SYSTEM);
							break;
						case 3:
							dialog.dismiss();
							break;
						default:
							break;
						}
					}
				}).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PHOTO_REQUEST_TAKEPHOTO:
			startPhotoZoom(Uri.fromFile(tempFile), 150);
			break;

		case PHOTO_REQUEST_GALLERY:
			if (data != null) {
				startPhotoZoom(data.getData(), 150);
			}

			break;

		case PHOTO_REQUEST_CUT:
			if (data != null)
				setPicToView(data);
			break;
		case PHOTO_REQUEST_SYSTEM:
			if(data!= null){
				String systemAvaterPath = data.getStringExtra("imagePath");
				if (!TextUtils.isEmpty(systemAvaterPath)) {
				Bitmap bm = BitmapFactory.decodeFile(systemAvaterPath);
				iv_user_pic.setImageBitmap(bm);
				}
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void startPhotoZoom(Uri uri, int size) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");

		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", size);
		intent.putExtra("outputY", size);
		intent.putExtra("return-data", true);

		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	private void setPicToView(Intent picdata) {// 将进行剪裁后的图片显示到UI界面上并保持在本地
		Bundle bundle = picdata.getExtras();
		if (bundle != null) {
			Bitmap bitmap = bundle.getParcelable("data");
			String path = savePhoto(bitmap);
			// System.out.println("保存路径"+path);
			iv_user_pic.setImageBitmap(bitmap);
		}
	}

	public String getImagePath(Intent data) {
		Uri selectedImage = data.getData();
		String[] filePathColumn = { MediaStore.Images.Media.DATA };

		Cursor cursor = getContentResolver().query(selectedImage,
				filePathColumn, null, null, null);
		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String picturePath = cursor.getString(columnIndex);
		cursor.close();
		return picturePath;
	}

	private String savePhoto(Bitmap photo) {
		String photoDir = "";
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] byteArray = baos.toByteArray();
			String saveDir = null;
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				saveDir = Environment.getExternalStorageDirectory()
						+ "/uploadphoto";
			} else {
				saveDir = getCacheDir() + "/uploadphoto";
			}
			File dir = new File(saveDir);
			if (!dir.exists()) {
				dir.mkdir();
			}
			String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
					.format(new Date());
//			String fileName = "photo_" + timeStamp + ".jpg";// 照片命名
			String fileName = "avater_" + userRegisterBean.getUserUID() + "_photo.jpg";
			File file = new File(saveDir, fileName);
			file.delete();
			if (!file.exists()) {
				file.createNewFile();
			}
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(byteArray);
			photoDir = file.getPath();
			// System.out.println("phtotoDir="+photoDir);
			if (!TextUtils.isEmpty(photoDir)) {
//				Utils.deleteFile(getCacheDir());
				userRegisterBean.setUserAvartarPath(photoDir);
				userRegisterBean.setModifyUserAvatar(true);
				userRegisterBean.setSuccessModifyUserAvater(false);
			}
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
		return photoDir;
	}

	private String getPhotoFileName() { // 使用系统当前日期加以调整作为照片的名称
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	private void handleBasicInfo() {
		Intent intent = new Intent(mContext,
				LoginRegistrationInterested3Activity.class);
		String nickName = et_nickname.getText().toString();
		intent.putExtra("nickName", nickName);
		String birthDay = et_birth.getText().toString();
		if (!Utils.isDateValid(birthDay)) {
			Toast.makeText(
					mContext,
					getResources().getString(
							R.string.login_registed_hobby_birth_error),
					Toast.LENGTH_SHORT).show();
			return;
		}
		if(getMyAge()<0) {
			UIUtils.ToastMessage(mContext, R.string.login_registed_hobby_birth_wrong);
			return;
		}
		if(!TextUtils.isEmpty(userRegisterBean.getFill_profile()) &&Integer.parseInt(userRegisterBean.getFill_profile()) == 1) {
			if(!userRegisterBean.isModifyUserAvatar()) {
				UIUtils.ToastMessage(mContext, "您还没有选择头像");
				return;
			}
		}
		String[] strs = birthDay.split("-");
		String age = strs[0] + strs[1] + strs[2];
		intent.putExtra("birthday", unixTimestamp2String(age) + "");// 设置用户生日
		String signature = Utils.getClearEmpty(et_signature.getText()
				.toString());
		if (signature.length() > 32) {
			Toast.makeText(mContext,
					R.string.login_registed_hobby_signature_error,
					Toast.LENGTH_LONG).show();
			return;
		}
		if (TextUtils.isEmpty(signature)) {
			intent.putExtra("sign", getResources().getString(
					R.string.login_registed_hobby_default_signature));
		} else {
			intent.putExtra("sign", signature);
		}
		if (cb_user_sex.isChecked()) {
			intent.putExtra("gender", 0);
		} else {
			intent.putExtra("gender", 1);
		}
		if(userRegisterBean.isModifyUserAvatar() && Integer.parseInt(userRegisterBean.getFill_profile())!=0) {//计算头像
		 
			String md5 = MD5Utils.getMD5String(userRegisterBean.getUserUID()+"");
			String dir1 = md5.substring(0,2);
			String dir2 = md5.substring(2, 4);
			String userAvater = "/userdata/avatar/"+dir1+"/"+dir2+"/"+userRegisterBean.getUserUID()+".jpg";
			intent.putExtra("avater", userAvater);
		}
		if(getIntent()!=null && !TextUtils.isEmpty(getIntent().getStringExtra("fromClass"))) {
			intent.putExtra("fromClass",getIntent().getStringExtra("fromClass"));
			startActivity(intent);
//			finish();
		}else {
			startActivity(intent);
//			finish();
		}
	}

	public static long unixTimestamp2String(String srcTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		Date result_date;
		long result_time = 0;
		try {
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
			result_date = sdf.parse(srcTime + " 00:00:00");
			// 返回的是毫秒数故除以1000
			result_time = result_date.getTime() / 1000;
		} catch (Exception e) {
		}
		return result_time;
	}

	public static String string2unixTimestamp(long utime) {
		Date date = new Date(utime * 1000);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		return sdf.format(date);
	}

	private boolean checkData() {
		String nickName = et_nickname.getText().toString();
		String birthDay = et_birth.getText().toString();
		if (!TextUtils.isEmpty(nickName) && !TextUtils.isEmpty(birthDay)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!TextUtils.isEmpty(userRegisterBean.getFill_profile()) &&Integer.parseInt(userRegisterBean.getFill_profile()) == 1) {
				AlertDialog.Builder builder = new Builder(mContext);
				builder.setIcon(R.drawable.ic_launcher);
				builder.setTitle(R.string.login_registed_warning);
				builder.setMessage(R.string.login_registed_confirm_leave);
				builder.setPositiveButton(
						getResources().getString(
								R.string.login_registed_confirm),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(mContext,
										LoginActivity.class);
								startActivity(intent);
								finish();
							}
						});
				builder.setNegativeButton(
						getResources()
								.getString(R.string.login_registed_cancel),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});
				builder.create().show();
			} 
//			else {
//				new AsyncTask<Void, Void, Integer>() {
//					
//					@Override
//					protected Integer doInBackground(Void... params) {
//						new LoginImpl().login(userRegisterBean.getUserName(), userRegisterBean.getUserPassword());
//						return null;
//					}
//					
//				}.execute();
//			}
			}
		return super.onKeyDown(keyCode, event);
	}

	public int getMyAge() {
		String birthStr = et_birth.getText().toString();
		String[] strs = birthStr.split("-");
		int years = Integer.parseInt(strs[0]);
		int month = Utils.getFormatDayOrMonth(strs[1]);
		int day = Utils.getFormatDayOrMonth(strs[2]);
		Calendar c = Calendar.getInstance();
		c.set(years, month, day);
		return Utils.getAge(c);
	}
	public String getCalendar() {
		String birth = string2unixTimestamp(Long.parseLong(userRegisterBean.getUserBirthday()));
		return birth.split(" ")[0];
	}
	@Override
	protected void onStart() {
		
		super.onStart();
	}
	@Override
	protected void onStop() {
		ShowProgressDialog.dismiss();
		super.onStop();
	}
	
}
