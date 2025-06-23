package com.shinlogis.wms.outbound.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TextField;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.common.config.Config;
import com.shinlogis.wms.common.config.Page;
import com.toedter.calendar.JDateChooser;

public class OutboundDetailPage extends Page {
	// 페이지 명
	private JPanel p_pageTitle; // 페이지명 패널
	private JLabel la_pageTitle; // 페이지명 담을 라벨

	// 검색영역
	private JPanel p_search; // 검색 Bar

	private JTextField tf_outBoundPlanId; // 출고예정ID
	private JTextField tf_outboundDetailId;// 출고상세ID
	private JTextField tf_productCode; // 출고상품코드
	private JTextField tf_productSupplier; // 상품공급사
	private JDateChooser ch_reservatedDate; // 출고예정일
	private JTextField tf_targetStore;// 상품코드
	private JTextField tf_container; // 보관창고
	private JDateChooser ch_registeredDate; // 출고등록일
	private JComboBox<String> cb_status; // 상태 (전체/예정/처리 중/완료/보류)
	private JButton bt_search; // 검색 버튼

	/* =============목록 영역 구성 요소============= */
	private JPanel p_table;
	private JPanel p_tableNorth;// 데이터 수 라벨, 수정버튼 패널

	private JLabel la_counter;
	private JTable tb_plan; // 출고예정 목록 테이블
//	private JScrollPane sc_table;
//	private DefaultTableModel model;

	public OutboundDetailPage(AppMain app) {
		super(app);
		// 제목영역
		p_pageTitle = new JPanel(new FlowLayout(FlowLayout.LEFT));
		la_pageTitle = new JLabel("출고관리 > 출고상세");

		// 제목영역 디자인
		p_pageTitle.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.PAGE_NAME_HEIGHT));
		p_pageTitle.setBackground(Color.LIGHT_GRAY);
		p_pageTitle.add(la_pageTitle);

		// 검색 영역
		p_search = new JPanel(new GridBagLayout());// 칸을 나눠서 컴포넌트들 배치하기로 함.
		p_search.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.SEARCH_BAR_HEIGHT));
		p_search.setBackground(Color.WHITE);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 8, 5, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// 테이블(컨텐트) 영역
		p_table = new JPanel(new FlowLayout());
		tb_plan = new JTable();
		// 테이블(컨텐트영역) 디자인

		p_table.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.TABLE_HEIGHT));
		p_table.add(tb_plan);
		p_table.setBackground(Color.GRAY);

		// 출고예정ID
		gbc.gridx = 0; // (0,0)
		gbc.gridy = 0; // (0,0)
		p_search.add(new JLabel("출고예정ID"), gbc);

		tf_outBoundPlanId = new JTextField(10);
		gbc.gridx = 1; // (1,0)
		p_search.add(tf_outBoundPlanId, gbc);

		// 출고예정상세 ID
		gbc.gridx = 2;// (2,0)
		p_search.add(new JLabel("출고상세ID"), gbc);

		tf_outboundDetailId = new JTextField(10);
		gbc.gridx = 3;// (3,0)
		p_search.add(tf_outboundDetailId, gbc);

		// 출고상품코드
		gbc.gridx = 4;// (4,0)
		p_search.add(new JLabel("상품코드"), gbc);

		tf_productCode = new JTextField(10);
		gbc.gridx = 5;// (5,0)
		p_search.add(tf_productCode, gbc);

		// 공급사명
		gbc.gridx = 6;// (6,0)
		p_search.add(new JLabel("공급사명"), gbc);

		tf_productSupplier = new JTextField(10);
		gbc.gridx = 7;// (7,0)
		p_search.add(tf_productSupplier, gbc);

		// 출고예정일
		gbc.gridx = 8;// (8,0)
		p_search.add(new JLabel("출고예정일"), gbc);

		ch_reservatedDate = new JDateChooser();
		ch_reservatedDate.setDateFormatString("yyyy-MM-dd");
		// 권장 높이보다 넓어지지 않게 하려고 높이를 chooser.getPreferredSize().height로 설정
		ch_reservatedDate.setPreferredSize(new Dimension(150, ch_reservatedDate.getPreferredSize().height));
		gbc.gridx = 9;// (9,0)
		p_search.add(ch_reservatedDate, gbc);

		// 출고지점
		gbc.gridy = 1; // (0,1)
		gbc.gridx = 0; // (0,1)
		p_search.add(new JLabel("출고지점"), gbc);

		gbc.gridx = 1; // (1,1)
		tf_targetStore = new JTextField(10);
		p_search.add(tf_targetStore, gbc);

		// 보관창고
		gbc.gridx = 2;// (2,1)
		p_search.add(new JLabel("보관창고"), gbc);

		tf_container = new JTextField(10);
		gbc.gridx = 3;// (3,1)
		p_search.add(tf_container, gbc);

		// 상태
		gbc.gridx = 4;// (4,1)
		p_search.add(new JLabel("상태"), gbc);

		cb_status = new JComboBox<>(new String[] { "전체", "예정", "처리 중", "완료", "보류" });
		gbc.gridx = 5;// (5,1)
		p_search.add(cb_status, gbc);

		// 출고일
		gbc.gridx = 6;// (6,1)
		p_search.add(new JLabel("등록일"), gbc);

		ch_registeredDate = new JDateChooser();
		ch_registeredDate.setDateFormatString("yyyy-MM-dd");
		ch_registeredDate.setPreferredSize(new Dimension(150, ch_registeredDate.getPreferredSize().height));
		gbc.gridx = 7;// (7,1)
		p_search.add(ch_registeredDate, gbc);

		// 검색버튼
		bt_search = new JButton("검색");
		bt_search.addActionListener(e -> {
			System.out.println("출고상세페이지 검색버튼을 눌렀습니다. 상세를 검색하는 쿼리문이 실행될 것입니다.");
		});
		gbc.gridx = 8;// (8,1)
		p_search.add(bt_search, gbc);

		/* ===================테이블 영역================= */
		/* ================ 검색 결과 카운트, 조회 버튼 영역 ========== */
		GridBagConstraints gbcTableNorth = new GridBagConstraints();
		p_tableNorth = new JPanel(new GridBagLayout());
		p_tableNorth.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.TABLE_NORTH_HEIGHT));

		la_counter = new JLabel("총 0개의 출고예정 검색");
		gbcTableNorth.gridx = 0;
		gbcTableNorth.gridy = 0;
		gbcTableNorth.weightx = 1.0; // 남는 공간 차지
		gbcTableNorth.anchor = GridBagConstraints.WEST;
		gbcTableNorth.insets = new Insets(0, 10, 0, 10); // 좌우 여백
		p_tableNorth.add(la_counter, gbcTableNorth);
		

		// 테이블(컨텐트영역) 디자인

		p_table.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.TABLE_HEIGHT));
		p_table.add(p_tableNorth);
		p_table.add(tb_plan);
		p_table.setBackground(Color.GRAY);

		// page에 만든 파츠들 부착.
		setLayout(new FlowLayout());
		add(p_pageTitle);
		add(p_search);
		add(p_table);

	}

}
