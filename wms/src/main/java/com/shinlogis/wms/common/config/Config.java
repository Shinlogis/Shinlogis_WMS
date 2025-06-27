package com.shinlogis.wms.common.config;

public class Config {
	
    public static final String url = "jdbc:mysql://localhost:3306/wms";
    public static final String id = "wms";
    public static final String password = "1234";
    
    public static final String PRODUCT_IMAGE_PATH = "C:\\public";
    
	/*-------------------------------------
	 각 페이지 정의
	 --------------------------------------*/
	public static final int MAIN_PAGE = 0; 
	public static final int INBOUND_PLAN_PAGE = 1; 
	public static final int INBOUND_ITEM_PAGE = 2; 
	public static final int INBOUND_PROCESS_PAGE = 3; 
	public static final int OUTBOUND_PLAN_PAGE = 4; 
	public static final int OUTBOUND_PROCESS_PAGE = 5;
	public static final int PRODUCT_PAGE   = 6; //상품페이지  
	public static final int INVENTORY_PAGE = 7; //재고관리페이지 
	public static final int WAREHOUSE_PAGE = 8; //창고관리페이지
	public static final int LOCATION_PAGE = 9;
	public static final int SUPPLIER_PAGE = 10;
	public static final int CHATTING_PAGE = 11;
	public static final int HEADQUATERS_MY_PAGE = 12;
	public static final int OUTBOUND_REGISTER_PAGE = 13;
	
	
	/*-------------------------------------
	 지점 페이지 정의
	 --------------------------------------*/
	public static final int ORDER_PAGE = 0; 
	public static final int ORDER_LIST_PAGE = 1; 
	public static final int LOCATION_MY_PAGE = 2; 
	public static final int LOCATION_CHATTING_PAGE = 3; 
	public static final int LOCATION_MAIN_PAGE = 4; 
	
	public static final int INBOUND_DETAIL_EDIT_DIALOG_PAGE = 13;

	/*-------------------------------------
	 관리자 앱 메인 설정
	 --------------------------------------*/
	public static final int ADMINMAIN_WIDTH = 1440;
	public static final int ADMINMAIN_HEIGHT = 1024;
	public static final int SIDE_WIDTH = 200;
	public static final int SIDE_HEIGHT = ADMINMAIN_HEIGHT;
	public static final int HEADER_WIDTH = ADMINMAIN_WIDTH - SIDE_WIDTH;
	public static final int HEADER_HEIGHT = 50;
	
	/*-------------------------------------
	 관리 페이지 설정
	 --------------------------------------*/
	public static final int CONTENT_WIDTH = ADMINMAIN_WIDTH - SIDE_WIDTH - 140; // 컨텐츠 영역 너비
	
	public static final int PAGE_NAME_HEIGHT = 30; // 페이지명 높이
	public static final int SEARCH_BAR_HEIGHT = 100;
	public static final int TABLE_HEIGHT = 785;
	public static final int TABLE_NORTH_HEIGHT = 40;
}
