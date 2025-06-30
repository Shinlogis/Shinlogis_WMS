package com.shinlogis.wms.inoutbound.outbound.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.common.config.ButtonEditor;
import com.shinlogis.wms.common.config.ButtonRenderer;
import com.shinlogis.wms.common.config.Config;
import com.shinlogis.wms.common.config.Page;
import com.shinlogis.wms.inoutbound.outbound.model.OrderDetail;
import com.shinlogis.wms.inoutbound.outbound.repository.OrderDAO;
import com.shinlogis.wms.inoutbound.outbound.repository.OrderDetailDAO;
import com.toedter.calendar.JDateChooser;

public class OutboundRegisterPage extends Page {
	/* ================= 페이지 목록 영역 ================ */
	private JPanel p_pageTitle; // 페이지명 패널
	private JLabel la_pageTitle; // 페이지명 담을 라벨

	/* ================= 검색 영역 ================ */
	private JPanel p_search; // 검색 Bar

	private JTextField tf_productId; // 상품ID
	private JTextField tf_outboundProduct; // 출고품목
	private JTextField tf_targetStore;// 출고지점
	private JDateChooser ch_reservatedDate; // 출고예정일
	private JDateChooser ch_registeredDate; // 출고예정 등록일
	private JComboBox<String> cb_status; // 상태 (전체/예정/처리 중/완료/보류)
	private JButton bt_search; // 검색 버튼

	/* ================= 목록 영역 ================ */
	private JPanel p_tableNorth;// 데이터 수 라벨, 조회, 등록버튼 패널
	private JPanel p_detail;
	private JLabel la_detail;

	private JLabel la_counter;
	private JTable tb_order; // 출고예정 목록 테이블
	private JTable tb_detail; // 출고예정 목록 테이블
	private JScrollPane sc_table;
	private JScrollPane sc_detailTable;
	private AbstractTableModel orderModel;
	private AbstractTableModel orderDetailModel;
//	private InboundPlanItemModel inboundPlanItemModel;
	private int count;
	private int storeOrderId = 0;
	AppMain appMain;
	

	public OutboundRegisterPage(AppMain appMain) {
		super(appMain);
		this.appMain = appMain;
		/* ===================제목 영역================= */
		p_pageTitle = new JPanel(new FlowLayout(FlowLayout.LEFT));
		la_pageTitle = new JLabel("출고관리 > 주문 조회 및 등록");

		// 제목영역 디자인
		p_pageTitle.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.PAGE_NAME_HEIGHT));
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
		p_search.add(new JLabel("상품ID"), gbcSearch);

		tf_productId = new JTextField(10);
		gbcSearch.gridx = 1; // (1,0)
		p_search.add(tf_productId, gbcSearch);

		// 출고품목
		gbcSearch.gridx = 2;// (2,0)
		p_search.add(new JLabel("상품명"), gbcSearch);

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

		cb_status = new JComboBox<>(new String[] { "전체", "예정", "완료" });
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
			System.out.println("주문내역 검색버튼을 눌렀습니다. 예정을 검색하는 쿼리문이 실행될 것입니다.");
		});
		gbcSearch.gridx = 8;// (7,1)
		p_search.add(bt_search, gbcSearch);

		/* ===================테이블 콘텐트 영역================= */

		/* ================ 검색 결과 카운트, 등록 및 조회 버튼 영역 ========== */
		GridBagConstraints gbcTableNorth = new GridBagConstraints();
		p_tableNorth = new JPanel(new GridBagLayout());
		p_tableNorth.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.TABLE_NORTH_HEIGHT));

		// count 변수에 출고예정을 담아줌
		OrderDAO orderDAO = new OrderDAO();
		count = orderDAO.countTotal();
		la_counter = new JLabel("총 " + count + "개의 주문내역 검색");
		gbcTableNorth.gridx = 0;
		gbcTableNorth.gridy = 0;
		gbcTableNorth.weightx = 1.0; // 남는 공간 차지
		gbcTableNorth.anchor = GridBagConstraints.WEST;
		gbcTableNorth.insets = new Insets(0, 10, 0, 10); // 좌우 여백
		p_tableNorth.add(la_counter, gbcTableNorth);

		/* ===================테이블 영역================= */

		// 테이블 디자인
		tb_order = new JTable(orderModel = new OrderModel());
		tb_order.setRowHeight(30);
		
		//주문상세 버튼 생성 및 클릭 이벤트 연결
	    tb_order.getColumn("주문 상세").setCellRenderer(new ButtonRenderer());
		tb_order.getColumn("주문 상세").setCellEditor(new ButtonEditor(new JCheckBox(), (table, row, column) -> {
		    // 해당 row의 첫 번째 컬럼 (storeOrderId 혹은 ioReceiptId 등) 값 가져오기
		    int selectedOrderId = Integer.parseInt(table.getValueAt(row, 0).toString());
//		    System.out.println("orderId가 " + selectedOrderId + "인 버튼이 눌렸습니다.");

		    // DAO로부터 새로운 상세 리스트 가져오기
		    OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
		    List<OrderDetail> newList = orderDetailDAO.select(selectedOrderId);

		    // 모델에 갱신된 리스트 주입 → 테이블 자동 리프레시
		    ((OrderDetailModel) orderDetailModel).setList(newList);
		}));
		
		
		
		//출고등록 버튼 생성 및 클릭 이벤트 연결
	    tb_order.getColumn("출고 등록").setCellRenderer(new ButtonRenderer());
		tb_order.getColumn("출고 등록").setCellEditor(new ButtonEditor(new JCheckBox(), (table, row, column) -> {
		    int selectedOrderId = Integer.parseInt(table.getValueAt(row, 0).toString());
		    OutboundRegisterDialog ourboundRegisterDialog = new OutboundRegisterDialog(appMain);

		}));
		
		tb_detail = new JTable(orderDetailModel = new OrderDetailModel(storeOrderId));
		tb_detail.setRowHeight(30);

		sc_table = new JScrollPane(tb_order);
		sc_table.setPreferredSize(new Dimension(1150, 300));

		GridBagConstraints gbcDetailTable = new GridBagConstraints();
		p_detail = new JPanel(new GridBagLayout());
		p_detail.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.TABLE_NORTH_HEIGHT));

		la_detail = new JLabel("해당 주문 상세보기");
		gbcDetailTable.gridx = 0;
		gbcDetailTable.gridy = 0;
		gbcDetailTable.weightx = 1.0; // 남는 공간 차지
		gbcDetailTable.anchor = GridBagConstraints.WEST;
		gbcDetailTable.insets = new Insets(0, 10, 0, 10); // 좌우 여백
		p_detail.add(la_detail, gbcDetailTable);

		// 상세보기 페이지

		sc_detailTable = new JScrollPane(tb_detail);
		sc_detailTable.setPreferredSize(new Dimension(1150, 300));
		
		// page에 만든 파츠들 부착.
		setLayout(new FlowLayout());
		add(p_pageTitle);
		add(p_search);
		add(p_tableNorth);
//		add(tb_plan);
		add(sc_table);
		add(p_detail);
		add(sc_detailTable);
//		add(p_table);

	}

}
