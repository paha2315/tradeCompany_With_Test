package trade_company.controllers.storekeeper;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import trade_company.models.Model;

public class StorekeeperController {
    @FXML
    private BorderPane parent;

    @FXML
    public void initialize() {
        Model.getInstance().getViewFactory().getStorekeeperSelectedMenuItem()
                .addListener((observable, oldVal, newVal) -> {
                    switch (newVal) {
                        case MANAGE_ORDERS ->
                                parent.setCenter(Model.getInstance().getViewFactory().getStorekeeperActionOrdersView());
                        case MANAGE_AVAILABILITY ->
                                parent.setCenter(Model.getInstance().getViewFactory().getStorekeeperActionAvailabilityView());
                        case REPORTS ->
                                parent.setCenter(Model.getInstance().getViewFactory().getStorekeeperReportsView());
                        case PROFILE ->
                                parent.setCenter(Model.getInstance().getViewFactory().getStorekeeperProfileView());
                        default -> parent.setCenter(Model.getInstance().getViewFactory().getDummyView());
                    }
                });
    }
}
