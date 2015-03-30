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
	public static  Item getJDItemInfo(String url,int tryTime)
	{
		tryTime--;
		Document doc;
		String host = "www.jd.com";
		Item currentItem=new Item();
		String itemID;
		try 
		{		
            doc = Jsoup.connect(url).get();
            itemID=getJDItemID(doc);
            currentItem = new Item(getJDItemName(doc),host,itemID,getJDItemPrice(itemID),
            		getJDItemCategory(doc),url,getJDItemImageUrl(doc),getJDItemDetail(doc)
    				);
            
		}
		catch (Exception e) 
		{
			if(tryTime>=0)
			{
				System.out.println("重新获取item信息,url为"+url+"item"+currentItem.toString());
				return getJDItemInfo(url,tryTime);
			}
			else
			{
				System.out.println("获取Item失败，url为"+url+"item"+currentItem.toString());
				e.printStackTrace();
				return null;
			}
			
			
		}
		return currentItem;
	}
	
	//获得商品名
	private static String getJDItemName(Element doc)
	{
		 Elements ItemInfo = doc.select("div.m-item-inner").select("div#itemInfo").select("div#name").select("h1");
		 if(ItemInfo.size()!=0)
		 {
			 return ItemInfo.text().replace('\'','_');
		 }
		
		 ItemInfo=doc.select("div#product-intro").select("div#name").select("h1");
		 if(ItemInfo.size()!=0)
		 {
			 return ItemInfo.text().replace('\'','_');
		 }
		
         return null;
	}
	
	
	//获得图片
	private	static String getJDItemImageUrl(Element doc)
	{
		 Elements ImgInfo = doc.select("div#preview").select("div.jqzoom").select("img[src]");
		 if(ImgInfo.size()!=0)
		 {
			 return ImgInfo.first().attr("src");
		 }
		 ImgInfo = doc.select("div#preview").select("div#spec-n1").select("img[src]");
		 if(ImgInfo.size()!=0)
		 {
			 return ImgInfo.first().attr("src");
		 }
		 return null;
		 
	}
	
	//商品ID
	private	static String getJDItemID(Element doc)
	{
		Elements idElement= doc.select("div.m-item-inner").select("div#summary-price").select("div.dd").select("a[data-sku]");
		String id=null;
		if(idElement.size()==0)
		{
			idElement= doc.select("div.clearfix").select("div.dd").select("a[data-sku]");
		}
		if(idElement.size()!=0)
		{
			id=idElement.attr("data-sku");
		}
		
		if(id!="")
		{
			return id;
		}
		else
		{
			return null;
		}
		
	}
	
	//商品价格
	private	static String getJDItemPrice(String itemID)
	{
		if(itemID==null)
		{
			return null;
		}
		String price=null;
		if(!itemID.isEmpty())
		{
			price=new FechUtil().getUrl("http://p.3.cn/prices/get?skuid=J_"+itemID+"&tpye=1");
		}
		
		if(price.length()>1)
		{
			price=price.substring(1,price.length()-2);
	        JSONObject jsonObject=JSONObject.fromObject(price);
	        return jsonObject.get("p").toString();
		}
		return null;
        
	}
	
	//商品类别
	private	static ArrayList<String> getJDItemCategory(Element doc)
	{
		ArrayList<String> itemCategory=new ArrayList<String>();
        Elements categoryInfo = doc.select("div.breadcrumb").select("a[clstag]");
        if(categoryInfo.size()==0)
        {
        	categoryInfo = doc.select("div.breadcrumb").select("a[href]");
        }
        for(Element category : categoryInfo)
        {
        	String itemCategoryInfo=category.text();
        	if(itemCategoryInfo!=null)
        	{
        		itemCategoryInfo=itemCategoryInfo.replace('\'','_');
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
		if(detailList.size()==0)
        {
			detailList = doc.select("div#product-detail-1").select("div.p-parameter").select("ui.p-parameter-list");
        }
		for(Element detailInfo : detailList)
		{
			String Detail=detailInfo.text();
			Detail=Detail.replace('\'','_');
			detail.add(Detail);
		}
		return detail.toString();
	}

}
