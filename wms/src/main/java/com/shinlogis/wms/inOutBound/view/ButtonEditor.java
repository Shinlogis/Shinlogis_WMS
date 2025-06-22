package com.shinlogis.wms.inOutBound.view;

import javax.swing.*;
import java.awt.*;

public class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private String label;
    private boolean clicked;
    private int row;



    public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);

        button.addActionListener(e -> {
            fireEditingStopped();
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        label = (value != null) ? value.toString() : "수정";
        button.setText(label);
        this.row = row;
        this.clicked = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (clicked) {
            // TODO: 클릭 시 동작 정의
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
