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
import com.shinlogis.wms.inoutbound.model.IODetail;
import com.shinlogis.wms.inoutbound.outbound.repository.OutboundDetailDAO;
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
	private JDateChooser ch_processedDate; // 출고등록일
	private JComboBox<String> cb_status; // 상태 (전체/예정/처리 중/완료/보류)
	private JButton bt_search; // 검색 버튼

	/* =============목록 영역 구성 요소============= */
	private JPanel p_table;
	private JPanel p_tableNorth;// 데이터 수 라벨, 수정버튼 패널

	private JLabel la_counter;
	private JTable tb_plan; // 출고예정 목록 테이블
	private JScrollPane sc_table;
	private AbstractTableModel model;
	private int count;
	
	OutboundDetailDAO outboundDetailDAO;
	
	public OutboundDetailPage(AppMain app) {
		super(app);
		outboundDetailDAO = new OutboundDetailDAO();
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

		cb_status = new JComboBox<>(new String[] { "전체", "예정", "진행 중", "완료", "보류" });
		gbc.gridx = 5;// (5,1)
		p_search.add(cb_status, gbc);

		// 출고일
		gbc.gridx = 6;// (6,1)
		p_search.add(new JLabel("출고일"), gbc);

		ch_processedDate = new JDateChooser();
		ch_processedDate.setDateFormatString("yyyy-MM-dd");
		ch_processedDate.setPreferredSize(new Dimension(150, ch_processedDate.getPreferredSize().height));
		gbc.gridx = 7;// (7,1)
		p_search.add(ch_processedDate, gbc);

		// 검색버튼
		bt_search = new JButton("검색");
		bt_search.addActionListener(e -> {
		    // 1. 검색 조건 수집
		    String planId = tf_outBoundPlanId.getText();
		    String detailId = tf_outboundDetailId.getText();
		    String code = tf_productCode.getText();
		    String supplier = tf_productSupplier.getText();
		    String store = tf_targetStore.getText();
		    String warehouse = tf_container.getText();
		    String status = cb_status.getSelectedItem().toString();

		    java.sql.Date reservedDate = null;
		    if (ch_reservatedDate.getDate() != null) {
		        reservedDate = new java.sql.Date(ch_reservatedDate.getDate().getTime());
		    }

		    java.sql.Date processedDate = null;
		    if (ch_processedDate.getDate() != null) {
		        processedDate = new java.sql.Date(ch_processedDate.getDate().getTime());
		    }

		    // 2. 검색 실행
		    List<IODetail> result = outboundDetailDAO.selectByCondition(
		        planId, detailId, code, supplier, reservedDate, store, warehouse, status, processedDate
		    );

		    // 3. 테이블 모델 교체
		    tb_plan.setModel(new OutboundDetailModel(result));

		    tb_plan.getColumn("수정").setCellRenderer(new ButtonRenderer());
		    tb_plan.getColumn("수정").setCellEditor(
		        new ButtonEditor(new JCheckBox(), (table, row, col) -> {
		        	//클릭시 동작도 다시 재 정의 해주기
		            int ioReceiptId = Integer.parseInt(table.getValueAt(row, 0).toString());
		            new StoreOrderDialog();
		        })
		    );

		    // 5. 카운터 갱신
		    la_counter.setText("총 " + result.size() + "개의 출고상세 검색");
		});



		gbc.gridx = 8;// (8,1)
		p_search.add(bt_search, gbc);

		/* ===================테이블 영역================= */
		/* ================ 검색 결과 카운트, 조회 버튼 영역 ========== */
		GridBagConstraints gbcTableNorth = new GridBagConstraints();
		p_tableNorth = new JPanel(new GridBagLayout());
		p_tableNorth.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.TABLE_NORTH_HEIGHT));

		
		
		
		
		tb_plan = new JTable(model = new OutboundDetailModel());
		tb_plan.setRowHeight(45);
		
		tb_plan.getColumn("수정").setCellRenderer(new ButtonRenderer());
		tb_plan.getColumn("수정").setCellEditor(
		    new ButtonEditor(new JCheckBox(), (table, row, column) -> {
		        // 상세보기 클릭 시 동작 정의
		        int ioReceiptId = Integer.parseInt(table.getValueAt(row, 0).toString());
		        new StoreOrderDialog();
		    })
		); 


		
		sc_table= new JScrollPane(tb_plan);
		sc_table.setPreferredSize(new Dimension(1150,660));
		
		count = outboundDetailDAO.countTotal();
		la_counter = new JLabel("총 "+ count + "개의 출고상세 검색");
		gbcTableNorth.gridx = 0;
		gbcTableNorth.gridy = 0;
		gbcTableNorth.weightx = 1.0; // 남는 공간 차지
		gbcTableNorth.anchor = GridBagConstraints.WEST;
		gbcTableNorth.insets = new Insets(0, 10, 0, 10); // 좌우 여백
		p_tableNorth.add(la_counter, gbcTableNorth);
		


		// page에 만든 파츠들 부착.
		setLayout(new FlowLayout());
		add(p_pageTitle);
		add(p_search);
		add(p_tableNorth);
		add(sc_table);

	}
	
	//출고예정에서 넘어오면 강제로 값을 입력받아서 출력해주기 위한 메서드
	public void searchByPlanId(String planId) {
	    tf_outBoundPlanId.setText(planId); // 텍스트 필드 설정
	    bt_search.doClick(); // 검색 버튼 강제 클릭 (이벤트 실행됨)
	}

}
