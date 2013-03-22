package com.igrs.tivic.phone.Activity;

import java.io.File;
import java.io.IOException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Bean.VisitBean;
import com.igrs.tivic.phone.Bean.Base.BaseParamBean;
import com.igrs.tivic.phone.Bean.PGC.ContentPublishReplyBean;
import com.igrs.tivic.phone.Bean.UGC.UGCPublishBean;
import com.igrs.tivic.phone.Bean.UGC.UGCPublishRetBean;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.ShowProgressDialog;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Impl.ContentImpl;
import com.igrs.tivic.phone.Impl.UGCImpl;
import com.igrs.tivic.phone.Utils.ModelUtil;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;
import com.igrs.tivic.phone.ViewGroup.PullToRefreshListView1;
import com.igrs.tivic.phone.ViewGroup.ScrollForeverTextView;
import com.igrs.tivic.phone.widget.AsyncImageView;

public class ContentPublishActivity extends Activity implements OnClickListener{

	private ImageView content_back;
	private ImageView publish_button;
	private static final int REQUESTCODE_PHOTOS = 0;//请求相册
	private static final int REQUESTCODE_CAMERA = 1;//请求照相机
	public static final int REQUESTCODE_SCREENSHOT = REQUESTCODE_CAMERA +1;//请求截屏

