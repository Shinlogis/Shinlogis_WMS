package com.shinlogis.wms.inoutbound.outbound.view;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.shinlogis.locationuser.order.model.StoreOrder;
import com.shinlogis.locationuser.order.repository.StoreOrderDAO;
import com.shinlogis.wms.inoutbound.outbound.model.Order;
import com.shinlogis.wms.inoutbound.outbound.repository.OrderDAO;
import com.shinlogis.wms.product.repository.ProductDAO;

/**
 * <h2>출고페이지에서 주문조회에 뿌려줄 모델
 * <li>DAO 및 메서드일체 인용 @author 예닮
 * 
 * @author 이세형
 */

public class OrderModel extends AbstractTableModel {
	OrderDAO orderDAO;
	Order order;
	ProductDAO productDAO;
	public List<Order> list;

	String[] column = { "주문ID", "주문 품목", "주문 일시", "주문 지점", "주문 상세", "출고 등록" };

	public OrderModel() {
		orderDAO = new OrderDAO();
		list = orderDAO.selectAll();
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
		// Product product=list.get(row);
		Order order = list.get(row);

		switch (col) {
		case 0:
			return Integer.toString(order.getStoreOrderId());
		case 1:
			if (order.getItems() == null) {
				return 0;
			} else if (order.getItems().size() == 1)
				return order.getItems().get(0).getProduct().getProductName();
			else {
				return order.getItems().get(0).getProduct().getProductName() + " 외 "
						+ (order.getItems().size() - 1) + "건";
			}
		case 2:
			try {
				String dateStr = order.getOrderDate(); // 예: "2025-06-27 12:34:56"
				SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 입력값 형식
				SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd"); // 출력 형식 (시간 제외)

				java.util.Date parsedDate = inputFormat.parse(dateStr);
				return outputFormat.format(parsedDate);
			} catch (Exception e) {
				e.printStackTrace();
				return order.getOrderDate(); // 파싱 실패 시 원본 문자열 그대로 출력
			}

		case 3:
			return order.getLocation().getLocationName();
		case 4:
			return "주문 상세";
		case 5:
			return "출고 등록";
		default:
			return null;
		}
	}
	@Override
	public boolean isCellEditable(int row, int column) {
		//columnb값을 받아오는것이기 때문에 &&조건을 쓰면 절대 불가능하게 만드는 것
		//따라서 || 해줘야 두개 모두의 editable을 얻을 수 있다.
	    return column == 4 || column == 5; // 버튼 컬럼만 클릭 가능하게
	}

	@Override
	public Class<?> getColumnClass(int col) {
		switch (col) {
		case 0:
			return String.class; // 주문ID
		case 1:
			return String.class; // 주문품목
		case 2:
			return String.class; // 주문일시
		case 3:
			return Object.class; // 총가격
		case 4:
			return Object.class; // 상세보기 버튼 받기
		case 5:
			return Object.class; // 등록 버튼 받기
		default:
			return Object.class;
		}
	}

	// 데이터 갱신
	public void setList(List<Order> newList) {
		this.list = newList;
		fireTableDataChanged(); // JTable에 데이터 변경 알림
	}
}
