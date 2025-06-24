package com.shinlogis.wms.outbound.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OutboundRegisterDialog extends JDialog {

	JPanel p_table;

	public OutboundRegisterDialog() {
		// 제목 서식.
		setTitle("출고 예정 등록");
		
		//본문서식
		p_table = new JPanel();
		p_table.setPreferredSize(new Dimension(700,700));
		p_table.setBackground(Color.CYAN);

		add(p_table, BorderLayout.SOUTH);
		setBounds(500, 200, 0, 0);
		setSize(new Dimension(700, 800));
		setVisible(true);
	}

}
