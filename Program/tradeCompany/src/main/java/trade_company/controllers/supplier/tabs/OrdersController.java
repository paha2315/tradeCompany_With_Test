package trade_company.controllers.supplier.tabs;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import trade_company.logic.sql_object.Document;
import trade_company.logic.sql_object.Order;
import trade_company.logic.sql_object.Product;
import trade_company.logic.supplier.tabs.OrdersLogic;
import trade_company.models.Model;
import trade_company.models.UserDataModel;
import trade_company.views.fictive_table_objects.SupplierOrderSelected;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;

public class OrdersController extends OrdersLogic {
    @FXML
    private ComboBox<String> combobox_filterWarehouse;

    @FXML
    private TableView<Product> table_products;
    @FXML
    private TableColumn<Product, String> column_prodNomenclature;
    @FXML
    private TableColumn<Product, String> column_prodArticle;
    @FXML
    private TableColumn<Product, String> column_prodName;
    @FXML
    private TableColumn<Product, String> column_prodMeasure;
    @FXML
    private TableColumn<Product, Double> column_prodMass;

    @FXML
    private TableView<SupplierOrderSelected> table_selected;
    @FXML
    private TableColumn<SupplierOrderSelected, String> column_selectedWarehouseName;
    @FXML
    private TableColumn<SupplierOrderSelected, String> column_selectedProdNomenclature;
    @FXML
    private TableColumn<SupplierOrderSelected, String> column_selectedProdArticle;
    @FXML
    private TableColumn<SupplierOrderSelected, String> column_selectedProdName;
    @FXML
    private TableColumn<SupplierOrderSelected, Double> column_selectedProdCount;

    @FXML
    private Button button_order;

    @FXML
    void initialize() {
        initTable_products();
        initTable_selected();
        addListeners();
        initCombobox_filterWarehouse();
    }

    private void addListeners() {
        button_order.setOnMouseClicked(this::makeOrder);
    }


    public void initCombobox_filterWarehouse() {
        initWarehouses();
        combobox_filterWarehouse.setItems(FXCollections.observableArrayList(warehousesName));
        combobox_filterWarehouse.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            curWarehouse = warehouses.get(warehousesName.indexOf(newValue));
            search();
        });
        combobox_filterWarehouse.getSelectionModel().select(0);
    }

    public void initTable_products() {
        initProducts();
        column_prodNomenclature.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomenclature()));
        column_prodArticle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getArticle()));
        column_prodName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        column_prodMeasure.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOkei().getName()));
        column_prodMass.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getMass()));

        table_products.setItems(products);
        table_products.setRowFactory(tv -> {
            TableRow<Product> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2 && !row.isEmpty()) {
                    TextInputDialog dialog = new TextInputDialog("");
                    dialog.setTitle("Создание запроса");
                    dialog.setHeaderText("");
                    dialog.setContentText("Введите необходимое количество продукта \"" + row.getItem().getName() + "\":");
                    Optional<String> result = dialog.showAndWait();
                    try {
                        if (result.isEmpty())
                            throw new Exception("");
                        add(curWarehouse, row.getItem(), Integer.parseInt(result.get()));
                        products.remove(row.getItem());
                    } catch (Exception e) {
                        Model.getInstance().getWindowFactory().showAlert(
                                Alert.AlertType.WARNING,
                                "Внимание",
                                "",
                                "Были введены некорректные данные.\nПовторите ввод.");
                    }
                }
            });
            return row;
        });

    }

    public void initTable_selected() {
        column_selectedWarehouseName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getWarehouse().getName()));
        column_selectedProdNomenclature.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getNomenclature()));
        column_selectedProdArticle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getArticle()));
        column_selectedProdName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getName()));
        column_selectedProdCount.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCount()));

        table_selected.setItems(selected);
        table_selected.setRowFactory(tv -> {
            TableRow<SupplierOrderSelected> row = new TableRow<>();
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
                            products.add(row.getItem().getProduct());
                            selected.remove(row.getItem());
                        }
                    });
                }
            });
            return row;
        });
    }

    public void makeOrder(MouseEvent mouseEvent) {
        for (var sel : selected) {
            Order order = new Order();
            order.setDocument((Document) null);
            order.setCompleted(2);
            order.setWarehouse(sel.getWarehouse());
            order.setPrice(sel.getProduct().getId());
            order.setCount(sel.getCount());
            order.setSupplier(UserDataModel.getInstance().getPerson().getSupplier());
            try {
                order.save();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        selected.clear();
        search();
    }
}
