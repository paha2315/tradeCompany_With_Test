package trade_company.controllers.storekeeper.tabs;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import trade_company.logic.sql_object.Availability;
import trade_company.logic.sql_object.Containers.ProductContainer;
import trade_company.logic.sql_object.Order;
import trade_company.logic.storekeeper.tabs.ReleaseLogic;
import trade_company.models.Model;
import trade_company.views.fictive_table_objects.StorekeeperIncompleteOrder;

import java.util.ArrayList;
import java.util.Arrays;

public class ReleaseController extends ReleaseLogic {
    @FXML
    private TextField textfield_article;
    @FXML
    private ComboBox<String> combobox_clientName;

    @FXML
    private TableView<StorekeeperIncompleteOrder> table_incompleteOrders;
    @FXML
    private TableColumn<StorekeeperIncompleteOrder, String> column_product_nomenclature;
    @FXML
    private TableColumn<StorekeeperIncompleteOrder, String> column_product_article;
    @FXML
    private TableColumn<StorekeeperIncompleteOrder, String> column_product_name;
    @FXML
    private TableColumn<StorekeeperIncompleteOrder, Double> column_orderRequestedCount;
    @FXML
    private TableColumn<StorekeeperIncompleteOrder, Double> column_productWillRemain;
    @FXML
    private TableColumn<StorekeeperIncompleteOrder, Double> column_productAllAvailable;

    @FXML
    private TableView<Availability> table_toRelease;
    @FXML
    private TableColumn<Availability, String> column_toRelease_article;
    @FXML
    private TableColumn<Availability, String> column_toRelease_name;
    @FXML
    private TableColumn<Availability, Double> column_toRelease_count;
    @FXML
    private TableColumn<Availability, String> column_toRelease_section_number;
    @FXML
    private TableColumn<Availability, String> column_toRelease_stack_number;

    @FXML
    private Button button_acceptRelease;
    @FXML
    private Button button_denyRelease;

    @FXML
    void initialize() {
        combobox_clientName.setItems(FXCollections.observableArrayList(comboboxClientsNamesData));
        combobox_clientName.getSelectionModel().select(0);

        initTableIncompleteOrders();
        initTableToRelease();
    }

    @FXML
    void selectedCity(ActionEvent event) {
        searchOrders(combobox_clientName.getValue(), textfield_article.getText());
    }

    @FXML
    void searchAvail(KeyEvent event) {
        searchOrders(combobox_clientName.getValue(), textfield_article.getText());
    }

    @FXML
    void ReleaseCommit(ActionEvent event) {
        onReleaseCommit();
        if (!toReleaseAvailabilityData.isEmpty())
            Model.getInstance().getWindowFactory().showAlert(Alert.AlertType.WARNING, "Отпуск товаров", "", "Не все товары были отпущены.");
        else
            Model.getInstance().getWindowFactory().showAlert(Alert.AlertType.INFORMATION, "Отпуск товаров", "", "Все товары успешно отпущены.");
        searchOrders(combobox_clientName.getValue(), textfield_article.getText());
    }

    @FXML
    void CancelCommit(ActionEvent event) {
        ArrayList<Order> orders = new ArrayList<>();
        for (Order order : toReleaseOrderData)
            if (!orders.contains(order))
                orders.add(order);
        for (var order : orders)
            order.delete();
        toReleaseOrderData.clear();
        toReleaseAvailabilityData.clear();
        searchOrders(combobox_clientName.getValue(), textfield_article.getText());
    }

    void initTableIncompleteOrders() {
        ProductContainer.setAll();
        column_product_nomenclature.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getNomenclature()));
        column_product_article.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getArticle()));
        column_product_name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getName()));
        column_orderRequestedCount.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getOrder().getCount()));
        column_productWillRemain.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getWillRemain()));
        column_productAllAvailable.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getAllCount()));
        table_incompleteOrders.setItems(incompleteOrdersData);

        table_incompleteOrders.setRowFactory(tv -> {
            TableRow<StorekeeperIncompleteOrder> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2 && !row.isEmpty()) {
                    moveIncompleteOrderToReleaseData(row.getItem());
                    table_incompleteOrders.setItems(incompleteOrdersData);
                    table_toRelease.setItems(toReleaseAvailabilityData);
                    searchOrders(combobox_clientName.getValue(), textfield_article.getText());
                }
            });
            return row;
        });
    }


    void initTableToRelease() {
        column_toRelease_article.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getArticle()));
        column_toRelease_name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getName()));
        column_toRelease_count.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCount()));
        column_toRelease_section_number.setCellValueFactory(cellData -> {
            int section = cellData.getValue().getSectionNumber();
            String value = (section != -1) ? Integer.toString(section) : "-";
            return new SimpleStringProperty(value);
        });
        column_toRelease_stack_number.setCellValueFactory(cellData -> {
            int stack = cellData.getValue().getStackNumber();
            String value = (stack != -1) ? Integer.toString(stack) : "-";
            return new SimpleStringProperty(value);
        });
        table_toRelease.setItems(toReleaseAvailabilityData);

        table_toRelease.setRowFactory(tv -> {
            TableRow<Availability> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2 && !row.isEmpty()) {
                    ButtonType buttonTypeConfirm = new ButtonType("Подтвердить", ButtonBar.ButtonData.APPLY);
                    ButtonType buttonTypeCancel = new ButtonType("Отменить", ButtonBar.ButtonData.CANCEL_CLOSE);
                    var result = Model.getInstance().getWindowFactory().showConfirmationAlert(
                            "Удалить",
                            "",
                            "Удалить товар \"" + row.getItem().getProduct().getName() + "\" из выбранного?",
                            Arrays.asList(buttonTypeConfirm, buttonTypeCancel));
                    if (result.isPresent() && result.get() == buttonTypeConfirm) {
                        int i = toReleaseAvailabilityData.size() - 1;
                        Order order = toReleaseOrderData.get(row.getIndex());
                        while (i >= 0) {
                            if (order.getId() == toReleaseOrderData.get(i).getId()) {
                                toReleaseAvailabilityData.remove(i);
                                toReleaseOrderData.remove(i);
                            }
                            i--;
                        }
                        searchOrders(combobox_clientName.getValue(), textfield_article.getText());
                    }
                }

            });
            return row;
        });
    }
}
