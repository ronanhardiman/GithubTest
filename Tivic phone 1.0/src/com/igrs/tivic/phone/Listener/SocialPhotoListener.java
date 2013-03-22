package com.igrs.tivic.phone.Listener;

import java.util.List;

import com.igrs.tivic.phone.Bean.Base.PhotoListBean;

public interface SocialPhotoListener {
	public void notifySocialPhotoUI(PhotoListBean myPhotos);
	
	public void notifyDeletePhotoUI();
}
