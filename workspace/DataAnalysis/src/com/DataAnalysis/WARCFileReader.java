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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import org.archive.io.ArchiveReader;
import org.archive.io.ArchiveRecord;
import org.archive.io.warc.WARCReaderFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/*
 * Reads WARC file and output meta data in a CSV file
 */

public class WARCFileReader {

	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	private static final String FILE_HEADER = "UserId,TotalPosts";
	public void  processWARCFile() throws IOException,MalformedURLException {
		String fn = "/home/ramu/Desktop/CompareCC/CC-MAIN-20161202170902-00331-ip-10-31-129-80.ec2.internald.warc.gz";
		FileInputStream is = new FileInputStream(fn);
		ArchiveReader ar = WARCReaderFactory.get(fn, is, true);
		File dirForums =new File("./BMWMetaDataCommonCrawl/");
		dirForums.mkdirs(); 
		File newFile= new File("./BMWMetaDataCommonCrawl/metadata.csv");
		FileOutputStream fos = new FileOutputStream(newFile,true);
		@SuppressWarnings("resource")
		PrintWriter fw = new PrintWriter(fos,true);
		fw.print(FILE_HEADER);
		fw.print(NEW_LINE_SEPARATOR);
		for(ArchiveRecord r : ar) {	
			//if(r.getHeader().getUrl()!=null && r.getHeader().getUrl().contains("f20.1addicts.com")){			
			OutputStream os = new ByteArrayOutputStream();	
			r.dump(os);	
			String f = os.toString();
			Document htmlDocument = Jsoup.parse(f);
			Elements fullThread = htmlDocument.select("body");
			Elements fullPost =fullThread.select("div[id=\"posts\"]");
			try{
			for(Element e: fullPost){
				List<String> myArrayList = new ArrayList<String>();
				List<String> timeStamp = new ArrayList<String>();
				List<String> totalPosts = new ArrayList<String>();
				//if(!e.getElementsByClass("alt1").first().text().isEmpty()&&e.getElementsByClass("div").first().text()!=null){
				Elements question = e.getElementsByClass("alt1");				
				Elements questionTag = question.select("div");				
				Element  questionText = questionTag.select("strong").first();
				String tQuestion=questionText.select("strong").first().text();
				Elements uName = e.getElementsByClass("bigusername");
				for (Element e1:uName){
					myArrayList.add(e1.text());
				}
				Elements nPosts = e.getElementsByClass("postBitScoreItem");
				Elements totalposts= nPosts.select("div[title=\"Post Count\"]");
				for(Element e2:totalposts){
					totalPosts.add(e2.text());
				}
				Elements thead = e.getElementsByClass("tborder");
				for(Element e3:thead){
					if (e3.id().startsWith("post"))
						timeStamp.add(e.select("td").first().text());
				}				
				for (int a=0;a<timeStamp.size()||a<totalposts.size()||a<myArrayList.size();a++){
				//fw.print(tQuestion);
				//fw.print(COMMA_DELIMITER);
				fw.print(myArrayList.get(a));
				fw.print(COMMA_DELIMITER);
				//fw.print(timeStamp.get(a));
				fw.print(totalPosts.get(a).replace(",", ""));
				//fw.print(COMMA_DELIMITER);
				//fw.print(Integer.toString(a));
				fw.print(NEW_LINE_SEPARATOR);
				}
			}
			}
			catch(NullPointerException npe){
				System.out.println("execption thrown at" +npe);
				}
			catch(IndexOutOfBoundsException iobe){
				System.out.println("execption thrown at" +iobe);
				}
			catch(IllegalArgumentException iobe){
				System.out.println("execption thrown at" +iobe);
				}
			} 
		}
	}
	