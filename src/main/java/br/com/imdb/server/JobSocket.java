package br.com.imdb.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.imdb.client.IMDB;

public class JobSocket extends Thread {
	
	Logger logger = Logger.getLogger(JobSocket.class);
	
	private Socket socket;
	
	public JobSocket(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		logger.info("connected"); 
		
        try {
        	socket.setSoTimeout(60000);
        	
        	OutputStreamWriter output = new OutputStreamWriter(socket.getOutputStream());        	
        	InputStreamReader reader = new InputStreamReader(socket.getInputStream());
        	BufferedReader bufferedReader = new BufferedReader(reader);

        	String msg = bufferedReader.readLine();
        	
        	if (isValidMessage(msg)) {
        		
        		logger.debug(msg);
        		
        		String queryMessage = getQueryMessage(msg);
            	
            	StringBuilder imdbMessage = getImdbData(queryMessage);
            	
            	output.write(imdbMessage.length() + ":" + imdbMessage.toString());
        	} else {
        		output.write("23:Invalid mensage recived");
            }
        	
        	output.flush();
            output.close();
            
        } catch (IOException ex) {
        	logger.error("Socket server error", ex);
        }
	}
	
	private boolean isValidMessage(String msg) {
		if (msg != null && !"".equals(msg) && msg.contains(":")) {
			String[] msgParties = msg.split(":");
			
			if(msgParties.length == 2) {
				try {
					int queryLength = Integer.parseInt(msgParties[0]);
					
					return msgParties[1].length() == queryLength;
					
				} catch (Exception ex) {
					logger.error("Error validating message", ex);
					return Boolean.FALSE;
				}
			}
		}
		return Boolean.FALSE;
	}
	
	private String getQueryMessage(String msg) {
		String[] msgParties = msg.split(":");
		return msgParties[1];
	}
	
	private StringBuilder getImdbData(String query) {
		StringBuilder title = new StringBuilder();
		try {
			List<String> titles = new IMDB(query).getListByTitles();

			titles.forEach(t -> {
				title.append(t).append("\n");
			});
			
		} catch (IOException ex) {
			logger.error("Error getting IMDB data", ex);
		}
		
		if (title.toString().isEmpty()) {
			title.append("No results");
		}
		
		return title;
	}
}
