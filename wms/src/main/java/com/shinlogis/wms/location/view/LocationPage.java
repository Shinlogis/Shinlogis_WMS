package com.shinlogis.wms.location.view;

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
import javax.swing.JTextField;
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
import com.shinlogis.wms.location.model.LocationModel;
import com.shinlogis.wms.product.repository.ProductDAO;
import com.toedter.calendar.JDateChooser;

public class LocationPage extends Page{

		
	/* ────────── 검색 영역 구성 요소 ────────── */
	private JPanel pSearch; // 검색 Bar
    private JTextField tf;
    private JButton btnSearch; // 검색 버튼
    
    private JPanel pSearch2; // 검색 Bar
    private JTextField tf2;
    private JButton btnSearch2; // 검색 버튼

    /* ────────── 목록 영역 구성 요소 ────────── */

    private LocationModel model;
  

    private JPanel pTable; //전체 영역 
    private JPanel pTable_Content; //content 영역 
    private JPanel pTable_Content_title; //content 제목 영역 
    private JLabel laContentTitle; 
    
    private JPanel pTable2; //전체 영역 
    private JPanel pTable_Content2; //content 영역 
    private JPanel pTable_Content_title2; //content 제목 영역 
    private JLabel laContentTitle2; 
    
    
	public LocationPage(AppMain appMain) {
		super(appMain);
			
		/* ==== 검색 영역 ==== */
		pSearch = new JPanel(new GridBagLayout()); // GridBagLayout: 칸(그리드)를 바탕으로 컴포넌트를 배치
		pSearch.setPreferredSize(new Dimension(Config.CONTENT_WIDTH/2, Config.SEARCH_BAR_HEIGHT));
		pSearch.setBackground(Color.WHITE);
		
		pSearch2 = new JPanel(new GridBagLayout()); // GridBagLayout: 칸(그리드)를 바탕으로 컴포넌트를 배치
		pSearch2.setPreferredSize(new Dimension(Config.CONTENT_WIDTH/2, Config.SEARCH_BAR_HEIGHT));
		pSearch2.setBackground(Color.WHITE);
		
		GridBagConstraints gbc = new GridBagConstraints();		
		gbc.insets = new Insets(5, 8, 5, 8); // 컴포넌트 주변 여백 설정
		gbc.fill = GridBagConstraints.HORIZONTAL; // 셀 안에서 공간을 채우는 방식 설정. HORIZONTAL: 가로방향으로 셀을 꽉 채우기 (JtextField의 너비가 셀만큼 쭉 늘어남) 
		
		 //검색 선택 
        gbc.gridx = 2;
        pSearch.add(new JLabel("지점검색"), gbc);
        tf = new JTextField();
        tf.setPreferredSize(new Dimension(150, tf.getPreferredSize().height));
        tf.setBackground(Color.WHITE);
        gbc.gridx = 3;
        pSearch.add(tf, gbc);
        
        gbc.gridx = 2;
        pSearch2.add(new JLabel("지점검색"), gbc);
        tf2 = new JTextField();
        tf2.setPreferredSize(new Dimension(150, tf2.getPreferredSize().height));
        tf2.setBackground(Color.WHITE);
        gbc.gridx = 3;
        pSearch2.add(tf2, gbc);
        
        // 검색 버튼
        btnSearch = new JButton("검색");
        gbc.gridx = 10;
        pSearch.add(btnSearch, gbc);
        
        btnSearch2=new JButton("검색");
        gbc.gridx = 10;
        pSearch2.add(btnSearch2, gbc);
        
        //1.pTable 영역 
        pTable = new JPanel(); // 전체 
        pTable_Content = new JPanel(); //전체 > 내용 
        pTable_Content_title = new JPanel(new FlowLayout()); //전체 > 내용 > 제목 
        laContentTitle = new JLabel("지점 목록"); //전체 > 내용 > 제목(라벨)
        model = new LocationModel(); 
        JTable table = new JTable(model); //전체 > 내용 > 테이블  
        JScrollPane scrollPane = new JScrollPane(table);
        
 
        pTable.setPreferredSize(new Dimension(Config.CONTENT_WIDTH/2, 580));
        pTable_Content.setPreferredSize(new Dimension(Config.CONTENT_WIDTH/2, 580));
        pTable_Content_title.setPreferredSize(new Dimension(800/2, Config.TABLE_NORTH_HEIGHT-10));
        laContentTitle.setPreferredSize(new Dimension(800/2, 20));
        scrollPane.setPreferredSize(new Dimension(800/2, 240));  
        pTable_Content_title.setBackground(Color.WHITE);
        pTable_Content.setBackground(Color.WHITE);
        pTable.setBackground(new Color(0xF1F1F1));
       
        pTable2 = new JPanel(); // 전체 
        pTable_Content2 = new JPanel(); //전체 > 내용 
        pTable_Content_title2 = new JPanel(new FlowLayout()); //전체 > 내용 > 제목 
        laContentTitle2 = new JLabel("주문 목록"); //전체 > 내용 > 제목(라벨)
        
        pTable2.setPreferredSize(new Dimension(Config.CONTENT_WIDTH/2, 580));
        pTable_Content2.setPreferredSize(new Dimension(Config.CONTENT_WIDTH/2, 580));
        pTable_Content_title2.setPreferredSize(new Dimension(800/2, Config.TABLE_NORTH_HEIGHT-10));
        laContentTitle2.setPreferredSize(new Dimension(800/2, 20));
        pTable_Content_title2.setBackground(Color.white);
        pTable_Content2.setBackground(Color.WHITE);
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
        table.getColumnModel().getColumn(3).setMinWidth(80);     // 최소 너비
        table.getColumnModel().getColumn(3).setMaxWidth(80);     // 최대 너비
        table.getColumnModel().getColumn(3).setPreferredWidth(80); // 선호 너비
 
        // 부착 영역  
        pTable_Content_title.add(laContentTitle);
        pTable_Content.add(pTable_Content_title);
        pTable_Content.add(scrollPane,BorderLayout.CENTER);
		pTable.add(pTable_Content); 
		
		setLayout(new FlowLayout());
		add(pSearch);
		add(pSearch2);
		add(pTable); //content 영역 
		add(pTable2);
		setBackground(new Color(0xF1F1F1));
		
		//검색 이벤트 
		btnSearch.addActionListener(e->{
			  String search = tf.getText();
		});
	}
	
	public void refresh() {
        List<StoreOrder> updatedList = new StoreOrderDAO().selectAll();
      //  model.setList(updatedList);
      //  model.fireTableDataChanged();
    }
}
