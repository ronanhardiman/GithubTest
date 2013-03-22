package com.igrs.tivic.phone.Listener;

import java.util.List;

import com.igrs.tivic.phone.Bean.Login.UserPOIItemBean;


public interface SocialRecommendListener {
	public void notifySocialRecommendUI(List<UserPOIItemBean> recommendList);
}
