package com.igrs.tivic.phone.Bean;
import android.location.Location;

public class LocationBean {
	private double lat;  //纬度
	private double lon;  //经度
	
	public LocationBean()
	{
		
	}

	public LocationBean(double lat, double lon) {
		super();
		this.lat = lat;
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	
	
	
}
