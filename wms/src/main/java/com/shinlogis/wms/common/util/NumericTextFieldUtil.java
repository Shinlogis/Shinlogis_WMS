package com.shinlogis.wms.common.util;

import javax.swing.*;
import javax.swing.text.*;

/**
 * 숫자만 입력 가능한 JTextField 유틸리티 클래스
 * 사용예시
 * tfDamagedQuantity = new JTextField(10);
 * NumericTextFieldUtil.applyNumericFilter(tfDamagedQuantity);
 * 
 * @author 김예진
 * @since 2025-06-25
 */
public class NumericTextFieldUtil {

	/**
	 * 숫자 입력만 허용하는 DocumentFilter
	 */
	private static class NumericDocumentFilter extends DocumentFilter {
        // 문자를 삽입할 때 숫자인지 확인
		@Override
		public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
				throws BadLocationException {
			if (string.matches("\\d+")) { // 숫자만 허용
				super.insertString(fb, offset, string, attr);
			}
		}

		// 붙여넣기 등으로 기존 문자열을 바꿀 때 숫자인지 확인
		@Override
		public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
				throws BadLocationException {
			if (text.matches("\\d+")) {
				super.replace(fb, offset, length, text, attrs);
			}
		}
	}

	/**
	 * 전달된 JTextField에 숫자만 입력 가능하도록 설정하는 메서드
	 *
	 * @author 김예진
	 * @since 2025-06-25
	 * @param textField 숫자만 허용할 JTextField
	 */
	public static void applyNumericFilter(JTextField textField) {
		PlainDocument doc = (PlainDocument) textField.getDocument();
		doc.setDocumentFilter(new NumericDocumentFilter());
	}
}
