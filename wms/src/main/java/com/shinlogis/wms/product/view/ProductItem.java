package com.shinlogis.wms.product.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.shinlogis.locationuser.order.view.LocationMainPage;
import com.shinlogis.wms.common.config.Config;
import com.shinlogis.wms.common.util.ImageUtil;
import com.shinlogis.wms.common.util.StringUtil;
import com.shinlogis.wms.product.model.Product;



public class ProductItem extends JPanel{
	Product product;
	Image image;
	ImageUtil imageUtil=new ImageUtil();
	LocationMainPage mainPage; //현재 producttem을 생성한 주체 페이지이므로 
	public ProductItem(LocationMainPage mainPage,Product product) {
		this.mainPage=mainPage;
		this.product=product;

		image = imageUtil.getImage(product.getThumbnailPath(),185, 120);
		//마우스 리스너 연결
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//어떤 상품을 선택했는지 그 정보를 보관 
				mainPage.product=product;
				//페이지전환 
				mainPage.appMain.showPage(Config.ORDER_PAGE);
			}
		});
		
		
		setPreferredSize(new Dimension(195, 170));
		setBackground(Color.WHITE);
	}

	protected void paintComponent(Graphics g) {
//		super.paintComponent(g);
//		
//		Graphics2D g2=(Graphics2D)g;
//		g2.drawImage(image, 5, 5, 185, 120, this); 
//		
//		//상품명 그리기 
//		g2.setFont(new Font("Gulim", Font.BOLD,20));
//		
//		String name = product.getProductName();
//	    int nameWidth = g2.getFontMetrics().stringWidth(name);
//	    int nameX = (getWidth() - nameWidth) / 2;
//	    g2.drawString(name, nameX, 160);
		super.paintComponent(g);
	    Graphics2D g2 = (Graphics2D) g.create();

	    int diameter = 120; // 원의 지름
	    int x = (getWidth() - diameter) / 2;
	    int y = 10;

	    // BufferedImage 생성
	    BufferedImage circleImage = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D gCircle = circleImage.createGraphics();

	    // 원형 클리핑
	    gCircle.setClip(new Ellipse2D.Float(0, 0, diameter, diameter));
	    gCircle.drawImage(image, 0, 0, diameter, diameter, null);
	    gCircle.dispose();

	    // 패널에 원형 이미지 그리기
	    g2.drawImage(circleImage, x, y, null);

	    // 상품명 그리기
	    g2.setFont(new Font("Gulim", Font.BOLD, 16));
	    String name = product.getProductName();
	    int nameWidth = g2.getFontMetrics().stringWidth(name);
	    int nameX = (getWidth() - nameWidth) / 2;
	    g2.drawString(name, nameX, y + diameter + 25); // 이미지 아래 여백을 두고 이름 출력

	    g2.dispose();
	}
	
}