package com.muri.presentation.controllers.order;

import com.muri.model.Order;
import com.muri.presentation.controllers.abstractcontroller.AbstractController;
import com.muri.presentation.views.order.OrderAddView;
import com.muri.presentation.views.order.OrderEditView;
import com.muri.presentation.views.order.OrderView;

public class OrderController extends AbstractController<Order> {
    public OrderController() {
        super(new OrderView(), new OrderAddView());
    }
}
