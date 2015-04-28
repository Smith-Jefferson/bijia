package com.ecust.spider.util;

import com.ecust.spider.Constants;
import com.ecust.spider.api.ItemFetcher;
import com.ecust.spider.fetcher.itemFetcher.JDItemFetcher;
import com.ecust.spider.fetcher.itemFetcher.SNItemFetcher;
import com.ecust.spider.fetcher.itemFetcher.YHDItemFetcher;

public class ItemFetcherFactory {
	public static ItemFetcher getItemFetcher(int type) {
		switch (type) {
		case Constants.JD:
			return new JDItemFetcher();
		case Constants.YHD:
			return new YHDItemFetcher();
		case Constants.SN:
			return new SNItemFetcher();
		default:
			break;
		}
		return null;
	}
}
