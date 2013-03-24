package com.igrs.tivic.phone.Adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Activity.ContentPublishActivity;
import com.igrs.tivic.phone.Activity.LoginActivity;
import com.igrs.tivic.phone.Activity.LoginRegistrationActivity;
import com.igrs.tivic.phone.Bean.PGC.ContentTypesBean;
import com.igrs.tivic.phone.Bean.PGC.NewsBean;
import com.igrs.tivic.phone.Bean.PGC.TVFindBean;
import com.igrs.tivic.phone.Bean.Waterfall.SendedDatasBean;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Impl.ContentImpl;
import com.igrs.tivic.phone.Parser.ContentDetailParser;
import com.igrs.tivic.phone.Utils.HttpClientUtils;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.ViewGroup.ContentsView.onButtonClickListener;
import com.igrs.tivic.phone.ViewGroup.ContentsView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ContentViewpagerAdapter extends PagerAdapter {
	private Context context;
	private int count;
	private Boolean mIsLogin;
	private ArrayList<ContentTypesBean> contentTypeBeanList;
	private ContentImpl cImpl;
	private HashMap<String, String> article_id_map;
	private ArrayList<String> id_list;
	
	public ContentViewpagerAdapter(Context context){
		cImpl = ContentImpl.getInstance();
		
		this.context = context;
	}
	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView((View)object);
	}
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		UIUtils.Logd("liq", "==========1111111111===instantiateItem");
		TivicGlobal.getInstance().currentCid = Integer.parseInt(id_list.get(position));
		View itemView = createItemView(contentTypeBeanList.get(position),isCollection(position));
		container.addView(itemView, 0);
		return itemView;
	}
	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		UIUtils.Logd("liq", "==========2222222222===setPrimaryItem");
		backgroundChangeListener.onClick(isCollection(position));
		TivicGlobal.getInstance().currentCid = Integer.parseInt(id_list.get(position));
	}
	//判断节目是否被收藏
	private boolean isCollection(int position) {
		Boolean isC = article_id_map.containsKey(String.valueOf(id_list.get(position)));
		return isC;
	}
	private View createItemView(ContentTypesBean contentTypesBean,Boolean isCollection) {
		ContentsView cView = new ContentsView(context);
		cView.setContent(contentTypesBean);
		cView.setOnButtonClickListener(new onButtonClickListener() {
			@Override
			public void onClick(int index) {
				switch (index) {
				case R.id.content_login:
					Intent content_intent = new Intent(context,LoginActivity.class);
					context.startActivity(content_intent);
					break;
				case R.id.content_reg:
					Intent content_register = new Intent(context,LoginRegistrationActivity.class);
					context.startActivity(content_register);
					break;
				case R.id.content_comment:
					Intent comment_intent = new Intent(context, ContentPublishActivity.class);
//					comment_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
					comment_intent.putExtra("content_id", TivicGlobal.getInstance().currentCid);
					context.startActivity(comment_intent);
					break;	
				/*case R.id.content_play:
					Intent it = new Intent(Intent.ACTION_VIEW); 
					//just for test
					String path ="http://forum.ea3w.com/coll_ea3w/attach/2008_10/12237832415.3gp";
					String path1 = "http://10.1.33.49/test/ido.mp4";//该测试视频 打开很慢.
					try {
						Uri uri = Uri.parse(path);
						it.setDataAndType(uri, "video/*");
						context.startActivity(it);
					} catch (ActivityNotFoundException e) {
						// TODO: handle exception
					}
					break;*/
				default:
					break;
				}
			}
		});
		return cView;
	}
	@Override
	public int getCount() {
		return count;
	}
	//重写这个方法 可以在发表评论后通过 viewpagerAdapter.notifyDataSetChanged()来让评论及时显示
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	public void setAdapterData(ArrayList<ContentTypesBean> contentTypeBeanList,ArrayList<String> id_list,HashMap<String, String> article_id_map) {
		this.contentTypeBeanList = contentTypeBeanList;
		this.id_list = id_list;
		this.article_id_map = article_id_map;
		count = contentTypeBeanList.size();
	}
	private onBackgroundChangeListener backgroundChangeListener;
	public interface onBackgroundChangeListener{
		public void onClick(Boolean isCollection);
	}
	public void setOnBackgroundListener(onBackgroundChangeListener backgroundChangeListener){
		this.backgroundChangeListener = backgroundChangeListener;
	}
}
