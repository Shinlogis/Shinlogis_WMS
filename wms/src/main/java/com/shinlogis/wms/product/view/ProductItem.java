package com.shinlogis.wms.product.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.shinlogis.locationuser.order.view.LocationMainPage;
import com.shinlogis.locationuser.order.view.OrderPage;
import com.shinlogis.wms.common.config.Config;
import com.shinlogis.wms.common.util.ImageUtil;
import com.shinlogis.wms.product.model.Product;
import com.shinlogis.wms.product.repository.ProductDAO;



public class ProductItem extends JPanel{
	Product product;
	Image image;
	ImageUtil imageUtil=new ImageUtil();
	LocationMainPage mainPage; //현재 producttem을 생성한 주체 페이지이므로 
	ProductDAO productDao = new ProductDAO();
	
	public ProductItem(LocationMainPage mainPage,Product product) {
		this.mainPage=mainPage;
		this.product=product;
		
		
		image = imageUtil.getImage(product.getThumbnailPath(),120, 120);
		//마우스 리스너 연결
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 선택한 상품 보관
		        mainPage.product = product;

		        // 페이지 전환
		        mainPage.appMain.showPage(Config.ORDER_PAGE);
				
			}
		});
		
		setPreferredSize(new Dimension(200, 175));
		setBackground(Color.WHITE);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	    Graphics2D g2 = (Graphics2D) g.create();

	    int diameter = 120; // 원의 지름
	    int x = (getWidth() - diameter) / 2;
	    int y = 10;

	    // BufferedImage 생성
	    BufferedImage circleImage = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D gCircle = circleImage.createGraphics();
	    
	    gCircle.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    gCircle.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	    gCircle.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	    // 원형 클리핑
	    gCircle.setClip(new Ellipse2D.Float(0, 0, diameter, diameter));
	   // gCircle.drawImage(image, 0, 0, diameter, diameter, null);
	    // 패딩값 추가
	    int padding = 15;
	    gCircle.drawImage(image, padding, padding, diameter - 2 * padding, diameter - 2 * padding, null);
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