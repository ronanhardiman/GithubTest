
package com.igrs.tivic.phone.Activity;
import java.util.ArrayList;
import java.util.HashMap;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Bean.PGC.CollectBean;
import com.igrs.tivic.phone.Bean.PGC.ContentTypesBean;
import com.igrs.tivic.phone.Bean.Waterfall.ProgramDetailBean;
import com.igrs.tivic.phone.Bean.Waterfall.SendedDatasBean;
import com.igrs.tivic.phone.Global.AppContext;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Impl.ContentImpl;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;
import com.igrs.tivic.phone.ViewGroup.ContentsView;
import com.igrs.tivic.phone.ViewGroup.ContentsView.onButtonClickListener;

public  class ContentsActivity extends BaseActivity implements OnClickListener{
	TivicGlobal tivicGlobal = TivicGlobal.getInstance();
	private ViewPager viewpager_content;
	private ContentImpl cImpl;
	private ArrayList<SendedDatasBean> sendDataList = new ArrayList<SendedDatasBean>();
	private ArrayList<ContentTypesBean> contentTypeBeanList;	//解析后的news tvfind video数据 (type)
	private int selectIndex ; //瀑布流点击的文章id
	private ArrayList<String> id_list;	//记录 三级页面article_id;
	private String article_id;
	private ImageButton button_collect;
	private HashMap<String, String> article_id_map = new HashMap<String, String>();
	public static final int device_width = TivicGlobal.getInstance().displayWidth;
	private MyViewPagerAdapter mAdapter;
	private Context mContext;
	//收藏文章
	private Handler collectionHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			int toast_id = 0;
			switch (msg.arg1) {
			case ContentImpl.MSG_COLLECTON:
				if(msg.arg2 == 0){
					final AlertDialog aDialog;
					synchronized (ContentsActivity.this) {
						ContextThemeWrapper ctw = new ContextThemeWrapper(
								ContentsActivity.this, R.style.dialogstyle);
						AlertDialog.Builder ab = new AlertDialog.Builder(ctw);
						ab.create();
						aDialog = ab.show();
					}
					//dismiss the AlertDialog 
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							if(aDialog != null){
								aDialog.dismiss();
							}
						}
					}, 1000);
					
					button_collect.setBackgroundResource(R.drawable.content_collected_button);
					String id = (String) msg.obj;
					//加入收藏列表
					((AppContext)getApplication()).putCollection(id, id);
