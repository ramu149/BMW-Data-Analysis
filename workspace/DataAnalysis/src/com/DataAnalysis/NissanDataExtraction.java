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
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class NissanDataExtraction {
	private static final String USER_AGENT="Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36";
	Document htmlDocument;
	Properties prop=new Properties();
	String propFileName="configNissan.properties";
	InputStream inputStream =getClass().getClassLoader().getResourceAsStream(propFileName);
	List<String> unusedLinks = new ArrayList<String>();
	Set<String> linksToProcess =  new HashSet<String>();
	public  List<String> forums(String url){
		List<String> subForums = new ArrayList<String>();
		try{
			Connection connection=Jsoup.connect(url).userAgent(USER_AGENT);
			htmlDocument =connection.timeout(50000).get();	
			Elements subForum = htmlDocument.select("table.tborder.vs_subforum");
			//System.out.println(subForum);
			Elements subForumLinks = subForum.select("td.alt1Active");
			for(Element link:subForumLinks){
				link = link.select("a").first();
				subForums.add(link.absUrl("href"));
			}
			System.out.println(subForums);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return subForums; 
	}
	public List<String> threadUrl(String url) throws IOException{
		List<String> threadUrls = new ArrayList<String>();
		List<String> multiplePages = new ArrayList<String>();
		try{
			Connection connection=Jsoup.connect(url).userAgent(USER_AGENT);
			 htmlDocument =connection.timeout(100000).get();	
			 Elements navDivTag = htmlDocument.getElementsByClass("alt1");//class8
			 Elements links = navDivTag.select("a");//t1
			 if(!htmlDocument.getElementsByClass("pagenav").text().isEmpty() && htmlDocument.getElementsByClass("pagenav").first()!=null){//class4
				 for(Element link : links){
					 link.getElementsByClass("inlineimg").remove();
						link.getElementsByTag("span").remove();	
						URL u =new URL(link.absUrl("href"));	
						 if(u.getPath().toString().contains("forum")&&!link.absUrl("href").contains("page")&&link.id().startsWith("thread_title_")){//misc1 for showthread
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
					htmlDocument =connection1.timeout(40000).get();	
					Elements navDivTag1 = htmlDocument.getElementsByClass("alt1");//class8
					Elements links1 = navDivTag1.select("a");//t1
					for(Element link : links1){
							URL u =new URL(link.absUrl("href"));	
							if(u.getPath().toString().contains("forum")&&!link.absUrl("href").contains("page")&&link.id().startsWith("thread_title_")){//misc1 for showthread
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
		System.out.println(threadUrls);
		return threadUrls;
	}
	public void processMetaData(String url) throws IOException,MalformedURLException{
		prop.load(inputStream);
		File f, f1;
		//forumPages(url);	
		//for(int i=0;i<forumPages.size();i++){//forumPages.size()
		File dir = new File(prop.getProperty("misc6"));
		dir.mkdir();
		int subString1=Integer.parseInt(prop.getProperty("misc5"));//misc5	
		 File dirForums1 =new File(prop.getProperty("misc6")+"/"+url.substring( subString1,url.length()-1));
		 dirForums1.mkdirs();
			System.out.println("Forum URL: "+url);
			List<String> forumDisplay = new ArrayList<String>();
			forumDisplay=forums(url);
			int subString=Integer.parseInt(prop.getProperty("misc5"));//misc5
			f = new File(String.format(prop.getProperty("misc6")+"/%s/ThreadDisplay.txt", url.substring( subString,url.length()-1)));
			f1 = new File(String.format(prop.getProperty("misc6")+"/%s/TimeStamps.txt", url.substring( subString,url.length()-1)));
			FileOutputStream fos = new FileOutputStream(f,true);
			PrintWriter pw = new PrintWriter(fos);
			FileOutputStream fos1 = new FileOutputStream(f1,true);
			PrintWriter pw1 = new PrintWriter(fos1);
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
					 htmlDocument =connection.timeout(100000).get();
					 if (connection.response().statusCode()==200) {	
					
						 	List<String> myArrayListResponse = new ArrayList<String>();	
						 	List<String> myArrayList = new ArrayList<String>();
						 	Elements links = htmlDocument.getElementsByClass(prop.getProperty("class9"));//class9
						 	if(htmlDocument.getElementsByClass(prop.getProperty("class4")).first()!=null && !htmlDocument.getElementsByClass(prop.getProperty("class4")).first().text().isEmpty()){//class4
						 	//List<String> locationOfUser = new ArrayList<String>();
						 //	Elements location = htmlDocument.getElementsByClass("alt2");
						 	//Elements loc	= location.select("smallfont").after("div[style=\"height:6px;\"]");
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
								Elements thead = htmlDocument.select("div.tborder.vbseo_like_postbit.user-post");
								List<String> timeStamp = new ArrayList<String>();
								for(Element e: thead){
									if (e.id().startsWith("post"))
										timeStamp.add(e.select("span[itemprop=dateCreated]").first().text());
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
								pw1.flush();
								//for(Element e: thead){
									//if (e.id().startsWith("post"))
										//timeStamp.add(e.select("td").first().text().substring(6));
								//}
								//pw.print(linkTextQuestion);
								Elements navDivTag = htmlDocument.getElementsByClass("main-column-text");
								Elements list1 = navDivTag.select("div");//t2	
								//list1.select("div.postquote").remove();
								//list1.select("div.smallfont").remove();
								
							
								for(Element E:list1){
										if(E.id().startsWith(prop.getProperty("misc8"))){
											if(!E.getElementsByTag(prop.getProperty("t6")).isEmpty() && E.getElementsByTag(prop.getProperty("t6"))!=null){
											//	E.getElementsByTag(prop.getProperty("t6")).parents().first().remove();
												myArrayListResponse.add(E.text().toString());	
											}
											else
											myArrayListResponse.add(E.text().toString());		
									}
									} 
								pw.println();
								pw.print((myArrayListResponse.get(0)));
								//System.out.println(myArrayListResponse.size());
								for(int n=1;n<myArrayListResponse.size();n++){
									
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
								        Elements thead1 = htmlDocument.select("div.tborder.vbseo_like_postbit.user-post");
										List<String> timeStamp1 = new ArrayList<String>();
										for(Element e: thead1){
											if (e.id().startsWith("post"))
												timeStamp1.add(e.select("span[itemprop=dateCreated]").first().text());
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
										pw1.flush();
										
						//    List<String> timeStamp1 = new ArrayList<String>();
							//for(Element e: thead1){
								//if (e.id().startsWith("post"))
									//timeStamp1.add(e.select("td").first().text().substring(6));
							//}
							Elements navDivTag1 = pageNavHTML.getElementsByClass("main-column-text");//class7
							Elements list2 = navDivTag1.select("div");//t2
							//list2.select("div.postquote").remove();
							//list2.select("div.smallfont").remove();
							//if(myArrayList.size()!=0)
							//pw.println("the responses belong to the page "+multiplePages.get(o));
							myArrayListResponse.clear();
							for(Element E:list2){
								if(E.id().startsWith(prop.getProperty("misc8"))){
									if(!E.getElementsByTag(prop.getProperty("t6")).isEmpty() && E.getElementsByTag(prop.getProperty("t6"))!=null){
										//E.getElementsByTag(prop.getProperty("t6")).parents().first().remove();
										myArrayListResponse.add(E.text().toString());
									}
									else
										myArrayListResponse.add(E.text().toString());		
								}
							}
							for(int q=0;q<myArrayListResponse.size();q++){
									// pw.println("the below response is posted by the user: "+myArrayList.get(q));	
								//if(timeStamp1.get(q).contains("2017")||timeStamp1.get(q).contains("2011")||timeStamp1.get(q).contains("2012")||timeStamp1.get(q).contains("2013")||timeStamp1.get(q).contains("2014")||timeStamp1.get(q).contains("2015")||timeStamp1.get(q).contains("2016"))
								//{
								
									pw.print(",");
									pw.print(((myArrayListResponse.get(q))));
									
								//}
									 pw.flush();
								
									 }
							
							myArrayListResponse.clear();
									 }
							myArrayList.clear();
							myArrayListResponse.clear();
							
								}
							multiplePages.clear();
							timeStamp.clear();
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
								Elements thead = htmlDocument.select("div.tborder.vbseo_like_postbit.user-post");
								List<String> timeStamp = new ArrayList<String>();
								for(Element e: thead){
									if (e.id().startsWith("post"))
										timeStamp.add(e.select("span[itemprop=dateCreated]").first().text());
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
								pw1.flush();
								
								//	for(Element e: thead1){
									//	if (e.id().startsWith("post"))
										//	timeStamp1.add(e.select("td").first().text().substring(6));
									//}
								Elements navDivTag = htmlDocument.getElementsByClass("main-column-text");//class7
								Elements list1 = navDivTag.select("div");//t2;
								
							for(Element E:list1){
								if(E.id().startsWith(prop.getProperty("misc8"))){
									if(!E.getElementsByTag(prop.getProperty("t6")).isEmpty() && E.getElementsByTag(prop.getProperty("t6"))!=null){
										E.getElementsByTag(prop.getProperty("t6")).parents().first().remove();
										myArrayListResponse.add(E.text().toString());
									}
									else
										myArrayListResponse.add(E.text().toString());		
								}
							}
							//if(myArrayListResponse.size()>1)
							//System.out.println(myArrayListResponse.size());
							pw.println();
							pw.print((myArrayListResponse.get(0))); 
							 for(int n=1;n<myArrayListResponse.size();n++){							
									// pw.println("the below response is posted by the user: "+myArrayList.get(n));	
								// if(timeStamp1.get(n).contains("2017")||timeStamp1.get(n).contains("2011")||timeStamp1.get(n).contains("2012")||timeStamp1.get(n).contains("2013")||timeStamp1.get(n).contains("2014")||timeStamp1.get(n).contains("2015")||timeStamp1.get(n).contains("2016"))
									//{
									
									 	pw.print(",");
										pw.print((myArrayListResponse.get(n)));
										
									//}
								 pw.flush();
							 }
							 
							 myArrayListResponse.clear();
							 timeStamp.clear();
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

	
	public static void main(String args[]) throws IOException{
	//	List<String> forums = new ArrayList<String>();
		//List<String> threads = new ArrayList<String>();
		NissanDataExtraction e = new NissanDataExtraction();
		e.processMetaData("http://www.nissanforums.com/sentra-pulsar-nx-b14-200sx/");
		//forums=e.forums("http://www.nissanforums.com/nissanforums-com-new-members/");
		//for(String url:forums){
			//threads = e.threadUrl(url);
		//}
		//System.out.println(threads.size());
	}
	
}
