package org.ticket.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.ticket.vo.TicketBody;


public class EaTicketWebCrawling {
	public Document ticketBody(){
		Document document = null;
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			HttpGet httpGet = new HttpGet("http://www.eaticket.com/all_performance/1/Show.showtime2/asc/6");
			
			System.out.println("Executing request " + httpGet.getRequestLine());
			CloseableHttpResponse response = client.execute(httpGet);
			System.out.println("--------------------------------------");
			System.out.println(response.getStatusLine());
			
			// response entity
			HttpEntity httpEntity = response.getEntity();
			document = Jsoup.parse(httpEntity.getContent(), "utf-8", "http://www.eaticket.com/");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return document;
	}
	
	public List<String> allItemHref(Document document){
		List<String> hrefList = new ArrayList<String>();
		if (document == null) {
			return hrefList;
		}
		Element infoElement = document.getElementById("performanceList");
		Elements itemElements = infoElement.getElementsByClass("performanceListItem");
		// 一个元素代表一项赛事
		for (Element element : itemElements) {
			// 得到每项赛事的a标签
			Elements hrefElements = element.getElementsByTag("a");
			if (hrefElements.size() > 0) {
				// 以为标签是重复的，只需要取得第一个即可
				String itemHref = hrefElements.get(0).attr("href");
				hrefList.add(document.baseUri() +itemHref);
			}
		}
		return hrefList;
	}
	
	/**
	 * 获取所有赛事的链接，多线程同时访问
	 */
	public void accessMutiWebpage(List<String> webLinks){
		// Create an HttpClient with ThreadSafeClientConnManager
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(100);
		
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
	}
	
	static class GetThread implements Callable<TicketBody>{
		private final CloseableHttpClient httpClient;
		private final HttpContext context;
		private final HttpGet httpGet;
		private final int id;

		public GetThread(CloseableHttpClient httpClient, HttpContext context,
				HttpGet httpGet, int id) {
			this.httpClient = httpClient;
			this.context = context;
			this.httpGet = httpGet;
			this.id = id;
		}

		public TicketBody call() throws Exception {
			try {
				System.out.println(id + " - about to get something from " + httpGet.getURI());
				CloseableHttpResponse response = httpClient.execute(httpGet, context);
				System.out.println(id + " - get executed");
				HttpEntity httpEntity = response.getEntity();
				Document document = Jsoup.parse(httpEntity.getContent(), "utf-8", "http://www.eaticket.com/");
				
				
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
	}
}
