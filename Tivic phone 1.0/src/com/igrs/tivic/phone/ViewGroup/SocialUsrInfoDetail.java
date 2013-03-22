package com.igrs.tivic.phone.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Adapter.SocialUserLabelAdapter;
import com.igrs.tivic.phone.Bean.Login.UserRegisterBean;
import com.igrs.tivic.phone.Bean.Social.SocialPoiBean;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Impl.LoginImpl;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.widget.AsyncImageView;
import com.umeng.analytics.i;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;

public class SocialUsrInfoDetail extends SocialUsrInfoDetailBase {
	private ArrayList<SocialPoiBean> beans = new ArrayList<SocialPoiBean>();
	private UserRegisterBean urb = TivicGlobal.getInstance().userRegisterBean;
	private GridView gv;
	private SocialUserLabelAdapter adapter;
	private List<SocialPoiBean> socialPoiBeans = urb.getFilterSocialPoiBeans();
	private LoginImpl loginImpl = new LoginImpl();
	public SocialUsrInfoDetail(Context context, AttributeSet attrs) {
		super(context, attrs);
//		initData();
		new AsyncTask<Void, Void, Integer>() {

			@Override
			protected Integer doInBackground(Void... params) {
				int result1 = loginImpl.getMyInfo();
					return result1;
			}
			protected void onPostExecute(Integer result) {
				if(result == -1) {
					UIUtils.ToastMessage(getContext(), R.string.net_error);
					return;
				} else if(result == 0) {
					initData();
				}
			};
		}.execute();
	}

	public SocialUsrInfoDetail(Context context) {
		this(context,null);
	}

	private void initData() {
		if(!TextUtils.isEmpty(urb.getUserBirthday())) {
			initBirthday();
		}
		initGender();
		initPoiItems();
	}

	private void initSinaWeibo() {
		if (urb.isBoundWeibo()) {
			SocialPoiBean bean = new SocialPoiBean(
					SocialPoiBean.SINAWEIBO_TYPE,
					R.drawable.social_bangding_weibo + "");
			beans.add(bean);
		}
	}

	private void initPoiItems() {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				new LoginImpl().getExtInfo(beans);
				return null;
			}

			protected void onPostExecute(Void result) {
				initSinaWeibo();
				initView();
			};
		}.execute();
	}

	public void initView() {
		View view = View.inflate(getContext(), R.layout.social_base_interest,
				null);
		gv = (GridView) view.findViewById(R.id.social_userinterest_gridview);
		adapter = new SocialUserLabelAdapter(getContext(), beans,SocialUserLabelAdapter.FILTER_TYPE);
		gv.setAdapter(adapter);
		addView(view);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Map<String,Integer> map = urb.getFilterItems();
				Set<Entry<String,Integer>> set = map.entrySet();
				for(Map.Entry<String, Integer> entry : set) {
					String key = entry.getKey();
					int value = map.get(key);
//					UIUtils.Logi("chen", "key=="+key+":value=="+value);
				}
				for(SocialPoiBean ss : urb.getFilterSocialPoiBeans()) {
//					UIUtils.Logi("chen", "type=="+ss.getType()+":value=="+ss.getValue());
				}
				SocialPoiBean spb = beans.get(position);
				ImageView iv_background = (ImageView) view.findViewById(R.id.iv_background);
				switch (spb.getType()) {
				case SocialPoiBean.AGE_TYPE:
					if(urb.getFilterItems().containsKey(SocialUserLabelAdapter.AGE)) {
						urb.getFilterItems().remove(SocialUserLabelAdapter.AGE);
					} else {
						urb.getFilterItems().put(SocialUserLabelAdapter.AGE, getYear());
					}
					break;
//				case SocialPoiBean.CATEGORY_TYPE:
//					
//					break;
				case SocialPoiBean.CONSTELLATION_TYPE:
					if(urb.getFilterItems().containsKey(SocialUserLabelAdapter.STAR)) {
						urb.getFilterItems().remove(SocialUserLabelAdapter.STAR);
					} else {
						urb.getFilterItems().put(SocialUserLabelAdapter.STAR, urb.getUserStars());
					}
					break;
				case SocialPoiBean.PROGRAM_TYPE:
				case SocialPoiBean.CATEGORY_TYPE:
				case SocialPoiBean.STAR_TYPE:
					if(socialPoiBeans.size()>0) {
							if(isContainBean(socialPoiBeans, spb)) {
								removeBean(socialPoiBeans, spb);
							} else {
								socialPoiBeans.add(spb);
							}
					} else {
						socialPoiBeans.add(spb);
					}
					break;
				case SocialPoiBean.GENDER_TYPE:
					if(urb.getFilterItems().containsKey(SocialUserLabelAdapter.GENDER)) {
						urb.getFilterItems().remove(SocialUserLabelAdapter.GENDER);
					} else {
						urb.getFilterItems().put(SocialUserLabelAdapter.GENDER, urb.getUserGender());
					}
					break;
				case SocialPoiBean.SINAWEIBO_TYPE:
					iv_background.setImageResource(R.drawable.social_bangding_selected);
					break;
//				case SocialPoiBean.STAR_TYPE:
//					if(socialPoiBeans.size()>0) {
//						if(isContainBean(socialPoiBeans, spb)) {
//							removeBean(socialPoiBeans, spb);
//						} else {
//							socialPoiBeans.add(spb);
//						}
//					} else {
//						socialPoiBeans.add(spb);
//					}
//					break;
				default:
					break;
				}
				adapter.notifyDataSetChanged();
			}
		});
	}

	private void initGender() {
		int gender = urb.getUserGender();
		SocialPoiBean genderBean = new SocialPoiBean();
		genderBean.setType(SocialPoiBean.GENDER_TYPE);
		if (gender == 0) {
			genderBean.setUrl(R.drawable.social_xingbie_nv + "");
		} else {
			genderBean.setUrl(R.drawable.social_xingbie_nan + "");
		}
		beans.add(genderBean);
	}

	private void initBirthday() {
		long time = Long.parseLong(urb.getUserBirthday());
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
//			ageBean = new SocialPoiBean(SocialPoiBean.AGE_TYPE,
//					R.drawable.social_touxiang_xitongtouxiang + "");
			ageBean = new SocialPoiBean(SocialPoiBean.AGE_TYPE,
					R.drawable.social_age_60 + "");
		}

		beans.add(ageBean);
	}
	private int getYear() {
		long time = Long.parseLong(urb.getUserBirthday());
		String dateStr = string2unixTimestamp(time);
		String birthStr = dateStr.split(" ")[0];
		String[] strs = birthStr.split("-");
		int years = Integer.parseInt(strs[0]);
		String str = years+"";
		StringBuilder sb = new StringBuilder(str);
		sb.deleteCharAt(str.length()-1);
		sb.append("0");
		return Integer.parseInt(sb.toString());
	}
	public String string2unixTimestamp(long utime) {
		Date date = new Date(utime * 1000);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		return sdf.format(date);
	}
	private boolean isContainBean(List<SocialPoiBean> list,SocialPoiBean item) {
		for(int i=0; i<list.size(); i++) {
			SocialPoiBean bean = list.get(i);
			if(bean.getValue() == item.getValue()) {
				return true;
			}
		}
		return false;
	}
	private void removeBean(List<SocialPoiBean> list,SocialPoiBean item) {
		for(int i=0; i<list.size(); i++) {
			SocialPoiBean bean = list.get(i);
			if(bean.getValue() == item.getValue()) {
				list.remove(bean);
				break;
			}
		}
	}
}
