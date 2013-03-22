package com.igrs.tivic.phone.Utils;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.MKLocationManager;
import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Global.TivicGlobal;
import android.content.Context;
import android.location.Location;
import android.widget.Toast;

public class LocationProvider {
	private static final String APIKEY="721EE88BB3AC80F6F6A13C212B65F4827C01CBD2";//申请的key
	private BMapManager mBMapManager;
	private MKLocationManager locationManager;
	private static LocationProvider provider;
	private GetLocationListener listener;
	
	public GetLocationListener getListener() {
		return listener;
	}
	public void setListener(GetLocationListener listener) {
		this.listener = listener;
	}
	public BMapManager getmBMapManager() {
		return mBMapManager;
	}
	public void setmBMapManager(BMapManager mBMapManager) {
		this.mBMapManager = mBMapManager;
	}
	private Context context;
	private LocationProvider(Context context) {
		this.context = context;
	}
	public static LocationProvider getInstance(Context context) {
		if(provider==null) {
			provider = new LocationProvider(context);
		}
		return provider;
	}
	private LocationListener myLocationListener  = new LocationListener() {
		
		@Override
		public void onLocationChanged(Location location) {
			if(location!=null) {
				double longitude = location.getLongitude();
				double latitude = location.getLatitude();
				TivicGlobal.lat = latitude;
				TivicGlobal.lon = longitude;
//				System.out.println("经度"+longitude);
//				System.out.println("纬度"+latitude);
			}
//			listener.onLocationGeted(location);
//			SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
//			Editor editor = sp.edit();
//			editor.putString("location", longitude+"-"+latitude);
//			editor.commit();

			
		}
	};
	public void init() {
		mBMapManager = new BMapManager(context);
        mBMapManager.init(APIKEY, new MKGeneralListener() {
		
			public void onGetPermissionState(int state) {
				if(state==300) {
					Toast.makeText(context, R.string.key_no_use, Toast.LENGTH_LONG).show();
				}
			}
			
			public void onGetNetworkState(int arg0) {
				
			}
		});
        locationManager = mBMapManager.getLocationManager();
	}
	public void getLocation() {
		
		if(locationManager.enableProvider(MKLocationManager.MK_GPS_PROVIDER)) {
			locationManager.enableProvider(MKLocationManager.MK_GPS_PROVIDER);
		} else if(locationManager.enableProvider(MKLocationManager.MK_NETWORK_PROVIDER)) {
			locationManager.enableProvider(MKLocationManager.MK_NETWORK_PROVIDER);
		}
		locationManager.requestLocationUpdates(myLocationListener);
	}
	public interface GetLocationListener{
		public void  onLocationGeted(Location location);
	}
}
