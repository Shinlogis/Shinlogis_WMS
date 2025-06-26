package com.shinlogis.wms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.shinlogis.locationuser.order.view.OrderPage;
import com.shinlogis.locationuser.orderList.view.OrderListPage;
import com.shinlogis.wms.Member.view.HeadquartersJoin;
import com.shinlogis.wms.Member.view.MemberLogin;
import com.shinlogis.wms.chat.client.location.LocationChat;
import com.shinlogis.wms.chat.view.ChattingPage;
import com.shinlogis.wms.common.config.Config;
import com.shinlogis.wms.common.config.Page;
import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.headquarters.model.HeadquartersUser;
import com.shinlogis.wms.headquarters.view.HeadquatersMyPage;
import com.shinlogis.wms.inoutbound.inbound.view.detail.DetailPage;
import com.shinlogis.wms.inoutbound.inbound.view.process.ProcessPage;
import com.shinlogis.wms.inoutbound.inbound.view.receipt.ReceiptPage;
import com.shinlogis.wms.inoutbound.outbound.view.OutboundDetailPage;
import com.shinlogis.wms.inoutbound.outbound.view.OutboundReceiptPage;
import com.shinlogis.wms.inoutbound.outbound.view.OutboundRegiterPage;
import com.shinlogis.wms.inventory.view.InventoryPage;
import com.shinlogis.wms.location.model.LocationUser;
import com.shinlogis.wms.location.view.LocatoinMyPage;
import com.shinlogis.wms.main.view.MainPage;
import com.shinlogis.wms.product.view.ProductPage;
import com.shinlogis.wms.supplier.view.SupplierPage;
import com.shinlogis.wms.warehouse.view.WarehousePage;

public class AppMain extends JFrame {
	JPanel p_west, p_center, p_north, p_content;
	JLabel la_inboundPlan, la_inboundDetail, la_inboundProcess;
	JLabel la_outboundPlan, la_outboundDetail, la_outboundRegister;
	JLabel la_inventory, la_warehouse, la_branch, la_supplier, la_chat, la_order, la_orderList, la_product, la_location_chat;

	JLabel la_user, la_logout;
	public Page[] pages;

	HeadquartersJoin memberJoin;
	boolean login = false;

	DBManager dbManager = DBManager.getInstance();
	public Connection conn;
	public HeadquartersUser headquartersUser;
	public LocationUser locationUser;
	private String role; // 내부적으로 사용할 역할(관리자,지점)

