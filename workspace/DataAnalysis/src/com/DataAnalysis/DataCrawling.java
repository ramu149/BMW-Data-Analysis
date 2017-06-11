
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
import java.text.SimpleDateFormat;
import java.util.*;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.NullPointerException;
import java.lang.IndexOutOfBoundsException;
import java.net.MalformedURLException;
import java.net.URL;
public class DataCrawling {
	private static final String USER_AGENT="Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36";
	private List<String> junkLinks = new ArrayList<String>();
	Document htmlDocument;
	Properties prop=new Properties();
	String propFileName="configBenz.properties";
	InputStream inputStream =getClass().getClassLoader().getResourceAsStream(propFileName);
	List<String> unusedLinks = new ArrayList<String>();
	Set<String> linksToProcess =  new HashSet<String>();
	/*
	 * Gets the URL from the command line arguments and returns List<String> of URLs to the subsequent method
	 * for processing the Categories.
	 * */
	@SuppressWarnings("unchecked")
	public List<String> forumDisplayPages(String url){
		List<String> forumDisplay = new ArrayList<String>();
		try{
			Connection connection=Jsoup.connect(url).userAgent(USER_AGENT);
			 htmlDocument =connection.timeout(10000).get();			 
			 Elements navDivTag = htmlDocument.getElementsByClass(prop.getProperty("class2"));//class2
			 Elements links = navDivTag.select(prop.getProperty("t1"));//t1
			 for(Element link : links){
				forumDisplay.add(link.absUrl("href"));
			 	}
			 forumDisplay.remove("http://www.benzworld.org/forums/payments.php");
			 forumDisplay.remove("http://www.benzworld.org/forums/payments.php");
			 forumDisplay.remove("http://www.benzworld.org/forums/payments.php");
			 System.out.println(forumDisplay);
			 Iterator<String> itr = forumDisplay.iterator();
			 while(itr.hasNext()){
				 String str = itr.next();
			
				 if(str.endsWith("&f=133")||str.endsWith("&f=36")||str.endsWith("&f=272")||str.endsWith("&f=4")||str.endsWith("&f=353")||str.endsWith("&f=8")
							||str.endsWith("&f=306")||str.endsWith("&f=62")||str.endsWith("&f=170")||str.endsWith("&f=467")||str.endsWith("&f=86")||str.endsWith("&f=56")||str.endsWith("&f=7"))
							itr.remove();
			 
			 }
		
			 System.out.println(forumDisplay);
			 if(forumDisplay.size()>0){
				 int subString=Integer.parseInt(prop.getProperty("misc5"));//misc5	
				 File dirForums1 =new File(prop.getProperty("misc6")+"/"+url.substring( subString,url.length()-1));
				 dirForums1.mkdirs();
			 	} 
			}	catch(Exception e){
				e.printStackTrace();
				}
	
		return forumDisplay;
	}
	/*
	 * threadUrl method will get Category URLs from forumDisplayPages
	 * and crawl each URL to capture Thread URLs
	 */
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
	/*
	 * processMetaData method will get thread URLs from threadUrl method
	 * and crawl each URL to capture thread request/reponse along with user information 
	 */
	public void processMetaData(String url) throws IOException,MalformedURLException{
		prop.load(inputStream);
		File f, f1;
		
			System.out.println("Forum URL: "+url);
			List<String> forumDisplay = new ArrayList<String>();
			forumDisplay=forumDisplayPages(url);
			int subString=Integer.parseInt(prop.getProperty("misc5"));//misc5
			
			f = new File(String.format(prop.getProperty("misc6")+"/%s/ThreadDisplay.txt", url.substring( subString,url.length()-1)));
			f1 = new File(String.format(prop.getProperty("misc6")+"/%s/TimeStamps.txt", url.substring( subString,url.length()-1)));
			
			FileOutputStream fos = new FileOutputStream(f,true);
			FileOutputStream fos1 = new FileOutputStream(f1,true);
			
			 PrintWriter pw = new PrintWriter(fos);
			 PrintWriter pw1 = new PrintWriter(fos1);
			 
			for(String category:forumDisplay){//forumDisplay.size()
				List<String> threadsLink = new ArrayList<String>(threadUrl(category));
			for(int k=0;k<threadsLink.size();k++){
				String thread=threadsLink.get(k);
				 URL u =new URL(thread);
			 try{	
				List<String> multiplePages = new ArrayList<String>();	
				 if(u.getPath().toString().contains(prop.getProperty("misc1"))){//misc1
				     Connection connection=Jsoup.connect( thread).userAgent(USER_AGENT);
					 htmlDocument =connection.timeout(30000).get();
					 if (connection.response().statusCode()==200) {	
					
						 	List<String> myArrayListResponse = new ArrayList<String>();	
						 	List<String> myArrayList = new ArrayList<String>();
						 	Elements links = htmlDocument.getElementsByClass(prop.getProperty("class9"));//class9
						 	if(htmlDocument.getElementsByClass(prop.getProperty("class4")).first()!=null && !htmlDocument.getElementsByClass(prop.getProperty("class4")).first().text().isEmpty()){//class4
						 	//List<String> locationOfUser = new ArrayList<String>();
						 	Elements location = htmlDocument.getElementsByClass("alt2");
						 	Elements loc	= location.select("smallfont").after("div[style=\"height:6px;\"]");
						
							Elements uname1=htmlDocument.getElementsByClass(prop.getProperty("class6"));//class6
							for(Element E2:uname1){
								if(!E2.text().contains(prop.getProperty("misc2")))
								myArrayList.add(E2.text());
									}
							Elements lqw=links.select(prop.getProperty("t7"));//t7
							Element linksQestion = lqw.select(prop.getProperty("t3")).first();//t3
							String linkTextQuestion =linksQestion.text();
						
								/*Elements thead = htmlDocument.getElementsByClass("tborder.vbseo_like_postbit.user-post");//bmw::tborder
								List<String> timeStamp = new ArrayList<String>();
								for(Element e: thead){
									if (e.id().startsWith("post"))

										timeStamp.add(e.select("span[itemprop=dateCreated]").first().text());//bmw::timeStamp.add(e.select("td").first().text().substring(6));
								}
								for(String ts:timeStamp){
									if (ts.startsWith("Yesterday")){
										Calendar calendar = Calendar.getInstance();
									    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
									    calendar.add(Calendar.DATE, -1);
									    Date yesterday = calendar.getTime();
									    pw1.println(ts.replace("Yesterday", dateFormat.format(yesterday).toString()));
									    }
									else if(ts.startsWith("Today")){
										Calendar calendar = Calendar.getInstance();
									    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
									    Date today = calendar.getTime();
									    pw1.println(ts.replace("Today", dateFormat.format(today).toString()));
									}
									else {
										pw1.println(ts);
									}
								}
								pw1.flush();*/
							
								Elements navDivTag = htmlDocument.getElementsByClass(prop.getProperty("class7"));
								Elements list1 = navDivTag.select(prop.getProperty("t5"));//t2	
								
								myArrayListResponse.clear();
								for(Element E:list1){
										if(E.id().startsWith(prop.getProperty("misc8"))){
											if(!E.getElementsByTag(prop.getProperty("t6")).isEmpty() && E.getElementsByTag(prop.getProperty("t6"))!=null)
												E.getElementsByTag(prop.getProperty("t6")).parents().first().remove();
											myArrayListResponse.add(E.text());		
									}
									} 
								pw.println();
								
								pw.print((myArrayListResponse.get(0)));
								for(int n=1;n<myArrayListResponse.size();n++){
								
											pw.print(",");
											pw.print((myArrayListResponse.get(n)));
											
										
										pw.flush();
									 		
											}
								
							myArrayList.clear();
							myArrayListResponse.clear();
							CrawlingMultiplePages multilePages = new  CrawlingMultiplePages();
							multiplePages.clear();
							multiplePages=multilePages.processMultilePages(thread);
							for(int o=0;o<multiplePages.size();o++){
								Connection pageNavConnection=Jsoup.connect( multiplePages.get(o)).userAgent(USER_AGENT);
								Document pageNavHTML =pageNavConnection.timeout(30000).get();
								
								if (connection.response().statusCode()==200){
								        Elements uname=pageNavHTML.getElementsByClass(prop.getProperty("class6"));//class6
								        for(Element E1:uname){
								            if(!E1.text().contains(prop.getProperty("misc2")))
								                myArrayList.add(E1.text());
								               }
						   /* Elements thead1 = pageNavHTML.getElementsByClass("tborder");
						    List<String> timeStamp1 = new ArrayList<String>();
							for(Element e: thead1){
								if (e.id().startsWith("post"))
									timeStamp1.add(e.select("td").first().text().substring(6));
							}
							for(String ts:timeStamp1){
								if (ts.startsWith("Yesterday")){
									Calendar calendar = Calendar.getInstance();
								    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
								    calendar.add(Calendar.DATE, -1);
								    Date yesterday = calendar.getTime();
								    pw1.println(ts.replace("Yesterday", dateFormat.format(yesterday).toString()));
								    }
								else if(ts.startsWith("Today")){
									Calendar calendar = Calendar.getInstance();
								    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
								    Date today = calendar.getTime();
								    pw1.println(ts.replace("Today", dateFormat.format(today).toString()));
								}
								else {
									pw1.println(ts);
								}
							}
							pw1.flush();*/
							
							Elements navDivTag1 = pageNavHTML.getElementsByClass(prop.getProperty("class7"));//class7
							Elements list2 = navDivTag1.select(prop.getProperty("t5"));//t2
							
							myArrayListResponse.clear();
							for(Element E:list2){
								if(E.id().startsWith(prop.getProperty("misc8"))){
									if(!E.getElementsByTag(prop.getProperty("t6")).isEmpty() && E.getElementsByTag(prop.getProperty("t6"))!=null)
									E.getElementsByTag(prop.getProperty("t6")).parents().first().remove();
								 myArrayListResponse.add(E.text());		
								}
							}
							for(int q=0;q<myArrayListResponse.size();q++){
								
									pw.print(",");
									pw.print(((myArrayListResponse.get(q))));
									
									 pw.flush();
								
									 }
							
							myArrayListResponse.clear();
									 }
							myArrayList.clear();
							myArrayListResponse.clear();
							
								}
							multiplePages.clear();
							
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
								
								 /*Elements thead1 = htmlDocument.getElementsByClass("tborder");
								    List<String> timeStamp1 = new ArrayList<String>();
									for(Element e: thead1){
										if (e.id().startsWith("post"))
											timeStamp1.add(e.select("td").first().text().substring(6));
									}
									for(String ts:timeStamp1){
										if (ts.startsWith("Yesterday")){
											Calendar calendar = Calendar.getInstance();
										    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
										    calendar.add(Calendar.DATE, -1);
										    Date yesterday = calendar.getTime();
										    pw1.println(ts.replace("Yesterday", dateFormat.format(yesterday).toString()));
										    }
										else if(ts.startsWith("Today")){
											Calendar calendar = Calendar.getInstance();
										    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
										    Date today = calendar.getTime();
										    pw1.println(ts.replace("Today", dateFormat.format(today).toString()));
										}
										else {
											pw1.println(ts);
										}
									}
									pw1.flush();*/
									
								Elements navDivTag = htmlDocument.getElementsByClass(prop.getProperty("class7"));//class7
								Elements list1 = navDivTag.select(prop.getProperty("t5"));//t2;
								myArrayListResponse.clear();
							for(Element E:list1){
								if(E.id().startsWith(prop.getProperty("misc8"))){
									if(!E.getElementsByTag(prop.getProperty("t6")).isEmpty() && E.getElementsByTag(prop.getProperty("t6"))!=null)
										E.getElementsByTag(prop.getProperty("t6")).parents().first().remove();
									myArrayListResponse.add(E.text());		
								}
							}
							pw.println();
							
							pw.print((myArrayListResponse.get(0))); 
							 for(int n=1;n<myArrayListResponse.size();n++){							
								
									 	pw.print(",");
										pw.print((myArrayListResponse.get(n)));
										
									
								 pw.flush();
							 }
							 myArrayListResponse.clear();
							}
							myArrayList.clear();
							
						}		 		
				 }
				 else{
						
					 threadsLink.remove(url);
					 this.junkLinks.add(url); 	
				}
					 pw.flush();
					 
				 }  
				
			 } 
			 catch(Exception e){
					e.printStackTrace();
					}
		}
		System.out.println("The no.of threads processed: "+threadsLink.size());
	}	
pw.close();
pw1.close();
}
}


