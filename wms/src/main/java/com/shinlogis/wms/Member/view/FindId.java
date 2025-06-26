package com.shinlogis.wms.Member.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.shinlogis.wms.common.config.Config;
import com.shinlogis.wms.headquarters.model.HeadquartersUser;
import com.shinlogis.wms.headquarters.repository.HeadquartersDAO;
import com.shinlogis.wms.location.repository.LocationUserDAO;

public class FindId extends JFrame{
	
	JPanel p_center;
	
	JLabel la_findID; 
	JLabel la_email;
	JLabel la_at;
	JTextField t_email;
	JComboBox cb_email;
	
	JPanel p_north;
	JButton bt;
	
	HeadquartersDAO headquartersDAO;
	LocationUserDAO locationUserDAO;
	
	 public FindId() {
		 p_center = new JPanel();
		 la_findID = new JLabel("아이디 찾기");
		 la_email = new JLabel("이메일");
		 la_at = new JLabel("@");
		 t_email = new JTextField();
		 cb_email = new JComboBox();
		 bt = new JButton("찾기");
		 p_north = new JPanel(new FlowLayout());
		 
		 headquartersDAO = new HeadquartersDAO();
		 locationUserDAO = new LocationUserDAO();
		 
		getContentPane().setBackground(Color.WHITE);
		this.setLayout(new java.awt.GridBagLayout());
		p_center = new JPanel(new FlowLayout());
		p_center.setLayout(new BoxLayout(p_center, BoxLayout.Y_AXIS));
		p_center.setPreferredSize(new Dimension(500,300));
		p_center.setBackground(Color.WHITE);
		p_center.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2)); 
		
		//스타일 
		la_findID.setFont(new Font("맑은고딕", Font.BOLD, 24));
		la_findID.setHorizontalAlignment(JLabel.CENTER);
		
		cb_email.addItem("naver.com");
		cb_email.addItem("daum.com");
		cb_email.addItem("gmail.com");
			
		
		//조립
		p_center.add(createCenterLine(la_findID));
		p_center.add(createEmailLine(la_email, t_email, la_at, cb_email));
		//버튼
		p_north.setOpaque(false);
		p_north.add(bt);
		p_center.add(p_north);
		
		add(p_center);
		
		 
		//이벤트
		bt.addActionListener(e->{
			findIdByEmailText();
		});
		 
		setBounds(200, 100, Config.ADMINMAIN_WIDTH, Config.ADMINMAIN_HEIGHT);
		setVisible(true);

	 }
	 
	 
		//이메일
		public JPanel createEmailLine(JLabel label, JTextField field,JLabel at, JComboBox box) {
			JPanel panel = new JPanel();
			panel.setOpaque(false);
			label.setPreferredSize(new Dimension(50, 30));
			field.setPreferredSize(new Dimension(180, 30));
			at.setPreferredSize(new Dimension(20, 30));
			box.setPreferredSize(new Dimension(120, 30));
			panel.add(label);
			panel.add(field);
			panel.add(at);
			panel.add(box);
			return panel;
		}

		// 가운데 정렬용 라벨 생성 메서드
		public JPanel createCenterLine(JComponent comp) {
			JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 1));
			panel.setPreferredSize(new Dimension(400, 40)); // 너비와 높이 확보
			panel.setOpaque(false);
			comp.setPreferredSize(new Dimension(200, 40));  // 중앙에 적절한 너비 설정
			
			panel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0)); //위쪽 여백
			
			panel.add(comp);
			return panel;
		}
		
		
		//이메일 통해 아이디 찾기
		public void findIdByEmailText() {
			
			String headquartersId = headquartersDAO.findIdByEmail(t_email.getText() + "@" + (String)cb_email.getSelectedItem());
			String locationUsersId = locationUserDAO.findIdByEmail(t_email.getText() + "@" + (String)cb_email.getSelectedItem());
			
			if(headquartersId != null) {
				new FindIdDialog(this, headquartersId);
				
			}else if(locationUsersId != null) {
				new FindIdDialog(this, locationUsersId);
			} else {
				JOptionPane.showMessageDialog(this, "해당 이메일로 등록된 아이디가 없습니다.");
			}
			
		}

	
}
