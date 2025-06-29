package com.shinlogis.wms.inoutbound.inbound.view.receipt;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.common.config.ButtonEditor;
import com.shinlogis.wms.common.config.ButtonRenderer;
import com.shinlogis.wms.common.config.Config;
import com.shinlogis.wms.common.config.Page;
import com.shinlogis.wms.inoutbound.inbound.repository.DetailDAO;
import com.shinlogis.wms.inoutbound.inbound.repository.ReceiptDAO;
import com.shinlogis.wms.inoutbound.inbound.view.detail.DetailPage;
import com.shinlogis.wms.inoutbound.model.IOReceipt;
import com.shinlogis.wms.inventory.view.InventoryPage;
import com.toedter.calendar.JDateChooser;

public class ReceiptPage extends Page {
	private JPanel pPageName;
	private JLabel laPageName;

	private JPanel pSearch;
	private JTextField tfPlanId;
	private JDateChooser chooser;
	private JTextField tfSupplierName;
	private JComboBox<String> cbStatus;
	private JTextField tfProduct;
	private JButton btnSearch;

	private JPanel pTable;
	private JLabel laPlanCount;
	private JTable tblPlan;
	private JScrollPane scTable;
	private ReceiptModel iModel;
	private JPanel pTableNorth;
	private JButton btnRegister;
	private JButton btnDelete;

	private DetailPage detailPage;
	private int currentPage = 1;
	private final int rowsPerPage = 14;
	private List<IOReceipt> fullList;
	private JButton btnPrevPage, btnNextPage;
	private JLabel laPageInfo;
	private JPanel pPaging;
	private AppMain appMain;
	
