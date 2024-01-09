package trade_company.controllers.supplier.tabs;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import trade_company.logic.supplier.tabs.SuppliesLogic;
import trade_company.models.Model;
import trade_company.models.UserDataModel;

public class SuppliesController extends SuppliesLogic {
    @FXML
    private ComboBox<String> combobox_warehouseSelector;
    @FXML
    private TextField textfield_price;
    @FXML
    private TextField textfield_count;
    @FXML
    private Button button_makeSupply;

    @FXML
    public void initialize() {
        combobox_warehouseSelector.getItems().setAll(warehouseNamesList);
        setupListeners();
    }

    private void setupListeners() {
        button_makeSupply.setOnAction(actionEvent -> {
            if (onSupplyPressed()) {
                textfield_count.clear();
                textfield_price.clear();
                combobox_warehouseSelector.getSelectionModel().select(-1);
                combobox_warehouseSelector.setValue("Выберите склад");
            }
        });
    }

    private boolean onSupplyPressed() {
        var result = doSupply(
                combobox_warehouseSelector.getSelectionModel().getSelectedItem(),
                textfield_price.getText(),
                textfield_count.getText());
        var commonAlertTitle = "Предложение товара";
        var windowFactory = Model.getInstance().getWindowFactory();
        switch (result) {
            case WAREHOUSE_INCOMPLETE ->
                    windowFactory.showAlert(Alert.AlertType.WARNING, commonAlertTitle, "", "Не выбран склад.");
            case FIELDS_INCOMPLETE ->
                    windowFactory.showAlert(Alert.AlertType.WARNING, commonAlertTitle, "", "Не заполнены поля.");
            case FIELDS_STR_TO_NUMBER_CONVERT_EXCEPTION ->
                    windowFactory.showAlert(Alert.AlertType.WARNING, commonAlertTitle, "", "Ошибка заполнения полей.");
            case SQL_EXCEPTION -> windowFactory.showAlert(Alert.AlertType.ERROR, commonAlertTitle, "", "Ошибка MySQL.");
            case OK -> {
                var productName = UserDataModel.getInstance().getPerson().getSupplier().getProduct().getName();
                var warehouseName = UserDataModel.getInstance().getWarehouse().getName();
                windowFactory.showAlert(Alert.AlertType.INFORMATION, commonAlertTitle, "", "Товар '" + productName + "' предложен складу '" + warehouseName + "' в количестве " + textfield_count.getText() + " единиц.");
                return true;
            }
        }
        return false;
    }
}
