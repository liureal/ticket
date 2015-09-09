package org.ticket.core;

import java.io.IOException;

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

public class EaticketWebCrawlingTest {
	@Test
	public void connectWebTest(){
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			HttpGet httpGet = new HttpGet("http://www.eaticket.com/all_performance/1/Show.showtime2/asc/6");
			
			System.out.println("Executing request " + httpGet.getRequestLine());
			CloseableHttpResponse response = client.execute(httpGet);
			System.out.println("--------------------------------------");
			System.out.println(response.getStatusLine());
			
			// response entity
			HttpEntity httpEntity = response.getEntity();
			Document document = Jsoup.parse(httpEntity.getContent(), "utf-8", "http://www.eaticket.com");
			Element element = document.getElementById("performanceList");
			Elements element2 = element.getElementsByClass("performanceListItem");
			for (Element element3 : element2) {
				Elements ele1 = element3.getElementsByTag("a");
				String str = ele1.get(0).attr("href");
				System.out.println(str);
			}
			System.out.println(element2.text());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
