
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DataExtraction {
	List<String> unusedLinks = new ArrayList<String>();
	Set<String> linksToProcess =  new HashSet<String>();
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	private static final String FILE_HEADER = "TimeStamp";
	Set<String> totalUserPosts =  new HashSet<String>();
	private static final String USER_AGENT="Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36";
	Document htmlDocument;
	Properties prop=new Properties();
	String propFileName="configBMW.properties";
	InputStream inputStream =getClass().getClassLoader().getResourceAsStream(propFileName);
	 @SuppressWarnings("unchecked")
	public List<String> categoryMetadata(String url){
		List<String> categories = new ArrayList<String>();
		//List<String> totalThreads = new ArrayList<String>();
		//List<String> totalPosts = new ArrayList<String>();
		//List<String> categoryName = new ArrayList<String>();
		try{
			Connection connection=Jsoup.connect(url).userAgent(USER_AGENT);
			 htmlDocument =connection.timeout(1000).get();	
			 htmlDocument.getElementsByClass("smallfont").remove();
			 Elements navDivTag = htmlDocument.getElementsByClass("alt1Active");
			 Elements links = navDivTag.select("a");
			 for(Element link : links){
				categories.add(link.absUrl("href"));
				//categoryName.add(link.getElementsByTag("strong").text());
			 	}
			 Iterator<String> itr = categories.iterator();
			 while(itr.hasNext()){
				 String str = itr.next(); 
				 if(str.endsWith("&f=133")||str.endsWith("&f=36")||str.endsWith("&f=272")||str.endsWith("&f=4")||str.endsWith("&f=353")||str.endsWith("&f=8")
							||str.endsWith("&f=306")||str.endsWith("&f=62")||str.endsWith("&f=170")||str.endsWith("&f=467")||str.endsWith("&f=86")||str.endsWith("&f=56")||str.endsWith("&f=7"))
							itr.remove();
			 }
			 
			/* Elements table = htmlDocument.getElementsByClass("tborder");
			 Elements tableElement1 = table.select("td");
			 for(Element threadCount : tableElement1){
				if (threadCount.getElementsByClass("alt1").text().length()!=0 && !threadCount.getElementsByClass("alt1").text().contains("BMW"))
				totalThreads.add(threadCount.getElementsByClass("alt1").text());
			 	} 
			 Elements tableElement2 = table.select("td");
			 for(Element posts:tableElement2){
				 if (posts.getElementsByClass("alt2").text().length()!=0 && !posts.getElementsByClass("alt2").text().contains("a")&& !posts.getElementsByClass("alt2").text().contains(" ") )
					 totalPosts.add(posts.getElementsByClass("alt2").text());
			 }*/
			 
			//File dirForums =new File("./BMWMetaData/"+url.substring( 7,url.length()-7));
			//dirForums.mkdirs();
			//File f = new File(String.format("./BMWMetaData/%s/ThreadDisplay.txt", url.substring( 6,url.length()-7)));
			//FileOutputStream fos = new FileOutputStream(f,true);
		//PrintWriter pw = new PrintWriter(fos);
			//pw.println("Category Overview");
			///pw.print(NEW_LINE_SEPARATOR);
			//pw.println("------------------");
			//pw.print(NEW_LINE_SEPARATOR);
			//pw.println();
			//pw.println("The Categories belong to the forum:" +url.substring( 7,url.length()-8));
			//pw.print(NEW_LINE_SEPARATOR);
			//for(int i=0;i<categories.size();i++){
				
				//pw.println("Category "+(i+1)+" have: "+totalThreads.get(i));
				//pw.println("total no.of posts in the current category are: "+totalPosts.get(i));
			//}
			//pw.println();
			//pw.println("========================================================================");
			//pw.println("========================================================================");
			//pw.println();
			//pw.flush();
		//pw.close();  
			}	catch(IOException ioe){
					System.out.print("execption thrown at" +ioe);
				}
				catch(NullPointerException npe){
					System.out.print("execption thrown at" +npe);
				}
				catch(IndexOutOfBoundsException ibe){
				System.out.print("execption thrown at" +ibe);
				}
				catch(IllegalArgumentException iobe){
					System.out.print("execption thrown at" +iobe);
				}
		return  categories;
	}
public List<String> threadUrl(String url) throws IOException{
	List<String> threadUrls = new ArrayList<String>();
	List<String> multiplePages = new ArrayList<String>();
	try{
		Connection connection=Jsoup.connect(url).userAgent(USER_AGENT);
		 htmlDocument =connection.timeout(20000).get();	
		 Elements navDivTag = htmlDocument.getElementsByClass("alt1");//class8
		 Elements links = navDivTag.select("a");//t1
		 if(!htmlDocument.getElementsByClass("pagenav").text().isEmpty() && htmlDocument.getElementsByClass("pagenav").first()!=null){//class4
			 for(Element link : links){
					URL u =new URL(link.absUrl("href"));	
					 if(u.getPath().toString().contains("showthread")&&!link.absUrl("href").contains("page")){//misc1 for showthread
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
				htmlDocument =connection1.timeout(10000).timeout(10000).get();	
				Elements navDivTag1 = htmlDocument.getElementsByClass(prop.getProperty("class8"));//class8
				Elements links1 = navDivTag1.select(prop.getProperty("t1"));//t1
				for(Element link : links1){
						URL u =new URL(link.absUrl("href"));	
						if(u.getPath().contains(prop.getProperty("misc1"))&&!link.absUrl("href").contains("page")){//misc1 for showthread
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
	}	catch(IOException ioe){
		System.out.print("execption thrown at" +ioe);
		}
	catch(NullPointerException npe){
		System.out.print("execption thrown at" +npe);
		}
	catch(IndexOutOfBoundsException iobe){
		System.out.print("execption thrown at" +iobe);
		}
	catch(IllegalArgumentException iae){
		System.out.print("execption thrown at" +iae);
		}	
	return threadUrls;
}
public void processMetaData(String url) throws IOException,MalformedURLException{	
	prop.load(inputStream);
	File dir = new File(prop.getProperty("misc6"));
	dir.mkdir();
	int subString1=Integer.parseInt(prop.getProperty("misc5"));//misc5	
	 File dirForums1 =new File(prop.getProperty("misc6")+"/"+url.substring( subString1,url.length()-1));
	 dirForums1.mkdirs();
	File f;//,unProcessedThreads;
	int subString=Integer.parseInt(prop.getProperty("misc5"));//misc5
	f = new File(String.format(prop.getProperty("misc6")+"/%s/ThreadDisplay.txt", url.substring( subString,url.length()-1)));

	FileOutputStream fos = new FileOutputStream(f,true);
	PrintWriter pw = new PrintWriter(fos);
	List<String> categories = new ArrayList<String>();
	categories=categoryMetadata(url);
	
	

	 
	pw.print(FILE_HEADER.toString());
	pw.print(NEW_LINE_SEPARATOR);
	for(String category:categories){//forumDisplay.size()
		List<String> threadLinks = new ArrayList<String>();
		threadLinks=threadUrl(category);
		for(int k=0;k<threadLinks.size();k++){		
			try{
				List<String> multiplePages = new ArrayList<String>();
				List<String> totalPosts = new ArrayList<String>();
				URL u =new URL(threadLinks.get(k));	
				if(u.getPath().toString().contains(prop.getProperty("misc1"))){//misc1		
					Connection connection=Jsoup.connect( threadLinks.get(k)).userAgent(USER_AGENT);
					htmlDocument =connection.timeout(10000).get();
					if (connection.response().statusCode()==200) {		 
						List<String> myArrayListResponse = new ArrayList<String>();	
						List<String> myArrayList = new ArrayList<String>();
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
								List<String> timeStamp = new ArrayList<String>();
								for(Element e: thead){
									if (e.id().startsWith("post"))
										timeStamp.add(e.select("td").first().text().substring(6));
								}
								for(String ts:timeStamp){
									if (ts.startsWith("Yesterday")){
										Calendar calendar = Calendar.getInstance();
									    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
									    calendar.add(Calendar.DATE, -1);
									    Date yesterday = calendar.getTime();
									    pw.println(ts.replace("Yesterday", dateFormat.format(yesterday).toString()));
									    }
									else if(ts.startsWith("Today")){
										Calendar calendar = Calendar.getInstance();
									    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
									    Date today = calendar.getTime();
									    pw.println(ts.replace("Today", dateFormat.format(today).toString()));
									}
									else {
										pw.println(ts);
									}
								}
								pw.flush();
								
								/*int n=0;
								String time;
								for(Element e:thead){
									
									if (e.id().startsWith("post")){
										time=e.select("td").first().text();
										//pw.print(linkTextQuestion);
										//pw.print(COMMA_DELIMITER);
										//pw.print(Integer.toString(n+1));
										//pw.print(COMMA_DELIMITER);
										if(n<myArrayList.size())
											pw1.print(myArrayList.get(n));
										//pw.print(COMMA_DELIMITER);
										pw.print("\"");
										pw.print(time.substring(6));
										pw.print("\"");
										pw.println();
										//pw.print(COMMA_DELIMITER);
										//if(n<totalPosts.size())
											//pw.print(totalPosts.get(n).replace(",", ""));
										//pw.print(COMMA_DELIMITER);
									//	pw.print(threadLinks.get(k));
										//pw.print(NEW_LINE_SEPARATOR);
										n++;
										}
									}
									n=0;*/
									
									nofPosts=nofPosts+myArrayList.size();
											}
								myArrayList.clear();
								myArrayListResponse.clear();	 
								
								for(int o=0;o<multiplePages.size();o++){
									Connection pageNavConnection=Jsoup.connect( multiplePages.get(o)).userAgent(USER_AGENT);
									Document pageNavHTML =pageNavConnection.timeout(10000).get();
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
								List<String> timeStamp = new ArrayList<String>();
								for(Element e: thead){
									if (e.id().startsWith("post"))
										timeStamp.add(e.select("td").first().text().substring(6));
								}
								
								for(String ts:timeStamp){
									if (ts.startsWith("Yesterday")){
										Calendar calendar = Calendar.getInstance();
									    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
									    calendar.add(Calendar.DATE, -1);
									    Date yesterday = calendar.getTime();
									    pw.println(ts.replace("Yesterday", dateFormat.format(yesterday).toString()));
									    }
									else if(ts.startsWith("Today")){
										Calendar calendar = Calendar.getInstance();
									    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
									    Date today = calendar.getTime();
									    pw.println(ts.replace("Today", dateFormat.format(today).toString()));
									}
									else {
										pw.println(ts);
									}
								}
								pw.flush();
								
							/*	int n=0;
								String time;
								for(Element e:thead){
									
									if (e.id().startsWith("post")){
										time=e.select("td").first().text();
									//pw.print(linkTextQuestion);
									//pw.print(COMMA_DELIMITER);
									//pw.print(Integer.toString(n+1));
									//pw.print(COMMA_DELIMITER);
									if(n<myArrayList.size())
										pw1.print(myArrayList.get(n));
									//pw.print(COMMA_DELIMITER);
									pw.print("\"");
									pw.print(time.substring(6));
									pw.print("\"");
									pw.println();
									//pw.print(COMMA_DELIMITER);
									//if(n<totalPosts.size())
										//pw.print(totalPosts.get(n).replace(",", ""));
									//pw.print(COMMA_DELIMITER);
									//pw.print(multiplePages.get(o));
									//pw.print(NEW_LINE_SEPARATOR);
									n++;	
									}
								}
								n=0;*/
								nofPosts=nofPosts+myArrayList.size();
										 }
								totalPosts.clear();
								myArrayList.clear();
								myArrayListResponse.clear();
								
									}
								pw.flush();
								totalPosts.clear();
								multiplePages.clear();
								URL u1=new URL(threadLinks.get(k+1));
								if(u.equals(u1)){
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
								int nofPosts=0;
								if(myArrayList.size()>0){
									Elements lq=links.select(prop.getProperty("t7"));//t2
									Element linksQestion1 = lq.select(prop.getProperty("t3")).first();//t3
									String linkTextQuestion1 =linksQestion1.text();
									Elements thead = htmlDocument.getElementsByClass("tborder");
									Elements nPosts = htmlDocument.getElementsByClass("postBitScoreItem");
									Elements totalposts= nPosts.select("div[title=\"Post Count\"]");
									for(Element e:totalposts){
										totalPosts.add(e.text());
									}
									List<String> timeStamp = new ArrayList<String>();
									for(Element e: thead){
										if (e.id().startsWith("post"))
											timeStamp.add(e.select("td").first().text().substring(6));
									}
									for(String ts:timeStamp){
										if (ts.startsWith("Yesterday")){
											Calendar calendar = Calendar.getInstance();
										    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
										    calendar.add(Calendar.DATE, -1);
										    Date yesterday = calendar.getTime();
										    pw.println(ts.replace("Yesterday", dateFormat.format(yesterday).toString()));
										    }
										else if(ts.startsWith("Today")){
											Calendar calendar = Calendar.getInstance();
										    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
										    Date today = calendar.getTime();
										    pw.println(ts.replace("Today", dateFormat.format(today).toString()));
										}
										else {
											pw.println(ts);
										}
									}
									pw.flush();
									/*int n=0;
									String time;
									for(Element e:thead){	
										
										if (e.id().startsWith("post")){
											time=e.select("td").first().text();
										//pw.print(linkTextQuestion1);
										//pw.print(COMMA_DELIMITER);
										//pw.print(Integer.toString(n+1));
										//pw.print(COMMA_DELIMITER);
										if(n<myArrayList.size())
											pw1.print(myArrayList.get(n));
										//pw.print(COMMA_DELIMITER);
										pw.print(time.substring(6));
										//pw.print(COMMA_DELIMITER);
										//if(n<totalPosts.size())
											//pw.print(totalPosts.get(n).replace(",", ""));
										//pw.print(COMMA_DELIMITER);
										//pw.print(threadLinks.get(k));
										//pw.print(NEW_LINE_SEPARATOR);
										
										n++;
										}
									}
									pw.println();
									n=0;*/
									nofPosts=nofPosts+myArrayList.size();
								}
								myArrayList.clear();
								myArrayListResponse.clear();	 
								nofPosts=0;
							}
						 }
					 else{
						 threadLinks.remove(url);
						 this.unusedLinks.add(url); 	
					}
						 pw.flush();
						
						
					 }  
				} 
			catch(IOException ioe){
				System.out.print("execption thrown at" +ioe);
				}
			catch(NullPointerException npe){
				System.out.print("execption thrown at" +npe);
				}
			catch(IndexOutOfBoundsException iobe){
				System.out.print("execption thrown at" +iobe);
				}
			catch(IllegalArgumentException iobe){
				System.out.print("execption thrown at" +iobe);
				}	 	
			}
			System.out.print("The no.of threads processed: "+threadLinks.size());
		}	 	
	pw.close();
		}
}