package trade_company.controllers.storekeeper;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import trade_company.logic.ProfileLogic;
import trade_company.models.Model;
import trade_company.models.UserDataModel;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class ProfileController extends ProfileLogic {
    @FXML
    private Button button_changePassword;
    @FXML
    private PasswordField passwordfield_current;
    @FXML
    private PasswordField passwordfield_new;
    @FXML
    private PasswordField passwordfield_repeat;
    @FXML
    private Text text_userPost;
    @FXML
    private Text text_userName;
    @FXML
    private Text text_warehouseName;

    @FXML
    public void initialize() {
        setupText();
        setupListeners();
    }

    private void setupListeners() {
        button_changePassword.setOnAction(actionEvent -> onChangePassword(passwordfield_current.getText(), passwordfield_new.getText(), passwordfield_repeat.getText()));
    }

    private void onChangePassword(String current, String new_, String new_repeated) {
        var windowFactory = Model.getInstance().getWindowFactory();
        try {
            var state = doChangePassword(current, new_, new_repeated);
            switch (state) {
                case OK -> {
                    windowFactory.showAlert(Alert.AlertType.INFORMATION, "Смена пароля", "", "Пароль пользователя '" + UserDataModel.getInstance().getPerson().getLogin() + "' успешно изменён.");
                    passwordfield_current.clear();
                    passwordfield_new.clear();
                    passwordfield_repeat.clear();
                }
                case NOT_MATCHES_MAIN ->
                        windowFactory.showAlert(Alert.AlertType.WARNING, "Смена пароля", "", "Введённый вами пароль не совпадает с текущим.");
                case NOT_MATCHES_NEW ->
                        windowFactory.showAlert(Alert.AlertType.WARNING, "Смена пароля", "", "Поля ввода и повторного ввода нового пароля не совпадают.");
            }
        } catch (NoSuchAlgorithmException e) {
            // нет смысла оповещать пользователя, т.к. он досюда даже не дойдёт при входе в систему, если бы данная ошибка произошла
            e.printStackTrace();
        } catch (SQLException e) {
            windowFactory.showAlert(Alert.AlertType.ERROR, "Ошибка MySQL", "", "Ошибка: что-то не так с базой данных.");
        }
    }

    protected void setupText() {
        text_userName.setText(getPersonName());
        text_userPost.setText(UserDataModel.getInstance().getPerson().getPost().getName());
        text_warehouseName.setText(UserDataModel.getInstance().getWarehouse().getName());
    }
}
