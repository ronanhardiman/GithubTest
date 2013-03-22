package com.igrs.tivic.phone.Bean.Waterfall;

import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;

import com.igrs.tivic.phone.Adapter.WaterfallListdapter;

public class ItemBean implements Parcelable{

	private ArrayList<ProgramDetailBean> programDetailBeans;
	private int itemtype = Integer.MAX_VALUE;
	
	private int beansSize;
	
	public int getItemtype() {
		
		if(itemtype==Integer.MAX_VALUE){
			itemtype = firstGetItemType();
		}
		return itemtype;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		beansSize = programDetailBeans.size();
		
		dest.writeInt(beansSize);
		
		for(int i=0;i<beansSize;i++){
			dest.writeParcelable(programDetailBeans.get(i),flags);
		}
	}
	
	public static final Parcelable.Creator<ItemBean> CREATOR = new Parcelable.Creator<ItemBean>() {
		// 重写Creator
		@Override//实现从source中创建出类的实例
		public ItemBean createFromParcel(Parcel source) {
			ItemBean ib = new ItemBean();
			ib.readFromParcel(source);
			return ib;
		}
		@Override//创建一个类型为T，长度为size的数组。
		public ItemBean[] newArray(int size) {
			return new ItemBean[size];
		}
	};
	public void readFromParcel(Parcel source){
		this.beansSize = source.readInt();
		for(int i=0;i<this.beansSize;i++){
			ProgramDetailBean pdb = source.readParcelable(ProgramDetailBean.class.getClassLoader());
			this.programDetailBeans.add(pdb);
		}
	}
	
	private int firstGetItemType(){
		if(programDetailBeans.size()==1 && programDetailBeans.get(0).getPlace().getDisplay_width()==2){//一个2*2
			return WaterfallListdapter.TYPE_SINGLE_PIC;
		}else if(programDetailBeans.size()==2){//两个1×1
			String f1 =  programDetailBeans.get(0).getFormat();
			String f2 =  programDetailBeans.get(1).getFormat();
			if("1".equals(f1) && "1".equals(f2)){
				return WaterfallListdapter.TYPE_PIC_PIC;
			}else if("1".equals(f1) && "0".equals(f2)){
				return WaterfallListdapter.TYPE_PIC_TEXT;
			}else if("0".equals(f1) && "1".equals(f2)){
				return WaterfallListdapter.TYPE_TEXT_PIC;
			}else{
				return WaterfallListdapter.TYPE_TEXT_TEXT;
			}
		}else{//最后就是剩一个1×1的情况
			if("0".equals( programDetailBeans.get(0).getFormat())){
				return WaterfallListdapter.TYPE_TEXT_TEXT;
			}else{
				return WaterfallListdapter.TYPE_PIC_PIC;
			}
		}
	}
	
	public ItemBean(){
		programDetailBeans = new ArrayList<ProgramDetailBean>();
	}
	public ArrayList<ProgramDetailBean> getProgramDetailBean() {
		return programDetailBeans;
	}
	public void addPdb(ProgramDetailBean pdb){
//		this.pdb = pdb;
		programDetailBeans.add(pdb);
	}
}
