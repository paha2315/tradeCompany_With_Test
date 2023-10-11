package trade_company.logic.storekeeper.tabs;

import SQL.SSQLController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import trade_company.logic.sql_object.*;
import trade_company.logic.sql_object.Containers.AvailabilityContainer;
import trade_company.logic.sql_object.Containers.OrderContainer;
import trade_company.logic.sql_object.Containers.ProductContainer;
import trade_company.logic.sql_object.Containers.SupplierContainer;
import trade_company.models.Model;
import trade_company.models.UserDataModel;
import trade_company.report.reportClass;
import trade_company.views.fictive_table_objects.StorekeeperIncompleteOrder;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import static java.lang.Math.min;
import static trade_company.report.reportClass.requestSaveFolderFromUser;


public class ReleaseLogic {
    protected ObservableList<StorekeeperIncompleteOrder> incompleteOrdersData;
    protected ObservableList<Availability> toReleaseAvailabilityData;
    protected ArrayList<Order> toReleaseOrderData;
    protected ArrayList<String> comboboxClientsNamesData;

    protected final String ALL_CLIENTS = "Все заказчики";

    public ReleaseLogic() {
        incompleteOrdersData = FXCollections.observableArrayList();
        toReleaseAvailabilityData = FXCollections.observableArrayList();
        toReleaseOrderData = new ArrayList<>();
        comboboxClientsNamesData = new ArrayList<>();

        initComboboxClientsNamesData();
        initIncompleteOrdersTableData();
    }

