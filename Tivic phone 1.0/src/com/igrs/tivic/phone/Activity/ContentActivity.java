
package com.igrs.tivic.phone.Activity;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Adapter.ContentViewpagerAdapter;
import com.igrs.tivic.phone.Adapter.ContentViewpagerAdapter.onBackgroundChangeListener;
import com.igrs.tivic.phone.Bean.PGC.CollectBean;
import com.igrs.tivic.phone.Bean.PGC.ContentTypesBean;
import com.igrs.tivic.phone.Bean.PGC.NewsBean;
import com.igrs.tivic.phone.Bean.PGC.TVFindBean;
import com.igrs.tivic.phone.Bean.PGC.VideoBean;
import com.igrs.tivic.phone.Bean.Waterfall.ItemBean;
import com.igrs.tivic.phone.Bean.Waterfall.ProgramDetailBean;
import com.igrs.tivic.phone.Bean.Waterfall.SendedDatasBean;
import com.igrs.tivic.phone.Bean.Waterfall.SubpageContent;
import com.igrs.tivic.phone.Global.AppContext;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.ShowProgressDialog;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Global.URLConfig;
import com.igrs.tivic.phone.Impl.ContentImpl;
import com.igrs.tivic.phone.Impl.MyTvImpl;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;

public  class ContentActivity extends BaseActivity implements OnClickListener{
	TivicGlobal tivicGlobal = TivicGlobal.getInstance();
	private ViewPager viewpager_content;
	private ContentImpl cImpl;
	private ArrayList<SendedDatasBean> sendDataList = new ArrayList<SendedDatasBean>();
	private ContentViewpagerAdapter viewpagerAdapter;
	private ArrayList<ContentTypesBean> contentTypeBeanList;	//解析后的news tvfind video数据 (type)
	private int selectIndex ; //瀑布流点击的文章id
	private ArrayList<String> id_list;	//记录 三级页面article_id;
	private String article_id;
	private ImageButton button_collect;
	private HashMap<String, String> article_id_map = new HashMap<String, String>();
	private TextView alert_info;
	public static final int device_width = TivicGlobal.getInstance().displayWidth;
	
	private Handler contentUpdateHander = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.arg1) {
			case ContentImpl.MSG_CONTENTS:
				contentTypeBeanList = (ArrayList<ContentTypesBean>) msg.obj;
				alert_info.setVisibility(View.INVISIBLE);
//				ShowProgressDialog.dismiss();
				break;
			/*case ContentImpl.MSG_NEWS:
				NewsBean nBean = (NewsBean)msg.obj;
				ContentTypesBean cTBean_news = new ContentTypesBean();
				cTBean_news.setType("news");
				cTBean_news.setObject(nBean);
				contentTypeBeanList.set(msg.arg2,cTBean_news);
				break;
			case ContentImpl.MSG_TVFIND:
				TVFindBean tBean = (TVFindBean)msg.obj;
				
				ContentTypesBean cTBean = new ContentTypesBean();
				cTBean.setType("tvfind");
				cTBean.setObject(tBean);
				contentTypeBeanList.set(msg.arg2,cTBean);
				break;
			case ContentImpl.MSG_VIDEO:
				VideoBean vBean = (VideoBean)msg.obj;
				ContentTypesBean cTBean_video = new ContentTypesBean();
				cTBean_video.setType("video");
				cTBean_video.setObject(vBean);
				contentTypeBeanList.set(msg.arg2,cTBean_video);
				break;*/
			default:
				break;
				
			}
			viewpagerAdapter = new ContentViewpagerAdapter(ContentActivity.this);
			viewpagerAdapter.setOnBackgroundListener(new onBackgroundChangeListener() {
				
				@Override
				public void onClick(Boolean isCollection) {
					if(isCollection){
						button_collect.setBackgroundResource(R.drawable.content_collected_button);
					}else{
						button_collect.setBackgroundResource(R.drawable.content_collect_button);
					}
					
				}
			});
			viewpagerAdapter.setAdapterData(contentTypeBeanList,tivicGlobal.mIsLogin,ContentActivity.this,id_list,article_id_map);
			viewpager_content.setAdapter(viewpagerAdapter);
			viewpager_content.setPageMarginDrawable(R.drawable.grey_background_pattern);
			viewpager_content.setPageMargin(getResources().getDimensionPixelSize(R.dimen.page_margin_width));
			viewpager_content.setCurrentItem(selectIndex,false);
			
		}
	};
	//收藏文章
	private Handler collectionHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			int toast_id = 0;
			switch (msg.arg1) {
			case ContentImpl.MSG_COLLECTON:
				if(msg.arg2 == 0){
					final AlertDialog aDialog;
					synchronized (ContentActivity.this) {
						ContextThemeWrapper ctw = new ContextThemeWrapper(
								ContentActivity.this, R.style.dialogstyle);
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
			UIUtils.ToastMessage(mContext, ContentActivity.this.getResources().getString(toast_id));
		}
	};
	//获取收藏列表
	private Handler collection_list_handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.arg1 == ContentImpl.MSG_COLLECTON_LIST){
				((AppContext)getApplication()).setCollection((HashMap<String, String>) msg.obj);
				article_id_map = ((AppContext)getApplication()).getCollectionMap(); 
//				article_id_map = (HashMap<String, String>) msg.obj;
//				System.err.println(article_id_map);
//				viewpagerAdapter.notifyDataSetChanged();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		UIUtils.setCurrentFuncID(FuncID.ID_CONTENT);
		super.onCreate(savedInstanceState);
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
			/*if(!cBeanList.isEmpty()){//测试数据 不能解析出json  这里自己提供测试数据
				if(cBeanList.get(0).getArticleList().get(0).contains("ipad")){
					sendDataList.clear();
					String tv_find_test = "http://devstatic.b0.upaiyun.com/article/2013/1/6/322_tvfind.html";
					ArrayList<String> list = new ArrayList<String>();
					list.add(tv_find_test);
					list.add(tv_find_test);
					SendedDatasBean sBean = new SendedDatasBean();
					sBean.articleUrlList = list;
					sBean.type = "tvfind";
					sendDataList.add(sBean);
					id_list.add("322");
				}
			}*/
			
		}
		UIUtils.Logi("lq", "selectIndex : "+selectIndex+" =========== "+"id_list ; "+ id_list.toString());
		
		initContentLayout();
		//读取收藏列表
		article_id_map = ((AppContext)getApplication()).getCollectionMap();
