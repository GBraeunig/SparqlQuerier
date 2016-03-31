package query;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CSVReader {

	public String run(String in) {

		String csvFile = in;
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ";";
		SPARQLQuerier spq = new SPARQLQuerier();
		String finalString = "";
		
		try {
			
			br = new BufferedReader(new FileReader(csvFile));
			
			
			//open JSON
			finalString += "{\n\"data\":[";
			
			while ((line = br.readLine()) != null) {
				// use semicolon as separator
				String[] temp_line = line.split(cvsSplitBy);
				System.out.println(temp_line.length);
				if (temp_line.length >= 4) {

					// build strings for output
					
					//individual keyword
					String linestring = "{\"ID\": \"" + spq.getSubjecttoprefLabel(temp_line[0]) + "\", ";
					//keyword name
					linestring += "\"Name\": \"" + temp_line[1] + "\", ";
					//count of metadatasets
					linestring += "\"Count\": \"" + temp_line[2] + "\",";
					//link property array
					linestring += "\"Links\": [";
					
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
					finalString += linestring;
				}
			}
			
			finalString = finalString.substring(0, finalString.length()-2);
			finalString += "]\n}";
			//System.out.println(finalString);
			

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

		System.out.println("Done");
		return finalString;
	}

}
