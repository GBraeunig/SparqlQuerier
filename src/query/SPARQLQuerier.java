package query;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;

public class SPARQLQuerier {
	
	private String query(String uri, String vocab){
		String result = "";
		uri = uri.substring(2,uri.length());
		//System.out.println(uri);
		String s2 = "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n"+"SELECT ?b \n"+"where{\n"+"<"+uri+"> "+"dc:"+vocab+" ?b. \n"+"}\n";
	    Query query = QueryFactory.create(s2); //s2 = the query above
	    QueryExecution qExe = QueryExecutionFactory.sparqlService( "http://metadata.demo.52north.org:8890/sparql", query );
	    ResultSet results = qExe.execSelect();
	    
	    
	    //ResultSetFormatter.out(System.out, results, query) ;
	    Object[] resultslist = ResultSetFormatter.toList(results).toArray();
	    for(int i = 0; i <resultslist.length; i++ ){
	    	String part = resultslist[i].toString();
	    	part = part.substring(8, part.length() -3);
	    	part = "\""+part+"\"";
	  
	    	//System.out.println(part);
	    	if ((resultslist.length >1) && (i >0)){
	    		
	    		result = result+", ";
	    		//cut last comma
	    		if(i == resultslist.length-1){
	    			result = result.substring(0,result.length()-1);
	    		}
	    		//System.out.println(result);
	    	}
	    	
	    	result = result+part;
	    	
	    	
	    }
	    if(result.equals("")){
    		result = "null";
    	}
	    //System.out.println(result);
		return result;
	}

	
	public String getTitle(String uri){
		String vocab ="title";
		String title = query(uri,vocab);
		return title;
	}
	
	public String getSubjects(String uri){
		String vocab ="subject";
		String subject = query(uri,vocab);
		return subject;
		}
	
	public String getSubjecttoprefLabel(String uri){
		String vocab ="prefLabel";
		String s2 = "PREFIX skos: <http://www.w3.org/2008/05/skos-xl#> \n"+"SELECT ?b \n"+"where{\n ?b "+"skos:"+vocab+"<"+uri+">\n"+"}\n";
		Query query = QueryFactory.create(s2); //s2 = the query above
		QueryExecution qExe = QueryExecutionFactory.sparqlService( "http://202.45.139.84:10035/catalogs/fao/repositories/agrovoc", query );
		System.out.println("done!");
		
		ResultSet results = qExe.execSelect();
		Object [] result = ResultSetFormatter.toList(results).toArray();
		String subject = "NULL";
		if(result.length > 0){
			subject = result[0].toString();
			subject = subject.substring(8, subject.length() -3);
		}
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
		System.out.println(subject);		
		return subject;
	}
}
