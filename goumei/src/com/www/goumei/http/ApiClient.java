package com.www.goumei.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.www.goumei.bean.Categories;
import com.www.goumei.bean.CategoriesModel;
import com.www.goumei.bean.FriendsDataList;
import com.www.goumei.bean.SavePicResult;
import com.www.goumei.bean.SaveVideoResult;
import com.www.goumei.bean.ThemeBean;
import com.www.goumei.bean.ThemeDetails;
import com.www.goumei.bean.ThemesBean;
import com.www.goumei.bean.ThemesDetails;
import com.www.goumei.bean.UpVideoDetailBean;
import com.www.goumei.bean.UserInfo;
import com.www.goumei.bean.UsersInfo;
import com.www.goumei.bean.Videos;
import com.www.goumei.bean.VideosDataList;
import com.www.goumei.http.req.SP_ID;
import com.www.goumei.utils.FileUtil;
import com.www.goumei.utils.L;
import com.www.goumei.utils.StringUtils;
import com.www.goumei.utils.TimeUtils;
import com.www.goumei.utils.Util;

/**
 * 
 * @ClassName: ApiClient
 * @Description: API客户端接口：用于访问网络数据
 * @author sunyouyi
 * @date 2015-1-19 下午9:39:41
 * 
 */
public class ApiClient {

	public static final String UTF_8 = "UTF-8";
	public static final String DESC = "descend";
	public static final String ASC = "ascend";

	private final static int TIMEOUT_CONNECTION = 20000;
	private final static int TIMEOUT_SOCKET = 20000;
	private final static int RETRY_TIME = 3;

	private static String appCookie;
	private static String appUserAgent;
	private static ThemesBean deserialize;
	private static CategoriesModel myCategoriesModel;

	// private static ObjectMapper objectMapper = new ObjectMapper();

	private static HttpClient getHttpClient() {

		HttpClient httpClient = new HttpClient();
		// 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
		// httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		// 设置 默认的超时重试处理策略
		httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		// 设置 连接超时时间
		httpClient.getHttpConnectionManager().getParams()
				.setConnectionTimeout(TIMEOUT_CONNECTION);
		// 设置 读数据超时时间
		httpClient.getHttpConnectionManager().getParams()
				.setSoTimeout(TIMEOUT_SOCKET);
		// 设置 字符集
		httpClient.getParams().setContentCharset(UTF_8);

		return httpClient;
	}

	private static GetMethod getHttpGet(String url) {
		GetMethod httpGet = new GetMethod(url);
		// 设置 请求超时时间
		httpGet.getParams().setSoTimeout(TIMEOUT_SOCKET);
		// httpGet.setRequestHeader("Host", URLs.HOST);
		httpGet.setRequestHeader("Connection", "Keep-Alive");
		// httpGet.setRequestHeader("Cookie", cookie);
		// httpGet.setRequestHeader("User-Agent", userAgent);
		return httpGet;
	}

	private static PostMethod getHttpPost(String url) {
		PostMethod httpPost = new PostMethod(url);
		// 设置 请求超时时间
		httpPost.getParams().setSoTimeout(TIMEOUT_SOCKET);
		// httpPost.setRequestHeader("Cookie", "");
		// httpPost.setRequestHeader("Connection", "Keep-Alive");
		httpPost.addRequestHeader("Content", "text/html,charset=UTF-8");
		return httpPost;
	}

	private static String saveStore(String p_url, Map<String, Object> params) {
		StringBuilder url = new StringBuilder(p_url);
		if (url.indexOf("?") < 0)
			url.append('?');

		for (String name : params.keySet()) {
			url.append('&');
			url.append(name);
			url.append('=');
			url.append(String.valueOf(params.get(name)));
			// 不做URLEncoder处理
			// url.append(URLEncoder.encode(String.valueOf(params.get(name)),
			// UTF_8));
		}

		return url.toString().replace("?&", "?");
	}

	private static String _MakeURL(String p_url, Map<String, Object> params) {
		StringBuilder url = new StringBuilder(p_url);
		if (url.indexOf("?") < 0)
			url.append('?');

		for (String name : params.keySet()) {
			url.append('&');
			url.append(name);
			url.append('=');
			url.append(String.valueOf(params.get(name)));
			// 不做URLEncoder处理
			// url.append(URLEncoder.encode(String.valueOf(params.get(name)),
			// UTF_8));
		}

		return url.toString().replace("?&", "?");
	}

