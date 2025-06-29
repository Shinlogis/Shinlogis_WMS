package com.shinlogis.locationuser.orderList.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import com.shinlogis.locationuser.order.model.StoreOrder;
import com.shinlogis.locationuser.order.model.StoreOrderItem;
import com.shinlogis.locationuser.order.model.StoreOrderItemModel;
import com.shinlogis.locationuser.order.model.StoreOrderModel;
import com.shinlogis.locationuser.order.repository.StoreOrderDAO;
import com.shinlogis.locationuser.order.repository.StoreOrderItemDAO;
import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.common.config.Config;
import com.shinlogis.wms.common.config.Page;
import com.shinlogis.wms.product.repository.ProductDAO;
import com.toedter.calendar.JDateChooser;

public class OrderListPage extends Page{

	/* ────────── 페이지명 영역 구성 요소 ────────── */    
	private JPanel pPageName; // 페이지명 패널
	private JLabel laPageName; // 페이지명
		
	/* ────────── 검색 영역 구성 요소 ────────── */
	private JPanel pSearch; // 검색 Bar

    private JDateChooser chooser; // 달력
    private JButton btnSearch; // 검색 버튼

    /* ────────── 목록 영역 구성 요소 ────────── */

    private StoreOrderModel model;
    ProductDAO productDao = new ProductDAO();

    private JPanel pTable; //전체 영역 
    private JPanel pTable_Content; //content 영역 
    private JPanel pTable_Content_title; //content 제목 영역 
    private JLabel laContentTitle;
    
    private JPanel pTable_Content_title2; //content 제목 영역 
    private JLabel laContentTitle2;
    private StoreOrderItemModel model2;
    
    JTable table2 ;
    JScrollPane scrollPane2;
    private JButton btnOrder;  // 주문하기버튼 
    int storeOrderId=0;
    
    
   
