package com.shinlogis.wms.inoutbound.inbound.view.detail;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
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
import com.shinlogis.wms.inoutbound.model.IODetail;
import com.toedter.calendar.JDateChooser;

/**
 * 입고예정상세 페이지입니다.
 * 
 * @author 김예진
 */
public class DetailPage extends Page {
	/* ────────── 페이지명 영역 구성 요소 ────────── */
	private JPanel pPageName; // 페이지명 패널
	private JLabel laPageName; // 페이지명

	/* ────────── 검색 영역 구성 요소 ────────── */
	private JPanel pSearch; // 검색 Bar

	private JTextField tfPlanId; // 입고예정ID
	private JTextField tfPlanItemId; // 입고예정상세ID
	private JTextField tfProductCode; // 상품코드
	private JDateChooser chooser; // 달력
	private JTextField tfSupplierName; // 공급사명
	private JComboBox<String> cbStatus; // 상태 (예정/처리 중/완료)
	private JTextField tfProduct; // 상품명
	private JButton btnSearch; // 검색 버튼

	/* ────────── 목록 영역 구성 요소 ────────── */
	private JPanel pTable;

	private JLabel laPlanCount;
	private JTable tblPlan; // 입고예정 목록 테이블
	private JScrollPane scTable;
	private DefaultTableModel model;
	private DetailModel inboundDetailModel;

	private JPanel pTableNorth;

