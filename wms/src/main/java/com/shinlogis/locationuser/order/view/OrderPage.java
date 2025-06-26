package com.shinlogis.locationuser.order.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import com.shinlogis.locationuser.order.model.OrderModel;
import com.shinlogis.locationuser.order.model.StoreOrder;
import com.shinlogis.locationuser.order.model.StoreOrderItem;
import com.shinlogis.locationuser.order.repository.StoreOrderDAO;
import com.shinlogis.locationuser.order.repository.StoreOrderItemDAO;
import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.common.Exception.OrderInsertException;
import com.shinlogis.wms.common.config.Config;
import com.shinlogis.wms.common.config.Page;
import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.product.model.Product;
import com.shinlogis.wms.product.repository.ProductDAO;

public class OrderPage extends Page{

	/* ────────── 페이지명 영역 구성 요소 ────────── */    
	private JPanel pPageName; // 페이지명 패널
	private JLabel laPageName; // 페이지명
		
	/* ────────── 검색 영역 구성 요소 ────────── */
    private JPanel pSearch; // 검색 Bar
    private JTextField tfProduct;       // 상품명  
    private JButton btnSearch; // 검색 버튼

    /* ────────── 목록 영역 구성 요소 ────────── */
    
    private JLabel laContentTitle;

    private OrderModel model;
    ProductDAO productDao = new ProductDAO();

    private JPanel pTable; //content 영역 
    private JPanel pTable_Content_title; //content 제목 영역 
    private JPanel pTable_Content; //content 내용 영역 
    private JButton btnOrder;  // 주문하기버튼 
    DBManager dbManager=DBManager.getInstance();
   
	public OrderPage(AppMain appMain) {
		super(appMain);
		
		
		/* ==== 검색 영역 ==== */
		pSearch = new JPanel(new GridBagLayout()); // GridBagLayout: 칸(그리드)를 바탕으로 컴포넌트를 배치
		pSearch.setPreferredSize(new Dimension(700, Config.SEARCH_BAR_HEIGHT));
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
        pPageName.setBackground(new Color(0xF1F1F1));
        
        
        
        //1.pTable 영역 
        pTable = new JPanel(); // 전체 
        pTable_Content = new JPanel(); //전체 > 내용 
        pTable_Content_title = new JPanel(new FlowLayout()); //전체 > 내용 > 제목 
        laContentTitle = new JLabel("총 몇개의 상품");  //전체 > 내용 > 제목(라벨)
        model = new OrderModel();
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table); //전체 > 내용 > 테이블 
        JPanel btnPanel = new JPanel();  //전체 > 내용 > 버튼 
        btnOrder = new JButton("주문하기");
        
        //1-1.pTable 스타일 영역 
        // 첫 열 마지막 열 제외하고 정렬 적용
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        TableColumnModel columnModel = table.getColumnModel();
        for (int i = 1; i < columnModel.getColumnCount() - 1; i++) {
            columnModel.getColumn(i).setCellRenderer(centerRenderer);
        }
        
        //헤더 가운데 정렬 
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        // 첫 번째 열(Column 0 = 체크박스)
        table.getColumnModel().getColumn(0).setMinWidth(60);     // 최소 너비
        table.getColumnModel().getColumn(0).setMaxWidth(60);     // 최대 너비
        table.getColumnModel().getColumn(0).setPreferredWidth(60); // 선호 너비
        table.getColumnModel().getColumn(3).setMinWidth(100);     // 최소 너비
        table.getColumnModel().getColumn(3).setMaxWidth(100);     // 최대 너비
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // 선호 너비

