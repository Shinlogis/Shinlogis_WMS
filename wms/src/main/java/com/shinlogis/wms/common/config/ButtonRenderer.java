package com.shinlogis.wms.common.config;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * JTable의 셀에 버튼을 생성하는 렌더러 클래스입니다.
 * 
 * @author 김예진
 * @since 2025-06-22
 */
public class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        setText(value != null ? value.toString() : "버튼");
        return this;
    }
}
