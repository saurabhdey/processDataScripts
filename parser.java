/*
Using the SAX parser to read through the full xml file(9GB) and extract the required values.
*/ 
import java.io.IOException;
import java.io.InputStream;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.helpers.DefaultHandler;

public class Saxmode{

	private String output;

	public String parsethis(InputStream conv2stream) throws ParserConfigurationException, SAXException, IOException {

		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();	
		parser.parse(conv2stream, handler);

		//	    System.out.println(output);
		return output;
	}


	public DefaultHandler handler = new DefaultHandler() {
		boolean cotdone = false;
		boolean cotcheck1 = false; 
		boolean cotvalue = false ;
		boolean testdate = false; 
		private String storedate = null ; 

		public void startElement(String uri, String localName,String qName, 
				Attributes attributes) throws SAXException {
		//	System.out.println("Start Element :" + qName);

		
			if (qName.equalsIgnoreCase("TestDate")){
				 testdate = true;
			}
				
			if (qName.equalsIgnoreCase("TestCode")){
				String tc = attributes.getValue("tc");      
				if (tc.equalsIgnoreCase("XX1")){
					cotdone = true;
			//		System.out.println("XX1");
				}
			}
			if (cotdone){
				if (qName.equalsIgnoreCase("QuantitativeResult")){
					cotcheck1 = true;
				}
			}
			if (cotdone){
				if (qName.equalsIgnoreCase("ProviderTestCode")){
					cotcheck1 = true;
				}
			}
			if (cotdone){
				if (qName.equalsIgnoreCase("ValueCode")||qName.equalsIgnoreCase("MeasureValue")){
					cotvalue = true;
				}
			}
		}
		public void endElement(String uri, String localName, 
				String qName) throws SAXException {

			//System.out.println("End Element :" + qName);

		}

		public void characters(char ch[], int start, int length) throws SAXException {

			if (testdate){ 
				storedate = new String(ch, start, length) ; 
				testdate = false ; 
			}

			
			//System.out.println("" + new String(ch, start, length));
			// print the characters
			if (cotvalue){
				output=null;
			//	System.out.println("Finalvalue:" + new String(ch, start, length));
				output = "" + new String(ch, start, length)+"," + storedate;
				//	System.out.println("output:"+output);
				storedate = null ;
				cotdone = false;
				cotcheck1 = false ;
				cotvalue = false ;
			}

		}
	};
}
