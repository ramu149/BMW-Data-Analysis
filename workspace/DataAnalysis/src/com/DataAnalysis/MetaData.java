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

public class MetaData {
	private static final String USER_AGENT="Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36";
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	private static final String FILE_HEADER = "ForumName, ThreadCount, TotalPosts";
	Properties prop=new Properties();
	String propFileName="configBMW.properties";
	InputStream inputStream =getClass().getClassLoader().getResourceAsStream(propFileName);
	public List<String> forumPages(String url) throws IOException{
		List<String> forumPages = new ArrayList<String>();
		prop.load(inputStream);	
		try{
		Connection connection=Jsoup.connect(url).userAgent(USER_AGENT);
		Document htmlDocument =connection.timeout(10000).get();	
		if (connection.response().statusCode()==200) 
			{
			 Elements links = htmlDocument.getElementsByClass(prop.getProperty("class1"));//class1
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
				//File dir = new File(prop.getProperty("misc6"));
				//dir.mkdir();
				}
		//File f=new File(prop.getProperty("misc6")+"/Forum.text");
		//FileOutputStream fos = new FileOutputStream(f,true);
		//PrintWriter pw = new PrintWriter(fos);	
		//for(String URL:forumPages){
			//pw.println(URL);
	//}
		//pw.flush();
		//pw.close();
		System.out.println(forumPages);
		}
		catch(Exception e){
		
		}
		return forumPages;
	}
	public  void getMetaData( String url){
		List<String> forums = new ArrayList<String>();
		try{
			forums = forumPages(url);
			File dir = new File(prop.getProperty("misc6"));
			dir.mkdir();
			File f=new File(prop.getProperty("misc6")+"/Forum.csv");
			FileOutputStream fos = new FileOutputStream(f,true);
			PrintWriter pw = new PrintWriter(fos);
			pw.print(FILE_HEADER);
			pw.print(NEW_LINE_SEPARATOR);
			for(String forum:forums){
				Connection connection=Jsoup.connect(forum).userAgent(USER_AGENT);
				Document htmlDocument =connection.timeout(100000).get();
				Elements table = htmlDocument.getElementsByClass("tborder");
				Elements tCount = table.select("td");
				int thCount = 0;
				int tpCount = 0; 
				for(Element threads:tCount){
					char character='\0';
					char character1='\0';
					//System.out.println((int)character);
					if(threads.getElementsByClass("alt2").text().toString().length()>1 && !threads.getElementsByClass("alt2").text().toString().contains(" ")&& !threads.getElementsByClass("alt2").text().toString().contains("BIMMERPOST Universal Forums"))
						character1 = threads.text().toString().charAt(0);
					if(threads.getElementsByClass("alt1").text().toString().length()>1 && !threads.getElementsByClass("alt1").text().toString().contains(" ")&& !threads.getElementsByClass("alt1").text().toString().contains("BIMMERPOST Universal Forums"))
						character = threads.text().toString().charAt(0); 
					if((int)character>=48&&(int)character<=57 && !threads.getElementsByClass("alt1").text().toString().contains(" ")&& !threads.getElementsByClass("alt1").text().toString().contains("BIMMERPOST Universal Forums"))
						thCount+=(Integer.parseInt(threads.getElementsByClass("alt1").text().toString().replace(",", "")));
					if((int)character1>=48&&(int)character1<=57 && !threads.getElementsByClass("alt2").text().toString().contains(" ")&& !threads.getElementsByClass("alt2").text().toString().contains("BIMMERPOST Universal Forums"))	
						tpCount+=(Integer.parseInt(threads.getElementsByClass("alt2").text().toString().replace(",", "")));	
				}
				System.out.println(forum);
				System.out.println(thCount-60434);
				System.out.println(tpCount-1647613);
				pw.print(forum);
				pw.print(COMMA_DELIMITER);
				pw.print(thCount-60434);
				pw.print(COMMA_DELIMITER);
				pw.print(tpCount-1647613);
				pw.print(NEW_LINE_SEPARATOR);
				pw.flush();
			}
			pw.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String args[]){
		MetaData m = new MetaData();
		m.getMetaData("http://www.bimmerpost.com/");
		
	}
}
