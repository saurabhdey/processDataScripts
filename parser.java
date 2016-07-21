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
		boolean urinecotdone = false;
		boolean urinecotcheck1 = false; 
		boolean urinecotvalue = false ;
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
				if (tc.equalsIgnoreCase("561")){
					urinecotdone = true;
			//		System.out.println("561");
				}
			}
			if (urinecotdone){
				if (qName.equalsIgnoreCase("QuantitativeResult")){
					urinecotcheck1 = true;
				}
			}
			if (urinecotdone){
				if (qName.equalsIgnoreCase("ProviderTestCode")){
					urinecotcheck1 = true;
				}
			}
			if (urinecotdone){
				if (qName.equalsIgnoreCase("ValueCode")||qName.equalsIgnoreCase("MeasureValue")){
					urinecotvalue = true;
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
			if (urinecotvalue){
				output=null;
			//	System.out.println("Finalvalue:" + new String(ch, start, length));
				output = "" + new String(ch, start, length)+"," + storedate;
				//	System.out.println("output:"+output);
				storedate = null ;
				urinecotdone = false;
				urinecotcheck1 = false ;
				urinecotvalue = false ;
			}

		}
	};
}
