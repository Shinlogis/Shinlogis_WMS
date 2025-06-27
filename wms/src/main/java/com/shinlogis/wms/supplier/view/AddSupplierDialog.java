package com.shinlogis.wms.supplier.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.common.Exception.SupplierException;
import com.shinlogis.wms.supplier.model.Supplier;
import com.shinlogis.wms.supplier.repository.SupplierDAO;

public class AddSupplierDialog extends JDialog{
	
	JPanel panel;
	JLabel la_name;
	JTextField t_name;
	JLabel la_address;
	JTextField t_address;
	JButton bt;
	
	SupplierDAO supplierDAO;
	SupplierModel supplierModel;
	
	public AddSupplierDialog(AppMain appMain, SupplierModel supplierModel) {
		super(appMain, "공급사 추가", true);
		
		this.supplierModel = supplierModel;
		
		this.setLocationRelativeTo(appMain);
		
		panel = new JPanel();
		la_name = new JLabel("공급사명");
		t_name = new JTextField();
		la_address = new JLabel("주소");
		t_address = new JTextField();
		bt = new JButton("추가");
		
		supplierDAO = new SupplierDAO();
		
		//스타일
		panel.setBackground(Color.WHITE);
		bt.setPreferredSize(new Dimension(80,60));
		
		
		//조립
		panel.add(createLine(la_name, t_name));
		panel.add(createLine(la_address, t_address));
		panel.add(bt);
		this.add(panel);

		bt.addActionListener(e->{
			insertSupplier();
		});
		
		this.setBounds(550,350,700,300);
		this.setVisible(true);
		//panel.updateUI();
		
	}
	
	
	//label, text 크기 조절
	public JPanel createLine(JLabel label, JTextField field) {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		label.setPreferredSize(new Dimension(150, 50));  // 라벨 고정 폭			
		field.setPreferredSize(new Dimension(500, 50)); // 필드 고정 폭
		panel.add(label);	
		panel.add(field);
		return panel;
	}
	
	
	//공급사 추가
	public void insertSupplier() {
		Supplier supplier = new Supplier();
		supplier.setName(t_name.getText());
		supplier.setAddress(t_address.getText());
		
		try {
			supplierDAO.insert(supplier);
			JOptionPane.showMessageDialog(this, "공급사 추가가 완료되었습니다.");
			this.dispose();
			supplierModel.tableChanged(); // 목록 새로고침 호출
		} catch (SupplierException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
	}
	
	
}