//					viewpagerAdapter.notifyDataSetChanged();
				}else if(msg.arg2 == -1){
					toast_id = R.string.net_error;
//					UIUtils.ToastMessage(mContext, ContentActivity.this.getResources().getString(R.string.net_error));
				}else if(msg.arg2 == 2){
					toast_id = R.string.addCollection_fail;
				}
				break;
			case ContentImpl.MSG_REMOVE_COLLECTON:
				if(msg.arg2 == 0){
					toast_id = R.string.remove_success;
//					UIUtils.ToastMessage(mContext, "remove collection success!!"+article_id);
					button_collect.setBackgroundResource(R.drawable.content_collect_button);
					String id = (String) msg.obj;
					((AppContext)getApplication()).removeCollection(id);
				}else if(msg.arg2 == -2){
					toast_id = R.string.net_error;
				}
				break;
			default:
				break;
			}
			if(toast_id != 0)
			UIUtils.ToastMessage(mContext, ContentsActivity.this.getResources().getString(toast_id));
		}
	};
	//获取收藏列表
	private Handler collection_list_handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.arg1 == ContentImpl.MSG_COLLECTON_LIST){
				((AppContext)getApplication()).setCollection((HashMap<String, String>) msg.obj);
				article_id_map = ((AppContext)getApplication()).getCollectionMap(); 
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		UIUtils.setCurrentFuncID(FuncID.ID_CONTENT);
		super.onCreate(savedInstanceState);
//		mInflater = LayoutInflater.from(this);
		mContext = this.getApplicationContext() ;
		selectIndex = this.getIntent().getIntExtra("currentId", 0);
//		UIUtils.Logd("lq", "selectIndex: = "+selectIndex);

		id_list = new ArrayList<String>();
		if(this.getIntent().getIntExtra("fromFragment", 0) == 0){
			ArrayList<ProgramDetailBean>  pBeanList = this.getIntent().getParcelableArrayListExtra("testt");
			for (ProgramDetailBean pBean : pBeanList) {
				SendedDatasBean sBean = new SendedDatasBean();
				sBean.type = pBean.getContent().getContent_tpye();
				sBean.articleUrlList = pBean.getContent().getArticleList();
				
				id_list.add(String.valueOf(pBean.getContent().getContent_id()));//记录 article_id
				sendDataList.add(sBean);
			}
			selectIndex = id_list.indexOf(String.valueOf(selectIndex)) != -1 ?  id_list.indexOf(String.valueOf(selectIndex)) : 0;
//			UIUtils.Loge("lq", "=== ContentActivity  pBeanList.size() ; "+ pBeanList.size());
		}else if(this.getIntent().getIntExtra("fromFragment", 0) == 1){//由SocialSaveFragment 跳转过来
			ArrayList<CollectBean> cBeanList = this.getIntent().getParcelableArrayListExtra("testt");
			for (CollectBean cBeans : cBeanList) {
				SendedDatasBean sBean = new SendedDatasBean();
				sBean.articleUrlList = cBeans.getArticleList();
				sBean.type = cBeans.getArticle_type();
				sendDataList.add(sBean);
				id_list.add(cBeans.getFav_id());
			}
		}
//		UIUtils.Logi("lq", "selectIndex : "+selectIndex+" =========== "+"id_list ; "+ id_list.toString());
		initContentLayout();
		//读取收藏列表
		article_id_map = ((AppContext)getApplication()).getCollectionMap();
		contentTypeBeanList = new ArrayList<ContentTypesBean>();
		initData2();
	}
	//异步加载数据
	private Handler loadingHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.arg1) {
			case ContentImpl.MSG_CONTENTS:
				
				contentTypeBeanList.set(msg.arg2, (ContentTypesBean) msg.obj);
				if(mAdapter == null){
					mAdapter = new MyViewPagerAdapter();
					viewpager_content.setAdapter(mAdapter);
					viewpager_content.setCurrentItem(selectIndex);
					tivicGlobal.currentCid = Integer.parseInt(id_list.get(selectIndex));
					
					viewpager_content.setOnPageChangeListener(new OnPageChangeListener() {
						
						@Override
						public void onPageSelected(int position) {
							cImpl.getContent(sendDataList.get(position), position, loadingHandler);
							tivicGlobal.currentCid = Integer.parseInt(id_list.get(position));
							isCollection(article_id_map.containsKey(String.valueOf(id_list.get(position))));
						}
						
						@Override
						public void onPageScrolled(int position, float arg1, int arg2) {
						}
						@Override
						public void onPageScrollStateChanged(int position) {
						}
					});
				}
				
				
				mAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		};
	};
