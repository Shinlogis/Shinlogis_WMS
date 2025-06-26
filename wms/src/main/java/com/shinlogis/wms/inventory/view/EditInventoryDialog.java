package com.shinlogis.wms.inventory.view;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.shinlogis.wms.inventory.model.InventoryDTO;
import com.shinlogis.wms.inventory.repository.InventoryDAO;

public class EditInventoryDialog extends JDialog {

	private JTextField tfProductCode;
	private JTextField tfProductName;
	private JTextField tfSupplierName;
	private JTextField tfWarehouseCode;
	private JTextField tfWarehouseName;
	private JTextField tfExpirationDate;
	private JTextField tfQuantity;
	private JButton btnSave;
	private JButton btnCancel;

	private InventoryDTO inventory;

	public EditInventoryDialog(Frame owner, InventoryDTO inventory) {
		super(owner, "재고 수정", true);
		this.inventory = inventory;

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// 창고코드
		tfWarehouseCode = new JTextField(inventory.getWarehouseCode());
		tfWarehouseCode.setEditable(false);
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(new JLabel("창고코드"), gbc);
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		add(tfWarehouseCode, gbc);
		gbc.gridwidth = 1;

		// 창고명
		tfWarehouseName = new JTextField(inventory.getWarehouseName());
		tfWarehouseName.setEditable(false);
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(new JLabel("창고명"), gbc);
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		add(tfWarehouseName, gbc);
		gbc.gridwidth = 1;

		// 상품코드
		tfProductCode = new JTextField(inventory.getProductCode());
		tfProductCode.setEditable(false);
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(new JLabel("상품코드"), gbc);
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		add(tfProductCode, gbc);
		gbc.gridwidth = 1;

		// 상품명
		tfProductName = new JTextField(inventory.getProductName());
		tfProductName.setEditable(false);
		gbc.gridx = 0;
		gbc.gridy = 3;
		add(new JLabel("상품명"), gbc);
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		add(tfProductName, gbc);
		gbc.gridwidth = 1;

		// 공급사명
		tfSupplierName = new JTextField(inventory.getSupplierName());
		tfSupplierName.setEditable(false);
		gbc.gridx = 0;
		gbc.gridy = 4;
		add(new JLabel("공급사명"), gbc);
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		add(tfSupplierName, gbc);
		gbc.gridwidth = 1;

		// 유통기한
		String expiryStr = inventory.getExpiryDate() != null ? inventory.getExpiryDate().toString() : "";
		tfExpirationDate = new JTextField(expiryStr);
		gbc.gridx = 0;
		gbc.gridy = 5;
		add(new JLabel("유통기한(YYYY-MM-DD)"), gbc);
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		add(tfExpirationDate, gbc);
		gbc.gridwidth = 1;

		// 수량
		tfQuantity = new JTextField(String.valueOf(inventory.getTotalQuantity()));
		gbc.gridx = 0;
		gbc.gridy = 6;
		add(new JLabel("총 수량"), gbc);
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		add(tfQuantity, gbc);
		gbc.gridwidth = 1;

		// 버튼
		btnSave = new JButton("저장");
		btnCancel = new JButton("취소");
		JPanel pButtons = new JPanel(new FlowLayout());
		pButtons.add(btnSave);
		pButtons.add(btnCancel);

		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.gridwidth = 3;
		add(pButtons, gbc);

		// 저장 버튼 리스너
		btnSave.addActionListener(e -> {
			try {
				String newExpiryText = tfExpirationDate.getText().trim();
				Date newExpiryDate = newExpiryText.isEmpty() ? null : Date.valueOf(newExpiryText);
				int newQuantity = Integer.parseInt(tfQuantity.getText().trim());

				// oldExpiryDate 따로 저장 필요 (만약 dto에 없으면 setter 추가)
				Date oldExpiryDate = inventory.getExpiryDate();

				boolean result = new InventoryDAO().updateMergedInventory(
					inventory.getWarehouseCode(),
					inventory.getProductCode(),
					oldExpiryDate,
					newExpiryDate,
					newQuantity
				);

				if (result) {
					JOptionPane.showMessageDialog(this, "수정 완료");
					dispose();
				} else {
					JOptionPane.showMessageDialog(this, "수정 실패");
				}
			} catch (IllegalArgumentException ex) {
				JOptionPane.showMessageDialog(this, "유통기한은 YYYY-MM-DD 형식이어야 합니다");
			}
		});

		btnCancel.addActionListener(e -> dispose());

		setSize(450, 400);
		setLocationRelativeTo(owner);
	}
}
