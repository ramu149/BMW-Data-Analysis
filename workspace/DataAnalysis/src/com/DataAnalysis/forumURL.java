package com.DataAnalysis;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.httpclient.Header;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class forumURL {
	private static final String USER_AGENT="Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36";
	Document htmlDocument;
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	private static final String FILE_HEADER = "ID, ForumName, TotalPosts";
	Properties prop=new Properties();
	String propFileName="configNissan.properties";
	InputStream inputStream =getClass().getClassLoader().getResourceAsStream(propFileName);
	public void forumPages(String url) throws IOException{
		List<String> forumPages = new ArrayList<String>();
		prop.load(inputStream);	
		try{
		Connection connection=Jsoup.connect(url).userAgent(USER_AGENT);
		htmlDocument =connection.timeout(10000).get();	
		if (connection.response().statusCode()==200) 
			{
			 Elements links = htmlDocument.getElementsByClass("nav");//class1
			 System.out.println(links);
				Elements linksOnPage =links.select(prop.getProperty("t1"));//getElementsByTag(prop.getProperty("t1"));//t1
				for( Element link : linksOnPage){
					if(link.getAllElements() != null){
					forumPages.add(link.absUrl("href"));
					forumPages.remove(prop.getProperty("g1"));
					forumPages.remove(prop.getProperty("g2"));
					forumPages.remove(prop.getProperty("g3"));
					forumPages.remove(prop.getProperty("g4"));
					forumPages.remove(prop.getProperty("g5"));
					forumPages.remove(prop.getProperty("g6"));
					forumPages.remove(prop.getProperty("g7"));
					forumPages.remove(prop.getProperty("g8"));
					forumPages.remove(prop.getProperty("g9"));
					forumPages.remove(prop.getProperty("g10"));
				}
				}
				File dir = new File(prop.getProperty("misc6"));
				dir.mkdir();
				}
		File f=new File(prop.getProperty("misc6")+"/Forum.csv");
		FileOutputStream fos = new FileOutputStream(f,true);
		PrintWriter pw = new PrintWriter(fos);	
		pw.print(FILE_HEADER);
		pw.println(NEW_LINE_SEPARATOR);
		for(String URL:forumPages){
			Connection connection1=Jsoup.connect(URL).userAgent(USER_AGENT);
			htmlDocument =connection1.timeout(10000).get();	
			Elements threads = htmlDocument.select("table.tborder.vs_subforum");
			System.out.println(threads);
			Elements threadTitle =threads.select("a.forum_title");//getElementsByTag(prop.getProperty("t1"));//t1
			Elements totalThreads = threads.select("td.alt1.thread_count");
			Elements totalPosts = threads.select("td.alt2.reply_count");
			if(threads.select("table.tborder.vs_subforum").text()!=""){
				//Elements threadCount =threads.select("alt1 thread_count");//getElementsByTag(prop.getProperty("t1"));//t1
				for(int i =0;i<threadTitle.size()&&i<totalThreads.size()&&i<totalPosts.size();i++){
					Element e=threadTitle.get(i);
					Element e1 = totalThreads.get(i);
					Element e2 = totalPosts.get(i);
					pw.print(e.text().toString());
					pw.print(COMMA_DELIMITER);
					pw.print(e1.text().toString().replace(",", ""));
					pw.print(COMMA_DELIMITER);
					pw.print(e2.text().toString().replace(",", ""));
					pw.print(NEW_LINE_SEPARATOR);
					//System.out.println(e.text().toString().replace(",", ""));
					//pw.print(e.select("table.tborder.vs_subforum").text().toString());
					pw.flush();
				}
			}
		}
		pw.flush();
		pw.close();
		
		}
		catch(Exception e){
		
		}
	}
}
