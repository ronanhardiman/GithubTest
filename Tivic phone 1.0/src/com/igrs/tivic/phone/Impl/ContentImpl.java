package com.igrs.tivic.phone.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.client.HttpClient;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.test.UiThreadTest;
import android.util.Log;
import android.util.SparseIntArray;

import com.igrs.tivic.phone.Bean.BaseResponseBean;
import com.igrs.tivic.phone.Bean.ScreenShotsBean;
import com.igrs.tivic.phone.Bean.VisitBean;
import com.igrs.tivic.phone.Bean.Base.BaseParamBean;
import com.igrs.tivic.phone.Bean.Base.PhotoListBean;
import com.igrs.tivic.phone.Bean.Base.UserBean;
import com.igrs.tivic.phone.Bean.EPG.EPGChannelListBean;
import com.igrs.tivic.phone.Bean.EPG.EPGDailyChannelInfo;
import com.igrs.tivic.phone.Bean.PGC.CollectBean;
import com.igrs.tivic.phone.Bean.PGC.CollectListBean;
import com.igrs.tivic.phone.Bean.PGC.ContentPublishListBean;
import com.igrs.tivic.phone.Bean.PGC.ContentPublishReplyBean;
import com.igrs.tivic.phone.Bean.PGC.ContentTypesBean;
import com.igrs.tivic.phone.Bean.PGC.NewsBean;
import com.igrs.tivic.phone.Bean.PGC.PushTVBean;
import com.igrs.tivic.phone.Bean.PGC.TVFindBean;
import com.igrs.tivic.phone.Bean.PGC.VideoBean;
import com.igrs.tivic.phone.Bean.UGC.UGCPublishBean;
import com.igrs.tivic.phone.Bean.UGC.UGCReplyListBean;
import com.igrs.tivic.phone.Bean.Waterfall.SendedDatasBean;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Global.URLConfig;
import com.igrs.tivic.phone.Interface.IContent;
import com.igrs.tivic.phone.Parser.ContentDetailParser;
import com.igrs.tivic.phone.Parser.EPGDataParser;
import com.igrs.tivic.phone.Parser.NotifyMessageParser;
import com.igrs.tivic.phone.Parser.UGCDataParser;
import com.igrs.tivic.phone.Parser.UsersDetailsParser;
import com.igrs.tivic.phone.Parser.WaterFallParser;
import com.igrs.tivic.phone.Utils.HttpClientUtils;
import com.igrs.tivic.phone.Utils.JsonForRequest;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;

public class ContentImpl implements IContent{
	private static ContentImpl instance = null;
	private HttpClientUtils httpClientUtils;
	private final static String charset = "utf-8";
	public final static int MSG_NEWS = 100;			//news
	public final static int MSG_TVFIND = 101;		//tvFind
	public final static int MSG_PUSHTV = 102;		//pushTv
	public final static int MSG_VIDEO = 103;		//video
	public final static int MSG_CONTENTS = 104;		//news + tvFind + pushTv
	public final static int MSG_WATERFALL = 105;	//waterfall
	public final static int MSG_COLLECTON = 106;	//收藏文章
	public final static int MSG_REMOVE_COLLECTON = MSG_COLLECTON +1;	//取消收藏
	public final static int MSG_IS_COLLECTON = MSG_REMOVE_COLLECTON +1;	//取消收藏
	public final static int MSG_COLLECTON_LIST = MSG_IS_COLLECTON +1;	//获取收藏列表
	public final static int MSG_REPLY_PUBLISH = MSG_COLLECTON_LIST + 1;
	private Handler contentUpdateHander;
	private Handler collectionHandler;
	private Handler collection_list_handler;	//收藏列表
	private final String TAG = "lq";
	
	private ContentImpl(){
		httpClientUtils = HttpClientUtils.getInstance();
	}
	public static ContentImpl getInstance(){
		if(instance == null){
			instance = new ContentImpl();
		}
		return instance;
	}
	@Override
	public NewsBean getNewsBean(String url) {
		String json = httpClientUtils.requestGet(url, charset);
		return ContentDetailParser.getContentNews(json);
	}

