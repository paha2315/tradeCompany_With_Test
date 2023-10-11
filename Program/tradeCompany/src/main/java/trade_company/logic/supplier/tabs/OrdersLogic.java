package trade_company.logic.supplier.tabs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import trade_company.logic.sql_object.Containers.ProductContainer;
import trade_company.logic.sql_object.Containers.WarehouseContainer;
import trade_company.logic.sql_object.Product;
import trade_company.logic.sql_object.Warehouse;
import trade_company.views.fictive_table_objects.SupplierOrderSelected;

import java.util.ArrayList;

public class OrdersLogic {
    protected ArrayList<Warehouse> warehouses;
    protected ArrayList<String> warehousesName;

    protected ObservableList<Product> products = FXCollections.observableArrayList();
    protected ObservableList<SupplierOrderSelected> selected = FXCollections.observableArrayList();

    protected Warehouse curWarehouse;

    public void initWarehouses() {
        warehousesName = new ArrayList<>();
        WarehouseContainer.setAll();
        warehouses = WarehouseContainer.getList();
        for (var warehouse : warehouses)
            warehousesName.add(warehouse.getName());
    }

    public void initProducts() {
        ProductContainer.setAll("WHERE (ID_Product IN (SELECT DISTINCT ID_Product FROM availability))");
        products.setAll(ProductContainer.getList());
    }

    public void search() {
        ProductContainer.setAll("WHERE (ID_Product IN (SELECT DISTINCT ID_Product FROM availability WHERE (ID_Warehouse = " + curWarehouse.getId() + ")))");
        ArrayList<Product> prods = ProductContainer.getList();
        products.clear();
        for (Product prod : prods) {
            boolean f = false;
            for (var sel : selected)
                if (sel.getProduct().getId() == prod.getId()) {
                    f = true;
                    break;
                }
            if (!f) products.add(prod);
        }
    }

    public void add(Warehouse warehouse, Product product, int count) {
        selected.add(new SupplierOrderSelected(warehouse, product, count));
    }
}
