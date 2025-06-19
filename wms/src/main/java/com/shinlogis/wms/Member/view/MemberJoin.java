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

public class MemberJoin extends JFrame{
	
	JPanel p_center;
	
	JLabel la_join;
	JLabel la_id;
	JButton bt_idCheck;
	JLabel la_pwd;
	JLabel la_pwdCheck;
	JLabel la_email;
	JLabel la_at;

	JTextField t_id;
	JTextField t_pwd;
	JTextField t_pwdCheck;
	JTextField t_email;
	JComboBox cb_email;
	
	JButton bt_join;
	
	public MemberJoin() {
		
		p_center = new JPanel();
		
		this.setLayout(new java.awt.GridBagLayout());
		p_center.setLayout(new BoxLayout(p_center, BoxLayout.Y_AXIS));
		p_center.setPreferredSize(new Dimension(500,500));
		p_center.setBackground(Color.WHITE);
		p_center.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 5)); 
		
		la_join = new JLabel("회원가입");
		la_id = new JLabel("아이디");
		bt_idCheck = new JButton("확인");
		la_pwd = new JLabel("비밀번호");
		la_pwdCheck = new JLabel("비밀번호 확인");
		la_email = new JLabel("이메일");
		la_at = new JLabel("@");
		
		t_id = new JTextField();
		t_pwd = new JTextField();
		t_pwdCheck = new JTextField();
		t_email = new JTextField();
		cb_email = new JComboBox();
		
		bt_join = new JButton("회원가입");
		
		//스타일
		cb_email.addItem("naver.com");
		cb_email.addItem("dauem.com");
		cb_email.addItem("google.com");
		la_join.setHorizontalAlignment(JLabel.CENTER);
		la_join.setFont(new Font("맑은고딕", Font.BOLD, 24));
		

		
		
		//조립
		p_center.add(createCenterLine(la_join));
		p_center.add(createIdLine(la_id, t_id, bt_idCheck));
		p_center.add(createLine(la_pwd, t_pwd));
		p_center.add(createLine(la_pwdCheck, t_pwdCheck));
		p_center.add(createEmailLine(la_email, t_email, la_at, cb_email));
		p_center.add(createCenterLine(bt_join));
		
		setBounds(200, 100, Config.ADMINMAIN_WIDTH, Config.ADMINMAIN_HEIGHT);
		add(p_center);
		setVisible(true);
	}
	
	//아이디, 비밀번호
	public JPanel createLine(JLabel label, JTextField field) {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		label.setPreferredSize(new Dimension(80, 30));  // 라벨 고정 폭
		field.setPreferredSize(new Dimension(180, 30)); // 필드 고정 폭
		panel.add(label);
		panel.add(field);
		return panel;
	}
	
	//아이디, 비밀번호 확인
	public JPanel createIdLine(JLabel label, JTextField field, JButton bt) {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		label.setPreferredSize(new Dimension(50, 30));
		field.setPreferredSize(new Dimension(180, 30));
		bt.setPreferredSize(new Dimension(80, 30));
		panel.add(label);
		panel.add(field);
		panel.add(bt);
		return panel;
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
		panel.add(box);
		return panel;
	}
	
	// 가운데 정렬용 라벨 또는 버튼용 패널 생성 메서드
	public JPanel createCenterLine(JComponent comp) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 1));
		panel.setPreferredSize(new Dimension(400, 40)); // 높이 약간 더 확보
		panel.setOpaque(false);
		comp.setPreferredSize(new Dimension(100, 30));  // 중앙에 적절한 너비 설정
		panel.add(comp);
		return panel;
	}
	
	public static void main(String[] args) {
		new MemberJoin();
	}


}