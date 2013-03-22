package com.igrs.tivic.phone.sqlite;

import com.igrs.tivic.phone.Global.Const;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final int VERSION = 1; 
	/**
	 * 构造函数
	 * @param context
	 * @param name 数据库名称
	 * @param factory
	 * @param version 数据库版本
	 */
	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	public DatabaseHelper(Context context, String name, int version){   
	       this(context,name,null,version);   
	    }   
	  
	public DatabaseHelper(Context context, String name){   
		 this(context,name,VERSION);   
	}   
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "create table" + Const.EPG_TABLE +"(currentDate TEXT PRIMARY KEY,epg_parser TEXT,storeTime TEXT)";
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
