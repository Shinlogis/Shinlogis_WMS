package com.shinlogis.wms.Member.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JLabel;

public class FindIdDialog extends JDialog{
	
	FindId findId;
	JLabel la_id;
	String id;
	
	public FindIdDialog(FindId findId, String id) {
		super(findId, "아이디 찾기", true);
		
		this.findId = findId;
		this.id = id;
		
		
		setLocationRelativeTo(findId); //누구를 대상으로 이 창을 띄울 건지
		setSize(300,100);
		getContentPane().setBackground(Color.white);

		
		setLayout(new FlowLayout());
		
		if(id != null) {
			la_id = new JLabel(id);
			la_id.setFont(new Font("Verdana", Font.BOLD, 24));
			
		} else {
			la_id = new JLabel("조회된 아이디가 없습니다.");
		}
		
		add(la_id);
		
		
		setLocationRelativeTo(null); //화면 중앙에 위치 시키기
		setVisible(true);
		
		
	}
}
