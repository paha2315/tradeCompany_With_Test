package trade_company.controllers.administrator;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import trade_company.models.Model;

public class AdministratorController {
    @FXML
    private BorderPane parent;

    @FXML
    public void initialize() {
        Model.getInstance().getViewFactory().getAdministratorSelectedMenuItem()
                .addListener((observable, oldVal, newVal) -> {
                    switch (newVal) {
                        case NEW_USER -> parent.setCenter(Model.getInstance()
                                .getViewFactory().getAdministratorNewPersonView());
                        case USER_LIST -> parent.setCenter(Model.getInstance()
                                .getViewFactory().getAdministratorUserListView());
                        case PROFILE -> parent.setCenter(Model.getInstance()
                                .getViewFactory().getAdministratorProfileView());
                        default -> parent.setCenter(Model.getInstance().getViewFactory().getDummyView());
                    }
                });
    }
}
