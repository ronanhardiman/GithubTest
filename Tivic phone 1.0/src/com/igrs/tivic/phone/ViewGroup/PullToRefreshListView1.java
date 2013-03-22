package com.igrs.tivic.phone.ViewGroup;

import java.util.Date;

import com.igrs.tivic.phone.R;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PullToRefreshListView1 extends ListView implements OnScrollListener {

	private static final String TAG = "PullToRefreshListView1";

	private final static int RELEASE_To_REFRESH = 0;
	private final static int PULL_To_REFRESH = 1;
	private final static int REFRESHING = 2;
	private final static int DONE = 3;
	private final static int LOADING = 4;

	private final static int RATIO = 3;

	private LayoutInflater inflater;

	private LinearLayout headView;

	private TextView tipsTextview;
	private TextView lastUpdatedTextView;
	private ImageView arrowImageView;
	private ProgressBar progressBar;

	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;

	private boolean isRecored;

	private int headContentWidth;
	private int headContentHeight;

	private int startY;
	private int firstItemIndex;

	private int state;

	private boolean isBack;

	private OnRefreshListener mOnRefreshListener;
	private OnLoadMoreListener mOnLoadMoreListener;
	private OnLoadPreviousDayEPGDataListener loadprevlistener;
	private OnLoadNextDayEPGDataListener loadnextlistener;
	// 底部的刷新按钮,y用于加载更多内容
	private boolean isloading = false;
	private int last_item_position = 0;
	private int visibleItemCount = 0;
	private Button loadMore;
	private ProgressBar processbar;
	private Context context;
	private View loadMoreView;
	private boolean isEPGMode = false;
	private boolean isMessageMode = false;
	
	private boolean isRefreshable;

	public PullToRefreshListView1(Context context) {
		super(context);
		init(context);
	}

	public PullToRefreshListView1(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public void initToEPGListviewMode()
	{
		isEPGMode = true;
		tipsTextview.setText(R.string.pull_to_previous_epg_label);
		loadMore.setText(R.string.pull_to_next_epg_label);
	}
	
	public void initToMessageListviewMode()
	{
		isMessageMode = true;
		tipsTextview.setText(R.string.social_get_message_history);
	}
	
	private void init(Context context) {
		this.context = context;
		setCacheColorHint(context.getResources().getColor(
				android.R.color.transparent));
		inflater = LayoutInflater.from(context);

		headView = (LinearLayout) inflater.inflate(
				R.layout.pull_to_refresh_header1, null);

		arrowImageView = (ImageView) headView
				.findViewById(R.id.head_arrowImageView);
		arrowImageView.setMinimumWidth(70);
		arrowImageView.setMinimumHeight(50);
		progressBar = (ProgressBar) headView
				.findViewById(R.id.head_progressBar);
		tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
		lastUpdatedTextView = (TextView) headView
				.findViewById(R.id.head_lastUpdatedTextView);

		measureView(headView);
		headContentHeight = headView.getMeasuredHeight();
		headContentWidth = headView.getMeasuredWidth();

		headView.setPadding(0, -1 * headContentHeight, 0, 0);
		headView.invalidate();

		Log.v("size", "width:" + headContentWidth + " height:"
				+ headContentHeight);

		addHeaderView(headView, null, false);
		setOnScrollListener(this);

		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);

		state = DONE;
		isRefreshable = false;
		//new add 
		loadMoreView = inflater.inflate(
				R.layout.load_more, null);
		loadMore = (Button) loadMoreView.findViewById(R.id.load_more);
		processbar = (ProgressBar) loadMoreView.findViewById(R.id.progressBar);
		loadMore.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isloading) {
					onLoadMore();
				}
			}
		});
		addFooterView(loadMoreView);
		setHeaderDividersEnabled(false);
		setFooterDividersEnabled(false);
	}

	public void deleteFooterView()
	{
		this.removeFooterView(loadMoreView);
	}
	
	public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		firstItemIndex = firstVisibleItem;
		// load More
		this.visibleItemCount = visibleItemCount;
		last_item_position = firstVisibleItem + visibleItemCount - 1;
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	public boolean onTouchEvent(MotionEvent event) {

		firstItemIndex = getFirstVisiblePosition();

		if (isRefreshable) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (firstItemIndex == 0 && !isRecored) {
					isRecored = true;
					startY = (int) event.getY();
				}
				break;

			case MotionEvent.ACTION_UP:

				if (state != REFRESHING && state != LOADING) {
					if (state == DONE) {
					}
					if (state == PULL_To_REFRESH) {
						state = DONE;
						changeHeaderViewByState();

					}
					if (state == RELEASE_To_REFRESH) {
						state = REFRESHING;
						changeHeaderViewByState();
						onRefresh();

					}
				}

				isRecored = false;
				isBack = false;

				break;

			case MotionEvent.ACTION_MOVE:
				int tempY = (int) event.getY();

				if (!isRecored && firstItemIndex == 0) {
					isRecored = true;
					startY = tempY;
				}

				if (state != REFRESHING && isRecored && state != LOADING) {

					if (state == RELEASE_To_REFRESH) {

						setSelection(0);
						//EPG模式下，下拉幅度小就可以刷新
						int pullheight = headContentHeight;
						if(isEPGMode)
							pullheight = 60;
						if (((tempY - startY) / RATIO < pullheight)
								&& (tempY - startY) > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();

						} else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();

						} else {
						}
					}
					if (state == PULL_To_REFRESH) {

						setSelection(0);
						//EPG模式下，下拉幅度小就可以刷新
						int pullheight = headContentHeight;
						if(isEPGMode)
							pullheight = 60;
						if ((tempY - startY) / RATIO >= pullheight) {
							state = RELEASE_To_REFRESH;
							isBack = true;
							changeHeaderViewByState();

						} else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();

						}
					}

					if (state == DONE) {
						if (tempY - startY > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
						}
					}

					if (state == PULL_To_REFRESH) {
						headView.setPadding(0, -1 * headContentHeight
								+ (tempY - startY) / RATIO, 0, 0);

					}

					if (state == RELEASE_To_REFRESH) {
						headView.setPadding(0, (tempY - startY) / RATIO
								- headContentHeight, 0, 0);
					}

				}

				break;
			}
		}

		return super.onTouchEvent(event);
	}

	private void changeHeaderViewByState() {
		switch (state) {
		case RELEASE_To_REFRESH:
			arrowImageView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			arrowImageView.clearAnimation();
			arrowImageView.startAnimation(animation);
			if(isEPGMode)
				tipsTextview.setText(R.string.pull_to_loading_epg_release_label);
			else
				tipsTextview.setText(R.string.pull_to_refresh_release_label);

			break;
		case PULL_To_REFRESH:
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.VISIBLE);
			if (isBack) {
				isBack = false;
				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(reverseAnimation);
				if(isEPGMode)
					tipsTextview.setText(R.string.pull_to_previous_epg_label);
				else if(isMessageMode)
					tipsTextview.setText(R.string.social_get_message_history);
				else
					tipsTextview.setText(R.string.pull_to_refresh_pull_label);
			} else {
				if(isEPGMode)
					tipsTextview.setText(R.string.pull_to_previous_epg_label);
				else if(isMessageMode)
					tipsTextview.setText(R.string.social_get_message_history);
				else
					tipsTextview.setText(R.string.pull_to_refresh_pull_label);
			}
			break;

		case REFRESHING:

			headView.setPadding(0, 0, 0, 0);

			progressBar.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.GONE);
			if(isEPGMode)
				tipsTextview.setText(R.string.pull_to_loading_epg_label);
			else
				tipsTextview.setText(R.string.pull_to_refresh_refreshing_label);
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			break;
		case DONE:
			headView.setPadding(0, -1 * headContentHeight, 0, 0);

			progressBar.setVisibility(View.GONE);
			arrowImageView.clearAnimation();
			arrowImageView.setImageResource(R.drawable.base_refresh);
			if(isEPGMode)
				tipsTextview.setText(R.string.pull_to_previous_epg_label);
			else if(isMessageMode)
				tipsTextview.setText(R.string.social_get_message_history);
			else
				tipsTextview.setText(R.string.pull_to_refresh_pull_label);
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			break;
		}
	}

	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
		mOnRefreshListener = onRefreshListener;
		isRefreshable = true;
	}

	public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
		mOnLoadMoreListener = onLoadMoreListener;
	}

	//only for EPG
	public void setOnPreviousDataLoadListener(OnLoadPreviousDayEPGDataListener onPrevListener) {
		loadprevlistener = onPrevListener;
		isRefreshable = true;
	}

	public void setOnNextDataLoadListener(OnLoadNextDayEPGDataListener onNextListener) {
		loadnextlistener = onNextListener;
	}
	
	public void onRefresh() {
		if(isEPGMode){
			if(loadprevlistener != null){
				loadprevlistener.onPreviousLoad();
			}
		}else{
			if (mOnRefreshListener != null) {
				mOnRefreshListener.onRefresh();

			}
		}
	}

	public void onLoadMore() {
		processbar.setVisibility(View.VISIBLE);
		loadMore.setVisibility(View.GONE);
		isloading = true;
		if(isEPGMode){
			if(loadnextlistener != null){
				loadnextlistener.onNextLoad();
			}
		}else{
			if (mOnLoadMoreListener != null) {
				mOnLoadMoreListener.onClickLoadMore();
			}
		}
	}
	

	public void onRefreshComplete() {
		state = DONE;
		lastUpdatedTextView.setText(context.getString(R.string.pull_to_refresh_date) + new Date().toLocaleString());
		changeHeaderViewByState();
	}

	public void onLoadMoreComplete() {
		resetFooter();
	}
	
	public void onPreviousLoadComplete() {
		state = DONE;
		lastUpdatedTextView.setText(context.getString(R.string.pull_to_refresh_date) + new Date().toLocaleString());
		changeHeaderViewByState();	
	}

	public void onNextLoadComplete() {
		resetFooter();
	}
	
	private void resetFooter() {
		processbar.setVisibility(View.GONE);
		loadMore.setVisibility(View.VISIBLE);
		isloading = false;
		invalidateViews();
		setSelection(last_item_position - visibleItemCount + 1);
	}

	public void showLoadMoreFooterVeiw() {
		loadMoreView.setVisibility(View.VISIBLE);
	}

	public void hideLoadMoreFooterView()
	{
		loadMoreView.setVisibility(View.GONE);
	}
	
	public interface OnRefreshListener {
		public void onRefresh();
	}

	public interface OnLoadMoreListener {
		public void onClickLoadMore();
	}
	
	//only for EPG
	public interface OnLoadPreviousDayEPGDataListener {
		public void onPreviousLoad();
	}

	public interface OnLoadNextDayEPGDataListener {
		public void onNextLoad();
	}
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	public void setAdapter(BaseAdapter adapter) {
		lastUpdatedTextView.setText(context.getString(R.string.pull_to_refresh_date) + new Date().toLocaleString());
		super.setAdapter(adapter);
	}

}

