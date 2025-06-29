package com.shinlogis.wms.inoutbound.inbound.view.detail;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Date;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.common.config.ButtonEditor;
import com.shinlogis.wms.common.config.ButtonRenderer;
import com.shinlogis.wms.common.config.Config;
import com.shinlogis.wms.common.config.Page;
import com.shinlogis.wms.inoutbound.inbound.repository.DetailDAO;
import com.shinlogis.wms.inoutbound.inbound.repository.ReceiptDAO;
import com.shinlogis.wms.inoutbound.model.IODetail;
import com.toedter.calendar.JDateChooser;

public class DetailPage extends Page {
	DetailDAO detailDAO = new DetailDAO();
	ReceiptDAO receiptDAO = new ReceiptDAO();
	
	private JPanel pPageName;
	private JLabel laPageName;

	private JPanel pSearch;
	public JTextField tfPlanId;
	private JTextField tfPlanItemId, tfProductCode, tfSupplierName, tfProduct;
	private JComboBox<String> cbStatus;
	private JDateChooser chooser;
	public JButton btnSearch;

	private JPanel pTable;
	private JLabel laPlanCount;
	private JTable tblPlan;
	private JScrollPane scTable;
	private DetailModel inboundDetailModel;
	private JPanel pTableNorth;

	private JPanel pPaging;
	private JButton btnPrevPage, btnNextPage;
	private JLabel laPageInfo;

	private List<IODetail> fullList;
	private int currentPage = 1;
	private final int rowsPerPage = 14;

