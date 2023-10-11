package trade_company.controllers.administrator;

import de.jensd.fx.glyphs.GlyphsStack;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import trade_company.logic.administrator.NewPersonLogic;
import trade_company.models.Model;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.NoSuchElementException;

public class NewPersonController extends NewPersonLogic {
    @FXML
    private TextField textfield_userSecondName;
    @FXML
    private TextField textfield_userFirstName;
    @FXML
    private TextField textfield_userThirdField;
    @FXML
    private ComboBox<String> combobox_userPost;
    @FXML
    private TextField textfiled_userLogin;
    @FXML
    private PasswordField passwordfield_userPassword;
    @FXML
    private TextField textfield_userPassword;
    @FXML
    private PasswordField passwordfield_userPasswordRepeat;
    @FXML
    private GlyphsStack glyphstack_fontIcons;
    @FXML
    private FontAwesomeIconView fonticon_eye;
    @FXML
    private FontAwesomeIconView fonticon_eyeslash;
    @FXML
    private Button button_createUser;

    @FXML
    public void initialize() {
        initGui();
        initUserPostCombobox();
        setupListeners();
    }

    protected void initGui() {
        fonticon_eye.setVisible(false);
        textfield_userPassword.toBack();
    }

    protected void setupListeners() {
        button_createUser.setOnAction(actionEvent -> onCreateUserPressed());

        /* Синхронизация поля ввода пароля и нередактируемого текстового */
        passwordfield_userPassword.setOnKeyPressed(keyEvent -> {
            if (!keyEvent.isShortcutDown())
                textfield_userPassword.setText(textfield_userPassword.getText() + keyEvent.getText());
        });

        /* Показываем пароль */
        glyphstack_fontIcons.setOnMousePressed(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) showPasswordAndEye(true);
        });
        glyphstack_fontIcons.setOnMouseReleased(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) showPasswordAndEye(false);
        });
    }

    private void showPasswordAndEye(boolean flag) {
        fonticon_eye.setVisible(flag);
        fonticon_eyeslash.setVisible(!flag);
        if (flag) {
            textfield_userPassword.toFront();
            passwordfield_userPassword.toBack();
        } else {
            textfield_userPassword.toBack();
            passwordfield_userPassword.toFront();
        }
    }

    protected void initUserPostCombobox() {
        initCombobox();
        combobox_userPost.setItems(FXCollections.observableArrayList(comboboxPostList));
    }

    protected void onCreateUserPressed() {
        var windowFactory = Model.getInstance().getWindowFactory();
        if (!passwordfield_userPassword.getText().equals(passwordfield_userPasswordRepeat.getText())) {
            windowFactory.showAlert(Alert.AlertType.WARNING, "Добавление пользователя", "", "Поля ввода и повторного ввода нового пароля не совпадают.");
            return;
        }
        String commonTitle = "Добавление пользователя";
        try {
            var firstName = textfield_userFirstName.getText().trim();
            var secondName = textfield_userSecondName.getText().trim();
            var thirdName = textfield_userThirdField.getText().trim();
            var postName = combobox_userPost.getSelectionModel().getSelectedItem();
            var login = textfiled_userLogin.getText().trim();
            var password = passwordfield_userPassword.getText();
            if (checkIfLoginUnique(login)) {
                if (!firstName.isEmpty() && !secondName.isEmpty() && !login.isEmpty() && !password.isEmpty()) {
                    try {
                        createNewPerson(firstName, secondName, thirdName, postName, login, password);
                        clearFields();
                    } catch (SQLException e) {
                        throw new NoSuchElementException();
                    }
                    windowFactory.showAlert(Alert.AlertType.INFORMATION, commonTitle, "", "Пользователь '" + (secondName + " " + firstName + " " + thirdName).trim() + "'\nс логином '" + login + "'\nуспешно добавлен в базу данных.\nТеперь он может войти под своей учётной записью.");
                } else
                    windowFactory.showAlert(Alert.AlertType.WARNING, commonTitle, "", "Поля 'Фамилия', 'Имя', 'Должность', 'Логин', 'Пароль' обязательны к заполнению.");
            } else
                windowFactory.showAlert(Alert.AlertType.WARNING, commonTitle, "", "Логин пользователя должен быть уникальным.");
        } catch (NoSuchElementException e) {
            windowFactory.showAlert(Alert.AlertType.WARNING, commonTitle, "", "Не выбрана должность пользователя.");
        } catch (SQLException e) {
            windowFactory.showAlert(Alert.AlertType.ERROR, "Ошибка MySQL", "", "Ошибка: что-то не так с базой данных.");
        } catch (NoSuchAlgorithmException e) {
            // нет смысла оповещать пользователя, т.к. он досюда даже не дойдёт при входе в систему, если бы данная ошибка произошла
            e.printStackTrace();
        }
    }

    protected void clearFields() {
        textfield_userFirstName.clear();
        textfield_userSecondName.clear();
        textfield_userThirdField.clear();
        combobox_userPost.getSelectionModel().select(-1);
        textfiled_userLogin.clear();
        passwordfield_userPassword.clear();
        textfield_userPassword.clear();
        passwordfield_userPasswordRepeat.clear();
    }
}
