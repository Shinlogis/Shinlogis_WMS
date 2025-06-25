package com.shinlogis.wms.chat.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.google.gson.Gson;
import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.chat.client.head.HeadClientThread;
import com.shinlogis.wms.common.config.Config;
import com.shinlogis.wms.common.config.Page;
import com.shinlogis.wms.common.util.Message;
import com.shinlogis.wms.location.model.Location;
import com.shinlogis.wms.location.repository.LocationDAO;

public class ChattingPage extends Page{

	JPanel pPageName;
	JLabel laPageName;
	
	JPanel p_center;
	JComboBox cb_location;
	
	LocationDAO locationDAO;
	List<Location> locations;
	
	
	String ip = "192.168.60.12";
	int port = 9999;
	
	
	public ChattingPage(AppMain appMain) {
		super(appMain);
		
		pPageName = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pPageName.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.PAGE_NAME_HEIGHT));
		laPageName = new JLabel("채팅 > 지점과 채팅하기");
		
		p_center = new JPanel(new FlowLayout(FlowLayout.LEFT));
		cb_location = new JComboBox();
		
		locationDAO = new LocationDAO();
		
		locations = locationDAO.getLocation();
		for(Location loc : locations) {
			cb_location.addItem(loc);
			System.out.println(loc.getLocationName());
		}
		
		
		//스타일
		setBackground(Color.LIGHT_GRAY);
		p_center.setBackground(Color.LIGHT_GRAY);
		p_center.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.PAGE_NAME_HEIGHT));
		
		//조립 
		p_center.add(cb_location);
		pPageName.add(laPageName);
		add(pPageName);
		add(p_center);
		
		
	}
	
	//지점 수 만큼 서버와 연결해두기
	public void createConnection() {
		//System.out.println("지점 수는 " + locations.size());
		for(int i=0; i<locations.size(); i++) {
			try {
				Socket socket = new Socket(ip, port);
				HeadClientThread headClientThread = new HeadClientThread(this, socket);//지점 수 만큼 소켓 만들기
				headClientThread.start();
				//메세지 만들기
				Message message = new Message();
				message.setRequestType("user");
				message.setFrom("head");
				message.setTo(Integer.toString(locations.get(i).getLocationId()));
				
				Gson gson = new Gson();
				String result = gson.toJson(message);
				
				headClientThread.send(result);
				
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	
	
	
}