package com.igrs.tivic.phone.widget;

import com.igrs.tivic.phone.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ImageTextView extends FrameLayout{

	private AsyncImageView iv;
	private TextView left_title_name;
	private ImageView right_icon_delete,video_icon;
	
	private RelativeLayout mask_container;
	private FrameLayout root;
	
	public ImageTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		root = (FrameLayout) li.inflate(R.layout.imagetext_layout, this);
//		root.setBackgroundResource(R.drawable.waterfall_textview_bg);
		iv = (AsyncImageView)root.findViewById(R.id.iv);
		left_title_name = (TextView)root.findViewById(R.id.left_title_name);
		
		right_icon_delete = (ImageView)root.findViewById(R.id.right_icon_delete);
		video_icon = (ImageView)root.findViewById(R.id.video_icon);
		mask_container = (RelativeLayout)root.findViewById(R.id.mask_container);
	}
	
	public void setMyBackRes(int resid){
		root.setBackgroundResource(resid);
	}
	public void setImageUrl(String url){
		iv.setUrl(url);
	}
	public AsyncImageView getIv() {
		return iv;
	}

	//设置蒙版左侧的icon
	public void setMaskLeftIcon(int resid){
		Drawable drawable = getResources().getDrawable(resid);
		// 这一步必须要做,否则不会显示.
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		left_title_name.setCompoundDrawables(drawable, null, null, null);
	}
	
	public void setLeftText(String content){
        left_title_name.setText(content);
	}
	
	public void setDeleteIconVisible(int visible){
		right_icon_delete.setVisibility(visible);
	}
	public void setMaskColor(int color){
		mask_container.setBackgroundColor(color);
	}
	public void setMaskResource(int resid){
		mask_container.setBackgroundResource(resid);
	}
	public void setMaskVisiable(int visible){
		mask_container.setVisibility(visible);
	}
	public ImageView getRight_icon_delete() {
		return right_icon_delete;
	}
	public void setBackGroundImageView(int imageResource){
		root.setBackgroundResource(imageResource);
	}
	public ImageView getVideo_icon() {
		return video_icon;
	}
}
