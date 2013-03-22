package com.igrs.tivic.phone.Activity;

import java.util.ArrayList;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Global.Const;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
/**
 * 引导页
 */
public class GuideActivity extends Activity implements OnPageChangeListener ,OnTouchListener{
	private ViewPager guideViewPager;
	private ArrayList<View> pageView;
	private ImageView[] dotViews;
	private LayoutInflater mInflater;
	private Integer[] textViews;
	private Integer[] imageView;
	private int count = 4;//引导页面 个数
	private ImageView dotView;
	private int lastX = 0;
	private int currentIndex;
	private SharedPreferences sp;
	private boolean guideAlready;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide_viewpager);
		mInflater = LayoutInflater.from(this);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		boolean guide = sp.getBoolean("guide_on", true);
		if(guide) {
			initView();
		} else {
			Intent intent = new Intent(GuideActivity.this,LoginActivity.class); 
			startActivity(intent);
			finish();
		}
	}

	private void initView() {
		
		if(pageView == null){
			pageView = new ArrayList<View>();
		}
		textViews = new Integer[]{R.drawable.guide1_epg_wenzi,R.drawable.guide2_pgc_wenzi,R.drawable.guide3_ugc_wenzi,R.drawable.guide4_jieping_wenzi};
		imageView = new Integer[]{R.drawable.guide1_epg,R.drawable.guide2_pgc,R.drawable.guide3_ugc,R.drawable.guide4_jieping};
		LinearLayout dots_layout = (LinearLayout) findViewById(R.id.dots_layout);
		dotViews = new ImageView[count];
		for (int i = 0; i < count; i++) {
			View guide_viewpager_item = mInflater.inflate(R.layout.guide_viewpage_item, null);
			ImageView exit_imageView = (ImageView) guide_viewpager_item.findViewById(R.id.exit_imageview);
			exit_imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//add 
					setGuided();
					Intent intent = new Intent(GuideActivity.this,LoginActivity.class); 
					startActivity(intent);
					GuideActivity.this.finish();
				}
			});
			guide_viewpager_item.findViewById(R.id.text_view).setBackgroundResource(textViews[i]);
			guide_viewpager_item.findViewById(R.id.image_main).setBackgroundResource(imageView[i]);
			
			pageView.add(guide_viewpager_item);
			//设置选项圆点
			dotView = new ImageView(this);
			dotView.setLayoutParams(new LayoutParams(20, 20));
			dotView.setPadding(20, 0, 20, 0);
			dotViews[i] = dotView;
			
			if(i == 0){
				dotViews[i].setBackgroundResource(R.drawable.viewpager_dot_focused);
			}else{
				dotViews[i].setBackgroundResource(R.drawable.viewpager_dot_normal);
			}
			dots_layout.addView(dotViews[i]);
		}
		guideViewPager = (ViewPager) findViewById(R.id.guidePages);
		GuidesPagerAdapter guiderPagerAdapter = new GuidesPagerAdapter();
		guideViewPager.setAdapter(guiderPagerAdapter);
		guideViewPager.setOnPageChangeListener(this);
		guideViewPager.setOnTouchListener(this);
	}
	class GuidesPagerAdapter extends PagerAdapter{
		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager)container).removeView(pageView.get(position));
		}
		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager)container).addView(pageView.get(position));
			return pageView.get(position);
		}
		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}
		@Override
		public int getCount() {
			return count;
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		currentIndex = arg0;
		for (int i = 0; i < dotViews.length; i++) {
			dotViews[arg0].setBackgroundResource(R.drawable.viewpager_dot_focused);
			if(i != arg0){
				dotViews[i].setBackgroundResource(R.drawable.viewpager_dot_normal);
			}
		}
	}
	private void setGuided(){
		sp.edit().putBoolean("guide_on", false).commit();
    }
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastX  = (int) event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			if((lastX - event.getX() > 100 && currentIndex == count - 1)){
				if(!guideAlready) {
					guideAlready = true;
//					System.out.println("wuwuwuwuwuwu");
					//add
					setGuided();
					Intent intent = new Intent(GuideActivity.this,LoginActivity.class); 
					startActivity(intent);
					finish();

				}
			}
			break;
		default:
			break;
		}
		return false;
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
//		setGuided();//取消引导
	}
}
