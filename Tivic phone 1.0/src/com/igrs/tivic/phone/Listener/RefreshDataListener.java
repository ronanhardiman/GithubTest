package com.igrs.tivic.phone.Listener;

import com.igrs.tivic.phone.Bean.Social.SocialNewsBean;

	 
	public interface RefreshDataListener {
		  
	 
	    public void onDataChanged(SocialNewsBean countBean);

	    public void onNetworkDisconnect();
}
