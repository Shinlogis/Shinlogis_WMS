package com.shinlogis.wms.common.util;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import javax.swing.JPanel;

public class RoundedPanel extends JPanel {
    private Color backgroundColor;
    private int cornerRadius = 20;
    private int shadowSize = 10; // 그림자 크기 (사방)
    private float shadowOpacity = 0.2f; // 0.0 ~ 1.0 (더 연하게)

    public RoundedPanel(LayoutManager layout, int radius, Color bgColor) {
        super(layout);
        cornerRadius = radius;
        backgroundColor = bgColor;
        setOpaque(false); // 배경 투명 처리
    }

    public RoundedPanel(int radius, Color bgColor) {
        this(null, radius, bgColor);
    }

    @Override
    protected void paintComponent(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        int shadowGap = shadowSize;

        BufferedImage shadowImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = shadowImage.createGraphics();

        // 부드러운 그림자 그리기
        g2.setColor(new Color(0, 0, 0, (int)(255 * shadowOpacity)));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fillRoundRect(shadowGap, shadowGap, width - shadowGap * 2, height - shadowGap * 2, cornerRadius, cornerRadius);
        g2.dispose();

        // 가우시안 블러 효과
        shadowImage = blurImage(shadowImage, shadowSize);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.drawImage(shadowImage, 0, 0, null);

        // 본체 패널
        g2d.setColor(backgroundColor);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.fillRoundRect(shadowGap, shadowGap, width - shadowGap * 2, height - shadowGap * 2, cornerRadius, cornerRadius);
        g2d.dispose();

        super.paintComponent(g);
    }

    // 간단한 Blur 필터 함수
    private BufferedImage blurImage(BufferedImage image, int radius) {
        float[] matrix = createGaussianKernel(radius);
        BufferedImageOp op = new ConvolveOp(new Kernel(radius, radius, matrix), ConvolveOp.EDGE_NO_OP, null);
        return op.filter(image, null);
    }

    // 가우시안 커널 생성 (간단한 분포)
    private float[] createGaussianKernel(int radius) {
        int size = radius * radius;
        float[] data = new float[size];
        float value = 1.0f / size;
        for (int i = 0; i < size; i++) data[i] = value;
        return data;
    }
}
