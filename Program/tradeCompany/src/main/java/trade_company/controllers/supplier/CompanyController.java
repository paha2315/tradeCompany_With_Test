package trade_company.controllers.supplier;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import trade_company.logic.sql_object.Containers.PersonContainer;
import trade_company.logic.sql_object.Person;
import trade_company.models.UserDataModel;

public class CompanyController {
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
    private TextField textfield_productName;
    @FXML
    private TextArea textarea_productDescription;
    @FXML
    private TextField textfield_productNomenclature;
    @FXML
    private TextField textfield_productArticle;
    @FXML
    private TextField textfield_productMeasure;
    @FXML
    private TextField textfield_productMass;
    @FXML
    private TextField textfield_percentNDS;

    @FXML
    private Button button_refreshData;

    @FXML
    public void initialize() {
        initData();
        setupListeners();
    }

    protected void initData() {
        refreshFields(UserDataModel.getInstance().getPerson());
    }

    protected void setupListeners() {
        button_refreshData.setOnAction(actionEvent -> {
            var person = UserDataModel.getInstance().getPerson();
            PersonContainer.setAll();
            person = PersonContainer.get(person.getId());
            refreshFields(person);
        });
    }

    private void refreshFields(Person person) {
        if (person.getSupplier() != null) {
            var supplier = person.getSupplier();
            textfield_companyName.setText(supplier.getName());
            textfield_companyCity.setText(supplier.getCity());
            textfield_companyFullAddress.setText(supplier.getAddressFull());
            textfield_companyOKPO.setText(Integer.toString(supplier.getOkpo()));
            textfield_companyPaymentAccount.setText(supplier.getPaymentAccount());
            textfield_companyPhoneNumber.setText(supplier.getPhone());
            textfield_companyZipCode.setText(supplier.getZipCode());
            textfield_companyBankInfo.setText(supplier.getBankInfo());

            var product = supplier.getProduct();
            textfield_productName.setText(product.getName());
            textarea_productDescription.setText(product.getDescription());
            textfield_productNomenclature.setText(product.getNomenclature());
            textfield_productArticle.setText(product.getArticle());
            textfield_productMeasure.setText(product.getOkei().getName());
            textfield_productMass.setText(Double.toString(product.getMass()));
            textfield_percentNDS.setText(Integer.toString(product.getPercentNDS()));
        } else {
            textfield_companyName.setText("Не указано");
            textfield_companyCity.setText("Не указано");
            textfield_companyFullAddress.setText("Не указано");
            textfield_companyOKPO.setText("Не указано");
            textfield_companyPaymentAccount.setText("Не указано");
            textfield_companyPhoneNumber.setText("Не указано");
            textfield_companyZipCode.setText("Не указано");
            textfield_companyBankInfo.setText("Не указано");

            textfield_productName.setText("Не указано");
            textarea_productDescription.setText("Не указано");
            textfield_productNomenclature.setText("Не указано");
            textfield_productArticle.setText("Не указано");
            textfield_productMeasure.setText("Не указано");
            textfield_productMass.setText("Не указано");
            textfield_percentNDS.setText("Не указано");
        }
    }
}
