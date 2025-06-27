package com.shinlogis.wms.common.util.weather;

public class WeatherData {
    private double temp;       // 현재 온도 (섭씨)
    private long sunrise;      // 일출 시간 (UNIX 타임스탬프)
    private long sunset;       // 일몰 시간 (UNIX 타임스탬프)
    private String icon;       // 날씨 아이콘 코드

    public WeatherData(double temp, long sunrise, long sunset, String icon) {
        this.temp = temp;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.icon = icon;
    }

    public double getTemp() {
        return temp;
    }

    public long getSunrise() {
        return sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    public String getIcon() {
        return icon;
    }

    @Override
    public String toString() {
        return "WeatherData{" +
                "temp=" + temp +
                ", sunrise=" + sunrise +
                ", sunset=" + sunset +
                ", icon='" + icon + '\'' +
                '}';
    }
}
