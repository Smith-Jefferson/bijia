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
import com.ecust.spider.fetcher.itemFetcher.YHDItemFetcher;
import com.ecust.spider.util.BloomFilter;

public class YHDListFetcher extends ListFetcher {

	@Override
	public void ExcuteList(String oneListUrl) {
		try {
			Document doc = Getdoc(oneListUrl, MAX_TRY);
			String[] Listclass = { "mod_search_list" };
			Map<String, String> pageClass = new HashMap<String, String>();
			pageClass.put("turn_page", "p");
			String[] removeString = { "页", ".", "确定" };
			LinkedBlockingQueue<String> itemList = new LinkedBlockingQueue<String>();
			// 得到当前页面的ItemList
			try {
				itemList = GetItemList("", doc, Listclass);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("获取一号店当前网页" + oneListUrl + "的item列表失败");
			}

			// 获取分页的最大页数和分页符号，初始化下一页
			// HashMap<Element, String>
			// nextMap=fetchListUtil.GetMaxpageUrl(doc,pageClass,removeString);
			int Urlend = 1;
			String page = "";
			String nextPage = "";
			try {
				HashMap<Element, String> nextMap = GetMaxpageUrl(doc,
						pageClass, removeString);
				Urlend = Integer.parseInt(nextMap.entrySet().iterator().next()
						.getKey().text().trim());
				page = nextMap.entrySet().iterator().next().getValue();
				nextPage = nextMap.entrySet().iterator().next().getKey()
						.absUrl("href");
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("当前页不存在分页" + oneListUrl);
			}

			// 循环获取当前页所包含的item地址，获得详情后写入数据库
			int length = "http://item.yhd.com/item/".length();
			try {
				for (int currentI = 2; currentI <= Urlend; currentI++) {
					// 调用处理item详情页面的方法
					while (!itemList.isEmpty()) {
						String url = itemList.poll();
						if (!BloomFilter.ifNotContainsSet(url)
								&& url.length() > length) {
							// 获取一号店商品详情入库
							Item item = new YHDItemFetcher().getItemInfo(url);
							if (item == null) {
								continue;
							}
							try {
								if (Value.getmSqlUtil() != null) {
									Value.getmSqlUtil().addItem(item,
											Constants.YHD_TABLE);
								} else {
									System.out.print("获取数据库实例失败！");
								}
							} catch (Exception e) {
								// TODO: handle exception
								System.out.println("获取item失败");
								e.printStackTrace();
							}
							/*
							 * ++i; System.out.println(url+"     "+i);//测试
							 */
						}

					}
					itemList.clear();
					nextPage = GetNextPage(nextPage, page, currentI);
					itemList = GetItemList(nextPage, null, Listclass);
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("当前分页url获取可能存在问题" + oneListUrl);
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("一号店LIST发生未知错误" + oneListUrl);
			e.printStackTrace();
		}
	}

	@Override
	public String GetNextPage(String nextPageUrl, String page, int currentI) {
		// 拼接地址
				String[] splitString = nextPageUrl.split("-");
				nextPageUrl = "";
				for (int i = 0; i < splitString.length - 1; i++) {
					if (splitString[i].contains(page)
							&& splitString[i + 1].equals("price")) {
						page += currentI;
						nextPageUrl += page + "-";
					} else {
						nextPageUrl += splitString[i] + "-";
					}

				}
				nextPageUrl += splitString[splitString.length - 1];
				return nextPageUrl;
	}

}
