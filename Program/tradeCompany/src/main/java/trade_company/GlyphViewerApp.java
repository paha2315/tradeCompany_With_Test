package trade_company;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GlyphViewerApp extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/FXMLs/GlyphViewer.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("Просмотрщик глифов \"de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon\"");
        stage.setScene(scene);
        stage.show();
    }
}
