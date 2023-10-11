package trade_company.controllers.supplier.tabs;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import trade_company.logic.sql_object.Order;
import trade_company.logic.supplier.tabs.RequestsControllerLogic;
import trade_company.models.Model;

import java.sql.SQLException;
import java.util.Arrays;

public class RequestsController extends RequestsControllerLogic {
    @FXML
    private ComboBox<String> combobox_filterWarehouse;

    @FXML
    private TableView<Order> table_requests;
    @FXML
    private TableColumn<Order, String> column_warehouseName;
    @FXML
    private TableColumn<Order, Double> column_requestedCount;
    @FXML
    private TableColumn<Order, Double> column_suggestPrice;
    @FXML
    private TableColumn<Order, Double> column_suggestPriceFull;

    @FXML
    private TableView<Order> table_selected;
    @FXML
    private TableColumn<Order, String> column_selectedWarehouseName;
    @FXML
    private TableColumn<Order, Double> column_selectedRequestedCount;
    @FXML
    private TableColumn<Order, Double> column_selectedSuggestPriceFull;

    @FXML
    private Button button_acceptRequests;
    @FXML
    private Button button_dismissRequests;

    @FXML
    public void initialize() {
        setupListeners();
        initCombobox_filterWarehouse();
        initTable_requests();
        initTable_selected();
    }

    private void setupListeners() {
        button_acceptRequests.setOnAction(this::acceptSelected);
        button_dismissRequests.setOnAction(this::declineSelected);
    }

    public void initCombobox_filterWarehouse() {
        initWarehouses();
        combobox_filterWarehouse.setItems(FXCollections.observableArrayList(warehousesName));
        combobox_filterWarehouse.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            search(warehouses.get(warehousesName.indexOf(newValue)));
        });
        combobox_filterWarehouse.getSelectionModel().select(0);
    }

    public void initTable_requests() {
        initRequests();
        column_warehouseName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getWarehouse().getName()));
        column_requestedCount.setCellValueFactory(cellData -> new SimpleObjectProperty<Double>(cellData.getValue().getCount()));
        column_suggestPrice.setCellValueFactory(cellData -> new SimpleObjectProperty<Double>(cellData.getValue().getPrice()));
        column_suggestPriceFull.setCellValueFactory(cellData -> new SimpleObjectProperty<Double>(cellData.getValue().getPrice() * cellData.getValue().getCount()));
        table_requests.setItems(requests);

        table_requests.setRowFactory(tv -> {
            TableRow<Order> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2 && !row.isEmpty()) {
                    selected.add(row.getItem());
                    requests.remove(row.getItem());
                }
            });
            return row;
        });
    }

    public void initTable_selected() {
        column_selectedWarehouseName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getWarehouse().getName()));
        column_selectedRequestedCount.setCellValueFactory(cellData -> new SimpleObjectProperty<Double>(cellData.getValue().getCount()));
        column_selectedSuggestPriceFull.setCellValueFactory(cellData -> new SimpleObjectProperty<Double>(cellData.getValue().getPrice() * cellData.getValue().getCount()));
        table_selected.setItems(selected);

        table_selected.setRowFactory(tv -> {
            TableRow<Order> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2 && !row.isEmpty()) {
                    ButtonType buttonTypeConfirm = new ButtonType("Подтвердить", ButtonBar.ButtonData.APPLY);
                    ButtonType buttonTypeCancel = new ButtonType("Отменить", ButtonBar.ButtonData.CANCEL_CLOSE);
                    Model.getInstance().getWindowFactory().showConfirmationAlert(
                            "Удалить",
                            "",
                            "Удалить запрос со склада \"" + row.getItem().getWarehouse().getName() + "\" из выбранного?",
                            Arrays.asList(buttonTypeConfirm, buttonTypeCancel)
                    ).ifPresent(buttonType -> {
                        if (buttonType == buttonTypeConfirm) {
                            requests.add(row.getItem());
                            selected.remove(row.getItem());
                        }
                    });
                }
            });
            return row;
        });
    }

    void acceptSelected(ActionEvent event) {
        for (var sel : selected) {
            sel.setCompleted(4);
            try {
                sel.update();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        selected.clear();
    }

    void declineSelected(ActionEvent event) {
        for (var sel : selected) {
            sel.delete();
        }
        selected.clear();
    }
}
