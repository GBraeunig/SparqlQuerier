package query;

import java.io.FileNotFoundException;
import java.io.PrintWriter;



/**
 * Terminal based application to transform CSV output from SILK matching to JSON compliant data.
 * Uses JENA library to process SPARQL queries to also add additional information from GLUES SPARQL Endpoint to JSON data set.
 * 
 * Needs Filename for CSV-File as first argument at launch
 * 
 * 
 * @author Georg Bräunig
 * @version 0.8 
 *
 */
public class Start
{
	/**
	 * Creates CSV Reader for CSV Data processing
	 * Use {@link #main(String[])}
	 * 
	 * @param args Name of the CSV File
	 */
    public static void main( String[] args ) {
        
    	//Name of CSV File
    	String in = args[0];
    	
    	//create and run CSV-Reader to process CSV data
    	CSVReader rdr = new CSVReader();
    	String output = rdr.run(in);
    	
    	//write JSON compliant String to File 
    	try {
			PrintWriter out = new PrintWriter("processed_"+in.substring(0, in.length()-8)+".json");
			out.println(output);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}