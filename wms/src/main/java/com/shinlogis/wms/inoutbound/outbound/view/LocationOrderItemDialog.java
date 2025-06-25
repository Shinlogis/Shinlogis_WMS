package com.shinlogis.wms.inoutbound.outbound.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class LocationOrderItemDialog extends JDialog{
	JPanel p_title;
	JTable tb_regist;
	JLabel la_title;
	JTable tb_table;
	JScrollPane sc_table;
	AbstractTableModel model;
	JButton bt_close;
	
	//임시버튼임.**********
	JButton bt_tempo;
	
	public LocationOrderItemDialog() {
		setLayout(new FlowLayout());
		// 제목 서식.
		la_title = new JLabel("지점 주문 상세 조회");
		p_title = new JPanel();
		p_title.add(la_title);
		p_title.setPreferredSize(new Dimension(700,100));
		p_title.setBackground(new Color(0xFF7F50));
		
		
		//본문서식
		model = new LocationOrderItemModel();
		tb_table= new JTable(model);
		tb_table.setRowHeight(45);
		sc_table = new JScrollPane(tb_table);
		sc_table.setPreferredSize(new Dimension(700,600));

		//확인버튼
		bt_close = new JButton("확인 완료");
		bt_close.addActionListener(e->{
			this.dispose();
		});
		bt_close.setPreferredSize(new Dimension(100, 40));
		
		
		add(p_title);
		add(sc_table);
		add(bt_close);
		setTitle("지점 주문 상세 조회");		
		setBounds(1000, 200, 0, 0);
		setSize(new Dimension(700, 800));
		setVisible(true);
	}
}
