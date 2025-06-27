package com.shinlogis.wms.chat.client.head;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.SwingUtilities;

import com.google.gson.Gson;
import com.shinlogis.wms.chat.view.ChattingPage;

public class HeadClientThread extends Thread{
	
	ChattingPage chattingPage;
	Socket socket;
	BufferedReader buffr;
	BufferedWriter buffw;
	public Message message;
	public HeadChat headChat;
	
	public HeadClientThread(ChattingPage chattingPage, Socket socket, Message message) {
		this.chattingPage = chattingPage;
		this.socket = socket;
		this.message = message;
		
		try {
			buffr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			buffw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	//받을 때는 문자열을 객체로
	public void listen() {
		try {
			String msg = buffr.readLine();
			//System.out.println("본사의 listen is " + msg);

			Gson gson = new Gson();
			Message obj = gson.fromJson(msg, Message.class);
			if(obj.getRequestType().equals("chat")) {
				if(obj.getMe().equals("head")) {
					SwingUtilities.invokeLater(()->{
						headChat.appendMessage(obj.getMsg(), true);
					});					
				}else {
					SwingUtilities.invokeLater(()->{
						headChat.appendMessage(obj.getMsg(), false);
					});										
				}
			}			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send(String msg) {
		try {
			//System.out.println("본사의 sendmsg is " + msg);
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
