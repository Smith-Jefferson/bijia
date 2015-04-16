package com.ecust.spider.fetcher.listFetcher;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.ecust.spider.Constants;
import com.ecust.spider.Value;
import com.ecust.spider.api.ListFetcher;
import com.ecust.spider.bean.model.Item;
import com.ecust.spider.fetcher.itemFetcher.JDItemFetcher;
import com.ecust.spider.util.BloomFilter;

public class JDListFetcher extends ListFetcher {

	@Override
	public void ExcuteList(String oneListUrl) {
		String[] Listclass = { "goods-list-v1", "plist" };
		Map<String, String> pageClass = new HashMap<String, String>();
		pageClass.put("p-num", "page=");
		pageClass.put("pagin", "p=");
		excuteGeneralList(oneListUrl, Listclass, pageClass, Constants.JD, 0);
	}

	@Override
	public String GetNextPage(String nextPageUrl, String page, int currentI) {
		// 拼接地址
		String[] splitString = nextPageUrl.split("&");
		nextPageUrl = "";
		for (int i = 0; i < splitString.length - 1; i++) {
			if (splitString[i].contains(page)
					&& splitString[i + 1].contains("JL")) {
				page += currentI;
				nextPageUrl += page + "&";
			} else {
				nextPageUrl += splitString[i] + "&";
			}

		}
		nextPageUrl += splitString[splitString.length - 1];
		return nextPageUrl;
	}

}
