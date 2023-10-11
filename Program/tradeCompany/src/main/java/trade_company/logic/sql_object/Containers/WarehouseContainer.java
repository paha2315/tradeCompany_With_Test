package trade_company.logic.sql_object.Containers;

import SQL.SSQLController;
import trade_company.logic.sql_object.Warehouse;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class WarehouseContainer {
    static Map<Integer, Warehouse> container = new TreeMap<>();
    static ArrayList<Warehouse> notSavedObjects = new ArrayList<>();

    public static Warehouse get(int id) {
        if (!container.containsKey(id)) container.put(id, new Warehouse(id));
        return container.get(id);
    }

    public static void setAll() {
        setAll("");
    }

    public static void setAll(String where) {
        container.clear();
        try {
            ResultSet result = SSQLController.Query("Select * from Warehouse " + where);
            while (result.next()) {
                Warehouse warehouse = getWarehouseFromResultSet(result);
                container.put(warehouse.getId(), warehouse);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Warehouse getWarehouseFromResultSet(ResultSet result) throws SQLException {
        int id = result.getInt("ID_Warehouse");
        String name = result.getString("Name");
        String city = result.getString("City");
        String street = result.getString("Street");
        int buildingNumber = result.getInt("BuildingNumber");
        String resq = result.getString("BuildingLiteral");
        char buildingLiteral = '\0';
        if (resq.length() > 0) buildingLiteral = resq.charAt(0);
        return new Warehouse(id, name, city, street, buildingNumber, buildingLiteral);
    }

    public static ArrayList<Warehouse> getList() {
        ArrayList<Warehouse> list = new ArrayList<>();
        for (var entry : container.entrySet()) {
            list.add(entry.getValue());
        }
        return list;
    }

    public void add(Warehouse object) {
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
