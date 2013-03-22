package com.igrs.tivic.phone.ViewGroup;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.test.UiThreadTest;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Activity.ContentActivity;
import com.igrs.tivic.phone.Activity.VideoPlayActivity;
import com.igrs.tivic.phone.Adapter.ContentPublishListViewAdapter2;
import com.igrs.tivic.phone.AsyncTask.ContentPublishAsyncTask;
import com.igrs.tivic.phone.AsyncTask.ContentPublishAsyncTask.ContentPublishDataUpdateListener;
import com.igrs.tivic.phone.Bean.PGC.ContentPublishBean;
import com.igrs.tivic.phone.Bean.PGC.ContentPublishListBean;
import com.igrs.tivic.phone.Bean.PGC.ContentTypesBean;
import com.igrs.tivic.phone.Bean.PGC.NewsBean;
import com.igrs.tivic.phone.Bean.PGC.ParagraphBean;
import com.igrs.tivic.phone.Bean.PGC.TVFindBean;
import com.igrs.tivic.phone.Bean.PGC.TVFindParagraphBean;
import com.igrs.tivic.phone.Bean.PGC.VideoBean;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Utils.ModelUtil;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;
import com.igrs.tivic.phone.ViewGroup.ContentExtraPromptView.Publish;
import com.igrs.tivic.phone.ViewGroup.PullToRefreshListView1.OnLoadMoreListener;
import com.igrs.tivic.phone.ViewGroup.PullToRefreshListView1.OnRefreshListener;
import com.igrs.tivic.phone.widget.AsyncImageView;
/**
 * 三级页面
 * @author admin
 *
 */
