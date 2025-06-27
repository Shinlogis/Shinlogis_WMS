package com.shinlogis.wms.supplier.view;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

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

public class EditSupplierDialog extends JDialog {

	JPanel panel;
	JLabel la_name;
	JTextField t_name;
	JLabel la_address;
	JTextField t_address;
	JButton bt;

	SupplierDAO supplierDAO;
	SupplierModel supplierModel;
	Supplier supplier;

	public EditSupplierDialog(AppMain appMain,Supplier supplier, SupplierModel supplierModel) {
		super(appMain, "공급사 추가", true);

		this.supplierModel = supplierModel;
		this.supplier = supplier;

		this.setLocationRelativeTo(appMain);
		
		supplierDAO = new SupplierDAO();

		panel = new JPanel();
		la_name = new JLabel("공급사명");
		t_name = new JTextField(supplier.getName());
		la_address = new JLabel("주소");
		t_address = new JTextField(supplier.getAddress());
		bt = new JButton("추가");


		// 스타일
		panel.setBackground(Color.WHITE);
		bt.setPreferredSize(new Dimension(80, 60));

		// 조립
		panel.add(createLine(la_name, t_name));
		panel.add(createLine(la_address, t_address));
		panel.add(bt);
		this.add(panel);

		bt.addActionListener(e -> {
			editSupplier();
		});

		this.setBounds(550, 350, 700, 300);
		this.setVisible(true);
	}

	// label, text 크기 조절
	public JPanel createLine(JLabel label, JTextField field) {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		label.setPreferredSize(new Dimension(150, 50)); // 라벨 고정 폭
		field.setPreferredSize(new Dimension(500, 50)); // 필드 고정 폭
		panel.add(label);
		panel.add(field);
		return panel;
	}
	

	
	//버튼 누르면 편집
	public void editSupplier() {
		
		supplier.setName(t_name.getText());
		supplier.setAddress(t_address.getText());
		
		try {
			supplierDAO.editSupplier(supplier);
			JOptionPane.showMessageDialog(this, "편집이 완료되었습니다.");
			this.dispose();
			supplierModel.tableChanged(); // 목록 새로고침 호출
			
		} catch (SupplierException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
		
		
		
	}
	
	

}
