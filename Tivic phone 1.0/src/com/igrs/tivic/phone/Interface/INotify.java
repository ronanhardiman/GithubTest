package com.igrs.tivic.phone.Interface;

import com.igrs.tivic.phone.Bean.Base.BaseParamBean;
import com.igrs.tivic.phone.Bean.Social.SocialNewsBean;

public interface INotify {

	public boolean getNotifyList(BaseParamBean param);
	
	//该接口需要后台Service在线程中调用
	public SocialNewsBean getCountNew(BaseParamBean param);
}
