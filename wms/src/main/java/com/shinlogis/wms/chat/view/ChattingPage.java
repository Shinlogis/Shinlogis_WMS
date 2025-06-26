package com.shinlogis.wms.chat.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.google.gson.Gson;
import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.chat.client.head.HeadChat;
import com.shinlogis.wms.chat.client.head.HeadClientThread;
import com.shinlogis.wms.chat.client.head.Message;
import com.shinlogis.wms.chat.client.location.LocationChat;
import com.shinlogis.wms.common.config.Config;
import com.shinlogis.wms.common.config.Page;
import com.shinlogis.wms.location.model.Location;
import com.shinlogis.wms.location.repository.LocationDAO;

public class ChattingPage extends Page{

	JPanel pPageName;
	JLabel laPageName;
	
	JPanel p_center;
	JComboBox cb_location;
	
	LocationDAO locationDAO;
	List<Location> locations;
	
	
	String ip = "192.168.60.18";
	int port = 9999;
	Location dummy = null;
	Vector<HeadClientThread> vector = new Vector<>(); 
	
	
	public ChattingPage(AppMain appMain) {
		super(appMain);
		
		pPageName = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pPageName.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.PAGE_NAME_HEIGHT));
		laPageName = new JLabel("채팅 > 지점과 채팅하기");
		
		p_center = new JPanel(new FlowLayout(FlowLayout.LEFT));
		cb_location = new JComboBox();
		
		locationDAO = new LocationDAO();
		
		dummy = new Location();
		dummy.setLocationName("지점선택");
		cb_location.addItem(dummy);
		locations = locationDAO.getLocation();
		for(Location loc : locations) {
			cb_location.addItem(loc);
		}
		cb_location.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					Location location = (Location)cb_location.getSelectedItem();
					HeadChat headChat = new HeadChat(location);
					
					//백터에 들어있는 쓰레드를 대상으로 콤보박스에서 선택한 지점과 같은 녀석에게 지금 띄운 headChat을 넘겨주자
					int index = getIndexOfThreadInList(vector, location.getLocationId()); //채팅을 담당할 쓰레드
					vector.get(index).headChat = headChat;
					headChat.headClientThread = vector.get(index);
				}
			}
		});
		
		
		
		
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
	
	//특정 List안에 들어있는 쓰레드가 보유한 Message안의 locationId를 가진자를 찾기 
	public int getIndexOfThreadInList(Vector<HeadClientThread> vec, int locationId) {
		int index = -1;
		
		for(int i=0; i<vec.size(); i++) {
			HeadClientThread hc = vec.get(i);
			if(locationId==hc.message.getTarget().getLocationId()) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	//지점 수 만큼 서버와 연결해두기
	public void createConnection() {
		//System.out.println("지점 수는 " + locations.size());
		for(int i=0; i<locations.size(); i++) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				Message message = new Message();
				Socket socket = new Socket(ip, port);
				HeadClientThread headClientThread = new HeadClientThread(this, socket,message);//지점 수 만큼 소켓 만들기
				headClientThread.start();
				
				//메세지 만들기
				message.setRequestType("init");
				message.setMe("head");
				message.setTarget(locations.get(i));
				
				Gson gson = new Gson();
				String result = gson.toJson(message);
				
				headClientThread.send(result); //최초의 접속과 동시에 지점의 정보를 넘긴다
				vector.add(headClientThread);
				
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	
	
	
}