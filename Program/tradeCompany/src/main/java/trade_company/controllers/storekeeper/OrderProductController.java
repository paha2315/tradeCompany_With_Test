package trade_company.controllers.storekeeper;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import trade_company.logic.sql_object.Order;
import trade_company.logic.sql_object.Supplier;
import trade_company.logic.storekeeper.OrderProductLogic;
import trade_company.models.DialogDataModel;
import trade_company.models.Model;
import trade_company.models.UserDataModel;

import java.sql.SQLException;
import java.util.Arrays;

public class OrderProductController extends OrderProductLogic {
    @FXML
    private ComboBox<String> combobox_supplierCity;
    @FXML
    private TextField textfield_article;

    @FXML
    private TableView<Supplier> table_products;
    @FXML
    private TableColumn<Supplier, String> column_supplierName;
    @FXML
    private TableColumn<Supplier, String> column_prodNomenclature;
    @FXML
    private TableColumn<Supplier, String> column_prodArticle;
    @FXML
    private TableColumn<Supplier, String> column_prodName;
    @FXML
    private TableColumn<Supplier, String> column_prodMeasure;
    @FXML
    private TableColumn<Supplier, Double> column_prodMass;

    @FXML
    private TableView<Order> table_selected;
    @FXML
    private TableColumn<Order, String> column_selectedSupplierName;
    @FXML
    private TableColumn<Order, String> column_selectedProdNomenclature;
    @FXML
    private TableColumn<Order, String> column_selectedProdArticle;
    @FXML
    private TableColumn<Order, String> column_selectedProdName;
    @FXML
    private TableColumn<Order, Double> column_selectedProdPrice;
    @FXML
    private TableColumn<Order, Double> column_selectedProdCount;

    @FXML
    private Button button_order;

    @FXML
    public void initialize() {
        initCombobox_supplier();
        initTable_products();
        initTable_selected();
        addListeners();
    }

    private void addListeners() {
        textfield_article.textProperty().addListener((observable, oldValue, newValue) -> {
            search(combobox_supplierCity.getValue(), newValue);
        });
        button_order.setOnMouseClicked(this::commit);
    }

    private void commit(MouseEvent mouseEvent) {
        for (var order : orders) {
            try {
                order.setCompleted(0);
                order.save();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        orders.clear();
    }

    void initCombobox_supplier() {
        initComboBoxCity();
        combobox_supplierCity.setItems(FXCollections.observableArrayList(citys));
        combobox_supplierCity.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            search(newValue, textfield_article.getText());
        });
        combobox_supplierCity.getSelectionModel().select(0);
    }

    public void initTable_products() {
        initSuppliers();
        column_supplierName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        column_prodNomenclature.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getNomenclature()));
        column_prodArticle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getArticle()));
        column_prodName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getName()));
        column_prodMeasure.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getOkei().getName()));
        column_prodMass.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getProduct().getMass()));
        table_products.setItems(suppliers);

        table_products.setRowFactory(tv -> {
            TableRow<Supplier> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2 && !row.isEmpty()) {
                    Model.getInstance().getDialogFactory().showRequestOrderDetailsDialog(row.getItem().getProduct().getName());

                    var data = DialogDataModel.getInstance().getRequestOrderDetailsDialogData();
                    if (data.isOk()) {
                        Order order = new Order();
                        order.setCount(data.getCount());
                        order.setPrice(data.getPrice());
                        order.setSupplier(row.getItem());
                        order.setWarehouse(UserDataModel.getInstance().getWarehouse());
                        order.setCompleted(0);
                        orders.add(order);
                    } else if (data.wasException()) {
                        Model.getInstance().getWindowFactory().showAlert(
                                Alert.AlertType.WARNING, "Заказ товара", "", "Некоторые поля некорректно заполнены.\nПовторите ввод.");
                    }
                    search(combobox_supplierCity.getValue(), textfield_article.getText());
                }
            });
            return row;
        });
    }

    public void initTable_selected() {
        column_selectedSupplierName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSupplier().getName()));
        column_selectedProdNomenclature.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSupplier().getProduct().getNomenclature()));
        column_selectedProdArticle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSupplier().getProduct().getArticle()));
        column_selectedProdName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSupplier().getProduct().getName()));
        column_selectedProdPrice.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPrice()));
        column_selectedProdCount.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCount()));
        table_selected.setItems(orders);

        table_selected.setRowFactory(tv -> {
            TableRow<Order> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2 && !row.isEmpty()) {
                    ButtonType buttonTypeConfirm = new ButtonType("Подтвердить", ButtonBar.ButtonData.APPLY);
                    ButtonType buttonTypeCancel = new ButtonType("Отменить", ButtonBar.ButtonData.CANCEL_CLOSE);
                    Model.getInstance().getWindowFactory().showConfirmationAlert(
                            "Удалить",
                            "",
                            "Удалить \"" + row.getItem().getSupplier().getProduct().getName() + "\" из выбранного?",
                            Arrays.asList(buttonTypeConfirm, buttonTypeCancel)
                    ).ifPresent(buttonType -> {
                        if (buttonType == buttonTypeConfirm) {
                            orders.remove(row.getItem());
                            search(combobox_supplierCity.getValue(), textfield_article.getText());
                        }
                    });
                }
            });
            return row;
        });
    }
}
