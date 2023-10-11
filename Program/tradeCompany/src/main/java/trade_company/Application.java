package trade_company;

import javafx.stage.Stage;
import trade_company.models.Model;

public class Application extends javafx.application.Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        Model.getInstance().getWindowFactory().showLoginWindow();
    }
}