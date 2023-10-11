package trade_company.views.fictive_table_objects;

import trade_company.logic.sql_object.Order;
import trade_company.logic.sql_object.Product;

public class StorekeeperIncompleteOrder {
    Order order;
    Product product;
    double willRemain;
    double allCount;

    public StorekeeperIncompleteOrder() {
        this.order = null;
        this.product = null;
        this.willRemain = 0.;
        this.allCount = 0.;
    }

    public StorekeeperIncompleteOrder(Order order, Product product, double willRemain, double allCount) {
        this.order = order;
        this.product = product;
        this.willRemain = willRemain;
        this.allCount = allCount;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public double getAllCount() {
        return allCount;
    }

    public void setAllCount(double allCount) {
        this.allCount = allCount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getWillRemain() {
        return willRemain;
    }

    public void setWillRemain(double willRemain) {
        this.willRemain = willRemain;
    }
}
