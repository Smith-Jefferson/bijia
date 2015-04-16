package com.ecust.spider.task;

import java.util.ArrayList;
import java.util.Queue;
import java.util.TimerTask;

import com.ecust.spider.Constants;
import com.ecust.spider.Value;
import com.ecust.spider.util.JsoupUtil;
import com.ecust.spider.util.ThreadCarveUtil;

public class DailyTask extends TimerTask {

	static ArrayList<String> mArrayList;
	static ArrayList<Queue<String>> treadQueues;
	private int type;
	private String mapUrl;

	public DailyTask(int type) {
		this.type = type;
	}

	public DailyTask() {
	}

	@Override
	public void run() {
		if (!Value.getDbState() && Value.getmSqlUtil() != null) {
			Value.getmSqlUtil().deleteAll(Constants.JD_TABLE); // 清空数据库
			Value.getmSqlUtil().deleteAll(Constants.YHD_TABLE);
		}
//		switch (type) {
//		case Constants.JD:
//			mapUrl = Constants.JD_MAP_URL;
//			break;
//		case Constants.YHD:
//			mapUrl = Constants.YHD_MAP_URL;
//			break;
//		default:
//			break;
//		}
//		addMapToQueue(Constants.JD_MAP_URL);
		addMapToQueue(Constants.YHD_MAP_URL);
		for (int i = 0; i < Constants.THREAD_NUM; i++) {
			new Thread(new SpiderExecuter() {
			}) {
			}.start();
		}
		System.out.println(Value.totalQueue.size());
	}

	private void addMapToQueue(String map) {
		mArrayList = JsoupUtil.praseArray(map);
		Value.totleNum += mArrayList.size();
		Value.addQueue(mArrayList);
	}

}
