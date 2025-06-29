package com.shinlogis.wms.product.view;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.shinlogis.wms.product.model.ProductDTO;
import com.shinlogis.wms.product.repository.ProductDAO;

public class AddProductDialog extends JDialog {

	private JTextField tfProductCode;
	private JTextField tfProductName;
	private JComboBox<String> cbSupplierName;
	private JComboBox<String> cbStorageTypeName;
	private JTextField tfProductPrice;
	private JTextField tfProductImg;
	private JButton btnSave;
	private JButton btnCancel;

	private ProductDTO productDTO;

	public AddProductDialog(Frame owner, ProductDTO productDTO) {
		super(owner, "상품 추가", true);
		this.productDTO = productDTO;

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// 상품코드
		tfProductCode = new JTextField(productDTO.getProductCode());
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(new JLabel("상품코드"), gbc);
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		add(tfProductCode, gbc);
		gbc.gridwidth = 1;

		// 상품명
		tfProductName = new JTextField(productDTO.getProductName());
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(new JLabel("상품명"), gbc);
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		add(tfProductName, gbc);
		gbc.gridwidth = 1;

		// 공급사명
		String[] SupplierCodes = { "맛나제과", "푸드킹", "오렌지수산", "해피축산", "청과직송", "건강푸드", "농장직송", "웰빙식품", "수산왕", "바다찬물산" };
		cbSupplierName = new JComboBox<>(SupplierCodes);

		// 선택된 기본값 설정 (productDTO 기반)
		cbSupplierName.setSelectedItem(productDTO.getProductCode());

		// 레이아웃 위치 동일하게 설정
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(new JLabel("공급사명"), gbc);
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		add(cbSupplierName, gbc);
		gbc.gridwidth = 1;

		// 보관타입
		String[] StorageTypeNames = { "상온", "냉장", "냉동" };
		cbStorageTypeName = new JComboBox<>(StorageTypeNames);

		// 선택된 기본값 설정 (productDTO 기반)
		cbStorageTypeName.setSelectedItem(productDTO.getStorageTypeName());

		// 레이아웃 위치 동일하게 설정
		gbc.gridx = 0;
		gbc.gridy = 3;
		add(new JLabel("보관타입"), gbc);
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		add(cbStorageTypeName, gbc);
		gbc.gridwidth = 1;

		// 상품 가격
		tfProductPrice = new JTextField(String.valueOf(productDTO.getPrice()));
		gbc.gridx = 0;
		gbc.gridy = 4;
		add(new JLabel("상품가격"), gbc);
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		add(tfProductPrice, gbc);
		gbc.gridwidth = 1;

		// 상품 이미지
		tfProductImg = new JTextField(productDTO.getThumbnailPath());
		gbc.gridx = 0;
		gbc.gridy = 5;
		add(new JLabel("상품 이미지"), gbc);
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		add(tfProductImg, gbc);
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

		btnSave.addActionListener(e -> {
			try {
				String productCode = tfProductCode.getText().trim();
				String productName = tfProductName.getText().trim();
				String supplierName = (String) cbSupplierName.getSelectedItem();
				String storageTypeName = (String) cbStorageTypeName.getSelectedItem();
				int price = Integer.parseInt(tfProductPrice.getText().trim());
				String thumbnailPath = tfProductImg.getText().trim();

				ProductDAO dao = new ProductDAO();
				int supplierId = dao.getSupplierIdByName(supplierName);
				int storageTypeId = dao.getStorageTypeIdByName(storageTypeName);

				if (supplierId == 0 || storageTypeId == 0) {
					JOptionPane.showMessageDialog(this, "공급사명 또는 보관타입명이 잘못되었습니다.");
					return;
				}

				productDTO.setProductCode(productCode);
				productDTO.setProductName(productName);
				productDTO.setSupplierName(supplierName);
				productDTO.setSupplierId(supplierId);
				productDTO.setStorageTypeName(storageTypeName);
				productDTO.setStorageTypeId(storageTypeId);
				productDTO.setPrice(price);
				productDTO.setThumbnailPath(thumbnailPath);

				boolean success = dao.insertProduct(productDTO);

				if (success) {
					JOptionPane.showMessageDialog(this, "상품이 성공적으로 등록되었습니다.");
					dispose();
				} else {
					JOptionPane.showMessageDialog(this, "상품 등록에 실패했습니다.");
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "가격은 숫자로 입력해주세요.");
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "오류가 발생했습니다: " + ex.getMessage());
			}
		});
		pack();  // 컴포넌트 기준으로 크기 조절
		setLocationRelativeTo(null);  // 화면 중앙에 위치시키기
	
	}
	

}
