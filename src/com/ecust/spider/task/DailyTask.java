package com.ecust.spider.task;

import java.util.ArrayList;
import java.util.Queue;
import java.util.TimerTask;

import com.ecust.spider.mConstants;
import com.ecust.spider.mValue;
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

	@Override
	public void run() {
		if (!mValue.getDbState() && mValue.getmSqlUtil() != null) {
			mValue.getmSqlUtil().deleteAll(mConstants.JD_TABLE); // 清空数据库
			mValue.getmSqlUtil().deleteAll(mConstants.YHD_TABLE);
		}
		switch (type) {
		case mConstants.JD:
			mapUrl = mConstants.JD_MAP_URL;
			break;
		case mConstants.YHD:
			mapUrl = mConstants.YHD_MAP_URL;
			break;
		default:
			break;
		}
		mArrayList = JsoupUtil.praseArray(mapUrl);
		mValue.totleNum += mArrayList.size();
		mValue.addQueue(mArrayList);
		for (int i = 0; i < mConstants.THREAD_NUM; i++) {
			new Thread(new SpiderExecuter() {
			}) {
			}.start();
		}
		System.out.println(mValue.totalQueue.size());
		// treadQueues = ThreadCarveUtil.Carve(mArrayList,ThreadCarveUtil.NUM);
		// for (Queue<String> mQueue:treadQueues){
		// System.out.println(mQueue.size());
		// new Thread(new SpiderExecuter(mQueue)
		// {}){}.start();
		// }
	}

}
