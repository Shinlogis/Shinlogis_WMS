package com.shinlogis.wms.inbound.view.detail;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.shinlogis.wms.damagedCode.repository.DamagedCodeDAO;
import com.shinlogis.wms.inoutbound.model.IODetail;
import com.shinlogis.wms.product.repository.ProductDAO;
import com.shinlogis.wms.snapshot.repository.SnapshotDAO;
import com.shinlogis.wms.warehouse.model.Warehouse;
import com.shinlogis.wms.warehouse.repository.WarehouseDAO;
import com.toedter.calendar.JDateChooser;

public class CheckDetailDialog extends JDialog {
	private IODetail ioDetail;
	private ProductDAO productDAO = new ProductDAO();
	private SnapshotDAO snapshotDAO = new SnapshotDAO();
	private DamagedCodeDAO damagedCodeDAO = new DamagedCodeDAO();
	private WarehouseDAO warehouseDAO = new WarehouseDAO();

	private String beforeProductCode;
	private int beforeQuantity;
	private String beforeStatus;

	private JLabel laIOReceipt;
	private JLabel laIODetail;
	private JLabel laProductCode;
	private JLabel laProductName;
	private JLabel laSupplierName;
	private JLabel laPlannedQuantity;
	private JComboBox<String> cbDamagedTypeCode;
	private JTextField tfDamagedTypeCode;
	private JLabel laActualQuantity;
	private JTextField tfWarehouseCode;
	private JLabel laWarehouseName;
	private JLabel laWarehouseType;

	private JLabel laWarehouseCode;
	private JDateChooser chooser; // 유통기한 선택 달력

	private JButton btnSearch;
	private JButton btnSave;
	private JButton btnCancel;

	private List<Warehouse> warehouses; // 창고 검색 결과

	public CheckDetailDialog(Frame owner, IODetail detail, DetailModel model) {
		super(owner, "입고 검수", true);

		this.ioDetail = detail;
		laIOReceipt = new JLabel(String.valueOf(detail.getIoReceipt().getIoReceiptId()));
		laIODetail = new JLabel(String.valueOf(detail.getIoItemId()));
		laProductCode = new JLabel(detail.getProductSnapshot().getProductCode());
		laProductName = new JLabel(detail.getProductSnapshot().getProductName());
		laSupplierName = new JLabel(detail.getProductSnapshot().getSupplierName());
		laPlannedQuantity = new JLabel(String.valueOf(detail.getPlannedQuantity()));
		tfDamagedTypeCode = new JTextField(10);

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

		List<String> names = damagedCodeDAO.selectAllNames();
		cbDamagedTypeCode = new JComboBox<>();
		for (String name : names) {
			cbDamagedTypeCode.addItem(name);
		}
		gbc.gridx = 1;
		add(cbDamagedTypeCode, gbc);
		gbc.gridx = 2;
		add(new JLabel("파손수량"), gbc);
		gbc.gridx = 3;
		add(tfDamagedTypeCode, gbc);

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

		gbc.gridy = 7;
		gbc.gridx = 1;
		laWarehouseName = new JLabel(); 
		add(laWarehouseName, gbc);
		gbc.gridx = 2;
		laWarehouseType = new JLabel();
		add(laWarehouseType, gbc);

		btnSearch.addActionListener(e -> {
			warehouses = warehouseDAO.selectByCode(tfWarehouseCode.getText().trim().toString());
			if (warehouses.size() > 0) {
				JOptionPane.showMessageDialog(this, "사용 가능한 창고입니다.");
				laWarehouseName.setText(warehouses.get(0).getWarehouseName());
				laWarehouseType.setText(warehouses.get(0).getStorageType().getTypeName());
			} else {
				JOptionPane.showMessageDialog(this, "존재하지 않는 창고입니다.");
			}
		});

		// 버튼
		btnSave = new JButton("저장");
		btnCancel = new JButton("취소");
		JPanel pButtons = new JPanel(new FlowLayout());
		pButtons.add(btnSave);
		pButtons.add(btnCancel);

		gbc.gridx = 0;
		gbc.gridy = 8;
		gbc.gridwidth = 4;
		add(pButtons, gbc);

		// 저장 버튼
		btnSave.addActionListener(e -> {

			JOptionPane.showMessageDialog(this, "수정 완료");
			dispose();
			model.setData(Collections.emptyMap());
		});

		btnCancel.addActionListener(e -> dispose());

		setSize(530, 420);
		setLocationRelativeTo(owner);

	}
}