	public DetailPage(AppMain appMain) {
		super(appMain);

		pSearch = new JPanel(new GridBagLayout());
		pSearch.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.SEARCH_BAR_HEIGHT));
		pSearch.setBackground(Color.WHITE);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 8, 5, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		tfPlanId = new JTextField(10);
		tfPlanItemId = new JTextField(10);
		tfProductCode = new JTextField(10);
		tfSupplierName = new JTextField(10);
		tfProduct = new JTextField(10);
		cbStatus = new JComboBox<>(new String[] { "전체", "예정", "진행 중", "완료", "보류" });
		chooser = new JDateChooser();
		chooser.setDateFormatString("yyyy-MM-dd");

		gbc.gridx = 0; gbc.gridy = 0; pSearch.add(new JLabel("입고예정ID"), gbc);
		gbc.gridx = 1; pSearch.add(tfPlanId, gbc);
		gbc.gridx = 2; pSearch.add(new JLabel("입고상세ID"), gbc);
		gbc.gridx = 3; pSearch.add(tfPlanItemId, gbc);
		gbc.gridx = 4; pSearch.add(new JLabel("상품코드"), gbc);
		gbc.gridx = 5; pSearch.add(tfProductCode, gbc);
		gbc.gridx = 6; pSearch.add(new JLabel("상품명"), gbc);
		gbc.gridx = 7; pSearch.add(tfProduct, gbc);

		gbc.gridy = 1; gbc.gridx = 0; pSearch.add(new JLabel("공급사명"), gbc);
		gbc.gridx = 1; pSearch.add(tfSupplierName, gbc);
		gbc.gridx = 2; pSearch.add(new JLabel("상태"), gbc);
		gbc.gridx = 3; pSearch.add(cbStatus, gbc);
		gbc.gridx = 4; pSearch.add(new JLabel("입고예정일자"), gbc);
		gbc.gridx = 5; pSearch.add(chooser, gbc);

		btnSearch = new JButton("검색");
		gbc.gridx = 7; pSearch.add(btnSearch, gbc);

		pPageName = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pPageName.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.PAGE_NAME_HEIGHT));
		laPageName = new JLabel("입고관리 > 입고상세");
		pPageName.add(laPageName);

		pTableNorth = new JPanel(new FlowLayout());
		pTableNorth.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.TABLE_NORTH_HEIGHT));
		laPlanCount = new JLabel("총 0개의 입고상세 검색");
		laPlanCount.setPreferredSize(new Dimension(Config.CONTENT_WIDTH - 200, 30));
		pTableNorth.add(laPlanCount);

		JButton btnDelete = new JButton("삭제");
		btnDelete.addActionListener(cbStatus);

		// 버튼 클릭 시 이벤트
		btnDelete.addActionListener(e -> {
			int cnt = 0;
		    List<Integer> checkedIds = inboundDetailModel.getSelectedDetailIds();
		    for (int i=0; i<checkedIds.size(); i++) {
		    	int ioDetailId = checkedIds.get(i);
				// 선택한 입고상세를 비활성화
		    	detailDAO.deactivateIoDetail(ioDetailId);
		    	cnt++;

				// 상위 입고예정 ID 찾기
				int ioReceiptId = detailDAO.findReceiptIdByDetailId(ioDetailId);

				// 위에서 입고상세를 비활성화시킨 후, 해당 상위 입고예정이 모두 비활성화됐을 경우
				if (detailDAO.areAllDetailsInactiveByReceiptId(ioDetailId)){
					// 해당 입고예정에는 더이상 입고상세가 존재하지 않으므로, 비활성화
					receiptDAO.deactivateIoReceipt(ioReceiptId);
				}
		    }
		    JOptionPane.showMessageDialog(this, cnt+"건을 삭제했습니다.");
		    currentPage = 1;
		    loadDetailData(getSearchFilters());
		});

		JPanel pRightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pRightButtons.setPreferredSize(new Dimension(160, 30));
		pRightButtons.setOpaque(false);
		pRightButtons.add(btnDelete);

		pTableNorth.add(pRightButtons);

		tblPlan = new JTable(inboundDetailModel = new DetailModel());
		tblPlan.setRowHeight(45);
		tblPlan.getColumnModel().getColumn(0).setCellEditor(tblPlan.getDefaultEditor(Boolean.class));
		tblPlan.getColumnModel().getColumn(0).setCellRenderer(tblPlan.getDefaultRenderer(Boolean.class));

		tblPlan.getColumn("수정").setCellRenderer(new ButtonRenderer());
		tblPlan.getColumn("수정").setCellEditor(new ButtonEditor(new JCheckBox(), (table, row, column) -> {
			IODetail detail = inboundDetailModel.getIODetailAt(row);
			EditDetailDialog dialog = new EditDetailDialog(appMain, detail, inboundDetailModel);
			dialog.setVisible(true);
		}));

		tblPlan.getColumn("입고").setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
			IODetail detail = inboundDetailModel.getIODetailAt(row);
			if (detail != null && detail.isProcessable()) return new JButton("검수");
			JLabel label = new JLabel("완료");
			label.setFont(label.getFont().deriveFont(Font.PLAIN));
			return label;
		});
		tblPlan.getColumn("입고").setCellEditor(new ButtonEditor(new JCheckBox(), (table, row, column) -> {
			IODetail detail = inboundDetailModel.getIODetailAt(row);
			CheckDetailDialog dialog = new CheckDetailDialog(appMain, detail, inboundDetailModel);
			dialog.setVisible(true);
		}));

		scTable = new JScrollPane(tblPlan);
		scTable.setPreferredSize(new Dimension(Config.CONTENT_WIDTH - 40, 680));

		btnPrevPage = new JButton("이전");
		btnNextPage = new JButton("다음");
		laPageInfo = new JLabel("1 / 1");

		btnPrevPage.addActionListener(e -> {
			if (currentPage > 1) {
				currentPage--;
				loadDetailData(getSearchFilters());
			}
		});

		btnNextPage.addActionListener(e -> {
			int totalPages = (int) Math.ceil(fullList.size() / (double) rowsPerPage);
			if (currentPage < totalPages) {
				currentPage++;
				loadDetailData(getSearchFilters());
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

		btnSearch.addActionListener(e -> {
			currentPage = 1;
			loadDetailData(getSearchFilters());
		});

		loadDetailData(Collections.emptyMap());
	}

	private Map<String, Object> getSearchFilters() {
		Map<String, Object> filters = new HashMap<>();
		if (!tfPlanId.getText().trim().isEmpty()) filters.put("io_receipt_id", Integer.parseInt(tfPlanId.getText().trim()));
		if (!tfPlanItemId.getText().trim().isEmpty()) filters.put("io_detail_id", Integer.parseInt(tfPlanItemId.getText().trim()));
		if (!tfProductCode.getText().trim().isEmpty()) filters.put("product_code", tfProductCode.getText().trim());
		if (!tfProduct.getText().trim().isEmpty()) filters.put("product_name", tfProduct.getText().trim());
		if (!tfSupplierName.getText().trim().isEmpty()) filters.put("supplier_name", tfSupplierName.getText().trim());
		String status = (String) cbStatus.getSelectedItem();
		if (!"전체".equals(status)) filters.put("status", status);
		if (chooser.getDate() != null) filters.put("scheduled_date", new Date(chooser.getDate().getTime()));
		return filters;
	}

	private void loadDetailData(Map<String, Object> filters) {
		inboundDetailModel.setFullData(filters);
		fullList = inboundDetailModel.getFullData();
		int totalRows = fullList.size();
		int totalPages = (int) Math.ceil(totalRows / (double) rowsPerPage);
		if (totalPages == 0) totalPages = 1;

		int start = (currentPage - 1) * rowsPerPage;
		int end = Math.min(start + rowsPerPage, totalRows);
		inboundDetailModel.setCurrentPageData(fullList.subList(start, end));

		laPlanCount.setText("총 " + totalRows + "개의 입고상세 검색");
		laPageInfo.setText(currentPage + " / " + totalPages);
		btnPrevPage.setEnabled(currentPage > 1);
		btnNextPage.setEnabled(currentPage < totalPages);
	}

	public void refreshDetailModel() {
		currentPage = 1;
		loadDetailData(Collections.emptyMap());
	}


}