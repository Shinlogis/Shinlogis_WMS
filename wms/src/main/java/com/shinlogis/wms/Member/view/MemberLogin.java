package com.shinlogis.wms.Member.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.common.config.Config;
import com.shinlogis.wms.headquarters.model.HeadquartersUser;
import com.shinlogis.wms.headquarters.repository.HeadquartersDAO;
import com.shinlogis.wms.location.model.LocationUser;
import com.shinlogis.wms.location.repository.LocationDAO;
import com.shinlogis.wms.location.repository.LocationUserDAO;

public class MemberLogin extends JFrame{
	
	JPanel p_center;
	
	JLabel la_login;
	JLabel la_id;
	JLabel la_pwd;
	
	JTextField t_id;
	JPasswordField t_pwd;
	
	JPanel p_bt;
	JButton bt_admin;
	JButton bt_location;
	
	JLabel find_id;
	JLabel find_pwd;
	JLabel headquarters_join;
	JLabel location_join;
	
	HeadquartersDAO headquartersDAO;
	LocationUserDAO locationUserDAO;
	HeadquartersUser headquartersUser;
	LocationUser locationUser;
	
	public MemberLogin() {
		
		getContentPane().setBackground(Color.WHITE);
		this.setLayout(new GridBagLayout());
		p_center = new JPanel(new FlowLayout());
		p_center.setLayout(new BoxLayout(p_center, BoxLayout.Y_AXIS));
		p_center.setPreferredSize(new Dimension(400,400));
		p_center.setBackground(Color.WHITE);
		p_center.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2)); 
		
		la_login = new JLabel("로그인");
		la_id = new JLabel("아이디");
		la_pwd = new JLabel("비밀번호");
		
		t_id = new JTextField();
		t_pwd = new JPasswordField();
		
		find_id = new JLabel("아이디 찾기");
		find_pwd = new JLabel("비밀번호 찾기");
		headquarters_join = new JLabel("본사 회원가입");
		location_join = new JLabel("지점 회원가입");
	
		p_bt = new JPanel();
		bt_admin = new JButton("관리자");
		bt_location = new JButton("지점");
		
		locationUserDAO = new LocationUserDAO();
		headquartersDAO = new HeadquartersDAO();
		
		
		//스타일
		la_login.setHorizontalAlignment(JLabel.CENTER);
		la_login.setFont(new Font("맑은고딕", Font.BOLD, 24));
		
		Dimension d = new Dimension(200, 30);
		t_id.setPreferredSize(d);
		t_pwd.setPreferredSize(d);
		
		p_bt.setBackground(Color.WHITE);
		
		
		
		//제조
		p_center.add(createCenterLine(la_login));
		p_center.add(createLine(la_id, t_id));
		p_center.add(createLine(la_pwd, t_pwd));
		
		p_bt.add(bt_admin);
		p_bt.add(bt_location);
		p_center.add(p_bt);
		
		p_center.add(createFind(find_id, find_pwd));
		p_center.add(createFind(headquarters_join, location_join));
		
		
		add(p_center);
		
		

		//이벤트 
		//본사로 로그인
		bt_admin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				headquartersUserLogin();
				
			}
		});
		
		//지점으로 로그인 
		bt_location.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				locationUserLogin();
			}
		});
		
		
		//본사 회원가입
		headquarters_join.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new HeadquartersJoin();
			}
		});
		
		//지점 회원가입
		location_join.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new LocationJoin();
			}
		});
		
		//아이디 찾기
		find_id.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new FindId();
			}
		});
		
		//비밀번호 찾기
		find_pwd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new FindPwd();
			}
		});
		
		setBounds(200, 100, Config.ADMINMAIN_WIDTH, Config.ADMINMAIN_HEIGHT);
		setVisible(true);
	
	}
	
	//아이디
	public JPanel createLine(JLabel label, JTextField field) {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		label.setPreferredSize(new Dimension(80, 30));  // 라벨 고정 폭			field.setPreferredSize(new Dimension(180, 30)); // 필드 고정 폭
		panel.add(label);	
		panel.add(field);
		return panel;
	}
	
	//비밀번호
	public JPanel createPwdLine(JLabel label, JPasswordField field) {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		label.setPreferredSize(new Dimension(80, 30));  // 라벨 고정 폭
		field.setPreferredSize(new Dimension(180, 30)); // 필드 고정 폭
		panel.add(label);
		panel.add(field);
		return panel;
	}
		
		
		
	//아이디, 비밀번호 찾기, 본사, 지점 회원가입
	public JPanel createFind(JLabel label1, JLabel label2) {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		label1.setPreferredSize(new Dimension(80, 15));
		label2.setPreferredSize(new Dimension(80, 15));
		panel.add(label1);
		panel.add(label2);
		return panel;
	}

	// 가운데 정렬용 라벨 생성 메서드
	public JPanel createCenterLine(JComponent comp) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 1));
		panel.setPreferredSize(new Dimension(400, 40)); // 너비와 높이 확보
		panel.setOpaque(false);
		comp.setPreferredSize(new Dimension(100, 30));  // 중앙에 적절한 너비 설정
		
		panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); //위쪽 여백 
		
		panel.add(comp);
		return panel;
	}
	
	
	//본사 로그인
	public void headquartersUserLogin() {
		HeadquartersUser user = new HeadquartersUser();
		user.setId("admin");
		user.setPw("1234");
		//		user.setId(t_id.getText());
		//		user.setPw(new String(t_pwd.getPassword()));
		
		headquartersUser = headquartersDAO.Login(user);
	
		if(headquartersUser != null) {
			System.out.println("로그인 여부확인");
			JOptionPane.showMessageDialog(this, "로그인 완료");

			AppMain appMain=new AppMain(headquartersUser, null);
			appMain.initUI();
		}else {
			JOptionPane.showMessageDialog(this, "입력 정보를 다시 확인해 주세요");
			return;
		}
	}
	
	
	
	
	//지점 로그인
	public void locationUserLogin() {
		
		LocationUser user = new LocationUser();
		user.setId(t_id.getText());
		user.setPw(new String(t_pwd.getPassword()));
		
		locationUser = locationUserDAO.Login(user);
		
		if(locationUser != null) {
			JOptionPane.showMessageDialog(this, "로그인 완료");
			
			AppMain appMain=new AppMain(null, locationUser);
			appMain.initUI();
		}else {
			JOptionPane.showMessageDialog(this, "입력 정보를 다시 확인해 주세요");
			return;
		}
	}
	
	
	public static void main(String[] args) {
		new MemberLogin();
	}
	

}