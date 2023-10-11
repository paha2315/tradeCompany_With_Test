package trade_company.logic.sql_object.Containers;

import SQL.SSQLController;
import trade_company.logic.sql_object.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class OrderContainer {
    static Map<Integer, Order> container = new TreeMap<>();
    static ArrayList<Order> notSavedObjects = new ArrayList<>();

    public static Order get(int id) {
        if (!container.containsKey(id)) container.put(id, new Order(id));
        return container.get(id);
    }

    public static void setAll() {
        setAll("");
    }

    public static void setAll(String where) {
        container.clear();
        try {
            ResultSet result = SSQLController.Query("SELECT * FROM orders " + where);
            while (result.next()) {
                Order avail = getOrderFromResultSet(result);
                container.put(avail.getId(), avail);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Order getOrderFromResultSet(ResultSet result) throws SQLException {
        int id = result.getInt("ID_Order");
        Integer documtntID;
        documtntID = result.getInt("ID_Document");
        if (result.wasNull()) documtntID = null;
        int idSupplier = result.getInt("ID_Supplier");
        int idWarehouse = result.getInt("ID_Warehouse");
        double price = result.getDouble("Price");
        double count = result.getDouble("Count");
        int completed = result.getInt("Completed");

        return new Order(id, documtntID, idSupplier, idWarehouse, price, count, completed);
    }

    public static ArrayList<Order> getList() {
        ArrayList<Order> list = new ArrayList<>();
        for (var entry : container.entrySet()) {
            list.add(entry.getValue());
        }
        return list;
    }

    public void add(Order object) {
        if (object.getId() == 0) notSavedObjects.add(object);
        else container.put(object.getId(), object);
    }

    public void save() throws SQLException {
        for (var object : container.entrySet()) {
            object.getValue().update();
        }
        for (var object : notSavedObjects) {
            object.save();
            add(object);
        }
        notSavedObjects.clear();
    }
}
