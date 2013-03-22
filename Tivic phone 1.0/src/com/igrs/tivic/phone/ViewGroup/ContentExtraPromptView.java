package com.igrs.tivic.phone.ViewGroup;

import com.igrs.tivic.phone.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 1, 未登录 提示 登录 注册 2, 内容没有评论,提示添加评论
 * 
 * @author admin
 * 
 */
public class ContentExtraPromptView extends LinearLayout implements
		OnClickListener {
	private Context mContext;
	private LayoutInflater mInflater;
	private TextView content_comment;
	private Boolean isPublish;//false 表示没有评论 ,跳转到添加评论界面.true 表示 没有登录,跳转到登录注册界面

	public ContentExtraPromptView(Context context) {
		super(context);
	}

	public ContentExtraPromptView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context.getApplicationContext();
		mInflater = LayoutInflater.from(mContext);
		initView();
	}

	public ContentExtraPromptView(Context context, Boolean isPublish) {
		super(context);
		mContext = context.getApplicationContext();
		mInflater = LayoutInflater.from(mContext);
		this.isPublish = isPublish;
		initView();
	}

	private void initView() {
		if (isPublish) {
			mInflater.inflate(R.layout.publish_prompt, this, true);
			TextView content_log = (TextView) findViewById(R.id.content_login);
			content_log.setOnClickListener(this);
			TextView content_reg = (TextView) findViewById(R.id.content_reg);
			content_reg.setOnClickListener(this);
		} else {
			mInflater.inflate(R.layout.prompt_to_publish, this, true);
			content_comment = (TextView) findViewById(R.id.content_comment);
			content_comment.setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		publish.jumpToPublish(id);
	}

	public interface Publish {
		public void jumpToPublish(int id);
	}

	private Publish publish;

	public void setPublish(Publish publish) {
		this.publish = publish;
	}
}
