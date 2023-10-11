package trade_company.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class WindowFactory {
    protected static Stage createStage(FXMLLoader loader, String title) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.setResizable(false);
        return stage;
    }

    public void showLoginWindow() {
        FXMLLoader loader = new FXMLLoader(ViewFactory.class.getResource("/FXMLs/Login.fxml"));
        createStage(loader, "Торговая фирма").show();
    }

    public void showStorekeeperWindow() {
        FXMLLoader loader = new FXMLLoader(ViewFactory.class.getResource("/FXMLs/Storekeeper/Storekeeper.fxml"));
        createStage(loader, "Торговая фирма: кладовщик").show();
    }

    public void showAdministratorWindow() {
        FXMLLoader loader = new FXMLLoader(ViewFactory.class.getResource("/FXMLs/Administrator/Administrator.fxml"));
        createStage(loader, "Торговая фирма: администратор").show();
    }

    public void showSupplierWindow() {
        FXMLLoader loader = new FXMLLoader(ViewFactory.class.getResource("/FXMLs/Supplier/Supplier.fxml"));
        createStage(loader, "Торговая фирма: поставщик").show();
    }

    public void showAlert(Alert.AlertType type, String title, String headerText, String contentText) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.show();
    }

    public Optional<ButtonType> showConfirmationAlert(String title, String headerText, String contentText, List<ButtonType> buttons) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.getButtonTypes().setAll(buttons);
        return alert.showAndWait();
    }
}
