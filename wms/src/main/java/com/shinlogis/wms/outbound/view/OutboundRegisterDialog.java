package com.shinlogis.wms.outbound.view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JPanel;

public class OutboundRegisterDialog extends JDialog{
	JPanel p_title;
	public OutboundRegisterDialog() {
		p_title = new JPanel();
		p_title.setPreferredSize(new Dimension(700,800));
		p_title.setBackground(Color.PINK);
		
		
		System.out.println("등록다이얼로그가 생성 되었습니다.");
		add(p_title);
		setSize(new Dimension(700,800));
		setVisible(true);
	}
	
}
