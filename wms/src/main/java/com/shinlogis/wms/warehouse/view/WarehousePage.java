package com.shinlogis.wms.warehouse.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.common.config.ButtonEditor;
import com.shinlogis.wms.common.config.ButtonRenderer;
import com.shinlogis.wms.common.config.Config;
import com.shinlogis.wms.common.config.Page;
import com.shinlogis.wms.inventory.view.InventoryPage;
import com.shinlogis.wms.warehouse.model.Warehouse;
import com.shinlogis.wms.warehouse.repository.WarehouseDAO;

public class WarehousePage extends Page {

	private JPanel pPageName;
	private JLabel laPageName;

	private JPanel pSearch;
	private JTextField tfCode, tfName, tfLocation;
	public JButton btnSearch;

	private JPanel pTable;
	private JLabel laPlanCount;
	private JTable table;
	private JScrollPane scrollPane;
	private DefaultTableModel model;
	private JPanel pTableNorth;

	private JPanel pPaging;
	private JButton btnPrevPage, btnNextPage;
	private JLabel laPageInfo;

	private String[] columnNames = { "선택", "창고코드", "창고명", "위치", "재고보기" };

	private int currentPage = 1;
	private final int rowsPerPage = 10;
	private List<Warehouse> fullList;

	private AppMain appMain;

