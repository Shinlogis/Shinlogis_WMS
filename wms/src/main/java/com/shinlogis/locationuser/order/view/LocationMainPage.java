package com.shinlogis.locationuser.order.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.common.config.Config;
import com.shinlogis.wms.common.config.Page;
import com.shinlogis.wms.common.util.ImageUtil;
import com.shinlogis.wms.product.model.Product;
import com.shinlogis.wms.product.repository.ProductDAO;

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
	
	//최신상품목록 중 유저가 지금 선택한 바로 그 상품 
	public Product product;
	
	
	public LocationMainPage(AppMain appMain) {
		super(appMain);
	
		//생성
		//패널을 이름없는 익명 클래스로 재정의하는 코드를 작성...장점? 별도의 .java파일을 생성할 필요
		//없고, 내부 클래스이다 보니, 외부클래스인 MainPage의 멤버를 공유할 수 있다.
		image1 = imageUtil.getImage("images/banner.jpg",Config.CONTENT_WIDTH/3, 300);
		image2 = imageUtil.getImage("images/banner.jpg",Config.CONTENT_WIDTH/3, 300);
		image3 = imageUtil.getImage("images/banner.jpg",Config.CONTENT_WIDTH/3, 300);
		
		p_visual = new JPanel();
		la_title=new JLabel("WMS 창고관리 시스템 ");
		la_title.setFont(new Font("맑은 고딕", Font.BOLD, 30));
				
		img1= new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);// update() 에 지워진 배경을 스스로 복구
				g.drawImage(image1, 0, 0, Config.CONTENT_WIDTH/3, 350, p_visual);
			}
		};
		img2= new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);// update() 에 지워진 배경을 스스로 복구
				g.drawImage(image2, Config.CONTENT_WIDTH/3, 0, Config.CONTENT_WIDTH/3, 350, p_visual);
			}
		};
		img3= new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);// update() 에 지워진 배경을 스스로 복구
				g.drawImage(image3, 0, 0, Config.CONTENT_WIDTH/3, 350, p_visual);
			}
		};
		p_content = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10)); //마진 주기 
		
		//스타일
		p_visual.setPreferredSize(new Dimension( Config.CONTENT_WIDTH, 350));
		p_content.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, 300));
		
		p_visual.setBackground(Color.CYAN);
		p_content.setBackground(Color.RED);
		
		//최신 상품 생성하기
	//	createRecentList();
		
		//조립 
		p_visual.add(la_title);
		p_visual.add(img1);
		p_visual.add(img2);
		p_visual.add(img3);
		add(p_visual);
		add(p_content);
		
		setVisible(true);
	}
	
//	//최신상품 패널 원하는 p_content 에 출력
//	public void createRecentList() {
//		List<Product> productList=productDAO.selectRecentList(6);
//		
//		for(int i=0;i<productList.size();i++) {
//			Product product=productList.get(i); //리스트에서 상품을 하나씩 꺼내자 
//			
//			ProductItem productItem=new ProductItem(this,product); //상품을 하나를 표현하는 카드 
//			p_content.add(productItem);      
//		}
//	}

}
