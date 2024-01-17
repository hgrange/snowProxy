package com.herve;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Test2 {

	public static void main(String[] args) throws UnsupportedEncodingException, URISyntaxException, InterruptedException, ExecutionException 
	{	
		//String path = "https://dev181141.service-now.com/api/now/v2/table/change_request?sysparm_display_value=true&sysparm_exclude_reference_link=true&sysparm_offset=0&sysparm_limit=100&sysparm_query=sys_updated_onBETWEENjavascript%3Ags.dateGenerate%28'2023-11-09','05:44:33'%29%40javascript%3Ags.dateGenerate%28'2023-11-09','05:44:43'%29%5Esys_updated_by!=abel.tuter";
		//String path = "https://dev181141.service-now.com/api/now/v2/table/change_request?sysparm_display_value=true&sysparm_exclude_reference_link=true&sysparm_offset=0&sysparm_limit=100&sysparm_query=sys_updated_onBETWEENjavascript:gs.dateGenerate('2023-11-04','02:35:33')@javascript:gs.dateGenerate('2023-11-04','02:35:43')^sys_updated_by!=abel.tuter";
		String path = "https://dev181141.service-now.com/api/now/v2/table/incident?sysparm_display_value=true&sysparm_exclude_reference_link=true&sysparm_offset=13320&sysparm_limit=100&sysparm_query=sys_updated_onBETWEENjavascript:gs.dateGenerate('272023-11-09','08:47:13')@javascript:gs.dateGenerate('2023-11-09','08:48:18')^sys_updated_by!=abel.tuter";
		String path2 = path.replaceAll("\\(", "%28").replaceAll("\\)", "%29").replaceAll("\\^","%5E");
		String username = "abel.tuter";
		String password = "P4ssw0rd!";
		String rawAuth = username + ":" + password;
        String auth = Base64.getEncoder().encodeToString(rawAuth.getBytes());
		
	    HttpClient httpClient = HttpClient.newHttpClient();
	    
	    URI uri = URI.create(path2);
	    HttpRequest.Builder builder = HttpRequest.newBuilder(uri)
	    		.header("accept", "application/json").header("authorization", "Basic " + auth);
	    	    
	    HttpRequest request = builder.GET().build();
	       
	    CompletableFuture<HttpResponse<String>> response = 
	    		     httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

	      HttpResponse<String> res = response.get();

	      String bodyString = res.body();
	      System.out.println("bodyString = " + bodyString);
	}

}
