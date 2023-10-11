package trade_company.controllers.storekeeper.tabs.specific_dialog;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import trade_company.logic.storekeeper.tabs.specific_dialog.AddAvailDetailsLogic;

public class AddAvailDetailsController extends AddAvailDetailsLogic {
    @FXML
    private Button button_accept;
    @FXML
    private Button button_cancel;
    @FXML
    private Text text_productName;
    @FXML
    private TextField textfield_packageType;
    @FXML
    private TextField textfield_packageWidth;
    @FXML
    private TextField textfield_packageHeight;
    @FXML
    private TextField textfield_packageDepth;
    @FXML
    private TextField textfield_sectionNumber;
    @FXML
    private TextField textfield_stackNumber;

    @FXML
    public void initialize() {
        text_productName.setText(getProductName());
        button_accept.setOnAction(actionEvent -> {
            onAcceptPressed(
                    textfield_packageType.getText().trim(),
                    textfield_packageWidth.getText(),
                    textfield_packageHeight.getText(),
                    textfield_packageDepth.getText(),
                    textfield_sectionNumber.getText(),
                    textfield_stackNumber.getText());
            exit();
        });
        button_cancel.setOnAction(actionEvent -> {
            exit();
        });
    }

    protected void exit() {
        Stage stage = (Stage) button_cancel.getScene().getWindow();
        stage.close();
    }
}
