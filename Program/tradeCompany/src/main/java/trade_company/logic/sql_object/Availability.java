package trade_company.logic.sql_object;

import SQL.DBObject;
import SQL.SSQLController;
import trade_company.logic.sql_object.Containers.AvailabilityContainer;
import trade_company.logic.sql_object.Containers.ProductContainer;
import trade_company.logic.sql_object.Containers.WarehouseContainer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Availability extends DBObject {
    Product product;
    Warehouse warehouse;
    double count;
    String packageType;
    double width, height, depth;
    int sectionNumber, stackNumber;
    double price;

    public Availability() {
        super();
    }

    public Availability(Availability availability) {
        super(availability.getId());
        set(availability);
    }

    public Availability(int id) {
        super(id);
        select();
    }

    public Availability(int id, int idProduct, int idWarehouse, double count, String packageType, double width, double height, double depth, double price, int sectionNumber, int stackNumber) {
        super(id);
        setProduct(idProduct);
        setWarehouse(idWarehouse);
        setCount(count);
        setPackageType(packageType);
        setWidth(width);
        setHeight(height);
        setDepth(depth);
        setSectionNumber(sectionNumber);
        setStackNumber(stackNumber);
        setPrice(price);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setProduct(int idProduct) {
        setProduct(ProductContainer.get(idProduct));
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public void setWarehouse(int idWarehouse) {
        setWarehouse(WarehouseContainer.get(idWarehouse));
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public int getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(int sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public int getStackNumber() {
        return stackNumber;
    }

    public void setStackNumber(int stackNumber) {
        this.stackNumber = stackNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void set(Availability availability) {
        setProduct(availability.getProduct());
        setWarehouse(availability.getWarehouse());
        setCount(availability.getCount());
        setPackageType(availability.getPackageType());
        setWidth(availability.getWidth());
        setHeight(availability.getHeight());
        setDepth(availability.getDepth());
        setSectionNumber(availability.getSectionNumber());
        setStackNumber(availability.getStackNumber());
        setPrice(availability.getPrice());
    }

    @Override
    public void select() {
        try {
            ResultSet result = SSQLController.Query("Select * from availability where ID_Availability=" + getId());
            if (!result.next()) throw new SQLException("No availability found with ID = " + getId());
            set(AvailabilityContainer.getAvailabilityFromResultSet(result));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getSQLUpdate() {
        return "UPDATE course_work.availability SET\n" + "ID_Product = " + getProduct().getId() + ",\n" + "ID_Warehouse = " + getWarehouse().getId() + ",\n" + "SectionNumber = " + getSectionNumber() + ",\n" + "StackNumber = " + getStackNumber() + ",\n" + "Count = " + this.getCount() + ",\n" + "Price = " + getPrice() + ",\n" + "PackageType = '" + getPackageType() + "',\n" + "PackageWidth = " + getWidth() + ",\n" + "PackageHeight = " + getHeight() + ",\n" + "PackageDepth = " + getDepth() + "\n" + "WHERE ID_Availability = " + getId() + ";";
    }

    @Override
    public String getSQLInsert() {
        return "INSERT INTO availability VALUES " + "(NULL," + getProduct().getId() + "," + getWarehouse().getId() + "," + getSectionNumber() + "," + getStackNumber() + "," + getCount() + "," + getPrice() + ",'" + getPackageType() + "'," + getWidth() + "," + getHeight() + "," + getDepth() + ");";
    }

    @Override
    public String getSQLDelete() {
        return "Delete from availability where ID_Availability = " + getId();
    }
}
