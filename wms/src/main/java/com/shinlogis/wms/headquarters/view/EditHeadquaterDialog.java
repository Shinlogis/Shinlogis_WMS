package com.shinlogis.wms.headquarters.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.common.Exception.HeadquartersException;
import com.shinlogis.wms.common.util.StringUtil;
import com.shinlogis.wms.headquarters.model.HeadquartersUser;
import com.shinlogis.wms.headquarters.repository.HeadquartersDAO;
import com.shinlogis.wms.supplier.model.Supplier;
import com.shinlogis.wms.supplier.repository.SupplierDAO;
import com.shinlogis.wms.supplier.view.SupplierModel;

public class EditHeadquaterDialog extends JDialog {
	JPanel panel;
	JLabel la_id;
	JLabel show_id;
	JLabel la_email;
	JTextField t_email;
	JLabel la_at;
	JComboBox cb_email;
	
	JPanel p_south;
	JButton bt;
	JButton bt_reset;

	HeadquartersDAO headquartersDAO;
	HeadquaterModel headquaterModel;
	HeadquartersUser headquartersUser;

	public EditHeadquaterDialog(AppMain appMain, HeadquartersUser headquartersUser, HeadquaterModel headquaterModel) {
		super(appMain, "회원 수정", true);

		this.headquaterModel = headquaterModel;
		this.headquartersUser = headquartersUser;

		this.setLocationRelativeTo(appMain);

		headquartersDAO = new HeadquartersDAO();

		panel = new JPanel();
		la_id = new JLabel("아이디");
		show_id = new JLabel(headquartersUser.getId());
		la_email = new JLabel("이메일");
		
		String[] email = headquartersUser.getEmail().split("@");
		t_email = new JTextField(email[0]);
		la_at = new JLabel("@");
		cb_email = new JComboBox();
		
		p_south = new JPanel();
		bt = new JButton("수정");
		bt_reset = new JButton("비밀번호 초기화");

		// 스타일
		cb_email.addItem("naver.com");
		cb_email.addItem("daum.com");
		cb_email.addItem("google.com");
		panel.setBackground(Color.WHITE);
		p_south.setBackground(Color.WHITE);
		bt.setPreferredSize(new Dimension(60, 40));
		bt_reset.setPreferredSize(new Dimension(100, 40));

		// 조립
		panel.add(createLine(la_id, show_id));
		panel.add(createEmailLine(la_email,t_email, la_at, cb_email));
		p_south.add(bt);
		p_south.add(bt_reset);
		this.add(panel);
		this.add(p_south, BorderLayout.SOUTH);
		

		//이벤트
		bt.addActionListener(e -> {
			editHeaquater();
		});
		
		bt_reset.addActionListener(e->{
			resetPwd();
		});

		this.setBounds(550, 350, 700,200);
		this.setVisible(true);
	}

	// label, text 크기 조절
	public JPanel createLine(JLabel label, JLabel field) {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		label.setPreferredSize(new Dimension(100, 30)); // 라벨 고정 폭
		field.setPreferredSize(new Dimension(300, 30)); // 필드 고정 폭
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

	
	//수정
	public void editHeaquater() {
		
		try {
			headquartersUser.setEmail(t_email.getText(), (String)cb_email.getSelectedItem());
			headquartersDAO.edit(headquartersUser);
			
			JOptionPane.showMessageDialog(this, "회원정보가 수정되었습니다.");
			this.dispose();
			headquaterModel.tableChanged();
			
		} catch (HeadquartersException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	//비밀번호 0000으로 초기화 
	public void resetPwd() {
		
		int result = JOptionPane.showConfirmDialog(this, "정말 초기화 하시겠습니까?","비밀번호 초기화 확인", JOptionPane.YES_NO_OPTION);
		
		if(result == JOptionPane.YES_OPTION) {
			String pwd = StringUtil.getSecuredPass("0000");
			headquartersUser.setPw(pwd);
			
			try {
				headquartersDAO.updatePwd(headquartersUser);
				JOptionPane.showMessageDialog(this, "비밀번호가 0000으로 초기화 되었습니다.");
				
				this.dispose();
				headquaterModel.tableChanged();
			} catch (HeadquartersException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, e.getMessage());
			}
		}else {
			this.dispose();
		}
		
	}
	
	
}