public class ContentsView extends FrameLayout implements OnClickListener,ContentPublishDataUpdateListener,
	OnRefreshListener,OnLoadMoreListener{
	private Context mContext;
	private ContentTypesBean contentTypesBean;
	private LayoutInflater mInflater;
	private RelativeLayout content_item;
	private Gallery gallery;
	private ArrayList<TVFindParagraphBean> tPBeanList;
	private TextView title_textView;
	private TextView describe_title;
	private TextView describe_detail;
	private TextView describe_price;
	private ArrayList<TVFindParagraphBean> tvFindBeanList;
	private ImageView pay_image;
	private onButtonClickListener buttonClickListener;
	private HashMap<String, String> a_id_map;
	private Activity activity;
	private String pay_url = "";
	private int offset = 0;
	private int gallery_item_width;
	float density = getResources().getDisplayMetrics().density;	//密度
	private String nbsp = "&nbsp";
	
	//zhanglr add
	ArrayList<ContentPublishBean> replyList = new ArrayList<ContentPublishBean>();
	ContentPublishListViewAdapter2 publishListViewAdapter;
	PullToRefreshListView1 listView;
	
	public void setOnButtonClickListener(onButtonClickListener onButtonClickListener){
		this.buttonClickListener = onButtonClickListener;
	}
	
	public ContentsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
	}
	public ContentsView(Context context){
		super(context);
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
	}
	
	public void init() {
		// tvfind
		if("tvfind".equals(contentTypesBean.getType())){
			
			content_item = (RelativeLayout) mInflater.inflate(R.layout.content_business, null);
			gallery = (Gallery) content_item.findViewById(R.id.gallery1);
			
//			RelativeLayout left_mark_layout = (RelativeLayout) content_item.findViewById(R.id.left_mark_layout);
//			left_mark_layout.setLayoutParams(new FrameLayout.LayoutParams(20, FrameLayout.LayoutParams.MATCH_PARENT));
//			RelativeLayout right_mark_layout = (RelativeLayout) content_item.findViewById(R.id.right_mark_layout);
//			right_mark_layout.setLayoutParams(new RelativeLayout.LayoutParams(20, RelativeLayout.LayoutParams.MATCH_PARENT));
			
//			int len = TivicGlobal.getInstance().displayWidth;
//			UIUtils.Logd("liq", "len : tvfind imagewidth === "+ContentActivity.device_width);
//			gallery_item_width = (int) (330 + 50*density);	//设置tvfind 商品图片的宽度
			gallery_item_width = ContentActivity.device_width*5/6;
			
			TextView right_mark_layout = (TextView) content_item.findViewById(R.id.right_mark_layout);
//			right_mark_layout.setLayoutParams(new RelativeLayout.LayoutParams(40, 100));
			right_mark_layout.getLayoutParams().height = gallery_item_width*3/4;
			right_mark_layout.getLayoutParams().width = ContentActivity.device_width/12;
			
			TextView left_mark_layout = (TextView) content_item.findViewById(R.id.left_mark_layout);
//			right_mark_layout.setLayoutParams(new RelativeLayout.LayoutParams(40, 100));
			left_mark_layout.getLayoutParams().height = gallery_item_width*3/4;
			left_mark_layout.getLayoutParams().width = ContentActivity.device_width/12;
			
			if(contentTypesBean.getObject() != null)
			tPBeanList = ((TVFindBean) contentTypesBean.getObject()).getParagraphList();
			
			content_item.findViewById(R.id.pay_image).setOnClickListener(this);
			ArrayList<String> urlsList = new ArrayList<String>(); //商品的图片(包括引言图片)路径
			tvFindBeanList = new ArrayList<TVFindParagraphBean>();
			if(tPBeanList != null && !tPBeanList.isEmpty())
			for (int i = 0; i < tPBeanList.size(); i++) {
				
				TVFindParagraphBean tPBean = tPBeanList.get(i);
				urlsList.addAll(tPBean.getImgUrl());
				ArrayList<String> urls = tPBean.getImgUrl();
				for (int j = 0; j < urls.size(); j++) {
					tvFindBeanList.add(tPBean);
				}
			}
			title_textView = (TextView) content_item.findViewById(R.id.title_textView);
			describe_title = (TextView) content_item.findViewById(R.id.describe_title);
			describe_detail = (TextView) content_item.findViewById(R.id.describe_detail);
			describe_price = (TextView) content_item.findViewById(R.id.describe_price);
			pay_image = (ImageView) content_item.findViewById(R.id.pay_image);
			
			MyGalleryAdapter myGalleryAdapter = new MyGalleryAdapter(urlsList);
			gallery.setAdapter(myGalleryAdapter);
			gallery.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View view,
						int position, long arg3) {
					TVFindParagraphBean tvPBean = tvFindBeanList.get(position % tvFindBeanList.size());
					title_textView.setText(tvPBean.getTitle());
					describe_title.setText(tvPBean.getLable());
					describe_detail.setText(tvPBean.getIntroduce());
					if(!ModelUtil.isEmpty(tvPBean.getPrice())){
						describe_price.setText(getResources().getString(R.string.store_price) + tvPBean.getPrice());
//						describe_price.setTextColor(Color.RED);
						pay_image.setVisibility(View.VISIBLE);
						pay_url = tvPBean.getLinkUrl();
					}else{
						describe_price.setText(null);
						pay_image.setVisibility(View.INVISIBLE);
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {}
			});
			
		}else if("news".equals(contentTypesBean.getType())){//news
			content_item = (RelativeLayout) mInflater.inflate(R.layout.content_news, null);
			if(contentTypesBean != null && contentTypesBean.getObject() != null){
				
			
			ArrayList<ParagraphBean> pBeanList = ((NewsBean)contentTypesBean.getObject()).getParagraphList();
			LinearLayout news_items_layout = (LinearLayout) content_item.findViewById(R.id.news_items_layout);
			for (int i = 0; i < pBeanList.size(); i++) {
				ParagraphBean pBean = pBeanList.get(i);
				if("txt_normal".equals(pBean.getType())){//文字格式内容
					TextView tView = new TextView(mContext);
					tView.setText("\u3000\u3000"+pBean.getContent());
					tView.setTextSize(16);
					tView.setTextColor(getResources().getColor(R.color.base_content));
					tView.setTypeface(Typeface.DEFAULT,Typeface.NORMAL);
					tView.setLineSpacing(10, 1.1f);
//					tView.setPadding(left, top, right, bottom);
//					tView.setGravity(Gravity.LEFT);
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//					lp.gravity = Gravity.RIGHT;
					news_items_layout.addView(tView, lp);
				}else if("img".equals(pBean.getType())){//图片内容
					AsyncImageView imageView = new AsyncImageView(mContext);
					imageView.setDefaultImageDrawable(getResources().getDrawable(R.drawable.base_queshengtu4_3));
					imageView.setUrl(pBean.getContent()+"!w512");
					imageView.setAdjustViewBounds(true);
					imageView.setScaleType(ScaleType.CENTER);		//变形 空白 小图靠左
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					lp.gravity = Gravity.CENTER;
					lp.topMargin = 10;
					lp.bottomMargin = 10;
					news_items_layout.addView(imageView, lp);
				}
			}
			TextView news_title = (TextView) content_item.findViewById(R.id.news_title);
			news_title.setText(((NewsBean)contentTypesBean.getObject()).getTitle());
			TextView news_date_source = (TextView) content_item.findViewById(R.id.news_date_source);
			StringBuilder sb = new StringBuilder();
			sb.append(((NewsBean)contentTypesBean.getObject()).getDate())
			.append("     ")
			.append(((NewsBean)contentTypesBean.getObject()).getSource())
			.append("  ");
			
			news_date_source.setText(sb);
			}
		}else if("video".equals(contentTypesBean.getType())){// video
			content_item = (RelativeLayout) mInflater.inflate(R.layout.content_video, null);
			if(contentTypesBean.getObject() != null){
				
			VideoBean vBean = ((VideoBean)contentTypesBean.getObject());
			
			//
			TextView video_from = (TextView) content_item.findViewById(R.id.video_from);
			
			StringBuilder sb = new StringBuilder();
			sb.append(getResources().getString(R.string.video_from)+" ");
			int icon_id = vBean.getSource();	//video come from type
			if(icon_id < Utils.video_image_id.length && icon_id >= 0){
				video_from.setCompoundDrawablesWithIntrinsicBounds(0, 0, Utils.video_image_id[icon_id], 0);
			}else{
				sb.append(getResources().getString(R.string.other_21));;
			}
			video_from.setText(sb);
			
			TextView video_title = (TextView) content_item.findViewById(R.id.video_title);
			video_title.setText(vBean.getTitle());
			AsyncImageView video_poster = (AsyncImageView) content_item.findViewById(R.id.video_poster);
			RelativeLayout.LayoutParams rLp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, activity.getWindowManager().getDefaultDisplay().getWidth() * 3/4);
			video_poster.setLayoutParams(rLp);
			video_poster.setUrl(vBean.getPosterUrl()+"!w512");
			ImageView content_play = (ImageView) content_item.findViewById(R.id.content_play);
			final String video_url = vBean.getVideoUrl();
			content_play.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
//					Intent it = new Intent(Intent.ACTION_VIEW); 
//					String path = video_url;
//					Uri uri = Uri.parse(path); 
//			        it.setDataAndType(uri , "video/*"); 
//			        ContentsView.this.mContext.startActivity(it); 
					//play video with MediaPlayer
					if(video_url == null) UIUtils.ToastMessage(mContext, getResources().getString(R.string.error_videurl));
					try {
						Intent video_intent = new Intent();
						video_intent
								.setClass(mContext, VideoPlayActivity.class);
						video_intent.putExtra("video_url", video_url);
						UIUtils.Logd("lq", "video_url : "+video_url);
						mContext.startActivity(video_intent);
					} catch (ActivityNotFoundException e) {
						UIUtils.ToastMessage(mContext, getResources().getString(R.string.base_player_not_found));
					}
				}
			});
		}
		}
		
		FrameLayout frameLayout = (FrameLayout) mInflater.inflate(R.layout.content_viewpager_listview, null);
		listView = (PullToRefreshListView1) frameLayout.findViewById(R.id.listview);
		listView.hideLoadMoreFooterView();
		listView.addHeaderView(content_item);
		listView.addFooterView(mInflater.inflate(R.layout.content_null_view, null),null,false);
		
		listView.setOnRefreshListener(this);
		listView.setOnLoadMoreListener(this);
		//加载文章评论
		publishListViewAdapter = new ContentPublishListViewAdapter2(mContext, replyList);
		listView.setAdapter(publishListViewAdapter);
		
		this.addView(frameLayout);
		
		if(!TivicGlobal.getInstance().mIsLogin){
			noticeExtra(true);
		}
		
		clearPublishData();
		initPublishData();
	}
	//tvfind 图片展示
	public class MyGalleryAdapter extends  BaseAdapter{
		private ArrayList<String> urlsList;
		int mGalleryItemBackground;
		
		public MyGalleryAdapter(ArrayList<String> urlsList){
			this.urlsList = urlsList;
//			TypedArray ta = mContext.obtainStyledAttributes(R.styleable.Gallery);
//			mGalleryItemBackground = ta.getResourceId(R.styleable.Gallery_android_galleryItemBackground, 0);
//			ta.recycle();
			
		}
		@Override
		public int getCount() {
			return urlsList.size();
		}
		@Override
		public Object getItem(int position) {
//			return urlsList.get(position % urlsList.size());
			return position;
		}
		@Override
		public long getItemId(int position) {
			return position % urlsList.size();
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			AsyncImageView aView = new AsyncImageView(mContext);
			aView.setDefaultImageResource(R.drawable.base_queshengtu4_3);
			aView.setUrl(urlsList.get(position % urlsList.size())+"!w512");
			aView.setLayoutParams(new Gallery.LayoutParams(gallery_item_width,gallery_item_width*3/4));
//			aView.setUrl(urlsList.get(position % urlsList.size()));
//			aView.setLayoutParams(new Gallery.LayoutParams(TivicGlobal.getInstance().displayWidth*2/3,TivicGlobal.getInstance().displayHeight*2/3));
			aView.setScaleType(ImageView.ScaleType.FIT_XY);
//			aView.setBackgroundResource(mGalleryItemBackground);
//			aView.setBackgroundColor(android.R.color.transparent);
			
			return aView;
		}
	}
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.pay_image){//点击购买 链接
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(TextUtils.isEmpty(pay_url)? "http://www.baidu.com": pay_url));
			mContext.startActivity(intent);
		}
		buttonClickListener.onClick(id);
	}
	public void setContent(ContentTypesBean contentTypesBean, Boolean mIsLogin,Activity activity,Boolean isCollection) {
		this.contentTypesBean = contentTypesBean;
		this.activity = activity;
		init();
	}
	
	public void setContent(ContentTypesBean contentTypesBean, Activity activity) {
		this.contentTypesBean = contentTypesBean;
		this.activity = activity;
		init();
	}
	
	/**
	 * 点击view 中 button事件回调
	 * @author admin
	 */
	public interface onButtonClickListener{
		public void onClick(int index);
	}
	
	//zhanglr add
