package com.lq.viewpagerloadingtest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class FirstGrideActivity extends Activity {
	private ArrayList<String> StringList = new ArrayList<String>();
//	private int listSize = 15;
	private String[] images_urls = {
			"http://share.baidu.com/static/web/img/imgshare/preview_img_small.jpg?v=e0f4900b.jpg",
			"http://www.wowstar.info/TuPian/imagesALL/200803/200832102841839.jpg",
			"http://news.hainan.net/Editor/UploadFile08/2009w10r23f/20091023173826767.jpg",
			"http://pic10.nipic.com/20101031/3320946_122138507000_2.jpg",
			"http://news.replays.net/Uploads/photo/20100611/201006111057344381.jpg" };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gridelayout);
		initView();
	}

	private void initView() {
		StringList = (ArrayList<String>) Arrays.asList(images_urls);
		StringList.addAll(StringList);
		StringList.addAll(StringList);
//		for (int i = 0; i < listSize; i++) {
//			StringList.add(i, "this is : "+i);
//		}
		final GridView gridView = (GridView) findViewById(R.id.gridview);
		MyGrideAdapter myGrideAdapter = new MyGrideAdapter();
		gridView.setAdapter(myGrideAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
//				bundle.putInt("grideSize", gridView.getCount());
				bundle.putInt("currentId", arg2);
				bundle.putStringArrayList("StringList", StringList);
				intent.putExtras(bundle);
				intent.setClass(FirstGrideActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
	}
	public class MyGrideAdapter extends BaseAdapter{

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tView;
			if(convertView == null){
				tView = new TextView(FirstGrideActivity.this);
				tView.setText(StringList.get(position));
				tView.setHeight(85);
				tView.setWidth(85);
//				tView.setBackgroundColor(android.R.color.holo_red_light);
				tView.setBackgroundResource(R.drawable.ic_launcher);
				tView.setGravity(Gravity.CENTER);
			}else{
				tView = (TextView) convertView;
			}
			
			return tView;
		}

		@Override
		public int getCount() {
			return StringList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}
		
		
	}

}