//	private ContentsView cView2;
	private void isCollection(Boolean isCollection) {
		if(isCollection){
			button_collect.setBackgroundResource(R.drawable.content_collected_button);
		}else{
			button_collect.setBackgroundResource(R.drawable.content_collect_button);
		}
	}
	
	private void initData2(){
		for (int i = 0; i < sendDataList.size(); i++) {
			contentTypeBeanList.add(null);
		}
		
		cImpl = ContentImpl.getInstance();
		cImpl.getContent(sendDataList.get(selectIndex), selectIndex, loadingHandler);
		cImpl.getCollectionList(collection_list_handler);
		
//		mAdapter = new MyViewPagerAdapter();
//		viewpager_content.setAdapter(mAdapter);
//		viewpager_content.setCurrentItem(selectIndex);
//		tivicGlobal.currentCid = Integer.parseInt(id_list.get(selectIndex));
//		
//		viewpager_content.setOnPageChangeListener(new OnPageChangeListener() {
//			
//			@Override
//			public void onPageSelected(int position) {
//				cImpl.getContent(sendDataList.get(position), position, loadingHandler);
//				tivicGlobal.currentCid = Integer.parseInt(id_list.get(position));
//				isCollection(article_id_map.containsKey(String.valueOf(id_list.get(position))));
//			}
//			
//			@Override
//			public void onPageScrolled(int position, float arg1, int arg2) {
//			}
//			@Override
//			public void onPageScrollStateChanged(int position) {
//			}
//		});
	}
	public class MyViewPagerAdapter extends PagerAdapter{
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View itemView = null;
			if(contentTypeBeanList.get(position) != null){
				itemView = createItemView(position);
			}else{
				RelativeLayout rLayout = new RelativeLayout(mContext);
				
				ProgressBar pBar = new ProgressBar(mContext);
				RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				rp.addRule(RelativeLayout.CENTER_IN_PARENT);
				pBar.setLayoutParams(rp);
				rLayout.addView(pBar);
				
				itemView = rLayout;
			}
			((ViewPager)container).addView(itemView,0);
			return itemView;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager)container).removeView((View)object);
		}
		
		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}
		
		@Override
		public void startUpdate(ViewGroup container) {
			super.startUpdate(container);
		}
		@Override
		public int getCount() {
			return contentTypeBeanList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}

	public View createItemView(int position) {
		ContentsView cView2 = new ContentsView(this);
		cView2.setContent(contentTypeBeanList.get(position));
		cView2.setOnButtonClickListener(new onButtonClickListener() {
			
			@Override
			public void onClick(int index) {
				switch (index) {
				case R.id.content_login:
					Intent content_intent = new Intent(mContext,LoginActivity.class);
					startActivity(content_intent);
					break;
				case R.id.content_reg:
					Intent content_register = new Intent(mContext,LoginRegistrationActivity.class);
					startActivity(content_register);
					break;
				case R.id.content_comment:
					Intent comment_intent = new Intent(mContext, ContentPublishActivity.class);
					comment_intent.putExtra("content_id", TivicGlobal.getInstance().currentCid);
					startActivity(comment_intent);
					break;	
				default:
					break;
				}
			}
		});
		return cView2;
	}

	private void initContentLayout()
	{
		viewpager_content = (ViewPager) findViewById(R.id.viewpager_content);
		ImageView back_control = (ImageView) findViewById(R.id.icon_back_control);
		back_control.setOnClickListener(this);
		button_collect = (ImageButton) findViewById(R.id.button_collect);
		button_collect.setOnClickListener(this);
		findViewById(R.id.button_comment).setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		UIUtils.setCurrentFuncID(FuncID.ID_CONTENT);
		if(mAdapter != null){
			mAdapter.notifyDataSetChanged();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();	
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.icon_back_control://topbar 返回按钮
			finish();
			break;
		case R.id.button_collect://点击收藏 
			if(tivicGlobal.mIsLogin){
				addCollection();//添加article_id到收藏列表
			}else{
				Intent content_intent = new Intent(this,LoginActivity.class);
				startActivity(content_intent);
			}
			break;
		case R.id.button_comment://点击评论
			if(tivicGlobal.mIsLogin){
				Intent comment_intent = new Intent(this, ContentPublishActivity.class);
				comment_intent.putExtra("content_id", TivicGlobal.getInstance().currentCid);
				startActivity(comment_intent);
			}else{
				Intent content_intent = new Intent(this,LoginActivity.class);
				startActivity(content_intent);
			}
			break;
		default:
			break;
		}
	}
	//添加收藏
	private void addCollection() {
		int select_article_id = viewpager_content.getCurrentItem();
		article_id = id_list.get(select_article_id);
		cImpl = ContentImpl.getInstance();
		cImpl.setCollectionHandler(collectionHandler);
		if(((AppContext)getApplication()).getCollectionMap().containsKey(article_id)){//本地收藏map 有该文章的id
			cImpl.removeCollection(article_id);
		}else{//本地收藏表中没有该 文章 id
			cImpl.addCollection(article_id);
		}
	}
}
