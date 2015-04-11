package com.ecust.spider.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class FetchUtil {
	String htm_str;
	private HttpClient hc;
	final static int tryNum = 3;
	private int mtryTime;

	public String getUrl(String url) {
		return getUrlByCon(url, tryNum);
	}

	public String getUrl(String url, int tryTime) {
		mtryTime = tryTime;
		try {
			hc = HttpClients.createDefault();
			HttpGet hg = new HttpGet(url);
			HttpResponse response = hc.execute(hg);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				InputStream stream = response.getEntity().getContent();
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				int i = -1;
				while ((i = stream.read()) != -1) {
					outStream.write(i);
				}
				htm_str = new String(outStream.toByteArray(), "gb2312");
			}
		} catch (Exception e) {
			if (--mtryTime >= 0) {
				System.out.println("获取价格：" + url + "时失败，剩余重试次数"
						+ (mtryTime + 1));
				return getUrl(url, mtryTime);
			} else
				e.printStackTrace();
		}
		return htm_str;
	}

	public String getUrlByCon(String url, int tryTime) {
		mtryTime = tryTime;
		try {
			URL mURL = new URL(url);
			URLConnection urlConnection = mURL.openConnection();
			InputStream inputStream = urlConnection.getInputStream();
			htm_str = StreamToString(inputStream);
		} catch (Exception e) {
			if (--mtryTime >= 0) {
				System.out.println("获取价格：" + url + "时失败，剩余重试次数"
						+ (mtryTime + 1));
				return getUrlByCon(url, mtryTime);
			} else
				e.printStackTrace();
		}
		return htm_str;
	}

	private String StreamToString(InputStream inputStream) {
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		StringBuilder result = new StringBuilder();
		String line = null;
		try {
			while ((line = bufferedReader.readLine()) != null) {
				result.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStreamReader.close();
				inputStream.close();
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result.toString();
	}
}
