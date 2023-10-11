package trade_company.logic.storekeeper.tabs;

import SQL.SSQLController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import trade_company.logic.sql_object.Availability;
import trade_company.logic.sql_object.Containers.OrderContainer;
import trade_company.logic.sql_object.Document;
import trade_company.logic.sql_object.Order;
import trade_company.logic.sql_object.Supplier;
import trade_company.models.Model;
import trade_company.models.UserDataModel;
import trade_company.report.reportClass;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static trade_company.report.reportClass.requestSaveFolderFromUser;

public class AddAvailLogic {
    protected ObservableList<Order> incomingOrderData;
    protected ObservableList<Availability> toAcceptAvailabilityData;
    protected ArrayList<Order> toAcceptOrderData;
    protected ArrayList<String> comboboxCityData;

    protected final String ALL_CITIES = "Все заказчики";

    public AddAvailLogic() {
        incomingOrderData = FXCollections.observableArrayList();
        toAcceptAvailabilityData = FXCollections.observableArrayList();
        toAcceptOrderData = new ArrayList<>();
        comboboxCityData = new ArrayList<>();

        initComboBoxCity();
        initIncomingData();
    }

    protected void initIncomingData() {
        OrderContainer.setAll("WHERE (ID_Document is Null AND Completed IN (1, 4) AND ID_Warehouse=" + UserDataModel.getInstance().getWarehouse().getId() + ")");
        incomingOrderData = FXCollections.observableArrayList(OrderContainer.getList());
    }

    String getSearchQuery(String city, String filter) {
        Set<Integer> selectedOrders = new HashSet<>();
        for (var item : toAcceptAvailabilityData)
            selectedOrders.add(item.getId());
        var sb = new StringBuilder();
        for (var order_id : selectedOrders)
            sb.append(order_id).append(",");
        String filterBySelectedOrders = sb.toString();
        if (!filterBySelectedOrders.isEmpty()) {
            filterBySelectedOrders = "o.ID_Order NOT IN ("
                    + filterBySelectedOrders.substring(0, filterBySelectedOrders.length() - 1) + ") AND ";
        }

        String filterBySupplierCity = city.equals(ALL_CITIES) ? "" : " AND s.City='" + city + "'";
        return "SELECT * FROM orders o"
                + " JOIN supplier s ON (o.ID_Supplier = s.ID_Supplier" + filterBySupplierCity + ")"
                + " JOIN product p ON (" + filterBySelectedOrders + " p.ID_Product = s.ID_Product)"
                + " WHERE p.Article like '%" + filter + "%' AND o.ID_Document is Null AND Completed IN (1, 4)"
                + " AND ID_Warehouse=" + UserDataModel.getInstance().getWarehouse().getId() + ";";
    }

    private void initComboBoxCity() {
        //Add "All" types
        comboboxCityData.add(ALL_CITIES);
        try {
            ResultSet result = SSQLController.Query("SELECT DISTINCT s.City FROM supplier s"
                    +" JOIN orders o ON o.ID_Supplier=s.ID_Supplier;");
            while (result.next()) {
                comboboxCityData.add(result.getString("City"));
            }
            result.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void searchOrders(String city, String search) {
        String sqlQuery = getSearchQuery(city, search);
        incomingOrderData.clear();
        ArrayList<Order> orders = new ArrayList<>();
        try {
            ResultSet result = SSQLController.Query(sqlQuery);
            while (result.next()) {
                orders.add(OrderContainer.getOrderFromResultSet(result));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (var order : orders) {
            boolean f = true;
            for (var saveOrder : toAcceptOrderData)
                if (saveOrder.getId() == order.getId()) {
                    f = false;
                    break;
                }
            if (f)
                incomingOrderData.add(order);
        }
    }

    protected void makeTorg11(ArrayList<Availability> availabilities) throws IOException {
        String formName = "ТОРГ-11";
        var formWriter = new reportClass();

        String folder = requestSaveFolderFromUser(formName);
        if (folder == null) {
            Model.getInstance().getWindowFactory().showAlert(Alert.AlertType.WARNING, "Сохранение товарного ярлыка", "", "Пользователь отказался от сохранения файлов.");
            return;
        }

        String filePath = folder + "\\" + formName + "_" + Document.getFormattedDate(LocalDate.now()) + "_" + availabilities.get(0).getSectionNumber() + "-" + availabilities.get(0).getStackNumber();

        final int SIZE = 7;
        int filesWritten = 0;
        int counter = SIZE;

        ArrayList<Availability> toFile = new ArrayList<>();

        for (var item : availabilities) {
            if (counter == 0) {
                counter = SIZE;
                // drop file
                String filePath1 = filePath;
                if (filesWritten != 0)
                    filePath1 += "(" + filesWritten + ")";
                formWriter.makeTORG11(filesWritten + 1, toFile, UserDataModel.getInstance().getPerson(), filePath1);

                filesWritten++;
                toFile.clear();
            }
            toFile.add(item);
            counter--;
        }

        if (counter != SIZE) {
            //drop file
            String filePath1 = filePath;
            if (filesWritten != 0)
                filePath1 += "(" + filesWritten + ")";
            formWriter.makeTORG11(filesWritten + 1, toFile, UserDataModel.getInstance().getPerson(), filePath1);
        }
    }

    protected void makeM4(ArrayList<Availability> availabilities, Supplier supplier) throws IOException {
        String formName = "М-4";
        var formWriter = new reportClass();

        String folder = requestSaveFolderFromUser(formName);
        if (folder == null) {
            Model.getInstance().getWindowFactory().showAlert(Alert.AlertType.WARNING, "Сохранение приходного ордера", "", "Пользователь отказался от сохранения файлов.");
            return;
        }

        int orderNumber = 1;
        try {
            var result = SSQLController.Query("SELECT count(*) cnt FROM orders WHERE orders.Completed IN (1,4);");
            result.next();
            orderNumber = result.getInt("cnt") + 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String filePath = folder + "\\" + formName + "_" + orderNumber + "_" + Document.getFormattedDate(LocalDate.now());

        formWriter.makeM4(orderNumber, supplier, UserDataModel.getInstance().getWarehouse(), availabilities, UserDataModel.getInstance().getPerson(), filePath);
    }
}
