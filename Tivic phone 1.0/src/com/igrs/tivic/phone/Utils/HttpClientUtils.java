package com.igrs.tivic.phone.Utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.HttpResponseCache;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.igrs.tivic.phone.Bean.Base.BaseParamBean;
import com.igrs.tivic.phone.Global.AppContext;
import com.igrs.tivic.phone.Global.Const;

public class HttpClientUtils {
	private static HttpClientUtils instance;
	private String TAG = "HttpClientUtils";
	DefaultHttpClient httpClient;

	public static HttpClientUtils getInstance() {
		if (instance == null) {
			instance = new HttpClientUtils();
		}
		return instance;
	}

	private HttpClientUtils() {
		httpClient = new DefaultHttpClient();
	}

	public void exit() {
		instance = null;
		httpClient = null;
	}

	public String makeJsonRequest(BaseParamBean bean) {
		// TODO
		return null;
	}

	// public void updateData(String urlString)
	// {
	// HttpURLConnection urlConnection = null;
	//
	// try {
	// URL url = new URL(urlString);
	// urlConnection = (HttpURLConnection) url.openConnection();
	// long currentTime =System.currentTimeMillis();
	// long expires = urlConnection.getHeaderFieldDate("Expires",currentTime);
	// long lastModified = urlConnection.getHeaderFieldDate("Last-Modified",
	// currentTime);
	// //setDataExpirationDate(expires);
	// if(lastModified < lastUpdateTime)
	// {
	// // Skip update
	//
	// }else{
	// // Parse update
	//
	// lastUpdateTime = lastModified;
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	//
	// if (urlConnection != null)
	// urlConnection.disconnect();
	// }
	//
	// }

	public void createReponseCache(Context context) {
		try {
			File httpCacheDir = new File(context.getCacheDir(), "http");
			long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
			HttpResponseCache.install(httpCacheDir, httpCacheSize);
		} catch (IOException e) {
			Log.i(TAG, "HTTP response cache installation failed:" + e);
		}
	}

	public void clearResponseCache() {
		HttpResponseCache cache = HttpResponseCache.getInstalled();
		if (cache != null) {
			cache.flush();
		}
	}

	// CMS get 获取服务器数据 可设置缓存
/*	public String requestGetGZIP(String urlString, String charset) {
		HttpURLConnection urlConnection = null;
		InputStream is = null;
		BufferedReader br = null;
		ByteArrayOutputStream outStream = null;
		StringBuffer sb = new StringBuffer();
		GZIPInputStream gzin = null;
		InputStreamReader isr = null;
		try {
			URL url = new URL(urlString);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setConnectTimeout(Const.SOCKET_CONNECTION_TIMEOUT);
			urlConnection.setReadTimeout(Const.SOCKET_CONNECTION_TIMEOUT);
			urlConnection.setRequestMethod("GET");
			urlConnection
			.setRequestProperty("Accept-Encoding", "gzip, deflate");
			setRequestProperty(urlString, urlConnection);
			// cache the data
			// urlConnection.addRequestProperty("Cache-Control",
			// "only-if-cached");
			// urlConnection.addRequestProperty("Cache-Control", "no-cache");
			urlConnection.addRequestProperty("Cache-Control", "max-age=0");
			is = urlConnection.getInputStream();
			gzin = new GZIPInputStream(is);
			isr = new InputStreamReader(gzin, charset); // 设置读取流的编码格式，自定义
			br = new BufferedReader(isr);
			String s = null;
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
				if (br != null)
					br.close();
				if (outStream != null)
					outStream.close();
				if (isr != null)
					isr.close();
				if (gzin != null)
					gzin.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (urlConnection != null)
				urlConnection.disconnect();
		}
		return sb.toString();
	}*/

