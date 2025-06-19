package com.shinlogis.wms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
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
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.shinlogis.wms.common.config.Config;
import com.shinlogis.wms.common.config.Page;
import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.inOutBound.view.InboundPlanPage;
import com.shinlogis.wms.main.view.MainPage;

public class AppMain extends JFrame {
    JPanel p_west, p_center, p_north, p_content;
    JLabel la_inboundPlan, la_inboundDetail, la_inboundProcess;
    JLabel la_outboundPlan, la_outboundDetail;
    JLabel la_inventory, la_container, la_branch, la_supplier, la_chat;
	Page[] pages;

    DBManager dbManager = DBManager.getInstance();
    public Connection conn;
    //public Admin admin = new Admin();

    public AppMain() {
        initUI();
        connect();
        createPage();
        setBounds(200, 100, Config.ADMINMAIN_WIDTH, Config.ADMINMAIN_HEIGHT);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dbManager.release(conn);
                System.exit(0);
            }
        });
    }

    private void initUI() {
        // 메인 패널 초기화
        p_center = new JPanel(new BorderLayout());
        p_west = new JPanel();
        p_north = new JPanel();
        p_content = new JPanel();

        // 상단 바 설정
        p_north.setPreferredSize(new Dimension(Config.ADMINMAIN_WIDTH - Config.SIDE_WIDTH, Config.HEADER_HEIGHT));
        p_north.setBackground(Color.YELLOW);

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
      
    }

    private void createSidebar() {
        p_west.setPreferredSize(new Dimension(Config.SIDE_WIDTH, Config.SIDE_HEIGHT));
        p_west.setBackground(new Color(0xFF7F50));
        p_west.setLayout(new BoxLayout(p_west, BoxLayout.Y_AXIS));
        p_west.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        p_west.add(Box.createVerticalStrut(40)); // 40픽셀 아래로 내림
        
        // 메뉴 항목 생성
        la_inboundPlan = createMenuItem("입고 예정", Config.INBOUND_PLAN_PAGE);
        la_inboundDetail = createMenuItem("입고 상세", Config.PRODUCT_PAGE);
        la_inboundProcess = createMenuItem("입고 처리", Config.INBOUND_PLAN_PAGE);
        la_outboundPlan = createMenuItem("출고 예정", Config.INBOUND_ITEM_PAGE);
        la_outboundDetail = createMenuItem("출고 상세", Config.INBOUND_ITEM_PAGE);
        la_inventory = createMenuItem("재고 관리", Config.INBOUND_PROCESS_PAGE);
        la_container = createMenuItem("창고 관리", Config.INBOUND_PROCESS_PAGE);
        la_branch = createMenuItem("지점 관리", Config.INBOUND_PROCESS_PAGE);
        la_supplier = createMenuItem("공급사 관리", Config.OUTBOUNT_PROCESS_PAGE);
        la_chat = createMenuItem("지점과 채팅하기", Config.OUTBOUND_PLAN_PAGE);

        // 이벤트 연결
        la_inboundPlan.addMouseListener(new MouseAdapter() {
      		@Override
      		public void mouseClicked(MouseEvent e) {
      			showPage(Config.INBOUND_PLAN_PAGE);
      			System.out.println("click");
      		}
      	});

        // 메뉴 그룹 추가
        addMenuGroups();
    }

    private void addMenuGroups() {
        p_west.add(createMenuGroup("입고관리", false, la_inboundPlan, la_inboundDetail, la_inboundProcess));
        p_west.add(createMenuGroup("출고관리", false, la_outboundPlan, la_outboundDetail));
        p_west.add(createMenuGroup("재고관리", false, la_inventory, la_container));
        p_west.add(createMenuGroup("지점관리", false, la_branch));
        p_west.add(createMenuGroup("공급사관리", false, la_supplier));
        p_west.add(createMenuGroup("채팅", true, la_chat)); // 마지막만 하단 흰 줄 제거
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

        Border outerBorder = noBottomBar
                ? BorderFactory.createEmptyBorder() // 마지막이면 하단 선 없음
                : BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE);

        group.setBorder(BorderFactory.createCompoundBorder(
                outerBorder,
                BorderFactory.createEmptyBorder(5, 0, 5, 0)
        ));

        return group;
    }

    private void connect() {
        conn = dbManager.getConnection();
    }
    
	/**
	 * 쇼핑몰에 사용할 모든 페이지 생성 및 부착
	 */
	public void createPage() {
		pages = new Page[2];

		pages[0] = new MainPage(this);
		pages[1] = new InboundPlanPage(this);
		

		for (int i = 0; i < pages.length; i++) {
			p_content.add(pages[i]);
		}
	}

	/**
	 * 부착된 페이지들을 대상으로, 어떤 페이지들을 보여줄지 결정하는 메서드
	 */
	public void showPage(int target) {

		for (int i = 0; i < pages.length; i++) {
			pages[i].setVisible((i == target) ? true : false);
		}
	}

    public static void main(String[] args) {
        new AppMain();
    }
}