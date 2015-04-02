package com.ecust.spider.task;

import java.util.Queue;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

import com.ecust.spider.mConstants;
import com.ecust.spider.mValue;
import com.ecust.spider.api.Task;
import com.ecust.spider.util.BloomFilter;
import com.ecust.spider.util.QueueFetcher;
import com.ecust.spider.util.JsoupUtil;

public class SpiderExecuter implements Task {
	//京东专用爬去线程
	private static String table=mConstants.JD_TABLE;
	private Queue<String> mQueue;
	
	public SpiderExecuter(Queue<String> mQueue){	
		this.mQueue = mQueue;
	}
	
	@Override
	public void run() {
		if(!mValue.getDbState()&&mValue.getmSqlUtil()!=null){
			mValue.getmSqlUtil().deleteAll(mConstants.JD_TABLE);	//清空数据库
			mValue.getmSqlUtil().deleteAll(mConstants.YHD_TABLE);
		}
		QueueFetcher.fetchQueue(mQueue);
	}

}
