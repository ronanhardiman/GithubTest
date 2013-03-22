package com.igrs.tivic.phone.Impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;

import com.igrs.tivic.phone.Bean.BaseResponseBean;
import com.igrs.tivic.phone.Bean.VisitBean;
import com.igrs.tivic.phone.Bean.Base.BaseParamBean;
import com.igrs.tivic.phone.Bean.Waterfall.ItemBean;
import com.igrs.tivic.phone.Bean.Waterfall.Place;
import com.igrs.tivic.phone.Bean.Waterfall.ProgramDetailBean;
import com.igrs.tivic.phone.Bean.Waterfall.ProgramDetailForPager;
import com.igrs.tivic.phone.Bean.Waterfall.ProgramDetailForPagers;
import com.igrs.tivic.phone.Bean.Waterfall.SubpageContent;
import com.igrs.tivic.phone.Bean.Waterfall.WaterfallAdapterData;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Global.URLConfig;
import com.igrs.tivic.phone.Parser.NotifyMessageParser;
import com.igrs.tivic.phone.Parser.WaterFallParser;
import com.igrs.tivic.phone.Utils.HttpClientUtils;
import com.igrs.tivic.phone.Utils.JsonForRequest;
import com.igrs.tivic.phone.Utils.UIUtils;

public class WaterFallImpl {

	
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
	
