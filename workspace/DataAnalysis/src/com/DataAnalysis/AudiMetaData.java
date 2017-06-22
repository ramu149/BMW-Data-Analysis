package com.DataAnalysis;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AudiMetaData {
	private static final String USER_AGENT="Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36";
	Document htmlDocument;
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	private static final String FILE_HEADER = "ID, ForumName, TotalPosts";
	Properties prop=new Properties();
	String propFileName="configAudi.properties";
	InputStream inputStream =getClass().getClassLoader().getResourceAsStream(propFileName);
	public void forumPages(String url) throws IOException{
		
		prop.load(inputStream);	
		try{
			System.out.println("Test1");
		File f=new File(prop.getProperty("misc6")+"/Forum.csv");
		FileOutputStream fos = new FileOutputStream(f,true);
		PrintWriter pw = new PrintWriter(fos);	
		pw.print(FILE_HEADER);
		pw.println(NEW_LINE_SEPARATOR);
			Connection connection=Jsoup.connect(url).userAgent(USER_AGENT);
			htmlDocument =connection.timeout(10000).get();	
			Elements threads = htmlDocument.select("table.tborder");
			//System.out.println(threads);
			Elements threadTitle =threads.select("table.tbody.tr.td.alt1Active.div.a.strong");//getElementsByTag(prop.getProperty("t1"));//t1
			Elements totalThreads = threads.select("tbody.tr.td.alt1");
			Elements totalPosts = threads.select("tbody.tr.td.alt2");
			System.out.println("Test2");
			System.out.println(threadTitle.size());
			System.out.println(totalThreads.size());
			System.out.println(totalPosts.size());
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
					System.out.println("Test3");
					//System.out.println(e.text().toString().replace(",", ""));
					//pw.print(e.select("table.tborder.vs_subforum").text().toString());
					pw.flush();
				}
			
		
		pw.flush();
		pw.close();
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}