    private void initComboboxClientsNamesData() {
        //Add "All" types
        comboboxClientsNamesData.add(ALL_CLIENTS);
        try {
            ResultSet result = SSQLController.Query("SELECT DISTINCT s.Name FROM supplier s"
                    + " JOIN orders o ON o.ID_Supplier=s.ID_Supplier;");
            while (result.next())
                comboboxClientsNamesData.add(result.getString("Name"));
            result.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void initIncompleteOrdersTableData() {
        var wh_id = UserDataModel.getInstance().getWarehouse().getId();
        OrderContainer.setAll("WHERE (ID_Warehouse=" + wh_id + " AND Completed=2 AND ID_Document is Null)");
        for (var item : OrderContainer.getList()) {
            var product = ProductContainer.get((int) Math.round(item.getPrice()));
            var incompleteOrder = new StorekeeperIncompleteOrder(item, product, 0, 0);
            ResultSet resultSet;
            double count = 0;
            try {
                resultSet = SSQLController.Query("SELECT sum(Count) as SUMM FROM availability\n" +
                        "WHERE (ID_Warehouse=" + wh_id + " AND ID_Product=" + Math.round(item.getPrice()) + ")");
                if (resultSet.next())
                    count = resultSet.getDouble("SUMM");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            incompleteOrder.setWillRemain(count);
            incompleteOrder.setAllCount(count);
            incompleteOrdersData.add(incompleteOrder);
        }
    }

    protected void moveIncompleteOrderToReleaseData(StorekeeperIncompleteOrder incompleteOrder) {
        var wh_id = UserDataModel.getInstance().getWarehouse().getId();
        var product = incompleteOrder.getProduct();
        AvailabilityContainer.setAll("WHERE (ID_Warehouse=" + wh_id + " AND ID_Product=" + product.getId() + " AND Count!=0)");

        ArrayList<Availability> sortedAvailabilityList = AvailabilityContainer.getList();
        sortedAvailabilityList.sort((lhs, rhs) -> Double.compare(rhs.getPrice(), lhs.getPrice()));

        double needCount = incompleteOrder.getOrder().getCount();

        int emptyAvailabilityCounter = 0;

        for (var item : sortedAvailabilityList) {
            if (needCount <= 0)
                break;

            double itemCount = item.getCount();
            // ищем в нижней таблице списания с этого элемента и уменьшаем количество доступного
            for (var item2 : toReleaseAvailabilityData) {
                if (item2.getId() == item.getId())
                    itemCount -= item2.getCount();
            }

            needCount -= itemCount;
            Availability fictionalAvail = null;

            if (itemCount == 0)
                emptyAvailabilityCounter++;

            if (needCount > 0) // полное списание
                fictionalAvail = new Availability(
                        item.getId(), item.getProduct().getId(), item.getWarehouse().getId(),
                        itemCount,
                        item.getPackageType(), item.getWidth(), item.getHeight(), item.getDepth(), item.getPrice(),
                        item.getSectionNumber(), item.getStackNumber());
            else               // частичное списание
                fictionalAvail = new Availability(
                        item.getId(), item.getProduct().getId(), item.getWarehouse().getId(),
                        item.getCount() + needCount,
                        item.getPackageType(), item.getWidth(), item.getHeight(), item.getDepth(), item.getPrice(),
                        item.getSectionNumber(), item.getStackNumber());
            if (itemCount != 0) {
                toReleaseAvailabilityData.add(fictionalAvail);
                toReleaseOrderData.add(new Order(
                        incompleteOrder.getOrder().getId(),
                        null,
                        incompleteOrder.getOrder().getSupplier().getId(),
                        wh_id,
                        fictionalAvail.getPrice(),
                        fictionalAvail.getCount(),
                        2
                ));
            }
        }
        if (emptyAvailabilityCounter == sortedAvailabilityList.size()) {
            var item = sortedAvailabilityList.get(0);
            var fictionalAvail = new Availability(
                    item.getId(), item.getProduct().getId(), item.getWarehouse().getId(),
                    0,
                    "", 0., 0., 0., 0.,
                    -1, -1);
            toReleaseAvailabilityData.add(fictionalAvail);
            toReleaseOrderData.add(new Order(
                    incompleteOrder.getOrder().getId(),
                    null,
                    incompleteOrder.getOrder().getSupplier().getId(),
                    wh_id,
                    fictionalAvail.getPrice(),
                    fictionalAvail.getCount(),
                    2
            ));
        }

        incompleteOrdersData.remove(incompleteOrder);
    }

    private String getSearchQuery(String name, String filter) {
        Set<Integer> selectedOrders = new HashSet<>();
        for (var item : toReleaseOrderData)
            selectedOrders.add(item.getId());
        var sb = new StringBuilder();
        for (var order_id : selectedOrders)
            sb.append(order_id).append(",");
        String filterBySelectedOrders = sb.toString();
        if (!filterBySelectedOrders.isEmpty()) {
            filterBySelectedOrders = "o.ID_Order NOT IN (" + filterBySelectedOrders.substring(0, filterBySelectedOrders.length() - 1) + ") AND ";
        }

        String filterByClientName = name.equals(ALL_CLIENTS) ? "" : " AND s.Name='" + name + "'";
        return "SELECT * FROM orders o"
                + " JOIN product p ON (" + filterBySelectedOrders + " p.ID_Product = o.Price)"
                + " JOIN supplier s ON (o.ID_Supplier = s.ID_Supplier" + filterByClientName + ")"
                + " WHERE p.Article like '%" + filter + "%' AND o.ID_Document is Null AND Completed=2"
                + " AND ID_Warehouse=" + UserDataModel.getInstance().getWarehouse().getId() + ";";
    }

    protected void searchOrders(String name, String filter) {
        String sqlQuery = getSearchQuery(name, filter);
        var wh_id = UserDataModel.getInstance().getWarehouse().getId();
        incompleteOrdersData.clear();
        try {
            ResultSet result = SSQLController.Query(sqlQuery);
            while (result.next()) {
                var product = ProductContainer.get(Math.round(result.getInt("Price")));
                incompleteOrdersData.add(new StorekeeperIncompleteOrder(OrderContainer.getOrderFromResultSet(result), product, 0, 0));
            }
            for (var item : incompleteOrdersData) {
                result = SSQLController.Query("SELECT sum(Count) AS allCount FROM availability\n" +
                        "WHERE (ID_Warehouse=" + wh_id + " AND ID_Product=" + Math.round(item.getOrder().getPrice()) + ")");
                if (result.next()) {
                    item.setAllCount(result.getDouble("allCount"));
                    //заполнение willRemain
                    double willRemain = item.getAllCount();
                    for (var item2 : toReleaseAvailabilityData)
                        if (item2.getProduct().getId() == item.getProduct().getId())
                            willRemain -= item2.getCount();
                    item.setWillRemain(willRemain);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void onReleaseCommit() {
        Map<Integer, ArrayList<Availability>> availabilityMap = new HashMap<>();
        Map<Integer, Double> countMap = new HashMap<>();

        // Для М-15
        HashMap<Integer, ArrayList<Availability>> groupByClients = new HashMap<>();

        for (int i = 0; i < toReleaseOrderData.size(); i++) {
            var order_id = toReleaseOrderData.get(i).getId();
            if (!availabilityMap.containsKey(order_id)) {
                availabilityMap.put(order_id, new ArrayList<>());
                countMap.put(order_id, 0.);
            }
            availabilityMap.get(order_id).add(toReleaseAvailabilityData.get(i));
            countMap.put(order_id, countMap.get(order_id) + toReleaseAvailabilityData.get(i).getCount());

            var client_id = toReleaseOrderData.get(i).getSupplier().getId();
            if (!groupByClients.containsKey(client_id))
                groupByClients.put(client_id, new ArrayList<>());
            groupByClients.get(client_id).add(toReleaseAvailabilityData.get(i));
        }

        try {
            for (var item : groupByClients.entrySet())
                makeM15(item.getValue(), SupplierContainer.get(item.getKey()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        toReleaseOrderData.clear();
        toReleaseAvailabilityData.clear();
        Date date = new Date();
        for (var item : countMap.entrySet()) {
            try {
                Order order = OrderContainer.get(item.getKey());
                if (item.getValue() == order.getCount()) {
                    Document document = new Document();
                    document.setDate(date);
                    document.save();
                    for (var availability : availabilityMap.get(item.getKey())) {
                        Availability_link availLink = new Availability_link();
                        Availability trueAvail = AvailabilityContainer.get(availability.getId());

                        availLink.setDocument(document);
                        availLink.setAvailability(trueAvail);
                        availLink.setCount(-availability.getCount());
                        availLink.save();

                        trueAvail.setCount(trueAvail.getCount() - availability.getCount());
                        trueAvail.save();
                    }
                    order.setDocument(document);
                    order.setCompleted(6);
                    order.save();
                } else {
                    for (var availability : availabilityMap.get(item.getKey())) {
                        toReleaseOrderData.add(order);
                        toReleaseAvailabilityData.add(availability);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    protected void makeM15(ArrayList<Availability> availabilities, Supplier supplier) throws IOException {
        String formName = "М-15";
        var formWriter = new reportClass();

        String folder = requestSaveFolderFromUser(formName);
        if (folder == null) {
            Model.getInstance().getWindowFactory().showAlert(Alert.AlertType.WARNING, "Сохранение накладной на отпуск товаров на сторону", "", "Пользователь отказался от сохранения файлов.");
            return;
        }

        int orderNumber = 1;
        try {
            var result = SSQLController.Query("SELECT count(*) cnt FROM orders WHERE orders.Completed=6;");
            result.next();
            orderNumber = result.getInt("cnt") + 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String filePath = folder + "\\" + formName + "_" + orderNumber + "_" + Document.getFormattedDate(LocalDate.now());

        formWriter.makeM15(orderNumber, availabilities, supplier, filePath);
    }
}
