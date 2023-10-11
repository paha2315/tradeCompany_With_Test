package trade_company.logic.storekeeper;

import trade_company.logic.sql_object.Product;
import trade_company.logic.sql_object.Warehouse;

public interface IAvailability {
    int getId();

    void setId(int id);

    Product getProduct();

    void setProduct(int idProduct);

    Warehouse getWarehouse();

    void setWarehouse(int idWarehouse);

    double getCount();

    void setCount(double count);

    String getPackageType();

    void setPackageType(String packageType);

    double getWidth();

    void setWidth(double width);

    double getHeight();

    void setHeight(double height);

    double getDepth();

    void setDepth(double depth);

    int getSectionNumber();

    void setSectionNumber(int sectionNumber);

    int getStackNumber();

    void setStackNumber(int stackNumber);
}
