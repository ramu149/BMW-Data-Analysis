
/****************************************************************************
Ramu Nerella
Data Intensive and Computing Ecosystems lab,
School of Computing, Clemson University  

Copyright Clemson University. All rights reserved.
Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/

package com.DataAnalysis;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class InfluentialUserCalculation {
	List<String> unusedLinks = new ArrayList<String>();
	Set<String> linksToProcess =  new HashSet<String>();
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	private static final String FILE_HEADER = "ResponseID,UserName,TimeStamp,No.ofPosts,ThreadURL";
	Set<String> totalUserPosts =  new HashSet<String>();
	private static final String USER_AGENT="Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36";
	Document htmlDocument;
	Properties prop=new Properties();
	String propFileName="configBMW.properties";
	InputStream inputStream =getClass().getClassLoader().getResourceAsStream(propFileName);
	 public List<String> categoryMetadata(String url){
		List<String> categories = new ArrayList<String>();
		List<String> totalThreads = new ArrayList<String>();
		List<String> totalPosts = new ArrayList<String>();
		List<String> categoryName = new ArrayList<String>();
		try{
			Connection connection=Jsoup.connect(url).userAgent(USER_AGENT);
			 htmlDocument =connection.timeout(10000).get();	
			 htmlDocument.getElementsByClass("smallfont").remove();
			 Elements navDivTag = htmlDocument.getElementsByClass("alt1Active");
			 Elements links = navDivTag.select("a");
			 for(Element link : links){
				categories.add(link.absUrl("href"));
				if (categories.size()==1)
					break;
				categoryName.add(link.getElementsByTag("strong").text());
			 	}
			 Elements table = htmlDocument.getElementsByClass("tborder");
			 Elements tableElement1 = table.select("td");
			 for(Element threadCount : tableElement1){
				if (threadCount.getElementsByClass("alt1").text().length()!=0 && !threadCount.getElementsByClass("alt1").text().contains("BMW"))
				totalThreads.add(threadCount.getElementsByClass("alt1").text());
			 	} 
			 Elements tableElement2 = table.select("td");
			 for(Element posts:tableElement2){
				 if (posts.getElementsByClass("alt2").text().length()!=0 && !posts.getElementsByClass("alt2").text().contains("a")&& !posts.getElementsByClass("alt2").text().contains(" ") )
					 totalPosts.add(posts.getElementsByClass("alt2").text());
			 }
			 File dirForums =new File("./BMWMetaData/"+url.substring( 7,url.length()-7));
				dirForums.mkdirs();
			}	catch(Exception e){
					e.printStackTrace();
				}
		return categories;
	}
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
								 if(threadUrls.size()==1)
									 break;
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
									if(threadUrls.size()==1)
										 break;
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
public void processMetaData(String url) throws IOException,MalformedURLException{	
	prop.load(inputStream);
	Map<String,Integer> degreeOfUser= new HashMap<String,Integer>();
	Map<String,Integer> repostsDegree = new HashMap<String,Integer>();
	List<String> categories = new ArrayList<String>();
	categories=categoryMetadata(url);
	File quoteDegree;
	int folderName=Integer.parseInt(prop.getProperty("misc5"));//misc5
	quoteDegree = new File(String.format(prop.getProperty("misc6")+"/%s/quoteDegree.csv", url.substring( folderName,url.length()-7)));
	FileOutputStream qos = new FileOutputStream(quoteDegree,true);
	PrintWriter qw = new PrintWriter(qos);
	String quoteHeader = "UserName,TaggedPosts,ThreadURL";
	qw.print(quoteHeader.toString());
	qw.print(NEW_LINE_SEPARATOR);
	File repostsDegre;
	int folderNameReposts=Integer.parseInt(prop.getProperty("misc5"));//misc5
	repostsDegre = new File(String.format(prop.getProperty("misc6")+"/%s/totalPosts.csv", url.substring( folderNameReposts,url.length()-7)));
	FileOutputStream ros = new FileOutputStream(repostsDegre,true);
	PrintWriter rw = new PrintWriter(ros);
	String quoteHeaderCSV = "UserName,TotalPostsInTheThread,TotalPostsInTheForum,ThreadURL";
	rw.print(quoteHeaderCSV.toString());
	rw.print(NEW_LINE_SEPARATOR);
	for(String category:categories){//forumDisplay.size()
		List<String> threadLinks = new ArrayList<String>();
		threadLinks=threadUrl(category);
		for(int k=0;k<threadLinks.size();k++){	
			if(k==1) break;
			try{
				List<String> multiplePages = new ArrayList<String>();
				List<String> totalPosts = new ArrayList<String>();
				URL u =new URL(threadLinks.get(k));	
				if(u.getPath().toString().contains(prop.getProperty("misc1"))){//misc1		
					Connection connection=Jsoup.connect( threadLinks.get(k)).userAgent(USER_AGENT);
					htmlDocument =connection.timeout(20000).get();
					if (connection.response().statusCode()==200) {	
						List<String> myArrayListResponse = new ArrayList<String>();	
						List<String> repost = new ArrayList<String>();
						List<String> myArrayList = new ArrayList<String>();
						int b=0;
						Elements links = htmlDocument.getElementsByClass(prop.getProperty("class9"));//class9
						if(htmlDocument.getElementsByClass(prop.getProperty("class4")).first()!=null && !htmlDocument.getElementsByClass(prop.getProperty("class4")).first().text().isEmpty()){//class4
							 CrawlingMultiplePages mPages = new CrawlingMultiplePages();
							 multiplePages=mPages.processMultilePages(threadLinks.get(k));
							 Elements uname1=htmlDocument.getElementsByClass(prop.getProperty("class6"));//class6
							for(Element E2:uname1){
								if(!E2.text().contains(prop.getProperty("misc2")))
									myArrayList.add(E2.text());
							}
							Elements lqw=links.select(prop.getProperty("t7"));//t7
							Element linksQestion = lqw.select(prop.getProperty("t3")).first();//t3
							String linkTextQuestion =linksQestion.text();
							int nofPosts=0;
							if(myArrayList.size()>0){
								Elements thead = htmlDocument.getElementsByClass("tborder");
								Elements nPosts = htmlDocument.getElementsByClass("postBitScoreItem");
								Elements totalposts= nPosts.select("div[title=\"Post Count\"]");
								for(Element e:totalposts){
									totalPosts.add(e.text());
								}
								Elements navDivTag = htmlDocument.getElementsByClass(prop.getProperty("class7"));
								Elements list1 = navDivTag.select(prop.getProperty("t5"));//t2	
								//list1.select("div.postquote").remove();
								//list1.select("div.smallfont").remove();
								int c=0;
								String name=null;
								repost.clear();
								for(Element E:list1){
									if(c<myArrayList.size())
									 name = myArrayList.get(c);
									myArrayListResponse.add(E.text());
									if(E.id().startsWith(prop.getProperty("misc8")))
									if(!E.getElementsByTag(prop.getProperty("t6")).isEmpty() && E.getElementsByTag(prop.getProperty("t6"))!=null)
									if(!E.select("strong").text().isEmpty()){
										repost.add(E.getElementsByClass("alt2").select("strong").first().text());
										
										}
									c++;
									}
								b=0;
								degreeOfUser.put(myArrayList.get(0),1);
								for(int n=1;n<myArrayListResponse.size();n++){
									if(myArrayListResponse.size()>0&&n<myArrayList.size()){
										if(!degreeOfUser.containsKey(myArrayList.get(n))) {
											degreeOfUser.put(myArrayList.get(n),1);
											}
											else {
											degreeOfUser.put(myArrayList.get(n), degreeOfUser.get(myArrayList.get(n))+1);
											}
								 		}
											}									
								for(int i=0;i<repost.size();i++){
									if(!repostsDegree.containsKey(repost.get(i)))
										repostsDegree.put(repost.get(i), 1);
									else 
										repostsDegree.put(repost.get(i), repostsDegree.get(repost.get(i))+1);
							 		}
								repost.clear();										
									nofPosts=nofPosts+myArrayList.size();
											}
								myArrayList.clear();
								myArrayListResponse.clear();	 
								for(int o=0;o<multiplePages.size();o++){
									Connection pageNavConnection=Jsoup.connect( multiplePages.get(o)).userAgent(USER_AGENT);
									Document pageNavHTML =pageNavConnection.timeout(20000).get();
									if (connection.response().statusCode()==200){
									        Elements uname=pageNavHTML.getElementsByClass(prop.getProperty("class6"));//class6
									        for(Element E1:uname){
									            if(!E1.text().contains(prop.getProperty("misc2")))
									                myArrayList.add(E1.text());
									               }
								Elements thead = pageNavHTML.getElementsByClass("tborder");
								Elements nPosts = pageNavHTML.getElementsByClass("postBitScoreItem");
								Elements totalposts= nPosts.select("div[title=\"Post Count\"]");
								for(Element e:totalposts){
									totalPosts.add(e.text());
								}
								Elements navDivTag = pageNavHTML.getElementsByClass(prop.getProperty("class7"));
								Elements list1 = navDivTag.select(prop.getProperty("t5"));//t2	
								int c=0;
								String name=null;
								repost.clear();
								for(Element E:list1){
								 if(c<myArrayList.size())
									name = myArrayList.get(c);
								myArrayListResponse.add(E.text());	
								if(E.id().startsWith(prop.getProperty("misc8")))
									if(!E.getElementsByTag(prop.getProperty("t6")).isEmpty() && E.getElementsByTag(prop.getProperty("t6"))!=null)
								if(!E.select("strong").text().isEmpty()){
									repost.add(E.getElementsByClass("alt2").select("strong").first().text());
									
								}
								c++;
								}
								b=0;
								degreeOfUser.put(myArrayList.get(0),1);
							for(int n=1;n<myArrayListResponse.size();n++){
								if(myArrayListResponse.size()>0&&n<myArrayList.size()){
									if(!degreeOfUser.containsKey(myArrayList.get(n))) {
										degreeOfUser.put(myArrayList.get(n),1);
										}
										else {
									
										degreeOfUser.put(myArrayList.get(n), degreeOfUser.get(myArrayList.get(n))+1);
										}
							 		}
									}
							for(int i=0;i<repost.size();i++){
								if(!repostsDegree.containsKey(repost.get(i)))
									repostsDegree.put(repost.get(i), 1);
								else 
									repostsDegree.put(repost.get(i), repostsDegree.get(repost.get(i))+1);
						 		}
							repost.clear();
								nofPosts=nofPosts+myArrayList.size();
										 }
								totalPosts.clear();
								myArrayList.clear();
								myArrayListResponse.clear();
									}	
								
								for(Map.Entry<String,Integer> entry : repostsDegree.entrySet()){
									qw.print(entry.getKey());
									qw.print(COMMA_DELIMITER);
									qw.print(entry.getValue());									
									qw.print(COMMA_DELIMITER);
									qw.print(threadLinks.get(k));									
									qw.print(NEW_LINE_SEPARATOR);
									
								}
								repostsDegree.clear();							
								for(Map.Entry<String,Integer> entry : degreeOfUser.entrySet()){
									rw.print(entry.getKey());
									rw.print(COMMA_DELIMITER);
									rw.print(entry.getValue());
									rw.print(COMMA_DELIMITER);
									rw.print(threadLinks.get(k));
									rw.print(NEW_LINE_SEPARATOR);
									
								}
								degreeOfUser.clear();
								totalPosts.clear();
								qw.flush();
								rw.flush();
								multiplePages.clear();
								URL u1=new URL(threadLinks.get(k+1));
								if(u.equals(u)==u1.equals(u1)){
								threadLinks.remove(k+1);
								}
								nofPosts=0;
							 	}
							else {
								Elements uname=htmlDocument.getElementsByClass(prop.getProperty("class6"));//class6
								for(Element E1:uname){
								    if(!E1.text().contains(prop.getProperty("misc2")))
								    myArrayList.add(E1.text());
								   }
								if(myArrayList.size()>0){
									Elements lq=links.select(prop.getProperty("t7"));//t2
									Element linksQestion1 = lq.select(prop.getProperty("t3")).first();//t3
									String linkTextQuestion1 =linksQestion1.text();
									Elements thead = htmlDocument.getElementsByClass("tborder");
									Elements nPosts = htmlDocument.getElementsByClass("postBitScoreItem");
									Elements totalposts= nPosts.select("div[title=\"Post Count\"]");
									Elements navDivTag = htmlDocument.getElementsByClass(prop.getProperty("class7"));
									Elements list1 = navDivTag.select(prop.getProperty("t5"));//t2	
									int c=0;
									String name=null;
									for(Element E:list1){
										//if(E.id().startsWith(prop.getProperty("misc8"))){
											//if(!E.getElementsByTag(prop.getProperty("t6")).isEmpty() && E.getElementsByTag(prop.getProperty("t6"))!=null)
												//E.getElementsByTag(prop.getProperty("t6")).parents().first().remove();
									 if(c<myArrayList.size())
										name = myArrayList.get(c);
								//	if (!name.isEmpty())
								//		degreeOfUserCounter.put(E.text(), name);
								//	else
								//		degreeOfUserCounter.put(E.text(), null);
									myArrayListResponse.add(E.text());	
									if(E.id().startsWith(prop.getProperty("misc8")))
										if(!E.getElementsByTag(prop.getProperty("t6")).isEmpty() && E.getElementsByTag(prop.getProperty("t6"))!=null)
									if(!E.select("strong").text().isEmpty()){
										repost.add(E.getElementsByClass("alt2").select("strong").first().text());
									}
									c++;
									}
									//System.out.println(degreeOfUserCounter);
									b=0;
									degreeOfUser.put(myArrayList.get(0),1);
								for(int n=1;n<myArrayListResponse.size();n++){
									if(myArrayListResponse.size()>0&&n<myArrayList.size()){
										if(!degreeOfUser.containsKey(myArrayList.get(n))) {
											degreeOfUser.put(myArrayList.get(n),1);
											}
											else {
										
											degreeOfUser.put(myArrayList.get(n), degreeOfUser.get(myArrayList.get(n))+1);
											}
								 		}
										}
								for(int i=0;i<repost.size();i++){
									if(!repostsDegree.containsKey(repost.get(i)))
										repostsDegree.put(repost.get(i), 1);
									else 
										repostsDegree.put(repost.get(i), repostsDegree.get(repost.get(i))+1);
							 		}
								for(Map.Entry<String,Integer> entry : repostsDegree.entrySet()){
									qw.print(entry.getKey());
									qw.print(COMMA_DELIMITER);
									qw.print(entry.getValue());									
									qw.print(COMMA_DELIMITER);
									qw.print(threadLinks.get(k));									
									qw.print(NEW_LINE_SEPARATOR);
									
								}
								repostsDegree.clear();
								for(Map.Entry<String,Integer> entry : degreeOfUser.entrySet()){
									rw.print(entry.getKey());
									rw.print(COMMA_DELIMITER);
									rw.print(entry.getValue());
									rw.print(COMMA_DELIMITER);
									rw.print(threadLinks.get(k));
									rw.print(NEW_LINE_SEPARATOR);
									
								}
								degreeOfUser.clear();
									for(Element e:totalposts){
										totalPosts.add(e.text());
									}
								
								}
								myArrayList.clear();
								myArrayListResponse.clear();	 
								//degreeOfUser.clear();
							}
						 }
					 else{
						 threadLinks.remove(url);
						 this.unusedLinks.add(url); 	
					}
						 rw.flush();
						 
					 }  
				} 
			catch(Exception e){
				e.printStackTrace();
				}	 	
			}
			System.out.print("The no.of threads processed: "+threadLinks.size());	
		}
	qw.close();
	rw.close();
}

}