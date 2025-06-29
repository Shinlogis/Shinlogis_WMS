package com.shinlogis.wms.common.util.weather;
import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class WeatherPanel extends JPanel {
    private JLabel lblCity;
    private JLabel lblTemp;
    private JLabel lblDescription;
    private JLabel lblSunrise;
    private JLabel lblSunset;
    private JLabel lblIcon;

    public WeatherPanel(String city) {
        setPreferredSize(new Dimension(280, 180));
        setBackground(new Color(30, 144, 255)); // DodgerBlue 배경색
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setLayout(new BorderLayout());

        // 상단: 도시명
        lblCity = new JLabel(city);
        lblCity.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        lblCity.setForeground(Color.WHITE);
        lblCity.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblCity, BorderLayout.NORTH);

        // 중앙: 아이콘 + 온도 & 설명
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        lblIcon = new JLabel();
        lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(lblIcon, BorderLayout.WEST);

        JPanel tempDescPanel = new JPanel(new GridLayout(2, 1));
        tempDescPanel.setOpaque(false);

        lblTemp = new JLabel("온도: 로딩중...");
        lblTemp.setFont(new Font("맑은 고딕", Font.BOLD, 36));
        lblTemp.setForeground(Color.WHITE);

        lblDescription = new JLabel(""); // 날씨 설명 (예: 맑음, 흐림 등)
        lblDescription.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        lblDescription.setForeground(Color.WHITE);

        tempDescPanel.add(lblTemp);
        tempDescPanel.add(lblDescription);
        centerPanel.add(tempDescPanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // 하단: 일출, 일몰
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        bottomPanel.setOpaque(false);

        lblSunrise = new JLabel("일출: 로딩중...");
        lblSunrise.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        lblSunrise.setForeground(Color.WHITE);

        lblSunset = new JLabel("일몰: 로딩중...");
        lblSunset.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        lblSunset.setForeground(Color.WHITE);

        bottomPanel.add(lblSunrise);
        bottomPanel.add(lblSunset);

        add(bottomPanel, BorderLayout.SOUTH);

        // 날씨 정보 비동기 로딩
        new Thread(() -> {
            try {
                WeatherData data = WeatherFetcher.fetch(city);
                SwingUtilities.invokeLater(() -> {
                    lblTemp.setText(String.format("%.1f℃", data.getTemp()));

                    // 날씨 설명 추가 (예: 맑음, 비 등)
                    // JSON에서 description을 꺼내려면 WeatherFetcher fetch 함수 수정 필요함
                    // 우선 아이콘 코드로 간단한 예시 처리
                    String icon = data.getIcon();
                    String desc = mapIconToDescription(icon);
                    lblDescription.setText(desc);

                    lblSunrise.setText("일출: " + WeatherFetcher.formatTime(data.getSunrise()));
                    lblSunset.setText("일몰: " + WeatherFetcher.formatTime(data.getSunset()));

                    try {
                        String iconUrl = "https://openweathermap.org/img/wn/" + icon + "@2x.png";
                        lblIcon.setIcon(new ImageIcon(new URL(iconUrl)));
                    } catch (Exception e) {
                        lblIcon.setText("아이콘 없음");
                    }
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    lblTemp.setText("날씨 정보 로딩 실패");
                    lblDescription.setText("");
                    lblSunrise.setText("");
                    lblSunset.setText("");
                    lblIcon.setIcon(null);
                });
            }
        }).start();
    }

    // 아이콘 코드로 간단한 설명 매핑
    private String mapIconToDescription(String icon) {
        switch (icon) {
            case "01d": return "맑음";
            case "01n": return "맑은 밤";
            case "02d": return "구름 조금";
            case "02n": return "밤 구름 조금";
            case "03d": case "03n": return "구름 많음";
            case "04d": case "04n": return "흐림";
            case "09d": case "09n": return "소나기";
            case "10d": case "10n": return "비";
            case "11d": case "11n": return "뇌우";
            case "13d": case "13n": return "눈";
            case "50d": case "50n": return "안개";
            default: return "";
        }
    }
}
