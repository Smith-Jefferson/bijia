package com.ecust.spider;


import java.util.ArrayList;
import java.util.Queue;

import com.ecust.spider.task.SpiderTask;
import com.ecust.spider.util.JsoupUtil;
import com.ecust.spider.util.SqlUtil;
import com.ecust.spider.util.ThreadCarveUtil;

public class SpiderMain {
	
	public static void main(String[] args) {
		SqlUtil mSqlUtil = new SqlUtil(mConstants.DB_NAME,mConstants.DB_USER_NAME,mConstants.DB_USER_PASS);
		mValue.setmSqlUtil(mSqlUtil);
		
		new Thread(new SpiderTask(mConstants.JD)
		{}){}.start();
		
		new Thread(new SpiderTask(mConstants.YHD)
		{}){}.start();
		
	}
}
