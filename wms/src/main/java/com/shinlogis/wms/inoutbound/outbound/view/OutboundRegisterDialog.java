package com.shinlogis.wms.inoutbound.outbound.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

public class OutboundRegisterDialog extends JDialog {
	JPanel p_title;
	JTable tb_regist;
	JLabel la_title;
	
	JPanel p_content;
	
	JPanel p_bttn;
	JButton bt_close;
	JButton bt_register;
	
	public OutboundRegisterDialog() {
		setLayout(new FlowLayout());
		// 제목 서식.
		la_title = new JLabel("출고등록");
		la_title.setFont(new Font("맑은 고딕", Font.PLAIN, 20)); //제목양식
		p_title = new JPanel();
		p_title.add(la_title);
		p_title.setPreferredSize(new Dimension(700,100));
		p_title.setBackground(new Color(0xFF7F50));
		
		//본문서식
		p_content = new JPanel();
		p_content.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH; // 셀을 가득 채움
		gbc.insets = new Insets(5, 5, 5, 5); // 셀 간 여백

		for (int y = 0; y < 4; y++) { // 세로 4줄
		    for (int x = 0; x < 5; x++) { // 가로 5칸
		        gbc.gridx = x; // x 위치 (열)
		        gbc.gridy = y; // y 위치 (행)
		        JLabel label = new JLabel("[" + y + "," + x + "]");
		        p_content.add(label, gbc);
		    }
		}
		
//==================버튼생성
		
		// 버튼 패널 생성
		p_bttn = new JPanel();
		bt_register = new JButton("등록");
		bt_close = new JButton("닫기");
		p_bttn.setPreferredSize(new Dimension(700, 80));
		p_bttn.setBackground(new Color(0xF5F5F5)); // 연한 회색 배경 (원하는 색으로 변경 가능)

		// 버튼들을 버튼 패널에 추가
		p_bttn.add(bt_register);
		p_bttn.add(bt_close);

		// 버튼 패널을 다이얼로그에 추가
		add(p_bttn);
		
		add(p_title);
		add(p_content);
		add(p_bttn);
		setTitle("출고 등록 하기");		
		setBounds(300, 200, 0, 0);
		setSize(new Dimension(700, 800));
		setVisible(true);
	}

}
