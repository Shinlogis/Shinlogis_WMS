package com.shinlogis.wms.chat.client.location;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.SwingUtilities;

import com.google.gson.Gson;
import com.shinlogis.wms.chat.client.head.Message;
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
			//System.out.println("지점의 listen 메세지 " + msg);

			//프로토콜 분석 
			Gson gson = new Gson();
			Message obj = gson.fromJson(msg, Message.class);
			if(obj.getRequestType().equals("chat")) {
				if(obj.getMe().equals("location")) {
					
					SwingUtilities.invokeLater(()->{
						client.appendMessage(obj.getMsg(), true);
					});					
				}else {
					
					SwingUtilities.invokeLater(()->{
						client.appendMessage(obj.getMsg(), false);
						
					});										
				}
			}			
			
		
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send(String msg) {
		try {
			//System.out.println("지사의 sendmsg is" + msg);
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