	public WarehousePage(AppMain appMain) {
		super(appMain);

		this.appMain = appMain;

		/* ==== 검색 영역 ==== */
		pSearch = new JPanel(new GridBagLayout());
		pSearch.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.SEARCH_BAR_HEIGHT));
		pSearch.setBackground(Color.WHITE);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 8, 5, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		int col = 0;

		gbc.gridx = col++;
		pSearch.add(new JLabel("창고코드"), gbc);
		tfCode = new JTextField(10);
		gbc.gridx = col++;
		pSearch.add(tfCode, gbc);

		gbc.gridx = col++;
		pSearch.add(new JLabel("창고명"), gbc);
		tfName = new JTextField(10);
		gbc.gridx = col++;
		pSearch.add(tfName, gbc);

		gbc.gridx = col++;
		pSearch.add(new JLabel("위치"), gbc);
		tfLocation = new JTextField(10);
		gbc.gridx = col++;
		pSearch.add(tfLocation, gbc);

		btnSearch = new JButton("검색");
		gbc.gridx = col;
		pSearch.add(btnSearch, gbc);

		/* ==== 페이지명 ==== */
		pPageName = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pPageName.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.PAGE_NAME_HEIGHT));
		laPageName = new JLabel("창고관리");
		pPageName.add(laPageName);

		/* ==== 테이블 상단 카운트 ==== */
		pTableNorth = new JPanel(new BorderLayout());
		pTableNorth.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.TABLE_NORTH_HEIGHT));

		laPlanCount = new JLabel("총 0개의 상품 검색");
		laPlanCount.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		// 선택삭제 버튼 오른쪽에 배치
		JButton btnDeleteSelected = new JButton("선택삭제");
		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		rightPanel.setOpaque(false);
		rightPanel.setBackground(Color.WHITE); // 배경색 일치
		rightPanel.add(btnDeleteSelected);

		// 상단 패널에 부착
		pTableNorth.add(laPlanCount, BorderLayout.WEST);
		pTableNorth.add(rightPanel, BorderLayout.EAST);

		btnDeleteSelected.addActionListener(e -> deleteSelectedWarehouses());

		/* ==== 테이블 ==== */
		model = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				int invenCol = getColumnIndex("재고보기");
				int checkboxCol = getColumnIndex("선택");
				return column == invenCol || column == checkboxCol;
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				if (columnIndex == getColumnIndex("선택")) {
					return Boolean.class; // 체크박스를 위한 Boolean 타입
				}
				return String.class;
			}
		};

		table = new JTable(model);
		table.setRowHeight(45);
		table.getTableHeader().setPreferredSize(new Dimension(Config.CONTENT_WIDTH - 20, 45));
		table.getColumn("재고보기").setCellRenderer(new ButtonRenderer());
		table.getColumn("재고보기").setCellEditor(new ButtonEditor(new JCheckBox(), (tbl, row, column) -> {
			String warehouseCode = (String) tbl.getValueAt(row, 1); // 첫 번째 열이 창고코드
			showInventoryPage(warehouseCode);
		}));

		scrollPane = new JScrollPane(table);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(Config.CONTENT_WIDTH - 20, 495));

		/* ==== 페이징 ==== */
		pPaging = new JPanel(new FlowLayout(FlowLayout.CENTER));
		pPaging.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, 40));
		btnPrevPage = new JButton("이전");
		btnNextPage = new JButton("다음");
		laPageInfo = new JLabel("1 / 1");
		pPaging.add(btnPrevPage);
		pPaging.add(laPageInfo);
		pPaging.add(btnNextPage);

		/* ==== 테이블 포함 영역 ==== */
		pTable = new JPanel(new FlowLayout());
		pTable.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.TABLE_HEIGHT));
		pTable.setBackground(Color.WHITE);
		pTable.add(pTableNorth);
		pTable.add(scrollPane);
		pTable.add(pPaging);

		/* ==== 최종 배치 ==== */
		setLayout(new FlowLayout());
		add(pPageName);
		add(pSearch);
		add(pTable);
		setBackground(Color.LIGHT_GRAY);

		// 이벤트
		btnSearch.addActionListener(e -> {
			currentPage = 1;
			loadWarehouseData();
		});

		btnPrevPage.addActionListener(e -> {
			if (currentPage > 1) {
				currentPage--;
				loadWarehouseData();
			}
		});

		btnNextPage.addActionListener(e -> {
			int totalPages = (int) Math.ceil(fullList.size() / (double) rowsPerPage);
			if (currentPage < totalPages) {
				currentPage++;
				loadWarehouseData();
			}
		});

		// 최초 로딩
		loadWarehouseData();
	}

	private void loadWarehouseData() {
		model.setRowCount(0);

		String code = tfCode.getText().trim();
		String name = tfName.getText().trim();
		String location = tfLocation.getText().trim();

		WarehouseDAO dao = new WarehouseDAO();
		fullList = dao.selectWarehouseList(code, name, location); // List<Warehouse> 리턴

		int totalRows = fullList.size();
		int totalPages = (int) Math.ceil(totalRows / (double) rowsPerPage);
		if (totalPages == 0)
			totalPages = 1;

		int start = (currentPage - 1) * rowsPerPage;
		int end = Math.min(start + rowsPerPage, totalRows);

		for (int i = start; i < end; i++) {
			Warehouse warehouse = fullList.get(i);
			model.addRow(new Object[] { false, warehouse.getWarehouseCode(), warehouse.getWarehouseName(),
					warehouse.getAddress(), "보기", });
		}

		laPlanCount.setText("총 " + totalRows + "개의 창고 검색");
		laPageInfo.setText(currentPage + " / " + totalPages);
		btnPrevPage.setEnabled(currentPage > 1);
		btnNextPage.setEnabled(currentPage < totalPages);
	}

	public void showInventoryPage(String warehouseCode) {
		InventoryPage inventoryPage = (InventoryPage) appMain.pages[Config.INVENTORY_PAGE];

		inventoryPage.warehouseCode.setText(warehouseCode); // 값을 전달
		inventoryPage.productCode.setText("");
		inventoryPage.btnSearch.doClick(); // 검색 이벤트 실행
		appMain.showPage(Config.INVENTORY_PAGE);
	}

	private int getColumnIndex(String colName) {
		for (int i = 0; i < columnNames.length; i++) {
			if (columnNames[i].equals(colName))
				return i;
		}
		return -1;
	}

	public void resetFields() {
		tfCode.setText("");
		tfName.setText("");
		tfLocation.setText("");
	}

	private void deleteSelectedWarehouses() {
		WarehouseDAO dao = new WarehouseDAO();

		int checkboxCol = getColumnIndex("선택");
		int codeCol = getColumnIndex("창고코드");

		List<String> codesToDelete = new ArrayList<>();

		for (int i = 0; i < model.getRowCount(); i++) {
			Boolean checked = (Boolean) model.getValueAt(i, checkboxCol);
			if (checked != null && checked) {
				String code = (String) model.getValueAt(i, codeCol);
				codesToDelete.add(code);
			}
		}

		if (codesToDelete.isEmpty()) {
			JOptionPane.showMessageDialog(this, "삭제할 창고를 선택해주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(this, codesToDelete.size() + "개의 창고를 삭제하시겠습니까?", "삭제 확인",
				JOptionPane.YES_NO_OPTION);

		if (confirm == JOptionPane.YES_OPTION) {
			int deletedCount = dao.deleteWarehousesByCodes(codesToDelete);
			JOptionPane.showMessageDialog(this, deletedCount + "개의 창고가 삭제되었습니다.", "삭제 완료",
					JOptionPane.INFORMATION_MESSAGE);
			loadWarehouseData();
		}
	}
}