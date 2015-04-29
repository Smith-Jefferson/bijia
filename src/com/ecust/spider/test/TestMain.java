package com.ecust.spider.test;

import com.ecust.spider.Constants;
import com.ecust.spider.Value;
import com.ecust.spider.fetcher.listFetcher.SNListFetcher;
import com.ecust.spider.task.SpiderTask;
import com.ecust.spider.util.QueueFetcher;
import com.ecust.spider.util.SqlUtil;

public class TestMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*SqlUtil mSqlUtil = new SqlUtil(Constants.DB_NAME,Constants.DB_USER_NAME,Constants.DB_USER_PASS);
		Value.setmSqlUtil(mSqlUtil);
		if(!Value.getDbState()&&Value.getmSqlUtil()!=null){
			Value.getmSqlUtil().deleteAll(Constants.JD_TABLE);	//清空数据库
			Value.getmSqlUtil().deleteAll(Constants.YHD_TABLE);
		}*/
		
		SNListFetcher listFetcher=new SNListFetcher();
		listFetcher.ExcuteList("http://list.suning.com/1-262691-1-0-0-9264.html");
		
//		new Thread(new Runnable()
//		{
//
//			@Override
//			public void run() {
//				QueueFetcher.ExcuteItemQueue("http://list.jd.com/list.html?cat=670,671,672");
//			}}){}.start();
//			new Thread(new Runnable()
//			{
//
//				@Override
//				public void run() {
//					QueueFetcher.ExcuteItemQueue("http://list.yhd.com/c21969-0-0/");
//				}}){}.start();
//				new Thread(new Runnable()
//				{
//
//					@Override
//					public void run() {
//						QueueFetcher.ExcuteItemQueue("http://list.jd.com/list.html?cat=9987,653,655");
//					}}){}.start();
//					new Thread(new Runnable()
//					{
//
//						@Override
//						public void run() {
//							QueueFetcher.ExcuteItemQueue("http://list.yhd.com/c21307-0-0/");
//						}}){}.start();
	}

}
