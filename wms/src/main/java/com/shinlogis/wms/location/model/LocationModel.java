package com.shinlogis.wms.location.model;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import com.shinlogis.wms.common.Exception.LocationException;
import com.shinlogis.wms.location.repository.LocationDAO;
import com.shinlogis.wms.product.model.Product;

public class LocationModel extends  AbstractTableModel{
	Location location;
	
	public List<Location> list;

	String[] column = { "지점ID", "지점명", "위치", "상태"};

	public LocationModel() {
		LocationDAO locationDao = new LocationDAO();
		list = locationDao.getLocation();
	}

	@Override
	public int getColumnCount() {
		return column.length;
	}

	public String getColumnName(int col) {
		return column[col];
	}

	@Override
	public int getRowCount() {
		return list.size();
	}

	// 행열에 값 넣기
	@Override
	public Object getValueAt(int row, int col) {
		location = list.get(row);

		switch (col) {
		case 0:
			return location.getLocationId();
		case 1:
			return location.getLocationName();
		case 2:
			return location.getAddress();
		case 3:
			return location.getStatus();
		default:
			return null;
		}
	}
	
	@Override
	public void setValueAt(Object value, int row, int col) {
	    if (col == 3) {
	        String status = value.toString();
	        Location location = list.get(row); // 해당 행의 객체 가져오기
	        location.setStatus(status);        // 상태 변경
	        
	        LocationDAO locationDao = new LocationDAO();
	        
	        try {
				locationDao.update(location.getLocationId(), status);
				JOptionPane.showMessageDialog(null,status+" 상태변경에 성공하였습니다");
			} catch (LocationException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				JOptionPane.showMessageDialog(null, e.getMessage());
				list = locationDao.getLocation();
				setList(list);
			} 
	    }
	    fireTableCellUpdated(row, col);
	}
	@Override
	public boolean isCellEditable(int row, int col) {
		
		return col==3;
	}

	// 데이터 갱신
	public void setList(List<Location> newList) {
		this.list = newList;
		fireTableDataChanged(); // JTable에 데이터 변경 알림
	}
}
