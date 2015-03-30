package com.ecust.spider;

import com.ecust.spider.util.SqlUtil;

public class mValue {
	//此类保存运行状态
	private static boolean dbIsClear=false;	//数据库是否清空
	private static SqlUtil mSqlUtil;
	public static int count = 0;
	public static int threadNo=0;
	public static int totleNum;
	public static int doneNum = 0;
	public static boolean getDbState() {
		return dbIsClear;
	}

	public static void setDbState(boolean dbIsClear) {
		mValue.dbIsClear = dbIsClear;
	}

	public static SqlUtil getmSqlUtil() {
		return mSqlUtil;
	}

	public static void setmSqlUtil(SqlUtil mSqlUtil) {
		mValue.mSqlUtil = mSqlUtil;
	}



}
