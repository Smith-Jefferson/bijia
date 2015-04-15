package com.ecust.spider.util;

import com.ecust.spider.api.ListFetcher;
import com.ecust.spider.fetcher.listFetcher.JDListFetcher;
import com.ecust.spider.fetcher.listFetcher.YHDListFetcher;

public class ListFetcherFactory {

	public static final String Jd = "jd";
	public static final String Yhd = "yhd";

	public static ListFetcher getListFetcher(String oneListUrl) {
		if (oneListUrl.contains(Jd)) {
			return new JDListFetcher();
		} else if (oneListUrl.contains(Yhd)) {
			return new YHDListFetcher();
		} else {
			System.out.println("暂时还无法处理的网站");
		}
		return null;
	}

}
