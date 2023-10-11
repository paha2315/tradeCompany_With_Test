package trade_company.logic.general;

public interface IWarehouse {
    int getId();

    void setId(int id);

    String getName();

    void setName(String name);

    String getCity();

    void setCity(String city);

    String getStreet();

    void setStreet(String street);

    int getBuildingNumber();

    void setBuildingNumber(int buildingNumber);

    char getBuildingLiteral();

    void setBuildingLiteral(char buildingLiteral);
}
