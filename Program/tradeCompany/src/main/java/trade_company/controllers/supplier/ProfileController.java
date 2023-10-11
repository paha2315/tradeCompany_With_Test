package trade_company.controllers.supplier;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import trade_company.logic.sql_object.Containers.ProductContainer;
import trade_company.logic.supplier.SupplierProfileLogic;
import trade_company.models.Model;
import trade_company.models.UserDataModel;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class ProfileController extends SupplierProfileLogic {
    @FXML
    private Button button_changePassword;
    @FXML
    private PasswordField passwordfield_current;
    @FXML
    private PasswordField passwordfield_new;
    @FXML
    private PasswordField passwordfield_repeat;
    @FXML
    private Text text_userName;
    @FXML
    private Text text_userPost;

    @FXML
    private TextField textfield_companyName;
    @FXML
    private TextField textfield_companyCity;
    @FXML
    private TextField textfield_companyFullAddress;
    @FXML
    private TextField textfield_companyOKPO;
    @FXML
    private TextField textfield_companyPaymentAccount;
    @FXML
    private TextField textfield_companyPhoneNumber;
    @FXML
    private TextField textfield_companyZipCode;
    @FXML
    private TextField textfield_companyBankInfo;

    @FXML
    private ComboBox<String> combobox_allProducts;

    @FXML
    private VBox vbox_registration;
    @FXML
    private TextField textfield_productName;
    @FXML
    private TextArea textarea_productDescription;
    @FXML
    private TextField textfield_productNomenclature;
    @FXML
    private TextField textfield_productArticle;
    @FXML
    private TextField textfield_measureFilter;
    @FXML
    private ComboBox<String> combobox_productMeasure;
    @FXML
    private TextField textfield_productMass;
    @FXML
    private TextField textfield_percentNDS;

    @FXML
    private Button button_registerCompanyAndProduct;

    @FXML
    public void initialize() {
        initText();
        initRegistrationForm();
        setupListeners();
    }

    protected void initRegistrationForm() {
        var supplier = UserDataModel.getInstance().getPerson().getSupplier();
        if (supplier != null) {
            vbox_registration.setDisable(true);
            vbox_registration.setVisible(false);
            button_registerCompanyAndProduct.setDisable(true);
            button_registerCompanyAndProduct.setVisible(false);
        } else {
            combobox_allProducts.getItems().setAll(productsList);
            combobox_allProducts.getSelectionModel().select(0);
            combobox_productMeasure.getItems().setAll(OKEIsNames);
            button_registerCompanyAndProduct.setDisable(false);
            button_registerCompanyAndProduct.setVisible(true);
        }
    }

    private void setupListeners() {
        button_changePassword.setOnAction(actionEvent -> onChangePassword(passwordfield_current.getText(), passwordfield_new.getText(), passwordfield_repeat.getText()));

        textarea_productDescription.textProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal.length() > 512) {
                textarea_productDescription.setText(newVal.substring(0, 512));
                textarea_productDescription.setScrollTop(Double.MAX_VALUE);
            }
        });
        textfield_measureFilter.textProperty().addListener((observable, oldVal, newVal) -> onFilterModified(newVal));

        combobox_allProducts.getSelectionModel().selectedIndexProperty().addListener((observable, oldVal, newVal) -> {
            disableProductEditing(newVal.intValue() != 0);
            if (newVal.intValue() == 0)
                initializeCreationOfNewProduct();
            else
                selectExistingProduct(newVal.intValue() - 1);
        });

        button_registerCompanyAndProduct.setOnAction(actionEvent -> {
            if (onRegisterCompanyAndProduct()) {
                vbox_registration.setDisable(true);
                vbox_registration.setVisible(false);
            }
        });
    }

    private void selectExistingProduct(int selected_idx) {
        var product = ProductContainer.getList().get(selected_idx);
        UserDataModel.getInstance().setProduct(product);

        textfield_productName.setText(product.getName());
        textarea_productDescription.setText(product.getDescription());
        textfield_productNomenclature.setText(product.getNomenclature());
        textfield_productArticle.setText(product.getArticle());
        textfield_measureFilter.setText("");
        combobox_productMeasure.getSelectionModel().select(product.getOkei().getName());
        textfield_productMass.setText(Double.toString(product.getMass()));
        textfield_percentNDS.setText(Integer.toString(product.getPercentNDS()));
    }

    private void onFilterModified(String filterText) {
        if (filterText.isEmpty()) combobox_productMeasure.getItems().setAll(OKEIsNames);
        try {
            combobox_productMeasure.getItems().clear();
            var filtered = searchOKEIs(filterText);
            combobox_productMeasure.getItems().setAll(filtered);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    protected void initText() {
        text_userName.setText(getPersonName());
        text_userPost.setText(UserDataModel.getInstance().getPerson().getPost().getName());
    }

    protected void initializeCreationOfNewProduct() {
        UserDataModel.getInstance().setProduct(null);
        textfield_productName.setText("");
        textarea_productDescription.setText("");
        textfield_productNomenclature.setText("");
        textfield_productArticle.setText("");
        textfield_measureFilter.setText("");
        combobox_productMeasure.getSelectionModel().select(-1);
        combobox_productMeasure.setValue("Выберите единицу измерения");
        textfield_productMass.setText("");
        textfield_percentNDS.setText("");
    }

    protected void disableProductEditing(boolean flag) {
        textfield_productName.setEditable(!flag);
        textarea_productDescription.setEditable(!flag);
        textfield_productNomenclature.setEditable(!flag);
        textfield_productArticle.setEditable(!flag);
        textfield_measureFilter.setDisable(flag);
        combobox_productMeasure.setDisable(flag);
        textfield_productMass.setEditable(!flag);
        textfield_percentNDS.setEditable(!flag);
    }

    private boolean onRegisterCompanyAndProduct() {
        int prodId;
        // подгружаем необязательные поля
        String prodDescription;
        String prodStorageConditions;
        if (combobox_allProducts.getSelectionModel().getSelectedIndex() != 0) { // в этом случае продукт лежит в модели
            var product = UserDataModel.getInstance().getProduct();
            prodId = product.getId();
            prodDescription = product.getDescription();
            prodStorageConditions = product.getStorageConditions();
        } else {
            prodId = 0;
            prodDescription = textarea_productDescription.getText().trim();
            prodStorageConditions = "";
        }
        var creationStatus = doCreateSupplierWithProduct(
                prodId,
                textfield_productName.getText().trim(),
                prodDescription.trim(),
                textfield_productNomenclature.getText().trim(),
                textfield_productArticle.getText().trim(),
                combobox_productMeasure.getSelectionModel().getSelectedItem(),
                textfield_productMass.getText(),
                textfield_percentNDS.getText(),
                prodStorageConditions.trim(),
                textfield_companyName.getText().trim(),
                textfield_companyCity.getText().trim(),
                textfield_companyFullAddress.getText().trim(),
                textfield_companyOKPO.getText(),
                textfield_companyPaymentAccount.getText().trim(),
                textfield_companyPhoneNumber.getText().trim(),
                textfield_companyZipCode.getText().trim(),
                textfield_companyBankInfo.getText().trim());

        String commonAlertTitle = "Регистрация компании и товара";
        var windowFactory = Model.getInstance().getWindowFactory();
        switch (creationStatus) {
            case COMPANY_INCOMPLETE ->
                    windowFactory.showAlert(Alert.AlertType.WARNING, commonAlertTitle, "", "Сначала заполните данные о компании.");
            case PRODUCT_INCOMPLETE ->
                    windowFactory.showAlert(Alert.AlertType.WARNING, commonAlertTitle, "", "Не заполнены обязательные поля о товаре.");
            case COMPANY_STR_TO_NUMBER_CONVERT_EXCEPTION ->
                    windowFactory.showAlert(Alert.AlertType.WARNING, commonAlertTitle, "", "Некорректно заполнено поле ОКПО компании.");
            case PRODUCT_STR_TO_NUMBER_CONVERT_EXCEPTION ->
                    windowFactory.showAlert(Alert.AlertType.WARNING, commonAlertTitle, "", "Некорректно заполнено поле массы или НДС продукта.");
            case SQL_EXCEPTION ->
                    windowFactory.showAlert(Alert.AlertType.ERROR, commonAlertTitle, "", "Произошла ошибка MySQL.");
            case OK -> {
                windowFactory.showAlert(Alert.AlertType.INFORMATION, commonAlertTitle, "", "Данные успешно внесены в базу данных.");
                return true;
            }
        }

        return false;
    }
}
