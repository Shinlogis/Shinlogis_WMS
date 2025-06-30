// (패키지 및 import 생략하지 않음)
package com.shinlogis.wms.product.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.common.config.ButtonEditor;
import com.shinlogis.wms.common.config.ButtonRenderer;
import com.shinlogis.wms.common.config.Config;
import com.shinlogis.wms.common.config.Page;
import com.shinlogis.wms.inventory.view.InventoryPage;
import com.shinlogis.wms.product.model.ProductDTO;
import com.shinlogis.wms.product.repository.ProductDAO;

public class ProductPage extends Page {

	// ... (필드 생략 없음)
	private JPanel pPageName;
	private JLabel laPageName;

	private JPanel pSearch;
	private JTextField productCode, productName, supplierName, priceMin, priceMax;
	private JComboBox<String> comboStorageType;
	public JButton btnSearch;
	private JPanel pTable;
	private JLabel laPlanCount;
	private JTable tblPlan;
	private JScrollPane scTable;
	private DefaultTableModel model;
	private JPanel pTableNorth;

	private JPanel pPaging;
	private JButton btnPrevPage, btnNextPage;
	private JLabel laPageInfo;

	private String[] columnNames = { "선택", "사진", "상품코드", "상품명", "공급사명", "보관타입", "가격", "재고보기" };

	private int currentPage = 1;
	private final int rowsPerPage = 5;
	private List<ProductDTO> fullList;

	private AppMain appMain;
	private JButton btnDeleteSelected;

