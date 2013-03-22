package com.igrs.tivic.phone.Listener;

import com.igrs.tivic.phone.Bean.UGC.UGCDataBean;

public interface UGCItemClickListener {

	public void onReplyClicked(UGCDataBean bean, int position);
	public void onUpClicked(UGCDataBean bean, int position);
}