	public ReceiptPage(AppMain appMain, DetailPage detailPage) {
		super(appMain);
		this.appMain = appMain;
		this.detailPage = detailPage;
		ReceiptDAO receiptDAO = new ReceiptDAO();
		DetailDAO detailDAO = new DetailDAO();

		pSearch = new JPanel(new GridBagLayout());
		pSearch.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.SEARCH_BAR_HEIGHT));
		pSearch.setBackground(Color.WHITE);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 8, 5, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		tfPlanId = new JTextField(10);
		chooser = new JDateChooser();
		chooser.setDateFormatString("yyyy-MM-dd");
		tfSupplierName = new JTextField(10);
		cbStatus = new JComboBox<>(new String[] {"전체", "예정", "진행 중", "완료", "보류"});
		tfProduct = new JTextField(10);
		btnSearch = new JButton("검색");
		
		gbc.gridx = 0; gbc.gridy = 0; pSearch.add(new JLabel("입고예정ID"), gbc);
		gbc.gridx = 1; pSearch.add(tfPlanId, gbc);
		gbc.gridx = 2; pSearch.add(new JLabel("입고예정일자"), gbc);
		gbc.gridx = 3; pSearch.add(chooser, gbc);
		gbc.gridx = 4; pSearch.add(new JLabel("공급사명"), gbc);
		gbc.gridx = 5; pSearch.add(tfSupplierName, gbc);
		gbc.gridx = 6; pSearch.add(new JLabel("상태"), gbc);
		gbc.gridx = 7; pSearch.add(cbStatus, gbc);
		gbc.gridx = 8; pSearch.add(new JLabel("상품명"), gbc);
		gbc.gridx = 9; pSearch.add(tfProduct, gbc);
		gbc.gridx = 10; pSearch.add(btnSearch, gbc);

		btnSearch.addActionListener(e -> {
			currentPage = 1;
			loadReceiptData(getSearchFilters());
		});

		pPageName = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pPageName.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.PAGE_NAME_HEIGHT));
		laPageName = new JLabel("입고관리 > 입고예정");
		pPageName.add(laPageName);

		pTableNorth = new JPanel(new FlowLayout());
		laPlanCount = new JLabel("총 0개의 입고예정 검색");
		laPlanCount.setPreferredSize(new Dimension(Config.CONTENT_WIDTH - 150, 30));
		pTableNorth.add(laPlanCount);

		btnRegister = new JButton("등록");
		btnRegister.addActionListener(e -> {
			AddRecieptDialog dialog = new AddRecieptDialog(appMain, appMain, iModel, () -> {
				if (detailPage != null) detailPage.refreshDetailModel();
				loadReceiptData(Collections.emptyMap());
			});
			dialog.setVisible(true);
		});
		pTableNorth.add(btnRegister);

		btnDelete = new JButton("삭제");
		// 버튼 클릭 시 이벤트
		btnDelete.addActionListener(e -> {
			int receiptCnt = 0;
			int detailCnt = 0;
			List<Integer> checkedIds = iModel.getSelectedReceiptIds();
			for (int i=0; i<checkedIds.size(); i++) {
				int ioReceipt = checkedIds.get(i);
				// 선택한 입고예정을 비활성화
				receiptDAO.deactivateIoReceipt(ioReceipt);
				receiptCnt++;

				// 하위 입고상세 ID 찾기
				List<Integer> detailIds = receiptDAO.findDetailIsdByReceiptId(ioReceipt);

				// 비활성화시킨 입고예정의 하위 입고상세가 존재하는 경우, 모두 비활성화
				if (!detailIds.isEmpty()){
					for (int id: detailIds){
						detailDAO.deactivateIoDetail(id);
						detailCnt++;
					}
				}
			}
			JOptionPane.showMessageDialog(this, "입고예정" + receiptCnt+"건을 삭제했습니다.\n하위 입고상세 "+detailCnt+"건이 삭제되었습니다.");
			currentPage = 1;
			loadReceiptData(getSearchFilters()); // 입고예정 새로고침
		});
		pTableNorth.add(btnDelete);

		iModel = new ReceiptModel();
		tblPlan = new JTable(iModel);
		tblPlan.setRowHeight(45);
		tblPlan.getColumn("상세보기").setCellRenderer(new ButtonRenderer());
		tblPlan.getColumn("상세보기").setCellEditor(new ButtonEditor(new JCheckBox(), (table, row, column) -> {
			int receipt = (int)iModel.getValueAt(row, 1);
			showDetailPage(receipt);
		}));


		scTable = new JScrollPane(tblPlan);
		scTable.setPreferredSize(new Dimension(Config.CONTENT_WIDTH - 20, 680));

		btnPrevPage = new JButton("이전");
		btnNextPage = new JButton("다음");
		laPageInfo = new JLabel("1 / 1");
		btnPrevPage.addActionListener(e -> {
			if (currentPage > 1) {
				currentPage--;
				loadReceiptData(getSearchFilters());
			}
		});
		btnNextPage.addActionListener(e -> {
			int totalPages = (int) Math.ceil(fullList.size() / (double) rowsPerPage);
			if (currentPage < totalPages) {
				currentPage++;
				loadReceiptData(getSearchFilters());
			}
		});

		pPaging = new JPanel(new FlowLayout());
		pPaging.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, 40));
		pPaging.add(btnPrevPage);
		pPaging.add(laPageInfo);
		pPaging.add(btnNextPage);

		pTable = new JPanel(new FlowLayout());
		pTable.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.TABLE_HEIGHT));
		pTable.setBackground(Color.WHITE);
		pTable.add(pTableNorth);
		pTable.add(scTable);
		pTable.add(pPaging);

		setLayout(new FlowLayout());
		add(pPageName);
		add(pSearch);
		add(pTable);
		setBackground(Color.LIGHT_GRAY);

		loadReceiptData(Collections.emptyMap());
	}

	private Map<String, Object> getSearchFilters() {
		Map<String, Object> filters = new HashMap<>();
		if (!tfPlanId.getText().trim().isEmpty())
			filters.put("io_receipt_id", Integer.parseInt(tfPlanId.getText().trim()));
		if (chooser.getDate() != null)
			filters.put("scheduled_date", new java.sql.Date(chooser.getDate().getTime()));
		if (!tfSupplierName.getText().trim().isEmpty())
			filters.put("supplier_name", tfSupplierName.getText().trim());
		String status = (String) cbStatus.getSelectedItem();
		if (!"전체".equals(status))
			filters.put("status", status);
		if (!tfProduct.getText().trim().isEmpty())
			filters.put("product_name", tfProduct.getText().trim());
		return filters;
	}
	private void loadReceiptData(Map<String, Object> filters) {
		// 1. 전체 데이터 로드
		iModel.setFullData(filters);
		fullList = iModel.getFullList();

		// 2. 페이징 계산
		int totalRows = fullList.size();
		int totalPages = (int) Math.ceil(totalRows / (double) rowsPerPage);
		if (totalPages == 0) totalPages = 1;

		// 3. 현재 페이지 데이터 설정
		int start = (currentPage - 1) * rowsPerPage;
		int end = Math.min(start + rowsPerPage, totalRows);
		List<IOReceipt> currentPageList = fullList.subList(start, end);
		iModel.setCurrentPageData(currentPageList);

		// 4. UI 갱신
		laPlanCount.setText("총 " + totalRows + "개의 입고예정 검색");
		laPageInfo.setText(currentPage + " / " + totalPages);
		btnPrevPage.setEnabled(currentPage > 1);
		btnNextPage.setEnabled(currentPage < totalPages);
	}

	public void refresh() {
		currentPage = 1;
		loadReceiptData(Collections.emptyMap());
	}

	public void showDetailPage(int detailId) {
		DetailPage detailPage = (DetailPage) appMain.pages[Config.INBOUND_ITEM_PAGE];

		detailPage.tfPlanId.setText(Integer.toString(detailId)); // 값을 전달
		detailPage.btnSearch.doClick(); // 검색 이벤트 실행
		appMain.showPage(Config.INBOUND_ITEM_PAGE);
	}


}