//		ShowProgressDialog.show(this, null, null);
		contentTypeBeanList = new ArrayList<ContentTypesBean>();
		parserData2();	//解析三级页面数据
//		upLoadPhoto();//测试上传图片
		
	}
	
	/**
	 * 按瀑布流顺序解析数据,顺序显示三级页面各个模块
	 */
	private void parserData2() {
		alert_info.setVisibility(View.VISIBLE);
		cImpl = ContentImpl.getInstance();
		cImpl.setContentUpdateHandler(contentUpdateHander);
		cImpl.getAllContents(sendDataList, contentTypeBeanList);
//		cImpl.getChannelList(); 	//just for test
		cImpl.setCollection_list_handler(collection_list_handler);
//		cImpl.getCollectionList2(); 	//just for test
		cImpl.getCollectedArticleIdList();		//get all collection one time
		
//		cImpl.getCollectedArticleIdList();
//		cImpl.getContentReply();	//just for test
		
		//test
//		MyTvImpl tvImpl = MyTvImpl.getInstance();
//		tvImpl.getFocusProgramsList(String.valueOf(Utils.unixTimestamp2String("2013-01-28")));
		
		//test
//		cImpl.getUserBaseInfo();
		//test for epglist
//		cImpl.getDayEPGList();
		
		//test search
//		SearchImpl sImpl = SearchImpl.getInstance();
//		sImpl.searchPrograms();
		//test visit
//		cImpl.UpdateLastVisit();
	}

	private void initContentLayout()
	{
		viewpager_content = (ViewPager) findViewById(R.id.viewpager_content);
		ImageView back_control = (ImageView) findViewById(R.id.icon_back_control);
		back_control.setOnClickListener(this);
		button_collect = (ImageButton) findViewById(R.id.button_collect);
		button_collect.setOnClickListener(this);
		findViewById(R.id.button_comment).setOnClickListener(this);
		alert_info = (TextView)findViewById(R.id.alert_info_id);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		UIUtils.setCurrentFuncID(FuncID.ID_CONTENT);
		if(viewpagerAdapter != null){
			viewpagerAdapter.notifyDataSetChanged();
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
				
//				ContextThemeWrapper ctw = new ContextThemeWrapper(this, R.style.dialogstyle);
//				AlertDialog.Builder ab = new AlertDialog.Builder(ctw);
//				ab.create();ab.show();
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
