package com.ecust.spider.api;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ecust.spider.util.ListFilter;

public abstract class ListFetcher {
	protected static final int MAX_TRY = 3;

	public abstract void ExcuteList(String oneListUrl);

	public abstract String GetNextPage(String nextPageUrl, String page,
			int currentI);
	

	
	protected HashMap<Element, String> GetMaxpageUrl(Document doc,
			Map<String, String> pageclass, String[] removeString) {
		Iterator<Entry<String, String>> iterator = pageclass.entrySet()
				.iterator();
		Elements elements = null;
		Elements links = null;
		String page = "";
		while (iterator.hasNext()) {
			Entry<String, String> oneEntry = iterator.next();
			elements = doc.getElementsByClass(oneEntry.getKey());

			links = elements.select("a[href]");
			page = oneEntry.getValue();
			if (links != null && !links.isEmpty()) {
				break;
			}

		}
		Elements listlink = new Elements();
		for (int i = 0; i < links.size(); i++) {
			if (!links.get(i).text().contains(removeString[0])
					&& !links.get(i).text().contains(removeString[1])
					&& !links.get(i).text().contains(removeString[2]))
				listlink.add(links.get(i));
		}
		HashMap<Element, String> nextMap = new HashMap<Element, String>();
		nextMap.put(listlink.last(), page);
		return nextMap;
	}
	
	protected Document Getdoc(String oneListUrl, int tryTime) {
		int mTryTime = --tryTime;

		Document doc = null;
		Connection conn = null;
		try {
			// 获取item页，总共有多少页
			conn = Jsoup.connect(oneListUrl);
			conn.header(
					"User-Agent",
					"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2 Googlebot/2.1");
			
			doc = conn.timeout(200 * 1000).get();// 如果页面没有抓全，重新抓取
			if (doc == null && tryTime >= 0) {
				System.out.println("解析list：" + oneListUrl + "的 DOC 时出错！剩余尝试次数："
						+ tryTime);
				return Getdoc(oneListUrl, mTryTime);
			} else if (doc == null) {
				System.out.println("解析list：" + oneListUrl + "的 DOC 时出错！");
			}
		} catch (Exception e) {
			if (tryTime >= 0) {
				System.out.println("解析list：" + oneListUrl + "的时出错！剩余尝试次数："
						+ tryTime);
				return Getdoc(oneListUrl, mTryTime);
			} else {
				System.out.println("解析list：" + oneListUrl + "时出错！");
				e.printStackTrace();
			}
		}
		return doc;
	}
	
	protected LinkedBlockingQueue<String> GetItemList(String url,
			Document document, String[] classString) {
		LinkedBlockingQueue<String> Itemlist = new LinkedBlockingQueue<String>();
		try {
			Document doc = null;
			if (document == null) {
				doc = Getdoc(url, MAX_TRY);
			} else {
				doc = document;
			}
			Elements links = null;
			for (int j = 0; j < classString.length; j++) {
				links = doc.getElementsByClass(classString[j])
						.select("a[href]");
				if (links != null && !links.isEmpty()) {
					break;
				}
			}
			if (links == null || links.isEmpty()) {
				links = doc.select("a[href]");
			}
			for (Element link : links) {
				if (ListFilter.UrlJudge(link.attr("abs:href"), ListFilter.ITEM)) {
					Itemlist.add(link.attr("abs:href"));
				}
			}
			// System.out.println(Itemlist.toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return Itemlist;
	}
	
}
