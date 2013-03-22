package com.igrs.tivic.phone.Utils;

import java.util.concurrent.ExecutorService;

import android.content.Context;

import com.igrs.tivic.phone.widget.GDApplication;
import com.igrs.tivic.phone.widget.ImageCache;

/**
 * Class that provides several utility methods related to GreenDroid.
 * 
 * @author Cyril Mottier
 */
public class GDUtils {

    private GDUtils() {
    }
    private static GDApplication gdApplication;
    /**
     * Return the current {@link GDApplication}
     * 
     * @param context The calling context
     * @return The {@link GDApplication} the given context is linked to.
     */
    public static GDApplication getGDApplication(Context context) {
    	if(gdApplication == null){
    		gdApplication = GDApplication.getInstance();
    	}
        return gdApplication ;
//    	return (GDApplication) context.getApplicationContext();
    }

    /**
     * Return the {@link GDApplication} image cache
     * 
     * @param context The calling context
     * @return The image cache of the current {@link GDApplication}
     */
    public static ImageCache getImageCache(Context context) {
        return getGDApplication(context).getImageCache();
    }

    /**
     * Return the {@link GDApplication} executors pool.
     * 
     * @param context The calling context
     * @return The executors pool of the current {@link GDApplication}
     */
    public static ExecutorService getExecutor(Context context) {
        return getGDApplication(context).getExecutor();
    }

}
