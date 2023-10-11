package trade_company.logic.sql_object.Containers;

import SQL.SSQLController;
import trade_company.logic.sql_object.Supplier;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class SupplierContainer {
    static Map<Integer, Supplier> container = new TreeMap<>();
    static ArrayList<Supplier> notSavedObjects = new ArrayList<>();

    public static Supplier get(int id) {
        if (!container.containsKey(id)) container.put(id, new Supplier(id));
        return container.get(id);
    }

    public static void setAll() {
        setAll("");
    }

    public static void setAll(String where) {
        container.clear();
        try {
            ResultSet result = SSQLController.Query("SELECT * FROM supplier " + where);
            while (result.next()) {
                Supplier supplier = getSupplierFromResultSet(result);
                container.put(supplier.getId(), supplier);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Supplier getSupplierFromResultSet(ResultSet result) throws SQLException {
        int id = result.getInt("ID_Supplier");
        int idProduct = result.getInt("ID_Product");
        String name, city, addressFull;
        int okpo = result.getInt("OKPO");
        String paymentAccount, phone, zipCode, bankInfo;
        name = result.getString("Name");
        city = result.getString("City");
        addressFull = result.getString("AddressFull");
        paymentAccount = result.getString("PaymentAccount");
        phone = result.getString("Phone");
        zipCode = result.getString("ZipCode");
        bankInfo = result.getString("BankInfo");
        return new Supplier(id, idProduct, name, city, addressFull, okpo, paymentAccount, phone, zipCode, bankInfo);
    }

    public static ArrayList<Supplier> getList() {
        ArrayList<Supplier> list = new ArrayList<>();
        for (var entry : container.entrySet()) {
            list.add(entry.getValue());
        }
        return list;
    }

    public void add(Supplier object) {
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
