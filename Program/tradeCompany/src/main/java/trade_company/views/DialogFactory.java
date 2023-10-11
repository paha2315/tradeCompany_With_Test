package trade_company.views;

import javafx.fxml.FXMLLoader;
import trade_company.controllers.storekeeper.tabs.specific_dialog.AddAvailDetailsController;
import trade_company.controllers.storekeeper.tabs.specific_dialog.RequestOrderDetailsController;

public class DialogFactory {

    public void showAddProductDetailsDialog(String productName) {
        FXMLLoader loader = new FXMLLoader(ViewFactory.class.getResource("/FXMLs/Storekeeper/Tabs/SpecificDialog/AddAvailDetails.fxml"));
        var controller = new AddAvailDetailsController();
        loader.setController(controller);
        controller.init(productName);
        WindowFactory.createStage(loader, "Детали поступления").showAndWait();
    }

    public void showRequestOrderDetailsDialog(String productName) {
        FXMLLoader loader = new FXMLLoader(ViewFactory.class.getResource("/FXMLs/Storekeeper/Tabs/SpecificDialog/RequestOrderDetails.fxml"));
        var controller = new RequestOrderDetailsController();
        loader.setController(controller);
        controller.init(productName);
        WindowFactory.createStage(loader, "Детали заказа").showAndWait();
    }

}
