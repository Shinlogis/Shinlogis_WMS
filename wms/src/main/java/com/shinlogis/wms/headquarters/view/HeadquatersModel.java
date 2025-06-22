package com.shinlogis.wms.headquarters.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.shinlogis.wms.headquarters.model.HeadquartersUser;
import com.shinlogis.wms.headquarters.repository.HeadquartersDAO;

public class HeadquatersModel extends AbstractTableModel{
	String[] column = {"항목명", "값", "수정"};
	String[][] data;
	
	public HeadquatersModel(HeadquartersUser headquartersUser) {
		
        if (headquartersUser != null) {
            data = new String[][] {
                {"아이디", headquartersUser.getId(),""},
                {"비밀번호", headquartersUser.getPw(),""},
                {"이메일", headquartersUser.getEmail(),""}
            };
        } else {
            data = new String[0][0]; // null일 경우 빈 배열
        }
    }
	
	
	
	@Override
	public int getRowCount() {
		return data.length;
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
	public void setValueAt(Object value, int row, int col) {
		data[row][col] = (String)value;
		fireTableCellUpdated(row, col); //바뀐데이터 화면에 반영되게
		
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		return col == 1;
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		if(col == 1 && row ==1) {
			return "****";
		}
		
		if(col < 2) {
			return data[row][col];
		}else if(col == 2) {
			return "수정";
		}
		return null;
	}

}
