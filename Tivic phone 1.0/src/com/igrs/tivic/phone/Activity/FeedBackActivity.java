package com.igrs.tivic.phone.Activity;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class FeedBackActivity extends Activity {
	private ImageButton ib_close;
	private EditText et_content;
	private Button bt_submit;
	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feed_back);
		mContext = this;
		init();
		setListeners();
	}
	private void init() {
		ib_close = (ImageButton)findViewById(R.id.feedback_close_button);
    	et_content = (EditText)findViewById(R.id.feedback_content);
    	bt_submit = (Button)findViewById(R.id.feedback_publish);
        et_content.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart ;
            private int selectionEnd ;
            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                    int arg3) {
                temp = s;
            }
            
            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2,
                    int arg3) {
            }
            
            @Override
            public void afterTextChanged(Editable s) {
                 selectionStart = et_content.getSelectionStart();
                selectionEnd = et_content.getSelectionEnd();
                if (temp.length() > 100) {
                    Toast.makeText(FeedBackActivity.this,
                            R.string.input_word_many, Toast.LENGTH_SHORT)
                            .show();
                    s.delete(selectionStart-1, selectionEnd);
                    int tempSelection = selectionStart;
                    et_content.setText(s);
                    et_content.setSelection(tempSelection);
                }
            }


        });
		
	}
	private void setListeners() {
		ib_close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		bt_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String content = et_content.getText().toString();
				if(TextUtils.isEmpty(content)) {
					Toast.makeText(mContext, R.string.setting_feedback_error, Toast.LENGTH_SHORT).show();
					return;
				} 
				if(!Utils.isConnected(mContext)) {
					UIUtils.ToastMessage(mContext, R.string.net_error);
					return;
				}
				Toast.makeText(mContext, R.string.setting_feedback_success, Toast.LENGTH_SHORT).show();
				finish();
			}
		});
	}
	
}
