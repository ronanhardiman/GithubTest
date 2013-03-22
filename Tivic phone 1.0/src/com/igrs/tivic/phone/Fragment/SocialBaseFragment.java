package com.igrs.tivic.phone.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Activity.SocialBaseActivity;
import com.igrs.tivic.phone.Activity.SocialPhotoBrowseActivity;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Utils.GDUtils;
import com.igrs.tivic.phone.Utils.UIUtils;

	public class SocialBaseFragment extends Fragment {
		Context mContext;
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			mContext = this.getActivity().getApplicationContext();
		}
	
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onActivityCreated(savedInstanceState);

		}
		
		@Override
		public void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
		}
		
		
		@Override
		public void onStop() {
			// TODO Auto-generated method stub
			super.onStop();
		}
		
		@Override
		public void onAttach(Activity activity) {
			// TODO Auto-generated method stub
			super.onAttach(activity);
		}
		
		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
		}
		
		@Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();

		}
		
		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			GDUtils.getGDApplication(mContext).destory();
			System.gc();
		}
	
		@Override
		public void onLowMemory() {
			// TODO Auto-generated method stub
			super.onLowMemory();
			GDUtils.getGDApplication(mContext).onLowMemory();
			System.gc();
		}

		protected boolean checkSDCard() {
			// TODO Auto-generated method stub
			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
				// Log.v("TestFile",
				// "SD card is not avaiable/writeable right now.");
				Toast.makeText(mContext, mContext.getString(R.string.sd_invalid),Toast.LENGTH_SHORT).show();
				return false;
			}
			return true;
		}
	}