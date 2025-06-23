package com.shinlogis.wms.inOutBound.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

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
/**
* 출고예정 페이지
*	@author 이세형
*/
public class OutboundReceiptPage extends Page {
	// 페이지 명
	private JPanel p_pageTitle; // 페이지명 패널
	private JLabel la_pageTitle; // 페이지명 담을 라벨

	// 검색영역
	private JPanel p_search; // 검색 Bar

	private JTextField tf_outBoundPlanId; // 출고예정ID
	private JTextField tf_outBoundProduct; //출고상품ID 
	private JTextField tf_targetStore;// 상품코드
	private JDateChooser ch_reservatedDate; // 달력
	private JDateChooser ch_registeredDate; // 달력
	private JTextField tf_supplierName; // 공급사명
	private JComboBox<String> cb_status; // 상태 (예정/처리 중/완료)
	private JTextField tf_product; // 상품명
	private JButton bt_search; // 검색 버튼


	/* ────────── 목록 영역 구성 요소 ────────── */
	private JPanel p_table;

	private JLabel la_planCount;
	private JTable tb_plan; // 입고예정 목록 테이블
	private JScrollPane sc_table;
	private DefaultTableModel model;
	private InboundPlanItemModel inboundPlanItemModel;

	public OutboundReceiptPage(AppMain app) {

		super(app);
		//제목영역
		p_pageTitle = new JPanel(new FlowLayout(FlowLayout.LEFT));
		la_pageTitle = new JLabel("출고관리 > 출고예정");
		//제목영역 디자인
		p_pageTitle.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.PAGE_NAME_HEIGHT));
		p_pageTitle.setBackground(Color.YELLOW);
		p_pageTitle.add(la_pageTitle);
		
		//검색 영역
		p_search = new JPanel(new GridBagLayout());//칸을 나눠서 컴포넌트들 배치하기로 함.
		p_search.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.SEARCH_BAR_HEIGHT));
		p_search.setBackground(Color.WHITE);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 8, 5, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		//출고예정ID
		gbc.gridx = 0;	//(0,0)
		gbc.gridy = 0; //(0,0)
		p_search.add(new JLabel("출고예정ID"), gbc);
		
		tf_outBoundPlanId = new JTextField(10);
		gbc.gridx = 1; //(1,0)
		p_search.add(tf_outBoundPlanId, gbc);
		
		
		//출고품목
		gbc.gridx = 2;//(2,0)
		p_search.add(new JLabel("출고품목"), gbc);
		
		tf_outBoundProduct = new JTextField(10);
		gbc.gridx = 3; //(3,0)
		p_search.add(tf_outBoundProduct, gbc);
		
		
		//출고지점
		gbc.gridx = 4;//(4,0)
		p_search.add(new JLabel("출고지점"), gbc);
	
		tf_targetStore = new JTextField(10);
		gbc.gridx = 5;//(5,0)
		p_search.add(tf_targetStore, gbc);
		
		//출고 예정일
		gbc.gridx = 6;//(6,0)
		p_search.add(new JLabel("출고예정일"), gbc);
		
		ch_reservatedDate = new JDateChooser();
		ch_reservatedDate.setDateFormatString("yyyy-MM-dd");//mm은 '분'을 의미
		ch_reservatedDate.setPreferredSize( new Dimension(150, ch_reservatedDate.getPreferredSize().height));
		gbc.gridx = 7;//(7,0)
		p_search.add(ch_reservatedDate, gbc);
		
		//상태
		gbc.gridy = 1; //(0,1)
		gbc.gridx = 0;//(0,1)
		p_search.add(new JLabel("상태"),gbc);
		
		cb_status = new JComboBox<>(new String[] {"전체", "예정", "진행 중", "완료", "보류"});
		gbc.gridx = 1;//(1,1)
		p_search.add(cb_status, gbc);		
		
		//등록날짜
		gbc.gridx = 2;//(2,1)
		p_search.add(new JLabel("등록일"),gbc);
		
		ch_registeredDate = new JDateChooser();
		ch_registeredDate.setDateFormatString("yyyy-MM-dd");
		ch_registeredDate.setPreferredSize(new Dimension(150,ch_registeredDate.getPreferredSize().height));
		gbc.gridx = 3;//(3,1)		
		p_search.add(ch_registeredDate, gbc);
		
		//검색버튼
		bt_search = new JButton("검색");
		bt_search.addActionListener(e ->{
			System.out.println("출고예정페이지 검색버튼을 눌렀습니다. 검색하는 쿼리문이 실행될 것입니다.");
		});
		gbc.gridx = 9;//(7,1)
		p_search.add(bt_search, gbc);
		
		
		
		
		
		
		
		
		
		
		
		//테이블(컨텐트) 영역
		p_table = new JPanel(new FlowLayout());
		tb_plan = new JTable();
		//테이블(컨텐트영역) 디자인
		
		p_table.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.TABLE_HEIGHT));
		p_table.add(tb_plan);
		p_table.setBackground(Color.GRAY);
		
		
		
		
		
		//검색영역 디자인
		p_search.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.SEARCH_BAR_HEIGHT));
		
		
		
		
		
		
		//page에 만든 파츠들 부착.
		setLayout(new FlowLayout());
		add(p_pageTitle);
		add(p_search);
		add(p_table);
		
		
	}

}
