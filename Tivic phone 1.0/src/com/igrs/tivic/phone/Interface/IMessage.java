package com.igrs.tivic.phone.Interface;

import com.igrs.tivic.phone.Bean.Base.BaseParamBean;
import com.igrs.tivic.phone.Bean.Social.SocialMessageBean;

public interface IMessage {

	public boolean getMessageList(BaseParamBean param);
	
	public boolean getMsgHistoryList(BaseParamBean param);
	
	public boolean sendMessage(BaseParamBean param, SocialMessageBean bean);
	
}
