package trade_company.logic.sql_object;

import SQL.DBObject;
import SQL.SSQLController;
import trade_company.logic.general.IWarehouse;
import trade_company.logic.sql_object.Containers.WarehouseContainer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Warehouse extends DBObject implements IWarehouse {
    String name;
    String city;
    String street;
    int buildingNumber;
    char buildingLiteral;

    public Warehouse() {
        super();
    }

    public Warehouse(int id) {
        super(id);
        select();
    }

    public Warehouse(int id, String name, String city, String street, int buildingNumber, char buildingLiteral) {
        super(id);
        setName(name);
        setCity(city);
        setStreet(street);
        setBuildingNumber(buildingNumber);
        setBuildingLiteral(buildingLiteral);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(int buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public char getBuildingLiteral() {
        return buildingLiteral;
    }

    public void setBuildingLiteral(char buildingLiteral) {
        this.buildingLiteral = buildingLiteral;
    }

    public void set(Warehouse warehouse) {
        setName(warehouse.getName());
        setCity(warehouse.getCity());
        setStreet(warehouse.getStreet());
        setBuildingNumber(warehouse.getBuildingNumber());
        setBuildingLiteral(warehouse.getBuildingLiteral());
    }

    @Override
    public void select() {
        try {
            ResultSet result = SSQLController.Query("Select * from warehouse where ID_Warehouse=" + getId());
            if (!result.next()) throw new SQLException("No Warehouse found with ID = " + getId());
            set(WarehouseContainer.getWarehouseFromResultSet(result));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getSQLUpdate() {
        return "UPDATE warehouse SET\n" + "Name = '" + getName() + "',\n" + "City = '" + getCity() + "',\n" + "Street = '" + getStreet() + "',\n" + "BuildingNumber = '" + getBuildingNumber() + "',\n" + "BuildingLiteral = '" + getBuildingLiteral() + "'\n" + "WHERE ID_Warehouse = " + getId() + ";";
    }

    @Override
    public String getSQLInsert() {
        return "INSERT INTO warehouse VALUES\n" + "(NULL,'" + getName() + "','" + getCity() + "','" + getStreet() + "'," + getBuildingNumber() + ",'" + getBuildingLiteral() + "');";
    }

    @Override
    public String getSQLDelete() {
        return "DELETE FROM warehouse WHERE ID_Warehouse = " + getId();
    }

}