	// CMS get 获取服务器数据 可设置缓存
	public String requestGet(String urlString, String charset) {
		HttpURLConnection urlConnection = null;
		InputStream is = null;
		BufferedReader br = null;
		ByteArrayOutputStream outStream = null;
		StringBuffer sb = new StringBuffer();
		GZIPInputStream gzin = null;
		InputStreamReader isr = null;
		
		if(!AppContext.instance.isNetworkConnected()){
			sb.append(Const.SOCKET_TIMEOUT_EXCEPTION);
			return sb.toString();
		}
		try {
			URL url = new URL(urlString);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setConnectTimeout(Const.SOCKET_CONNECTION_TIMEOUT);
			urlConnection.setReadTimeout(Const.SOCKET_CONNECTION_TIMEOUT);
			//需要设置 gzip的请求头 才可以获取Content-Encoding响应码
			urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
//			urlConnection.setRequestMethod("GET");
//			urlConnection.setDoInput(true);
//			urlConnection.setUseCaches(true);
			// cache the data
			// urlConnection.addRequestProperty("Cache-Control",
			// "only-if-cached");
			// urlConnection.addRequestProperty("Cache-Control", "no-cache");
//			 urlConnection.addRequestProperty("Cache-Control", "max-age=1000*3600");
			 urlConnection.addRequestProperty("Cache-Control", "max-age=0");
			 
			// 开始连接
//			urlConnection.connect();
//			if (HttpURLConnection.HTTP_OK == urlConnection.getResponseCode()) {
				String encoding = urlConnection.getContentEncoding();
				BufferedReader reader = null;
				String resultText = null; // 返回的文本信息
				if (null != encoding && encoding.toLowerCase().equals("gzip")) {
					GZIPInputStream gin = new GZIPInputStream(
							urlConnection.getInputStream());
					reader = new BufferedReader(new InputStreamReader(gin));
				} else {
					reader = new BufferedReader(new InputStreamReader(
							urlConnection.getInputStream(),charset));
				}
				while ((resultText = reader.readLine()) != null) {
					sb.append(resultText);
				}
				resultText = sb.toString();
//			}
			urlConnection.disconnect();
		} catch (Exception e) {
			sb.append(Const.SOCKET_TIMEOUT_EXCEPTION);
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
				if (br != null)
					br.close();
				if (outStream != null)
					outStream.close();
				if (isr != null)
					isr.close();
				if (gzin != null)
					gzin.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (urlConnection != null)
				urlConnection.disconnect();
		}
		return sb.toString();
	}

	private void setRequestProperty(String urlString,
			HttpURLConnection urlConnection) {
		// urlConnection.setDoOutput(true);
//		 urlConnection.setDoInput(true);
		urlConnection.setUseCaches(true);
		urlConnection
				.setRequestProperty(
						"User-agent",
						"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 GTB5");
		urlConnection
				.setRequestProperty(
						"Accept",
						"text/html,application/xhtml+xml,application/xml,application/json;q=0.9,*/*;q=0.8");
		urlConnection.setRequestProperty("Accept-Language", "zh-cn,zh;q=0.5");
		urlConnection.setRequestProperty("Accept-Charset",
				"GB2312,utf-8;q=0.7,*;q=0.7");
		urlConnection.setRequestProperty("Keep-Alive", "500");
		urlConnection.setRequestProperty("Connection", "keep-alive");
		urlConnection.setRequestProperty("Referer", urlString);
	}

	/*public String httpUrlPost(String pathUrl, String requestString) {
		String resultjson = null;
		try {

			// 建立连接
			URL url = new URL(pathUrl);
			HttpURLConnection httpConn = (HttpURLConnection) url
					.openConnection();

			// //设置连接属性
			httpConn.setDoOutput(true);// 使用 URL 连接进行输出
			httpConn.setDoInput(true);// 使用 URL 连接进行输入
			httpConn.setUseCaches(false);// 忽略缓存
			httpConn.setRequestMethod("POST");// 设置URL请求方法

			// 设置请求属性
			// 获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致
			byte[] requestStringBytes = requestString.getBytes("utf-8");
			httpConn.setRequestProperty("Content-length", ""
					+ requestStringBytes.length);
			// httpConn.setRequestProperty("Content-Type",
			// "application/octet-stream");
			httpConn.setRequestProperty("Content-Type", "multipart/form-data");
			httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
			httpConn.setRequestProperty("Charset", "UTF-8");
			//
			String name = URLEncoder.encode("黄武艺", "utf-8");
			httpConn.setRequestProperty("NAME", name);
			// httpConn.setRequestProperty("json", name);

			// 建立输出流，并写入数据
			OutputStream outputStream = httpConn.getOutputStream();
			outputStream.write(requestStringBytes);
			outputStream.close();
			// 获得响应状态
			int responseCode = httpConn.getResponseCode();
			if (HttpURLConnection.HTTP_OK == responseCode) {// 连接成功

				// 当正确响应时处理数据
				StringBuffer sb = new StringBuffer();
				String readLine;
				BufferedReader responseReader;
				// 处理响应流，必须与服务器响应流输出的编码一致
				responseReader = new BufferedReader(new InputStreamReader(
						httpConn.getInputStream(), "utf-8"));
				while ((readLine = responseReader.readLine()) != null) {
					sb.append(readLine).append("\n");
				}
				responseReader.close();
				resultjson = sb.toString();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return resultjson;
	}*/

