

package com.shinlogis.wms.inbound.view.process;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
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
import com.shinlogis.wms.inbound.repository.ReceiptDAO;
import com.toedter.calendar.JDateChooser;

/**
 * 입고예정 전표 페이지입니다.
 * 
 * @author 김예진
 */
public class ProcessPage extends Page {
	/* ────────── 페이지명 영역 구성 요소 ────────── */
	private JPanel pPageName; // 페이지명 패널
	private JLabel laPageName; // 페이지명

	/* ────────── 검색 영역 구성 요소 ────────── */
	private JPanel pSearch; // 검색 Bar

	private JTextField tfDetailId; // 입고상세ID
	private JDateChooser chooser; // 달력
	private JTextField tfProductName; // 공급사명
	private JComboBox<String> cbStatus; // 상태 (예정/처리 중/완료)
	private JTextField tfUser; // 상품명
	private JButton btnSearch; // 검색 버튼

	/* ────────── 목록 영역 구성 요소 ────────── */
	private JPanel pTable;

	private JLabel laPlanCount;
	private JTable tblPlan; // 입고예정 목록 테이블
	private JScrollPane scTable;
	private ProcessModel iModel;

	private JPanel pTableNorth;
	private JButton btnRegister; // 입고예정등록

	public ProcessPage(AppMain appMain) {
		super(appMain);

		ReceiptDAO ioReceiptDAO = new ReceiptDAO();

		/* ==== 검색 영역 ==== */
		pSearch = new JPanel(new GridBagLayout()); // GridBagLayout: 칸(그리드)를 바탕으로 컴포넌트를 배치
		pSearch.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.SEARCH_BAR_HEIGHT));
		pSearch.setBackground(Color.WHITE);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 8, 5, 8); // 컴포넌트 주변 여백 설정
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

		// 상태
		gbc.gridx = 6;
		pSearch.add(new JLabel("파손코드"), gbc);
		cbStatus = new JComboBox<>(new String[] { "전체", "예정", "진행 중", "완료", "보류" });
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
		// 검색 버튼 클릭 시 입력한 검색어를 조건으로 select
		btnSearch.addActionListener(e -> {
			// 검색 필터를 저장
			Map<String, Object> filters = new HashMap<>();
			if (!tfDetailId.getText().trim().isEmpty())
				filters.put("io_detail_id", Integer.parseInt(tfDetailId.getText().trim()));
			if (chooser.getDate() != null)
				filters.put("processed_date", new java.sql.Date(chooser.getDate().getTime()));
			if (!tfProductName.getText().trim().isEmpty())
				filters.put("product_name", tfProductName.getText().trim());
			String status = (String) cbStatus.getSelectedItem();
			if (!"전체".equals(status))
				filters.put("status", status);
			// TODO: 유저 추가
//			if (!tfUser.getText().trim().isEmpty())
//				filters.put("product_name", tfUser.getText().trim());

			// 모델에 필터 적용
			iModel.setData(filters);
			laPlanCount.setText("총 " + iModel.getRowCount() + "개의 입고처리 검색");
		});

		gbc.gridx = 10;
		pSearch.add(btnSearch, gbc);

		/* ==== 페이지명 영역 ==== */
		pPageName = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pPageName.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.PAGE_NAME_HEIGHT));
		laPageName = new JLabel("입고관리 > 입고처리");
		pPageName.add(laPageName);

		/* ==== 검색 결과 카운트, 등록 버튼 영역 ==== */
		pTableNorth = new JPanel(new FlowLayout());
		pTableNorth.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.TABLE_NORTH_HEIGHT));

		laPlanCount = new JLabel("총 0개의 입고처리 검색");
		laPlanCount.setPreferredSize(new Dimension(Config.CONTENT_WIDTH - 150, 30));
		pTableNorth.add(laPlanCount);

		/* ==== 테이블 영역 ==== */
		pTable = new JPanel(new FlowLayout()); // FlowLayout: 컴포넌트를 좌에서 우로 순서대로, 한 줄에 배치하는 레이아웃 매니저

		tblPlan = new JTable(iModel = new ProcessModel());
		// 상세보기 버튼
//		tblPlan.getColumn("상세보기").setCellRenderer(new ButtonRenderer());
//		tblPlan.getColumn("상세보기").setCellEditor(new ButtonEditor(new JCheckBox(), (table, row, column) -> {
//			IODetail detail = iModel.getIODetailAt(row);
//			EditInboundDetailDialog dialog = new EditInboundDetailDialog(appMain, receipt);
//			dialog.setVisible(true);
//		}));

		scTable = new JScrollPane(tblPlan);
		scTable.setPreferredSize(new Dimension(Config.CONTENT_WIDTH - 40, Config.TABLE_HEIGHT - 60));

		pTable.add(laPlanCount);

		pTable.add(pTableNorth);
		pTable.add(scTable);

		pTable.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.TABLE_HEIGHT));
		pTable.setBackground(Color.WHITE);

		/* ==== 레이아웃 배치 ==== */
		setLayout(new FlowLayout());
		add(pPageName);
		add(pSearch);
		add(pTable);

		setBackground(Color.LIGHT_GRAY);

	}
}
