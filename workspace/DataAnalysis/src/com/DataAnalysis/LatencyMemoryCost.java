
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class LatencyMemoryCost {
	 private long times;
	 private long time;
	 long memory;
	 private static final String COMMA_DELIMITER = ",";
	 private static final String NEW_LINE_SEPARATOR = "\n";
	 //private static final String FILE_HEADER = "Time,MemoryObject";
	 public void startMeasure(){
		 time = System.currentTimeMillis();
		     }
	 public void endMeasure() throws FileNotFoundException {
		 times = System.currentTimeMillis()-time;
			File dirForums =new File("./BMWMetaData/LatencyAAndMemoryCost3");
			dirForums.mkdirs();
			File f = new File(String.format("./BMWMetaData/LatencyAAndMemoryCost3/Time.csv"));
			FileOutputStream fos = new FileOutputStream(f,true);
			PrintWriter pw = new PrintWriter(fos);
			//pw.print(FILE_HEADER);
			
			pw.print(times);
			pw.print(COMMA_DELIMITER);
			pw.print(System.currentTimeMillis());
			pw.print(NEW_LINE_SEPARATOR);
			pw.flush();
			pw.close();
		     }
	 public void memoryConsumption() throws FileNotFoundException{
		 File f = new File(String.format("./BMWMetaData/LatencyAAndMemoryCost3/Memory.csv"));
			FileOutputStream fos = new FileOutputStream(f,true);
			PrintWriter pw = new PrintWriter(fos);
			pw.print(memory);
			pw.print(COMMA_DELIMITER);
			pw.print(System.currentTimeMillis());
			pw.print(NEW_LINE_SEPARATOR);
			pw.flush();
			pw.close();
	 }
	
	
}
