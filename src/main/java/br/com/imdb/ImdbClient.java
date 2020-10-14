package br.com.imdb;

import br.com.imdb.server.ApiServerSocket;

public class ImdbClient {

	public static void main(String args[]) {
		ImdbClient api = new ImdbClient();
		api.boot();
	}
	
	public void boot() {
		ApiServerSocket.getInstance().startTask();
	}
}
