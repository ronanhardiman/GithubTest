package com.igrs.tivic.phone.Impl;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Handler;
import android.os.Message;

import com.igrs.tivic.phone.Activity.SearchActivity;
import com.igrs.tivic.phone.Bean.Base.BaseParamBean;
import com.igrs.tivic.phone.Bean.EPG.EPGProgramsBean;
import com.igrs.tivic.phone.Bean.UGC.SearchProgramBean;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Global.URLConfig;
import com.igrs.tivic.phone.Parser.SearchParser;
import com.igrs.tivic.phone.Utils.HttpClientUtils;
import com.igrs.tivic.phone.Utils.JsonForRequest;
import com.igrs.tivic.phone.Utils.UIUtils;
/**
 * 搜索
 * @author admin
 *
 */
public class SearchImpl {
	private static SearchImpl instance = null;
	private HttpClientUtils httpClientUtils;
	private Handler recommendRearchHandler;
	private SearchImpl(){
		httpClientUtils = HttpClientUtils.getInstance();
	}
	public static SearchImpl getInstance(){
		if(instance == null){
			instance = new SearchImpl();
		}
		return instance;
	}
	
	/**
	 * 节目搜索接口
	search/program
	 */
	public void searchPrograms(final /*BaseParamBean bBean*/String search_data){
		new Thread(new Runnable() {
			@Override
			public void run() {
				HashMap<String, String> searchMap = new HashMap<String, String>();
				BaseParamBean bBean = new BaseParamBean();
				bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserUID());
				bBean.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
				bBean.setVer(1);
				
				SearchProgramBean sBean = new SearchProgramBean();
//				sBean.setSearchProgramsName("神探");
				sBean.setSearchProgramsName(search_data);
//				sBean.setStart_day("2012-12-17");
//				sBean.setEnd_day("2012-12-27");
				
				searchMap.put("json", JsonForRequest.getSearchProgramsList(bBean,sBean));
				String search_json = httpClientUtils.requestPostHttpClient(URLConfig.get_search_program, Const.DEFAULT_ENCODING, searchMap);
				ArrayList<EPGProgramsBean> eBeanList = SearchParser.getSearchPrograms(search_json);
				UIUtils.Logd("lq", "============search_json : "+search_json);
				/*ArrayList<EPGProgramsBean> eBeanList = new ArrayList<EPGProgramsBean>();
				for (int i = 0; i < 3; i++) {
					EPGProgramsBean eBean = new EPGProgramsBean();
					eBean.setProgram_name("program_name: "+i+search_data);
					eBean.setChannel_name("channel_name"+i);
					eBeanList.add(eBean);
				}*/
				Message msg = recommendRearchHandler.obtainMessage();
				msg.what = SearchActivity.PERSONAL_RECOMMEND;
				msg.obj = eBeanList;
				recommendRearchHandler.sendMessage(msg);
			}
		}).start();
	}
	/**
	 * 节目热度表_用户访问最多的节目
	search/pghot_user_visit
	描述：返回访问量前5的节目，按访问量降序排列。
	 */
	public void searchHotProgram(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				HashMap<String, String> searchMap = new HashMap<String, String>();
				BaseParamBean bBean = new BaseParamBean();
				bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserUID());
				bBean.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
				bBean.setVer(1);
				
				searchMap.put("json", JsonForRequest.getSearchHotProgramsList(bBean));
				String search_json = httpClientUtils.requestPostHttpClient(URLConfig.get_search_hot_program, Const.DEFAULT_ENCODING, searchMap);
				ArrayList<EPGProgramsBean> eBeanList = SearchParser.getHotProgramsList(search_json);
				
				/*ArrayList<EPGProgramsBean> eBeanList = new ArrayList<EPGProgramsBean>();
				for (int i = 0; i < 3; i++) {
					EPGProgramsBean eBean = new EPGProgramsBean();
					eBean.setProgram_name("program_name"+i);
					eBeanList.add(eBean);
				}*/
				
				Message msg = recommendRearchHandler.obtainMessage();
				msg.what = SearchActivity.EDITOR_RECOMMEND;
				msg.obj = eBeanList;
				recommendRearchHandler.sendMessage(msg);
			}
		}).start();
	}
	
	public ArrayList<EPGProgramsBean> searchHotProgram2(){
		HashMap<String, String> searchMap = new HashMap<String, String>();
		BaseParamBean bBean = new BaseParamBean();
		bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserUID());
		bBean.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
		bBean.setVer(1);
		
		searchMap.put("json", JsonForRequest.getSearchHotProgramsList(bBean));
		String search_json = httpClientUtils.requestPostHttpClient(URLConfig.get_search_hot_program, Const.DEFAULT_ENCODING, searchMap);
		return SearchParser.getHotProgramsList(search_json);
	}
	
	public void setRecommendSearchHandler(Handler recommendSearchHandler){
		this.recommendRearchHandler = recommendSearchHandler;
	}
}
