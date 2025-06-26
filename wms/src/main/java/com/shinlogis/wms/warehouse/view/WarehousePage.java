package com.shinlogis.wms.warehouse.view;

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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.common.config.ButtonEditor;
import com.shinlogis.wms.common.config.ButtonRenderer;
import com.shinlogis.wms.common.config.Config;
import com.shinlogis.wms.common.config.Page;
import com.shinlogis.wms.inventory.view.InventoryPage;
import com.shinlogis.wms.warehouse.model.Warehouse;
import com.shinlogis.wms.warehouse.repository.WarehouseDAO;

public class WarehousePage extends Page {

    private JPanel pPageName;
    private JLabel laPageName;

    private JPanel pSearch;
    private JTextField tfCode, tfName, tfLocation;
    public JButton btnSearch;

    private JPanel pTable;
    private JLabel laCount;
    private JTable table;
    private JScrollPane scrollPane;
    private DefaultTableModel model;
    private JPanel pTableNorth;

    private JPanel pPaging;
    private JButton btnPrevPage, btnNextPage;
    private JLabel laPageInfo;

    private String[] columnNames = { "창고코드", "창고명", "위치", "재고보기" };

    private int currentPage = 1;
    private final int rowsPerPage = 10;
    private List<Warehouse> fullList;

    private AppMain appMain;
    
    public WarehousePage(AppMain appMain) {
        super(appMain);

        this.appMain = appMain;
        
        /* ==== 검색 영역 ==== */
        pSearch = new JPanel(new GridBagLayout());
        pSearch.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.SEARCH_BAR_HEIGHT));
        pSearch.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int col = 0;

        gbc.gridx = col++;
        pSearch.add(new JLabel("창고코드"), gbc);
        tfCode = new JTextField(10);
        gbc.gridx = col++;
        pSearch.add(tfCode, gbc);

        gbc.gridx = col++;
        pSearch.add(new JLabel("창고명"), gbc);
        tfName = new JTextField(10);
        gbc.gridx = col++;
        pSearch.add(tfName, gbc);

        gbc.gridx = col++;
        pSearch.add(new JLabel("위치"), gbc);
        tfLocation = new JTextField(10);
        gbc.gridx = col++;
        pSearch.add(tfLocation, gbc);

        btnSearch = new JButton("검색");
        gbc.gridx = col;
        pSearch.add(btnSearch, gbc);

        /* ==== 페이지명 ==== */
        pPageName = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pPageName.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.PAGE_NAME_HEIGHT));
        laPageName = new JLabel("창고관리");
        pPageName.add(laPageName);

        /* ==== 테이블 상단 카운트 ==== */
        pTableNorth = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pTableNorth.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.TABLE_NORTH_HEIGHT));
        laCount = new JLabel("총 0개의 창고 검색");
        pTableNorth.add(laCount);

        /* ==== 테이블 ==== */
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                int invenCol = getColumnIndex("재고보기");
                return column == invenCol;
            }
        };

        table = new JTable(model);
        table.setRowHeight(45);
        table.getTableHeader().setPreferredSize(new Dimension(Config.CONTENT_WIDTH - 20, 45));
        table.getColumn("재고보기").setCellRenderer(new ButtonRenderer());
        table.getColumn("재고보기").setCellEditor(new ButtonEditor(new JCheckBox(), (tbl, row, column) -> {
            String warehouseCode = (String) tbl.getValueAt(row, 0); // 첫 번째 열이 창고코드
            System.out.println("선택된 창고코드: " + warehouseCode);
            showInventoryPage(warehouseCode);
        }));
        
        scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(Config.CONTENT_WIDTH - 20, 495));

        /* ==== 페이징 ==== */
        pPaging = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pPaging.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, 40));
        btnPrevPage = new JButton("이전");
        btnNextPage = new JButton("다음");
        laPageInfo = new JLabel("1 / 1");
        pPaging.add(btnPrevPage);
        pPaging.add(laPageInfo);
        pPaging.add(btnNextPage);

        /* ==== 테이블 포함 영역 ==== */
        pTable = new JPanel(new FlowLayout());
        pTable.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.TABLE_HEIGHT));
        pTable.setBackground(Color.WHITE);
        pTable.add(pTableNorth);
        pTable.add(scrollPane);
        pTable.add(pPaging);

        /* ==== 최종 배치 ==== */
        setLayout(new FlowLayout());
        add(pPageName);
        add(pSearch);
        add(pTable);
        setBackground(Color.LIGHT_GRAY);

        // 이벤트
        btnSearch.addActionListener(e -> {
            currentPage = 1;
            loadWarehouseData();
        });

        btnPrevPage.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                loadWarehouseData();
            }
        });

        btnNextPage.addActionListener(e -> {
            int totalPages = (int) Math.ceil(fullList.size() / (double) rowsPerPage);
            if (currentPage < totalPages) {
                currentPage++;
                loadWarehouseData();
            }
        });

        // 최초 로딩
        loadWarehouseData();
    }

    private void loadWarehouseData() {
        model.setRowCount(0);

        String code = tfCode.getText().trim();
        String name = tfName.getText().trim();
        String location = tfLocation.getText().trim();

        WarehouseDAO dao = new WarehouseDAO();
        fullList = dao.selectWarehouseList(code, name, location);  // List<Warehouse> 리턴

        int totalRows = fullList.size();
        int totalPages = (int) Math.ceil(totalRows / (double) rowsPerPage);
        if (totalPages == 0) totalPages = 1;

        int start = (currentPage - 1) * rowsPerPage;
        int end = Math.min(start + rowsPerPage, totalRows);

        for (int i = start; i < end; i++) {
            Warehouse warehouse = fullList.get(i);
            model.addRow(new Object[] {
                warehouse.getWarehouseCode(),
                warehouse.getWarehouseName(),
                warehouse.getAddress(),
                "재고보기",
            });
        }

        laCount.setText("총 " + totalRows + "개의 창고 검색");
        laPageInfo.setText(currentPage + " / " + totalPages);
        btnPrevPage.setEnabled(currentPage > 1);
        btnNextPage.setEnabled(currentPage < totalPages);
    }
    
    public void showInventoryPage(String warehouseCode) {
    	InventoryPage inventoryPage = (InventoryPage) appMain.pages[Config.INVENTORY_PAGE];

        inventoryPage.warehouseCode.setText(warehouseCode);  // 값을 전달
        inventoryPage.btnSearch.doClick();                // 검색 이벤트 실행
        appMain.showPage(Config.INVENTORY_PAGE);
    }
    
    private int getColumnIndex(String colName) {
        for (int i = 0; i < columnNames.length; i++) {
            if (columnNames[i].equals(colName))
                return i;
        }
        return -1;
    }
    
    public void resetFields() {
        tfCode.setText("");
        tfName.setText("");
        tfLocation.setText("");
    }
}