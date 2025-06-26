package com.shinlogis.wms.common.config;

import javax.swing.*;
import java.awt.*;

/**
 * JTable의 셀의 버튼 클릭 시의 기능을 추가하는 셀 편집자 클래스입니다.
 * 사용 예:
 * table.getColumn("수정").setCellEditor(new ButtonEditor(new JCheckBox(), (table, row, column) -> {
 *     // 수정 버튼 클릭 시 실행할 동작
 * }));
 * InboundDetailPage.java의 테이블 영역 코드에 예시 있습니다.
 * 
 * @author 김예진
 * @since 2025-06-22
 */
public class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private String label;
    private boolean clicked;
    private int row;
    private JTable table;

    private ButtonClickListener listener;

    public interface ButtonClickListener {
        void onClick(JTable table, int row, int column);
    }

    /**
     * 생성자: 버튼 클릭 시 실행할 리스너를 외부에서 주입
     * 
     * @author 김예진
     * @param checkBox 편집기 부모 컴포넌트 
     * @param listener 버튼 클릭 시 실행할 사용자 정의 동작
     */
    public ButtonEditor(JCheckBox checkBox, ButtonClickListener listener) {
        super(checkBox);
        this.listener = listener;
        button = new JButton();
        button.setOpaque(true);

        // 버튼 클릭 시 편집 종료 → getCellEditorValue() 호출 유도
        button.addActionListener(e -> {
            fireEditingStopped(); // 이거 호출해야 getCellEditorValue가 실행됨
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.label = (value != null) ? value.toString() : "버튼";
        this.button.setText(label);
        this.table = table;
        this.row = row;
        this.clicked = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (clicked && listener != null) {
            // 유효한 행(row)인지 검사
            if (table != null && row >= 0 && row < table.getRowCount()) {
                listener.onClick(table, row, table.getEditingColumn());
            } else {
                System.out.println("ButtonEditor: Invalid row index - " + row);
            }
        }
        clicked = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        clicked = false;
        return super.stopCellEditing();
    }
}
