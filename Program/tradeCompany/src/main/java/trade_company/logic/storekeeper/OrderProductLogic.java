package trade_company.logic.storekeeper;

import SQL.SSQLController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import trade_company.logic.sql_object.Containers.SupplierContainer;
import trade_company.logic.sql_object.Order;
import trade_company.logic.sql_object.Supplier;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderProductLogic {
    public final String ALL_CITIES = "Все города";
    public ObservableList<Supplier> suppliers = FXCollections.observableArrayList();
    public ObservableList<Order> orders = FXCollections.observableArrayList();
    public ArrayList<String> citys = new ArrayList<>();

    public void initSuppliers() {
        SupplierContainer.setAll();
        suppliers.setAll(SupplierContainer.getList());
    }

    private String SQLQ(String city, String filter) {

        String filterBySupplierCity = city.equals(ALL_CITIES) ? "" : " AND s.City='" + city + "'";

        return "SELECT ID_Supplier FROM supplier s " +
               "JOIN product p ON s.ID_Product = p.ID_Product " +
               "WHERE p.Article LIKE '%" + filter + "%' " + filterBySupplierCity;
    }

    public void initComboBoxCity() {
        citys.add(ALL_CITIES);
        try {
            ResultSet result = SSQLController.Query("SELECT DISTINCT city FROM supplier");
            while (result.next()) {
                citys.add(result.getString("city"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void search(String city, String filter) {
        String sqlQuery = SQLQ(city, filter);
        suppliers.clear();
        try {
            ResultSet result = SSQLController.Query(sqlQuery);
            while (result.next()) {
                Supplier supplier = SupplierContainer.get(result.getInt("ID_Supplier"));
                boolean f = false;
                for (var order : orders) {
                    if (order.getSupplier().getId() == supplier.getId()) {
                        f = true;
                        break;
                    }
                }
                if (!f) suppliers.add(supplier);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
