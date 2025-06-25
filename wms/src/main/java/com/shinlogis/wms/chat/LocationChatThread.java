package com.shinlogis.wms.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

//서버측에서 메시지를 처리하는 쓰레드(아직 본점용인지, 지점용인지는 모호)
public class LocationChatThread extends Thread{

	Server server;
	Socket socket;
	BufferedReader buffr;
	BufferedWriter buffw;
	
	String locationName = "B";
	
	
	public LocationChatThread(Server server,Socket socket) {
		this.server = server;
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
			send(msg);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send(String msg) {
		
		//지점의 쓰레드 중 현재 쓰레드가 보유한 key값과 일치하는 쓰레드에게만 보내자
		
		try {
			buffw.write(msg + "\n");
			buffw.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while(true) {
			listen();
		}
	}
	
}
