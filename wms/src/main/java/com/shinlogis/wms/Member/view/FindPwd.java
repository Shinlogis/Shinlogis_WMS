package com.shinlogis.wms.Member.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.HeadlessException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.shinlogis.wms.common.Exception.EmailException;
import com.shinlogis.wms.common.Exception.HeadquartersException;
import com.shinlogis.wms.common.config.Config;
import com.shinlogis.wms.common.util.MailSender;
import com.shinlogis.wms.headquarters.repository.HeadquartersDAO;
import com.shinlogis.wms.location.repository.LocationUserDAO;

public class FindPwd extends JFrame {

	JPanel p_center;

	JLabel la_findPWD;
	JLabel la_id;
	JTextField t_id;
	JLabel la_email;
	JLabel la_at;
	JTextField t_email;
	JComboBox cb_email;

	JPanel p_north;
	JButton bt;

	HeadquartersDAO headquartersDAO;
	LocationUserDAO locationUserDAO;

	public FindPwd() {
		p_center = new JPanel();
		la_findPWD = new JLabel("비밀번호 찾기");
		la_id = new JLabel("아이디");
		t_id = new JTextField();
		la_email = new JLabel("이메일");
		la_at = new JLabel("@");
		t_email = new JTextField();
		cb_email = new JComboBox();
		bt = new JButton("찾기");
		p_north = new JPanel(new FlowLayout());

		headquartersDAO = new HeadquartersDAO();
		locationUserDAO = new LocationUserDAO();

		getContentPane().setBackground(Color.WHITE);
		this.setLayout(new java.awt.GridBagLayout());
		p_center = new JPanel(new FlowLayout());
		p_center.setLayout(new BoxLayout(p_center, BoxLayout.Y_AXIS));
		p_center.setPreferredSize(new Dimension(500, 300));
		p_center.setBackground(Color.WHITE);
		p_center.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));

		// 스타일
		la_findPWD.setFont(new Font("맑은고딕", Font.BOLD, 24));
		la_findPWD.setHorizontalAlignment(JLabel.CENTER);

		cb_email.addItem("naver.com");
		cb_email.addItem("daum.com");
		cb_email.addItem("gmail.com");

		// 조립
		p_center.add(createCenterLine(la_findPWD));
		p_center.add(createLine(la_id, t_id));
		p_center.add(createEmailLine(la_email, t_email, la_at, cb_email));
		// 버튼
		p_north.setOpaque(false);
		p_north.add(bt);
		p_center.add(p_north);

		add(p_center);

		// 이벤트
		bt.addActionListener(e -> {
			findPwdByIdEmail();
		});

		setBounds(200, 100, Config.ADMINMAIN_WIDTH, Config.ADMINMAIN_HEIGHT);
		setVisible(true);

	}

	// 아이디
	public JPanel createLine(JLabel label, JTextField field) {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setPreferredSize(new Dimension(400, 10)); // 줄 높이 조절
		label.setPreferredSize(new Dimension(80, 30)); // 라벨 고정 폭
		field.setPreferredSize(new Dimension(180, 30)); // 필드 고정 폭
		panel.add(label);
		panel.add(field);
		return panel;
	}

	// 이메일
	public JPanel createEmailLine(JLabel label, JTextField field, JLabel at, JComboBox box) {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setPreferredSize(new Dimension(400, 10)); // 줄 높이 조절
		label.setPreferredSize(new Dimension(50, 30));
		field.setPreferredSize(new Dimension(180, 30));
		at.setPreferredSize(new Dimension(20, 30));
		box.setPreferredSize(new Dimension(120, 30));
		panel.add(label);
		panel.add(field);
		panel.add(at);
		panel.add(box);
		return panel;
	}

	// 가운데 정렬용 라벨 생성 메서드
	public JPanel createCenterLine(JComponent comp) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 1));
		panel.setPreferredSize(new Dimension(400, 40)); // 너비와 높이 확보
		panel.setOpaque(false);
		comp.setPreferredSize(new Dimension(200, 40)); // 중앙에 적절한 너비 설정

		panel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0)); // 위쪽 여백

		panel.add(comp);
		return panel;
	}

	// 비밀번호 찾기
	public void findPwdByIdEmail() throws HeadquartersException{
		try {
			String pwd = headquartersDAO.findPwd(t_id.getText(), t_email.getText() + "@" + (String) cb_email.getSelectedItem());
			
			
			if (pwd != null) {

				String title = "임시 비밀번호 안내";
				String content = "<h2>[WMS] 임시 비밀번호 발급</h2>" + "<p>요청하신 임시 비밀번호는 아래와 같습니다:</p>"
						+ "<p><strong style='font-size:18px;'>" + pwd + "</strong></p>"
						+ "<p>로그인 후 반드시 비밀번호를 변경해 주세요.</p>";

				MailSender sender = new MailSender(); // 메일 세션 준비
				String targetMail = t_email.getText() + "@" + (String)cb_email.getSelectedItem();
				sender.sendHtml(targetMail, title, content); // HTML 메일 전송

				JOptionPane.showMessageDialog(this, "임시 비밀번호가 메일로 전송되었습니다.");
			} else {
				JOptionPane.showMessageDialog(this, "일치하는 계정이 없습니다.");
			}

		} catch (HeadquartersException e) {
			e.printStackTrace();
			throw new HeadquartersException(e.getMessage(), e);
		} catch (EmailException e) {
			e.printStackTrace();
			throw new EmailException(e.getMessage(), e);
		}

	}

}
