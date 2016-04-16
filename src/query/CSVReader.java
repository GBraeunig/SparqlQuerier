package query;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * CSV Reader takes input CSV File containing SILK matches and processes  
 * JSON File with correct data for Webapp.
 * 
 * Missing data for linked data sets is queried from GLUES Sparql Endpoint
 * so ensure Internet access 
 * 
 * @author Georg Bräunig
 * @version 0.8 
 * 
 * 
 * 
 * 
 */
public class CSVReader {
/**
 * starts CSV-Reader to process CSV Inputfile. Only accepts ";" separated Files
 *
 * @throws Exeption if Input CSV  file is invalid
 * @param in CSVFile containing SILK matches
 * @return String representing JSON-Object for Webapp usage
 */
	public String run(String in) {

		//Variables for data handling
		String csvFile = in;
		
		String line = "";
		String finalString = "";
		
		//Splitter for CSV File - modify if necessary 
		String cvsSplitBy = ";";
		
		//Objects for data handling
		SPARQLQuerier spq = new SPARQLQuerier();
		BufferedReader br = null;
		
		//read CSV File
		try {
			
			br = new BufferedReader(new FileReader(csvFile));			
			
			//start String for JSON Object
			finalString += "{\n\"data\":[";
			
			//read each line as long as there are any
			while ((line = br.readLine()) != null) {
				
				// use designated separator for splitting
				String[] temp_line = line.split(cvsSplitBy);

				if (temp_line.length >= 4) {

					// build strings for output
					
					//individual keyword ID property
					String linestring = "{\"ID\": \"" + spq.getSubjecttoprefLabel(temp_line[0]) + "\", ";
					
					//keyword name property
					linestring += "\"Name\": \"" + temp_line[1] + "\", ";
					
					//count of linked metadata sets property
					linestring += "\"Count\": \"" + temp_line[2] + "\",";
					
					//array of linked data sets
					linestring += "\"Links\": [";
					
					//cycle through linked data sets to determine 
					for (int i = 3; i < temp_line.length; i++) {
						
						//open js object
						linestring +="{";
						//ID property
						linestring +="\"ID\": \""+temp_line[i].substring(2,temp_line[i].length()-4)+"\", ";
						//Title property
						linestring +="\"title\": "+spq.getTitle(temp_line[i])+", ";
						//subjects array
						linestring +="\"subjects\": ["+spq.getSubjects(temp_line[i])+"]";
						//close object
						linestring +="},";						
						//cut last comma for last item
						if (i == temp_line.length-1){
							linestring = linestring.substring(0, linestring.length()-1);
						}
					}
					//close item
					linestring += "]},";
					linestring += "\n";
					
					//add item String to String representing complete item
					finalString += linestring;
				}
			}
			
			//final String of JSON-Object
			finalString = finalString.substring(0, finalString.length()-2);
			finalString += "]\n}";
			

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		//return the final String for JSON-Object
		System.out.println("Done");
		return finalString;
	}

}
