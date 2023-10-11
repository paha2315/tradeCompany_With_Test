package trade_company.logic.supplier;

import SQL.SSQLController;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import trade_company.logic.ProfileLogic;
import trade_company.logic.sql_object.Containers.OkeiContainer;
import trade_company.logic.sql_object.Containers.ProductContainer;
import trade_company.logic.sql_object.OKEI;
import trade_company.logic.sql_object.Product;
import trade_company.logic.sql_object.Supplier;
import trade_company.models.Model;
import trade_company.models.UserDataModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class SupplierProfileLogic extends ProfileLogic {
    protected ArrayList<String> productsList;
    protected ArrayList<OKEI> OKEIs;
    protected ArrayList<String> OKEIsNames;
    protected boolean was_exception;
    protected boolean was_sqlException;

    public SupplierProfileLogic() {
        productsList = new ArrayList<>();
        productsList.add("Новый");
        fillProductsList();
        was_exception = false;
        was_sqlException = false;
        initOKEIs();
    }

    private void initOKEIs() {
        OKEIsNames = new ArrayList<>();
        OkeiContainer.setAll();
        OKEIs = OkeiContainer.getList();
        for (var okei : OKEIs)
            OKEIsNames.add(okei.getName());
    }

    public ArrayList<String> searchOKEIs(String template) throws SQLException {
        was_exception = false;
        var filtered = new ArrayList<String>();
        ResultSet result = SSQLController.Query("SELECT * FROM okei_code WHERE name LIKE '%" + template + "%'");
        while (result.next())
            filtered.add(result.getString("Name"));
        return filtered;
    }

    private void fillProductsList() {
        ProductContainer.setAll();
        for (var item : ProductContainer.getList())
            productsList.add(item.getName());
    }

    protected SupplierCreationStatus doCreateSupplierWithProduct(
            int prodId, String prodName, String prodDescr, String prodNomenclature, String prodArticle,
            String prodOkeiName, String prodMassStr, String prodNdsStr, String prodStorageConditions,
            String suppName, String suppCity, String suppAddressFull, String suppOkpoStr, String suppPaymentAccount,
            String suppPhoneNumber, String suppZipCode, String suppBankInfo) {

        // Обработка данных о компании
        if (suppName.isBlank() || suppCity.isBlank() || suppAddressFull.isBlank() || suppOkpoStr.isBlank()
            || suppPaymentAccount.isBlank() || suppPhoneNumber.isBlank() || suppZipCode.isBlank()
            || suppBankInfo.isBlank())
            return SupplierCreationStatus.COMPANY_INCOMPLETE;

        int suppOkpo;
        try {
            suppOkpo = Integer.parseInt(suppOkpoStr);
        } catch (NumberFormatException e) {
            return SupplierCreationStatus.COMPANY_STR_TO_NUMBER_CONVERT_EXCEPTION;
        }

        // Обработка данных о продукте
        if (prodName.isBlank() || prodNomenclature.isBlank() || prodArticle.isBlank() || prodOkeiName.isBlank()
            || prodMassStr.isBlank() || prodNdsStr.isBlank())
            return SupplierCreationStatus.PRODUCT_INCOMPLETE;
        int prodIdOkei;
        int prodNds;
        double prodMass;
        try {
            prodNds = Integer.parseInt(prodNdsStr);
            prodMass = Double.parseDouble(prodMassStr);
            ResultSet result = SSQLController.Query("SELECT * FROM okei_code WHERE name='" + prodOkeiName + "'");
            result.next();
            prodIdOkei = result.getInt("ID_OKEI");
        } catch (SQLException | NumberFormatException e) {
            return SupplierCreationStatus.PRODUCT_STR_TO_NUMBER_CONVERT_EXCEPTION;
        }

        // Помещаем данные в БД.
        var buttonConfirm = new ButtonType("Подтвердить", ButtonBar.ButtonData.APPLY);
        var buttonCancel = new ButtonType("Отменить", ButtonBar.ButtonData.CANCEL_CLOSE);
        var result = Model.getInstance().getWindowFactory().showConfirmationAlert(
                "Регистрация компании и товара", "",
                "Вы действительно хотите зарегистрировать эту компанию и товар?\nУдостоверьтесь в правильности заполнения всех полей.\nОтменить данное действие будет невозможно.",
                Arrays.asList(buttonConfirm, buttonCancel));
        if (result.isPresent() && result.get() == buttonConfirm) {
            try {
                // Создаём продукт
                if (prodId == 0) {
                    Product product = new Product(prodId, prodName, prodDescr, prodNomenclature, prodArticle, prodIdOkei,
                            prodMass, prodNds, prodStorageConditions, false);
                    product.save();
                    prodId = product.getId();
                    UserDataModel.getInstance().setProduct(product);
                }

                // Создаём поставщика
                Supplier supplier = new Supplier(0, prodId, suppName, suppCity, suppAddressFull, suppOkpo,
                        suppPaymentAccount, suppPhoneNumber, suppZipCode, suppBankInfo);
                supplier.save();
                UserDataModel.getInstance().setSupplier(supplier);

                // Обновляем персону
                UserDataModel.getInstance().getPerson().setSupplier(supplier);
                UserDataModel.getInstance().getPerson().update();
            } catch (SQLException e) {
                return SupplierCreationStatus.SQL_EXCEPTION;
            }
            return SupplierCreationStatus.OK;
        }

        return SupplierCreationStatus.CANCELLED;
    }

    public enum SupplierCreationStatus {
        OK,
        CANCELLED,
        COMPANY_INCOMPLETE,
        COMPANY_STR_TO_NUMBER_CONVERT_EXCEPTION,
        PRODUCT_INCOMPLETE,
        PRODUCT_STR_TO_NUMBER_CONVERT_EXCEPTION,
        SQL_EXCEPTION
    }
}