	public DetailPage(AppMain appMain) {
		super(appMain);

		/* ==== 검색 영역 ==== */
		pSearch = new JPanel(new GridBagLayout()); // GridBagLayout: 칸(그리드)를 바탕으로 컴포넌트를 배치
		pSearch.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.SEARCH_BAR_HEIGHT));
		pSearch.setBackground(Color.WHITE);

		// GridBagConstraints: 이 컴포넌트를 그리드에 어떻게 배치할지 설정하는 규칙 객체
		// GridBagLayout에 컴포넌트를 add()할 때마다 그 컴포넌트의 위치, 크기, 여백, 정렬 방법 등을 담은 설정값을 같이 넘김
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 8, 5, 8); // 컴포넌트 주변 여백 설정
		gbc.fill = GridBagConstraints.HORIZONTAL; // 셀 안에서 공간을 채우는 방식 설정. HORIZONTAL: 가로방향으로 셀을 꽉 채우기 (JtextField의 너비가
													// 셀만큼 쭉 늘어남)

		// 입고예정ID
		gbc.gridx = 0; // 열(column) 0번
		gbc.gridy = 0; // 행(row) 0번
		pSearch.add(new JLabel("입고예정ID"), gbc); // Grid 좌표 (0, 0)에 입고예정ID 라벨 추가

		tfPlanId = new JTextField(10);
		gbc.gridx = 1; // 열 1번 (행은 바꾸지 않았으므로 여기서도 0)
		pSearch.add(tfPlanId, gbc);

		// 입고예정 상세ID
		gbc.gridx = 2;
		pSearch.add(new JLabel("입고예정상세ID"), gbc);
		tfPlanItemId = new JTextField(10);
		gbc.gridx = 3;
		pSearch.add(tfPlanItemId, gbc);

		// 상품코드
		gbc.gridx = 4;
		pSearch.add(new JLabel("상품코드"), gbc);
		tfProductCode = new JTextField(10);
		gbc.gridx = 5;
		pSearch.add(tfProductCode, gbc);

		// 상품명
		gbc.gridx = 6;
		pSearch.add(new JLabel("상품명"), gbc);
		tfProduct = new JTextField(10);
		gbc.gridx = 7;
		pSearch.add(tfProduct, gbc);

		// 공급사명
		gbc.gridy = 1;
		gbc.gridx = 0;
		pSearch.add(new JLabel("공급사명"), gbc);
		tfSupplierName = new JTextField(10);
		gbc.gridx = 1;
		pSearch.add(tfSupplierName, gbc);

		// 상태
		gbc.gridx = 2;
		pSearch.add(new JLabel("상태"), gbc);
		cbStatus = new JComboBox<>(new String[] { "전체", "예정", "진행 중", "완료", "보류" });
		gbc.gridx = 3;
		pSearch.add(cbStatus, gbc);

		// 입고예정일자
		gbc.gridx = 4;
		pSearch.add(new JLabel("입고예정일자"), gbc);
		chooser = new JDateChooser();
		chooser.setDateFormatString("yyyy-MM-dd");
		chooser.setPreferredSize(new Dimension(150, chooser.getPreferredSize().height)); // 권장 높이보다 넓어지지 않게 하려고 높이를
																							// chooser.getPreferredSize().height로
																							// 설정
		gbc.gridx = 5;
		pSearch.add(chooser, gbc);

		// 검색 버튼
		btnSearch = new JButton("검색");
		// 버튼 클릭 시 입력한 검색어를 조건으로 select
		btnSearch.addActionListener(e -> {
			// 검색 필터를 저장
			Map<String, Object> filters = new HashMap<>();
			if (!tfPlanId.getText().trim().isEmpty())
				filters.put("io_receipt_id", Integer.parseInt(tfPlanId.getText().trim()));

			if (!tfPlanItemId.getText().trim().isEmpty())
				filters.put("io_detail_id", Integer.parseInt(tfPlanItemId.getText().trim()));

			if (!tfProductCode.getText().trim().isEmpty())
				filters.put("product_code", tfProductCode.getText().trim());

			if (!tfProduct.getText().trim().isEmpty())
				filters.put("product_name", tfProduct.getText().trim());

			if (!tfSupplierName.getText().trim().isEmpty())
				filters.put("supplier_name", tfSupplierName.getText().trim());

			String status = (String) cbStatus.getSelectedItem();
			if (!"전체".equals(status))
				filters.put("status", status);

			if (chooser.getDate() != null)
				filters.put("scheduled_date", new java.sql.Date(chooser.getDate().getTime()));

			// 모델에 필터 적용
			inboundDetailModel.setData(filters);
			laPlanCount.setText("총 " + inboundDetailModel.getRowCount() + "개의 입고상세 검색");
		});
		gbc.gridx = 7;
		pSearch.add(btnSearch, gbc);

		/* ==== 페이지명 영역 ==== */
		pPageName = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pPageName.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.PAGE_NAME_HEIGHT));
		laPageName = new JLabel("입고관리 > 입고상세");
		pPageName.add(laPageName);

		/* ==== 검색 결과 카운트, 등록 버튼 영역 ==== */
		pTableNorth = new JPanel(new FlowLayout());
		pTableNorth.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.TABLE_NORTH_HEIGHT));

		laPlanCount = new JLabel("총 0개의 입고상세 검색");
		laPlanCount.setPreferredSize(new Dimension(Config.CONTENT_WIDTH - 150, 30));
		pTableNorth.add(laPlanCount);

		/* ==== 테이블 영역 ==== */
		pTable = new JPanel(new FlowLayout()); // FlowLayout: 컴포넌트를 좌에서 우로 순서대로, 한 줄에 배치하는 레이아웃 매니저

		tblPlan = new JTable(inboundDetailModel = new DetailModel());
		// JTable에 버튼을 setCellRenderer로 그리고 setCellEditor로 기능 추가
		// 수정 버튼
		tblPlan.getColumn("수정").setCellRenderer(new ButtonRenderer());
		tblPlan.getColumn("수정").setCellEditor(new ButtonEditor(new JCheckBox(), (table, row, column) -> {
			IODetail detail = inboundDetailModel.getIODetailAt(row);
			EditDetailDialog dialog = new EditDetailDialog(appMain, detail, inboundDetailModel);
			dialog.setVisible(true);
		}));

		// 입고처리 검수 버튼
		tblPlan.getColumn("입고처리").setCellRenderer(new ButtonRenderer());
		tblPlan.getColumn("입고처리").setCellEditor(new ButtonEditor(new JCheckBox(), (table, row, column) -> {
			IODetail detail = inboundDetailModel.getIODetailAt(row);
			CheckDetailDialog dialog = new CheckDetailDialog(appMain, detail, inboundDetailModel);
			dialog.setVisible(true);
		}));

		scTable = new JScrollPane(tblPlan);
		scTable.setPreferredSize(new Dimension(Config.CONTENT_WIDTH - 40, Config.TABLE_HEIGHT - 60));

		pTable.add(laPlanCount);
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
