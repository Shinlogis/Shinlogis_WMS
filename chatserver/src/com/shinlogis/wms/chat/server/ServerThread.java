package com.shinlogis.wms.chat.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;

import com.google.gson.Gson;
import com.shinlogis.wms.chat.common.Message;


//서버측에서 메시지를 처리하는 쓰레드(아직 본점용인지, 지점용인지는 모호)
public class ServerThread extends Thread{

	Server server;
	Socket socket;
	BufferedReader buffr;
	BufferedWriter buffw;
	
	String locationName = "B";
	
	
	public ServerThread(Server server,Socket socket) {
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
			
			//전송된 메세지를 분석하여 적절한 처리를 해야 함
			//System.out.println(msg);
			Gson gson = new Gson();
			Message obj = gson.fromJson(msg, Message.class);
			
			if(obj.getRequestType().equals("user")) {
				String from = obj.getFrom();
				String to = obj.getTo();
				System.out.println("지점 pk " + to);
				
				//본점용 맵에 쓰레드 담기
				if(from.equals("head")) {
					server.headMap.put(to, this);
					System.out.println("현재까지 " + server.headMap.size());
				}else if(from.equals("location")) {
					server.locationMap.put(to, this);
					System.out.println("현재까지 " + server.locationMap.size());
				}
			}
			
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
