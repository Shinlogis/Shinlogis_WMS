package com.shinlogis.wms.main.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.util.List;

import javax.swing.JPanel;

import com.shinlogis.wms.AppMain;
import com.shinlogis.wms.common.config.Config;
import com.shinlogis.wms.common.config.Page;
import com.shinlogis.wms.common.util.ImageUtil;
import com.shinlogis.wms.product.model.Product;
import com.shinlogis.wms.product.repository.ProductDAO;


public class MainPage extends Page{
	public MainPage(AppMain appMain) {
		super(appMain);
		
		setBackground(Color.red);
	}
}
