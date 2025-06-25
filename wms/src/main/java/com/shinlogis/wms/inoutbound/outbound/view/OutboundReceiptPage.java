package com.shinlogis.wms.inoutbound.outbound.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.common.config.Config;
import com.shinlogis.wms.common.config.Page;
import com.shinlogis.wms.inoutbound.outbound.repository.OutboundReceiptDAO;
import com.toedter.calendar.JDateChooser;

/**
 * 출고예정 페이지
 * 
 * @author 이세형
 */
public class OutboundReceiptPage extends Page {
	/* ================= 페이지 목록 영역 ================ */
	private JPanel p_pageTitle; // 페이지명 패널
	private JLabel la_pageTitle; // 페이지명 담을 라벨

	/* ================= 검색 영역 ================ */
	private JPanel p_search; // 검색 Bar

	private JTextField tf_outboundPlanId; // 출고예정ID
	private JTextField tf_outboundProduct; // 출고품목
	private JTextField tf_targetStore;// 출고지점
	private JDateChooser ch_reservatedDate; // 출고예정일
	private JDateChooser ch_registeredDate; // 출고예정 등록일
	private JComboBox<String> cb_status; // 상태 (전체/예정/처리 중/완료/보류)
	private JButton bt_search; // 검색 버튼

	/* ================= 목록 영역 ================ */
	private JPanel p_tableNorth;//데이터 수 라벨, 조회, 등록버튼 패널
	private JPanel p_tableSouth;//데이터 수 라벨, 조회, 등록버튼 패널
	private JPanel buttonPanel;//버튼 두개 붙일 패널

	private JLabel la_counter;
	private JButton bt_regist;// 출고예정 등록버튼
	private JButton bt_confirm;// 출고신청 조회
	private JTable tb_plan; // 출고예정 목록 테이블
	private JScrollPane sc_table;
	private AbstractTableModel model;
//	private InboundPlanItemModel inboundPlanItemModel;
	OutboundReceiptDAO outboundReceiptDAO;
	
