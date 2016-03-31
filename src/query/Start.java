package query;

import java.io.FileNotFoundException;
import java.io.PrintWriter;




public class Start
{
    public static void main( String[] args ) {
        
    	String in = args[0];
    	CSVReader rdr = new CSVReader();
    	String output = rdr.run(in);
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