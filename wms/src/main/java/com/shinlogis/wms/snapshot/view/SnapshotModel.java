package com.shinlogis.wms.snapshot.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.shinlogis.wms.snapshot.model.Snapshot;
import com.shinlogis.wms.snapshot.repository.SnapshotDAO;

/**
 * 스냅샷 테이블 모델입니다
 * @author 김예진
 * @since 2025-06-20
 */
public class SnapshotModel extends AbstractTableModel {
	
	SnapshotDAO snapshotDAO = new SnapshotDAO();

	List<Snapshot> snapshotList;
	String[] column = { "스냅샷ID", "상품코드", "상품명", "보관타입 코드", "공급사명", "가격", "유통기한"};
	
	public SnapshotModel() {
//		snapshotList = snapshotDAO
	}
	
	@Override
	public int getRowCount() {
		return 0;
	}
	@Override
	public int getColumnCount() {
		return 0;
	}
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return null;
	}
	

}
