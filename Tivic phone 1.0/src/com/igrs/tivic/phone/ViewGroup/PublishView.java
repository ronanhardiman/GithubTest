package com.igrs.tivic.phone.ViewGroup;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
//import android.widget.AbsListView.OnScrollListener;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Activity.ContentPublishActivity;
import com.igrs.tivic.phone.Adapter.UGCPublishListViewAdapter2;
import com.igrs.tivic.phone.AsyncTask.UGCPublishAsyncTask;
import com.igrs.tivic.phone.AsyncTask.UGCPublishAsyncTask.UGCPublishDataUpdateListener;
import com.igrs.tivic.phone.Bean.UGC.UGCDataBean;
import com.igrs.tivic.phone.Bean.UGC.UGCReplyBean;
import com.igrs.tivic.phone.Bean.UGC.UGCReplyListBean;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.ShowProgressDialog;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Utils.LocationUtils;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.ViewGroup.ContentExtraPromptView.Publish;
import com.igrs.tivic.phone.ViewGroup.PullToRefreshListView1.OnLoadMoreListener;
import com.igrs.tivic.phone.ViewGroup.PullToRefreshListView1.OnRefreshListener;
import com.igrs.tivic.phone.widget.AsyncImageView;

public class PublishView extends FrameLayout implements// OnClickListener,
//		OnScrollListener,
		UGCPublishDataUpdateListener,OnRefreshListener, OnLoadMoreListener  {
	private Context mContext;
	private LayoutInflater inflater;
	private String TAG = "PublishView";
	private UGCDataBean ugcBean;
	private ArrayList<UGCReplyBean> replyList = new ArrayList<UGCReplyBean>();
	private onButtonClickListener buttonClickListener;
	private TivicGlobal tivicGlobal = TivicGlobal.getInstance();
	private UGCPublishListViewAdapter2 publishListViewAdapter;
	private PullToRefreshListView1 replyListView;
	private ContentExtraPromptView extraPromptView;
	private LinearLayout viewpager_item;
	private final int subnailWidth = 128;
//	private View empty_item;


	private int offset = 0;
//	private boolean isloading = false;
//	private int last_item_position = 0;
//	private Button loadMore;
//	private ProgressBar processbar;
//	private View loadMoreView;
	private LinearLayout id_empty;
	
	public PublishView(Context context) {
		super(context);
		this.mContext = context;
		inflater = LayoutInflater.from(mContext);
		initLayout();
	}

	public PublishView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		inflater = LayoutInflater.from(mContext);
		initLayout();
	}


	private void noData(boolean isShow) {
		
		if (isShow) {
			id_empty.removeAllViews();
			id_empty.addView(extraPromptView);
			id_empty.setVisibility(View.VISIBLE);
		} else {
			id_empty.removeAllViews();
			id_empty.setVisibility(View.GONE);
		}

	}

	private void initLayout() {
		// 帖子详情
		viewpager_item = (LinearLayout) inflater.inflate(
				R.layout.ugc_publish_viewpager_item, null);
//		empty_item = inflater.inflate(R.layout.content_null_view, null);
		// 评论部分
		FrameLayout listview = (FrameLayout) inflater.inflate(
				R.layout.content_viewpager_listview, null);

		if (replyListView == null)
			replyListView = (PullToRefreshListView1) listview.findViewById(R.id.listview);
		replyListView.hideLoadMoreFooterView();
		replyListView.setOnRefreshListener(this);
		replyListView.setOnLoadMoreListener(this);
		replyListView.addHeaderView(viewpager_item);
//		replyListView.addFooterView(empty_item,null,false);
//		loadMoreView = inflater.inflate(R.layout.load_more, null);	
//		loadMore = (Button) loadMoreView.findViewById(R.id.load_more);
//		processbar = (ProgressBar) loadMoreView.findViewById(R.id.progressBar);
//		loadMore.setOnClickListener(new Button.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (!isloading) {
//					loadMore();
//				}
//			}
//		});
		
		id_empty = (LinearLayout) viewpager_item
				.findViewById(R.id.id_empty);
		extraPromptView = new ContentExtraPromptView(mContext,
				false);
		extraPromptView.setPublish(new Publish() {
			@Override
			public void jumpToPublish(int id) {
				buttonClickListener.onClick(id);
			}
		});
		id_empty.setVisibility(View.GONE);
		publishListViewAdapter = new UGCPublishListViewAdapter2(mContext,
				replyList);
		replyListView.setAdapter(publishListViewAdapter);
		addView(listview);

		
	}

	public void initData() {

		AsyncImageView publish_image = (AsyncImageView) viewpager_item
				.findViewById(R.id.publish_image);
		TextView title = (TextView) viewpager_item
				.findViewById(R.id.id_text_title);
		TextView content = (TextView) viewpager_item
				.findViewById(R.id.id_text_content);
		TextView time = (TextView) viewpager_item.findViewById(R.id.text_time1);

		final MyScrollViewGroup viewGroup = (MyScrollViewGroup) viewpager_item.findViewById(R.id.viewgroup);
		AsyncImageView image_user = (AsyncImageView) viewpager_item.findViewById(R.id.icon_usr);
		TextView text_name = (TextView) viewpager_item.findViewById(R.id.text_name);
		TextView text_distance = (TextView) viewpager_item.findViewById(R.id.text_distence);
		TextView text_time = (TextView) viewpager_item.findViewById(R.id.text_time);
		ImageView image_gender = (ImageView)viewpager_item.findViewById(R.id.icon_gender);
		ImageView image_user_1 = (ImageView) viewpager_item.findViewById(R.id.icon_usr_1);
		TextView current_watch = (TextView) viewpager_item.findViewById(R.id.text_current_watch);
		final TextView user_sign = (TextView)viewpager_item.findViewById(R.id.text_user_sign);
		
		if (ugcBean != null) {
			if(ugcBean.getUsrinfo().getUserGender() == 0)
			{
				image_gender.setImageResource(R.drawable.ugc_gender_femaile);
				image_user.setDefaultImageResource(R.drawable.base_default_avater);
				image_user_1.setBackgroundResource(R.drawable.ugc_zhuiren_famale_online);
			}else{
				image_gender.setImageResource(R.drawable.ugc_gender_maile);
				image_user.setDefaultImageResource(R.drawable.base_default_avater);
				image_user_1.setBackgroundResource(R.drawable.ugc_zhuiren_male_online);
			}
			text_name.setText(ugcBean.getUsrinfo().getUserNickName());
			image_user.setAvaterUrl(UIUtils.getUsrAvatar(ugcBean.getUsrinfo().getUid()));
			if(ugcBean.getvBean().getVisit_ProgramTitle() == null)
				current_watch.setText(mContext.getString(R.string.social_programe_title_empty));
			else
			{
				current_watch.setText(mContext.getString(R.string.social_programe_title) + ugcBean.getvBean().getVisit_ProgramTitle());
			}
			text_time.setText(" ");
			text_distance.setText(LocationUtils.getDistance(mContext, TivicGlobal.getInstance().userRegisterBean.getLoactionBean(), ugcBean.getUsrinfo().getUsrLocation()));
			
			//click for zhui ren
			image_user.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(ugcBean.getOfficial() == 1)
						return;
					viewGroup.scrollMenuBar();
					user_sign.setText(ugcBean.getSign());
				}
			});
			//click for detail
			image_user_1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.putExtra("uid", ugcBean.getUsrinfo().getUid());
					intent.setClassName("com.igrs.tivic.phone", "com.igrs.tivic.phone.Activity.SocialBaseActivity");
					UIUtils.setCurrentFuncID(FuncID.ID_SOCIAL_FRIENDS_DETAIL);
					mContext.startActivity(intent);
				}
			});
			current_watch.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.e(TAG, "kevin add: click watch!!!!");
					if(ugcBean.getvBean().getVisit_pid()!= null && ugcBean.getvBean().getVisit_ProgramTitle()!=null)
					{	
						Intent intent = new Intent();
						intent.putExtra("program_id", ugcBean.getvBean().getVisit_pid());
						intent.putExtra("program_title", ugcBean.getvBean().getVisit_ProgramTitle());
						intent.putExtra("channel_id", ugcBean.getvBean().getVisit_channelid());
						intent.putExtra("channel_name", ugcBean.getvBean().getVisit_ChannelName());
						intent.setClassName("com.igrs.tivic.phone", "com.igrs.tivic.phone.Activity.WaterfallActivity");
						UIUtils.setCurrentFuncID(FuncID.ID_WATERFALL);
						mContext.startActivity(intent);
					}
				}
			});
						