	public OutboundReceiptPage(AppMain appMain) {
		
		super(appMain);
		outboundReceiptDAO = new OutboundReceiptDAO();
		/* ===================제목 영역================= */
		p_pageTitle = new JPanel(new FlowLayout(FlowLayout.LEFT));
		la_pageTitle = new JLabel("출고관리 > 출고예정");

		// 제목영역 디자인
		p_pageTitle.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.PAGE_NAME_HEIGHT));
		p_pageTitle.setBackground(Color.YELLOW);
		p_pageTitle.add(la_pageTitle);

		/* ===================검색 영역================= */
		p_search = new JPanel(new GridBagLayout());// 칸을 나눠서 컴포넌트들 배치하기로 함.

		p_search.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.SEARCH_BAR_HEIGHT));
		p_search.setBackground(Color.WHITE);
		GridBagConstraints gbcSearch = new GridBagConstraints();
		gbcSearch.insets = new Insets(5, 8, 5, 8);
		gbcSearch.fill = GridBagConstraints.HORIZONTAL;

		// 출고예정ID
		gbcSearch.gridx = 0; // (0,0)
		gbcSearch.gridy = 0; // (0,0)
		p_search.add(new JLabel("출고예정ID"), gbcSearch);

		tf_outboundPlanId = new JTextField(10);
		gbcSearch.gridx = 1; // (1,0)
		p_search.add(tf_outboundPlanId, gbcSearch);

		// 출고품목
		gbcSearch.gridx = 2;// (2,0)
		p_search.add(new JLabel("출고품목"), gbcSearch);

		tf_outboundProduct = new JTextField(10);
		gbcSearch.gridx = 3; // (3,0)
		p_search.add(tf_outboundProduct, gbcSearch);

		// 출고지점
		gbcSearch.gridx = 4;// (4,0)
		p_search.add(new JLabel("출고지점"), gbcSearch);

		tf_targetStore = new JTextField(10);
		gbcSearch.gridx = 5;// (5,0)
		p_search.add(tf_targetStore, gbcSearch);

		// 출고 예정일
		gbcSearch.gridx = 6;// (6,0)
		p_search.add(new JLabel("출고예정일"), gbcSearch);

		ch_reservatedDate = new JDateChooser();
		ch_reservatedDate.setDateFormatString("yyyy-MM-dd");// mm은 '분'을 의미
		// 권장 높이보다 넓어지지 않게 하려고 높이를 chooser.getPreferredSize().height로 설정
		ch_reservatedDate.setPreferredSize(new Dimension(150, ch_reservatedDate.getPreferredSize().height));
		gbcSearch.gridx = 7;// (7,0)
		p_search.add(ch_reservatedDate, gbcSearch);

		// 상태
		gbcSearch.gridy = 1; // (0,1)
		gbcSearch.gridx = 0;// (0,1)
		p_search.add(new JLabel("상태"), gbcSearch);

		cb_status = new JComboBox<>(new String[] { "전체", "예정", "처리 중", "완료", "보류" });
		gbcSearch.gridx = 1;// (1,1)
		p_search.add(cb_status, gbcSearch);

		// 등록날짜
		gbcSearch.gridx = 2;// (2,1)
		p_search.add(new JLabel("등록일"), gbcSearch);

		ch_registeredDate = new JDateChooser();
		ch_registeredDate.setDateFormatString("yyyy-MM-dd");
		ch_registeredDate.setPreferredSize(new Dimension(150, ch_registeredDate.getPreferredSize().height));
		gbcSearch.gridx = 3;// (3,1)
		p_search.add(ch_registeredDate, gbcSearch);

		// 검색버튼
		bt_search = new JButton("검색");
		bt_search.addActionListener(e -> {
			System.out.println("출고예정페이지 검색버튼을 눌렀습니다. 예정을 검색하는 쿼리문이 실행될 것입니다.");
		});
		gbcSearch.gridx = 8;// (7,1)
		p_search.add(bt_search, gbcSearch);
		
		

		/* ===================테이블 콘텐트 영역================= */

		
		/* ================ 검색 결과 카운트, 등록 및 조회 버튼 영역 ========== */
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
		
		// 버튼을 묶어서 하나의 panel에 담고 그 panel에 FlowLayout적용
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0)); // 버튼
		bt_confirm = new JButton("지점 주문 조회");
		bt_regist = new JButton("출고 예정 등록");
		buttonPanel.add(bt_confirm);
		buttonPanel.add(bt_regist);

		gbcTableNorth.gridx = 1;
		gbcTableNorth.weightx = 0; // 공간 차지 X
		gbcTableNorth.anchor = GridBagConstraints.EAST;
		p_tableNorth.add(buttonPanel, gbcTableNorth);


		
		//등록버튼에 다이얼로그 불러오는 이벤트연결
		bt_regist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new OutboundRegisterDialog();
			}
		});
		
		/* ===================테이블 영역================= */
		p_tableSouth = new JPanel();		
		p_tableSouth.setBackground(Color.GREEN);
		p_tableSouth.setPreferredSize(new Dimension(Config.CONTENT_WIDTH,Config.TABLE_HEIGHT ));
		
		//테이블 디자인
				
		tb_plan = new JTable(model = new OutboundReceiptModel());
		tb_plan.setRowHeight(45);
		 
		sc_table= new JScrollPane(tb_plan);
		sc_table.setPreferredSize(new Dimension(1150,660));
		
		// 테이블(컨텐트영역) 디자인

//		p_table.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.TABLE_HEIGHT));
//		p_table.setBackground(Color.GRAY);
//		p_table.add(p_tableSouth);
//		p_table.add(sc_table);

		// page에 만든 파츠들 부착.
		setLayout(new FlowLayout());
		add(p_pageTitle);
		add(p_search);
		add(p_tableNorth);
//		add(tb_plan);
		add(sc_table);
//		add(p_table);

	}

}
