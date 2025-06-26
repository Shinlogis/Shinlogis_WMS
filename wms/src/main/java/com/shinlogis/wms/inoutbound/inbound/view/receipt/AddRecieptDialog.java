package com.shinlogis.wms.inoutbound.inbound.view.receipt;

import java.awt.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.headquarters.model.HeadquartersUser;
import com.shinlogis.wms.inoutbound.inbound.repository.DetailDAO;
import com.shinlogis.wms.inoutbound.inbound.repository.ReceiptDAO;
import com.shinlogis.wms.inoutbound.model.IOReceipt;
import com.shinlogis.wms.inoutbound.model.InboundForm;
import com.shinlogis.wms.product.model.Product;
import com.shinlogis.wms.product.repository.ProductDAO;
import com.shinlogis.wms.snapshot.repository.SnapshotDAO;
import com.toedter.calendar.JDateChooser;

public class AddRecieptDialog extends JDialog {
	private AppMain appMain;
	private HeadquartersUser user;

	private JPanel formPanel;
	private JScrollPane scrollPane;
	private JButton btnAddRow, btnSave, btnCancel;

	private ProductDAO productDAO = new ProductDAO();
	private ReceiptDAO receiptDAO = new ReceiptDAO();
	private DetailDAO detailDAO = new DetailDAO();
	private SnapshotDAO snapshotDAO = new SnapshotDAO();
	private List<RowComponents> rowComponentsList = new ArrayList<>();

	public AddRecieptDialog(Frame owner, AppMain appMain) {
		super(owner, "입고예정 등록", true);
		this.appMain = appMain;
		this.user =	appMain.headquartersUser;

		setLayout(new BorderLayout());

		formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
		scrollPane = new JScrollPane(formPanel);
		add(scrollPane, BorderLayout.CENTER);

		addFormRow();

		JPanel bottomPanel = new JPanel(new FlowLayout());
		btnAddRow = new JButton("추가");
		btnSave = new JButton("저장");
		btnCancel = new JButton("취소");

		bottomPanel.add(btnAddRow);
		bottomPanel.add(btnSave);
		bottomPanel.add(btnCancel);
		add(bottomPanel, BorderLayout.SOUTH);

		btnAddRow.addActionListener(e -> addFormRow());

		// 저장 버튼에 액션 리스너 추가
		btnSave.addActionListener(e -> {
			int successCount = 0; // 입고 성공 건 수 카운트
			List<InboundForm> inboundForms = new ArrayList<>();
			Map<String, Object> receiptMap = new HashMap<>();

			// 각 행에 입력한 정보 추출
			for (RowComponents row : rowComponentsList) {
				String code = row.tfProductCode.getText().trim();
				String qtyStr = row.tfQuantity.getText().trim();

				if (code.isEmpty() || row.laProductName.getText().trim().isEmpty()
						|| row.laSupplierName.getText().trim().isEmpty()) {
					JOptionPane.showMessageDialog(this, "상품 정보가 누락된 항목이 있습니다.");
					return;
				}

				int qty;
				try {
					qty = Integer.parseInt(qtyStr);
					if (qty < 0)
						throw new NumberFormatException();
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(this, "수량은 0 이상의 숫자여야 합니다.");
					return;
				}

				Product product = productDAO.selectByCode(code); // 완전한 상품 객체
				if (product == null) {
					JOptionPane.showMessageDialog(this, "존재하지 않는 상품입니다: " + code);
					return;
				}

				java.util.Date utilDate = row.chooser.getDate();
				if (utilDate == null) {
					JOptionPane.showMessageDialog(this, "예정일을 선택해주세요.");
					return;
				}

				Date sqlDate = new java.sql.Date(utilDate.getTime());

				// 입력한 정보로 form 생성
				InboundForm form = new InboundForm();
				form.setProduct(product);
				form.setQuantity(qty);
				form.setPlannedDate(sqlDate);

				inboundForms.add(form);
			}

			// 입고상세 생성
			for (InboundForm form : inboundForms) {
				// 입고예정 생성
				receiptMap = receiptDAO.insertReceipt(form, user);
				int receiptId = (Integer) receiptMap.get("id"); // 위에서 생성한 입고예정의 id

				int snapshotId = snapshotDAO.createSnapshotFromForm(form); // 스냅샷 ID 받아오기
				int result = detailDAO.insertDetail(receiptId, snapshotId, form, user); // insert에 전달
				if (result == 0) {
					JOptionPane.showMessageDialog(this, "등록에 실패했습니다.");
				} else {
					successCount++;
				}
			}

			JOptionPane.showMessageDialog(this, successCount + "건 등록 완료");
			dispose();
		});

		btnCancel.addActionListener(e -> dispose());

		setSize(1400, 550);
		setLocationRelativeTo(owner);
	}

