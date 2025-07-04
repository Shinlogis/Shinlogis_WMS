package com.shinlogis.wms.Member.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

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

import com.shinlogis.wms.common.Exception.HeadquartersException;
import com.shinlogis.wms.common.config.Config;
import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.headquarters.model.HeadquartersUser;
import com.shinlogis.wms.headquarters.repository.HeadquartersDAO;

public class HeadquartersJoin extends JFrame {

	JPanel p_center;

	JLabel la_join;
	JLabel la_id;
	JButton bt_idCheck;
	JLabel la_pwd;
	JLabel la_pwdCheck;
	JLabel la_email;
	JLabel la_at;

	JTextField t_id;
	JPasswordField t_pwd;
	JPasswordField t_pwdCheck;
	JTextField t_email;
	JComboBox cb_email;

	JButton bt_join;

	DBManager dbManager = DBManager.getInstance();
	HeadquartersDAO headquartersDAO;
	boolean idCheck = false;
	MemberLogin memberLogin;

	public HeadquartersJoin(MemberLogin memberLogin) {
		this.memberLogin = memberLogin;
		p_center = new JPanel();

		getContentPane().setBackground(Color.WHITE);
		this.setLayout(new GridBagLayout());
		p_center.setLayout(new BoxLayout(p_center, BoxLayout.Y_AXIS));
		p_center.setPreferredSize(new Dimension(500, 500));
		p_center.setBackground(Color.WHITE);
		p_center.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));

		la_join = new JLabel("본사 회원가입");
		la_id = new JLabel("아이디");
		bt_idCheck = new JButton("확인");
		la_pwd = new JLabel("비밀번호");
		la_pwdCheck = new JLabel("비밀번호 확인");
		la_email = new JLabel("이메일");
		la_at = new JLabel("@");

		t_id = new JTextField();
		t_pwd = new JPasswordField();
		t_pwdCheck = new JPasswordField();
		t_email = new JTextField();
		cb_email = new JComboBox();

		bt_join = new JButton("회원가입");

		headquartersDAO = new HeadquartersDAO();

		// 스타일
		cb_email.addItem("naver.com");
		cb_email.addItem("daum.com");
		cb_email.addItem("google.com");
		la_join.setHorizontalAlignment(JLabel.CENTER);
		la_join.setFont(new Font("맑은고딕", Font.BOLD, 24));

		// 조립
		p_center.add(createCenterLine(la_join));
		p_center.add(createIdLine(la_id, t_id, bt_idCheck));
		p_center.add(createLine(la_pwd, t_pwd));
		p_center.add(createLine(la_pwdCheck, t_pwdCheck));
		p_center.add(createEmailLine(la_email, t_email, la_at, cb_email));
		p_center.add(createCenterLine(bt_join));

		// 이벤트
		// 아이디 체크
		bt_idCheck.addActionListener(e -> {
			idCheck();
			idCheck = true;
		});

		// 회원가입 버튼
		bt_join.addActionListener(e -> {
			checkValid();

		});

		setBounds(200, 100, Config.ADMINMAIN_WIDTH, Config.ADMINMAIN_HEIGHT);
		add(p_center);
		setVisible(true);
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

	// 아이디 확인
	public JPanel createIdLine(JLabel label, JTextField field, JButton bt) {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		label.setPreferredSize(new Dimension(50, 30));
		field.setPreferredSize(new Dimension(180, 30));
		bt.setPreferredSize(new Dimension(80, 30));
		panel.add(label);
		panel.add(field);
		panel.add(bt);
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
		comp.setPreferredSize(new Dimension(150, 30)); // 중앙에 적절한 너비 설정

		panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // 위쪽 여백

		panel.add(comp);
		return panel;
	}

	// 회원가입
	public void regist() {
		try {
			HeadquartersUser headquartersUser = new HeadquartersUser();
			headquartersUser.setId(t_id.getText());
			headquartersUser.setPw(new String(t_pwd.getPassword()));
			headquartersUser.setEmail(t_email.getText(), (String) cb_email.getSelectedItem());

			headquartersDAO.insert(headquartersUser);

		} catch (HeadquartersException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage());
		}

	}

	// 아이디 중복 검사
	public void idCheck() {
		if (headquartersDAO.checkId(t_id.getText())) {
			// if문의 값이 true이면 사용 가능한 아이디
			JOptionPane.showMessageDialog(this, "사용 가능한 아이디입니다.");
		} else {
			// false면 중복 아이디
			JOptionPane.showMessageDialog(this, "중복된 아이디입니다.");
		}
	}

	// 유효성 검사
	public void checkValid() {
		if (t_id.getText().length() == 0) {
			JOptionPane.showMessageDialog(this, "아이디를 입력하세요");
		} else if (t_pwd.getPassword().length == 0) {
			JOptionPane.showMessageDialog(this, "비밀번호를 입력하세요");
		} else if (!idCheck) {
			JOptionPane.showMessageDialog(this, "아이디 중복검사를 하세요");
		} else if (t_pwdCheck.getPassword().length == 0) {
			JOptionPane.showMessageDialog(this, "이메일을 입력하세요");
		} else if (t_email.getText().length() == 0) {
			JOptionPane.showMessageDialog(this, "이메일을 입력하세요");
		} else if (!headquartersDAO.checkId(t_id.getText())) {
			JOptionPane.showMessageDialog(this, "중복된 아이디입니다. 다른 아이디를 입력하세요");
			return;
		} else if (!Arrays.equals(t_pwd.getPassword(), t_pwdCheck.getPassword())) {
			JOptionPane.showMessageDialog(this, "비밀번호를 다시 확인해 주세요");
			return;
		} else if (!headquartersDAO.checkEmail(t_email.getText() + "@" + (String) cb_email.getSelectedItem())) {
			JOptionPane.showMessageDialog(this, "중복된 이메일입니다. 다시 확인해 주세요");
			return;
		} else {

			try {
				regist();
				JOptionPane.showMessageDialog(this, "회원가입이 완료되었습니다.");
				this.dispose();
				if(memberLogin != null) {
					memberLogin.dispose();
				}
				new MemberLogin();
			} catch (HeadquartersException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, e.getMessage());
			}

		}
	}

}