	/*public String requesPost2(String urlString, String charset, String json) {
		StringBuffer sb = new StringBuffer();
		String reContent = "";
		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Context-type", "multipart/form-data");
			conn.setDoOutput(true);

			OutputStream os = conn.getOutputStream();
			os.write(json.getBytes());

			os.flush();
			os.close();
			if (conn.getResponseCode() == 200) {
				InputStream is = conn.getInputStream();

				String str = null;
				BufferedReader br = new BufferedReader(new InputStreamReader(
						is, charset));
				while ((str = br.readLine()) != null) {
					sb.append(str + "\n");
				}
				is.close();
			}
		} catch (Exception e) {
		}
		return sb.toString();
	}*/

	/*public String requesPost(String urlString, String charset) {
		HttpURLConnection urlConnection = null;
		String reContent = "";
		try {
			URL url = new URL(urlString);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setConnectTimeout(Const.SOCKET_CONNECTION_TIMEOUT);
			urlConnection.setReadTimeout(Const.SOCKET_CONNECTION_TIMEOUT);
			urlConnection.setRequestMethod("POST");
			urlConnection.setRequestProperty("Context-type",
					"multipart/form-data");
			setRequestProperty(urlString, urlConnection);
			InputStream is = urlConnection.getInputStream();
			DataInputStream dataInputStream = new DataInputStream(is);
			BufferedReader br = null;
			StringBuffer sb = new StringBuffer();
			String str = "";
			br = new BufferedReader(new InputStreamReader(dataInputStream,
					charset));
			while ((str = br.readLine()) != null) {
				sb.append(str + "\n");
			}
			String re = new String(sb);
			reContent = re;

		} catch (SocketTimeoutException e) {
			reContent = Const.SOCKET_TIMEOUT_EXCEPTION + "";
		} catch (ConnectException e) {
			reContent = Const.SOCKET_TIMEOUT_EXCEPTION + "";
		} catch (Exception e) {
			// System.out.println(e.getMessage());
		} finally {
			urlConnection.disconnect();
		}
		return reContent;
	}*/

	/*public String postFile(String actionUrl, Map<String, String> params,
			Map<String, File> files) throws IOException {

		String BOUNDARY = "---------------------------"
				+ System.currentTimeMillis();// 分割符
		String PREFIX = "--"; // 前缀
		String LINEND = "\r\n"; // 换行符
		String MULTIPART_FROM_DATA = "multipart/form-data";// 数据类型
		String CHARSET = "UTF-8";// 字符编码

		URL uri = new URL(actionUrl);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		conn.setReadTimeout(5 * 1000);
		conn.setRequestMethod("POST");
		setRequestProperty(actionUrl, conn);
		// conn.setRequestProperty("Authorization", "Basic " +
//		 Base64.encodeBytes((username + ":" + passwd).getBytes()));//认证
		conn.setRequestProperty("Charsert", "UTF-8");
		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
				+ ";boundary=" + BOUNDARY);

		// 首先组拼文本类型的参数
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);
			sb.append("Content-Disposition: form-data; name=\""
					+ entry.getKey() + "\"" + LINEND);
			sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
			sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
			sb.append(LINEND);
			sb.append(entry.getValue());
			sb.append(LINEND);
		}

		DataOutputStream outStream = new DataOutputStream(
				conn.getOutputStream());
		outStream.write(sb.toString().getBytes());

		InputStream in = null;
		// 发送文件数据
		if (files != null) {
			for (Map.Entry<String, File> file : files.entrySet()) {
				StringBuilder sb1 = new StringBuilder();
				sb1.append(PREFIX);
				sb1.append(BOUNDARY);
				sb1.append(LINEND);
				// name是post中传参的键 filename是文件的名称
				sb1.append("Content-Disposition: form-data; name=\""
						+ file.getKey() + "\"; filename=\"" + file.getValue()
						+ "\"" + LINEND);
				sb1.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINEND);
				sb1.append(LINEND);
				outStream.write(sb1.toString().getBytes());
				InputStream is = new FileInputStream(file.getValue());

				int bytesAvailable;
				while ((bytesAvailable = is.available()) > 0) {
					int bufferSize = Math.min(bytesAvailable, 4096);
					byte[] buffer = new byte[bufferSize];
					int bytesRead = is.read(buffer, 0, bufferSize);
					outStream.write(buffer, 0, bytesRead);
				}

				is.close();
				outStream.write(LINEND.getBytes());

			}

			// 请求结束标志

		}
		byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
		outStream.write(end_data);
		outStream.flush();
		outStream.close();
		// 得到响应码
		StringBuilder sb2 = null;
		int res = conn.getResponseCode();
		if (res == 200) {
			in = conn.getInputStream();
			int ch;
			sb2 = new StringBuilder();
			while ((ch = in.read()) != -1) {
				sb2.append((char) ch);
			}
		} else {
			return "error";
		}
		conn.disconnect();
		return sb2.toString();
	}*/

