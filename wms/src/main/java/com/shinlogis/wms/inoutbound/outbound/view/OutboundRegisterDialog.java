package com.shinlogis.wms.inoutbound.outbound.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.inoutbound.model.IODetail;
import com.shinlogis.wms.inoutbound.model.IOReceipt;
import com.shinlogis.wms.inoutbound.outbound.model.Order;
import com.shinlogis.wms.inoutbound.outbound.repository.OutboundReceiptDAO;
import com.shinlogis.wms.location.model.Location;
import com.shinlogis.wms.location.repository.LocationDAO;
import com.toedter.calendar.JDateChooser;

public class OutboundRegisterDialog extends JDialog {
    JPanel p_title;
    JLabel la_title;

    JPanel p_form;
    JDateChooser ch_reservatedDate;
    JTextField tf_productName;
    JTextField tf_quantity;
    JComboBox<Location> cb_location;
    private List<IODetail> outboundDetailList = new ArrayList<>();
    AppMain appMain;
    Date ScheduledDate;
    JTable tb_products;
    DefaultTableModel model;

    JPanel p_bttn;
    JButton bt_register;
    JButton bt_close;

    public OutboundRegisterDialog(AppMain appMain) {
    	this.appMain= appMain;
        setLayout(new BorderLayout());

        // ===== 제목
        la_title = new JLabel("출고등록");
        la_title.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        la_title.setHorizontalAlignment(SwingConstants.CENTER);
        p_title = new JPanel(new BorderLayout());
        p_title.setPreferredSize(new Dimension(700, 50));
        p_title.setBackground(new Color(0xFF7F50));
        p_title.add(la_title);

        // ===== 입력 폼
        p_form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        ch_reservatedDate = new JDateChooser();
        ch_reservatedDate.setDateFormatString("yyyy-MM-dd");
        ch_reservatedDate.setPreferredSize(new Dimension(150, 25));
        cb_location = new JComboBox<>();
        tf_productName = new JTextField();
        tf_productName.setPreferredSize(new Dimension(150,25));
        tf_quantity = new JTextField();
        tf_quantity.setPreferredSize(new Dimension(150,25));
        gbc.insets = new Insets(5, 5, 5, 5); // 컴포넌트 간 간격
        gbc.fill = GridBagConstraints.HORIZONTAL; // 가로로 늘리기
        gbc.weightx = 0.3; // 라벨 너비 비율

        // 1행 - 출고예정일
        gbc.gridx = 0;
        gbc.gridy = 0;
        p_form.add(new JLabel("출고예정일"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        p_form.add(ch_reservatedDate, gbc);

        // 2행 - 주문지점
        gbc.gridx = 2;
        gbc.weightx = 0.3;
        p_form.add(new JLabel("주문지점"), gbc);

        gbc.gridx = 3;
        gbc.weightx = 0.7;
        p_form.add(cb_location, gbc);

     // y=1 위치를 빈 공간으로 유지하기 위해
        gbc.gridx = 0;
        gbc.gridy = 1; // 전체 칼럼 폭을 차지하게
        gbc.weighty = 0.1; // 조금 아래로 밀리게
        p_form.add(new JLabel(""), gbc);

        
        
        // 3행 - 상품명
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        p_form.add(new JLabel("상품명"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        p_form.add(tf_productName, gbc);

        // 4행 - 주문수량
        gbc.gridx = 2;
        gbc.weightx = 0.3;
        p_form.add(new JLabel("주문수량"), gbc);

        gbc.gridx = 3;
        gbc.weightx = 0.7;
        p_form.add(tf_quantity, gbc);

        
        loadLocations();

        // ===== 테이블

        // ===== 버튼
        p_bttn = new JPanel();
        bt_register = new JButton("등록");
        bt_close = new JButton("닫기");
        p_bttn.add(bt_register);
        p_bttn.add(bt_close);

        bt_register.addActionListener(e -> {
            try {
                // 1. 출고 전표 insert
                OutboundReceiptDAO receiptDAO = new OutboundReceiptDAO();
                IOReceipt receipt = new IOReceipt();
                IODetail detail = new IODetail();
                //선택한 날짜를 담아 줄 테이트 추적..
                ScheduledDate = new Date(ch_reservatedDate.getDate().getTime());
                int plannedQuantity = Integer.parseInt(tf_quantity.getText());
                // UI에서 입력받은 값들 세팅
                Location selectedLocation = (Location) cb_location.getSelectedItem();
                String locationName = selectedLocation.getLocationName();
                String productName = tf_productName.getText();

				int	ioReceiptId = receiptDAO.insertAllOutbounds(appMain, receipt, detail, ScheduledDate, plannedQuantity, locationName, productName);

                // 2. 출고 상세 insert
                

                System.out.println("출고 등록 완료!");
                this.dispose(); // 닫기

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        bt_close.addActionListener(e -> this.dispose());

        // ===== 전체 배치
        add(p_title, BorderLayout.NORTH);
        add(p_form, BorderLayout.CENTER);
        add(new JScrollPane(tb_products), BorderLayout.SOUTH);
        add(p_bttn, BorderLayout.PAGE_END);

        setTitle("출고 등록 하기");        
        setBounds(300, 200, 700, 500);
        setVisible(true);
    }

    //출고지점 이름을 선택하게끔 하기 위한 메서드
    private void loadLocations() {
        LocationDAO locationDAO = new LocationDAO();
        List<Location> locations = locationDAO.getLocation();
        for (Location loc : locations) {
            cb_location.addItem(loc);
        }
    }
}
