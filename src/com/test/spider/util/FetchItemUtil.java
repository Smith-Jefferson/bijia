package com.test.spider.util;

import java.io.IOException;
import java.util.ArrayList;

import net.sf.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.test.spider.entity.*;

public class FetchItemUtil {
	
	//获得京东商品信息
	public static  Item getJDItemInfo(String url)
	{ 
		Document doc;
		String host = "www.jd.com";
		Item currentItem = null;
		String itemID;
		try 
		{		
            doc = Jsoup.connect(url).get();
            itemID=getJDItemID(doc);
            currentItem = new Item(getJDItemName(doc),host,itemID,getJDItemPrice(itemID),
            		getJDItemCategory(doc),url,getJDItemImageUrl(doc),getJDItemDetail(doc)
    				);
//            System.out.println(currentItem);
		}
		catch (Exception e) 
		{
			System.out.println("获取Item时"+url+"发生错误");
			e.printStackTrace();
			return null;
		}
		return currentItem;
	}
	
	//获得商品名
	private static String getJDItemName(Element doc)
	{
		 Elements ItemInfo = doc.select("div.m-item-inner").select("div#itemInfo");
         Elements ItemNameElements=ItemInfo.select("div#name");
         return ItemNameElements.first().text();
	}
	
	
	//获得图片
	private	static String getJDItemImageUrl(Element doc)
	{
		 Elements ImgInfo = doc.select("div#preview").select("div.jqzoom").select("img[src]");
		 return ImgInfo.first().attr("src");
	}
	
	//商品ID
	private	static String getJDItemID(Element doc)
	{
		return doc.select("div.m-item-inner").select("div#summary-price").select("div.dd").select("a[data-sku]").attr("data-sku");
	}
	
	//商品价格
	private	static String getJDItemPrice(String itemID)
	{
		String price=new FechUtil().getUrl("http://p.3.cn/prices/get?skuid=J_"+itemID+"&tpye=1");
        price=price.substring(1,price.length()-2);
        JSONObject jsonObject=JSONObject.fromObject(price);
        return jsonObject.get("p").toString();
	}
	
	//商品类别
	private	static ArrayList<String> getJDItemCategory(Element doc)
	{
		ArrayList<String> itemCategory=new ArrayList<String>();
        Elements categoryInfo = doc.select("div.breadcrumb").select("a[clstag]");
        for(Element category : categoryInfo)
        {
        	String itemCategoryInfo=category.text();
        	if(itemCategoryInfo!=null)
        	{
        		itemCategory.add(itemCategoryInfo);
        	}
        }
        return itemCategory;
	}
	
	//详细信息
	private	static String getJDItemDetail(Element doc)
	{
		ArrayList<String> detail=new ArrayList<String>();
		Elements detailList=doc.select("ul.p-parameter-list");
		for(Element detailInfo : detailList)
		{
			String Detail=detailInfo.text();
			detail.add(Detail);
		}
		return detail.toString();
	}

}
