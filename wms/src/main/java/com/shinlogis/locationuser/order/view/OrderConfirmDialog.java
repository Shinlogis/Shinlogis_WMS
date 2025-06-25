package com.shinlogis.locationuser.order.view;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import com.shinlogis.wms.common.config.Config;
import com.shinlogis.wms.product.model.Product;

public class OrderConfirmDialog extends JDialog {

    private boolean confirm = false;

    public OrderConfirmDialog(Frame parent, List<Product> selectedProducts) {
        super(parent, "주문 확인서", true);
        init(selectedProducts);
        setLocationRelativeTo(parent); 
        
	    // 현재 위치 가져오기
	    Point center =this.getLocation();
	    int x = center.x +110;
	    int y = center.y-30;      
	    
	    System.out.println(x);
	    System.out.print(y);
	    
	    setLocation(x, y);
        setVisible(true);
    }

    private void init(List<Product> selectedProducts) {
    	JPanel orderconfirmTitle = new JPanel();
    	JLabel title = new JLabel("주문 확인서");
    	JPanel orderconfirm = new JPanel();
    	JScrollPane scrollPane = new JScrollPane(orderconfirm);
    	JPanel buttonPanel = new JPanel();
    	JButton okBtn = new JButton("확인");
    	JButton cancelBtn = new JButton("취소");

    	title.setFont(new Font("맑은 고딕", Font.BOLD, 24));
    	title.setForeground(Color.WHITE);
    	orderconfirmTitle.setBackground(new Color(0xFF7F50));
    	orderconfirmTitle.setPreferredSize(new Dimension(280, 45));

    	orderconfirm.setLayout(new BoxLayout(orderconfirm, BoxLayout.Y_AXIS));
    	orderconfirm.setBackground(Color.WHITE);

    	for (Product p : selectedProducts) {
    	    String orderItem = p.getProductName() + " " + p.getQuantity() + "개 " + (p.getPrice() * p.getQuantity()) + "원";
    	    JLabel item = new JLabel(orderItem);
    	    item.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
    	    item.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 0));
    	    orderconfirm.add(item);
    	}
   
    	scrollPane.setPreferredSize(new Dimension(300, 200));
    	scrollPane.setBorder(null);
    	buttonPanel.setBackground(Color.WHITE);
    	
    	okBtn.addActionListener(e -> {
    	    confirm = true;
    	    dispose();
    	});
    	cancelBtn.addActionListener(e -> {
    	    confirm = false;
    	    dispose();
    	});

    	orderconfirmTitle.add(title);
    	buttonPanel.add(okBtn);
    	buttonPanel.add(cancelBtn);

    	setLayout(new BorderLayout());
    	add(orderconfirmTitle, BorderLayout.NORTH);
    	add(scrollPane, BorderLayout.CENTER);
    	add(buttonPanel, BorderLayout.SOUTH);
    	setSize(300, 350);
    }

    public boolean isConfirmed() {
        return confirm;
    }
}
