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
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import com.google.gson.Gson;
import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.chat.client.head.Message;
import com.shinlogis.wms.location.model.Location;

public class LocationChat extends JFrame{
	
	JTextPane chatPane;
    JScrollPane scrollPane;

    StyledDocument doc;
    

	JPanel p_north;
	JTextField t_ip;
	JTextField t_port;
	JButton bt;

	JTextArea area;
	JScrollPane scroll;
	JTextField t_input;

	JPanel p_south;
	AppMain appMain;
	
	Location location;
	LocationClientThread locationClientThread;

	public LocationChat(AppMain appMain, Location location) {
		this.appMain = appMain;
		this.location = location;
		
		chatPane = new JTextPane();
        chatPane.setEditable(false);
        doc = chatPane.getStyledDocument();
        scrollPane = new JScrollPane(chatPane);
		
		p_north = new JPanel();
		t_ip = new JTextField("192.168.123.101");
		t_port = new JTextField("9999");
		bt = new JButton("접속하기");
		
		area = new JTextArea();
		scroll = new JScrollPane(area);
		t_input = new JTextField();
		
		p_south = new JPanel();
		
		t_ip.setPreferredSize(new Dimension(120,30));
		t_port.setPreferredSize(new Dimension(90,30));
		bt.setPreferredSize(new Dimension(80,30));
		
		scroll.setPreferredSize(new Dimension(300,400));
		area.setBackground(Color.PINK);
		
		t_input.setPreferredSize(new Dimension(350, 40));
		
		
		//조립
		p_north.add(t_ip);
		p_north.add(t_port);
		p_north.add(bt);
		p_south.add(t_input);
		
		add(p_north,BorderLayout.NORTH);
		add(scroll);
		add(p_south, BorderLayout.SOUTH);
		add(scrollPane, BorderLayout.CENTER);
		
		
		//이벤트(버튼 누르면 접속되게)
		bt.addActionListener(e->{
			createConnection();
		});
		
		t_input.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					
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
			
			appendMessage(location.getLocationName() + " 이 입장했습니다.", false);
			
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
	
	 //채팅 정렬 스타일
    public void appendMessage(String message, boolean isMine) {
        // 스타일 생성
        SimpleAttributeSet attrSet = new SimpleAttributeSet();
        StyleConstants.setAlignment(attrSet, isMine ? StyleConstants.ALIGN_RIGHT : StyleConstants.ALIGN_LEFT);
        StyleConstants.setForeground(attrSet, isMine ? Color.BLUE : Color.BLACK);
        StyleConstants.setFontSize(attrSet, 14);
        StyleConstants.setSpaceAbove(attrSet, 4);
        StyleConstants.setSpaceBelow(attrSet, 4);

        try {

            // 새 단락 삽입
            int length = doc.getLength();
            doc.insertString(length, message + "\n", attrSet);
            doc.setParagraphAttributes(length, message.length(), attrSet, false);

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
	
	


}
