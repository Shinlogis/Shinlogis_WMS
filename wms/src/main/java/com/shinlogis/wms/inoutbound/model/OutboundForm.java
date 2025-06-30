package com.shinlogis.wms.inoutbound.model;

import com.shinlogis.wms.product.model.Product;

public class OutboundForm {
    public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	private Product product;
}
