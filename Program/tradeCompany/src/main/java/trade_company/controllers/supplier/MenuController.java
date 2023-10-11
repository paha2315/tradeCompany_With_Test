package trade_company.controllers.supplier;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import trade_company.models.Model;
import trade_company.models.UserDataModel;
import trade_company.views.options.SupplierMenuOptions;

public class MenuController {
    @FXML
    private Button button_logout;
    @FXML
    private Button button_myCompany;
    @FXML
    private Button button_orders;
    @FXML
    private Button button_profile;

    @FXML
    public void initialize() {
        setupListeners();
    }

    private void setupListeners() {
        button_myCompany.setOnAction(actionEvent -> Model.getInstance().getViewFactory().getSupplierSelectedMenuItem().set(SupplierMenuOptions.MY_COMPANY));
        button_orders.setOnAction(actionEvent -> {
            if (UserDataModel.getInstance().getPerson().getSupplier() != null)
                Model.getInstance().getViewFactory().getSupplierSelectedMenuItem().set(SupplierMenuOptions.ORDERS_SUPPLIES);
            else
                Model.getInstance().getWindowFactory().showAlert(Alert.AlertType.WARNING, "Не зарегистрирована компания.", "", "Вам пока ещё не доступна данная вкладка.");
        });
        button_profile.setOnAction(actionEvent -> Model.getInstance().getViewFactory().getSupplierSelectedMenuItem().set(SupplierMenuOptions.PROFILE));
        button_logout.setOnAction(actionEvent -> onClose());
    }

    private void onClose() {
        Model.getInstance().getViewFactory().resetSupplierCompanyView();
        Model.getInstance().getViewFactory().resetSupplierOrdersView();
        Model.getInstance().getViewFactory().resetSupplierProfileView();

        Model.getInstance().getViewFactory().getSupplierSelectedMenuItem().set(SupplierMenuOptions.DUMMY);
        UserDataModel.getInstance().setPerson(null);
        UserDataModel.getInstance().setWarehouse(null);
        UserDataModel.getInstance().setSupplier(null);
        UserDataModel.getInstance().setProduct(null);

        Stage stage = (Stage) button_logout.getScene().getWindow();
        stage.close();

        Model.getInstance().getWindowFactory().showLoginWindow();
    }
}
