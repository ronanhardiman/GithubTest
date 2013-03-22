package com.igrs.tivic.phone.Bean.Base;

public class BaseParamBean {

	int uid;  		//userID 用户ID
	String sid; 		//用户登录后的唯一识别码，重要！
	int cid; 		//频道ID
	int pid; 		//programID 栏目ID
	String contentId;	//文章id
	int bar_id;    //指定贴吧id，同节目id
//	int contentId;	//文章id
	int tid; 		//帖子ID
	int ppid; 		//暂时用不到 ，评论id
	int post_id ;  //帖子id，同tid。可选，仅在翻页时有效。
	int reply_id;  //回复id，可选，仅在翻页时有效。
	int nid;		//通知id，备用
	int mid; 		//消息id，备用
	
	String udid; 	//设备型号 或 本地缓存中的unix1970时间偏移量 （用于多设备登录）
	String deviceType;  //ipad phone区分设备类型
	String type; 	//设备类型
	
	int sortType;  	// 0,1,2  // 0 大于time的按时间升序；1 小于是startTime的按时间降序; 2 所有的记录按时间降序
	int countInPage; //一次取countInPage条数据
	int pageIndex; //用于分页加载数据时，页标
	long startTime; // 起始时间（如获取帖子等）或基准时间（如获取通知和私信等）
	int offset; 	//偏移量，用于帖子和评论和通知获取下、上页数据使用
	int ver = 1; //版本号，默认是1不需更改
	
	int page_flag; //分页标识 0：取首页；1：向后翻页，获取小于上次返回的最后一条post_id的帖子；2：向前翻页，获取大于上次返回的第一条post_id的帖子。	
	int reqnum;     //每次获取数量，  取值范围[1,30]
	int opreate = 0; //用于消息变更状态 0：只查询 1：查询并设置所有未读消息为已读，且仅在pageIndex为0时生效。
	int official = 0; //是否同步到微博，默认不同步0
	int partner_id; //用于私信记录的好友id
	
	public int getReply_id() {
		return reply_id;
	}
	public void setReply_id(int reply_id) {
		this.reply_id = reply_id;
	}
	public int getPost_id() {
		return post_id;
	}
	public void setPost_id(int post_id) {
		this.post_id = post_id;
	}
	public int getBar_id() {
		return bar_id;
	}
	public void setBar_id(int bar_id) {
		this.bar_id = bar_id;
	}
	public int getPage_flag() {
		return page_flag;
	}
	public void setPage_flag(int page_flag) {
		this.page_flag = page_flag;
	}
	public int getReqnum() {
		return reqnum;
	}
	public void setReqnum(int reqnum) {
		this.reqnum = reqnum;
	}
	public int getVer() {
		return ver;
	}
	public void setVer(int ver) {
		this.ver = ver;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getSortType() {
		return sortType;
	}
	public void setSortType(int sortType) {
		this.sortType = sortType;
	}
	public int getCountInPage() {
		return countInPage;
	}
	public void setCountInPage(int countInPage) {
		this.countInPage = countInPage;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getUdid() {
		return udid;
	}
	public void setUdid(String udid) {
		this.udid = udid;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public int getPpid() {
		return ppid;
	}
	public void setPpid(int ppid) {
		this.ppid = ppid;
	}
	
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public int getOpreate() {
		return opreate;
	}
	public void setOpreate(int opreate) {
		this.opreate = opreate;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getOfficial() {
		return official;
	}
	public void setOfficial(int official) {
		this.official = official;
	}
	public int getNid() {
		return nid;
	}
	public void setNid(int nid) {
		this.nid = nid;
	}
	public int getMid() {
		return mid;
	}
	public void setMid(int mid) {
		this.mid = mid;
	}
	public int getPartner_id() {
		return partner_id;
	}
	public void setPartner_id(int partner_id) {
		this.partner_id = partner_id;
	}
	
	
	
}
