package com.ecust.spider.util;

import java.util.ArrayList;

import net.sf.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ecust.spider.mValue;
import com.ecust.spider.entity.Item;

public class FetchYHDItemUtil 
{
	private static int trytime=3;
	public static Item getYHDItemInfo(String url)
	{
		return getYHDItemInfo(url,trytime);
	}
	
	public static Item getYHDItemInfo(String url,int tryTime)
	{
		tryTime--;
		Document doc;
		
		String host = "www.yhd.com";
		Item currentItem=new Item();
		String itemID;
		try 
		{		
            doc = Jsoup.connect(url).get();
            //FileUtil.toTxt(doc.toString());
            itemID=getYHDItemID(doc);
            currentItem = new Item(getYHDItemName(doc),host,itemID,getYHDItemPrice(itemID),
            		getYHDItemCategory(doc),url,getYHDItemImageUrl(doc),getYHDItemDetail(doc)
    				);
            
		}
		catch (Exception e) 
		{
			if(tryTime>=0)
			{
				System.out.println("重新获取item信息,url为"+url+"item"+currentItem.toString()+" 剩余次数："+(tryTime+1));
				return getYHDItemInfo(url,tryTime);
			}
			else
			{
				System.out.println("获取Item失败，url为"+url+"item"+currentItem.toString());
				e.printStackTrace();
				mValue.errNum++;
				return null;
			}
		}
		return currentItem;
	}
	
	private static String getYHDItemID(Element doc)
	{
		Elements idInfo=doc.select("input#productMercantId").select("input[value]");
		String id=null;
		if(idInfo.size()!=0)
		{
			id=idInfo.attr("value");
			return id;
		}
		return id;
	}
	
	private static String getYHDItemName(Element doc)
	{
		Elements nameInfo=doc.select("input#cnName").select("input[value]");
		String name=null;
		
		if(nameInfo.size()!=0)
		{
			name=nameInfo.attr("value").replace('\'','_');
			return name;
		}
		return name;
	}
	
	private static String getYHDItemPrice(String itemID)
	{
		if(itemID==null)
		{
			return null;
		}
		String price=null;
		if(!itemID.isEmpty())
		{
			price=new FetchUtil().getUrl("http://gps.yhd.com/restful/detail?mcsite=1&provinceId=1&pmId="+itemID);
		}
		
		if(price.length()>1)
		{
	        JSONObject jsonObject=JSONObject.fromObject(price);
	        return jsonObject.get("yhdPrice").toString();
		}
		return null;
	}
	
	private static String getYHDItemImageUrl(Element doc)
	{
		Elements imgUrlInfo=doc.select("div.detail_wrap").select("img#J_prodImg");
		String imgUrl=null;
		if(imgUrlInfo.size()!=0)
		{
			imgUrl=imgUrlInfo.attr("src");
			return imgUrl;
		}
		return imgUrl;
	}
	
	private static String getYHDItemDetail(Element doc)
	{
		ArrayList<String> detail=new ArrayList<String>();
		Elements detailInfo=doc.select("div.detail_wrap").select("td[style=vertical-align:top]").select("li");
		
		if(detailInfo.size()!=0)
		{
			for(Element info : detailInfo)
			{
				detail.add(info.text().replace('\'','_'));
			}
			return detail.toString();
		}
		detailInfo=doc.select("div.des").select("div.descon").select("div.desitem").select("dd[title]");
		
		if(detailInfo.size()!=0)
		{
			for(Element info :detailInfo)
			{
				detail.add(info.text().replace('\'','_'));
			}
			return detail.toString();
		}
		return "暂无介绍";
	}
	
	private static ArrayList<String> getYHDItemCategory(Element doc)
	{
		Elements categoryInfo=doc.select("div.detail_wrap").select("div[data-tpa=DETAIL_CRUMBBOX]").select("a[onclick]");
		ArrayList<String> categoryList=new ArrayList<String>();
		if(categoryInfo.size()!=0)
		{
			for(Element info : categoryInfo)
			{
				categoryList.add(info.textNodes().get(0).text());
			}
			return categoryList;
		}
		return categoryList;
	}
	
}
