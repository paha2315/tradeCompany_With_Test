module trade_company {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires org.apache.poi.ooxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires de.jensd.fx.glyphs.commons;


    opens trade_company to javafx.fxml;
    exports trade_company;
    exports SQL;
    opens SQL to javafx.fxml;
    exports trade_company.controllers;
    opens trade_company.controllers to javafx.fxml;
    exports trade_company.controllers.storekeeper;
    opens trade_company.controllers.storekeeper to javafx.fxml;
    exports trade_company.controllers.administrator;
    opens trade_company.controllers.administrator to javafx.fxml;
    exports trade_company.controllers.supplier;
    opens trade_company.controllers.supplier to javafx.fxml;
    exports trade_company.controllers.supplier.tabs;
    opens trade_company.controllers.supplier.tabs to javafx.fxml;
    exports trade_company.controllers.storekeeper.tabs;
    opens trade_company.controllers.storekeeper.tabs to javafx.fxml;

    opens trade_company.controllers.storekeeper.tabs.specific_dialog to javafx.fxml;
}