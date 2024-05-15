package com.muri.presentation.controllers.bill;

import com.muri.model.Bill;
import com.muri.presentation.controllers.abstractcontroller.AbstractController;
import com.muri.presentation.views.bill.BillView;

public class BillController extends AbstractController<Bill> {
    public BillController() {
        super(new BillView());
    }
}
