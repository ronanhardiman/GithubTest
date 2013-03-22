package com.igrs.tivic.phone.ViewGroup;

//import static android.util.Log.e;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.R.string;
import com.igrs.tivic.phone.Activity.BaseActivity;
import com.igrs.tivic.phone.Activity.SearchActivity;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;

public class BaseTopBar extends LinearLayout {

	private ImageView icon_backcontrl;
	private ImageView icon_func;
	private ImageView icon_channel;
	private ImageView icon_search;
	private ImageView icon_help;
	private ScrollForeverTextView text_func;
	private LinearLayout selector;
	private Button text_selector1;
	private Button text_selector2;
	private String TAG = "BaseTopBar";
	private Activity parentActivity;

	UGCNewClickListener onNewUGCListner;

	public BaseTopBar(Context context) {
		this(context, null);
	}

	Context m_context;
	TivicGlobal tivicGlobal = TivicGlobal.getInstance();

	public BaseTopBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		m_context = context;
		LayoutInflater.from(context).inflate(R.layout.base_top_bar, this, true);
		initLayout();
		init();
	}

	public void setOnUGCNewListner(UGCNewClickListener _onNewUGCListner) {
		this.onNewUGCListner = _onNewUGCListner;
	}

	public void initLayout() {

		icon_backcontrl = (ImageView) findViewById(R.id.icon_back_control);
		icon_func = (ImageView) findViewById(R.id.icon_func);
		icon_channel = (ImageView) findViewById(R.id.icon_channel);
		icon_search = (ImageView) findViewById(R.id.icon_search);

		if (UIUtils.getCurrentFuncID() == FuncID.ID_UGC) {
			icon_search.setImageResource(R.drawable.base_top_publish);
			icon_search.setVisibility(View.VISIBLE);
		} else if (UIUtils.getCurrentFuncID() == FuncID.ID_PUBLISH) {
			icon_search.setVisibility(View.INVISIBLE);
		}

		icon_help = (ImageView) findViewById(R.id.icon_help);
		text_func = (ScrollForeverTextView) findViewById(R.id.text_title);
		selector = (LinearLayout) findViewById(R.id.select_bar);
		text_selector1 = (Button) findViewById(R.id.text_selector1);
		text_selector2 = (Button) findViewById(R.id.text_selector2);
		icon_backcontrl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO
				//add by chen qiang
				if(tivicGlobal.mCurrentFuncID == FuncID.ID_SOCIAL_FRIENDS_DETAIL) {//点击按钮返回时设置为从List里去好友id为true
					TivicGlobal.getInstance().userRegisterBean.isNeedsUid = true;
				} else {
					TivicGlobal.getInstance().userRegisterBean.isNeedsUid = false;
				}
				
				if (tivicGlobal.mCurrentFuncID == FuncID.ID_EPG
						|| tivicGlobal.mCurrentFuncID == FuncID.ID_MYTV
						|| tivicGlobal.mCurrentFuncID == FuncID.ID_PUSHTV) {
					// TODO open control panel
				} else {
					if (parentActivity != null) {
						FragmentManager fm = parentActivity
								.getFragmentManager();
						int popstatckcount = fm.getBackStackEntryCount();
						if (popstatckcount > 0)
							fm.popBackStack();
						else{
							if(parentActivity instanceof SearchActivity){
								if(((SearchActivity) parentActivity).isFirstSearch){
									parentActivity.finish();
								}else{
									((SearchActivity) parentActivity).initEditerData();
									((SearchActivity) parentActivity).setListViewColor();
									((SearchActivity) parentActivity).setFirstSearch(true);
									((SearchActivity) parentActivity).setEditFrameLayout(true);
								}
							}else{
								parentActivity.finish();
							}
						}
					}
				}

				// Log.i(TAG, "kevin add: icon_backcontrl click!");
			}

		});

		icon_func.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO
				// Log.i(TAG, "kevin add: icon_func click!");
				showSelectorBar();
			}

		});

		icon_channel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO
				// Log.i(TAG, "kevin add: icon_channel click!");

			}

		});
		icon_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO
				// Log.i(TAG, "kevin add: icon_search click!");
				if (UIUtils.getCurrentFuncID() != FuncID.ID_UGC)
					jumpToSearch();
				else {
					// open publish dialog
					if (onNewUGCListner != null)
						onNewUGCListner.onUGCNewClick();

				}
			}

		});
		icon_help.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO
				// Log.i(TAG, "kevin add: icon_help click!");
				jumpToHelp();
			}

		});
		text_selector1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO
				// Log.i(TAG, "kevin add: text_selector1 click!");
				if (text_selector1.getText().equals(
						m_context.getString(R.string.base_mytv))) {
					hideSelectorBar();
					jumpToMyTV();
				} else if (text_selector1.getText().equals(
						m_context.getString(R.string.base_epg))) {
					hideSelectorBar();
					jumpToEPG();
				} else if (text_selector1.getText().equals(
						m_context.getString(R.string.base_pushtv))) {
					hideSelectorBar();
					jumpToPushTV();
				}
			}

		});

		text_selector2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO
				// Log.i(TAG, "kevin add: text_selector2 click!");
				if (text_selector2.getText().equals(
						m_context.getString(R.string.base_mytv))) {
					hideSelectorBar();
					jumpToMyTV();
				} else if (text_selector2.getText().equals(
						m_context.getString(R.string.base_epg))) {
					hideSelectorBar();
					jumpToEPG();
				} else if (text_selector2.getText().equals(
						m_context.getString(R.string.base_pushtv))) {
					hideSelectorBar();
					jumpToPushTV();
				}
			}

		});

	}

	private void jumpToMyTV() {
		if (!tivicGlobal.mIsLogin) {
			Toast.makeText(m_context, R.string.base_login_remind,
					Toast.LENGTH_LONG).show();
			try {
				Intent intent = new Intent();
				intent.setClassName("com.igrs.tivic.phone",
						"com.igrs.tivic.phone.Activity.LoginActivity");
				UIUtils.setCurrentFuncID(FuncID.ID_LOGIN);
				if (parentActivity != null)
					parentActivity.startActivity(intent);

				// Log.i(TAG, "kevin add: jiump to LoginActivity");
			} catch (Exception e) {
				Log.e(TAG, "startActivity error! " + e.toString());
			}
		} else {
			try {
				Intent intent = new Intent();
				intent.setClassName("com.igrs.tivic.phone",
						"com.igrs.tivic.phone.Activity.MyTVActivity");
				UIUtils.setCurrentFuncID(FuncID.ID_MYTV);
				if (parentActivity != null)
					parentActivity.startActivity(intent);
			} catch (Exception e) {
				Log.e(TAG, "startActivity error! " + e.toString());
			}
		}
	}

	private void jumpToEPG() {

		try {
			Intent intent = new Intent();
			intent.setClassName("com.igrs.tivic.phone",
					"com.igrs.tivic.phone.Activity.EPGActivity");
			UIUtils.setCurrentFuncID(FuncID.ID_EPG);
			if (parentActivity != null)
				parentActivity.startActivity(intent);
		} catch (Exception e) {
			Log.e(TAG, "startActivity error! " + e.toString());
		}

	}

	private void jumpToPushTV() {
		try {
			Intent intent = new Intent();
			intent.setClassName("com.igrs.tivic.phone",
					"com.igrs.tivic.phone.Activity.PushTVActivity");
			UIUtils.setCurrentFuncID(FuncID.ID_PUSHTV);
			if (parentActivity != null)
				parentActivity.startActivity(intent);
		} catch (Exception e) {
			Log.e(TAG, "startActivity error! " + e.toString());
		}

	}

	private void jumpToHelp() {

		try {
			Intent intent = new Intent();
			intent.setClassName("com.igrs.tivic.phone",
					"com.igrs.tivic.phone.Activity.HelpActivity");
			if (parentActivity != null)
				parentActivity.startActivity(intent);
//			Log.i(TAG, "kevin add: jiump to HelpActivity");
		} catch (Exception e) {
			Log.e(TAG, "startActivity error! " + e.toString());
		}

	}

	private void jumpToSearch() {

		try {
			Intent intent = new Intent();
			intent.setClassName("com.igrs.tivic.phone",
					"com.igrs.tivic.phone.Activity.SearchActivity");
			UIUtils.setCurrentFuncID(FuncID.ID_SEARCH);
			if (parentActivity != null)
				parentActivity.startActivity(intent);
			// Log.i(TAG, "kevin add: jiump to SearchActivity");
		} catch (Exception e) {
			Log.e(TAG, "startActivity error! " + e.toString());
		}

	}

	public void init() {

		switch (tivicGlobal.mCurrentFuncID) {
		case FuncID.ID_MYTV:
			text_func.setText(string.base_mytv);
			text_func.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO
					// Log.i(TAG, "kevin add: icon_func click!");
					showSelectorBar();
				}

			});
			icon_backcontrl.setVisibility(View.INVISIBLE);
			icon_search.setVisibility(View.VISIBLE);
			// icon_backcontrl.setImageResource(R.drawable.base_top_control);
			break;
		case FuncID.ID_EPG:
			text_func.setText(string.base_epg);
			text_func.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO
					// Log.i(TAG, "kevin add: icon_func click!");
					showSelectorBar();
				}

			});
			icon_backcontrl.setVisibility(View.INVISIBLE);
			icon_search.setVisibility(View.VISIBLE);

			// icon_backcontrl.setImageResource(R.drawable.base_top_control);
			break;
		case FuncID.ID_PUSHTV:
			text_func.setText(string.base_pushtv);
			text_func.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO
					// Log.i(TAG, "kevin add: icon_func click!");
					showSelectorBar();
				}

			});
			icon_backcontrl.setVisibility(View.INVISIBLE);
			icon_search.setVisibility(View.VISIBLE);
			// icon_backcontrl.setImageResource(R.drawable.base_top_control);
			break;
		case FuncID.ID_SOCIAL:
		case FuncID.ID_SOCIAL_INFO:
		case FuncID.ID_SOCIAL_PUBLISH:
		case FuncID.ID_SOCIAL_SAVE:
		case FuncID.ID_SOCIAL_FRIENDS:
		case FuncID.ID_SOCIAL_NOTIFY:
		case FuncID.ID_SOCIAL_MESSAGE:
		case FuncID.ID_SOCIAL_MKFRIEND:
			setSocialTitle();
			break;
		case FuncID.ID_SEARCH:
			setSearchTitle();
		default:
			setChannelInfo();
			break;
		}
	}

	public void hideSelectorBar() {
		if (selector.isShown()) {
			selector.setVisibility(View.GONE);
		}
	}

	public void showSelectorBar() {
		if (parentActivity != null) {
			if (parentActivity instanceof BaseActivity) {
				BaseActivity act = (BaseActivity) parentActivity;
				act.mainGroup.closeScrollMenuBar();
			}
		}
		if (!selector.isShown()) {
			icon_func.setImageResource(R.drawable.base_top_up);
			selector.setVisibility(View.VISIBLE);
			if (tivicGlobal.mCurrentFuncID == FuncID.ID_EPG) {
				text_selector1.setText(R.string.base_mytv);
				text_selector2.setText(R.string.base_pushtv);
			} else if (tivicGlobal.mCurrentFuncID == FuncID.ID_MYTV) {
				text_selector1.setText(R.string.base_epg);
				text_selector2.setText(R.string.base_pushtv);
			} else if (tivicGlobal.mCurrentFuncID == FuncID.ID_PUSHTV) {
				text_selector1.setText(R.string.base_epg);
				text_selector2.setText(R.string.base_mytv);
			} else {
				icon_func.setImageResource(R.drawable.base_top_down);
				selector.setVisibility(View.GONE);
			}
		} else {
			icon_func.setImageResource(R.drawable.base_top_down);
			selector.setVisibility(View.GONE);
		}
	}

	public void setParent(Activity activity) {
		this.parentActivity = activity;
	}
	
	public Activity getParentActivity() {
		return parentActivity;
	}

	public interface UGCNewClickListener {

		public void onUGCNewClick();

	}

	public void setChannelInfo() {
		if (TivicGlobal.getInstance().currentVisit != null) {
			String programename = "节目名称";
			if (TivicGlobal.getInstance().currentVisit.getVisit_ProgramTitle() != null
					&& !TivicGlobal.getInstance().currentVisit
							.getVisit_ProgramTitle().isEmpty())
				programename = TivicGlobal.getInstance().currentVisit
						.getVisit_ProgramTitle();

			String channelname = TivicGlobal.getInstance().currentVisit
					.getVisit_ChannelName();
			int channelimg_id = Utils.setChannelIcon(m_context, channelname);

			icon_channel.setVisibility(View.VISIBLE);
			if (channelimg_id != 0) {
				icon_channel.setImageResource(channelimg_id);
			} else {
				icon_channel.setVisibility(View.INVISIBLE);
			}
			text_func.setText(programename);

		} else {
			icon_channel.setVisibility(View.INVISIBLE);
		}
		icon_func.setVisibility(View.INVISIBLE);
		text_func.setVisibility(View.VISIBLE);
		icon_backcontrl.setImageResource(R.drawable.base_top_return);
		if (UIUtils.getCurrentFuncID() != FuncID.ID_UGC)
			icon_search.setVisibility(View.INVISIBLE);

	}

	public void setUGCPublishTitle() {
		icon_channel.setVisibility(View.INVISIBLE);
		text_func.setText(m_context.getString(R.string.base_tieba));
		icon_backcontrl.setImageResource(R.drawable.base_top_return);
		icon_func.setVisibility(View.INVISIBLE);
		icon_search.setVisibility(View.INVISIBLE);

	}

	public void setSocialTitle() {
		icon_channel.setVisibility(View.INVISIBLE);
		text_func.setText(m_context.getString(R.string.base_social));
		icon_backcontrl.setImageResource(R.drawable.base_top_return);
		icon_func.setVisibility(View.INVISIBLE);
		icon_search.setVisibility(View.INVISIBLE);

	}

	public void setSearchTitle() {
		icon_channel.setVisibility(View.INVISIBLE);
		text_func.setText(m_context.getString(R.string.base_search));
		icon_backcontrl.setImageResource(R.drawable.base_top_return);
		icon_func.setVisibility(View.INVISIBLE);
		icon_search.setVisibility(View.INVISIBLE);
		icon_help.setVisibility(View.INVISIBLE);
	}
}
