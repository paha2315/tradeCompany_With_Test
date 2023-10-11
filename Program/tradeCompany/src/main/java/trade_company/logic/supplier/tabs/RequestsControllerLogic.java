package trade_company.logic.supplier.tabs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import trade_company.logic.sql_object.Containers.OrderContainer;
import trade_company.logic.sql_object.Containers.WarehouseContainer;
import trade_company.logic.sql_object.Order;
import trade_company.logic.sql_object.Warehouse;
import trade_company.models.UserDataModel;

import java.util.ArrayList;

public class RequestsControllerLogic {
    protected ArrayList<Warehouse> warehouses;
    protected ArrayList<String> warehousesName;
    protected ObservableList<Order> requests = FXCollections.observableArrayList();
    protected ObservableList<Order> selected = FXCollections.observableArrayList();

    public void initWarehouses() {
        warehousesName = new ArrayList<>();
        WarehouseContainer.setAll();
        warehouses = WarehouseContainer.getList();
        for (var warehouse : warehouses)
            warehousesName.add(warehouse.getName());
    }

    public void initRequests() {
        var suppId = UserDataModel.getInstance().getPerson().getSupplier().getId();
        OrderContainer.setAll("WHERE (Completed = 0) AND (ID_Supplier = " + suppId + ")");
        requests.setAll(OrderContainer.getList());
    }

    public void search(Warehouse warehouse) {
        var suppId = UserDataModel.getInstance().getPerson().getSupplier().getId();
        OrderContainer.setAll("WHERE (Completed = 0) AND (ID_Supplier = " + suppId + ") AND (ID_Warehouse = " + warehouse.getId() + ")");
        ArrayList<Order> orders = OrderContainer.getList();
        requests.clear();
        for (Order cur : orders) {
            boolean f = false;
            for (var sel : selected)
                if (sel.getId() == cur.getId()) {
                    f = true;
                    break;
                }
            if (!f)
                requests.add(cur);
        }
    }
}
