package com.lq.viewpagerloadingtest;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
public class MainActivity extends Activity {

	private ViewPager viewpager;
	private MyViewPagerAdapter mAdapter;
//	private ArrayList<String> StringArrayList = new ArrayList<String>();	
	private ArrayList<String> StringList;	
	private int initSize = 5; 
	private int listSize;	
	private int currentId ;	
	private int currentViewpagerID ;	
	
	
	public int getCurrentViewpagerID() {
		return currentViewpagerID;
	}

	public void setCurrentViewpagerID(int currentViewpagerID) {
		this.currentViewpagerID = currentViewpagerID;
	}

	public int getListSize() {
		return listSize;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent intent = getIntent();
		currentId = intent.getIntExtra("currentId", 0);
		listSize = intent.getIntExtra("grideSize", 20);
		StringList = intent.getStringArrayListExtra("StringList");
		initView();
	}

	private void initView() {
		
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		int leftViewId =currentId - 2;
		if(leftViewId < 0 ) leftViewId = 0 ;
		int rightViewId = leftViewId + initSize;
		if(rightViewId > StringList.size()) rightViewId = StringList.size();
		
		mAdapter = new MyViewPagerAdapter(this);
		mAdapter.setDate(leftViewId,rightViewId);
		mAdapter.setList(StringList);
		viewpager.setAdapter(mAdapter);
		viewpager.setCurrentItem(currentId);
		currentViewpagerID = viewpager.getCurrentItem();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void initData(int position){
		int len = position + 1;
		StringList.set(len, "loading : "+len);
		mAdapter.setList(StringList);
		mAdapter.notifyDataSetChanged();
	}
	
	
	public ArrayList<String> initData(int size,int position){
//		listSize = listSize + size;
		int leftId = currentViewpagerID + 1;
		if(leftId < 0 ) leftId = 0;
		if(leftId > StringList.size()) leftId = StringList.size();
		int rightId = leftId + size;
		if(rightId > StringList.size()) rightId = StringList.size();
		List<String> sList = StringList.subList(leftId, rightId);
		
		for (int i = 0; i < sList.size(); i++) {
			StringList.set(leftId, sList.get(i)+"!!");
		}
//		initSize = initSize + size;
		return StringList;
	}
	
}
