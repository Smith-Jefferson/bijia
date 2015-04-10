package com.ecust.spider.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;

import javax.naming.InitialContext;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ecust.spider.mConstants;
import com.ecust.spider.mValue;
import com.ecust.spider.entity.Item;

public class FetchListUtil {
	
	public static final int MAX_INT = 3;
	
	public static void ExcuteYhdList(String oneListUrl){
		try{
		Document doc=FetchListUtil.Getdoc(oneListUrl,MAX_INT);
		String[] Listclass={"mod_search_list"};
		Map<String, String> pageClass=new HashMap<String, String>();
		pageClass.put("turn_page","p") ;
		String[] removeString={"页",".","确定"};
		LinkedBlockingQueue<String> itemList=new LinkedBlockingQueue<String>();
		//得到当前页面的ItemList
		try {
			itemList=GetItemList("", doc, Listclass);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("获取一号店当前网页"+oneListUrl+"的item列表失败");
		}
		
		//获取分页的最大页数和分页符号，初始化下一页
//		HashMap<Element, String> nextMap=fetchListUtil.GetMaxpageUrl(doc,pageClass,removeString);
		int Urlend=1;String page="";String nextPage="";
		try {
			HashMap<Element, String> nextMap=GetMaxpageUrl(doc,pageClass,removeString);
			Urlend=Integer.parseInt(nextMap.entrySet().iterator().next().getKey().text().trim());
			page=nextMap.entrySet().iterator().next().getValue();
			nextPage=nextMap.entrySet().iterator().next().getKey().absUrl("href");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("当前页不存在分页"+oneListUrl);
		}
	
		//循环获取当前页所包含的item地址，获得详情后写入数据库
		int length="http://item.yhd.com/item/".length();
		try {
		for(int currentI=2;currentI<=Urlend;currentI++){
			//调用处理item详情页面的方法
			while (! itemList.isEmpty()) {
				String url=itemList.poll();
				if (!BloomFilter.ifNotContainsSet(url) && url.length()>length) {
					//获取一号店商品详情入库
					Item item =FetchYHDItemUtil.getYHDItemInfo(url);
					if(item==null){
						continue;
					}
					if(mValue.getmSqlUtil()!=null){
						mValue.getmSqlUtil().addItem(item,mConstants.YHD_TABLE);
					}else{
						System.out.print("获取数据库实例失败！");
					}
/*					++i;
					System.out.println(url+"     "+i);//测试
*/					
				}
				
			}
			itemList.clear();
			nextPage=GetYhdNextPage(nextPage,page,currentI); 
				itemList=GetItemList(nextPage, null, Listclass);
			} 	
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("当前分页url获取可能存在问题"+oneListUrl);
			e.printStackTrace();
		}
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("一号店LIST发生未知错误"+oneListUrl);
			e.printStackTrace();
		}
		
		
		
	}
	public static HashMap<Element, String> GetMaxpageUrl(Document doc,Map<String, String>pageclass,String[] removeString){
		Iterator<Entry<String, String>> iterator=pageclass.entrySet().iterator();
		Elements elements=null;
		Elements links=null;
		String page = "";
		while (iterator.hasNext()) {
			Entry<String, String> oneEntry = iterator.next();
			elements=doc.getElementsByClass(oneEntry.getKey());
			
			links=elements.select("a[href]");
			page=oneEntry.getValue();
			if (links!=null && !links.isEmpty()) {
				break;
			}
			
		}
		Elements listlink=new Elements();
		for (int i=0;i<links.size();i++) {
			if(!links.get(i).text().contains(removeString[0])&&!links.get(i).text().contains(removeString[1])&&!links.get(i).text().contains(removeString[2]))
				listlink.add(links.get(i));
		}
		HashMap<Element,String> nextMap=new HashMap<Element,String>();
		nextMap.put(listlink.last(), page);
		return nextMap;
		
	}
	public static  String GetYhdNextPage(String nextPageUrl,String page,int currentI){
		//拼接地址
				String[] splitString=nextPageUrl.split("-");
				nextPageUrl="";
				for (int i = 0; i < splitString.length-1; i++) {
					if (splitString[i].contains(page)&& splitString[i+1].equals("price")) {	
						page +=currentI;
						nextPageUrl +=page+"-";
					}
					else {
						nextPageUrl +=splitString[i]+"-";
					}
					
				}
				nextPageUrl +=splitString[splitString.length-1];
				return nextPageUrl;
		
		
	}
	
