package com.shinlogis.wms.inoutbound.inbound.view.detail;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.shinlogis.wms.common.util.NumericTextFieldUtil;
import com.shinlogis.wms.damagedCode.repository.DamagedCodeDAO;
import com.shinlogis.wms.inoutbound.inbound.repository.DetailDAO;
import com.shinlogis.wms.inoutbound.model.IODetail;
import com.shinlogis.wms.product.model.Product;
import com.shinlogis.wms.product.repository.ProductDAO;
import com.shinlogis.wms.snapshot.model.Snapshot;
import com.shinlogis.wms.snapshot.repository.SnapshotDAO;
import com.shinlogis.wms.warehouse.model.Warehouse;
import com.shinlogis.wms.warehouse.repository.WarehouseDAO;
import com.toedter.calendar.JDateChooser;

public class CheckDetailDialog extends JDialog {
	private DamagedCodeDAO damagedCodeDAO = new DamagedCodeDAO();
	private WarehouseDAO warehouseDAO = new WarehouseDAO();
	private DetailDAO detailDAO = new DetailDAO();

	private JLabel laIOReceipt;
	private JLabel laIODetail;
	private JLabel laProductCode;
	private JLabel laProductName;
	private JLabel laProductType;
	private JLabel laSupplierName;
	private JLabel laPlannedQuantity;
	private JComboBox<String> cbDamagedTypeCode;
	private JTextField tfDamagedQuantity;
	private JTextField tfWarehouseCode;
	private JLabel laWarehouseName;
	private JLabel laWarehouseType;

	private JDateChooser chooser; // 유통기한 선택 달력

	private JButton btnSearch;
	private JButton btnSave;
	private JButton btnCancel;

	private Warehouse warehouse; // 창고 검색 결과

