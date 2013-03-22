
package com.igrs.tivic.phone.Interface;

import java.util.ArrayList;
import java.util.List;

import android.util.SparseArray;
import android.util.SparseIntArray;

import com.igrs.tivic.phone.Bean.MarqueeListBean;
import com.igrs.tivic.phone.Bean.EPG.EPGChannelBean;
import com.igrs.tivic.phone.Bean.EPG.EPGChannelListBean;
import com.igrs.tivic.phone.Bean.EPG.EPGDailyChannelInfo;
import com.igrs.tivic.phone.Bean.EPG.EPGProgramBean;




public interface IEPG {

	public List<EPGChannelBean> getEPGChannelList(String currentDate);
	public ArrayList<EPGChannelBean> getEPGChannelList1(String currentDate);
	public void getEPGChannelList2(String currentDate);
	public void getEPGChannelList3(String currentDate);
	
	public EPGChannelListBean getChannelList();
	/**
	 * get program list by param as follow
	 * @param channel_id
	 * @param loadProgramDate 加载program的日期
	 * @return
	 */
	public EPGDailyChannelInfo getProgramList(String channel_id ,String loadProgramDate);
	/**
	 * get collection list
	 * @return
	 */
	public SparseArray<SparseIntArray> getChannelProgramFocusList();
	/**
	 * attention program
	 * @param program_id
	 * @param c_id
	 */
	public void focusProgram(String program_id,String channel_id);
	
	/**
	 * cancel attention program
	 * @param program_id
	 * @param channel_id
	 */
	public void removeFocus(String program_id, String channel_id);
	
	/**
	 * get marquee
	 * @return
	 */
	public MarqueeListBean getMarquee();
	
	public ArrayList<EPGProgramBean> getEPGProgramList1(String channel_id);
	public void getEPGProgramList2(String channel_id);
	public void getEPGProgramList3(String channel_id);
	
	
	
}
