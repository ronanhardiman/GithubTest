package com.igrs.tivic.phone.ViewGroup;

import java.util.HashMap;
import java.util.Iterator;

import android.view.View;


public class SocialUsrInfoManager {
	public static final int INFO = 0;
	public static final int PHOTO = 1;
	private HashMap<Integer, SocialUsrInfoDetailBase> mHashMap;
	
	private SocialUsrInfoContainer mContainer;

	public SocialUsrInfoManager() {
		mHashMap = new HashMap<Integer, SocialUsrInfoDetailBase>();
	}
	
	public void setmContainer(SocialUsrInfoContainer container){
		this.mContainer = container;
	}
	
	public void showDetailWindow(int num,SocialUsrInfoDetailBase mUsrInfoDetailBase){
		/*if(!mHashMap.containsKey(num)){
			mHashMap.put(num, mUsrInfoDetailBase);
			if(!(mUsrInfoDetailBase instanceof SocialUsrInfoDetail)){
					mContainer.addView(mUsrInfoDetailBase);
			}
		}
		for (Iterator iter = mHashMap.keySet().iterator(); iter.hasNext();) {
			Object key = iter.next();
			SocialUsrInfoDetailBase sdb = mHashMap.get(key);
			sdb.setVisibility(View.INVISIBLE);
		}
		mUsrInfoDetailBase.setVisibility(View.VISIBLE);*/
	}
}