//			image_user.setUrl(UIUtils.getUsrAvatar(ugcBean.getUsrinfo().getUid()));
//			image_user.setOnClickListener(new OnClickListener() { // 点击放大
//				public void onClick(View paramView) {
//					//官方微博同步帖子，不能追人
//					if(ugcBean.getOfficial() == 1)
//						return;
//					//追人到个人信息
//					Intent intent = new Intent();
//					intent.putExtra("uid", ugcBean.getUsrinfo().getUid());
//					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					intent.setClassName("com.igrs.tivic.phone",
//							"com.igrs.tivic.phone.Activity.SocialBaseActivity");
//					UIUtils.setCurrentFuncID(FuncID.ID_SOCIAL_FRIENDS_DETAIL);
//					mContext.startActivity(intent);
//					//追人到所在节目瀑布流
////					if(ugcBean.getvBean()!=null){
////						Intent intent2 = new Intent();
////						intent2.putExtra("program_id", ugcBean.getvBean().getVisit_pid());
////						intent2.putExtra("program_title", ugcBean.getvBean().getVisit_ProgramTitle());
////						intent2.putExtra("channel_id", ugcBean.getvBean().getVisit_channelid());
////						intent2.putExtra("channel_name", ugcBean.getvBean().getVisit_ChannelName());
////	
////						intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////						intent2.setClassName("com.igrs.tivic.phone",
////								"com.igrs.tivic.phone.Activity.WaterfallActivity");
////						UIUtils.setCurrentFuncID(FuncID.ID_WATERFALL);
////						mContext.startActivity(intent2);
////					}
//				}
//			});
//			if (ugcBean.getUsrinfo().getUserGender() == 0) {
//				image_gender.setImageResource(R.drawable.ugc_gender_femaile);
//				usrImg.setDefaultImageResource(R.drawable.base_usrinfo);
//			} else {
//				image_gender.setImageResource(R.drawable.ugc_gender_maile);
//				usrImg.setDefaultImageResource(R.drawable.base_usr_info_male);
//			}
//			usrName.setText(ugcBean.getUsrinfo().getUserNickName());
//			distance.setText(LocationUtils.getDistance(mContext, TivicGlobal.getInstance().userRegisterBean.getLoactionBean(), ugcBean.getUsrinfo().getUsrLocation()));
			title.setText(ugcBean.getTitle());
			content.setText("       " + ugcBean.getContent());
			content.setTypeface(Typeface.DEFAULT,Typeface.NORMAL);
			time.setText(ugcBean.getTime());
		}
		if(ugcBean.getImageUrl() == null || ugcBean.getImageUrl().length() < Const.imgHearder.length())
		{
			publish_image.setVisibility(View.GONE);
		}
		else
		{
			publish_image.setVisibility(View.VISIBLE);		
			if (!ugcBean.getImageUrl().startsWith("http://")) 
				ugcBean.setImageUrl(
						Const.imgHearder + ugcBean.getImageUrl());
				if(ugcBean.getOfficial() == 1)
					publish_image.setUrl(ugcBean.getImageUrl(), 4);
				else
					publish_image.setUrl(UIUtils.getImgSubnailUrl(ugcBean.getImageUrl(), subnailWidth));
				publish_image.setDefaultImageResource(R.drawable.base_subnail);
				publish_image.setScaleType(ImageView.ScaleType.FIT_XY);
				publish_image.setOnClickListener(new OnClickListener() { // 点击放大
					public void onClick(View paramView) {
						Intent intent = new Intent();
						// TODO put Extra resId or imgUrl to BaseSubnailActivity
						intent.putExtra("imgUrl", ugcBean.getImageUrl());
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.setClassName("com.igrs.tivic.phone",
								"com.igrs.tivic.phone.Activity.BaseSubnailActivity");
						mContext.startActivity(intent);
					}
				});
		}
		

		clearPublishData();
		initPublishData();

	}

	public void clearPublishData() {
		replyList.clear();
		offset = 0;
	}

