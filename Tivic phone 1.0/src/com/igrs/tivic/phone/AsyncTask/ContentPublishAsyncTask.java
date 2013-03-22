package com.igrs.tivic.phone.AsyncTask;

import android.os.AsyncTask;
import android.util.Log;

import com.igrs.tivic.phone.Bean.Base.BaseParamBean;
import com.igrs.tivic.phone.Bean.PGC.ContentPublishListBean;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Impl.ContentImpl;

public class ContentPublishAsyncTask extends AsyncTask<String, Object, ContentPublishListBean>{
	private ContentPublishDataUpdateListener updateListener;
	private ContentPublishListBean datalist = null;
	private int pageIndex = 1;
	private String content_id;
	private int offset = 0;
	
	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		//datalist = new UGCReplyListBean();
	}

	@Override
	protected ContentPublishListBean doInBackground(String... params) {
		// TODO Auto-generated method stub
		content_id = params[0];
		ContentImpl contentImpl = ContentImpl.getInstance();
		BaseParamBean param = new BaseParamBean();
		param.setUid(TivicGlobal.getInstance().userRegisterBean.getUserId());
		//Log.i("kevin add:", "jsonrequest 49 = contentId = " + content_id);

		param.setContentId(content_id);
		param.setVer(2); //注意这个接口的version是2
		param.setOffset(offset);
		param.setSortType(2); // 分页方式，2-所有记录按时间降序排列
		param.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
		param.setCountInPage(Const.COUNTINPAGE);
		param.setPageIndex(offset);
		datalist = contentImpl.getReplyList(param);
		return datalist;
	}

	@Override
	protected void onPostExecute(ContentPublishListBean result) {
		if(updateListener != null)
			updateListener.onReplyListGet(result);
	}
	
	public void setUpdateUIListener(ContentPublishDataUpdateListener listener) {
		this.updateListener = listener;
	}


	public void setPageIndext(int index) {
		this.pageIndex = index;

	}
	
	public interface ContentPublishDataUpdateListener{
		public void onReplyListGet(ContentPublishListBean datalist);
	}
}
