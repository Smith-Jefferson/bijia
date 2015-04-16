package com.ecust.spider.util;

import java.util.Queue;

import com.ecust.spider.Value;
import com.ecust.spider.fetcher.listFetcher.JDListFetcher;
import com.ecust.spider.fetcher.listFetcher.YHDListFetcher;

public class QueueFetcher {

	public static void fetchQueue(Queue<String> mQueue) {
		if (Value.getDbState()) {
			while (!mQueue.isEmpty()) {
				String url = mQueue.poll();
				ListFetcherFactory.getListFetcher(url).ExcuteList(url);
				System.out.println(++Value.doneNum + "/" + Value.totleNum);
			}
		} else {
			System.out.println("数据库未清空！");
		}
	}
}