	/***
	 * get请求URL
	 * 
	 * @param url
	 * @throws AppException
	 */
	public static String http_get(String url, boolean retry) {

		HttpClient httpClient = null;
		GetMethod httpGet = null;

		String responseBody = "";
		int time = 0;
		do {
			try {
				httpClient = getHttpClient();
				httpGet = getHttpGet(url);
				int statusCode = httpClient.executeMethod(httpGet);
				if (statusCode != HttpStatus.SC_OK) {
					// throw AppException.http(statusCode);
				}
				responseBody = httpGet.getResponseBodyAsString();
				// System.out.println("XMLDATA=====>"+responseBody);
				break;
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
				// throw AppException.http(e);
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				// throw AppException.network(e);
			} finally {
				// 释放连接
				httpGet.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME && retry);

		System.out.println("get请求的url---" + url);
		Log.i("info_out", "response:" + responseBody);
		return responseBody;
	}

	public static String http_post(String url, boolean retry) {

		HttpClient httpClient = null;
		PostMethod httpPost = null;

		String responseBody = "";
		int time = 0;
		do {
			try {
				httpClient = getHttpClient();
				httpPost = getHttpPost(url);
				int statusCode = httpClient.executeMethod(httpPost);
				if (statusCode != HttpStatus.SC_OK) {
					// throw AppException.http(statusCode);
				}
				responseBody = httpPost.getResponseBodyAsString();
				// System.out.println("XMLDATA=====>"+responseBody);
				break;
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
				// throw AppException.http(e);
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				// throw AppException.network(e);
			} finally {
				// 释放连接
				httpPost.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME && retry);

		System.out.println("get请求的url---" + url);
		Log.i("info_out", "response:" + responseBody);
		return responseBody;
	}

	/**
	 * 公用post方法
	 * 
	 * @param url
	 * @param params
	 * @param files
	 * @throws AppException
	 */
	private static String _post_reconize(String url,
			Map<String, Object> params, Map<String, File> files,
			boolean againFlag) {
		// System.out.println("post_url==> "+url);
		// String cookie = getCookie(appContext);
		// String userAgent = getUserAgent(appContext);

		HttpClient httpClient = null;

		PostMethod httpPost = null;
		// post表单参数处理
		int length = (params == null ? 0 : params.size())
				+ (files == null ? 0 : files.size());
		Part[] parts = new Part[length];
		int i = 0;
		if (params != null)
			for (String name : params.keySet()) {
				parts[i++] = new StringPart(name, String.valueOf(params
						.get(name)), UTF_8);
				// System.out.println("post_key==> "+name+"    value==>"+String.valueOf(params.get(name)));
			}
		if (files != null)
			for (String file : files.keySet()) {
				try {
					parts[i++] = new FilePart(file, files.get(file));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				// System.out.println("post_key_file==> "+file);
			}

		String responseBody = "";
		int time = 0;
		do {
			try {
				httpClient = getHttpClient();
				httpPost = getHttpPost(url);
				httpPost.setRequestEntity(new MultipartRequestEntity(parts,
						httpPost.getParams()));
				int statusCode = httpClient.executeMethod(httpPost);
				if (statusCode == HttpStatus.SC_OK) {
					responseBody = new String(
							httpPost.getResponseBodyAsString());
				}
				// else if(statusCode == HttpStatus.SC_OK)
				// {
				// Cookie[] cookies = httpClient.getState().getCookies();
				// String tmpcookies = "";
				// for (Cookie ck : cookies) {
				// tmpcookies += ck.toString()+";";
				// }
				// Log.i("info_out","cookie:"+tmpcookies);
				// if(appContext != null && tmpcookies != ""){
				// appContext.setProperty("cookie", tmpcookies);
				// appCookie = tmpcookies;
				// }
				// }

				// responseBody = new String(httpPost.getResponseBody(),UTF_8);
				// responseBody = new String(httpPost.getResponseBody(), "GBK");
				// Log.i("info_out","res"+responseBody2);
				// System.out.println("XMLDATA=====>"+responseBody);
				break;
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
				// throw AppException.http(e);
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				// throw AppException.network(e);
			} finally {
				// 释放连接
				httpPost.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME && againFlag);

		return responseBody;
	}

	/**
	 * 公用post方法
	 * 
	 * @param url
	 * @param params
	 * @param files
	 * @throws AppException
	 */
	private static String _post(String url, Map<String, Object> params,
			Map<String, File> files, boolean againFlag) {
		// System.out.println("post_url==> "+url);
		// String cookie = getCookie(appContext);
		// String userAgent = getUserAgent(appContext);

		HttpClient httpClient = null;
		PostMethod httpPost = null;
		// post表单参数处理
		int length = (params == null ? 0 : params.size())
				+ (files == null ? 0 : files.size());
		Part[] parts = new Part[length];
		int i = 0;
		if (params != null)
			for (String name : params.keySet()) {
				parts[i++] = new StringPart(name, String.valueOf(params
						.get(name)));
				// System.out.println("post_key==> "+name+"    value==>"+String.valueOf(params.get(name)));
			}
		if (files != null)
			for (String file : files.keySet()) {
				try {
					parts[i++] = new FilePart(file, files.get(file));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				// System.out.println("post_key_file==> "+file);
			}

		String responseBody = "";
		int time = 0;
		do {
			try {
				httpClient = getHttpClient();
				httpPost = getHttpPost(url);
				httpPost.setRequestEntity(new MultipartRequestEntity(parts,
						httpPost.getParams()));
				int statusCode = httpClient.executeMethod(httpPost);
				if (statusCode == HttpStatus.SC_OK) {
					Log.e("responseBody", httpPost.getResponseBodyAsString());
					responseBody = httpPost.getResponseBodyAsString();
				} else {

				}

				// System.out.println("XMLDATA=====>"+responseBody);
				break;
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();

			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生网络异常
				e.printStackTrace();

			} finally {
				// 释放连接
				httpPost.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME && againFlag);

		// responseBody = responseBody.replaceAll("\\p{Cntrl}", "");
		// if(responseBody.contains("result") &&
		// responseBody.contains("errorCode") &&
		// appContext.containsProperty("user.uid")){
		// try {
		// Result res = Result.parse(new
		// ByteArrayInputStream(responseBody.getBytes()));
		// if(res.getErrorCode() == 0){
		// appContext.Logout();
		// appContext.getUnLoginHandler().sendEmptyMessage(1);
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }

		return responseBody;
	}

	/**
	 * 
	 * @param url
	 * @param params
	 * @param againFlag
	 * @return
	 */
	private static String _post_httpPost(String url,
			List<BasicNameValuePair> params, boolean againFlag) {
		// 和GET方式一样，先将参数放入List
		String responseBody = "";

		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = null;
			HttpPost postMethod = new HttpPost(url);
			postMethod.setEntity(new UrlEncodedFormEntity(params, "utf-8")); // 将参数填入POST
																				// Entity中

			httpResponse = httpClient.execute(postMethod); // 执行POST方法
			/*
			 * Log.i("TAG", "resCode = " +
			 * httpResponse.getStatusLine().getStatusCode()); //获取响应码
			 * Log.i("TAG", "result = " +
			 * EntityUtils.toString(httpResponse.getEntity(), "utf-8"));
			 * //获取响应内容
			 */int resCode = httpResponse.getStatusLine().getStatusCode();
			if (resCode == HttpStatus.SC_OK) {
				responseBody = EntityUtils.toString(httpResponse.getEntity(),
						"utf-8");
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return responseBody;
	}

	/**
	 * 
	 * @param url
	 * @param params
	 * @param againFlag
	 * @return
	 */
	protected static String _post_httpPostFormJsonStr(String url, String jsonStr) {
		String responseBody = "";
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			// 添加http头信息
			// httppost.addHeader("Authorization", "your token"); // 认证token
			httppost.addHeader("Content-Type", "application/json");
			httppost.addHeader("User-Agent", "imgfornote");
			httppost.addHeader("charset", HTTP.UTF_8);
			L.e("jsonStr::::::" + jsonStr);
			StringEntity entity = new StringEntity(jsonStr, HTTP.UTF_8);
			entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			httppost.setEntity(entity);
			HttpResponse response;
			response = httpclient.execute(httppost);
			// 检验状态码，如果成功接收数据
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) {
				responseBody = EntityUtils.toString(response.getEntity());
			}
			Log.e("TAG", "" + responseBody);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseBody;
	}

	public static VideosDataList getMyAttention(final int type,
			final int pageNo, final int pageSize) {
		String newUrl = "";
		final ReqBase reqBase = new ReqBase();

		newUrl = _MakeURL(Urls.URL_VIDEOLIST, new HashMap<String, Object>() {
			{
				put("apiVersion", reqBase.getApiVersion());
				put("appKey", reqBase.getAppKey());
				put("timeStamp", reqBase.getTimeStamp());
				put("sign", reqBase.getSign());
				put("executorID", reqBase.getExecutorID());
				put("moduleKeyLogo", reqBase.getModuleKeyLogo());
				put("actionKeyLogo", reqBase.getActionKeyLogo());
				put("pageNo", pageNo);
				put("pageSize", pageSize);
				if (type != 0) {
					if (type == 1) {
						put("orderFiled", "CreateTime DESC");
					} else if (type == 2) {
						put("orderFiled", "Hits DESC");
					}
				}
			}
		});

		String re = "";
		String bodyString = "";
		VideosDataList videosDataList = null;
		JSONObject obj = new JSONObject();
		// 判断是否是用户ID
		/*
		 * if (isInteger(value)) { obj.put("IdentityNo", value); Log.e("TAG",
		 * "IdentityNo:" + value); } else { obj.put("displayName", value);
		 * Log.e("TAG", "displayName:" + value); }
		 */

		try {
			obj.put("apiVersion", "0.0.1");
			obj.put("appKey", "1056491");
			obj.put("timeStamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new Date()));
			obj.put("sign", "D631A790384AE697874B07264EA1D8E7");
			obj.put("executorID", Integer.parseInt(SP_ID.id));
			obj.put("moduleKeyLogo", "");
			obj.put("actionKeyLogo", "");
			obj.put("pageNo", 1);
			obj.put("pageSize", 6);
			obj.put("FansID", "1");
			if (type != 0) {
				if (type == 1) {
					obj.put("orderFiled", "CreateTime DESC");
				} else if (type == 2) {
					obj.put("orderFiled", "Hits DESC");
				}
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String jsonStr = obj.toString();

		try {
			re = _post_httpPostFormJsonStr(Urls.URL_VIDEOLIST, obj.toString());
			if (!StringUtils.isEmpty(re)) {
				JSONObject jsonObject = new JSONObject(re);
				bodyString = jsonObject.optString("Body");
			}
			videosDataList = FastJSONHelper.deserialize(bodyString,
					VideosDataList.class);

		} catch (Exception e) {
			String a = e.getLocalizedMessage();
		}
		return videosDataList;

	}

	public static VideosDataList getCategoryDetail(final int categoryIDs,
			final int pageNo, final int pageSize) {
		String newUrl = "";
		final ReqBase reqBase = new ReqBase();

		newUrl = _MakeURL(Urls.URL_VIDEOLIST, new HashMap<String, Object>() {
			{
				put("apiVersion", reqBase.getApiVersion());
				put("appKey", reqBase.getAppKey());
				put("timeStamp", reqBase.getTimeStamp());
				put("sign", reqBase.getSign());
				put("executorID", reqBase.getExecutorID());
				put("moduleKeyLogo", reqBase.getModuleKeyLogo());
				put("actionKeyLogo", reqBase.getActionKeyLogo());
				put("actionKeyLogo", reqBase.getActionKeyLogo());
				put("pageNo", pageNo);
				put("pageSize", pageSize);
				put("categoryIDs", categoryIDs);

			}
		});

		String re = "";
		String bodyString = "";
		VideosDataList videosDataList = null;
		JSONObject obj = new JSONObject();
		// 判断是否是用户ID
		/*
		 * if (isInteger(value)) { obj.put("IdentityNo", value); Log.e("TAG",
		 * "IdentityNo:" + value); } else { obj.put("displayName", value);
		 * Log.e("TAG", "displayName:" + value); }
		 */

		try {
			obj.put("apiVersion", "0.0.1");
			obj.put("appKey", "1056491");
			obj.put("timeStamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new Date()));
			obj.put("sign", "D631A790384AE697874B07264EA1D8E7");
			obj.put("executorID", Integer.parseInt(SP_ID.id));
			obj.put("moduleKeyLogo", "");
			obj.put("actionKeyLogo", "");
			obj.put("pageNo", pageNo);
			obj.put("pageSize", pageSize);
			obj.put("FansID", "1");
			obj.put("categoryIDs", categoryIDs);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String jsonStr = obj.toString();

		try {
			re = _post_httpPostFormJsonStr(Urls.URL_VIDEOLIST, obj.toString());
			if (!StringUtils.isEmpty(re)) {
				JSONObject jsonObject = new JSONObject(re);
				bodyString = jsonObject.optString("Body");
			}
			videosDataList = FastJSONHelper.deserialize(bodyString,
					VideosDataList.class);

		} catch (Exception e) {
			String a = e.getLocalizedMessage();
		}
		return videosDataList;

	}

	public static FriendsDataList getSearchFriend(final String values,
			final int pageNo, final int pageSize) {
		String newUrl = "";
		final ReqBase reqBase = new ReqBase();

		newUrl = _MakeURL(Urls.URL_VIDEOLIST, new HashMap<String, Object>() {
			{
				put("apiVersion", reqBase.getApiVersion());
				put("appKey", reqBase.getAppKey());
				put("timeStamp", reqBase.getTimeStamp());
				put("sign", reqBase.getSign());
				put("executorID", reqBase.getExecutorID());
				put("moduleKeyLogo", reqBase.getModuleKeyLogo());
				put("actionKeyLogo", reqBase.getActionKeyLogo());
				put("pageNo", pageNo);
				put("pageSize", pageSize);
				// put("categoryIDs",categoryIDs);

			}
		});

		String re = "";
		String bodyString = "";
		FriendsDataList friendsDataList = null;
		JSONObject obj = new JSONObject();
		// 判断是否是用户ID
		/*
		 * try { if (Util.isInteger(values)) { obj.put("IdentityNo", values);
		 * Log.e("TAG", "IdentityNo:" + values); } else { obj.put("displayName",
		 * values); Log.e("TAG", "displayName:" +values); } } catch
		 * (JSONException e2) { // TODO Auto-generated catch block
		 * e2.printStackTrace(); }
		 */

		try {
			obj.put("apiVersion", "0.0.1");
			obj.put("appKey", "1056491");
			obj.put("timeStamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new Date()));
			obj.put("sign", "D631A790384AE697874B07264EA1D8E7");
			obj.put("executorID", Integer.parseInt(SP_ID.id));
			obj.put("moduleKeyLogo", "");
			obj.put("actionKeyLogo", "");
			obj.put("pageNo", pageNo);
			obj.put("pageSize", pageSize);
			obj.put("FansID", "1");
			// 判断是否是用户ID
			if (Util.isInteger(values)) {
				obj.put("IdentityNo", values);
				Log.e("TAG", "IdentityNo:" + values);
			} else {
				obj.put("displayName", values);
				Log.e("TAG", "displayName:" + values);
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String jsonStr = obj.toString();

		try {
			re = _post_httpPostFormJsonStr(Urls.queryUser, obj.toString());
			if (!StringUtils.isEmpty(re)) {
				JSONObject jsonObject = new JSONObject(re);
				bodyString = jsonObject.optString("Body");
			}
			friendsDataList = FastJSONHelper.deserialize(bodyString,
					FriendsDataList.class);

		} catch (Exception e) {
			String a = e.getLocalizedMessage();
		}
		return friendsDataList;
	}
	/**
	 * 上传视频到服务器
	 * @param filePath
	 * @return
	 */
	public static SaveVideoResult SaveVideo(final String filePath) {
		String newUrl = "";
		final ReqBase reqBase = new ReqBase();
		SaveVideoResult saveVideoResult = null;

		String re = "";
		String bodyString = "";
		byte[] video_byte = FileUtil.getBytes(filePath);

		SaveVideoReq saveVideoReq = new SaveVideoReq(video_byte);
		String jsonstr = FastJSONHelper.serialize(saveVideoReq);

		// String jsonStr=obj.toString();

		try {
			re = _post_httpPostFormJsonStr(Urls.SaveVideo, jsonstr);
			if (!StringUtils.isEmpty(re)) {
				JSONObject jsonObject = new JSONObject(re);
				bodyString = jsonObject.optString("Body");
			}
			saveVideoResult = FastJSONHelper.deserialize(bodyString,
					SaveVideoResult.class);

		} catch (Exception e) {
			String a = e.getLocalizedMessage();
		}
		return saveVideoResult;
	}

	/**
	 * 获取主题
	 * 
	 * @return
	 */
	public static List<ThemeBean> getTheme() {
		String resultStr = "";
		List<ThemeBean> themeBeans;
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(Urls.URL_DISCOVER);
			httpPost.addHeader("Content-Type", "application/json");
			httpPost.addHeader("User-Agent", "imgfornote");
			httpPost.addHeader("charset", HTTP.UTF_8);
			JSONObject obj = new JSONObject();
			obj.put("apiVersion", "v1.0.0");
			obj.put("appKey", "1056491");
			obj.put("timeStamp", TimeUtils.getTime());
			obj.put("sign", "6D00E637E7AADBB610DC0C6E74A78265");
			obj.put("executorID", Integer.parseInt(SP_ID.id));
			obj.put("pageNo", 1);
			obj.put("pageSize", 8);
			obj.put("ModuleKeyLogo", "");
			obj.put("ActionKeyLogo", "");
			StringEntity entity = new StringEntity(obj.toString(), HTTP.UTF_8);
			entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			httpPost.setEntity(entity);
			HttpResponse response;
			response = httpClient.execute(httpPost);
			// 检验状态码，如果成功接收数据
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) {
				resultStr = EntityUtils.toString(response.getEntity());
				resultStr = resultStr.replace("iD", "themeiD");
				Log.e("TAG", resultStr);
			}
			if (resultStr != null && !resultStr.equals("")) {
				JSONObject jsonObject = new JSONObject(resultStr);
				String optString = jsonObject.optString("Body");
				deserialize = FastJSONHelper.deserialize(optString,
						ThemesBean.class);
				themeBeans = deserialize.getModels();
				return themeBeans;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取这个主题的详情
	 */
	public static List<Videos> getThemeMore(ThemeBean bean) {
		List<Videos> list = new ArrayList<Videos>();
		String resultStr = "";
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(Urls.URL_DISCOVER_OPEN);
			httpPost.addHeader("Content-Type", "application/json");
			httpPost.addHeader("User-Agent", "imgfornote");
			httpPost.addHeader("charset", HTTP.UTF_8);
			JSONObject obj = new JSONObject();
			obj.put("ThemeIDs", bean.getThemeiD());
			obj.put("pageNo", 1);
			obj.put("pageSize", 10);
			obj.put("apiVersion", "v1.0.0");
			obj.put("appKey", "1056491");
			obj.put("timeStamp", TimeUtils.getTime());
			obj.put("sign", "6D00E637E7AADBB610DC0C6E74A78265");
			obj.put("executorID", Integer.parseInt(SP_ID.id));
			StringEntity entity = new StringEntity(obj.toString(), HTTP.UTF_8);
			entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			httpPost.setEntity(entity);
			HttpResponse response;
			response = httpClient.execute(httpPost);
			// 检验状态码，如果成功接收数据
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) {
				resultStr = EntityUtils.toString(response.getEntity());
				Log.e("TAG", resultStr);
			}
			if (!resultStr.equals("")) {
				JSONObject jsonObject = new JSONObject(resultStr);
				String optString = jsonObject.optString("Body");
				if (!optString.equals("") && optString != null) {
					VideosDataList details = FastJSONHelper.deserialize(
							optString, VideosDataList.class);
					if (details != null) {
						list.addAll(details.getModels());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 获取验证码
	 * 
	 * @param Telphone
	 *            手机号
	 * @return
	 */
	public static String GetShortMessageCode(final String Telphone) {
		String code = "";
		String re = "";
		String bodyString = "";
		GetShortMessageCodeReq getShortMessageCodeReq = new GetShortMessageCodeReq(
				Telphone);
		String jsonstr = FastJSONHelper.serialize(getShortMessageCodeReq);

		try {
			re = _post_httpPostFormJsonStr(Urls.GetShortMessageCode, jsonstr);
			if (!StringUtils.isEmpty(re)) {
				JSONObject jsonObject = new JSONObject(re);
				bodyString = jsonObject.optString("Body");
				JSONObject object = new JSONObject(bodyString);
				code = object.getString("message");
			}

		} catch (Exception e) {
			String a = e.getLocalizedMessage();
		}
		return code;
	}

	/**
	 * 登录验证
	 * 
	 * @param number
	 * @param pass
	 */
	public static UserInfo loginClient(String number, String pass) {
		Log.e("TAG", number + "--" + pass);
		UserInfo info = null;
		String result = "";
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(Urls.URL_LOGIN);
			httpPost.addHeader("Content-Type", "application/json");
			httpPost.addHeader("User-Agent", "imgfornote");
			httpPost.addHeader("charset", HTTP.UTF_8);
			JSONObject obj = new JSONObject();
			obj.put("apiVersion", "v1.0.0");
			obj.put("appKey", "1056491");
			obj.put("timeStamp", TimeUtils.getTime());
			obj.put("sign", "6D00E637E7AADBB610DC0C6E74A78265");
			obj.put("executorID", Integer.parseInt(SP_ID.id));
			obj.put("ModuleKeyLogo", "");
			obj.put("ActionKeyLogo", "");
			obj.put("Telphone", number);
			obj.put("Password", pass);
			StringEntity entity = new StringEntity(obj.toString(), HTTP.UTF_8);
			entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			httpPost.setEntity(entity);
			HttpResponse response;
			response = httpClient.execute(httpPost);
			// 检验状态码，如果成功接收数据
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) {
				result = EntityUtils.toString(response.getEntity());
			}
			Log.e("TAG", "1111" + result);
			if (result != null) {
				JSONObject jsonObject = new JSONObject(result);
				String optString = jsonObject.optString("Body");
				UsersInfo usersInfo = FastJSONHelper.deserialize(optString,
						UsersInfo.class);
				if (usersInfo != null) {
					info = usersInfo.getUser();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}

	/**
	 * 获取这个视频的详情-----
	 */
	public static List<ThemeDetails> getVideoDetail(final int VideosID) {
		List<ThemeDetails> list = new ArrayList<ThemeDetails>();
		String resultStr = "";
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(Urls.URL_DISCOVER_OPEN);
			httpPost.addHeader("Content-Type", "application/json");
			httpPost.addHeader("User-Agent", "imgfornote");
			httpPost.addHeader("charset", HTTP.UTF_8);
			JSONObject obj = new JSONObject();
			obj.put("VideosID", VideosID);
			obj.put("apiVersion", "v1.0.0");
			obj.put("appKey", "1056491");
			obj.put("timeStamp", TimeUtils.getTime());
			obj.put("sign", "6D00E637E7AADBB610DC0C6E74A78265");
			obj.put("executorID", Integer.parseInt(SP_ID.id));
			obj.put("pageNo", 1);
			obj.put("pageSize", 10);
			StringEntity entity = new StringEntity(obj.toString(), HTTP.UTF_8);
			entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			httpPost.setEntity(entity);
			HttpResponse response;
			response = httpClient.execute(httpPost);
			// 检验状态码，如果成功接收数据
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) {
				resultStr = EntityUtils.toString(response.getEntity());
				Log.e("TAG", resultStr);
			}
			if (!resultStr.equals("")) {
				JSONObject jsonObject = new JSONObject(resultStr);
				String optString = jsonObject.optString("Body");
				if (!optString.equals("") && optString != null) {
					ThemesDetails details = FastJSONHelper.deserialize(
							optString, ThemesDetails.class);
					if (details != null) {
						list.addAll(details.getModels());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 注册用户
	 * 
	 * @param username
	 * @param password
	 */
	public static int RegisterUser(String username, String password) {
		int id = -1;
		String result = "";
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(Urls.URL_REGISTER);
			httpPost.addHeader("Content-Type", "application/json");
			httpPost.addHeader("User-Agent", "imgfornote");
			httpPost.addHeader("charset", HTTP.UTF_8);
			JSONObject obj = new JSONObject();
			obj.put("apiVersion", "v1.0.0");
			obj.put("appKey", "1056491");
			obj.put("timeStamp", TimeUtils.getTime());
			obj.put("sign", "6D00E637E7AADBB610DC0C6E74A78265");
			obj.put("executorID", Integer.parseInt(SP_ID.id));
			obj.put("ModuleKeyLogo", "");
			obj.put("ActionKeyLogo", "");
			obj.put("Telphone", username);
			obj.put("Password", password);
			StringEntity entity = new StringEntity(obj.toString(), HTTP.UTF_8);
			entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			httpPost.setEntity(entity);
			HttpResponse response;
			response = httpClient.execute(httpPost);
			// 检验状态码，如果成功接收数据
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) {
				result = EntityUtils.toString(response.getEntity());
				Log.e("TAG", result);
				JSONObject jsonObject = new JSONObject(result);
				String optString = jsonObject.optString("Body");
				JSONObject jsonObj = new JSONObject(optString);
				id = jsonObj.getInt("iD");

			}
			Log.e("TAG", result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
	
	
	/**
	 * 获取分类
	 * 
	 * @return
	 */
	public static List<Categories> getCategories(int pageNo, int pageSize) {
		String resultStr = "";
		List<Categories> CategoriesBean;
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(Urls.URL_CATEGORIES);
			httpPost.addHeader("Content-Type", "application/json");
			httpPost.addHeader("User-Agent", "imgfornote");
			httpPost.addHeader("charset", HTTP.UTF_8);
			JSONObject obj = new JSONObject();
			obj.put("apiVersion", "v1.0.0");
			obj.put("appKey", "1056491");
			obj.put("timeStamp", TimeUtils.getTime());
			obj.put("sign", "6D00E637E7AADBB610DC0C6E74A78265");
			obj.put("executorID", Integer.parseInt(SP_ID.id));
			obj.put("pageNo", pageNo);
			obj.put("pageSize", pageSize);
			obj.put("ModuleKeyLogo", "");
			obj.put("ActionKeyLogo", "");
			StringEntity entity = new StringEntity(obj.toString(), HTTP.UTF_8);
			entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			httpPost.setEntity(entity);
			HttpResponse response;
			response = httpClient.execute(httpPost);
			// 检验状态码，如果成功接收数据
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) {
				resultStr = EntityUtils.toString(response.getEntity());
				//resultStr = resultStr.replace("iD", "themeiD");
				Log.e("TAG", resultStr);
			}
			if (resultStr != null && !resultStr.equals("")) {
				JSONObject jsonObject = new JSONObject(resultStr);
				String optString = jsonObject.optString("Body");
				myCategoriesModel = FastJSONHelper.deserialize(optString,
						CategoriesModel.class);
				CategoriesBean = myCategoriesModel.getModels();
				return CategoriesBean;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 上传封面到服务器
	 * @param filePath
	 * @return
	 */
	public static SavePicResult SavePicture(final String filePath) {
		String newUrl = "";
		final ReqBase reqBase = new ReqBase();
		SavePicResult savePicResult = null;

		String re = "";
		String bodyString = "";
		byte[] pic_byte = FileUtil.getBytes(filePath);

		SaveVideoReq savePicReq = new SaveVideoReq(pic_byte);
		String jsonstr = FastJSONHelper.serialize(savePicReq);

		// String jsonStr=obj.toString();

		try {
			re = _post_httpPostFormJsonStr(Urls.SAVE_PICTURE, jsonstr);
			if (!StringUtils.isEmpty(re)) {
				JSONObject jsonObject = new JSONObject(re);
				bodyString = jsonObject.optString("Body");
			}
			savePicResult = FastJSONHelper.deserialize(bodyString, SavePicResult.class);
		} catch (Exception e) {
			e.getLocalizedMessage();
		}
		return savePicResult;
	}
	
	/**
	 * 上传视频详细信息到服务器
	 * @param bean
	 * @return
	 */
	public static int updateVideoDetailsMsg(UpVideoDetailBean bean) {
		
		String result = "";
		int code = 0;
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(Urls.SAVE_VIDEO_DETAILS);
			httpPost.addHeader("Content-Type", "application/json");
			httpPost.addHeader("User-Agent", "imgfornote");
			httpPost.addHeader("charset", HTTP.UTF_8);
			JSONObject obj = new JSONObject();
			obj.put("apiVersion", "v1.0.0");
			obj.put("appKey", "1056491");
			obj.put("timeStamp", TimeUtils.getTime());
			obj.put("sign", "6D00E637E7AADBB610DC0C6E74A78265");
			obj.put("executorID", Integer.parseInt(SP_ID.id));
			obj.put("userID", bean.getUserID());
			obj.put("isPublish", bean.getIsPublish());
			obj.put("title", bean.getTitle());
			obj.put("cover", bean.getCover());
			obj.put("themeID", bean.getThemeID());
			obj.put("categoryID", bean.getCategoryID());
			obj.put("url", bean.getUrl());
			obj.put("link", bean.getLink());
			obj.put("linkDescription", bean.getLinkDescription());
			
			StringEntity entity = new StringEntity(obj.toString(), HTTP.UTF_8);
			entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			httpPost.setEntity(entity);
			HttpResponse response;
			response = httpClient.execute(httpPost);
			// 检验状态码，如果成功接收数据
			code = response.getStatusLine().getStatusCode();
			if (code == 200) {
				result = EntityUtils.toString(response.getEntity());
			}
			Log.e("TAG", "updateVideoDetailsMsg=" + result);
//			if (result != null) {
//				JSONObject jsonObject = new JSONObject(result);
//				String optString = jsonObject.optString("Body");
//				UsersInfo usersInfo = FastJSONHelper.deserialize(optString,
//						UsersInfo.class);
//				if (usersInfo != null) {
//					info = usersInfo.getUser();
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return code;
	}
	

}
