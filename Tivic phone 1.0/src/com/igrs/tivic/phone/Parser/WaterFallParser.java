package com.igrs.tivic.phone.Parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.igrs.tivic.phone.Bean.Waterfall.Place;
import com.igrs.tivic.phone.Bean.Waterfall.ProgramDetailBean;
import com.igrs.tivic.phone.Bean.Waterfall.ProgramDetailForPager;
import com.igrs.tivic.phone.Bean.Waterfall.ProgramDetailForPagers;
import com.igrs.tivic.phone.Bean.Waterfall.SubpageContent;
import com.igrs.tivic.phone.Utils.ModelUtil;
import com.igrs.tivic.phone.Utils.UIUtils;
import com.igrs.tivic.phone.Utils.Utils;

public class WaterFallParser {
	/**
	 * waterfall 新接口 解析.
	 * @param json
	 * @return
	 */
	public static ProgramDetailForPager parserProgramDetail2(String json){
		if(!Utils.isJsonValid(json))
			return null;
		ProgramDetailForPager pagersList = new ProgramDetailForPager();
			try {
				JSONObject joe = new JSONObject(json);
				if(!joe.isNull("ret")){
					if(0 == joe.getInt("ret")){
						if(!joe.isNull("resp")){
							JSONObject job = joe.getJSONObject("resp");
							pagersList.setPublishTime(job.getString("publishTime"));
							if(!job.isNull("contents")){
								JSONArray jsar = job.getJSONArray("contents");
								int len  = jsar.length();
								ArrayList<ArrayList<ProgramDetailBean>> blockList = new ArrayList<ArrayList<ProgramDetailBean>>();
								for (int i = 0; i < len; i++) {
									JSONObject jsob = jsar.getJSONObject(i);
									if(!jsob.isNull("blockList")){
										JSONArray jna = jsob.getJSONArray("blockList");
										int count = jna.length();
										ArrayList<ProgramDetailBean> pBeanList= new ArrayList<ProgramDetailBean>(); 
										for (int j = 0; j < count; j++) {
											ProgramDetailBean pBean = new ProgramDetailBean();
											JSONObject jnb = jna.getJSONObject(j);
											Place p = new Place();
											if(!jnb.isNull("x") && !ModelUtil.isEmpty(jnb.getString("x"))){
												p.setPoint_x(Float.parseFloat(jnb.getString("x")));
											}
											if(!jnb.isNull("y") && !ModelUtil.isEmpty(jnb.getString("y"))){
												p.setPoint_y(Float.parseFloat(jnb.getString("y")));
											}
											if(!jnb.isNull("width") && !ModelUtil.isEmpty("width")){
												p.setDisplay_width(Float.parseFloat(jnb.getString("width")));
											}
											if(!jnb.isNull("height") && !ModelUtil.isEmpty("height")){
												p.setDisplay_height(Float.parseFloat(jnb.getString("height")));
											}
											pBean.setPlace(p);
											pBean.setFormat(jnb.getString("format"));
											if(!jnb.isNull("content")){
												JSONObject jbj = jnb.getJSONObject("content");
												SubpageContent content = new SubpageContent();
												content.setContent_id(jbj.getInt("id"));
												content.setTitle(jbj.getString("title"));
												content.setContent_time(jbj.getString("time"));
												content.setSummary(jbj.getString("summary"));
												content.setSource(jbj.getString("source"));
												content.setMedia_type(jbj.getString("mediaType"));
												content.setMedia_url(jbj.getString("mediaUrl"));
												content.setLink(jbj.getString("link"));
												content.setContent_tpye(jbj.getString("type"));
												if(!jbj.isNull("articleList")){
													JSONArray jor = jbj.getJSONArray("articleList");
													int counts = jor.length();
													ArrayList<String> urls = new ArrayList<String>();
													for (int k = 0; k < counts; k++) {
														urls.add(jor.getString(k));
													}
													content.setArticleList(urls);
												}
												content.setVideoImage(jbj.getString("videoImage"));
												content.setTop(jbj.getString("top"));
												pBean.setContent(content);
											}
											pBeanList.add(pBean);
										}
										blockList.add(pBeanList);
									}
									pagersList.setContentBlockList(blockList);
								}
							}
							if(!job.isNull("pages")){
								JSONArray jsar = job.getJSONArray("pages");
								int len  = jsar.length();
								ArrayList<ArrayList<ProgramDetailBean>> blockList = new ArrayList<ArrayList<ProgramDetailBean>>();
								for (int i = 0; i < len; i++) {
									JSONObject jsob = jsar.getJSONObject(i);
									if(!jsob.isNull("blockList")){
										JSONArray jna = jsob.getJSONArray("blockList");
										int count = jna.length();
										ArrayList<ProgramDetailBean> pBeanList= new ArrayList<ProgramDetailBean>(); 
										for (int j = 0; j < count; j++) {
											ProgramDetailBean pBean = new ProgramDetailBean();
											JSONObject jnb = jna.getJSONObject(j);
											Place p = new Place();
											if(!jnb.isNull("x") && !ModelUtil.isEmpty(jnb.getString("x"))){
												p.setPoint_x(Float.parseFloat(jnb.getString("x")));
											}
											if(!jnb.isNull("y") && !ModelUtil.isEmpty(jnb.getString("y"))){
												p.setPoint_y(Float.parseFloat(jnb.getString("y")));
											}
											if(!jnb.isNull("width") && !ModelUtil.isEmpty(jnb.getString("width"))){
												p.setDisplay_width(Float.parseFloat(jnb.getString("width")));
											}
											if(!jnb.isNull("height") && !ModelUtil.isEmpty(jnb.getString("height"))){
												p.setDisplay_height(Float.parseFloat(jnb.getString("height")));
											}
											pBean.setPlace(p);
											pBean.setFormat(jnb.getString("format"));
											if(!jnb.isNull("content")){
												JSONObject jbj = jnb.getJSONObject("content");
												SubpageContent content = new SubpageContent();
												content.setContent_id(jbj.getInt("id"));
												content.setTitle(jbj.getString("title"));
												content.setContent_time(jbj.getString("time"));
												content.setSummary(jbj.getString("summary"));
												content.setSource(jbj.getString("source"));
												content.setMedia_type(jbj.getString("mediaType"));
												content.setMedia_url(jbj.getString("mediaUrl"));
												content.setLink(jbj.getString("link"));
												content.setContent_tpye(jbj.getString("type"));
												if(!jbj.isNull("articleList")){
													JSONArray jor = jbj.getJSONArray("articleList");
													int counts = jor.length();
													ArrayList<String> urls = new ArrayList<String>();
													for (int k = 0; k < counts; k++) {
														urls.add(jor.getString(k));
													}
													content.setArticleList(urls);
												}
												content.setVideoImage(jbj.getString("videoImage"));
												content.setTop(jbj.getString("top"));
												pBean.setContent(content);
											}
											pBeanList.add(pBean);
										}
										blockList.add(pBeanList);
									}
									pagersList.setContentBlockList(blockList);
								}
							}
						}
					}else{
						
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
//			UIUtils.Loge("lq", "pagersList : "+pagersList.getContentBlockList().size());
		return pagersList;
	}
	public static ArrayList<ProgramDetailForPagers> parserProgramDetail(String json){
		if(!Utils.isJsonValid(json))
			return null;
		ArrayList<ProgramDetailForPagers> pagers = new ArrayList<ProgramDetailForPagers>();
		try {
			JSONArray programDetailArray = new JSONArray(json);
			int count = programDetailArray.length();
			for (int i = 0; i < count; i++) {
				ArrayList<ProgramDetailBean> programDetail = new ArrayList<ProgramDetailBean>();
				ProgramDetailForPagers pdf = new ProgramDetailForPagers();
				JSONObject jobj = programDetailArray.getJSONObject(i);
				JSONArray ja = jobj.getJSONArray("blockList");
				int len = ja.length();
				for (int j = 0; j < len; j++) {
					JSONObject jo = ja.getJSONObject(j);
					ProgramDetailBean pb = new ProgramDetailBean();
					
					pb.setId(j);//设置id为数组的index值
					
					Place p = new Place();
					SubpageContent sc = new SubpageContent();
					p.setPoint_x(jo.getInt("x"));
					p.setPoint_y(jo.getInt("y"));
					p.setDisplay_width(jo.getInt("width"));
					p.setDisplay_height(jo.getInt("height"));
					
					pb.setPlace(p);
					
					pb.setFormat(jo.getString("format"));
					if(!jo.isNull("content")){
						JSONObject content = jo.getJSONObject("content");
						sc.setContent_id(content.getInt("id"));
						sc.setTitle(content.getString("title"));
						sc.setContent_time(content.getString("time"));
						sc.setSummary(content.getString("summary"));
						sc.setSource(content.getString("source"));
						sc.setMedia_type(content.getString("mediaType"));
						sc.setMedia_url(content.getString("mediaUrl"));
						sc.setLink(content.getString("link"));
						sc.setContent_tpye(content.getString("type"));
						
						sc.setVideoImage(content.getString("videoImage"));
						sc.setTop(content.getString("top"));
						
						JSONArray jsona = content.getJSONArray("articleList");
						if(!content.isNull("articleList")){
							
							ArrayList<String> urls = new ArrayList<String>();
							for (int k = 0; k < jsona.length(); k++) 
							{	
							urls.add(k,jsona.getString(k));
//							sc.setArticleList_0(jsona.getJSONObject(0).getString("url"));
//							sc.setArticleList_1(jsona.getJSONObject(1).getString("url"));
							}
							sc.setArticleList(urls);
						}
						pb.setContent(sc);
					}
					
					programDetail.add(pb);
				}
				pdf.setProgramDetailBeans(programDetail);
				pagers.add(pdf);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return pagers;
		
	}
}
