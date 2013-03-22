package com.igrs.tivic.phone.Global;

public class URLConfig {
	
//开发环境
//	public static final String SNSIP = "http://10.1.33.4";
//	public final static String avarter_path = "http://devimagev2.b0.upaiyun.com";
//	public final static String CMSIP = "http://devstatic.b0.upaiyun.com";
//	public final static String snapshot_path = "http://snapshot.tivic.com";
//测试环境
	public static final String SNSIP = "http://10.1.33.34";
	public final static String avarter_path = "http://unstableimagev2.b0.upaiyun.com";
	public final static String CMSIP = "http://unstablestatic.b0.upaiyun.com";
	public final static String snapshot_path = "http://snapshot.tivic.com";	
	
	
	
	public static final String port="8080";	
	public static final String passport ="passport";	
	public static final String header = SNSIP + "/index.php";
	
	//register

	
	public final static String reg_register = header+"/api/user/register";
	public static final String reg_checkPhoneNumber = header+"/api/user/valid_code";//验证手机
	public static final String resend_sms = header+"/api/user/resend_sms";//发送验证短信到手机
	public static final String modify_user_register_info = header+"/api/user/modify_info";//修改用户注册时的基本信息
	public static final String resend_mail = header+"/api/user/resend_mail";//再次发送邮件
	public static final String send_mail_for_pwd= header+"/api/findPWD/request";//发送找回密码邮件
	public static final String send_msg_for_pwd = header+"/api/findPWD/request";//发送找回密码短信
	public static final String verify_msg_for_pwd = header+"/api/findPWD/verify_code";//验证找回密码的短信
	public static final String modify_pwd_by_phone = header+"/api/findPWD/confirm";//通过短信修改密码
	public static final String starts_info = header+"/api/star/list";
	public static final String programs_info = header+"/api/program/list";
	public static final String categories_info = header+"/api/category/list";
	public static final String login_by_weibo = header+"/api/snsaccount/login";//通过微博登陆
	public static final String fasten_weibo = header+"/api/snsaccount/bind";//绑定微博
	public static final String REDIRECT_URL = "http://10.1.33.48";//新浪微博回调地址	
	public static final String unfasten_weibo = header+"/api/snsaccount/unbind";//解除绑定微博
	public static final String exists_user = SNSIP + "/index.php/api/user/exists_user"; //检测用户是否被占用
	// login								  
	public static final String modify_pwd = header+"/api/user/modify_pwd";//修改用户密码
	public static final String upgrade_info = header+"/api/upgrade/upgrade_info";//获得最新版本
	public static final String poi_item_info = SNSIP + "/index.php/api/user/get_info_ext";//获取感兴趣的节目、明星、内容的信息
	public static final String update_user_location = header+"/api/user/update_location";//获得最新版本
	