//	int currentIndex = 1;
//	boolean isloading = false;
	
	private void initPublishData() {
//		if(isloading)
//			return;
		if (!TivicGlobal.getInstance().mIsLogin)
			return;
		if (replyList == null) {
			replyList = new ArrayList<ContentPublishBean>();
		}
		if (offset == 0)
			replyList.clear();
		ContentPublishAsyncTask loadDataTask = new ContentPublishAsyncTask();
		loadDataTask.setUpdateUIListener(this);
//		loadDataTask.setPageIndext(1);
		loadDataTask.setOffset(offset);
		loadDataTask.execute(String.valueOf(TivicGlobal.getInstance().currentCid));
//		isloading = true;
	}
	
	public void clearPublishData() {
		if(replyList != null)
			replyList.clear();
		offset = 0;
	}
	
	private void loadMore() {
//		processbar.setVisibility(View.VISIBLE);
//		loadMore.setVisibility(View.GONE);
//		if(isloading)
//			return;
//		currentIndex++;
		
		if(offset < 0 || offset == replyList.size() )
			return;
		offset = replyList.size();
		initPublishData();
	}
	
	@Override
	public void onReplyListGet(ContentPublishListBean datalist)
	{
		if (datalist != null && datalist.getPublishList() != null) {
			//Log.d("ContentsView", "kevin add : datalist.getPublishList().size = " + datalist.getPublishList().size());
			replyList.addAll(datalist.getPublishList());
			if(datalist.getTotal() - replyList.size() > 0) //后面还有未加载数据
			{
				listView.showLoadMoreFooterVeiw();
				//数据大于一页30条
				//添加加载更多视图
			}else{
				listView.hideLoadMoreFooterView();
			}
			
			if (replyList.size() == 0) {
				//提示用户列表为空
				noticeExtra(false);
			} else {		
				
			}
			
			if(offset > 0 && replyList.size() - offset > 0){
				listView.setSelection(replyList.size() - offset);
			}

	} else {
		Log.i("ContentsView", "kevin add: msg.obj == null");
	}
		publishListViewAdapter.notifyDataSetChanged();
		
		if(offset == 0){
			listView.onRefreshComplete();
		}else{
			listView.onLoadMoreComplete();
		}
	
//	isloading = false;
	}
	//未登录 提示,评论为空 提示
	private void noticeExtra(boolean b) {
		LinearLayout layout_prompt = (LinearLayout) content_item.findViewById(R.id.layout_prompt);
		layout_prompt.removeAllViews();
		
		ContentExtraPromptView extraPromptView = new ContentExtraPromptView(mContext, b);
		layout_prompt.addView(extraPromptView);
		extraPromptView.setPublish(new Publish() {
			@Override
			public void jumpToPublish(int id) {
				buttonClickListener.onClick(id);
			}
		});
	}

	@Override
	public void onClickLoadMore() {
		loadMore();
	}

	@Override
	public void onRefresh() {
		clearPublishData();
		initPublishData();
		
	}
}
