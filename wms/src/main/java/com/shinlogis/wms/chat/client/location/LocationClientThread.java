package com.shinlogis.wms.chat.client.location;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.shinlogis.wms.chat.view.ChattingPage;

public class LocationClientThread extends Thread{
	
	LocationChat client;
	Socket socket;
	BufferedReader buffr;
	BufferedWriter buffw;
	
	public LocationClientThread(LocationChat client, Socket socket) {
		this.client = client;
		this.socket = socket;
		
		try {
			buffr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			buffw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void listen() {
		String msg = null;
		
		try {
			msg = buffr.readLine();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send(String msg) {
		try {
			buffw.write(msg + "\n");
			buffw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		while(true) {
			listen();
		}
	}
}
