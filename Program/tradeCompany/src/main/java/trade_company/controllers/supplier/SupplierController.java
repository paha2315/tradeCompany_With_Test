package trade_company.controllers.supplier;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import trade_company.models.Model;

public class SupplierController {
    @FXML
    private BorderPane parent;

    @FXML
    public void initialize() {
        Model.getInstance().getViewFactory().getSupplierSelectedMenuItem()
                .addListener((observable, oldVal, newVal) -> {
                    switch (newVal) {
                        case MY_COMPANY -> parent.setCenter(Model.getInstance()
                                .getViewFactory().getSupplierCompanyView());
                        case ORDERS_SUPPLIES -> parent.setCenter(Model.getInstance()
                                .getViewFactory().getSupplierOrdersView());
                        case PROFILE -> parent.setCenter(Model.getInstance()
                                .getViewFactory().getSupplierProfileView());
                        default -> parent.setCenter(Model.getInstance().getViewFactory().getDummyView());
                    }
                });
    }
}
