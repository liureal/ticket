package org.ticket.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.ticket.vo.TicketBody;
import org.ticket.vo.TicketInfo;

public class EveryItemTest {
	@Test
	public void everyItemTest(){

		CloseableHttpClient client = HttpClients.createDefault();
		try {
			HttpGet httpGet = new HttpGet("http://www.eaticket.com/ticket/25757");
			
			System.out.println("Executing request " + httpGet.getRequestLine());
			CloseableHttpResponse response = client.execute(httpGet);
			System.out.println("--------------------------------------");
			System.out.println(response.getStatusLine());
			
			// response entity
			HttpEntity httpEntity = response.getEntity();
			Document document = Jsoup.parse(httpEntity.getContent(), "utf-8", "http://www.eaticket.com");
			Element element = document.getElementById("price_tbl");
			Elements element2 = element.getElementsByTag("td");
			TicketBody body = new TicketBody();
			// 第一个td中是时间
			body.setMatchDate(element2.get(0).text());
			// 第二个td是票据信息
			List<TicketInfo> infoList = new ArrayList<TicketInfo>();
			String ticketStr = element2.get(1).text();
			char[] cs = ticketStr.toCharArray();
			StringBuffer convertBuffer = new StringBuffer();
			for (char c : cs) {
				if (c == ' ') {
					convertBuffer.append(" ");
				}else {
					convertBuffer.append(c);
				}
			}
			String[] infos = convertBuffer.toString().split("    ");
			for (String string : infos) {
				TicketInfo info = new TicketInfo();
				info.setPrice(string.substring(0, string.indexOf("(")));
				info.setStatus(string.substring(string.indexOf("(")+1, string.indexOf(")")));
				infoList.add(info);
			}
			for (TicketInfo ticket : infoList) {
				System.out.print("票价："+ticket.getPrice() + "\t");
				System.out.println("状态："+ticket.getStatus());
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
}