	@Override
	public PushTVBean getPushTVBean(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TVFindBean getTVFindBean(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VideoBean getVideoBean(String url) {
		// TODO Auto-generated method stub
		return null;
	}
	public void setContentUpdateHandler(Handler handler){
		this.contentUpdateHander = handler;
	}
	public void setCollectionHandler(Handler collectionHandler){
		this.collectionHandler = collectionHandler;
	}
	public void setCollection_list_handler(Handler collection_list_handler){
		this.collection_list_handler = collection_list_handler;
	}
	//获取news 数据
	@Override
	public void getNews(final String url,final int index) {
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				String json = httpClientUtils.requestGet(url, charset);
				Message msg = contentUpdateHander.obtainMessage();
				msg.arg1 = MSG_NEWS;
				msg.arg2 = index;
				msg.obj = ContentDetailParser.getContentNews(json);
				contentUpdateHander.sendMessage(msg);
			}
		}).start();
	}
	
	//获取tvfind 数据
	@Override
	public void getTvFind(final String url,final int index) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				String json = httpClientUtils.requestGet(url, charset);
				Message msg = contentUpdateHander.obtainMessage();
				msg.arg1 = MSG_TVFIND;
				msg.arg2 = index;
				msg.obj = ContentDetailParser.getContentTVfind(json);
				contentUpdateHander.sendMessage(msg);
			}
		}).start();
		
	}
	//获取 Video
	@Override
	public void getVideo(final String url,final int index) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				String json = httpClientUtils.requestGet(url, charset);
				Message msg = contentUpdateHander.obtainMessage();
				msg.arg1 = MSG_VIDEO;
				msg.arg2 = index;
				msg.obj = ContentDetailParser.getContentVide(json);
				contentUpdateHander.sendMessage(msg);
			}
		}).start();
		
	}
	//获取所有三级页面内容
	public void getAllContents(final ArrayList<SendedDatasBean> sendDataList,final ArrayList<ContentTypesBean> contentTypeBeanList){
				new Thread(new Runnable() {
					@Override
					public void run() {
						for (int i = 0; i < sendDataList.size(); i++) {
							SendedDatasBean sBean = sendDataList.get(i);
							for (int j = 0; j < sBean.articleUrlList.size(); j++) {
								String url = sBean.articleUrlList.get(j);
								String json = httpClientUtils.requestGet(url, charset);
								if("tvfind".equals(sBean.type)){
									ContentTypesBean cBean = new ContentTypesBean();
									cBean.setObject(ContentDetailParser.getContentTVfind(json));
									cBean.setType("tvfind");
									contentTypeBeanList.add(cBean);
								}else if("news".equals(sBean.type)){
									ContentTypesBean cBean = new ContentTypesBean();
									cBean.setObject(ContentDetailParser.getContentNews(json));
									cBean.setType("news");
									contentTypeBeanList.add(cBean);
								}else if("video".equals(sBean.type)){
									ContentTypesBean cBean = new ContentTypesBean();
									cBean.setObject(ContentDetailParser.getContentVide(json));
									cBean.setType("video");
									contentTypeBeanList.add(cBean);
								}
							}
						}
						Message msg = contentUpdateHander.obtainMessage();
						msg.arg1 = MSG_CONTENTS;
						msg.obj = contentTypeBeanList;
					    contentUpdateHander.sendMessage(msg);
					}
				}).start();
	}
	
	//get single content data
	public void getContent(final SendedDatasBean sBean,final int position,final Handler handler){
		new Thread(new Runnable() {
			@Override
			public void run() {
				ContentTypesBean cBean = new ContentTypesBean();
				for (int j = 0; j < sBean.articleUrlList.size(); j++) {
					String url = sBean.articleUrlList.get(j);
					String json = httpClientUtils.requestGet(url, charset);
					if("tvfind".equals(sBean.type)){
//						ContentTypesBean cBean = new ContentTypesBean();
						cBean.setObject(ContentDetailParser.getContentTVfind(json));
						cBean.setType("tvfind");
					}else if("news".equals(sBean.type)){
//						ContentTypesBean cBean = new ContentTypesBean();
						cBean.setObject(ContentDetailParser.getContentNews(json));
						cBean.setType("news");
					}else if("video".equals(sBean.type)){
//						ContentTypesBean cBean = new ContentTypesBean();
						cBean.setObject(ContentDetailParser.getContentVide(json));
						cBean.setType("video");
					}
				}
				handler.obtainMessage(1, MSG_COLLECTON, position, cBean).sendToTarget();
			}
		}).start();
	}
	
	//获取单个三级页面内容 
		public void getAllContents(final SendedDatasBean sBean,final ArrayList<ContentTypesBean> contentTypeBeanList){
					new Thread(new Runnable() {
						@Override
						public void run() {
								for (int j = 0; j < sBean.articleUrlList.size(); j++) {
									String url = sBean.articleUrlList.get(j);
									String json = httpClientUtils.requestGet(url, charset);
									if("tvfind".equals(sBean.type)){
										ContentTypesBean cBean = new ContentTypesBean();
										cBean.setObject(ContentDetailParser.getContentTVfind(json));
										cBean.setType("tvfind");
										contentTypeBeanList.add(cBean);
									}else if("news".equals(sBean.type)){
										ContentTypesBean cBean = new ContentTypesBean();
										cBean.setObject(ContentDetailParser.getContentNews(json));
										cBean.setType("news");
										contentTypeBeanList.add(cBean);
									}else if("video".equals(sBean.type)){
										ContentTypesBean cBean = new ContentTypesBean();
										cBean.setObject(ContentDetailParser.getContentVide(json));
										cBean.setType("video");
										contentTypeBeanList.add(cBean);
									}
								}
							Message msg = contentUpdateHander.obtainMessage();
							msg.arg1 = MSG_CONTENTS;
							msg.obj = contentTypeBeanList;
						    contentUpdateHander.sendMessage(msg);
						}
					}).start();
		}
	//获取PushTV
	@Override
	public void getPushTv(final String url) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String json = httpClientUtils.requestGet(url, charset);//测试requestGet 方法 异常
				//Log.e("aaa","=========="+json);
				Message msg = contentUpdateHander.obtainMessage();
				msg.arg1 = MSG_PUSHTV;
				msg.obj = ContentDetailParser.getContentPushTV(json);
				contentUpdateHander.sendMessage(msg);
			}
		}).start();
		
	}
	//获取瀑布流
	@Override
	public void getWaterFall(final String url) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String json = httpClientUtils.requestGet(url, charset);
				Message msg = contentUpdateHander.obtainMessage();
				msg.arg1 = MSG_WATERFALL;
				msg.obj = WaterFallParser.parserProgramDetail2(json);
				contentUpdateHander.sendMessage(msg);
			}
		}).start();
	}
	/**
	 * 上传图片
	 * key : json 和file 目前是固定的.
	 * 目前只能一次上传一张图片.
	 */
	public void upLoadPhoto(String url,String filePath){
		
		String photo_path = "";//图片path
		
		BaseParamBean bBean = new BaseParamBean();
		bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserUID());
		bBean.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
		bBean.setVer(1);
		
		HashMap<String, String> map = new HashMap<String, String>();
		String jsonToSting = JsonForRequest.UploadPhoto(bBean);
		map.put("json", jsonToSting);
		map.put("file", filePath);
		String json_upload_photo = httpClientUtils.requestPostHttpClient(URLConfig.get_photo_upload, charset, map);
	}
	/**
	 * 6、删除用户图片
user/del_photo
	 * @param urlList
	 */
	public void deletePhoto(final ArrayList<String> urlList){
		new Thread(new Runnable() {
			@Override
			public void run() {
				BaseParamBean bBean = new BaseParamBean();
				bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserUID());
				bBean.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
				bBean.setVer(1);
				
				HashMap<String, String> map = new HashMap<String, String>();
				String jsonToSting = JsonForRequest.deletePhotos(bBean, urlList);
				map.put("json", jsonToSting);
				String json_upload_photo = httpClientUtils.requestPostHttpClient(URLConfig.get_photos_delete, charset, map);
				BaseResponseBean brBean = NotifyMessageParser.getBaseResponse(json_upload_photo);
				
			}
		}).start();
	}
	
	/**
	 * 获取照片列表  
	 */
	public void getPhotosList(/*final String url*/){
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				BaseParamBean bBean = new BaseParamBean();
				bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserUID());
				bBean.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
				bBean.setVer(1);
				int page_no = 0 ;//第几页
				int per_page = 20 ;//每次获取多少条
				String json = httpClientUtils.requestPostHttpClient4(URLConfig.get_photo_list, "utf-8", JsonForRequest.getPhotoList(bBean, page_no, per_page));
				PhotoListBean pBean = UsersDetailsParser.getUserPhotoList(json);
			}
		}).start();
		
	}
	/**
	 * 
	 * @param imagePath 头像绝对路径
	 */
	/*public void modifyUserAvatar(String imagePath){
		BaseParamBean bBean = new BaseParamBean();
		bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserUID());
		bBean.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
		bBean.setVer(1);
		
		HashMap<String, String> map = new HashMap<String, String>();
		String jsonToSting = JsonForRequest.modifyUserAvatar(bBean);
		map.put("json", jsonToSting);
		map.put("file", imagePath);
		
		String json_modify_avatar = httpClientUtils.requestPostHttpClient(URLConfig.get_modify_info, charset, map);
	}*/
	/**
	 * 收藏文章
	 * @param article_id
	 */
	public void addCollection(final String article_id){
		new Thread(new Runnable() {
			@Override
			public void run() {
				BaseParamBean bBean = new BaseParamBean();
				bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserUID());
				bBean.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
				bBean.setVer(1);
				bBean.setContentId(article_id);//cms 中的文章 id 没有与 sns 的同步.这里 用测试数据 (1,3,5,7,9)
				
				HashMap<String, String> map = new HashMap<String, String>();
				String jsonToSting = JsonForRequest.add_removeCollection(bBean);
				map.put("json", jsonToSting);
				String json = httpClientUtils.requestPostHttpClient(URLConfig.get_add_collection, charset, map);
				BaseResponseBean brBean = NotifyMessageParser.getBaseResponse(json);
				CollectBean cBean = UsersDetailsParser.AddCollection(json);
				Message msg = collectionHandler.obtainMessage();
				msg.arg1 = MSG_COLLECTON;
				if(brBean != null){
					msg.arg2 = brBean.getRet();
				}else{
					msg.arg2 = -1;
				}
				
				msg.obj = article_id;
				collectionHandler.sendMessage(msg);
				UIUtils.Logd("lq", "addConection ===json : "+json + " article_id"+article_id);
			}
		}).start();
	}
	/**
	 * 取消收藏文章
	 * @param article_id
	 */
	public void removeCollection(final String article_id){
		new Thread(new Runnable() {
			@Override
			public void run() {
				BaseParamBean bBean = new BaseParamBean();
				bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserUID());
				bBean.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
				bBean.setVer(1);
				bBean.setContentId(article_id);//cms 中的文章 id 没有与 sns 的同步.这里 用测试数据 (1,3,5,7,9)
				
				HashMap<String, String> map = new HashMap<String, String>();
				String jsonToSting = JsonForRequest.add_removeCollection(bBean);
				map.put("json", jsonToSting);
				String json = httpClientUtils.requestPostHttpClient(URLConfig.get_remove_collection, charset, map);
				BaseResponseBean brBean = NotifyMessageParser.getBaseResponse(json);
				Message msg = collectionHandler.obtainMessage();
				msg.arg1 = MSG_REMOVE_COLLECTON;
				if(brBean != null){
					msg.arg2 = brBean.getRet();
				}else{
					msg.arg2 = -2;
				}
				msg.obj = article_id;
				collectionHandler.sendMessage(msg);
				UIUtils.Logd("lq", "remove  json : "+json +"article_id : "+article_id);
			}
		}).start();
	}
	/**
	 * 是否收藏
	 * @param article_id
	 */
	public void isCollection(final int[] article_id){
		new Thread(new Runnable() {
			@Override
			public void run() {
				BaseParamBean bBean = new BaseParamBean();
				bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserUID());
				bBean.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
				bBean.setVer(1);
//				bBean.setContentId(article_id);//cms 中的文章 id 没有与 sns 的同步.这里 用测试数据 (1,3,5,7,9)
				
				HashMap<String, String> map = new HashMap<String, String>();
				String jsonToSting = JsonForRequest.isCollection(bBean, article_id);
				map.put("json", jsonToSting);
				String json = httpClientUtils.requestPostHttpClient(URLConfig.get_is_collection, charset, map);
				BaseResponseBean brBean = NotifyMessageParser.getBaseResponse(json);
				
				Message msg = collectionHandler.obtainMessage();
				msg.arg1 = MSG_IS_COLLECTON;
				msg.arg2 = brBean.getRet();
				collectionHandler.sendMessage(msg);
			}
		}).start();
	}
	
	//获取频道列表
	public void getChannelList(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				HashMap<String, String> map = new HashMap<String, String>();
				String jsonToSting = JsonForRequest.getChannelList();
				map.put("json", jsonToSting);
				String json = httpClientUtils.requestPostHttpClient(URLConfig.get_epgChannelList, charset, map);
				EPGChannelListBean epgListBean = EPGDataParser.getChannelList(json);
			}
		}).start();
	}
	/**
	 * 获取EPG信息, 公共接口，无需登录
pgc/epg_list
描述：获取某一天，某一频道的EPG信息，按start_display时间升序排列
	 */
	public void getDayEPGList(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				BaseParamBean bBean = new BaseParamBean();
				bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserUID());
				bBean.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
				bBean.setVer(1);
				
				String day = "2012-12-27";
				String day2 = "2012-12-22";
				String day3 = "2012-12-19";
				String channel_id = "1";
				String _day =String.valueOf(Utils.unixTimestamp2String(day));
				HashMap<String, String> map = new HashMap<String, String>();
				String jsonToSting = JsonForRequest.getEPGList(bBean, _day, channel_id);
				map.put("json", jsonToSting);
				String json = httpClientUtils.requestPostHttpClient(URLConfig.get_epg_list, charset, map);
				EPGDailyChannelInfo epgDailyChannelInfo = EPGDataParser.getEPGDailyInfo(json);
			}
		}).start();
	}
	
	public ContentPublishListBean getReplyList(BaseParamBean param) {
		if (param == null)
			return null;
		ContentPublishListBean replyList = null;
		final String jsonrequest = JsonForRequest.getReplyList(param);
		//Log.i("kevin add:", "jsonrequest 50 = " + jsonrequest);

		String jsonret = httpClientUtils.requestPostHttpClient4(
				URLConfig.Content_getReplyListUrl, "utf-8", jsonrequest);
		if (jsonret.equals(String.valueOf(Const.SOCKET_TIMEOUT_EXCEPTION))) {
			// 服务器请求失败
			replyList = null;
		} else {
			replyList = ContentDetailParser.getContentReplyList(jsonret);
		}
//		Log.i("kevin add:", "jsonrequest 51 = " + jsonret + "replyList total = " + replyList.getTotal());
//		UIUtils.Logd("lq", "ContentPublishListBean :  = " + jsonret);

		return replyList;
	}
	
	public boolean replyPublish(BaseParamBean param, ContentPublishReplyBean bean) {
		if (param == null)
			return false;

		final BaseParamBean paramtest = param;
		final ContentPublishReplyBean beantest = bean;
		new Thread() {
			public void run() {
				// step1
				// step2
				final String jsonrequest = JsonForRequest.replyArticle(
						paramtest, beantest);
				Log.i("kevin add:", "jsonrequest replayArticle = " + jsonrequest);
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("json", jsonrequest);
				if (beantest.getType() == 0)
					map.put("file", beantest.getPhotoUrl());
				String jsonret = httpClientUtils.requestPostHttpClient(
						URLConfig.Content_replyUrl, "utf-8", map);
				Log.i("kevin add:", "jsonret 52 = " + jsonret);
//				BaseResponseBean retbean = NotifyMessageParser
//						.getBaseResponse(jsonret);
				// if(retbean.getRet() != 0)
				// {
				// Log.i("kevin add:", "jsonrequest 53 = " + retbean.getMsg());
				// }
				int ret = ContentDetailParser.getContentReply(jsonret);
				// step3
				// TODO

				Message msg = contentUpdateHander.obtainMessage();
				msg.arg1 = MSG_REPLY_PUBLISH;
				msg.arg2 = ret;
				if (jsonret.equals(String
						.valueOf(Const.SOCKET_TIMEOUT_EXCEPTION))) {
					// 服务器请求失败
					msg.arg2 = -1;
				}
				contentUpdateHander.sendMessage(msg);
			}
		}.start();
		return true;
	}
	
	//获取 指定 用户的基本信息
	public void getUserBaseInfo(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				BaseParamBean bBean = new BaseParamBean();
				bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserUID());
				bBean.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
				bBean.setVer(1);
				bBean.setPartner_id(28);	//高峰测试帐号
				
				
				HashMap<String, String> map = new HashMap<String, String>();
				String jsonToSting = JsonForRequest.getBaseInfo_ExtInfo(bBean);
				map.put("json", jsonToSting);
				String json = httpClientUtils.requestPostHttpClient(URLConfig.get_base_info, "utf-8", map);
				UserBean pBean = UsersDetailsParser.getUserBaseInfo(json);
				
			}
		}).start();
	}
	/**
	 * #####更新最近浏览节目位置
user/visit
	 */
	
