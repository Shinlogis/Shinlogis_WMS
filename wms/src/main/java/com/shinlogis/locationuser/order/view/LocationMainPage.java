package com.shinlogis.locationuser.order.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.common.config.Config;
import com.shinlogis.wms.common.config.Page;
import com.shinlogis.wms.common.util.ImageUtil;
import com.shinlogis.wms.product.model.Product;
import com.shinlogis.wms.product.repository.ProductDAO;
import com.shinlogis.wms.product.view.ProductItem;

public class LocationMainPage extends Page{
	JPanel p_visual; //메인 비주얼 영역(메인 배너 영역 - carousel)
	JLabel la_title;
	JPanel img1;
	JPanel img2;
	JPanel img3;
	JPanel p_content;//상품이 출력될 영역 
	ImageUtil imageUtil=new ImageUtil();
	Image image1;
	Image image2;
	Image image3;
	ProductDAO productDAO=new ProductDAO();
	
	public AppMain appMain;
	
	//최신상품목록 중 유저가 지금 선택한 바로 그 상품 
	public Product product;
	
	
	public LocationMainPage(AppMain appMain) {
		super(appMain);
		this.appMain=appMain;
		setLayout(new BorderLayout());
		
		image1 = imageUtil.getImage("images/carrot.jpg",Config.CONTENT_WIDTH/3, 300);
		image2 = imageUtil.getImage("images/carrot.jpg",Config.CONTENT_WIDTH/3, 300);
		image3 = imageUtil.getImage("images/carrot.jpg",300, 250);
		
		p_visual = new JPanel();
		la_title=new JLabel("WMS 창고관리 시스템",JLabel.CENTER);
		la_title.setFont(new Font("맑은 고딕", Font.BOLD, 30));
		la_title.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, 50)); // 높이 지정
		
		JPanel p_images = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		p_images.setOpaque(false); // 배경 투명하게
				
		img1= new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);// update() 에 지워진 배경을 스스로 복구
				g.drawImage(image1, 0, 0, Config.CONTENT_WIDTH/3, 350, p_visual);
			}
		};
		img2= new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);// update() 에 지워진 배경을 스스로 복구
				g.drawImage(image2, 0, 0, Config.CONTENT_WIDTH/3, 350, p_visual);
			}
		};
		img3= new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);// update() 에 지워진 배경을 스스로 복구
				g.drawImage(image3, 0, 0, Config.CONTENT_WIDTH/3, 350, p_visual);
			}
		};
		
		Dimension imgSize = new Dimension(250, 250);
		img1.setPreferredSize(imgSize);
		img2.setPreferredSize(imgSize);
		img3.setPreferredSize(imgSize);
		
		p_content = new JPanel(new FlowLayout(FlowLayout.LEFT, 45, 20)); //마진 주기 
		//스타일
		p_visual.setPreferredSize(new Dimension( Config.CONTENT_WIDTH, 350));
		p_content.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, 340));
		
		p_visual.setBackground(Color.WHITE);
		p_content.setBackground(Color.WHITE);
		
		//상온 
		img1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				 p_content.removeAll();
		         createRecentList(1);
		         p_content.revalidate();
		         p_content.repaint();
			}
		});
		
		//냉장 
		img2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				p_content.removeAll();
				createRecentList(2);
				p_content.revalidate();
				p_content.repaint();
			}
		});
		
		//냉동 
		img3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				p_content.removeAll();
				createRecentList(3);
				p_content.revalidate();
				p_content.repaint();
			}
		});
		
		//조립
		p_visual.add(la_title, BorderLayout.NORTH);
		p_images.add(img1);
		p_images.add(img2);
		p_images.add(img3);
		p_visual.add(p_images, BorderLayout.CENTER);
		add(p_visual,BorderLayout.NORTH);
		add(p_content);
		
		setVisible(true);
	}
	
	//최신상품 패널 원하는 p_content 에 출력
	public void createRecentList(int storeTypeId) {
		List<Product> productList=productDAO.selectthumbnail(storeTypeId);
		
		for(int i=0;i<productList.size();i++) {
			Product product=productList.get(i); //리스트에서 상품을 하나씩 꺼내자 
			
			ProductItem productItem=new ProductItem(this,product); //상품을 하나를 표현하는 카드 
			p_content.add(productItem);      
		}
	}

}
