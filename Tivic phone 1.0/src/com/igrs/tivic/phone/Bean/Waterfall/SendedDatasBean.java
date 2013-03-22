package com.igrs.tivic.phone.Bean.Waterfall;

import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * 传递给三级页面的内容
 */
public class SendedDatasBean implements Parcelable {

	public ArrayList<String> articleUrlList;
	public String type;
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override//将类的数据写入外部提供的Parcel中
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(articleUrlList);
		dest.writeString(type);
	}

	//Parcelable.Creator接口，本接口有两个方法 
	public static final Parcelable.Creator<SendedDatasBean> CREATOR = new Parcelable.Creator<SendedDatasBean>() {
		// 重写Creator
		@Override//实现从source中创建出类的实例
		public SendedDatasBean createFromParcel(Parcel source) {
			SendedDatasBean sdb = new SendedDatasBean();
			sdb.articleUrlList = source.readArrayList(ArrayList.class.getClassLoader());
			sdb.type = source.readString();
			return sdb;
		}
		@Override//创建一个类型为T，长度为size的数组。
		public SendedDatasBean[] newArray(int size) {
			return new SendedDatasBean[size];
		}
	};
}
