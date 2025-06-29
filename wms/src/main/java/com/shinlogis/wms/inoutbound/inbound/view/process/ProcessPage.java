package com.shinlogis.wms.inoutbound.inbound.view.process;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.common.config.Config;
import com.shinlogis.wms.common.config.Page;
import com.shinlogis.wms.damagedCode.repository.DamagedCodeDAO;
import com.shinlogis.wms.inoutbound.inbound.repository.ReceiptDAO;
import com.shinlogis.wms.inoutbound.model.IODetail;
import com.toedter.calendar.JDateChooser;

public class ProcessPage extends Page {
	/* ────────── 페이지명 영역 구성 요소 ────────── */
	private JPanel pPageName; 
	private JLabel laPageName;

	/* ────────── 검색 영역 구성 요소 ────────── */
	private JPanel pSearch; 

	private JTextField tfDetailId; 
	private JDateChooser chooser; 
	private JTextField tfProductName; 
	private JComboBox<String> cbStatus; 
	private JTextField tfUser; 
	private JButton btnSearch; 

	/* ────────── 목록 영역 구성 요소 ────────── */
	private JPanel pTable;

	private JLabel laPlanCount;
	private JTable tblPlan; 
	private JScrollPane scTable;
	private ProcessModel iModel;

	private JPanel pTableNorth;
	private JButton btnRegister; 

	/* ────────── 페이징 영역 구성 요소 ────────── */
	private JPanel pPaging;
	private JButton btnPrevPage, btnNextPage;
	private JLabel laPageInfo;
	private int currentPage = 1;
	private final int rowsPerPage = 10;
	private List<IODetail> fullList;