//	@Override
//	public void onScroll(AbsListView view, int firstVisibleItem,
//			int visibleItemCount, int totalItemCount) {
//		//this.visibleItemCount = visibleItemCount;
//		last_item_position = firstVisibleItem + visibleItemCount - 1;
//
//	}

	private void initPublishData() {
//		if(isloading)
//			return;
		if (!tivicGlobal.mIsLogin)
			return;
		if (replyList == null) {
			replyList = new ArrayList<UGCReplyBean>();
		}
		if (offset == 0)
			replyList.clear();
		UGCPublishAsyncTask loadDataTask = new UGCPublishAsyncTask();
		loadDataTask.setUpdateUIListener(this);
		loadDataTask.setOffset(offset);
		loadDataTask.setPosition(ugcBean);
		loadDataTask.execute();
//		isloading = true;
	}

	private void loadMore() {
//		if(isloading)
//			return;
//		processbar.setVisibility(View.VISIBLE);
//		loadMore.setVisibility(View.GONE);
		if (offset < 0 || offset == replyList.size())
			return;
		offset = replyList.size();
		initPublishData();
	}

//	@Override
//	public void onScrollStateChanged(AbsListView view, int scrollState) {
//		int itemsLastIndex = publishListViewAdapter.getCount() - 1; // 数据集最后一项的索引
//		int lastIndex = itemsLastIndex + 1; // 加上底部的loadMoreView项
//		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
//				&& last_item_position == lastIndex) {
//			// 如果是自动加载,可以在这里放置异步加载数据的代码
//			// Log.i("LOADMORE", "loading...");
//		}
//	}

	public void setViewsData(UGCDataBean databean) {
		this.ugcBean = databean;
	}

	public void setOnButtonClickListener(
			onButtonClickListener onButtonClickListener) {
		this.buttonClickListener = onButtonClickListener;
	}

	public interface onButtonClickListener {
		public void onClick(int index);
	}
	
	public void onReplyListGet(UGCReplyListBean datalist)
	{
		if (datalist != null && datalist.getDataList() != null) {

				replyList.addAll(datalist.getDataList());
				if(datalist.getCount() - replyList.size() > 0) //后面还有未加载数据
				{
					replyListView.showLoadMoreFooterVeiw(); // 设置列表底部视图; // 设置列表底部视图
//					loadMore.setVisibility(View.VISIBLE);
//					replyListView.setAdapter(publishListViewAdapter);
				}else{
					replyListView.hideLoadMoreFooterView(); // 设置列表底部视图
//					loadMore.setVisibility(View.GONE);
//					replyListView.setAdapter(publishListViewAdapter);
				}
				
				
				if (replyList.size() == 0) {
					noData(true);
				} else {		
					noData(false);
				}
				
				if(offset > 0 && replyList.size()-offset > 0)
				{
					replyListView.setSelection(replyList.size()-offset);
				}

		} else {
			Log.i(TAG, "kevin add: msg.obj == null");
		}
		publishListViewAdapter.notifyDataSetChanged();
		
		if (offset == 0) {
//			ShowProgressDialog.dismiss();
			replyListView.onRefreshComplete();
		} else {
			replyListView.onLoadMoreComplete();

		}

//		isloading = false;
	}
	
	@Override
	public void onRefresh() {

		clearPublishData();
		initPublishData();
	}

	@Override
	public void onClickLoadMore() {

		loadMore();
	}
}
