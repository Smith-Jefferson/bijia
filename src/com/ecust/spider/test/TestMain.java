package com.ecust.spider.test;

import com.ecust.spider.mConstants;
import com.ecust.spider.mValue;
import com.ecust.spider.task.SpiderTask;
import com.ecust.spider.util.QueueFetcher;
import com.ecust.spider.util.SqlUtil;

public class TestMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SqlUtil mSqlUtil = new SqlUtil(mConstants.DB_NAME,mConstants.DB_USER_NAME,mConstants.DB_USER_PASS);
		mValue.setmSqlUtil(mSqlUtil);
		if(!mValue.getDbState()&&mValue.getmSqlUtil()!=null){
			mValue.getmSqlUtil().deleteAll(mConstants.JD_TABLE);	//清空数据库
			mValue.getmSqlUtil().deleteAll(mConstants.YHD_TABLE);
		}
		new Thread(new Runnable()
		{

			@Override
			public void run() {
				QueueFetcher.ExcuteItemQueue("http://list.jd.com/list.html?cat=670,671,672");
			}}){}.start();
			new Thread(new Runnable()
			{

				@Override
				public void run() {
					QueueFetcher.ExcuteItemQueue("http://list.yhd.com/c21969-0-0/");
				}}){}.start();
				new Thread(new Runnable()
				{

					@Override
					public void run() {
						QueueFetcher.ExcuteItemQueue("http://list.jd.com/list.html?cat=9987,653,655");
					}}){}.start();
					new Thread(new Runnable()
					{

						@Override
						public void run() {
							QueueFetcher.ExcuteItemQueue("http://list.yhd.com/c21307-0-0/");
						}}){}.start();
	}

}
