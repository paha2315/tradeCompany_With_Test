package trade_company.logic;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import trade_company.models.Model;
import trade_company.models.UserDataModel;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Arrays;

public class ProfileLogic {
    protected String getPersonName() {
        var person = UserDataModel.getInstance().getPerson();
        if (person.getThirdName().isEmpty())
            return "%s %s!".formatted(person.getSecondName(), person.getFirstName());
        return "%s %s %s!".formatted(person.getSecondName(), person.getFirstName(), person.getThirdName());
    }

    protected PasswordChangeStatus doChangePassword(String current, String new_, String new_repeated) throws NoSuchAlgorithmException, SQLException {
        var currentPasswdHash = Hasher.createSHAHash(current);
        if (!currentPasswdHash.equals(UserDataModel.getInstance().getPerson().getPassword()))
            return PasswordChangeStatus.NOT_MATCHES_MAIN;
        if (!new_.equals(new_repeated))
            return PasswordChangeStatus.NOT_MATCHES_NEW;
        var buttonConfirm = new ButtonType("Подтвердить", ButtonBar.ButtonData.APPLY);
        var buttonCancel = new ButtonType("Отменить", ButtonBar.ButtonData.CANCEL_CLOSE);
        var result = Model.getInstance().getWindowFactory().showConfirmationAlert("Смена пароля", "", "Вы уверены что хотите изменить пароль?", Arrays.asList(buttonConfirm, buttonCancel));
        if (result.isPresent() && result.get() == buttonConfirm) {
            var newPasswdHash = Hasher.createSHAHash(new_);
            UserDataModel.getInstance().getPerson().setPassword(newPasswdHash);
            UserDataModel.getInstance().getPerson().update();
            return PasswordChangeStatus.OK;
        }
        return PasswordChangeStatus.CANCELLED;
    }

    public enum PasswordChangeStatus {
        OK,
        CANCELLED,
        NOT_MATCHES_MAIN,
        NOT_MATCHES_NEW,
    }
}
