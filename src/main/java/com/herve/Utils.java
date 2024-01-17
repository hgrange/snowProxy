package com.herve;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

public class Utils {

  static final Logger log = LoggerFactory.getLogger(Utils.class);
  static String url = System.getenv("SNOW_URL");
  static String logsdir = System.getenv("SNOW_LOGS");

  String get(HttpHeaders headers, UriInfo uriInfo, String logFile, ReqRepDao reqrepDAO) throws IOException, URISyntaxException, InterruptedException, ExecutionException {
	      
	      url = "https://www.google.com";
	      FileWriter fw = new FileWriter(logsdir+logFile, true);
	      BufferedWriter bw = new BufferedWriter(fw);
	      PrintWriter out = new PrintWriter(bw);
	     
	      MultivaluedMap<String, String> a_headers = headers.getRequestHeaders();
	      int size = a_headers.size();
	   
	      Iterator<Map.Entry<String, List<String>>> iterator =   a_headers.entrySet().iterator();
	      
	      String[] S_headers = new String[size * 2 ];
	      
	      int index = 0;
	      List<String> restrictedHeaders = Arrays.asList("Connection","Host");
	      while (iterator.hasNext()) {
	    	    Map.Entry<String, List<String>> pair = iterator.next();
	    	    int i_values = 0;
	    		if ( !( restrictedHeaders.contains( pair.getKey() ) ) ) {
	    	      while ( i_values < pair.getValue().size() ) {
	    	      	  S_headers[index]=pair.getKey();
	    	    	  index++;
	    	    	  S_headers[index]=pair.getValue().get(i_values);
	    	    	  index++;
	    	    	  i_values++;
	    	      }
	    		} 
	       }

	      //String[] S_headers = a_headers.toString().replace("deflate","Accept-Encoding=deflate").replace("=",",").replace("[","").replace("]","").split(",");
	      String auth = null;
	      try {
	        auth = headers.getRequestHeader("authorization").get(0);
	      } catch (IndexOutOfBoundsException iobe ) {
	    	out.close();
	    	bw.close();
		    fw.close();
	    	return "No authorisation header !!!\n";
	      }

	      String path = uriInfo.getPath();
	      String query = uriInfo.getRequestUri().getQuery();

	      String query2 = query.replaceAll("//","/").replaceAll("\\(", "%28").replaceAll("\\)", "%29").replaceAll("\\^","%5E"); 

	      out.println(new Timestamp(System.currentTimeMillis()) +" ---------- GET -------------");
	      String uri = (url+path+"?"+query2).replaceAll("com\\/\\/","com\\/");
	      out.println("uri = "+uri);
	      URI sourceURI = new URI(uri);
	      
	      HttpClient httpClient = HttpClient.newHttpClient();
	      
	      HttpRequest request = HttpRequest.newBuilder(sourceURI).header("accept", "application/json")
	               .header("authorization", auth).GET().build();
	      //HttpRequest request = HttpRequest.newBuilder(sourceURI).headers(S_headers).GET().build();
	    	     
	      CompletableFuture<HttpResponse<String>> response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
	      HttpResponse<String> res = response.get();

	      String bodyString = res.body();
	      String id = path+"?"+query2;
      	  ReqRep prevReqRep = reqrepDAO.readReqRep(id);
      	  
	      if ( res.statusCode() == 200 ) {
	    	 if (prevReqRep == null) {
	    	  
	        	ReqRep reqrep = new ReqRep();
	        	reqrep.setRequest(id);
		    	reqrep.setResponse(bodyString);
	          	reqrepDAO.createReqRep(reqrep);
	    	 }
	     } else {
	    	 if (prevReqRep != null) {
	    		 bodyString = prevReqRep.getResponse();
	    	 }
	     }
	/*         if (!reqrepDAO.findRequest(request).isEmpty()) {
	              return Response.status(Response.Status.BAD_REQUEST)
	                             .entity("RequestResponse already exists").build();
	          }

	          reqrepDAO.updateReqRep(reqrep);
	          return Response.status(Response.Status.NO_CONTENT).build();
	          
	    	  
	    	  reqrepDAO.createReqRep(reqrep);*/
	      
	      
	      out.println("sourceURI = "+sourceURI.toString());
	      out.println("status = "+ res.statusCode());
	      out.println("rep = "+bodyString);
	      out.println("----------------------------");
	      //logger.log(Level.INFO, "rep-Get = "+json.toString());
	          

	      out.close();
	      bw.close();
	      fw.close();
	      return bodyString;
  }
}