	public ProcessPage(AppMain appMain) {
		super(appMain);

		ReceiptDAO ioReceiptDAO = new ReceiptDAO();
		DamagedCodeDAO damagedCodeDAO = new DamagedCodeDAO();

		// 검색 영역 세팅
		pSearch = new JPanel(new GridBagLayout()); 
		pSearch.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.SEARCH_BAR_HEIGHT));
		pSearch.setBackground(Color.WHITE);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 8, 5, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// 입고상세ID
		gbc.gridx = 0; 
		gbc.gridy = 0; 
		pSearch.add(new JLabel("입고상세ID"), gbc); 

		tfDetailId = new JTextField(10);
		gbc.gridx = 1; 
		pSearch.add(tfDetailId, gbc);

		// 입고처리일자
		gbc.gridx = 2;
		pSearch.add(new JLabel("입고처리일"), gbc);
		chooser = new JDateChooser();
		chooser.setDateFormatString("yyyy-MM-dd");
		chooser.setPreferredSize(new Dimension(150, chooser.getPreferredSize().height));
		
		gbc.gridx = 3;
		pSearch.add(chooser, gbc);

		// 상품명
		gbc.gridx = 4;
		pSearch.add(new JLabel("상품명"), gbc);
		tfProductName = new JTextField(10);
		gbc.gridx = 5;
		pSearch.add(tfProductName, gbc);

		// 파손코드
		gbc.gridx = 6;
		pSearch.add(new JLabel("파손코드"), gbc);
		List<String> codeName = damagedCodeDAO.selectAllNames();
		codeName.add(0, "전체");  
		String[] codeArray = codeName.toArray(new String[0]); 
		cbStatus = new JComboBox<>(codeArray);
		gbc.gridx = 7;
		pSearch.add(cbStatus, gbc);

		// 담당자
		gbc.gridx = 8;
		pSearch.add(new JLabel("담당자"), gbc);
		tfUser = new JTextField(10);
		gbc.gridx = 9;
		pSearch.add(tfUser, gbc);

		// 검색 버튼
		btnSearch = new JButton("검색");
		gbc.gridx = 10;
		pSearch.add(btnSearch, gbc);

		btnSearch.addActionListener(e -> {
			currentPage = 1;
			loadData(getSearchFilters());
		});

		// 페이지명 영역
		pPageName = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pPageName.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.PAGE_NAME_HEIGHT));
		laPageName = new JLabel("입고관리 > 입고처리");
		pPageName.add(laPageName);

		// 검색 결과 카운트 영역
		pTableNorth = new JPanel(new FlowLayout());
		pTableNorth.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.TABLE_NORTH_HEIGHT));

		laPlanCount = new JLabel("총 0개의 입고처리 검색");
		laPlanCount.setPreferredSize(new Dimension(Config.CONTENT_WIDTH - 150, 30));
		pTableNorth.add(laPlanCount);

		// 테이블 영역
		pTable = new JPanel(new FlowLayout());
		iModel = new ProcessModel();
		tblPlan = new JTable(iModel);
		tblPlan.setRowHeight(45);

		scTable = new JScrollPane(tblPlan);
		scTable.setPreferredSize(new Dimension(Config.CONTENT_WIDTH - 40, 680)); 

		pTable.add(pTableNorth);
		pTable.add(scTable);

		pTable.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.TABLE_HEIGHT));
		pTable.setBackground(Color.WHITE);

		// 페이징 버튼 및 라벨
		pPaging = new JPanel(new FlowLayout());
		pPaging.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, 40));
		btnPrevPage = new JButton("이전");
		btnNextPage = new JButton("다음");
		laPageInfo = new JLabel("1 / 1");

		btnPrevPage.setEnabled(false);
		btnNextPage.setEnabled(false);

		btnPrevPage.addActionListener(e -> {
			if (currentPage > 1) {
				currentPage--;
				loadData(getSearchFilters());
			}
		});

		btnNextPage.addActionListener(e -> {
			int totalPages = (int) Math.ceil(fullList.size() / (double) rowsPerPage);
			if (currentPage < totalPages) {
				currentPage++;
				loadData(getSearchFilters());
			}
		});

		pPaging.add(btnPrevPage);
		pPaging.add(laPageInfo);
		pPaging.add(btnNextPage);

		// 레이아웃 배치
		setLayout(new FlowLayout());
		add(pPageName);
		add(pSearch);
		add(pTable);
		add(pPaging);

		setBackground(Color.LIGHT_GRAY);

		// 초기 데이터 로드
		loadData(new HashMap<>());
	}

	private Map<String, Object> getSearchFilters() {
		Map<String, Object> filters = new HashMap<>();
		if (!tfDetailId.getText().trim().isEmpty())
			filters.put("io_detail_id", Integer.parseInt(tfDetailId.getText().trim()));
		if (chooser.getDate() != null)
			filters.put("processed_date", new java.sql.Date(chooser.getDate().getTime()));
		if (!tfProductName.getText().trim().isEmpty())
			filters.put("product_name", tfProductName.getText().trim());

		String status = (String) cbStatus.getSelectedItem();
		if (!"전체".equals(status))
			filters.put("damage_code_name", status);

		if (!tfUser.getText().trim().isEmpty())
			filters.put("user_name", tfUser.getText().trim());

		return filters;
	}

	private void loadData(Map<String, Object> filters) {
		// 전체 데이터 조회 후 페이징 처리
		fullList = iModel.loadFullData(filters); // ProcessModel에 전체 데이터 조회 메서드 필요
		int totalRows = fullList.size();
		int totalPages = (int) Math.ceil(totalRows / (double) rowsPerPage);
		if (totalPages == 0) totalPages = 1;

		// 현재 페이지 데이터 부분만 잘라서 모델에 세팅
		int start = (currentPage - 1) * rowsPerPage;
		int end = Math.min(start + rowsPerPage, totalRows);
		List<IODetail> currentPageList = fullList.subList(start, end);
		iModel.setData(currentPageList);


		laPlanCount.setText("총 " + totalRows + "개의 입고처리 검색");
		laPageInfo.setText(currentPage + " / " + totalPages);
		btnPrevPage.setEnabled(currentPage > 1);
		btnNextPage.setEnabled(currentPage < totalPages);
	}
}
