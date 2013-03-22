package com.igrs.tivic.phone.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Adapter.SocialUserLabelAdapter;
import com.igrs.tivic.phone.Bean.Login.UserPOIItemBean;
import com.igrs.tivic.phone.Bean.Social.SocialPoiBean;
import com.igrs.tivic.phone.Global.ShowProgressDialog;
import com.igrs.tivic.phone.Impl.SocialFriendsDetailsImpl;
import com.igrs.tivic.phone.Utils.UIUtils;

public class SocialUsrInfoInterestFragment extends Fragment {
	private Context mContext;
	private GridView gv_friend_data;
	private ArrayList<SocialPoiBean> beans = new ArrayList<SocialPoiBean>();
	private SocialUserLabelAdapter adapter;
//add by zhanglr
	private int currentUid = 0;
	private final String TAG = "SocialUsrInfoInterestFragment";
	private SocialFriendsDetailsImpl sfdi = SocialFriendsDetailsImpl.getInstance();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		mContext = getActivity();
		Bundle bundle = this.getArguments();
		if(bundle != null)
			currentUid = bundle.getInt("uid");
		UIUtils.Logd(TAG, "Bundle uid = " + currentUid);
		super.onCreate(savedInstanceState);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = View.inflate(mContext.getApplicationContext(),R.layout.social_base_interest, null);
		gv_friend_data = (GridView) view.findViewById(R.id.social_userinterest_gridview);
		
		initFriendData();
		
		return view;
	}


	private void initFriendData() {
	new AsyncTask<Void, Void, ArrayList<UserPOIItemBean>>() {
			protected void onPreExecute() {
				ShowProgressDialog.show2(mContext,R.string.login_registed_handle, null);
			}

			@Override
			protected ArrayList<UserPOIItemBean> doInBackground(Void... params) {
				ArrayList<UserPOIItemBean> userPOIItemBeans = sfdi.getFriendInfo(currentUid);
				return userPOIItemBeans;
			};
			protected void onPostExecute(ArrayList<UserPOIItemBean> result) {
				ShowProgressDialog.dismiss();
				handleResult(result);
			};
		}.execute();
	}




	protected void handleResult(ArrayList<UserPOIItemBean> result) {
		if(result == null) {
			UIUtils.ToastMessage(mContext, R.string.net_error);
			return;
		}
		for(UserPOIItemBean bean : result) {//处理年龄
			if(bean.getPoi_key().equals("age")) {
				initBirthday(bean.getPoi_value()+"");
				break;
			}
		}
		for(UserPOIItemBean bean : result) {//处理性别
			if(bean.getPoi_key().equals("gender")) {
				initGender(bean.getPoi_value());
				break;
			}
		}
		for(UserPOIItemBean bean : result) {//处理星座
			if(bean.getPoi_key().equals("astroid")) {
				initStar(bean.getPoi_value());
				break;
			}
		}
		for(UserPOIItemBean bean : result) {//处理感兴趣的明星和节目
			if(bean.getPoi_key().equals("star_id")) {
				SocialPoiBean starBean = new SocialPoiBean(SocialPoiBean.STAR_TYPE, bean.getPoi_icon());
				beans.add(starBean);
			} else if(bean.getPoi_key().equals("program_id")) {
				SocialPoiBean proBean = new SocialPoiBean(SocialPoiBean.PROGRAM_TYPE, bean.getPoi_icon());
				beans.add(proBean);
			} else if(bean.getPoi_key().equals("category_id")) {
				SocialPoiBean cateBean = new SocialPoiBean(SocialPoiBean.CATEGORY_TYPE, bean.getPoi_icon());
				beans.add(cateBean);
			}
		}
//		for(UserPOIItemBean bean : result) {
//			System.out.println("bean.getPoi_key()="+bean.getPoi_key());
//		}
		adapter = new SocialUserLabelAdapter(mContext, beans, SocialUserLabelAdapter.OTHER_TYPE);
		gv_friend_data.setAdapter(adapter);
	}
	private void initStar(int key) {
		int[] constellation = { R.drawable.social_date_xingzuo_baiyang,
				R.drawable.social_date_xingzuo_jinniu,
				R.drawable.social_date_xingzuo_shuangzi,
				R.drawable.social_date_xingzuo_juxie,
				R.drawable.social_date_xingzuo_shizi,
				R.drawable.social_date_xingzuo_chunv,
				R.drawable.social_date_xingzuo_tianping,
				R.drawable.social_date_xingzuo_tianxie,
				R.drawable.social_date_xingzuo_sheshou,
				R.drawable.social_date_xingzuo_mojie,
				R.drawable.social_date_xingzuo_shuiping,
				R.drawable.social_date_xingzuo_shuangyu };
		SocialPoiBean constellBean = new SocialPoiBean(SocialPoiBean.CONSTELLATION_TYPE, constellation[key]+"");
		beans.add(constellBean);
	}
	private void initGender(int gender) {
		SocialPoiBean genderBean = new SocialPoiBean();
		genderBean.setType(SocialPoiBean.GENDER_TYPE);
		if (gender == 0) {
			genderBean.setUrl(R.drawable.social_xingbie_nv + "");
		} else {
			genderBean.setUrl(R.drawable.social_xingbie_nan + "");
		}
		beans.add(genderBean);
	}
	private void initBirthday(String birth) {
		long time = Long.parseLong(birth);
		String dateStr = string2unixTimestamp(time);
		String birthStr = dateStr.split(" ")[0];
		String[] strs = birthStr.split("-");
		int years = Integer.parseInt(strs[0]);
		int age = Integer.parseInt((years + "").charAt(2) + "");
		SocialPoiBean ageBean = null;
		if (age == 0) {
			ageBean = new SocialPoiBean(SocialPoiBean.AGE_TYPE,
					R.drawable.social_age_00 + "");
		} else if (age == 1) {
			ageBean = new SocialPoiBean(SocialPoiBean.AGE_TYPE,
					R.drawable.social_age_10 + "");
		} else if (age == 6) {
			ageBean = new SocialPoiBean(SocialPoiBean.AGE_TYPE,
					R.drawable.social_age_60 + "");
		} else if (age == 7) {
			ageBean = new SocialPoiBean(SocialPoiBean.AGE_TYPE,
					R.drawable.social_age_70 + "");
		} else if (age == 8) {
			ageBean = new SocialPoiBean(SocialPoiBean.AGE_TYPE,
					R.drawable.social_age_80 + "");
		} else if (age == 9) {
			ageBean = new SocialPoiBean(SocialPoiBean.AGE_TYPE,
					R.drawable.social_age_90 + "");
		} else {
			ageBean = new SocialPoiBean(SocialPoiBean.AGE_TYPE,
					R.drawable.social_age_60 + "");
//			ageBean = new SocialPoiBean(SocialPoiBean.AGE_TYPE,
//					R.drawable.social_touxiang_xitongtouxiang + "");
		}

		beans.add(ageBean);
	}
	public String string2unixTimestamp(long utime) {
		Date date = new Date(utime * 1000);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		return sdf.format(date);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
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
}