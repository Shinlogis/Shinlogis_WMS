package com.shinlogis.wms.chat.client.head;


import javax.swing.*;

import javax.swing.text.*;

import com.google.gson.Gson;
import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.location.model.Location;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;



public class HeadChat extends JFrame{

	JTextPane chatPane;
    JScrollPane scrollPane;

    StyledDocument doc;
    
    JPanel p_north;
    JLabel la_name;
    JPanel p_south;
    JTextField t_input;
    
    AppMain appMain;

	Location location;
	
	public HeadClientThread headClientThread;


    public HeadChat(Location location) {
    	this.location = location;
    	
    	p_north = new JPanel();
		la_name = new JLabel(location.getLocationName() + "과 채팅");

        p_south = new JPanel();
        t_input = new JTextField();
        
        t_input.setPreferredSize(new Dimension(350, 40));


        chatPane = new JTextPane();
        chatPane.setEditable(false);
        doc = chatPane.getStyledDocument();
        scrollPane = new JScrollPane(chatPane);


        //조립
        p_north.add(la_name);
        p_south.add(t_input);
        add(p_north, BorderLayout.NORTH);
        add(p_south, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);


        
        //키 이벤트
        t_input.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					Message message = new Message();
					// 메세지 만들기
					message.setRequestType("chat");
					message.setMe("head");
					message.setTarget(location);
					message.setMsg(t_input.getText());
					t_input.setText("");

					//보낼 때는 객체를 문자열로
					Gson gson = new Gson();
					String result = gson.toJson(message);

					headClientThread.send(result);
				}
			}
		});

        
        //창 닫기
        addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				/*
				Message message = new Message();
				// 메세지 만들기
				message.setRequestType("eixt");
				message.setMe("head");

				Gson gson = new Gson();
				String result = gson.toJson(message);

				headClientThread.send(result);
				*/
				System.out.println("closing");
			}
		});
        


        setBounds(600, 200, 400, 500);
        setVisible(true);
        

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

