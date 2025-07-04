package com.shinlogis.wms.headquarters.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.common.config.ButtonEditor;
import com.shinlogis.wms.common.config.ButtonRenderer;
import com.shinlogis.wms.common.config.Config;
import com.shinlogis.wms.common.config.Page;
import com.shinlogis.wms.headquarters.model.HeadquartersUser;
import com.shinlogis.wms.headquarters.repository.HeadquartersDAO;

public class MemberManagement extends Page {

	// 페이지 명
	private JPanel pPageName;
	private JLabel laPageName;

	// 검색
	private JPanel p_search; // 검색 Bar
	private JTextField t_name;
	private JButton bt_search;

	// 검색 결과
	public JPanel p_table;
	private JLabel laPlanCount; // 공급사 몇개 나올지
	private JTable table;
	private JScrollPane scroll;
	private HeadquaterModel model;
	private JPanel p_tableNorth;
	private JPanel p_btNorth;
	private JButton bt_delete;
	
	private HeadquartersDAO headquartersDAO;

	public MemberManagement(AppMain appMain) {
		super(appMain);
		
		headquartersDAO = new HeadquartersDAO();

		/* ==== 페이지 명 영역 ==== */
		pPageName = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pPageName.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.PAGE_NAME_HEIGHT));
		laPageName = new JLabel("회원관리");
		pPageName.add(laPageName);

		/* ==== 검색 영역 ==== */
		p_search = new JPanel(new GridBagLayout()); // GridBagLayout: 칸(그리드)를 바탕으로 컴포넌트를 배치
		p_search.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.SEARCH_BAR_HEIGHT));
		p_search.setBackground(Color.WHITE);

		// GridBagConstraints: 이 컴포넌트를 그리드에 어떻게 배치할지 설정하는 규칙 객체
		// GridBagLayout에 컴포넌트를 add()할 때마다 그 컴포넌트의 위치, 크기, 여백, 정렬 방법 등을 담은 설정값을 같이 넘김
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 8, 5, 8); // 컴포넌트 주변 여백 설정
		gbc.fill = GridBagConstraints.HORIZONTAL; // 셀 안에서 공간을 채우는 방식 설정. HORIZONTAL: 가로방향으로 셀을 꽉 채우기 (JtextField의 너비가
													// 셀만큼 쭉 늘어남)

		// 검색이름
		gbc.gridx = 8;
		p_search.add(new JLabel("회원ID"), gbc);
		t_name = new JTextField(10);
		gbc.gridx = 9;
		p_search.add(t_name, gbc);

		// 검색 버튼
		bt_search = new JButton("검색");
		gbc.gridx = 10;
		p_search.add(bt_search, gbc);

		/* ==== 검색 결과 카운트 영역 ==== */
		p_tableNorth = new JPanel(new FlowLayout());
		p_tableNorth.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.TABLE_NORTH_HEIGHT));
		laPlanCount = new JLabel("총 n개의 회원 검색");
		laPlanCount.setPreferredSize(new Dimension(Config.CONTENT_WIDTH - 150, 30));
		p_tableNorth.add(laPlanCount);

		model = new HeadquaterModel();

		table = new JTable(model);
		table.setRowHeight(45);
		// 컬럼 헤더 가운데 정렬
		DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) table.getTableHeader()
				.getDefaultRenderer();
		headerRenderer.setHorizontalAlignment(JLabel.CENTER);

		// 컬럼 너비 조정
		table.getColumnModel().getColumn(0).setPreferredWidth(20); // 체크박스 (선택)
		table.getColumnModel().getColumn(1).setPreferredWidth(40); // 번호
		table.getColumnModel().getColumn(2).setPreferredWidth(200); // 공급사명
		table.getColumnModel().getColumn(3).setPreferredWidth(400); // 주소
		table.getColumnModel().getColumn(4).setPreferredWidth(40); // 수정

		table.getTableHeader().setPreferredSize(new Dimension(Config.CONTENT_WIDTH - 20, 45));
		table.setPreferredScrollableViewportSize(new Dimension(Config.CONTENT_WIDTH - 20, 495));

		// 번호 왼쪽 정렬(이거 안하면 오른쪽 정렬 됨)
		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		table.getColumnModel().getColumn(1).setCellRenderer(leftRenderer); // 번호 열 = 인덱스 1

		// JTable에 버튼을 setCellRenderer로 그리고 setCellEditor로 기능 추가
		// 수정 버튼
		table.getColumn("수정").setCellRenderer(new ButtonRenderer());
		table.getColumn("수정").setCellEditor(new ButtonEditor(new JCheckBox(), (table, row, column) -> {
			HeadquartersUser headquartersUser = model.getHeaquaterAt(row);
			new EditHeadquaterDialog(appMain, headquartersUser, model);
		}));

		scroll = new JScrollPane(table);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // 세로 스크롤 비활성화
		scroll.setPreferredSize(new Dimension(Config.CONTENT_WIDTH - 20, 495));

		p_btNorth = new JPanel();
		bt_delete = new JButton("삭제");
		p_btNorth.add(bt_delete);
		p_btNorth.setOpaque(false);

		p_table = new JPanel(new FlowLayout());
		p_table.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.TABLE_HEIGHT)); // 785
		p_table.setBackground(Color.WHITE);
		p_table.add(p_tableNorth); // 40
		p_table.add(p_btNorth, BorderLayout.NORTH);
		p_table.add(scroll); // 660
		

		// 스타일
		setBackground(Color.LIGHT_GRAY);

		// 조립
		setLayout(new FlowLayout());
		add(pPageName);
		add(p_search);
		add(p_table);
		
		
		//이벤트
		bt_delete.addActionListener(e->{
			delete();
		});
		
		bt_search.addActionListener(e -> {
			search();
		});

		count();

	}
	
	
	public void delete() {
		model.deleteHeaquater();
	}
	
	public void search() {
		
		HeadquartersUser headquartersUser = new HeadquartersUser();
		headquartersUser.setId(t_name.getText().trim());
		
		List<HeadquartersUser> user = headquartersDAO.searchHeadquater(headquartersUser);
		
		if(user.size() < 1) {
			JOptionPane.showMessageDialog(this, "회원명이 존재하지 않습니다.");
		}else {
			model.list = user;
			table.updateUI();
			laPlanCount.setText("총 " + user.size() + " 명의 회원 검색");
		}
	}
	
	//회원 수 카운트
	public void count() {
		List<HeadquartersUser> count = headquartersDAO.headquaterList();
		laPlanCount.setText("총 " + count.size() + " 명의 회원 검색");
	}
			
			
			
			
			
}
