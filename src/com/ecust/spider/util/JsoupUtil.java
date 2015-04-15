package com.ecust.spider.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ecust.spider.Constants;
import com.ecust.spider.Value;
import com.ecust.spider.bean.model.Item;

public class JsoupUtil {
	
	
	public static LinkedHashMap<String,String> prase(String url){ 
		LinkedHashMap<String,String> mMap = new LinkedHashMap<String,String>();
		try {
            Document doc = Jsoup.connect(url).get(); 
            Elements lists = doc.getElementsByClass("mc");
            Elements links = lists.select("a[href]"); 
            for (Element link : links) { 
            	mMap.put(link.text(), link.attr("abs:href"));
            }  
            
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mMap;
	}
	
	public static LinkedBlockingQueue<String> praseQueue(String url){
		LinkedBlockingQueue<String> mQueue = new LinkedBlockingQueue<String>();
		try {
            Document doc = Jsoup.connect(url).get(); 
            Elements lists = doc.getElementsByClass("mc");
            Elements links = lists.select("a[href]"); 
            for (Element link : links) { 
            	if(ListFilter.UrlJudge(link.attr("abs:href"), ListFilter.LIST)){
            		mQueue.add(link.attr("abs:href"));
            	}
            }  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mQueue; 
	}
	
	public static ArrayList<String> praseArray(String url){
		ArrayList<String> mQueue = new ArrayList<String>();
		try {
            Document doc = Jsoup.connect(url).get(); 
            Elements lists = doc.getElementsByClass("mc");
            Elements links = lists.select("a[href]"); 
            for (Element link : links) { 
            	if(ListFilter.UrlJudge(link.attr("abs:href"), ListFilter.LIST)){
            		mQueue.add(link.attr("abs:href"));
            	}
            }  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mQueue; 
	}
	
	public static ArrayList<String> praseYHDArray(String url){
		ArrayList<String> mQueue = new ArrayList<String>();
		try {
            Document doc = Jsoup.connect(url).get(); 
            Elements lists = doc.getElementsByTag("em");
            Elements links = lists.select("span").select("a[href]"); 
            for (Element link : links) { 
            	if(link.attr("abs:href").startsWith("http://list.yhd.com")){
            		mQueue.add(link.attr("abs:href"));
            	}
            }  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mQueue; 
	}
	
	public static void main(String[] args){
		ArrayList<String> mArrayList = praseYHDArray("http://www.yhd.com/marketing/allproduct.html");
		System.out.print(mArrayList);
	}
	
	
	
	

}