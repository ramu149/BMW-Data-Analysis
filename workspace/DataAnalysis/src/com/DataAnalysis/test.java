package com.DataAnalysis;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class test {
	private static final String USER_AGENT="Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36";
	private List<String> junkLinks = new ArrayList<String>();
	Document htmlDocument;
	Properties prop=new Properties();
	String propFileName="configBMW.properties";
	InputStream inputStream =getClass().getClassLoader().getResourceAsStream(propFileName);
	List<String> unusedLinks = new ArrayList<String>();
	Set<String> linksToProcess =  new HashSet<String>();
	public List<String> threadUrl(String url) throws IOException{
		List<String> threadUrls = new ArrayList<String>();
		List<String> multiplePages = new ArrayList<String>();
		try{
			Connection connection=Jsoup.connect(url).userAgent(USER_AGENT);
			 htmlDocument =connection.timeout(50000).get();	
			 Elements navDivTag = htmlDocument.getElementsByClass("alt1");//class8
			 Elements links = navDivTag.select("a");//t1
			 if(!htmlDocument.getElementsByClass("pagenav").text().isEmpty() && htmlDocument.getElementsByClass("pagenav").first()!=null){//class4
				 for(Element link : links){
					 link.getElementsByClass("inlineimg").remove();
						link.getElementsByTag("span").remove();	
					 URL u =new URL(link.absUrl("href"));	
						
						
						 if(u.getPath().toString().contains("showthread")&&!link.absUrl("href").contains("page")&&link.id().startsWith("thread_title_")){//misc1 for showthread
							 threadUrls.add(link.absUrl("href"));
							}
						 else if(u.getPath().toString().contains("forumdisplay")) 
							 this.linksToProcess.add(link.absUrl("href"));
						 else{
							 this.unusedLinks.add(link.absUrl("href"));
							 }
					 }
				CrawlingMultiplePages multilePages = new  CrawlingMultiplePages();
				multiplePages=multilePages.processMultilePages(url);
				for(int y=0;y<multiplePages.size();y++)
				{
					Connection connection1=Jsoup.connect(multiplePages.get(y)).userAgent(USER_AGENT);
					htmlDocument =connection1.timeout(50000).get();	
					Elements navDivTag1 = htmlDocument.getElementsByClass(prop.getProperty("class8"));//class8
					Elements links1 = navDivTag1.select("a");//t1
					for(Element link : links1){
						link.getElementsByClass("inlineimg").remove();
						link.getElementsByTag("span").remove();	
						URL u =new URL(link.absUrl("href"));	
							
							if(u.getPath().contains(prop.getProperty("misc1"))&&!link.absUrl("href").contains("page")&&link.id().startsWith("thread_title_")){//misc1 for showthread
								threadUrls.add(link.absUrl("href"));
							}
							 else{
								this.unusedLinks.add(link.absUrl("href"));
									}
						    }
		}
			 	}
			 else{
				 for(Element link : links){
				 URL u =new URL(link.absUrl("href"));	
				 if(u.getPath().toString().contains(prop.getProperty("misc1"))&&!link.absUrl("href").contains("page")){//misc1 for showthread
					 threadUrls.add(link.absUrl("href"));	
				 }
				 else{
					 this.unusedLinks.add(link.absUrl("href"));	
				 }
			 }
		}	 	
		}	catch(Exception e){
			e.printStackTrace();
			}	
		System.out.println(threadUrls.size());
		return threadUrls;
	}
	public static void main(String args[]) throws IOException{
		test t = new test();
		
		System.out.println(t.threadUrl("http://www.6post.com/forums/forumdisplay.php?f=133&order=desc&page=2"));
	}
	
}
