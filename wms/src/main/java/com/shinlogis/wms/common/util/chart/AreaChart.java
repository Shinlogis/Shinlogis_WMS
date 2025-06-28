package com.shinlogis.wms.common.util.chart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class AreaChart extends JPanel {
	List<Map<String, Object>> map;

    public AreaChart(String title,  List<Map<String, Object>> map) {
    	this.map = map;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        CategoryDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset, title);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 300));

        add(chartPanel, BorderLayout.CENTER);
    }
    
    private CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map<String, Object> row : map) {
            String date = (String) row.get("날짜");
            Number inboundQty = (Number) row.get("입고수량");
            Number outboundQty = (Number) row.get("출고수량");

            if (inboundQty != null) {
                dataset.addValue(inboundQty, "입고량", date);
            }
            if (outboundQty != null) {
                dataset.addValue(outboundQty, "출고량", date);
            }
        }
        return dataset;
    }




    private JFreeChart createChart(CategoryDataset dataset, String title) {
        JFreeChart chart = ChartFactory.createAreaChart(
            title,                  // 차트 제목
            "",              // X축 라벨
            "",                   // Y축 라벨
            dataset,
            PlotOrientation.VERTICAL,
            true, true, false
        );

        // 폰트 설정
        Font font = new Font("맑은 고딕", Font.PLAIN, 12);
        chart.setTitle(new TextTitle(title, new Font("맑은 고딕", Font.BOLD, 14)));

        // 플롯 설정
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setForegroundAlpha(0.5f);
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.WHITE);

        // X축
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        domainAxis.setTickLabelFont(font);
        domainAxis.setLabelFont(font);

        // Y축
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setTickLabelFont(font);
        rangeAxis.setLabelFont(font);

        // 범례
        if (chart.getLegend() != null) {
            chart.getLegend().setItemFont(font);
        }

        return chart;
    }
}
