package trade_company.logic.sql_object;

import SQL.DBObject;
import SQL.SSQLController;
import trade_company.logic.sql_object.Containers.DocumentContainer;
import trade_company.logic.sql_object.Containers.OrderContainer;
import trade_company.logic.sql_object.Containers.SupplierContainer;
import trade_company.logic.sql_object.Containers.WarehouseContainer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Order extends DBObject {

    Document document;
    Supplier supplier;
    Warehouse warehouse;
    double price;
    double count;
    int completed;

    public Order() {
    }

    public Order(int id) {
        super(id);
        select();
    }

    public Order(int id, Integer IDdocument, int IDsupplier, int IDwarehouse, double price, double count, int completed) {
        super(id);
        setDocument(IDdocument);
        setSupplier(IDsupplier);
        setWarehouse(IDwarehouse);
        setPrice(price);
        setCount(count);
        setCompleted(completed);
    }

    public Document getDocument() {
        return document;
    }

    public String getDocumentID() {
        if (document == null)
            return null;
        return String.valueOf(getDocument().getId());
    }

    public void setDocument(Integer id) {
        if (id == null) setDocument((Document) null);
        else setDocument(DocumentContainer.get(id));
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(int idSupplier) {
        setSupplier(SupplierContainer.get(idSupplier));
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(int id) {
        setWarehouse(WarehouseContainer.get(id));
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    @Override
    public void select() {
        try {
            ResultSet result = SSQLController.Query("Select * from order where ID_Order=" + getId());
            if (!result.next()) throw new SQLException("No documtne found with ID = " + getId());
            set(OrderContainer.getOrderFromResultSet(result));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void set(Order order) {
        setDocument(order.getDocument());
        setSupplier(order.getSupplier());
        setWarehouse(order.getWarehouse());
        setPrice(order.getPrice());
        setCount(order.getCount());
        setCompleted(order.getCompleted());
    }

    @Override
    protected String getSQLUpdate() {
        return "UPDATE orders SET " +
               "`ID_Document` = " + getDocumentID() + ", " +
               "`ID_Supplier` = " + getSupplier().getId() + ", " +
               "`ID_Warehouse` = " + getWarehouse().getId() + ", " +
               "`Price` = " + price + ", " +
               "`Count` = " + count + ", " +
               "`Completed` = " + completed + " " +
               "WHERE `ID_Order` = " + getId() + ";";
    }

    @Override
    protected String getSQLInsert() {
        return "INSERT INTO orders VALUES " +
               "(NULL," + getDocumentID() + "," + getSupplier().getId() + "," + getWarehouse().getId() + "," + price + "," + count + "," + completed + ");";
    }

    @Override
    protected String getSQLDelete() {
        return "DELETE FROM orders " +
               "WHERE `ID_Order` = " + getId() + ";";
    }
}
