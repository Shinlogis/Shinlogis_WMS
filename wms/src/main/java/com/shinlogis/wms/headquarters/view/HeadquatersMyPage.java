
package com.shinlogis.wms.headquarters.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellRenderer;

import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.common.Exception.HeadquartersException;
import com.shinlogis.wms.common.config.Config;
import com.shinlogis.wms.common.config.Page;
import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.headquarters.model.HeadquartersUser;
import com.shinlogis.wms.headquarters.repository.HeadquartersDAO;
import com.shinlogis.wms.Member.view.MemberLogin;

public class HeadquatersMyPage extends Page {

	JPanel pPageName;
	JLabel laPageName;

	JPanel p_center;

	JLabel la_update;
	JLabel la_id;
	JLabel la_pwd;
	JLabel la_pwdCheck;
	JLabel la_email;
	JLabel la_at;

	JLabel show_id;
	JPasswordField t_pwd;
	JPasswordField t_pwdCheck;
	JTextField t_email;
	JComboBox cb_email;

	JButton bt_update;
	JButton bt_delete;

	DBManager dbManager = DBManager.getInstance();
	private HeadquartersUser headquartersUser;
	private HeadquartersDAO headquartersDAO;
	int pk = 0;

	public HeadquatersMyPage(AppMain appMain, int pk) {
		super(appMain);
		this.pk = pk;

		pPageName = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pPageName.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.PAGE_NAME_HEIGHT));
		laPageName = new JLabel("마이페이지");

		p_center = new JPanel();

		this.setLayout(new FlowLayout());
		p_center.setLayout(new BoxLayout(p_center, BoxLayout.Y_AXIS));
		p_center.setPreferredSize(new Dimension(500, 500));
		p_center.setBackground(Color.WHITE);
		p_center.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));

		la_update = new JLabel("마이페이지");
		la_id = new JLabel("아이디");
		la_pwd = new JLabel("비밀번호");
		la_pwdCheck = new JLabel("비밀번호 확인");
		la_email = new JLabel("이메일");
		la_at = new JLabel("@");

		show_id = new JLabel();
		t_pwd = new JPasswordField();
		t_pwdCheck = new JPasswordField();
		t_email = new JTextField();
		cb_email = new JComboBox();

		bt_update = new JButton("수정");
		bt_delete = new JButton("탈퇴");

		headquartersDAO = new HeadquartersDAO();

		// 스타일
		setBackground(Color.LIGHT_GRAY);
		cb_email.addItem("naver.com");
		cb_email.addItem("daum.com");
		cb_email.addItem("google.com");
		la_update.setHorizontalAlignment(JLabel.CENTER);
		la_update.setFont(new Font("맑은고딕", Font.BOLD, 24));
		bt_update.setPreferredSize(new Dimension(80, 50));
		bt_delete.setPreferredSize(new Dimension(80, 50));

		// 조립
		pPageName.add(laPageName);
		add(pPageName);
		add(Box.createVerticalStrut(50)); // 50px만큼 아래로 밀기

		p_center.add(createCenterLine(la_update));
		p_center.add(createIdLine(la_id, show_id));
		p_center.add(createLine(la_pwd, t_pwd));
		p_center.add(createLine(la_pwdCheck, t_pwdCheck));
		p_center.add(createEmailLine(la_email, t_email, la_at, cb_email));
		p_center.add(createButtonLine(bt_update, bt_delete));
		add(p_center);

		// 이벤트
		// 수정 버튼
		bt_update.addActionListener(e -> {
			updateMyInfo();

		});
		
		//탈퇴 버튼 
		bt_delete.addActionListener(e ->{
			deleteMyInfo();
		});

		setBounds(200, 100, Config.ADMINMAIN_WIDTH, Config.ADMINMAIN_HEIGHT);
		showMyInfo();
	}

	// 아이디, 비밀번호
	public JPanel createLine(JLabel label, JPasswordField field) {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		label.setPreferredSize(new Dimension(80, 30)); // 라벨 고정 폭
		field.setPreferredSize(new Dimension(180, 30)); // 필드 고정 폭
		panel.add(label);
		panel.add(field);
		return panel;
	}

	// 아이디
	public JPanel createIdLine(JLabel label, JLabel field) {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		label.setPreferredSize(new Dimension(80, 30));
		field.setPreferredSize(new Dimension(180, 30));
		panel.add(label);
		panel.add(field);
		return panel;
	}

	// 이메일
	public JPanel createEmailLine(JLabel label, JTextField field, JLabel at, JComboBox box) {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		label.setPreferredSize(new Dimension(50, 30));
		field.setPreferredSize(new Dimension(180, 30));
		at.setPreferredSize(new Dimension(15, 30));
		box.setPreferredSize(new Dimension(120, 30));
		panel.add(label);
		panel.add(field);
		panel.add(at);
		panel.add(box);
		return panel;
	}

	// 가운데 정렬용 라벨 또는 버튼용 패널 생성 메서드
	public JPanel createCenterLine(JComponent comp) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 1));
		panel.setPreferredSize(new Dimension(400, 40)); // 높이 약간 더 확보
		panel.setOpaque(false);

		panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // 위쪽 여백

		panel.add(comp);
		return panel;
	}

	// 버튼 수정, 탈퇴
	public JPanel createButtonLine(JButton bt1, JButton bt2) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 1));
		panel.setPreferredSize(new Dimension(400, 40)); // 높이 약간 더 확보
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // 위쪽 여백

		panel.add(bt1);
		panel.add(bt2);
		return panel;
	}

	// 마이페이지 보여주기
	public void showMyInfo() {
		headquartersUser = headquartersDAO.selectById(pk);
		show_id.setText("a");
		t_pwd.setText("");
		t_pwdCheck.setText("");

//		String[] email = headquartersUser.getEmail().split("@");
//		t_email.setText(email[0]);

	}

	// 수정
	public void updateMyInfo(){
		String pw = new String(t_pwd.getPassword());
		String pwCheck = new String(t_pwdCheck.getPassword());
		String email = t_email.getText().trim() + "@" + ((String) cb_email.getSelectedItem()).trim();

		if (!pw.isEmpty()) {
			if (!pw.equals(pwCheck)) {
				JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다.");
				return;
			}
			headquartersUser.setPw(pw);
		} else {
			headquartersUser.setPw(null);
		}

		if (!headquartersUser.getEmail().trim().equals(email)) {
			if (!headquartersDAO.checkEmailById(email, pk)) {
				JOptionPane.showMessageDialog(this, "중복된 이메일입니다. 다시 확인해 주세요");
				return;
			}
			headquartersUser.setEmail(t_email.getText().trim(), (String) cb_email.getSelectedItem());
		}

		try {
			headquartersDAO.edit(headquartersUser);
			JOptionPane.showMessageDialog(this, "변경이 완료되었습니다.");
		} catch (HeadquartersException e) {
			e.printStackTrace();
			throw new HeadquartersException(e.getMessage());
		}

	}
	
	
	//탈퇴 
	public void deleteMyInfo() {
		try {
			Window window =  SwingUtilities.getWindowAncestor(this); //회원탈퇴 시 마이페이지 포함 앱메인 닫기
			headquartersDAO.delete(headquartersUser);
			JOptionPane.showMessageDialog(this, "회원 탈퇴 완료");
			headquartersUser = null;
			
			window.dispose();
			new MemberLogin();
		} catch (Exception e) {
			e.printStackTrace();
			throw new HeadquartersException(e.getMessage());
		}
	}

	
	
}
