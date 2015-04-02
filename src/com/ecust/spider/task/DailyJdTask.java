package com.ecust.spider.task;

import java.util.ArrayList;
import java.util.Queue;
import java.util.TimerTask;

import com.ecust.spider.mConstants;
import com.ecust.spider.mValue;
import com.ecust.spider.util.JsoupUtil;
import com.ecust.spider.util.ThreadCarveUtil;

public class DailyJdTask extends TimerTask {

	static ArrayList<String> mArrayListJD;
	static ArrayList<Queue<String>> treadQueues;
	@Override
	public void run() {
		mArrayListJD = JsoupUtil.praseArray(mConstants.JD_MAP_URL);
		mValue.totleNum=mArrayListJD.size();
		treadQueues = ThreadCarveUtil.Carve(mArrayListJD,ThreadCarveUtil.NUM);
		for (Queue<String> mQueue:treadQueues){
			System.out.println(mQueue.size());
			new Thread(new JdSpiderExecuter(mQueue)
			{}){}.start();
		}
	}

}