	public static final String login_login = SNSIP + "/index.php/api/user/login";
	public static final String login_getUID = SNSIP + "/sns/api/v1/login.php?";
	public static final String login_logout = SNSIP + port + "/api/v1/logout.json";
	public static final String login_sns = SNSIP + "/sns/api/v1/login.php";
	public static final String login_weibo = "";
	
	
	public static final String search_userinfo = ""; //指定被查询的用户    获取基本信息
	public static final String search_userinfo_ext = ""; //指定被查询的用户    获取扩展信息
	public static final String modify_userinfo = ""; //修改用户信息
	//MyTv
	public static final String get_MyTv_list = SNSIP + "/index.php/api/mytv/pg_list_all";//获取指定用户的关注节目列表:全部节目,按加入关注的时间降序排列
	public static final String get_remove_focus = SNSIP + "/index.php/api/mytv/pg_unfav";//取消关注
	public static final String get_add_focus = SNSIP + "/index.php/api/mytv/pg_fav";//关注节目
	public static final String get_MyTv_daily_list = SNSIP + "/index.php/api/mytv/pg_list_day";//获取我的关注节目列表:某一天会播出的节目
	public static final String get_MyTv_channel_program_list = SNSIP + "/index.php/api/mytv/pg_list_with_channel";//5,获取的指定用户的关注节目—频道列表
	//epg
	public static final String epgUrl = SNSIP + "/cms/sample_epg.php?when=";
	public static final String get_epgChannelList = SNSIP + "/index.php/api/pgc/channel_list";//频道列表信息
	public static final String get_epg_list = SNSIP + "/index.php/api/pgc/epg_list";//获取EPG信息, 公共接口，无需登录
	//social
	public static final String get_base_info = SNSIP + "/index.php/api/user/get_info_base";//获取基本信息
	public static final String get_ext_info = SNSIP + "/index.php/api/user/get_info_ext";//获取扩展信息
	public static final String get_photo_list = SNSIP + "/index.php/api/user/list_photo";//获取用户照片列表（我或他人）
	public static final String get_photo_upload = SNSIP + "/index.php/api/user/upload_photo";//上传用户图片
	public static final String get_photos_delete = SNSIP + "/index.php/api/user/del_photo";//删除用户图片
	public static final String get_modify_info = SNSIP + "/index.php/api/user/modify_info";//修改用户信息
	public static final String get_modify_password = SNSIP + "/index.php/api/user/modify_pwd";//修改用户密码
	public static final String get_modify_location = SNSIP + "/index.php/api/user/update_location";//更改用户的Location
	public static final String get_user_checkin = SNSIP + "/index.php/api/user/checkin";//用户签到
	public static final String get_recommend_star_info = SNSIP + "/index.php/api/star/list";//获取扩展信息(stars)
	public static final String get_recommend_category_info = SNSIP + "/index.php/api/category/list";//获取扩展信息(category)
	public static final String get_recommend_program_info = SNSIP + "/index.php/api/program/list";//获取扩展信息(program)
	public static final String get_last_visit_program = SNSIP + "/index.php/api/user/visit";//更新最近浏览节目位置
	public static final String get_count_news = SNSIP + "/index.php/api/status/count_new";//聚合查询： message news
	public static final String get_add_collection = SNSIP + "/index.php/api/article/favorite";//加入收藏
	public static final String get_remove_collection = SNSIP + "/index.php/api/article/unfavorite";//取消收藏
	public static final String get_collection_list = SNSIP + "/index.php/api/article/blockList";//3.获取指定用户的收藏列表(接口升级VERSION 2)
	public static final String get_collectionId_list = SNSIP + "/index.php/api/article/list_all_ids";//获取指定用户的所有收藏文章id
	public static final String get_is_collection = SNSIP + "/index.php/api/article/isfavorite";//是否收藏
	public static final String get_fansList = SNSIP + "/index.php/api/friend/list_fans";//获取指定用户的粉丝列表
	public static final String get_follow_List = SNSIP + "/index.php/api/friend/list_follow";//获取指定用户的好友列表
	public static final String get_stranger_List = SNSIP + "/index.php/api/friend/list_stranger";//获取登录用户的陌生人列表
	public static final String get_blocked_List = SNSIP + "/index.php/api/friend/list_blocked";//获取登录用户的黑名单列表
	public static final String get_follow_people = SNSIP + "/index.php/api/friend/follow";//关注(他人)
	public static final String get_Unfollow_people = SNSIP + "/index.php/api/friend/unfollow";//取消关注(他人)
	public static final String get_block_people = SNSIP + "/index.php/api/friend/block";//拉黑(他人)
	public static final String get_Unblock_people = SNSIP + "/index.php/api/friend/unblock";//取消拉黑(他人)
	public static final String get_reply_list = SNSIP + "/index.php/api/lanmu/list_reply";//获取评论列表（接口升级）version2
	public static final String get_reply = SNSIP + "/index.php/api/lanmu/reply";//回复栏目文章
	public static final String get_find_people = SNSIP + "/index.php/api/friend/find_people";//9, 查找用户
	
	
	//ugc
	public static final String UGC_getUGCListUrl = header+"/api/tieba/list_post";
	public static final String UGC_getUsrUGCListUrl = header+"/api/tieba/list_post_user";
	public static final String UGC_postUGCUrl = header+"/api/tieba/post";
	public static final String UGC_getReplyListUrl = header+"/api/tieba/list_reply";
	public static final String UGC_replyUrl = header+"/api/tieba/reply";
	public static final String UGC_supportUrl = header+"/api/tieba/up";
	//notify and message
	public static final String Notify_getNotifyListUrl = header + "/api/notice/history";
	public static final String Message_getMessageHomeListUrl = header + "/api/message/home";
	public static final String Message_getMessageHistoryListUrl = header + "/api/message/history";
	public static final String Message_sendMessageUrl = header + "/api/message/send";
	//count new
	public static final String getCountNewUrl = header + "/api/status/count_new";
	//Content reply
	public static final String Content_getReplyListUrl = header+"/api/lanmu/list_reply";
	public static final String Content_replyUrl = header+"/api/lanmu/reply";	
	//marquee
	public static final String get_marqueeIdList = CMSIP+ "/marquee/phone/marquee.html";	//跑马灯
	//PGC
	public static final String get_waterfallList = CMSIP + "/cascade/phone/{programID}.html";//瀑布流
	public static final String get_pushtvList = CMSIP + "/hotlist/phone/hotlist.html";	//热播榜单
	//search 
	public static final String get_search_program = SNSIP + "/index.php/api/search/program";//节目搜索接口
	public static final String get_search_hot_program = SNSIP + "/index.php/api/search/pghot_user_visit";//节目热度表_用户访问最多的节目
	//screen shots
//	public static final String get_screen_shot = snapshot_path + "/v3/get_snapshots.php";//获取频道截图列表
	public static final String get_screen_shot = SNSIP + "/index.php/api/snapshot/get";//获取频道截图列表
	//just for test
	public static final String test_cascade_new = CMSIP + "/cascade/102_phone.html";//新增加的--瀑布流测试
	public static final String test_cascade2 = CMSIP + "/cascade/104_phone.html";//瀑布流测试
//	public static final String test_video = "http://devstatic.b0.upaiyun.com/article/2012/12/13/275_video.html";
//	public static final String test_news = CMSIP + "/test/phone/test_news.html";
//	public static final String test_news2 = "http://devstatic.b0.upaiyun.com/article/2012/12/20/318_news.html";
//	public static final String test_tfind = CMSIP + "/test/phone/test_tvfind.html";
//	public static final String test_tvfind2 = "http://devstatic.b0.upaiyun.com/article/2012/12/14/309_tvfind.html";
//	public static final String test_hotList = CMSIP + "/test/phone/test_hotList.html";
//	public static final String test_hotList2 = "http://devstatic.b0.upaiyun.com/test/phone/test_hotList.html";		
//	public static final String test_cascade = CMSIP + "/test/phone/test_cascade.html";//瀑布流
	public static final String test_marqueeIdList = "http://10.1.33.49" + "/test/phone/test_marqueeIdList.html";
//	public static final String test_findpwd = SNSIP + "/retrieve_pwd.php";		
	}
