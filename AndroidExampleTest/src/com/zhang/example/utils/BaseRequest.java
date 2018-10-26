package com.zhang.example.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import android.util.Log;


public class BaseRequest {
	private static final HttpClient CLIENT = new DefaultHttpClient();
	private static final String ENCODE_TYPE = "UTF-8";
	
	static {
		CLIENT.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		CLIENT.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, Constant.HTTP.TIMEOUT);
		CLIENT.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,Constant.HTTP.TIMEOUT);
	}

	protected static String postRequest(String url, Map<String,Object> params) {
		HttpPost httpPost = new HttpPost(url);
		try {
			if(params!=null&&!params.isEmpty()) {
				List<NameValuePair> values = new ArrayList<NameValuePair>();
				Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
				while(it.hasNext()) {
					Map.Entry<String, Object> entry = it.next();
					values.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
				}
				HttpEntity httpEntity = new UrlEncodedFormEntity(values,ENCODE_TYPE);
				httpPost.setEntity(httpEntity);
			}
			HttpResponse response = CLIENT.execute(httpPost);
			if(response.getStatusLine().getStatusCode()!=HttpStatus.SC_OK) {
				return null;
			}
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity, ENCODE_TYPE);
		} catch (Exception e) {
			Log.e("BaseRequest", "", e);
		}
		return null;
	}
	
//	protected static String postMultipartRequest(String url, Map<String,Object> params) {
//		HttpPost httpPost = new HttpPost(url);
//		try {
//			if(params!=null&&!params.isEmpty()) {
//				MultipartEntity mpEntity = new MultipartEntity();
//				Iterator<Entry<String, Object>> it = params.entrySet().iterator();
//				while(it.hasNext()) {
//					Entry<String,Object> entry = it.next();
//					String key = entry.getKey();
//					Object value = entry.getValue();
//					if(value instanceof File) {
//						ContentBody body = new FileBody((File)value, "image/jpg");
//						mpEntity.addPart(key, body);
//					}else {
//						ContentBody body = new StringBody(value.toString(),Constant.CHARSET_UTF_8);
//						mpEntity.addPart(key,body);
//					}
//				}
//				httpPost.setEntity(mpEntity);
//			}
//			HttpResponse response = CLIENT.execute(httpPost);
//			if(response.getStatusLine().getStatusCode()!=HttpStatus.SC_OK) {
//				return null;
//			}
//			HttpEntity entity = response.getEntity();
//			return EntityUtils.toString(entity, ENCODE_TYPE);
//		} catch (Exception e) {
//			Log.e("BaseRequest", "", e);
//		}
//		return null;
//	}
	
	protected static String getRequest(String url) {
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = CLIENT.execute(get);
			if(response.getStatusLine().getStatusCode()!=HttpStatus.SC_OK) {
				return null;
			}
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity, ENCODE_TYPE);
		} catch (Exception e) {
			Log.e("BaseRequest", "", e);
		}
		return null;
	}
}
