package com.ecust.spider.task;


import java.util.Calendar;
import java.util.Date;
import java.util.Queue;
import java.util.Timer;

import com.ecust.spider.api.Task;

public class SpiderTask implements Task {
	private int type;
	
	public static final int JD = 0;
	public static final int YHD = 1;
	
	
	public SpiderTask(int type){
		this.type = type;
	}
	@Override
	public void run() {
		dailyTask();
	}
	
	private void dailyTask()
	{

		int hour=5;
		int min=0;
		int sec=0;

		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, min);
		calendar.set(Calendar.SECOND, sec);
		Date time = calendar.getTime();

		Timer timer = new Timer();
		
		switch(type){
		case JD:
	    timer.schedule(new DailyJdTask(), time);
	    break;
		case YHD:
		    timer.schedule(new DailyJdTask(), time);
		    break;
		 default:
			 break;
		}
	}

}
