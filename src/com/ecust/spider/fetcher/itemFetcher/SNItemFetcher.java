package com.ecust.spider.fetcher.itemFetcher;

import java.util.ArrayList;

import net.sf.json.JSONObject;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ecust.commen.util.FetchUtil;
import com.ecust.spider.api.ItemFetcher;
import com.ecust.spider.bean.model.Item;

public class SNItemFetcher extends ItemFetcher {
	private int snIDlength=18;
	@Override
	public Item getItemInfo(String url) {
		return getItemInfo(url,MAX_TRY);
	}

	@Override
	public Item getItemInfo(String url, int tryTime) {
		
		return super.getGeneralItemInfo("http://www.suning.com/", url, tryTime);
	}

	@Override
	public String getItemName(Element doc) {
		Elements nameInfo=doc.select("input#ga_itemDataBean_description_name");
		String name=null;
		if(nameInfo.size()!=0)
		{
			name=nameInfo.attr("value");
			return name;
		}
		return name;
	}

	@Override
	public String getItemImageUrl(Element doc) {
		Elements imgInfo=doc.select("div.imgview").select("div.imgview-thumb-main").select("div.imgview-thumb-main").select("li");
		String imgUrl=null;
		if(imgInfo.size()!=0)
		{
			imgUrl=imgInfo.first().select("img").attr("src-large");
			return imgUrl;
		}
		return imgUrl;
	}

	@Override
	public String getItemID(Element doc) {
		Elements idInfo=doc.select("input#ga_itemDataBean_itemID");
		String id=null;
		if(idInfo.size()!=0)
		{
			id=idInfo.attr("value");
			id=getCompleteSNID(id,snIDlength);
			return id;
		}
		return id;
	}

	@Override
	public String getItemPrice(String itemID) {
		if (itemID == null) {
			return null;
		}
		String price = null;
		
		if (!itemID.isEmpty()) {
			price = new FetchUtil().getUrl("http://www.suning.com/webapp/wcs/stores/ItemPrice/"
					+ itemID + "__9264_12113_1.html");
		}

		if (price.length() > 1) {
			String statusInfo=null;
			statusInfo = price.substring(15, price.length() - 3);
			JSONObject statusInfoObj = JSONObject.fromObject(statusInfo);
			
			String saleInfo= statusInfoObj.get("saleInfo").toString();
			saleInfo=saleInfo.substring(1,saleInfo.length()-1);
			JSONObject saleInfoObj=JSONObject.fromObject(saleInfo);
			
			price=saleInfoObj.get("netPrice").toString();
			return price;
		}
		return null;
	}

	@Override
	public ArrayList<String> getItemCategory(Element doc) {
		// TODO Auto-generated method stub
		Elements categoryInfo=doc.select("div.breadcrumb").select("a#category1");
		String[] category=null;
		ArrayList<String> categoryList=new ArrayList<String>();
		if(categoryInfo.size()!=0)
		{
			category=categoryInfo.text().split("/");
			for(String str: category)
			{
				categoryList.add(str);
			}
			
			return categoryList;
		}
		return categoryList;
	}

	@Override
	public String getItemDetail(Element doc) {
		Elements detailInfo=doc.select("div#kernelParmeter").select("li");
		if(detailInfo.size()==0)
		{
			detailInfo=doc.select("div.bookcon-main").select("dl#bookCon_1").select("li");
		}
		String detailStr=null;
		for(Element element: detailInfo)
		{
			if(detailStr==null)
			{
				detailStr=element.text();
			}
			else
			{
				detailStr=detailStr+" "+element.text()+" ";
			}
			
		}
		return detailStr;
	}
	
	private String getCompleteSNID(String id,int idlength)
	{
		int currentlength=id.length();
		int zeroNum=idlength-currentlength;
		String completeID=new String();
		for(int i=0;i<zeroNum;i++)
		{
			completeID+="0";
		}
		completeID+=id;
		return completeID;
		
	}


}
