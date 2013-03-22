package com.igrs.tivic.phone.AsyncTask;

import android.os.AsyncTask;

import com.igrs.tivic.phone.Bean.Base.BaseParamBean;
import com.igrs.tivic.phone.Bean.UGC.UGCDataBean;
import com.igrs.tivic.phone.Bean.UGC.UGCReplyListBean;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Impl.UGCImpl;

public class UGCPublishAsyncTask extends AsyncTask<String[], Object, UGCReplyListBean>{
	private UGCPublishDataUpdateListener updateListener;
	private UGCReplyListBean datalist = null;
	private int offset = 0;
	private UGCDataBean ugcBean;
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		//datalist = new UGCReplyListBean();
	}

	@Override
	protected UGCReplyListBean doInBackground(String[]... params) {
		// TODO Auto-generated method stub
		UGCImpl ugcImpl = UGCImpl.getInstance();
		BaseParamBean param = new BaseParamBean();
		param.setVer(1);
		if (ugcBean != null) {
			param.setUid(TivicGlobal.getInstance().userRegisterBean.getUserId()); // 用户本人uid，基本参数
			param.setTid(ugcBean.getTid()); // 节目id

		} else {
			param.setUid(29); // 其实在这里这个参数没用
			param.setTid(170); // 节目id
		}
		param.setVer(2); //注意这个接口的version是2
		param.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
		param.setPid(102);
		param.setStartTime(0); // 基准时间，paging=2时该字段无效
		param.setSortType(2); // 分页方式，2-所有记录按时间降序排列
		param.setCountInPage(Const.COUNTINPAGE);
		param.setPageIndex(offset);
		param.setOffset(offset);

		datalist = ugcImpl.getReplyList2(param);
		return datalist;
	}

	@Override
	protected void onPostExecute(UGCReplyListBean result) {
		// TODO Auto-generated method stub
		if(updateListener != null)
			updateListener.onReplyListGet(result);
	}
	
	public void setUpdateUIListener(UGCPublishDataUpdateListener listener) {
		this.updateListener = listener;
	}
	
	public void setPosition(UGCDataBean _ugcBean) {
		this.ugcBean = _ugcBean;
	}

	public void setOffset(int _offset) {
		this.offset = _offset;

	}
	
	public interface UGCPublishDataUpdateListener{
		public void onReplyListGet(UGCReplyListBean datalist);
	}
}
