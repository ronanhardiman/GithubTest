package zp.picture;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

/**
 * м╗пе
 * 
 * @author zhouping
 * 
 */
public class HttpClient {
	private final static String TAG = "http";

	public static String sendRequest() {

		return null;
	}

	public static final String GET = "get";
	public static final String POST = "post";

	public static byte[] simpleHttpGet(String url) {
		try {
			Log.d(TAG, url);
			HttpGet request = new HttpGet(url);
			DefaultHttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				return null;
			}
			HttpEntity entity = response.getEntity();
			Header contentEncoding = entity.getContentEncoding();
			InputStream is = entity.getContent();
			if (contentEncoding != null
					&& contentEncoding.getValue().equalsIgnoreCase("gzip")) {
				is = new GZIPInputStream(is);
			}
			ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[4096];
			while (true) {
				int count = is.read(buffer);
				if (count <= 0)
					break;
				swapStream.write(buffer, 0, count);
			}

			is.close();
			return swapStream.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void getOriginalPic(final String url, final int position,
			final Handler handler) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				byte[] bs = simpleHttpGet(url);
				if (bs != null) {
					Bitmap image = BitmapFactory.decodeByteArray(bs, 0,
							bs.length);
					handler.obtainMessage(1, position, 0, image).sendToTarget();
				}

			}
		}).start();
	}

}
