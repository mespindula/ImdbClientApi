package br.com.imdb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import org.junit.Test;

public class ImdbClientApiTest {

	@Test
	public void testSocketConnectionOK() {
		boolean connectedOk = false;
		new ImdbClient().boot();
		
		try {
			Socket socket = new Socket("localhost", 5050);
			
			connectedOk = socket.isConnected();
	        
	        socket.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
				
		assertTrue(connectedOk);
	}
	
	@Test
	public void testSocketConnectionError() {
		boolean connectedOk = false;
		new ImdbClient().boot();
		
		try {
			Socket socket = new Socket("localhost", 1234);
			
			connectedOk = socket.isConnected();
	        
	        socket.close();

		} catch (IOException ex) {
			connectedOk = Boolean.FALSE;
		}
				
		assertTrue(!connectedOk);
	}
	
	@Test
	public void testSocketInvalidMessage() {
		String msgRecived = "";
		new ImdbClient().boot();
		
		try {
			Socket socket = new Socket("localhost", 5050);
			
			OutputStream output = socket.getOutputStream();
			
			PrintWriter writer = new PrintWriter(output, true);
			writer.println("120:Borat");
			
	        InputStreamReader reader = new InputStreamReader(socket.getInputStream());
	        BufferedReader bufferedReader = new BufferedReader(reader);
	        
	        msgRecived = bufferedReader.readLine();
	        
	        socket.close();
        
		} catch (IOException ex) {
			ex.printStackTrace();
		}
				
		assertEquals("Invalid mensage recived", msgRecived.substring(msgRecived.indexOf(":") +1));
	}
	
	@Test
	public void testSocketNoResultsMessage() {
		String msgRecived = "";
		new ImdbClient().boot();
		
		try {
			Socket socket = new Socket("localhost", 5050);
			
			OutputStream output = socket.getOutputStream();
			
			PrintWriter writer = new PrintWriter(output, true);
			writer.println("9:Borat____");
			
	        InputStreamReader reader = new InputStreamReader(socket.getInputStream());
	        BufferedReader bufferedReader = new BufferedReader(reader);
	        
	        msgRecived = bufferedReader.readLine();
	        
	        socket.close();
        
		} catch (IOException ex) {
			ex.printStackTrace();
		}
				
		assertEquals("No results", msgRecived.substring(msgRecived.indexOf(":") +1));
	}
	
	@Test
	public void testSocketMessageOK() {
		String msgRecived = "";
		new ImdbClient().boot();
		
		try {
			Socket socket = new Socket("localhost", 5050);
			
			OutputStream output = socket.getOutputStream();
			
			PrintWriter writer = new PrintWriter(output, true);
			writer.println("9:corporate");
			
	        InputStreamReader reader = new InputStreamReader(socket.getInputStream());
	        BufferedReader bufferedReader = new BufferedReader(reader);
	        
	        String line;
	        while ((line = bufferedReader.readLine()) != null) {
	        	msgRecived += line + "\n";
            }
	        
	        socket.close();
        
		} catch (IOException ex) {
			ex.printStackTrace();
		}
				
		assertTrue(msgRecived.contains(":"));
		
		String payload = msgRecived.substring(msgRecived.indexOf(":") +1);
		int payloadLength = Integer.parseInt(msgRecived.substring(0, msgRecived.indexOf(":")));
		
		assertTrue(payload.length() == payloadLength);
	}
}
