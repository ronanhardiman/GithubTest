package com.lq.viewpagerloadingtest;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
/**
 * viewpager 动态加载 
 * 首次进入加载左右5条数据 , 左右滑动的时候 根据有无数据 继续加载 前后的数据.
 * @author liq
 *
 */
public class MainActivity extends Activity {

	private ViewPager viewpager;
	private MyViewPagerAdapter mAdapter;
//	private ArrayList<String> StringArrayList = new ArrayList<String>();	//viewpager 实际加载的大小
	private ArrayList<String> StringList;	//viewpager 总大小
	private int initSize = 5; //初始化 viewpager 的大小, < 5;
	private int listSize;	//viewpage 总的大小
	private int currentId ;	//点击的view id
	private int currentViewpagerID ;	//当前显示的 viewpager ID
	
	
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
		//初始化 viewpager 大小
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
		currentViewpagerID = viewpager.getCurrentItem();//当前显示的viewpager  的id
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	/**
	 * 动态加载一个数据
	 * @param position
	 */
	public void initData(int position){
		int len = position + 1;
		StringList.set(len, "loading : "+len);
		mAdapter.setList(StringList);
		mAdapter.notifyDataSetChanged();
	}
	
	/**
	 * 动态加载 viewpager 数据
	 * @param size 加载数据的个数
	 * @param position 当前页面id
	 * @return
	 */
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
