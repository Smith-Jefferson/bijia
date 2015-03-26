package com.test.spider.util;

import java.util.Queue;

import com.test.spider.mValue;

public class JdFetcher {
	public static void fetchQueue(Queue<String> mQueue){
		if (mValue.getDbState()){
		while(!mQueue.isEmpty()){
			String url=mQueue.poll();
			if (!BloomFilter.ifNotContainsSet(url)) {
				JsoupUtil.ExcuteItemQueue(url);
				System.out.println(++mValue.doneNum+"/"+mValue.totleNum);
				}else{
					System.out.println("数据库未清空！");
				}
			}
		}
	}
}
