package com.igrs.tivic.phone.Activity;

import java.util.ArrayList;
import java.util.List;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Adapter.PoiItemAdapter2;
import com.igrs.tivic.phone.Bean.Login.UserPOIItemBean;
import com.igrs.tivic.phone.Bean.Login.UserRegisterBean;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.ShowProgressDialog;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Global.URLConfig;
import com.igrs.tivic.phone.Impl.LoginImpl;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;
import com.igrs.tivic.phone.ViewGroup.MyGridView;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginRegistrationInterested3Activity extends Activity implements
		OnClickListener, OnItemClickListener {
	private Gallery gallery_star, gallery_programm;
	private Button bt_login_registed_interest_step,
			bt_login_registed_interest_complete;
	private List<UserPOIItemBean> stars, programs, categories;// 从服务器获取的items
	private List<UserPOIItemBean> searchStars, searchPrograms,
			searchCategories;
	private Context mContext;
	private MyGridView gv_category;
	private LoginImpl loginImpl = new LoginImpl();
	private UserRegisterBean urb = TivicGlobal.getInstance().userRegisterBean;
	private PoiItemAdapter2 starAdapter, programAdapter, categoryAdapter;
	private List<UserPOIItemBean> userPOIItemBeans = TivicGlobal.getInstance().userRegisterBean
			.getUserPoiItems();// 用户之前已经选好的感兴趣的条目
	private static final int MAX_SIZE = 3;
	private TextView tv_stars, tv_programs, tv_categories;
	private StringBuilder starSb = new StringBuilder();
	private StringBuilder progSb = new StringBuilder();
	private StringBuilder cateSb = new StringBuilder();
	private ImageView iv_star_search, iv_program_search, iv_content_search;
	private EditText et_star_name, et_program_name, et_content_name;
	private ImageView iv_fasten_weibo;
	private SharedPreferences sp;
	private Editor editor;
	private Weibo weibo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_registed_interested_one);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		editor = sp.edit();
		mContext = this;
		init();
		setListeners();
		new AsyncTask<Void, Void, Integer>() {

			@Override
			protected Integer doInBackground(Void... params) {
				int result = loginImpl.getMyExtInfo();
				return result;
			}
			protected void onPostExecute(Integer result) {
				if(result == 0) {
					userPOIItemBeans = urb.getUserPoiItems();
				}
				initDatas();
			};
		}.execute();
	}

	private void init() {
		gallery_star = (Gallery) findViewById(R.id.gallery_star);
		gallery_programm = (Gallery) findViewById(R.id.gallery_programm);
		gv_category = (MyGridView) findViewById(R.id.gv_category);
		bt_login_registed_interest_step = (Button) findViewById(R.id.bt_login_registed_interest_step);
		bt_login_registed_interest_complete = (Button) findViewById(R.id.bt_login_registed_interest_complete);

		tv_categories = (TextView) findViewById(R.id.tv_categories);
		tv_programs = (TextView) findViewById(R.id.tv_programs);
		tv_stars = (TextView) findViewById(R.id.tv_stars);

		et_content_name = (EditText) findViewById(R.id.et_content_name);
		et_program_name = (EditText) findViewById(R.id.et_program_name);
		et_star_name = (EditText) findViewById(R.id.et_star_name);
		iv_star_search = (ImageView) findViewById(R.id.iv_star_search);
		iv_program_search = (ImageView) findViewById(R.id.iv_program_search);
		iv_content_search = (ImageView) findViewById(R.id.iv_content_search);
		iv_fasten_weibo = (ImageView) findViewById(R.id.iv_fasten_weibo);
		if(urb.isBoundWeibo()) {
			iv_fasten_weibo.setImageResource(R.drawable.login_register_personal_interest_survey_micro_blog_binding);
		} 
		if(urb.getUserType() == 3) {//微博账号登陆时自动标识为绑定状态
			iv_fasten_weibo.setImageResource(R.drawable.login_register_personal_interest_survey_micro_blog_binding);
			iv_fasten_weibo.setEnabled(false);
		}
	}

	private void setListeners() {
		bt_login_registed_interest_complete.setOnClickListener(this);
		bt_login_registed_interest_step.setOnClickListener(this);
		gallery_programm.setOnItemClickListener(this);
		gallery_star.setOnItemClickListener(this);
		gv_category.setOnItemClickListener(this);

		iv_content_search.setOnClickListener(this);
		iv_program_search.setOnClickListener(this);
		iv_star_search.setOnClickListener(this);
		iv_fasten_weibo.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_login_registed_interest_complete:
			handleComplete();
			break;
		case R.id.iv_star_search:
			searchStars();
			break;
		case R.id.iv_program_search:
			searchPrograms();
			break;
		case R.id.iv_content_search:
			searchContents();
			break;
		case R.id.iv_fasten_weibo:
			handleWeiBo();
			break;
		case R.id.bt_login_registed_interest_step:
//			Intent intent = new Intent(mContext,LoginRegistrationBaseInfoActivity.class);
//			if(!TextUtils.isEmpty(getIntent().getStringExtra("fromClass"))) {
//				intent.putExtra("fromClass", getClass().getName());
//				startActivity(intent);
//				finish();
//			} else {
//				startActivity(intent);
//				finish();
//			}
			finish();
			break;
		default:
			break;
		}
	}

	

	private void searchStars() {
		String starName = Utils.getClearEmpty(et_star_name.getText().toString());
		if (TextUtils.isEmpty(starName)) {
			Toast.makeText(mContext, R.string.login_regsited_interest_star_name_error, Toast.LENGTH_SHORT).show();
			return;
		}
		List<UserPOIItemBean> items = searchItemByName(starName, searchStars);
		if (items.size() <= 0) {
			Toast.makeText(mContext, R.string.login_regsited_interest_star_not_found, Toast.LENGTH_SHORT).show();
			return;
		}
//		System.out.println("是否被选中"+items.get(0).isChecked());
//		initSelectedPoiItem(items);
		starAdapter.setPoiItemsBeans(items);
		starAdapter.notifyDataSetChanged();
		setStars(items);
	}

	private void searchPrograms() {
		String programName = Utils.getClearEmpty(et_program_name.getText().toString());
		if (TextUtils.isEmpty(programName)) {
			Toast.makeText(mContext, R.string.login_regsited_interest_program_name_error, Toast.LENGTH_SHORT).show();
			return;
		}
		List<UserPOIItemBean> items = searchItemByName(programName, searchPrograms);
		if (items.size() <= 0) {
			Toast.makeText(mContext, R.string.login_regsited_interest_program_not_found, Toast.LENGTH_SHORT).show();
			return;
		}
		programAdapter.setPoiItemsBeans(items);
		programAdapter.notifyDataSetChanged();
		setPrograms(items);
	}

	private void searchContents() {
		String contName = Utils.getClearEmpty(et_content_name.getText().toString());
		if (TextUtils.isEmpty(contName)) {
			Toast.makeText(mContext, R.string.login_regsited_interest_cate_name_error, Toast.LENGTH_SHORT).show();
			return;
		}
		List<UserPOIItemBean> items = searchItemByName(contName, searchCategories);
		if (items.size() <= 0) {
			Toast.makeText(mContext, R.string.login_regsited_interest_cate_not_found, Toast.LENGTH_SHORT).show();
			return;
		}
		categoryAdapter.setPoiItemsBeans(items);
		categoryAdapter.notifyDataSetChanged();
		setCategories(items);
	}

	private void handleComplete() {
		new AsyncTask<Void, Void, Integer>() {
			protected void onPreExecute() {
				ShowProgressDialog.show2(mContext,R.string.login_registed_handle, null);
			};

			@Override
			protected Integer doInBackground(Void... params) {
				Intent intent = getIntent();
				if(intent!=null) {
					urb.setUserNickname(intent.getStringExtra("nickName"));
					urb.setUserBirthday(intent.getStringExtra("birthday"));
					urb.setUserSign(intent.getStringExtra("sign"));
					urb.setUserGender(intent.getIntExtra("gender", 0));
					if(Integer.parseInt(urb.getFill_profile())!=0) {
						urb.setUserAvatar(intent.getStringExtra("avater"));
					}
				}
				
				int result = loginImpl.modifyUserBasicInfo2(urb,
						getKeysFromSelected(UserPOIItemBean.STAR_TYPE),
						getKeysFromSelected(UserPOIItemBean.PROGRAM_TYPE),
						getKeysFromSelected(UserPOIItemBean.CATE_TYPE));
				return result;
			}

			protected void onPostExecute(Integer result) {
				ShowProgressDialog.dismiss();
				handleResult(result);
			};
		}.execute();
	}

	protected void handleResult(Integer result) {
		if (result == 0) {// 修改成功
			urb.setSuccessModifyUserAvater(true);
			urb.getFilterItems().clear();
			urb.getFilterSocialPoiBeans().clear();
			if(!urb.getFill_profile().equals("0")) {
				urb.setFill_profile("0");//手动修改成已经填好资料
			}
			Toast.makeText(
					mContext,
					getResources().getString(
							R.string.login_regsited_modify_success),
					Toast.LENGTH_SHORT).show();
			if(!TextUtils.isEmpty(getIntent().getStringExtra("fromClass"))) {
				LoginRegistrationBaseInfoActivity.loginRegistrationBaseInfoActivity.finish();
				finish();
			} else {
				UIUtils.setCurrentFuncID(FuncID.ID_EPG);
				Intent i = new Intent(this, EPGActivity.class);
				startActivity(i);
				LoginRegistrationBaseInfoActivity.loginRegistrationBaseInfoActivity.finish();
				finish();
			}
			
//			new AsyncTask<Void, Void, Void>() {
//
//				@Override
//				protected Void doInBackground(Void... params) {
//					loginImpl.login(urb.getUserName(), urb.getUserPassword());
//					return null;
//				}
//				
//			};

		} else if (result == 2) {
			Toast.makeText(
					mContext,
					getResources().getString(
							R.string.login_regsited_modify_error),
					Toast.LENGTH_LONG).show();
			return;
		}else if(result == Const.USER_NICKNAME_EXISTS) {
			UIUtils.ToastMessage(mContext,R.string.login_regsited_nickname_exists);
			return;
		}
		else if (result == -1) {
			UIUtils.ToastMessage(mContext, R.string.net_error);
		}
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.gallery_star:
			handleClickStarItem(position, view);
			break;
		case R.id.gallery_programm:
			handleClickProgrammItem(position, view);
			break;
		case R.id.gv_category:
			handleClickCategoryItem(position, view);
			break;
		default:
			break;
		}

	}

	private void handleClickStarItem(int position, View view) {
		UserPOIItemBean item = stars.get(position);
//		getItemByKeyAndType(searchStars).setChecked()
		//----------------------------------------------
//		UserPOIItemBean item = searchStars.get(position);
		ImageView iv_selected = (ImageView) view.findViewById(R.id.iv_selected);

		if (item.isChecked()) {
			item.setChecked(false);
			for(UserPOIItemBean userPOIItemBean : searchStars) {
				if(userPOIItemBean.getPoi_key().equals(item.getPoi_key()) && userPOIItemBean.getPoi_type()==item.getPoi_type()) {
					userPOIItemBean.setChecked(false);
				}
			}
			iv_selected.setVisibility(View.INVISIBLE);
			textViewDeleteString(tv_stars, starSb, item.getPoi_text());
			removeItemByKeyAndType(item.getPoi_key(), UserPOIItemBean.STAR_TYPE);
			if (getSelectedCount(UserPOIItemBean.STAR_TYPE) <= 0) {
				tv_stars.setText(R.string.login_registed_not_choose_favorite_star);
			}
//			String msg = getResources().getString(
//					R.string.login_registed_hobby_favorite_canceled)
//					+ "  "
//					+ item.getPoi_text()
//					+ "  "
//					+ getResources().getString(
//							R.string.login_registed_hobby_favorite_remained)
//					+ (MAX_SIZE - getSelectedCount(UserPOIItemBean.STAR_TYPE))
//					+ getResources().getString(
//							R.string.login_registed_hobby_star_remained);
//			Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
		} else {
			if (getSelectedCount(UserPOIItemBean.STAR_TYPE) >= MAX_SIZE) {
				Toast.makeText(
						mContext,
						getResources().getString(
								R.string.login_registed_hobby_favorite_max)
								+ MAX_SIZE
								+ getResources()
										.getString(
												R.string.login_registed_hobby_star_remained),
						Toast.LENGTH_LONG).show();
				return;
			}
			item.setChecked(true);
			for(UserPOIItemBean userPOIItemBean : searchStars) {
				if(userPOIItemBean.getPoi_key().equals(item.getPoi_key()) && userPOIItemBean.getPoi_type()==item.getPoi_type()) {
					userPOIItemBean.setChecked(true);
				}
			}
			iv_selected.setVisibility(View.VISIBLE);
			textViewAddString(tv_stars, starSb, item.getPoi_text());
			userPOIItemBeans.add(item);
//			String msg = getResources().getString(
//					R.string.login_registed_hobby_favorite_selected)
//					+ "  "
//					+ item.getPoi_text()
//					+ "  "
//					+ getResources().getString(
//							R.string.login_registed_hobby_favorite_remained)
//					+ (MAX_SIZE - getSelectedCount(UserPOIItemBean.STAR_TYPE))
//					+ getResources().getString(
//							R.string.login_registed_hobby_star_remained);
//			Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
		}
	}
