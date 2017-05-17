
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import com.DataAnalysis.DataExtraction;

public class CrawlingMain {
	public static void main (String args[]) throws IOException{
		/*File file = new File("./Forum.text");
		FileReader fileReader = new FileReader(file);
		@SuppressWarnings("resource")
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String fURL=null;*/
		
		//while((fURL=bufferedReader.readLine())!=null){
			//DataCrawling forum = new DataCrawling();
			InfluentialUserCalculation forum = new InfluentialUserCalculation();
			//CrawlingTitles forum=new CrawlingTitles();
			forum.processMetaData("http://f87.bimmerpost.com/forums/");
		//	Series6 s = new Series6();
			//s.processMetaData("http://www.6post.com/forums/");
			
			//AudiMetaData forum = new AudiMetaData();
			//forum.forumPages("http://www.audiforums.com/forum/");
		//http://f30.bimmerpost.com/forums/
		//I3
		//http://www.7post.com/forums/
			//DataExtraction e = new DataExtraction();
			//e.processMetaData("http://f20.1addicts.com/forums/");
		//	InfluentialUserCalculation ie = new InfluentialUserCalculation();
			//ie.processMetaData("http://f15.bimmerpost.com/forums/");
		//}
			
			//System.out.println(21&1);
	}	
}
