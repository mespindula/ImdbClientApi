package br.com.imdb.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class IMDB {
	
	Logger logger = Logger.getLogger(IMDB.class);
	
	String queryMessage;
	
	public IMDB(String queryMessage) {
		this.queryMessage = queryMessage;
	}
	
	public List<String> getListByTitles() throws IOException {
		List<String> titles = new ArrayList<String>();
		
		String urlStr = "https://www.imdb.com/find?s=tt&exact=true&q="+queryMessage;
		logger.info("Calling "+urlStr); 
		
		URL url = new URL(urlStr);
		URLConnection urlConn = url.openConnection();
		InputStreamReader in = new InputStreamReader(urlConn.getInputStream(), "UTF-8");
		BufferedReader reader = new BufferedReader(in);
	         
		String page = "";
	    String current;
	         
         while((current = reader.readLine()) != null) {
        	 page += current;
         }
         
         String[] partial = page.split("<td class=\"result_text\">(.*?)/\" >");
         
         for (int index = 1; index < partial.length; index++) {
        	 if (partial[index].contains(") <")) {
        		 titles.add(partial[index].substring(0, partial[index].indexOf(") <") + 1).replace("</a>", ""));
        	 }
         }
         
         return titles;
	}
	
}
