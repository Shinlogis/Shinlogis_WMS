package com.shinlogis.wms.inbound.view;

import com.shinlogis.wms.inbound.model.IODetail;
import com.shinlogis.wms.product.model.Product;
import com.shinlogis.wms.snapshot.model.Snapshot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.HashMap;

public class EditInboundDetailDialog extends JDialog {

    private JTextField tfProductCode;
    private JLabel laProductName;
    private JTextField tfSupplierName;
    private JTextField tfQuantity;
    private JComboBox<String> cbStatus;
    private JButton btnSearch;
    private JButton btnSave;
    private JButton btnCancel;

    private IODetail ioDetail;

    public EditInboundDetailDialog(Frame owner, IODetail detail) {
        super(owner, "입고상세 수정", true);
        this.ioDetail = detail;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 상품코드 필드와 검색 버튼
        tfProductCode = new JTextField(detail.getProductSnapshot().getProductCode());
        btnSearch = new JButton("검색");
        laProductName = new JLabel(detail.getProductSnapshot().getProductName());

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("상품코드"), gbc);
        gbc.gridx = 1;
        add(tfProductCode, gbc);
        gbc.gridx = 2;
        add(btnSearch, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("상품명"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(laProductName, gbc);
        gbc.gridwidth = 1;

        // 공급사명
        tfSupplierName = new JTextField(detail.getProductSnapshot().getSupplierName());
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("공급사명"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(tfSupplierName, gbc);
        gbc.gridwidth = 1;

        // 수량
        tfQuantity = new JTextField(String.valueOf(detail.getPlannedQuantity()));
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("예정 수량"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(tfQuantity, gbc);
        gbc.gridwidth = 1;

        // 상태
        cbStatus = new JComboBox<>(new String[]{"예정", "진행 중", "완료", "보류"});
        cbStatus.setSelectedItem(detail.getStatus());
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

        // 검색 버튼 동작
        btnSearch.addActionListener(e -> {
//            String code = tfProductCode.getText().trim();
//            Product product = ProductDAO.findByCode(code);
//            if (product != null) {
//                laProductName.setText(product.getProductName());
//                tfSupplierName.setText(product.getSupplierName());
//            } else {
//                laProductName.setText("상품 없음");
//                tfSupplierName.setText("공급사 없음");
//            }
        });


        // 저장 버튼
        btnSave.addActionListener(e -> {
            try {
                int quantity = Integer.parseInt(tfQuantity.getText());
                ioDetail.setPlannedQuantity(quantity);
                ioDetail.getProductSnapshot().setProductCode(tfProductCode.getText());
                ioDetail.getProductSnapshot().setProductName(laProductName.getText());
                ioDetail.getProductSnapshot().setSupplierName(tfSupplierName.getText());
                ioDetail.setStatus((String) cbStatus.getSelectedItem());

                JOptionPane.showMessageDialog(this, "수정 완료");
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "수량은 숫자만 입력해주세요");
            }
        });

        btnCancel.addActionListener(e -> dispose());

        setSize(450, 350);
        setLocationRelativeTo(owner);
        setVisible(true);
    }
}
