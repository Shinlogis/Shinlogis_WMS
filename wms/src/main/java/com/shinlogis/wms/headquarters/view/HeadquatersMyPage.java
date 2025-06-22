package com.shinlogis.wms.headquarters.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.common.Exception.HeadquartersException;
import com.shinlogis.wms.common.config.Config;
import com.shinlogis.wms.common.config.Page;
import com.shinlogis.wms.headquarters.model.HeadquartersUser;
import com.shinlogis.wms.headquarters.repository.HeadquartersDAO;

public class HeadquatersMyPage extends Page{

	private JPanel pPageName;
	private JLabel laPageName;
	private JTable table;
	private JScrollPane scroll;
	
	private HeadquatersModel headquatersModel;
	private HeadquartersUser headquartersUser;
	private HeadquartersDAO headquartersDAO;
	
	public HeadquatersMyPage(AppMain appMain, int pk) {
		super(appMain);
		
		pPageName = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pPageName.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.PAGE_NAME_HEIGHT));
		laPageName = new JLabel("마이페이지");
		
		headquartersDAO = new HeadquartersDAO();
		headquartersUser = headquartersDAO.selectById(pk);
		
		headquatersModel = new HeadquatersModel(headquartersUser);
		
		table = new JTable(headquatersModel);
		scroll = new JScrollPane(table);
		
//		JButton renderBt = new JButton("수정"); //보여지기 용 
//		JButton editBt = new JButton("수정"); //편집용

//		table.getColumn("수정").setCellRenderer((tbl, value, isSelected, hasFocus, row, col) -> renderBt);
//
//		table.getColumn("수정").setCellEditor(new DefaultCellEditor(new JTextField()) {
//			int editingRow = -1;  // 클릭한 행 기억
//			{
//		    	editBt.addActionListener(e -> {
//		            //이벤트
//		    		editCell(editingRow);
//		    		fireEditingStopped(); // 버튼 클릭 후 편집 종료
//		        });
//		    }
//		    @Override
//		    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//		    	editingRow = row;
//		    	return editBt;
//		    }
//		});
		
		// 1) 버튼 렌더러 (버튼 모양 보여주기)
		table.getColumn("수정").setCellRenderer((tbl, value, isSelected, hasFocus, row, col) -> new JButton("수정"));

		// 2) 버튼 에디터 (버튼 클릭 이벤트 처리)
		table.getColumn("수정").setCellEditor(new DefaultCellEditor(new JTextField()) {
		    private JButton editBt = new JButton("수정");
		    private int editingRow = -1;

		    {
		        editBt.addActionListener(e -> {
		            System.out.println("버튼 클릭, editingRow = " + editingRow);
		            editCell(editingRow);
		            fireEditingStopped();
		        });
		        editorComponent = editBt;
		        clickCountToStart = 1;
		    }

		    @Override
		    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		        editingRow = row;
		        return editorComponent;
		    }

		    @Override
		    public Object getCellEditorValue() {
		        return "수정";
		    }
		});




		
		//스타일
		setBackground(Color.LIGHT_GRAY);
		table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 16));
		table.setFont(new Font("맑은 고딕", Font.PLAIN, 16)); 
		table.setRowHeight(80); // 행 높이 (간격) 키움
		scroll.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.TABLE_HEIGHT - 400)); 
		int editColumnIndex = table.getColumnModel().getColumnIndex("수정");
		table.getColumnModel().getColumn(editColumnIndex).setPreferredWidth(70);
		table.getColumnModel().getColumn(editColumnIndex).setMaxWidth(70);
		table.getColumnModel().getColumn(editColumnIndex).setMinWidth(70);


		//조립
		pPageName.add(laPageName);
		add(pPageName);
		add(Box.createVerticalStrut(200));  // 50px만큼 아래로 밀기
		add(scroll);
	}
	
	private void editCell(int row) {
		 if (row == -1) {
	            JOptionPane.showMessageDialog(this, "수정할 행이 선택되지 않았습니다.");
	            return;
	        }
		
		 System.out.println("editCell 호출, row=" + row);
		    String name = (String)headquatersModel.getValueAt(row, 0);
		    String value = (String)headquatersModel.getValueAt(row, 1); //사용자가 입력한 값
		    System.out.println("수정 필드: " + name + ", 수정 값: " + value);
		
		switch(name) {
		case "아이디": headquartersUser.setId(value); break;
		case "비밀번호": headquartersUser.setPw(value); break;
		case "이메일": String[] email = value.split("@");
			if(email.length == 2) {
				headquartersUser.setEmail(email[0], email[1]);
			}else {
				JOptionPane.showMessageDialog(this, "이메일 형식이 올바르지 않습니다.");
				return;
			} break;
		}
		
		try {
	        headquartersDAO.edit(headquartersUser);
	        JOptionPane.showMessageDialog(this, "수정이 완료되었습니다."); 
	    } catch (HeadquartersException e) {
	        JOptionPane.showMessageDialog(this, "수정 실패: " + e.getMessage());
	        e.printStackTrace();
	    }
	}
}
