package com.shinlogis.wms.inoutbound.inbound.view.process;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import com.shinlogis.wms.inoutbound.inbound.repository.DetailDAO;
import com.shinlogis.wms.inoutbound.inbound.repository.ReceiptDAO;
import com.shinlogis.wms.inoutbound.model.IODetail;
import com.shinlogis.wms.snapshot.model.Snapshot;

/**
 * InboundProcessModel 테이블모델 입니다.
 * 페이징 기능 추가됨
 * 
 * @author 김예진
 */
public class ProcessModel extends AbstractTableModel {
    DetailDAO ioDetailDAO = new DetailDAO();
    ReceiptDAO ioReceiptDAO = new ReceiptDAO();
    Snapshot snapshot = new Snapshot();

    // 전체 데이터 리스트 (페이징 전 데이터)
    private List<IODetail> fullDataList = new ArrayList<>();

    // 현재 페이지에 보여줄 데이터 리스트
    private List<IODetail> pagedDataList = new ArrayList<>();

    String[] column = { "입고상세ID", "상품코드", "상품명", "예정입고수량", "파손코드", "파손수량",
            "실제입고수량", "저장창고", "처리일자", "담당자"};

    public ProcessModel() {
        // 기본 생성 시 전체 데이터 모두 불러옴 (빈 필터)
        this.fullDataList = ioDetailDAO.selectProcess(Map.of());
        // 기본적으로 첫 페이지는 빈 데이터로 세팅
        this.pagedDataList = new ArrayList<>();
    }

    /**
     * rowIndex의 io_detail 데이터를 가져오는 메서드입니다
     * @author 김예진
     * @since 2025-06-22
     * @param rowIndex
     * @return
     */
    public IODetail getIODetailAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < pagedDataList.size()) {
            return pagedDataList.get(rowIndex);
        } else {
            return null;
        }
    }

    /**
     * 전체 데이터 로드 (필터 적용)
     * @author 김예진
     * @since 2025-06-27
     * @param filters 필터 조건
     * @return 전체 데이터 리스트
     */
    public List<IODetail> loadFullData(Map<String, Object> filters) {
        this.fullDataList = ioDetailDAO.selectProcess(filters);
        return fullDataList;
    }

    /**
     * 페이징 데이터 설정
     * @author 김예진
     * @since 2025-06-27
     * @param pagedList 현재 페이지에 보여줄 데이터
     */
    public void setData(List<IODetail> pagedList) {
        this.pagedDataList = pagedList;
        fireTableDataChanged();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        // 편집 가능 컬럼들 반환 (필요에 따라 조정)
        return column == 9; // 담당자 컬럼만 편집 가능 예시
    }

    @Override
    public int getRowCount() {
        return pagedDataList.size();
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
    public Object getValueAt(int rowIndex, int columnIndex) {
        IODetail ioDetail = pagedDataList.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return ioDetail.getIoDetailId();
            case 1:
                return ioDetail.getProductSnapshot().getProductCode();
            case 2:
                return ioDetail.getProductSnapshot().getProductName();
            case 3:
                return ioDetail.getPlannedQuantity();
            case 4:
                return ioDetail.getDamagedCode() != null ? ioDetail.getDamagedCode().getName() : "";
            case 5:
                return ioDetail.getDamageQuantity();
            case 6:
                return ioDetail.getActualQuantity();
            case 7:
                return ioDetail.getWarehouse() != null ? ioDetail.getWarehouse().getWarehouseCode() : "";
            case 8:
                return ioDetail.getProccessedDate();
            case 9:
                return ioDetail.getUser() != null ? ioDetail.getUser().getId() : "";
            default:
                return "";
        }
    }
}
