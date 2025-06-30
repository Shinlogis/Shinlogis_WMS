package com.shinlogis.wms.main.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.common.config.Page;
import com.shinlogis.wms.common.util.RoundedPanel;
import com.shinlogis.wms.common.util.chart.AreaChart;
import com.shinlogis.wms.common.util.chart.BarChart;
import com.shinlogis.wms.common.util.weather.WeatherPanel;
import com.shinlogis.wms.inoutbound.model.IODetail;
import com.shinlogis.wms.statistic.StatisticDAO;

public class MainPage extends Page {

    private StatisticDAO statisticDAO = new StatisticDAO();
    private List<Map<String, Object>> barChartMapList;
    private List<Map<String, Object>> areaChartMapList;

    private JPanel pTop;
    private JPanel pt1, pt2, pt3, pt4, pt5;
    private JLabel pt1Title, pt1Value;
    private JLabel pt2Title, pt2Value;
    private JLabel pt3Title, pt3Value;

    private JPanel pMid;
    private JPanel pBottom;

    public MainPage(AppMain appMain) {
        super(appMain);

        // === Top Panel: 오늘 입출고 현황 ===
        pTop = new JPanel(new GridLayout(1, 5, 5, 0));
        pTop.setPreferredSize(new Dimension(0, 120));
        pTop.setBackground(new Color(245, 245, 245));
        pTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        pt1 = createStatPanel("오늘 입고예정", Integer.toString(statisticDAO.getTotalInboundToday()), new Color(72, 145, 220));
        pt2 = createStatPanel("오늘 입고완료", Integer.toString(statisticDAO.getTotalInboundCompleted()), new Color(60, 120, 200));
        pt3 = createStatPanel("오늘 출고예정", Integer.toString(statisticDAO.getTotalOutboundToday()), new Color(220, 72, 72));
        pt4 = createStatPanel("오늘 출고완료", Integer.toString(statisticDAO.getTotalOutboundCompleted()), new Color(200, 60, 60));
        pt5 = createStatPanel("오늘 출고금액", Integer.toString(statisticDAO.getTotalOutboundCompletedPrice()) + " ₩", new Color(72, 220, 128));

        pTop.add(pt1);
        pTop.add(pt2);
        pTop.add(pt3);
        pTop.add(pt4);
        pTop.add(pt5);

        // === Mid Panel: 차트 + 날씨 + 입출고 게시판 + 그래프 ===
        pMid = new JPanel(new GridBagLayout());
        pMid.setBackground(Color.WHITE);
        pMid.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;

        // 위쪽 왼쪽: AreaChart (0.7 비율)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        gbc.weighty = 1.0;
        areaChartMapList = statisticDAO.get7daysInOutQuantity();
        AreaChart areaChart = new AreaChart("최근 7일 간 입출고 수량", areaChartMapList);
        JPanel areaChartWrapper = new RoundedPanel(new BorderLayout(), 20, Color.WHITE);
        areaChartWrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        areaChartWrapper.add(areaChart, BorderLayout.CENTER);
        pMid.add(areaChartWrapper, gbc);

        // 위쪽 오른쪽: WeatherPanel (0.3 비율)
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        gbc.weighty = 1.0;
        WeatherPanel weatherPanel = new WeatherPanel("Seoul");
        weatherPanel.setPreferredSize(new Dimension(280, 180));
        JPanel weatherWrapper = new RoundedPanel(new BorderLayout(), 20, Color.WHITE);
        weatherWrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        weatherWrapper.add(weatherPanel, BorderLayout.CENTER);
        pMid.add(weatherWrapper, gbc);

        // 아래 왼쪽: 최근 입고 / 출고 내역 패널 (0.4 비율)
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.4;
        gbc.weighty = 1.0;

        JPanel bottomLeftWrapper = new RoundedPanel(new BorderLayout(10, 10), 20, Color.WHITE);
        bottomLeftWrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // DAO 호출
        List<IODetail> inboundList = statisticDAO.getRecentCompletedInboundDetails();
        List<IODetail> outboundList = statisticDAO.getRecentCompletedOutboundDetails();

        // 입고 목록 모델 생성
        DefaultListModel<String> inboundModel = new DefaultListModel<>();
        for (IODetail detail : inboundList) {
            int receiptId = detail.getIoReceipt().getIoReceiptId();
            String productName = detail.getProductSnapshot().getProductName();
            int quantity = detail.getActualQuantity();
            inboundModel.addElement(String.format("[입고 #%d] %s - %d개", receiptId, productName, quantity));
        }
        JList<String> inboundJList = new JList<>(inboundModel);
        inboundJList.setBorder(BorderFactory.createTitledBorder("최근 입고 완료"));
        inboundJList.setBackground(Color.WHITE);

        // 출고 목록 모델 생성
        DefaultListModel<String> outboundModel = new DefaultListModel<>();
        for (IODetail detail : outboundList) {
            outboundModel.addElement(String.format("[출고 #%d] %s - %d개",
                    detail.getIoReceipt().getIoReceiptId(),
                    detail.getProductSnapshot().getProductName(),
                    detail.getActualQuantity()));
        }
        JList<String> outboundJList = new JList<>(outboundModel);
        outboundJList.setBorder(BorderFactory.createTitledBorder("최근 출고 완료"));
        outboundJList.setBackground(Color.WHITE);

        // 두 리스트 가로 배치
        JPanel listPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        listPanel.setOpaque(false);
        listPanel.add(new JScrollPane(inboundJList));
        listPanel.add(new JScrollPane(outboundJList));

        bottomLeftWrapper.add(listPanel, BorderLayout.CENTER);
        pMid.add(bottomLeftWrapper, gbc);

        // 아래 오른쪽: BarChart (0.6 비율)
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.6;
        gbc.weighty = 1.0;

        barChartMapList = statisticDAO.getTop5OutboundLocationsToday();
        BarChart barChart = new BarChart("최다 출고지점 그래프", barChartMapList);
        JPanel barChartWrapper = new RoundedPanel(new BorderLayout(), 20, Color.WHITE);
        barChartWrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        barChartWrapper.add(barChart, BorderLayout.CENTER);
        pMid.add(barChartWrapper, gbc);

        // === Bottom Panel: 빈 공간 또는 추가 기능 공간 ===
        pBottom = new JPanel();
        pBottom.setPreferredSize(new Dimension(0, 80));
        pBottom.setBackground(Color.WHITE);

        // === MainPage 레이아웃 ===
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);

        add(pTop, BorderLayout.NORTH);
        add(pMid, BorderLayout.CENTER);
        add(pBottom, BorderLayout.SOUTH);
    }


    // 입출고 통계 패널 만드는 메서드
    private JPanel createStatPanel(String title, String value, Color color) {
        JPanel panel = new RoundedPanel(new BorderLayout(5, 5), 20, color);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setPreferredSize(new Dimension(150, 100));
        panel.setMinimumSize(new Dimension(150, 100));
        panel.setMaximumSize(new Dimension(150, 100));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font("맑은 고딕", Font.BOLD, 28));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);

        return panel;
    }

    // 출고금액 포맷 메서드
    private String getFormattedOutboundAmount() {
        int amount = 123456789;
        return String.format("%,d", amount);
    }
    
    public void refreshData() {
        pt1Value.setText(Integer.toString(statisticDAO.getTotalInboundToday()));
        pt2Value.setText(Integer.toString(statisticDAO.getTotalInboundCompleted()));
        pt3Value.setText(Integer.toString(statisticDAO.getTotalOutboundToday()));


        // AreaChart 및 BarChart 데이터 갱신 필요 시 재호출하거나 차트에 refresh() 메서드 구현 후 호출
    }


    
}
