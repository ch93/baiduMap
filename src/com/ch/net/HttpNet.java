package com.ch.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * 使用HttpClient进行网络操作
 * @author ch
 *
 */
public class HttpNet {
	
	private HttpClient httpClient = null;
	
	HttpResponse response = null;
	
	public HttpNet() {
		this.httpClient = HttpClients.createDefault();
	}

	
	public String doPost(String url, List<NameValuePair> params) {
		String result = null;
		HttpPost httpPost = new HttpPost(url);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				result = EntityUtils.toString(response.getEntity());
			} else {
				System.out.println(" HttpStatus: " + statusCode + "  there is error with httpPost！");
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
		return result;
	}

}