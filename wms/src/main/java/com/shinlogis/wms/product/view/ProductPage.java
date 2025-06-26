package com.shinlogis.wms.product.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

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
import com.shinlogis.wms.product.model.ProductDTO;
import com.shinlogis.wms.product.repository.ProductDAO;

public class ProductPage extends Page {

    private JPanel pPageName;
    private JLabel laPageName;

    private JPanel pSearch;
    private JTextField productCode, productName, supplierName, priceMin, priceMax;
    private JComboBox<String> comboStorageType;
    public JButton btnSearch;

    private JPanel pTable;
    private JLabel laPlanCount;
    private JTable tblPlan;
    private JScrollPane scTable;
    private DefaultTableModel model;
    private JPanel pTableNorth;

    private JPanel pPaging;
    private JButton btnPrevPage, btnNextPage;
    private JLabel laPageInfo;

    private String[] columnNames = { "상품코드", "상품명", "공급사명", "보관타입", "가격" };

    private int currentPage = 1;
    private final int rowsPerPage = 10;
    private List<ProductDTO> fullList;

    public ProductPage(AppMain appMain) {
        super(appMain);

        /* ==== 검색 영역 ==== */
        pSearch = new JPanel(new GridBagLayout());
        pSearch.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.SEARCH_BAR_HEIGHT));
        pSearch.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0, col = 0;

        gbc.gridx = col++;
        gbc.gridy = row;
        pSearch.add(new JLabel("상품코드"), gbc);
        productCode = new JTextField(10);
        gbc.gridx = col++;
        pSearch.add(productCode, gbc);

        gbc.gridx = col++;
        pSearch.add(new JLabel("상품명"), gbc);
        productName = new JTextField(10);
        gbc.gridx = col++;
        pSearch.add(productName, gbc);

        gbc.gridx = col++;
        pSearch.add(new JLabel("공급사명"), gbc);
        supplierName = new JTextField(10);
        gbc.gridx = col++;
        pSearch.add(supplierName, gbc);

        gbc.gridx = col++;
        pSearch.add(new JLabel("보관타입"), gbc);
        String[] storageTypes = { "전체", "냉장", "냉동", "상온" };
        comboStorageType = new JComboBox<>(storageTypes);
        gbc.gridx = col++;
        pSearch.add(comboStorageType, gbc);

        gbc.gridx = col++;
        pSearch.add(new JLabel("가격"), gbc);
        priceMin = new JTextField(5);
        priceMax = new JTextField(5);
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pricePanel.add(priceMin);
        pricePanel.add(new JLabel(" ~ "));
        pricePanel.add(priceMax);
        gbc.gridx = col++;
        pSearch.add(pricePanel, gbc);

        btnSearch = new JButton("검색");
        gbc.gridx = col;
        pSearch.add(btnSearch, gbc);

        /* ==== 페이지명 영역 ==== */
        pPageName = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pPageName.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.PAGE_NAME_HEIGHT));
        laPageName = new JLabel("상품관리");
        pPageName.add(laPageName);

        /* ==== 검색 결과 카운트 영역 ==== */
        pTableNorth = new JPanel(new FlowLayout());
        pTableNorth.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.TABLE_NORTH_HEIGHT));
        laPlanCount = new JLabel("총 0개의 상품 검색");
        laPlanCount.setPreferredSize(new Dimension(Config.CONTENT_WIDTH - 150, 30));
        pTableNorth.add(laPlanCount);

        /* ==== 테이블 영역 ==== */
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblPlan = new JTable(model);
        tblPlan.setRowHeight(45);
        tblPlan.getTableHeader().setPreferredSize(new Dimension(Config.CONTENT_WIDTH - 20, 45));
        tblPlan.setPreferredScrollableViewportSize(new Dimension(Config.CONTENT_WIDTH - 20, 495));

        scTable = new JScrollPane(tblPlan);
        scTable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scTable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scTable.setPreferredSize(new Dimension(Config.CONTENT_WIDTH - 20, 495));

        /* ==== 페이징 컨트롤 ==== */
        pPaging = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pPaging.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, 40));
        btnPrevPage = new JButton("이전");
        btnNextPage = new JButton("다음");
        laPageInfo = new JLabel("1 / 1");
        btnPrevPage.setEnabled(false);
        btnNextPage.setEnabled(false);
        pPaging.add(btnPrevPage);
        pPaging.add(laPageInfo);
        pPaging.add(btnNextPage);

        /* ==== 테이블 컨테이너 ==== */
        pTable = new JPanel(new FlowLayout());
        pTable.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.TABLE_HEIGHT));
        pTable.setBackground(Color.WHITE);
        pTable.add(pTableNorth);
        pTable.add(scTable);
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
            loadProductData();
        });

        btnPrevPage.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                loadProductData();
            }
        });

        btnNextPage.addActionListener(e -> {
            int totalPages = (int) Math.ceil(fullList.size() / (double) rowsPerPage);
            if (currentPage < totalPages) {
                currentPage++;
                loadProductData();
            }
        });

        // 첫 로딩
        loadProductData();
    }

    private void loadProductData() {
        model.setRowCount(0);

        // 검색 필터 값
        String code = productCode.getText().trim();
        String name = productName.getText().trim();
        String supplier = supplierName.getText().trim();
        String storageType = (String) comboStorageType.getSelectedItem();
        String priceMinStr = priceMin.getText().trim();
        String priceMaxStr = priceMax.getText().trim();

        int minPrice = priceMinStr.isEmpty() ? 0 : Integer.parseInt(priceMinStr);
        int maxPrice = priceMaxStr.isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(priceMaxStr);

        if ("전체".equals(storageType)) {
            storageType = null;
        }

        ProductDAO dao = new ProductDAO();
        fullList = dao.selectProductList(code, name, supplier, storageType, minPrice, maxPrice);

        int totalRows = fullList.size();
        int totalPages = (int) Math.ceil(totalRows / (double) rowsPerPage);
        if (totalPages == 0) totalPages = 1;

        int start = (currentPage - 1) * rowsPerPage;
        int end = Math.min(start + rowsPerPage, totalRows);

        for (int i = start; i < end; i++) {
            ProductDTO dto = fullList.get(i);
            Object[] row = {
                dto.getProductCode(),
                dto.getProductName(),
                dto.getSupplierName(),
                dto.getStorageTypeName(),
                dto.getPrice()
            };
            model.addRow(row);
        }

        laPlanCount.setText("총 " + totalRows + "개의 상품 검색");
        laPageInfo.setText(currentPage + " / " + totalPages);
        btnPrevPage.setEnabled(currentPage > 1);
        btnNextPage.setEnabled(currentPage < totalPages);
    }
    
    public void resetFields() {
        productCode.setText("");
        productName.setText("");
        supplierName.setText("");
        comboStorageType.setSelectedIndex(0); // "전체"로 초기화
        priceMin.setText("");
        priceMax.setText("");
    }
}