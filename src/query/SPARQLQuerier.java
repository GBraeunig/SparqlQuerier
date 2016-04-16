package query;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
/**
 * SPARQLQuerier handles communication with SPARQL Endpoint for GLUES Metadata. Additional external information might be requested this way.
 * URL for SPARQL Endpoint is currently "http://metadata.demo.52north.org:8890/sparql"
 * Currently hardcoded för "dublin core" vocabulary
 * 
 * @version 0.8
 * 
 * @author Georg Bräunig
 *
 */
public class SPARQLQuerier {
	/**
	 * Query the SPARQL Endpoint for data entries.
	 * 
	 * Needs the URI for the Keyword and the corresponding predicate for Triple.
	 * 
	 * 
	 * @param uri URI for the keyword 
	 * @param vocab predicate for required information (example: subject,..)
	 * @return data items resulting query
	 */
	private String query(String uri, String vocab){
		
		//variables for data handling
		String result = "";
		uri = uri.substring(2,uri.length());
		
		//String for query
		String s2 = "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n"+"SELECT ?b \n"+"where{\n"+"<"+uri+"> "+"dc:"+vocab+" ?b. \n"+"}\n";
		
		//Query processing
	    Query query = QueryFactory.create(s2); //s2 = the query above
	    QueryExecution qExe = QueryExecutionFactory.sparqlService( "http://metadata.demo.52north.org:8890/sparql", query );
	    ResultSet results = qExe.execSelect();
	    
	    //divide Resultset in array of individual results
	    Object[] resultslist = ResultSetFormatter.toList(results).toArray();
	    
	    //cycle through results and format them for JSON compliance 
	    for(int i = 0; i <resultslist.length; i++ ){
	    	String part = resultslist[i].toString();
	    	part = part.substring(8, part.length() -3);
	    	part = "\""+part+"\"";
	 
	    	if ((resultslist.length >1) && (i >0)){
	    		
	    		result = result+", ";
	    		
	    		//cut last comma
	    		if(i == resultslist.length-1){
	    			result = result.substring(0,result.length()-1);
	    		}
	    	}	    	
	    	result = result+part;	    	
	    }
	    
	    //if result is empty define "NULL" object for JSON-Object
	    if(result.equals("")){
    		result = "null";
    	}
	    //return result of the query
		return result;
	}

	/**
	 *  Method to query for Title of keyword using private query method
	 * 
	 * @param uri URI of the keyword
	 * @return String for Title of the keyword
	 */
	public String getTitle(String uri){
		String vocab ="title";
		String title = query(uri,vocab);
		return title;
	}
	
	/**
	 * Method to query for subjects linked to keyword
	 * 
	 * @param uri URI of the keyword
	 * @return String representing linked subjects to keyword
	 */
	public String getSubjects(String uri){
		String vocab ="subject";
		String subject = query(uri,vocab);
		return subject;
		}
	
	/**
	 * Special method for substituting matched prefLabel with general URI for Object.
	 * Necessary to correct for unique identifier for Keyword across languages
	 * 
	 * @param uri URI for prefLabel for Keyword
	 * @return corresponding ID URI 
	 */
	public String getSubjecttoprefLabel(String uri){
		
		//Strings for Query processing
		String vocab ="prefLabel";
		String s2 = "PREFIX skos: <http://www.w3.org/2008/05/skos-xl#> \n"+"SELECT ?b \n"+"where{\n ?b "+"skos:"+vocab+"<"+uri+">\n"+"}\n";
		
		//Query Processing
		Query query = QueryFactory.create(s2); //s2 = the query above
		QueryExecution qExe = QueryExecutionFactory.sparqlService( "http://202.45.139.84:10035/catalogs/fao/repositories/agrovoc", query );
		ResultSet results = qExe.execSelect();
		
		//Formating result to create JSON compliant String
		Object [] result = ResultSetFormatter.toList(results).toArray();
		String subject = "NULL";
		if(result.length > 0){
			subject = result[0].toString();
			subject = subject.substring(8, subject.length() -3);
		}
		
		//also compensate for usage of "alLabel" predicate during SILK matching
		if(subject.equals("NULL")){
			vocab ="altLabel";
			s2 = "PREFIX skos: <http://www.w3.org/2008/05/skos-xl#> \n"+"SELECT ?b \n"+"where{\n ?b "+"skos:"+vocab+"<"+uri+">\n"+"}\n";
			query = QueryFactory.create(s2); 
			qExe = QueryExecutionFactory.sparqlService( "http://202.45.139.84:10035/catalogs/fao/repositories/agrovoc", query );
			results = qExe.execSelect();
			result = ResultSetFormatter.toList(results).toArray();
			if(result.length > 0){
				subject = result[0].toString();
				subject = subject.substring(8, subject.length() -3);
			}
		}
		
		//return String for ID URI for keyword
		return subject;
	}
}
