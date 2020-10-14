package br.com.imdb.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

public class ApiServerSocket {
	
	static Logger logger = Logger.getLogger(ApiServerSocket.class);
	
	private static ApiServerSocket serverSocket = null;
	private static Thread task = null;
	private ServerSocket server;
	
	private boolean stop = false;
    private boolean finalized = false;
  

	public static ApiServerSocket getInstance() {
		if(serverSocket == null){
			serverSocket = new ApiServerSocket();
        }
        
        return serverSocket;
	}
	
	private ApiServerSocket() {	}
	
	public boolean startTask(){
        if(task == null || !task.isAlive()){
            task = new Thread(new Runnable() {
                @Override
                public void run() {
                      runTask();
                }
            });

            stop = false;
            task.start();

           while(!task.isAlive()){
               if(task.isAlive()) {
                   return true;
               }
           }

           return task.isAlive();
        }else{
            return task.isAlive();
        }
    }
	
	public boolean stopTask() {
        if(task != null && task.isAlive()){
            stop = true;
            try {
                server.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        	
            while(!finalized){
                if(finalized) {
                    return true;
                }
            }
            
            return true;
        }else{
            return !task.isAlive();
        }
    }
	
	private void runTask(){
		int port = 5050;
        try {
            server = new ServerSocket(port);           
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        finalized = false;
        
        while(!stop){
            try {
            	logger.info("listening at port " + port); 
            	
            	Socket socket = server.accept();
            	
            	new JobSocket(socket).start();
                
            } catch(IOException e){
                e.printStackTrace();
                continue;
            }
        }
        finalized = true;
    }
}
