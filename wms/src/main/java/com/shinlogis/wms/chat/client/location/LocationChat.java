package com.shinlogis.wms.chat.client.location;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.google.gson.Gson;
import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.common.util.Message;
import com.shinlogis.wms.headquarters.model.HeadquartersUser;
import com.shinlogis.wms.location.model.LocationUser;

public class LocationChat extends JFrame{

	JPanel p_north;
	JTextField t_ip;
	JTextField t_port;
	JButton bt;

	JTextArea area;
	JScrollPane scroll;
	JTextField t_input;

	JPanel p_south;
	AppMain appMain;

	public LocationChat(AppMain appMain) {
		this.appMain = appMain;
		
		p_north = new JPanel();
		t_ip = new JTextField("192.168.60.12");
		t_port = new JTextField("9999");
		bt = new JButton("채팅하기");
		
		area = new JTextArea();
		scroll = new JScrollPane(area);
		t_input = new JTextField();
		
		p_south = new JPanel();
		
		t_ip.setPreferredSize(new Dimension(120,30));
		t_port.setPreferredSize(new Dimension(90,30));
		bt.setPreferredSize(new Dimension(80,30));
		
		scroll.setPreferredSize(new Dimension(300,400));
		area.setBackground(Color.PINK);
		
		t_input.setPreferredSize(new Dimension(300,30));
		
		
		
		p_north.add(t_ip);
		p_north.add(t_port);
		p_north.add(bt);
		
		p_south.add(t_input);
		
		add(p_north,BorderLayout.NORTH);
		add(scroll);
		add(p_south, BorderLayout.SOUTH);
		
		bt.addActionListener(e->{
			createConnection();
		});
		
		setBounds(600,200,400,500);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		
	}
	
	
	public void createConnection() {
		try {
			Socket socket = new Socket(t_ip.getText(), Integer.parseInt(t_port.getText()));
			LocationClientThread locationClientThread = new LocationClientThread(this, socket);
			locationClientThread.start();
			
			//내가 지점인 걸 알리기
			Message message = new Message();
			message.setRequestType("user");
			message.setFrom("location");
			message.setTo("head");
			message.setContent(Integer.toString(appMain.locationUser.getLocationUserId()));
			
			Gson gson = new Gson();
			String result = gson.toJson(message);
			
			locationClientThread.send(result);
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

//	public static void main(String[] args) {
//		new Client();
//
//	}

}
