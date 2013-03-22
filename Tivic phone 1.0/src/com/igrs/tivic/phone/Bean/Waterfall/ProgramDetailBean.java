package com.igrs.tivic.phone.Bean.Waterfall;


import android.os.Parcel;
import android.os.Parcelable;

public class ProgramDetailBean implements Comparable<ProgramDetailBean>,Parcelable{
	
	public static final int TOP_NEXT = 0;//上面有紧邻元素
	public static final int BOTTOM_NEXT = 1;//下面有紧邻元素
	public static final int LEFT_NEXT = 2;//左面有紧邻元素
	public static final int RIGHT_NEXT = 3;//右面有紧邻元素
	public static final int SINGLE_ELE = 4;//单独元素
	
	private String format;
	private int id;//该实现在集合中的索引
	private Place place;//模块的位置,和宽高
	private SubpageContent content;//模块内容
	private int type = SINGLE_ELE;//此单元格类型  默认为单独元素单元格
	
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(content, flags);
	}
	
	public static final Parcelable.Creator<ProgramDetailBean> CREATOR = new Parcelable.Creator<ProgramDetailBean>() {
		// 重写Creator
		@Override//实现从source中创建出类的实例
		public ProgramDetailBean createFromParcel(Parcel source) {
			ProgramDetailBean pdb = new ProgramDetailBean();
			pdb.content = source.readParcelable(SubpageContent.class.getClassLoader());
			return pdb;
		}
		@Override//创建一个类型为T，长度为size的数组。
		public ProgramDetailBean[] newArray(int size) {
			return new ProgramDetailBean[size];
		}
	};
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public Place getPlace() {
		return place;
	}
	public void setPlace(Place place) {
		this.place = place;
	}
	public SubpageContent getContent() {
		return content;
	}
	public void setContent(SubpageContent content) {
		this.content = content;
	}
//	@Override
//	public String toString() {
//		return "Place [format=" + format +", place" + place.toString() +",SubpageContent=" + content+"]";
//	}
	
	/**
	 * 比较器    作用是让这个ProgramDetailBean所在的集合进行排序，规则是先按行从小到大 （y） 再按 列（x）从小到大
	 * 未排序                  排序后
	 * 0,0    0,0
	   1,0    1,0
	   1,1    2,0
	   0,2    1,1
	   1,2    2,1
	   2,0    0,2
	   2,1    1,2
	 */
	@Override//
	public int compareTo(ProgramDetailBean another) {
		int retult = 0;
		float mValy = place.getPoint_y();
		float anotherValy=another.getPlace().getPoint_y();
		retult = mValy == anotherValy ? 0 :(mValy<anotherValy?-1:1);	
		
		if(retult==0){
			float mValx = place.getPoint_x();
			float anotherValx = another.getPlace().getPoint_x();
			retult = mValx == anotherValx ? 0 :(mValx<anotherValx?-1:1);	
		}
		
		return retult;
	}
	
}
