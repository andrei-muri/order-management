package com.muri.presentation.controllers.product;

import com.muri.model.Product;
import com.muri.presentation.controllers.abstractcontroller.AbstractController;
import com.muri.presentation.views.product.ProductAddView;
import com.muri.presentation.views.product.ProductEditView;
import com.muri.presentation.views.product.ProductView;

public class ProductController extends AbstractController<Product> {
    public ProductController() {
        super(new ProductView(), new ProductAddView());
    }
}