/*	{
	    "sid": "ugqrcqu3pkh7tmln3pfa0147a6",
	    "uid": 31,
	    "ver": 1
	    "req": {
	        "visit": 647	//访问的节目ID
			"channelid": 10	//频道id（可选）
	    },
	}
	成功
	{
		"ret" :  0,
		"errcode" :  0,
		"msg" :  "ok",
		"resp" : {}
	}*/
	
	public void UpdateLastVisit(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				BaseParamBean bBean = new BaseParamBean();
				bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserUID());
				bBean.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
				bBean.setVer(1);
				
				VisitBean vBean = new VisitBean();
				vBean.setVisit_channelid("1");	//可选
				vBean.setVisit_pid("102");	
				
				HashMap<String, String> map = new HashMap<String, String>();
				String jsonToSting = JsonForRequest.updataLastVisit(bBean, vBean);
				map.put("json", jsonToSting);
				String json = httpClientUtils.requestPostHttpClient(URLConfig.get_last_visit_program, "utf-8", map);
				BaseResponseBean brBean = NotifyMessageParser.getBaseResponse(json);
				
			}
		}).start();
	}
	/**
	 * 获取收藏列表
	 */
	public ArrayList<CollectBean> getCollectionList(int partner_id,int pageIndex){
		Log.e("afff","=====================pageIndex=="+pageIndex);
		BaseParamBean bBean = new BaseParamBean();
		bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserUID());
		bBean.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
		bBean.setCountInPage(10);
		bBean.setPageIndex(pageIndex);
		bBean.setOffset(pageIndex);
		bBean.setVer(2);
		bBean.setPartner_id(partner_id);
		bBean.setType("phone");
		
		HashMap<String, String> map = new HashMap<String, String>();
		String jsonToSting = JsonForRequest.getUserCollectionList(bBean);
		map.put("json", jsonToSting);
		String json = httpClientUtils.requestPostHttpClient(URLConfig.get_collection_list, charset, map);
