package com.shinlogis.wms.inventory.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.common.config.Config;
import com.shinlogis.wms.common.config.Page;
import com.shinlogis.wms.inventory.model.InventoryDTO;
import com.shinlogis.wms.inventory.repository.InventoryDAO;
import com.toedter.calendar.JDateChooser;

public class InventoryPage extends Page {

	private JPanel pPageName;
	private JLabel laPageName;

	private JPanel pSearch;
	private JTextField warehouseCode, productCode, supplierName, warehouseName, productName;
	private JDateChooser chooser;
	private JButton btnSearch;

	public JPanel pTable;
	private JLabel laPlanCount;
	private JTable tblPlan;
	private JScrollPane scTable;
	private DefaultTableModel model;
	private JPanel pTableNorth;
	private JButton btnRegister;

	private JPanel pPaging;
	private JButton btnPrevPage, btnNextPage;
	private JLabel laPageInfo;

	private String[] columnNames = { "창고코드", "창고명", "상품코드", "상품명", "공급사명", "유통기한", "총재고수량" };

	private int currentPage = 1;
	private final int rowsPerPage = 10;
	private List<InventoryDTO> fullList;

	public InventoryPage(AppMain appMain) {
		super(appMain);

		/* ==== 검색 영역 ==== */
		pSearch = new JPanel(new GridBagLayout());
		pSearch.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.SEARCH_BAR_HEIGHT));
		pSearch.setBackground(Color.WHITE);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 8, 5, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = 0;
		gbc.gridy = 0;
		pSearch.add(new JLabel("창고코드"), gbc);
		warehouseCode = new JTextField(10);
		gbc.gridx = 1;
		pSearch.add(warehouseCode, gbc);

		gbc.gridx = 2;
		pSearch.add(new JLabel("상품코드"), gbc);
		productCode = new JTextField(10);
		gbc.gridx = 3;
		pSearch.add(productCode, gbc);

		gbc.gridx = 4;
		pSearch.add(new JLabel("공급사명"), gbc);
		supplierName = new JTextField(10);
		gbc.gridx = 5;
		pSearch.add(supplierName, gbc);

		gbc.gridx = 6;
		pSearch.add(new JLabel("유통기한"), gbc);
		chooser = new JDateChooser();
		chooser.setDateFormatString("yyyy-MM-dd");
		chooser.setPreferredSize(new Dimension(150, chooser.getPreferredSize().height));
		gbc.gridx = 7;
		pSearch.add(chooser, gbc);
		
		gbc.gridx = 8;
		pSearch.add(new JLabel("창고명"), gbc);
		warehouseName = new JTextField(10);
		gbc.gridx = 9;
		pSearch.add(warehouseName, gbc);
		
		btnSearch = new JButton("검색");
		gbc.gridx = 10;
		pSearch.add(btnSearch, gbc);

		gbc.gridy = 1;
		gbc.gridx = 0;
		pSearch.add(new JLabel("상품명"), gbc);
		productName = new JTextField(10);
		gbc.gridx = 1;
		pSearch.add(productName, gbc);

		/* ==== 페이지명 영역 ==== */
		pPageName = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pPageName.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.PAGE_NAME_HEIGHT));
		laPageName = new JLabel("재고관리 > 상품관리");
		pPageName.add(laPageName);

		/* ==== 검색 결과 카운트 영역 ==== */
		pTableNorth = new JPanel(new FlowLayout());
		pTableNorth.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.TABLE_NORTH_HEIGHT));
		laPlanCount = new JLabel("총 n개의 재고 검색");
		laPlanCount.setPreferredSize(new Dimension(Config.CONTENT_WIDTH - 150, 30));
		pTableNorth.add(laPlanCount);

		/* ==== 테이블 영역 ==== */
		model = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // 현재 상태 수정 불가
			}
		};

		tblPlan = new JTable(model);
		tblPlan.setRowHeight(45);
		tblPlan.getTableHeader().setPreferredSize(new Dimension(Config.CONTENT_WIDTH - 20, 45));
		tblPlan.setPreferredScrollableViewportSize(new Dimension(Config.CONTENT_WIDTH - 20, 495));

		for (int i = 0; i < tblPlan.getColumnCount(); i++) {
			tblPlan.getColumnModel().getColumn(i).setPreferredWidth(60);
		}

		scTable = new JScrollPane(tblPlan);
		scTable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scTable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); //세로 스크롤 비활성화
		scTable.setPreferredSize(new Dimension(Config.CONTENT_WIDTH - 20, 495));

		/* ==== 페이징 컨트롤 ==== */
		pPaging = new JPanel(new FlowLayout(FlowLayout.CENTER));
		pPaging.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, 40));
		btnPrevPage = new JButton("이전");
		btnNextPage = new JButton("다음");
		laPageInfo = new JLabel("1 / 1");
		btnPrevPage.setEnabled(false);
		btnNextPage.setEnabled(false);
		pPaging.add(btnPrevPage);
		pPaging.add(laPageInfo);
		pPaging.add(btnNextPage);

		pTable = new JPanel(new FlowLayout());
		pTable.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.TABLE_HEIGHT)); // 785
		pTable.setBackground(Color.WHITE);
		pTable.add(pTableNorth); // 40
		pTable.add(scTable); // 660
		pTable.add(pPaging); // 40

		/* ==== 최종 배치 ==== */
		setLayout(new FlowLayout());
		add(pPageName);
		add(pSearch);
		add(pTable);
		setBackground(Color.LIGHT_GRAY);
		
		// 로딩 및 이벤트
		loadInventoryData();

		btnSearch.addActionListener(e -> {
			currentPage = 1;
			loadInventoryData();
		});

		btnPrevPage.addActionListener(e -> {
			if (currentPage > 1) {
				currentPage--;
				loadInventoryData();
			}
		});

		btnNextPage.addActionListener(e -> {
			int totalPages = (int) Math.ceil(fullList.size() / (double) rowsPerPage);
			if (currentPage < totalPages) {
				currentPage++;
				loadInventoryData();
			}
		});
	}

	private void loadInventoryData() {
		model.setRowCount(0);

		InventoryDAO dao = new InventoryDAO();
		fullList = dao.selectInventoryDetails();

		int totalRows = fullList.size();
		int totalPages = (int) Math.ceil(totalRows / (double) rowsPerPage);
		if (totalPages == 0)
			totalPages = 1;

		int start = (currentPage - 1) * rowsPerPage;
		int end = Math.min(start + rowsPerPage, totalRows);

		for (int i = start; i < end; i++) {
			InventoryDTO dto = fullList.get(i);
			Object[] row = { dto.getWarehouseCode(), dto.getWarehouseName(), dto.getProductCode(), dto.getProductName(),
					dto.getSupplierName(), dto.getExpiryDate(),

					dto.getTotalQuantity() };
			model.addRow(row);
		}

		laPlanCount.setText("총 " + totalRows + "개의 재고 검색");
		laPageInfo.setText(currentPage + " / " + totalPages);
		btnPrevPage.setEnabled(currentPage > 1);
		btnNextPage.setEnabled(currentPage < totalPages);
	}
}
