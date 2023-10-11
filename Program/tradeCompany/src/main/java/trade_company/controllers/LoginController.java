package trade_company.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import trade_company.logic.login.LoginLogic;
import trade_company.models.Model;
import trade_company.models.UserDataModel;
import trade_company.views.options.LoginRolesOptions;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class LoginController extends LoginLogic {
    @FXML
    private ChoiceBox<String> choicebox_roleSelector;
    @FXML
    private TextField textField_username;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button button_login;
    @FXML
    private Label label_error;
    @FXML
    private Label label_optionalSelect;
    @FXML
    private ChoiceBox<String> choicebox_optionalSelect;

    @FXML
    public void initialize() {
        choicebox_roleSelector.getItems().addAll(mRolesList);
        choicebox_optionalSelect.getItems().addAll(warehouseNames);
        label_error.setVisible(false);
        setupListeners();
        choicebox_roleSelector.getSelectionModel().select(0);
        choicebox_optionalSelect.getSelectionModel().select(0);
    }

    private void setupListeners() {
        button_login.setOnAction(actionEvent -> {
            String role = choicebox_roleSelector.getSelectionModel().getSelectedItem();
            if (role == null) role = "";
            String login = textField_username.getText();
            String password = passwordField.getText();

            doLogin(role, login, password);
        });

        choicebox_roleSelector.getSelectionModel().selectedIndexProperty().addListener((observable, oldVal, newVal) -> {
            if ((Integer) newVal < 0) {
                enableOptionalCombobox(false);
                return;
            }
            var role = choicebox_roleSelector.getItems().get((Integer) newVal);
            if (role.equals(LoginRolesOptions.STOREKEEPER.label)) {
                enableOptionalCombobox(true);
                label_optionalSelect.setText("Выберите склад, на который заступаете работать:");
            } else {
                enableOptionalCombobox(false);
            }
        });

        choicebox_optionalSelect.getSelectionModel().selectedIndexProperty().addListener((observable, oldVal, newVal) -> {
            if ((Integer) newVal >= 0) {
                var wh = warehouses.get((Integer) newVal);
                UserDataModel.getInstance().setWarehouse(wh);
            }
        });

        textField_username.textProperty().addListener((observable, oldVal, newVal) -> {
            label_error.setVisible(false);
            passwordField.setText("");
        });
        passwordField.textProperty().addListener((observable, oldVal, newVal) -> label_error.setVisible(false));
    }

    public void doLogin(String role, String login, String password) {
        label_error.setVisible(false);
        try {
            if (personIsAuthorized(role, login, password)) {
                var found = findUserRoleById(UserDataModel.getInstance().getPerson().getId());
                if (found.isPresent()) {
                    var role_type = found.get();
                    switch (role_type) {
                        case STOREKEEPER -> Model.getInstance().getWindowFactory().showStorekeeperWindow();
                        case SUPPLIER -> Model.getInstance().getWindowFactory().showSupplierWindow();
                        case ADMIN -> Model.getInstance().getWindowFactory().showAdministratorWindow();
                    }
                    ((Stage) label_error.getScene().getWindow()).close();
                } else throw new SecurityException();
            } else throw new SecurityException();
        } catch (SecurityException e) {
            label_error.setVisible(true);
            label_error.setText("Ошибка: учётная запись с такими данными не найдена.");
        } catch (SQLException e) {
            label_error.setVisible(true);
            label_error.setText("Ошибка: что-то не так с базой данных.");
        } catch (NoSuchAlgorithmException e) {
            label_error.setVisible(true);
            label_error.setText("Критическая ошибка: не найден алгоритм шифрования SHA-256.");
        }
    }

    protected void enableOptionalCombobox(boolean status) {
        label_optionalSelect.setDisable(!status);
        label_optionalSelect.setVisible(status);
        choicebox_optionalSelect.setDisable(!status);
        choicebox_optionalSelect.setVisible(status);
        if (!status)
            UserDataModel.getInstance().setWarehouse(null);
        else if (choicebox_optionalSelect.getSelectionModel().getSelectedIndex() != -1)
            UserDataModel.getInstance().setWarehouse(warehouses.get(choicebox_optionalSelect.getSelectionModel().getSelectedIndex()));
    }
}