	public ProductPage(AppMain appMain) {
		super(appMain);
		this.appMain = appMain;

		/* 검색 영역 구성 (생략 없이 동일하게 유지) */
		pSearch = new JPanel(new GridBagLayout());
		pSearch.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.SEARCH_BAR_HEIGHT));
		pSearch.setBackground(Color.WHITE);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 8, 5, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		int row = 0, col = 0;

		gbc.gridx = col++; gbc.gridy = row; pSearch.add(new JLabel("상품코드"), gbc);
		productCode = new JTextField(10); gbc.gridx = col++; pSearch.add(productCode, gbc);

		gbc.gridx = col++; pSearch.add(new JLabel("상품명"), gbc);
		productName = new JTextField(10); gbc.gridx = col++; pSearch.add(productName, gbc);

		gbc.gridx = col++; pSearch.add(new JLabel("공급사명"), gbc);
		supplierName = new JTextField(10); gbc.gridx = col++; pSearch.add(supplierName, gbc);

		gbc.gridx = col++; pSearch.add(new JLabel("보관타입"), gbc);
		String[] storageTypes = { "전체", "냉장", "냉동", "상온" };
		comboStorageType = new JComboBox<>(storageTypes); gbc.gridx = col++; pSearch.add(comboStorageType, gbc);

		gbc.gridx = col++; pSearch.add(new JLabel("가격"), gbc);
		priceMin = new JTextField(5); priceMax = new JTextField(5);
		JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		pricePanel.add(priceMin); pricePanel.add(new JLabel(" ~ ")); pricePanel.add(priceMax);
		gbc.gridx = col++; pSearch.add(pricePanel, gbc);

		btnSearch = new JButton("검색");
		styleButton(btnSearch, new Color(70, 130, 180));
		gbc.gridx = col; pSearch.add(btnSearch, gbc);

		/* 페이지명 영역 */
		pPageName = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pPageName.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.PAGE_NAME_HEIGHT));
		laPageName = new JLabel("상품관리");
		laPageName.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		pPageName.add(laPageName);

		/* 테이블 상단 영역 */
		pTableNorth = new JPanel(new BorderLayout());
		pTableNorth.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.TABLE_NORTH_HEIGHT));
		laPlanCount = new JLabel("총 0개의 상품 검색");

		JButton btnAdd = new JButton("추가");
		btnDeleteSelected = new JButton("선택삭제");
		styleButton(btnAdd,  new Color(60, 120, 180));
		styleButton(btnDeleteSelected, new Color(60, 120, 180));

		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		rightPanel.setOpaque(false);
		rightPanel.setBackground(Color.WHITE);
		rightPanel.add(btnAdd);
		rightPanel.add(btnDeleteSelected);

		pTableNorth.add(laPlanCount, BorderLayout.WEST);
		pTableNorth.add(rightPanel, BorderLayout.EAST);

		/* 테이블 모델 */
		model = new DefaultTableModel(columnNames, 0) {
			@Override public Class<?> getColumnClass(int columnIndex) {
				if (columnIndex == 0) return Boolean.class;
				if (columnIndex == 1) return ImageIcon.class;
				return String.class;
			}
			@Override public boolean isCellEditable(int row, int column) {
				int invenCol = getColumnIndex("재고보기");
				int checkCol = getColumnIndex("선택");
				return column == checkCol || column == invenCol;
			}
		};

		tblPlan = new JTable(model);
		TableColumnModel columnModel = tblPlan.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(7);
		columnModel.getColumn(1).setPreferredWidth(120);
		columnModel.getColumn(2).setPreferredWidth(120);
		columnModel.getColumn(3).setPreferredWidth(120);
		columnModel.getColumn(4).setPreferredWidth(120);
		columnModel.getColumn(5).setPreferredWidth(120);
		columnModel.getColumn(6).setPreferredWidth(120);
		columnModel.getColumn(7).setPreferredWidth(30);
		tblPlan.setRowHeight(120);
		tblPlan.setGridColor(new Color(220, 220, 220));
		tblPlan.setBackground(Color.WHITE);
		tblPlan.setSelectionBackground(new Color(230, 240, 255));

		JTableHeader header = tblPlan.getTableHeader();
		header.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, 40));
		header.setBackground(new Color(245, 245, 245));
		header.setFont(new Font("맑은 고딕", Font.BOLD, 13));

		tblPlan.getColumn("재고보기").setCellRenderer(new ButtonRenderer());
		tblPlan.getColumn("재고보기").setCellEditor(new ButtonEditor(new JCheckBox(), (JTable tbl, int Row, int column) -> {
			String productCode = (String) tbl.getValueAt(Row, 2);
			showInventoryPage(productCode);
		}));
		tblPlan.setPreferredScrollableViewportSize(new Dimension(Config.CONTENT_WIDTH - 20, 650));

		scTable = new JScrollPane(tblPlan);
		scTable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scTable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scTable.setPreferredSize(new Dimension(Config.CONTENT_WIDTH - 20, 680));

		/* 페이징 */
		pPaging = new JPanel(new FlowLayout(FlowLayout.CENTER));
		pPaging.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, 40));
		btnPrevPage = new JButton("이전");
		btnNextPage = new JButton("다음");
		styleButton(btnPrevPage, Color.GRAY);
		styleButton(btnNextPage, Color.GRAY);
		laPageInfo = new JLabel("1 / 1");
		btnPrevPage.setEnabled(false);
		btnNextPage.setEnabled(false);
		pPaging.add(btnPrevPage);
		pPaging.add(laPageInfo);
		pPaging.add(btnNextPage);

		/* 전체 테이블 패널 */
		pTable = new JPanel(new FlowLayout());
		pTable.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.TABLE_HEIGHT));
		pTable.setBackground(Color.WHITE);
		pTable.add(pTableNorth);
		pTable.add(scTable);
		pTable.add(pPaging);

		/* 최종 배치 */
		setLayout(new FlowLayout());
		add(pPageName);
		add(pSearch);
		add(pTable);
		setBackground(Color.LIGHT_GRAY);

		// 이벤트 리스너
		btnDeleteSelected.addActionListener(e -> handleDeleteSelected());
		btnAdd.addActionListener(e -> {
			ProductDTO dto = new ProductDTO();
			AddProductDialog dialog = new AddProductDialog(appMain, dto);
			dialog.setVisible(true);
			loadProductData();
		});
		btnSearch.addActionListener(e -> {
			currentPage = 1;
			loadProductData();
		});
		btnPrevPage.addActionListener(e -> {
			if (currentPage > 1) {
				currentPage--;
				loadProductData();
			}
		});
		btnNextPage.addActionListener(e -> {
			int totalPages = (int) Math.ceil(fullList.size() / (double) rowsPerPage);
			if (currentPage < totalPages) {
				currentPage++;
				loadProductData();
			}
		});

		loadProductData(); // 초기 데이터
	}

	private void handleDeleteSelected() {
		int rowCount = model.getRowCount();
		List<String> codesToDelete = new ArrayList<>();
		for (int i = 0; i < rowCount; i++) {
			Boolean selected = (Boolean) model.getValueAt(i, 0);
			if (selected != null && selected) {
				codesToDelete.add((String) model.getValueAt(i, 2));
			}
		}
		if (codesToDelete.isEmpty()) {
			JOptionPane.showMessageDialog(null, "삭제할 상품을 선택해주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		int confirm = JOptionPane.showConfirmDialog(null, codesToDelete.size() + "개의 상품을 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);
		if (confirm != JOptionPane.YES_OPTION) return;

		ProductDAO dao = new ProductDAO();
		int deletedCount = dao.deleteProductsByCodes(codesToDelete);
		if (deletedCount > 0) {
			fullList.removeIf(dto -> codesToDelete.contains(dto.getProductCode()));
			for (int i = rowCount - 1; i >= 0; i--) {
				String code = (String) model.getValueAt(i, 2);
				if (codesToDelete.contains(code)) model.removeRow(i);
			}
			int totalRows = fullList.size();
			int totalPages = (int) Math.ceil(totalRows / (double) rowsPerPage);
			if (totalPages == 0) totalPages = 1;
			if (currentPage > totalPages) currentPage = totalPages;
			loadProductData();
			JOptionPane.showMessageDialog(null, deletedCount + "개의 상품이 삭제되었습니다.", "삭제 완료", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "상품 삭제에 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void loadProductData() {
		model.setRowCount(0);
		String code = productCode.getText().trim();
		String name = productName.getText().trim();
		String supplier = supplierName.getText().trim();
		String storageType = (String) comboStorageType.getSelectedItem();
		String priceMinStr = priceMin.getText().trim();
		String priceMaxStr = priceMax.getText().trim();
		int minPrice = priceMinStr.isEmpty() ? 0 : Integer.parseInt(priceMinStr);
		int maxPrice = priceMaxStr.isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(priceMaxStr);
		if ("전체".equals(storageType)) storageType = null;

		ProductDAO dao = new ProductDAO();
		fullList = dao.selectProductList(code, name, supplier, storageType, minPrice, maxPrice);

		int totalRows = fullList.size();
		int totalPages = (int) Math.ceil(totalRows / (double) rowsPerPage);
		if (totalPages == 0) totalPages = 1;
		int start = (currentPage - 1) * rowsPerPage;
		int end = Math.min(start + rowsPerPage, totalRows);

		for (int i = start; i < end; i++) {
			ProductDTO dto = fullList.get(i);
			Object[] row = {
				false,
				resizeImage(dto.getThumbnailPath(), 100, 100),
				dto.getProductCode(),
				dto.getProductName(),
				dto.getSupplierName(),
				dto.getStorageTypeName(),
				dto.getPrice(),
				"보기"
			};
			model.addRow(row);
		}

		laPlanCount.setText("총 " + totalRows + "개의 상품 검색");
		laPageInfo.setText(currentPage + " / " + totalPages);
		btnPrevPage.setEnabled(currentPage > 1);
		btnNextPage.setEnabled(currentPage < totalPages);
	}

	public void showInventoryPage(String productCode) {
		InventoryPage inventoryPage = (InventoryPage) appMain.pages[Config.INVENTORY_PAGE];
		inventoryPage.productCode.setText(productCode);
		inventoryPage.warehouseCode.setText("");
		inventoryPage.btnSearch.doClick();
		appMain.showPage(Config.INVENTORY_PAGE);
	}

	private int getColumnIndex(String colName) {
		for (int i = 0; i < columnNames.length; i++) {
			if (columnNames[i].equals(colName)) return i;
		}
		return -1;
	}

	private ImageIcon resizeImage(String path, int width, int height) {
		if (path == null || path.isEmpty()) {
			path = "images/default.png";
		}
		URL imgUrl = getClass().getClassLoader().getResource(path);
		if (imgUrl == null) {
			return new ImageIcon();
		}
		ImageIcon icon = new ImageIcon(imgUrl);
		Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return new ImageIcon(image);
	}

	public void resetFields() {
		productCode.setText("");
		productName.setText("");
		supplierName.setText("");
		comboStorageType.setSelectedIndex(0);
		priceMin.setText("");
		priceMax.setText("");
	}

	// 버튼 스타일 공통 메서드
	private void styleButton(JButton button, Color bgColor) {
		button.setBackground(bgColor);
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
		button.setPreferredSize(new Dimension(100, 30));
	}
}