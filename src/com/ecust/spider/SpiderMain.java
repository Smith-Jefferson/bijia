package com.ecust.spider;


import java.util.ArrayList;
import java.util.Queue;

import com.ecust.spider.util.JsoupUtil;
import com.ecust.spider.util.SpiderTask;
import com.ecust.spider.util.SqlUtil;
import com.ecust.spider.util.ThreadCarveUtil;

public class SpiderMain {
	
	final static String jdMapUrl = mConstants.JD_MAP_URL;
	static  String htm_str;
	static String html;
	static ArrayList<String> mArrayListJD;
	static ArrayList<String> mArrayListYHD;
	static ArrayList<Queue<String>> treadQueues;
	public static void main(String[] args) {
		SqlUtil mSqlUtil = new SqlUtil(mConstants.DB_NAME,mConstants.DB_USER_NAME,mConstants.DB_USER_PASS);
		mValue.setmSqlUtil(mSqlUtil);
		mArrayListJD = JsoupUtil.praseArray(mConstants.JD_MAP_URL);
		mArrayListYHD = JsoupUtil.praseArray(mConstants.YHD_MAP_URL);
		mValue.totleNum=mArrayListJD.size();
//		treadQueues = ThreadCarveUtil.Carve(mArrayListJD,ThreadCarveUtil.SIZE);
		treadQueues = ThreadCarveUtil.Carve(mArrayListJD,ThreadCarveUtil.NUM);
		for (Queue<String> mQueue:treadQueues){
			System.out.println(mQueue.size());
			new Thread(new SpiderTask(jdMapUrl,mQueue)
			{}){}.start();
		}
		treadQueues = null;
	}
}
