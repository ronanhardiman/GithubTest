package com.igrs.tivic.phone.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Bean.Login.UserRegisterBean;
import com.igrs.tivic.phone.Global.Const;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Global.URLConfig;
import com.igrs.tivic.phone.widget.AsyncImageView;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory.Options;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.text.TextUtils;

/**
 * COPYRIGHT NOTICE Copyright (c) 2009～2012, IGRSlab All rights reserved.
 * 
 * @author wjf
 * @file Utils.java
 * @brief TODO
 * @version
 * @date 2012-5-10
 * 
 */
public class Utils {
	private static SimpleDateFormat df_yyyy_MM_dd_HH_mm_ss = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat df_MM_dd_HH_mm_ss = new SimpleDateFormat(
			"MM-dd HH:mm:ss");
	private static SimpleDateFormat df_MM_dd_HH_mm = new SimpleDateFormat(
			"MM-dd HH:mm");
	private static SimpleDateFormat df_HH_mm_ss = new SimpleDateFormat(
			"HH:mm:ss");
	private static SimpleDateFormat df_HH_mm = new SimpleDateFormat("HH:mm");

	/**
	 * 判断是否连接到互联网
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnected(Context context) {
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo network = connectivityManager.getActiveNetworkInfo();
			if (connectivityManager != null) {
				if (network != null && network.isConnected()) {
					if (network.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * 
	 * @return
	 */
	public static long getCurrentDateYMDUNIX() {
		Date date = new Date();
		return date.getTime();
	}

