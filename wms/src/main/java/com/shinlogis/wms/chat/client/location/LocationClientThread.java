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
			//client.area.append(msg + "\n");
			//프로토콜 분석 
			//내가 지점인 걸 알리기
			Gson gson = new Gson();
			Message obj = gson.fromJson(msg, Message.class);
			
			if(obj.getRequestType().equals("chat")) {
				if(obj.getMe().equals("head")) {
					SwingUtilities.invokeLater(() -> {
						client.addMyMessage(obj.getMsg()); //우측 정렬 
					});	
				}else {
					SwingUtilities.invokeLater(() -> {
						client.addOtherMessage(obj.getMsg()); // 💬 왼쪽 정렬
					});						
				}
			}
			
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
