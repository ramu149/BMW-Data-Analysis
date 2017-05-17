/****************************************************************************
Ramu Nerella
Data Intensive and Computing Eco-Systems lab,
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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
/*
 * Downloads the WARC file 
 */

public class DownloadWARCFile {

  public static void downloadFile() throws IOException {		 
		 String fileName = "/home/ramu/Desktop/ccouter/CC-MAIN-20161020183841-00250-ip-10-171-6-4.ec2.internal.warc.gz";
		 URL link = new URL("https://commoncrawl.s3.amazonaws.com/crawl-data/CC-MAIN-2016-44/segments/1476988721174.97/warc/CC-MAIN-20161020183841-00250-ip-10-171-6-4.ec2.internal.warc.gz");
		 InputStream in = new BufferedInputStream(link.openStream());
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
		 byte[] buf = new byte[1048576];
		 int   n = 0;
		 while (-1!=(n=in.read(buf)))
		 {
		    out.write(buf, 0, n);
		 }	 
		 out.close();
		 in.close();
		 byte[] response = out.toByteArray();
		 FileOutputStream fos = new FileOutputStream(fileName);
		 fos.write(response);		
		 fos.close();
		 System.out.println("Finished");
	}
}
 