	/**
	 * 
	 * @param date
	 *            日期的格式：2012-05-10
	 * @return
	 */
	public static long getCurrentDateYMDUNIX(String date) {
		try {
			SimpleDateFormat sdf_yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");
			Date d = sdf_yyyy_MM_dd.parse(date);
			return d.getTime();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取当前日期的格式 格式：2012-05-10
	 * 
	 * @return
	 */
	public static String getCurrentDateYMD() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(new Date());
	}

	/**
	 * 转化日期格式
	 * 
	 * @param programDate
	 *            日期格式：yyyy-MM-dd HH:mm:ss
	 * @return 返回日期格式：MM-dd HH:mm:ss
	 */
	public static String getProgramDateMDHMS(String programDate) {
		try {
			Date date = df_yyyy_MM_dd_HH_mm_ss.parse(programDate);
			return df_MM_dd_HH_mm_ss.format(date);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return programDate;
	}

	/**
	 * 转化日期格式
	 * 
	 * @param programDate
	 *            日期格式：yyyy-MM-dd HH:mm:ss
	 * @return 返回日期格式：MM-dd HH:mm
	 */
	public static String getProgramDateMDHM(String programDate) {
		try {
			Date date = df_yyyy_MM_dd_HH_mm_ss.parse(programDate);
			return df_MM_dd_HH_mm.format(date);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return programDate;
	}

	/**
	 * 转化日期格式
	 * 
	 * @param programDate
	 *            日期格式 yyyy-MM-dd HH:mm:ss
	 * @return 返回日期格式： HH::mm:ss
	 */
	public static String getProgramDateHMS(String programDate) {
		try {
			Date date = df_yyyy_MM_dd_HH_mm_ss.parse(programDate);
			return df_HH_mm_ss.format(date);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return programDate;
	}

	/**
	 * 转化日期格式
	 * 
	 * @param programDate
	 *            日期格式 yyyy-MM-dd HH:mm:ss
	 * @return 返回日期格式： HH::mm
	 */
	public static String getProgramDateHM(String programDate) {
		try {
			Date date = df_yyyy_MM_dd_HH_mm_ss.parse(programDate);
			return df_HH_mm.format(date);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return programDate;
	}

	/**
	 * 当前日期是周几
	 * 
	 * @return 从周一到周日 值为1---7
	 * @throws Exception
	 */
	public static int dayForWeek() {
		Calendar c = Calendar.getInstance();
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return dayForWeek;
	}

	/**
	 * 获取当前是几点（只是 “时”）
	 * 
	 * @return
	 */
	public static int getHour() {
		Calendar c1 = Calendar.getInstance();
		return c1.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获取从今天开始，往后七天的日期数组
	 * 
	 * @return
	 */
	public static String[] getFromDateToWeek() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String[] arr = new String[7];
		for (int i = 0; i < arr.length; i++) {
			Date date = new Date();
			long myTime = (date.getTime() / 1000) + 60 * 60 * 24 * i;
			date.setTime(myTime * 1000);
			arr[i] = simpleDateFormat.format(date);
		}
		return arr;
	}

	/**
	 * 获取从今天开始，往前或往后后days天的日期
	 * 
	 * @return
	 */
	public static String getFromDateToWeek(int days) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		long myTime = (date.getTime() / 1000) + 60 * 60 * 24 * days;
		date.setTime(myTime * 1000);
		return simpleDateFormat.format(date);
	}

	/**
	 * 获取当前日期的格式 格式：2012-05-10 09:04:55
	 * 
	 * @return
	 */
	public static String getCurrentDateYMDHMS() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.format(new Date());
	}

	/**
	 * 获取当前时间的格式 格式：09:04:55
	 * 
	 * @return
	 */
	public static String getCurrentTimeHMS() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		return simpleDateFormat.format(new Date());
	}

	/**
	 * 当前的时间,是第多少分钟
	 * 
	 * @return
	 */
	public static int getCurrentMinutes() {
		Date date = new Date();
		return date.getHours() * 60 + date.getMinutes();
	}

	/**
	 * 判断当前输入性别是否合法 male/female
	 * 
	 * @return
	 */
	public static boolean isGender(String sex) {
		if (sex.equalsIgnoreCase("male") || sex.equalsIgnoreCase("female"))
			return true;
		else
			return false;
	}

	/**
	 * 判断当前输入的用户名是否合法，判断当前输入的是否为用户名
	 * 
	 * @return
	 */
	public static boolean isUsrNameValid(String str) {
		Pattern p = Pattern.compile("^[A-Za-z][A-Za-z0-9_]{4,16}[A-Za-z0-9]$");
		Matcher m = p.matcher(str);
		boolean isValid = m.matches();
		return isValid;
	}

	/**
	 * 判断当前输入的邮箱是否合法，判断当前输入的是否为邮箱
	 * 
	 * @return
	 */
	public static boolean isEmailValid(String str) {
		Pattern p = Pattern.compile("\\w+@(\\w+\\.)+[a-z]{2,3}");
		Matcher m = p.matcher(str);
		boolean isValid = m.matches();
		return isValid;
	}

	/**
	 * 判断当前输入的手机号是否合法，判断当前输入的是否为手机号
	 * 
	 * @return
	 */
	public static boolean isPhoneNumValid(String str) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(str);
		boolean isValid = m.matches();
		return isValid;
	}

	/**
	 * 判断当前返回值uid是否合法
	 * 
	 * @return
	 */
	public static boolean isUIDValid(String uid) {
		if (uid != null && uid.length() > 10)
			return true;
		else
			return false;
	}

	/**
	 * 判断密码password是否合法 6-15位数字或字母都可
	 * 
	 * @return
	 */
	public static boolean isPasswordValid(String password) {
		if (password != null && password.length() >= 6
				&& password.length() < 15)
			return true;
		else
			return false;
	}

	/**
	 * 判断当前Url是否标准的content://样式，如果不是，则返回绝对路径
	 * 
	 * @param uri
	 * @return
	 */
	public static String getAbsolutePathFromNoStandardUri(Uri mUri) {
		String filePath = null;

		String mUriString = mUri.toString();
		mUriString = Uri.decode(mUriString);

		String pre1 = "file://" + Const.SDCARD + File.separator;
		String pre2 = "file://" + Const.SDCARD_MNT + File.separator;

		if (mUriString.startsWith(pre1)) {
			filePath = Environment.getExternalStorageDirectory().getPath()
					+ File.separator + mUriString.substring(pre1.length());
		} else if (mUriString.startsWith(pre2)) {
			filePath = Environment.getExternalStorageDirectory().getPath()
					+ File.separator + mUriString.substring(pre2.length());
		}
		return filePath;
	}

	/**
	 * 通过uri获取文件的绝对路径
	 * 
	 * @param uri
	 * @return
	 */
	public static String getAbsoluteImagePath(Activity context, Uri uri) {
		String imagePath = "";
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.managedQuery(uri, proj, // Which columns to
														// return
				null, // WHERE clause; which rows to return (all rows)
				null, // WHERE clause selection arguments (none)
				null); // Order-by clause (ascending by name)

		if (cursor != null) {
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			if (cursor.getCount() > 0 && cursor.moveToFirst()) {
				imagePath = cursor.getString(column_index);
			}
		}

		return imagePath;
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileFormat(String fileName) {
		if (ModelUtil.isEmpty(fileName))
			return "";

		int point = fileName.lastIndexOf('.');
		return fileName.substring(point + 1);
	}

	/**
	 * 根据文件绝对路径获取文件名
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileName(String filePath) {
		if (ModelUtil.isEmpty(filePath))
			return "";
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
	}

	/**
	 * 获取图片缩略图 只有Android2.1以上版本支持
	 * 
	 * @param imgName
	 * @param kind
	 *            MediaStore.Images.Thumbnails.MICRO_KIND
	 * @return
	 */
	public static Bitmap loadImgThumbnail(Activity context, String imgName,
			int kind) {
		Bitmap bitmap = null;

		String[] proj = { MediaStore.Images.Media._ID,
				MediaStore.Images.Media.DISPLAY_NAME };

		Cursor cursor = context.managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, proj,
				MediaStore.Images.Media.DISPLAY_NAME + "='" + imgName + "'",
				null, null);

		if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
			ContentResolver crThumb = context.getContentResolver();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1;
			bitmap = MediaStore.Images.Thumbnails.getThumbnail(crThumb,
					cursor.getInt(0), kind, options);
		}
		return bitmap;
	}

