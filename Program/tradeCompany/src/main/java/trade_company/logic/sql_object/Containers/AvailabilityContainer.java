package trade_company.logic.sql_object.Containers;

import SQL.SSQLController;
import trade_company.logic.sql_object.Availability;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class AvailabilityContainer {
    static Map<Integer, Availability> container = new TreeMap<>();
    static ArrayList<Availability> notSavedObjects = new ArrayList<>();

    public static Availability get(int id) {
        if (!container.containsKey(id)) container.put(id, new Availability(id));
        return container.get(id);
    }

    public static void setAll() {
        setAll("");
    }

    public static void setAll(String where) {
        container.clear();
        try {
            ResultSet result = SSQLController.Query("Select * from Availability " + where);
            while (result.next()) {
                Availability avail = getAvailabilityFromResultSet(result);
                container.put(avail.getId(), avail);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Availability getAvailabilityFromResultSet(ResultSet result) throws SQLException {
        int id = result.getInt("ID_Availability");
        int idProduct = result.getInt("ID_Product");
        int idWarehouse = result.getInt("ID_Warehouse");
        int count = result.getInt("Count");
        String packageType = result.getString("PackageType");
        double width = result.getDouble("PackageWidth");
        double height = result.getDouble("PackageHeight");
        double depth = result.getDouble("PackageDepth");
        int sectionNumber = result.getInt("SectionNumber");
        int stackNumber = result.getInt("StackNumber");
        double price = result.getDouble("Price");
        return new Availability(id, idProduct, idWarehouse, count, packageType, width, height, depth, price, sectionNumber, stackNumber);
    }

    public static ArrayList<Availability> getList() {
        ArrayList<Availability> list = new ArrayList<>();
        for (var entry : container.entrySet()) {
            list.add(entry.getValue());
        }
        return list;
    }

    public void add(Availability object) {
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