//Log.e("aaa","==========="+json);		System.out.println("ContentImpl GetCollectionList request  map : "+map);
		CollectListBean cBeanList = UsersDetailsParser.getCollectionList(json);
		ArrayList<CollectBean> cBList = new ArrayList<CollectBean>();
		if(cBeanList != null){
			cBList = cBeanList.getCollectList();
			if(cBList != null){
//			Log.e("aaa","==========="+cBList.size());
			}else{
				cBList = null;
//			Log.e("aaa","======null=====");
			}
		}
		
		return cBList;
	}
	
	/**
	 * 获取收藏列表
	 */
	public void getCollectionList2(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				ArrayList<CollectBean> cBList= getCollectionList(TivicGlobal.getInstance().userRegisterBean.getUserUID(),0);
				
				HashMap<String, String> article_id = new HashMap<String, String>();
				if(null != cBList && !cBList.isEmpty())
				for (CollectBean collectBean : cBList) {
					article_id.put(collectBean.getFav_id(), collectBean.getFav_id());
				}
				
				Message msg = collection_list_handler.obtainMessage();
				msg.arg1 = MSG_COLLECTON_LIST;
				msg.obj = article_id;
				collection_list_handler.sendMessage(msg);
				UIUtils.Logd("lq", "CollectionList : "+article_id);
			}
		}).start();
	}
	
	/**
	 * 
	 * @param handler
	 */
	public void getCollectionList(final Handler handler){
		new Thread(new Runnable() {
			@Override
			public void run() {
				BaseParamBean bBean = new BaseParamBean();
				bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserUID());
				bBean.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
				bBean.setVer(2);
				String list_json = httpClientUtils.requestPostHttpClient4(URLConfig.get_collectionId_list, charset, JsonForRequest.getCollectArticleIds(bBean));
				SparseIntArray sArrayList = UsersDetailsParser.getCollectedArticlesIdList(list_json);
				HashMap<String, String> id_map = new HashMap<String, String>();
				if(sArrayList != null)
				for (int i = 0; i < sArrayList.size(); i++) {
					id_map.put(String.valueOf(sArrayList.keyAt(i)), String.valueOf(sArrayList.valueAt(i)));
				}
				handler.obtainMessage(0, MSG_COLLECTON_LIST, 1, id_map).sendToTarget();
//				Message msg = collection_list_handler.obtainMessage();
//				msg.arg1 = MSG_COLLECTON_LIST;
//				msg.obj = id_map;
//				collection_list_handler.sendMessage(msg);
				UIUtils.Logd("lq", "articleIdList : "+id_map);
			}
		}).start();
	}
	
	/**
	 * 5.获取指定用户的所有收藏文章id
	/article/list_all_ids
	 */
	public void getCollectedArticleIdList(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				BaseParamBean bBean = new BaseParamBean();
				bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserUID());
				bBean.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
				bBean.setVer(2);
				String list_json = httpClientUtils.requestPostHttpClient4(URLConfig.get_collectionId_list, charset, JsonForRequest.getCollectArticleIds(bBean));
				SparseIntArray sArrayList = UsersDetailsParser.getCollectedArticlesIdList(list_json);
				HashMap<String, String> id_map = new HashMap<String, String>();
				if(sArrayList != null)
				for (int i = 0; i < sArrayList.size(); i++) {
					id_map.put(String.valueOf(sArrayList.keyAt(i)), String.valueOf(sArrayList.valueAt(i)));
				}
				Message msg = collection_list_handler.obtainMessage();
				msg.arg1 = MSG_COLLECTON_LIST;
				msg.obj = id_map;
				collection_list_handler.sendMessage(msg);
				UIUtils.Logd("lq", "articleIdList : "+id_map);
			}
		}).start();
	}
	/**
	 * #####获取频道截图列表
snapshot.tivic.com/v3/get_snapshots.php

(1)截屏是一个独立服务，因此域名与SNS的域名不同。
(2)未实现timestamp和count,因此这两个参数可以不传。
	 */
	public String[] getScreenShotImagesList(int channelId){
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
				BaseParamBean bBean = new BaseParamBean();
				bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserUID());
				bBean.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
				bBean.setVer(1);
				
				ScreenShotsBean sBean = new ScreenShotsBean();
				sBean.setChannel_id(channelId+"");
				
				String json = httpClientUtils.requestPostHttpClient4(URLConfig.get_screen_shot, charset, JsonForRequest.getSrceenShots(bBean, sBean));
				String[] screen_shot = UsersDetailsParser.getScreenShotsImagesList(json);
				return screen_shot;
			}
//		}).start();
//	}
	
}