	public AppMain(HeadquartersUser headquartersUser, LocationUser locationUser) {
		this.headquartersUser = headquartersUser;
		this.locationUser = locationUser;

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dbManager.release(conn);
				System.exit(0);
			}
		});
	}

	public void initUI() {
		// 로그인한 사용자가 누구인지 판단하기
		if (headquartersUser != null) {
			role = "headquartersUser";
		} else if (locationUser != null) {
			role = "locationUser";
		} else {
			JOptionPane.showMessageDialog(this, "사용자 정보가 없습니다.");
			return;
		}

		// 메인 패널 초기화
		p_center = new JPanel(new BorderLayout());
		p_west = new JPanel();
		p_north = new JPanel();
		p_content = new JPanel();

		// 상단 바 설정
		p_north.setPreferredSize(new Dimension(Config.ADMINMAIN_WIDTH - Config.SIDE_WIDTH, Config.HEADER_HEIGHT));
		p_north.setBackground(Color.YELLOW);
		createMyPage();

		// 사이드 바 설정
		createSidebar();

		// 조립
		p_center.setPreferredSize(new Dimension(Config.ADMINMAIN_WIDTH - Config.SIDE_WIDTH, Config.ADMINMAIN_HEIGHT));
		p_center.setBackground(Color.BLUE);

		p_content.setPreferredSize(p_center.getPreferredSize());
		p_content.setBackground(Color.WHITE);

		p_center.add(p_north, BorderLayout.NORTH);
		p_center.add(p_content, BorderLayout.CENTER);
		add(p_west, BorderLayout.WEST);
		add(p_center, BorderLayout.CENTER);

		connect();
		createPage();
		setBounds(200, 100, Config.ADMINMAIN_WIDTH, Config.ADMINMAIN_HEIGHT);
		setVisible(true);
	}

	private void createSidebar() {
		p_west.setPreferredSize(new Dimension(Config.SIDE_WIDTH, Config.SIDE_HEIGHT));
		p_west.setBackground(new Color(0xFF7F50));
		p_west.setLayout(new BoxLayout(p_west, BoxLayout.Y_AXIS));
		p_west.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

		p_west.add(Box.createVerticalStrut(40)); // 40픽셀 아래로 내림

		if ("headquartersUser".equals(role)) {
			// 메뉴 항목 생성
			la_inboundPlan = createMenuItem("입고 예정", Config.INBOUND_PLAN_PAGE);
			la_inboundDetail = createMenuItem("입고 상세", Config.PRODUCT_PAGE);
			la_inboundProcess = createMenuItem("입고 처리", Config.INBOUND_PLAN_PAGE);
			la_outboundPlan = createMenuItem("출고 예정", Config.INBOUND_ITEM_PAGE);
			la_outboundDetail = createMenuItem("출고 상세", Config.INBOUND_ITEM_PAGE);
			la_outboundRegister = createMenuItem("주문 조회 및 등록", Config.INBOUND_ITEM_PAGE);
			la_product = createMenuItem("상품 관리", Config.PRODUCT_PAGE);
			la_inventory = createMenuItem("재고 관리", Config.INVENTORY_PAGE);
			la_warehouse= createMenuItem("창고 관리", Config.WAREHOUSE_PAGE);
			la_branch = createMenuItem("지점 관리", Config.LOCATION_PAGE);
			la_supplier = createMenuItem("공급사 관리", Config.SUPPLIER_PAGE);
			la_chat = createMenuItem("지점과 채팅하기", Config.CHATTING_PAGE);

			//주문 조회 및 등록 연결 추가
			la_outboundRegister.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					showPage(Config.OUTBOUND_REGISTER_PAGE);
				}
			});
			
			la_outboundDetail.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					showPage(Config.OUTBOUND_PROCESS_PAGE);
				}
			});

			// 출고예정 페이지 보여주는 이벤트 연결@이세형
			la_outboundPlan.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					showPage(Config.OUTBOUND_PLAN_PAGE);
				}
			});

			// 이벤트 연결
			la_inboundPlan.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					showPage(Config.INBOUND_PLAN_PAGE);
				}
			});

			// 입고상세 이벤트 연결 추가
			la_inboundDetail.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					showPage(Config.INBOUND_ITEM_PAGE);
				}
			});

			// 입고처리 이벤트 연결 추가
			la_inboundProcess.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					showPage(Config.INBOUND_PROCESS_PAGE);
				}
			});

			la_product.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					ProductPage proPage = (ProductPage) pages[Config.PRODUCT_PAGE];
					proPage.resetFields(); // 여기서 초기화
					proPage.btnSearch.doClick();
					showPage(Config.PRODUCT_PAGE);
				}
			});

			// 재고 페이지 연결
			la_inventory.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					InventoryPage invPage = (InventoryPage) pages[Config.INVENTORY_PAGE];
					invPage.resetFields(); // 여기서 초기화
					invPage.btnSearch.doClick();
					showPage(Config.INVENTORY_PAGE); // 그리고 화면 전환
				}
			});

			// 창고 페이지 연결
			la_warehouse.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					WarehousePage warePage = (WarehousePage) pages[Config.WAREHOUSE_PAGE];
					warePage.resetFields(); // 여기서 초기화
					warePage.btnSearch.doClick();
					showPage(Config.WAREHOUSE_PAGE);
				}
			});
			
			//공급사 페이지
			la_supplier.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					showPage(Config.SUPPLIER_PAGE);
				}
			});
			
			//채팅 페이지
			la_chat.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(headquartersUser.getId().equals("yujin")) {
						showPage(Config.CHATTING_PAGE);
						ChattingPage chattingPage = (ChattingPage)pages[Config.CHATTING_PAGE];
						chattingPage.createConnection();
						System.out.println("접속 시도 중");
					}else {
						JOptionPane.showMessageDialog(AppMain.this, "권한이 없습니다.");
					}
				}
			});

		} else if ("locationUser".equals(role)) {
			la_order = createMenuItem("물품 신청", Config.OUTBOUND_PROCESS_PAGE);
			la_orderList = createMenuItem("발주내역 조회", Config.OUTBOUND_PLAN_PAGE);
			la_location_chat = createMenuItem("본사와 채팅하기", Config.CHATTING_PAGE);

			// 이벤트 연결
			la_order.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					showPage(0);
				}
			});
			// 이벤트 연결
			la_orderList.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					showPage(1);
				}
			});
			
			//채팅 페이지
			la_location_chat.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					new LocationChat(AppMain.this, locationUser.getLocation());
					
				}
			});
		}

		// 메뉴 그룹 추가
		addMenuGroups();
	}

	private void createMyPage() {
		p_north.setLayout(new FlowLayout(FlowLayout.RIGHT, 30, 0)); // 오른쪽 정렬
		p_north.removeAll();

		la_user = new JLabel();
		la_user.setFont(new Font("맑은 고딕{", Font.BOLD, 20));
		la_logout = new JLabel("로그아웃");
		la_logout.setFont(new Font("맑은 고딕", Font.BOLD, 20));

		if (headquartersUser != null) {
			la_user.setText(headquartersUser.getId());
		} else if (locationUser != null) {
			la_user.setText(locationUser.getId());
		}

		la_logout.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // 위쪽에 여백 추가
		la_user.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // 위쪽에 여백 추가
		p_north.add(la_user);
		p_north.add(la_logout);

		// 이벤트
		// 로그아웃
		la_logout.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (headquartersUser != null) {
					headquartersUser = null;
					new MemberLogin();
				} else if (locationUser != null) {
					locationUser = null;
					new MemberLogin();
				}
			}
		});

		// 본사 마이페이지
		la_user.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (headquartersUser != null) {
					showPage(Config.HEADQUATERS_MY_PAGE);
				} else if (locationUser != null) {
					showPage(Config.LOCATION_MY_PAGE);
				}
			}
		});
	}

	private void addMenuGroups() {
		if ("headquartersUser".equals(role)) {
			p_west.add(createMenuGroup("입고관리", false, la_inboundPlan, la_inboundDetail, la_inboundProcess));
			p_west.add(createMenuGroup("출고관리", false, la_outboundPlan, la_outboundDetail, la_outboundRegister));
			p_west.add(createMenuGroup("재고관리", false, la_product, la_inventory, la_warehouse));
			p_west.add(createMenuGroup("지점관리", false, la_branch));
			p_west.add(createMenuGroup("공급사관리", false, la_supplier));
			p_west.add(createMenuGroup("채팅", true, la_chat)); // 마지막만 하단 흰 줄 제거
		} else if ("locationUser".equals(role)) {
			p_west.add(createMenuGroup("주문하기", false, la_order, la_orderList));
			p_west.add(createMenuGroup("채팅하기", false, la_location_chat));

		}

	}

	private JLabel createMenuItem(String text, int pageIndex) {
		JLabel label = new JLabel(text);
		label.setForeground(Color.WHITE);
		label.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		label.setBorder(new EmptyBorder(5, 30, 5, 0));
		label.setCursor(new Cursor(Cursor.HAND_CURSOR));
		label.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// 페이지 이동 구현 예정
			}

			public void mouseEntered(MouseEvent e) {
				label.setForeground(Color.YELLOW);
			}

			public void mouseExited(MouseEvent e) {
				label.setForeground(Color.WHITE);
			}
		});
		return label;
	}

	/**
	 * @param title       그룹 이름
	 * @param noBottomBar true면 마지막 그룹이라 하단 선 제거
	 * @param items       메뉴 항목들
	 */
	private JPanel createMenuGroup(String title, boolean noBottomBar, JLabel... items) {
		JPanel group = new JPanel();
		group.setLayout(new BoxLayout(group, BoxLayout.Y_AXIS));
		group.setOpaque(false);

		JLabel groupTitle = new JLabel(title);
		groupTitle.setForeground(Color.WHITE);
		groupTitle.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		groupTitle.setBorder(new EmptyBorder(10, 15, 5, 0));

		group.add(Box.createVerticalStrut(10));
		group.add(groupTitle);
		group.add(Box.createVerticalStrut(5));
		for (JLabel item : items) {
			group.add(item);
		}
		group.add(Box.createVerticalStrut(10));

		Border outerBorder = noBottomBar ? BorderFactory.createEmptyBorder() // 마지막이면 하단 선 없음
				: BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE);

		group.setBorder(BorderFactory.createCompoundBorder(outerBorder, BorderFactory.createEmptyBorder(5, 0, 5, 0)));

		return group;
	}

	private void connect() {
		conn = dbManager.getConnection();
	}

	/**
	 * 쇼핑몰에 사용할 모든 페이지 생성 및 부착
	 */
	public void createPage() {

		if ("headquartersUser".equals(role)) {
			pages = new Page[14];

			pages[0] = new MainPage(this);
			pages[1] = new ReceiptPage(this);
			pages[2] = new DetailPage(this);
			pages[3] = new ProcessPage(this);
			pages[4] = new OutboundReceiptPage(this);
			pages[5] = new OutboundDetailPage(this);
			pages[6] = new ProductPage(this);
			pages[7] = new InventoryPage(this);
			pages[8] = new WarehousePage(this);
			pages[9] = null;
			pages[10] = new SupplierPage(this);
			pages[11] = new ChattingPage(this);
			pages[12] = new HeadquatersMyPage(this,headquartersUser.getHeadquartersUserId());
			pages[13] = new OutboundRegiterPage(this);

		} else if ("locationUser".equals(role)) {
			pages = new Page[4];

			pages[0] = new OrderPage(this);
			pages[1] = new OrderListPage(this);
			pages[2] = new LocatoinMyPage(this, locationUser.getLocationUserId());
			pages[3] = null;
		}

		for (int i = 0; i < pages.length; i++) {
			if (pages[i] != null) {
				p_content.add(pages[i]);
			}
		}
	}

	/**
	 * 부착된 페이지들을 대상으로, 어떤 페이지들을 보여줄지 결정하는 메서드
	 */
	public void showPage(int target) {

		for (int i = 0; i < pages.length; i++) {
			if (pages[i] != null) {
				pages[i].setVisible(i == target);
			}
		}
	}
}
//    public static void main(String[] args) {
//        new AppMain();
//    }
