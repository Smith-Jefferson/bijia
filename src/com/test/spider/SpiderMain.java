package com.test.spider;


import java.util.ArrayList;
import java.util.Queue;

import com.test.spider.util.JsoupUtil;
import com.test.spider.util.SpiderTask;
import com.test.spider.util.SqlUtil;
import com.test.spider.util.ThreadCarveUtil;

public class SpiderMain {
	
	final static String jdMapUrl = mConstants.JD_MAP_URL;
	static  String htm_str;
	static String html;
	static ArrayList<String> mArrayList;
	static ArrayList<Queue<String>> treadQueues;
	public static void main(String[] args) {
		SqlUtil mSqlUtil = new SqlUtil(mConstants.DB_NAME,mConstants.DB_USER_NAME,mConstants.DB_USER_PASS);
		mValue.setmSqlUtil(mSqlUtil);
		mArrayList = JsoupUtil.praseArray(mConstants.JD_MAP_URL);
		mValue.totleNum=mArrayList.size();
		treadQueues = ThreadCarveUtil.Carve(mArrayList);
		for (Queue<String> mQueue:treadQueues){
			new Thread(new SpiderTask(jdMapUrl,mQueue)
			{}){}.start();
		}
		treadQueues = null;
//		new Thread(new SpiderTask(jdMapUrl)
//		{}){}.start();

	}
}
