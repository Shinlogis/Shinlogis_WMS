package com.shinlogis.wms.common.util.chart;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;

public class BarChart extends JPanel {
	List<Map<String, Object>> map;

	public BarChart(String title, List<Map<String, Object>> map) {
		this.map = map;
	    DefaultCategoryDataset dataset = createDataset();

	    JFreeChart chart = ChartFactory.createBarChart(
	            title,
	            "",   // x축 레이블
	            "",     // y축 레이블
	            dataset
	    );

	    // 한글 폰트 생성 (맑은 고딕, 크기 12)
	    Font font = new Font("맑은 고딕", Font.PLAIN, 10);

	    // 차트 제목 폰트 설정
	    chart.setTitle(new TextTitle(title, font));

	    // 플롯 가져오기
	    CategoryPlot plot = chart.getCategoryPlot();

	    // X축 폰트 설정
	    CategoryAxis domainAxis = plot.getDomainAxis();
	    domainAxis.setLabelFont(font);       // 축 라벨 폰트
	    domainAxis.setTickLabelFont(font);   // 축 눈금 폰트

	    // Y축 폰트 설정
	    NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
	    rangeAxis.setLabelFont(font);
	    rangeAxis.setTickLabelFont(font);

	    // 범례 폰트 설정
	    if (chart.getLegend() != null) {
	        chart.getLegend().setItemFont(font);
	    }

	    ChartPanel chartPanel = new ChartPanel(chart);
	    chartPanel.setPreferredSize(new Dimension(200, 300));

	    this.setLayout(new BorderLayout());
	    this.add(chartPanel, BorderLayout.CENTER);
	}


    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map<String, Object> row : map) { // 리스트에 있는 값을 하나씩 꺼냄
            Number cnt = (Number) row.get("cnt"); // 숫자
            String name = row.get("name").toString(); // 정보명

            dataset.addValue(cnt, "출고량", name); 
        }


        return dataset;
    }
}
