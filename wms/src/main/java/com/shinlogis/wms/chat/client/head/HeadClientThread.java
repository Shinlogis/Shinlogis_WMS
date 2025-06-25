package com.shinlogis.wms.chat.client.head;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.shinlogis.wms.chat.view.ChattingPage;

public class HeadClientThread extends Thread{
	
	ChattingPage chattingPage;
	Socket socket;
	BufferedReader buffr;
	BufferedWriter buffw;
	
	public HeadClientThread(ChattingPage chattingPage, Socket socket) {
		this.chattingPage = chattingPage;
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
