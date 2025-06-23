package com.shinlogis.wms.Member.view;

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
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.shinlogis.wms.common.config.Config;

public class FindPwd extends JFrame{

JPanel p_center;
	
	JLabel la_findPWD;
	JLabel la_id;
	JTextField t_id;
	JLabel la_email;
	JLabel la_at;
	JTextField t_email;
	JComboBox box;
	
	JButton bt;
	
	 public FindPwd() {
		 p_center = new JPanel();
		 la_findPWD = new JLabel("비밀번호 찾기");
		 la_id = new JLabel("아이디");
		 t_id = new JTextField();
		 la_email = new JLabel("이메일");
		 la_at = new JLabel("@");
		 t_email = new JTextField();
		 box = new JComboBox();
		 
		getContentPane().setBackground(Color.WHITE);
		this.setLayout(new java.awt.GridBagLayout());
		p_center = new JPanel(new FlowLayout());
		p_center.setLayout(new BoxLayout(p_center, BoxLayout.Y_AXIS));
		p_center.setPreferredSize(new Dimension(500,300));
		p_center.setBackground(Color.WHITE);
		p_center.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2)); 
		
		//스타일 
		la_findPWD.setFont(new Font("맑은고딕", Font.BOLD, 24));
		la_findPWD.setHorizontalAlignment(JLabel.CENTER);
		
		box.addItem("naver.com");
		box.addItem("daum.com");
		box.addItem("gmail.com");
			
		
		//조립
		p_center.add(createCenterLine(la_findPWD));
		p_center.add(createLine(la_id, t_id));
		p_center.add(createEmailLine(la_email, t_email, la_at, box));
		add(p_center);
		 
		 
		setBounds(200, 100, Config.ADMINMAIN_WIDTH, Config.ADMINMAIN_HEIGHT);
		setVisible(true);

	 }
	 
		//아이디
		public JPanel createLine(JLabel label, JTextField field) {
			JPanel panel = new JPanel();
			panel.setOpaque(false);
			panel.setPreferredSize(new Dimension(400, 10));  //  줄 높이 조절
			label.setPreferredSize(new Dimension(80, 30));  // 라벨 고정 폭
			field.setPreferredSize(new Dimension(180, 30)); // 필드 고정 폭
			panel.add(label);
			panel.add(field);
			return panel;
		}
	 
	 
		//이메일
		public JPanel createEmailLine(JLabel label, JTextField field,JLabel at, JComboBox box) {
			JPanel panel = new JPanel();
			panel.setOpaque(false);
			panel.setPreferredSize(new Dimension(400, 10));  //  줄 높이 조절
			label.setPreferredSize(new Dimension(50, 30));
			field.setPreferredSize(new Dimension(180, 30));
			at.setPreferredSize(new Dimension(20, 30));
			box.setPreferredSize(new Dimension(120, 30));
			panel.add(label);
			panel.add(field);
			panel.add(box);
			return panel;
		}

		// 가운데 정렬용 라벨 생성 메서드
		public JPanel createCenterLine(JComponent comp) {
			JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 1));
			panel.setPreferredSize(new Dimension(400, 40)); // 너비와 높이 확보
			panel.setOpaque(false);
			comp.setPreferredSize(new Dimension(200, 40));  // 중앙에 적절한 너비 설정
			
			panel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0)); //위쪽 여백
			
			panel.add(comp);
			return panel;
		}
		
		public static void main(String[] args) {
			new FindPwd();
		}
		
}
