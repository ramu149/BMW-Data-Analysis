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

public class Series6 {
	private static final String USER_AGENT="Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36";
	private List<String> junkLinks = new ArrayList<String>();
	Document htmlDocument;
	Properties prop=new Properties();
	String propFileName="configBMW.properties";
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
					htmlDocument =connection1.timeout(10000).get();	
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
		}	catch(Exception e){
			e.printStackTrace();
			}	
		return threadUrls;
	}
	/*
	 * processMetaData method will get thread URLs from threadUrl method
	 * and crawl each URL to capture thread request/reponse along with user information 
	 */
	public void processMetaData(String url) throws IOException,MalformedURLException{
		prop.load(inputStream);
		File f, f1;
		//forumPages(url);	
		//for(int i=0;i<forumPages.size();i++){//forumPages.size()
			System.out.println("Forum URL: "+url);
			List<String> forumDisplay = new ArrayList<String>();
			List<String> postID	= new ArrayList<String>();
			forumDisplay=forumDisplayPages(url);
			int subString=Integer.parseInt(prop.getProperty("misc5"));//misc5
			f = new File(String.format(prop.getProperty("misc6")+"/%s/ThreadDisplay.txt", url.substring( subString,url.length()-1)));
			FileOutputStream fos = new FileOutputStream(f,true);
			 PrintWriter pw = new PrintWriter(fos);
			for(String category:forumDisplay){//forumDisplay.size()
				List<String> threadsLink = new ArrayList<String>(threadUrl(category));
				//threadsLink=(Set<String>) threadUrl(category);
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
						 	/*for(Element l:loc){
						 		if(l.text().startsWith("Location"))
						 			locationOfUser.add(l.text().substring(10));
						 			System.out.println(l.child(5).text().toString().substring(10));
						 	}
						 	System.out.println(locationOfUser);*/
							Elements uname1=htmlDocument.getElementsByClass(prop.getProperty("class6"));//class6
							for(Element E2:uname1){
								if(!E2.text().contains(prop.getProperty("misc2")))
								myArrayList.add(E2.text());
									}
							Elements lqw=links.select(prop.getProperty("t7"));//t7
							Element linksQestion = lqw.select(prop.getProperty("t3")).first();//t3
							String linkTextQuestion =linksQestion.text();
							//if(myArrayList.size()>0){
								//pw.println("Forum: "+url.substring( subString,url.length()-1));	
								//pw.println("the no.of categories in the forum "+url.substring( subString,url.length()-1)+": "+forumDisplay.size());	
								//pw.println("thread URL: "+threadsLink.get(k));
								//pw.println("User: "+myArrayList.get(0)); 
								//pw.println(locationOfUser.get(0));
								Elements thead = htmlDocument.getElementsByClass("tborder");
								List<String> timeStamp = new ArrayList<String>();
								for(Element e: thead){
									if (e.id().startsWith("post"))
										timeStamp.add(e.select("td").first().text().substring(6));
								}
								//pw.print(linkTextQuestion);
								Elements navDivTag = htmlDocument.getElementsByClass(prop.getProperty("class7"));
								Elements list1 = navDivTag.select(prop.getProperty("t5"));//t2	
								Element postId = list1.first();
								String post = postId.id();
								//list1.select("div.postquote").remove();
								//list1.select("div.smallfont").remove();
								myArrayListResponse.clear();
								for(Element E:list1){
										if(E.id().startsWith(prop.getProperty("misc8"))){
											if(!E.getElementsByTag(prop.getProperty("t6")).isEmpty() && E.getElementsByTag(prop.getProperty("t6"))!=null)
												E.getElementsByTag(prop.getProperty("t6")).parents().first().remove();
											myArrayListResponse.add(E.text());
											
									}
									} 
								pw.println();
								pw.print(post);
								pw.print(",");
								if (timeStamp.get(0).startsWith("Yesterday")){
									Calendar calendar = Calendar.getInstance();
								    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
								    calendar.add(Calendar.DATE, -1);
								    Date yesterday = calendar.getTime();
								    pw.print(timeStamp.get(0).replace("Yesterday", dateFormat.format(yesterday).toString()));
								    }
								else if(timeStamp.get(0).startsWith("Today")){
									Calendar calendar = Calendar.getInstance();
								    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
								    Date today = calendar.getTime();
								    pw.print(timeStamp.get(0).replace("Today", dateFormat.format(today).toString()));
								}
								else {
									pw.print(timeStamp.get(0));
								}
								pw.print(",");
								pw.print(linkTextQuestion);
								pw.print(",");
								pw.print((myArrayListResponse.get(0)));
							/*	for(int n=1;n<myArrayListResponse.size();n++){
									
										//pw.println("the below response is posted by the user: "+myArrayList.get(n));	
									 	//pw.println(locationOfUser.get(n));
										//if(timeStamp.get(n).contains("2017")||timeStamp.get(n).contains("2011")||timeStamp.get(n).contains("2012")||timeStamp.get(n).contains("2013")||timeStamp.get(n).contains("2014")||timeStamp.get(n).contains("2015")||timeStamp.get(n).contains("2016"))
									//{
											pw.print(",");
											pw.print((myArrayListResponse.get(n)));
										//}
										pw.flush();
									 		
											}
								
										//}
							
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
						    Elements thead1 = pageNavHTML.getElementsByClass("tborder");
						    List<String> timeStamp1 = new ArrayList<String>();
							for(Element e: thead1){
								if (e.id().startsWith("post"))
									timeStamp1.add(e.select("td").first().text().substring(6));
							}
							Elements navDivTag1 = pageNavHTML.getElementsByClass(prop.getProperty("class7"));//class7
							Elements list2 = navDivTag1.select(prop.getProperty("t5"));//t2
							//list2.select("div.postquote").remove();
							//list2.select("div.smallfont").remove();
							//if(myArrayList.size()!=0)
							//pw.println("the responses belong to the page "+multiplePages.get(o));
							myArrayListResponse.clear();
							for(Element E:list2){
								if(E.id().startsWith(prop.getProperty("misc8"))){
									if(!E.getElementsByTag(prop.getProperty("t6")).isEmpty() && E.getElementsByTag(prop.getProperty("t6"))!=null)
									E.getElementsByTag(prop.getProperty("t6")).parents().first().remove();
								 myArrayListResponse.add(E.text());	
								 
								}
							}
							for(int q=0;q<myArrayListResponse.size();q++){
									// pw.println("the below response is posted by the user: "+myArrayList.get(q));	
							//	if(timeStamp1.get(q).contains("2017")||timeStamp1.get(q).contains("2011")||timeStamp1.get(q).contains("2012")||timeStamp1.get(q).contains("2013")||timeStamp1.get(q).contains("2014")||timeStamp1.get(q).contains("2015")||timeStamp1.get(q).contains("2016"))
								//{
								
									pw.print(",");
									pw.print(((myArrayListResponse.get(q))));
							//	}
									 pw.flush();
								
									 }
							
							myArrayListResponse.clear();
									 }
							myArrayList.clear();
							myArrayListResponse.clear();
							
								}
							multiplePages.clear();*/
							URL u1=new URL(threadsLink.get(k+1));
							if(u.equals(u1)){
							threadsLink.remove(k+1);
							} 
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
								//pw.println("Forum: "+url.substring( subString,url.length()-1));	
								//pw.println("the no.of categories in the forum "+url.substring( subString,url.length()-1)+": "+forumDisplay.size());	
								//pw.println("URL : "+threadsLink.get(k));
								
								//pw.println("the question on the web page is posted by the user: "+myArrayList.get(0)); 
								//pw.print(linkTextQuestion1);
								 Elements thead1 = htmlDocument.getElementsByClass("tborder");
								    List<String> timeStamp1 = new ArrayList<String>();
									for(Element e: thead1){
										if (e.id().startsWith("post"))
											timeStamp1.add(e.select("td").first().text().substring(6));
									}
								Elements navDivTag = htmlDocument.getElementsByClass(prop.getProperty("class7"));//class7
								Elements list1 = navDivTag.select(prop.getProperty("t5"));//t2;
								Element postId = list1.first();
								String post = postId.id();
								myArrayListResponse.clear();
							for(Element E:list1){
								if(E.id().startsWith(prop.getProperty("misc8"))){
									if(!E.getElementsByTag(prop.getProperty("t6")).isEmpty() && E.getElementsByTag(prop.getProperty("t6"))!=null)
										E.getElementsByTag(prop.getProperty("t6")).parents().first().remove();
									myArrayListResponse.add(E.text());	
									
								}
							}
							//if(myArrayListResponse.size()>1)
							pw.println();
							pw.print(post);
							pw.print(",");
							if (timeStamp1.get(0).startsWith("Yesterday")){
								Calendar calendar = Calendar.getInstance();
							    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
							    calendar.add(Calendar.DATE, -1);
							    Date yesterday = calendar.getTime();
							    pw.print(timeStamp1.get(0).replace("Yesterday", dateFormat.format(yesterday).toString()));
							    }
							else if(timeStamp1.get(0).startsWith("Today")){
								Calendar calendar = Calendar.getInstance();
							    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
							    Date today = calendar.getTime();
							    pw.print(timeStamp1.get(0).replace("Today", dateFormat.format(today).toString()));
							}
							else {
								pw.print(timeStamp1.get(0));
							}
							pw.print(",");
							pw.print(linkTextQuestion1);
							pw.print(",");
							pw.print((myArrayListResponse.get(0))); 
							/* for(int n=1;n<myArrayListResponse.size();n++){							
									// pw.println("the below response is posted by the user: "+myArrayList.get(n));	
								// if(timeStamp1.get(n).contains("2017")||timeStamp1.get(n).contains("2011")||timeStamp1.get(n).contains("2012")||timeStamp1.get(n).contains("2013")||timeStamp1.get(n).contains("2014")||timeStamp1.get(n).contains("2015")||timeStamp1.get(n).contains("2016"))
									//{
									
									 	pw.print(",");
										pw.print((myArrayListResponse.get(n)));
										
									//}
								 pw.flush();
							 }*/
							 
							 myArrayListResponse.clear();
							}
							myArrayList.clear();
							if(k<threadsLink.size()-1){
							URL u1=new URL(threadsLink.get(k+1));
							if(u.equals(u1)){
							threadsLink.remove(k+1);
							} 	
							}
						}
						 	URL u1=new URL(threadsLink.get(k+1)); 
							if(u.equals(u1)){
							threadsLink.remove(k+1);
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
}

}
