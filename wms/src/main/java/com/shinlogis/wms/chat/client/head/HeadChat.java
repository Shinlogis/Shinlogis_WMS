package com.shinlogis.wms.chat.client.head;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import com.google.gson.Gson;
import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.location.model.Location;

public class HeadChat extends JFrame {

	JPanel p_north;
	JLabel la_name;

	JTextPane area;
	// JTextArea area;
	JScrollPane scroll;
	JTextField t_input;

	JPanel p_south;
	AppMain appMain;

	Location location;
	public HeadClientThread headClientThread;

	public HeadChat(Location location) {
		this.location = location;

		p_north = new JPanel();
		la_name = new JLabel(location.getLocationName());

		area = new JTextPane(); // 💡 여기 변경됨
		area.setEditable(false);
		area.setContentType("text/html"); // HTML 사용 가능하게 설정
		area.setText("<html><body></body></html>"); // 초기화
		// area = new JTextArea();
		scroll = new JScrollPane(area);
		t_input = new JTextField();

		p_south = new JPanel();

		scroll.setPreferredSize(new Dimension(300, 400));
		area.setBackground(Color.PINK);

		t_input.setPreferredSize(new Dimension(300, 30));

		p_north.add(la_name);
		p_south.add(t_input);

		add(p_north, BorderLayout.NORTH);
		add(scroll);
		add(p_south, BorderLayout.SOUTH);

		t_input.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String text = t_input.getText().trim();
					if (!text.isEmpty()) {
						addMyMessage(text); // 오른쪽 정렬로 추가
						t_input.setText("");
						
						Message message = new Message();
						// 메세지 만들기
						message.setRequestType("chat");
						message.setMe("head");
						message.setTarget(location);
						message.setMsg(t_input.getText());
						t_input.setText("");

						Gson gson = new Gson();
						String result = gson.toJson(message);

						headClientThread.send(result);
					}
				}
			}
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Message message = new Message();
				// 메세지 만들기
				message.setRequestType("exit");
				message.setMe("head");

				Gson gson = new Gson();
				String result = gson.toJson(message);

				headClientThread.send(result);
			}
		});

		setBounds(600, 200, 400, 500);
		setVisible(true);

	}
	
	
	// 오른쪽 정렬로 내가 보낸 메시지 출력
    public void addMyMessage(String msg) {
        appendMessage("<div style='text-align: right; color: black;'>" + escapeHtml(msg) + "</div>");
    }

    // 왼쪽 정렬로 상대방 메시지 출력
    public void addOtherMessage(String msg) {
        appendMessage("<div style='text-align: left; color: black;'>" + escapeHtml(msg) + "</div>");
    }
    
 // HTML 메시지를 area에 추가
    private void appendMessage(String html) {
        try {
            HTMLEditorKit kit = (HTMLEditorKit) area.getEditorKit();
            HTMLDocument doc = (HTMLDocument) area.getDocument();
            kit.insertHTML(doc, doc.getLength(), html, 0, 0, null);
            area.setCaretPosition(doc.getLength()); // 스크롤 아래로
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 특수문자 이스케이프 처리
    private String escapeHtml(String text) {
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

}
