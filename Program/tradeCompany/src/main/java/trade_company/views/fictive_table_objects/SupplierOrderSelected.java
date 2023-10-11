package trade_company.views.fictive_table_objects;

import trade_company.logic.sql_object.Product;
import trade_company.logic.sql_object.Warehouse;

public class SupplierOrderSelected {
    Warehouse warehouse;
    Product product;
    double count;

    public SupplierOrderSelected() {
        this.warehouse = null;
        this.product = null;
        this.count = 0.;
    }

    public SupplierOrderSelected(Warehouse warehouse, Product product, double count) {
        this.warehouse = warehouse;
        this.product = product;
        this.count = count;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }
}
