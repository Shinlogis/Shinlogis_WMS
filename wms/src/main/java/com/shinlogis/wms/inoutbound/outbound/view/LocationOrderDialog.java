package com.shinlogis.wms.inoutbound.outbound.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

public class LocationOrderDialog extends JDialog{
	JPanel p_title;
	JPanel p_table;
	JTable tb_regist;
	JLabel la_title;
	
	public LocationOrderDialog() {
		// 제목 서식.
		setTitle("지점 주문 조회");		
		//본문서식
		la_title = new JLabel("지점 주문 조회");
		p_title = new JPanel();
		p_title.add(la_title);
		p_title.setPreferredSize(new Dimension(700,100));
		p_title.setBackground(new Color(0xFF7F50));
		
		p_table = new JPanel();
		p_table.setPreferredSize(new Dimension(700,700));
		p_table.setBackground(Color.CYAN);

		add(p_title);
		add(p_table, BorderLayout.SOUTH);
		setBounds(500, 200, 0, 0);
		setSize(new Dimension(700, 800));
		setVisible(true);
	}

}
