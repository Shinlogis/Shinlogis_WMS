package com.shinlogis.wms.chat.client.location;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import com.google.gson.Gson;
import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.chat.client.head.Message;
import com.shinlogis.wms.location.model.Location;

public class LocationChat extends JFrame{

	JPanel p_north;
	JTextField t_ip;
	JTextField t_port;
	JButton bt;

	JTextPane area;
	//JTextArea area;
	JScrollPane scroll;
	JTextField t_input;

	JPanel p_south;
	AppMain appMain;
	
	Location location;
	LocationClientThread locationClientThread;

	public LocationChat(AppMain appMain, Location location) {
		this.appMain = appMain;
		this.location = location;
		
		p_north = new JPanel();
		t_ip = new JTextField("192.168.60.18");
		t_port = new JTextField("9999");
		bt = new JButton("접속하기");
		
		area = new JTextPane(); // 💡 여기 변경됨
		area.setEditable(false);
		area.setContentType("text/html"); // HTML 사용 가능하게 설정
		area.setText("<html><body></body></html>"); // 초기화
		//area = new JTextArea();
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
		
		t_input.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					String text = t_input.getText().trim();
					if (!text.isEmpty()) {
						addMyMessage(text); // 오른쪽 정렬로 추가
						t_input.setText("");
						
					Message message = new Message();
					//메세지 만들기
					message.setRequestType("chat");
					message.setMe("location");
					message.setTarget(location);
					message.setMsg(t_input.getText());
					t_input.setText("");
					
					Gson gson = new Gson();
					String result = gson.toJson(message);
					
					locationClientThread.send(result);
					t_input.setText("");
					}
				}
			}
		});
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Message message = new Message();
				//메세지 만들기
				message.setRequestType("eixt");
				message.setMe("location");
				message.setTarget(location);
				
				Gson gson = new Gson();
				String result = gson.toJson(message);
				
				locationClientThread.send(result);
			}
		});
		
		setBounds(600,200,400,500);
		setVisible(true);
		
		
		
	}
	
	
	public void createConnection() {
		try {
			Socket socket = new Socket(t_ip.getText(), Integer.parseInt(t_port.getText()));
			locationClientThread = new LocationClientThread(this, socket);
			locationClientThread.start();
			
			//내가 지점인 걸 알리기
			Message message = new Message();
			message.setRequestType("init");
			message.setMe("location");
			message.setTarget(appMain.locationUser.getLocation());
			
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
	
	// 오른쪽 정렬로 내가 보낸 메시지 출력
    public void addMyMessage(String msg) {
        appendMessage("<div style='text-align: right; color: black;'>" + escapeHtml(msg) + "</div>");
    }

    // 왼쪽 정렬로 상대방 메시지 출력
    public void addOtherMessage(String msg) {
        appendMessage("<div style='text-align: left; color: black;'>" + escapeHtml(msg) + "</div>");
    }
    
 // HTML 메시지를 area에 추가
    private void appendMessage(String html) {
        try {
            HTMLEditorKit kit = (HTMLEditorKit) area.getEditorKit();
            HTMLDocument doc = (HTMLDocument) area.getDocument();
            kit.insertHTML(doc, doc.getLength(), html, 0, 0, null);
            area.setCaretPosition(doc.getLength()); // 스크롤 아래로
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 특수문자 이스케이프 처리
    private String escapeHtml(String text) {
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
	


}
