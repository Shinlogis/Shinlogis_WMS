package com.shinlogis.wms.inoutbound.outbound.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.shinlogis.locationuser.order.model.StoreOrderModel;

public class OrderDialog extends JDialog{
	JPanel p_title;
	JTable tb_regist;
	JLabel la_title;
	JTable tb_table;
	AbstractTableModel model;
	JButton bt_close;
	
//임시	
	JPanel p_tempo;
	JLabel la_tempo;
	public OrderDialog() {
		setLayout(new FlowLayout());
		// 제목 서식.
		la_title = new JLabel("출고 예정 등록");
		p_title = new JPanel();
		p_title.add(la_title);
		p_title.setPreferredSize(new Dimension(700,100));
		p_title.setBackground(new Color(0xFF7F50));
		
		//본문서식
		//임시로 구현해놓은 것 tempo는 나중에 다 지우자.
		p_tempo = new JPanel();
		la_tempo = new JLabel("출고등록 관련 기능이 구현될 예정입니다.(미구현)"); 
		p_tempo.add(la_tempo);
		p_tempo.setPreferredSize(new Dimension(700, 100));
		p_tempo.setBackground(Color.LIGHT_GRAY);
		//확인버튼
		bt_close = new JButton("확인 완료");
		bt_close.addActionListener(e->{
			this.dispose();
		});
		bt_close.setPreferredSize(new Dimension(100, 40));
		
		
		add(p_title);
		add(p_tempo);
		add(bt_close);
		setTitle("출고 예정 등록");		
		setBounds(500, 200, 0, 0);
		setSize(new Dimension(700, 800));
		setVisible(true);
	}
	
}
