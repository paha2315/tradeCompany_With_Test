package trade_company.logic.sql_object.Containers;

import SQL.SSQLController;
import trade_company.logic.sql_object.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class ProductContainer {
    static Map<Integer, Product> container = new TreeMap<>();
    static ArrayList<Product> notSavedObjects = new ArrayList<>();

    public static Product get(int id) {
        if (!container.containsKey(id)) container.put(id, new Product(id));
        return container.get(id);
    }

    public static void setAll() {
        setAll("");
    }

    public static void setAll(String where) {
        container.clear();
        try {
            ResultSet result = SSQLController.Query("Select * from Product " + where);
            while (result.next()) {
                Product product = getProductFromResultSet(result);
                container.put(product.getId(), product);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Product getProductFromResultSet(ResultSet result) throws SQLException {
        int id = result.getInt("ID_Product");
        String name = result.getString("Name");
        String description = result.getString("Description");
        String nomenclature = result.getString("Nomenclature");
        String article = result.getString("Article");
        int okeiID = result.getInt("ID_OKEI");
        double mass = result.getDouble("Mass");
        String storageConditions = result.getString("StorageConditions");
        int percentNDS = result.getInt("PercentNDS");
        boolean deleted = result.getBoolean("Deleted");
        return new Product(id, name, description, nomenclature, article, okeiID, mass, percentNDS, storageConditions, deleted);
    }

    public static ArrayList<Product> getList() {
        ArrayList<Product> list = new ArrayList<>();
        for (var entry : container.entrySet()) {
            list.add(entry.getValue());
        }
        return list;
    }

    public void add(Product object) {
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
