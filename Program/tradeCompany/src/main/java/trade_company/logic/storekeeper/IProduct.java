package trade_company.logic.storekeeper;

import trade_company.logic.sql_object.OKEI;

public interface IProduct {
    int getId();

    void setId(int id);

    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    String getNomenclature();

    void setNomenclature(String nomenclature);

    String getArticle();

    void setArticle(String article);

    OKEI getOkei();

    void setOkei(int okei_id);

    double getMass();

    void setMass(double mass);

    String getStorageConditions();

    void setStorageConditions(String storageConditions);
}