	private void addFormRow() {
		JPanel row = new JPanel(new GridBagLayout());
		row.setAlignmentX(Component.LEFT_ALIGNMENT);
		row.setMaximumSize(new Dimension(2000, 40)); // 한 줄에 넉넉히 표현

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(2, 4, 2, 4);
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;

		int col = 0;

		row.add(new JLabel("상품코드"), gbc);
		gbc.gridx = ++col;
		JTextField tfProductCode = new JTextField(10);
		row.add(tfProductCode, gbc);

		gbc.gridx = ++col;
		JButton btnSearch = new JButton("검색");
		row.add(btnSearch, gbc);

		gbc.gridx = ++col;
		row.add(new JLabel("상품명"), gbc);
		gbc.gridx = ++col;
		JLabel laProductName = new JLabel(" ");
		laProductName.setPreferredSize(new Dimension(120, 25));
		row.add(laProductName, gbc);

		gbc.gridx = ++col;
		row.add(new JLabel("공급사명"), gbc);
		gbc.gridx = ++col;
		JLabel laSupplierName = new JLabel(" ");
		laSupplierName.setPreferredSize(new Dimension(120, 25));
		row.add(laSupplierName, gbc);

		gbc.gridx = ++col;
		row.add(new JLabel("가격"), gbc);
		gbc.gridx = ++col;
		JLabel laPrice = new JLabel(" ");
		laPrice.setPreferredSize(new Dimension(80, 25));
		row.add(laPrice, gbc);

		gbc.gridx = ++col;
		row.add(new JLabel("보관타입"), gbc);
		gbc.gridx = ++col;
		JLabel laStorageType = new JLabel(" ");
		laStorageType.setPreferredSize(new Dimension(80, 25));
		row.add(laStorageType, gbc);

		gbc.gridx = ++col;
		row.add(new JLabel("수량"), gbc);
		gbc.gridx = ++col;
		JTextField tfQuantity = new JTextField(5);
		row.add(tfQuantity, gbc);

		gbc.gridx = ++col;
		row.add(new JLabel("예정일"), gbc);
		gbc.gridx = ++col;
		JDateChooser chooser = new JDateChooser();
		chooser.setPreferredSize(new Dimension(120, 25));
		chooser.setDateFormatString("yyyy-MM-dd");
		row.add(chooser, gbc);

		gbc.gridx = ++col;
		JButton btnDelete = new JButton("삭제");
		row.add(btnDelete, gbc);

		// 이벤트
		btnSearch.addActionListener(e -> {
			Product product = productDAO.selectByCode(tfProductCode.getText().trim());
			if (product != null) {
				laProductName.setText(product.getProductName());
				laSupplierName.setText(product.getSupplier().getName());
				laPrice.setText(String.valueOf((int) product.getPrice()));
				laStorageType.setText(product.getStorageType().getTypeName());
				laProductName.setForeground(Color.BLUE);
				laSupplierName.setForeground(Color.BLUE);
				laPrice.setForeground(Color.BLUE);
				laStorageType.setForeground(Color.BLUE);

			} else {
				JOptionPane.showMessageDialog(this, "존재하지 않는 상품입니다.");
			}
		});

		btnDelete.addActionListener(e -> {
			formPanel.remove(row);
			formPanel.revalidate();
			formPanel.repaint();
			rowComponentsList.removeIf(rc -> rc.tfProductCode == tfProductCode);
		});

		formPanel.add(row);
		formPanel.revalidate();
		formPanel.repaint();

		SwingUtilities.invokeLater(() -> {
			JScrollBar vertical = scrollPane.getVerticalScrollBar();
			vertical.setValue(vertical.getMaximum());
		});

		rowComponentsList
				.add(new RowComponents(tfProductCode, btnSearch, laProductName, laSupplierName, tfQuantity, chooser));
	}

	private static class RowComponents {
		JTextField tfProductCode;
		JButton btnSearch;
		JLabel laProductName;
		JLabel laSupplierName;
		JTextField tfQuantity;
		JDateChooser chooser;

		public RowComponents(JTextField tfProductCode, JButton btnSearch, JLabel laProductName, JLabel laSupplierName,
				JTextField tfQuantity, JDateChooser chooser) {
			this.tfProductCode = tfProductCode;
			this.btnSearch = btnSearch;
			this.laProductName = laProductName;
			this.laSupplierName = laSupplierName;
			this.tfQuantity = tfQuantity;
			this.chooser = chooser;
		}
	}
}
