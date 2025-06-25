package com.shinlogis.wms.chat.client.head;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.shinlogis.wms.AppMain;

public class HeadChat extends JFrame{
	

	JPanel p_north;
	JTextField t_ip;
	JTextField t_port;
	JButton bt;

	JTextArea area;
	JScrollPane scroll;
	JTextField t_input;

	JPanel p_south;
	AppMain appMain;
	
	
	public HeadChat() {
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
		
		setBounds(600,200,400,500);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	

}
