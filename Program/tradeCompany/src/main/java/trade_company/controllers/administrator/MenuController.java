package trade_company.controllers.administrator;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import trade_company.models.Model;
import trade_company.models.UserDataModel;
import trade_company.views.options.AdministratorMenuOptions;

public class MenuController {
    @FXML
    private Button button_newPerson;
    @FXML
    private Button button_userList;
    @FXML
    private Button button_profile;
    @FXML
    private Button button_logout;

    @FXML
    public void initialize() {
        setupListeners();
    }

    private void setupListeners() {
        button_newPerson.setOnAction(actionEvent -> Model.getInstance().getViewFactory().getAdministratorSelectedMenuItem().set(AdministratorMenuOptions.NEW_USER));
        button_userList.setOnAction(actionEvent -> Model.getInstance().getViewFactory().getAdministratorSelectedMenuItem().set(AdministratorMenuOptions.USER_LIST));
        button_profile.setOnAction(actionEvent -> Model.getInstance().getViewFactory().getAdministratorSelectedMenuItem().set(AdministratorMenuOptions.PROFILE));
        button_logout.setOnAction(actionEvent -> onClose());
    }

    private void onClose() {
        Model.getInstance().getViewFactory().resetAdministratorNewPersonView();
        Model.getInstance().getViewFactory().resetAdministratorUserListView();
        Model.getInstance().getViewFactory().resetAdministratorProfileView();

        Model.getInstance().getViewFactory().getAdministratorSelectedMenuItem().set(AdministratorMenuOptions.DUMMY);
        UserDataModel.getInstance().setPerson(null);
        UserDataModel.getInstance().setWarehouse(null);

        Stage stage = (Stage) button_logout.getScene().getWindow();
        stage.close();

        Model.getInstance().getWindowFactory().showLoginWindow();
    }
}
