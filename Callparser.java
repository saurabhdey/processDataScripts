import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class Callparser{

	public static void main(String[] args) throws IOException, ParserConfigurationException {
		long startTime = System.currentTimeMillis();

		String filePath = "/home/med-data/labresult.xml";
		PrintWriter Writeobj = new PrintWriter(new BufferedWriter(new FileWriter("Extractedvalues.csv", false)));
		String heading = "customerno"+","+"LabValue"+","+"TestDate" ;
	
		// Write out first line for output 
		Writeobj.println(heading);

		InputStream inps = null;
		// declare all string holders 
		String inrecord,outrecord,storerec;
		// initialize above 
		inrecord = storerec = outrecord = null ;

		try {
			inps = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// Create new input stream reader
		InputStreamReader instrm = new InputStreamReader(inps);

		// Create the object of BufferedReader object
		BufferedReader br = new BufferedReader(instrm);
		
		Saxparser sax1 = new Saxparser(); 

		while((inrecord = br.readLine()) != null) { 

				storerec = inrecord.substring(0,10); 
				inrecord = inrecord.substring(10);
				
			if (inrecord != null)  { 
				
				InputStream conv2stream = new ByteArrayInputStream(inrecord.getBytes(Charset.forName("UTF-8")));
	 			String value;
				try {
					value = sax1.parsethis(conv2stream);
				} catch (SAXException e) {
				    // e.printStackTrace();
					System.out.println(storerec);
					continue;
				}
//	 			System.out.println("output:"+value);
	 			
			// Convert returned input stream to string just in case using apache commons 
			// String value = IOUtils.toString(conv2stream, "UTF-8");
				
			// Convert returned input stream to string using scanner 
			// String value = new Scanner(outps,"UTF-8").useDelimiter("\\A").next();
	 			
				outrecord = storerec.trim().concat(",").concat(value);
//				System.out.println(outrecord);
				
			// Write out csv  
				Writeobj.println(outrecord);
							
			}
			else break;
		}		
		Writeobj.close();
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Total Process Time in (ms):"+totalTime);
	}

}