	public static Bitmap loadImgThumbnail(String filePath, int w, int h) {
		Bitmap bitmap = getBitmapByPath(filePath);
		return zoomBitmap(bitmap, w, h);
	}

	/**
	 * 获取bitmap
	 * 
	 * @param filePath
	 * @return
	 */
	public static Bitmap getBitmapByPath(String filePath) {
		return getBitmapByPath(filePath, null);
	}

	/**
	 * 创建缩略图
	 * 
	 * @param context
	 * @param largeImagePath
	 *            原始大图路径
	 * @param thumbfilePath
	 *            输出缩略图路径
	 * @param square_size
	 *            输出图片宽度
	 * @param quality
	 *            输出图片质量
	 * @throws IOException
	 */
	public static void createImageThumbnail(Context context,
			String largeImagePath, String thumbfilePath, int square_size,
			int quality) throws IOException {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inSampleSize = 1;
		// 原始图片bitmap
		Bitmap cur_bitmap = getBitmapByPath(largeImagePath, opts);

		if (cur_bitmap == null)
			return;

		// 原始图片的高宽
		int[] cur_img_size = new int[] { cur_bitmap.getWidth(),
				cur_bitmap.getHeight() };
		// 计算原始图片缩放后的宽高
		int[] new_img_size = scaleImageSize(cur_img_size, square_size);
		// 生成缩放后的bitmap
		Bitmap thb_bitmap = zoomBitmap(cur_bitmap, new_img_size[0],
				new_img_size[1]);
		// 生成缩放后的图片文件
		saveImageToSD(thumbfilePath, thb_bitmap, quality);
	}

