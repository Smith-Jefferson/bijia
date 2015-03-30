package com.test.spider.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class FechUtil {
	 static String htm_str;
	private HttpClient hc;
	final static int tryNum=3;
	private int mtryTime;
	public String getUrl(String url){
		return getUrl(url,tryNum);
	}
	public String getUrl(String url,int tryTime){
		mtryTime = tryTime;
		 try{
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
			  }catch (Exception e) {  
				  if(--mtryTime>=0){
					  System.out.println("获取价格："+url+"时失败，剩余重试次数"+mtryTime+1);
					  return getUrl(url,mtryTime);
				  }else
				  e.printStackTrace();
			     }
		return htm_str;
	 }
}
