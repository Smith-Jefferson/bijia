package com.ecust.spider.util;

import java.util.Queue;

import com.ecust.spider.mValue;

public class QueueFetcher {
	
	public static final String Jd="jd";
	public static final String Yhd="yhd";
	
	public static void fetchQueue(Queue<String> mQueue){
		if (mValue.getDbState()){
		while(!mQueue.isEmpty()){
			String url=mQueue.poll();
			if (!BloomFilter.ifNotContainsSet(url)) {
				ExcuteItemQueue(url);
				System.out.println(++mValue.doneNum+"/"+mValue.totleNum);
				}else{
					System.out.println("数据库未清空！");
				}
			}
		}
	}
	
	public static void ExcuteItemQueue(String oneListUrl){
		//初始化信息，item列表Itemlist，分页div的class，内容列表的div的class
		
	//	FetchListUtil.ExcuteYhdList(doc, oneListUrl);
		if( oneListUrl.contains(Jd)){
			FetchListUtil.ExcuteJdList(oneListUrl);}
		else if(oneListUrl.contains(Yhd)){
			FetchListUtil.ExcuteYhdList(oneListUrl);
		}
		else{
			System.out.println("暂时还无法处理的网站");
		}
	}
}
