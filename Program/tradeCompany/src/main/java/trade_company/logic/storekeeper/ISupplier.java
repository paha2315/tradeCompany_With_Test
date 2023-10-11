package trade_company.logic.storekeeper;

import trade_company.logic.sql_object.Product;

public interface ISupplier {
    int getId();

    void setId(int id);

    Product getProduct();

    void setProduct(Product product);

    void setProduct(int idProduct);

    String getName();

    void setName(String name);

    String getPhone();

    void setPhone(String phone);
}
