package com.ecust.spider.task;

import java.util.Queue;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

import com.ecust.spider.Constants;
import com.ecust.spider.Value;
import com.ecust.spider.api.Task;
import com.ecust.spider.util.BloomFilter;
import com.ecust.spider.util.QueueFetcher;
import com.ecust.spider.util.JsoupUtil;

public class SpiderExecuter implements Task {
	// 京东专用爬去线程
	private static String table = Constants.JD_TABLE;
	private Queue<String> mQueue;

	public SpiderExecuter(Queue<String> mQueue) {
		this.mQueue = mQueue;
	}

	public SpiderExecuter() {
		this.mQueue = Value.totalQueue;
	}

	@Override
	public void run() {
		QueueFetcher.fetchQueue(mQueue);
	}

}