	public CheckDetailDialog(Frame owner, IODetail detail, DetailModel model) {
		super(owner, "입고 검수", true);
		Snapshot snapshot = detail.getProductSnapshot();

		laIOReceipt = new JLabel(String.valueOf(detail.getIoReceipt().getIoReceiptId()));
		laIODetail = new JLabel(String.valueOf(detail.getIoDetailId()));
		laProductCode = new JLabel(detail.getProductSnapshot().getProductCode());
		laProductName = new JLabel(detail.getProductSnapshot().getProductName());
		laProductType = new JLabel(detail.getProductSnapshot().getStorageType().getTypeName());
		laSupplierName = new JLabel(detail.getProductSnapshot().getSupplierName());
		laPlannedQuantity = new JLabel(String.valueOf(detail.getPlannedQuantity()));
		tfDamagedQuantity = new JTextField(10); 
		
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = 0;
		gbc.gridy = 0;
		add(new JLabel("입고예정ID"), gbc);
		gbc.gridx = 1;
		add(laIOReceipt, gbc);

		gbc.gridy = 1;
		gbc.gridx = 0;
		add(new JLabel("입고상세ID"), gbc);
		gbc.gridx = 1;
		add(laIODetail, gbc);

		gbc.gridy = 2;
		gbc.gridx = 0;
		add(new JLabel("상품"), gbc);
		gbc.gridx = 1;
		add(laProductCode, gbc);
		gbc.gridx = 2;
		add(laProductName, gbc);
		gbc.gridx = 3;
		add(laProductType, gbc);

		gbc.gridy = 3;
		gbc.gridx = 0;
		add(new JLabel("공급사"), gbc);
		gbc.gridx = 1;
		add(laSupplierName, gbc);

		gbc.gridy = 4;
		gbc.gridx = 0;
		add(new JLabel("입고예정 수량"), gbc);
		gbc.gridx = 1;
		add(laPlannedQuantity, gbc);

		gbc.gridy = 5;
		gbc.gridx = 0;
		add(new JLabel("파손코드"), gbc);
		gbc.gridx = 1;
		cbDamagedTypeCode = new JComboBox<>();
		// db 내 파손코드 종류 검색
		List<String> names = damagedCodeDAO.selectAllNames();
		for (String name : names) {
			// db 내 파손코드 이름으로 콤보박스의 아이템을 추가
			cbDamagedTypeCode.addItem(name);
		}
		add(cbDamagedTypeCode, gbc);
		
		gbc.gridx = 2;
		add(new JLabel("파손수량"), gbc);
		gbc.gridx = 3;
		add(tfDamagedQuantity, gbc);
		NumericTextFieldUtil.applyNumericFilter(tfDamagedQuantity); // 숫자만 입력 가능한 텍스트필드로 설정
		// "정상"을 선택하면 파손수량 컬럼은 0으로 수정 불가하게 고정
		cbDamagedTypeCode.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				String selected = (String) cbDamagedTypeCode.getSelectedItem();
				if ("정상".equals(selected)) {
					tfDamagedQuantity.setEnabled(false);
					tfDamagedQuantity.setText("0"); // 선택 시 값도 0으로 초기화 (선택)
				} else {
					tfDamagedQuantity.setEnabled(true);
				}
			}
		});
		// 파손코드의 초기값이 "정상"이므로, 파손수량 컬럼의 초기값을 0으로 수정 불가하게 고정
		String initSelected = (String) cbDamagedTypeCode.getSelectedItem();
		if ("정상".equals(initSelected)) {
		    tfDamagedQuantity.setEnabled(false);
		    tfDamagedQuantity.setText("0");
		} else {
		    tfDamagedQuantity.setEnabled(true);
		}

		// 저장창고 필드와 검색 버튼
		gbc.gridy = 6;
		gbc.gridx = 0;
		add(new JLabel("저장창고"), gbc);
		tfWarehouseCode = new JTextField("", 10);
		gbc.gridx = 1;
		add(tfWarehouseCode, gbc);
		btnSearch = new JButton("검색");
		gbc.gridx = 2;
		add(btnSearch, gbc);
		btnSearch.addActionListener(e -> {
			// 검색한 저장창고
			List<Warehouse> result = warehouseDAO.selectByCode(tfWarehouseCode.getText().trim());
			if (!result.isEmpty()) {
				// 상품과 저장창고의 보관타입이 동일한지 확인
				warehouse = warehouseDAO.selectByCode(tfWarehouseCode.getText().trim().toString()).get(0);
				if (snapshot.isStorageTypeMatched(snapshot, warehouse)) {
					JOptionPane.showMessageDialog(this, "사용 가능한 창고입니다.");
					laWarehouseName.setText(warehouse.getWarehouseName());
					laWarehouseType.setText(warehouse.getStorageType().getTypeName());
				} else {
					JOptionPane.showMessageDialog(this, snapshot.getStorageType().getTypeName()+" 상품은 "+warehouse.getStorageType().getTypeName()+" 창고에 보관할 수 없습니다.");
				}
			} else {
				JOptionPane.showMessageDialog(this, "존재하지 않는 창고입니다.");
			}
		});

		gbc.gridy = 7;
		gbc.gridx = 1;
		laWarehouseName = new JLabel(); 
		add(laWarehouseName, gbc);
		gbc.gridx = 2;
		laWarehouseType = new JLabel();
		add(laWarehouseType, gbc);
		
		gbc.gridy = 8;
		gbc.gridx = 0;
		add(new JLabel("유통기한"), gbc);
		gbc.gridx = 1;
		chooser = new JDateChooser();
		chooser.setDateFormatString("yyyy-MM-dd");
		chooser.setPreferredSize(new Dimension(150, chooser.getPreferredSize().height));
		add(chooser, gbc);

		// 버튼
		btnSave = new JButton("저장");
		btnCancel = new JButton("취소");
		JPanel pButtons = new JPanel(new FlowLayout());
		pButtons.add(btnSave);
		pButtons.add(btnCancel);

		gbc.gridy = 9;
		gbc.gridx = 0;
		gbc.gridwidth = 4;
		add(pButtons, gbc);

		// 저장 버튼
		btnSave.addActionListener(e -> {
			int result = detailDAO.processInboundDetail(detail.getIoDetailId(), cbDamagedTypeCode.getSelectedIndex()+1, Integer.parseInt(tfDamagedQuantity.getText().trim()), Integer.parseInt(laPlannedQuantity.getText().toString()), warehouse.getWarehouseId());
			if (result > 0) {
				JOptionPane.showMessageDialog(this, "검수 완료");				
			} else {
				JOptionPane.showMessageDialog(this, "오류가 발생했습니다.");
			}
			dispose();
			model.setData(Collections.emptyMap());
		});

		btnCancel.addActionListener(e -> dispose());

		setSize(530, 460);
		setLocationRelativeTo(owner);
	}
	
	
}
