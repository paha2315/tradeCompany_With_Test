package trade_company.logic.sql_object;

import SQL.DBObject;
import SQL.SSQLController;
import trade_company.logic.sql_object.Containers.OkeiContainer;
import trade_company.logic.sql_object.Containers.ProductContainer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Product extends DBObject {
    String name;
    String description;
    String nomenclature;
    String article;
    OKEI okei;
    double mass;
    String storageConditions;
    int percentNDS;
    boolean deleted;

    public Product() {
        super();
    }

    public Product(int id) {
        super(id);
        select();
    }

    public Product(int id, String name, String description, String nomenclature, String article, int okei_id, double mass, int percentNDS, String storageConditions, boolean deleted) {
        super(id);
        setName(name);
        setDescription(description);
        setNomenclature(nomenclature);
        setArticle(article);
        setOkei(okei_id);
        setMass(mass);
        setPercentNDS(percentNDS);
        setStorageConditions(storageConditions);
        setDeleted(deleted);
    }

    public int getPercentNDS() {
        return percentNDS;
    }

    public void setPercentNDS(int percentNDS) {
        this.percentNDS = percentNDS;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNomenclature() {
        return nomenclature;
    }

    public void setNomenclature(String nomenclature) {
        this.nomenclature = nomenclature;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public OKEI getOkei() {
        return okei;
    }

    public void setOkei(int okei_id) {
        setOkei(OkeiContainer.get(okei_id));
    }

    public void setOkei(OKEI okei) {
        this.okei = okei;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public String getStorageConditions() {
        return storageConditions;
    }

    public void setStorageConditions(String storageConditions) {
        this.storageConditions = storageConditions;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void set(Product product) {
        setName(product.getName());
        setDescription(product.getDescription());
        setNomenclature(product.getNomenclature());
        setArticle(product.getArticle());
        setOkei(product.getOkei());
        setMass(product.getMass());
        setStorageConditions(product.getStorageConditions());
        setDeleted(product.getDeleted());
    }

    @Override
    public void select() {
        try {
            ResultSet result = SSQLController.Query("Select * from Product where ID_Product=" + getId());
            if (!result.next()) throw new SQLException("No Product found with ID = " + getId());
            set(ProductContainer.getProductFromResultSet(result));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getSQLUpdate() {
        return "UPDATE product SET "
               + "Name='" + name + "', "
               + "Description='" + description + "', "
               + "Nomenclature='" + nomenclature + "', "
               + "Article='" + article + "', "
               + "ID_OKEI=" + okei.getId() + ", "
               + "Mass=" + mass + ", "
               + "PercentNDS=" + percentNDS + ", "
               + "StorageConditions='" + storageConditions + "', "
               + "Deleted=" + deleted + " "
               + "WHERE ID_Product=" + getId() + ";";
    }

    @Override
    public String getSQLInsert() {
        return "INSERT INTO product VALUES ("
               + "Null, "
               + "'" + name + "', "
               + "'" + description + "', "
               + "'" + nomenclature + "', "
               + "'" + article + "', "
               + okei.getId() + ", "
               + mass + ", "
               + percentNDS + ", "
               + "'" + storageConditions + "', "
               + deleted + ");";
    }

    @Override
    public String getSQLDelete() {
        return "DELETE FROM product WHERE ID_OKEI = " + getId();
    }

}
