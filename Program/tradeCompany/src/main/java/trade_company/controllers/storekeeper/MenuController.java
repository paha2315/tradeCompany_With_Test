package trade_company.controllers.storekeeper;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import trade_company.models.Model;
import trade_company.models.UserDataModel;
import trade_company.views.options.StorekeeperMenuOptions;

public class MenuController {
    @FXML
    private Button button_orders;
    @FXML
    private Button button_availability;
    @FXML
    private Button button_reports;
    @FXML
    private Button button_profile;
    @FXML
    private Button button_logout;


    @FXML
    public void initialize() {
        setupListeners();
    }

    private void setupListeners() {
        button_orders.setOnAction(actionEvent -> Model.getInstance().getViewFactory()
                .getStorekeeperSelectedMenuItem().set(StorekeeperMenuOptions.MANAGE_ORDERS));
        button_availability.setOnAction(actionEvent -> Model.getInstance().getViewFactory()
                .getStorekeeperSelectedMenuItem().set(StorekeeperMenuOptions.MANAGE_AVAILABILITY));
        button_reports.setOnAction(actionEvent -> Model.getInstance().getViewFactory()
                .getStorekeeperSelectedMenuItem().set(StorekeeperMenuOptions.REPORTS));
        button_profile.setOnAction(actionEvent -> Model.getInstance().getViewFactory()
                .getStorekeeperSelectedMenuItem().set(StorekeeperMenuOptions.PROFILE));
        button_logout.setOnAction(actionEvent -> onClose());
    }

    private void onClose() {
        Model.getInstance().getViewFactory().resetStorekeeperOrdersView();
        Model.getInstance().getViewFactory().resetStorekeeperActionAvailabilityView();
        Model.getInstance().getViewFactory().resetStorekeeperReportsView();
        Model.getInstance().getViewFactory().resetStorekeeperProfileView();

        Model.getInstance().getViewFactory().getStorekeeperSelectedMenuItem().set(StorekeeperMenuOptions.DUMMY);
        UserDataModel.getInstance().setPerson(null);
        UserDataModel.getInstance().setWarehouse(null);

        Stage stage = (Stage) button_logout.getScene().getWindow();
        stage.close();

        Model.getInstance().getWindowFactory().showLoginWindow();
    }
}















