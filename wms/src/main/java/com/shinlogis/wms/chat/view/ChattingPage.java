package com.shinlogis.wms.chat.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.common.config.Config;
import com.shinlogis.wms.common.config.Page;
import com.shinlogis.wms.location.model.Location;
import com.shinlogis.wms.location.repository.LocationDAO;

public class ChattingPage extends Page{

	JPanel pPageName;
	JLabel laPageName;
	
	JPanel p_center;
	JComboBox cb_location;
	
	LocationDAO locationDAO;
	
	
	public ChattingPage(AppMain appMain) {
		super(appMain);
		
		pPageName = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pPageName.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.PAGE_NAME_HEIGHT));
		laPageName = new JLabel("채팅 > 지점과 채팅하기");
		
		p_center = new JPanel(new FlowLayout(FlowLayout.LEFT));
		cb_location = new JComboBox();
		
		locationDAO = new LocationDAO();
		
		List<Location> locations = new ArrayList<>();
		for(Location loc : locationDAO.getLocation()) {
			cb_location.addItem(loc);
		}
		
		
		//스타일
		setBackground(Color.LIGHT_GRAY);
		p_center.setBackground(Color.LIGHT_GRAY);
		p_center.setPreferredSize(new Dimension(Config.CONTENT_WIDTH, Config.PAGE_NAME_HEIGHT));
		
		//조립 
		p_center.add(cb_location);
		pPageName.add(laPageName);
		add(pPageName);
		add(p_center);
		
		
	}
}