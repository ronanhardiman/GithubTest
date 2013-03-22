
package com.igrs.tivic.phone.Activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.test.UiThreadTest;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Adapter.ListViewSearchAdapter;
import com.igrs.tivic.phone.Bean.EPG.EPGProgramsBean;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Impl.SearchImpl;
import com.igrs.tivic.phone.Utils.ModelUtil;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;
import com.igrs.tivic.phone.widget.AsyncImageView;

public class SearchActivity extends BaseActivity{

	private String TAG = "SearchActivity";
	private ArrayList<EPGProgramsBean> lvSearchData = new ArrayList<EPGProgramsBean>();
	private ListViewSearchAdapter lvSearchAdapter;
	private ImageButton mSearchBtn;
	private EditText mSearchEditer;
	private ProgressBar mProgressbar;
	public static final int EDITOR_RECOMMEND = 1;//编辑推荐内容
	public static final int PERSONAL_RECOMMEND = 2;//用户输入的想搜索的内容
	private String curSearchContent = "";
	private ListView search_listview;
	private final static int DATA_LOAD_ING = 0x001;
	private final static int DATA_LOAD_COMPLETE = 0x002;
	private InputMethodManager imm;
	private SearchImpl sImpl;
	public static Boolean fromEditer = true;	//搜索行为 来自编辑(true)   还是来自用户（false）
	public boolean isFirstSearch = true;		//是否是推荐页面
	private FrameLayout edit_framelayout;
	
	public boolean isFirstSearch() {
		return isFirstSearch;
	}
	public void setFirstSearch(boolean isFirstSearch) {
		this.isFirstSearch = isFirstSearch;
	}
	private Handler mSearchHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case EDITOR_RECOMMEND:
				lvSearchData = (ArrayList<EPGProgramsBean>) msg.obj;
				fromEditer = true;
				break;
			case PERSONAL_RECOMMEND:
				isFirstSearch = false;	
				if(msg.obj ==null ||((ArrayList<EPGProgramsBean>) msg.obj).isEmpty()){
					setEditFrameLayout(true);
					UIUtils.ToastMessage(SearchActivity.this, getString(R.string.no_input_content));
				}else{
					setEditFrameLayout(false);
					lvSearchData = (ArrayList<EPGProgramsBean>) msg.obj;
					fromEditer = false;
				}
				break;
			default:
				break;
			}
			headButtonSwitch(DATA_LOAD_COMPLETE);
			lvSearchAdapter = new ListViewSearchAdapter(SearchActivity.this,/*lvSearchData,*/R.layout.search_listitem);
			if(lvSearchData != null){
				lvSearchAdapter.setListData(lvSearchData);
				search_listview.setAdapter(lvSearchAdapter);
				search_listview.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View  v,
							int position, long arg3) {
						setEditFrameLayout(false);
						isFirstSearch = false;
						if(fromEditer){
							String data = v.getContentDescription().toString();
							initPersonData(data);
						}else{
							EPGProgramsBean eBean = lvSearchData.get(position);
							Intent s_intent = new Intent();
							s_intent.setClassName(SearchActivity.this, "com.igrs.tivic.phone.Activity.WaterfallActivity");
							if(eBean.getChannel_id() != 0){
								s_intent.putExtra("channel_id", eBean.getChannel_id());
								s_intent.putExtra("channel_name", Utils.getChannelNameById(mContext, eBean.getChannel_id()));
							}
							s_intent.putExtra("program_id", eBean.getProgram_id());
							s_intent.putExtra("program_title", eBean.getProgram_name());
//							s_intent.putExtra("program_title", getResources().getString(R.string.base_search_result));
							UIUtils.setCurrentFuncID(FuncID.ID_WATERFALL);
							SearchActivity.this.startActivity(s_intent);
						}
						mSearchEditer.setText(v.getContentDescription().toString());
					}
				});
			}else{
				changeListViewColor();
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		UIUtils.setCurrentFuncID(FuncID.ID_SEARCH);
		super.onCreate(savedInstanceState);
		mContext = this.getApplicationContext();
		search_listview = (ListView) findViewById(R.id.search_listview);
		
		this.initSearchLayout();
		initEditerData();
		mBaseTopBar.setSearchTitle();
	}
	@Override
	public void init() {
		super.init();
		
	}
	/**
	 * 推荐的搜索内容
	 */
	public void initEditerData() {
		if(!Utils.isConnected(this))
			UIUtils.ToastMessage(this, getResources().getString(R.string.net_error));
		sImpl = SearchImpl.getInstance();
		sImpl.setRecommendSearchHandler(mSearchHandler);
		sImpl.searchHotProgram();
		headButtonSwitch(DATA_LOAD_ING);
	}
	
	/**
	 * 搜索按钮 加载进度条
	 * @param dataLoadComplete
	 */
	private void headButtonSwitch(int type) {
		switch (type) {
    	case DATA_LOAD_ING:
    		mSearchBtn.setClickable(false);
//			mProgressbar.setVisibility(View.VISIBLE);
    		search_listview.setVisibility(View.INVISIBLE);
			break;
		case DATA_LOAD_COMPLETE:
			mSearchBtn.setClickable(true);
//			mProgressbar.setVisibility(View.GONE);
			search_listview.setVisibility(View.VISIBLE);
			break;
		}
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		UIUtils.setCurrentFuncID(FuncID.ID_SEARCH);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	//初始化视图控件
	public void initSearchLayout()
	{	
		hideMenuBar();
		imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		mSearchBtn = (ImageButton)findViewById(R.id.search_btn);
    	mSearchEditer = (EditText)findViewById(R.id.search_editer);
    	edit_framelayout = (FrameLayout) findViewById(R.id.edit_framelayout);
    	
    	mSearchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				curSearchContent = mSearchEditer.getText().toString();
//				if(!TextUtils.isEmpty(curSearchContent))
//				setEditFrameLayout(false);
				mSearchEditer.clearFocus();
				initPersonData(curSearchContent);
			}
		});
    	mSearchEditer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){  
					imm.showSoftInput(v, 0);  
		        }  
		        else{  
		            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
		        }  
			}
		});
	}
	//搜索用户输入内容
	private void initPersonData(String curSearchContent) {
		changeListViewColor();
		if(!ModelUtil.isEmpty(curSearchContent)){
			headButtonSwitch(DATA_LOAD_ING);
			sImpl.searchPrograms(curSearchContent);
		}else{
			UIUtils.ToastMessage(SearchActivity.this, getString(R.string.input_content));
		}
		if(lvSearchData != null)
		lvSearchData.clear();
		lvSearchAdapter.notifyDataSetChanged();
	}
	
	public void setListViewColor(){
		search_listview.setBackgroundColor(getResources().getColor(R.color.black));
	}
	private void changeListViewColor() {
		search_listview.setBackgroundColor(getResources().getColor(R.color.white));
	}
	/**
	 * 设置搜索输入edit 显示与否
	 */
	public void setEditFrameLayout(boolean isVisibility){
		if(edit_framelayout != null){
			if(isVisibility) {
				edit_framelayout.setVisibility(View.VISIBLE);
			}else{
				edit_framelayout.setVisibility(View.GONE);
			}
		}
	}
}
