package com.shinlogis.wms.headquarters.view;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import com.shinlogis.wms.headquarters.model.HeadquartersUser;
import com.shinlogis.wms.headquarters.repository.HeadquartersDAO;
import com.shinlogis.wms.supplier.model.Supplier;

public class HeadquaterModel extends AbstractTableModel{
	
	HeadquartersDAO headquartersDAO;
	List<HeadquartersUser> list;
	String[] column = { "선택", "번호", "아이디", "이메일", "수정" };

	public HeadquaterModel() {
		headquartersDAO = new HeadquartersDAO();
		list = headquartersDAO.headquaterList();
	}
	
	@Override
	public int getRowCount() {
		return list.size();
	}

	@Override
	public int getColumnCount() {
		return column.length;
	}

	@Override
	public String getColumnName(int col) {
		return column[col];
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		HeadquartersUser headquartersUser = list.get(row);

		switch (col) {
		case 0:
			return headquartersUser.isChecked();
		case 1:
			return Integer.toString(headquartersUser.getHeadquartersUserId());
		case 2:
			return headquartersUser.getId();
		case 3:
			return headquartersUser.getEmail();
		case 4:
			return "수정";
		default:
			return null;
		}

	}
	
	public HeadquartersUser getHeaquaterAt(int rowIndex) {
		if (rowIndex >= 0 && rowIndex < list.size()) {
			return list.get(rowIndex);
		} else {
			return null;
		}
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		return col == 0 || col == 4;
	}
	
	@Override
	public Class<?> getColumnClass(int col) {
		switch (col) {
		case 0:
			return Boolean.class; // 체크박스 열
		case 1:
			return Integer.class; //pk값 
		case 2:
			return String.class; // 아이디
		case 3:
			return String.class; // 이메일
		case 4:
			return String.class; //수정
		default:
			return Object.class;
		}
	}
	
	@Override
	public void setValueAt(Object value, int row, int col) {
		if (col == 0) { // 체크박스 열이면
			list.get(row).setChecked((Boolean) value); // 공급사 객체에 체크 상태 반영
			fireTableCellUpdated(row, col); // 변경 알림
		}
	}
	
	//테이블 삭제
	public void deleteHeaquater() {
		boolean hasChecked = false;
		
		for(HeadquartersUser user : list) {
			if(user.isChecked()) {
				hasChecked = true;
				user.setStatus("탈퇴");
				try {
					headquartersDAO.delete(user);
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
			}
		}
		
		if(!hasChecked) {
			JOptionPane.showMessageDialog(null, "삭제할 회원을 선택해 주세요");
		} else {
			
			JOptionPane.showMessageDialog(null, "회원 삭제에 성공하였습니다.");
			list = headquartersDAO.headquaterList();
			fireTableDataChanged();
		}
		
	
	}
	
	
	
	// 테이블 새로 갱신
		public void tableChanged() {
			list = headquartersDAO.headquaterList();
			fireTableDataChanged();
		}

}
