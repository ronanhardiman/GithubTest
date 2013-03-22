package com.igrs.tivic.phone.Interface;

import com.igrs.tivic.phone.Bean.Base.BaseParamBean;
import com.igrs.tivic.phone.Bean.UGC.UGCPublishBean;

public interface IUGC {

	public boolean getUGCList(BaseParamBean param);
	
	public boolean replyPublish(BaseParamBean param, UGCPublishBean bean);
	
	public boolean supportPublish(BaseParamBean param);
	
	public boolean newPublish(BaseParamBean param, UGCPublishBean bean);
	
	public boolean getReplyList(BaseParamBean param);
	
	public boolean getMyUGCList(BaseParamBean param);
}
