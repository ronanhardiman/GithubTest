package com.igrs.tivic.phone.Listener;

import java.util.List;

import com.igrs.tivic.phone.Bean.EPG.EPGChannelBean;
import com.igrs.tivic.phone.Bean.EPG.EPGChannelListBean;
import com.igrs.tivic.phone.Bean.EPG.EPGDailyChannelInfo;
import com.igrs.tivic.phone.Bean.EPG.EPGProgramBean;

public interface EPGUpdateUIListener {

	public void onChannelListGet(EPGChannelListBean result);
	
	public void onProgramListGet(EPGDailyChannelInfo result, int position);

}