	public static void UpdateLastVisit(String cid,String pid,String ptitle,String channel_name){
		final VisitBean vb = new VisitBean();
		vb.setVisit_channelid(cid);vb.setVisit_pid(pid);vb.setVisit_ProgramTitle(ptitle);
		vb.setVisit_ChannelName(channel_name);
		final HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
		new Thread(new Runnable() {
			@Override
			public void run() {
				BaseParamBean bBean = new BaseParamBean();
				bBean.setUid(TivicGlobal.getInstance().userRegisterBean.getUserUID());
				bBean.setSid(TivicGlobal.getInstance().userRegisterBean.getUserSID());
				bBean.setVer(1);
				
				VisitBean vBean = new VisitBean();
				vBean.setVisit_channelid(vb.getVisit_channelid());	//可选
				vBean.setVisit_pid(vb.getVisit_pid());	
				
				HashMap<String, String> map = new HashMap<String, String>();
				String jsonToSting = JsonForRequest.updataLastVisit(bBean, vBean);
				map.put("json", jsonToSting);
				String json = httpClientUtils.requestPostHttpClient(URLConfig.get_last_visit_program, "utf-8", map);
//				UIUtils.Loge("lq", "updateLastVisit : json "+json+"  vBean.getVisit_channelid() : "+vb.getVisit_channelid());
				BaseResponseBean brBean = NotifyMessageParser.getBaseResponse(json);
				if(brBean != null)
				{
					Log.e("currentPid","==================UpdateLastVisit===="+brBean.getRet());
					UIUtils.setVisitBean(vb);
				}
			}
		}).start();
	}
	/**
	 * 获取放在raw目录下的本地文件
	 * @param context
	 * @return
	 */
	public static ArrayList<ItemBean> getContent(Context context) {

		ArrayList<ItemBean> itembeans = new ArrayList<ItemBean>();
		try {
			InputStream fis = context.getAssets().open("cc.txt");
			byte[] buff = new byte[1024 * 1024];
			fis.read(buff);
			String jj = new String(buff, 0, buff.length, "UTF-8");
			// Log.e("cys","=========================="+jj);

			// 将所有页的内容放入同一个集合中(pdbs_old)
			ArrayList<ProgramDetailForPagers> pdfps = WaterFallParser
					.parserProgramDetail(jj);

			for (int i = 0; i < pdfps.size(); i++) {
				ArrayList<ProgramDetailBean> onePagerBeans = pdfps.get(i)
						.getProgramDetailBeans();// 获得一页的数据

				// 模拟3行2列的情况 删除多余行列的数据
				ArrayList<ProgramDetailBean> deledObjs = new ArrayList<ProgramDetailBean>();
				for (int j = 0; j < onePagerBeans.size(); j++) {// 模拟3行2列的情况
					// 而是用的接口数据是3行4列
					// 因此要把第三列和第四列数据删除（也就是将x坐标为2和3的数据删除）
					ProgramDetailBean pdb = onePagerBeans.get(j);
					if (pdb.getPlace().getPoint_x() == 2
							|| pdb.getPlace().getPoint_x() == 3) {
						deledObjs.add(pdb);
					}
				}
				onePagerBeans.removeAll(deledObjs);// 删除多余接口数据

				splitProgramDetailBean(onePagerBeans);// 将01，10的单元格进行拆分

				Collections.sort(onePagerBeans);// 根据ProgramDetailBean的比较器排序，排序之后pdbs就是先排行后排列的有序集合了

				for (int index = 0; index < onePagerBeans.size(); index++) {// 封装itembean
																			// 其有两种类型
																			// 其内部元素集合为2个1×1的
																			// 和
																			// 1个2×2的
					ItemBean ib = null;
					if (onePagerBeans.get(index).getPlace().getDisplay_width() == 1) {
						ib = new ItemBean();
						ib.addPdb(onePagerBeans.get(index));
						if (index + 1 < onePagerBeans.size())
							ib.addPdb(onePagerBeans.get(++index));

					} else {
						ib = new ItemBean();
						ib.addPdb(onePagerBeans.get(index));
					}
					itembeans.add(ib);
				}
			}

			return itembeans;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 这个是网络真实数据
	 * @param context
	 * @return
	 */
	public static WaterfallAdapterData getContentByUrl(Context context,String pid) {


		WaterfallAdapterData wad = new WaterfallAdapterData();
		
		String getListUrl = URLConfig.get_waterfallList;
		if(pid==null) pid="100";
		String url = getListUrl.replace("{programID}",pid);
//		Log.e("WaterFallImpl", "==========uil = " + url);
		ArrayList<ItemBean> itembeans = new ArrayList<ItemBean>();		
		String json = HttpClientUtils.getInstance().requestGet(url,"utf-8");
//		Log.e("WaterFallImpl", "============= " + json);
		ProgramDetailForPager pagers = WaterFallParser.parserProgramDetail2(json);
		if(pagers == null || pagers.getContentBlockList()==null || pagers.getContentBlockList().size() == 0) 
			return wad;
		ArrayList<ArrayList<ProgramDetailBean>> pdfps = pagers.getContentBlockList();
		
		ArrayList<ProgramDetailBean> sendToContentActivityData = new ArrayList<ProgramDetailBean>();
		
		for (int i = 0; i < pdfps.size(); i++) {
			ArrayList<ProgramDetailBean> onePagerBeans = pdfps.get(i);// .getProgramDetailBeans();//获得一页的数据
			sendToContentActivityData.addAll(onePagerBeans);
			splitProgramDetailBean(onePagerBeans);// 将01，10的单元格进行拆分

			Collections.sort(onePagerBeans);// 根据ProgramDetailBean的比较器排序，排序之后pdbs就是先排行后排列的有序集合了
			
			//封装itembean其有两种类型   其内部元素集合为2个1×1的和1个2×2的
			for (int index = 0; index < onePagerBeans.size(); index++){
				ItemBean ib = null;
				if (onePagerBeans.get(index).getPlace().getDisplay_width() == 1) {
					ib = new ItemBean();
					ib.addPdb(onePagerBeans.get(index));
					if (index + 1 < onePagerBeans.size()){
						ib.addPdb(onePagerBeans.get(++index));
					}

				} else {
					ib = new ItemBean();
					ib.addPdb(onePagerBeans.get(index));
				}
				itembeans.add(ib);
			}
		}
		wad.setSendToContentActivityData(sendToContentActivityData);
		wad.setItemBeans(itembeans);
		return wad;
	}
	
	private static ProgramDetailBean createOject(ProgramDetailBean oldBean,
			String media_type, String format, Place p) {
		if (p == null) {
			p = new Place();
			p.setPoint_x(oldBean.getPlace().getPoint_x());
			p.setPoint_y(oldBean.getPlace().getPoint_y());
			p.setDisplay_height(1);
			p.setDisplay_width(1);
		}
		SubpageContent sc = new SubpageContent();
		ArrayList<String> articleList = new ArrayList<String>();

		for (String str : oldBean.getContent().getArticleList()) {
			articleList.add(str);
		}

		sc.setArticleList(articleList);
		sc.setContent_id(oldBean.getContent().getContent_id());
		sc.setContent_time(oldBean.getContent().getContent_time());
		sc.setContent_tpye(oldBean.getContent().getContent_tpye());
		sc.setLink(oldBean.getContent().getLink());
		sc.setMedia_type(media_type);
		sc.setMedia_url(oldBean.getContent().getMedia_url());
		sc.setSource(oldBean.getContent().getSource());
		sc.setSummary(oldBean.getContent().getSummary());
		sc.setTitle(oldBean.getContent().getTitle());

		ProgramDetailBean pgb = new ProgramDetailBean();
		pgb.setContent(sc);
		pgb.setFormat(format);
		pgb.setPlace(p);
		pgb.setId(oldBean.getId());
		return pgb;
	}

	private static void splitProgramDetailBean(
			ArrayList<ProgramDetailBean> targetBeans) {

		// 保存新创建的对象集合
		ArrayList<ProgramDetailBean> newBeans = new ArrayList<ProgramDetailBean>();

		for (ProgramDetailBean bean : targetBeans) {
			if ("01".equals(bean.getFormat())) {// 文字在前
				ProgramDetailBean pgb = createOject(bean, "text", "0", null);// 生成文字对应的对象
				newBeans.add(pgb);
				if (bean.getPlace().getDisplay_height() == 1) {// 文字在左，图片在右
					// 修改图片对应的对象
					bean.setFormat("1");
					bean.getPlace()
							.setPoint_x(bean.getPlace().getPoint_x() + 1);
					bean.getPlace().setDisplay_width(1);
					bean.getPlace().setDisplay_height(1);

					pgb.setType(ProgramDetailBean.RIGHT_NEXT);
					bean.setType(ProgramDetailBean.LEFT_NEXT);
				} else {// 文字在上，图片在下
						// 修改图片对应的对象
					bean.setFormat("1");
					bean.getPlace()
							.setPoint_y(bean.getPlace().getPoint_y() + 1);
					bean.getPlace().setDisplay_width(1);
					bean.getPlace().setDisplay_height(1);

					pgb.setType(ProgramDetailBean.BOTTOM_NEXT);
					bean.setType(ProgramDetailBean.TOP_NEXT);
				}
			} else if ("10".equals(bean.getFormat())) {// 图在前
				// 生成图片对应的对象
				ProgramDetailBean pgb = createOject(bean, "image", "1", null);
				newBeans.add(pgb);
				if (bean.getPlace().getDisplay_height() == 1) {// 图在左，文字在右
					bean.setFormat("0");
					bean.getPlace()
							.setPoint_x(bean.getPlace().getPoint_x() + 1);
					bean.getPlace().setDisplay_width(1);
					bean.getPlace().setDisplay_height(1);
					bean.getContent().setMedia_type("text");

					pgb.setType(ProgramDetailBean.RIGHT_NEXT);
					bean.setType(ProgramDetailBean.LEFT_NEXT);
				} else {// 图在上，文字在下
					bean.setFormat("0");
					bean.getPlace()
							.setPoint_y(bean.getPlace().getPoint_y() + 1);
					bean.getPlace().setDisplay_width(1);
					bean.getPlace().setDisplay_height(1);
					bean.getContent().setMedia_type("text");

					pgb.setType(ProgramDetailBean.BOTTOM_NEXT);
					bean.setType(ProgramDetailBean.TOP_NEXT);
				}
			}
		}
		targetBeans.addAll(newBeans);
	}
}
