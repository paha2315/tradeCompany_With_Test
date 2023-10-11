package trade_company.logic.sql_object;

import SQL.DBObject;
import SQL.SSQLController;
import trade_company.logic.sql_object.Containers.ProductContainer;
import trade_company.logic.sql_object.Containers.SupplierContainer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Supplier extends DBObject {
    Product product;
    String name;
    String city;
    String addressFull;
    int okpo;
    String paymentAccount;
    String phone;
    String zipCode;
    String bankInfo;

    public Supplier() {
        super();
    }

    public Supplier(int id) {
        super(id);
        select();
    }

    public Supplier(int id, int idProduct, String name, String city, String addressFull, int okpo, String paymentAccount, String phone, String zipCode, String bankInfo) {
        super(id);
        setProduct(idProduct);
        setName(name);
        setCity(city);
        setAddressFull(addressFull);
        setOkpo(okpo);
        setPaymentAccount(paymentAccount);
        setPhone(phone);
        setZipCode(zipCode);
        setBankInfo(bankInfo);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getOkpo() {
        return okpo;
    }

    public void setOkpo(int okpo) {
        this.okpo = okpo;
    }

    public String getPaymentAccount() {
        return paymentAccount;
    }

    public void setPaymentAccount(String paymentAccount) {
        this.paymentAccount = paymentAccount;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getBankInfo() {
        return bankInfo;
    }

    public void setBankInfo(String bankInfo) {
        this.bankInfo = bankInfo;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setProduct(int idProduct) {
        this.product = ProductContainer.get(idProduct);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressFull() {
        return addressFull;
    }

    public void setAddressFull(String addressFull) {
        this.addressFull = addressFull;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void set(Supplier supplier) {
        setProduct(supplier.getProduct());
        setName(supplier.getName());
        setCity(supplier.getCity());
        setAddressFull(supplier.getAddressFull());
        setOkpo(supplier.getOkpo());
        setPaymentAccount(supplier.getPaymentAccount());
        setPhone(supplier.getPhone());
        setZipCode(supplier.getZipCode());
        setBankInfo(supplier.getBankInfo());
    }

    @Override
    public void select() {
        try {
            ResultSet result = SSQLController.Query("SELECT * FROM supplier WHERE ID_Supplier=" + getId());
            if (!result.next()) throw new SQLException("No Supplier found with ID = " + getId());
            set(SupplierContainer.getSupplierFromResultSet(result));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getSQLUpdate() {
        return "UPDATE supplier SET\nID_Product=" + getProduct().getId() + ",\nName='" + name + "',\nCity='" + city + "',\nAddressFull='" + addressFull + "',\nOKPO='" + okpo + "',\nPaymentAccount='" + paymentAccount + "',\nPhone='" + phone + "',\nZipCode='" + zipCode + "',\nBankInfo='" + bankInfo + "\nWHERE ID_Supplier=" + getId();
    }

    @Override
    public String getSQLInsert() {
        return "INSERT INTO supplier VALUES\n(NULL," + getProduct().getId() + ",'" + name + "','" + city + "','" + addressFull + "','" + okpo + "','" + paymentAccount + "','" + phone + "','" + zipCode + "','" + bankInfo + "')";
    }

    @Override
    public String getSQLDelete() {
        return "DELETE FROM supplier WHERE ID_Supplier = " + getId();
    }

}
