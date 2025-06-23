package com.shinlogis.locationuser.order.view;

import java.awt.BorderLayout;
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

import com.shinlogis.locationuser.order.model.OrderModel;
import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.common.config.Config;
import com.shinlogis.wms.common.config.Page;
import com.toedter.calendar.JDateChooser;

public class OrderPage extends Page{

	/* ────────── 페이지명 영역 구성 요소 ────────── */    
	private JPanel pPageName; // 페이지명 패널
	private JLabel laPageName; // 페이지명
		
	/* ────────── 검색 영역 구성 요소 ────────── */
    private JPanel pSearch; // 검색 Bar
    private JTextField tfProduct;       // 상품명  
    private JButton btnSearch; // 검색 버튼

    /* ────────── 목록 영역 구성 요소 ────────── */
    
    private JLabel laPlanCount;
    private JTable tblPlan; // 입고예정 목록 테이블
    private JScrollPane scTable;
    private DefaultTableModel model;     

    private JPanel pTable; //content 영역 
    private JPanel pTableNorth; //content 제목 영역 
    private JPanel pTableCenter; //content 내용 영역 
    private JButton btnOrder;  // 주문하기버튼 
   
    
	public OrderPage(AppMain appMain) {
		super(appMain);
		
		/* ==== 검색 영역 ==== */
		pSearch = new JPanel(new GridBagLayout()); // GridBagLayout: 칸(그리드)를 바탕으로 컴포넌트를 배치
		pSearch.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.SEARCH_BAR_HEIGHT));
		pSearch.setBackground(Color.WHITE);
		
		// GridBagConstraints: 이 컴포넌트를 그리드에 어떻게 배치할지 설정하는 규칙 객체
		// GridBagLayout에 컴포넌트를 add()할 때마다 그 컴포넌트의 위치, 크기, 여백, 정렬 방법 등을 담은 설정값을 같이 넘김
		GridBagConstraints gbc = new GridBagConstraints();		
		gbc.insets = new Insets(5, 8, 5, 8); // 컴포넌트 주변 여백 설정
		gbc.fill = GridBagConstraints.HORIZONTAL; // 셀 안에서 공간을 채우는 방식 설정. HORIZONTAL: 가로방향으로 셀을 꽉 채우기 (JtextField의 너비가 셀만큼 쭉 늘어남) 
		

        // 상품명
        gbc.gridx = 8;
        pSearch.add(new JLabel("상품명"), gbc);
        tfProduct = new JTextField(10);
        gbc.gridx = 9;
        pSearch.add(tfProduct, gbc);

        // 검색 버튼
        btnSearch = new JButton("검색");
        gbc.gridx = 10;
        pSearch.add(btnSearch, gbc);
        
        /* ==== 페이지명 영역 ==== */
        pPageName = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pPageName.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.PAGE_NAME_HEIGHT));
        laPageName = new JLabel("주문하기 > 물품신청");
        pPageName.add(laPageName);
        
        
        pTableNorth = new JPanel(new FlowLayout());
        pTableNorth.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.TABLE_NORTH_HEIGHT));
        
        laPlanCount = new JLabel("총 몇개의 상품");
        laPlanCount.setPreferredSize(new Dimension(Config.CONTENT_WIDTH-150, 30));
        pTableNorth.add(laPlanCount);
        
        //content 내용 영역 
        pTableCenter = new JPanel(new FlowLayout());
        pTableCenter.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.TABLE_HEIGHT-Config.TABLE_NORTH_HEIGHT));
        pTable = new JPanel(new FlowLayout()); // FlowLayout: 컴포넌트를 좌에서 우로 순서대로, 한 줄에 배치하는 레이아웃 매니저
        OrderModel model = new OrderModel();
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(350, 150));  // 원하는 크기 설정
        btnOrder = new JButton("주문하기");
        
        pTableCenter.add(scrollPane);
        pTableCenter.add(btnOrder);
        pTableCenter.setBackground(Color.PINK);
   

//	 	tblPlan = new JTable(model);
//		scTable = new JScrollPane(tblPlan);
//		scTable.setPreferredSize(new Dimension()); 
		
		pTable.add(pTableNorth,BorderLayout.NORTH); //content 영역에  
		pTable.add(pTableCenter); //content 영역에  
		//pTable.add(scTable);
       // pTable.add(pTableSouth);
        
		pTable.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.TABLE_HEIGHT));
		pTable.setBackground(Color.red);
		
		/* ==== 레이아웃 배치 ==== */
		setLayout(new FlowLayout());
		add(pPageName);
		add(pSearch); 
		add(pTable); //content 영역 
		
		setBackground(Color.LIGHT_GRAY);
				
	}
}
