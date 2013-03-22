package com.igrs.tivic.phone.Utils;

import java.math.BigDecimal;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Bean.LocationBean;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationUtils {

	double self_weidu = 0;
	double self_jindu = 0;
	static Context context;
	private static LocationUtils instance = null;
	private static LocationBean locationBean = null;
	private static final double EARTH_RADIUS = 6378137.0;

	public static LocationUtils getInstance(Context context) {
		if (instance == null)
			instance = new LocationUtils(context);
		return instance;

	}

	public static LocationBean getLocationBean() {
		return locationBean;
	}

	/*
	 * 计算两点间的距离（A到B），显示与好友的距离时，请使用此静态接口 
	 * 参数：lat_a lng_a A的经纬度，注意单位是double lat_b
	 * lng_b B的经纬度，注意单位是double 
	 * 返回值： 以米M为单位的double值
	 */

	public static double getDistance(double lat_a, double lng_a, double lat_b,
			double lng_b) {
		double radLat1 = (lat_a * Math.PI / 180.0);
		double radLat2 = (lat_b * Math.PI / 180.0);
		double a = radLat1 - radLat2;
		double b = (lng_a - lng_b) * Math.PI / 180.0;
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round((s * 10000) / 10000);
		return s;
	}

	/*
	 * 计算两点间的距离（from到to），显示与好友的距离时，请使用此静态接口 
	 * 参数：LocationBean from和to的经纬度，注意单位是LocationBean 
	 * 返回值： 字符串（例如：位置未知、1.5km、200m等）
	 */
	public static String getDistance(Context context, LocationBean from, LocationBean to) {
		double lat_a, lng_a, lat_b, lng_b;
		if (from == null || to == null){
			return context.getString(R.string.loc_unknown);
		}

		lat_a = from.getLat();
		lng_a = from.getLon();
		lat_b = to.getLat();
		lng_b = to.getLon();

		double radLat1 = (lat_a * Math.PI / 180.0);
		double radLat2 = (lat_b * Math.PI / 180.0);
		double a = radLat1 - radLat2;
		double b = (lng_a - lng_b) * Math.PI / 180.0;
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round((s * 10000) / 10000);
		
		if (s == 0)
			return String.valueOf("0m");
		if (s > 1000) {
			double km = formatNumber2(s/1000.0);
			if(km > 1000)
				return (">1000km");
			else
				return (String.valueOf(km)+"km");
		}
		return (String.valueOf(s)+"m");
	}

	LocationUtils(Context context) {
		this.context = context;
		init();
	}

	public double getLon() {
		return self_jindu;
	}

	public double getLat() {
		return self_weidu;
	}

	/*
	 * 将double型参数转化为只保留两位小数的double
	 */
	public static double formatNumber2(double pDouble) {
		BigDecimal bd = new BigDecimal(pDouble);
		BigDecimal bd1 = bd.setScale(2, bd.ROUND_HALF_UP);
		pDouble = bd1.doubleValue();
		long ll = Double.doubleToLongBits(pDouble);

		return pDouble;
	}

	public void init() {

		LocationManager locationm = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		String provider = locationm.getBestProvider(criteria, true);
		if (provider != null) {
			Location location = locationm.getLastKnownLocation(provider);
			// 获得上次的记录
			gps_loc(location);
		} else {
			UIUtils.ToastMessage(context,
					"Please open your GPS to get the location!");
		}

		LocationListener GPS_listener = new LocationListener() {
			// 监听位置变化，实时获取位置信息
			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				// 位置发生改变时
				gps_loc(location);
			}
		};
		if (provider != null)
			locationm.requestLocationUpdates(provider, 1000, 0, GPS_listener);
	}

	// 获得自己位置
	private void gps_loc(Location location) {
		if (location != null) {
			self_weidu = location.getLatitude();
			self_jindu = location.getLongitude();
		} else {
			self_weidu = 0;
			self_jindu = 0;
		}
		if (locationBean == null) {
			locationBean = new LocationBean(self_weidu, self_jindu);
		} else {
			locationBean.setLat(self_weidu);
			locationBean.setLon(self_jindu);
		}
	}

}
