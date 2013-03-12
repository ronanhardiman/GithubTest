package zp.picture;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * @author zhouping
 * 
 */
public class PicViewActivity extends Activity {
	private static final String TAG = "PicViewActivity";

//	private MyPagerAdapter myAdapter;
	private MyPagerAdapter2 myAdapter;
	private ViewPager myViewPager;
	private List<View> listViews;
	private int firstId = 1;
	Context mContext;

	private String[] urls = {
			"http://share.baidu.com/static/web/img/imgshare/preview_img_small.jpg?v=e0f4900b.jpg",
			"http://www.wowstar.info/TuPian/imagesALL/200803/200832102841839.jpg",
			"http://news.hainan.net/Editor/UploadFile08/2009w10r23f/20091023173826767.jpg",
			"http://pic10.nipic.com/20101031/3320946_122138507000_2.jpg",
			"http://news.replays.net/Uploads/photo/20100611/201006111057344381.jpg" };

	private LayoutInflater mInflater;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContext = this;
		mInflater = LayoutInflater.from(mContext);
		setContentView(R.layout.activity_pic);

		listViews = new ArrayList<View>();
		if (null == urls) {
			finish();
		}
		initData();
	}
	//chushihua  data
	private void initData() {
		HttpClient.getOriginalPic(urls[firstId], firstId, mHandler);
		changeProgressBar(true);
		myAdapter = new MyPagerAdapter2(listViews);
		myViewPager = (ViewPager) findViewById(R.id.myMesPager);
		myViewPager.setAdapter(myAdapter);
		myViewPager.setCurrentItem(firstId);
		
	}

	private void changeProgressBar(boolean b) {
		if(b){
			((ProgressBar)findViewById(R.id.progress)).setVisibility(View.VISIBLE);
		}else{
			((ProgressBar)findViewById(R.id.progress)).setVisibility(View.GONE);
		}
		
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				
				if(listViews ==null || 0 ==listViews.size())
					return;

				// ͼƬ��ȡ��ɺ���м��أ�����ProgressBar
				

				int p = msg.arg1;
				Bitmap b = msg.obj == null ? null : (Bitmap) msg.obj;
				View v = (listViews.get(p));

				((ImageView) v.findViewById(R.id.pic)).setImageBitmap(b);
				((ProgressBar) v.findViewById(R.id.progress))
						.setVisibility(View.GONE);

				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();

//		pageswitch();
	}

	private void pageswitch() {
		listViews.clear();

		int max = urls.length;
		for (int i = 0; i < max; i++) {
			listViews.add(addView());
		}

//		myAdapter = new MyPagerAdapter(listViews);
		myViewPager = (ViewPager) findViewById(R.id.myMesPager);
		myViewPager.setAdapter(myAdapter);
		myViewPager.setCurrentItem(0);

		myViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// ������ǰ��ͼ���ȡͼƬ
				print("onPageSelected:" + arg0 + " " + urls[arg0]);
				String url = urls[arg0];
				HttpClient.getOriginalPic(url, arg0, mHandler);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// ��1��2��������1����ǰ����
				// print("onPageScrolled:" + arg0 + " " + arg1 + " " + arg2);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// Log.d("k", "onPageScrollStateChanged - " + arg0);
				// ״̬������0���У�1�����ڻ����У�2Ŀ��������
				print("onPageScrollStateChanged:" + arg0);

			}
		});

		// ��ʼ��ȡ��һ��ͼ
		HttpClient.getOriginalPic(urls[0], 0, mHandler);

	}

	private View addView() {
		return mInflater.inflate(R.layout.item_pic, null);
	}

	private void print(String msg) {
		Log.d(TAG, msg);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			listViews.clear();
			finish();
		}

		return super.onKeyDown(keyCode, event);
	}

}
