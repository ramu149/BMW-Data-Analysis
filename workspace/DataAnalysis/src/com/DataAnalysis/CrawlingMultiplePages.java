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

import java.io.IOException;
import java.io.InputStream;
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
/*
 * Captures the multiple page navigation URLs in threads and Categories 
 */
public class CrawlingMultiplePages {
	Set<String> needsToBeProcessedLinks =  new HashSet<String>();
	List<String> junkLinks = new ArrayList<String>();
	List<String> threadsDisplayPage = new ArrayList<String>();
	List<String> multipleLinks = new ArrayList<String>();
	List<String> multiplePages = new ArrayList<String>();
	private static final String USER_AGENT="Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36";
	Document htmlDocument;
	long memory;
	Properties prop=new Properties();
	String propFileName="configNissan.properties";
	InputStream inputStream =getClass().getClassLoader().getResourceAsStream(propFileName);
	public List<String>  processMultilePages(String url) throws IOException{
		prop.load(inputStream);
		Connection connection=Jsoup.connect(url).userAgent(USER_AGENT);
		htmlDocument =connection.timeout(20000).get();	
		Element navigate=htmlDocument.getElementsByClass(prop.getProperty("class4")).first();//class4
	 	Element navi =navigate.getElementsByClass(prop.getProperty("class5")).first();//class5
	 	Elements murl =navigate.getElementsByClass(prop.getProperty("class3"));//class3
	 	Elements mlinks = murl.select(prop.getProperty("t1"));//t1
	 	String pages=navi.text();
	 	int pageNum= 0;
		pageNum=Integer.parseInt((pages.substring(10)).replaceAll("\\s+",""));
		for (Element e:mlinks)
			 multipleLinks.add(e.absUrl("href")); 	
		int num=Integer.parseInt(prop.getProperty("misc4"));//misc4
		String s=multipleLinks.get(0).substring( 0,multipleLinks.get(0).length()-num);	
		for(int x=2;x<=pageNum;x++)
			 multiplePages.add(s+x+prop.getProperty("misc3"));
		return multiplePages;
		}
}