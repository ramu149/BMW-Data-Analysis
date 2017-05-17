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
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class CommonCrawlIndex {
	private static final String USER_AGENT="Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36";
	static Document htmlDocument;
	public static void main (String args[]) throws IOException{
		CommonCrawlIndexl("http://index.commoncrawl.org/CC-MAIN-2016-44-index?url=http%3A%2F%2Ff20.1addicts.com%2Fforums%2F*&output=json");
	}
	public static void CommonCrawlIndexl(String url){
		
		List<String> indexUrl=new ArrayList<String>();
		try{
			
			Connection connection=Jsoup.connect(url).userAgent(USER_AGENT);
			
			htmlDocument =connection.timeout(10000).get();
			//Elements indexes = htmlDocument.select("pre");
			Elements indexes = htmlDocument.select("body");
			for (Element index:indexes){
			String index1=index.text().toString();
			JSONObject jsonObj = new JSONObject(index1);
			indexUrl.add(jsonObj.getString("filename"));
			}
			System.out.println(indexUrl);
		}
		catch(Exception e){
			
		}
		
		
	}
	
 	

}
