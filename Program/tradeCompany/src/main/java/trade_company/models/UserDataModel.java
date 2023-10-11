package trade_company.models;

import trade_company.logic.sql_object.Person;
import trade_company.logic.sql_object.Product;
import trade_company.logic.sql_object.Supplier;
import trade_company.logic.sql_object.Warehouse;

public class UserDataModel {
    private static volatile UserDataModel instance;

    private Person person;
    private Warehouse warehouse;
    private Supplier supplier;
    private Product product;

    private UserDataModel() {
        person = null;
        warehouse = null;
    }

    public static UserDataModel getInstance() {
        UserDataModel localInstance = instance;
        if (localInstance == null) {
            synchronized (UserDataModel.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new UserDataModel();
                }
            }
        }
        return localInstance;
    }


    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
