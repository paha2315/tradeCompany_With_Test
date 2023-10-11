package trade_company.controllers.storekeeper.tabs;

import SQL.SSQLController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import trade_company.logic.sql_object.*;
import trade_company.logic.sql_object.Containers.AvailabilityContainer;
import trade_company.logic.sql_object.Containers.SupplierContainer;
import trade_company.logic.storekeeper.tabs.AddAvailLogic;
import trade_company.models.DialogDataModel;
import trade_company.models.Model;
import trade_company.models.UserDataModel;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class AddAvailController extends AddAvailLogic {
    @FXML
    private ComboBox<String> combobox_supplierCity;
    @FXML
    private TextField textfield_articleFilter;

    @FXML
    private TableView<Order> table_incoming;
    @FXML
    private TableColumn<Order, String> column_incoming_nomenclature;
    @FXML
    private TableColumn<Order, String> column_incoming_article;
    @FXML
    private TableColumn<Order, String> column_incoming_name;
    @FXML
    private TableColumn<Order, String> column_incoming_measure;
    @FXML
    private TableColumn<Order, Double> column_incoming_count;
    @FXML
    private TableColumn<Order, Double> column_incoming_mass;

    @FXML
    private TableView<Availability> table_toAccept;
    @FXML
    private TableColumn<Availability, String> column_toAccept_article;
    @FXML
    private TableColumn<Availability, String> column_toAccept_name;
    @FXML
    private TableColumn<Availability, Double> column_toAccept_count;
    @FXML
    private TableColumn<Availability, String> column_toAccept_measure;
    @FXML
    private TableColumn<Availability, Integer> column_toAccept_section_number;
    @FXML
    private TableColumn<Availability, Integer> column_toAccept_stack_number;

    @FXML
    private Button button_acceptGoods;

    @FXML
    void initialize() {
        combobox_supplierCity.setItems(FXCollections.observableArrayList(comboboxCityData));
        combobox_supplierCity.getSelectionModel().select(0);

        initTableIncoming();
        initTableToAccept();
        setupListeners();
    }

    private void setupListeners() {
        button_acceptGoods.setOnAction(this::acceptGoodsCommit);
    }

    private static int hashSectionAndStack(int section, int stack) {
        return section * 2000083 + stack;
    }

    @FXML
    void acceptGoodsCommit(ActionEvent event) {
        var document = new Document();
        document.setDate(new Date());
        try {
            document.save();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        HashMap<Integer, ArrayList<Availability>> availabilities = new HashMap<>();
        AvailabilityContainer.setAll("WHERE Count != 0;");
        for (var item : AvailabilityContainer.getList()) {
            int hash = hashSectionAndStack(item.getSectionNumber(), item.getStackNumber());
            if (!availabilities.containsKey(hash))
                availabilities.put(hash, new ArrayList<>());
            availabilities.get(hash).add(item);
        }

        Set<Integer> recentlyAddedAvailabilities = new HashSet<>();

        try {
            HashMap<Integer, ArrayList<Availability>> groupBySuppliers = new HashMap<>();
            for (int i = 0; i < toAcceptAvailabilityData.size(); i++) {
                int supp_id = toAcceptOrderData.get(i).getSupplier().getId();
                if (!groupBySuppliers.containsKey(supp_id))
                    groupBySuppliers.put(supp_id, new ArrayList<>());
                groupBySuppliers.get(supp_id).add(toAcceptAvailabilityData.get(i));
            }
            for (var item : groupBySuppliers.entrySet())
                makeM4(item.getValue(), SupplierContainer.get(item.getKey()));

        } catch (IOException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < toAcceptAvailabilityData.size(); i++) {
            toAcceptAvailabilityData.get(i).setWarehouse(UserDataModel.getInstance().getWarehouse());
            try {
                Availability avail = toAcceptAvailabilityData.get(i);
                ResultSet result = SSQLController.Query("SELECT ID_Availability FROM availability " +
                        "WHERE ID_Product=" + toAcceptAvailabilityData.get(i).getProduct().getId() + " AND ID_Warehouse=" + toAcceptAvailabilityData.get(i).getWarehouse().getId() + " AND Price=" + toAcceptAvailabilityData.get(i).getPrice());
                if (result.next()) {
                    var trueAvailability = AvailabilityContainer.get(result.getInt("ID_Availability"));
                    trueAvailability.setCount(trueAvailability.getCount() + toAcceptAvailabilityData.get(i).getCount());
                    avail = trueAvailability;
                }

                // Набираем данные для создания ТОРГ-11
                if (avail.getId() == 0) {
                    int hash = hashSectionAndStack(avail.getSectionNumber(), avail.getStackNumber());
                    recentlyAddedAvailabilities.add(hash);
                    if (!availabilities.containsKey(hash))
                        availabilities.put(hash, new ArrayList<>());
                    availabilities.get(hash).add(avail);
                }

                avail.save();

                toAcceptOrderData.get(i).setDocument(document);
                if (toAcceptOrderData.get(i).getCompleted() == 1) {
                    toAcceptOrderData.get(i).setCompleted(5);
                }
                toAcceptOrderData.get(i).update();

                Availability_link availability_link = new Availability_link();
                availability_link.setDocument(document);
                availability_link.setAvailability(avail);
                availability_link.setCount(toAcceptAvailabilityData.get(i).getCount());
                availability_link.save();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            for (var hash : recentlyAddedAvailabilities)
                makeTorg11(availabilities.get(hash));
        } catch (IOException e) {
            e.printStackTrace();
        }

        toAcceptAvailabilityData.clear();
        toAcceptOrderData.clear();
        searchOrders(combobox_supplierCity.getValue(), textfield_articleFilter.getText());
    }

    @FXML
    void searchAvail(KeyEvent event) {
        searchOrders(combobox_supplierCity.getValue(), textfield_articleFilter.getText());
    }

    void initTableIncoming() {
        initIncomingData();
        column_incoming_nomenclature.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSupplier().getProduct().getNomenclature()));
        column_incoming_article.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSupplier().getProduct().getArticle()));
        column_incoming_name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSupplier().getProduct().getName()));
        column_incoming_measure.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSupplier().getProduct().getOkei().getName()));
        column_incoming_count.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCount()));
        column_incoming_mass.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSupplier().getProduct().getMass()));
        table_incoming.setItems(incomingOrderData);

        table_incoming.setRowFactory(tv -> {
            TableRow<Order> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2 && !row.isEmpty()) {
                    Model.getInstance().getDialogFactory().showAddProductDetailsDialog(row.getItem().getSupplier().getProduct().getName());

                    var data = DialogDataModel.getInstance().getAddProductDetailsDialogData();
                    if (data.isOk()) {
                        var availability = new Availability();
                        availability.setProduct(row.getItem().getSupplier().getProduct());
                        availability.setWarehouse(UserDataModel.getInstance().getWarehouse());
                        availability.setSectionNumber(data.getSection());
                        availability.setStackNumber(data.getStack());

                        availability.setCount(row.getItem().getCount());
                        availability.setPrice(row.getItem().getPrice());

                        availability.setPackageType(data.getPackageType());
                        availability.setWidth(data.getPackageWidth());
                        availability.setHeight(data.getPackageHeight());
                        availability.setDepth(data.getPackageDepth());

                        toAcceptAvailabilityData.add(availability);
                        toAcceptOrderData.add(row.getItem());
                        incomingOrderData.remove(row.getItem());
                    } else if (data.wasException()) {
                        Model.getInstance().getWindowFactory().showAlert(
                                Alert.AlertType.WARNING,
                                "Приход товара",
                                "",
                                "Были введены некорректные данные.\nПовторите ввод.");
                    }
                }
            });
            return row;
        });
    }

    void initTableToAccept() {
        column_toAccept_article.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getArticle()));
        column_toAccept_name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getName()));
        column_toAccept_measure.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getOkei().getName()));
        column_toAccept_count.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCount()));
        column_toAccept_section_number.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSectionNumber()));
        column_toAccept_stack_number.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStackNumber()));
        table_toAccept.setItems(toAcceptAvailabilityData);

        table_toAccept.setRowFactory(tv -> {
            TableRow<Availability> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2 && !row.isEmpty()) {
                    ButtonType buttonTypeConfirm = new ButtonType("Подтвердить", ButtonBar.ButtonData.APPLY);
                    ButtonType buttonTypeCancel = new ButtonType("Отменить", ButtonBar.ButtonData.CANCEL_CLOSE);
                    Model.getInstance().getWindowFactory().showConfirmationAlert(
                            "Удалить",
                            "",
                            "Удалить \"" + row.getItem().getProduct().getName() + "\" из выбранного?",
                            Arrays.asList(buttonTypeConfirm, buttonTypeCancel)
                    ).ifPresent(buttonType -> {
                        if (buttonType == buttonTypeConfirm) {
                            toAcceptAvailabilityData.remove(row.getItem());
                            toAcceptOrderData.remove(row.getIndex());
                            searchOrders(combobox_supplierCity.getValue(), textfield_articleFilter.getText());
                        }
                    });
                }

            });
            return row;
        });
    }

    @FXML
    void selectedSupplierCity(ActionEvent event) {
        searchOrders(combobox_supplierCity.getValue(), textfield_articleFilter.getText());
    }
}