	private EditText c_text;
	private EditText c_title;
	private TextView comment_numberwords;
	private static final int MAX_TEXT_LENGTH = 140;//最大输入字数
	private static final int MAX_TITLE_LENGTH = 30;//最大输入字数
	private ImageView comment_image;
	private AsyncImageView selected_image;
	private String theLarge;
	private String theThumbnail;
	private UGCImpl ugcImpl;
	private ContentImpl contentImpl;
	private Handler ugcUpdateHandler; 
	private String TAG = "ContentPublishActivity";
	private int currentTid = 0;  //传进来的帖子id
	private int currentContentid = 0; //传进来的文章id
	private int currentPid = 0; //传进来的节目id
	private LayoutInflater mInflater;
	private AlertDialog imageDialog;
	private InputMethodManager imm;
	private ImageButton relative_sina_button;
	private boolean relative_sina = false;//发帖 或者评论 是否关联到sina微博
	private Toast mToast;
	private boolean isBoundSinaWeibo = false;	//帐号是否绑定到微博
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_publish_layout);
		//软键盘管理类
		imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		//add zhanglr
		ugcImpl = UGCImpl.getInstance();
		contentImpl = ContentImpl.getInstance();
		currentTid = getIntent().getIntExtra("tid", 0);
		currentPid = getIntent().getIntExtra("pid", 102);
		currentContentid = getIntent().getIntExtra("content_id", 0);
		mInflater = LayoutInflater.from(this);
		isBoundSinaWeibo = TivicGlobal.userRegisterBean.isBoundWeibo();
		this.initView();
	}
	@Override
	protected void onResume() {
		super.onResume();
		EditText edittext = (EditText)findViewById(R.id.c_edit);
		if(UIUtils.getCurrentFuncID() == FuncID.ID_UGC)//发新帖
		{	
			c_title.setVisibility(View.VISIBLE);
			edittext.setHint(R.string.default_text_comment_content);
		}else if(UIUtils.getCurrentFuncID() == FuncID.ID_CONTENT){
			//文章评论
			edittext.setHint(R.string.default_text_comment);
		}else{
			//帖子评论
			edittext.setHint(R.string.default_text_comment);
		}
	}

	
	private void initView() {
		//设置topBar
		ImageView text_title = (ImageView) findViewById(R.id.icon_title);
		ScrollForeverTextView content_name = (ScrollForeverTextView) findViewById(R.id.content_name);
		VisitBean vBean = TivicGlobal.getInstance().currentVisit;
				
		if(TivicGlobal.getInstance().currentVisit != null){
//			text_title.setText(vBean.getVisit_ChannelName());
//			text_title.setCompoundDrawablesWithIntrinsicBounds(0, Utils.setChannelIcon(this, vBean.getVisit_ChannelName()), 0, 0);
			if(Utils.setChannelIconSmall(this, vBean.getVisit_ChannelName()) != 0){
				text_title.setBackgroundResource(Utils.setChannelIconSmall(this, vBean.getVisit_ChannelName()));
			}
			content_name.setText(vBean.getVisit_ProgramTitle());
		}
		publish_button = (ImageView) findViewById(R.id.publish_button);
		publish_button.setOnClickListener(this);
		content_back = (ImageView) findViewById(R.id.content_back);
		content_back.setOnClickListener(this);
		Button select_pictures = (Button) findViewById(R.id.select_pictures_button);
		select_pictures.setOnClickListener(this);
		LinearLayout clearwords_layout = (LinearLayout) findViewById(R.id.tweet_pub_clearwords);
		clearwords_layout.setOnClickListener(this);
		
		relative_sina_button = (ImageButton) findViewById(R.id.relative_sina_button);
		relative_sina_button.setOnClickListener(this);
		
		comment_numberwords = (TextView) findViewById(R.id.comment_numberwords);
		comment_image = (ImageView) findViewById(R.id.comment_imageview);
		selected_image = (AsyncImageView) findViewById(R.id.selected_imageview);
		selected_image.setOnClickListener(this);
		c_text = (EditText) findViewById(R.id.c_edit);
		c_title = (EditText)findViewById(R.id.c_title);
		//编辑器添加文本监听
		c_text.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//显示剩余可输入的字数
				comment_numberwords.setText(MAX_TEXT_LENGTH - s.length() + "");
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		//设置最大输入字数
		InputFilter[] text_filters = new InputFilter[1];
		text_filters[0] = new InputFilter.LengthFilter(MAX_TEXT_LENGTH);
		c_text.setFilters(text_filters);
		InputFilter[] title_filters = new InputFilter[1];
		title_filters[0] = new InputFilter.LengthFilter(MAX_TITLE_LENGTH);
		c_title.setFilters(title_filters);
		
		if(ugcUpdateHandler == null)
			ugcUpdateHandler = new Handler(){
			int replyCount = 0;
			@Override
			public void handleMessage(Message msg) {
				switch (msg.arg1) {
				case UGCImpl.MSG_UGC_NEW:
					ShowProgressDialog.dismiss();
					UGCPublishRetBean listbean = (UGCPublishRetBean) msg.obj;
					if (listbean != null) {						
						publish(true,0);
					}else{
						publish(false,0);
					}
					// update ui
					
					break;
				case UGCImpl.MSG_REPLY_PUBLISH:
					ShowProgressDialog.dismiss();
					replyCount = msg.arg2;
					if(replyCount > 0){						
						publish(true, replyCount);
					}else{
						publish(false, 0);
					}
					break;
				case ContentImpl.MSG_REPLY_PUBLISH:
					ShowProgressDialog.dismiss();
					replyCount = msg.arg2;
					if(replyCount > 0){						
						publish(true, replyCount);
					}else{
						publish(false, 0);
					}
					break;
					
				}

			}
		};
		ugcImpl.setUgcUpdateUIHandler(ugcUpdateHandler);
		contentImpl.setContentUpdateHandler(ugcUpdateHandler);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.selected_imageview:
			deleteImage();
			break;
		case R.id.tweet_pub_clearwords:
			if(!TextUtils.isEmpty(c_text.getText().toString()))
			clearWords();
			break;
		case R.id.content_back:
			this.finish();
			break;
		case R.id.publish_button:
			sendPublish();
			break;
		case R.id.select_pictures_button:
			//隐藏软键盘
			imm.hideSoftInputFromWindow(c_text.getWindowToken(), 0);
			imageChooseItem();//选择图片
			break;
		case R.id.relative_sina_button:
			int describe_id = 0;
//			UIUtils.Loge("liq", "isBoundSinaWweibo : "+isBoundSinaWeibo+"   relative_sina : "+relative_sina);
			if(isBoundSinaWeibo){
				if(relative_sina){
					relative_sina_button.setBackgroundResource(R.drawable.relative_sina_false);
					describe_id = R.string.un_bound_sina;
					relative_sina = false;
				}else{
					describe_id = R.string.bound_sina;
					relative_sina_button.setBackgroundResource(R.drawable.relative_sina_true);
					relative_sina = true;
				}
			}else{
				describe_id = R.string.prompt_bound_sina;
			}
			if(mToast == null){
				mToast = Toast.makeText(this, describe_id, Toast.LENGTH_SHORT);
			}else{
				mToast.setText(describe_id);
			}
			mToast.show();
			break;
		default:
			break;
		}
	}
	/**
	 * 删除文字
	 */
	private void clearWords() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.clear_words);
		builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//清除文字
				c_text.setText("");
				comment_numberwords.setText("140");
			}
		});
		builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
		
	}
	private void deleteImage() {
		imm.hideSoftInputFromWindow(c_text.getWindowToken(), 0);
		new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_info)
		.setTitle(getString(R.string.delete_image))
		.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				selected_image.setVisibility(View.GONE);
				if(theLarge != null)
				theLarge = null;
				dialog.dismiss();
			}
		})
		.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).create().show();
		
	}
	//选择获取从什么地方 获取 图片
	private void imageChooseItem() {
		FrameLayout ImageslistView = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.content_viewpager_listview, null);
		PullToRefreshListView1 listView = (PullToRefreshListView1) ImageslistView.findViewById(R.id.listview);
		listView.deleteFooterView();
		ImageAdapter imageAdapter = new ImageAdapter();
		listView.setAdapter(imageAdapter);
		
		imageDialog = new AlertDialog.Builder(this).setView(ImageslistView)
				.setTitle(R.string.choose_image)
				.setIcon(android.R.drawable.btn_star)
				.create();
		imageDialog.show();
	}
	private class ImageAdapter extends BaseAdapter{
//		public int [] image_ids = new int []{R.drawable.content_gallery_button,R.drawable.content_photograph_button,R.drawable.screenshot_button};
		CharSequence [] textItems = {ContentPublishActivity.this.getString(R.string.social_xiangce)
				,ContentPublishActivity.this.getString(R.string.social_paizhao)
				,ContentPublishActivity.this.getString(R.string.social_screen_shot)};
		public ImageAdapter(){
			
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = mInflater.inflate(R.layout.image_listview_item, null);
				convertView.setTag(convertView);
			}else{
				convertView.getTag();
			}
//			ImageButton image_button = (ImageButton) convertView.findViewById(R.id.image_button);
//			image_button.setBackgroundResource(image_ids[position]);
			TextView text_view = (TextView) convertView.findViewById(R.id.text_view);
			text_view.setText(textItems[position]);
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(position == 0){//打开图库
						Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
						getImage.addCategory(Intent.CATEGORY_OPENABLE);
						getImage.setType("image/*");
						startActivityForResult(Intent.createChooser(getImage, getResources().getString(R.string.choose_image)), REQUESTCODE_PHOTOS);
					}else if(position == 1){//拍照
						takePhoto();
					}else if(position == 2){//screen shot
						Intent intent = new Intent(ContentPublishActivity.this,ScreenShotActivity.class);
						startActivityForResult(intent, REQUESTCODE_SCREENSHOT);
					}
					imageDialog.dismiss();
					UIUtils.Logd("lq", " alert : "+ v);
				}
			});
			
			return convertView;
		}
		
	}
	/**
	 * 手机拍照
	 */
	private void takePhoto() {
		String savePath = "";
		String storageState = Environment.getExternalStorageState();
		if(storageState.equals(Environment.MEDIA_MOUNTED)){
			savePath = Const.PHOTO_DIR;
			File savedir = new File(savePath);
			if(!savedir.exists()){
				savedir.mkdirs();
			}
		}
		//没有挂载SD卡，无法保存文件
		if(ModelUtil.isEmpty(savePath)){
			Toast.makeText(this, getString(R.string.sdcard_not_exists), Toast.LENGTH_SHORT).show();
		}
		
//		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String fileName = Const.PHOTO_FILE;//照片命名
		File out = new File(savePath, fileName);
		Uri uri = Uri.fromFile(out);
		
		theLarge = savePath + fileName;//该照片的绝对路径
		
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(intent, REQUESTCODE_CAMERA);
	}
	
	private void publish(boolean ret, int replyCount)
	{
		if(ret)
		{
			comment_image.setVisibility(View.GONE);
			c_text.setText("");
			c_title.setText("");
			c_text.clearComposingText();
			this.finish();
		}
		else
		{	
			Toast.makeText(this, getString(R.string.social_publish_failed), Toast.LENGTH_SHORT).show();
 
		}
	}
	
	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		if(resultCode != RESULT_OK) return;
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == 1 && msg.obj !=null){
					selected_image.setImageBitmap((Bitmap)msg.obj);
					selected_image.setVisibility(View.VISIBLE);
				}
			}
		};
		new Thread(){
			public void run() {
				Bitmap bitmap = null;
				if (requestCode == REQUESTCODE_PHOTOS) {
					
					if(data == null) return;
					Uri thisUri = data.getData();
					String thePath = Utils.getAbsolutePathFromNoStandardUri(thisUri);
					if(ModelUtil.isEmpty(thePath)){
						theLarge = Utils.getAbsoluteImagePath(ContentPublishActivity.this, thisUri);
					}else{
						theLarge = thePath;
					}
									
					
					//获取图片缩略图 只有Android2.1以上版本支持
					String imageName = Utils.getFileName(theLarge);
					bitmap = Utils.loadImgThumbnail(ContentPublishActivity.this, imageName, MediaStore.Images.Thumbnails.MICRO_KIND);
				} else if (requestCode == REQUESTCODE_CAMERA) {//拍摄图片
					theLarge = Const.PHOTO_DIR + Const.PHOTO_FILE;

				}else if(requestCode == REQUESTCODE_SCREENSHOT){//截屏图片
					bitmap = null;
					theLarge = data.getStringExtra("image_path");
				}
				if(bitmap == null && !ModelUtil.isEmpty(theLarge))
	        	{
	        		bitmap = Utils.loadImgThumbnail(theLarge, 100, 100);
	        	}
				if(bitmap != null){
					
					String savePath = Const.PHOTO_DIR;
					File savedir = new File(savePath);
					if(!savedir.exists()){
						savedir.mkdirs();
					}
					String largeFileName = Utils.getFileName(theLarge);
					String largeFilePath = savePath + largeFileName;
					//判断是否已存在缩略图
					if(largeFileName.startsWith("thumb_") && new File(largeFilePath).exists()) 
					{
						theThumbnail = largeFilePath;
					}else{
						//生成上传的800宽度图片
						String thumbFileName = "thumb_" + largeFileName;
						theThumbnail = savePath + thumbFileName;
						if(new File(theThumbnail).exists()){
						}else{
							//压缩上传的图片
							try {
								Utils.createImageThumbnail(ContentPublishActivity.this, theLarge, Const.PHOTO_DIR+"thumb_"+Const.PHOTO_FILE, 800, 80);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					//保存评论临时图片
					Message msg = new Message();
					msg.what = 1;
					msg.obj = bitmap;
					handler.sendMessage(msg);
				}
				
				
			};
		}.start();
	}
	
	public void sendPublish()
	{
		
		if(UIUtils.getCurrentFuncID() == FuncID.ID_UGC)//发新帖
		{	
			String title = c_title.getText().toString();
			String content = c_text.getText().toString();
			String imgpath = theLarge;
			int type = 2;
			if(imgpath != null && !imgpath.startsWith("http://"))
				type = 0;
			else if(imgpath != null && imgpath.startsWith("http://")){
				type = 1;
			}else if(!relative_sina){
				type = 2;
			}
			if(title == null || title.length() == 0)
			{
				Toast.makeText(this, getString(R.string.ugc_title_empty), Toast.LENGTH_SHORT).show();
				return;
			}
			if((content == null || content.length() == 0) && (imgpath == null || imgpath.isEmpty()))
			{
				Toast.makeText(this, getString(R.string.ugc_content_empty), Toast.LENGTH_SHORT).show();
				return;
			}
			ShowProgressDialog.show(this, null, null);

			BaseParamBean param = new BaseParamBean();
			param.setVer(1);
			param.setUid(TivicGlobal.getInstance().userRegisterBean.getUserId());
			param.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
			if(TivicGlobal.getInstance().currentVisit.getVisit_pid() != null)
				currentPid = Integer.parseInt(TivicGlobal.getInstance().currentVisit.getVisit_pid());
			param.setPid(currentPid);
			UGCPublishBean bean = new UGCPublishBean();
			bean.setContent(content);
			bean.setPhotoUrl(imgpath);
			bean.setPid(currentPid);
			if(relative_sina){
				bean.setSyncSinaFlag(1);
			}else{
				bean.setSyncSinaFlag(0);
			}
			bean.setTitle(title);
			bean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserId());
			if(theLarge == null)
				bean.setPhotoUrl("");
			else	
				bean.setPhotoUrl(theLarge);
			bean.setType(type);
			ugcImpl.newPublish(param, bean);
			
		}else if(UIUtils.getCurrentFuncID() == FuncID.ID_CONTENT){
			//文章评论
			String content = c_text.getText().toString();
			String imgpath = theLarge;
			int type = 2;
			if(imgpath != null && !imgpath.startsWith("http://"))
				type = 0;
			else if(imgpath != null && imgpath.startsWith("http://")){
				type = 1;
			}else if(!relative_sina){
				type = 2;
			}

			if((content == null || content.length() == 0) && (imgpath == null || imgpath.isEmpty()))
			{
				Toast.makeText(this, getString(R.string.ugc_content_empty), Toast.LENGTH_SHORT).show();
				return;
			}
			ShowProgressDialog.show(this, null, null);

			BaseParamBean param = new BaseParamBean();
			param.setVer(1);
			param.setUid(TivicGlobal.getInstance().userRegisterBean.getUserId());
			param.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
			param.setContentId(String.valueOf(currentContentid));
			ContentPublishReplyBean bean = new ContentPublishReplyBean();
			bean.setReplyText(content);
			bean.setPhotoUrl(imgpath);
			bean.setContentId(currentContentid);
			bean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserId());
			if(relative_sina){
				bean.setSyncSinaFlag(1);
			}else{
				bean.setSyncSinaFlag(0);
			}
			if(theLarge == null)
				bean.setPhotoUrl("");
			else	
				bean.setPhotoUrl(theLarge);
			bean.setType(type);
			contentImpl.replyPublish(param, bean);
		}else{
			//回帖或评论
			String content = c_text.getText().toString();
			String imgpath = theLarge;
			int type = 2;
			if(imgpath != null && !imgpath.startsWith("http://"))
				type = 0;
			else if(imgpath != null && imgpath.startsWith("http://")){
				type = 1;
			}else if(!relative_sina){
				type = 2;
			}

			if((content == null || content.length() == 0) && (imgpath == null || imgpath.isEmpty()))
			{
				Toast.makeText(this, getString(R.string.ugc_content_empty), Toast.LENGTH_SHORT).show();
				return;
			}
			ShowProgressDialog.show(this, null, null);

			BaseParamBean param = new BaseParamBean();
			param.setVer(1);
			param.setUid(TivicGlobal.getInstance().userRegisterBean.getUserId());
			param.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
			param.setPid(currentPid);
			param.setTid(currentTid);
			UGCPublishBean bean = new UGCPublishBean();
			bean.setContent(content);
			bean.setPhotoUrl(imgpath);
			if(TivicGlobal.getInstance().currentVisit.getVisit_pid()!=null)
				bean.setPid(Integer.parseInt(TivicGlobal.getInstance().currentVisit.getVisit_pid()));
			else
				bean.setPid(102);
			if(relative_sina){
				bean.setSyncSinaFlag(1);
			}else{
				bean.setSyncSinaFlag(0);
			}
			bean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserId());
			if(theLarge == null)
				bean.setPhotoUrl("");
			else	
				bean.setPhotoUrl(theLarge);
			bean.setType(type);
			ugcImpl.replyPublish(param, bean);
		}
	}
	
	
	


}
