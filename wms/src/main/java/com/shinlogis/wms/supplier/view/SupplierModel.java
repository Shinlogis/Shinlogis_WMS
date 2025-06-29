package com.shinlogis.wms.supplier.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

import com.shinlogis.wms.common.Exception.SupplierException;
import com.shinlogis.wms.inoutbound.model.IODetail;
import com.shinlogis.wms.supplier.model.Supplier;
import com.shinlogis.wms.supplier.repository.SupplierDAO;

public class SupplierModel extends AbstractTableModel {

	SupplierDAO supplierDAO;
	List<Supplier> list;
	String[] column = { "선택", "번호", "공급사명", "주소", "수정" };

	public SupplierModel() {
		supplierDAO = new SupplierDAO();
		list = supplierDAO.showSuppliers();
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
		Supplier supplier = list.get(row);

		switch (col) {
		case 0:
			return supplier.isChecked();
		case 1:
			return Integer.toString(supplier.getSupplierId());
		case 2:
			return supplier.getName();
		case 3:
			return supplier.getAddress();
		case 4:
			return "수정";
		default:
			return null;
		}

	}

	public Supplier getSupplierAt(int rowIndex) {
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
			return String.class; //공급사명
		case 3:
			return String.class; //주소
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

	// 테이블 삭제
	public void deleteSupplier() {

		boolean hasChecked = false;
		
		for (Supplier supplier : list) {
			if (supplier.isChecked()) {
				hasChecked = true;
				supplier.setStatus("비활성");
				try {
					supplierDAO.deleteSupplier(supplier);
				} catch (SupplierException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
			}
		}
		
		if(!hasChecked) {
			JOptionPane.showMessageDialog(null, "삭제할 공급사를 선택해 주세요");
			return;
		}
		
		JOptionPane.showMessageDialog(null, "삭제에 성공하였습니다.");
		list = supplierDAO.showSuppliers();
		fireTableDataChanged();
	}
	
	

	// 테이블 새로 갱신
	public void tableChanged() {
		list = supplierDAO.showSuppliers();
		fireTableDataChanged();
	}

}