        pTable.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.TABLE_HEIGHT));
        pTable_Content.setPreferredSize(new Dimension(700, 500));
        pTable_Content_title.setPreferredSize(new Dimension(600, Config.TABLE_NORTH_HEIGHT));
        laContentTitle.setPreferredSize(new Dimension(600, 30));
        scrollPane.setPreferredSize(new Dimension(600, 350));  
        btnPanel.setPreferredSize(new Dimension(500, 80)); 
        btnOrder.setPreferredSize(new Dimension(100, 50));
        
        pTable_Content_title.setBackground(Color.white);
        pTable_Content.setBackground(Color.WHITE);
        btnPanel.setBackground(Color.white);
        pTable.setBackground(new Color(0xF1F1F1));
        
        //1.2 pTable 부착 영역 
        pTable_Content_title.add(laContentTitle);
        pTable_Content.add(pTable_Content_title);
        pTable_Content.add(scrollPane,BorderLayout.CENTER);
        btnPanel.add(btnOrder);
        pTable_Content.add(btnPanel);
    
        //pTable에 부착 
		pTable.add(pTable_Content); 
	
		setLayout(new FlowLayout());
		add(pPageName);
		add(pSearch); 
		add(pTable); //content 영역 

		setBackground(new Color(0xF1F1F1));
		
		//검색 이벤트 
		btnSearch.addActionListener(e->{
			List<Product> p=productDao.selectSearchProduct(tfProduct.getText());
			model.setList(p);
			pTable.updateUI();
		});
	
		//주문하기 이벤트 
		btnOrder.addActionListener(e -> {
			// 전체 상품 리스트 중 체크만 된 것
			List<Product> checkedProducts = model.getAllProducts().stream()
			    .filter(Product::isChecked)
			    .toList();

			if (checkedProducts.isEmpty()) {
			    JOptionPane optionPane = new JOptionPane(
					    "상품을 선택하세요.",
					    JOptionPane.WARNING_MESSAGE
					);
			    JDialog dialog = optionPane.createDialog("입력 오류");
				dialog.setLocation(715, 320); // 원하는 좌표
				dialog.setVisible(true);
			    return;
			}

			// 체크는 했지만 수량을 입력하지 않은 상품
			List<Product> checkedZero = checkedProducts.stream()
			    .filter(p -> p.getQuantity() <= 0)
			    .toList();

			if (!checkedZero.isEmpty()) {
				JOptionPane optionPane = new JOptionPane(
						"체크한 상품 중\n수량이 입력되지 않은 항목이 있습니다.",
					    JOptionPane.WARNING_MESSAGE
					);
				JDialog dialog = optionPane.createDialog("입력 오류");
				dialog.setLocation(680, 320); // 원하는 좌표
				dialog.setVisible(true);
			    return;
			}
		
			if(isConfirmed()) {
				Connection con=dbManager.getConnection();
				try {
					con.setAutoCommit(false);//start
					StoreOrder storeOrder =new StoreOrder();
					StoreOrderDAO storeOrderDao = new StoreOrderDAO();
					StoreOrderItemDAO storeItemDao= new StoreOrderItemDAO();
					
					storeOrder=model.getStoreOrder(appMain.locationUser.getLocation().getLocationId());
					
					try {
						storeOrderDao.insert(storeOrder);
						int pk=storeOrderDao.getRecentId();
						storeOrder.setStoreOrderId(pk);
						
						
						
						for (StoreOrderItem item : storeOrder.getItems()) {
						    item.setStoreOrderId(pk);  
						    storeItemDao.insert(item);
						    item.setStoreOrderId(pk); 
						}
						
						con.commit();
						
					} catch (OrderInsertException e1) {
						e1.printStackTrace();
						con.rollback();
						JOptionPane.showMessageDialog(this, e1.getMessage());
					} 
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					try {
						con.rollback();
					} catch (SQLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				}finally {
					try {
						con.setAutoCommit(true);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}//start
				}
				
				
			}
			
			//List<Product> p=productDao.selectOrderProduct();
		
			pTable.updateUI();    
			tfProduct.setText("");
		});			
	}
	
	//주문확인 
	public boolean isConfirmed() {
		 OrderConfirmDialog dialog = new OrderConfirmDialog(null, model.getSelectedProducts());
		 return dialog.isConfirmed();
    }
}