	/*public String requestPostHttpClient(String urlString, String encoding,
			List<NameValuePair> paramPairs) {
		StringBuffer sb = new StringBuffer();
		if(!AppContext.instance.isNetworkConnected()){
			sb.append(Const.SOCKET_TIMEOUT_EXCEPTION);
			return sb.toString();
		}
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),
				10 * 1000);
		HttpConnectionParams.setSoTimeout(httpClient.getParams(), 10 * 1000);
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramPairs,
					encoding);
			HttpPost httppost = new HttpPost(urlString);
			httppost.setEntity(entity);
			HttpResponse httpResponse = httpClient.execute(httppost);
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream is = httpEntity.getContent();
			String str = null;
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					encoding));
			while ((str = br.readLine()) != null) {
				sb.append(str + "\n");
			}
		} catch (Exception e) {
			sb = new StringBuffer();
			sb.append(Const.SOCKET_TIMEOUT_EXCEPTION);
		}
		return sb.toString();
	}*/

	/*public String requestPostHttpClient3(String urlString, String encoding,
			String jsonObject) {
		StringBuffer sb = new StringBuffer();
		if(!AppContext.instance.isNetworkConnected()){
			sb.append(Const.SOCKET_TIMEOUT_EXCEPTION);
			return sb.toString();
		}
		
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),
				10 * 1000);
		HttpConnectionParams.setSoTimeout(httpClient.getParams(), 10 * 1000);
		
		try {
			// List<NameValuePair> nameValuePair = new
			// ArrayList<NameValuePair>();
			// nameValuePair.add(new BasicNameValuePair("jsonString",
			// jsonObject));

			// 绑定到请求 Entry
			StringEntity se = new StringEntity(jsonObject);

			HttpPost httppost = new HttpPost(urlString);
			// httppost.addHeader("Content-Type", "application/json");
			httppost.addHeader("Content-Type", "multipart/form-data");
			// httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
			httppost.setEntity(se);
			// 发送请求
			HttpResponse httpResponse = httpClient.execute(httppost);
			// 得到应答的字符串
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream is = httpEntity.getContent();
			String str = null;
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					encoding));
			while ((str = br.readLine()) != null) {
				sb.append(str + "\n");
			}
		} catch (Exception e) {
			sb = new StringBuffer();
			sb.append(Const.SOCKET_TIMEOUT_EXCEPTION);
		}
		return sb.toString();
	}*/

	/**
	 * MultipartEntity 添加httpmime jar 包
	 * 
	 * @param urlString
	 * @param encoding
	 * @param jsonObject
	 * @return
	 */
	public String requestPostHttpClient4(String urlString, String encoding,
			String jsonObject) {
		StringBuffer sb = new StringBuffer();
		if(!AppContext.instance.isNetworkConnected()){
			sb.append(Const.SOCKET_TIMEOUT_EXCEPTION);
			return sb.toString();
		}

		try {
			HttpPost httppost = new HttpPost(urlString);
			//请求超时
			httppost.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, Const.SOCKET_CONNECTION_TIMEOUT);
			//读取超时
			httppost.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, Const.SOCKET_CONNECTION_TIMEOUT);
			httppost.getParams().setBooleanParameter(
					CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
			MultipartEntity multipartEntity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);