//	public UserPOIItemBean getItemByKeyAndType(String key,int type,List<UserPOIItemBean> items) {
//		
//	}
	private void handleClickProgrammItem(int position, View view) {
		UserPOIItemBean item = programs.get(position);
		ImageView iv_selected = (ImageView) view.findViewById(R.id.iv_selected);
		if (item.isChecked()) {
			item.setChecked(false);
			for(UserPOIItemBean userPOIItemBean : searchPrograms) {
				if(userPOIItemBean.getPoi_key().equals(item.getPoi_key()) && userPOIItemBean.getPoi_type()==item.getPoi_type()) {
					userPOIItemBean.setChecked(false);
				}
			}
			iv_selected.setVisibility(View.INVISIBLE);
			textViewDeleteString(tv_programs, progSb, item.getPoi_text());
			removeItemByKeyAndType(item.getPoi_key(),
					UserPOIItemBean.PROGRAM_TYPE);

			if (getSelectedCount(UserPOIItemBean.PROGRAM_TYPE) <= 0) {

				tv_programs
						.setText(R.string.login_registed_not_choose_favorite_program);
			}
//			String msg = getResources().getString(
//					R.string.login_registed_hobby_favorite_canceled)
//					+ "  "
//					+ item.getPoi_text()
//					+ "  "
//					+ getResources().getString(
//							R.string.login_registed_hobby_favorite_remained)
//					+ (MAX_SIZE - getSelectedCount(UserPOIItemBean.PROGRAM_TYPE))
//					+ getResources().getString(
//							R.string.login_registed_hobby_program_remained);
//			Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
		} else {
			if (getSelectedCount(UserPOIItemBean.PROGRAM_TYPE) >= MAX_SIZE) {
				Toast.makeText(
						mContext,
						getResources().getString(
								R.string.login_registed_hobby_favorite_max)
								+ MAX_SIZE
								+ getResources()
										.getString(
												R.string.login_registed_hobby_program_remained),
						Toast.LENGTH_LONG).show();
				return;
			}
			item.setChecked(true);
			for(UserPOIItemBean userPOIItemBean : searchPrograms) {
				if(userPOIItemBean.getPoi_key().equals(item.getPoi_key()) && userPOIItemBean.getPoi_type()==item.getPoi_type()) {
					userPOIItemBean.setChecked(true);
				}
			}
			iv_selected.setVisibility(View.VISIBLE);
			textViewAddString(tv_programs, progSb, item.getPoi_text());
			userPOIItemBeans.add(item);
//			String msg = getResources().getString(
//					R.string.login_registed_hobby_favorite_selected)
//					+ "  "
//					+ item.getPoi_text()
//					+ "  "
//					+ getResources().getString(
//							R.string.login_registed_hobby_favorite_remained)
//					+ (MAX_SIZE - getSelectedCount(UserPOIItemBean.PROGRAM_TYPE))
//					+ getResources().getString(
//							R.string.login_registed_hobby_program_remained);
//			Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
		}
	}

	private void handleClickCategoryItem(int position, View view) {
		UserPOIItemBean item = categories.get(position);
		ImageView iv_selected = (ImageView) view.findViewById(R.id.iv_selected);
		if (item.isChecked()) {
			item.setChecked(false);
			for(UserPOIItemBean userPOIItemBean : searchCategories) {
				if(userPOIItemBean.getPoi_key().equals(item.getPoi_key()) && userPOIItemBean.getPoi_type()==item.getPoi_type()) {
					userPOIItemBean.setChecked(false);
				}
			}
			iv_selected.setVisibility(View.INVISIBLE);
			textViewDeleteString(tv_categories, cateSb, item.getPoi_text());
			removeItemByKeyAndType(item.getPoi_key(), UserPOIItemBean.CATE_TYPE);

			if (getSelectedCount(UserPOIItemBean.CATE_TYPE) <= 0) {

				tv_categories
						.setText(R.string.login_registed_not_choose_favorite_content);
			}
//			String msg = getResources().getString(
//					R.string.login_registed_hobby_favorite_canceled)
//					+ "  "
//					+ item.getPoi_text()
//					+ "  "
//					+ getResources().getString(
//							R.string.login_registed_hobby_favorite_remained)
//					+ (MAX_SIZE - getSelectedCount(UserPOIItemBean.CATE_TYPE))
//					+ getResources().getString(
//							R.string.login_registed_hobby_content_remained);
//			Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
		} else {
			if (getSelectedCount(UserPOIItemBean.CATE_TYPE) >= MAX_SIZE) {
				Toast.makeText(
						mContext,
						getResources().getString(
								R.string.login_registed_hobby_favorite_max)
								+ MAX_SIZE
								+ getResources()
										.getString(
												R.string.login_registed_hobby_content_remained),
						Toast.LENGTH_LONG).show();
				return;
			}
			item.setChecked(true);
			for(UserPOIItemBean userPOIItemBean : searchCategories) {
				if(userPOIItemBean.getPoi_key().equals(item.getPoi_key()) && userPOIItemBean.getPoi_type()==item.getPoi_type()) {
					userPOIItemBean.setChecked(true);
				}
			}
			iv_selected.setVisibility(View.VISIBLE);
			textViewAddString(tv_categories, cateSb, item.getPoi_text());
			userPOIItemBeans.add(item);
//			String msg = getResources().getString(
//					R.string.login_registed_hobby_favorite_selected)
//					+ "  "
//					+ item.getPoi_text()
//					+ "  "
//					+ getResources().getString(
//							R.string.login_registed_hobby_favorite_remained)
//					+ (MAX_SIZE - getSelectedCount(UserPOIItemBean.CATE_TYPE))
//					+ getResources().getString(
//							R.string.login_registed_hobby_content_remained);
//			Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
		}

	}

	public void initDatas() {
		new AsyncTask<Void, Void, Void>() {
			protected void onPreExecute() {
				ShowProgressDialog.show2(mContext,R.string.login_regsited_interest_load_data, null);
			};

			@Override
			protected Void doInBackground(Void... params) {
				stars = loginImpl.getPoiStarsInfo(urb);
				searchStars = loginImpl.getPoiStarsInfo(urb);
				programs = loginImpl.getPoiProgramsInfo(urb);
				searchPrograms = loginImpl.getPoiProgramsInfo(urb);
				categories = loginImpl.getPoiCategoriesInfo(urb);
				searchCategories = loginImpl.getPoiCategoriesInfo(urb);
				if(stars==null || programs==null || categories==null || searchStars==null || searchPrograms==null || searchCategories==null) {
					return null;
				}
				initSelectedPoiItem(stars);
				initSelectedPoiItem(searchStars);
				initSelectedPoiItem(programs);
				initSelectedPoiItem(searchPrograms);
				initSelectedPoiItem(categories);
				initSelectedPoiItem(searchCategories);
				return null;
			}

			protected void onPostExecute(Void result) {
				if(stars==null || programs==null || categories==null || searchStars==null || searchPrograms==null || searchCategories==null) {
					UIUtils.ToastMessage(mContext, R.string.net_error);
					return;
				}
				initTextView();
				starAdapter = new PoiItemAdapter2(mContext, stars,
						PoiItemAdapter2.GALLERY_TYPE);
				gallery_star.setAdapter(starAdapter);
				programAdapter = new PoiItemAdapter2(mContext, programs,
						PoiItemAdapter2.GALLERY_TYPE);
				gallery_programm.setAdapter(programAdapter);
				categoryAdapter = new PoiItemAdapter2(mContext, categories,
						PoiItemAdapter2.GRIDVIEW_TYPE);
				gv_category.setAdapter(categoryAdapter);
				ShowProgressDialog.dismiss();
			};
		}.execute();
	}

	private void initTextView() {// 初始化textView
		for (String str : getSelectedNames(stars)) {
			textViewAddString(tv_stars, starSb, str);
		}
		for (String str : getSelectedNames(programs)) {
			textViewAddString(tv_programs, progSb, str);
		}
		for (String str : getSelectedNames(categories)) {
			textViewAddString(tv_categories, cateSb, str);
		}
	}

	public void textViewAddString(TextView tv, StringBuilder sb, String str) {
		sb.append(str + "/");
		tv.setText(sb.toString());
	}

	public void textViewDeleteString(TextView tv, StringBuilder sb, String str) {
		String sbStr = sb.toString();
		if (sbStr.contains(str)) {
			int start = sbStr.indexOf(str);
			sb.delete(start, start + str.length() + 1);
		}
		tv.setText(sb.toString());
	}

	protected void initSelectedPoiItem(List<UserPOIItemBean> poiItemBeans) {// 初始化用户之前已经选中的item
		List<String> keys = null;
		if(userPOIItemBeans!=null) {
			keys = getSelectedKeys(userPOIItemBeans);
		}
		if(keys.size()>0) {
			for (UserPOIItemBean poiItemBean : poiItemBeans) {
				if (keys.contains(poiItemBean.getPoi_key())) {
					poiItemBean.setChecked(true);
				}
			}
		}
//		for (UserPOIItemBean poiItemBean : poiItemBeans) {
//			if(userPOIItemBeans!=null) {
//				List<String> keys = getSelectedKeys(userPOIItemBeans);
//				if (keys.contains(poiItemBean.getPoi_key())) {
//					poiItemBean.setChecked(true);
//				}
//			}
//		}
	}

	public List<String> getSelectedKeys(List<UserPOIItemBean> beans) {// 获得用户选好的keys
		List<String> keys = new ArrayList<String>();
		for (UserPOIItemBean userPOIItemBean : beans) {
			if (userPOIItemBean.isChecked()) {
				keys.add(userPOIItemBean.getPoi_key());
			}
		}
		return keys;
	}

	public List<String> getSelectedNames(List<UserPOIItemBean> beans) {// 获得用户选好的names
		List<String> keys = new ArrayList<String>();
		for (UserPOIItemBean userPOIItemBean : beans) {
			if (userPOIItemBean.isChecked()) {
				keys.add(userPOIItemBean.getPoi_text());
			}
		}
		return keys;
	}

	public List<UserPOIItemBean> getSelectedItems(List<UserPOIItemBean> beans) {// 获得用户选好的items
		List<UserPOIItemBean> items = new ArrayList<UserPOIItemBean>();
		for (UserPOIItemBean userPOIItemBean : beans) {
			if (userPOIItemBean.isChecked()) {
				items.add(userPOIItemBean);
			}
		}
		return items;
	}

	public int getSelectedCount(int type) {// 获得已经选择的感兴趣的数目
		int count = 0;
		for (UserPOIItemBean item : userPOIItemBeans) {
			if (item.getPoi_type() == type) {
				count++;
			}
		}
		return count;
	}

	public List<UserPOIItemBean> searchItemByName(String name,
			List<UserPOIItemBean> items) {
		List<UserPOIItemBean> result = new ArrayList<UserPOIItemBean>();
		for (UserPOIItemBean item : items) {
			if (item.getPoi_text().equals(name)) {
				result.add(item);
			}
		}
		return result;
	}

	public void removeItemByKeyAndType(String key, int type) {
		for (UserPOIItemBean poiItemBean : userPOIItemBeans) {
			if (poiItemBean.getPoi_type() == type
					&& poiItemBean.getPoi_key().equals(key)) {
				userPOIItemBeans.remove(poiItemBean);
				break;
			}
		}
	}

	public List<String> getKeysFromSelected(int type) {
		List<String> keys = new ArrayList<String>();
		for (UserPOIItemBean item : userPOIItemBeans) {
			if (item.getPoi_type() == type) {
				keys.add(item.getPoi_key());
			}
		}
		return keys;
	}
	private void handleWeiBo() {
		if(urb.isBoundWeibo()) {
			unFastenWeibo();
		} else {
			String access_token = sp.getString("access_token", "");
			String uid = sp.getString("uid", "");
			if(TextUtils.isEmpty(access_token) || TextUtils.isEmpty(uid)) {//当用户没有登陆过微博时
				loginByWeibo();
			} else {
				fastenWeibo(uid, access_token);
			}
		}
	}
	private void fastenWeibo(final String uid, final String access_token) {
		new AsyncTask<Void, Void, Integer>() {
			protected void onPreExecute() {
				ShowProgressDialog.show2(mContext,R.string.login_registed_isfastening_weibo, null);
			};
			@Override
			protected Integer doInBackground(Void... params) {
				int result = loginImpl.fastenWeibo(access_token, uid);
				return result;
			}
			protected void onPostExecute(Integer result) {
				ShowProgressDialog.dismiss();
				if(result==0) {//微博绑定成功
					urb.setBoundWeibo(true);
					iv_fasten_weibo.setImageResource(R.drawable.login_register_personal_interest_survey_micro_blog_binding);
					Toast.makeText(mContext, R.string.login_registed_fasten_weibo_success, Toast.LENGTH_LONG).show();
				} else if(result==2) {//微博绑定失败
					Toast.makeText(mContext, R.string.login_registed_fasten_weibo_fail, Toast.LENGTH_LONG).show();
				}else if (result == -1) {
					UIUtils.ToastMessage(mContext, R.string.net_error);
				}
			};
		}.execute();
	}

	private void loginByWeibo() {
		CookieSyncManager.createInstance(getApplicationContext()); 
		CookieSyncManager.getInstance().startSync(); 
		CookieManager.getInstance().removeAllCookie();//清除浏览器缓存
		weibo = Weibo.getInstance(Const.CONSUMER_KEY, URLConfig.REDIRECT_URL);
		weibo.authorize(mContext, new MyWeiboAuthListener());
	}
	private class MyWeiboAuthListener implements WeiboAuthListener{

		@Override
		public void onCancel() {
			Toast.makeText(mContext, R.string.login_weibo_cancel_auth, Toast.LENGTH_LONG).show();
		}

		@Override
		public void onComplete(Bundle bundle) {
			Toast.makeText(mContext, R.string.login_weibo_success, Toast.LENGTH_LONG).show();
			final String uid = bundle.getString("uid");
			final String access_token = bundle.getString("access_token");
			if(!TextUtils.isEmpty(access_token)) {
				editor.putString("access_token", access_token);//存放取得的access_token
				editor.commit();
			}
			if(!TextUtils.isEmpty(uid)){
				editor.putString("uid", uid);
				editor.commit();
			}
			if(!TextUtils.isEmpty(uid)&& !TextUtils.isEmpty(access_token)) {
				fastenWeibo(uid, access_token);
			} 
		}



		@Override
		public void onError(WeiboDialogError arg0) {
//			Toast.makeText(mContext, R.string.login_weibo_error, Toast.LENGTH_LONG).show();
			
		}

		@Override
		public void onWeiboException(WeiboException arg0) {
//			Toast.makeText(mContext, R.string.login_weibo_error, Toast.LENGTH_LONG).show();
		}
		
	}
	private void unFastenWeibo() {//解除绑定新浪微博
			CookieSyncManager.createInstance(getApplicationContext()); 
			CookieSyncManager.getInstance().startSync(); 
			CookieManager.getInstance().removeAllCookie();//清除浏览器缓存
			
			
			new AsyncTask<Void, Void, Integer>() {
				protected void onPreExecute() {
					ShowProgressDialog.show2(mContext,R.string.login_registed_isunfastening_weibo, null);
				};
				@Override
				protected Integer doInBackground(Void... params) {
					int result = loginImpl.unFastenWeibo();
					return result;
				}
				protected void onPostExecute(Integer result) {
					ShowProgressDialog.dismiss();
					if(result==0) {//解除微博绑定成功
						editor.remove("access_token");
						editor.remove("uid");
						editor.commit();
						iv_fasten_weibo.setImageResource(R.drawable.login_registed_weibo_normal);
						urb.setBoundWeibo(false);
						Toast.makeText(mContext, R.string.login_registed_unfasten_weibo_success, Toast.LENGTH_LONG).show();
					} else if(result==2) {//解除微博绑定失败
						Toast.makeText(mContext, R.string.login_registed_unfasten_weibo_fail, Toast.LENGTH_LONG).show();
					}else if (result == -1) {
						UIUtils.ToastMessage(mContext, R.string.net_error);
					}
				};
			}.execute();
		
	}

	public List<UserPOIItemBean> getStars() {
		return stars;
	}

	public void setStars(List<UserPOIItemBean> stars) {
		this.stars = stars;
	}

	public List<UserPOIItemBean> getPrograms() {
		return programs;
	}

	public void setPrograms(List<UserPOIItemBean> programs) {
		this.programs = programs;
	}

	public List<UserPOIItemBean> getCategories() {
		return categories;
	}

	public void setCategories(List<UserPOIItemBean> categories) {
		this.categories = categories;
	}
	@Override
	protected void onStop() {
		ShowProgressDialog.dismiss();
		super.onStop();
	}
}
