package com.shinlogis.wms.chat.client.head;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import com.google.gson.Gson;
import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.location.model.Location;

public class HeaChat_backup extends JFrame {

	JPanel p_north;
	JLabel la_name;

	
	JTextArea area;
	JScrollPane scroll;
	JTextField t_input;

	JPanel p_south;
	AppMain appMain;

	Location location;
	public HeadClientThread headClientThread;

	public HeaChat_backup(Location location) {
		this.location = location;

		p_north = new JPanel();
		la_name = new JLabel(location.getLocationName());

		
		area = new JTextArea();
		scroll = new JScrollPane(area);
		t_input = new JTextField();

		p_south = new JPanel();

		scroll.setPreferredSize(new Dimension(300, 400));
		area.setBackground(Color.PINK);

		t_input.setPreferredSize(new Dimension(300, 30));

		p_north.add(la_name);
		p_south.add(t_input);

		add(p_north, BorderLayout.NORTH);
		add(scroll);
		add(p_south, BorderLayout.SOUTH);

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

					Gson gson = new Gson();
					String result = gson.toJson(message);

					headClientThread.send(result);
				}
			}
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Message message = new Message();
				// 메세지 만들기
				message.setRequestType("eixt");
				message.setMe("head");

				Gson gson = new Gson();
				String result = gson.toJson(message);

				headClientThread.send(result);
			}
		});

		setBounds(600, 200, 400, 500);
		setVisible(true);

	}

}
