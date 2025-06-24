package com.shinlogis.wms.chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client extends JFrame{

	JPanel p_north;
	JTextField t_port;
	JButton bt;

	JTextArea area;
	JScrollPane scroll;
	JTextField t_input;

	JPanel p_south;

	public Client() {
		p_north = new JPanel();
		t_port = new JTextField();
		bt = new JButton("서버가동");
		
		area = new JTextArea();
		scroll = new JScrollPane(area);
		t_input = new JTextField();
		
		p_south = new JPanel();
		
		t_port.setPreferredSize(new Dimension(100,30));
		bt.setPreferredSize(new Dimension(60,30));
		
		scroll.setPreferredSize(new Dimension(300,400));
		area.setBackground(Color.PINK);
		
		t_input.setPreferredSize(new Dimension(300,30));
		
		
		p_north.add(t_port);
		p_north.add(t_port);
		p_north.add(bt);
		
		p_south.add(t_input);
		
		add(p_north,BorderLayout.NORTH);
		add(scroll);
		add(p_south, BorderLayout.SOUTH);
		
		setBounds(200,100,350,500);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		
	}

	public static void main(String[] args) {
		new Client();

	}

}
