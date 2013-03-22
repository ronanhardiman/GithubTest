package com.igrs.tivic.phone.Fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Activity.ScreenShotActivity;
import com.igrs.tivic.phone.Activity.SocialBaseActivity;
import com.igrs.tivic.phone.Adapter.SocialMessageDetailListViewAdapter;
import com.igrs.tivic.phone.Bean.ScreenShotsBean;
import com.igrs.tivic.phone.Bean.Base.BaseParamBean;
import com.igrs.tivic.phone.Bean.Social.SocialMessageBean;
import com.igrs.tivic.phone.Bean.Social.SocialMessageHistoryListBean;
import com.igrs.tivic.phone.Bean.Social.SocialMessageListBean;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.ShowProgressDialog;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Impl.MessageImpl;
import com.igrs.tivic.phone.Utils.ModelUtil;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;
import com.igrs.tivic.phone.ViewGroup.PullToRefreshListView1;
import com.igrs.tivic.phone.ViewGroup.PullToRefreshListView1.OnLoadMoreListener;
import com.igrs.tivic.phone.ViewGroup.PullToRefreshListView1.OnRefreshListener;
import com.igrs.tivic.phone.widget.AsyncImageView;

public class SocialMessageDetailFragment extends SocialBaseFragment implements
		OnRefreshListener, OnLoadMoreListener {

	LinkedList<SocialMessageBean> myList = new LinkedList<SocialMessageBean>();
	SocialMessageDetailListViewAdapter myAdapter;
	PullToRefreshListView1 messageList;
	ViewGroup msgdetailmain;
	private ImageView sendButton = null;
	private EditText contentEditText = null;
	Handler updateUItHandler;
	private MessageImpl messageInterface;
	private String TAG = "SocialMessageDetailFragment";
	int partner_id = 0;
	int page_flag = 0;
	int totalCount = 0;
	private int msg_id_offset = 0;
	private boolean isSending = false;
	// add new
	private static final int MAX_TEXT_LENGTH = 140;// 最大输入字数
	private AsyncImageView imageSubnail;
	private static final int REQUESTCODE_PHOTOS = 200;// 请求相册
	private static final int REQUESTCODE_CAMERA = 201;// 请求照相机
	private static final int REQUESTCODE_SNAPSHOT = 203;// 请求截屏
	private static final int MSG_REQUESTIMG = 204; // 返回的要发送的图片
	private static final int MSG_REQUESTIMGPATH = 205; // 返回的要发送的图片
	private static final int MSG_DELETEIMGPATH = 206; // 返回的要发送的图片

	private String imgPath; // 保存取得图片的全路径
	private InputMethodManager imm;
	private AlertDialog imageDialog;
	private LayoutInflater mInflater;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		msgdetailmain = (ViewGroup) inflater.inflate(R.layout.social_msgdetail,
				null);

		if (getArguments() != null)
			partner_id = getArguments().getInt("uid");

		initSocialMessageDetailLayout();
		mInflater = LayoutInflater.from(this.getActivity());

		return msgdetailmain;
	}

	private void initSocialMessageDetailLayout() {

		imm = (InputMethodManager) this.getActivity().getSystemService(
				Context.INPUT_METHOD_SERVICE);
		messageList = (PullToRefreshListView1) msgdetailmain
				.findViewById(R.id.id_list);
		messageList.initToMessageListviewMode();
		messageList.hideLoadMoreFooterView();
		messageList.setOnRefreshListener(this);
		messageList.setOnLoadMoreListener(this);

		if (myAdapter == null)
			myAdapter = new SocialMessageDetailListViewAdapter(mContext, myList);
		messageList.setAdapter(myAdapter);
		messageList.setItemsCanFocus(false);

		contentEditText = (EditText) msgdetailmain.findViewById(R.id.c_edit);
		imageSubnail = (AsyncImageView) msgdetailmain
				.findViewById(R.id.comment_image);
		imageSubnail.setDefaultImageResource(R.drawable.public_input_pic);
		imageSubnail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imm.hideSoftInputFromWindow(contentEditText.getWindowToken(), 0);
				imageChooseItem();// 选择图片

			}
		});
		setEditTextFilter();

		sendButton = (ImageView) msgdetailmain.findViewById(R.id.publish_button);
		sendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
					// 发送消息
				if (!isSending)
					sendMessage();
			}
		});

	}

	private void setEditTextFilter() {

		// 设置最大输入字数
		InputFilter[] filters = new InputFilter[1];
		filters[0] = new InputFilter.LengthFilter(MAX_TEXT_LENGTH);
		contentEditText.setFilters(filters);

	}


	/**
	 * 手机拍照
	 */
	private void takePhoto() {
		String savePath = "";
		String storageState = Environment.getExternalStorageState();
		if (storageState.equals(Environment.MEDIA_MOUNTED)) {
			savePath = Const.PHOTO_DIR;
			File savedir = new File(savePath);
			if (!savedir.exists()) {
				savedir.mkdirs();
			}
		}
		// 没有挂载SD卡，无法保存文件
		if (ModelUtil.isEmpty(savePath)) {
			Toast.makeText(mContext, getString(R.string.sdcard_not_exists), Toast.LENGTH_SHORT)
					.show();
		}

		String fileName = Const.PHOTO_FILE;// 照片命名
		File out = new File(savePath, fileName);
		Uri uri = Uri.fromFile(out);

		imgPath = savePath + fileName;// 该照片的绝对路径

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		getActivity().startActivityForResult(intent, REQUESTCODE_CAMERA);
	}

	Handler bitmapHandler = null;

	private void initMessageData() {
		if (bitmapHandler == null) {
			bitmapHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {

					if (msg.what == MSG_REQUESTIMG && msg.obj != null) {
						imageSubnail.setImageBitmap((Bitmap) msg.obj);

					} else if (msg.what == MSG_REQUESTIMGPATH
							&& msg.obj != null) {
						imgPath = (String)msg.obj;
					}else if(msg.what == MSG_DELETEIMGPATH){
						imgPath = null;
						imageSubnail.setImageResource(R.drawable.public_input_pic);
					}

				}
			};

		}
		SocialBaseActivity ac = (SocialBaseActivity) this.getActivity();
		ac.setUpdateSendMessageSubnailHandler(bitmapHandler);

		if (updateUItHandler == null) {
			updateUItHandler = new Handler() {

				@Override
				public void handleMessage(Message msg) {
					switch (msg.arg1) {
					case MessageImpl.MSG_GET_MESSAGE_HISTORY_LIST:
						int hispos = 0;
						SocialMessageHistoryListBean listbean = (SocialMessageHistoryListBean) msg.obj;
						if (listbean != null
								&& listbean.getMessageList() != null) {
							hispos = myList.size();
							totalCount = listbean.getCount();
							if (totalCount > 0) {
								myList.addAll(0,listbean.getMessageList());
							}
						} else {
							Log.i(TAG, "kevin add: msg.obj == null");
						}
						// update ui
						if (listbean!=null && (totalCount > myList.size() || listbean.getIfMore() == 1)) {
//							messageList.showLoadMoreFooterVeiw(); // 设置列表底部视图
							page_flag = 1;
						} else {
//							messageList.hideLoadMoreFooterView(); // 设置列表底部视图
							page_flag = 0;
						}

						myAdapter.notifyDataSetChanged();
						if (msg_id_offset == 0) {
							ShowProgressDialog.dismiss();
							messageList.setSelection(messageList.getCount()-1);
						} else {
							if(messageList.getCount()-hispos >= 0 )
							messageList.setSelection(messageList.getCount()-hispos);
						}
						messageList.onRefreshComplete();
//						if (listbean != null && listbean.getCount() > myList.size())
							msg_id_offset = myList.get(0)
									.getMid();
//						else
//							msg_id_offset = 0;
						break;
					case MessageImpl.MSG_SEND_MESSAGE:
						isSending = false;
						SocialMessageBean ret = (SocialMessageBean) msg.obj;
						if (ret != null) {
							// send(ret);
							imm.hideSoftInputFromWindow(contentEditText.getWindowToken(), 0);
							clearMessageData();
							initMessageData();
							contentEditText.setText("");
							imageSubnail
									.setImageResource(R.drawable.public_input_pic);
							imgPath = null;

						}else{
							UIUtils.ToastMessage(mContext, getString(R.string.social_message_failed));
						}

						break;

					}

				}
			};
		}
		if (myList == null) {
			myList = new LinkedList<SocialMessageBean>();
		}
		if (msg_id_offset == 0)
			myList.clear();
		messageInterface = MessageImpl.getInstance();
		messageInterface.setUpdateUIHandler(updateUItHandler);
		BaseParamBean param = new BaseParamBean();
		param.setUid(TivicGlobal.getInstance().userRegisterBean.getUserId()); // 给我发私信用户的uid，用于获取我俩所有消息纪录
		param.setPartner_id(partner_id);
		param.setVer(1);
		param.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
		param.setCountInPage(Const.COUNTINPAGE);
		param.setReqnum(Const.COUNTINPAGE);
		param.setPage_flag(page_flag);
		param.setMid(msg_id_offset);
		param.setOffset(msg_id_offset); // 同上
		param.setOpreate(1); // 将消息置为已读
		if (msg_id_offset == 0)
			ShowProgressDialog.show(this.getActivity(), null, null);
		messageInterface.getMsgHistoryList(param);

	}

	private void clearMessageData() {
		myList.clear();
		myAdapter.notifyDataSetChanged();
		if(messageList!=null)
			messageList.hideLoadMoreFooterView();
		msg_id_offset = 0;
		page_flag = 0;
	}

	@Override
	public void onRefresh() {
		//下拉加载更多历史记录
		loadMore();

	}

	@Override
	public void onClickLoadMore() {
		//点击更多刷新
		clearMessageData();
		initMessageData();
	}

	private void loadMore() {
		if (page_flag == 0)//msg_id_offset == 0 || page_flag == 0)
		{
			messageList.onRefreshComplete();
			UIUtils.ToastMessage(getActivity(), getString(R.string.social_get_history_done));
			return;
		}
		initMessageData();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onPause() {
		super.onPause();
		clearMessageData();
		imm.hideSoftInputFromWindow(contentEditText.getWindowToken(), 0);
	}

	@Override
	public void onStop() {
		super.onStop();

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public void onResume() {
		UIUtils.setCurrentFuncID(FuncID.ID_SOCIAL_MESSAGE_DETAIL);
		SocialBaseActivity ac = (SocialBaseActivity) this.getActivity();
		ac.setMenuBarFocus(FuncID.ID_SOCIAL_MESSAGE_DETAIL);
		super.onResume();
		initMessageData();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

	}

	public void sendMessage() {
		String messagetext = contentEditText.getText().toString();
//		Log.d(TAG, "kevin add: send imgPath = " + imgPath);

		if (messagetext.isEmpty() && (imgPath == null || imgPath.isEmpty())) {
			UIUtils.ToastMessage(mContext,
					getString(R.string.social_message_empty));
			return;
		}
		isSending = true;
		BaseParamBean param = new BaseParamBean();
		SocialMessageBean bean = new SocialMessageBean();

		bean.setText(messagetext);
		bean.setImgUrl(imgPath);
		bean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserId());
		bean.setComeMsg(false);
		if (imgPath == null || imgPath.length() == 0
				|| imgPath.startsWith("http://"))
			bean.setType(1);// 网络图片
		else
			bean.setType(0);// 本地图片

		param.setUid(TivicGlobal.getInstance().userRegisterBean.getUserId()); // 给我发私信用户的uid，用于获取我俩所有消息纪录
		param.setPartner_id(partner_id);
		param.setVer(1);
		param.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());

		messageInterface.sendMessage(param, bean);

	}

	// 选择获取从什么地方获取图片
	private void imageChooseItem() {
		FrameLayout ImageslistView = (FrameLayout) LayoutInflater.from(
				this.getActivity()).inflate(
				R.layout.content_viewpager_listview, null);
		PullToRefreshListView1 listView = (PullToRefreshListView1) ImageslistView
				.findViewById(R.id.listview);
		listView.deleteFooterView();
		ImageAdapter imageAdapter = new ImageAdapter();
		listView.hideLoadMoreFooterView();
		listView.setAdapter(imageAdapter);

		imageDialog = new AlertDialog.Builder(this.getActivity())
				.setView(ImageslistView).setTitle(R.string.choose_image)
				.setIcon(android.R.drawable.btn_star).create();
		imageDialog.show();
	}

	private class ImageAdapter extends BaseAdapter {
//		public int[] image_ids = new int[] { R.drawable.content_gallery_button,
//				R.drawable.content_photograph_button,
//				R.drawable.screenshot_button };
		CharSequence[] textItems = {
				SocialMessageDetailFragment.this
						.getString(R.string.social_xiangce),
				SocialMessageDetailFragment.this
						.getString(R.string.social_paizhao),
				SocialMessageDetailFragment.this
						.getString(R.string.social_screen_shot),
				SocialMessageDetailFragment.this
						.getString(R.string.social_delete_image)};

		public ImageAdapter() {

		}

		@Override
		public int getCount() {
			return textItems.length;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.image_listview_item,
						null);
				convertView.setTag(convertView);
			} else {
				convertView.getTag();
			}

			TextView text_view = (TextView) convertView
					.findViewById(R.id.text_view);
			text_view.setText(textItems[position]);
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (position == 0) {// 打开图库
						Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
						getImage.addCategory(Intent.CATEGORY_OPENABLE);
						getImage.setType("image/*");
						getActivity().startActivityForResult(Intent
								.createChooser(getImage, getResources()
										.getString(R.string.choose_image)),
								REQUESTCODE_PHOTOS);
					} else if (position == 1) {// 拍照
						takePhoto();
					} else if (position == 2) {// screen shot
						Intent intent = new Intent(
								SocialMessageDetailFragment.this.getActivity(),
								ScreenShotActivity.class);
						getActivity().startActivityForResult(intent, REQUESTCODE_SNAPSHOT);
					} else if (position == 3) {// 删除
						Message msg3 = new Message();
						msg3.what = MSG_DELETEIMGPATH;
						bitmapHandler.sendMessage(msg3);
					}
					imageDialog.dismiss();
				}
			});

			return convertView;
		}

	}

}