			multipartEntity.addPart("json",
					new StringBody(jsonObject, Charset.forName("utf-8")));

			httppost.setEntity(multipartEntity);
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httppost);
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream is = httpEntity.getContent();
			String str = null;
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					encoding));
			while ((str = br.readLine()) != null) {
				sb.append(str + "\n");
			}
		} catch (Exception e) {
			sb = new StringBuffer();
			sb.append(Const.SOCKET_TIMEOUT_EXCEPTION);
		}
		return sb.toString();
	}
	
	/**
	 * 	request  参数 多个类型: json, file
	 * @param urlString 
	 * @param encoding
	 * @param map post 参数  key : "json" ,value : json.toString
	 * 					    key : "file" , value :  file is path
	 * @return
	 */
	public String requestPostHttpClient(String urlString, String encoding,
			HashMap<String, String> map) {
		StringBuffer sb = new StringBuffer();
		if(!AppContext.instance.isNetworkConnected()){
			sb.append(Const.SOCKET_TIMEOUT_EXCEPTION);
			return sb.toString();
		}
		try {
			HttpPost httppost = new HttpPost(urlString);
			//请求超时
			httppost.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, Const.SOCKET_CONNECTION_TIMEOUT);
			//读取超时
			httppost.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, Const.SOCKET_CONNECTION_TIMEOUT);
			httppost.getParams().setBooleanParameter(
					CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
			MultipartEntity multipartEntity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			for (Map.Entry<String, String> entry : map.entrySet()) {
				if("json".equals(entry.getKey())){
					multipartEntity.addPart("json", new StringBody(entry.getValue(), Charset.forName(encoding)));
				}else if("file".equals(entry.getKey()) && !TextUtils.isEmpty(entry.getValue())){
//					System.out.println("----------------file add part ------------");
					multipartEntity.addPart("file", new FileBody(new File(entry.getValue())));
				}
			}
			httppost.setEntity(multipartEntity);
		
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httppost);
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream is = httpEntity.getContent();
			String str = null;
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					encoding));
			while ((str = br.readLine()) != null) {
				sb.append(str + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
			sb = new StringBuffer();
			sb.append(Const.SOCKET_TIMEOUT_EXCEPTION);
		}
		return sb.toString();
	}

	public String requestPostHttpClient2(String urlString, String encoding,
			String jsonObject) {
		StringBuffer sb = new StringBuffer();
		if(!AppContext.instance.isNetworkConnected()){
			sb.append(Const.SOCKET_TIMEOUT_EXCEPTION);
			return sb.toString();
		}
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),
				10 * 1000);
		HttpConnectionParams.setSoTimeout(httpClient.getParams(), 10 * 1000);
		try {
			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
			nameValuePair.add(new BasicNameValuePair("json", jsonObject));
			HttpPost httppost = new HttpPost(urlString);
			httppost.addHeader("Content-Type", "multipart/form-data");
			// httppost.addHeader("Content-Type", "application/json");
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePair,
					HTTP.UTF_8));
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httppost);
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream is = httpEntity.getContent();
			String str = null;
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					encoding));
			while ((str = br.readLine()) != null) {
				sb.append(str + "\n");
			}
		} catch (Exception e) {
			sb = new StringBuffer();
			sb.append(Const.SOCKET_TIMEOUT_EXCEPTION);
		}
		return sb.toString();
	}

	/*public String requestGetHttpClient(String urlString, String encoding) {
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),
				10 * 1000);
		HttpConnectionParams.setSoTimeout(httpClient.getParams(), 10 * 1000);
		StringBuffer sb = new StringBuffer();
		try {
			HttpGet httpget = new HttpGet(urlString);
			HttpResponse httpResponse = httpClient.execute(httpget);
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream is = httpEntity.getContent();
			String str = null;
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					encoding));
			while ((str = br.readLine()) != null) {
				sb.append(str + "\n");
			}
		} catch (Exception e) {
			sb = new StringBuffer();
			sb.append(Const.SOCKET_TIMEOUT_EXCEPTION);
		}
		return sb.toString();
	}*/
}
