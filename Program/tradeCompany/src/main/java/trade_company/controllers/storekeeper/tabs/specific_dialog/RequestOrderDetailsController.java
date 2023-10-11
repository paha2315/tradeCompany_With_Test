package trade_company.controllers.storekeeper.tabs.specific_dialog;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import trade_company.logic.storekeeper.tabs.specific_dialog.RequestOrderDetailsLogic;

public class RequestOrderDetailsController extends RequestOrderDetailsLogic {
    @FXML
    private Text text_productName;
    @FXML
    private TextField textfield_count;
    @FXML
    private TextField textfield_price;

    @FXML
    private Button button_accept;
    @FXML
    private Button button_cancel;

    @FXML
    public void initialize() {
        text_productName.setText(getProductName());
        button_accept.setOnAction(actionEvent -> {
            onAcceptPressed(textfield_count.getText(), textfield_price.getText());
            exit();
        });
        button_cancel.setOnAction(actionEvent -> exit());
    }

    protected void exit() {
        Stage stage = (Stage) button_cancel.getScene().getWindow();
        stage.close();
    }
}
