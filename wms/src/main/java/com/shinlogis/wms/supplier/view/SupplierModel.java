package com.shinlogis.wms.supplier.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.shinlogis.wms.supplier.model.Supplier;
import com.shinlogis.wms.supplier.repository.SupplierDAO;

public class SupplierModel extends AbstractTableModel{
	
	SupplierDAO supplierDAO;
	List<Supplier> list;
	String[] column = {"번호", "공급사명", "주소"};
	
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
		
		String value = null;
		
		switch(col) {
		case 0:value=Integer.toString(supplier.getSupplierId()); break;
		case 1:value = supplier.getName(); break;
		case 2:value = supplier.getAddress(); break;
		}
		
		return value;
	}

}
