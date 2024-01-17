package com.herve;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.http.HttpClient;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ServletProxy
 */
@WebServlet({ "/ServletProxy", "/test2" })
public class ServletProxy extends HttpServlet {
	private static final long serialVersionUID = 1L;


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	synchronized protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
		//response.getWriter().append("{\"type\": \"JSON\"}");
		//response.getWriter().append("Served at: ").append(request.getContextPath());
        Map<String, String[]> params = request.getParameterMap();	
        Map<String, Object> headers = Collections.list(request.getHeaderNames())    
        	    .stream()
        	    .collect(Collectors.toMap(
        	        Function.identity(), 
        	        h -> Collections.list(request.getHeaders(h))
        	    ));
        
		System.out.println("Print GET parameters");
		String contextPath = request.getContextPath();
		String queryString = request.getQueryString();
	
	
		TrustManager[] trustAllCertificates = new TrustManager[] {
		        (TrustManager) new X509TrustManager() {
		            @Override
		            public X509Certificate[] getAcceptedIssuers() {
		                return null; // Not relevant.
		            }
		            @Override
		            public void checkClientTrusted(X509Certificate[] certs, String authType) {
		                // Do nothing. Just allow them all.
		            }
		            @Override
		            public void checkServerTrusted(X509Certificate[] certs, String authType) {
		                // Do nothing. Just allow them all.
		            }
		        }
		};
		  

		    HostnameVerifier trustAllHostnames = new HostnameVerifier() {
		        @Override
		        public boolean verify(String hostname, SSLSession session) {
		            return true; // Just allow them all.
		        }
		    };

		    
		
		System.setProperty("jsse.enableSNIExtension", "false");
	    SSLContext sc = null;
		try {
			sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCertificates, new SecureRandom());
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    
	    URL url =  new URL("https://dev181141.service-now.com/" + "?" + queryString);
	 
		
	    CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setDoOutput(true);
		connection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        connection.setDefaultHostnameVerifier(trustAllHostnames);
        Enumeration<?> e;
        for (e = request.getHeaderNames(); e.hasMoreElements();) {
            String nextHeaderName = (String) e.nextElement();
            String headerValue = request.getHeader(nextHeaderName);
            if ( nextHeaderName.compareTo("Host") == 0 ) {
            	headerValue = "dev181141.service-now.com";
            }

            connection.setRequestProperty(nextHeaderName, headerValue);
        }
        connection.connect();
        JsonParser jp = new JsonParser();
        
				
//		InputStream input = url.openStream();
        OutputStream outputStream = connection.getOutputStream();
        
        InputStream input = (InputStream)connection.getContent();
		InputStreamReader isr = new InputStreamReader(input);
//		JsonElement root = jp.parse(isr);



/*		byte[] buffer = new byte[20*1024 ];

		while(true) {
			  int readSize=input.read(buffer);
			  if(readSize==-1)
			    break;
			  outputStream.write(buffer,0,readSize);
		} */


		BufferedReader in = new BufferedReader(isr);
		String i;   
		
		

		System.out.println("-------------------------");
		System.out.println("headers = "+e);
		System.out.println("url = "+url);
		System.out.println("responseCode = " + connection.getResponseCode());
		System.out.println("response = ");
		
		PrintWriter out = response.getWriter();  
		
        while ((i = in.readLine()) != null)    
        {   
        	System.out.println(i);
        	out.println(i);
        }   
        
        out.close();

	}

	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("------- doPost ----------");
		//doGet(request, response);
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doHead(HttpServletRequest, HttpServletResponse)
	 */
	protected void doHead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
