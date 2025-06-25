package com.shinlogis.wms.inbound.view;


import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.shinlogis.wms.inbound.repository.InboundDetailDAO;
import com.shinlogis.wms.inoutbound.model.IODetail;
import com.shinlogis.wms.product.model.Product;
import com.shinlogis.wms.product.repository.ProductDAO;
import com.shinlogis.wms.snapshot.repository.SnapshotDAO;

public class EditInboundDetailDialog extends JDialog {

    private JTextField tfProductCode;
    private JLabel laProductName;
    private JLabel laSupplierName;
    private JTextField tfQuantity;
    private JComboBox<String> cbStatus;
    private JButton btnSearch;
    private JButton btnSave;
    private JButton btnCancel;

    private IODetail ioDetail;
    private ProductDAO productDAO = new ProductDAO();
    private SnapshotDAO snapshotDAO = new SnapshotDAO();
    private InboundDetailDAO inboundDetailDAO = new InboundDetailDAO();
    
    private String beforeProductCode;
    private int beforeQuantity;
    private String beforeStatus;
    

    public EditInboundDetailDialog(Frame owner, IODetail detail, InboundDetailModel model) {
        super(owner, "입고상세 수정", true);
        
        
        this.ioDetail = detail;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 상품코드 필드와 검색 버튼
        tfProductCode = new JTextField(detail.getProductSnapshot().getProductCode(), 10); 
        btnSearch = new JButton("검색");
        btnSearch.addActionListener(e -> {
        	Product product = productDAO.selectByCode(tfProductCode.getText().trim());
        	if (product != null) {
        		laProductName.setText(product.getProductName());
        		laProductName.setForeground(Color.BLUE);
        		laSupplierName.setText(product.getSupplier().getName());
        		laSupplierName.setForeground(Color.BLUE);
        	} else {
        		JOptionPane.showMessageDialog(this, "존재하지 않는 상품입니다.");
        	}
        });
        
        laProductName = new JLabel(detail.getProductSnapshot().getProductName());

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("상품코드"), gbc);
        gbc.gridx = 1;
        add(tfProductCode, gbc);
        beforeProductCode = tfProductCode.getText(); // 기존 상품 코드 저장
        gbc.gridx = 2;
        add(btnSearch, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("상품명"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(laProductName, gbc);
        gbc.gridwidth = 1;

        // 공급사명
        laSupplierName = new JLabel(detail.getProductSnapshot().getSupplierName());
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("공급사명"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(laSupplierName, gbc);
        gbc.gridwidth = 1;

        // 수량
        tfQuantity = new JTextField(String.valueOf(detail.getPlannedQuantity()));
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("예정 수량"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(tfQuantity, gbc);
        beforeQuantity = Integer.parseInt(tfQuantity.getText()); // 기존 상품 수량 저장
        gbc.gridwidth = 1;

        // 상태
        cbStatus = new JComboBox<>(new String[]{"예정", "진행 중", "완료", "보류"});
        cbStatus.setSelectedItem(detail.getStatus());
        beforeStatus = detail.getStatus();
        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("상태"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(cbStatus, gbc);
        gbc.gridwidth = 1;

        // 버튼
        btnSave = new JButton("저장");
        btnCancel = new JButton("취소");
        JPanel pButtons = new JPanel(new FlowLayout());
        pButtons.add(btnSave);
        pButtons.add(btnCancel);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 3;
        add(pButtons, gbc);


        // 저장 버튼
        btnSave.addActionListener(e -> {
            String productCode = tfProductCode.getText().trim();
            String quantityStr = tfQuantity.getText().trim();
            String productName = laProductName.getText().trim();
            String supplierName = laSupplierName.getText().trim();

            // 검증 1: 상품코드 입력 여부
            if (productCode.isEmpty()) {
                JOptionPane.showMessageDialog(this, "상품코드를 입력해주세요.");
                return;
            }

            // 검증 2: 검색된 상품 정보가 표시되었는지
            if (productName.isEmpty() || supplierName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "상품 검색을 먼저 진행해주세요.");
                return;
            }

            // 검증 3: 수량 숫자 여부 및 음수 방지
            int quantity;
            try {
                quantity = Integer.parseInt(quantityStr);
                if (quantity < 0) {
                    JOptionPane.showMessageDialog(this, "수량은 0 이상이어야 합니다.");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "수량은 숫자만 입력해주세요.");
                return;
            }

            // 검증 4: 상태 선택 여부
            String status = (String) cbStatus.getSelectedItem();
            if (status == null || status.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "상태를 선택해주세요.");
                return;
            }

            // TODO: 저장 update
            // 상품코드가 변경되었을 경우
            if (!tfProductCode.getText().trim().equals(beforeProductCode)) {
            	// 스냅샷을 변경
            	snapshotDAO.updateSnapshotByCode(detail.getProductSnapshot().getSnapshotId(), tfProductCode.getText().trim());          	
            }
            // 상품수량이 변경되었을 경우
            if (Integer.parseInt(tfQuantity.getText().trim()) != beforeQuantity) {
                inboundDetailDAO.updatePlanQuantity(detail.getIoItemId(), Integer.parseInt(tfQuantity.getText().trim()));
            }
            // 상태가 변경되었을 경우
            if (!cbStatus.getSelectedItem().equals(beforeStatus)) {
            	inboundDetailDAO.updateStatus(detail.getIoItemId(), (String)cbStatus.getSelectedItem());
            }

            JOptionPane.showMessageDialog(this, "수정 완료");
            dispose();
            model.setData(Collections.emptyMap());
        });


        btnCancel.addActionListener(e -> dispose());

        setSize(450, 350);
        setLocationRelativeTo(owner);
        
    }
}
