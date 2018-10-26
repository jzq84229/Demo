package com.zhang.example.utils;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

public class AsyncHttpRequest extends AsyncTask<String, Void, String> {
	
	public static final String GET = "get";
	public static final String POST = "post";
	
	private static final HttpClient CLIENT = new DefaultHttpClient();
	private static final String ENCODE_TYPE = "UTF-8";
	
	static {
		CLIENT.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, Constant.HTTP.TIMEOUT);
		CLIENT.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,Constant.HTTP.TIMEOUT);
	}
	
	private String url;
	private List<NameValuePair> values;
    private int requestCode;
	private HttpHandler handler;
//	private String[] requestParams;
	private long starTime;
	
	public AsyncHttpRequest(HttpHandler ctx, String url, List<NameValuePair> values){
		this.handler = ctx;
		this.url = url;
		this.values = values;
	}
	
	@Override
	protected String doInBackground(String... params) {
		String result = null;
		if (params[0] != null) {
			String request = params[1];
			requestCode = Integer.parseInt(params[0]);
			if (POST.equals(request)) {
				result = postRequest(url, values);
			} else if (GET.equals(request)) {
				result = getRequest(url);
			}
//			if(params.length >1) requestParams = params;
		} else {
			result = postRequest(url, values);
		}
		return result;
	}

	
	@Override
	protected void onPostExecute(String result) { 
		if(handler!= null){
		    handler.callBackFunction(result, requestCode);
	    }
//		System.out.println("Url:" + url +" ---------> " +( System.currentTimeMillis() - starTime));
//		System.out.println("Url: ---------> " +result);
	}
	
	@Override
	protected void onCancelled() {
	    super.onCancelled();
	}
	
	private static String postRequest(String url, List<NameValuePair> values) {
		HttpPost httpPost = new HttpPost(url);
		try {
			if (values != null) {
				HttpEntity httpEntity = new UrlEncodedFormEntity(values,ENCODE_TYPE);
				httpPost.setEntity(httpEntity);
			}
			HttpResponse response = CLIENT.execute(httpPost);
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity, ENCODE_TYPE);
		} catch (Exception e) {
			e.printStackTrace();
//			Log.e("BaseRequest", "", e);
		}
		return null;
	}
	
	private static String getRequest(String url) {
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = CLIENT.execute(get);
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity, ENCODE_TYPE);
		} catch (Exception e) {
			e.printStackTrace();
//			Log.e("BaseRequest", "", e);
		}
		return null;
	}

	public interface HttpHandler {
		/*
		 *result 请求的返回结果 
		 *requestCode 请求标识符
		 */
		public Object callBackFunction(String result, int requestCode);
	}

}