	public static void ExcuteJdList(String oneListUrl){
		try{
		Document doc=Getdoc(oneListUrl,MAX_INT);
		String[] Listclass={"goods-list-v1","plist"};
		Map<String, String> pageClass=new HashMap<String, String>();
		pageClass.put("p-num", "page=");
		pageClass.put("pagin", "p=");
		String[] removeString={"页",".","确定"};
		//得到当前页面的ItemList
		LinkedBlockingQueue<String> itemList=new LinkedBlockingQueue<String>();
		try {
			 itemList=GetItemList("", doc, Listclass);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("获取京东当前网页"+oneListUrl+"的item列表失败");
			e.printStackTrace();
		}
		
		//获取分页的最大页数和分页符号，初始化下一页
//		HashMap<Element, String> nextMap=GetMaxpageUrl(doc,pageClass,removeString);
		int Urlend=1;String page="";String nextPage="";
		try {
			HashMap<Element, String> nextMap=GetMaxpageUrl(doc,pageClass,removeString);
			Urlend=Integer.parseInt(nextMap.entrySet().iterator().next().getKey().text().trim());
			page=nextMap.entrySet().iterator().next().getValue();
			nextPage=nextMap.entrySet().iterator().next().getKey().absUrl("href");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("当前页不存在分页"+oneListUrl);
		}
		
		//循环获取当前页所包含的item地址，获得详情后写入数据库
		for(int currentI=2;currentI<=Urlend;currentI++){
			//调用处理item详情页面的方法
			while (! itemList.isEmpty()) {
				String url=itemList.poll();
				if (!BloomFilter.ifNotContainsSet(url)) {
					Item item =	FetchJDItemUtil.getJDItemInfo(url);
					if(item==null){
						continue;
					}
					if(mValue.getmSqlUtil()!=null){
						mValue.getmSqlUtil().addItem(item,mConstants.JD_TABLE);
					}else{
						System.out.print("获取数据库实例失败！");
					}
				}
				
			}
			itemList.clear();
			try {
				nextPage=FetchListUtil.GetJdNextPage(nextPage,page,currentI);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("获取京东分页失败");
				e.printStackTrace();
			}
			try {
				itemList=GetItemList(nextPage, null, Listclass);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("获取京东当前网页"+oneListUrl+"的item列表失败");
				e.printStackTrace();
			}
			
			
		}
		
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("京东list发生未知错误 "+oneListUrl);
			e.printStackTrace();
		}
		
	}
	public static String GetJdNextPage(String nextPageUrl,String page,int currentI) {
		//拼接地址
		String[] splitString=nextPageUrl.split("&");
		nextPageUrl="";
		for (int i = 0; i < splitString.length-1; i++) {
			if (splitString[i].contains(page)&& splitString[i+1].contains("JL")) {	
				page +=currentI;
				nextPageUrl +=page+"&";
			}
			else {
				nextPageUrl +=splitString[i]+"&";
			}
			
		}
		nextPageUrl +=splitString[splitString.length-1];
		return nextPageUrl;
	}
	
	//解决连接超时问题，多次连接
	public static Document Getdoc(String oneListUrl,int tryTime){
		int mTryTime = --tryTime;
		
		Document doc=null;
		try {
			//获取item页，总共有多少页
			doc = Jsoup.connect(oneListUrl).get();
				doc = Jsoup.connect(oneListUrl).get(); //如果页面没有抓全，重新抓取
		if(doc==null&&tryTime>=0){
			System.out.println("解析list："+oneListUrl+"的 DOC 时出错！剩余尝试次数："+tryTime);
			return Getdoc(oneListUrl,mTryTime);
		}else if(doc==null){
			System.out.println("解析list："+oneListUrl+"的 DOC 时出错！");
		}
		} catch (Exception e) {
			if(tryTime>=0){
				System.out.println("解析list："+oneListUrl+"的时出错！剩余尝试次数："+tryTime);
				return Getdoc(oneListUrl,mTryTime);
			}else{
			System.out.println("解析list："+oneListUrl+"时出错！");
			e.printStackTrace();
			}
		}
		return doc;
	}
	
	//获取item列表
		public static LinkedBlockingQueue<String> GetItemList(String url,Document document,String[] classString){
			LinkedBlockingQueue<String> Itemlist=new LinkedBlockingQueue<String>();
			try {
				Document doc=null;
				if(document == null){
					doc = Getdoc(url,MAX_INT);
				}
				else {
					doc=document;
				}
				Elements links =null;
				for (int j = 0; j < classString.length; j++) {
					links= doc.getElementsByClass(classString[j]).select("a[href]");
					if (links !=null && !links.isEmpty()) {
						break;
					}
				}
				if (links ==null || links.isEmpty()) {
					links=doc.select("a[href]");
				}
	            for (Element link : links) { 
	            	if(ListFilter.UrlJudge(link.attr("abs:href"), ListFilter.ITEM)){
	            		Itemlist.add(link.attr("abs:href"));
	            	}
	            } 
	        //  System.out.println(Itemlist.toString());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			return Itemlist;
		}
	
	

}