	public OrderListPage(AppMain appMain) {
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
		
		
		 //주문일자 선택 
        gbc.gridx = 2;
        pSearch.add(new JLabel("주문일자"), gbc);
        chooser = new JDateChooser();
        chooser.setDateFormatString("yyyy-MM-dd");
        chooser.setPreferredSize(new Dimension(150, chooser.getPreferredSize().height));
        chooser.setBackground(Color.WHITE);
        gbc.gridx = 3;
        pSearch.add(chooser, gbc);
        
        // 검색 버튼
        btnSearch = new JButton("검색");
        gbc.gridx = 10;
        pSearch.add(btnSearch, gbc);
        
        /* ==== 페이지명 영역 ==== */
        pPageName = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pPageName.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.PAGE_NAME_HEIGHT));
        laPageName = new JLabel("주문하기 > 발주내역 조회");
        pPageName.add(laPageName);
        pPageName.setBackground(new Color(0xF1F1F1));
        
        //1.pTable 영역 
        pTable = new JPanel(); // 전체 
        pTable_Content = new JPanel(); //전체 > 내용 
        pTable_Content_title = new JPanel(new FlowLayout()); //전체 > 내용 > 제목 
        laContentTitle = new JLabel("주문 목록"); //전체 > 내용 > 제목(라벨)
        model = new StoreOrderModel(); 
        JTable table = new JTable(model); //전체 > 내용 > 테이블  
        JScrollPane scrollPane = new JScrollPane(table);
        
        pTable.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.TABLE_HEIGHT));
        pTable_Content.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, 580));
        //1.1 pTable 스타일영역 
        pTable_Content_title.setPreferredSize(new Dimension(800, Config.TABLE_NORTH_HEIGHT-10));
        laContentTitle.setPreferredSize(new Dimension(800, 20));
        scrollPane.setPreferredSize(new Dimension(800, 240));  
        
        pTable_Content_title.setBackground(Color.white);
        pTable_Content.setBackground(Color.WHITE);
        pTable.setBackground(new Color(0xF1F1F1));
        
        
        //정렬 적용
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        TableColumnModel columnModel = table.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setCellRenderer(centerRenderer);
        }
        
        //헤더 가운데 정렬 
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 25));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                label.setBackground(new Color(0xFF7F50)); // 배경 색상
                label.setForeground(Color.WHITE);     // 글자 색상
                label.setHorizontalAlignment(SwingConstants.CENTER); // 가운데 정렬
                label.setFont(label.getFont().deriveFont(Font.BOLD, 14f)); // 글자 크기 키우기
                return label;
            }
        });
        table.setShowGrid(true); // 격자선 보이기
        table.setShowVerticalLines(true); // 세로선 보이기
        table.setGridColor(Color.LIGHT_GRAY); // 선 색상 설정
        table.setBorder(null); // JTable 자체 테두리 제거

        // 첫 번째 열(Column 0 = 체크박스)
        table.getColumnModel().getColumn(0).setMinWidth(60);     // 최소 너비
        table.getColumnModel().getColumn(0).setMaxWidth(60);     // 최대 너비
        table.getColumnModel().getColumn(0).setPreferredWidth(60); // 선호 너비
 
        //1.2 pTable 부착 영역  
        pTable_Content_title.add(laContentTitle);
        pTable_Content.add(pTable_Content_title);
        pTable_Content.add(scrollPane,BorderLayout.CENTER);
		pTable.add(pTable_Content); 
		
		//2.pTable2 영역 
        pTable_Content_title2 = new JPanel(new FlowLayout()); //전체 > 내용 > 제목 
        laContentTitle2 = new JLabel("주문 상세 목록"); //전체 > 내용 > 제목(라벨)
        model2 = new StoreOrderItemModel(storeOrderId); 
        JTable table2 = new JTable(model2); //전체 > 내용 > 테이블 
        JScrollPane scrollPane2 = new JScrollPane(table2);
        
        //2.1 pTable2 스타일영역 
        pTable_Content_title2.setPreferredSize(new Dimension(800, Config.TABLE_NORTH_HEIGHT-10));
        laContentTitle2.setPreferredSize(new Dimension(800, 20));
        scrollPane2.setPreferredSize(new Dimension(800, 200));  
        pTable_Content_title2.setBackground(Color.white);
        table2.getColumnModel().getColumn(0).setMinWidth(60);     // 최소 너비
        table2.getColumnModel().getColumn(0).setMaxWidth(60);     // 최대 너비
        table2.getColumnModel().getColumn(0).setPreferredWidth(60); // 선호 너비
		
        //2.2 pTable2 부착 영역 
        pTable_Content_title2.add(laContentTitle2);
        pTable_Content.add(pTable_Content_title2);
        pTable_Content.add(scrollPane2,BorderLayout.CENTER);
        
        pTable.add(pTable_Content);;	
		
		setLayout(new FlowLayout());
		add(pPageName);
		add(pSearch); 
		add(pTable); //content 영역 
		setBackground(new Color(0xF1F1F1));
		
		//검색 이벤트 
		btnSearch.addActionListener(e->{
			  Date selectedDate = chooser.getDate();

			    if (selectedDate != null) {
			        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			        String formatted = sdf.format(selectedDate);
			        System.out.println("포맷된 날짜: " + formatted);

			        StoreOrderDAO dao = new StoreOrderDAO();
			        List<StoreOrder> items = dao.selectByDate(formatted);
			        model.setList(items);
			    } else {
			        JOptionPane.showMessageDialog(null, "날짜를 선택하세요.");
			    }
		});
		
		table.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        int row = table.getSelectedRow(); // 클릭된 행
		        if (row != -1) {
		            Object value = table.getValueAt(row, 0);
		            if (value != null) {
		                storeOrderId = Integer.parseInt(value.toString());

		                // DAO 호출해서 주문 상세 가져오기
		                StoreOrderItemDAO dao= new StoreOrderItemDAO();
		                List<StoreOrderItem> list = dao.select(storeOrderId);

		                model2.setList(list); 
		            }
		        }
		    }
		});
	}
	
	public void refresh() {
        List<StoreOrder> updatedList = new StoreOrderDAO().selectAll();
        model.setList(updatedList);
        model.fireTableDataChanged();
    }
}
