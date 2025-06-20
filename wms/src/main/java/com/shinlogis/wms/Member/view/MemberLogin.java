package com.shinlogis.wms.Member.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.common.config.Config;
import com.shinlogis.wms.headquarters.model.HeadquartersUser;
import com.shinlogis.wms.location.model.LocationUser;

public class MemberLogin extends JFrame{
	


	JPanel p_center;
	
	JLabel la_login;
	
	
	JTextField t_id;
	JTextField t_pwd;
	
	JButton bt_admin;
	JButton bt_location;
	HeadquartersUser user=new HeadquartersUser();
	LocationUser locationUser=new LocationUser(); //나중에 new 지우고 로그인 성공 시 new 하
	
	public MemberLogin() {
		
		p_center = new JPanel(new FlowLayout());
		p_center.setBackground(Color.WHITE);
		p_center.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 5)); 
		
		la_login = new JLabel("로그인");
		
		t_id = new JTextField();
		t_pwd = new JTextField();
	
		bt_admin = new JButton("관리자");
		bt_location = new JButton("지점");
		
		
		
		la_login.setHorizontalAlignment(JLabel.CENTER);
		la_login.setFont(new Font("맑은고딕", Font.BOLD, 24));
		
		Dimension d = new Dimension(200, 30);
		t_id.setPreferredSize(d);
		t_pwd.setPreferredSize(d);
		
		
		
		//제조
		p_center.add(la_login);
		p_center.add(t_id);
		p_center.add(t_pwd);
		p_center.add(bt_admin);
		p_center.add(bt_location);
		
		

		//이벤트 
		bt_admin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				AppMain appMain=new AppMain();
				appMain.headquartersUser=user;
				appMain.initUI();
			}
		});
		
		
		bt_location.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				AppMain appMain=new AppMain();
				appMain.locationUser=locationUser;
				appMain.initUI();
			}
		});
		add(p_center);
		setBounds(200, 100, Config.ADMINMAIN_WIDTH, Config.ADMINMAIN_HEIGHT);
		setVisible(true);
	
	}
	
	public static void main(String[] args) {
		new MemberLogin();
	}
	

}