	/**
	 * 写图片文件到SD卡
	 * 
	 * @throws IOException
	 */
	public static void saveImageToSD(String filePath, Bitmap bitmap, int quality)
			throws IOException {
		if (bitmap != null) {
			FileOutputStream fos = new FileOutputStream(filePath);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.JPEG, quality, stream);
			byte[] bytes = stream.toByteArray();
			fos.write(bytes);
			fos.close();
		}
	}

	/**
	 * 放大缩小图片
	 * 
	 * @param bitmap
	 * @param w
	 * @param h
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		Bitmap newbmp = null;
		if (bitmap != null) {
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			Matrix matrix = new Matrix();
			float scaleWidht = ((float) w / width);
			float scaleHeight = ((float) h / height);
			matrix.postScale(scaleWidht, scaleHeight);
			newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
					true);
		}
		return newbmp;
	}

	/**
	 * 计算缩放图片的宽高
	 * 
	 * @param img_size
	 * @param square_size
	 * @return
	 */
	public static int[] scaleImageSize(int[] img_size, int square_size) {
		if (img_size[0] <= square_size && img_size[1] <= square_size)
			return img_size;
		double ratio = square_size
				/ (double) Math.max(img_size[0], img_size[1]);
		return new int[] { (int) (img_size[0] * ratio),
				(int) (img_size[1] * ratio) };
	}

	private static Bitmap getBitmapByPath(String filePath, Options opts) {
		FileInputStream fis = null;
		Bitmap bitmap = null;
		try {
			File file = new File(filePath);
			fis = new FileInputStream(file);
			bitmap = BitmapFactory.decodeStream(fis, null, opts);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return bitmap;
	}

	/**
	 * 判断日期格式是否正确
	 */
	public static boolean isDateValid(String str) {
		Pattern p = Pattern
				.compile("\\d{4}-(((0[1-9])|(1[0-2])))(-((0[1-9])|([1-2][0-9])|(3[0-1])))?");
		Matcher m = p.matcher(str);
		boolean isValid = m.matches();
		return isValid;
	}

	/**
	 * 根据日期获取星座
	 */
	public static String getConstellation(Integer month, Integer day,String all,String zuo) {
//		String s = "魔羯水瓶双鱼白羊金牛双子巨蟹狮子处女天秤天蝎射手魔羯";
		Integer[] arr = { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };
		Integer num = month * 2 - (day < arr[month - 1] ? 2 : 0);
		return all.substring(num, num + 2) + zuo;
	}

	/**
	 * 根据出生日期计算年龄
	 */
	public static int getAge(Calendar birthDay) {
		Calendar cal = Calendar.getInstance();

		if (cal.before(birthDay)) {
			return -1;
		}

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(birthDay.getTime());

		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				// monthNow==monthBirth
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				} else {
					// do nothing
				}
			} else {
				// monthNow>monthBirth
				age--;
			}
		} else {
			// monthNow<monthBirth
			// donothing
		}

		return age;
	}

	public static int getFormatDayOrMonth(String str) {
		if (str.charAt(0) == '0') {
			return Integer.parseInt(str.charAt(1) + "");
		}
		return Integer.parseInt(str);
	}

	/**
	 * 获取设备 ID
	 */
	public static String getAndroidDeviceID(Context context) {
		return Secure
				.getString(context.getContentResolver(), Secure.ANDROID_ID);
	}

	/***
	 * String转成calendar
	 */
	public static Calendar getCalendar(String _birthday) {
		String birthday = _birthday;
		if (birthday == null || birthday.length() != 10) {
			birthday = "1985-01-01";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		try {
			Date date = sdf.parse(birthday);
			cal.setTime(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cal;
	}

	/**
	 * 时间戳转化为 : yyyy-MM-dd HH:mm:ss
	 * 
	 * @param time
	 * @return
	 */
	public static String getDate(String time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		String d = format.format(Long.parseLong(time));
		return d;
	}

	public static Date getDate2(String time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(Long.valueOf(time));
		Date d = cal.getTime();
		return d;
	}

	// 将字符串时间转化为秒数(yyyyMMddHHmmss)
	public static Long unixTimestamp2String(String srcTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date result_date;
		long result_time = 0;
		try {
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
			result_date = sdf.parse(srcTime + " 00:00:00");
			// 返回的是毫秒数故除以1000
			result_time = result_date.getTime() / 1000;
		} catch (Exception e) {
		}
		return result_time;
	}

	// 秒数(yyyyMMddHHmmss) 转换为字符串
	public static String string2unixTimestamp(long utime) {
		Date date = new Date(utime * 1000);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		return sdf.format(date);
	}

	public static String formatToTime(String time) {
		String datetime = string2unixTimestamp(Long.parseLong(time));
		String timenew = datetime
				.substring(new String("yyyy-MM-dd ").length() - 1);
		return timenew;
	}

	public static String formatToDate(String time) {
		String datetime = string2unixTimestamp(Long.parseLong(time));
		String datenew = datetime.substring(0,
				new String("yyyy-MM-dd ").length() - 1);
		return datenew;
	}

	public static String formatToDateTime(String time) {
		String datetime = string2unixTimestamp(Long.parseLong(time));
		return datetime;
	}

	// String -> 星期四 12:22
	public static String formatToWeek(String time) {
		Date date = new Date(Long.parseLong(time) * 1000);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat myFmt3 = new SimpleDateFormat("MM-dd HH:mm");
		// SimpleDateFormat myFmt3=new SimpleDateFormat("MM-dd E  HH:mm");
		myFmt3.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		return myFmt3.format(date);
	}

	/*
	 * 清除缓存 cache为getCacheDir()
	 */
	public static void deleteFile(File cache) {
		for (File file : cache.listFiles()) {
			if (file.isFile()) {
				file.delete();
			} else {
				deleteFile(file);
				file.delete();
			}
		}
	}

	/*
	 * 清除字符串里的空格
	 */
	public static String getClearEmpty(String str) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			if (!(str.charAt(i) + "").equals(" ")) {
				sb.append(str.charAt(i) + "");
			}
		}
		return sb.toString();
	}

	/**
	 * 获取台标资源id
	 * 
	 * @param context
	 * @param channel_name
	 *            频道名称
	 * @return
	 */
	public static int setChannelIcon(Context context, String channel_name) {
		if (channel_name == null || channel_name.isEmpty())
			return 0;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.lvyou)))
			return R.drawable.big_channel_icon_16_lvyou;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.liaoning)))
			return R.drawable.big_channel_icon_19_liaoning;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.henan)))
			return R.drawable.big_channel_icon_21_henan;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.anhui)))
			return R.drawable.big_channel_icon_25_anhui;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.jiangsu)))
			return R.drawable.big_channel_icon_27_jiangsu;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.hunan)))
			return R.drawable.big_channel_icon_29_hunan;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.guangxi)))
			return R.drawable.big_channel_icon_33_guangxi;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.beijing)))
			return R.drawable.beijing_big;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.dongfang)))
			return R.drawable.big_channel_icon_35_dongfang;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.dongnan)))
			return R.drawable.big_channel_icon_36_dongnan;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.xizangyitao)))
			return R.drawable.big_channel_icon_39_xizang;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.heilongjiang)))
			return R.drawable.big_channel_icon_17_heilongjiang;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.jilin)))
			return R.drawable.big_channel_icon_18_jilin;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.hebei)))
			return R.drawable.big_channel_icon_20_hebei;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.shanxi)))
			return R.drawable.big_channel_icon_23_shanxi;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.shanxi3)))
			return R.drawable.big_channel_icon_22_shanxi;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.shandong)))
			return R.drawable.big_channel_icon_24_shandong;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.zhejiang)))
			return R.drawable.big_channel_icon_26_zhejiang;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.jiangxi)))
			return R.drawable.big_channel_icon_28_jiangxi;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.hubei)))
			return R.drawable.big_channel_icon_30_hubei;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.guizhou)))
			return R.drawable.big_channel_icon_31_guizhou;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.yunnan)))
			return R.drawable.big_channel_icon_32_yunnan;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.gansu)))
			return R.drawable.big_channel_icon_37_gansu;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.ningxia)))
			return R.drawable.big_channel_icon_38_ningxia;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.xinjiang)))
			return R.drawable.big_channel_icon_40_xinjiang;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.neimenggu)))
			return R.drawable.big_channel_icon_41_neimenggu;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.tianjin)))
			return R.drawable.big_channel_icon_42_tianjin;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.sichuan)))
			return R.drawable.big_channel_icon_43_sichuan;

		// cctv & btv channel
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.cctv_1)))
			return R.drawable.all_channel_icon_1_title;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.cctv_2)))
			return R.drawable.all_channel_icon_2_title;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.cctv_3)))
			return R.drawable.all_channel_icon_3_title;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.cctv_4)))
			return R.drawable.all_channel_icon_4_title;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.cctv_5)))
			return R.drawable.all_channel_icon_5_title;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.cctv_6)))
			return R.drawable.all_channel_icon_6_title;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.cctv_7)))
			return R.drawable.all_channel_icon_7_title;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.cctv_8)))
			return R.drawable.all_channel_icon_8_title;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.cctv_9)))
			return R.drawable.all_channel_icon_9_title;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.cctv_10)))
			return R.drawable.all_channel_icon_10_title;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.cctv_11)))
			return R.drawable.all_channel_icon_11_title;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.cctv_12)))
			return R.drawable.all_channel_icon_12_title;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.cctv_13)))
			return R.drawable.all_channel_icon_13_title;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.cctv_14)))
			return R.drawable.all_channel_icon_14_title;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.cctv_15)))
			return R.drawable.all_channel_icon_15_title;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.BTV_wenyi)))
			return R.drawable.all_channel_icon_45_btvwenyi;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.BTV_caijing)))
			return R.drawable.all_channel_icon_48_btvcaijing;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.BTV_kejiao)))
			return R.drawable.all_channel_icon_46_btvkejiao;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.BTV_shenghuo)))
			return R.drawable.all_channel_icon_50_btvtishenghuo;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.BTV_tiyu)))
			return R.drawable.all_channel_icon_49_btvtiyu;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.BTV_yingshi)))
			return R.drawable.all_channel_icon_47_btvyingshi;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.chongqing)))
			return R.drawable.all_channel_icon_44_chongqing;

		return R.drawable.beijing_big;
	}

	/**
	 * 获取台标资源id--small台标
	 * 
	 * @param context
	 * @param channel_name
	 *            频道名称
	 * @return
	 */
	public static int setChannelIconSmall(Context context, String channel_name) {
		if (channel_name == null || channel_name.isEmpty())
			return 0;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.lvyou)))
			return R.drawable.small_channel_icon_16_lvyou;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.liaoning)))
			return R.drawable.small_channel_icon_19_liaoning;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.henan)))
			return R.drawable.small_channel_icon_21_henan;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.anhui)))
			return R.drawable.small_channel_icon_25_anhui;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.jiangsu)))
			return R.drawable.small_channel_icon_27_jiangsu;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.hunan)))
			return R.drawable.small_channel_icon_29_hunan;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.guangxi)))
			return R.drawable.small_channel_icon_33_guangxi;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.beijing)))
			return R.drawable.beijing_big;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.dongfang)))
			return R.drawable.small_channel_icon_35_dongfang;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.dongnan)))
			return R.drawable.small_channel_icon_36_dongnan;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.xizangyitao)))
			return R.drawable.small_channel_icon_39_xizang;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.heilongjiang)))
			return R.drawable.small_channel_icon_17_heilongjiang;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.jilin)))
			return R.drawable.small_channel_icon_18_jilin;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.hebei)))
			return R.drawable.small_channel_icon_20_hebei;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.shanxi)))
			return R.drawable.small_channel_icon_23_shanxi;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.shanxi3)))
			return R.drawable.small_channel_icon_22_shanxi;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.shandong)))
			return R.drawable.small_channel_icon_24_shandong;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.zhejiang)))
			return R.drawable.small_channel_icon_26_zhejiang;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.jiangxi)))
			return R.drawable.small_channel_icon_28_jiangxi;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.hubei)))
			return R.drawable.small_channel_icon_30_hubei;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.guizhou)))
			return R.drawable.small_channel_icon_31_guizhou;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.yunnan)))
			return R.drawable.small_channel_icon_32_yunnan;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.gansu)))
			return R.drawable.small_channel_icon_37_gansu;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.ningxia)))
			return R.drawable.small_channel_icon_38_ningxia;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.xinjiang)))
			return R.drawable.small_channel_icon_40_xinjiang;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.neimenggu)))
			return R.drawable.small_channel_icon_41_neimenggu;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.tianjin)))
			return R.drawable.small_channel_icon_42_tianjin;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.sichuan)))
			return R.drawable.small_channel_icon_43_sichuan;

		// cctv & btv channel
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.cctv_1)))
			return R.drawable.all_channel_icon_1_title;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.cctv_2)))
			return R.drawable.all_channel_icon_2_title;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.cctv_3)))
			return R.drawable.all_channel_icon_3_title;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.cctv_4)))
			return R.drawable.all_channel_icon_4_title;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.cctv_5)))
			return R.drawable.all_channel_icon_5_title;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.cctv_6)))
			return R.drawable.all_channel_icon_6_title;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.cctv_7)))
			return R.drawable.all_channel_icon_7_title;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.cctv_8)))
			return R.drawable.all_channel_icon_8_title;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.cctv_9)))
			return R.drawable.all_channel_icon_9_title;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.cctv_10)))
			return R.drawable.all_channel_icon_10_title;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.cctv_11)))
			return R.drawable.all_channel_icon_11_title;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.cctv_12)))
			return R.drawable.all_channel_icon_12_title;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.cctv_13)))
			return R.drawable.all_channel_icon_13_title;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.cctv_14)))
			return R.drawable.all_channel_icon_14_title;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.cctv_15)))
			return R.drawable.all_channel_icon_15_title;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.BTV_wenyi)))
			return R.drawable.all_channel_icon_45_btvwenyi;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.BTV_caijing)))
			return R.drawable.all_channel_icon_48_btvcaijing;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.BTV_kejiao)))
			return R.drawable.all_channel_icon_46_btvkejiao;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.BTV_shenghuo)))
			return R.drawable.all_channel_icon_50_btvtishenghuo;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.BTV_tiyu)))
			return R.drawable.all_channel_icon_49_btvtiyu;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.BTV_yingshi)))
			return R.drawable.all_channel_icon_47_btvyingshi;
		if (channel_name.trim().equals(
				context.getResources().getString(R.string.chongqing)))
			return R.drawable.all_channel_icon_44_chongqing;

		return R.drawable.beijing_big;
	}

	/**
	 * 获取台标资源id
	 * 
	 * @param context
	 * @param channel_name
	 *            频道名称
	 * @return
	 */
	public static String getChannelNameById(Context context, int id) {
		if (id == 0)
			return null;
		switch (id) {
		case (16):
			return context.getResources().getString(R.string.lvyou);
		case (19):
			return context.getResources().getString(R.string.liaoning);
		case (21):
			return context.getResources().getString(R.string.henan);
		case (25):
			return context.getResources().getString(R.string.anhui);
		case (27):
			return context.getResources().getString(R.string.jiangsu);
		case (29):
			return context.getResources().getString(R.string.hunan);
		case (33):
			return context.getResources().getString(R.string.guangxi);
		case (34):
			return context.getResources().getString(R.string.beijing);
		case (35):
			return context.getResources().getString(R.string.dongfang);
		case (36):
			return context.getResources().getString(R.string.dongnan);
		case (39):
			return context.getResources().getString(R.string.xizangyitao);
		case (17):
			return context.getResources().getString(R.string.heilongjiang);
		case (18):
			return context.getResources().getString(R.string.jilin);
		case (20):
			return context.getResources().getString(R.string.hebei);
		case (22):
			return context.getResources().getString(R.string.shanxi);
		case (23):
			return context.getResources().getString(R.string.shanxi3);
		case (24):
			return context.getResources().getString(R.string.shandong);
		case (26):
			return context.getResources().getString(R.string.zhejiang);
		case (28):
			return context.getResources().getString(R.string.jiangxi);
		case (30):
			return context.getResources().getString(R.string.hubei);
		case (31):
			return context.getResources().getString(R.string.guizhou);
		case (32):
			return context.getResources().getString(R.string.yunnan);
		case (37):
			return context.getResources().getString(R.string.gansu);
		case (38):
			return context.getResources().getString(R.string.ningxia);
		case (40):
			return context.getResources().getString(R.string.xinjiang);
		case (41):
			return context.getResources().getString(R.string.neimenggu);
		case (42):
			return context.getResources().getString(R.string.tianjin);
		case (43):
			return context.getResources().getString(R.string.sichuan);

			// cctv & btv channel
		case (1):
			return context.getResources().getString(R.string.cctv_1);
		case (2):
			return context.getResources().getString(R.string.cctv_2);
		case (3):
			return context.getResources().getString(R.string.cctv_3);
		case (4):
			return context.getResources().getString(R.string.cctv_4);
		case (5):
			return context.getResources().getString(R.string.cctv_5);
		case (6):
			return context.getResources().getString(R.string.cctv_6);
		case (7):
			return context.getResources().getString(R.string.cctv_7);
		case (8):
			return context.getResources().getString(R.string.cctv_8);
		case (9):
			return context.getResources().getString(R.string.cctv_9);
		case (10):
			return context.getResources().getString(R.string.cctv_10);
		case (11):
			return context.getResources().getString(R.string.cctv_11);
		case (12):
			return context.getResources().getString(R.string.cctv_12);
		case (13):
			return context.getResources().getString(R.string.cctv_13);
		case (14):
			return context.getResources().getString(R.string.cctv_14);
		case (15):
			return context.getResources().getString(R.string.cctv_15);
		case (45):
			return context.getResources().getString(R.string.BTV_wenyi);
		case (48):
			return context.getResources().getString(R.string.BTV_caijing);
		case (46):
			return context.getResources().getString(R.string.BTV_kejiao);
		case (50):
			return context.getResources().getString(R.string.BTV_shenghuo);
		case (49):
			return context.getResources().getString(R.string.BTV_tiyu);
		case (47):
			return context.getResources().getString(R.string.BTV_yingshi);
		case (44):
			return context.getResources().getString(R.string.chongqing);
		}
		return null;
	}

	public static boolean isJsonValid(String json) {
		if (json == null || json.isEmpty() || json.equals("205")
				|| json.startsWith("<"))
			return false;
		else
			return true;
	}

	public static void initAvater(final AsyncImageView aiv) {//处理头像的显示
		UserRegisterBean userbean = TivicGlobal.getInstance().userRegisterBean;
		String userAvaterPath = userbean.getUserAvartarPath();
		if (userbean.isModifyUserAvatar()
				&& userbean.isSuccessModifyUserAvater()
				&& !TextUtils.isEmpty(userAvaterPath)) {
			Bitmap bm = BitmapFactory.decodeFile(userAvaterPath);
			aiv.setImageBitmap(bm);
//			UIUtils.Logi("chen", "avater from SD");
		} else {
			if (userbean.getUserAvatar() != null
					&& !TextUtils.isEmpty(userbean.getUserAvatar())) {
				if (userbean.getUserAvatar().contains(".jpg")) {// 初始化用户头像
					final String imagePath = URLConfig.avarter_path
							+ userbean.getUserAvatar() + "!w128";
//					aiv.setUrl(imagePath);
					new AsyncTask<Void, Void, Bitmap>() {
						@Override
						protected Bitmap doInBackground(Void... params) {
							Bitmap bitmap = getImageFromNet(imagePath);
							return bitmap;
						}

						protected void onPostExecute(Bitmap result) {
							if(result!=null) {
								aiv.setImageBitmap(result);
							} else {
								aiv.setDefaultImageResource(R.drawable.base_default_avater);
							}
//							UIUtils.Logi("chen", "avater from NET");
						};
					}.execute();
				}
			}
		}
	}
	public static Bitmap getImageFromNet(String path) {
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(6 * 1000);
			conn.setRequestMethod("GET");
			conn.addRequestProperty("Cache-Control", "no-cache");
			if (conn.getResponseCode() == 200) {
				InputStream is = conn.getInputStream();
				Bitmap bitmap = BitmapFactory.decodeStream(is);
				return bitmap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	//tvfind video 来源 图标
	public static final int[] video_image_id = new int[]{R.drawable.video_youku,R.drawable.video_wole,R.drawable.video_aipai,R.drawable.video_baidushipin,
		R.drawable.video_baofengyingyin,R.drawable.video_cntv,R.drawable.video_fenghuang,
		R.drawable.video_xunleikankan,R.drawable.video_fengxing,R.drawable.video_jidong,R.drawable.video_ku6,
		R.drawable.video_leshiwang,R.drawable.video_mangguotv,R.drawable.video_pps,R.drawable.video_pptv,
		R.drawable.video_aiqiyi,R.drawable.video_xinlangshipin,R.drawable.video_souhushipin,R.drawable.video_tengxunshipin,
		R.drawable.video_tudou,R.drawable.video_youshi};
	
	/**
	 * 获取video 来源的 (优酷)
	 * @param id
	 * @return
	 */
	public static int getVideoNameId(int id){
		int [] sources = new int[]{R.string.youku_0,R.string.wo_1,R.string.aipai_2,R.string.badu_3,R.string.baofeng_4,R.string.CNTV_5,R.string.fenghuang_6,
				R.string.xunlei_7,R.string.fengxing_8,R.string.jidong_9,R.string.ku6_10,R.string.leshi_11,R.string.mangguo_12,R.string.pps_13,R.string.pptv_14,
				R.string.qiyi_15,R.string.sinavideo_16,R.string.sohuvideo_17,R.string.qqvideo_18,R.string.tudou_19,R.string.youshi_20,R.string.other_21
			};
		return sources[id];
	}
}
